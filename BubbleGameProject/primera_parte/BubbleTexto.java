/*			PRACTICA REALIZADA POR:
 * 			Alvaro Gomez Ramos				NIA:100307009
 * 			Carlos Contreras Sanz			NIA:100303562*/

package primera_parte;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class BubbleTexto {
										//constantes de tipo de como esta la casilla
	static final char RED='r';
	static final char GREEN='g';
	static final char BLUE='b';
	static final char ORANGE='o';
	static final char YELLOW='y';
	static final char GRAY='a';
	static final char PURPLE='p';
	static final char EMPTY=' ';
										//constantes de tipo de accion que podemos hacer
	static final char IZQUIERDA='A';
	static final char DERECHA='D';
	static final char DISPARO='S';
	static final char SALIR='Q';
	

	
	
	public static char [][] generarTablero (int niv){//metodo con el que generamos un tablero de 14x9 y lo rellenamos
		char[][]tablero=new char[14][9];
		for(int ii=0;ii<tablero.length;ii++){			//bucles que utilizamos para dejar el tablero limpio, lo llenamos de EMPTY
			for(int ee=0;ee<tablero[ii].length;ee++){
				tablero[ii][ee]=EMPTY;
			}//fin for ee
		}//fin for ii	
		

														//numero de filas que rellenamos
				//si el nivel es el 1 o 2, 4 filas llenas, si es >=3, +1 por cada nivel extra
		int dificultad =(tablero.length)/3;//lleno estas filas
		switch (niv){
			case 3: dificultad=dificultad+1;break;
			case 4: dificultad=dificultad+2;break;
			case 5: dificultad=dificultad+3;break;
		}

													//rellenamos aleatoriamente las filas superiores del tablero que correspondan al nivel (dificultad)
		for(int ii=0;ii<dificultad;ii++){
			for(int ee=0;ee<tablero[ii].length;ee++){
							//en cada nivel se añade un color de burbuja, hasta el maximo de 7 colores+el espacio
				int aux=(int)(Math.random()*(niv+4));
				switch(aux){
				case 0: tablero[ii][ee]=EMPTY;break;
				case 1: tablero[ii][ee]=RED;break;
				case 2: tablero[ii][ee]=GREEN;break;
				case 3: tablero[ii][ee]=BLUE;break;
				case 4: tablero[ii][ee]=ORANGE;break;
				case 5: tablero[ii][ee]=YELLOW;break;
				case 6: tablero[ii][ee]=GRAY;break;
				case 7: tablero[ii][ee]=PURPLE;break;
				}//fin del switch
			}//fin del for ee-columnas
		}//fin del for ii-filas
		
		borrarBurbujasNoSujetas (tablero);//ya que el tablero generado tiene huecos, puede ser que haya burbujas sueltas al crearlo
	
		return tablero;
	}//fin de generarTablero
	
	public static void imprimirTablero (char [][] b, int dir,char burbuja,ArrayList<Posicion> arr, int a, int tot){//imprimimos el tablero, la direccion de disparo, la ayuda para apuntar, cual es la proxima burbuja y los disparos que llevamos 
										//ayuda para apuntar
		int longitud = arr.size();//Nos hace que la trayectoria que nos da el array list se marque con '.'
		for(int ii=0;ii<(longitud);ii++){
			b[arr.get(ii).fila][arr.get(ii).columna]='.';
		}//fin for ii
		
		//recibimos el tablero, la direccion de disparo y el color de la proxima burbuja
										//imprimimos tablero y el marco del tablero
		System.out.println("+-------------------+");
		for (int ii=0;ii<b.length;ii++){
			for (int ee=0;ee<b[ii].length;ee++){
				if (ee== (b[ii].length-1) ){
					System.out.println(b[ii][ee]+" |");
				}//fin del if ultima columna
				else if(ee==0){
					System.out.print("| "+b[ii][ee]+" ");
				}//fin del else if primera columna
				else {
					System.out.print(b[ii][ee] + " ");
				}//fin del else, en el medio
			}//fin del for ee
		}//fin dee for ii
		System.out.println("+-------------------+");
		System.out.println("-4-3-2-1  0  1 2 3 4 ");
									//imprimimos la direccion de disparo
		System.out.println("La trayectoria del disparo es: " + dir);
									//imprimimos el color de la burbuja que vamos a disparar
		System.out.println("La siguiente burbuja es de color: " + burbuja);
		System.out.println("Llevas "+a+" disparos realizados en este nivel, y " + tot + " en total.");
		
		limpiarTrayectoria(b);//Nos limpia la trayectoria del disparo (los '.') del tablero
		
	}//fin metodo imprimirTablero
	
	public static void limpiarTrayectoria(char[][]b){//Nos limpia la trayectoria del disparo (los '.') del tablero
		for(int ii=0;ii<b.length;ii++){
			for(int ee=0;ee<b[ii].length;ee++){
				if(b[ii][ee]=='.'){
					b[ii][ee]=EMPTY;
				}//fin del if si hay punto
			}//fin for ee columnas
		}//fin del for ii filas
	}//fin del metodo limpiarTrayectoria
	
	public static char obtenerAcciónJugador(){  //pide introducir la accion: realizar el disparo, moverse o salir.
									//leemos lo que nos da el usuario por teclado
		Scanner lector=new Scanner(System.in);
		System.out.println("Introduzca su proxima accion");
		String accion=( lector.next() ).toUpperCase();//para que sea posible con mayusculas y minusculas
		char ac= accion.charAt(0);
		boolean comprobar=true; 			//Se crea para repetir el bucle y pedir la accion si esta mal introducida
		while(comprobar){//se repite mientras no nos introduzcan una accion(tecla) valida
			if(ac!=IZQUIERDA && ac!=DISPARO && ac!=DERECHA && ac!=SALIR){//comprobamos que si es diferente de la acciones que podemos realizar, se lo volvemos a pedir
				System.out.println("Accion no valida, introduzca una valida");
				accion=( lector.next() ).toUpperCase();//para que sea posible con mayusculas y minusculas
				ac= accion.charAt(0);
			}//fin del if de accion incorrecta
			else{
				comprobar=false;            //hace que se deje de ejecutar el bucle cuando tenemos una accion valida
			}//fin del else accioin correcta
		}//fin del while de comprobar 
		//lector.close(); //da error si no se comenta, no sabemos porque
		return ac;
	}//fin del metodo obtener accion jugador
	
	public static int moverDerecha(int current, char[][]b){//comprobamos si es posible disparar con direccion +1 de la actual, es decir si nos podemos mover a la derecha
		//current direccion de disparo actual y char[][] es el tablero
		if (current<(b[0].length)/2){		//comprobamos que el int que recibimos no esta en la ultima columna para mover a la derecha
			current++;
		}
		return current;//devolvemos la direccion de disparo
	}

	public static int moverIzquierda(int current, char[][]b){//comprobamos si es posible disparar con direccion -1 de la actual, es decir si nos podemos mover a la izquierda
		//current direccion de disparo actual y char[][] es el tablero
		if (current>(b[0].length)/-2){		//comprobamos que el int que recibimos no esta en la primera columna para mover a la izquierda
			current--;
		}
		return current;//devolvemos la direccion de disparo
	}

	public static char generarBurbuja(int niv){//generamos la proxima burbuja a disparar. el numero de colores posibles depende del nivel.
		if (niv>4){//para que si hay mas niveles, genere todas las brubujas sin importar si estamos en el 5 o en 40
			niv=4;
		}
		//depende del nivel se generaran mas o menos colores
		int col=(int)((Math.random())*(niv+3));
		char color=EMPTY;
		switch(col){
		case 0: color=RED;break;
		case 1: color=GREEN;break;
		case 2: color=BLUE;break;
		case 3: color=ORANGE;break;
		case 4: color=YELLOW;break;
		case 5: color=GRAY;break;
		case 6: color=PURPLE;break;
		}//fin switch	
		return color;//devolvemos el color de la proxima burbuja
	}//fin metodo generarBurbuja

	public static boolean ab_iz (Posicion pos, char [][] tab){//compruebo que no cruzo digonal \
		boolean valido=true;
		if (pos.fila!=(tab.length-1) && pos.columna!=0 ){//si no estoy en la ultima fila y en la primera columna
			if (tab[pos.fila][pos.columna-1]!=EMPTY){//miro a la izquierda
				if (tab[pos.fila+1][pos.columna]!=EMPTY){//miro abajo
					valido=false;
				}
			}
		}
		
		
		
		return valido;
	}
	
	public static boolean ab_de (Posicion pos, char [][] tab){//compruebo que no cruzo digonal /
		boolean valido=true;
		
		if (pos.fila!=(tab.length-1) && pos.columna!=(tab[0].length-1) ){//si no estoy en la ultima fila y en la ultima columna
				if (tab[pos.fila][pos.columna+1]!=EMPTY){//miro a la derecha
					if (tab[pos.fila+1][pos.columna]!=EMPTY){//miro abajo
						valido=false;
					}
				}
		}
		return valido;
	}
		
	public static ArrayList<Posicion> obtenerTrayectoria(char [][] b, Posicion a, int shootDirection){//obtenemos lista de posiciones pro las que pasa el disparo
		ArrayList<Posicion> trayectoria =new ArrayList();
		
														//CASO DE DISPARO == 0
		if (shootDirection==0){	
			for (; a.fila>=0 && b[a.fila][a.columna]==EMPTY ; a.fila--){//disparo recto hacia arriba
				Posicion w=new Posicion();
				w.fila=a.fila;
				w.columna=a.columna;
				trayectoria.add(w);
			}//fin for
		}//fin if disparo=0
														//CASO DE DISPARO > 0
		if (shootDirection>0){//en el if hacemos el caso de disparo > 0
			boolean sigue=true;
			a.columna=a.columna+shootDirection;
			while(a.fila>=0 && b[a.fila][a.columna]==EMPTY && sigue ){
				//se añaden posiciones a la lista mientras las condiciones de parada no se den
				for (;a.fila>=0 && a.columna<(b[0].length-1) && a.columna>=0 && b[a.fila][a.columna]==EMPTY && sigue ;a.fila--,a.columna++){
					//si se cruza diagonal, no se añade y se para
					if(a.columna==0){
						if (ab_de(a,b)==true){	
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(a,b)==true){
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else{
							sigue=false;
						}
					}
					
									
				}//fin for
				for(;a.fila>=0 && a.columna<=(b[0].length) && a.columna>=1 && b[a.fila][a.columna]==EMPTY && sigue ;a.fila--,a.columna--){
					//si se cruza diagonal, no se añade y se para
					if (a.columna<(b[0].length-1)){
						if (ab_de(a,b)==true){	
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(a,b)==true){
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else{
							sigue=false;
						}
					}
					
				}//fin for
			}//fin while	
		}//fin if
		else{			
														//CASO DE DISPARO < 0
			boolean sigue=true;
			a.columna=a.columna+shootDirection;
			while(a.fila>=0 && b[a.fila][a.columna]==EMPTY && sigue){
				//se añaden posiciones a la lista mientras las condiciones de parada no se den
				for (;a.fila>=0 && a.columna<=(b[0].length) && a.columna>=1 && b[a.fila][a.columna]==EMPTY  && sigue ;a.fila--,a.columna--){
					//si se cruza diagonal, no se añade y se para
					if (a.columna<(b[0].length-1)){
						if (ab_de(a,b)==true){	
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(a,b)==true){
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else{
							sigue=false;
						}
					}
					
				}//fin del for
				for(;a.fila>=0 && a.columna<(b[0].length-1) && a.columna>=0 && b[a.fila][a.columna]==EMPTY && sigue;a.fila--,a.columna++){
					//si se cruza diagonal, no se añade y se para
					if(a.columna==0){
						if (ab_de(a,b)==true){	
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else {
							sigue=false;
						}
					}
					else {
						if (ab_iz(a,b)==true){
							Posicion w=new Posicion();
							w.fila=a.fila;
							w.columna=a.columna;
							trayectoria.add(w);
						}
						else{
							sigue=false;
						}
					}
					
				}//fin del for
			}//fin del while	
		}//fin del else	
		return trayectoria;
	}//fin del metodo

	public static void borrarBurbujasNoSujetas (char [][] b){//borramos las burujas que no esten sujetas
		//empezamos a comprobar la segunda fila, ya que la primera esta sujeta. si una posicion esta suelta pasa a ser EMPTY. al terminar una fila miramos la de abajo
		for (int ii=1;ii<b.length;ii++){
			for (int ee=0;ee<b[ii].length;ee++){
				if (ee==0){
					if(b[(ii)-1][ee]==EMPTY && b[(ii)-1][(ee)+1]==EMPTY){
						b[ii][ee]=EMPTY;
					}//cierro if
				}//cierro if ee==0
				else if(ee==(b[0].length)-1){
					if(b[(ii)-1][ee]==EMPTY && b[(ii)-1][(ee)-1]==EMPTY){
						b[ii][ee]=EMPTY;
					}//cierro if
				}//cierro if ee==ultima
				else{
					if(b[(ii)-1][ee]==EMPTY && b[(ii)-1][(ee)-1]==EMPTY && b[(ii)-1][(ee)+1]==EMPTY){
						b[ii][ee]=EMPTY;
					}//cierro if
				}//cierro ELSE ee!=0
			}//cierro for ee columnas
		}//cierro for ii filas
	}//cierro metodo borrarBurbujasNoSujetas

	public static boolean existenBurbujas (char [][] b){//Metodo que comprueba si existen o no burbujas, si existen devuelve un true y si no un false
		boolean hayBurbujas=false;//por defecto suponemos que no hay burbujas
		for (int ii=0 ; ii<b.length && hayBurbujas==false ;ii++){
			for (int ee=0;ee<b[ii].length && hayBurbujas==false ;ee++){
				if (b[ii][ee]!=EMPTY){
					hayBurbujas=true;//si hay alguna, suficiente para saber que aun no hemos ganado
				}//fin del if de hay burbujas ahi
			}//fin del for ee
		}//fin dee for ii
		return hayBurbujas;
	}//fin del metodo  existenBurbujas
	
	public static void borrarBurbujasAgrupadas(char [][] b, Posicion p) {
		if(b[p.fila][p.columna] != EMPTY)
			vecinos(b, p.fila, p.columna, b[p.fila][p.columna]);					
	}

	private static void vecinos(char [][] b, int i_comienzo, int j_comienzo, char destino) {		
		Stack<Integer> ic = new Stack<Integer>();
		Stack<Integer> jc = new Stack<Integer>();
		ic.add(i_comienzo);
		jc.add(j_comienzo);
		int t = 0;
		boolean [][] visitados = new boolean[b.length][b[0].length];
		while( !ic.isEmpty() ) {
			int i = ic.pop();
			int j = jc.pop();
			visitados[i][j] = true;
			t++;						
			if(j-1 >= 0) {
				if(b[i][j-1] == destino && !visitados[i][j-1]) {
					ic.push(i);
					jc.push(j-1);
				}
			}
			if(j+1 < b[0].length) {
				if(b[i][j+1] == destino && !visitados[i][j+1]) {
					ic.push(i);
					jc.push(j+1);
				}
			}
			if(i-1 >=0) {
				if(b[i-1][j] == destino && !visitados[i-1][j]) {
					ic.push(i-1);
					jc.push(j);
				}
			}
			if(i+1 < b.length) {
				if(b[i+1][j] == destino && !visitados[i+1][j]) {
					ic.push(i+1);
					jc.push(j);
				}
			}

			if(j-1 >=0 && i-1 >=0) {
				if(b[i-1][j-1] == destino && !visitados[i-1][j-1]) {
					ic.push(i-1);
					jc.push(j-1);
				}
			}
			if(j+1 < b[0].length && i-1 >= 0 ) {
				if(b[i-1][j+1] == destino && !visitados[i-1][j+1]) {
					ic.push(i-1);
					jc.push(j+1);
				}
			}
			if(j-1>=0 && i+1<b.length) {
				if(b[i+1][j-1] == destino && !visitados[i+1][j-1]) {
					ic.push(i+1);
					jc.push(j-1);
				}
			}
			if(j+1<b[0].length && i+1<b.length) {
				if(b[i+1][j+1] == destino && !visitados[i+1][j+1]) {
					ic.push(i+1);
					jc.push(j+1);
				}
			}
		}	
		if(t >= 3)
			for(int i=0; i<visitados.length; i++)
				for(int j=0; j<visitados[i].length; j++)
					if(visitados[i][j])
						b[i][j] = EMPTY;
	}
	
	public static void imprimirArrayList (ArrayList<Posicion> arr){//imprimimos el array list
		int largo = arr.size();
		for(int ii=0;ii<(largo);ii++){
			if(ii==0){
				System.out.println("La burbuja pasa por las posiciones:");
			}
			System.out.println("( "+arr.get(ii).fila+" , "+arr.get(ii).columna+" )");
		}//fin for ii
	}//fin imprimirArrayList

	public static void contarBurbujas(char [][] b){//contamos cuantas burbujas quedan en el tablero
		int contador =0;
		for(int ii=0;ii<b.length;ii++){
			for(int ee=0;ee<b[ii].length;ee++){
				if(b[ii][ee]!=EMPTY){
					contador++;
				}
			}
		}
		System.out.println( "Hay " + contador + " burbujas en el tablero.");//en vez de devolverlo, lo imprimimos directamente
	}
	
	public static boolean ultimaFilaOcupada(char [][] b){//comprueba si hay alguna brubuja en la ultima fila (la de mas abajo) del tablero.
		boolean ocupada=false;
		
		for (int ii=0; ii<(b[0].length-1);ii++){//miro cada posicion(ii, columna) de la ultima fila (b.length-1) si esta ocupada
			if (b[b.length-1][ii]!=EMPTY){
				ocupada=true;//si hay alguna ocupada no se podra bajar el tablero en el nivel 5, y perderemos
			}
		}
		
		return ocupada;
	}
	
	public static void bajarTablero(char [][] b){//desplazo el tablero una fila hacia abajo y relleno la fila de arriba de burbujas aleatoriamente
		//desplazo todo hacia abajo una fila
		for (int ii=(b.length-2); ii>=0; ii--){//filas
			for (int ee=0;ee<b[0].length; ee++){//columnas
				b[ii+1][ee]=b[ii][ee];
			}
		}
		
		//genero burbujas aleatoriamente para la fila de arriba del todo
		
		for (int ii=0; ii<(b[0].length-1);ii++){//recorro las columnas
			b[0][ii]=generarBurbuja(4);//para que genere de todos los colores
		}
		
}

	public static void jugar(){
		
		int nivel = 1;//nivel en el que empezamos el juego.
		boolean hasPerdido=false;//una de las condiciones de parada del nivel/juego
		int disparosTotales = 0;//contador para disparos en todos los niveles
		System.out.println("Instrucciones:"+'\n'+"Pulsa A para mover el cañon a la izquierda."+'\n'+"Pulsa D para mover el cañon a la derecha."+'\n'+"Pulsa S para disparar la burbuja."+'\n'+"Pulsa Q para salir del juego.");
		int contadorTiradas = 0;//contador para disparos en este nivel
/*EMPIEZA EL BUCLE DE LOS NIVELES*/
		for (;nivel<6 && hasPerdido==false ;nivel++){//parara al pasarnos el nivel 5 o al perder o al salir
		
			System.out.println("\n" + "___________________________________________");
			System.out.println("__________________NIVEL " + nivel +"__________________" + "\n"+ "\n");
	
			if (nivel==5){//si estamos en el ultimo nivel nos advierte:
				System.out.println("En este nivel, cada 25 tiradas, se aumenta la dificultad. \nSuerte \n");
			}
			
			int disparos=0;//reseteamos los disparos que llevamos este nivel
			boolean fin=true;//para acabar el bucle while,y terminar el nivel/juego
			int direc=0;//direccion inicial 0 al principio de cada nivel
			char[][]tablero=generarTablero(nivel);//generamos tablero en funcion del nivel en el que estemos
			Posicion posIn =new Posicion();//posicion inicial
			posIn.fila=(tablero.length-1);
			posIn.columna=(tablero[0].length/2);
			char csb=generarBurbuja(nivel);//burbuja inicial de disparo, numero de colores en funcion del nivel
			contarBurbujas(tablero);// nos dice cuantas burbujas hay en el tablero

			while(fin){//se repetira hasta que salgamos o hasta que no haya burbujas en el tablero
															//reiniciamos la posicion de disparo, porque si no se quedaria la ultima del arraylist
				posIn.fila=(tablero.length-1);
				posIn.columna=(tablero[0].length/2);
				ArrayList<Posicion> L=obtenerTrayectoria(tablero, posIn, direc);//obtiene la lista de posiciones por las que pasa la burbuja al dispararse
				imprimirTablero(tablero,direc,csb,L,disparos,disparosTotales);//imprimimos...
				char action=obtenerAcciónJugador();//obtenemos la accion del jugador
				if(action==DERECHA){//si es a la derecha variamos la direccion (en caso de ser posible) uno a la derecha
					direc=moverDerecha(direc,tablero);
				}
				else if(action==IZQUIERDA){//si es a la izquierda variamos la direccion (en caso de ser posible) uno a la izquierda
					direc=moverIzquierda(direc,tablero);
				}
				else if (action==SALIR){//si decidimos salir finalizamos el for de niveles y el while de este nivel.
					fin=false;
					hasPerdido=true;	
					System.out.println("Has decidido salir del juego.");
					System.out.println("¡Hasta otra!");
				}
				else if(action==DISPARO){//si disparamos....
					imprimirArrayList(L);//imprimo la lista de posiciones por las que pasa
					System.out.println("\n" + "\n");
					disparos++;
					disparosTotales++;
					
					if(L.size()==0){//si la burbuja que he disparado no ha recorrido ninguna posicion pierdo
						System.out.println("Fin del Juego, Has Perdido");
						fin=false;//para el while de este nivel
						hasPerdido=true;//para el for de los niveles
					}//SI se da el caso de que el ArrayList esta vacio, ya no puedes disparar mas.
					else {//si SI que he hecho un disparo valido y no he perdido...
						Posicion destino = new Posicion();						
						destino.fila=L.get(L.size()-1).fila;
						destino.columna=L.get(L.size()-1).columna;
						tablero[destino.fila][destino.columna]=csb;//con esto actualizamos el tablero, colocamos en la ultima posicion del arraylist la burbuja que he disparado
						borrarBurbujasAgrupadas(tablero,destino);//limpiamos el tablero
						borrarBurbujasNoSujetas(tablero);//limpiamos el tablero
						contarBurbujas(tablero);// nos dice cuantas burbujas hay en el tablero	
					}
					
					if(nivel==5 && L.size()!=0){//si estamos en el nivel 5...(lo de bajar el tablero cada 25 tiradas)
						//si el arraylist no esta vacio (podria ir dentro del else de arriba, pero bueno...)
						contadorTiradas++;//añadimos unas tirada mas
	
						if (contadorTiradas==23){
							System.out.println("\n ATENCION: \n En 2 tiradas el juego se complicara \n \n");
						}
						else if (contadorTiradas==24){
							System.out.println("\n ATENCION: \n En la proxima tirada se desplazara hacia abajo el tablero y se añadira una fila de brubujas.  \n \n");
						}
						else if (contadorTiradas==25){ //si ya estamos en la tirada 25....
							contadorTiradas=0;//reseteamos el contador
							System.out.println("\n ATENCION: \n Se ha añadido una fila de burbujas.  \n \n");
							if (ultimaFilaOcupada(tablero)==true){//si no se puede bajar el tablero, se pierde
								System.out.println("Has perdido, no se puede desplazar el tablero hacia abajo");
								fin=false;
								hasPerdido=true;
							}
							if (ultimaFilaOcupada(tablero)!=true){//si se puede bajar el tablero se baja
								bajarTablero(tablero);
							}
						}//fin else 25 disparos	
					}//fin if de bajar el tablero cada 25  tiradas
					
					if(existenBurbujas(tablero)==false && nivel != 5){//si no quedan burbujas pasamos de nivel
						System.out.println("Enhorabuena has pasado el nivel " + nivel);
						fin=false;
					}
					else if (existenBurbujas(tablero)==false && nivel == 5){//si no quedan burbujas hemos ganado
						System.out.println("Has GANADO el juego!");
						fin=false;
						hasPerdido=true;
					}
					else{//si quedan burbujas el juego continua y generamso una nueva
						csb=generarBurbuja(nivel);//nueva burbuja de disparo
					}
					
	
				}//fin del else de disparar	
			}//fin del bucle while
			
		}//FIN DEL FOR DE NIVELES
		
	}//fin del metodo jugar
	
	
	
	public static void main(String[]args){
		jugar();
	}//fin del main
}//fin de la clase














