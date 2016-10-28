using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class PileTest
    {
        /// <summary>
        /// prueba unitaria del metodo GetRandomPiece
        /// no debe retornar null
        /// </summary>
        [TestMethod]
       
        public void PileMethod_GetRandomPiece()
        {

            Pile myPile = new Pile();
            Piece prueba = myPile.GetRandomPiece();
            Assert.AreNotEqual(null , prueba); 

        }
    }
}
