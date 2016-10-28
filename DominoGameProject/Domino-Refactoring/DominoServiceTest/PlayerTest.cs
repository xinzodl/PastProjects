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
    public class PlayerTest : SystemException 
    {

        /// <summary>
        /// prueba unitaria del constructor con nombre valido
        /// y posición No valida2
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArgument), "Error en la posicion")]
        public void PlayerConstructor_PosInavlid1()
        {
            Player myPlayer = new Player("jessi", 4, null);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre valido
        /// y posición No valida2
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArgument), "Error en la posicion")]
        public void PlayerConstructor_PosInavlid2()
        {
            Player myPlayer = new Player("jessi", -1, null);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre valido
        /// y posición valida
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_NamePos1()
        {
            Player myPlayer = new Player("jessi", 0, null);
            Assert.AreEqual("jessi", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre valido y no bot
        /// y posición valida
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_NamePos2()
        {
            Player myPlayer = new Player("jessi", 3, null);
            Assert.AreEqual("jessi", myPlayer.playerName);
            Assert.AreEqual(3, myPlayer.tablePosition);
            Assert.AreEqual(false, myPlayer.playerBot);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre null
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArgument) ," Error en nombre null")]
        public void PlayerConstructor_NullName()
        {
            Player myPlayer = new Player(null ,0, null);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre reservado de bot
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_BotName1()
        {
            Player myPlayer = new Player("fernando", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("fernando", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre reservado de bot
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_BotName2()
        {
            Player myPlayer = new Player("gumersindo", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("gumersindo", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre reservado de bot
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_BotName3()
        {
            Player myPlayer = new Player("rodolfo", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("rodolfo", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }

        /// <summary>
        /// prueba unitaria del constructor con nombre reservado de bot
        /// </summary>
        [TestMethod]
        public void PlayerConstructor_BotName4()
        {
            Player myPlayer = new Player("genaro", 0, null);
            Assert.AreEqual(true, myPlayer.playerBot);
            Assert.AreEqual("genaro", myPlayer.playerName);
            Assert.AreEqual(0, myPlayer.tablePosition);
        }
    }
}
