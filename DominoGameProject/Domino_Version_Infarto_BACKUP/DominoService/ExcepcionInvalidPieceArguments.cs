/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  ExcepcionInvalidPieceArguments
 *  Descripción de la clase:
 *  Clase de excepcion para InvalidPieceArguments.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class ExcepcionInvalidPieceArguments : SystemException
    {

        private string messageError;
        public string MessageError
        {
            get
            {
                return messageError;
            }
        }
        public ExcepcionInvalidPieceArguments() { }
        public ExcepcionInvalidPieceArguments(string message) { }
        public ExcepcionInvalidPieceArguments(string message, System.Exception inner) { this.messageError = message; }

        //constructor needed for serialization
        protected ExcepcionInvalidPieceArguments(System.Runtime.Serialization.SerializationInfo info, System.Runtime.Serialization.StreamingContext hola) { }

    }
}