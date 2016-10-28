package aima.core.environment.hidato;

import aima.core.search.framework.GoalTest;

/**
 * Clase que implementa la funcion que decide si las metas se cumplen en un estado dado o no
 * Puede contener atributos para saber cuales son las metas de este problema o estar incluidas en el estado.
 */
public class FuncionMetas implements GoalTest {
	
	public FuncionMetas(){
		
	}

	public boolean isGoalState(Object state) {
		Estado tablero_sol = (Estado)state;
		
		
		/*Con lo comentado no expandimos el ultimo nodo*/
		/*for (int ii=0; ii<tablero_sol.tablero.length;ii++){//si hay algun 0 es que faltan casillas pro rellenar, asi que no he llegado alameta
  			for(int jj=0; jj<tablero_sol.tablero[0].length;jj++){
  				if (tablero_sol.tablero[ii][jj]==0){return false;}
  			}
		}*/
		
		/*int i = tablero_sol.posicion[0];
		int j = tablero_sol.posicion[1];
		
		if(i>0 && tablero_sol.tablero[i-1][j] == (tablero_sol.maximo)){//Compruebo arriba
			return true;
		}
		if(i>0 && j>0 && tablero_sol.tablero[i-1][j-1]==(tablero_sol.maximo)){//compruebo arriba-izquierda
			return true;
		}
		
		if(j>0 && tablero_sol.tablero[i][j-1]==(tablero_sol.maximo)){//compruebo izquierda
			return true;
		}

		if(i<(tablero_sol.tablero.length-1) && j>0 && tablero_sol.tablero[i+1][j-1]==(tablero_sol.maximo)){//compruebo abajo-izquierda
			return true;
		}
		
		if(i<(tablero_sol.tablero.length-1) && tablero_sol.tablero[i+1][j]==(tablero_sol.maximo)){//compruebo abajo
			return true;
		}
		
		if(i<(tablero_sol.tablero.length-1) && j<(tablero_sol.tablero[0].length-1) && tablero_sol.tablero[i+1][j+1]==(tablero_sol.maximo)){//compruebo abajo-derecha
			return true;
		}
		if(j<(tablero_sol.tablero[0].length-1) && tablero_sol.tablero[i][j+1]==(tablero_sol.maximo)){//compruebo derecha
			return true;
		}
		if(i>0 && j<(tablero_sol.tablero[0].length-1) && tablero_sol.tablero[i-1][j+1]==(tablero_sol.maximo)){//compruebo arriba-derecha
			return true;
		}
		return false;*/
		
		/*de esta forma expandimos el ultimo nodo, por lo que estaremos en el estado meta*/
		if(tablero_sol.tablero[tablero_sol.posicion[0]][tablero_sol.posicion[1]]==tablero_sol.maximo){
			System.out.println(tablero_sol.toString());
			return true;
		}
		return false;
	}
}
