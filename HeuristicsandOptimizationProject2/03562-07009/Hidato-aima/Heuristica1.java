package aima.core.environment.hidato;

import aima.core.search.framework.HeuristicFunction;

/**
 * Implementa una heuristica para el problema de los ascensores
 */
public class Heuristica1 implements HeuristicFunction{

	@Override
	public double h(Object state) {
		Estado estado = (Estado)state;
		/*System.out.println("ESTO ES LA HEURISTICA Y NO DEBERIA ENTRAR");*/
		int hvalue = 0;
		
		for(int i=0;i<estado.tablero.length;i++){
			for(int j=0;j<estado.tablero[0].length;j++){
				if(estado.tablero[i][j]==0){
					hvalue++;
				}
			}
			
		}

		/*Calcular el valor de la heuristica para el estado y almacenarlo en hvalue */
 
		return hvalue;
	}

}
