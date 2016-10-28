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
using System.Windows.Forms;
using DominoCliente.DominoServiceReference;
using DominoService;

namespace DominoCliente
{
    /// <summary>
    /// clase Receiver, se encarga de recibir los
    /// mensajes y objetos del servidor y mostrarlos
    /// en la interfaz del usuario.
    /// </summary>
    public class Receiver : IDominoServiceCallback
    {
      
        Domino   domino;

        /// <summary>
        /// contructor de la clase, recibe como
        /// parametro la instancia de la interfaz del usuario.
        /// </summary>
        public Receiver(Domino domino)
        {
            this.domino = domino;
        }

        /// <summary>
        /// metodo usado para la eliminación de la ficha puesta en la mesa de otro jugador
        /// </summary>
        public void ShowPieceUpdate(PieceData piece)
        {

            /// <summary>
            ///Se muestra al jugador una pieza que se encuentra en su poder
            /// </summary>           
            domino.PaintPieceInPlayerHandUpdate(piece.PieceValue, piece.PiecePosition, piece.PlayerPosition);

            /// <summary>
            /// Se actualiza el valor de las piezas de las que dispone el jugador
            /// </summary>           
            domino.DecrementPlayerAvailablePieces(piece.PlayerPosition);
        }


        /// <summary>
        /// CALLBACK METHODS
        /// se encarga de enviar a la interfaz del usuario
        /// un objeto que contiene los datos del jugador
        /// </summary>
        public void NewPlayerJoined(PlayerData player)
        {
            domino.NewPlayer(player.PlayerName, player.PlayerPosition);
        }

        /// <summary>
        /// CALLBACK METHODS
        /// se encarga de enviar a la interfaz del usuario
        /// los mensajes de error al sentar un jugador a la mesa.
        /// </summary>
        public void PlayerNotSat(string ErrorMessage)
        {
            domino.MessageError(ErrorMessage);
        }

        /// <summary>
        /// CALLBACK METHODS
        /// se encarga de enviar al jugador de la mesa
        /// un objeto que contiene los datos de la pieza de domino
        /// del jugador.
        /// </summary>
        public void ShowPiece(PieceData piece)
        {

            /// <summary>
            ///Se muestra al jugador una pieza que se encuentra en su poder
            /// </summary>           
            domino.PaintPieceInPlayerHand(piece.PieceValue, piece.PiecePosition, piece.PlayerPosition);

            /// <summary>
            /// Se actualiza el valor de las piezas de las que dispone el jugador
            /// </summary>           
            domino.IncrementPlayerAvailablePieces(piece.PlayerPosition);
        }
        //Tenemos X, Y, Valor de la ficha, valor de la ficha anterior y posición del jugador
        public void PuttedPiece(PieceData piece){
        
        }

        /// <summary>
        /// CALLBACK METHODS
        /// se encarga de enviar a la interfaz del usuario
        /// un objeto que contiene los datos de la pieza de domino
        /// del jugador y los muestra en la mesa.
        /// </summary>
        public void PutPiece(PieceData piece)
        {
            /// <summary>
            /// Deja de mostrar la Pieza en el jugador que la acaba de poner
            /// </summary>        
            domino.HidePieceInHand(piece.PiecePosition, piece.PlayerPosition);
            /// <summary>
            /// Pone en la mesa la Pieza con la que tiene que empezar el Jugador.
            /// </summary>
            
            domino.PutOnTablePieceInitial(piece.StartPieceInt);
            /// <summary>
            /// Se actualiza el valor de las piezas de las que dispone el jugador
            /// </summary>
            
            domino.UpdatePiecePlayer(piece.PlayerPosition);
        }
 
        /// <summary>
        /// CALLBACK METHODS
        /// metodo que coloca la ficha seleccionada por cualquier otro
        /// jugador, en la mesa.
        /// </summary>
        public void NewPutPieceOnPanel(PieceData piece)
        {

            domino.PutNewOnTablePiece(piece);

        }

        /// <summary>
        /// CALLBACK METHODS
        /// metodo que actualiza el turno, habilitando o deshabillitando las acciones
        /// </summary>
        public void ElTurnoEsDe(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2)
        {
            domino.ActualizaTurno(posicionJugadorConTurno, puedePoner, puedo1, puedo2);
        }

        public void ActualizarMarcadores(int[] Marcadores) {
            domino.ActualizarMarcadores(Marcadores);
        }

        public void LimpiarCliente() { 
        
        }
    }
}
