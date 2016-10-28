
#ifndef _LC_H
#define _LC_H

	#include <stdio.h>
	#include <stdlib.h>
	#include <signal.h>
	#include <unistd.h>

	#include "link.h"

        int syscall_open  ( mqd_t *q_req, mqd_t *q_res, int client_id ) ;
        int syscall_write ( mqd_t *q_req, mqd_t *q_res, int client_id, int block_id, char *block ) ;
        int syscall_read  ( mqd_t *q_req, mqd_t *q_res, int client_id, int block_id, char *block ) ;
        int syscall_close ( mqd_t *q_req, mqd_t *q_res ) ;

#endif  // _LC_H


