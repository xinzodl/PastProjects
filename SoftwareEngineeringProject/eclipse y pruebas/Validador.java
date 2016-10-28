/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class Validador {
	
	protected String comprueba_idioma (String idioma){
		idioma = idioma.trim().toLowerCase();

		if (idioma.equals("castellano") || idioma.equals("español")){
			return "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789 !¡¿?',;.:ÁÉÍÓÚáéíóú";
		}
		else if (idioma.equals("ingles") || idioma.equals("inglés") || idioma.equals("english")){
			return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !¡¿?',;.:";
		}
		else if (idioma.equals("frances") || idioma.equals("francés") || idioma.equals("français")){
			return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !¡¿?',;.:çÇÂâÊêÎîÔôÛûÀàÈèÙùËëÏïÜüŸÿÁÉÍÓÚáéíóúŒÆ";
		}
		else {
			return "";
		}
	}/*FIN de metodo comprueba_idioma*/
	
	
	protected boolean comprueba_texto (String texto, String alfabeto){//true:pasa prueba, false: no pasa prueba
		//prueba que se realiza en todos los metodos de cifrado/descifrado
		if ( texto.length()<3 || texto.length()>144 || !comprueba_alfabeto(texto, alfabeto)){//texto incorrecto
			return false;
		}
		else{//texto correcto
			return true;
		}
	}/*FIN de metodo comprueba_texto*/
	
	
	protected boolean comprueba_clave_vigenere (String texto, String clave){//true:pasa prueba, false: no pasa prueba
		//longitud clave entre 2 y mitad de longitd de texto +1
		if (clave.length()<2 || clave.length()>(texto.length()/2+1)){
			return false;
		}
		else{
			return true;
		}
		
	}/*FIN de metodo comprueba_clave*/
	
	
 	protected boolean comprueba_clave_vernam (String texto, String clave){//true:pasa prueba, false: no pasa prueba
		if (texto.length()==clave.length()){//se permiten claves de la msima longitud del texto
			return true;
		}
		else{
			return false;
		}
	}/*FIN de metodo comprueba_clave*/
	
 	
	private boolean comprueba_alfabeto(String texto, String alfabeto){//true:esta bien, false:caracteres incorrectos
		int ii = 0;
		boolean sigue = true;
		while (ii<texto.length() && sigue){
			sigue = alfabeto.contains(String.valueOf(texto.charAt(ii)));
			ii++;
		}
		return sigue;
	}/*FIN de metodo comprueba_alfabeto*/

}/*FIN CLASE VALIDADOR*/
