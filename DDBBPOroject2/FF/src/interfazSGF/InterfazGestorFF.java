package interfazSGF;
import java.util.*;

/**
 *Interfaz que describe los m�todos que debe implementar el gestor de ficheros.
 */
public interface InterfazGestorFF {
  
  /**
   *Abre el sistema de ficheros a partir del archivo especificado como par�metro.
   *@param file nombre del archivo a abrir.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String abrir_sistema(String file);

  /**
   *Cierra el sistema de ficheros, todos los ficheros de datos, antes de salir. 
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String cerrar_sistema();

  /**
   * Guarda el contenido de la memoria intermedia que se haya modificado.  
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */  
  public String guardar_todo();
  
  /**
   * Obtiene un listado del tipo especificado.
   * @param tipo Entero que identifica el listado a recuperar. Tipos definidos en el enunciado de la pr�ctica.
   *@param lista Vector de Strings en el que se guardar�n los resultados de la invocaci�n del m�todo.
   *Deber�n concatenarse en cada una de estas cadenas de caracteres todos los campos del listado concreto.
   * @return El n�mero de resultados listados.
   */  
  public int listar(int tipo, Vector<String> lista);

  /**
   * M�todo para realizar la b�squeda por acceso invertido.  
   * @param buf_in El buffer que contiene el estado de los registros en la ventana en el momento de pulsar el bot�n (o el item del men�).
   *@param lista Vector de Strings en el que se guardar�n los resultados de la invocaci�n del m�todo. Deber�n concatenarse en cada una de estas cadenas de caracteres todos los campos requeridos en el acceso invertido.
   * @return El n�mero de resultados listados.
   */  
  public int acceso_invertido(BufferDeRegistro buf_in, Vector<String> lista);
  
  /**
   * Lee un fichero con la organizaci�n serial y el dise�o inicial y almacena 
   * los registros en los correspondientes archivos.
   * @param file Nombre completo del archivo desde el que se importa
   */
  public String importar(String file);
  
  /**
   * Compacta el archivo especificado como par�metro.
   *@param archivo el nombre del archivo a compactar.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String compactar(String archivo);


  /**
   * Busca los registros de un archivo concreto que cumplen unas determinadas condiciones especificadas en buf_in y devuelve el primero de ellos en buf_out.
   * @param archivo que contiene el tipo de registros sobre el que se va a buscar 
   * @param buf_in buffer que contiene las condiciones de b�squeda. 
   *         S�lo se deben aplicar las condiciones que afecten al archivo actual.
   * @param buf_out buffer en el que se devuelve el primer resultado de la b�squeda.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */ 
  public String buscar(String archivo, BufferDeRegistro buf_in, BufferDeRegistro buf_out);

  /**
   * Recupera el siguiente registro que cumple el criterio de b�squeda actual
   * En el caso de navegaci�n por registros (Ir a hijo, Ir a hijo virtual) 
   * permite recuperar de forma sucesiva los hijos. 
   *@param buf_out buffer en el que se devuelve el registro siguiente.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
    public String siguiente(BufferDeRegistro buf_out);


  /**
   * Recupera el registro anterior al actual que cumple el criterio de b�squeda actual
   * En el caso de navegaci�n por registros (Ir a hijo, Ir a hijo virtual) 
   * permite recuperar de forma sucesiva los hijos. 
   *@param buf_out buffer en el que se devuelve el registro anterior.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String anterior(BufferDeRegistro buf_out);



  /**
   * Inserta un registro en el archivo que est� seleccionado 
   * @param archivo el nombre del archivo en el que se insertar� el registro.
   *@param buf_in el buffer que contiene el registro a insertar.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String insertar(String archivo, BufferDeRegistro buf_in);


  /**
   * Borra (de forma f�sica) un registro dentro del archivo.
   *@param archivo el nombre del archivo del que se borrar� el registro.
   *@param buf_in el buffer que contiene el registro que debe borrarse.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */ 
  public String borrar(String archivo, BufferDeRegistro buf_in);

	
  /**
   * Actualiza un registro de un archivo.
   *@param archivo el archivo en el que debe actualizarse un registro.
   *@param viejo el registro antiguo que debe actualizarse.
   *@param nuevo el registro tal y como debe quedar despu�s de la actualizaci�n.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */ 

  public String actualizar(String archivo, BufferDeRegistro viejo, BufferDeRegistro nuevo);
  // M�todos obligatorios para recorrer v�nculos


  /**
   * Comienza la navegacion por los hijos del registro padre actual del archivo especificado y muestra el primer hijo.
   * El resto de hijos se muestran mediante el metodo siguiente.
   * @param archivo archivo hijo seleccionado.
   *@param buf_out registro hijo que se devolver�.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */  
  public String ir_a_hijo(String archivo, BufferDeRegistro buf_out);



  /**
   * Muestra el padre del registro actual.
   *@param buf_out registro padre que se devolver�.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String ir_a_padre(BufferDeRegistro buf_out);


  // M�todos opcionales para recorrer v�nculos


  /**
   * Muestra el padre virtual del archivo especificado.
   * @param archivo nombre del archivo padre virtual seleccionado.
   *@param buf_out registro padre que se devolver�.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String ir_a_padre_virtual(String archivo, BufferDeRegistro buf_out);

  /**
   * Comienza la navegacion a los hijos virtuales del registro padre actual y muestra el primer hijo virtual 
   * El resto de hijos se muestran mediante el metodo siguiente
   * @param archivo archivo hijo virtual seleccionado.
   *@param buf_out hijo virtual que se devolver�.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */  

  public String ir_a_hijo_virtual(String archivo, BufferDeRegistro buf_out);


  /**
   * B�squeda con el propio lenguaje de consulta: Por ejemplo utilizando OR/NOT 
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */ 

  public String busqueda_avanzada(String consulta, BufferDeRegistro buf_in, BufferDeRegistro buf_out);


  /**
   * Reorganiza el �rea de almacenamiento direccionado asignando un nuevo tama�o o cambiando la 
   * clave de direccionamiento.
   * @param area archivo o area de direccionamiento que se va a reorganizar
   * @param tamano nuevo tama�o en cubos 
   * @param buf_in Buffer de entrada, para operaciones de cambio de Clave de Direccionamiento  
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String reorganizar(String area,String tamano, BufferDeRegistro buf_in);  

  /**
   *Crea un nuevo �ndice.
   *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
   */
  public String crear_indice(String clave,String tipo, BufferDeRegistro buf_in);
/**
 *Destruye el �ndice especificado.
  *@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
          */
  public String destruir_indice(String clave,BufferDeRegistro buf_in);
/**
 *Reorganiza un �ndice.
 **@return Devuelve una cadena de caracteres que se mostrar� en la parte inferior de la ventana de interfaz como resultado de la ejecuci�n de este m�todo.
 */
  public String reorganizar_indice(String clave, BufferDeRegistro buf_in);
}
