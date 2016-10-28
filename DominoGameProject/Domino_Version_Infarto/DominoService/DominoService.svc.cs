/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  DominoService
 *  Descripción de la clase:
 *  Clase que recibe las llamadas de los clientes al servidor, y redirigepara que sean tratadas en mesa.
 *  Además implementa las llamadas de comunicacion de vuelta al cliente (Callback).
 */

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
    public class DominoService : IDominoService
    {
        public void RequestPutPiece(PieceData data) { 
            Table.getTable().RequesPutPiece(data);
        }
		public void RequestSitPlayer(string PlayerName)
		{
			IDominoServiceCallbackContract Callback = OperationContext.Current.GetCallbackChannel<IDominoServiceCallbackContract>();

            try
            {
                Table.getTable().RequestSitPlayer(PlayerName, Callback);

                int PlayerPosition = Table.getTable().playersRegistered.Count;
                // Ha podido sentar al nuevo jugador y se notifica a todos los sentados los datos del nuevo jugador
                //Callback.NewPlayerJoined(PlayerName, PlayerPosition);
                PlayerData jugadorNuevo = new PlayerData(PlayerName, PlayerPosition - 1);
                Callback.NewPlayerJoined(jugadorNuevo);
                foreach (Player PlayerRegistered in Table.getTable().playersRegistered)
                {
                    PlayerData jugadorRegistrado = new PlayerData(PlayerRegistered.playerName, PlayerRegistered.tablePosition);
                    PlayerRegistered.PlayerCallbackChannel.NewPlayerJoined(jugadorNuevo);
                    Callback.NewPlayerJoined(jugadorRegistrado);
                    //PlayerRegistered.PlayerCallbackChannel.NewPlayerJoined(PlayerName, PlayerPosition-1);
                    //Callback.NewPlayerJoined(PlayerRegistered.playerName, PlayerRegistered.tablePosition);
                }

                if (Table.getTable().playersRegistered.Count == Table.getTable().GetMAX_PLAYERS() && !Table.getTable().gameHasStarted)
                {
                    Table.getTable().StartGame();    
                }
            }
            catch (ExcepcionNombreRepetido)
            {
                Callback.PlayerNotSat("Ya hay un jugador sentado a la mesa con ese nombre");
            }
            catch (ExcepcionMesaLlena)
            {
                Callback.PlayerNotSat("La mesa ya tiene todas las posiciones ocupadas");
            }

		}

        public void NotifyFirstPiece(List<Player> PlayersRegistered, Piece CurrentStartPiece, int CurrentStartPiecePosition, int PlayerWhoStarts)
        {
            // Se notifica a los jugadores la ficha puesta de inicio y quien la pone 
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                //mesa.PlayersRegistered[j].PlayerCallbackChannel.PutPiece(mesa.CurrentStartPiece.ToInt(), mesa.CurrentStartPiecePosition, mesa.PlayerWhoStarts);
                PieceData pieza = new PieceData(CurrentStartPiece.ToInt(), CurrentStartPiecePosition, PlayerWhoStarts);
                PlayersRegistered[j].PlayerCallbackChannel.PutPiece(pieza);
            }
            // Quita de las Piezas disponibles del Jugador con la que se empieza
            
            Table.getTable().playersRegistered[Table.getTable().playerWhoStarts].PlayerPieces.Pieces.Remove(Table.getTable().currentStartPiece);
        }

        public void NotifyTokensToPlayers(List <Player>PlayersRegistered)
        {
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                for (int k = 0; k < PlayersRegistered.ElementAt(j).PlayerPieces.Pieces.Count; k++)
                {
                    for (int z = 0; z < PlayersRegistered.Count; z++)
                    {
                        //mesa.PlayersRegistered.ElementAt(z).PlayerCallbackChannel.ShowPiece(mesa.PlayersRegistered.ElementAt(j).PlayerPieces.Pieces[k].ToInt(), k, mesa.PlayersRegistered.ElementAt(j).tablePosition);
                        PieceData pieza = new PieceData(PlayersRegistered.ElementAt(j).PlayerPieces.Pieces[k].ToInt(), k, PlayersRegistered.ElementAt(j).tablePosition);
                        PlayersRegistered.ElementAt(z).PlayerCallbackChannel.ShowPiece(pieza);

                    }
                }
            }
        }

        // Actualiza Marcadores a todas las Instancias
        public void ActualizarMarcadores(List<Player> PlayersRegistered, int[] Marcadores){
            foreach (Player PlayerRegistered in PlayersRegistered){
                PlayerRegistered.PlayerCallbackChannel.ActualizarMarcadores(Marcadores);
            }
        }

        // Es llamado cuando el jugador ha pasado turno
        public void PasoElTurno(){
            Table.getTable().PasoTurno();
        }

        public void ActualizaLosTurnos(List<Player> PlayersRegistered, int turnoEsDe, bool puedePoner, int puedo1, int puedo2){
            for (int j = 0; j < PlayersRegistered.Count; j++){
                PlayersRegistered[j].PlayerCallbackChannel.ElTurnoEsDe(turnoEsDe, puedePoner, puedo1, puedo2);
            }
        }

        // Limpia las ventanas de los clientes
        public void LimpiarClientes(List<Player> Clientes)
        {
            foreach (Player PlayerRegistered in Clientes){
                PlayerRegistered.PlayerCallbackChannel.LimpiarCliente();

            }
            System.Threading.Thread.Sleep(2000);


        }
        public void NewPieceOnTable(PieceData data, List<Player> PlayersRegistered)
        {
            foreach (Player player in PlayersRegistered) {
                if (player.tablePosition != data.jugador) {
                    player.PlayerCallbackChannel.NewPieceOnTable(data);
                }
            }
        }

        public void EndGame(List<Player> PlayersRegistered, int[] Marcadores) {
            foreach (Player player in PlayersRegistered){
                player.PlayerCallbackChannel.EndGame(Marcadores);
            }
            Table.getTable().resetTable();
        }
        public void ColocaBot(int [] valors, Player playerBot) {
            playerBot.PlayerCallbackChannel.ColocaBot(valors,playerBot.tablePosition);
        }
    }

}
