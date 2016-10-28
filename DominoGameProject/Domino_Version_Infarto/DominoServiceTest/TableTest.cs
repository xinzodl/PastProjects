#define MYTEST
using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class TableTest
    {
        [TestMethod]
        [ExpectedException(typeof(ExcepcionMesaLlena), "Mesa llena")]
        public void testTable_sentarJugador()
        {
            Table mesa = new Table();
            mesa.RequestSitPlayer("a", null);
            Assert.AreEqual(mesa.playersRegistered[0].playerName, "a");
            mesa.RequestSitPlayer("k", null);
            mesa.RequestSitPlayer("e", null);
            mesa.RequestSitPlayer("b", null);
            mesa.RequestSitPlayer("a2", null);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionNombreRepetido), "Jugador ya sentado")]
        public void testTable_sentarJugadorRepetido()
        {
            Table mesa = new Table();
            mesa.RequestSitPlayer("a", null);
            mesa.RequestSitPlayer("a", null);
        }


        [TestMethod]
        public void testTable_startGame()
        {
            Table mesa = new Table();
            mesa.RequestSitPlayer("1", null);
            mesa.RequestSitPlayer("2", null);
            mesa.RequestSitPlayer("3", null);
            mesa.RequestSitPlayer("4", null);
            try
            {
                mesa.StartGame();
            }
            catch (NullReferenceException) { 
            }
            Assert.AreEqual(mesa.playersRegistered[0].PlayerPieces.Pieces[6].FirstSquareValue, 0);
            Assert.AreEqual(mesa.playersRegistered[0].PlayerPieces.Pieces[6].SecondSquareValue, 3);

            Assert.AreEqual(mesa.playersRegistered[1].PlayerPieces.Pieces[0].FirstSquareValue, 1);
            Assert.AreEqual(mesa.playersRegistered[1].PlayerPieces.Pieces[0].SecondSquareValue, 3);

          
        }
         [TestMethod]
        public void testTable_resetTable() 
        {
            Table.getTable().RequestSitPlayer("1", null);
            Table.getTable().RequestSitPlayer("2", null);
            Table.getTable().RequestSitPlayer("3", null);
            Table.getTable().RequestSitPlayer("4", null);
            Table.getTable().resetTable();
            Assert.AreEqual(0, Table.getTable().playersRegistered.Count);
        }

         [TestMethod]
         public void testTable_getTable()
         {
             Assert.AreNotEqual(null,Table.getTable());

             Assert.AreEqual(typeof(Table),Table.getTable().GetType());
             
         }
    }
}
