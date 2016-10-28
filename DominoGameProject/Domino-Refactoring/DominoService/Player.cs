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
using System.Web;

namespace DominoService
{
    /// <summary>
    /// clase Player, gestiona los jugadores
    /// </summary>
    public class Player
    {
        private string[] NombresReservados = new string[] { "fernando", "genaro", "rodolfo", "gumersindo" };//lista de nombres reservados
        private Boolean Bot;
        private string PlayerName;

        public Player(string Name, int Position, IDominoServiceCallbackContract Channel)
        {
            if(Name == null)
            {
                throw new InvalidPlayerArgument("el nombre no es valido");

            }
            else if ((Position < 0) || (Position > 3))
            {
                throw new InvalidPlayerArgument("la posicion no es valida");
            }
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

        /// <summary>
        /// metodo que retorna el nombre del jugador.
        /// </summary> 
        public string playerName
        {
            get
            {
                return PlayerName;
            }
        }

        /// <summary>
        /// metodo que retorna si el jugador es bot.
        /// </summary> 
        /// //PlayerName ya existe, por lo que le pongo la B delante de Bot
        public Boolean playerBot
        {
            get
            {
                return Bot;
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

        /// <summary>
        /// metodo que retorna la mano de fichas 
        /// del jugador.
        /// </summary> 
        public Hand PlayerPieces
        {
            get;
            set;
        }

        /// <summary>
        /// metodo que agrega una ficha al jugador.
        /// </summary> 
        public void GivePiece(Piece PieceGiven)
        {

            this.PlayerPieces.AddPiece(PieceGiven);
        }

    }
}