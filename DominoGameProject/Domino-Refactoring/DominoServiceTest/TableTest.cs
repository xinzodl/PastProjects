using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class TableTest
    {
        /// <summary>
        /// prueba unitaria del RequestSitPlayer con nombre no valido
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(InvalidTableArgument), " Error en nombre null")]
        public void RequestSitPlayer_NomInavlid()
        {
            Table myTable = new Table(null);
            myTable.RequestSitPlayer(null, null); 

        }
    }
}
