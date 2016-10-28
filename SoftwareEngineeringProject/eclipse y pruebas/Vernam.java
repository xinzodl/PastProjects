/*
	AUTORES:	Alvaro Gomez Ramos 100307009
				Carlos Contreras Sanz 100303592
				Pincipios de Desarrollo de Software
				UC3M 2013/2014
*/
package cifrador;

public class Vernam {
	
	protected String cifra_vernam (String texto, String clave){
	String devolver = "";
	char aux;
	
	for (int ii=0; ii<texto.length();ii++){
		aux = (char)(texto.charAt(ii)^clave.charAt(ii));
		devolver = devolver + aux;
	}

	return devolver;
}
	
	
	
	
	
	protected String descifra_vernam (String texto, String clave){
		String devolver = "";
		char aux;
		
		for (int ii=0; ii<texto.length();ii++){
			aux = (char)(texto.charAt(ii)^clave.charAt(ii));
			devolver = devolver + aux;
		}

		return devolver;
	}/*FIN de metodo descifra_vernam*/
	
	
	
}/*FIN DE LA CLASE VERNAM*/
