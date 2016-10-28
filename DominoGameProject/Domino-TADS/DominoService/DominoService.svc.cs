using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace DominoService
{
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class DominoService : IDominoService{

        private static DominoService service = new DominoService();

        public static DominoService getInstance(){
            return service;
        }

		public void RequestSitPlayer(string PlayerName){

			IDominoServiceCallbackContract Callback = OperationContext.
                Current.GetCallbackChannel<IDominoServiceCallbackContract>();

            if (Table.getInstance().TableIsNotFull()){
                if (Table.getInstance().IsNameRegistered(PlayerName) == false){
                    Table.getInstance().SitPlayer(PlayerName, Callback);
                }
                else {
                    Callback.PlayerNotSat("Ya hay un jugador sentado a la mesa con ese nombre");
                }
            } else{
                Callback.PlayerNotSat("La mesa ya tiene todas las posiciones ocupadas");
            }
		}

        //Avisa a todos los jugadores con quien tiene el turno
        public void WhoHasTurn(List<Player> players, int playerWhoHasTurn){

        }


        public void NotifyTokensToPlayers(List<Player> PlayersRegistered){

            PieceData pieceData;
            for (int j = 0; j < PlayersRegistered.Count; j++){
                Player player = PlayersRegistered.ElementAt(j);

                for (int k = 0; k < PlayersRegistered.ElementAt(j).getHand().getPieces().Count; k++){
                    pieceData = new PieceData(player.getHand().getPieces()[k].ToInt(),k, player.getName(), player.getPosition());

                    for (int z = 0; z < PlayersRegistered.Count; z++){
                        // send piece to player who is being updated
                        PlayersRegistered.ElementAt(z).getCallback().ShowPiece(pieceData);
                    }
                }
            }
        }

        public void NotifyNewPlayer(List<Player> PlayersRegistered, IDominoServiceCallbackContract Callback, 
            int PlayerPosition, string PlayerName){

            PlayerData playerData = new PlayerData(PlayerName, PlayerPosition);
            Callback.NewPlayerJoined(playerData);

            foreach (Player PlayerRegistered in PlayersRegistered){
                PlayerData PlayerRegisteredData = new PlayerData(PlayerRegistered.getName(), PlayerRegistered.getPosition());
                PlayerRegistered.getCallback().NewPlayerJoined(playerData);
                Callback.NewPlayerJoined(PlayerRegisteredData);
            }
        }
        public void PutInitialPieceAlert(List<Player> PlayersRegistered, int CurrentStartPiece, 
            int CurrentStartPiecePosition, int PlayerWhoStarts){

            // Se notifica a los jugadores la ficha puesta de inicio y quien la pone 
            for (int j = 0; j < PlayersRegistered.Count; j++){
                PlayersRegistered[j].getCallback()
                    .PutPiece(CurrentStartPiece, CurrentStartPiecePosition, PlayerWhoStarts);
            }
        }
    }
}
