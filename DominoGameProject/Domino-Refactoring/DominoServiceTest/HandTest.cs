using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class HandTest
    {
        /// <summary>
        /// prueba unitaria del metodo Add Piece parametro null
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidHandArgument), " Error en nombre null y posición null")]
        public void AddPiece_Null()
        {

            Hand myHand = new Hand();
            myHand.AddPiece (null ); 


        }
    }
}
