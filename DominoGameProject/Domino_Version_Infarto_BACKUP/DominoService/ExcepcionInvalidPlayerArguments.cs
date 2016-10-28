/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  ExcepcionInvalidPlayerArguments
 *  Descripción de la clase:
 *  Clase de excepcion para InvalidPlayerArguments.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class ExcepcionInvalidPlayerArguments: SystemException
    {

        private string messageError;
        public string MessageError
        {
            get
            {
                return messageError;
            }
        }
        public ExcepcionInvalidPlayerArguments() { }
        public ExcepcionInvalidPlayerArguments(string message) { }
        public ExcepcionInvalidPlayerArguments(string message, System.Exception inner) { this.messageError=message;}

        //constructor needed for serialization
        protected ExcepcionInvalidPlayerArguments(System.Runtime.Serialization.SerializationInfo info, System.Runtime.Serialization.StreamingContext hola) { }

    }
}