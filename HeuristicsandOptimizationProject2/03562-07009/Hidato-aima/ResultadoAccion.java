package aima.core.environment.hidato;


import aima.core.agent.Action;
import aima.core.search.framework.ResultFunction;

/**
 * Clase que implementa la funcion que devuelve el estado resultante de aplicar una accion sobre un estado.
 */
public class ResultadoAccion implements ResultFunction{

	@Override
	public Object result(Object s, Action a) {
		Estado estado = (Estado) s; //Estado sobre el que se aplica la accion
		Accion accion = (Accion) a; //Accion a aplicar sobre el estado
		
		Estado sucesor = new Estado(estado);
		
		//Apliocamos la accion al estado para crear los estados sucesores
		
		sucesor.tablero[accion.posicion_cambia[0]][accion.posicion_cambia[1]]=sucesor.tablero[sucesor.posicion[0]][sucesor.posicion[1]]+1;//sumamos +1
		sucesor.posicion[0]=accion.posicion_cambia[0];//actualizamos la posicion del estado sucesor, para seguir expandiendo nodos
		sucesor.posicion[1]=accion.posicion_cambia[1];
		return sucesor;
	}

}
