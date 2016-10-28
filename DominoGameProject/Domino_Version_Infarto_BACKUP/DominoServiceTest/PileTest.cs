using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class PileTest
    {
        [TestMethod]
        public void testPiletest_Ordenada()
        {
            Pile pila = new Pile();
            Piece pieza00 = new Piece(0, 0);
            Piece pieza10 = pila.GetPiezaOrdenada();
            Assert.AreEqual(pieza00.FirstSquareValue, pieza10.FirstSquareValue);
            Assert.AreEqual(pieza00.FirstSquareValue, pieza10.FirstSquareValue);
            Piece pieza01 = new Piece(0, 1);
            Piece pieza11 = pila.GetPiezaOrdenada();
            Assert.AreEqual(pieza01.FirstSquareValue, pieza11.FirstSquareValue);
            Assert.AreEqual(pieza01.FirstSquareValue, pieza11.FirstSquareValue);
            Piece pieza02 = new Piece(1, 1);
            Piece pieza12 = pila.GetPiezaOrdenada();
            Assert.AreEqual(pieza02.FirstSquareValue, pieza12.FirstSquareValue);
            Assert.AreEqual(pieza02.FirstSquareValue, pieza12.FirstSquareValue);
        }
    }
}
