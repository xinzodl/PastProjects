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
 * Implementa una memoria intermedia para el sistema gestor de ficheros sin pol�tica de liberaci�n definida.
 *Esta clase deber� extenderse para implementar la pol�tica de liberaci�n.
 *  
 */

public abstract class MemoriaIntermedia {
    static VentanaPrincipal ventana=null;
        /**N�mero de p�ginas de la memoria intermedia.*/        
	private int NUMERO_BUFFER = 16;
        /**N�mero de bytes de cada p�gina de memoria.*/
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
         *M�todo que imprime por pantalla el estado de la memoria intermedia
         */
	public final void imprimir(){
		for(int i=0;i<entradas.length;i++){
			System.out.println("\nP�gina "+i+"\n");
            imprimirPagina(i);                
        }
	}
        
        /**
         *M�todo que imprime por pantalla el estado de una p�gina de la memoria intermedia.
         *@param i P�gina de la que se imprimir� el estado por pantalla.
         */
        public final void imprimirPagina(int i){
            byte linea[]=new byte[16];
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Posici�n|\t\t\t\t\t\t\t\tBytes\t\t\t\t\t\t\t\t|ASCII");
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
	 * @param numeroDePaginas N�mero de p�ginas de que constar� la memoria intermedia.
	 * @param tamannoDeBloque N�mero de bytes de cada bloque de la p�gina.
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
	 * Construye un objeto MemoriaIntermedia de 16 p�ginas de 1024 bytes cada una.
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
         *Devuelve el n�mero de p�ginas de la memoria intermedia.
         @return entero que representa el n�mero de p�ginas que maneja la memoria intermedia.*/
        public final int getNumeroDePaginas(){return NUMERO_BUFFER;}
        /**Devuelve el tama�o de los bloques de la memoria intermedia.
         @return entero que representa el tama�o de las p�ginas de la memoria intermedia.*/
        public final int getTamannoDeBloque(){return LONGITUD_BUFFER;}
	/**
	 * Abre un fichero en la memoria intermedia 
	 * @param nombre direcci�n absoluta o relativa del fichero que se va a abrir
	 * @param modo como los permisos de RandomAccessFile: b�sicamente:
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
	 * Hace permanentes los cambios de las p�ginas del FileChannel fc que est�n en memoria 
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
	 * 				  si el numero de bloque excede el tama�o del fichero, el bloque se
	 * 				  devuelve vac�o 
	 * @return un buffer (ByteBuffer) con la informaci�n de ese bloque
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
	 * Libera una p�gina de la memoria utilizando una politica de liberaci�n definida en el m�todo politicaDeLiberacion. 
	 * La p�gina liberada se guarda en su fichero.
	 * @return la posici�n de la meoria liberada
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
         *M�todo que define la pol�tica de liberaci�n que debe aplicarse. 
         * Este m�todo se invoca durante la carga de un bloque de memoria, 
         * cuando no quedan p�ginas libres, para decidir cual de todas las 
         * p�ginas es la que se sobreescribir� con el nuevo bloque.
         *@param modo El modo del nuevo bloque que se desea alojar en la memoria intermedia. Puede modo de lectura (false) o de escritura(true)
         *@return el identificador de la p�gina en la que se escribir� el bloque.
         */
        public abstract int liberarPagina(boolean modo);   
        
        /**
         *M�todo que permite controlar cu�ndo una p�gina es utilizada. Cada vez que se utiliza una determinada p�gina de la memoria intermedia. El sistema invoca este m�todo indicando el n�mero de p�gina que se utiliz�.
         *@param i P�gina que ha sido utilizada.
         */
        public abstract void paginaReferenciada(int i);

	/**
	 * Vacia una p�gina de la memoria y la marca como disponible
	 * @param posBuffer numero de la p�gina
	 */
	private void vaciarPagina(int posBuffer) {
		entradas[posBuffer].canal = null;
		entradas[posBuffer].bloque = -1;
		numOcupadas--;		
	}
	
	/**
	 * Guarda una p�gina de la memoria intermedia en su fichero 
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
	 * directamente. En el caso de que el bloque no est� en memoria hace espacio para el, liberando 
	 * otras p�ginas. Si el bloque supera el tama�o del fichero, se crea un bloque vac�o que al escribirse
	 * adapta el tama�o del fichero. 
	 * @param fc 
	 * @param numBloque n�mero de bloque a traer a memoria
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
			// La entrada no est� en memoria 
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
	 * Devuelve el n�mero de accesos de lectura a disco realizados desde la �ltima vez que 
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
