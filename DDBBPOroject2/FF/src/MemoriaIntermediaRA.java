import interfazSGF.*;

/**
 *Implementaci�n de una memoria intermedia con pol�tica de liberaci�n aleatoria
 */

public class MemoriaIntermediaRA extends MemoriaIntermedia{
    
        /**
         *Constructor de una memoria intermedia con pol�tica de liberaci�n 
         *aleatoria con un n�mero de p�ginas y tama�o de bloque a definir.
         *@param numeroDePaginas N�mero de p�ginas de la memoria intermedia
         *@param tamannoDeBloque Tama�o de bloque de las p�ginas de la memoria 
         *intermedia
         */
        public MemoriaIntermediaRA(int numeroDePaginas, int tamannoDeBloque){
            super(numeroDePaginas,tamannoDeBloque);
        }
        
        /**
         *M�todo devuelve el identificador de la p�gina a liberar durante 
         *la carga de un bloque de memoria, cuando no quedan p�ginas libres, 
         *siguiendo una pol�tica aleatoria.
         *@param el modo del nuevo bloque que se desea alojar en alguna p�gina 
         *de la memoria intermedia. Puede ser de lectura (false) o 
         *escritura (true).
         *@return el identificador de la p�gina en la que se escribir� el bloque.
         */
	public int liberarPagina(boolean modo) {
                int pagina =(int)Math.floor(Math.random()*this.getNumeroDePaginas());
               // System.out.println("\tMemoriaIntermediaRA>>P�gina a liberar: "+pagina+", �modo de escritura?:"+modo);
		return pagina;
        }
        
        /**
         *M�todo que permite controlar cu�ndo una p�gina es utilizada. 
         *Cada vez que se utiliza una determinada p�gina de la memoria 
         *intermedia. El sistema invoca este m�todo indicando el n�mero de
         *p�gina que se utiliz�.
         *@param i P�gina que ha sido utilizada.
         */
        public void paginaReferenciada(int i){
           // System.out.println("\tMemoriaIntermediaRA>>P�gina referenciada: "+i);
            //Esta memoria no realiza ninguna acci�n cuando se utiliza alguna p�gina.
        }
}
