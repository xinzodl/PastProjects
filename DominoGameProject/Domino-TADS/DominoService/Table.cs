using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService{

    public class Table{
        private bool GameHasStarted = false;
        private List<Player> PlayersRegistered = new List<Player>();
        private int MAX_PLAYERS = 4;
        private static Table table= new Table();
        private Pile PiecesPile;
        private int PlayerWhoStarts = 0; // Posición del Jugador que inicia la partida.
        private Piece CurrentStartPiece = new Piece(-1, -1);
        private int CurrentStartPiecePosition = -1;
        private int playerWhoHasTurn;
        //METHODS

        //
        
        public static Table getInstance(){
            return table;
        }

        public bool IsGameStarted(){
            return this.GameHasStarted;
        }

        public Pile getPile(){
            return this.PiecesPile;
        }

        public bool TableIsNotFull(){
            return this.PlayersRegistered.Count < MAX_PLAYERS;
        }

        private void StartGame(){
            GameHasStarted = true;
            Table.getInstance().PiecesPile = new Pile();

            AssignTokensToPlayers();

            DominoService.getInstance().NotifyTokensToPlayers(PlayersRegistered);

            DominoService.getInstance().PutInitialPieceAlert(PlayersRegistered, CurrentStartPiece.ToInt(), CurrentStartPiecePosition, PlayerWhoStarts);

            // Quita de las Piezas disponibles del Jugador con la que se empieza
            PlayersRegistered[PlayerWhoStarts].getHand().getPieces().Remove(CurrentStartPiece);
            this.playerWhoHasTurn = (PlayerWhoStarts+1)%4;

            DominoService.getInstance().WhoHasTurn(PlayersRegistered, playerWhoHasTurn);

        }

        private void AssignTokensToPlayers(){
        #if DEBUG
                    AssignTokensMyOrder();
        #else
                    AssignTokensRamdomly();
        #endif

        }
        private void AssignTokensRamdomly(){
            // Se reparte a cada Jugador siete Piezas                   
            for (int p = 0; p < PlayersRegistered.Count; p++){
                for (int i = 0; i < Hand.getMaxSize(); i++){
                    Piece AuxiliarRandomPiece = Table.getInstance().getPile().GetRandomPiece();
                    PlayersRegistered[p].GivePiece(AuxiliarRandomPiece);

                    // Si la Pieza es un doble
                    if (AuxiliarRandomPiece.IsDouble()){
                        // Si es un doble mayor que el actual, se actualiza la Pieza de empiece y el Jugador que la tiene
                        if (AuxiliarRandomPiece.secondSquareValue > this.CurrentStartPiece.secondSquareValue){
                            this.CurrentStartPiece = AuxiliarRandomPiece;
                            this.CurrentStartPiecePosition = i;
                            this.PlayerWhoStarts = p;
                        }
                    }
                }
            }
        }
        //Método DEBUG
        private void AssignTokensMyOrder(){
            // Se reparte a cada Jugador siete Piezas                   
            for (int p = 0; p < PlayersRegistered.Count; p++){
                for (int i = 0; i < Hand.getMaxSize(); i++){
                    Piece AuxiliarPiece = Table.getInstance().getPile().GetFirstPiece();
                    PlayersRegistered[p].GivePiece(AuxiliarPiece);

                    // Si la Pieza es un doble
                    if (AuxiliarPiece.IsDouble()){
                        // Si es un doble mayor que el actual, se actualiza la Pieza de empiece y el Jugador que la tiene
                        if (AuxiliarPiece.secondSquareValue > this.CurrentStartPiece.secondSquareValue){
                            this.CurrentStartPiece = AuxiliarPiece;
                            this.CurrentStartPiecePosition = i;
                            this.PlayerWhoStarts = p;
                        }
                    }
                }
            }
        }

        public void SitPlayer(string PlayerName, IDominoServiceCallbackContract Callback){
            int PlayerPosition = PlayersRegistered.Count;

            // Se añade el nuevo jugador a la lista de los sentados en la mesa
            this.PlayersRegistered.Add(new Player(PlayerName, PlayerPosition, Callback));

            // Ha podido sentar al nuevo jugador y se notifica a todos los sentados los datos del nuevo jugador
            DominoService.getInstance().NotifyNewPlayer(PlayersRegistered, Callback, PlayerPosition, PlayerName);

            

            // La mesa está completa y el juego todavía no ha empezado
            if (!this.TableIsNotFull() && !this.IsGameStarted()){
                StartGame();
            }
        }

        public bool IsNameRegistered(string PlayerName){
            bool RepeatedName = false;
            int index = 0;
            while ((index < PlayersRegistered.Count) && (RepeatedName == false)){
                if (PlayersRegistered[index].getName() == PlayerName){
                    RepeatedName = true;
                }
                index++;
            }
            return RepeatedName;
        }
        

    }
}