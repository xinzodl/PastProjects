/*
*
*	Autores: Carlos Contreras Sanz 100303562
*		 Alejandro Garcia-Cantarero Alanon 100307006
*		 Alvaro Gomez Ramos 100307009
*
*/


#include  <stdio.h>
#include  <stdlib.h>
#include  <string.h>

#include "queue.h"


/* 
 * Enqueues a requests in the queue given by parameter.
 * Returns a pointer to the new queue.
 * 
 * NOTE: the element is enqueued by value (the functions makes a copy o the element)
 * not by reference.
 * 
 */
struct queue* enqueue(struct queue* s, request * req)
{
  struct queue_element* p = malloc(sizeof(struct queue_element) );

  if( NULL == p )
    {
      fprintf(stderr, "IN %s, %s: malloc() failed\n", __FILE__, "list_add");
      return s; 
    }
  memcpy(&(p->req), req, sizeof(request));
  p->next = NULL;
  //Double link
  p->prev = NULL;
  if( NULL == s )
    {
      printf("Queue not initialized\n");
      free(p);
      return s;
    }
  else if( NULL == s->head && NULL == s->tail )
    {
      /* printf("Empty list, adding p->data: %d\n\n", p->data);  */
      s->head = s->tail = p;
      return s;
    }
  else if( NULL == s->head || NULL == s->tail )
    {
      fprintf(stderr, "There is something seriously wrong with your assignment of head/tail to the list\n");
      free(p);
      return NULL;
    }
  else
    {
      
      /* printf("List not empty, adding element to tail\n"); */
      s->tail->next = p;
      // Double link
      p->prev = s->tail;
      s->tail = p;
    }
  return s;
}


/* 
 * Remove the first element and returns it into the pointer given by parameter.
 * 
 * Returns 0 or -1 in case of error.
 * 
 * NOTE: the given pointer must have alloc'd memory. The functions makes a copy
 * of the element into the given pointer.
 */
int dequeue( struct queue* s, request * req )
{
  struct queue_element* h = NULL;
  struct queue_element* p = NULL;

  if( NULL == s )
    {
      //printf("List is empty\n");
      return -1;
    }
  else if( NULL == s->head && NULL == s->tail )
    {
      //printf("Well, List is empty\n");
      return -1;
    }
  else if( NULL == s->head || NULL == s->tail )
    {
      printf("There is something seriously wrong with your list\n");
      printf("One of the head/tail is empty while other is not \n");
      return -1;
    }
  h = s->head;
  p = h->next;

  memcpy(req, &(h->req), sizeof(request));
  free(h);
  s->head = p;
  if( NULL == s->head ){
      s->tail = s->head;  /* The element tail was pointing to is free(), so we need an update */
  }else{
      // Double link
      s->head->prev = NULL;
  }
  return 0;
}

/*
 * Checks if the queue is empty.
 * Returns 1 it is empty and 0 in other case.
 */
int queue_empty ( struct queue* s ) { return (s->head == NULL); }

/*
 * Creates a new queue.
 * 
 * Returns the pointer to the new created queue.
 */
struct queue* queue_new(void)
{
	struct queue* p = malloc(sizeof(struct queue));
	if( NULL == p )
		fprintf(stderr, "LINE: %d, malloc() failed\n", __LINE__);
	p->head = p->tail = NULL;
	p->size=0;
	return p;
}

/*
 * Destroy the queue.
 * Dequeues every element and frees the structure.
 */
void queue_destroy(struct queue *q){
	request req;
	int res = dequeue(q,&req); 
	
	while(res == 0){
		res = dequeue(q,&req);
	}
	free(q);
}


/*
*
*______________________________________________________________________________________________
*					NUEVO
*______________________________________________________________________________________________
*
*
*/
void refresca_actualiza (struct queue* s, request * refrescaEste){//quita de donde este, y pone la nueva peticion actualizada
	if (estaEnCola(s,refrescaEste)==0){//si no esta en la cola, no devuelve nada
		return;
	}
	queue_element* encolaEste = malloc(sizeof(queue_element)); //crea una cola auxiliar para moverse por ella
	encolaEste = s->head;
	while(encolaEste != NULL && encolaEste->req.block_id != refrescaEste->block_id){//mientras no lo encuentre va recorriendo la cola
		encolaEste=encolaEste->next;
	}
	borraElemento(s,encolaEste);//borro de la posicion que estuviese
	enqueue(s,refrescaEste);//encolo el nuevo, ya que es el actualizado
	
}
int estaEnCola(struct queue* s, request * req){//0 no esta, 1 si esta
//id del bloque es suficiente para saber si esta almacenado, no?
	if (queue_empty(s)==1){// si esta vacia, pues casi que no puede estar en la cola
		return 0;
	}
	struct queue_element* h = s->head;
	while (h!=NULL){
		if (req->block_id==h->req.block_id){
			return 1;
		}else{
			h=h->next;
		}		
	}
	return 0;
}



void borraElemento (struct queue* q, struct queue_element* aux){
	if (queue_empty(q)==1 || estaEnCola(q,&aux->req)==0){//si no hay nada en la cola o no esta el elemento, pues no puedo borrar nada
		return;
	}
	//recorro cola en busca de datos con este block_id
	queue_element* este = malloc(sizeof(queue_element));
	este = q->head;
	while(este != NULL && este->req.block_id != aux->req.block_id){//cuando lo encuentro, salgo
		este=este->next;
	}
	aux=este;//borro el nodo de la cola que tiene el bloque buscado
	if(aux->req.block_id == q->head->req.block_id || aux->req.block_id == q->tail->req.block_id){//si lo que busco es cabeza o cola
		if(aux->req.block_id == q->head->req.block_id && aux->req.block_id==q->tail->req.block_id){
			q->head = q->tail = NULL;
		}
		else if(aux->req.block_id==q->head->req.block_id){
			q->head = aux->next;
			q->head->prev = NULL;
		}
		else if(aux->req.block_id==q->tail->req.block_id){
			q->tail = aux->prev;
			q->tail->next = NULL;
		}
	}else{
	// It is an element in the middle of the queue
		aux->prev->next = aux->next;
		aux->next->prev = aux->prev;
		aux->next = NULL;
		aux->prev = NULL;
	}
//	q->size--;
	free(aux);
}



void refresca (struct queue* s, request * refrescaEste){//quita de donde este, y pone el ultimo (mas recientemente usado)
	if (estaEnCola(s,refrescaEste)==0){//cosa mala, porque no esta en la cola
		return;
	}
	queue_element* encolaEste = malloc(sizeof(queue_element));
	encolaEste = s->head;
	while(encolaEste != NULL && encolaEste->req.block_id != refrescaEste->block_id){//cuando lo encuentro, salgo
		encolaEste=encolaEste->next;
	}
	borraElemento(s,encolaEste);//borro de la posicion que estuviese
	enqueue(s,&encolaEste->req);//encolo request, que lleva los datos en realidad
	refrescaEste->block=encolaEste->req.block;//copio datos al que me pasan por parametros

}


void borraRequest (struct queue* q, request* aux){
	if (queue_empty(q)==1 || estaEnCola(q,aux)==0){//si no hay nada en la cola o no esta el elemento, pues no puedo borrar nada
		return;
	}
	//recorro cola en busca de datos con este block_id
	queue_element* este = malloc(sizeof(queue_element));
	este = q->head;
	while(este != NULL && este->req.block_id != aux->block_id){//cuando lo encuentro, salgo
		este=este->next;
	}
	if(este->req.block_id == q->head->req.block_id || este->req.block_id == q->tail->req.block_id){//si lo que busco es cabeza o cola
		//borro el nodo de la cola que tiene el bloque buscado
		if(este->req.block_id == q->head->req.block_id && este->req.block_id==q->tail->req.block_id){
			q->head = q->tail = NULL;
		}else if(este->req.block_id==q->head->req.block_id){
			q->head = este->next;
			q->head->prev = NULL;
		}else if(este->req.block_id==q->tail->req.block_id){
			q->tail = este->prev;
			q->tail->next = NULL;
		}
	}else{
	// It is an element in the middle of the queue
		este->prev->next = este->next;
		este->next->prev = este->prev;
		este->next = NULL;
		este->prev = NULL;
	}
	free(este);
//	q->size--;
}


