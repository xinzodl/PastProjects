
/*
 *  disk_hw.c
 *
 *  DrvSim. Version 1.2
 *
 *  INFODSO@ARCOS.INF.UC3M.ES
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <errno.h>
#include <unistd.h>

#include "link.h"

#define NUM_BLOCKS 100

void handle_crtl_c()
{
        char fname[1024] ;

	printf("Ctrl-C recived - exiting...\n");

        sprintf(fname, "/%d_q_diskhw_req", getuid()) ;
	link_delete(fname);

        sprintf(fname, "/%d_q_diskhw_res", getuid()) ;
	link_delete(fname);

	exit(0);
}

int main ( int argc, char *argv[] )
{
    char fname[1024] ;
    int  last_block, current_block ;
    int  sim_dsk ;
    int  ret, readed, written ;

    mqd_t q_req, q_res ;
    disk_request current_request ;
    
    // Handle Ctrl+C signal
    signal(SIGINT, handle_crtl_c);

    sprintf(fname, "/%d_q_diskhw_req", getuid()) ;
    link_delete(fname) ;
    ret = link_open(&q_req, fname,1, 0) ;
    if (ret < 0) return -1;

    sprintf(fname, "/%d_q_diskhw_res", getuid()) ;
    link_delete(fname) ;
    ret = link_open(&q_res, fname,1, 0) ;
    if (ret < 0) return -1;

    last_block = 0 ;

    ret = link_receive(&q_req, &current_request) ;

    while (1)
    {
        current_block = current_request.block_num ;

	if(current_block >= NUM_BLOCKS){
	    printf("DISK: ERROR block requested %d is greater than disk size %d\n", current_block, NUM_BLOCKS);
	    current_request.error = -1;
	    ret = link_send(&q_res, &current_request) ;
	}else{
	
	    // sleep
	    // Simulates access time. In an SSD: 0 seek time + 2s access time
	    printf("DISK: accessing to block %d\n", current_block);
	    usleep((abs(current_block - last_block))*0 + 2000000) ;
	
	    // send back data/ack
	    if (READ == current_request.operation )
	    {
		memset(&(current_request.buffer), 'x', BLOCK_SIZE) ;

		sim_dsk = open("disk.dat", O_RDWR);
		if (sim_dsk < 0) 
		{
		    printf("ERROR opening disk.dat \n");
		    return -1;
		}
		
		ret = lseek(sim_dsk, current_block*BLOCK_SIZE, SEEK_SET);

		readed = 0;
		do 
		{
		    ret = read(sim_dsk, 
			      &(current_request.buffer)+readed, 
			      BLOCK_SIZE-readed);
		    readed = readed + ret;
		} while (readed < BLOCK_SIZE && ret >= 0);

		close(sim_dsk) ;

		if (ret < 0) 
		    current_request.error = -1;
		else current_request.error =  0;

		printf("DISK: %d bytes read from block %d \n", readed, current_block);
		ret = link_send(&q_res, &current_request) ;
	    }
	    else if (WRITE == current_request.operation )
	    {
		sim_dsk = open("disk.dat", O_RDWR);
		if (sim_dsk < 0) 
		{
		    printf("ERROR opening disk.dat \n");
		    return -1;
		}

		ret = lseek(sim_dsk, current_block*BLOCK_SIZE, SEEK_SET);

		written = 0;
		do 
		{
		    ret = write(sim_dsk, 
			      &(current_request.buffer)+written, 
			      BLOCK_SIZE-written);
		    written = written + ret;
		} while (written < BLOCK_SIZE && ret >= 0);

		close(sim_dsk) ;

		if (ret < 0) 
		    current_request.error = -1;
		else current_request.error =  0;
		
		printf("DISK: %d bytes written to block %d \n", written, current_block);
		ret = link_send(&q_res, &current_request) ;
	    }
	    
	    // receive next request
	    last_block = current_block ;
	}
	ret = link_receive(&q_req, &current_request) ;
    }

    return 1;
}

