package interfazSGF;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


/**
 * Implementa una memoria intermedia para el sistema gestor de ficheros sin política de liberación definida.
 *Esta clase deberá extenderse para implementar la política de liberación.
 *  
 */

public abstract class MemoriaIntermedia {
    static VentanaPrincipal ventana=null;
        /**Número de páginas de la memoria intermedia.*/        
	private int NUMERO_BUFFER = 16;
        /**Número de bytes de cada página de memoria.*/
	private int LONGITUD_BUFFER = 1024;
	@SuppressWarnings("unused")
	private static final String R = "r";
	private static final String RW = "rw";
	private static final boolean ESCRITURA = true;
	private static final boolean SOLO_LECTURA = false;	
        
	private class EntradaMemoria {
		FileChannel canal;
		long bloque;
		ByteBuffer buffer;
		
		protected EntradaMemoria() {
			this.canal = null;
			this.bloque = -1;
			this.buffer = ByteBuffer.allocate(LONGITUD_BUFFER);
		}
	}
		
	private EntradaMemoria entradas[];
	private int numOcupadas;        
	private Map<FileChannel,Boolean> infoCanales;    
	private int numAccesos;
        
        /**
         *Método que imprime por pantalla el estado de la memoria intermedia
         */
	public final void imprimir(){
		for(int i=0;i<entradas.length;i++){
			System.out.println("\nPágina "+i+"\n");
            imprimirPagina(i);                
        }
	}
        
        /**
         *Método que imprime por pantalla el estado de una página de la memoria intermedia.
         *@param i Página de la que se imprimirá el estado por pantalla.
         */
        public final void imprimirPagina(int i){
            byte linea[]=new byte[16];
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Posición|\t\t\t\t\t\t\t\tBytes\t\t\t\t\t\t\t\t|ASCII");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("0\t|");
                for(int j=0;j<getTamannoDeBloque();j++){
                    byte b=entradas[i].buffer.get(j);
                    System.out.print(b+"\t");
                    linea[j%linea.length]=b;
                    if((j+1)%linea.length==0){
                        System.out.println("|"+new String(linea)+"|");
                        if((getTamannoDeBloque()-1)>j)System.out.print((j+1)+"\t|");
                    }
                }
                if(getTamannoDeBloque()%linea.length!=0){
                        System.out.print(new String(linea));
                        System.out.println();
                    }
                System.out.println();
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        /**
	 * Construye un objeto MemoriaIntermedia para operar como memoria intermedia en el sistema gestor de ficheros.
	 * @param numeroDePaginas Número de páginas de que constará la memoria intermedia.
	 * @param tamannoDeBloque Número de bytes de cada bloque de la página.
	 */
	public MemoriaIntermedia(int numeroDePaginas, int tamannoDeBloque) {
                NUMERO_BUFFER=numeroDePaginas;
                LONGITUD_BUFFER = tamannoDeBloque;
		entradas = new EntradaMemoria[NUMERO_BUFFER];
		for (int i = 0; i < entradas.length; i++) {
			entradas[i] =  new EntradaMemoria();	
		} 
		numOcupadas = 0;
		this.infoCanales = new HashMap<FileChannel,Boolean>();
		this.resetContadorAccesos(); 
                VentanaPrincipal.memoria=this;
	}
	
        /**
	 * Construye un objeto MemoriaIntermedia de 16 páginas de 1024 bytes cada una.
	 */
        public MemoriaIntermedia() {                
		entradas = new EntradaMemoria[NUMERO_BUFFER];
		for (int i = 0; i < entradas.length; i++) {
			entradas[i] =  new EntradaMemoria();	
		} 
		numOcupadas = 0;
		this.infoCanales = new HashMap<FileChannel,Boolean>();
		this.resetContadorAccesos(); 
                VentanaPrincipal.memoria=this;            
        }
        
        /**
         *Devuelve el número de páginas de la memoria intermedia.
         @return entero que representa el número de páginas que maneja la memoria intermedia.*/
        public final int getNumeroDePaginas(){return NUMERO_BUFFER;}
        /**Devuelve el tamaño de los bloques de la memoria intermedia.
         @return entero que representa el tamaño de las páginas de la memoria intermedia.*/
        public final int getTamannoDeBloque(){return LONGITUD_BUFFER;}
	/**
	 * Abre un fichero en la memoria intermedia 
	 * @param nombre dirección absoluta o relativa del fichero que se va a abrir
	 * @param modo como los permisos de RandomAccessFile: básicamente:
	 * 					r   - abre el fichero para solo lectura 
	 * 					rw  - abre el fichero para lectura/escritura, 
	 * 						  si no existe y los permisos lo permiten lo crea  
	 * @return canal que sirve para referenciar al fichero
	 * @throws IOException cuando el fichero no existe
	 */
	public final FileChannel abrir(String nombre, String modo) 
	throws FileNotFoundException, IllegalArgumentException  {
		RandomAccessFile raf = new RandomAccessFile(nombre, modo);
		FileChannel fc = raf.getChannel();
		if (modo.trim().equals(RW)) {
			this.infoCanales.put(fc,ESCRITURA); 	
		} else {
			this.infoCanales.put(fc,SOLO_LECTURA);
		}
		return fc;
	}

	/**
	 * Hace permanentes los cambios de las páginas del FileChannel fc que estén en memoria 
	 * guardandolas en disco
	 * @param fc fichero 
	 */
	public final void guardar(FileChannel fc) {
		if (fc.isOpen()) {
			for (int i = 0; i < entradas.length; i++) {
				if (entradas[i].canal == fc) {
					try {
						if (this.infoCanales.get(fc) == ESCRITURA) 
							this.guardarPagina(i);	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * Vacia la memoria intermedia de todas los bloques correspondientes al 
	 * FileChannel fc 
	 * @param fc 
	 */
	private void vaciar(FileChannel fc) {
		if (fc.isOpen()) {
			for (int i = 0; i < entradas.length; i++) {
				if (entradas[i].canal == fc) {
				this.vaciarPagina(i);
				}
			}
		}		
	}
	
	/**
	 * Cierra un fichero y hace permanentes los cambios en los buffers 
	 * de ese fichero que se encuentren en la memoria intermedia 
	 * @param fc canal que identifica al fichero
	 */
	public final void cerrar(FileChannel fc) {
		this.guardar(fc);
		this.vaciar(fc);
		try {
			this.infoCanales.remove(fc);
			fc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Trae un bloque del fichero a memoria intermedia 
	 * @param fc canal o fichero del que se va a leer el bloque 
	 * @param numBloque numero de bloque que se quiere leer. El primer bloque es cero. 
	 * 				  si el numero de bloque excede el tamaño del fichero, el bloque se
	 * 				  devuelve vacío 
	 * @return un buffer (ByteBuffer) con la información de ese bloque
	 * @throws IOException
	 */
	private ByteBuffer cargar(FileChannel fc, int numBloque) throws IOException{
		int posLibre = -1;
		if (numOcupadas>=NUMERO_BUFFER) {
			posLibre = this.liberarPagina1(this.infoCanales.get(fc));
		} else {
			for (int i = 0; i < entradas.length; i++) {
				if (entradas[i].canal == null) {
					posLibre = i;
					break;
				}
			}
		}
		if (fc.isOpen()) {
			 int pos = numBloque*LONGITUD_BUFFER;
			 entradas[posLibre].buffer.clear();
			 int estado = fc.read(entradas[posLibre].buffer, pos);
			 if (estado == -1) {
				 // La longitud del fichero es menor que la que se pide
				 // Hacemos que no importe, se actualizara al guardar
				 entradas[posLibre].buffer.put(new byte[LONGITUD_BUFFER]);
			 }
			 entradas[posLibre].canal = fc;
			 entradas[posLibre].bloque = numBloque;
			 numOcupadas++;
			 this.incrementarContadorAccesos();
		}
                paginaReferenciada(posLibre);
		return entradas[posLibre].buffer;
	}
	
	
	/**
	 * Libera una página de la memoria utilizando una politica de liberación definida en el método politicaDeLiberacion. 
	 * La página liberada se guarda en su fichero.
	 * @return la posición de la meoria liberada
	 * @throws IOException
	 */
        private int liberarPagina1(boolean modo) throws IOException {
		int posLibre = -1;
		if (numOcupadas < NUMERO_BUFFER) {
			for (int i = 0; i < this.entradas.length; i++) {
				if ((this.entradas[i].canal == null) && (this.entradas[i].bloque == -1)) { 
					posLibre = i;
				}
			}
			
		} else {
                    posLibre=liberarPagina(modo);
                    this.guardarPagina(posLibre);
                    this.vaciarPagina(posLibre);
		}
		return posLibre;
	}
        
        /**
         *Método que define la política de liberación que debe aplicarse. 
         * Este método se invoca durante la carga de un bloque de memoria, 
         * cuando no quedan páginas libres, para decidir cual de todas las 
         * páginas es la que se sobreescribirá con el nuevo bloque.
         *@param modo El modo del nuevo bloque que se desea alojar en la memoria intermedia. Puede modo de lectura (false) o de escritura(true)
         *@return el identificador de la página en la que se escribirá el bloque.
         */
        public abstract int liberarPagina(boolean modo);   
        
        /**
         *Método que permite controlar cuándo una página es utilizada. Cada vez que se utiliza una determinada página de la memoria intermedia. El sistema invoca este método indicando el número de página que se utilizó.
         *@param i Página que ha sido utilizada.
         */
        public abstract void paginaReferenciada(int i);

	/**
	 * Vacia una página de la memoria y la marca como disponible
	 * @param posBuffer numero de la página
	 */
	private void vaciarPagina(int posBuffer) {
		entradas[posBuffer].canal = null;
		entradas[posBuffer].bloque = -1;
		numOcupadas--;		
	}
	
	/**
	 * Guarda una página de la memoria intermedia en su fichero 
	 * @param posBuffer numero de la pagina
	 */
	private void guardarPagina(int posBuffer) throws IOException {
		if (entradas[posBuffer].canal.isOpen()) {
			long pos = entradas[posBuffer].bloque*LONGITUD_BUFFER;
//			entradas[posBuffer].buffer.flip();
			entradas[posBuffer].buffer.clear();
			if(this.infoCanales.get(entradas[posBuffer].canal))entradas[posBuffer].canal.write(entradas[posBuffer].buffer,pos);
			//  27-02-2006: Comentado para evitar que se cuenten los accesos de solo escritura
			//	this.incrementarContadorAccesos();
		} else {
			throw new IOException();
		}
	}

	/**
	 * Proporciona el bloque de un fichero, si el bloque se encuentra en memoria lo proporciona 
	 * directamente. En el caso de que el bloque no esté en memoria hace espacio para el, liberando 
	 * otras páginas. Si el bloque supera el tamaño del fichero, se crea un bloque vacío que al escribirse
	 * adapta el tamaño del fichero. 
	 * @param fc 
	 * @param numBloque número de bloque a traer a memoria
	 * @return ByteBuffer: bloque de datos 
	 * @throws IOException
	 */
	public final ByteBuffer adquirir(FileChannel fc, int numBloque) throws IOException {
		ByteBuffer buffer = null;
		
		if (numBloque<0) {
			throw new IllegalArgumentException();
		}
		
		if (this.infoCanales.containsKey(fc) == false) {
			throw new IllegalArgumentException();
		}
		
		if (numOcupadas == 0) {
			// lee la entrada del fichero ya que la memoria esta vacia 
			buffer = this.cargar(fc,numBloque);
		} else { 
			// trata de encontrarla en la memoria
			for (int i = 0; i < entradas.length; i++) {
				if ((entradas[i].canal == fc) && (entradas[i].bloque == numBloque)) {
                                        paginaReferenciada(i);
					buffer = entradas[i].buffer;
					break;
				}
			}
			// La entrada no está en memoria 
			if (buffer == null) {
				buffer = this.cargar(fc,numBloque);
			}
		}
		
		return buffer;
	}
	
	/**
	 *  Empieza la nueva cuenta para el contador de accesos de lectura 
	 */
        void resetContadorAccesos() {           
		this.numAccesos = 0;
                if(ventana!=null)ventana.actualizar_accesos(numAccesos);
	}
	
	/**
	 * Devuelve el número de accesos de lectura a disco realizados desde la última vez que 
	 * se ha reiniciado el contador
	 * @return numero de accesos 
	 */
	public final int getContadorAccesos() {
		return this.numAccesos;
	}

	/**
	 * incrementa el contador de accesos de lectura 
	 *
	 */
	private void incrementarContadorAccesos() {
		this.numAccesos++;
                if(ventana!=null)ventana.actualizar_accesos(numAccesos);
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		Set<Entry<FileChannel,Boolean>> openFiles = this.infoCanales.entrySet();
		
		for (Entry<FileChannel, Boolean> entry : openFiles) {
			FileChannel fc = entry.getKey();
			this.cerrar(fc);
		}
		super.finalize();
	}		
}
