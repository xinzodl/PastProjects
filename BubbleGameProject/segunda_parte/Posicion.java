/*			PRACTICA REALIZADA POR:
 * 			Alvaro Gomez Ramos				NIA:100307009
 * 			Carlos Contreras Sanz			NIA:100303562*/

package segunda_parte;

public class Posicion {
	private int fila;
	private int columna;
																				//Constructores
	public Posicion(int f, int c){
		fila=f;
		columna=c;
	}
	public Posicion(){}//Constructor por defecto
																				//Metodos get
	public int getFila(){
		return fila;
	}
	public int getColumna(){
		return columna;
	}
																				//Metodo String toString
	public String toString(){
		return ("Fila: "+fila+", columna: "+columna);
		
	}
}


