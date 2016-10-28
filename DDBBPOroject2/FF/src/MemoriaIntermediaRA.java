import interfazSGF.*;

/**
 *Implementación de una memoria intermedia con política de liberación aleatoria
 */

public class MemoriaIntermediaRA extends MemoriaIntermedia{
    
        /**
         *Constructor de una memoria intermedia con política de liberación 
         *aleatoria con un número de páginas y tamaño de bloque a definir.
         *@param numeroDePaginas Número de páginas de la memoria intermedia
         *@param tamannoDeBloque Tamaño de bloque de las páginas de la memoria 
         *intermedia
         */
        public MemoriaIntermediaRA(int numeroDePaginas, int tamannoDeBloque){
            super(numeroDePaginas,tamannoDeBloque);
        }
        
        /**
         *Método devuelve el identificador de la página a liberar durante 
         *la carga de un bloque de memoria, cuando no quedan páginas libres, 
         *siguiendo una política aleatoria.
         *@param el modo del nuevo bloque que se desea alojar en alguna página 
         *de la memoria intermedia. Puede ser de lectura (false) o 
         *escritura (true).
         *@return el identificador de la página en la que se escribirá el bloque.
         */
	public int liberarPagina(boolean modo) {
                int pagina =(int)Math.floor(Math.random()*this.getNumeroDePaginas());
               // System.out.println("\tMemoriaIntermediaRA>>Página a liberar: "+pagina+", ¿modo de escritura?:"+modo);
		return pagina;
        }
        
        /**
         *Método que permite controlar cuándo una página es utilizada. 
         *Cada vez que se utiliza una determinada página de la memoria 
         *intermedia. El sistema invoca este método indicando el número de
         *página que se utilizó.
         *@param i Página que ha sido utilizada.
         */
        public void paginaReferenciada(int i){
           // System.out.println("\tMemoriaIntermediaRA>>Página referenciada: "+i);
            //Esta memoria no realiza ninguna acción cuando se utiliza alguna página.
        }
}
