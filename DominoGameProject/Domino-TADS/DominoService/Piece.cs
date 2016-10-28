using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa una Ficha */
    public class Piece{
        private bool IsGiven;

        public int firstSquareValue { get; set; }

        public int secondSquareValue { get; set; }

        public Piece(int firstValue, int secondValue){
            if ((firstValue == -1) && (secondValue == -1)) { }// Pieza inicial permitida
            else if ((firstValue < 0) || (firstValue > 6))
                throw new PieceException("First value out of range.");
            else if ((secondValue < 0) || (secondValue > 6))
                throw new PieceException("Second value out of range.");
            this.firstSquareValue = firstValue;
            this.secondSquareValue = secondValue;

            IsGiven = false;
        }

        public int ToInt(){
            return (this.firstSquareValue * 10 + this.secondSquareValue);
        }
        public bool IsDouble(){
            return this.firstSquareValue == this.secondSquareValue;
        }
    }
}