using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;


namespace DominoServiceTest
{
    [TestClass]
    public class HandTest
    {
        [TestMethod]
        [ExpectedException(typeof(ExcepcionNullPiece), "Pieza es null")]
        public void testAddPieceHand_NullPiece()
        {
            Hand myHand = new Hand();
            myHand.AddPiece(null);
        }

        [TestMethod]
        public void testAddPieceHand_OK()
        {
            Hand myHand = new Hand();
            Piece pieza = new Piece(1, 1);
            myHand.AddPiece(pieza);
            Assert.AreEqual(pieza, myHand.Pieces[0]);
        }

    }
}
