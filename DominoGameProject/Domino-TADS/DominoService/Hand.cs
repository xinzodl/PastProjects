using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa las Fichas que tiene un Jugador */
    public class Hand{
        private static int MAX_SIZE = 7;

        private List<Piece> Pieces;

        public Hand(){
            Pieces = new List<Piece>();
        }

        public void AddPiece(Piece NewPiece){
            if (NewPiece == null)
                throw new InvalidPieceAtHandException("Piece to add can't be null.");
            this.Pieces.Add(NewPiece);
        }

        public static int getMaxSize() {
            return MAX_SIZE;
        }

        public List<Piece> getPieces() {
            return this.Pieces;
        }
    }
}