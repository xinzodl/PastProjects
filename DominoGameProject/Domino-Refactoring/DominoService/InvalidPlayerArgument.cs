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
    /// clase InvalidPlayerArgument
    /// </summary>
    public class InvalidPlayerArgument : SystemException 
    {
        private string messageError;

        public InvalidPlayerArgument() { this.messageError = null; }
        public InvalidPlayerArgument(string message) { this.messageError = message; }
        public InvalidPlayerArgument(string message, System.Exception inner) { }

        public string MeesageError
        {
            get { return messageError; }
            
        }

        protected InvalidPlayerArgument(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext contex) { }

    }
}