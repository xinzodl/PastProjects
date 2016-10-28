/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class Attbash  {
	
	
	protected String cifra_attbash (String texto, char[] array_alfabeto) {
		String devuelve = "";//para guardar lo que vamos cifrando
		char clave [] = invertir_array(array_alfabeto);//se cifra con el el fabeto dado la vuelta
		int indice;//para saber por que caracter debems cambiar uno dado
		
		for (int ii=0;ii<texto.length();ii++){//recorremos el texto, miramos en que posicion del alfabeto esta eese caracter, y devolvemos el caracter de esa misma posicion, pero de la clave
			indice = getIndex(texto.charAt(ii), array_alfabeto);
			devuelve = devuelve + clave[indice];
		}
		
		return devuelve;
	}/*FIN de metodo cifra_attbash*/
	
	
	protected String descifra_attbash (String texto, char[] array_alfabeto){
		//podiamos copiar lo de arriba, o decirle que cifre con la clave com alfabeto, que es lo mismo que descifrar
		return cifra_attbash(texto, invertir_array(array_alfabeto));
	}/*FIN de metodo descifra_attbash*/
	
	
	
	
	
	
	/*METODOS AUXILIARES*/
	
	private char [] invertir_array (char [] array){//invierto un array
		
		int longitud = array.length;
		char devuelve [] = new char[longitud];
		
		for (int ii=0; ii<longitud; ii++){
			devuelve[ii]=array[longitud-ii-1];
		}
				
		return devuelve;
	}/**/
	
	
	private int getIndex(char aa, char [] array) {//se en que posicion de un array esta un caracter
		for (int ii=0; ii<array.length; ii++){
			if (array[ii]==aa){
				return ii;
			}
		}
		System.out.println("ALGO HA IDO MAL, NO DEBERIA SALIR DEL BUCLE");
		return -1;
	}
	
}/*FUN DE LA CLASE ATTBASH*/
