
/*
 *  client.c
 *
 *  DrvSim. Version 1.2
 *
 *  INFODSO@ARCOS.INF.UC3M.ES
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

#include "lc.h"


int main ( int argc, char *argv[] )
{
    int    ret ;
    int    id_client, tag_client ;
    char   block[BLOCK_SIZE] ;
    mqd_t  q_req, q_res ;
    int    i, res;


    /* 1) check/get parameters... */
    if (argc > 2)
    {
        printf("Usage: %s [client id]\n", argv[0]) ;
        exit(-1) ;
    }

    if (2 == argc)
         tag_client = atoi(argv[1]) ;
    else tag_client = 0 ;

    id_client = getpid() ;

    sleep(1) ;

    /* 2) Client */
    ret = syscall_open(&q_req, &q_res, id_client) ;
    if (ret < 0) return -1;

    for (i=0; i<5; i++)
    {
            // Reset block to 'c' (should be overwritten by block contents)
	    memset(&block, 'c', BLOCK_SIZE); 
	    res = syscall_read(&q_req, &q_res, id_client, i + tag_client, block) ;
	    if (res < 0) {
		fprintf(stderr, "CLIENT %d - ERROR on syscall_read \n",id_client);
	    } else {
		printf("CLIENT %d reads from disk: %c ... %c\n", id_client, block[0], block[BLOCK_SIZE-1]);
	    }
	    
	    // Reset block to 'c' (should be overwritten by block contents)
	    memset(&block, 'c', BLOCK_SIZE); 
	    res = syscall_read(&q_req, &q_res, id_client, i + tag_client, block) ;
	    if (res < 0) {
		fprintf(stderr, "CLIENT %d - ERROR on syscall_read \n",id_client);
	    } else {
		printf("CLIENT %d reads from disk: %c ... %c\n", id_client, block[0], block[BLOCK_SIZE-1]);
	    }

            sleep(1) ;
    }

    printf("CLIENT %d: END\n", id_client) ;
    ret = syscall_close(&q_req, &q_res) ;
    if (ret < 0) return -1;

    /* 3) End */
    return 1 ;
}

