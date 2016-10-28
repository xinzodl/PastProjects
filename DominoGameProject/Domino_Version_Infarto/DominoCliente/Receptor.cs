/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Receptor
 *  Descripción de la clase:
 *  Clase que reparte recibe llamdas del servidor
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using DominoCliente.DominoServiceReference;
using System.ServiceModel;



namespace DominoCliente
{
    public class Receptor : IDominoServiceCallback
    {
        public Controlador control;
        InstanceContext context;

        public Receptor(Controlador controladorIN)
        {
            control = controladorIN;
            context = new InstanceContext(this); 
        }
        public void ActualizarMarcadores(int[] marcadores) { 
            control.ActualizarMarcadores(marcadores);
        }
        public void ElTurnoEsDe(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2)
        {
            control.ActualizaTurno(posicionJugadorConTurno, puedePoner, puedo1, puedo2);
        }
        public void LimpiarCliente(){
            control.LimpiarCliente();
        }
        public InstanceContext GetContext()
        {
            return context;
        }

        public void NewPlayerJoined(PlayerData jugador)
        {
            control.NewPlayerJoined(jugador);
        }

        public void PlayerNotSat(string errorMessage)
        {
            control.PlayerNotSat(errorMessage);
        }

        public void ShowPiece(PieceData pieza)
        {
            control.ShowPiece(pieza);
        }

        public void PutPiece(PieceData pieza)
        {
            control.PutPiece(pieza);
        }

         public void NewPieceOnTable(PieceData pieza){
             this.control.NewPieceOnTable(pieza);
         }
         public void EndGame(int [] marcadores){
             this.control.EndGame(marcadores);
         }
         public void ColocaBot(int[] valors, int playerposition) {
             control.ColocaBot(valors,playerposition);
         }

    }
}
