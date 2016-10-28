/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  ExcepcionNullPiece
 *  Descripción de la clase:
 *  Clase de excepcion para NullPiece.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class ExcepcionNullPiece : SystemException
    {
        private string messageError;
        public string MessageError
        {
            get
            {
                return messageError;
            }
        }
        public ExcepcionNullPiece() { }
        public ExcepcionNullPiece(string message) { }
        public ExcepcionNullPiece(string message, System.Exception inner) { this.messageError = message; }

        //constructor needed for serialization
        protected ExcepcionNullPiece(System.Runtime.Serialization.SerializationInfo info, System.Runtime.Serialization.StreamingContext hola) { }
    }
}