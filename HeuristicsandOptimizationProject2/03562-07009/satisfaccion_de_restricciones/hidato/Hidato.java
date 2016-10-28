package hidato;

import org.jacop.core.*; 
import org.jacop.constraints.*; 
import org.jacop.search.*; 

import java.util.*;
import java.io.*;
import java.util.Scanner;

import org.jacop.constraints.Alldistinct;
import org.jacop.constraints.XplusCeqZ;
import org.jacop.constraints.PrimitiveConstraint;


public class Hidato {
	public static void main (String[] args) {
	
//F:\LINUX\Practica_2_heuristica\hidato.txt

		//Cogemos los parametros que necesitamos (ruta del hidato a resolver)
		if (args.length != 1) {
            System.out.println("Hace falta un argumento <Ruta donde este el archivo que contiene el Sudoku>");
            System.exit(0);
        }
		String ruta= args[0];;
	 
		System.out.println("Abriendo el archivo: "+ruta);
		
		
		//Abrimos el txt del hidato a resolver
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
	 
		//Creamos el vector de variables
		IntVar variables[][]=null;
		//variables auxiliares
		int dimFila=0;
        int dimCol=0;
        int dimension=0;
		int aux[][]=null;
		int fila=0;
		int columna=0;
		int numeros=0;
		// define FD store
		Store store = new Store();

		
		
	    try {
	    	// Apertura del fichero y creacion de BufferedReader para poder
	    	// hacer una lectura comoda (disponer del metodo readLine()).
	    	archivo = new File (ruta);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
				 
	         // Lectura del fichero
	         String linea;
	        //int contador=0;
	         //vamos a irlo metiendo,
	         while((linea=br.readLine())!=null){
	        	 if(dimension==0 || dimension==1){
	        		 for(int i=0;i<linea.length();i++){
	 	        		if(dimension==0 && (int)linea.charAt(i)>48 && (int)linea.charAt(i)<=57  ){//es numero el primero
	 	        			if( i+1<linea.length() && (int)linea.charAt(i+1)>=48 && (int)linea.charAt(i+1)<=57  ){//es numero el segundo
	 	        				dimFila= ((int)linea.charAt(i)-48)*10 + (int)linea.charAt(i+1)-48;
	 	        				i++;
	 	        			}else{
	 	        				dimFila= ((int)linea.charAt(i)-48);
	 	        			} 
	 	        			dimension++;
	 	        		}//para el primer numero
	 	        		else if ( dimension==1 && (int)linea.charAt(i)>48 && (int)linea.charAt(i)<=57 ){
	 	        			if( i+1<linea.length() && (int)linea.charAt(i+1)>=48 && (int)linea.charAt(i+1)<=57  ){//es numero el segundo
	 	        				dimCol= ((int)linea.charAt(i)-48)*10 + (int)linea.charAt(i+1)-48;
	 	        				i++;
	 	        			}else{
	 	        				dimCol= ((int)linea.charAt(i)-48);
	 	        			}
	 	        			dimension++;
	 	        		}//fin else if
	 	        	 }//fin for
	        	 }
	        	 if(dimension==2){
		        		 dimension++;
		        		 variables= new IntVar[dimFila][dimCol];
		        		 aux= new int[dimFila][dimCol];
	        	 }else if (dimension>2){//ya he leido las dimensiones, entonces estoy leyendo los numero
	        		 for(int i=0;i<linea.length();i++){
		 	        		if((int)linea.charAt(i)>48 && (int)linea.charAt(i)<=57  ){//es numero el primero
		 	        			if( i+1<linea.length() && (int)linea.charAt(i+1)>=48 && (int)linea.charAt(i+1)<=57  ){//es numero el segundo
		 	        				aux[fila][columna]= ((int)linea.charAt(i)-48)*10 + (int)linea.charAt(i+1)-48;
		 	        				i++;
		 	        				columna++;
		 	        			}else{
			 	        			aux[fila][columna]=((int)linea.charAt(i)-48);
		 	        				columna++;
		 	        			} 
		 	        		}else if ((int)linea.charAt(i)==95){//es guion
		 	        			aux[fila][columna]=0;
		 	        			columna++;
		 	        		}else if((int)linea.charAt(i)==35){//es almohadilla
		 	        			aux[fila][columna]=-1;
		 	        			columna++;
		 	        		}
		 	        	 }//fin for
	        		 fila++;
	        		 columna=0;
	        	 }//fin else es numero que meter a matriz aux
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
		
	    /*
	     *en este punto tenemos en una matriz auxiliar de int, donde:
	     *numeros se representan como int
	     *# se representa como 0, cosas que no hay que tocar
	     *_ se representa como -1, numeros que hay que averiguar 
	    */
	    
	  
	    
	    //recorro para saber cual es el maximo y el minimo del dominio
	    int maximo=0;
	    int minimo=dimFila*dimCol;
		for (int ii=0; ii<aux.length;ii++){
			for(int jj=0; jj<aux[0].length;jj++){
				if(aux[ii][jj]>=maximo){//maximo
					maximo=aux[ii][jj];
				}
				if( aux[ii][jj]<=minimo && aux[ii][jj]>0){//minimo
					minimo=aux[ii][jj];
				}
			}
		}//fin de for maximo/minimo
	 
		
		
	    /*
	     * Vamos ahora a inicializar las variables IntVar con el dominio correspondiente (dominio = 0 para las #)
	     * */
		int contador=0;
		for (int ii=0; ii<aux.length;ii++){
			for(int jj=0; jj<aux[0].length;jj++){
				if(aux[ii][jj]==-1){//#
					 variables[ii][jj] = new IntVar(store, "Posicion"+contador,0,0);
				}else if( aux[ii][jj]==0){//rellenar
					variables[ii][jj] = new IntVar(store, "Posicion"+contador,minimo,maximo);
					numeros++;//para saber cuantos numeros hay
				}else{//es numero, dominio es el mismo
					variables[ii][jj] = new IntVar(store, "Posicion"+contador,aux[ii][jj],aux[ii][jj]);
					numeros++;//para saber cuantos numeros hay
				}
			}
		}//fin de for inicializar variables
		
		
		
		
		/*
		 * nos toca ahora ver cuando se añaden restricciones 
		 * */
		ArrayList restricciones;
		for (int ii=0; ii<aux.length;ii++){//filas
			for(int jj=0; jj<aux[0].length;jj++){//columnas
				if (aux[ii][jj]>=0){//si somos numero
					restricciones = new ArrayList();
					if (ii>0 && aux[ii-1][jj]>=0){//arriba es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii-1][jj]));	
						}
					}
					if (ii>0 && jj>0 && aux[ii-1][jj-1]>=0 ){//arriba izquierda es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii-1][jj-1]));						
						}
					}
					if (jj>0 && aux[ii][jj-1]>=0){//izquierda es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii][jj-1]));
						}
					}
					if (ii!=(aux.length-1) && jj>0 && aux[ii+1][jj-1]>=0){//abajo izquierda es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii+1][jj-1]));
						}
					}
					if (ii!=(aux.length-1) && aux[ii+1][jj]>=0){//abajo es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii+1][jj]));
						}
					}
					if (ii!=(aux.length-1) && jj!=(aux[0].length-1) && aux[ii+1][jj+1]>=0){//diagonal abajo derecha es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii+1][jj+1]));
						}
					}
					if (jj!=(aux[0].length-1) && aux[ii][jj+1]>=0){//derecha es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii][jj+1]));
						}
					}
					if (ii>0 && jj!=(aux[0].length-1) && aux[ii-1][jj+1]>=0 ){//arriba derecha es numero
						if (aux[ii][jj]!=maximo){
							restricciones.add(new XplusCeqZ(variables[ii][jj], 1, variables[ii-1][jj+1]));
						}
					}
					if (!restricciones.isEmpty()){//hay que añadir restricciones
						store.impose(new Or(restricciones));
					}
					restricciones=null;
				}	
			}//fin for restricciones jj
		}//fin for restricciones ii
	
	
	//pasamos la matriz a vector
	IntVar vector []= new IntVar[dimFila*dimCol];
	for (int ii=0; ii<variables.length;ii++){
		for(int jj=0; jj<variables[0].length;jj++){
			vector[ii*dimCol + jj]=variables[ii][jj];
		}
	}
	
	//imprimo vector
	System.out.println("Imprimo vector:");
	for(int i=0;i<vector.length;i++){
		if(i%dimCol==0){
			System.out.println();
		}
		System.out.print(vector[i].dom() + "\t");
	}
	
	/*SOLUCION		*/
	Search<IntVar> label = new DepthFirstSearch<IntVar>();
    SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(vector, new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
	
   Boolean result = label.labeling(store, select);
    if (result) {
    	//Imprimimos el cubo
		for(int i=0;i<vector.length;i++){
			if(i%dimCol==0){
				System.out.println();
			}
			System.out.print(vector[i].dom() + "\t");
		}
    } else{
      System.out.println("*** No");
    }
    System.out.println();
	
	}//FIN DEL MAIN
}
