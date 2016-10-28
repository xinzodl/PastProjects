/*
*
*	Autores: Carlos Contreras Sanz 100303562
*		 Alejandro Garcia-Cantarero Alanon 100307006
*		 Alvaro Gomez Ramos 100307009
*
*/

#ifndef _QUEUE_H_
#define _QUEUE_H_

#include  <stdio.h>
#include  <stdlib.h>
#include  <string.h>
#include  "link.h"


typedef struct request 
{
	/* TO-DO DESIGN YOUR OWN REQUEST STRUCTURE */
  pid_t p_id;
  int operation;
  int block_id;
  char *block;
  int error;
} request;

typedef struct queue_element
{
  request req;
  struct queue_element* next;
  struct queue_element* prev;
}queue_element;


struct queue
{
  struct queue_element* head;
  struct queue_element* tail;
  int size;
};

/* Enqueue an element */
struct queue* enqueue(struct queue* s, request * req);
/* Dequeue an element */
int dequeue( struct queue* s, request * req );
/* Return 1 if the queue is empty and 0 otherwise*/
int queue_empty ( struct queue* s );
/* Create an empty queue */
struct queue* queue_new(void);
void queue_destroy(struct queue *q);
/*NUEVOS*/
int estaEnCola(struct queue* s, request * req);//0 no esta, 1 si esta
void borraElemento (struct queue* q, struct queue_element* aux);
void borraRequest (struct queue* q, request* aux);
void refresca (struct queue* s, request * req);//quita de donde este, y pone el ultimo (mas recientemente usado)
void refresca_actualiza (struct queue* s, request * refrescaEste);//quita de donde este, y pone la nueva petición actualizada 
#endif


