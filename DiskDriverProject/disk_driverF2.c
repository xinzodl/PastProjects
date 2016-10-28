/*
*
*	Autores: Carlos Contreras Sanz 100303562
*		 Alejandro Garcia-Cantarero Alanon 100307006
*		 Alvaro Gomez Ramos 100307009
*
*/

#include "disk_driver.h"
#include "queue.h"

int ocupado;
struct queue* colaPeticiones;
struct queue* cache;

int mount_driver(){
    ocupado = 0;//0 libre 1 ocupado
    colaPeticiones = queue_new();
    cache  = queue_new();//se encola segun se piden. el que sale con dequeue es el menos usado (LRU)
    return 0;
}



int disk_driver_block_request ( pid_t p_id, int operation, int block_id, char *block, int error )
{//me llaman siempre que quieran algo del disco
	request* peticion = malloc(sizeof(request));
	char * b2 = malloc (BLOCK_SIZE);
	memcpy(b2,block,BLOCK_SIZE);//para que no me lo sobrrrescriban luego
	peticion->p_id=p_id;
	peticion->operation=operation;
	peticion->block_id=block_id;
	peticion->block=b2;
	peticion->error=error;
	int ret;
    if (operation==0 && estaEnCola(cache,peticion)==1){//si esta en cache y es de lectura la doy directamente
	//refresco en cache (quito de donde este y encolo en cache el nodo con los datos)
	refresca (cache,peticion);//los datos han quedado en datos->block
	printf("DRIVER: request block_id= %i found in cache. Sending to client pid= %i... \n",peticion->block_id, peticion->p_id);
	ret = send_data_to_process(peticion->p_id, peticion->operation, peticion->block_id, peticion->block, peticion->error);//los devuelvo
	if (ret < 0){return -1;}
	return 0;
    }else{//si no esta en cache o es escritura, caso normal+borrar si esta en cache
	if (estaEnCola(cache,peticion)==1){//si que esta, pues la borro
		borraRequest(cache,peticion);
		cache->size--;
	}
	if (ocupado==0){//puedo pedir
		ocupado=1;
		printf("DRIVER: data requested to device - pid: %i, block_id: %i\n",p_id,block_id);
		ret = request_data_to_device(p_id, operation, block_id, block, error);
		if (ret<1) {return -1;}
	}else {//me espero, y lo encolo
		printf("DRIVER: device in use. Request enqueued - pid= %d, block_id= %i\n",peticion->p_id,peticion->block_id);
		colaPeticiones=enqueue(colaPeticiones,peticion);
	}//fin else ocupado
    }//fin else si no cache
free(peticion);
    return 1;
}//FIN disk_driver_block_request




int disk_driver_hardware_interrupt_handler ( pid_t p_id, int operation, int block_id, char *block, int error )
{//cuando disco retorna valor (queda libre) debo hacer la primera peticion almacenada en la cola
	request* peticion3 = malloc(sizeof(request));
	char * b2 = malloc (BLOCK_SIZE);
	memcpy(b2,block,BLOCK_SIZE);
	peticion3->p_id=p_id;
	peticion3->operation=operation;
	peticion3->block_id=block_id;
	peticion3->block=b2;
	peticion3->error=error;    
	//hago acciones sobre cache
	if (operation==0){//lectura
		//"refreco" borrando una y metiendo la nueva
		if (estaEnCola(cache,peticion3)==1){//si que esta, pues la refresco
			refresca (cache,peticion3);
		}else {
			if (cache->size>=10){//tenemos que desencolar antes de meter el nuevo
				request* aux = malloc(sizeof(request));
				dequeue(cache,aux);//desencolamos
				cache->size--;
				printf("DRIVER: element block_id= %i discarded by LRU policy.\n",aux->block_id);
				free(aux);
			}
			cache=enqueue(cache,peticion3);//encolo nueva lectura hecha
			cache->size++;
			printf("DRIVER: element block_id= %i inserted in cache.\n", peticion3->block_id);
		}

	}else{//escritura
		if (estaEnCola(cache,peticion3)==1){//si que esta, pues la borro
			borraRequest(cache,peticion3);
			cache->size--;
			printf("DRIVER: write operation over cached element block_id= %i. Discarded.\n",peticion3->block_id);
		}
	}
free(peticion3);
	//ahora ya sigo normalmente
	int ret ;
    	printf("DRIVER: request finished. Sending data to process - pid: %i, block_id: %i\n",p_id,block_id);
    	ret = send_data_to_process(p_id, operation, block_id, block, error) ;
	if (ret < 0){
		return -1;
	}else{//el disco nos ha dejado los datos en *block de forma satisfactoria
		if (queue_empty (colaPeticiones) == 0){//tenemos que desencolar y hacer nueva peticion 
			request* peticion2 = malloc(sizeof(request));//creamos estructura
			int des = dequeue(colaPeticiones,peticion2);//desencolamos
			if (des<0) {return -1;}
			int ret2;
			printf("DRIVER: pending requests. Data requested to device - pid: %i,  block_id: %i \n",peticion2->p_id,peticion2->block_id);
			ret2 = request_data_to_device(peticion2->p_id, peticion2->operation, peticion2->block_id, peticion2->block, peticion2->error) ;
free(peticion2);	
			if (ret2 < 0) {
				return -1;
			}
		}else{//disco ya no esta ocupado
			ocupado=0;
			printf("DRIVER: all requests done. Waiting for new requests... \n");
		}
	}
    return 1;
}//FIN METODO


int unmount_driver(){
    queue_destroy(colaPeticiones);
    queue_destroy(cache);
    return 0;
}





