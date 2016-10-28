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
    /// clase Hand, que representa las Fichas que tiene un Jugador 
    /// </summary>
    
    public class Hand
    {

        public List<Piece> Pieces;

        public Hand()
        {
            Pieces = new List<Piece>();
        }

        /// <summary>
        /// metodo que agrega una pieza a la mano.
        /// </summary> 
        public void AddPiece(Piece NewPiece)
        {
            if (NewPiece == null)
            {
                throw new InvalidHandArgument("la pieza no es valida");
            }
            else
            {
                this.Pieces.Add(NewPiece);
            }
            
        }


    }
}