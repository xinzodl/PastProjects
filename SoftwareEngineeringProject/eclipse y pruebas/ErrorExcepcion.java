/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class ErrorExcepcion extends Exception {

	private static final long serialVersionUID = 1L;
	
		
	public ErrorExcepcion(){
		this("Algun parametro es incorrecto.");
	}
	
	public ErrorExcepcion(String queEstaMal){
		super(queEstaMal + " Operacion abortada.");
	}
	
}/*FIN DE LA CLASE ErrorExcepcion*/
