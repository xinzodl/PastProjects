/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Pile
 *  Descripción de la clase:
 *  Clase que sirve para guardar las fichas dadas y las que quedan por dar.
 *  Implementa las distintas formas de repartir.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa la Pila de Fichas */
    public class Pile
    {
        private List<Piece> DominoPiecesAvailables;
        private List<Piece> DominoPiecesGiven;

        public Pile()
        {

            this.DominoPiecesAvailables = new List<Piece>();
            this.DominoPiecesGiven = new List<Piece>();

            int Combinations = 1;

            int FirstSquareValue = 0;
            int SecondSquareValue = 0;

            //  Crea las fichas del dominó 00-01-11-02-12-22-03-13-23-33-04-14-24-34-44-05-15-25-35-45-55-06-16-26-36-46-56-66
            for (int i = 0; i < 7; i++)
            {
                for (int j = 0; j < Combinations; j++)
                {

                    DominoPiecesAvailables.Add(new Piece(FirstSquareValue, SecondSquareValue));
                    FirstSquareValue++;
                }

                Combinations++;
                FirstSquareValue = 0;
                SecondSquareValue++;

            }
            //esta linea es mia, para dormirlo 50ms
            //System.Threading.Thread.Sleep(50);

        }
        public Piece GetFirstPiece()
        {
            this.DominoPiecesGiven.Add(DominoPiecesAvailables.First());
            this.DominoPiecesAvailables.RemoveAt(0);
            return this.DominoPiecesGiven.Last();
        }
        // Devuelve una ficha aleatoria y que no está repartida de la pila de fichas
        public Piece GetRandomPiece()
        {

            Random RandomGenerator = new Random();
            int RandomNumber = RandomGenerator.Next(0, this.DominoPiecesAvailables.Count);

            Piece RandomPiece = DominoPiecesAvailables[RandomNumber];

            this.DominoPiecesGiven.Add(DominoPiecesAvailables[RandomNumber]);
            this.DominoPiecesAvailables.Remove(RandomPiece);

            return RandomPiece;

        }

        public Piece GetPiezaOrdenada()
        {
            //damos las piezas en orden, segun fueorn creadas
            Piece Pieza = DominoPiecesAvailables[0];
            this.DominoPiecesGiven.Add(DominoPiecesAvailables[0]);
            this.DominoPiecesAvailables.Remove(Pieza);
            return Pieza;
        }

        public List<Piece> GetAvailables() 
        {
            return DominoPiecesAvailables;
        }

    }
}