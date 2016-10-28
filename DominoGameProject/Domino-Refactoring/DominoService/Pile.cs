/* ---------------------------------------
 * Autores:
 * Marcos Alejandro Pariona
 * Jéssica Zamora Castillo.
 * Grupo de prácticas: Grupo 6.
 * Fecha: 20/04/2015.
 * TADS 2014/2015. Proyecto Dominó.
-----------------------------------------*/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /// <summary>
    /// clase Pile, representa la Pila de Fichas
    /// </summary>  
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
            /// <summary>
            ///  Crea las fichas del dominó
            /// </summary>
            
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

        }

        /// <summary>
        /// Devuelve una ficha aleatoria y que no está repartida de la pila de fichas
        /// </summary>
       
        public Piece GetRandomPiece()
        {
         

                Random RandomGenerator = new Random();
                int RandomNumber = RandomGenerator.Next(0, this.DominoPiecesAvailables.Count);

                Piece RandomPiece = DominoPiecesAvailables[RandomNumber];

                this.DominoPiecesGiven.Add(DominoPiecesAvailables[RandomNumber]);
                this.DominoPiecesAvailables.Remove(RandomPiece);
               
               return RandomPiece;
           

        }

        /// <summary>
        /// metodo que retorna la primera ficha de la lista, siempre.
        /// </summary> 
        public Piece GetFirstPiece()
        {

            Piece firstPiece = DominoPiecesAvailables[0];
            this.DominoPiecesGiven.Add(DominoPiecesAvailables[0]);
            this.DominoPiecesAvailables.Remove(firstPiece);

            return firstPiece ;

        }

    }
}