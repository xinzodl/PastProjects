using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace DominoService
{

    /// <summary>
    /// clase PlayerData, Envía datos deljugador a la interfaz
    /// </summary>
    [DataContract]
    public class PlayerData
    {
        //string PlayerName, int PlayerPosition
        [DataMember]
        public string PlayerName;
        [DataMember]
        public int PlayerPosition;

        public PlayerData(string PlayerName, int PlayerPosition)
        {
            this.PlayerName = PlayerName;
            this.PlayerPosition = PlayerPosition;
        }

    }

    /// <summary>
    /// clase PieceData, Envía Datos de la Pieza a la interfaz
    /// </summary>
    [DataContract]
    public class PieceData
    {
        //int PieceValue, int PiecePosition, int PlayerPosition
        //int StartPieceInt, int PiecePosition, int PlayerPosition
        [DataMember]
        public int PieceValue;
        [DataMember]
        public int PrevPieceValue;
        [DataMember]
        public int StartPieceInt;
        [DataMember]
        public int PiecePosition;
        [DataMember]
        public int PlayerPosition;
        //Girar ficha hasta ubicarla en la posición deseada
        [DataMember]
        public int Rotacion;
        //izquierda, derecha, arriba o abajo
        [DataMember]
        public int Ubicacion;
        //X e Y: La posición dentro del panel de la mesa
        [DataMember]
        public int PosX;

        [DataMember]
        public int PosY;



        public PieceData(int PieceValue,int StartPieceInt,int PiecePosition,int PlayerPosition)
        {
            this.PieceValue = PieceValue;
            this.StartPieceInt = StartPieceInt;
            this.PiecePosition = PiecePosition;
            this.PlayerPosition = PlayerPosition;
        }
        public PieceData(int PieceValue, int X, int Y, int PrevPieceValue, int PlayerPosition)
        {
            this.PieceValue = PieceValue;
            this.PosY= StartPieceInt;
            this.PosX = PiecePosition;
            this.PlayerPosition = PlayerPosition;
            this.PrevPieceValue = PrevPieceValue;
        }

    }


    /// <summary>
    /// clase Domino Service, Servidor que Gestiona el Juego de Domino.
    /// </summary>
    [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
    public class DominoService : IDominoService
    {

        Table mesa;
        PieceData pieceD;
        PlayerData playerD;

        public DominoService() 
        {
            mesa = new Table(this);
        } 

        public void RequestSitPlayer(string PlayerName)
        {
            IDominoServiceCallbackContract Callback = OperationContext.Current.GetCallbackChannel<IDominoServiceCallbackContract>();

            mesa.RequestSitPlayer(PlayerName, Callback);
            
        }

        //envia la ficha al servidor ,para ponerlo en todas las instancias
        public void RequestPutPiecePlayer(PieceData piece, PieceData prevPiece)
        {
            IDominoServiceCallbackContract Callback = OperationContext.Current.GetCallbackChannel<IDominoServiceCallbackContract>();

            mesa.RequestPutPiecePlayer(piece, prevPiece, Callback);
            
        }

     

        public void PutPieceOnTable(List<Player> PlayersRegistered, Piece CurrentStartPiece, int CurrentStartPiecePosition, int PlayerWhoStarts)
        {
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                pieceD = new PieceData(0,CurrentStartPiece.ToInt(), CurrentStartPiecePosition, PlayerWhoStarts);
                PlayersRegistered[j].PlayerCallbackChannel.PutPiece(pieceD);
            }
        }

   
        public void SendPieces(List<Player> PlayersRegistered)
        {
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                for (int k = 0; k < PlayersRegistered.ElementAt(j).PlayerPieces.Pieces.Count; k++)
                {
                    for (int z = 0; z < PlayersRegistered.Count; z++)
                    {
                        pieceD = new PieceData(PlayersRegistered.ElementAt(j).PlayerPieces.Pieces[k].ToInt(), 0, k, PlayersRegistered.ElementAt(j).tablePosition);
                        PlayersRegistered.ElementAt(z).PlayerCallbackChannel.ShowPiece(pieceD);
                    }
                }
            }
        }


        public void InfoPlayerJoined(List<Player> PlayersRegistered, string PlayerName, IDominoServiceCallbackContract Callback, int PlayerPosition)
        {
            playerD = new PlayerData(PlayerName, PlayerPosition);
            //aqui me siento yo
            Callback.NewPlayerJoined(playerD);
            foreach (Player PlayerRegistered in PlayersRegistered)
            {
                //aqui informo a los demas jugadores que me he sentado yo
                PlayerRegistered.PlayerCallbackChannel.NewPlayerJoined(playerD);

                PlayerData playerOld = new PlayerData(PlayerRegistered.playerName, PlayerRegistered.tablePosition);
                //aqui me informo yo de los jugadores que ya estan sentados
                Callback.NewPlayerJoined(playerOld);
            }
        }
        /// <summary>
        /// Actualiza la mano del jugador que puso la ficha, en todas las Instancias.
        /// </summary>
        public void UpdatePiecesPlayers(List<Player> PlayersRegistered, PieceData piece)
        {
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                //todos los jugadores, menos el que puso la ficha.
                if (piece.PlayerPosition != j)
                {
                    PlayersRegistered.ElementAt(j).PlayerCallbackChannel.ShowPieceUpdate(piece); 
                    
                }
            }
        }

        /// <summary>
        /// Actualiza Marcadores a todas las Instancias
        /// </summary>
        public void ActualizarMarcadores(List<Player> PlayersRegistered, int[] Marcadores){
            foreach (Player PlayerRegistered in PlayersRegistered){               
                    PlayerRegistered.PlayerCallbackChannel.ActualizarMarcadores(Marcadores);
            }
        }



        /// <summary>
        /// Envío la ficha a todas las Instancias
        /// </summary> 
        public void InfoPiecePutOntable(List<Player> PlayersRegistered, PieceData piece, IDominoServiceCallbackContract Callback)
        {

            foreach (Player PlayerRegistered in PlayersRegistered)
            {
                //La ficha que manda ese jugador, que sea diferente a los demás jugadores, para así no mandarme a mí mismo la ficha
                if (piece.PlayerPosition != PlayerRegistered.tablePosition)
                {
                    //aqui informo a los demas jugadores la ficha que se ha puesto
                    PlayerRegistered.PlayerCallbackChannel.NewPutPieceOnPanel(piece);
                }

            }

        }

        /// <summary>
        /// Es llamado cuando el jugador ha pasado turno
        /// </summary> 
        public void PasoElTurno(int quienPasa)
        {
            mesa.PasoTurno(quienPasa);
        }

        public void ActualizaLosTurnos(List<Player> PlayersRegistered, int turnoEsDe, bool puedePoner, int puedo1, int puedo2)
        {
            for (int j = 0; j < PlayersRegistered.Count; j++)
            {
                PlayersRegistered[j].PlayerCallbackChannel.ElTurnoEsDe(turnoEsDe, puedePoner, puedo1, puedo2);
            }
        }
        
        /// <summary>
        /// Limpia las ventanas de los clientes
        /// </summary> 
        public void LimpiarClientes(List<Player> Clientes){
            foreach (Player PlayerRegistered in Clientes){
                    PlayerRegistered.PlayerCallbackChannel.LimpiarCliente();
            }
        }
    }

}
