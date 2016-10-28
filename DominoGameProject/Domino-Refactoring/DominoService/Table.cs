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
    /// clase Table, representa la mesa del juego
    /// </summary>
    public class Table
    {

        public static int MAX_PLAYERS = 4;
        public static int MAX_PIECES = 7;
        public static int NULL_VALUE = -1;
        int[] puedoColocar = new int[2];

        private int PlayerWhoStarts = 0; // Posición del Jugador que inicia la partida.
        private Piece CurrentStartPiece = new Piece(NULL_VALUE, NULL_VALUE);
        private int CurrentStartPiecePosition = NULL_VALUE;
        private List<Player> PlayersRegistered = new List<Player>();
        private List<Piece> TablePieces = new List<Piece>();
        private int [] Marcadores= new int[2];
        bool GameHasStarted = false;
        private int puntuaciónFin=200;
        int turno = -1;

        private Pile PiecesPile;
        private IDominoServiceCallbackContract Callback;

        private DominoService servidor;

        public Table(DominoService servidor)
        {

            this.servidor = servidor;
        }

        public void cambiaTurno()
        {

            turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno

            if(PlayersRegistered.ElementAt(turno).playerBot){
                //si es bot comprobamos si puede poner ficha o no
                Boolean ret = ElDelTurnoPuedePoner(TablePieces, PlayersRegistered.ElementAt(turno));
                if(ret){
                    //Si puede poner ficha
                    Bot boterino = new Bot();
                }else{
                    //Si no puede poner ficha pasa turno automaticamente
                    cambiaTurno();
                }
            }else{
                //Jugador Humano
                servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
            }
            
           
        }
        private void EndGame(int Posición_Ganador) { 

        }
        /// <summary>
        /// metodo que se ejecuta al acabar la Mano/Juego
        /// </summary> 
        private void EndHand(int value) {
            int puntos = 0;
            Player Ganador=null;
            switch (value) {
                case 0:
                    Ganador = PlayersRegistered.ElementAt(this.turno);
                    break;
                case 1:
                    int puntos2 = 0;
                    foreach (Player player in PlayersRegistered){
                        foreach (Piece pieza in player.PlayerPieces.Pieces){
                            puntos2 = puntos2 + pieza.FirstSquareValue + pieza.SecondSquareValue;
                        }
                        if (puntos2 > puntos) {
                               Ganador = player;
                               puntos = puntos2;
                        }
                        puntos2 = 0;
                    }
                    break;
                default: throw (new InvalidTableArgument("Entro en EndGame Cuando no debía"));
            }
            if (Ganador == null) throw (new InvalidTableArgument("Entro en EndGame Cuando no debía"));
            foreach (Player player in PlayersRegistered){
                if (player.tablePosition != Ganador.tablePosition){
                    foreach (Piece pieza in player.PlayerPieces.Pieces){
                        puntos = puntos + pieza.FirstSquareValue + pieza.SecondSquareValue;
                    }
                }
            }
            this.Marcadores[Ganador.tablePosition % 2] = this.Marcadores[Ganador.tablePosition % 2] + puntos;
            servidor.ActualizarMarcadores(this.PlayersRegistered, this.Marcadores);
            if (Marcadores[Ganador.tablePosition % 2]>=this.puntuaciónFin) {
                EndGame(Ganador.tablePosition);
            }
            this.servidor.LimpiarClientes(PlayersRegistered);
        }
        /// <summary>
        /// metodo que comprueba si algun jugador se quedó sin fichas o si se cerró la mesa
        /// </summary> 
        private int IsHandEnded() {
            if (this.PlayersRegistered.ElementAt(this.turno).PlayerPieces.Pieces.Count == 0) return 0;
            int valor_libre1;
            int valor_libre2;
            Piece ficha = TablePieces.ElementAt(0);
            Piece ficha_ant = TablePieces.ElementAt(1);

            valor_libre1 = ValorLibreEntreDosFichas(ficha, ficha_ant);

            ficha = TablePieces.ElementAt(TablePieces.Count-1);
            ficha_ant = TablePieces.ElementAt(TablePieces.Count-2);

            valor_libre2 = ValorLibreEntreDosFichas(ficha, ficha_ant);

            if (AreAllPiecesOfThisValueInTable(valor_libre1)||AreAllPiecesOfThisValueInTable(valor_libre2)) {
                return 1;
            }

            return -1;
        }
        /// <summary>
        /// Te dice el valor no emparejado
        /// </summary> 
        private static int ValorLibreEntreDosFichas(Piece ficha, Piece ficha_ant){
            int valor_libre1;
            if (ficha.IsDouble()){
                valor_libre1 = ficha.FirstSquareValue;
            }else
                if ((ficha_ant.IsDouble() && ficha.FirstSquareValue != ficha_ant.FirstSquareValue) || (ficha.FirstSquareValue != ficha_ant.FirstSquareValue && ficha.FirstSquareValue != ficha_ant.SecondSquareValue)){
                    valor_libre1 = ficha.FirstSquareValue;
                }else if ((ficha_ant.IsDouble() && ficha.SecondSquareValue != ficha_ant.FirstSquareValue) || (ficha.SecondSquareValue != ficha_ant.FirstSquareValue && ficha.SecondSquareValue != ficha_ant.SecondSquareValue)){
                    valor_libre1 = ficha.SecondSquareValue;
                }else{
                    throw (new InvalidTableArgument("No esta bien la lista de piezas"));
                }
            return valor_libre1;
        }
        /// <summary>
        /// metodo que recibe un valor y dice si aparece 7 veces en la mesa
        /// </summary> 
        private Boolean AreAllPiecesOfThisValueInTable(int value) {
            int contador = 0;
            for (int i = 0; i < TablePieces.Count; i++) {
                Piece actual = TablePieces.ElementAt(i);
                if (actual.FirstSquareValue == value || actual.SecondSquareValue == value) contador++;
                if (contador == 7) return true;
            }
            return false;
        
        }
        /// <summary>
        /// metodo que recibe la ficha del jugador y lo envía a todas las instancias 
        /// </summary> 
        public void RequestPutPiecePlayer(PieceData piece, PieceData prevPiece, IDominoServiceCallbackContract Callback)
        {
            Piece pieceAux = null;

            for(int i = 0; i<PlayersRegistered.ElementAt(piece.PlayerPosition).PlayerPieces.Pieces.Count;i++){
                if(PlayersRegistered.ElementAt(piece.PlayerPosition).PlayerPieces.Pieces.ElementAt(i).ToInt() == piece.PieceValue){
                    pieceAux = PlayersRegistered.ElementAt(piece.PlayerPosition).PlayerPieces.Pieces.ElementAt(i);
                }
            }
            int compare=prevPiece.PieceValue;
           
            
          

            if (TablePieces.ElementAt(0).ToInt()==compare)
            {
                TablePieces.Insert(0, pieceAux);
                Console.WriteLine(TablePieces.ElementAt(0).ToInt());
                int ret = IsHandEnded();
                if (ret != -1) {
                    EndHand(ret);
                    return;
                }else{
                    cambiaTurno();
                }
                //turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
                //servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
            }
            else if (TablePieces.ElementAt(TablePieces.Count-1).ToInt() == compare)
            {
                    TablePieces.Add(pieceAux);
                    int ret = IsHandEnded();
                    if (ret != -1){
                        EndHand(ret);
                        return;
                    }else{
                        cambiaTurno();
                    }                   // turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
                   // servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
            } else {
                Callback.PlayerNotSat("error no tiene que entrar aqui");
                //Lanzar Error
            }
            piece.PiecePosition=RemovePiecePlayer(piece.PlayerPosition,piece);
            servidor.InfoPiecePutOntable(PlayersRegistered, piece, Callback);

            //actualiza la mano en las demas Instancias
            servidor.UpdatePiecesPlayers(PlayersRegistered, piece);

            //esto es de los turnos
            //turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
            //servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]) , puedoColocar[0], puedoColocar[1]);
        }

        /// <summary>
        /// metodo que recibe el nombre del jugador y gestiona 
        /// su incorporación a la mesa.
        /// </summary> 
        public void RequestSitPlayer(string PlayerName, IDominoServiceCallbackContract Callback)
        {
            this.Callback  = Callback;

            if ((PlayerName == null) || (Callback == null))
            {
                throw new InvalidTableArgument("el nombre no es valido");

            }
            else
            {

                if (PlayersRegistered.Count < MAX_PLAYERS)
                {
                    bool RepeatedName = false;
                    int index = 0;
                    /// <summary>
                    /// Comprobar si el nombre esta repetido
                    /// </summary>
                   
                    CheckedRepeatedName(PlayerName, ref RepeatedName, ref index);

                    if (!RepeatedName)
                    {
                        int PlayerPosition = PlayersRegistered.Count;
                        /// <summary>
                        /// Ha podido sentar al nuevo jugador y se notifica a todos los sentados los datos del nuevo jugador
                        /// </summary>
                        
                        servidor.InfoPlayerJoined(PlayersRegistered, PlayerName, Callback, PlayerPosition);
                        /// <summary>
                        /// Se añade el nuevo jugador a la lista de los sentados en la mesa
                        /// </summary>
                        
                        
                        PlayersRegistered.Add(new Player(PlayerName, PlayerPosition, Callback));
                        /// <summary>
                        /// La mesa está completa y el juego todavía no ha empezado       
                        /// </summary>
                                     
                        if (PlayersRegistered.Count == MAX_PLAYERS && !GameHasStarted) //metodo stargame
                        {
                            StarGame();
                        }
                    }
                    else
                    {
                        Callback.PlayerNotSat("Ya hay un jugador sentado a la mesa con ese nombre");
                    }
                }
                else
                {
                    Callback.PlayerNotSat("La mesa ya tiene todas las posiciones ocupadas");
                }

            }         

        }
    
        
        /// <summary>
        /// metodo que inicia el juego.
        /// </summary> 
        private void StarGame()
        {
            /// <summary>
            ///se crea la nueva pila de fichas,                     
            ///DistributionPlayerPiece();     
            /// </summary>
           
            AssignPieces();
            /// <summary>
            /// Se notifica a cada Jugador las fichas de todos los Jugadores, incluidas las suyas                       
           /// </summary>
           
            servidor.SendPieces(PlayersRegistered);
            /// <summary>
            /// Se notifica a los jugadores la ficha puesta de inicio y quien la pone                       
            /// </summary>
           
            servidor.PutPieceOnTable(PlayersRegistered,CurrentStartPiece,CurrentStartPiecePosition,PlayerWhoStarts);
            /// <summary>
            /// Quita de las Piezas disponibles del Jugador con la que se empieza                    
            /// </summary>
           
            PlayersRegistered[PlayerWhoStarts].PlayerPieces.Pieces.Remove(CurrentStartPiece);
            TablePieces.Add(CurrentStartPiece);

            //esto es de los turnos
            turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
            servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
        }

         /// <summary>
         /// metodo que asigna las fichas del domino al jugador
         /// </summary>      
        private void AssignPieces()
        {
            
           // #if DEBUG
           // AssignPiecesPredictive();  
           // # else
            AssignPiecesRandom();
          //  #endif
        }

        /// <summary>
        /// metodo que asigna las fichas del domino de 
        /// forma predictiva.
        /// </summary> 
        private void AssignPiecesPredictive()
        {
            GameHasStarted = true;
            this.PiecesPile = new Pile();
            /// <summary>
            /// Se reparte a cada Jugador siete Piezas, de forma predictiva
            /// el primer jugador las primeras 7 piezas, el segundo las siguientes 7
            /// y asi sucesivamente, el cuarto jugador siempre tendra el doble 6.                  
            /// </summary>
            
            for (int p = 0; p < PlayersRegistered.Count; p++)
            {
                for (int i = 0; i < MAX_PIECES; i++)
                {
                    Piece AuxiliarFirstPiece = this.PiecesPile.GetFirstPiece();
                    PlayersRegistered[p].GivePiece(AuxiliarFirstPiece);
                    /// <summary>
                    ///crear un metodo para asignar el doble 6 private void isThisStaringPiece(piece,player)             
                    /// </summary>
                   
                    DoublePiece(p, i, AuxiliarFirstPiece);

                }
            }
        }

        /// <summary>
        /// metodo que asigna las fichas del domino de 
        /// forma aleatoria.
        /// </summary> 
        private void AssignPiecesRandom()
        {
            GameHasStarted = true;
            this.PiecesPile = new Pile();
            /// <summary>
            /// Se reparte a cada Jugador siete Piezas            
            /// </summary>
                               
            for (int p = 0; p < PlayersRegistered.Count; p++)
            {
                for (int i = 0; i < MAX_PIECES; i++)
                {
                    Piece AuxiliarRandomPiece = this.PiecesPile.GetRandomPiece();
                    PlayersRegistered[p].GivePiece(AuxiliarRandomPiece);
                    /// <summary>
                    ///crear un metodo para asignar el doble 6 private void isThisStaringPiece(piece,player)            
                    /// </summary>
                    
                    DoublePiece(p, i, AuxiliarRandomPiece);

                }
            }
        }

        /// <summary>
        /// metodo que asigna la ficha doble al jugador.
        /// </summary> 
         private void DoublePiece(int p, int i, Piece AuxiliarRandomPiece)
        {
            /// <summary>
            /// Si la Pieza es un doble           
            /// </summary>
            
            if (AuxiliarRandomPiece.FirstSquareValue == AuxiliarRandomPiece.SecondSquareValue)
            {
                /// <summary>
                /// Si es un doble mayor que el actual, se actualiza la Pieza de empiece y el Jugador que la tiene          
                /// </summary>
                
                if (AuxiliarRandomPiece.SecondSquareValue > this.CurrentStartPiece.SecondSquareValue)
                {
                    this.CurrentStartPiece = AuxiliarRandomPiece;
                    this.CurrentStartPiecePosition = i;
                    this.PlayerWhoStarts = p;
                    this.turno = this.PlayerWhoStarts;
                }
            }
        }

         /// <summary>
         /// metodo que comprueba si el nombre esta repetido.
         /// </summary> 
        private void CheckedRepeatedName(string PlayerName, ref bool RepeatedName, ref int index)
        {
            while ((index < PlayersRegistered.Count) && (RepeatedName == false))
            {
                if (PlayersRegistered[index].playerName == PlayerName)
                {
                    RepeatedName = true;
                }
                index++;
            }
        }
        //Metodo para elimir del servidor la ficha puesta
        public int RemovePiecePlayer(int PlayerPosition, PieceData piece)
        {
            int vup;
            int vdown;
        
            vup = piece.PieceValue / 10;
            vdown = piece.PieceValue % 10;

            //Recorremos las fichas del jugador, y quitamos la ficha puesta en la mesa
            for (int i = 0; i < PlayersRegistered[PlayerPosition].PlayerPieces.Pieces.Count; i++)
            {
                if ((vup==PlayersRegistered[PlayerPosition].PlayerPieces.Pieces[i].FirstSquareValue) && (vdown==PlayersRegistered[PlayerPosition].PlayerPieces.Pieces[i].SecondSquareValue))
                {
                    //Se borra
                    PlayersRegistered[PlayerPosition].PlayerPieces.Pieces.RemoveAt(i);
                    return i;
                }
            }
            return -1;
        }

        public void PasoTurno(int quienPasa)//cuando no ha podido poner porque no tenia fichas y tiene que pasar, llega aqui
        {
           /* if (quienPasa!=turno)
            {
               throw new InvalidPieceArgument("apaño para lanzar excepcion, aunque no deberia pasar esto");
            }
            turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
            servidor.ActualizaLosTurnos(PlayersRegistered, turno, ElDelTurnoPuedePoner(TablePieces, PlayersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
        */
            cambiaTurno();
        }

        private bool ElDelTurnoPuedePoner(List<Piece> estadoTablero, Player elDelTurno)
        {

            bool puedePoner = false;

            if (estadoTablero.Count == 1)
            {//Unicamente tengo el seis doble
                puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                puedoColocar[1] = estadoTablero.ElementAt(0).SecondSquareValue;
            }
            else
            {
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


            for (int ii=0; ii<elDelTurno.PlayerPieces.Pieces.Count && !puedePoner;ii++)//si encuentro alguna que puedo poner, paro y digo que si, si no temrina bucle y dice que no
            {
                if(elDelTurno.PlayerPieces.Pieces[ii].FirstSquareValue==puedoColocar[0] || elDelTurno.PlayerPieces.Pieces[ii].SecondSquareValue==puedoColocar[0] || elDelTurno.PlayerPieces.Pieces[ii].FirstSquareValue==puedoColocar[1] || elDelTurno.PlayerPieces.Pieces[ii].SecondSquareValue==puedoColocar[1] )
                {
                    puedePoner=true;
                }
            }

            return puedePoner;
        }

    }
}