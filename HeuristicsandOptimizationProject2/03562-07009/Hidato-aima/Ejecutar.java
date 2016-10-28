package aima.core.environment.hidato;

import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.IOException;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;

/**
 * Clase que Ejecuta los algoritmos en el problema deseado.
 */

//R:\LINUX\Practica_2_heuristica\hidato_original.txt
public class Ejecutar {
	public int llevo=0;

    private static Algoritmo getAlgoritmo(String arg) {
        arg = arg.toLowerCase();
        if (arg.equals("amplitud")) {
            return Algoritmo.Amplitud;
        } else if (arg.equals("profundidad")) {
            return Algoritmo.Profundidad;
        } else if (arg.equals("astar")) {
            return Algoritmo.Astar;
        } else if (arg.equals("gbfs")) {
            return Algoritmo.GBFS;
        } else {
            System.err.println("Algoritmo desconocido: " + arg);
            System.exit(-1);
            return null;
        }
    }

    private static Heuristica getHeuristica(String arg) {
        arg = arg.toLowerCase();
        if (arg.equals("h1")) {
            return Heuristica.H1;
        }else if(arg.equals("h2")){
        	return Heuristica.H2;
        }else {
            System.err.println("Heuristica desconocida: " + arg);
            System.exit(-1);
            return null;
        }
    }
    // Identificador para los algoritmos que pueden ejecutarse

    public enum Algoritmo {
        Amplitud, Profundidad, Astar, GBFS
    };

    // Poner aqui un nuevo identificador para cada heurística creada
    public enum Heuristica {

        H1, H2
    };

    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Utilizacion: java Ejecutar <algoritmo> <heuristica> <ruta> "+ args.length);
            System.exit(0);
        }
        
        Algoritmo algoritmo = getAlgoritmo(args[0]);
        Heuristica heuristica = getHeuristica(args[1]);
        String ruta = args[2];
        /*Lectura del ficheros*/ 
        
        
      //Abrimos el txt del hidato a resolver
      	File archivo = null;
      	FileReader fr = null;
  		BufferedReader br = null;
      	 
      //Creamos el vector de variables
      	
      	Estado estado;
      	//variables auxiliares
      	int dimFila=0;
        int dimCol=0;
        int dimension=0;
      	int aux[][]=null;
      	int fila=0;
      	int columna=0;
      	int contador_fijos=0;
      		
      		
      	try {
      		// Apertura del fichero y creacion de BufferedReader para poder
      		// hacer una lectura comoda (disponer del metodo readLine()).
   	    	archivo = new File (ruta);
   			fr = new FileReader (archivo);
   			br = new BufferedReader(fr);
   			
      				 
   			// Lectura del fichero
      	    String linea;
      	    
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
      		 		aux= new int[dimFila][dimCol];
      	        }else if (dimension>2){//ya he leido las dimensiones, entonces estoy leyendo los numero
      	        	for(int i=0;i<linea.length();i++){
      	        		if((int)linea.charAt(i)>48 && (int)linea.charAt(i)<=57  ){//es numero el primero
      	        			contador_fijos++;
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
      		System.out.println("TENEMOS " + contador_fijos + "CELLDAS FIJAS");
      		int cf_min=0;
      	    int cc_min=0;
      		int maximo=0;
      	    int minimo=dimFila*dimCol;
      		for (int ii=0; ii<aux.length;ii++){
      			for(int jj=0; jj<aux[0].length;jj++){
      				if(aux[ii][jj]>=maximo){//maximo
      					maximo=aux[ii][jj];
      				}
      				if( aux[ii][jj]<=minimo && aux[ii][jj]>0){//minimo
      					minimo=aux[ii][jj];
      					cf_min=ii;
      					cc_min=jj;
      				}
      			}
      		}//fin de for maximo/minimo
      		
      		estado=new Estado(aux, maximo, cf_min, cc_min);
        
        
        // Inicializacion de las funciones que definen el problema.
        // Pueden incluirse parametros en los constructores segun sea necesario
        AccionesDisponibles accDisponibles = new AccionesDisponibles();
        ResultadoAccion resAccion = new ResultadoAccion();
        FuncionMetas fMetas = new FuncionMetas();
        CosteAccion costeAccion = new CosteAccion();

	/* Imprime el estado para comprobar que se ha cargado correctamente */
        System.out.println(estado.toString());
        try {
            Problem problem = new Problem(estado, accDisponibles,
                    resAccion, fMetas, costeAccion);

            HeuristicFunction hf = null;
            switch (heuristica) {
                case H1:
                    System.out.println("Heuristica: h1");
                    hf = new Heuristica1();
                    break;
                // Poner aqui el resto de heuristicas
                case H2:
                	System.out.println("Heuristica: h2");
                    hf = new Heuristica2();
                    break;
                default:
                    System.out.println("Heuristica invalida");
                    System.exit(-1);
            }

            Search search = null;
            switch (algoritmo) {
                case Amplitud:
                    System.out.println("Algoritmo: Amplitud");
                    search = new BreadthFirstSearch();
                    break;
                case Profundidad:
                    System.out.println("Algoritmo: Profundidad");
                    search = new DepthFirstSearch(new GraphSearch());
                    break;
                case Astar:
                    System.out.println("Algoritmo: A star");
                    search = new AStarSearch(new GraphSearch(), hf);
                    break;
                case GBFS:
                    System.out.println("Algoritmo: GBFS");
                    search = new GreedyBestFirstSearch(new GraphSearch(), hf);
                    break;
                default:
                    System.out.println("Algoritmo invalido");
                    System.exit(-1);
            }

            long t = System.currentTimeMillis();
            List<Action> actionList = search.search(problem);
            long t2 = System.currentTimeMillis();
           
            printActions(actionList);

            System.out.println("Time: " + (t2 - t) / 1000.0 + " s");
            printInstrumentation(search.getMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Metrics metric) {
        for (String key : metric.keySet()) {
            String property = metric.get(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List<Action> actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i).toString();
            System.out.println(action);
        }
        System.out.println("Plan Length: " + actions.size());
    }
}
