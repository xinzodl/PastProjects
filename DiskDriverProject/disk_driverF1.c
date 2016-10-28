/*
 *	Autores:
 *	Carlos Contreras Sanz			100303562
 *	Alejandro Garcia-Cantarero Alañon	100307006
 *	Alvaro Gomez Ramos			100307009
 * 
 */


#include "disk_driver.h"
#include "queue.h"


static struct queue* colaPeticiones;//Cola donde se encolaran las peticiones que se le realicen al driver mientras esta ocupado
int ocupado;//0 esta libre, 1 esta ocupado

int mount_driver(){
    colaPeticiones = queue_new();
    return 0;
}

int disk_driver_block_request ( pid_t p_id, int operation, int block_id, char *block, int error )
{
    int ret = 0;

    if (ocupado==1){//solo puede haber una petion a la vez, las que lleguen mientras se atiende una, se encolan para luego
	//creamos la peticion con los datos necesarios para luego hacerla al HW cuando sea posible.
	
	//reservamos memoria para la peticion que se va a encolar
	request* peticion = malloc(sizeof(request) );
	peticion->p_id = p_id;
	peticion->operation = operation; 
	peticion->block_id = block_id;
	//reservamos memoria para el bloque que nos han pasado por la peticion
	char * puntero_bloque = malloc(BLOCK_SIZE); 
	memcpy(puntero_bloque, block,BLOCK_SIZE);
	
	peticion->block = puntero_bloque;
	peticion->error = error;
	printf("DRIVER: device in use. Request enqueued - pid: %d, block_id: %d\n", p_id, block_id);
	colaPeticiones = enqueue(colaPeticiones, peticion);//encolamos la peticion que nos han pedido, ya que el dispositivo esta ocupado
    	
    	free(peticion);//liberamos la peticion una vez que se ha encolado
    }else {//dispositivo libre, se puede pedir directamente
	ocupado = 1;
	printf("DRIVER: data requested to device - pid: %d, block_id: %d\n", p_id, block_id);
        ret = request_data_to_device(p_id, operation, block_id, block, error);//Le pedimos al dispositivo directamente la peticion
        if (ret < 0){ ocupado = 0; return -1;}//si la peticion no se ha hecho bien, no esta ocupado el dispositivo
    }
    
    return 1;
}

int disk_driver_hardware_interrupt_handler ( pid_t p_id, int operation, int block_id, char *block, int error )
{
    int desencolado;
    int ret ;
    printf("DRIVER: request finished. Sending data to process - pid: %d, block_id: %d\n", p_id, block_id);
    ret = send_data_to_process(p_id, operation, block_id, block, error) ;//le enviamos el resultado de la peticion al cliente

    if (ret < 0) return -1;
    //Ahora vamos a ver si quedan peticiones por atender
    if (queue_empty (colaPeticiones)==0){//Cola de peticiones no vacia
	request* peticion = malloc(sizeof(request) );//reservamos memoria para la peticion que tenemos que obtener del desencolado
	desencolado = dequeue(colaPeticiones, peticion);//desencolamos la peticion de la cola de peticiones
	if (desencolado < 0) return -1;//Error al desencolar
	//Desencolado correctamente
	printf("DRIVER: pending requests. Data requested to device - pid: %d, block_id: %d\n", peticion->p_id, peticion->block_id);
	ret = request_data_to_device(peticion->p_id, peticion->operation, peticion->block_id, peticion->block, peticion->error);//Realizamos la peticion desencolada
	
	free(peticion);//liberamos la memoria reservada para la peticion que desencolamos 
	if (ret < 0) return -1;
    }else{//Cola de peticiones vacia
	printf("DRIVER: all requests done. Waiting for new request... \n");
	ocupado = 0;//Si la cola de peticiones esta vacia el driver ya no esta coupado
    }

    return 1;
}

int unmount_driver(){
    queue_destroy(colaPeticiones);
    return 0;
}
