using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoCliente;

namespace DominoClienteIUTest
{
    [TestClass]
    public class DominoTest
    {
        /// <summary>
        /// prueba unitario del los metodos CallBack del cliente
        /// prueba NewPlayerJoined nombre null
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidDominoArgument), " Error en nombre null y posición null")]
        public void NewPlayerJoined_Null()
        {

            Domino  myDomino = new Domino();
            myDomino.NewPlayerJoined(null,1);


        }
    }
}
