/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Piece
 *  Descripción de la clase:
 *  Clase que sirve para guardar los datos de una ficha.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa una Ficha */
    public class Piece
    {
        //private bool IsGiven;

        public int FirstSquareValue { get; set; }

        public int SecondSquareValue { get; set; }

        public Piece(int FirstValue, int SecondValue)
        {
            if (FirstValue < -1 || FirstValue > 6 || SecondValue < -1 || SecondValue > 6)
            {
                throw new ExcepcionInvalidPieceArguments("Valores de pieza no correctos");
            }
            this.FirstSquareValue = FirstValue;
            this.SecondSquareValue = SecondValue;

            //IsGiven = false;
        }

        public int ToInt()
        {
            return (this.FirstSquareValue * 10 + this.SecondSquareValue);
        }
        public bool IsDouble() 
        {
            if (this.SecondSquareValue == this.FirstSquareValue) return true;
            return false;
        }
    }
}