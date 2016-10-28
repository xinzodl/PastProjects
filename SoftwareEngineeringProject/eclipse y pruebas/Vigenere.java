/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class Vigenere {
	
	protected String cifra_vigenere (String texto, String clave, char[] array_alfabeto){
		char array_clave [] = generar_array_clave(clave, texto);
		String devolver="";
		int index_1, index_2, aux;
		
		for (int ii=0; ii<texto.length();ii++){
			index_1 = getIndex(texto.charAt(ii), array_alfabeto);
			index_2 = getIndex(array_clave[ii], array_alfabeto);
			aux = (index_1 + index_2)%(array_alfabeto.length);
			devolver = devolver + array_alfabeto[aux];
		}
		
		return devolver;
	}/*FIN de metodo cifra_vigenere*/
	

	protected String descifra_vigenere (String texto, String clave, char[] array_alfabeto){
		char array_clave [] = generar_array_clave(clave, texto);
		String devolver="";
		int index_1, index_2, aux;
		
		for (int ii=0; ii<texto.length();ii++){
			index_1 = getIndex(texto.charAt(ii), array_alfabeto);
			index_2 = getIndex(array_clave[ii], array_alfabeto);
			aux = (index_1 - index_2 + array_alfabeto.length)%(array_alfabeto.length);//la suma de +array_alfabeto.length es para casos de resta negativa
			devolver = devolver + array_alfabeto[aux];
		}
		
		return devolver;
	}/*FIN de metodo descifra_vigenere*/
	

	/*METODOS AUXILIARES*/
	private char [] generar_array_clave(String clave, String texto){
		char devolver [] = new char[texto.length()];
		
		int ii = 0;
		while (ii<texto.length()){
			devolver[ii]=clave.charAt(ii%clave.length());
			ii++;
		}
		
		return devolver;
	}
	
	
	private int getIndex(char aa, char [] array) {//se en que posicion de un array esta un caracter
		for (int ii=0; ii<array.length; ii++){
			if (array[ii]==aa){
				return ii;
			}
		}
		System.out.println("ALGO HA IDO MAL, NO DEBERIA SALIR DEL BUCLE");
		return -1;
	}
	
}/*FIN DE LA CLASE VIGENERE*/
