using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;


namespace DominoServiceTest
{
    [TestClass]
    public class PieceTest
    {

        /// <summary>
        /// prueba unitaria del constructor con valor limite cero
        /// </summary>
        [TestMethod]     
        public void PieceConstructor_valorLimite1()
        {

            Piece myPiece = new Piece(0, 0);

        }

        /// <summary>
        /// prueba unitaria del constructor con valor limite 6
        /// </summary>
        [TestMethod]      
        public void PieceConstructor_valorLimite2()
        {

            Piece myPiece = new Piece(6, 6);

        }


        /// <summary>
        /// prueba unitaria del constructor con valor limite -1
        /// </summary>
        [TestMethod]
        public void PieceConstructor_valorLimite3(){

            Piece myPiece = new Piece(-1, -1);

        }


        /// <summary>
        /// prueba unitaria del constructor con valor limite 7
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidPieceArgument), " Error en nombre null y posición null")]
        public void PieceConstructor_valorLimite4()
        {

            Piece myPiece = new Piece(7, 7);

        }

        /// <summary>
        /// el primer parametro del constructor debe ser siempre 
        /// menor igual que el segundo, se prueba en el caso contrario
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidPieceArgument), " Error en nombre null y posición null")]
        public void PieceConstructor_valorLimite5()
        {

            Piece myPiece = new Piece(6,5);

        }

        /// <summary>
        /// el primer parametro del constructor debe ser siempre 
        /// </summary>
        [TestMethod]
        public void Piece_ToInt()
        {

            Piece myPiece = new Piece(2,4);
            // //this.FirstSquareValue * 10 + this.SecondSquareValue
            int valor = ((2 * 10) + 4);
            Assert.AreEqual(valor, myPiece.ToInt());

        }

    }

}
