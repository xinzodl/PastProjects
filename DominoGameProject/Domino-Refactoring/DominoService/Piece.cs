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
    /// clase Piece, Representa una Ficha 
    /// </summary> 
    public class Piece
    {
        private bool IsGiven;
        public int FirstSquareValue { get; set; }
        public int SecondSquareValue { get; set; }

        public Piece(int FirstValue, int SecondValue)
        {

            //esta parte esta comentada, ya que no funciona la aplicación
            //si esta habilitada, para las pruebas si funciona.
            if (FirstValue == -1 && SecondValue == -1) { 
            }else
            if (FirstValue < 0 || FirstValue > 6){
               throw new InvalidPieceArgument("parametros fuera del rango");
            }
            else if (SecondValue < 0 || SecondValue > 6)
{
                throw new InvalidPieceArgument("parametros fuera del rango");
            }
            else if (FirstValue > SecondValue){
                throw new InvalidPieceArgument("First Value no valido");
            }
              this.FirstSquareValue = FirstValue;
              this.SecondSquareValue = SecondValue;
              IsGiven = false;
            //this.FirstSquareValue = FirstValue;
            //this.SecondSquareValue = SecondValue;
            //IsGiven = false;
                         
        }

        /// <summary>
        /// metodo que retorna el valor de la ficha.
        /// </summary> 
        public int ToInt()
        {
            return (this.FirstSquareValue * 10 + this.SecondSquareValue);
        }
        public String ToString() {
            return this.FirstSquareValue.ToString() + this.SecondSquareValue.ToString();
        }
        public bool IsDouble() { 
            if (this.FirstSquareValue==this.SecondSquareValue)return true;
            return false;
        }
    }
}