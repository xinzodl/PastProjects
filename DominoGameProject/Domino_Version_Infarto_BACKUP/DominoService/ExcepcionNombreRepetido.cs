/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  ExcepcionNombreRepetido
 *  Descripción de la clase:
 *  Clase de excepcion para NombreRepetido.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class ExcepcionNombreRepetido: SystemException
    {
        private string messageError;
        public string MessageError
        {
            get
            {
                return messageError;
            }
        }
        public ExcepcionNombreRepetido() { }
        public ExcepcionNombreRepetido(string message) { }
        public ExcepcionNombreRepetido(string message, System.Exception inner) { this.messageError=message;}

        //constructor needed for serialization
        protected ExcepcionNombreRepetido(System.Runtime.Serialization.SerializationInfo info, System.Runtime.Serialization.StreamingContext hola) { }
    }
}