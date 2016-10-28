#include "atomic_stack.cpp"
#include <vector>
#include <thread>
#include <pthread.h>
#include <memory>
#include <iostream>


stack <int>pila;

void push(int aux, int mete){
	for (int i = 0; i < aux; ++i, mete++) {
		pila.push(mete);
	//std::cout << "PUSH: " << mete <<std::endl;
	}
}
void pop(int aux){
	for (int i = 0; i < aux; ++i) {
		pila.pop();
	}
}


int main(int argc, char* argv[]){
	if (argc < 3) {
		std::cout << "Usage:" << std::endl;
		std::cout << "   " << argv[0] << " <n_threads> <tasks_per_thread>" << std::endl;
		return 0;
	}
	int n_threads = atoi( argv[1]);
	int tasks_per_thread = atoi(argv[2]);


	auto start = std::chrono::system_clock::now();	//Tomamos el tiempo inicial
	
	/*		CODIGO			*/

	/*Creacion de Hilos*/
	std::vector<std::thread> threads;
	int mete=0;
	for (int i = 0; i < n_threads; ++i) {
		if (i%2==0){
			threads.push_back(std::thread(push,tasks_per_thread,mete));
			mete=mete+tasks_per_thread;
		}else{
			threads.push_back(std::thread(pop, tasks_per_thread));
		}
	}//FIN for creaion hilos

	/*Esperamos a que todos los hilos terminen*/
	for (int i = 0; i < n_threads; ++i) {
		threads[i].join();
	}



	/*		FIN DE CODIGO		*/
	
	auto end = std::chrono::system_clock::now();	//Tomamos el tiempo final
	auto duration = std::chrono::duration_cast<std::chrono::microseconds> (end - start);	//Restamos los tiempos final-inicial para obtener en ms el tiempo
	std::cout << "Tiempo empleado: " <<duration.count() << std::endl;/*si divido entre 1M tengo segundos*/



	return 0;
}//FIN DE MAIN
