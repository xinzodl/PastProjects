/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Table
 *  Descripción de la clase:
 *  Clase que sirve para guardar el estado de la mesa (con jugadores setando y fichas puestas).
 *  Implementa la logica de pasar turno, principio y fin de juego, hace llamdas a los bot si es necesario,
 *  y basicamente es el controlador de la partida y la logica que la gobierna.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class Table
    {//responsable de coordinar los turnos y las acciones que se tengan que realizar en la mesa en el ámbito de cada mano y partida
          private static Table mesa;
          public static readonly int NULL_VALUE = -1;
          public static readonly  int MAX_PLAYERS = 4;
          int[] puedoColocar = new int[2];//extremos de la mesa
          private List<Piece> tablePieces = new List<Piece>();
          private int[] marcadores = new int[2];
          private readonly int puntuaciónFin = 200;
          int turno = -1;//se guarda posicion deljugador ocn turno
          DominoService servidor = new DominoService();
          public int playerWhoStarts = 0; // Posición del Jugador que inicia la partida.
          public Piece currentStartPiece = new Piece(NULL_VALUE, NULL_VALUE);
          public int currentStartPiecePosition = NULL_VALUE;
          public List<Player> playersRegistered = new List<Player>();
          public bool gameHasStarted = false;
          public Pile piecesPile;

          public void resetTable() {
              mesa = new Table();
          }

          public static Table getTable()
          {
              if (mesa == null) mesa = new Table();
              return mesa;
          }

          private void CambiaTurno()
          {
              turno = (turno + 1) % 4;//actualizo correctamente quien tiene el turno
              //ahora se informa a todos del nuevo estado de los turnos. se informa de si el que tiene el turno puede poner o no
              servidor.ActualizaLosTurnos(playersRegistered, turno, ElDelTurnoPuedePoner(tablePieces, playersRegistered[turno]), puedoColocar[0], puedoColocar[1]);
              if (playersRegistered.ElementAt(turno).playerBot)
              {
                  //si es bot comprobamos si puede poner ficha o no
                  Boolean ret = ElDelTurnoPuedePoner(tablePieces, playersRegistered.ElementAt(turno));
                  if (ret)
                  {
                      Bot boterino = new Bot();
                      int[] aux = boterino.PutPieceBot(playersRegistered.ElementAt(turno), tablePieces);
                      servidor.ColocaBot(aux, playersRegistered.ElementAt(turno));
                  }
                  else
                  {
                      //Si no puede poner ficha pasa turno automaticamente
                      CambiaTurno();
                  }
              }
          }

          private bool ElDelTurnoPuedePoner(List<Piece> estadoTablero, Player elDelTurno)
          {
              bool puedePoner = false;
              ObtenerExtremosMesa(estadoTablero);//actualizo puedoColocar con los valores extremo de la mesa
              for (int ii = 0; ii < elDelTurno.PlayerPieces.Pieces.Count && !puedePoner; ii++)//miro si tengo pieza con uno de los numeros igual al de alguno de lso extremos del tablero
              {//si encuentro alguna que puedo poner, paro y digo que si, si no temrina bucle y dice que no{
                  if (elDelTurno.PlayerPieces.Pieces[ii].FirstSquareValue == puedoColocar[0] || elDelTurno.PlayerPieces.Pieces[ii].SecondSquareValue == puedoColocar[0] || elDelTurno.PlayerPieces.Pieces[ii].FirstSquareValue == puedoColocar[1] || elDelTurno.PlayerPieces.Pieces[ii].SecondSquareValue == puedoColocar[1])
                  {
                      puedePoner = true;
                  }
              }
              return puedePoner;
          }

          private void ObtenerExtremosMesa(List<Piece> estadoTablero)
          {
              if (estadoTablero.Count == 1)
              {//Unicamente tengo el seis doble
                  puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                  puedoColocar[1] = estadoTablero.ElementAt(0).SecondSquareValue;
              }
              else
              {//obtengo numeros en lso extremos del tablero
                  if ((estadoTablero.ElementAt(0).FirstSquareValue == estadoTablero.ElementAt(1).FirstSquareValue) || (estadoTablero.ElementAt(0).FirstSquareValue == estadoTablero.ElementAt(1).SecondSquareValue))
                  {
                      puedoColocar[0] = estadoTablero.ElementAt(0).SecondSquareValue;
                  }
                  else
                  {
                      puedoColocar[0] = estadoTablero.ElementAt(0).FirstSquareValue;
                  } if ((estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue == estadoTablero.ElementAt((estadoTablero.Count) - 2).FirstSquareValue) || (estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue == estadoTablero.ElementAt((estadoTablero.Count) - 2).SecondSquareValue))
                  {
                      puedoColocar[1] = estadoTablero.ElementAt((estadoTablero.Count) - 1).SecondSquareValue;
                  }
                  else
                  {
                      puedoColocar[1] = estadoTablero.ElementAt((estadoTablero.Count) - 1).FirstSquareValue;
                  }
              }
          }

          public void PasoTurno()
          {//cuando no ha podido poner porque no tenia fichas y tiene que pasar, llega aqui{
              CambiaTurno();
          }

          public void RequesPutPiece(PieceData data) {
              Player player = this.playersRegistered.ElementAt(data.jugador);
              Piece pieza = null;
              foreach (Piece piece in player.PlayerPieces.Pieces) {//obtengo pieza que quiero poner
                  if (piece.ToInt() == data.valor) pieza = piece;
              }
              if (data.index < 27) tablePieces.Insert(0, pieza);//añado por la izquierda a la mesa
              if (data.index > 27) tablePieces.Add(pieza);//añado por la derecha a la mesa
              servidor.NewPieceOnTable(data,playersRegistered);//pongo ficha
              player.PlayerPieces.Pieces.Remove(pieza);//quito de la mano del jugador la puesta 
              int ret = IsHandEnded();
              if (ret != -1){//si ha terminado la mano, ejecuto el fin de mano
                  EndHand(ret);
              }else{//si noha terminado, cambio turno al siguiente
                  CambiaTurno();
              }        
          }

          private void EndGame(int posición_Ganador){
              servidor.EndGame(playersRegistered,this.marcadores);
          }

          // metodo que se ejecuta al acabar la Mano/Juego
          private void EndHand(int value)
          {
              int puntos = 0;
              Player ganador = null;
              SetWinner(value, ref puntos, ref ganador);//obtengo el ganador de la mano
              if (ganador == null) throw (new Exception("Entro en EndGame Cuando no debía"));
              puntos = AddPointsToWinner(puntos, ganador);//añado los puntos al ganador

              this.marcadores[ganador.tablePosition % 2] = this.marcadores[ganador.tablePosition % 2] + puntos;
              servidor.ActualizarMarcadores(this.playersRegistered, this.marcadores);
              if (marcadores[ganador.tablePosition % 2] >= this.puntuaciónFin)
              {
                  EndGame(ganador.tablePosition);

              }
              else
              {
                  NewHand();//se resetan cosas para empezar nueva mano
              }
          }

          //se resetan cosas para empezar nueva mano
          private void NewHand()
          {
              this.servidor.LimpiarClientes(playersRegistered);
              this.tablePieces = null;
              this.tablePieces = new List<Piece>();
              this.currentStartPiece = new Piece(NULL_VALUE, NULL_VALUE);
              this.currentStartPiecePosition = NULL_VALUE;
              this.playerWhoStarts = NULL_VALUE;
              this.turno = -1;
              StartGame();
          }

          //añado los puntos al ganador
          private int AddPointsToWinner(int puntos, Player ganador)
          {
              foreach (Player player in playersRegistered)
              {
                  if (player.tablePosition != ganador.tablePosition)
                  {
                      foreach (Piece pieza in player.PlayerPieces.Pieces)
                      {
                          puntos = puntos + pieza.FirstSquareValue + pieza.SecondSquareValue;
                      }
                  }
                  player.PlayerPieces.Pieces.Clear();
              }
              return puntos;
          }

          //obtengo el ganador de la mano
          private void SetWinner(int value, ref int puntos, ref Player ganador)
          {
              switch (value)
              {
                  case 0:
                      ganador = playersRegistered.ElementAt(this.turno);
                      break;
                  case 1:
                      int puntos2 = 0;
                      foreach (Player player in playersRegistered)
                      {
                          foreach (Piece pieza in player.PlayerPieces.Pieces)
                          {
                              puntos2 = puntos2 + pieza.FirstSquareValue + pieza.SecondSquareValue;
                          }
                          if (puntos2 > puntos)
                          {
                              ganador = player;
                              puntos = puntos2;
                          }
                          puntos2 = 0;
                      }
                      break;
                  default: throw (new Exception("Entro en EndGame Cuando no debía"));
              }
          }

          // metodo que comprueba si algun jugador se quedó sin fichas o si se cerró la mesa
          private int IsHandEnded()
          {
              if (this.playersRegistered.ElementAt(this.turno).PlayerPieces.Pieces.Count == 0) return 0;
              int valor_libre1;
              int valor_libre2;
              Piece ficha = tablePieces.ElementAt(0);
              Piece ficha_ant = tablePieces.ElementAt(1);
              valor_libre1 = ValorLibreEntreDosFichas(ficha, ficha_ant);
              ficha = tablePieces.ElementAt(tablePieces.Count - 1);
              ficha_ant = tablePieces.ElementAt(tablePieces.Count - 2);
              valor_libre2 = ValorLibreEntreDosFichas(ficha, ficha_ant);
              if (AreAllPiecesOfThisValueInTable(valor_libre1) || AreAllPiecesOfThisValueInTable(valor_libre2))
              {
                  return 1;
              }
              return -1;
          }

          // Te dice el valor no emparejado
          private int ValorLibreEntreDosFichas(Piece ficha, Piece ficha_ant)
          {
              int valor_libre1;
              if (ficha.IsDouble())
              {
                  valor_libre1 = ficha.FirstSquareValue;
              }
              else
                  if ((ficha_ant.IsDouble() && ficha.FirstSquareValue != ficha_ant.FirstSquareValue) || (ficha.FirstSquareValue != ficha_ant.FirstSquareValue && ficha.FirstSquareValue != ficha_ant.SecondSquareValue))
                  {
                      valor_libre1 = ficha.FirstSquareValue;
                  }
                  else if ((ficha_ant.IsDouble() && ficha.SecondSquareValue != ficha_ant.FirstSquareValue) || (ficha.SecondSquareValue != ficha_ant.FirstSquareValue && ficha.SecondSquareValue != ficha_ant.SecondSquareValue))
                  {
                      valor_libre1 = ficha.SecondSquareValue;
                  }
                  else
                  {
                      throw (new Exception("No esta bien la lista de piezas"));
                  }
              return valor_libre1;
          }

           // metodo que recibe un valor y dice si aparece 7 veces en la mesa
          private Boolean AreAllPiecesOfThisValueInTable(int value)
          {
              int contador = 0;
              for (int i = 0; i < tablePieces.Count; i++)
              {
                  Piece actual = tablePieces.ElementAt(i);
                  if (actual.FirstSquareValue == value || actual.SecondSquareValue == value) contador++;
                  if (contador == 7) return true;
              }
              return false;

          }

          public int GetMAX_PLAYERS()
          {
              return MAX_PLAYERS;
          }

          //pido sentar nuevo jugador
          public void RequestSitPlayer(string playerName, IDominoServiceCallbackContract callback)
          {
              if (playersRegistered.Count < MAX_PLAYERS)
              {
                  if (NombreRepetido(playerName) == false)
                  {
                      int PlayerPosition = playersRegistered.Count;
                      // Se añade el nuevo jugador a la lista de los sentados en la mesa
                      playersRegistered.Add(new Player(playerName, PlayerPosition, callback));
                  }
                  else
                  {
                      ExcepcionNombreRepetido ex = new ExcepcionNombreRepetido("Ya hay un jugador sentado a la mesa con ese nombre");
                      throw ex;
                  }
              }
              else
              {
                  ExcepcionMesaLlena ex = new ExcepcionMesaLlena("La mesa ya tiene todas las posiciones ocupadas");
                  throw ex;
              }
          }

          //compruebo nombre repetido
          private bool NombreRepetido(string playerName)
          {
              bool RepeatedName = false;
              int index = 0;
              while ((index < playersRegistered.Count) && (RepeatedName == false))
              {
                  if (playersRegistered[index].playerName == playerName)
                  {
                      RepeatedName = true;
                  }
                  index++;
              }
              return RepeatedName;
          }

          //comienza unnuevo juego
          public void StartGame()
          {
              gameHasStarted = true;
              this.piecesPile = null;
              this.piecesPile = new Pile();
              AssignTokensToPlayers();
              servidor.NotifyTokensToPlayers(this.playersRegistered);
              tablePieces.Add(currentStartPiece);
              servidor.NotifyFirstPiece(this.playersRegistered, this.currentStartPiece, this.currentStartPiecePosition, this.playerWhoStarts);
              CambiaTurno();
          }

          //repartir fichas
          private void AssignTokensToPlayers()
          {
                #if DEBUG
                        AssignTokensMyOrder();
                #else
                        AssignPiecesRandomly();
                #endif
          }

          //Método DEBUG-repartir de forma ordenada
          private void AssignTokensMyOrder()
          {
              // Se reparte a cada Jugador siete Piezas                   
              for (int p = 0; p < playersRegistered.Count; p++)
              {
                  for (int i = 0; i < Hand.MAX_PIECES; i++)
                  {
                      Piece AuxiliarPiece = this.piecesPile.GetFirstPiece();
                      playersRegistered[p].GivePiece(AuxiliarPiece);
                      // Si la Pieza es un doble
                      if (AuxiliarPiece.IsDouble())
                      {
                          // Si es un doble mayor que el actual, se actualiza la Pieza de empiece y el Jugador que la tiene
                          if (AuxiliarPiece.SecondSquareValue > this.currentStartPiece.SecondSquareValue)
                          {
                              this.currentStartPiece = AuxiliarPiece;
                              this.currentStartPiecePosition = i;
                              this.playerWhoStarts = p;
                              this.turno = this.playerWhoStarts;
                          }
                      }
                  }
              }
          }

          //repartir de forma aleatoria
          private void AssignPiecesRandomly()
          {
              // Se reparte a cada Jugador siete Piezas                   
              for (int p = 0; p < playersRegistered.Count; p++)
              {
                  for (int i = 0; i < Hand.MAX_PIECES; i++)
                  {
                      Piece AuxiliarRandomPiece = this.piecesPile.GetRandomPiece();
                      playersRegistered[p].GivePiece(AuxiliarRandomPiece);
                      // Si la Pieza es un doble
                      if (AuxiliarRandomPiece.FirstSquareValue == AuxiliarRandomPiece.SecondSquareValue)
                      {
                          // Si es un doble mayor que el actual, se actualiza la Pieza de empiece y el Jugador que la tiene
                          if (AuxiliarRandomPiece.SecondSquareValue > this.currentStartPiece.SecondSquareValue)
                          {
                              this.currentStartPiece = AuxiliarRandomPiece;
                              this.currentStartPiecePosition = i;
                              this.playerWhoStarts = p;
                              this.turno = playerWhoStarts;
                          }
                      }
                  }
              }
          }

          
    }
}