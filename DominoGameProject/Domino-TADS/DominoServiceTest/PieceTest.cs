using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;

namespace DominoServiceTest
{
    /*
     * Vamos a realizar las siguientes pruebas
     *  - Constructor:
     *      * FirstValue
     *          > -1, 0, 1, 5, 6, 7
     *      * SecondValue
     *          > -1, 0, 1, 5, 6, 7
     */
    [TestClass]
    public class PieceTest{
        /* Comprobamos los calores límite que podemos introducir en el constructor de la pieza */
        [TestMethod]
        [ExpectedException(typeof(PieceException), "First value out of range.")]
        public void Piece_ConstructorTest_FirstValue__1(){

            Piece piece = new Piece(-1,0);
        }

        [TestMethod]
        public void Piece_ConstructorTest_FirstValue_0(){

            Piece piece = new Piece(0, 0);
        }

        [TestMethod]
        public void Piece_ConstructorTest_FirstValue_1(){

            Piece piece = new Piece(1, 0);
        }

        [TestMethod]
        public void Piece_ConstructorTest_FirstValue_5(){

            Piece piece = new Piece(5, 0);
        }

        [TestMethod]
        public void Piece_ConstructorTest_FirstValue_6(){

            Piece piece = new Piece(6, 0);
        }

        [TestMethod]
        [ExpectedException(typeof(PieceException), "First value out of range.")]
        public void Piece_ConstructorTest_FirstValue_7(){

            Piece piece = new Piece(7, 0);
        }

        [TestMethod]
        [ExpectedException(typeof(PieceException), "Second value out of range.")]
        public void Piece_ConstructorTest_SecondtValue__1(){

            Piece piece = new Piece(0, -1);
        }

        [TestMethod]
        public void Piece_ConstructorTest_SecondValue_0(){

            Piece piece = new Piece(0, 0);
        }

        [TestMethod]
        public void Piece_ConstructorTest_SecondValue_1(){

            Piece piece = new Piece(0, 1);
        }

        [TestMethod]
        public void Piece_ConstructorTest_SecondValue_5(){

            Piece piece = new Piece(0, 5);
        }

        [TestMethod]
        public void Piece_ConstructorTest_SecondValue_6(){

            Piece piece = new Piece(0, 6);
        }

        [TestMethod]
        [ExpectedException(typeof(PieceException), "Second value out of range.")]
        public void Piece_ConstructorTest_SecondValue_7(){

            Piece piece = new Piece(0, 7);
        }
        /* Comprobamos que la pieza de inicio (-1, -1) se puede crear correctamentes */
        [TestMethod]
        public void Piece_ConstructorTest_Start_Piece(){
        
            Piece piece = new Piece(-1, -1);
        }
    }
}
