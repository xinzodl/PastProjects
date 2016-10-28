/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Controlador
 *  Descripción de la clase:
 *  Clase que reparte llamdas al emisor y receptor
 */
using DominoCliente.DominoServiceReference;
using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DominoCliente
{
    public class Controlador
    {
        public Emisor envia;
        public Receptor recibe;
        public Domino vista;
        public int myPosition;
        public int [] extremos;

        public Controlador(Domino view, InstanceContext miContexto)
        {
            recibe = new Receptor(this);
            envia = new Emisor(this, recibe.GetContext());
            vista = view;
        }

        public void closeServer()
        {
            envia.closeServer();
        }

        //EMISOR
        public void BtnSentarse_Click(object sender, EventArgs e, TextBox cajaTexto)
        {
            string texto = cajaTexto.Text;
            if (texto != "")
            {
                vista.DisableTxtBoxPlayerName();
                envia.RequestSitPlayer(texto);
            }
            else
            {
                vista.NombreVacio();
            }
        }

       
        //RECEPTOR
        public void NewPlayerJoined(PlayerData jugador)
        {
            string PlayerName = jugador.nombre;
            int PlayerPosition = jugador.posicion;
            vista.NewPlayerJoined(jugador);
            if (PlayerName == vista.GetTextBoxPlayerName().Text)
            {
                myPosition = PlayerPosition;
                vista.DisableBoton();
            }
        }

        public void PlayerNotSat(string errorMessage)
        {
            vista.PlayerNotSat(errorMessage);
        }

        public void ShowPiece(PieceData pieza)
        {
            vista.ShowPiece(pieza, myPosition);
        }

        public void PutPiece(PieceData pieza)
        {
            vista.PutPiece(pieza);
        }
        public void NewPieceOnTable(PieceData data) {
            vista.ShowPieceOnIndex(data.valor, data.index,data.selector);
            vista.HidePieceOfAnotherPlayer(data.jugador, data.posicionEnMano);
        }
        public void ActualizaTurno(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2){
            vista.ActualizaTurno(posicionJugadorConTurno, puedePoner, puedo1, puedo2);
        }
        public void ActualizarMarcadores(int[] marcadores){
            vista.ActualizarMarcador(marcadores);
        }
        public void LimpiarCliente(){
            vista.LimpiarCliente();
        }
        public void EndGame(int[] marcadores) { 
            vista.EndGame(marcadores);
        }
        public void ColocaBot(int[] valors, int posicion){
            vista.ColocaBot(valors, posicion);
        }
    }
}
