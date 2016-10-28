/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Player
 *  Descripción de la clase:
 *  Clase que sirve para guardar los datos de un jugador (nombre, posicion, mano...).
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa un Jugador */
    public class Player
    {
        private string[] NombresReservados = new string[] { "fernando", "genaro", "rodolfo", "gumersindo" };//lista de nombres reservados
        private Boolean Bot;

        private string PlayerName;
        public string playerName
        {
            get
            {
                return PlayerName;
            }
        }

        private int TablePosition;
        public int tablePosition
        {
            get
            {
                return TablePosition;
            }
        }

        public IDominoServiceCallbackContract PlayerCallbackChannel { get; set; }

        public Hand PlayerPieces
        {
            get;
            set;
        }

        public Player(string Name, int Position, IDominoServiceCallbackContract Channel)
        {
            if (Name == null)
            {
                throw new ExcepcionInvalidPlayerArguments("El nombre es nulo");
            }
            else if (Position > 3 || Position < 0)
            {
                throw new ExcepcionInvalidPlayerArguments("La posicion no es correcta");
            }
            else if (Name.Length == 0)
            {
                throw new ExcepcionInvalidPlayerArguments("El nombre esta vacio");
            }
            /*else if (Channel == null)
            {
                throw new ExcepcionInvalidPlayerArguments("El canal es nulo");
            }*/
            else
            {
                this.Bot = false;
                for (int i = 0; i < NombresReservados.Length; i++)
                {//nombre de bot
                    if (Name.ToLower().Trim().Equals(NombresReservados[i].ToLower().Trim()))
                    {
                        this.Bot = true;
                    }
                }
                this.PlayerName = Name;
                this.TablePosition = Position;
                this.PlayerCallbackChannel = Channel;
                this.PlayerPieces = new Hand();
            }
        }

        public void GivePiece(Piece PieceGiven)
        {
            this.PlayerPieces.AddPiece(PieceGiven);
        }

        //Metodo que devuelve si un jugador es bot o no
        public Boolean playerBot
        {
            get
            {
                return Bot;
            }
        }

    }
}