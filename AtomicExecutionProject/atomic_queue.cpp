#include <atomic>
#include <pthread.h>
#include <memory>
#include <iostream>

/*
		DUDAAAAAAAAAAAAAAAAAAAAAAAAA
		al decir que el previo final apunta al nuevo:
			aux2->next = new_node;
		eso tiene que ser atomico de alguna forma?
*/

template<typename T>
class queue
{
private:
	/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
	struct node//tenemos estos datos en el nodo
	{
		std::shared_ptr<T> data;//datos????
		node* next;//puntero a siguiente nodo
		node(T const& data_):data(new T(data_)){} //constructor????
	};

	std::atomic<node*> head;//puntero a lo primero de la cola, se ira actualizando
	std::atomic<node*> last;//puntero a lo ultimo de la cola, se ira actualizando
public:
	void enqueue(T const& data)
	{
		/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
		node* const new_node=new node(data); 
		node* aux = head.load(); 
		node* aux2 = last.load(); 
		while(!head){						//si entro primero, soy cabeza		
			head.compare_exchange_weak(aux,new_node);			
		}
		while(!last.compare_exchange_weak(aux2,new_node));	//me pongo al final cuando pueda
		if (aux2){
			aux2->next = new_node; 				//el previo final debe apuntar a mi
		}
	}

	std::shared_ptr<T> dequeue()
	{
		/* DESARROLLE EL CODIGO A PARTIR DE ESTE PUNTO */
		node* old_head=head.load();
		while(old_head && !head.compare_exchange_weak(old_head,old_head->next));
		return old_head ? old_head->data : std::shared_ptr<T>();
	}
};




















