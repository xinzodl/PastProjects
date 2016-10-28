/*			PRACTICA REALIZADA POR:
 * 			Alvaro Gomez Ramos				NIA:100307009
 * 			Carlos Contreras Sanz			NIA:100303562*/

package segunda_parte;

/** Clase Tablero 
 *  Representa el tablero de juego 
 *  @author Juan Gomez Romero 
 *  COMPLETAR ESTA CLASE PARA QUE FUNCIONE */

import java.util.ArrayList;
import java.util.Stack;


/*OJO: NO SABEMOS PORQUE PERO ECLIPSE NOS ESCRIBE SOLO:
 * import puzzleBobble.Posicion;
 * Y ESO HACE QUE DE ERROR EL PROGRAMA, AL BORRARLO FUNCIONA*/


public class Tablero {
	public enum Celda {RED, GREEN, BLUE, PINK, ORANGE, GRAY, YELLOW, EMPTY};/** Tipos de celda */	
	public final static int FILAS = 16;	/** Numero de filas (coordenada Y) */
	public final static int COLUMNAS = 11;	/** Numero de columnas (coordenada X) -- Debe ser un número impar! */
																				//Empezamos a meter metodos private
	private Celda[][]t;	//Array que representa el tablero
	private Celda siguienteBurbuja;	//Almacena la burbuja que será disparada
	private int direccion;	//Almacena un entero que representa la dirección del próximo disparo
	private int filaCañon;	//Fila en la que está situada en cañón
	private int colCañon;	//Columna en la que está situada en cañón
	
/*NIVEL*/ public static int nivel=1;//nivel inicial es 1
/*NIVEL*/ public static int disparos=0;//Disparos que te ha costado pasar el nivel
																				//Constructores
	public Tablero(){
/*N*/	t=generarTablero ();//modificado para añadir nivel
/*N*/	generarBurbuja();//modificado para añadir nivel
		direccion=getDireccion();
		filaCañon=FILAS-1;
		colCañon=COLUMNAS/2;
	}
	/*N*/public Celda[][] generarTablero (){//metodo con el que generamos un tablero y lo rellenamos
		Celda[][]t=new Celda[FILAS][COLUMNAS];
		for(int ii=0;ii<t.length;ii++){			//bucles que utilizamos para dejar el tablero limpio
			for(int ee=0;ee<t[0].length;ee++){
				t[ii][ee]=Celda.EMPTY;
			}//fin for ee
		}//fin for ii	
		
		
															//numero de filas que rellenamos
															//si el nivel es el 1 o 2, 4 filas llenas, si es >=3, +1 por cada nivel extra
		int dificultad =(t.length)/3;//lleno estas filas inicialmente
		switch (nivel){//para rellenar mas o menos filas
			case 3: dificultad=(t.length)/3+1;break;
			case 4: dificultad=(t.length)/3+2;break;
		}

															//rellenamos aleatoriamente las <dificultad> filas superiores del tablero
		for(int ii=0;ii<dificultad;ii++){
			for(int ee=0;ee<t[ii].length;ee++){
															//en cada nivel se añade un color de burbuja, hasta el maximo de 6 colores+el espacio
				int aux=(int)(Math.random()*(nivel+4));
				switch(aux){
					case 0: t[ii][ee]=Celda.EMPTY;break;
					case 1: t[ii][ee]=Celda.RED;break;
					case 2: t[ii][ee]=Celda.GREEN;break;
					case 3: t[ii][ee]=Celda.BLUE;break;
					case 4: t[ii][ee]=Celda.ORANGE;break;
					case 5: t[ii][ee]=Celda.YELLOW;break;
					case 6: t[ii][ee]=Celda.GRAY;break;
					case 7: t[ii][ee]=Celda.PINK;break;
				}//fin del switch
			}//fin del for ee-columnas
		}//fin del for ii-filas
		
		//borrarBurbujasNoSujetas ();//como hay huecos al generar el tablero es posible que haya algunas burbujas no sueltas cuando se crea.
		
		return t;
		
	}//fin de generarTablero
	
	/** Devuelve la celda de la posición especificada 
	 *  @param fila Fila de la celda
	 *  @param col Columna de la celda 
	 *  @returns Celda de la posición (fila, col) *
	 *  @deprecated A ser implementado por los alumnos */
	public Celda getCelda(int fila, int col) {
		return t[fila][col];
	}
	
	/** Fija la celda en la posición especificada 
	 *  @param fila Fila de la celda
	 *  @param col Columna de la celda 
	 *  @param celda Celda a fijar 
	 *  @deprecated A ser implementado por los alumnos */
	public void setCelda(int fila, int col, Celda celda) {
		if(fila<FILAS && fila>=0){
			if(col<COLUMNAS && col>=0){
				t[fila][col]=celda;
			}
		}
	}
	
	/** Obtiene la siguiente burbuja 
	 *  @returns Devuelve el tipo de la siguiente burbuja a ser disparada 
	 *  @deprecated A ser implementado por los alumnos */
	public Celda getSiguienteBurbuja() {
		return siguienteBurbuja;
	}
	
	/** Obtiene la dirección de disparo 
	 *  @returns Dirección de disparo en el rango admitido según la dimensión del tablero 
	 *  @deprecated A ser implementado por los alumnos*/
	public int getDireccion() {
		return direccion;
	}

	public int moverDerecha(){
		if (direccion<(t[0].length)/2){
			direccion++;
		}
		return direccion;
	}
	
	public int moverIzquierda(){
		if (direccion>(t[0].length)/-2){
			direccion--;
		}
		return direccion;
	}
	
	/*N*/public void generarBurbuja() {
							//depende del nivel se generaran mas o menos colores
		int colores=(int)((Math.random())*(nivel+3));
		switch(colores){
			case 0: siguienteBurbuja=Celda.RED;break;
			case 1: siguienteBurbuja=Celda.GREEN;break;
			case 2: siguienteBurbuja=Celda.BLUE;break;
			case 3: siguienteBurbuja=Celda.ORANGE;break;
			case 4: siguienteBurbuja=Celda.YELLOW;break;
			case 5: siguienteBurbuja=Celda.GRAY;break;
			case 6: siguienteBurbuja=Celda.PINK;break;
		}//fin switch
		
	}
	
	public void borrarBurbujasNoSujetas (){
		for (int ii=1;ii<t.length;ii++){
			for (int ee=0;ee<t[ii].length;ee++){
				if (ee==0){
					if(t[(ii)-1][ee]==Celda.EMPTY && t[(ii)-1][(ee)+1]==Celda.EMPTY){
						t[ii][ee]=Celda.EMPTY;
					}//cierro if
				}//cierro if ee==0
				else if(ee==(t[0].length)-1){
					if(t[(ii)-1][ee]==Celda.EMPTY && t[(ii)-1][(ee)-1]==Celda.EMPTY){
						t[ii][ee]=Celda.EMPTY;
					}//cierro if
				}//cierro if ee==ultima
				else{
					if(t[(ii)-1][ee]==Celda.EMPTY && t[(ii)-1][(ee)-1]==Celda.EMPTY && t[(ii)-1][(ee)+1]==Celda.EMPTY){
						t[ii][ee]=Celda.EMPTY;
					}//cierro if
				}//cierro ELSE ee!=0
			}//cierro for ee columnas
		}//cierro for ii filas
	}//cierro metodo borrarBurbujasNoSujetas
	
	public boolean existenBurbujas (){//Metodo que comprueba si existen o no burbujas, si existen devuelve un true y si no un false
		boolean hayBurbujas=false;
		for (int ii=0 ; ii<t.length && hayBurbujas==false ;ii++){
			for (int ee=0;ee<t[ii].length && hayBurbujas==false ;ee++){
				if (t[ii][ee]!=Celda.EMPTY){
					hayBurbujas=true;
				}//fin del if de hay burbujas ahi
			}//fin del for ee
		}//fin dee for ii
		return hayBurbujas;
	}//fin del metodo  existenBurbujas
	
	public boolean ab_iz (Posicion pos){
		boolean valido=true;
		if (pos.getFila()!=(t.length-1) && pos.getColumna()!=0 ){//si no estoy en la ultima fila y en la primera columna
		if (t[pos.getFila()][(pos.getColumna())-1]!=Celda.EMPTY){//miro a la izquierda
			if (t[(pos.getFila())+1][pos.getColumna()]!=Celda.EMPTY){//miro abajo
				valido=false;
			}
		}
		}
		return valido;
	}
	
	public boolean ab_de (Posicion pos){
		boolean valido=true;
		if (pos.getFila()!=(t.length-1) && pos.getColumna()!=(t[0].length-1) ){//si no estoy en la ultima fila y en la ultima columna
				if (t[pos.getFila()][(pos.getColumna())+1]!=Celda.EMPTY){//miro a la derecha
					if (t[(pos.getFila())+1][pos.getColumna()]!=Celda.EMPTY){//miro abajo
						valido=false;
					}
				}
		}
		return valido;
	}
	
	public ArrayList<Posicion> obtenerTrayectoria(){
		Posicion posin;
		ArrayList<Posicion> trayectoria =new ArrayList();
		//el disparo se detendra si se llega a una casilla ya ocupada <&& b[a.fila][a.columna]==' '>
		//int a=posin.getFila();																										//CASO DE DISPARO == 0
		if (direccion==0){	
			for (int fila = filaCañon, columna = colCañon; fila>=0 && t[fila][columna]==Celda.EMPTY ; fila--){//disparo recto hacia arriba
				posin=new Posicion(fila, columna);
				trayectoria.add(posin);
			}//fin for
		}//fin if disparo=0
																												//CASO DE DISPARO > 0
		else if (direccion>0){//en el if hacemos el caso de disparo > 0
			boolean sigue=true;
			int fila = filaCañon, columna = (colCañon)+direccion;
			while(fila>=0 && t[fila][columna]==Celda.EMPTY && sigue){
				for (;fila>=0 && columna<((t[0].length)-1) && columna>=0 && t[fila][columna]==Celda.EMPTY && sigue;fila--,columna++){
					posin=new Posicion(fila, columna);
					if(columna==0){
						if (ab_de(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
				}//fin for
				for(;fila>=0 && columna<=(t[0].length) && columna>=1 && t[fila][columna]==Celda.EMPTY && sigue;fila--,columna--){
					posin=new Posicion(fila, columna);
//					if (ab_de(posin)==true){
//						trayectoria.add(posin);
//					}
//					else {
//						sigue=false;
//					}
					if (columna<(t[0].length-1)){
						if (ab_de(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(posin)==true){
						trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
				}//fin for
			}//fin while	
		}//fin if
		else{																		//CASO DE DISPARO < 0
			boolean sigue=true;
			int fila = filaCañon, columna = (colCañon)+direccion;
			while(fila>=0 && t[fila][columna]==Celda.EMPTY && sigue){
				for (;fila>=0 && columna<=(t[0].length) && columna>=1 && t[fila][columna]==Celda.EMPTY && sigue;fila--,columna--){
					posin=new Posicion(fila, columna);
					if (columna<(t[0].length-1)){
						if (ab_de(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(posin)==true){
						trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
				}//fin del for
				for(;fila>=0 && columna<((t[0].length)-1) && columna>=0 && t[fila][columna]==Celda.EMPTY && sigue;fila--,columna++){
					posin=new Posicion(fila, columna);
					if(columna==0){
						if (ab_de(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(posin)==true){
							trayectoria.add(posin);
						}
						else {
							sigue=false;
						}
					}
				}//fin del for
			}//fin del while	
		}//fin del else	
		return trayectoria;
	}//fin del metodo
	
	/** Borra los grupos de burbujas conectadas del tablero a partir de la posicion pasada 
	 *  pasada por parámetro. NO MODIFICAR ESTE MÉTODO.
	 *  Implementación del algoritmo de relleno por difusión. 
	 *  @param p Posición de comienzo para borrar grupos de burbujas conectadas*/
	public void borrarBurbujasAgrupadas(Posicion p) {
		int i= p.getFila(), j= p.getColumna();
		neighbors(i, j, getCelda(i, j));							
	}
	
	//--------------------------------------------------------------------
	// DO NOT MODIFY
	/** Support method for internal use */
	private void neighbors(int i_comienzo, int j_comienzo, Celda destino) {		
		Stack<Integer> ic = new Stack<Integer>();
		Stack<Integer> jc = new Stack<Integer>();
		
		ic.add(i_comienzo);
		jc.add(j_comienzo);
		
		int t = 0;
		boolean [][] visitados = new boolean[FILAS][COLUMNAS];
		
		while( !ic.isEmpty() ) {
			int i = ic.pop();
			int j = jc.pop();
			visitados[i][j] = true;
						
			t++;						

			if(j-1 >= 0) {
				if(getCelda(i, j-1) == destino && !visitados[i][j-1]) {
					ic.push(i);
					jc.push(j-1);
				}
			}
			if(j+1 < COLUMNAS) {
				if(getCelda(i, j+1) == destino && !visitados[i][j+1]) {
					ic.push(i);
					jc.push(j+1);
				}
			}
			if(i-1 >=0) {
				if(getCelda(i-1, j) == destino && !visitados[i-1][j]) {
					ic.push(i-1);
					jc.push(j);
				}
			}
			if(i+1 < FILAS) {
				if(getCelda(i+1, j) == destino && !visitados[i+1][j]) {
					ic.push(i+1);
					jc.push(j);
				}
			}
			if(j-1 >=0 && i-1 >=0) {
				if(getCelda(i-1, j-1) == destino && !visitados[i-1][j-1]) {
					ic.push(i-1);
					jc.push(j-1);
				}
			}
			if(j+1 < COLUMNAS && i-1 >= 0 ) {
				if(getCelda(i-1, j+1) == destino && !visitados[i-1][j+1]) {
					ic.push(i-1);
					jc.push(j+1);
				}
			}
			if(j-1>=0 && i+1<FILAS) {
				if(getCelda(i+1, j-1) == destino && !visitados[i+1][j-1]) {
					ic.push(i+1);
					jc.push(j-1);
				}
			}
			if(j+1<COLUMNAS && i+1<FILAS) {
				if(getCelda(i+1, j+1) == destino && !visitados[i+1][j+1]) {
					ic.push(i+1);
					jc.push(j+1);
				}
			}

		}	
		
		if(t >= 3)
			for(int i=0; i<visitados.length; i++)
				for(int j=0; j<visitados[i].length; j++)
					if(visitados[i][j])
						setCelda(i, j, Celda.EMPTY);
	}
	
	
}


