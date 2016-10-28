using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using DominoService;
using System.Linq;

namespace DominoServiceTest
{

    [TestClass]
    public class PileTest{
        /*
         * Vamos a realizar las siguientes pruebas
         *  - Constructor:
         *      La pila de fichas no contiene fichas duplicadas
         *      La pila de fichas tiene 28 fichas
         *  - GetPiece
         *      La pila retorna una excepción cuando no hay fichas disponibles
         *  - GetRandompiece
         *      La pila retorna una excepción cuando no hay fichas disponibles
         */
        /* Fichas creadas correctamente */
        [TestMethod]
        public void Pile_Constructor_Test_Correct_Pieces_Created(){

            Pile pile = new Pile();
        }
        /* Número de fichas creadas es el correcto */
        [TestMethod]
        public void Pile_Constructor_Test_CorrectNumber_Of_Pieces(){

            Pile pile = new Pile();

        }
        /* Solicitamos 29 fichas */
        [TestMethod]
        [ExpectedException(typeof(PileException), "There are two pieces with the same value.")]
        public void Pile_Get_First_Piece_29_times(){

            Pile pile = new Pile();
            int i;
            for (i = 0; i < 28; i++) pile.GetFirstPiece();

            pile.GetFirstPiece();
        }
        /* solicitamos 29 fichas con el método random */
        [TestMethod]
        [ExpectedException(typeof(PileException), "There are two pieces with the same value.")]
        public void Pile_Get_Random_Piece_29_times()
        {

            Pile pile = new Pile();
            int i;
            for (i = 0; i < 28; i++) pile.GetRandomPiece();

            pile.GetRandomPiece();
        }
    }
}
