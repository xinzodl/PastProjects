using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;


namespace DominoServiceTest
{
    [TestClass]
    public class PieceDataTest
    {
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPieceData_NumerosMal_1()
        {
            PieceData myPlayer = new PieceData(-1, 1, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Numero de ficha no correcto")]
        public void testConstructorPieceData_NumerosMal_2()
        {
            PieceData myPlayer = new PieceData(67, 1, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Posicion de jugador incorrecta")]
        public void testConstructorPieceData_PosPlayerMal_1()
        {
            PieceData myPlayer = new PieceData(22, 1, -1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Posicion de jugador incorrecta")]
        public void testConstructorPieceData_PosPlayerMal_2()
        {
            PieceData myPlayer = new PieceData(22, 1, 5);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPieceArguments), "Posicion de ficha incorrecta")]
        public void testConstructorPieceData_PosPieceMal()
        {
            PieceData myPlayer = new PieceData(22, -1, 1);
        }

        [TestMethod]
        public void testConstructorPieceData_OK()
        {
            PieceData myPlayer = new PieceData(22, 1, 1);
            Assert.AreEqual(myPlayer.jugador, 1);
            Assert.AreEqual(myPlayer.posicionEnMano, 1);
            Assert.AreEqual(myPlayer.valor, 22);
        }

    }
}
