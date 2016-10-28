/* ---------------------------------------
 * Autores:
 * Marcos Alejandro Pariona
 * Jéssica Zamora Castillo.
 * Grupo de prácticas: Grupo 6.
 * Fecha: 20/04/2015.
 * TADS 2014/2015. Proyecto Dominó.
-----------------------------------------*/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /// <summary>
    /// clase InvalidPieceArgument
    /// </summary>
    public class InvalidPieceArgument : SystemException
    {
        private string messageError;

        public InvalidPieceArgument() { this.messageError = null; }
        public InvalidPieceArgument(string message) { this.messageError = message; }
        public InvalidPieceArgument(string message, System.Exception inner) { }

        public string MeesageError
        {
            get { return messageError; }

        }

        protected InvalidPieceArgument(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext contex) { }

    }
}