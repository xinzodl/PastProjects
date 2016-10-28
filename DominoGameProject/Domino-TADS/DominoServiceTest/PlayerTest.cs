using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    [TestClass]
    public class PlayerTest
    {
        // Valores que pueden tomar cada una de las variables y sobre los que vamos a realizar pruebas.
        /* Name: 
         *  - No puede ser nulo
         *  - Nombre correcto
         * Posicion:
         *  - posicion -1
         *  - posicion 0, 1, 2, 3
         *  - posicion 4
         *  Channel:
         *   - NULL
         */

        /* Name tests */


        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArguments), "Name can't be null.")]
        public void PlayerConstructorTest_NullName(){

            Player myPlayer = new Player(null, 1, null);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArguments), "Name can't be empty.")]
        public void PlayerConstructorTest_EmptyName(){

            Player myPlayer = new Player("", 1, null);
        }

        [TestMethod]
        public void PlayerConstructorTest_ValidName(){

            Player myPlayer = new Player("Pedro", 1, null);
        }

        /* Position tests */

        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArguments), "Invalid Position.")]
        public void PlayerConstructorTest_Position__1(){

            Player myPlayer = new Player("test", -1, null);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidPlayerArguments), "Invalid Position.")]
        public void PlayerConstructorTest_Position4(){

            Player myPlayer = new Player("test", 4, null);
        }

        [TestMethod]
        public void PlayerConstructorTest_Position0(){

            Player myPlayer = new Player("test", 0, null);
        }

        [TestMethod]
        public void PlayerConstructorTest_Position1(){

            Player myPlayer = new Player("test", 1, null);
        }

        [TestMethod]
        public void PlayerConstructorTest_Position2(){

            Player myPlayer = new Player("test", 2, null);
        }

        [TestMethod]
        public void PlayerConstructorTest_Position3(){

            Player myPlayer = new Player("test", 3, null);
        }
    }
}
