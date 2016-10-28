#include <pthread.h>
#include <memory>
#include <iostream>
#include <mutex>
#include <thread>

template<typename T>
class queue
{
public:
	/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
	struct node //Estructura del Nodo
	{
		std::shared_ptr<T> data;
		node* next; //puntero al siguiente nodo en la cola, es decir el que ha llegado despues
		node(T const& data_):data(new T(data_)){} //Constructor
	};

	
	std::mutex queue_mutex;
	node* head;//primer nodo de la cola, el que va a salir
	node* last;//ultimo nodo de la cola
public:
	void enqueue(T const& data)
	{
		/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
		node* const new_node=new node(data);	//crea nodo con datos que le pasamos
		queue_mutex.lock();			//bloqueo mutex
		if(!head){				//si cola vacia
			head=new_node;			//como no tengo nada, el nuevo es primero
//std::cout << std::this_thread::get_id() <<" Cola vacia, Meto: "<< data << std::endl;
		}else{					//si que hay cosas en la cola
			last->next=new_node;		//siguiente nodo del ultimo, sera el nuevo
//std::cout << std::this_thread::get_id() << " Cola no vacia, Meto: "<< data << std::endl;
		}
		last = new_node; 			//nuevo final es el nuevo nodo
		queue_mutex.unlock();			//liberamos mutex
	}



	std::shared_ptr<T> dequeue()
	{
		/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
		queue_mutex.lock();			//bloqueo mutex	
		node* old_head=head;			//guardo cabeza antigua
		if(!head){				//si resulta que no hay (cola vacia)
//std::cout << std::this_thread::get_id() << " Cola vacia, No puedo sacar"<< std::endl;
			queue_mutex.unlock();		//devuelvo mutex
			return std::shared_ptr<T>();	//devuelvo una cosa vacia,sin data creada(?) 
		}else{					//si que hay nodos en la cola, no vacia
			head = old_head->next;		//nueva cabeza es la siguiente a la vieja
//std::cout << std::this_thread::get_id() << " Cola no vacia, Saco: "<< *old_head->data  << std::endl;

			queue_mutex.unlock();		//libero mutex
			return old_head->data;		//devuelvo datos que he sacado
		}
	}



};
