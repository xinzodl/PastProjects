/*
 * COPYRIGHT 2015 Ginés García Avilés - Luis Antonio Pérez Mulero
 *      TADS_DOMINO_2015
 *      TADS
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DominoCliente.DominoServiceReference;
using System.ServiceModel;

namespace DominoCliente
{
    class Receiver : IDominoServiceCallback
    {
        private static Receiver receiver;
        private Domino domino;
        private static InstanceContext instanceContext;



        private Receiver(Domino domino){
            this.domino = domino;
            instanceContext = new InstanceContext(this);
        }
        
        public static void setInstance(Domino domino) {
            if (receiver == null){
                receiver = new Receiver(domino);
            }
        }
        public static InstanceContext getInstance(){
            return instanceContext;
        }
        /*Callback Methods*/
        public void NewPlayerJoined(PlayerData player){
            this.domino.PaintPlayerJoined(player.Name, player.Position);
        }

        public void PlayerNotSat(string ErrorMessage) {
            this.domino.PlayerNotSat(ErrorMessage);
        }

        public void ShowPiece(PieceData piece){
            this.domino.paintPieceInPlayersHand(piece.Value, piece.Position, piece.PlayerPosition);
            this.domino.incrementPlayerAvailablePieces(piece.PlayerPosition);
        }


        public void PutPiece(int StartPieceInt, int PiecePosition, int PlayerPosition) {
            // Deja de mostrar la Pieza en el jugador que la acaba de poner
            this.domino.HidePieceInHand(PiecePosition, PlayerPosition);

            // Pone en la mesa la Pieza con la que tiene que empezar el Jugador.
            this.domino.PaintPieceInTable(StartPieceInt);

            // Se actualiza el valor de las piezas de las que dispone el jugador
            this.domino.DecrementPlayerAvailablePieces(PlayerPosition);
        }


    }
}
