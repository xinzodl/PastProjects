import interfazSGF.*;

//Descripción de la jerarquía de Archivos Lógicos del Sistema de Ficheros

public class Esquema implements InterfazEsquema{
    // Nombres de los Archivos Lógicos
    public String [] archivos(){
        return new String[]{      
            "Maestro",	//Archivo 0
            "Hijo1",    //Archivo 1
            "Raíz2",    //Archivo 2
            "Hijo2",    //Archivo 3
            "Hijo3",    //Archivo 4
        };
    }
   
    // Identificador de los Hijos de cada Archivo
    public int [][] hijos(){
        return new int[][]{
            {1},        //Hijos del Archivo Lógico 0
            {},         //Hijos del Archivo Lógico 1
            {3,4},      //Hijos del Archivo Lógico 2
            {},         //Hijos del Archivo Lógico 3
            {}          //Hijos del Archivo Lógico 4
        };
    }
   
    // Identificador de los Hijos Virtuales de cada Archivo
    public int [][] hijos_virtuales(){
        return new int[][]{
            {},         //Hijos Virtuales del Archivo Lógico 0        
            {},         //Hijos Virtuales del Archivo Lógico 1
            {1},        //Hijos Virtuales del Archivo Lógico 2
            {},         //Hijos Virtuales del Archivo Lógico 3
            {}          //Hijos Virtuales del Archivo Lógico 4
        };
    }
}