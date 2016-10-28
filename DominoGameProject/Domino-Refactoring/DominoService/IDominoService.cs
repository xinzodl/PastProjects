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
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace DominoService
{
    /// <summary>
    /// Puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de interfaz "IService1" en el código y en el archivo de configuración a la vez.
    /// </summary>
    
    [ServiceContract(CallbackContract = typeof(IDominoServiceCallbackContract))]
    public interface IDominoService
    {
        [OperationContract(IsOneWay = true)]
        void RequestSitPlayer(string PlayerName);
        //La interfaaz llama a este método y envía la ficha al servidor
        [OperationContract(IsOneWay = true)]
        void RequestPutPiecePlayer(PieceData piece, PieceData prevPiece);
        [OperationContract(IsOneWay = true)]
        void PasoElTurno(int quienPasa);
    }

    public interface IDominoServiceCallbackContract
    {
        
        [OperationContract(IsOneWay = true)]
        void NewPlayerJoined(PlayerData player);
        //Enviar datos para actualizar al poner ficha
        [OperationContract(IsOneWay = true)]
        void PuttedPiece(PieceData piece);

        [OperationContract(IsOneWay = true)]
        void PlayerNotSat(string ErrorMessage);

        [OperationContract(IsOneWay = true)]
        void ShowPiece(PieceData piece);

        [OperationContract(IsOneWay = true)]
        void ActualizarMarcadores(int [] Marcadores);

        [OperationContract(IsOneWay = true)]
        void LimpiarCliente();

        [OperationContract(IsOneWay = true)]
        void ShowPieceUpdate(PieceData piece);

        [OperationContract(IsOneWay = true)]
        void PutPiece(PieceData piece);

        //Método donde el servidor envía al cliente la ficha colocada por el servidor
        [OperationContract(IsOneWay = true)]
        void NewPutPieceOnPanel(PieceData piece);

        [OperationContract(IsOneWay = true)]
        void ElTurnoEsDe(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2);

    }




}