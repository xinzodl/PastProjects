/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  PieceData
 *  Descripción de la clase:
 *  Clase que sirve para guardar los datos de una ficha (que hay que 
 *  comunicar a los clientes, para ser capaces de intar e identificar 
 *  correctamente).
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;


namespace DominoService
{
    [DataContract]
    public class PieceData
    {
        [DataMember]
        public int jugador;
        [DataMember]
        public int valor;
        [DataMember]
        public int posicionEnMano;
        [DataMember]
        public int index;
        [DataMember]
        public int selector;

        public PieceData()
        {
            jugador = -1;
            valor = -1;
            posicionEnMano = -1;
            index = -1;
            selector = -1;
        }

        public PieceData(int PieceValue, int PiecePosition, int PlayerPosition)
        {
            if (PieceValue < 0 || PieceValue> 66)
            {
                throw new ExcepcionInvalidPieceArguments("Valor de pieza no correcto");
            }
            else if (PlayerPosition<0 || PlayerPosition>4)
            {
                throw new ExcepcionInvalidPieceArguments("Posicion de Jugador no correcta");
            }
            else if (PiecePosition<0)
            {
                throw new ExcepcionInvalidPieceArguments("Posicion de Pieza no correcta");
            }
            jugador = PlayerPosition;
            valor = PieceValue;
            posicionEnMano = PiecePosition;
        }
    }
}