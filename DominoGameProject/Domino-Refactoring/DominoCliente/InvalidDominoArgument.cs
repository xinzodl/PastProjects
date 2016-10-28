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
using System.Text;
using System.Threading.Tasks;

namespace DominoCliente
{
    /// <summary>
    /// clase  InvalidDominoArgument 
    /// </summary>
    public class InvalidDominoArgument : SystemException
    {
        private string messageError;

        public InvalidDominoArgument() { this.messageError = null; }
        public InvalidDominoArgument(string message) { this.messageError = message; }
        public InvalidDominoArgument(string message, System.Exception inner) { }

        public string MeesageError
        {
            get { return messageError; }

        }

        protected InvalidDominoArgument(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext contex) { }

    }
}
