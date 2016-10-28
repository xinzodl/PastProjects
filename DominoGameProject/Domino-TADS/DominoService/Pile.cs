using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService{
    /* Representa la Pila de Fichas */
    public class Pile{
        private List<Piece> DominoPiecesAvailables;
        private List<Piece> DominoPiecesGiven;

        public Pile(){

            this.DominoPiecesAvailables = new List<Piece>();
            this.DominoPiecesGiven = new List<Piece>();
            int Combinations = 1;
            int FirstSquareValue = 0;
            int SecondSquareValue = 0;

            //  Crea las fichas del dominó
            int i, j;
            for (i = 0; i < 7; i++){
                for (j = 0; j < Combinations; j++){

                    DominoPiecesAvailables.Add(new Piece(FirstSquareValue, SecondSquareValue));
                    FirstSquareValue++;
                }

                Combinations++;
                FirstSquareValue = 0;
                SecondSquareValue++;
            }
            Console.WriteLine(this.DominoPiecesAvailables.Count + " " + is_there_duplicates(this));
            if (is_there_duplicates(this)){
                throw new PileException("There are two pieces with the same value.");
            }
            else if (this.DominoPiecesAvailables.Count != 28){
                throw new PileException("Wrong number of pieces generated.");
            }
        }

        public Piece GetFirstPiece(){

            if (this.DominoPiecesAvailables.Count == 0)
                throw new PileException("No pieces available.");

            this.DominoPiecesGiven.Add(DominoPiecesAvailables.First());
            this.DominoPiecesAvailables.RemoveAt(0);
            return this.DominoPiecesGiven.Last();
        }

        // Devuelve una ficha aleatoria y que no está repartida de la pila de fichas
        public Piece GetRandomPiece(){

            if (this.DominoPiecesAvailables.Count == 0)
                throw new PileException("No pieces available.");

            Random RandomGenerator = new Random();
            int RandomNumber = RandomGenerator.Next(0, this.DominoPiecesAvailables.Count);

            Piece RandomPiece = DominoPiecesAvailables[RandomNumber];

            this.DominoPiecesGiven.Add(DominoPiecesAvailables[RandomNumber]);
            this.DominoPiecesAvailables.Remove(RandomPiece);
            
            return RandomPiece;

        }

        private bool is_there_duplicates(Pile pile){

            int i, j;
            i = 1;
            foreach (Piece piece in this.DominoPiecesAvailables){
                for (j = i; j < 28; j++){
                    if (piece.ToInt() == this.DominoPiecesAvailables.ElementAt(j).ToInt()) return true;
                }
                i++;
            }
            return false;
        }

    }
}