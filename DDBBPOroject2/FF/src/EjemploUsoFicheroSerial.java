/**
 *Ejemplo de como debe usarse el driver serial para acceder a un fichero.
 */
import interfazSGF.*;
public class EjemploUsoFicheroSerial {
    
    public static void main(String[] args){
		try{
			//Nuevo driver serial
                        Fichero t = new Fichero();
                        
                        //Fichero al que se accede 'prueba.txt' en modo de 
                        //escritura y lectura.
			t.abrirFichero("prueba.txt","RW");
                        
                        //Tama�o inicial del fichero
                        System.out.println("El tamaño del fichero es " + t.tamano());
                        
                        //Creaci�n de tres bloques de memoria, el primero 
                        //relleno de  caracteres 'A' (c�digo ASCII 61), el 
                        //segundo de caracteres 'B' (c�digo ASCII 62) y el 
                        //tercero de caracteres 'C' (c�digo ASCII 63).
                        
                        byte [] bloque0=new byte[t.TAM_BLOQUE];
                        for(int i=0;i<t.TAM_BLOQUE;i++)bloque0[i]=0x61;
                        byte [] bloque1=new byte[t.TAM_BLOQUE];
                        for(int i=0;i<t.TAM_BLOQUE;i++)bloque1[i]=0x62;
                        byte [] bloque2=new byte[t.TAM_BLOQUE];
                        for(int i=0;i<t.TAM_BLOQUE;i++)bloque2[i]=0x63;
                        
                        //Escritura en el fichero de los bloques
                        t.escribirBloque(bloque0);
                        t.escribirBloque(bloque1);
                        t.escribirBloque(bloque2);
                        
                        //Tama�o del fichero despu�s de la escritura
                        System.out.println("El tamano del fichero es " + t.tamano());
                        
                        //Fijando el puntero a la posici�n inicial
                        t.reset();
                        
                        //Lectura del bloque apuntado (bloque 0)
                        byte[] b = t.leerBloque();
                        for(int i=0;i<t.TAM_BLOQUE;i++){			
				System.out.print((char)b[i]);
                        }                        
                        System.out.println();
			
                        //Salto de un bloque (salto del bloque 1)
                        t.desplazar(1);
                        
                        //Lectura del bloque apuntado (bloque 2)
                        b = t.leerBloque();
                        for(int i=0;i<t.TAM_BLOQUE;i++){			
				System.out.print((char)b[i]);
                        }                        
                        System.out.println();
			
                        //Cierre del fichero
			t.cerrarFichero();
		}catch(Exception e){
			System.out.println(e);
		}
	}
    
}
