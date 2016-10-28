/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Bot
 *  Descripción de la clase:
 *  Clase que implementa la estrategia del bot, y decide que ficha poner de un jugador.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class Bot
    {
        public int[] puedoColocar = new int[2];//numeros (de 1 cifra) en los extremos del tablero
        public int[] piezasBordes = new int[2];//piezas de los extremos del tablero
        public int[] contador = new int[7] { 0, 0, 0, 0, 0, 0, 0 };//array para contar numeros repetidos
        public int valorMasRepetido;//almacena el valor mas repetido, por lo tanto de que se intentara poner primero ficha (estrategia de muchas de una pinta)

        /*Metodo PutPieceBot
         * es llamado desde Table, con el jugador bot que tiene que poner (ya contiene su mano) y 
         * con el estado del tablero, para ver que ficha deberia colocar
         * calcula que ficha debe colocar y la devuelve
         * PRECONDICION: solo llega en caso de que SI pueda colocar ficha el bot, si no puede, no llega.
         */
        public int[] PutPieceBot(Player jugador, List<Piece> estadoTablero)
        {
            int[] valors = new int[2];//seran 0: ficha que coloco de mi mano 1: ficha del tablero al lado de la que coloco
            ValoresDisponibles(estadoTablero);//Actualiza array puedoColocar y piezasBordes
            Contador(jugador);//cuenta las veces que tengo valor en mi mano (para estrategia muchas de una pinta)
            List<Piece> ordenadas = OrdenaMano(jugador);//lista ordenada de fichas segun frecuencia de aparicion de los numeros
            for (int i = 0; i < ordenadas.Count; i++)
            {//recorro la lista ordenada, comrpobando si puedo poner la ficha 1º por la izquierda, y luego porla erecha
                int ret = PrimeroEncuentraIzq(ordenadas.ElementAt(i));//primero miro si puedo poner esta ficha por la izquierda
                if (ret == -1)//-1 es que no puedo colocar por ahi
                {
                    ret = PrimeroEncuentraDrch(ordenadas.ElementAt(i));//si no la pueod poner por la izquierda, pruebo por la derecha
                    if (ret != -1)
                    {
                        valors[0] = ret;
                        valors[1] = piezasBordes[1];
                        break;
                    }
                }
                else
                {//si he podido colocar por la izquierda
                    valors[0] = ret;
                    valors[1] = piezasBordes[0];
                    break;
                }
            }
            return valors;
        }

        //Metodo ValoresDisponibles: Actualiza array puedoColocar y piezasBordes
        private void ValoresDisponibles(List<Piece> estadoTablero)
        {
            if (estadoTablero.Count == 1)
            {//Unicamente tengo el seis doble
                puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                puedoColocar[1] = estadoTablero.ElementAt(0).SecondSquareValue;
                piezasBordes[0] = estadoTablero.ElementAt(0).ToInt();
                piezasBordes[1] = estadoTablero.ElementAt(0).ToInt();
            }
            else
            {//hay fichas puestas en la mesa
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

        //Metodo OrdenaMano: lista ordenada de fichas segun frecuencia de aparicion de los numeros
        private List<Piece> OrdenaMano(Player jugador)
        {
            List<Piece> Pieces = jugador.PlayerPieces.Pieces;
            List<Piece> ordenadas = new List<Piece>();
            for (int i = 0; i < Pieces.Count; i++)
            {
                if ((Pieces.ElementAt(i).FirstSquareValue == valorMasRepetido) || (Pieces.ElementAt(i).SecondSquareValue == valorMasRepetido))
                {
                    ordenadas.Insert(0, Pieces.ElementAt(i));
                }
                else
                {
                    ordenadas.Add(Pieces.ElementAt(i));
                }
            }
            return ordenadas;
        }



        private int PrimeroEncuentraIzq(Piece Pieces)
        {
            if ((Pieces.FirstSquareValue == puedoColocar[0]) || (Pieces.SecondSquareValue == puedoColocar[0]))
                {
                    return Pieces.ToInt();
                }
            return -1;
        }

        private int PrimeroEncuentraDrch(Piece Pieces)
        {
                if ((Pieces.FirstSquareValue == puedoColocar[1]) || (Pieces.SecondSquareValue == puedoColocar[1]))
                {
                    return Pieces.ToInt();
                }
            return -1;
        }

        //Metodo Contador: cuenta las veces que tengo valor en mi mano (para estrategia muchas de una pinta)
        private void Contador(Player jugador)
        {
            List<Piece> Pieces = jugador.PlayerPieces.Pieces;

            for (int i = 0; i < Pieces.Count; i++)
            {
                contador[Pieces.ElementAt(i).FirstSquareValue]++;
                contador[Pieces.ElementAt(i).SecondSquareValue]++;
            }

            int max = contador.Max();
            for (int j = 0; j < contador.Length; j++)
            {
                if (contador[j] == max)
                {
                    valorMasRepetido = j;
                    break;
                }
            }
        }
    }
}