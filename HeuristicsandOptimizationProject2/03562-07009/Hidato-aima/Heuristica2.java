package aima.core.environment.hidato;

import aima.core.search.framework.HeuristicFunction;

/**
 * Implementa una heuristica para el problema de los ascensores
 */
public class Heuristica2 implements HeuristicFunction{

	@Override
	public double h(Object state) {
		Estado estado = (Estado)state;
		/*Heuristica que devuelve la distancia entre el nodo en el que estoy y el nodo meta */
		int i_max=0;
		int j_max=0;
		
		for (int ii=0; ii<estado.tablero.length;ii++){//si hay algun 0 es que faltan casillas pro rellenar, asi que no he llegado alameta
  			for(int jj=0; jj<estado.tablero[0].length;jj++){
  				if (estado.tablero[ii][jj]==estado.maximo){
  					i_max=ii;
  					j_max=jj;
  				}
  			}
		}
		
		if((i_max+j_max == estado.posicion[0]+estado.posicion[1]) || (Math.abs(i_max+j_max) == Math.abs(estado.posicion[0]+estado.posicion[1]))){
			return Math.abs(i_max-estado.posicion[0]);
		}else{
			return (Math.abs(i_max-estado.posicion[0])+Math.abs(j_max-estado.posicion[1]));
		}
	}

}
