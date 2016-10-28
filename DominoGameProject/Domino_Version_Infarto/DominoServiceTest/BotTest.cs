using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DominoService;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DominoServiceTest
{
    [TestClass]
    public class BotTest
    {
        [TestMethod]
        public void PutPieceBotTest0()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Piece piece1 = new Piece(0, 6);
            Piece piece2 = new Piece(0, 3);
            myPlayer.PlayerPieces.AddPiece(piece1);
            myPlayer.PlayerPieces.AddPiece(piece2);
            Piece piece3 = new Piece(6, 6);

            List<Piece> lista = new List<Piece>();
            lista.Add(piece3);

            Bot botPruebas = new Bot();
            int[] ret = botPruebas.PutPieceBot(myPlayer, lista);
            Assert.AreEqual(6, ret[0]);
            Assert.AreEqual(66, ret[1]);
        }

        /// <summary>
        /// prueba unitaria del metodo ValoresDisponibles para colocar por la derecha
        /// </summary>
        [TestMethod]
        public void PutPieceBotTest1()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Piece piece1 = new Piece(0, 6);
            Piece piece2 = new Piece(0, 3);
            myPlayer.PlayerPieces.AddPiece(piece1);
            myPlayer.PlayerPieces.AddPiece(piece2);
            Piece piece3 = new Piece(5, 6);
            Piece piece4 = new Piece(6, 6);

            List<Piece> lista = new List<Piece>();
            lista.Add(piece3);
            lista.Add(piece4);

            Bot botPruebas = new Bot();
            int[] ret = botPruebas.PutPieceBot(myPlayer, lista);
            Assert.AreEqual(6, ret[0]);
            Assert.AreEqual(66, ret[1]);
        }

        /// <summary>
        /// prueba unitaria del metodo ValoresDisponibles para colocar por la izquierda
        /// </summary>
        [TestMethod]
        public void PutPieceBotTest2()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Piece piece1 = new Piece(0, 5);
            Piece piece2 = new Piece(0, 3);
            myPlayer.PlayerPieces.AddPiece(piece1);
            myPlayer.PlayerPieces.AddPiece(piece2);
            Piece piece3 = new Piece(5, 6);
            Piece piece4 = new Piece(6, 6);

            List<Piece> lista = new List<Piece>();
            lista.Add(piece3);
            lista.Add(piece4);

            Bot botPruebas = new Bot();
            int[] ret = botPruebas.PutPieceBot(myPlayer, lista);
            Assert.AreEqual(5, ret[0]);
            Assert.AreEqual(56, ret[1]);
        }

        /// <summary>
        /// prueba unitaria del metodo ValoresDisponibles para colocar por la izquierda
        /// </summary>
        [TestMethod]
        public void PutPieceBotTest3()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Piece piece1 = new Piece(0, 5);
            Piece piece5 = new Piece(1, 5);
            Piece piece2 = new Piece(0, 3);
            myPlayer.PlayerPieces.AddPiece(piece1);
            myPlayer.PlayerPieces.AddPiece(piece2);
            myPlayer.PlayerPieces.AddPiece(piece5);
            Piece piece3 = new Piece(5, 6);
            Piece piece4 = new Piece(6, 6);

            List<Piece> lista = new List<Piece>();
            lista.Add(piece3);
            lista.Add(piece4);

            Bot botPruebas = new Bot();
            int[] ret = botPruebas.PutPieceBot(myPlayer, lista);
            Assert.AreEqual(5, ret[0]);
            Assert.AreEqual(56, ret[1]);
        }
    }
}
