using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoSeviceTest
{
    [TestClass]
    public class PlayerDataTest
    {
        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Nombre de jugador nulo")]
        public void testConstructorPlayerData_NullName()
        {
            PlayerData myPlayer = new PlayerData(null, 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Nombre de jugador vacio")]
        public void testConstructorPlayerData_VoidName()
        {
            PlayerData myPlayer = new PlayerData("", 1);
        }

        [TestMethod]
        [ExpectedException(typeof(ExcepcionInvalidPlayerArguments), "Posicion incorrecta")]
        public void testConstructorPlayerData_WrongPosition()
        {
            PlayerData myPlayer = new PlayerData("j", -1);
        }

        [TestMethod]
        public void testConstructorPlayerData_OK()
        {
            PlayerData myPlayer = new PlayerData("j", 1);
            Assert.AreEqual(myPlayer.nombre, "j");
            Assert.AreEqual(myPlayer.posicion, 1);
        }
    }
}