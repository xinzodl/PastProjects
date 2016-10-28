/**
 *Ejemplo de c�mo debe usarse el una implementaci�n de MemoriaIntermedia, 
 * en este caso la memoria interm�dia con p�litica de liberaci�n aleatoria: 
 * MemoriaIntermediaRA.
 */
import interfazSGF.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class EjemploUsoMemoriaIntermedia {
        
    /**
	 * Ejemplo de uso de una memoria intermedia con pol�tica de liberaci�n aleatoria de 2 p�ginas de tama�o 10 bytes.
	 * @param args
	 */
	public static void main(String[] args) {
            int numeroDePaginas=2; //N�mero de p�ginas de la memoria intermedia que se crear�
            int tamannoDeBloque=16; //Tama�o de las p�ginas de la memoria intermedia

		/*
		 * Creamos un objeto memoria intermedia 
		 * Cada p�gina de memoria contendr� uno de los bloques de nuestros ficheros 
		 * Podremos varios ficheros abiertos dentro de la memoria intermedia tanto de lectura como de escritura 
		 */  
		MemoriaIntermedia  mem = new MemoriaIntermediaRA(numeroDePaginas,tamannoDeBloque); 
		ByteBuffer buffer; // Objeto para la gesti�n de un buffer (java.nio.ByteBuffer) 
		FileChannel f0;    // Referencia a un fichero (java.nio.channel.FileChannel)
                FileChannel f1;    // Referencia a otro fichero (java.nio.channel.FileChannel)
		
		String f0_cadena0 = "f0 cadena 0";//Cadena de caracteres que se escribir� en el fichero f0 en el bloque 0
		String f0_cadena1 = "f0 cadena 1";//Cadena de caracteres que se escribir� en el fichero f0 en el bloque 1
                String f0_cadena2 = "f0 cadena 2";//Cadena de caracteres que se escribir� en el fichero f0 en el bloque 2
                
                String f1_cadena0 = "f1 cadena 0";//Cadena de caracteres que se escribir� en el fichero f1 en el bloque 0
		String f1_cadena1 = "f1 cadena 1";//Cadena de caracteres que se escribir� en el fichero f1 en el bloque 1
                String f1_cadena2 = "f1 cadena 2";//Cadena de caracteres que se escribir� en el fichero f1 en el bloque 2
                
                byte [] bytesCadena=new byte[11]; //Array que se utilizar� para recuperar cadenas de 10 caracteres de los ficheros
                
		try {
                        //Estado inicial de las p�ginas de la memoria intermedia
                        System.out.println("\n0: Estado inicial\n");
                        mem.imprimir();
                    
                        System.out.println("\n1: Escribiendo bloque 0 de prueba0.txt\n");
                        
			// Se abre el fichero 'prueba0.txt' en la memoria intermedia en la variable f0 (si no existe el fichero se crea)
			f0 = mem.abrir("prueba0.txt","rw");	
                        
                        // Se adquiere el primer bloque del fichero f0 a trav�s de la memoria intermedia (1 acceso)
			buffer = mem.adquirir(f0, 0);
                                                	
			// Se lleva el puntero a la posici�n inicial del bloque
			buffer.clear();
                        //Se escribe en el bloque la cadena de caracteres f0_cadena0.
			buffer.put(f0_cadena0.getBytes());

                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n2: Escribiendo bloque 1 de prueba0.txt\n");                        
                        // Se adquiere el bloque 1 de f0, se lleva el puntero a la posici�n inicial y se escribe en �l la cadena de caracteres f0_cadena1 (1 acceso, van 2)
                        buffer = mem.adquirir(f0, 1);
			buffer.clear();
			buffer.put(f0_cadena1.getBytes());
                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n3: Escribiendo bloque 2 de prueba0.txt\n");
                        
                        // Se adquiere el bloque 2 de f0, se lleva el puntero a la posici�n inicial y se escribe en �l la cadena de caracteres f0_cadena2 (1 acceso, van 3)
                        buffer = mem.adquirir(f0, 2);
			buffer.clear();
			buffer.put(f0_cadena2.getBytes());
                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n4: Leyendo bloque 0 de prueba0.txt y reemplaz�ndolo por may�sculas\n");
                        
                        // Se recupera el contenido del bloque 0 de f0, se lleva el puntero a la posici�n inicial y se imprimen por pantalla sus 11 primeros bytes (m�ximo 1 acceso si el bloque ya no estuviera cargado en alguna p�gina, como mucho van 4)
                        buffer = mem.adquirir(f0, 0);
                        buffer.clear();                        
                        buffer.get(bytesCadena);
			System.out.println(new String(bytesCadena));
                        //Se pone lleva el puntero a la posici�n inicial del buffer y se sustituye por la misma cadena en may�sculas
                        buffer.clear();
                        buffer.put(new String(bytesCadena).toUpperCase().getBytes());
			                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n5: Escribiendo bloque 0 de prueba1.txt\n");
                        
                        // Se abre el fichero 'prueba1.txt' en la memoria intermedia en la variable f1 (si no existe el fichero se crea).
			f1 = mem.abrir("prueba1.txt","rw");
                        
                        // Se adquiere el bloque 0 de f1, se lleva el puntero a la posici�n inicial y se escribe en �l la cadena de caracteres f1_cadena0 (1 acceso, como mucho van 5)
                        buffer = mem.adquirir(f1, 0);
			buffer.clear();
			buffer.put(f1_cadena0.getBytes());
                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n6: Escribiendo bloque 1 de prueba1.txt\n");
                        
                        // Se adquiere el bloque 1 de f1, se lleva el puntero a la posici�n inicial y se escribe en �l la cadena de caracteres f1_cadena1 (1 acceso, como mucho van 6)
                        buffer = mem.adquirir(f1, 1);
			buffer.clear();
			buffer.put(f1_cadena1.getBytes());
                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n7: Escribiendo bloque 2 de prueba1.txt\n");
                        
                        // Se adquiere el bloque 2 de f1, se lleva el puntero a la posici�n inicialy se escribe en �l la cadena de caracteres f1_cadena2 (1 acceso, como mucho van 7)
                        buffer = mem.adquirir(f1, 2);
			buffer.clear();
			buffer.put(f1_cadena2.getBytes());
                        
                        //Estado actual de las p�ginas de la memoria intermedia
                        mem.imprimir();
                        
                        // Se cierran los ficheros
			mem.cerrar(f0);
                        mem.cerrar(f1);
                        
                        // Se abren los ficheros en modo de s�lo lectura
			f0 = mem.abrir("prueba0.txt","r");
                        f1 = mem.abrir("prueba1.txt","r");
                        
                        //Se recuperan todas las cadenas de caracteres escritas en los ficheros sabiendo que todas tienen 11 bytes.
                                                                      
                        buffer = mem.adquirir(f0, 0); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 8)
                        buffer.clear();                        
                        buffer.get(bytesCadena); //Lectura de toda la cadena
                        System.out.println("\nprueba0.txt, bloque 0 (recuperada de una vez):'"+new String(bytesCadena)+"'\n");

                        buffer = mem.adquirir(f0, 1); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 9)
                        buffer.clear();
                        byte[] bytesMediaCadena1=new byte[5];
                        byte[] bytesMediaCadena2=new byte[6];
                        buffer.get(bytesMediaCadena1); //Lectura de media cadena
                        buffer.get(bytesMediaCadena2); //Lectura de la otra media cadena
                        System.out.println("\nprueba0.txt, bloque 1 (recuperada como dos mitades):'"+new String(bytesMediaCadena1)+new String(bytesMediaCadena2)+"'\n");
                        
                        buffer = mem.adquirir(f0, 2); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 10)
                        buffer.clear();                        
                        for(int i=0;i<11;i++)
                            bytesCadena[i]=buffer.get(); //Lectura byte a byte
                        System.out.println("\nprueba0.txt, bloque 2 (recuperada byte a byte):'"+new String(bytesCadena)+"'\n");
                        buffer = mem.adquirir(f1, 0); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 11)
                        buffer.clear();
                        buffer.get(bytesCadena);
                        System.out.println("\nprueba1.txt, bloque 0 (recuperada como dos mitades):'"+new String(bytesCadena)+"'\n");
                        
                        buffer = mem.adquirir(f1, 1); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 12)
                        buffer.clear();
                        bytesMediaCadena1=new byte[5];
                        bytesMediaCadena2=new byte[6];
                        buffer.get(bytesMediaCadena1); //Lectura de media cadena
                        buffer.get(bytesMediaCadena2); //Lectura de la otra media cadena
                        System.out.println("\nprueba1.txt, bloque 1:'"+new String(bytesMediaCadena1)+new String(bytesMediaCadena2)+"'\n");
                        
                        buffer = mem.adquirir(f1, 2); //(1 acceso m�ximo si este bloque ya ha sido liberado con antelaci�n, como mucho van 13)
                        buffer.clear();
                        for(int i=0;i<11;i++)
                            bytesCadena[i]=buffer.get(); //Lectura byte a byte
                        System.out.println("\nprueba1.txt, bloque 2 recuperada byte por byte):'"+new String(bytesCadena)+"'\n");
                                                                  
			// Se cierran los ficheros
			mem.cerrar(f0);
                        mem.cerrar(f1);
                        
			// N�mero de accesos realizados a memoria secundaria. A medida que se aumenta el n�mero de p�ginas de la memoria intermedia se reduce el n�mero de accesos --> probar ;)			
			System.out.println("\n\nN�mero de accesos : " + mem.getContadorAccesos()+"<=13");
			
			
			
			
		} catch (IOException e) {
			// Si se producen problemas, mostramos el origen del problema 
			e.printStackTrace();
		}
			
	}        
           
}
