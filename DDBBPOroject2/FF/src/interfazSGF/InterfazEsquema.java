   package interfazSGF;

   /**Interfaz que debe implementarse para representar un esquema de ficheros jer�rquico */
   public interface InterfazEsquema {
   
   /** 
    * Devuelve los nombres de los Archivos L�gicos.
    *@return array de Strings con los nombres de cada uno de los archivos de la jerarqu�a.
    */
   public String[] archivos();
   
   /** 
    * Devuvelve los identificadores de los Hijos de cada Archivo.
    *@return array de array de enteros que contiene, en la posici�n de cada uno de los archivos, la lista de identificadores de sus archivos hijos.
    */
   public int [][] hijos();
   /** 
    * Devuelve los identificadores de los Hijos Virtuales de cada Archivo.
    *@return array de array de enteros que contiene, en la posici�n de cada uno de los archivos, la lista de identificadores de sus archivos hijos virtuales.
    */
   public int [][] hijos_virtuales();      
   }