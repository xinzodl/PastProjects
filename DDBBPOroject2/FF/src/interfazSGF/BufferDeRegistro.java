package interfazSGF;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Buffer logico que contiene toda la informacion de un registro
 */
public class BufferDeRegistro {
    ///////////////// TITULO Y FECHA ///////////////////
    static String titulo="La Agencia Antidopaje";
    static String fecha="2014";
    static String version="1.6";
    
    ///////////////// CAMPOS ///////////////////
    //Nombres de los campos
    static String titulos[]=new String[]{
    //Deportista
    "Nombre",
    "Apellido1",
    "Apellido2",
    "CID",
    "Sexo",
    "Fecha nacimiento",
    "Apodo",
    "Nacionalidad",
    //Fichas 1
    "Deporte F1",
    "Nº federado F1",
    "Categoria F1",
    //Fichas 2
    "Deporte F2",
    "Nº federado F2",
    "Categoria F2",
    //Fichas 3
    "Deporte F3",
    "Nº federado F3",
    "Categoria F3",
    //Fichas 4
    "Deporte F4",
    "Nº federado F4",
    "Categoria F4",
    //Fichas 5
    "Deporte F5",
    "Nº federado F5",
    "Categoria F5"
    };
    
    //Columnas de los campos
    static int longitudes[]=new int[]{
    //Deportista
    35,
    30,
    30,
    12,
    5,
    10,
    25,    
    45,
    //Fichas
    15,
    5,
    15,
    15,
    5,
    15,
    15,
    5,
    15,
    15,
    5,
    15,
    15,
    5,
    15
    };
    
    //Filas de los campos
    static int filas[]=new int[]{
    //Deportista
    1,
    1,
    1,
    1,
    1,
    1,    
    1,
    1,
    //Ficha 1
    1,    
    1,
    1,
    //Ficha 2
    1,    
    1,
    1,
    //Ficha 3 
    1,    
    1,
    1,
    //Ficha 4
    1,    
    1,
    1,
    //Ficha 5,
    1,
    1,
    1
    };
    
    //////////////// APARTADOS ////////////////
    //Nombres apartados
    static String nombre_apartados[]=new String[]{
    "Deportista",
    "Ficha 1",
    "Ficha 2",
    "Ficha 3",
    "Ficha 4",
    "Ficha 5"
    };
    
    //Comienzo de los apartados
    static int comienzo_apartados[]=new int[]{
    0,
    8,
    11,
    14,
    17,
    20
    };
    
    ///////////////// CATALOGOS /////////////////////
    static String nombre_catalogos[]=new String[]{
    };
    static String [][] campos_catalogos=new String [][]{
        new String[]{},
    };
    //Mapeos que contienen el valor de los campos
    private Map<String,String> titulosValores=new HashMap<String,String>();
    
    /**
     *MÃ©todo para recuperar el valor de un determinado campo
     *@param nombre El nombre del campo a cuyo valor se quiere acceder
     *@return El valor de dicho campo
     */
    public String getCampo(String nombre){
        String retorno=titulosValores.get(nombre);
        if (retorno==null)retorno="";
        return retorno;
    }
    
    /**
     *Metodo para establecer el valor de un determinado campo
     *@param nombre El nombre del campo cuyo valor se desea establecer
     *@param valor El valor que se desea dar a dicho campo
     */
    public void setCampo(String nombre,String valor){
        String retorno=titulosValores.put(nombre,valor);        
    }
           
    Set<String> getTitulos(){return titulosValores.keySet();}
    
    void limpiar() {        
        for(int i=0;i<titulos.length;i++)setCampo(titulos[i],"");        
    }

    public void copiar(BufferDeRegistro buf_in) {
        for(int i=0;i<titulos.length;i++)setCampo(titulos[i],buf_in.getCampo(titulos[i]));        
    }  
    
    void lista(String [] valores){
        for(int i=0;i<titulos.length;i++)setCampo(titulos[i],valores[i]);        
    }
    
    String[] lista(){
        String [] retorno=new String[titulos.length];
        for(int i=0;i<retorno.length;i++)retorno[i]=getCampo(titulos[i]);    
        return retorno;
    }
}