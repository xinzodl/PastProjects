import interfazSGF.*;

//Descripci�n de la jerarqu�a de Archivos L�gicos del Sistema de Ficheros

public class Esquema implements InterfazEsquema{
    // Nombres de los Archivos L�gicos
    public String [] archivos(){
        return new String[]{      
            "Maestro",	//Archivo 0
            "Hijo1",    //Archivo 1
            "Ra�z2",    //Archivo 2
            "Hijo2",    //Archivo 3
            "Hijo3",    //Archivo 4
        };
    }
   
    // Identificador de los Hijos de cada Archivo
    public int [][] hijos(){
        return new int[][]{
            {1},        //Hijos del Archivo L�gico 0
            {},         //Hijos del Archivo L�gico 1
            {3,4},      //Hijos del Archivo L�gico 2
            {},         //Hijos del Archivo L�gico 3
            {}          //Hijos del Archivo L�gico 4
        };
    }
   
    // Identificador de los Hijos Virtuales de cada Archivo
    public int [][] hijos_virtuales(){
        return new int[][]{
            {},         //Hijos Virtuales del Archivo L�gico 0        
            {},         //Hijos Virtuales del Archivo L�gico 1
            {1},        //Hijos Virtuales del Archivo L�gico 2
            {},         //Hijos Virtuales del Archivo L�gico 3
            {}          //Hijos Virtuales del Archivo L�gico 4
        };
    }
}