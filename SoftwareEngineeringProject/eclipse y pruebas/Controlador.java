/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

/*mirar como de complicado es devolver el caracter que esta estorbando en el texto (en caso de no superar pruebas)
 * no es dificil, la cosa es si te apetece hacerlo xd y tienes tiempo claro,..*/
/*y mirar que pasa si me meten varias palabras separadas por espacion, hay que respetar los espacios?*/

/**OJO A LA CLAVE DE VIGENERE, QUE NO ES INT!*/

/**PARA VERNAM NECESITAMOS QUE LA LONGITUD DEL ALFABETO SEA DE POTENCIAS DE 2 */
public class Controlador {
	
	
	/*VARIABLES GLOBALES*/
	private Validador comprueba;
	private char[] array_alfabeto;
	private String cadena_alfabeto;
	/*el string contiene todos los caracteres que admite este cifrador, "junta" todos los caracteres aceptados en esa cadena, nos da igual de que idioma sea
	 	es mas, podemos cifrar textos que mezclen idiomas en su interior*/
	
	/*CONSTRUCTOR*/
	public Controlador(){
		comprueba = new Validador();
	}/*FIN de constructor*/
	
	
	/*METODOS*/
	public String haz_attbash (String texto, boolean indicador, String idioma) throws ErrorExcepcion{//true: cifra, false:descifra
		cadena_alfabeto=comprueba.comprueba_idioma(idioma);
		
		if (cadena_alfabeto.length()==0) {//si false, la ! le da la vuelta y entra: texto icorrecto
			throw new ErrorExcepcion("El alfabeto es incorrecto");
		}
		else if (!comprueba.comprueba_texto(texto, cadena_alfabeto)) {
			throw new ErrorExcepcion("El texto es incorrecto");
		}
		else{//texto correcto
			array_alfabeto=cadena_alfabeto.toCharArray();
			Attbash aux = new Attbash();
			if (indicador){//cifra
//				System.out.println("\"" + texto + "\"" + " cifrado con Attbash");
				return aux.cifra_attbash(texto, array_alfabeto);
			}
			else{//descifra
//				System.out.println("\"" + texto + "\"" + " descifrado con Attbash");
				return aux.descifra_attbash(texto, array_alfabeto);
			}
		}//fin else: texto correcto
		
	}/*FIN metodo haz_attbash*/
	
	public String haz_cesar (String texto, boolean indicador, int clave, String idioma) throws ErrorExcepcion{//true: cifra, false:descifra
		cadena_alfabeto=comprueba.comprueba_idioma(idioma);
		
		if (cadena_alfabeto.length()==0) {//si false, la ! le da la vuelta y entra: texto icorrecto
			throw new ErrorExcepcion("El alfabeto es incorrecto");
		}
		else if (!comprueba.comprueba_texto(texto, cadena_alfabeto)) {
			throw new ErrorExcepcion("El texto es incorrecto");
		}
		else{//texto correcto
			array_alfabeto=cadena_alfabeto.toCharArray();
			Cesar aux = new Cesar();
			if (indicador){//cifra
//				System.out.println("\"" + texto + "\"" + " cifrado con Cesar, clave " + clave);
				return aux.cifra_cesar(texto, clave, array_alfabeto);
			}
			else{//descifra
//				System.out.println("\"" + texto + "\"" + " descifrado con Cesar, clave " + clave);
				return aux.descifra_cesar(texto, clave, array_alfabeto);
			}
		}//fin else: texto correcto
		
	}/*FIN metodo haz_cesar*/
	
	public String haz_vigenere (String texto, boolean indicador, String clave, String idioma) throws ErrorExcepcion{//true: cifra, false:descifra
		cadena_alfabeto=comprueba.comprueba_idioma(idioma);
		
		if (cadena_alfabeto.length()==0) {//si false, la ! le da la vuelta y entra: texto icorrecto
			throw new ErrorExcepcion("El alfabeto es incorrecto");
		}
		else if (!comprueba.comprueba_texto(texto, cadena_alfabeto)) {
			throw new ErrorExcepcion("El texto es incorrecto");
		}
		else if (!comprueba.comprueba_clave_vigenere(texto, clave)){//si false, la ! le da la vuelta y entra: clave incorrecta
			throw new ErrorExcepcion("La clave es incorrecta");
		}
		else{//texto correcto
			array_alfabeto=cadena_alfabeto.toCharArray();
			Vigenere aux = new Vigenere();
			if (indicador){//cifra
//				System.out.println("\"" + texto + "\"" + " cifrado con Vigenere, clave " + clave);
				return aux.cifra_vigenere(texto, clave, array_alfabeto);
			}
			else{//descifra
//				System.out.println("\"" + texto + "\"" + " descifrado con Vigenere, clave " + clave);
				return aux.descifra_vigenere(texto, clave, array_alfabeto);
			}
		}//fin else: texto correcto	
	
	}/*FIN metodo haz_vigenere*/
	
	public String haz_vernam (String texto, boolean indicador, String clave, String idioma) throws ErrorExcepcion{//true: cifra, false:descifra
		cadena_alfabeto=comprueba.comprueba_idioma(idioma);
		
		if (indicador && cadena_alfabeto.length()==0) {//si false, la ! le da la vuelta y entra: texto icorrecto
			throw new ErrorExcepcion("El alfabeto es incorrecto");//si no codificamos, no nos preocupamos del alfabeto
		}
		else if (indicador && !comprueba.comprueba_texto(texto, cadena_alfabeto)) {//si no codificamos, no comprobamos que el texto este en el alfabeto
			throw new ErrorExcepcion("El texto es incorrecto");
		}
		else if (!comprueba.comprueba_clave_vernam(texto, clave)){//si false, la ! le da la vuelta y entra: clave incorrecta
			throw new ErrorExcepcion("La clave es incorrecta");
		}
		else{//texto correcto
			array_alfabeto=cadena_alfabeto.toCharArray();
			Vernam aux = new Vernam();
			if (indicador){//cifra
//				System.out.println("\"" + texto + "\"" + " cifrado con Vernam, clave " + clave);
				return aux.cifra_vernam(texto, clave);
			}
			else{//descifra
//				System.out.println("\"" + texto + "\"" + " descifrado con Vernam, clave " + clave);
				return aux.descifra_vernam(texto, clave);
			}
		}//fin else: texto correcto	
	
	}/*FIN metodo haz_vernam*/
	

	
}/*FIN DE CLASE CONTROLADOR*/
