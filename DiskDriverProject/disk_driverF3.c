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
struct queue* colaPeticiones; //cola de peticiones pendientes
struct queue* cache; // cache

int mount_driver(){
    ocupado = 0;//0 libre 1 ocupado
    colaPeticiones = queue_new();
    cache  = queue_new();//se encola segun se piden. el que sale con dequeue es el menos usado (LRU)
    return 0;
}




int disk_driver_block_request ( pid_t p_id, int operation, int block_id, char *block, int error )
{//me llaman siempre que quieran algo del disco

	request* peticion = malloc(sizeof(request)); // creamos la peticion
	peticion->p_id=p_id;
	peticion->operation=operation;
	peticion->block_id=block_id;
	char* b2 = malloc(BLOCK_SIZE);
	memcpy(b2, block, BLOCK_SIZE);
	peticion->block=b2;
	peticion->error=error;
	int ret;
    if (operation==0){//si  es de lectura 
	if (estaEnCola(cache,peticion)==1){//si el elemento está en la caché
		
	//refresco en cache (quito de donde este y encolo en cache el nodo con los datos)
		refresca (cache,peticion);//los datos han quedado en datos->block
		
		printf("DRIVER: request block_id= %i found in cache. Sending to client pid= %i... \n",peticion->block_id, peticion->p_id);
		ret = send_data_to_process(peticion->p_id, peticion->operation, peticion->block_id, peticion->block, peticion->error);//devuelve los datos al cliente
		
		if (ret < 0){return -1;}// si hay algun error lo devuelve
		
		return 0; // si ha ido todo bien devuelve 0
		
	}
	else{ //si no esta en caché 
	
		if (ocupado==0){//puedo pedir al dispositivo
			ocupado=1; //indicamos que el dispositivo esta ocupado
			printf("DRIVER: data requested to device - pid: %i, operation= %i, block_id: %i \n",p_id,operation,block_id);
			ret = request_data_to_device(p_id, operation, block_id, block, error); //pedimos datos al dispositivo
			if (ret<1) {return -1;} //si hay algun error lo devuelve
		}else {//si está ocupado, me espero encolando la petición en la cola de pendientes
			printf("DRIVER: device in use. Request enqueued - pid= %d, operation= %i, block_id= %i \n",p_id,operation,block_id);
			enqueue(colaPeticiones,peticion);
			
		}
		
	}
    }
    else{// si la petición es de escritura
	if (estaEnCola(cache,peticion)==1){//si está en la caché, la refresco y actualizo
		printf("DRIVER:write operation over cached element %i Replaced in cache.\n", peticion->block_id);
		refresca_actualiza(cache,peticion);
	}
	else{// si no esta en caché
		if (cache->size>=10){//y la caché está llena, tenemos que desencolar antes de meter el nuevo
			request* aux = malloc(sizeof(request));
			dequeue(cache,aux);//desencolamos
			cache->size--; //disminuimos el tamaño de caché
			printf("DRIVER: element block_id= %i discarded by LRU policy.\n",aux->block_id);
			free(aux);//liberamos memoria
		}
		enqueue(cache, peticion);	// cacheamos la petición	
		cache->size++; //aumentamos el tamaño de caché
		printf("DRIVER:write operation over uncached element %i Inserted in cache.\n", peticion->block_id);
		
		}
		int ret ;
    		printf("DRIVER: request %i written in cache. Sending response to client %i ... \n",block_id,p_id);
    		ret = send_data_to_process(p_id, operation, block_id, block, error) ;// enviamos los datos al proceso aunque no se haya realizado aun
		if (ret < 0){
			return -1;// si hay error los envia
		}
		if (ocupado==0){//si no esta ocupado el dispositivo puedo pedir
			ocupado=1; //notificamos que esta ocupado
			printf("DRIVER: data requested to device - pid: %i, operation= %i, block_id: %i \n",p_id,operation,block_id);
			ret = request_data_to_device(p_id, operation, block_id, block, error); //pedimos los datos
			if (ret<1) {return -1;}
		}else {//si no, me espero y encolo en cola pendientes
		printf("DRIVER: device in use. Request enqueued - pid= %d, operation= %i, block_id= %i \n",p_id,operation,block_id);
			enqueue(colaPeticiones,peticion);
			
		}
	    
	    
		
		
	}
    return 1;
}//FIN disk_driver_block_request





int disk_driver_hardware_interrupt_handler ( pid_t p_id, int operation, int block_id, char *block, int error )
{
	//creamos una petición con los datos
	request* peticion = malloc(sizeof(request));
	peticion->p_id=p_id;
	peticion->operation=operation;
	peticion->block_id=block_id;
	char* b2 = malloc(BLOCK_SIZE);
	memcpy(b2, block, BLOCK_SIZE);
	peticion->block=b2;
	peticion->error=error;    
	
	if (operation==0){//si la petición es de lectura
		
		if (estaEnCola(cache,peticion)==1){//si está en caché
			refresca(cache, peticion);//"refresco" actualizando el bloque de la caché
			printf("DRIVER: request block_id= %i found in cache. Sending to client pid= %i... \n",peticion->block_id, peticion->p_id);
		}
		else { //no esta en caché
			if (cache->size>=10){//tenemos que desencolar antes de meter el nuevo ya que la caché esta llena
				request* aux = malloc(sizeof(request));
				dequeue(cache,aux);//desencolamos
				cache->size--;
				printf("DRIVER: element block_id= %i discarded by LRU policy.\n",aux->block_id);
				free(aux);
			}
			enqueue(cache,peticion);//encolo nueva lectura hecha
			cache->size++;
			printf("DRIVER: element block_id= %i inserted in cache.\n", peticion->block_id);
		}
		printf("DRIVER: request finished. Sending data to process - pid: %i, operation: %i, block_id: %i \n",p_id,operation,block_id);
		int ret;
		ret = send_data_to_process(p_id, operation, block_id, block, error) ; //envio los datos al cliente
		if (ret < 0){
			return -1;
		}
			
	}
	else{//si es de escritura no realizamos nada, ya que la respuesta de lectura se ha enviado antes
		printf("DRIVER: data is persistent. -pid: %i, block_id: %i \n",p_id, block_id);
}

		if (queue_empty (colaPeticiones) == 0){//si quedan peticiones pendientes tenemos que desencolar y hacer nueva peticion 
			request* peticion2 = malloc(sizeof(request));//creamos estructura
			int des = dequeue(colaPeticiones,peticion2);//desencolamos
			if (des<0) return -1; 
			int ret2;
			printf("DRIVER: pending requests. Data requested to device - pid: %i, operation %i, block_id: %i \n",peticion2->p_id,peticion2->operation,peticion2->block_id);
			ret2 = request_data_to_device(peticion2->p_id, peticion2->operation, peticion2->block_id, peticion2->block, peticion2->error) ; //pedimos al dispositivo los datos
			if (ret2 < 0) {
				
				return -1;
			}

		}else{//si no quedan, significa que el disco ya no esta ocupado
			ocupado=0;
			printf("DRIVER: all requests done. Waiting for new requests... \n");
		}
	
    
    return 1;
}//FIN METODO



int unmount_driver(){
    return 0;
}








