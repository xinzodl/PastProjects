using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    /// <summary>
    /// Descripción resumida de HandTest
    /// </summary>
    [TestClass]
    public class HandTest{
        /* Insertamos una pieza nula en la mano */
        [TestMethod]
        [ExpectedException(typeof(InvalidPieceAtHandException), "Piece to add can't be null.")]
        public void Hand_Add_Piece_Test_Null(){

            Hand hand = new Hand();
            hand.AddPiece(null);
        }
        /* Insertamos una pieza correcta en la mano */
        [TestMethod]
        public void Hand_Add_Piece_Test(){

            Hand hand = new Hand();
            Piece piece = new Piece(0, 0);
            hand.AddPiece(piece);
        }
        /* Comprobamos que la pieza que hemos insertado es la correcta */
        [TestMethod]
        public void Hand_Add_Piece_Test_Correct_Piece(){

            Hand hand = new Hand();
            Piece piece = new Piece(0, 0);
            hand.AddPiece(piece);
            if (!hand.getPieces().Contains(piece)){
                throw new AssertFailedException("The las piece added at the list is not there.");
            }
        }
    }
}
