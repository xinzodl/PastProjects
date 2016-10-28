/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;
/**DEBEMOS SEPARAR LAS PALABRAS PARA CIFRAR?????*/
public class Cesar {
	
	protected String cifra_cesar (String texto, int clave, char[] array_alfabeto){
		String devuelve ="";
		int indice;
		int aux;
		clave = clave%array_alfabeto.length;//en caso de numeros mayores de la longitud del array como clave
		
		for (int ii=0;ii<texto.length();ii++){
			indice = getIndex(texto.charAt(ii), array_alfabeto);
			aux = (indice + clave + array_alfabeto.length)%(array_alfabeto.length);//la suma de +array_alfabeto.length es para el caso de clave negativa
			devuelve = devuelve + array_alfabeto[aux];
		}
		
		return devuelve;
	}/*FIN de metodo cifra_cesar*/
	
	
	protected String descifra_cesar (String texto, int clave, char[] array_alfabeto){//basta con cifrar con la clave en negativo
		return cifra_cesar(texto, -clave, array_alfabeto);
	}/*FIN de metodo descifra_cesar*/
	
	
	
	/*METODOS AUXILIARES*/
	private int getIndex(char aa, char [] array) {//se en que posicion de un array esta un caracter
		for (int ii=0; ii<array.length; ii++){
			if (array[ii]==aa){
				return ii;
			}
		}
		System.out.println("ALGO HA IDO MAL, NO DEBERIA SALIR DEL BUCLE");
		return -1;
	}
	
}/*FIN DE LA CLASE CESAR*/
