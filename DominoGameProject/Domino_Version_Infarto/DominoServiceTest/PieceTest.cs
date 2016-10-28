using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;


namespace DominoServiceTest
{
    [TestClass]
    public class PieceTest
    {
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPiece_NumerosMal_1()
        {
            Piece myPiece = new Piece(-2, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPiece_NumerosMal_2()
        {
            Piece myPiece = new Piece(67, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPiece_NumerosMal_3()
        {
            Piece myPiece = new Piece(1, -2);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPiece_NumerosMal_4()
        {
            Piece myPiece = new Piece(1, 71);
        }

        [TestMethod]
        public void testConstructorPiece_OK()
        {
            Piece myPiece = new Piece(1, 5);
            Assert.AreEqual(myPiece.FirstSquareValue, 1);
            Assert.AreEqual(myPiece.SecondSquareValue, 5);
        }
        [TestMethod]
        public void testConstructorIDouble_True()
        {
            Piece myPiece = new Piece(5, 5);
            Assert.AreEqual(myPiece.FirstSquareValue, myPiece.SecondSquareValue);
            Assert.AreEqual(myPiece.IsDouble(), true);
        }
        [TestMethod]
        public void testConstructorIDouble_False()
        {
            Piece myPiece = new Piece(1, 5);
            Assert.AreNotEqual(myPiece.FirstSquareValue, myPiece.SecondSquareValue);
            Assert.AreEqual(myPiece.IsDouble(), false);
        }
    }
}
