/*

AUTORES:
	Álvaro Gómez Ramos 100307009
	Carlos Contreras Sanz 100303592
	Sistemas Operativos, Ing. informática.
	Grupo 82 2013/2014 UC3M

*/

#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include "db_warehouse.h"
#include "concurrent.h"

/*para probar la concurrencia en este mismo archivo*/
#include <unistd.h>
#include <sys/time.h>
#include <string.h>

/*usaremos una estructura, que se inicializa y se pasa al nuevo producto creado
tiene mutex, variable de condicion y contador de lectores locales 
(para cuando se esta leyendo/escribiendo)*/

/* Variables Globales*/
pthread_mutex_t mut_global; /*mutex para operaciones globales*/
int no_lectores;/* numero de lectores actuales)*/
pthread_cond_t con_lectores;/*para cuando haya ya el maximo de lectores*/
int max_lectores;/*numero de lectores totales permitidos*/
int no_productos;/*productos activos en este momento*/
int max_prod;/*maximo de productos*/
struct local {/*estructura para controlar concurrencia de nivel de producto*/
	pthread_mutex_t mut_local;/*mutex escritor local*/
	pthread_cond_t con_lec_local;/*me paro si numero de lectores es maximo*/
	int leyendo_local;/*contador lectores locales*/
};

int concurrent_init(){
	int ret;
	pthread_mutex_init(&mut_global, NULL);
	pthread_cond_init(&con_lectores,NULL);
	no_lectores=0;
	no_productos=0;
	max_prod=16;
	max_lectores = MAX_READERS;
	ret = db_warehouse_init();
	return ret;
}

int concurrent_destroy(){
	int ret;
	pthread_mutex_destroy(&mut_global);
	pthread_cond_destroy(&con_lectores);
	ret = db_warehouse_destroy();
	return ret;
}

int concurrent_create_product(char *product_name){
	int ret;
	int size;
	struct local *str_local;

	/*controlo concurrencia global*/
	pthread_mutex_lock(&mut_global);/*esta operacion ha de ser la unica ejecutando a la vez*/
	while (no_lectores!=0 || no_productos>=max_prod){/*tengo que asegurarme de que no hay lectores (escritor local cuenta ademas como lector global)*/
		pthread_cond_wait(&con_lectores, &mut_global); 
	}

	/*operaciones protegidas, solo estas*/
	ret = db_warehouse_exists_product(product_name);	
	if (ret == 0){
		ret = db_warehouse_create_product(product_name);
		if (ret == 0){
			/*creo nueva estructura para este producto, e inicializo. ademas puntero a ella*/
			/*recordemos que estructura se usa para controlar concurrencia local*/
			str_local=(struct local *) malloc(sizeof(struct local));
			pthread_mutex_init(&(str_local->mut_local), NULL);
			pthread_cond_init(&(str_local->con_lec_local), NULL);
			str_local->leyendo_local=0;
			size = sizeof(str_local);
			/*con esta linea de abajo, esta estructura pertenece a este producto*/
			ret = db_warehouse_set_internal_data(product_name, str_local, size);
			no_productos++;/*un producto mas activo*/

		}
	}
				//printf("\n_______HE CREADO EL PRODUCTO NUMERO %i\n", no_productos);
	/*dejo de proteger el global*/
	pthread_cond_signal(&con_lectores);/*despierto a los que no pudiesen leer*/
	pthread_mutex_unlock(&mut_global);/*ya he terminado, ahora puede haber cosas ejecutando a la vez*/	

	return ret;
}

int concurrent_get_num_products(int *num_products){
	int ret;
	int num_products_aux = 0;

	/*controlo concurrencia global*/
	pthread_mutex_lock(&mut_global);/*esta operacion ha de ser la unica ejecutando a la vez*/
		while (no_lectores>=max_lectores){/*espero hasta que haya hueco de lector*/
			pthread_cond_wait(&con_lectores, &mut_global); 
		}
		no_lectores++;/*como hay hueco, añado un lector mas, y procedo a leer*/
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);

	/*Parte concurrente, no protegida (puede haber mas lectores locales/globales)*/
	ret = db_warehouse_get_num_products(&num_products_aux);
	*num_products = num_products_aux;
				//printf("\n____________EL NUMERO DE PRODUCTOS ES %i\n", num_products_aux);
	/*controlo concurrencia global*/
	pthread_mutex_lock(&mut_global);/*esta operacion ha de ser la unica ejecutando a la vez*/
		no_lectores--;/*ya he terminado, pues hay un lector menos*/
		pthread_cond_signal(&con_lectores);/*despierto los dormidos con la variable de condicion*/
	pthread_mutex_unlock(&mut_global);

	return ret;
}

int concurrent_delete_product(char *product_name){
	int ret, size;
	struct local *str_local;
	
	/*controlo concurrencia global*/
	pthread_mutex_lock(&mut_global);/*esta operacion ha de ser la unica ejecutando a la vez*/
	while (no_lectores!=0){/*tengo que asegurarme de que no hay lectores*/
		pthread_cond_wait(&con_lectores, &mut_global); 
	}
	
	/*operaciones protegidas, solo estas*/
	ret = db_warehouse_get_internal_data(product_name, (void**)&str_local, &size);
	if (ret == 0){
		pthread_mutex_destroy(&(str_local->mut_local));
		pthread_cond_destroy(&(str_local->con_lec_local));
		str_local = NULL;/*si lo dejo a null digo que no apunta a ningun sitio*/
		size = 0;
		/* Save the internal data after reset*/
		ret = db_warehouse_set_internal_data(product_name, str_local, size);
		free(str_local);
	}
	ret = db_warehouse_delete_product(product_name);
	if (ret==0){
		no_productos--;/*un producto menos activo*/
	}
	
	/*dejo de proteger el global*/
				//printf("\n_______EL NUMERO DE PRODUCTOS DESPUES DE BORRAR ES %i\n", no_productos);
	pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);/*ya he terminado, ahora puede haber cosas ejecutando a la vez*/	

	return ret;
}

int concurrent_increment_stock(char *product_name, int stock, int *updated_stock){
	int ret, size;
	struct local *str_local_temp;
	/*primero nos bloqueamos por lectores globales, y una vez dentro, por lectores locales, y tras eso operamos con el mutex local reservado y un lector global añadido*/
	int stock_aux=0;
	/*se controla concurrencia global, para poder operar sobre variables globales (numero de lectores)*/

	/*controlo concurrencia global:cuenta como lector*/
	/*pero si añadimos un lector global (no tenemos aun el local de lectores, luego cuando tengamos estructura)*/
	pthread_mutex_lock(&mut_global);
		while (no_lectores>=max_lectores){
			pthread_cond_wait(&con_lectores, &mut_global); 
		}
		no_lectores++;/*+1 lector global*/
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);
	/*he liberado el global*/


	ret = db_warehouse_get_internal_data(product_name, (void**)&str_local_temp, &size);/*obtengo datos para luego comprobar si puedo escribir en local o no*/
		/*ahora ya tenemos el mutex local, solo tenemos que comprobar que no esta siendo usado*/
		/*el mutex global ya no me lo bloquean, porque tenemos lectores a 1 al menos*/

	if (ret == 0){

		/*controlo concurrencia local*/
		pthread_mutex_lock(&(str_local_temp->mut_local));/*con esto impedire que entre hasta que no haya 0 lectores locales*/
			while (str_local_temp->leyendo_local>0){
				pthread_cond_wait(&(str_local_temp->con_lec_local),&(str_local_temp->mut_local));
			};
			/*operaciones protegidas, solo estas a nivel local*/
			ret = db_warehouse_get_stock(product_name, &stock_aux);
			stock_aux += stock;
			ret = db_warehouse_update_stock(product_name, stock_aux);
			*updated_stock = stock_aux;

		/*libero el local*/
		pthread_cond_signal(&(str_local_temp->con_lec_local));
		pthread_mutex_unlock(&(str_local_temp->mut_local));/*libero mutex de este producto*/
		
	}
	
	/*controlo concurrencia global:cuenta como lector*/
	/*resto lectores globales, resta protegida global*/
	pthread_mutex_lock(&mut_global);
		no_lectores--;
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);

	return ret;
}

int concurrent_decrement_stock(char *product_name, int stock, int *updated_stock){
	int ret, size;
	struct local *str_local_temp;
	/*primero nos bloqueamos por lectores globales, y una vez dentro, por lectores locales, y tras eso operamos con el mutex local reservado y un lector global añadido*/
	int stock_aux=0;

	/*pero, si añadimos un lector global (no tenemos aun el local de lectores, luego cuando tengamos estructura)*/
	/*controlo concurrencia global para añadir lector global*/
	pthread_mutex_lock(&mut_global);
		while (no_lectores>=max_lectores){
			pthread_cond_wait(&con_lectores, &mut_global); 
		}
		no_lectores++;/*+1 lector global*/
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);

	ret = db_warehouse_get_internal_data(product_name, (void**)&str_local_temp, &size);/*obtengo datos para luego comprobar si puedo escribir en local o no*/
		/*ahora ya tenemos el mutex local, solo tenemos que comprobar que no esta siendo usado*/
		/*el mutex global ya no me lo bloquean, porque tenemos lectores a 1 al menos*/

	if (ret == 0){
		/*controlo concurrencia local para ser el unico escritor*/
		pthread_mutex_lock(&(str_local_temp->mut_local));
			while (str_local_temp->leyendo_local>0){/*con esto impedire que entre hasta que no haya 0 lectores locales*/
				pthread_cond_wait(&(str_local_temp->con_lec_local),&(str_local_temp->mut_local));
			};
			/*operaciones protegidas, solo estas a nivel local*/
			ret = db_warehouse_get_stock(product_name, &stock_aux);
			stock_aux -= stock;
			ret = db_warehouse_update_stock(product_name, stock_aux);
			*updated_stock = stock_aux;

		/*libero el local*/
		pthread_cond_signal(&(str_local_temp->con_lec_local));
		pthread_mutex_unlock(&(str_local_temp->mut_local));/*libero mutex de este producto*/
	}

	/*resto lectores globales, resta protegida global*/
	pthread_mutex_lock(&mut_global);
		no_lectores--;
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);

	return ret;
}

int concurrent_get_stock(char *product_name, int *stock){
    int stock_aux=0;
	int ret, size;
	struct local *str_local_temp;

	/*añadimos un lector global (no tenemos aun el local, pero aun asi no necesitamos comparar)*/
	/*controlo concurrencia global para añadir lector global*/
	pthread_mutex_lock(&mut_global);
		while (no_lectores>=max_lectores){
			pthread_cond_wait(&con_lectores, &mut_global); 
		}
		no_lectores++;/*+1 lector global*/
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);
	
	ret = db_warehouse_get_internal_data(product_name, (void**)&str_local_temp, &size);/*obtengo datos para luego comprobar cuando puedo leer en local o no*/
	if (ret == 0){
		
		/*ahora ya tenemos el mutex local, solo tenemos que comprobar que no esta siendo usado*/
		/*el mutex global ya no me lo bloquean, porque tenemos lectores a 1 al menos*/
		/*no tengo que comprobar limite de escritores locales, ya que es el global el maximo*/
		
		/*protejo el local para añadir un lector local*/
		pthread_mutex_lock(&(str_local_temp->mut_local));
			str_local_temp->leyendo_local++;/*con esto impedire que entre escritor local*/
		pthread_mutex_unlock(&(str_local_temp->mut_local));

		/*no protegido*/
		ret = db_warehouse_get_stock(product_name, &stock_aux);
				//printf("_______EL STOCK DE %s AHORA ES DE %i\n", product_name, stock_aux);
		*stock = stock_aux;

		pthread_mutex_lock(&(str_local_temp->mut_local));
			str_local_temp->leyendo_local--;/*con esto impedire que entre escritor local*/
			pthread_cond_signal(&(str_local_temp->con_lec_local));
		pthread_mutex_unlock(&(str_local_temp->mut_local));
			
	}
	
	/*controlo concurrencia global para restar lector global*/
	pthread_mutex_lock(&mut_global);
		no_lectores--;
		pthread_cond_signal(&con_lectores);
	pthread_mutex_unlock(&mut_global);

    return ret;
}

