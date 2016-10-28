using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    /* Representa un Jugador */
    public class Player{
        private string PlayerName;
        private int TablePosition;
        private IDominoServiceCallbackContract PlayerCallbackChannel;
        private Hand PlayerPieces;

        public string getName(){
            return this.PlayerName;
        }

        public int getPosition(){
            return this.TablePosition;
        }

        public IDominoServiceCallbackContract getCallback() {
            return this.PlayerCallbackChannel;
        }

        public Hand getHand() {
            return this.PlayerPieces;
        }
        

        public Player(string name, int position, IDominoServiceCallbackContract Channel){
            if (name == null) throw new InvalidPlayerArguments("Name can't be null.");
            else if (name.Equals("")) throw new InvalidPlayerArguments("Name can't be empty.");
            else if ((position < 0) || (position > 3)) 
                throw new InvalidPlayerArguments("Invalid Position.");
            else{
                this.PlayerName = name;
                this.TablePosition = position;
                this.PlayerCallbackChannel = Channel;
                this.PlayerPieces = new Hand();
            }
        }

        public void GivePiece(Piece PieceGiven){
            this.PlayerPieces.AddPiece(PieceGiven);
        }

    }
}