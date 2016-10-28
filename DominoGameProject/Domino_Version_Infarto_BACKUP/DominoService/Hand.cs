/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Hand
 *  Descripción de la clase:
 *  Clase que almacena la mano (fichas) de un jugador.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa las Fichas que tiene un Jugador */
    public class Hand
    {
        public static int MAX_PIECES = 7;

        public List<Piece> Pieces;

        public Hand()
        {
            Pieces = new List<Piece>();
        }

        public void AddPiece(Piece NewPiece)
        {
            if (NewPiece == null)
            {
                throw new ExcepcionNullPiece("La pieza es null");
            }
            this.Pieces.Add(NewPiece);
        }
    }
}