package aima.core.environment.hidato;


import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;

import java.util.LinkedHashSet;

/**
 * Clase que implementa la funcion que devuelve las acciones disponibles en un estado dado.
 */
public class AccionesDisponibles  implements ActionsFunction {
	public Set<Action> actions(Object state) {
		Estado estado = (Estado)state;
		Set<Action> acciones = new LinkedHashSet<Action>();
		
		/*Las nuevas aplicables dado un estado deben añadirse al conjunto acciones
		 creando instancias de Accion y agregandolas con acciones.add() */
		
	
		/*Sabemos el valor que tiene la casilla en la que estamos, por lo que miramos que las celdas de alrededor sean uno mas que nosotros*/
		int valor_nuestro = estado.tablero[estado.posicion[0]][estado.posicion[1]];
		
		int i = estado.posicion[0];
		int j = estado.posicion[1];
		
		if(i>0 && estado.tablero[i-1][j] == (valor_nuestro+1)){//Compruebo arriba 
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j, i, j));
			return acciones;
		}
		if(i>0 && j>0 && estado.tablero[i-1][j-1]==(valor_nuestro+1)){//compruebo arriba-izquierda
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j-1, i, j));
			return acciones;
		}
		
		if(j>0 && estado.tablero[i][j-1]==(valor_nuestro+1)){//compruebo izquierda
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i,j-1, i, j));
			return acciones;
		}

		if(i<(estado.tablero.length-1) && j>0 && estado.tablero[i+1][j-1]==(valor_nuestro+1)){//compruebo abajo-izquierda
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j-1, i, j));
			return acciones;
		}
		
		if(i<(estado.tablero.length-1) && estado.tablero[i+1][j]==(valor_nuestro+1)){//compruebo abajo
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j, i, j));
			return acciones;
		}
		
		if(i<(estado.tablero.length-1) && j<(estado.tablero[0].length-1) && estado.tablero[i+1][j+1]==(valor_nuestro+1)){//compruebo abajo-derecha
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j+1, i, j));
			return acciones;
		}
		if(j<(estado.tablero[0].length-1) && estado.tablero[i][j+1]==(valor_nuestro+1)){//compruebo derecha
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i,j+1, i, j));
			return acciones;
		}
		if(i>0 && j<(estado.tablero[0].length-1) && estado.tablero[i-1][j+1]==(valor_nuestro+1)){//compruebo arriba-derecha
			acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j+1, i, j));
			return acciones;
		}
		
		/*Si no lo tiene alrededor comprobamos donde lo metemos*/
		if(!estado.ya_en_tablero.contains(valor_nuestro+1)){
			if(i>0 && estado.tablero[i-1][j] == 0){//Compruebo arriba 
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j, i, j));
			}
			if(i>0 && j>0 && estado.tablero[i-1][j-1]==0){//compruebo arriba-izquierda
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j-1, i, j));
			}
			
			if(j>0 && estado.tablero[i][j-1]==0){//compruebo izquierda
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i,j-1, i, j));
			}

			if(i<(estado.tablero.length-1) && j>0 && estado.tablero[i+1][j-1]==0){//compruebo abajo-izquierda
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j-1, i, j));
			}
			
			if(i<(estado.tablero.length-1) && estado.tablero[i+1][j]==0){//compruebo abajo
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j, i, j));
			}
			
			if(i<(estado.tablero.length-1) && j<(estado.tablero[0].length-1) && estado.tablero[i+1][j+1]==0){//compruebo abajo-derecha
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i+1,j+1, i, j));
			}
			if(j<(estado.tablero[0].length-1) && estado.tablero[i][j+1]==0){//compruebo derecha
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i,j+1, i, j));
			}
			if(i>0 && j<(estado.tablero[0].length-1) && estado.tablero[i-1][j+1]==0){//compruebo arriba-derecha
				acciones.add(new Accion(Accion.tipoAccion.SUMAR, i-1,j+1, i, j));
			}
		}
		return acciones;
	}

}
