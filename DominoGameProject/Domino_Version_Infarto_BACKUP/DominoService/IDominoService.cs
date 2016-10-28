/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  IDominoService
 *  Descripción de la clase:
 *  Clase que define las cabeceras de los metodos para la comunicacion desde y hacia el servidor.
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
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de interfaz "IService1" en el código y en el archivo de configuración a la vez.
    [ServiceContract(CallbackContract = typeof(IDominoServiceCallbackContract))]
    public interface IDominoService
    {
        [OperationContract(IsOneWay = true)]
        void RequestSitPlayer(string PlayerName);
        [OperationContract(IsOneWay = true)]
        void RequestPutPiece(PieceData data);
        [OperationContract(IsOneWay = true)]
        void PasoElTurno();
        
    }

    public interface IDominoServiceCallbackContract
    {
        [OperationContract(IsOneWay = true)]
        void ColocaBot(int[] valors, int posicion);

        [OperationContract(IsOneWay = true)]
        void ActualizarMarcadores(int[] Marcadores);

        [OperationContract(IsOneWay = true)]
        void ElTurnoEsDe(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2);

        [OperationContract(IsOneWay = true)]
        //void NewPlayerJoined(string PlayerName, int PlayerPosition);
        void NewPlayerJoined(PlayerData jugador);

        [OperationContract(IsOneWay = true)]
        void PlayerNotSat(string ErrorMessage);

        [OperationContract(IsOneWay = true)]
        //void ShowPiece(int PieceValue, int PiecePosition, int PlayerPosition);
        void ShowPiece(PieceData pieza);

        [OperationContract(IsOneWay = true)]
        void LimpiarCliente();

        [OperationContract(IsOneWay = true)]
        //void PutPiece(int StartPieceInt, int PiecePosition, int PlayerPosition);
        void PutPiece(PieceData pieza);

        [OperationContract(IsOneWay = true)]
        void NewPieceOnTable(PieceData data);
        [OperationContract(IsOneWay = true)]
        void EndGame(int[] Marcadores);
    }

}