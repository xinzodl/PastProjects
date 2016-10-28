/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  PlayerData
 *  Descripción de la clase:
 *  Clase que sirve para guardar los datos de un jugador, que es necesario pasar a los clientes.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;


namespace DominoService
{
    [DataContract]
    public class PlayerData
    {
        [DataMember]
        public string nombre;
        [DataMember]
        public int posicion;

        public PlayerData()
        {
            nombre = null;
            posicion = -1;
        }

        public PlayerData(string PlayerName, int PlayerPosition)
        {
            if (PlayerName == null)
                throw new ExcepcionInvalidPlayerArguments("El nombre es nulo");
            else if (PlayerPosition > 4 || PlayerPosition < 0)
            {
                throw new ExcepcionInvalidPlayerArguments("La posicion no es correcta");
            }
            else if (PlayerName.Length == 0)
            {
                throw new ExcepcionInvalidPlayerArguments("El nombre esta vacio");
            }
            else
            {
                nombre = PlayerName;
                posicion = PlayerPosition;
            }

        }
    }
}