/*
 * COPYRIGHT 2015 Ginés García Avilés - Luis Antonio Pérez Mulero
 *      TADS_DOMINO_2015
 *      TADS
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DominoCliente.DominoServiceReference;
using System.ServiceModel;

namespace DominoCliente
{
    class Dispatcher
    {

        private DominoServiceClient server;

        public Dispatcher(InstanceContext instanceContext) { 
            this.server = new DominoServiceClient(instanceContext);
        }

        public void CloseConnection() {
            this.server.Close();
        }

        public void OpenConnection(){
            this.server.Open();
        }   

        public void RequestSitPlayer(string name){
            this.server.RequestSitPlayer(name);
        }
    }
}
