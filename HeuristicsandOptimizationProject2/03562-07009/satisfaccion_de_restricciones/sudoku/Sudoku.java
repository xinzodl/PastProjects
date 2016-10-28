package sudoku;

import org.jacop.core.*; 
import org.jacop.constraints.*; 
import org.jacop.search.*; 

import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Sudoku {

//C:\Users\XinZodl\Desktop\Prueba.txt	
//F:\LINUX\Practica_2_heuristica\doc.txt
	public static void main (String[] args) {

		//Cogemos los parametros que necesitamos (ruta del sudoku a resolver)
        if (args.length != 1) {
            System.out.println("Hace falta un argumento <Ruta donde este el archivo que contiene el Sudoku>");
            System.exit(0);
        }
		String ruta= args[0];
		
		//para el usuario
		System.out.println("Abriendo el archivo: "+ruta);
		
		
		//Abrimos el txt del sudoku a resolver
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
	 
		//Creamos el vector de variables
		IntVar variables[] = new IntVar[81];
		// define FD store
		Store store = new Store();
        int contador=0;

	    try {
	    	// Apertura del fichero y creacion de BufferedReader para poder
	    	// hacer una lectura comoda (disponer del metodo readLine()).
	    	archivo = new File (ruta);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
				 
	         // Lectura del fichero
	         int linea;
	        //int contador=0;
	         while((linea=br.read())>=0){
	        	 if( (linea>48 && linea<=57) || linea==95 ){
		        	 if(linea==95){//si es gion bajo
		        		 variables[contador] = new IntVar(store, "Posicion"+contador,1,9);
		        		 //System.out.println(linea-48);		        		 
		        	 }else{//si es numero
		        		 variables[contador] = new IntVar(store, "Posicion"+contador,linea-48,linea-48);
		        	 }//fin else
	     		     contador++;
	            }//fin if numeros
	         }//fin while
	    }//fin try
	    catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	// En el finally cerramos el fichero, para asegurarnos
	    	// que se cierra tanto si todo va bien como si salta 
	    	// una excepcion.
	    	try{                    
	    		if( null != fr ){   
	    			fr.close();     
	    		}                  
	    	}catch (Exception e2){ 
	    		e2.printStackTrace();
	    	}
	    }//fin finally
		

	    /*IMPRIMIR EL SUDOKU*/
	    for(int ii=0; ii<81; ii++){
			System.out.print(variables[ii].dom() + "\t");
			if (ii%9==8){
				System.out.println("");
			}
	    }//FIN for imprimir
	    
	    
	    //RESTRICCIONES
		IntVar nueve[] = new IntVar[9];//iremos rellenado y creando restricciones
		
		//columnas deben tener todos los numeros, luego todos distintos
	    for (int hor=0; hor<9;hor++){
	    	for (int ver=0; ver<9; ver++){
	    		nueve[ver]=variables[ver*9+hor];
	    	}
    		store.impose(new Alldistinct(nueve));
	    }
		//filas deben tener todos los numeros, luego todos distintos
	    for (int ver=0; ver<9;ver++){
	    	for (int hor=0; hor<9; hor++){
	    		nueve[hor]=variables[hor+ver*9];
	    	}
    		store.impose(new Alldistinct(nueve));
	    }	    
	    //dentro de cada cuadrado todos los numeros
	    for (int ver=0; ver<9; ver=ver+3){
	    	for (int hor=0; hor<9; hor++){
	    		nueve[hor%3]=variables[hor+ver*9];
	    		nueve[hor%3+3]=variables[hor+(ver*9)+9];
	    		nueve[hor%3+3+3]=variables[hor+(ver*9)+9+9];
	    		if(hor==2 || hor==5){
	        		store.impose(new Alldistinct(nueve));	    			
	    		}
	    	}
	    }//fin for de cuadrados
	    
	    
	    Search<IntVar> label = new DepthFirstSearch<IntVar>();
	    SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(variables, new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
		
	    Boolean result = label.labeling(store, select);
	    if (result) {
	    	//Imprimimos el cubo
		    for(int ii=0; ii<81; ii++){
				System.out.print(variables[ii].dom() + " ");
				if (ii%9==8){
					System.out.println("");
				}
		    }//FIN for imprimir
	    } else{
	      System.out.println("*** No");
	    }
	    System.out.println();

	    
	    
	    
	    
	    
	    
	    
	    
	}//FIN DEL MAIN_________________________________
	
}
