package aima.core.environment.hidato;

import java.util.ArrayList;
import java.util.Arrays;



public class Estado {
	
	int tablero[][];//LAS CASILLAS SIN NUMERO TIENE QUE SER 0 y las # son -1!!!!
	int maximo;	//Maximo del tablero
	
	ArrayList<Integer> ya_en_tablero = new ArrayList<Integer>();	//aqui tendremos todos los numeros que tenemos colocados en el tablero
	
	int [] posicion=new int[2];	//Posicion sobre la que veremos que acciones aplicar
	
	
    /** Constructor de Estado para el estado inicial.  Se construye a partir de
      * estructuas que contengan la informacion de los ficheros de entrada 
      */
	public Estado(int inicial[][], int max, int pos_fila, int pos_columna){
		this.tablero = new int[inicial.length][inicial[0].length];
		this.maximo = max;
		this.posicion[0]=pos_fila;
		this.posicion[1]=pos_columna;
		
		for(int i=0;i<inicial.length;i++){
			for(int j=0;j<inicial[0].length;j++){
				this.tablero[i][j]=inicial[i][j];//LAS CASILLAS SIN NUMERO TIENE QUE SER 0 !!!! las de # son -1
				if(inicial[i][j]>0){
					this.ya_en_tablero.add(inicial[i][j]);
				}
			}
		}
	}


	/**
	 * Constructor de copia de un Estado.
	 * El nuevo estado debe ser una copia del anterior.
	 * 
	 * @param otro estado que debe copiarse
	 */
	public Estado(Estado otro) {
		this.tablero = new int[otro.tablero.length][otro.tablero[0].length];
		this.maximo = otro.maximo;
		this.posicion[0]=otro.posicion[0];
		this.posicion[1]=otro.posicion[1];
		for(int i=0;i<otro.tablero.length;i++){
			for(int j=0;j<otro.tablero[0].length;j++){
				this.tablero[i][j]=otro.tablero[i][j];
				if(otro.tablero[i][j]>0){
					this.ya_en_tablero.add(otro.tablero[i][j]);
				}
			}
		}
	}

	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maximo;
		result = prime * result + Arrays.hashCode(posicion);
		result = prime * result + Arrays.hashCode(tablero);
		result = prime * result
				+ ((ya_en_tablero == null) ? 0 : ya_en_tablero.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		if (maximo != other.maximo)
			return false;
		if (!Arrays.equals(posicion, other.posicion))
			return false;
		if (!Arrays.deepEquals(tablero, other.tablero))
			return false;
		if (ya_en_tablero == null) {
			if (other.ya_en_tablero != null)
				return false;
		} else if (!ya_en_tablero.equals(other.ya_en_tablero))
			return false;
		return true;
	}


	public String toString() {
		System.out.println("entra");
		String devuelve="";
		for (int ii=0; ii<tablero.length;ii++){
  			for(int jj=0; jj<tablero[0].length;jj++){
  				devuelve= devuelve + tablero[ii][jj] + "\t" ;
  			}
  			devuelve = devuelve + "\n";
		}
		return devuelve= devuelve+ "Estado [tablero=" + ", maximo="
				+ maximo;
	}
	
    /*Implementar hashcode(), equals() para el manejo de nodos repetidos */ 

	
    /*Implementar toString para cuando sea necesario ver la informacion del Estado*/
       
        
}
