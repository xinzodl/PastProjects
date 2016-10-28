using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace DominoService
{
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de interfaz "IService1" en el código y en el archivo de configuración a la vez.
    [ServiceContract(CallbackContract = typeof(IDominoServiceCallbackContract))]
    public interface IDominoService
    {
        [OperationContract(IsOneWay = true)]
        void RequestSitPlayer(string PlayerName);
    }

    public interface IDominoServiceCallbackContract
    {
        [OperationContract(IsOneWay = true)]
        void WhoHasTurn(PlayerData player);

        [OperationContract(IsOneWay = true)]
        void NewPlayerJoined(PlayerData player);

        [OperationContract(IsOneWay = true)]
        void PlayerNotSat(string ErrorMessage);

        [OperationContract(IsOneWay = true)]
        void ShowPiece(PieceData piece);

        [OperationContract(IsOneWay = true)]
        void PutPiece(int StartPieceInt, int PiecePosition, int PlayerPosition);

    }
    [DataContract]
    public class PlayerData{
        private String name;
        private int position;

        public PlayerData(String name, int position){
            this.name = name;
            this.position = position;
        }

        [DataMember]
        public string Name{
            get { return name ; }
            set{ name = value; }
        }

        [DataMember]
        public int Position{
            get { return position; }
            set { position = value; }
        }
        
    }
    [DataContract]
    public class PieceData{
        private int value;
        private int position;
        private String playerName;
        private int playerPosition;

        public PieceData(int value, int position, String name, int playerPosition){
            this.value = value;
            this.position = position;
            this.playerName = name;
            this.playerPosition = playerPosition;
        }

        [DataMember]
        public int Value{
            get { return value; }
            set { this.value = value; }
        }
        [DataMember]
        public int Position{
            get { return position; }
            set { position = value; }
        }
        [DataMember]
        public string PlayerName
        {
            get { return playerName; }
            set { playerName = value; }
        }
        [DataMember]
        public int PlayerPosition
        {
            get { return playerPosition; }
            set { playerPosition = value; }
        }
        
    }
}