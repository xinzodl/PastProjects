/**
 *Ejemplo de cómo debe usarse el una implementación de MemoriaIntermedia, 
 * en este caso la memoria intermédia con pólitica de liberación aleatoria: 
 * MemoriaIntermediaRA.
 */
import interfazSGF.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class EjemploUsoMemoriaIntermedia {
        
    /**
	 * Ejemplo de uso de una memoria intermedia con política de liberación aleatoria de 2 páginas de tamaño 10 bytes.
	 * @param args
	 */
	public static void main(String[] args) {
            int numeroDePaginas=2; //Número de páginas de la memoria intermedia que se creará
            int tamannoDeBloque=16; //Tamaño de las páginas de la memoria intermedia

		/*
		 * Creamos un objeto memoria intermedia 
		 * Cada página de memoria contendrá uno de los bloques de nuestros ficheros 
		 * Podremos varios ficheros abiertos dentro de la memoria intermedia tanto de lectura como de escritura 
		 */  
		MemoriaIntermedia  mem = new MemoriaIntermediaRA(numeroDePaginas,tamannoDeBloque); 
		ByteBuffer buffer; // Objeto para la gestión de un buffer (java.nio.ByteBuffer) 
		FileChannel f0;    // Referencia a un fichero (java.nio.channel.FileChannel)
                FileChannel f1;    // Referencia a otro fichero (java.nio.channel.FileChannel)
		
		String f0_cadena0 = "f0 cadena 0";//Cadena de caracteres que se escribirá en el fichero f0 en el bloque 0
		String f0_cadena1 = "f0 cadena 1";//Cadena de caracteres que se escribirá en el fichero f0 en el bloque 1
                String f0_cadena2 = "f0 cadena 2";//Cadena de caracteres que se escribirá en el fichero f0 en el bloque 2
                
                String f1_cadena0 = "f1 cadena 0";//Cadena de caracteres que se escribirá en el fichero f1 en el bloque 0
		String f1_cadena1 = "f1 cadena 1";//Cadena de caracteres que se escribirá en el fichero f1 en el bloque 1
                String f1_cadena2 = "f1 cadena 2";//Cadena de caracteres que se escribirá en el fichero f1 en el bloque 2
                
                byte [] bytesCadena=new byte[11]; //Array que se utilizará para recuperar cadenas de 10 caracteres de los ficheros
                
		try {
                        //Estado inicial de las páginas de la memoria intermedia
                        System.out.println("\n0: Estado inicial\n");
                        mem.imprimir();
                    
                        System.out.println("\n1: Escribiendo bloque 0 de prueba0.txt\n");
                        
			// Se abre el fichero 'prueba0.txt' en la memoria intermedia en la variable f0 (si no existe el fichero se crea)
			f0 = mem.abrir("prueba0.txt","rw");	
                        
                        // Se adquiere el primer bloque del fichero f0 a través de la memoria intermedia (1 acceso)
			buffer = mem.adquirir(f0, 0);
                                                	
			// Se lleva el puntero a la posición inicial del bloque
			buffer.clear();
                        //Se escribe en el bloque la cadena de caracteres f0_cadena0.
			buffer.put(f0_cadena0.getBytes());

                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n2: Escribiendo bloque 1 de prueba0.txt\n");                        
                        // Se adquiere el bloque 1 de f0, se lleva el puntero a la posición inicial y se escribe en él la cadena de caracteres f0_cadena1 (1 acceso, van 2)
                        buffer = mem.adquirir(f0, 1);
			buffer.clear();
			buffer.put(f0_cadena1.getBytes());
                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n3: Escribiendo bloque 2 de prueba0.txt\n");
                        
                        // Se adquiere el bloque 2 de f0, se lleva el puntero a la posición inicial y se escribe en él la cadena de caracteres f0_cadena2 (1 acceso, van 3)
                        buffer = mem.adquirir(f0, 2);
			buffer.clear();
			buffer.put(f0_cadena2.getBytes());
                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n4: Leyendo bloque 0 de prueba0.txt y reemplazándolo por mayúsculas\n");
                        
                        // Se recupera el contenido del bloque 0 de f0, se lleva el puntero a la posición inicial y se imprimen por pantalla sus 11 primeros bytes (máximo 1 acceso si el bloque ya no estuviera cargado en alguna página, como mucho van 4)
                        buffer = mem.adquirir(f0, 0);
                        buffer.clear();                        
                        buffer.get(bytesCadena);
			System.out.println(new String(bytesCadena));
                        //Se pone lleva el puntero a la posición inicial del buffer y se sustituye por la misma cadena en mayúsculas
                        buffer.clear();
                        buffer.put(new String(bytesCadena).toUpperCase().getBytes());
			                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n5: Escribiendo bloque 0 de prueba1.txt\n");
                        
                        // Se abre el fichero 'prueba1.txt' en la memoria intermedia en la variable f1 (si no existe el fichero se crea).
			f1 = mem.abrir("prueba1.txt","rw");
                        
                        // Se adquiere el bloque 0 de f1, se lleva el puntero a la posición inicial y se escribe en él la cadena de caracteres f1_cadena0 (1 acceso, como mucho van 5)
                        buffer = mem.adquirir(f1, 0);
			buffer.clear();
			buffer.put(f1_cadena0.getBytes());
                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n6: Escribiendo bloque 1 de prueba1.txt\n");
                        
                        // Se adquiere el bloque 1 de f1, se lleva el puntero a la posición inicial y se escribe en él la cadena de caracteres f1_cadena1 (1 acceso, como mucho van 6)
                        buffer = mem.adquirir(f1, 1);
			buffer.clear();
			buffer.put(f1_cadena1.getBytes());
                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        System.out.println("\n7: Escribiendo bloque 2 de prueba1.txt\n");
                        
                        // Se adquiere el bloque 2 de f1, se lleva el puntero a la posición inicialy se escribe en él la cadena de caracteres f1_cadena2 (1 acceso, como mucho van 7)
                        buffer = mem.adquirir(f1, 2);
			buffer.clear();
			buffer.put(f1_cadena2.getBytes());
                        
                        //Estado actual de las páginas de la memoria intermedia
                        mem.imprimir();
                        
                        // Se cierran los ficheros
			mem.cerrar(f0);
                        mem.cerrar(f1);
                        
                        // Se abren los ficheros en modo de sólo lectura
			f0 = mem.abrir("prueba0.txt","r");
                        f1 = mem.abrir("prueba1.txt","r");
                        
                        //Se recuperan todas las cadenas de caracteres escritas en los ficheros sabiendo que todas tienen 11 bytes.
                                                                      
                        buffer = mem.adquirir(f0, 0); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 8)
                        buffer.clear();                        
                        buffer.get(bytesCadena); //Lectura de toda la cadena
                        System.out.println("\nprueba0.txt, bloque 0 (recuperada de una vez):'"+new String(bytesCadena)+"'\n");

                        buffer = mem.adquirir(f0, 1); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 9)
                        buffer.clear();
                        byte[] bytesMediaCadena1=new byte[5];
                        byte[] bytesMediaCadena2=new byte[6];
                        buffer.get(bytesMediaCadena1); //Lectura de media cadena
                        buffer.get(bytesMediaCadena2); //Lectura de la otra media cadena
                        System.out.println("\nprueba0.txt, bloque 1 (recuperada como dos mitades):'"+new String(bytesMediaCadena1)+new String(bytesMediaCadena2)+"'\n");
                        
                        buffer = mem.adquirir(f0, 2); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 10)
                        buffer.clear();                        
                        for(int i=0;i<11;i++)
                            bytesCadena[i]=buffer.get(); //Lectura byte a byte
                        System.out.println("\nprueba0.txt, bloque 2 (recuperada byte a byte):'"+new String(bytesCadena)+"'\n");
                        buffer = mem.adquirir(f1, 0); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 11)
                        buffer.clear();
                        buffer.get(bytesCadena);
                        System.out.println("\nprueba1.txt, bloque 0 (recuperada como dos mitades):'"+new String(bytesCadena)+"'\n");
                        
                        buffer = mem.adquirir(f1, 1); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 12)
                        buffer.clear();
                        bytesMediaCadena1=new byte[5];
                        bytesMediaCadena2=new byte[6];
                        buffer.get(bytesMediaCadena1); //Lectura de media cadena
                        buffer.get(bytesMediaCadena2); //Lectura de la otra media cadena
                        System.out.println("\nprueba1.txt, bloque 1:'"+new String(bytesMediaCadena1)+new String(bytesMediaCadena2)+"'\n");
                        
                        buffer = mem.adquirir(f1, 2); //(1 acceso máximo si este bloque ya ha sido liberado con antelación, como mucho van 13)
                        buffer.clear();
                        for(int i=0;i<11;i++)
                            bytesCadena[i]=buffer.get(); //Lectura byte a byte
                        System.out.println("\nprueba1.txt, bloque 2 recuperada byte por byte):'"+new String(bytesCadena)+"'\n");
                                                                  
			// Se cierran los ficheros
			mem.cerrar(f0);
                        mem.cerrar(f1);
                        
			// Número de accesos realizados a memoria secundaria. A medida que se aumenta el número de páginas de la memoria intermedia se reduce el número de accesos --> probar ;)			
			System.out.println("\n\nNúmero de accesos : " + mem.getContadorAccesos()+"<=13");
			
			
			
			
		} catch (IOException e) {
			// Si se producen problemas, mostramos el origen del problema 
			e.printStackTrace();
		}
			
	}        
           
}
