
/*
 *  pkernel.c
 *
 *  DrvSim. Version 1.2
 *
 *  INFODSO@ARCOS.INF.UC3M.ES
 *
 */

#include <pthread.h>

#include "disk_driver.h"


/*
 * Global variables
 */
mqd_t q_req, q_res ;
mqd_t q_creq;

int disk_processing = 0; // Disk is currently processing a request


void handle_crtl_c()
{
	char fname[256];

	printf("Ctrl-C recived - exiting...\n");

	sprintf(fname, "/%d_q_client_req", getuid()) ;
	link_delete(fname);
	
	unmount_driver();

	exit(0);
}

/*
 * High level API
 */
int send_data_to_process ( pid_t p_id, int operation, int block_id, char *block, int error )
{
        disk_request current_request ;
	mqd_t q_cres;
        int ret ;
	char q_name[256];
	
	sprintf(q_name, "/%d_%d", getuid(), p_id) ;
	ret = link_open(&q_cres, q_name, 10, 0) ;
	if (ret < 0) return -1;

        current_request.process_requester = p_id ;
        current_request.operation = operation ;
        current_request.block_num = block_id ;
	current_request.error = error;
        memcpy(current_request.buffer,block,BLOCK_SIZE) ;

        ret = link_send(&q_cres, &current_request) ;
        if (ret < 0) return -1;
	
	ret = link_close(&q_cres) ;
        if (ret < 0) return -1;

        return 1 ;
}

int request_data_to_device   ( pid_t p_id, int operation, int block_id, char *block, int error )
{
        disk_request current_request ;
        int ret ;
	
	if(disk_processing){
		fprintf(stderr,"PKERNEL ERROR - Skipped request from %d. Disk was in use. \n",p_id);
		current_request.error = -1;

		// Send error response to client
		mqd_t q_cres; char q_name[256];
		sprintf(q_name, "/%d_%d", getuid(), p_id) ;
		ret = link_open(&q_cres, q_name, 10, 0); if (ret < 0) return -1;
		ret = link_send(&q_cres, &current_request) ; if (ret < 0) return -1;
		ret = link_close(&q_cres) ; if (ret < 0) return -1;

		return -1;
	}else{
		disk_processing = 1;
	}

        current_request.process_requester = p_id ;
        current_request.operation = operation ;
        current_request.block_num = block_id ;
	current_request.error = error;
        memcpy(current_request.buffer,block,BLOCK_SIZE) ;

        ret = link_send(&q_req, &current_request) ;
        if (ret < 0) return -1;

        return 1;
}


/*
 * Intermediate handlers
 */
void disk_driver_hwint     ( union sigval sv )
{
    struct sigevent not;
    disk_request current_request ;
    int ret ;

    // notification are one-shot mechanism...
    not.sigev_notify = SIGEV_THREAD;
    not.sigev_notify_function   = disk_driver_hwint ; // call disk_driver_hardware_interrupt
    not.sigev_notify_attributes = NULL;
    not.sigev_value.sival_ptr   = &q_res;

    ret = mq_notify(q_res, &not) ;
    if (ret < 0) {
        perror("mq_notify: ");
        return;
    }

    while (1)
    {
	    // receive request...
	    ret = link_receive(&q_res, &current_request) ;
	    if (ret < 0) return ;
	    
	    disk_processing = 0;

	   /* if (-1 == current_request.error)
            {
		// Send error response to client
		mqd_t q_cres; char q_name[256];
		sprintf(q_name, "/%d_%d", getuid(), current_request.process_requester) ;
		ret = link_open(&q_cres, q_name, 10, 0); if (ret < 0) return ;
		ret = link_send(&q_cres, &current_request) ; if (ret < 0) return ;
		ret = link_close(&q_cres) ; if (ret < 0) return ;
		  
	    }else{*/
	    
		// process request...
		disk_driver_hardware_interrupt_handler(current_request.process_requester,
							    current_request.operation,
							    current_request.block_num,
							    current_request.buffer,
							    current_request.error ) ;
							    
	    //}
    }
}

void disk_driver_breq      ( union sigval sv )
{
    struct sigevent not;
    int client_id ;
    disk_request current_request ;
    int ret ;

    client_id = (int)sv.sival_int ;

    // notification are one-shot mechanism...
    not.sigev_notify            = SIGEV_THREAD;
    not.sigev_notify_function   = disk_driver_breq; // call disk_driver_block_request
    not.sigev_notify_attributes = NULL;
    not.sigev_value.sival_int   = client_id;

    ret = mq_notify(q_creq, &not) ;
    if (ret < 0) {
        perror("mq_notify: ");
        return;
    }

    while (1)
    {
	    // receive request...
	    ret = link_receive(&q_creq, &current_request) ;
	    if (ret < 0) return ;

	    // process request...
	    disk_driver_block_request(current_request.process_requester,
					    current_request.operation,
					    current_request.block_num,
					    current_request.buffer,
					    current_request.error ) ;
    }
}


/*
 * Main (loop)
 */
int main ( int argc, char *argv[] )
{
    int ret ;

    char  q_name[1024] ;
    struct sigevent not;
    
    // Handle Ctrl+C signal
    signal(SIGINT, handle_crtl_c);
    
    if (mount_driver() < 0){
	fprintf(stderr, "ERROR MOUNTING DEVICE \n");
	exit(-1);
    }


    /* 1) check/get parameters... */
    if (argc < 1 || argc > 1)
    {
        printf("Usage: %s \n", argv[0]) ;
        exit(-1) ;
    }

    /* 2) connect with the disk_hw */
    sprintf(q_name, "/%d_q_diskhw_req", getuid()) ;
    ret = link_open(&q_req, q_name, 1, 0) ;
    if (ret < 0) return -1;

    sprintf(q_name, "/%d_q_diskhw_res", getuid()) ;
    ret = link_open(&q_res, q_name, 1, O_NONBLOCK) ;
    if (ret < 0) return -1;

    not.sigev_notify = SIGEV_THREAD;
    not.sigev_notify_function   = disk_driver_hwint ; // call disk_driver_hardware_interrupt
    not.sigev_notify_attributes = NULL;
    not.sigev_value.sival_ptr   = &q_res;

    ret = mq_notify(q_res, &not) ;
    if (ret < 0)
    {
        perror("mq_notify: ");
        return -1;
    }

    /* 3) connect with the clients */
    // Open connection for client requests
    sprintf(q_name, "/%d_q_client_req", getuid()) ;
    link_delete(q_name);
    ret = link_open(&q_creq, q_name, 10, O_NONBLOCK) ;
    if (ret < 0) return -1;

    not.sigev_notify            = SIGEV_THREAD;
    not.sigev_notify_function   = disk_driver_breq; // call disk_driver_block_request
    not.sigev_notify_attributes = NULL;
    not.sigev_value.sival_int   = 1;

    ret = mq_notify(q_creq, &not) ;
    if (ret < 0)
    {
	perror("mq_notify: ");
	return -1;
    }

    /* 4) keep service... */
    while (1) {
       pause();
    }

    return 1;
}

