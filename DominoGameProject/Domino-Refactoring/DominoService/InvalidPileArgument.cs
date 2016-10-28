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
    /// clase InvalidPileArgument
    /// </summary>
    public class InvalidPileArgument :SystemException 
    {

       private string messageError;

        public InvalidPileArgument() { this.messageError = null; }
        public InvalidPileArgument(string message) { this.messageError = message; }
        public InvalidPileArgument(string message, System.Exception inner) { }

        public string MeesageError
        {
            get { return messageError; }
            
        }

        protected InvalidPileArgument(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext contex) { }
    }
}