/*			PRACTICA REALIZADA POR:
 * 			Alvaro Gomez Ramos				NIA:100307009
 * 			Carlos Contreras Sanz			NIA:100303562*/

/** Implementación del juego
 *  @author Juan Gomez Romero
 * 
 *  COMPLETAR ESTA CLASE PARA QUE FUNCIONE */

package segunda_parte;

import java.util.ArrayList;

public class MiBubblesGUI extends BubblesGUI {

	//--------------------------------------------------------------------
	/* Implementación de la clase: MODIFICAR */

	/** Mapeo de teclas */
	private static final char SALIR = 'q';	
	private static final char IZQUIERDA = 'a';
	private static final char DERECHA = 'd';
	private static final char DISPARAR = 's';
	Tablero tablero;
	
	/** Constructor */
	public MiBubblesGUI() {// Crear y pintar el objeto tablero
		tablero=new Tablero();
/*N*/	tablero.borrarBurbujasNoSujetas();
		draw(tablero);
	}

	//--------------------------------------------------------------------
	/** Procesar las teclas pulsadas en la ventana de juego 
	 *  Implementación actual:
	 *  	q:          finaliza el juego
	 *  	Otras teclas: Actualiza el movimiento 
	 *  @param      Tecla pulsada 
	 *  @deprecated A ser implementado por los alumnos */
	@Override
	public void processKey(char tecla) {
		char t = Character.toLowerCase(tecla);
		ArrayList<Posicion> L;//obtiene la lista de posiciones por las que pasa la burbuja al dispararse
		switch (t) {
			case SALIR:            
				showMessage("El programa terminará");/* Cierra la ventana, fin del programa */
				terminate();
				break;
			case DERECHA:
				tablero.moverDerecha();
				draw(tablero);
				break;
			case IZQUIERDA:
				tablero.moverIzquierda();
				draw(tablero);break;
			case DISPARAR:
/*N*/			Tablero.disparos++;//he disparado una vez, añado uno al contador (un contador de disparos, nada mas)				
				L=tablero.obtenerTrayectoria();
				if(L.size()==0){//Has perdidio
					showMessage("Has perdido");
					terminate();
				}
				else{
					tablero.setCelda(L.get(L.size()-1).getFila(),L.get(L.size()-1).getColumna(), tablero.getSiguienteBurbuja());
					tablero.borrarBurbujasAgrupadas(L.get(L.size()-1));
					tablero.borrarBurbujasNoSujetas();
/*N*/				if(tablero.existenBurbujas()==false && Tablero.nivel<4){
/*N*/	
/*N*/					showMessage("¡¡Enhorabuena Has Pasado el Nivel " + Tablero.nivel + " !!");
/*N*/					showMessage("Te ha costado " + Tablero.disparos + " disparos.");
/*N*/					Tablero.disparos=0;//reseteo el contador de disparos para el nuevo nivel.
//						terminate();
/*N*/					Tablero.nivel++;
/*N*/					tablero=new Tablero();
/*N*/					tablero.borrarBurbujasNoSujetas();
/*N*/					draw(tablero);
					}//fin del if
/*N*/				else if(tablero.existenBurbujas()==false && Tablero.nivel==4){//nivel 5 ya no existe, se termina el juego
						showMessage("¡¡Enhorabuena Has Ganado el Juego!!");
						terminate();
					}//fin del if
					else{
						tablero.generarBurbuja();
						draw(tablero);
					}//fin del else
				}//fin del else
				break;
		}//fin del switch
	}
}




