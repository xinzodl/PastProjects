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
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using DominoCliente.DominoServiceReference;
using DominoService;

namespace DominoCliente
{
    /// <summary>
    /// clase Dispatcher, se encarga de las 
    /// peticiones de la interfaz al servidor
    /// </summary>
    public class Dispatcher
    {

        DominoServiceClient server;
        InstanceContext instanceContext;

        /// <summary>
        /// constructor de la clase, recive como
        /// parametro el objeto Receiver que es de tipo
        /// IDominoServiceCallback, también abre la
        /// conexión con el servidor.
        /// </summary>
        public Dispatcher(Receiver receiver)
        {
            instanceContext = new InstanceContext(receiver);
            server = new DominoServiceClient(instanceContext);
            server.Open();
        }

        /// <summary>
        /// envía petición al servidor de sentar
        /// un jugador a la mesa
        /// </summary>
        public void RequestSitPlayer(string PlayerName)
        {
            server.RequestSitPlayer(PlayerName);
        }
       

        /// <summary>
        /// envía la ficha seleccionada por el actual jugador, a
        /// todas las demas Instancias.
        /// </summary>
        public void RequestPutPiecePlayer(PieceData piece, PieceData prevPiece)
        {

            server.RequestPutPiecePlayer(piece, prevPiece);
           

        }


        /// <summary>
        /// se encarga de cerrar la conexión con el servidor.
        /// </summary>
        public void Close()
        {
            server.Close();
        }

        /// <summary>
        /// se encarga de pasar el turno
        /// </summary>
        public void PasoTurno(int miPosicion)
        {
            server.PasoElTurno(miPosicion);
        }

    }
}
