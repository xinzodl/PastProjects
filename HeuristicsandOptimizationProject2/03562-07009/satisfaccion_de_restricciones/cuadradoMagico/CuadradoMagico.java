package cuadradoMagico;

import org.jacop.core.*; 
import org.jacop.constraints.*; 
import org.jacop.search.*; 

import java.util.*;
import java.io.*;
import java.util.Scanner;

public class CuadradoMagico {

	//public static final String USAGE = "USAGE: java -classpath .:jacop-4.1.0.jar SendMoreMoney";

	public static void main (String[] args) {

		//Cogemos los parametros que necesitamos (Dimension del cuadrado)
        if (args.length != 1) {
            System.out.println("Hace falta un argumento <N para Cuadrado Magico de NxN>");
            System.exit(0);
        }
		int n= Integer.parseInt(args[0]);
	 
		//Que se entere el usuario
		System.out.println("El cuadrado tendra la dimension: "+n+"x"+n);
		
		//Creacion del tablero
		IntVar variables[] = new IntVar[n*n];
		
		// define FD store
		Store store = new Store();
		
		//Inicializamos cada una de las casillas de la matriz 
		//A la par, le asignamos el dominio de las variables
		int it=0;
		for(int i=0;i<variables.length;i++){
			variables[i] = new IntVar(store, "Posicion"+it , 1, n*n);
				it++;		
		}
		
		//RESTRICCIONES
		//este es el valor que deberan sumar cada una de las filas, columnas y diagonales principales
		IntVar suma = new IntVar(store, 0, 100);
		
		//los valores de la matriz han de ser distintos
		store.impose(new Alldistinct(variables));
		
		//Sumas de columnas, filas y diagonales
		IntVar vector[] = new IntVar[n];//lo iremos reutilizando para añadir restricciones
		
		//damos valor a las filas y hacemos que sumen lo mismo
		for(int i=0;i<variables.length;i=i+n){
			for(int j=0;j<n;j++){
				vector[j] = variables[i+j];
			}
			store.impose(new Sum (vector, suma));
		}
		
		//damos valor a las columnas y hacemos que sumen lo mismo
		for(int i=0;i<n;i++){
			int contador=0;
			for(int j=0;j<variables.length;j=j+n){
				vector[contador] = variables[j+i];
				contador++;
				
			}
			store.impose(new Sum (vector, suma));
		}
		
		//damos valor a la diagonal I-D y hacemos que sumen lo mismo
		int contador=0;
		for(int i=0;i<variables.length;i=n+i+1){
			vector[contador] = variables[i];
			contador++;
		}
		store.impose(new Sum (vector, suma));
		
		//damos valor a la diagonal D-I y hacemos que sumen lo mismo
		contador=0;
		for(int i=n-1;i<(variables.length)-1;i=n+i-1){
			vector[contador] = variables[i];
			contador++;
		}
		store.impose(new Sum (vector, suma));
		
		
		Search<IntVar> label = new DepthFirstSearch<IntVar>();
	    SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(variables, new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
		
	    Boolean result = label.labeling(store, select);
	    if (result) {
	    	//Imprimimos el cubo
			for(int i=0;i<variables.length;i++){
				if(i%n==0){
					System.out.println();
				}
				System.out.print(variables[i].dom() + "\t");
			}
	    } else{
	      System.out.println("*** No");
	    }
	    System.out.println();
		
	}
	
}
