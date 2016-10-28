using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class Bot
    {
        static int [] puedoColocar = new int[2];
        static int [] piezasBordes = new int[2];

        public int [] PutPieceBot(Player jugador, List<Piece> estadoTablero)
        {
            int[] valors = new int[2];
            ValoresDisponibles(estadoTablero);

            int ret = MuchosDeUnaPintaIzq(jugador);
            if (ret == 0)
            {
                ret = MuchosDeUnaPintaDrch(jugador);
                valors[0] = ret;
                valors[1] = piezasBordes[1];
            }
            else
            {
                valors[0] = ret;
                valors[1] = piezasBordes[0];
            }
            return valors;
        }

        public void ValoresDisponibles(List<Piece> estadoTablero)
        {
            if (estadoTablero.Count == 1)
            {//Unicamente tengo el seis doble
                puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                puedoColocar[1] = estadoTablero.ElementAt(0).SecondSquareValue;
                piezasBordes[0] = estadoTablero.ElementAt(0).ToInt();
                piezasBordes[1] = estadoTablero.ElementAt(0).ToInt();
            }
            else
            {
                piezasBordes[0] = estadoTablero.ElementAt(0).ToInt();
                piezasBordes[1] = estadoTablero.ElementAt((estadoTablero.Count) - 1).ToInt();
                if ((estadoTablero.ElementAt(0).FirstSquareValue == estadoTablero.ElementAt(1).FirstSquareValue) || (estadoTablero.ElementAt(0).FirstSquareValue == estadoTablero.ElementAt(1).SecondSquareValue))
                {
                    puedoColocar[0] = estadoTablero.ElementAt(0).SecondSquareValue;
                }
                else
                {
                    puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                }
                if ((estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue == estadoTablero.ElementAt((estadoTablero.Count) - 2).FirstSquareValue) || (estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue == estadoTablero.ElementAt((estadoTablero.Count) - 2).SecondSquareValue))
                {
                    puedoColocar[1] = estadoTablero.ElementAt((estadoTablero.Count) - 1).SecondSquareValue;
                }
                else
                {
                    puedoColocar[1] = estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue;
                }
            }
        }



        public int MuchosDeUnaPintaIzq(Player jugador)
        {
            List<Piece> Pieces = jugador.PlayerPieces.Pieces;

            for (int i = 0; i < Pieces.Count; i++ )
            {
                if ((Pieces.ElementAt(i).FirstSquareValue == puedoColocar[0]) || (Pieces.ElementAt(i).SecondSquareValue == puedoColocar[0]))
                {
                    return Pieces.ElementAt(i).ToInt();
                }
            }
            return 0;
        }

        public int MuchosDeUnaPintaDrch(Player jugador)
        {
            List<Piece> Pieces = jugador.PlayerPieces.Pieces;

            for (int i = 0; i < Pieces.Count; i++)
            {
                if ((Pieces.ElementAt(i).FirstSquareValue == puedoColocar[0]) || (Pieces.ElementAt(i).SecondSquareValue == puedoColocar[0]))
                {
                    return Pieces.ElementAt(i).ToInt();
                }
            }
            return 0;
        }

    }
}