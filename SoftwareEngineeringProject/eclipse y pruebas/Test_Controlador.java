/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cifrador.Controlador;
import cifrador.ErrorExcepcion;


public class Test_Controlador {

	static Controlador objeto;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		objeto = new Controlador();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testControlador() {
		fail("Not yet implemented");
	}

	//*************************************************************************************************************************************
	//					ATTBASH
	//*************************************************************************************************************************************
	
	@Test
	public void testHaz_attbash_1() throws ErrorExcepcion {
		//probamos que se ejecuta el componente, si se pasan parametros correctos en cada idioma
		//no deberia de dar problema
		String s1= objeto.haz_attbash("abcdefghijklmnÒopqrstuvwxyzABCDEFGHIJKLMN—OPQRSTUVWXYZ0123456789 !°ø?',;.:¡…Õ”⁄·ÈÌÛ˙",true, "castellano");
		String s2 = objeto.haz_attbash("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !°ø?',;.:",true, "ingles");
		String s3 = objeto.haz_attbash("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 !°ø?',;.:Á«¬‚ ÍŒÓ‘Ù€˚¿‡»ËŸ˘ÀÎœÔ‹¸üˇ¡…Õ”⁄·ÈÌÛ˙å∆",true, "frances");
		objeto.haz_attbash(s1, false, "castellano");
		objeto.haz_attbash(s2, false, "ingles");
		objeto.haz_attbash(s3, false, "frances");
	}//FIN test attbash_1
	
	@Test
	public void testHaz_attbash_2() throws ErrorExcepcion {
		//probamos que se ejecuta el componente, si se pasan parametros correctos
		//no deberia de dar problema
		objeto.haz_attbash("hola",false, "castellano");		
	}//FIN test attbash_2
	
	@Test
	public void testHaz_attbash_3() throws ErrorExcepcion {
		//probamos que se ejecuta el componente correctamente (cifra y descifra), si se pasan parametros correctos
		//no deberia de dar problema
		String texto = "ho la";
		String aux = objeto.haz_attbash(texto,true, "castellano");
		assertEquals(texto,objeto.haz_attbash(aux, false, "castellano"));
	}//FIN test attbash_3
	
	/*	
	 * con esto, si los parameteros son aceptados, deducimos que el metodo funcionara bien
	 *	probemos ahora que cosas no se aceptarian o no funcionarian
	 */
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_attbash_4() throws ErrorExcepcion {
		//probamos que si ejecuta el componente cuando texto<3 (2)caracteres
		//deberia lanzar la excepcion especificada
		objeto.haz_attbash("eh",true, "castellano");		
	}//FIN test attbash_4
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_attbash_5() throws ErrorExcepcion {
		//probamos que si ejecuta el componente cuando texto<144 (145) caracteres
		//deberia lanzar la excepcion especificada		
		objeto.haz_attbash("En un lugar de la Mancha, de cuyo nombre no quiero acordarme, "
				+ "no ha mucho tiempo que vivÌa un hidalgo de los de lanza en astillero, "
				+ "adarga antigu",true, "castellano");		
	}//FIN test attbash_5
	
	@Test
	public void testHaz_attbash_6() throws ErrorExcepcion {
		//probamos si se soporta el idioma expresado con espacios a los lados
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, " castellano");		
	}//FIN test attbash_6
	
	@Test
	public void testHaz_attbash_7() throws ErrorExcepcion {
		//probamos si se soporta el idioma expresado con espacios a los lados
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, "castellano ");		
	}//FIN test attbash_7
	
	@Test
	public void testHaz_attbash_8() throws ErrorExcepcion {
		//probamos si se soporta el idioma expresado en mayusculas
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, "CAStellANO");		
	}//FIN test attbash_8
	
	@Test
	public void testHaz_attbash_9() throws ErrorExcepcion {
		//probamos si se soporta el idioma ingles
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, "ingles");		
	}//FIN test attbash_9
	
	@Test
	public void testHaz_attbash_10() throws ErrorExcepcion {
		//probamos si se soporta el idioma frances
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, "frances");		
	}//FIN test attbash_10
	
	@Test
	public void testHaz_attbash_11() throws ErrorExcepcion {
		//probamos diferentes formas soportadas de introducir el idioma
		//no es demasiado correcto,pero no queremos extendernos demsiado
		//deberia caeptarlo
		assertEquals(objeto.haz_attbash(objeto.haz_attbash("hola",true, "ingles"), false, "ingles"),"hola");
		assertEquals(objeto.haz_attbash(objeto.haz_attbash("hola",true, "inglÈs"), false, "english"),"hola");
		assertEquals(objeto.haz_attbash(objeto.haz_attbash("hola",true, "francÈs"), false, "franÁais"),"hola");
		assertEquals(objeto.haz_attbash(objeto.haz_attbash("hola",true, "espaÒol"), false, "castellano"),"hola");
	}//FIN test attbash_11
	
	@Test(expected=NullPointerException.class)
	public void testHaz_attbash_12() throws ErrorExcepcion {
		//probamos si se soporta el idioma frances
		//deberia aceptarlo
		objeto.haz_attbash(null,true, "frances");		
	}//FIN test attbash_12
	
	@Test(expected=NullPointerException.class)
	public void testHaz_attbash_13() throws ErrorExcepcion {
		//probamos si se soporta el idioma frances
		//deberia aceptarlo
		objeto.haz_attbash("hola",true, null);		
	}//FIN test attbash_13
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_attbash_14() throws ErrorExcepcion {
		//probamos que pasa con caracteres no soportados
		//deberia rechazarlo
		objeto.haz_attbash("aliÒar",true, "frances");		
	}//FIN test attbash_14
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_attbash_15() throws ErrorExcepcion {
		//probamos que pasa con caracteres no soportados
		//deberia rechazarlo
		objeto.haz_attbash("«ÁaliÒar",true, "espaÒol");		
	}//FIN test attbash_15
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_attbash_16() throws ErrorExcepcion {
		//probamos que se ejecuta el componente, si se pasan parametros correctos
		//deberia rechazarlo
		objeto.haz_attbash("Îhola",true, "ingles");		
	}//FIN test attbash_16
	
	@Test
	public void testHaz_attbash_17() throws ErrorExcepcion {
		//probamos que si ejecuta el componente cuando texto=3 caracteres
		//deberia aceptarlo
		objeto.haz_attbash("ese",true, "castellano");		
	}//FIN test attbash_17
	
	@Test
	public void testHaz_attbash_18() throws ErrorExcepcion {
		//probamos que si ejecuta el componente cuando texto=144 caracteres
		//deberia aceptarlo		
		objeto.haz_attbash("En un lugar de la Mancha, de cuyo nombre no quiero acordarme, "
				+ "no ha mucho tiempo que vivÌa un hidalgo de los de lanza en astillero, "
				+ "adarga antig",true, "castellano");		
	}//FIN test attbash_18
	
	
	/**
	 * NOTA: a pesar de tratarse teoricamente de pruebas de caja negra, para evitar repetir pruebas
	 * y que se alargue este codigo de forma considerable, vamos a evitar repetir pruebas que sabemos 
	 * (porque conocemos el codigo, a pesar de ser caja negra) 
	 * por ejemplo pruebas de alfabetos, caracteres y valores erroneos (null) de entrada se dan por probados
	 * en todos los metodos, con identicos resultados
	 * 
	 * se probara por tanto si se cifra y descifra correctamente, y las distintas posibilidades con las claves.
	 * 
	 * */
	
	
	
	//*************************************************************************************************************************************
	//					CESAR
	//*************************************************************************************************************************************

		
	@Test
	public void testHaz_cesar_1() throws ErrorExcepcion{
		//probaremos a crifrar y descifrar lo mismo, ver que funciona el cifrado/descifrado cesar
		String ss= objeto.haz_cesar("hola", true, 1, "espaÒol");
		assertEquals(objeto.haz_cesar(ss, false, 1, "castellano"),"hola");
	}//FIN test cesar 1
	
	@Test
	public void testHaz_cesar_2() throws ErrorExcepcion{
		//probaremos a crifrar con clave negativa
		//deberia funcionar
		String ss= objeto.haz_cesar("hola", true, -100, "espaÒol");
		assertEquals(objeto.haz_cesar(ss, false, -100, "castellano"),"hola");
	}//FIN test cesar 2	
	
	@Test
	public void testHaz_cesar_3() throws ErrorExcepcion{
		//probaremos a crifrar con clave exageradamente grande
		//deberia funcionar
		String ss= objeto.haz_cesar("hola", true, 100000, "espaÒol");
		assertEquals(objeto.haz_cesar(ss, false, 100000, "castellano"),"hola");
	}//FIN test cesar 3
	
	//No puedo probar a llamar a cesar con clave string, o con clave double, eclipse no me lo permite, falla el metodo
	
	@Test
	public void testHaz_cesar_4() throws ErrorExcepcion{
		//probaremos a crifrar con clave 0
		//deberia funcionar
		String ss= objeto.haz_cesar("hola", true, 0, "espaÒol");
		assertEquals(objeto.haz_cesar(ss, false, 0, "castellano"),"hola");
	}//FIN test cesar 4
	
	
	
	//*************************************************************************************************************************************
	//					VIGENERE
	//*************************************************************************************************************************************


	@Test(expected=ErrorExcepcion.class)
	public void testHaz_vigenere_1() throws ErrorExcepcion{
		//probaremos a crifrar con clave incorrecta (longitud <2)
		//deberia rechazarlos
		String ss= objeto.haz_vigenere("hola", true, "h", "espaÒol");
		assertEquals(objeto.haz_vigenere(ss, false, "h", "castellano"),"hola");	
	}//FIN test vigenere 1
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_vigenere_2() throws ErrorExcepcion{
		//probaremos a crifrar con clave incorrecta (longitud excesiva)
		//deberia rechazarlos
		String ss= objeto.haz_vigenere("hola", true, "heyy", "espaÒol");
		assertEquals(objeto.haz_vigenere(ss, false, "heyy", "castellano"),"hola");	
	}//FIN test vigenere 2

	@Test
	public void testHaz_vigenere_3() throws ErrorExcepcion{
		//probaremos a crifrar correctamente
		//deberia aceptarlo
		String ss= objeto.haz_vigenere("hola", true, "uy", "espaÒol");
		assertEquals(objeto.haz_vigenere(ss, false, "uy", "castellano"),"hola");	
	}//FIN test vigenere 3
	
	
	//*************************************************************************************************************************************
	//					VERNAM
	//*************************************************************************************************************************************

	
	
	
	@Test
	public void testHaz_vernam_1() throws ErrorExcepcion{
		//probaremos a crifrar correctamente
		//deberia aceptarlo
		String ss= objeto.haz_vernam("hola", true, "oyes", "espaÒol");
		assertEquals(objeto.haz_vernam(ss, false, "oyes", "castellano"),"hola");
	}//FIN test vernam 1

	@Test(expected=ErrorExcepcion.class)
	public void testHaz_vernam_2() throws ErrorExcepcion{
		//probaremos a crifrar con clave erronea(<longitud)
		//deberia rechazarlo
		String ss= objeto.haz_vernam("hola", true, "ola", "espaÒol");
		assertEquals(objeto.haz_vernam(ss, false, "ola", "castellano"),"hola");
	}//FIN test vernam 2
	
	@Test(expected=ErrorExcepcion.class)
	public void testHaz_vernam_3() throws ErrorExcepcion{
		//probaremos a crifrar con clave erronea(>longitud)
		//deberia rechazarlo
		String ss= objeto.haz_vernam("hola", true, "yiiha", "espaÒol");
		assertEquals(objeto.haz_vernam(ss, false, "yiiha", "castellano"),"hola");
	}//FIN test vernam 3
	
	@Test
	public void testHaz_vernam_4() throws ErrorExcepcion{
		//probaremos a crifrar correcto y desdifrar cno idioma lo que sea
		//deberia aceptarlo
		String ss= objeto.haz_vernam("hola", true, "lala", "espaÒol");
		assertEquals(objeto.haz_vernam(ss, false, "lala", "asdfkjsdf asdfhoÒasdf"),"hola");
	}//FIN test vernam 4
	
	
	
}/*FIN DE CLASE*/
