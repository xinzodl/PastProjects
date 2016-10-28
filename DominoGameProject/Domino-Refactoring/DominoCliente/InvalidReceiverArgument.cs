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
    /// clase  InvalidReceiverArgument
    /// </summary>
    class InvalidReceiverArgument : SystemException
    {

         private string messageError;

        public InvalidReceiverArgument() { this.messageError = null; }
        public InvalidReceiverArgument(string message) { this.messageError = message; }
        public InvalidReceiverArgument(string message, System.Exception inner) { }

        public string MeesageError
        {
            get { return messageError; }

        }

        protected InvalidReceiverArgument(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext contex) { }

    }
}
