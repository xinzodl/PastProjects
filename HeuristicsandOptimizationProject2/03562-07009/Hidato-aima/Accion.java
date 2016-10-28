package aima.core.environment.hidato;

import aima.core.agent.impl.DynamicAction;


/**
 * La clase accion representa las acciones del dominio.
 * Pueden incluirse atributos y/o metodos auxiliares para poder 
   representar las acciones o bien utilizar el nombre de la accion.
 */
public class Accion extends DynamicAction{
	
	public enum tipoAccion {SUMAR}
	
	public String nombre;
	
	int posicion_cambia [] =new int[2];//Posicion sobre la que aplicaremos la accion
	int posicion_origen [] =new int[2];//Posicion de la que obtenemos el valor a poner en tablero[posicion_cambia] +1
	
	public tipoAccion tipo;
	/**
	 * Crea la accion a partir de su nombre
	 */
	public Accion(tipoAccion type, int fil_cambia, int col_cambia, int fil_orig, int col_orig) {
		super("Accion tipo: " +type.toString() + ", Desde Celda: ["+ Integer.toString(fil_orig) +","+Integer.toString(col_orig)+"] hasta Celda: [" + Integer.toString(fil_cambia) + ", "+ Integer.toString(col_cambia) + "]");
		this.tipo=type;
		this.posicion_cambia[0] = fil_cambia;
		this.posicion_cambia[1] = col_cambia;
		this.posicion_origen[0] = fil_orig;
		this.posicion_origen[1] = col_orig;
		this.nombre = "Accion tipo: " +type.toString() + ", Desde Celda: ["+ Integer.toString(fil_orig) +","+Integer.toString(col_orig)+"] hasta Celda: [" + Integer.toString(fil_cambia) + ", "+ Integer.toString(col_cambia) + "]";	
	}
	
	public String toString(){
		return this.nombre;
	}

}
