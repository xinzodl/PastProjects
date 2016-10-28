package interfazSGF;
import java.util.*;

/**
 *Interfaz que describe los métodos que debe implementar el gestor de ficheros.
 */
public interface InterfazGestorFF {
  
  /**
   *Abre el sistema de ficheros a partir del archivo especificado como parámetro.
   *@param file nombre del archivo a abrir.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String abrir_sistema(String file);

  /**
   *Cierra el sistema de ficheros, todos los ficheros de datos, antes de salir. 
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String cerrar_sistema();

  /**
   * Guarda el contenido de la memoria intermedia que se haya modificado.  
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */  
  public String guardar_todo();
  
  /**
   * Obtiene un listado del tipo especificado.
   * @param tipo Entero que identifica el listado a recuperar. Tipos definidos en el enunciado de la práctica.
   *@param lista Vector de Strings en el que se guardarán los resultados de la invocación del método.
   *Deberán concatenarse en cada una de estas cadenas de caracteres todos los campos del listado concreto.
   * @return El número de resultados listados.
   */  
  public int listar(int tipo, Vector<String> lista);

  /**
   * Método para realizar la búsqueda por acceso invertido.  
   * @param buf_in El buffer que contiene el estado de los registros en la ventana en el momento de pulsar el botón (o el item del menú).
   *@param lista Vector de Strings en el que se guardarán los resultados de la invocación del método. Deberán concatenarse en cada una de estas cadenas de caracteres todos los campos requeridos en el acceso invertido.
   * @return El número de resultados listados.
   */  
  public int acceso_invertido(BufferDeRegistro buf_in, Vector<String> lista);
  
  /**
   * Lee un fichero con la organización serial y el diseño inicial y almacena 
   * los registros en los correspondientes archivos.
   * @param file Nombre completo del archivo desde el que se importa
   */
  public String importar(String file);
  
  /**
   * Compacta el archivo especificado como parámetro.
   *@param archivo el nombre del archivo a compactar.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String compactar(String archivo);


  /**
   * Busca los registros de un archivo concreto que cumplen unas determinadas condiciones especificadas en buf_in y devuelve el primero de ellos en buf_out.
   * @param archivo que contiene el tipo de registros sobre el que se va a buscar 
   * @param buf_in buffer que contiene las condiciones de búsqueda. 
   *         Sólo se deben aplicar las condiciones que afecten al archivo actual.
   * @param buf_out buffer en el que se devuelve el primer resultado de la búsqueda.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */ 
  public String buscar(String archivo, BufferDeRegistro buf_in, BufferDeRegistro buf_out);

  /**
   * Recupera el siguiente registro que cumple el criterio de búsqueda actual
   * En el caso de navegación por registros (Ir a hijo, Ir a hijo virtual) 
   * permite recuperar de forma sucesiva los hijos. 
   *@param buf_out buffer en el que se devuelve el registro siguiente.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
    public String siguiente(BufferDeRegistro buf_out);


  /**
   * Recupera el registro anterior al actual que cumple el criterio de búsqueda actual
   * En el caso de navegación por registros (Ir a hijo, Ir a hijo virtual) 
   * permite recuperar de forma sucesiva los hijos. 
   *@param buf_out buffer en el que se devuelve el registro anterior.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String anterior(BufferDeRegistro buf_out);



  /**
   * Inserta un registro en el archivo que está seleccionado 
   * @param archivo el nombre del archivo en el que se insertará el registro.
   *@param buf_in el buffer que contiene el registro a insertar.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String insertar(String archivo, BufferDeRegistro buf_in);


  /**
   * Borra (de forma física) un registro dentro del archivo.
   *@param archivo el nombre del archivo del que se borrará el registro.
   *@param buf_in el buffer que contiene el registro que debe borrarse.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */ 
  public String borrar(String archivo, BufferDeRegistro buf_in);

	
  /**
   * Actualiza un registro de un archivo.
   *@param archivo el archivo en el que debe actualizarse un registro.
   *@param viejo el registro antiguo que debe actualizarse.
   *@param nuevo el registro tal y como debe quedar después de la actualización.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */ 

  public String actualizar(String archivo, BufferDeRegistro viejo, BufferDeRegistro nuevo);
  // Métodos obligatorios para recorrer vínculos


  /**
   * Comienza la navegacion por los hijos del registro padre actual del archivo especificado y muestra el primer hijo.
   * El resto de hijos se muestran mediante el metodo siguiente.
   * @param archivo archivo hijo seleccionado.
   *@param buf_out registro hijo que se devolverá.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */  
  public String ir_a_hijo(String archivo, BufferDeRegistro buf_out);



  /**
   * Muestra el padre del registro actual.
   *@param buf_out registro padre que se devolverá.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String ir_a_padre(BufferDeRegistro buf_out);


  // Métodos opcionales para recorrer vínculos


  /**
   * Muestra el padre virtual del archivo especificado.
   * @param archivo nombre del archivo padre virtual seleccionado.
   *@param buf_out registro padre que se devolverá.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String ir_a_padre_virtual(String archivo, BufferDeRegistro buf_out);

  /**
   * Comienza la navegacion a los hijos virtuales del registro padre actual y muestra el primer hijo virtual 
   * El resto de hijos se muestran mediante el metodo siguiente
   * @param archivo archivo hijo virtual seleccionado.
   *@param buf_out hijo virtual que se devolverá.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */  

  public String ir_a_hijo_virtual(String archivo, BufferDeRegistro buf_out);


  /**
   * Búsqueda con el propio lenguaje de consulta: Por ejemplo utilizando OR/NOT 
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */ 

  public String busqueda_avanzada(String consulta, BufferDeRegistro buf_in, BufferDeRegistro buf_out);


  /**
   * Reorganiza el área de almacenamiento direccionado asignando un nuevo tamaño o cambiando la 
   * clave de direccionamiento.
   * @param area archivo o area de direccionamiento que se va a reorganizar
   * @param tamano nuevo tamaño en cubos 
   * @param buf_in Buffer de entrada, para operaciones de cambio de Clave de Direccionamiento  
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String reorganizar(String area,String tamano, BufferDeRegistro buf_in);  

  /**
   *Crea un nuevo índice.
   *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
   */
  public String crear_indice(String clave,String tipo, BufferDeRegistro buf_in);
/**
 *Destruye el índice especificado.
  *@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
          */
  public String destruir_indice(String clave,BufferDeRegistro buf_in);
/**
 *Reorganiza un índice.
 **@return Devuelve una cadena de caracteres que se mostrará en la parte inferior de la ventana de interfaz como resultado de la ejecución de este método.
 */
  public String reorganizar_indice(String clave, BufferDeRegistro buf_in);
}
