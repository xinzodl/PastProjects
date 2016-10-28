using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using DominoCliente.DominoServiceReference;


namespace DominoCliente
{
    public partial class Emisor
    {
        public DominoServiceClient server;
        public Controlador control;

        public Emisor(Controlador controladorIN, InstanceContext context)
        {
            server = new DominoServiceClient(context);
            control = controladorIN;
            server.Open();
        }

        public void closeServer()
        {
            server.Abort();
        }

        public void RequestSitPlayer(string texto)
        {
            server.RequestSitPlayer(texto);
        }
        public void RequestPutPiece(PieceData data)
        {
            server.RequestPutPiece(data);
        }

        public void PasoTurno(){
            server.PasoElTurno();
        }

    }
}
