using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoSeviceTest
{
    [TestClass]
    public class PlayerTest
    {
        //Prueba Unitaria del constructor Player con nombre nulo
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Nombre de jugador nulo")]
        public void testConstructorPlayer_NullName()
        {
            Player myPlayer = new Player(null, 1, null);
        }

        //Prueba Unitaria del constructor Player con nombre vacio
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Nombre de jugador vacio")]
        public void testConstructorPlayer_VoidName()
        {
            Player myPlayer = new Player("", 1, null);
        }

        //Prueba Unitaria del constructor Player con posicion incorrecta <0
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Posicion incorrecta")]
        public void testConstructorPlayer_WrongPosition0()
        {
            Player myPlayer = new Player("j", -1, null);
        }

        //Prueba Unitaria del constructor Player con posicion incorrecta >3
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Posicion incorrecta")]
        public void testConstructorPlayer_WrongPosition1()
        {
            Player myPlayer = new Player("j", 4, null);
        }

        //Prueba Unitaria del constructor Player con nombre correcto y posicion valida
        [TestMethod]
        public void testConstructorPlayer_Correct()
        {
            Player myPlayer = new Player("j", 0, null);
            Assert.AreEqual("j", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        //Prueba Unitaria del constructor Player con nombre reservado para bot y posicion valida
        [TestMethod]
        public void testConstructorPlayer_BotName1()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("fernando", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        //Prueba Unitaria del constructor Player con nombre reservado para bot y posicion valida
        [TestMethod]
        public void testConstructorPlayer_BotName2()
        {
            Player myPlayer = new Player("gumersindo", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("gumersindo", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        //Prueba Unitaria del constructor Player con nombre reservado para bot y posicion valida
        [TestMethod]
        public void testConstructorPlayer_BotName3()
        {
            Player myPlayer = new Player("rodolfo", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("rodolfo", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        //Prueba Unitaria del constructor Player con nombre reservado para bot y posicion valida
        [TestMethod]
        public void testConstructorPlayer_BotName4()
        {
            Player myPlayer = new Player("genaro", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("genaro", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        /*[TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Canal es nulo")]
        public void testConstructorPlayer_NullChannel()
        {
            Player myPlayer = new Player("k", 1, null);
        }*/

    }
}
