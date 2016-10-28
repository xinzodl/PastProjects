package interfazSGF;
import java.io.*;

/**
 * Driver serial para el acceso a ficheros.
 */
public class Fichero {
	
	private RandomAccessFile f;
	private long filesize;
        /**
         *N�mero de bytes de cada bloque.
         */
	public static final int TAM_BLOQUE = 1024;
	
    /**     
     *Abre el fichero en el modo especificado. Apunta al primer bloque.
     *@param file nombre del fichero a abrir.
     *@param modo modo = "R" -> Lectura ;<br/>"W" -> Escritura ;<br/>"RW" -> Lectura escritura
     */
	public void abrirFichero(String file,String modo) throws IOException{
		if(modo.equals("R")){
			f = new RandomAccessFile(file,"r");
			filesize = f.length();
		}
		else if(modo.equals("W")){
			f = new RandomAccessFile(file,"w");
			filesize = f.length();
		}
		else if(modo.equals("RW")){
			f = new RandomAccessFile(file,"rw");
			filesize = f.length();
		}
		else{
			throw new IOException("Modo erroneo");
		}
	}
        
        /**Cierra el fichero (terminan todos los accesos)*/

	public void cerrarFichero() throws IOException{
		f.close();
	}

        /**Lee el bloque apuntado. Despu�s, se apuntar� al siguiente bloque.<br/>
    *Es necesario que el fichero haya sido abierto en modo "R" o en modo "RW".<br/>
    */
	public byte[] leerBloque() throws IOException{
		byte[] bloque = new byte[TAM_BLOQUE];
		f.read(bloque);
		return bloque;
	}
        
        /**
         *Desplaza el apuntador del fichero al comienzo del mismo (apunta al primer bloque).
         */

	public void reset() throws IOException{
		f.seek(0);
	}

        /**
         * Desplaza el apuntador del fichero un n�mero entero de desplazamientos.
            *Si el valor de 'desplazamiento' es negativo, retrocede. Si es positivo, avanza.
 *@param desplazamiento el n�mero de bloques a desplazar el puntero. 
*/
	public void desplazar(long desplazamiento) throws IOException{
		long currPos = f.getFilePointer();
		long newPos = currPos + (desplazamiento*TAM_BLOQUE);
		if(newPos < 0){ newPos = 0;}
		f.seek(newPos);
	}

        /**Escribe un bloque (1024 caracteres) sobre el bloque apuntado.<br/>
Es necesario que el fichero haya sido abierto en modo "W" o en modo "RW".<br/> 
 *@param bloque El array de bytes a escribir
*/
	public void escribirBloque(byte[] bloque) throws IOException{
		f.write(bloque);
	}

        /**Devuelve el tama�o actual del fichero abierto (en bloques).*/
	public long tamano() throws IOException{
		long res = f.length() + (TAM_BLOQUE -1);
		res /= TAM_BLOQUE;
		return res;
	}	
}