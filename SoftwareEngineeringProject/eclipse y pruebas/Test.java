/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class Test {

	public static void main(String[] args) throws ErrorExcepcion {

		Controlador prueba = new Controlador();
		
		/*
		//pruebas attbash
		System.out.println(prueba.haz_attbash("carlos pussy", true, "castellano"));
		System.out.println(prueba.haz_attbash("íú!.? s¿8  4", false, "castellano"));
		System.out.println(prueba.haz_attbash("üùÓU", false, "frances"));
		System.out.println(prueba.haz_attbash("oui!", true, "frances"));
		*/
		
		/*
		//pruebas cesar
		System.out.println(prueba.haz_cesar("hola como estas", true, 15, "castellano"));
		System.out.println(prueba.haz_cesar("hola como estas", true, 15+28, "ingles"));
		System.out.println(prueba.haz_cesar("hola como estas", true, 15-28, "frances"));
		System.out.println(prueba.haz_cesar("vDzoáqDADásHIoH", false, 15, "castellano"));
		*/
		System.out.println(prueba.haz_cesar("hola", true, 1, "castellano"));

		
		/*
		//prueba vigenere
		System.out.println(prueba.haz_vigenere("parisvautbienunemesse", true, "loup", "ingles"));
		System.out.println(prueba.haz_vigenere("AoLxDJuJEpCtyIHtxsMHp", false, "loup", "ingles"));
		 */
		
		/*int aa = 104;
		int bb = 101;
		System.out.println(aa^bb);
		System.out.println(13^104);*/
		
		
	/*	String cadena_alfabeto = "abcdefghijklmnñopqrstuvwxyz";
		char [] aux = cadena_alfabeto.toCharArray();
		System.out.println( 15^19 );
		System.out.println(aux[(15^19)%aux.length]);
		System.out.println((((1+27)^19))%aux.length);*/
		
		/*
		//prueba vernam
		String ss = prueba.haz_vernam("mi carro me lo robaron", true, "holaadiosa123456789012", "castellano");
		System.out.println(prueba.haz_vernam(ss, false, "holaadiosa123456789012", ""));
		*/
		
		
		/*
		char devolver [];
		devolver = "".toCharArray();
		System.out.println(devolver.length);
		*/
	
		
		
		
	}/*FIN DEL METODO MAIN*/

	
	
}/*FIN DE LA CLASE TEST*/
