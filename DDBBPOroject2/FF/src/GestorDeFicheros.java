import interfazSGF.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GestorDeFicheros implements InterfazGestorFF{        
    
	public byte [] buffer;
	public int puntero = 0;
	public Fichero fich = new Fichero();
	public BufferDeRegistro buffreg=new BufferDeRegistro();
	public MemoriaIntermediaRA memoria = new MemoriaIntermediaRA(16,1024);
	public FileChannel returnmem;
	public ByteBuffer buffermem;
	public int punteroSerial=0;
	public int N=500;
	public int punteroDesbordamiento=500;
	public int punteroDecode=0;
	public int punteroCubo;/*Recorre cubos*/
	public int punteroByte;/*Recorre bytes del cubo*/
	public BufferDeRegistro buffregOptimo=new BufferDeRegistro();
	public BufferDeRegistro condicion=new BufferDeRegistro();
	public BufferDeRegistro comparar=new BufferDeRegistro();
	public boolean [] conjuntoResultado;
	public boolean [] conjuntoResultadoAux;
	public int MAX_Cubos=0;
	
	public int punteroCuboDir;
	
    public GestorDeFicheros() {        
    }
    
    /* Abre un sistema de ficheros de tipo jerarquico a partir del ficheros de 
     * datos de nombre 'nombre_de_archivo'. 	
     */
    public String abrir_sistema(String nombre_de_archivo) {
    	
    	try {
			returnmem=memoria.abrir(nombre_de_archivo, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	//fich.abrirFichero(nombre_de_archivo, "R");
    	//importar(nombre_de_archivo);*/
    	
    	return(" Terminado metodo abrir_sistema " + nombre_de_archivo + " (no implementado)");
    }
    
    /* Cierra el sistema de ficheros, todos los ficheros de datos, antes de salir */
    public String cerrar_sistema() {
    	memoria.guardar(returnmem);
        return(" Terminado metodo cerrar_sistema (no implementado)");
    }
    
    /* Guarda el contenido de la memoria intermedia que se haya modificado
     * Obligatorio
     */
    public String guardar_todo() {
    	memoria.guardar(returnmem);
        return("Terminado metodo guardar_todo (no implementado)");
    }
    
    /* Obtiene un listado del tipo especificado. Cada uno de los resultados 
     * obtenidos sera añadido al vector 'lista' como una cadena de caracteres 
     * que contenga concatenados todos los campos que se requieran.
     * @param tipo Indica el tipo de listado a realizar. Puede ser:
     * tipo = 0: Catalogo de personajes, que debera incluir toda la informacion
     *      relativa a un personaje (ordenados alfabeticamente).
     * tipo = 1: Cata½logo de episodios, que debera incluir toda la informacion
     *      de los episodios del juego (ordenados alfabeticamente).
     * @return El numero de resultados encontrados.      
     */
    public int listar(int tipo, Vector<String> lista) {      
        return 0;//No hay listado, 0 filas.
    }

    /* Metodo para realizar la busqueda por acceso invertido. Cada uno de los 
     * resultados obtenidos sera añadido al vector 'lista' como una cadena 
     * de caracteres que contenga concatenados todos los campos que se requieran. 
     * @param buf_in El buffer que contiene el estado de los registros en la 
     * ventana en el momento de pulsar el boton (o el item del menu).
     * @return El numero de resultados encontrados.
     */
    public int acceso_invertido(BufferDeRegistro buf_in, Vector<String> lista) {      
        return 0;//No hay resultados, 0 filas.
    }
    
    /* Lee un fichero con la organizacion serial y el diseño inicial y almacena 
     * los registros en los correspondientes archivos.
     * @param: String file: Nombre completo del archivo desde el que se importa
     */
    public String importar(String file1) {
    	try {
			fich.abrirFichero(file1, "R");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			buffer = fich.leerBloque();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.buffreg=leerRegistro();
    	//System.out.println(RLtoString(buffreg));
    	
    	
        return("Terminado metodo importar " + file1+" (no implementado)");
    }
    
    
    /*DISPERSION MULTICLAVE*/
   public int transformadaNombre(String nombre){
	   int acumulado=0;
   		for(int i=0;i<nombre.trim().length();i++){
   			acumulado=acumulado+nombre.charAt(i);
   		}
   		return acumulado%2;
   }
   
   public int transformadaApellido1(String ap1){
	   int acumulado=0;
   		for(int i=0;i<ap1.trim().length();i++){
   			acumulado=acumulado+ap1.charAt(i);
   		}
   		return acumulado%32;
   }
    
   public int transformadaApellido2(String ap2){
	   int acumulado=0;
   		for(int i=0;i<ap2.trim().length();i++){
   			acumulado=acumulado+ap2.charAt(i);
   		}
   		return acumulado%8;
   }
   
   public int Transformada(int trans_nom, int trans_ap1, int trans_ap2){
	   return (trans_nom+(2*trans_ap2)+(16*trans_ap1))%500;
   }
    
    
    
    public int funcionDispersion(String recorrer){
    	int acumulado=0;
    	for(int i=0;i<recorrer.trim().length();i++){
    		acumulado=acumulado+recorrer.charAt(i);
    	}
    	return acumulado%this.N;
    }
    
    
    public byte leerByte(){
    	
    	byte b = this.buffer[puntero];
    	
    	if(this.puntero==1023){
    		try {
				this.buffer = fich.leerBloque();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		this.puntero=-1;
    	}
    	this.puntero++;
    	return b;
    }
    
    public BufferDeRegistro leerRegistro(){
    	BufferDeRegistro buffreg=new BufferDeRegistro();
    
    	buffreg.setCampo("CID", leerCadena(12));
    	leerByte();
    	buffreg.setCampo("Nombre", leerCadena(35));
    	buffreg.setCampo("Apellido1", leerCadena(30));
    	buffreg.setCampo("Apellido2", leerCadena(30));
    	leerByte();
    	buffreg.setCampo("Apodo", leerCadena(25));
    	leerByte();
    	buffreg.setCampo("Sexo", leerCadena(5));
    	leerByte();
    	buffreg.setCampo("Fecha nacimiento", leerCadena(10));
    	leerByte();
    	buffreg.setCampo("Nacionalidad", leerCadena(45));
    	leerByte();
    	//Fichas 1
    	buffreg.setCampo("Deporte F1", leerCadena(15));
    	buffreg.setCampo("Categoria F1", leerCadena(15));
    	buffreg.setCampo("Nº federado F1", leerCadena(5));
    	leerByte();
    	//Fichas 2
    	buffreg.setCampo("Deporte F2", leerCadena(15));
    	buffreg.setCampo("Categoria F2", leerCadena(15));
    	buffreg.setCampo("Nº federado F2", leerCadena(5));
    	leerByte();
    	//Fichas 3
    	buffreg.setCampo("Deporte F3", leerCadena(15));
    	buffreg.setCampo("Categoria F3", leerCadena(15));
    	buffreg.setCampo("Nº federado F3", leerCadena(5));
    	leerByte();
    	//Fichas 4
    	buffreg.setCampo("Deporte F4", leerCadena(15));
    	buffreg.setCampo("Categoria F4", leerCadena(15));
    	buffreg.setCampo("Nº federado F4", leerCadena(5));
    	leerByte();
    	//Fichas 5
    	buffreg.setCampo("Deporte F5", leerCadena(15));
    	buffreg.setCampo("Categoria F5", leerCadena(15));
    	buffreg.setCampo("Nº federado F5", leerCadena(5));
    	leerByte();
    	leerByte();
    	
    	return buffreg;
    }
    
    public String leerCadena(int tamaño){
    	String cadena="";
    	byte aux;
    	char aux1;
    	
    	for(int i=0;i<tamaño;i++){
    		aux=leerByte();
    		aux1=(char)aux;
    		cadena=cadena+String.valueOf(aux1);
    	}
    	return cadena;
    }
    
    
    public String RLtoString(BufferDeRegistro buf_in){
    	
    	/*CID*/
    	String regoptimo=buf_in.getCampo("CID");
    	
    	/*M.L.Nombre + Nombre*/
    	String nom=buf_in.getCampo("Nombre").trim();/*Obtenemos el Nombre omitiendo los espacios de la derecha*/
    	regoptimo=regoptimo+(char)nom.length()+nom;
    	
    	/*M.L.Apellido1 + Apellido1*/
    	String ap1=buf_in.getCampo("Apellido1").trim();/*Obtenemos el Apellido1 omitiendo los espacios de la derecha*/
    	regoptimo=regoptimo+(char)ap1.length()+ap1;
    	
    	/*M.L.Apellido2 + Apellido2*///(Si M.L.Ap2 == 0 entonces no existe)
    	String ap2=buf_in.getCampo("Apellido2").trim();/*Obtenemos el Apellido1 omitiendo los espacios de la derecha*/
    	if(ap2.length()==0){
    		regoptimo=regoptimo+(char)ap2.length();
    	}else{
    		regoptimo=regoptimo+(char)ap2.length()+ap2;
    	}
    	
    	/*M.L.Apodo + Apodo*///(Si M.L.Apo == 0 entonces no existe)
    	String apo=buf_in.getCampo("Apodo").trim();/*Obtenemos el Apellido1 omitiendo los espacios de la derecha*/
    	if(apo.length()==0){
    		regoptimo=regoptimo+(char)apo.length();
    	}else{
    		regoptimo=regoptimo+(char)apo.length()+apo;
    	}
    	
    	/*Sexo*///(Varon = 1, Mujer = 0)
    	String sex=buf_in.getCampo("Sexo").trim();/*Obtenemos el Apellido1 omitiendo los espacios de la derecha*/
    	if(sex.equalsIgnoreCase("mujer")){
    		regoptimo=regoptimo+0;
    	}else{
    		regoptimo=regoptimo+1;
    	}
    	
    	/*Fecha*/
    	String fech=buf_in.getCampo("Fecha nacimiento").trim();
    	regoptimo=regoptimo+fech.charAt(0)+fech.charAt(1)+fech.charAt(2)+fech.charAt(3)+fech.charAt(5)+fech.charAt(6)+fech.charAt(8)+fech.charAt(9);
    	
    	/*String año=""+fech.charAt(0)+fech.charAt(1)+fech.charAt(2)+fech.charAt(3);
    	int num_año=Integer.valueOf(año);
    	String mes=""+fech.charAt(5)+fech.charAt(6);
    	String dia=""+fech.charAt(8)+fech.charAt(9);
    	int num_mes=Integer.valueOf(mes);
    	int num_dia=Integer.valueOf(dia);
    	regoptimo=regoptimo+codificarBase256_2By(num_año)+(char)num_mes+(char)num_dia;*/
    	
    	/*M.L.Pais + Pais*/
    	String pai=buf_in.getCampo("Nacionalidad").trim();/*Obtenemos el Apellido1 omitiendo los espacios de la derecha*/
    	regoptimo=regoptimo+(char)pai.length()+pai;
    	
    	/*Fichas*/
    	int repeticiones=0;
    	int aux=1;
    	for(int i=0;i<5;i++){
    		if(buf_in.getCampo("Deporte F"+aux).trim().length()!=0){
    			repeticiones++;
    		}
    		aux++;
    	}
    	/*M.Repeticion Fichas*/
    	regoptimo=regoptimo+repeticiones;
    	
    	if(repeticiones!=0){
    		aux=1;
    		while(repeticiones>0){
    		
    			/*M.L.Deporte + Deporte*/
    			String dep=buf_in.getCampo("Deporte F"+aux).trim();/*Obtenemos el Nombre omitiendo los espacios de la derecha*/
    			regoptimo=regoptimo+(char)dep.length()+dep;
        	
    			/*M.L.Categoria + categoria*/
    			String cat=buf_in.getCampo("Categoria F"+aux).trim();/*Obtenemos el Nombre omitiendo los espacios de la derecha*/
    			regoptimo=regoptimo+(char)cat.length()+cat;
        	
    			/*Num federado*/
    			String num=buf_in.getCampo("Nº federado F"+aux).trim();
    			regoptimo=regoptimo+codificarBase256_3By(num);
        	
    			repeticiones--;
    			aux++;
    		}	
    	
    	}
    	return regoptimo;
    }
    
    
    
    
    /*Metodo que codifica un numero de base 10 a base 256, Cuando se desea de codificar con 3 Bytes*/
    public String codificarBase256_3By(String num){
    	int a=Integer.parseInt(num);
    	String sol="";
    	
    	int byte1=0;
		int byte2=0;
		int byte3=0;
		
		byte3=a%256;
		byte2=a/256;
		
		if(byte2>=256){
			byte1=byte2/256;
			byte2=byte2%256;
		}
		
		char b1=(char)byte1;
		char b2=(char)byte2;
		char b3=(char)byte3;
		
		sol=sol+b1+b2+b3;
    	return sol;
    }
    
    /*Metodo que decodifica un numero de base 256 a base 10, Cuando se desea de decodificar 3 Bytes*/
    public String decodificarBase256_3By(String num){
		String sol="";
		
		int byte1=(int)num.charAt(0);
		int byte2=(int)num.charAt(1);
		int byte3=(int)num.charAt(2);
		
		int decod=byte1*(256*256)+byte2*256+byte3;
		
		sol=sol+decod;
		return sol;
	}
    
    
    /*Metodo que decodifica un numero de base 256 a base 10, Cuando se desea de decodificar 2 Bytes*/
    public String codificarBase256_2By(int num){
    	//System.out.println("Numero a codificar: "+num);
    	int a=num;
    	String sol="";
    	
		int byte1=0;
		int byte2=0;
		
		byte2=a%256;
		byte1=a/256;
		
		
		
		char b1=(char)byte1;
		char b2=(char)byte2;
		
		sol=sol+b1+b2;
    	return sol;
    }
    
    /*Metodo que decodifica un numero de base 256 a base 10, Cuando se desea de decodificar 2 Bytes*/
    public int decodificarBase256_2By(String num){
		int sol=0;
		
		int byte1=(int)num.charAt(0);
		int byte2=(int)num.charAt(1);
		
		int decod=byte1*(256)+byte2;
		
		sol=sol+decod;
		//System.out.println("PPL decodificada: "+sol);
		return sol;
	}
    
    /*Metodo que Codifica una fecha con formato AAAA-MM-DD*/
    public String codificarFecha(String fech){
    	String año=""+fech.charAt(0)+fech.charAt(1)+fech.charAt(2)+fech.charAt(3);
    	String mes=""+fech.charAt(5)+fech.charAt(6);
    	String dia=""+fech.charAt(8)+fech.charAt(9);
    	
    	int num_año=Integer.valueOf(año);
    	
    	int num_mes=Integer.valueOf(mes);
    	int num_dia=Integer.valueOf(dia);
    	return codificarBase256_2By(num_año)+""+(char)num_mes+""+(char)num_dia;
	}
    
    /*Metodo que decodifica una fecha devolviendola con el formato AAAA-MM-DD*/
    public String deCodificarFecha(String fech){
		String smes="";
		String sdia="";
		
    	String año=""+fech.charAt(0)+fech.charAt(1);
    	String mes=""+fech.charAt(2);
    	String dia=""+fech.charAt(3);
    	
    	int num_año=decodificarBase256_2By(año);
    	int num_mes=Integer.valueOf(mes);
    	int num_dia=Integer.valueOf(dia);
    	
    	if(num_mes<10){
    		smes=0+""+num_mes;
    	}else{
    		smes=smes+num_mes;
    	}
    	if(num_dia<10){
    		sdia=0+""+num_dia;
    	}else{
    		sdia=sdia+num_dia;
    	}
    	return num_año+"-"+smes+"-"+sdia;
	}
    
    
    
    
    
    
    /*Informacion de Control del Cubo*/
     public int leerPPL(int num_cubo){
    	try {
			buffermem=memoria.adquirir(returnmem, num_cubo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int ppl=0;
    	
    	//byte byte1=buffermem.array()[1022];
   
    	//System.out.println(buffermem.array().length);
    	//char byte1=(char)buffermem.getChar(1021);
    	int byte1=buffermem.array()[1022];
    	int byte2=buffermem.array()[1023];
    	if(byte2<0){
    		byte2=byte2+256;
    	}
    	ppl=byte1*256+byte2;
    	
    	return ppl;
    }
     
     public void escribirPPL(int num_cubo, int ppl){
    	 int aux=ppl;
    	 
    	 String pplcoded=codificarBase256_2By(aux);
    	 
    	 try {
 			buffermem=memoria.adquirir(returnmem, num_cubo);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    	 
    	 buffermem.put(1022, (byte)pplcoded.charAt(0));
    	 buffermem.put(1023, (byte)pplcoded.charAt(1));
    	
     }
    
    
     
     /*Metodos auxiliares para la insercion*/
     public void escribirEnBloque(String reg, int num_bloque, int posicion){
    	 try {
  			buffermem=memoria.adquirir(returnmem, num_bloque);
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
    	 for(int i=0;i<reg.length();i++, posicion++){
    		 //System.out.println(posicion);
    		 buffermem.put(posicion, (byte)reg.charAt(i));
    	 }
     }
     
     public boolean escribirEnCubo(int num_cubo, String reg){
    	 int ppl=leerPPL(num_cubo);
    	
    	 if(reg.length()>(1022-ppl)){
    		 return false;
    	 }
    	 else{
    		
    		 escribirEnBloque(reg, num_cubo,ppl);
    		 escribirPPL(num_cubo, (ppl+reg.length()));
    		 return true;
    	 }
     }
    
    
    /* Ejecuta las operaciones necesarias para compactar el archivo 
     * especificado
     */
    public String compactar(String archivo){
        return("terminado metodo compactar " + archivo +" (no implementado)");
    }
    
    
    
    public boolean Encaja(BufferDeRegistro condicion, BufferDeRegistro candidato){
    	boolean solucion;
    	
    	/*Comprobamos el Nombre*/
    	boolean nombre=false;
    	if(condicion.getCampo("Nombre").trim().equalsIgnoreCase(candidato.getCampo("Nombre").trim()) || condicion.getCampo("Nombre").trim().length()==0){
    		nombre=true;
    	}
    	
    	/*Comprobamos el Apellido 1*/
    	boolean apellido1=false;
    	if(condicion.getCampo("Apellido1").trim().equalsIgnoreCase(candidato.getCampo("Apellido1").trim()) || condicion.getCampo("Apellido1").trim().length()==0){
    		apellido1=true;
    	}
    	
    	/*Comprobamos el Apellido 2*/
    	boolean apellido2=false;
    	if(condicion.getCampo("Apellido2").trim().equalsIgnoreCase(candidato.getCampo("Apellido2").trim()) || condicion.getCampo("Apellido2").trim().length()==0){
    		apellido2=true;
    	}
    	
    	/*Comprobamos el CID*/
    	boolean cid=false;
    	if(condicion.getCampo("CID").trim().equalsIgnoreCase(candidato.getCampo("CID").trim()) || condicion.getCampo("CID").trim().length()==0){
    		cid=true;
    	}
    	
    	/*Comprobamos el Sexo*/
    	boolean sexo=false;
    	if(condicion.getCampo("Sexo").trim().equalsIgnoreCase(candidato.getCampo("Sexo").trim()) || condicion.getCampo("Sexo").trim().length()==0){
    		sexo=true;
    	}
    	
    	/*Comprobamos la Fecha nacimiento*/
    	boolean fecha=false;
    	if(condicion.getCampo("Fecha nacimiento").trim().equalsIgnoreCase(candidato.getCampo("Fecha nacimiento").trim()) || condicion.getCampo("Fecha nacimiento").trim().length()==0){
    		fecha=true;
    	}
    	
    	/*Comprobamos el Apodo*/
    	boolean apodo=false;
    	if(condicion.getCampo("Apodo").trim().equalsIgnoreCase(candidato.getCampo("Apodo").trim()) || condicion.getCampo("Apodo").trim().length()==0){
    		apodo=true;
    	}
    	
    	/*Comprobamos la Nacionalidad*/
    	boolean pais=false;
    	if(condicion.getCampo("Nacionalidad").trim().equalsIgnoreCase(candidato.getCampo("Nacionalidad").trim()) || condicion.getCampo("Nacionalidad").trim().length()==0){
    		pais=true;
    	}
    	
    	//FICHAS
    	//Ficha 1
    	/*Comprobamos el Deporte F1*/
    	boolean df1=false;
    	if(condicion.getCampo("Deporte F1").trim().equalsIgnoreCase(candidato.getCampo("Deporte F1").trim()) || condicion.getCampo("Deporte F1").trim().length()==0){
    		df1=true;
    	}
    	
    	/*Comprobamos el Nº federado F1*/
    	boolean nf1=false;
    	if(condicion.getCampo("Nº federado F1").trim().equalsIgnoreCase(candidato.getCampo("Nº federado F1").trim()) || condicion.getCampo("Nº federado F1").trim().length()==0){
    		nf1=true;
    	}
    	
    	/*Comprobamos el Nº federado F1*/
    	boolean cf1=false;
    	if(condicion.getCampo("Categoria F1").trim().equalsIgnoreCase(candidato.getCampo("Categoria F1").trim()) || condicion.getCampo("Categoria F1").trim().length()==0){
    		cf1=true;
    	}
    	
    	//Ficha 2
    	/*Comprobamos el Deporte F2*/
    	boolean df2=false;
    	if(condicion.getCampo("Deporte F2").trim().equalsIgnoreCase(candidato.getCampo("Deporte F2").trim()) || condicion.getCampo("Deporte F2").trim().length()==0){
    		df2=true;
    	}
    	
    	/*Comprobamos el Nº federado F2*/
    	boolean nf2=false;
    	if(condicion.getCampo("Nº federado F2").trim().equalsIgnoreCase(candidato.getCampo("Nº federado F2").trim()) || condicion.getCampo("Nº federado F2").trim().length()==0){
    		nf2=true;
    	}
    	
    	/*Comprobamos el Nº federado F2*/
    	boolean cf2=false;
    	if(condicion.getCampo("Categoria F2").trim().equalsIgnoreCase(candidato.getCampo("Categoria F2").trim()) || condicion.getCampo("Categoria F2").trim().length()==0){
    		cf2=true;
    	}
    	
    	//Ficha 3
    	/*Comprobamos el Deporte F3*/
    	boolean df3=false;
    	if(condicion.getCampo("Deporte F3").trim().equalsIgnoreCase(candidato.getCampo("Deporte F3").trim()) || condicion.getCampo("Deporte F3").trim().length()==0){
    		df3=true;
    	}
    	
    	/*Comprobamos el Nº federado F3*/
    	boolean nf3=false;
    	if(condicion.getCampo("Nº federado F3").trim().equalsIgnoreCase(candidato.getCampo("Nº federado F3").trim()) || condicion.getCampo("Nº federado F3").trim().length()==0){
    		nf3=true;
    	}
    	
    	/*Comprobamos el Nº federado F3*/
    	boolean cf3=false;
    	if(condicion.getCampo("Categoria F3").trim().equalsIgnoreCase(candidato.getCampo("Categoria F3").trim()) || condicion.getCampo("Categoria F3").trim().length()==0){
    		cf3=true;
    	}
    	
    	//Ficha 4
    	/*Comprobamos el Deporte F4*/
    	boolean df4=false;
    	if(condicion.getCampo("Deporte F4").trim().equalsIgnoreCase(candidato.getCampo("Categoria F3").trim()) || condicion.getCampo("Categoria F3").trim().length()==0){
    		df4=true;
    	}
    	
    	/*Comprobamos el Nº federado F4*/
    	boolean nf4=false;
    	if(condicion.getCampo("Nº federado F4").trim().equalsIgnoreCase(candidato.getCampo("Nº federado F4").trim()) || condicion.getCampo("Nº federado F4").trim().length()==0){
    		nf4=true;
    	}
    	
    	/*Comprobamos el Nº federado F4*/
    	boolean cf4=false;
    	if(condicion.getCampo("Categoria F4").trim().equalsIgnoreCase(candidato.getCampo("Categoria F4").trim()) || condicion.getCampo("Categoria F4").trim().length()==0){
    		cf4=true;
    	}
    	
    	//Ficha 5
    	/*Comprobamos el Deporte F5*/
    	boolean df5=false;
    	if(condicion.getCampo("Deporte F5").trim().equalsIgnoreCase(candidato.getCampo("Deporte F5").trim()) || condicion.getCampo("Deporte F5").trim().length()==0){
    		df5=true;
    	}
    	
    	/*Comprobamos el Nº federado F5*/
    	boolean nf5=false;
    	if(condicion.getCampo("Nº federado F5").trim().equalsIgnoreCase(candidato.getCampo("Nº federado F5").trim()) || condicion.getCampo("Nº federado F5").trim().length()==0){
    		nf5=true;
    	}
    	
    	/*Comprobamos la Categoria F5*/
    	boolean cf5=false;
    	if(condicion.getCampo("Categoria F5").trim().equalsIgnoreCase(candidato.getCampo("Categoria F5").trim()) || condicion.getCampo("Categoria F5").trim().length()==0){
    		cf5=true;
    	}
    	
    	solucion=nombre && apellido1 && apellido2 && cid && sexo && fecha && apodo && pais && df1 && nf1 && cf1 && df2 && nf2 && cf2 && df3 && nf3 && cf3 && df4 && nf4 && cf4 && df5 && nf5 && cf5;
    	return solucion;
    }

    
    public byte leerByteFichOptimo(int num_bloque, int posicion){
    	try {
    		buffermem=memoria.adquirir(returnmem, num_bloque);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
        byte b = buffermem.get(punteroDecode+posicion);
        
        this.punteroDecode++;
        return b;
    }
    
    public String leerCadenaOptima(int tamaño, int num_bloque, int posicion){
    	String cadena="";
    	byte aux;
    	char aux1;
    	
    	for(int i=0;i<tamaño;i++){
    		aux=leerByteFichOptimo(num_bloque, posicion);
    		aux1=(char)aux;
    		cadena=cadena+String.valueOf(aux1);
    	}
    	return cadena;
    }
    
    
    public BufferDeRegistro leerRegistroOptimo(int bloque, int posicion){
    	BufferDeRegistro bufoptimo=new BufferDeRegistro();
    	
    	//CID
    	bufoptimo.setCampo("CID", leerCadenaOptima(12, bloque, posicion));
    	
    	//NOMBRE
    	int ml_nombre=/*Integer.valueOf(*/leerCadenaOptima(1, bloque, posicion).charAt(0);
    	bufoptimo.setCampo("Nombre", leerCadenaOptima(ml_nombre, bloque, posicion));
    	
    	//Apellido1
    	int ml_ap1=leerCadenaOptima(1, bloque, posicion).charAt(0);
    	bufoptimo.setCampo("Apellido1", leerCadenaOptima(ml_ap1, bloque, posicion));
    	
    	//Apellido2
    	int ml_ap2=leerCadenaOptima(1, bloque, posicion).charAt(0);
    	bufoptimo.setCampo("Apellido2", leerCadenaOptima(ml_ap2, bloque, posicion));
    	
    	//Apodo
    	int ml_apo=leerCadenaOptima(1, bloque, posicion).charAt(0);
    	bufoptimo.setCampo("Apodo", leerCadenaOptima(ml_apo, bloque, posicion));
    	
    	//Sexo
    	int sex=Integer.valueOf(leerCadenaOptima(1, bloque, posicion));
    	if(sex==1){
    		bufoptimo.setCampo("Sexo", "varon");
    	}else{
    		bufoptimo.setCampo("Sexo", "mujer");
    	}
    	
    	//Fecha nacimiento
    	String fech = leerCadenaOptima(8, bloque, posicion);
    	String año_guion=""+fech.charAt(0)+fech.charAt(1)+fech.charAt(2)+fech.charAt(3)+"-"+fech.charAt(4)+fech.charAt(5)+"-"+fech.charAt(6)+fech.charAt(7);
    	bufoptimo.setCampo("Fecha nacimiento", año_guion);
    	
    	//Pais
    	int ml_pai=leerCadenaOptima(1, bloque, posicion).charAt(0);
    	bufoptimo.setCampo("Nacionalidad", leerCadenaOptima(ml_pai, bloque, posicion));
    	
    	//Fichas
    	int mr_fich=Integer.valueOf(leerCadenaOptima(1, bloque, posicion));
    	int aux=1;
    	while(mr_fich>0){
    		//Deporte
    		int ml_dep=leerCadenaOptima(1, bloque, posicion).charAt(0);
        	bufoptimo.setCampo("Deporte F"+aux, leerCadenaOptima(ml_dep, bloque, posicion));
        	
        	//Categoria
        	int ml_cat=leerCadenaOptima(1, bloque, posicion).charAt(0);
        	bufoptimo.setCampo("Categoria F"+aux, leerCadenaOptima(ml_cat, bloque, posicion));
        	
        	//NumeroFed
        	String num_fed=leerCadenaOptima(3, bloque, posicion);
        	bufoptimo.setCampo("Nº federado F"+aux, decodificarBase256_3By(num_fed));

        	
        	aux++;
        	mr_fich--;
    	}
    	this.punteroDecode=0;
    	return bufoptimo;
    }
    

    
    /* Busca los registros de un archivo concreto que cumplen las condiciones
     *definidas en buf_in y devuelve el primero de tales registros en el buf_out.    
    * @param buf_in Archivo sobre el que se va a buscar.
    * @param buf_out Buffer que contiene las condiciones de busqueda, 
    *       solo se deben aplicar las condiciones que afecten al 
    *       archivo actual.
    * @param: buffer buf_out: buffer que contiene el primer resultado de la busqueda
    */ 
    public String buscar(String archivo, BufferDeRegistro buf_in, BufferDeRegistro buf_out) {
    	condicion.copiar(buf_in);
    	punteroCubo=0;
    	punteroByte=0;
    	//int transformacion=0;
    	punteroCuboDir=0;
    	//Busqueda Dispersion Multiclave
    	//CONJUNTO RESULTADO
    	this.conjuntoResultado=new boolean[this.MAX_Cubos];
    	this.conjuntoResultadoAux=new boolean[this.MAX_Cubos];
    	
    	//Lleno el conjunto resultado de trues
    	for (int i=0;i<this.conjuntoResultado.length;i++){
    		this.conjuntoResultado[i]=true;
    	}
    	//lleno el conjunto resultadoAux de falses
    	for (int i=0;i<this.conjuntoResultadoAux.length;i++){
    		this.conjuntoResultadoAux[i]=false;
    	}
    	
    	//Voy a buscar con la dispersion multiclave el cubo donde esta
    	int n1_inf, n2_inf, n3_inf, n1_sup, n2_sup, n3_sup;
    	
    	condicion.copiar(buf_in);
    	if(condicion.getCampo("Nombre").trim().length()!=0){

    		n1_inf=transformadaNombre(condicion.getCampo("Nombre").trim());
    		n1_sup=transformadaNombre(condicion.getCampo("Nombre").trim());
    		
    	}else{
    		
    		n1_inf=0;
    		n1_sup=1;
    	}
    	if(condicion.getCampo("Apellido2").trim().length()!=0){
    		
    		n2_inf=transformadaApellido2(condicion.getCampo("Apellido2").trim());
    		n2_sup=transformadaApellido2(condicion.getCampo("Apellido2").trim());
    		
    	}else{
    		
    		n2_inf=0;
    		n2_sup=7;
    	}
    	if(condicion.getCampo("Apellido1").trim().length()!=0){

    		n3_inf=transformadaApellido1(condicion.getCampo("Apellido1").trim());
    		n3_sup=transformadaApellido1(condicion.getCampo("Apellido1").trim());
    		
    	}else{

    		n3_inf=0;
    		n3_sup=31;
    	}
		System.out.println("nobucle0");

    	for(int i=n1_inf; i<=n1_sup; i++){
    		System.out.println("bucle1");
    		for(int j=n2_inf; j<=n2_sup; j++){
        		System.out.println("bucle2");

    			for(int z=n3_inf; z<=n3_sup; z++){
    	    		System.out.println("bucle3");

    				conjuntoResultadoAux[Transformada(i, z, j)]=true;
    			}
    		}
    	}
    	
    	//Lleno la zona serial de trues
    	for (int i=this.N;i<this.conjuntoResultadoAux.length;i++){
    		this.conjuntoResultadoAux[i]=true;
    	}
    	//&& Logico
    	for (int i=0;i<this.conjuntoResultadoAux.length;i++){
    		this.conjuntoResultado[i]=this.conjuntoResultado[i] && this.conjuntoResultadoAux[i];
    	}
    	
    	
    	
    	
    	/*for (int i=0;i<this.conjuntoResultado.length;i++){
    		this.conjuntoResultado[i]=true;
    	}*/
    	
    	siguiente(buf_out);
    	
    /*	
    	//Busqueda Serial
    	condicion.copiar(buf_in);
    	punteroCubo=0;
    	punteroByte=0;
    	int transformacion=0;
    	punteroCuboDir=0;
    	
    	siguiente(buf_out);
    */	
    	
    	
    /*	
    	//Busqueda Direccionada
    	//CONJUNTO RESULTADO
    	this.conjuntoResultado=new boolean[this.MAX_Cubos];
    	this.conjuntoResultadoAux=new boolean[this.MAX_Cubos];
    	for (int i=0;i<this.conjuntoResultado.length;i++){
    		this.conjuntoResultado[i]=true;
    	}
    	for (int i=0;i<this.conjuntoResultadoAux.length;i++){
    		this.conjuntoResultadoAux[i]=false;
    	}
    	
    	//Filtro Direccionamiento
    	if(!(condicion.getCampo("Nombre").trim().length()==0)){
    		transformacion=funcionDispersion(condicion.getCampo("Nombre").trim());
    		conjuntoResultadoAux[transformacion]=true;
    		for (int i=this.N;i<this.conjuntoResultadoAux.length;i++){
        		this.conjuntoResultadoAux[i]=true;
        	}
    		//&& Logico
        	for (int i=0;i<this.conjuntoResultadoAux.length;i++){
        		this.conjuntoResultado[i]=this.conjuntoResultado[i] && this.conjuntoResultadoAux[i];
        	}
    	}
    	siguiente(buf_out);
    */	
    	
    	//siguiente(buf_out);
    	//buf_out.copiar(buffregOptimo);
    	return("Terminado metodo buscar " + archivo+" (no implementado)");
    }
    
    
    
    public boolean endRegCubo(int num_cubo, int posicion){
    	
    	try {
    		buffermem=memoria.adquirir(returnmem, num_cubo);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	String espacio=" ";
    	char aux=espacio.charAt(0);
    	byte aux2=(byte)aux;
    	
    	if(buffermem.array()[posicion]==aux2){
    		return true;
    	}
    	else{
    		return false;
    	}	
    }
    
    
    
    
    
    
    /* Recupera el siguiente registro que cumple el criterio de busqueda actual
     * En el caso de navegaciï¿½n por registros (Ir a hijo, Ir a hijo virtual) 
     * permite recuperar de forma sucesiva los hijos. 
     */
    public String siguiente(BufferDeRegistro buf_out) {
    	int ppl;
    	
    	//Busqueda Dispersion Multiclave
    	while(punteroCuboDir<this.MAX_Cubos){
    		System.out.println("Cubo: "+punteroCuboDir);
    		if(conjuntoResultado[punteroCuboDir]){
        		System.out.println(2);
        		
    	    	ppl=leerPPL(this.punteroCuboDir);
    	    	System.out.println("PPL: "+ppl);
    			

    			while(punteroByte<ppl){
    	    		System.out.println("puntero: "+punteroByte+"    PPL: "+ppl);

    				comparar.copiar(leerRegistroOptimo(punteroCuboDir,punteroByte));
    				if(Encaja(this.condicion, this.comparar)){
    					System.out.println(4);
    					punteroByte=punteroByte+RLtoString(comparar).length();
    	    			buf_out.copiar(comparar);
    	    			return("Terminado metodo siguiente (no implementado)");
    				}else{
    					punteroByte=punteroByte+RLtoString(comparar).length();
    					if(endRegCubo(punteroCuboDir, punteroByte)){
    						punteroByte=1000;
    					}
    				}
    			}
    		}
    		punteroCuboDir++;
    		punteroByte=0;
    	}
    	
    	
    	
    	
    /*	
    	//Busqueda Direccionada
    	while(punteroCuboDir<this.MAX_Cubos){
    		//System.out.println("Cubo: "+punteroCuboDir);
    		if(conjuntoResultado[punteroCuboDir]){
        		//System.out.println(2);
        		
    	    	ppl=leerPPL(this.punteroCuboDir);
    	    	//System.out.println("PPL: "+ppl);
    			//return("Terminado metodo siguiente (no implementado)");

    			while(punteroByte<ppl){
    	    		System.out.println("puntero: "+punteroByte+"    PPL: "+ppl);

    				comparar.copiar(leerRegistroOptimo(punteroCuboDir,punteroByte));
    				if(Encaja(this.condicion, this.comparar)){
    					//System.out.println(4);
    					punteroByte=punteroByte+RLtoString(comparar).length();
    	    			buf_out.copiar(comparar);
    	    			return("Terminado metodo siguiente (no implementado)");
    				}else{
    					punteroByte=punteroByte+RLtoString(comparar).length();
    					if(endRegCubo(punteroCuboDir, punteroByte)){
    						punteroByte=1000;
    					}
    				}
    			}
    		}
    		punteroCuboDir++;
    		punteroByte=0;
    	}
    */	
    	
    	
    /*	
    	//Busqueda Serial
    	ppl=leerPPL(this.punteroCubo);
    	while(punteroByte<ppl){
    		//System.out.println("PunteroByte: "+this.punteroByte);
    		//System.out.println("PunteroCubo: "+this.punteroCubo);
    		comparar.copiar(leerRegistroOptimo(punteroCubo,punteroByte));
    		punteroByte=punteroByte+RLtoString(comparar).length();
    		if(Encaja(this.condicion, this.comparar)){
    			buf_out.copiar(comparar);
    			if(punteroByte>=ppl){
        			this.punteroCubo++;
        			this.punteroByte=0;
        			ppl=leerPPL(this.punteroCubo);
        		}
    			break;
    		}
    		if(punteroByte>=ppl){
    			this.punteroCubo++;
    			this.punteroByte=0;
    			ppl=leerPPL(this.punteroCubo);
    		}
    	}
    */	
    	
    		
    	
        return("Terminado metodo siguiente (no implementado)");
    }

   /* Recupera el registro anterior al actual que cumple el criterio de busqueda actual
    * En el caso de navegacion por registros (Ir a hijo, Ir a hijo virtual) 
    * permite recuperar de forma sucesiva los hijos. 
    */
    public String anterior(BufferDeRegistro buf_out) {
        return("Terminado metodo anterior (no implementado)");
    }

    /* Inserta un registro en el archivo que este seleccionado.
     */
    public String insertar(String archivo, BufferDeRegistro buf_in) {
    	int a=0;
    	//double media_tam_reg=0.0;
    	
    	//Dispersion Multiclave
    	while(5000>a){
    		buf_in.copiar(buffreg);
    		
    		String reg=RLtoString(buf_in);
    		//media_tam_reg=media_tam_reg+reg.length();
    		int direccion=Transformada(transformadaNombre(buf_in.getCampo("Nombre").trim()), transformadaApellido1(buf_in.getCampo("Apellido1").trim()), transformadaApellido2(buf_in.getCampo("Apellido2").trim()));
    		//System.out.println("Direccion de ");
    		if(!escribirEnCubo(direccion,reg)){
    			if(!escribirEnCubo(punteroDesbordamiento,reg)){
    				punteroDesbordamiento=punteroDesbordamiento+1;
    				escribirEnCubo(punteroDesbordamiento,reg);
    			}
    		}
    		this.buffreg=leerRegistro();
        	
    		a++;
    	}
    	this.MAX_Cubos=punteroDesbordamiento;
    	
    /*	
    	//Fichero Direccionado
    	while(5000>a){
    		buf_in.copiar(buffreg);
    		
    		String reg=RLtoString(buf_in);
    		//media_tam_reg=media_tam_reg+reg.length();
    		
    		int direccion=funcionDispersion(buffreg.getCampo("Nombre"));
    		if(!escribirEnCubo(direccion,reg)){
    			if(!escribirEnCubo(punteroDesbordamiento,reg)){
    				punteroDesbordamiento=punteroDesbordamiento+1;
    				escribirEnCubo(punteroDesbordamiento,reg);
    			}
    		}
    		this.buffreg=leerRegistro();
        	
    		a++;
    	}
    	this.MAX_Cubos=punteroDesbordamiento;
    /*
    /*	
    	//Fichero Serial
    	while(5000>a){
    		
    		buf_in.copiar(buffreg);
    		
    		String reg=RLtoString(buf_in);
    		//media_tam_reg=media_tam_reg+reg.length();
    	
    		//Fichero serial
    		if(!escribirEnCubo(punteroSerial,reg)){
    			punteroSerial=punteroSerial+1;
    			escribirEnCubo(punteroSerial,reg);
    		}
    		this.buffreg=leerRegistro();
    	
    		a++;
    	}
    	this.MAX_Cubos=punteroSerial;
    */	
    	
    	//System.out.println("La media de bytes utilizados por los registros es de: "+(media_tam_reg/5000));
    	
        return("Terminado metodo insertar " + archivo+" (no implementado)");
    }
  
    /* Borra (de forma fisica) un registro dentro del archivo
     */
    public String borrar(String archivo, BufferDeRegistro buf_in) {
        return("Terminado metodo borrar " + archivo+ " (no implementado)");
    }
  
    /* Actualiza un registro de un archivo */
    public String actualizar(String archivo, BufferDeRegistro viejo, BufferDeRegistro nuevo) {
        return("Terminado metodo actualizar " + archivo+" (no implementado)");
    }

    /* Comienza la navegacion por los hijos del registro padre actual del 
     * archivo especificado y muestra el primer hijo
     * El resto de hijos se muestran mediante el metodo siguiente
     * @param String archivo: archivo hijo seleccionado
     */  
    public String ir_a_hijo(String archivo, BufferDeRegistro buf_out) {
        return("Terminado metodo ir_a_hijo " + archivo+ " (no implementado)");
    }

    /* Muestra el padre del registro actual 
     */
    public String ir_a_padre(BufferDeRegistro buf_out) {
        return("Terminado metodo ir_a_padre (no implementado)");
    }
  
    /* Muestra el padre virtual especificado en archivo
     * @param archivo padre virtual seleccionado
     */
    public String ir_a_padre_virtual(String archivo, BufferDeRegistro buf_out) {
        return("Terminado metodo ir_a_padre_virtual " + archivo+ " (no implementado)");
    }

    /* Comienza la navegacion a los hijos virtuales del registro padre actual
     * y muestra el primer hijo virtual . El resto de hijos se muestran mediante
     * el metodo siguiente.
     * @param archivo hijo virtual seleccionado.
     */
    public String ir_a_hijo_virtual(String archivo, BufferDeRegistro buf_out) {
        return("Terminado metodo ir_a_hijo_virtual " + archivo + " (no implementado)");
    }

    /* Busqueda con el propio lenguaje de consulta: Por ejemplo utilizando OR/NOT */
    public String busqueda_avanzada(String consulta, BufferDeRegistro buf_in, BufferDeRegistro buf_out) {      

    	return("Terminado metodo busqueda_avanzada "+consulta+" (no implementado)");
    }
  
    /* Reorganiza el area de almacenamiento direccionado asignando un nuevo 
     * tamaï¿½o o cambiando la clave de direccionamiento.
     * @param area Archivo o area de direccionamiento que se va a reorganizar
     * @param tamaï¿½o Nuevo tamaï¿½o en cubos 
     * @param buffer Buf_in Buffer de entrada, para operaciones de cambio de Clave de Direccionamiento  
     */
    public String reorganizar(String area,String tamano, BufferDeRegistro buf_in) {
        return("Terminado metodo reorganizar "+area+" "+tamano+" (no implementado)");
    }
      
    public String crear_indice(String clave,String tipo, BufferDeRegistro buf_in) {
        return("Terminado metodo crear_indice "+clave+" "+tipo+" (no implementado)");
    }

    public String destruir_indice(String clave,BufferDeRegistro buf_in) {
        return("Terminado metodo destruir_indice "+clave+" (no implementado)");
    }

    public String reorganizar_indice(String clave, BufferDeRegistro buf_in) {
        return("Terminado metodo reorganizar_indice "+clave+" (no implementado)");
    }
  
    /*Ejecucion del sistema gestor de ficheros con interfaz grafica.*/
    public static void main(String arg[]){
        
        /* Se lanza una nueva ventana principal con una nueva instancia de 
        GestorDeFicheros y una nueva instancia de Esquema*/
        
        VentanaPrincipal.lanzar(new GestorDeFicheros(), new Esquema());
    } 
}
