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
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using DominoService;

namespace DominoCliente
{
    /// <summary>
    /// clase  Domino, es la interfaz del usuario
    /// </summary>
    public partial class Domino : Form
    {

        public const int alto = 60;
        public bool primeraVez=true;
        public const int ancho = 30;


        private List<ShowPiece> ListPiecePanelCompare;
        private List<GroupBox> PlayerGroupBoxes;
        private int myPosition = -1;

        Dispatcher dispatcher;
        Receiver receiver;
        bool pClick;

        //controlo si tengo el turno con esto, true es que si lo tengo, y por tanto puedo poner ficha
        bool tengoTurno = false;
        
        ContextMenu mnuContextMenu = new ContextMenu();


        private ShowPiece Activetile;

        /// <summary>
        /// Contructor de la clase, se encarga de crear lo 
        /// objetos Receiver y Dispatcher.
        /// </summary> 
        public Domino()
        {
            InitializeComponent();
            receiver = new Receiver(this);
            dispatcher = new Dispatcher(receiver);
            

        }

        /// <summary>
        /// Metodo click de sentar un jugadro a la mesa,
        /// envia al Dispatcher un nuevo jugador a sentarse.
        /// </summary> 
        private void btnSentarse_Click(object sender, EventArgs e)
        {

            if (txtBoxPlayerName.Text != "")
            {
                txtBoxPlayerName.Enabled = false;
                dispatcher.RequestSitPlayer(txtBoxPlayerName.Text);
            }
            else
            {
                MessageBox.Show("Es necesario que el nombre del jugador no esté vacío");
            }
        }


        /// <summary>
        /// Metodo que muestra en la interfaz el error que 
        /// envia el servidor cuando trata de sentarse 
        /// un jugador a la mesa.
        /// </summary> 
        public void MessageError(string ErrorMessage)
        {
            txtBoxPlayerName.Enabled = true;
            MessageBox.Show(ErrorMessage);
        }


        /// <summary>
        /// Metodo que muestra en la interfaz el nombre 
        /// y la posición del nuevo jugador sentado en la mesa.
        /// </summary> 
        public void NewPlayer(string PlayerName, int PlayerPosition)
        {
            PlayerGroupBoxes.ElementAt(PlayerPosition).Text = PlayerName;
            if (PlayerName == txtBoxPlayerName.Text)
            {
                myPosition = PlayerPosition;
                this.btnSentarse.Enabled = false;
            }
        }


        /// <summary>
        /// Metodo que asigna una imagen de la ficha del domino
        /// a cada ficha del jugador en la mesa, deacuerdo al
        /// valor de cada una de sus fichas.
        /// </summary> 
        public void PaintPieceInPlayerHand(int PieceValue, int PiecePosition, int PlayerPosition)
        {
            if (PieceValue == 66){
                return;
            }
            string pictureName;
            //Control[] pictureBoxPieces = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition, false);
            //PictureBox pictureBoxPiece = (PictureBox)pictureBoxPieces[0];

            pictureName = SelectImageFileNameToPaint(PieceValue, PlayerPosition);
            int picturePostion = imageListPieces.Images.IndexOfKey(pictureName);

            //pictureBoxPiece.Image = imageListPieces.Images[picturePostion];


            ShowPiece piece = new ShowPiece(PieceValue, PlayerPosition, PiecePosition, (Bitmap)imageListPieces.Images[picturePostion]);

            if (myPosition == PlayerPosition)
            {
                //piece.MouseDown += new MouseEventHandler(piece_MouseDown);
                //piece.MouseMove += new MouseEventHandler(piece_MouseMove);
                //piece.MouseUp += new MouseEventHandler(piece_MouseUp);

                piece.MouseUp += new MouseEventHandler(piece_Click);
            }

            PlayerGroupBoxes[PlayerPosition].Controls.Add(piece);

        }
        public void PaintPieceInPlayerHandUpdate(int pieceValue, int PiecePosition, int PlayerPosition)
        {

            ShowPiece fichaAux;

            //borramos la ficha dentro del groupBox del jugador
            foreach (Control x in PlayerGroupBoxes[PlayerPosition].Controls)
            {
                if (x is ShowPiece)     // si es ficha de groupBox
                {

                    fichaAux = (ShowPiece)x;

                    if (fichaAux.valorFicha == pieceValue)  //es la ficha cuando tiene el mismo valor.
                    {

                        PlayerGroupBoxes[PlayerPosition].Controls.Remove(fichaAux);
                        return;

                    }


                }
            }






        }




        /// <summary>
        /// metodo que retorna el nombre del archivo
        /// de la ficha de dominino del jugador actual,
        /// si no es la instancia del jugador, la ficha se oculta.
        /// </summary> 
        private string SelectImageFileNameToPaint(int PieceValue, int PlayerPosition)
        {
            string pictureName;
            if (myPosition == PlayerPosition)
            {

                pictureName = IdentifyPiecePictureName(PieceValue);
            }
            else
            {
                pictureName = "HiddenPiece.png";
            }
            return pictureName;
        }

        /// <summary>
        /// metodo que retorna el nombre de la ficha
        /// deacuerdo a su valor.
        /// </summary> 
        private string IdentifyPiecePictureName(int PieceValue)
        {
            string pictureName;
            int FirstSquareValue = PieceValue / 10;
            int SecondSquareValue = PieceValue % 10;
            pictureName = FirstSquareValue + "-" + SecondSquareValue + ".png";
            return pictureName;
        }

        /// <summary>
        /// metodo que incrementa la cantidad de piezas del
        /// jugador
        /// </summary> 
        public void IncrementPlayerAvailablePieces(int PlayerPosition)
        {
            Control[] Labels = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("txtLblPiecesCountJugador" + PlayerPosition, false);
            Label RemainingPieces = (Label)Labels[0];
            int NewValue = Convert.ToInt32(RemainingPieces.Text) + 1;
            RemainingPieces.Text = NewValue.ToString();
        }
        public void DecrementPlayerAvailablePieces(int PlayerPosition)
        {
            Control[] Labels = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("txtLblPiecesCountJugador" + PlayerPosition, false);
            Label RemainingPieces = (Label)Labels[0];
            int NewValue = Convert.ToInt32(RemainingPieces.Text) - 1;
            RemainingPieces.Text = NewValue.ToString();
        }
        /// <summary>
        /// metodo que actualiza la cantidad de piezas del
        /// jugador
        /// </summary> 
        public void UpdatePiecePlayer(int PlayerPosition)
        {
            Control[] Labels = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("txtLblPiecesCountJugador" + PlayerPosition, false);
            Label RemainingPieces = (Label)Labels[0];
            int NewValue = Convert.ToInt32(RemainingPieces.Text) - 1;
            RemainingPieces.Text = NewValue.ToString();
        }

        /// <summary>
        /// metodo que coloca la ficha
        /// doble en la mesa.
        /// </summary> 
        public void PutOnTablePieceInitial(int StartPieceInt)
        {
            string pictureName = IdentifyPiecePictureName(StartPieceInt); ;
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);


            ///this.pictureBoxPiece0.Image = this.imageListPieces.Images[picturePostion];

            MessageBox.Show("" + StartPieceInt);
            ShowPiece piece = new ShowPiece( StartPieceInt ,this.pnlTable, (Bitmap)imageListPieces.Images[picturePostion]);

                   
            //agrega a la ficha el evento del click, que se deberá  agregar cuando se asigna el turno
            //este metodo no debe estar aqui, ya que cuando se pone el doble 6, se pasa turno. Debe estar en el metodo pasar turno.
            piece.MouseClick  += new MouseEventHandler(piece_Click);

            //para quitar el evento Click
            //piece.MouseClick -= new MouseEventHandler(piece_Click);

            pnlTable.Controls.Add(piece);
            

        }

        /// <summary>
        /// metodo que oculta las piezas a los demas jugadores.
        /// </summary> 
        public void HidePieceInHand(int PiecePosition, int PlayerPosition)
        {   //Imprimir nombre del control
            Console.WriteLine("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition);
            //imprime el Name del contro  buscado: Debe coincidir con el anterior
            Console.WriteLine(PlayerGroupBoxes[PlayerPosition].Controls.Find("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition, false)[0].Name);
            //Remove del control encontrado
            this.PlayerGroupBoxes[PlayerPosition].Controls.Remove(PlayerGroupBoxes[PlayerPosition].Controls.Find("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition, false)[0]);         
            //Desesperación
        }


        /// <summary>
        /// Metodo que cierra el formulario actual.
        /// </summary> 
        private void Domino_FormClosed(object sender, FormClosedEventArgs e)
        {
            dispatcher.Close();
        }

        /// <summary>
        /// metodo que de carga del formulario actual.
        /// </summary> 
        private void Domino_Load(object sender, EventArgs e)
        {

            PlayerGroupBoxes = new List<GroupBox>();
            PlayerGroupBoxes.Add(grpBoxJugador1);
            PlayerGroupBoxes.Add(grpBoxJugador2);
            PlayerGroupBoxes.Add(grpBoxJugador3);
            PlayerGroupBoxes.Add(grpBoxJugador4);
            
            pClick = false;
        }



  
        /// <summary>
        /// metodo que pone la ficha seleccionada por algun otro jugador.
        /// </summary> 
        public void PutNewOnTablePiece(PieceData ficha)
        {

            string pictureName = IdentifyPiecePictureName(ficha.PieceValue); ;
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);

            //Se crea la ficha
            ShowPiece piece = new ShowPiece(ficha.PieceValue, (Bitmap)imageListPieces.Images[picturePostion], ficha.Rotacion);

            piece.Position(ficha.PosX, ficha.PosY);

            piece.Ubicación = ficha.Ubicacion;

            piece.MouseClick += new MouseEventHandler(piece_Click);


            //Se agrega a la mesa en la posición adecuada
            pnlTable.Controls.Add(piece);

           /* countPiece(piece.Ubicación);
            lblCount.Text = this.FichasTotalIzq.ToString();*/
        }
        //Click a la ficha
        private void piece_Click(object sender, EventArgs e)
        {
            if (!tengoTurno)//si no tengo turno no hago nada
            {
                //MessageBox.Show("Quiero macareno, que no tienes el turno.");
                return;
            }
            Activetile = sender as ShowPiece;
            //Si es la ficha de mi groupBox
            if ((PlayerGroupBoxes.ElementAt(myPosition).Controls.Contains(Activetile)) && !pClick)
            {
                
                ListPiecePanelCompare = new List<ShowPiece>();

                pClick = true;

                //La añade a la lista para luego compararla
                ListPiecePanelCompare.Add(Activetile);

            }
                //Si es del panel, y se ha hecho click pimero en el groupBox
            else if ((pnlTable.Controls.Contains(Activetile)) && pClick)
            {

                //La añade a la lista para luego compararla
                ListPiecePanelCompare.Add(Activetile);
                //Si hay una ficha en la mesa, muestra el menú para elegir dónde se quiere iniciar el juego:iqda o derecha
                if (pnlTable.Controls.Count == 1)
                {

                    MenuInicial.Show(pnlTable, ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y);

                }
                else
                {
                    if (ColocarfichaJuego())
                    {
                        //si colocamos la ficha correctamente, la enviamos a los demas
                        //jugadores la ficha
                        PieceData ficha = new PieceData(ListPiecePanelCompare[0].valorFicha, 0, 0, this.myPosition);
                        PieceData ficha_anterior = new PieceData((ListPiecePanelCompare[1] as ShowPiece).getValue(), 0, 0, this.myPosition); ;
                        ficha.Rotacion = ListPiecePanelCompare[0].GetRotaciones();
                        ficha.Ubicacion = ListPiecePanelCompare[0].Ubicación;
                        ficha.PosX = ListPiecePanelCompare[0].Location.X;
                        ficha.PosY = ListPiecePanelCompare[0].Location.Y;
                        DecrementPlayerAvailablePieces(this.myPosition);
                        //Manda al dispatcher la ficha puesta en el panel
                        dispatcher.RequestPutPiecePlayer(ficha, ficha_anterior);

                        //Si ponemos una ficha en un lado, se suma uno a esa posición, para controlar cuando llegue al borde
                       // countPiece(ficha.Ubicacion);
                      //  lblCount.Text = FichasTotalIzq.ToString();
                    }


                }



                pClick = false;
            }
            else
            {
                MessageBox.Show("¡ Seleccione Primero Una Ficha del Jugador !");
            }

            Activetile = null;
        }
      



        private void MenuInicialIzquierda_Click(object sender, EventArgs e)
        {

            if (jugarFichaEnLaIzquierda(0))
            {
                //si colocamos la ficha correctamente, la enviamos a los demas
                //jugadores
                PieceData ficha = new PieceData(ListPiecePanelCompare[0].valorFicha, 0, 0, this.myPosition);
                ficha.Rotacion = ListPiecePanelCompare[0].GetRotaciones();
                ficha.Ubicacion = ListPiecePanelCompare[0].Ubicación;
                ficha.PosX = ListPiecePanelCompare[0].Location.X;
                ficha.PosY = ListPiecePanelCompare[0].Location.Y;
                PieceData ficha_anterior = new PieceData((ListPiecePanelCompare[1] as ShowPiece).getValue(), 0, 0, this.myPosition); 
                DecrementPlayerAvailablePieces(this.myPosition);

                dispatcher.RequestPutPiecePlayer(ficha, ficha_anterior);

          //     countPiece(ficha.Ubicacion);
            }

        }

        private void MenuInicialDerecha_Click(object sender, EventArgs e)
        {
            if (jugarFichaEnLaDerecha(1))
            {

                //si colocamos la ficha correctamente, la enviamos a los demas
                //jugadores
                PieceData ficha = new PieceData(ListPiecePanelCompare[0].valorFicha, 0, 0, this.myPosition);
                ficha.Rotacion = ListPiecePanelCompare[0].GetRotaciones();
                ficha.Ubicacion = ListPiecePanelCompare[0].Ubicación;
                ficha.PosX = ListPiecePanelCompare[0].Location.X;
                ficha.PosY = ListPiecePanelCompare[0].Location.Y;
                PieceData ficha_anterior = new PieceData((ListPiecePanelCompare[1] as ShowPiece).getValue(), 0, 0, this.myPosition);
                DecrementPlayerAvailablePieces(this.myPosition);

                dispatcher.RequestPutPiecePlayer(ficha, ficha_anterior);
               // countPiece(ficha.Ubicacion);

            }
        }

        private void AsignarUbicaciónFicha(int ubicacion,bool giro)
        {

            if (pnlTable.Controls.Count < 3)
            {
                if (ubicacion == 0)
                {

                    // ListPiecePanelCompare[0] MI FICHA
                     //ListPiecePanelCompare[1] LA SELECCIONADA
                    ListPiecePanelCompare[0].Ubicación = 0;   //se asigna mi ficha a la izquierda (0)
                    ListPiecePanelCompare[1].Ubicación = 1;     //se asigna ficha mesa a la derecha (1)
                }
                else
                {
                    ListPiecePanelCompare[0].Ubicación = 1;   //se asigna mi ficha a la derecha
                    ListPiecePanelCompare[1].Ubicación = 0;     //se asigna ficha mesa a la izquierda
                }

            }
            else
            {
                //tooodoo aquiiiiiiiiiiiiiiiiiiiiiiiiiii
                switch (ubicacion)   // situación de la ficha seleccionada en la mesa
                {
                    case 0:   //izquierda
                       // if(giro){
                      //   ListPiecePanelCompare[0].Ubicación = 3;   //se asigna mi ficha a la izquieda
                       //  ListPiecePanelCompare[1].Ubicación = -1; }//no se asigna
                      //  else{
                            ListPiecePanelCompare[0].Ubicación = 0;   //se asigna mi ficha a la izquieda
                            ListPiecePanelCompare[1].Ubicación = -1; 
                      //  }
                        break;
                    case 1:   //derecha
                        ListPiecePanelCompare[0].Ubicación = 1;   //se asigna mi ficha a la derecha
                        ListPiecePanelCompare[1].Ubicación = -1;     //no se asigna
                        break;

                  /*  case 2:   //arriba
                        ListPiecePanelCompare[0].Ubicación = 2;   //se asigna mi ficha arriba (2)
                        ListPiecePanelCompare[1].Ubicación = -1;     //no se asigna
                        break;

                    case 3:   //abajo
                        ListPiecePanelCompare[0].Ubicación = 3;   //se asigna mi ficha abajo
                        ListPiecePanelCompare[1].Ubicación = -1;
                        break;*/

                }

            }

        }



        private bool ColocarfichaJuego()
        {

            bool valor = false;
            switch (this.ListPiecePanelCompare[1].Ubicación)   // situación de la ficha seleccionada en la mesa
            {
                case 0:   //izquierda
                    valor = jugarFichaEnLaIzquierda(0);
                    break;
                case 1:   //derecha
                    valor = jugarFichaEnLaDerecha(1);
                    break;
                case 2:   //arriba
                    valor = this.jugarFichaArriba(2);
                    break;
                case 3:   //abajo
                    valor = this.jugarFichaAbajo(3);

                    break;
            }

            return valor;

        }


        private bool jugarFichaEnLaIzquierda(int ubicacion)
        {
            bool valor = false;
            bool giro = false;
            if (contieneValorDeseado(ubicacion))
            {
               
                    giro = false;
                if (ColocarFichaIzquierda(giro))
                {
                    pnlTable.Controls.Add(ListPiecePanelCompare[0]);
                    AsignarUbicaciónFicha(ubicacion,giro);
                    valor = true;
                }
            }
            else
            {

                MessageBox.Show("la ficha del jugador no tiene el valor deseado");
                valor = false;
            }
            return valor;
        }

        //QUITAR LOS REF CON LA LISTA, YA QUE ESTA DECLARADA EN TODO EL FORMULARIO
        private bool jugarFichaEnLaDerecha(int ubicacion)
        {
            bool valor = false;
            if (contieneValorDeseado(ubicacion))
            {

                if (ColocarFichaDerecha())
                {
                    pnlTable.Controls.Add(ListPiecePanelCompare[0]);
                    AsignarUbicaciónFicha(ubicacion,false);
                    valor = true;
                }


            }
            else
            {

                MessageBox.Show("la ficha del jugador no tiene el valor deseado");
                valor = false;
            }
            return valor;
        }


        private bool jugarFichaArriba( int ubicacion)
        {
            bool valor = false;
            if (contieneValorDeseado(ubicacion))
            {

                if (ColocarFichaArriba())
                {
                    pnlTable.Controls.Add(ListPiecePanelCompare[0]);
                    AsignarUbicaciónFicha(ubicacion,false);
                    valor = true;
                }
            }
            else
            {

                MessageBox.Show("la ficha del jugador no tiene el valor deseado");
                valor = false;
            }
            return valor;
        }

        private bool jugarFichaAbajo(int ubicacion)
        {
            bool giro = false;
            bool valor = false;
            if (contieneValorDeseado(ubicacion))
            {
                
                if (ColocarFichaAbajo(giro))
                {
                   
                    pnlTable.Controls.Add(ListPiecePanelCompare[0]);
                    AsignarUbicaciónFicha(ubicacion,giro);
                    valor = true;
                }
            }
            else
            {

                MessageBox.Show("la ficha del jugador no tiene el valor deseado");
                valor = false;
            }
            return valor;

        }



        private bool contieneValorDeseado(int ubicacion)
        {
            bool valor = false;

            switch (ubicacion)
            {
                case 0:   //izquierda
                    if ((ListPiecePanelCompare[0].up == ListPiecePanelCompare[1].Izquierda) || (ListPiecePanelCompare[0].down == ListPiecePanelCompare[1].Izquierda))
                        valor = true;
                    break;
                case 1:   //derecha
                    if ((ListPiecePanelCompare[0].up == ListPiecePanelCompare[1].Derecha) || (ListPiecePanelCompare[0].down == ListPiecePanelCompare[1].Derecha))
                        valor = true;
                    break;
                case 2:   //arriba
                    if ((ListPiecePanelCompare[0].up == ListPiecePanelCompare[1].up) || (ListPiecePanelCompare[0].down == ListPiecePanelCompare[1].up))
                        valor = true;
                    break;
                case 3:   //abajo
                    if ((ListPiecePanelCompare[0].up == ListPiecePanelCompare[1].down) || (ListPiecePanelCompare[0].down == ListPiecePanelCompare[1].down))
                        valor = true;

                    break;
            }

            return valor;
        }

        private void giramosficha( int ubicacion, int comparacion)
        {


            switch (ubicacion)
            {
                case 0:   //izquierda
                    while (ListPiecePanelCompare[0].Derecha != comparacion)
                    {
                        ListPiecePanelCompare[0].girarFicha();
                    }
                    break;
                case 1:   //derecha
                    while (ListPiecePanelCompare[0].Izquierda != comparacion)
                    {
                        ListPiecePanelCompare[0].girarFicha();
                    }
                    break;
                case 2:   //arriba
                    while (ListPiecePanelCompare[0].down != comparacion)
                    {
                        ListPiecePanelCompare[0].girarFicha();
                    }
                    break;
                case 3:   //abajo
                    while (ListPiecePanelCompare[0].up != comparacion)
                    {
                        ListPiecePanelCompare[0].girarFicha();
                    }
                    break;
            }

        }

        private bool ColocarFichaIzquierda(bool giro)
        {

            try
            {

                //si ficha en mesa es doble y la nuestra no
                if ((ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble))
                {
                    if (giro) {
                        giramosficha(3, ListPiecePanelCompare[1].Izquierda);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X,ListPiecePanelCompare[1].Location.Y+alto);
                       
                    }else {               
                    giramosficha(0, ListPiecePanelCompare[1].Izquierda);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X - alto, ListPiecePanelCompare[1].Location.Y + ((alto - ancho) / 2));
                    
                    }
                    return true;
                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra no
                {
                    if (giro)
                    {
                        giramosficha(3, ListPiecePanelCompare[1].Izquierda);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y + ancho);
                    }
                    else {
                        giramosficha(0, ListPiecePanelCompare[1].Izquierda);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X - alto, ListPiecePanelCompare[1].Location.Y);
                        
                    }
                    return true;
                    

                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra si es doble
                {
                    //no giramos ficha,
                    if (giro)
                    {
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, (ListPiecePanelCompare[1].Location.Y +ancho));
                    }
                    else
                    {
                      ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X - ancho, (ListPiecePanelCompare[1].Location.Y + (ancho / 2)) - (alto / 2));
                    }
                    return true;

                }

                return false;
            }

            catch (Exception e)
            {

                MessageBox.Show(e.Message);
                return false;
            }

        }

        private bool ColocarFichaDerecha()
        {
            try
            {

                //si ficha en mesa es doble y la nuestra no
                if ((ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble))
                {
                    giramosficha(1,ListPiecePanelCompare[1].Derecha);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X + ancho, ListPiecePanelCompare[1].Location.Y + ((alto - ancho) / 2));
                    return true;
                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra no
                {
                    giramosficha(1,ListPiecePanelCompare[1].Derecha);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X + alto, ListPiecePanelCompare[1].Location.Y);
                    return true;

                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra si es doble
                {
                    //no giramos ficha,
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X + alto, (ListPiecePanelCompare[1].Location.Y + (ancho / 2)) - (alto / 2));
                    return true;

                }

                return false;
            }

            catch (Exception e)
            {

                MessageBox.Show(e.Message);
                return false;
            }


        }

        private bool ColocarFichaArriba()
        {

            try
            {

                //si ficha en mesa es doble y la nuestra no
                if ((ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble))
                {
                    giramosficha(2, ListPiecePanelCompare[1].up);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X + ((alto - ancho) / 2), ListPiecePanelCompare[1].Location.Y - alto);
                    return true;
                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra no
                {
                    giramosficha(2, ListPiecePanelCompare[1].up);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y - alto);
                    return true;

                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra si es doble
                {
                    //no giramos ficha,
                    ListPiecePanelCompare[0].Position((ListPiecePanelCompare[1].Location.X + (ancho / 2)) - (alto / 2), ListPiecePanelCompare[1].Location.Y - ancho);
                    return true;

                }

                return false;
            }

            catch (Exception e)
            {

                MessageBox.Show(e.Message);
                return false;
            }

        }

        private bool ColocarFichaAbajo(bool giro)
        {

            try
            {

                //si ficha en mesa es doble y la nuestra no
                if ((ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble))
                {
                    if(giro){
                        giramosficha(1, ListPiecePanelCompare[1].down);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X+alto, ListPiecePanelCompare[1].Location.Y);
                    }
                    else{
                    giramosficha(3, ListPiecePanelCompare[1].down);
                    ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X + ((alto - ancho) / 2), ListPiecePanelCompare[1].Location.Y + (alto + ancho));
                    }return true;
                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (!ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra no
                {
                    if (giro)
                    {
                        giramosficha(1, ListPiecePanelCompare[1].down);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y+alto);
                    }
                    else
                    {
                        giramosficha(3, ListPiecePanelCompare[1].down);
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y + alto);
                    } return true;

                }
                else if ((!ListPiecePanelCompare[1].isDoble) && (ListPiecePanelCompare[0].isDoble)) //si ficha en mesa no es doble y la nuestra si es doble
                {
                    //no giramos ficha,
                    if (giro)
                    {
                        ListPiecePanelCompare[0].Position(ListPiecePanelCompare[1].Location.X, ListPiecePanelCompare[1].Location.Y + alto);

                    }
                    else
                    {
                        ListPiecePanelCompare[0].Position((ListPiecePanelCompare[1].Location.X + (ancho / 2)) - (alto / 2), ListPiecePanelCompare[1].Location.Y + alto);
                    }
                    return true;

                }

                return false;
            }

            catch (Exception e)
            {

                MessageBox.Show(e.Message);
                return false;
            }


        }

        public void ActualizaTurno(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2)
        {
            //en cualquier caso actualizo la ayuda visual para saber quen lleva el turno
            pictureTurn0.BackColor = SystemColors.Control;
            pictureTurn1.BackColor = SystemColors.Control;
            pictureTurn2.BackColor = SystemColors.Control;
            pictureTurn3.BackColor = SystemColors.Control;
            OcultaBotonPasar();
            tengoTurno = false;
            switch (posicionJugadorConTurno)
            {
                case 0:
                    pictureTurn0.BackColor = Color.Green;
                    break;
                case 1:
                    pictureTurn1.BackColor = Color.Green;
                    break;
                case 2:
                    pictureTurn2.BackColor = Color.Green;
                    break;
                case 3:
                    pictureTurn3.BackColor = Color.Green;
                    break;
            }
            //ahora, si tengo turno entro
            if (posicionJugadorConTurno == myPosition)//el turno es mio, activo controles
            {
                tengoTurno = true;
                if (!puedePoner)//si no puedo poner activo el boton de pasar
                {
                    switch (posicionJugadorConTurno)
                    {
                        case 0:
                            BotonPasa0.Visible = true;
                            break;
                        case 1:
                            BotonPasa1.Visible = true;
                            break;
                        case 2:
                            BotonPasa2.Visible = true;
                            break;
                        case 3:
                            BotonPasa3.Visible = true;
                            break;
                    }
                }
                //MessageBox.Show("yo soy " + myPosition + " el turno es de " + posicionJugadorConTurno + " y puede poner es: " + puedePoner + " extre1 es " + puedo1 + " extremo2 es " + puedo2);
            }
            
        }

        private void pasoTurno(object sender, EventArgs e)//solo sera posible cuando el boton se active, esto solo pasa cuando no puedo poner
        {
            OcultaBotonPasar();
            dispatcher.PasoTurno(myPosition);
        }

        private void OcultaBotonPasar()
        {
            BotonPasa0.Visible = false;
            BotonPasa1.Visible = false;
            BotonPasa2.Visible = false;
            BotonPasa3.Visible = false;
        }
        public void ActualizarMarcadores(int [] Marcadores){
            this.Marcador1.Text = Marcadores[0].ToString();
            this.Marcador2.Text = Marcadores[1].ToString();
        }

      
    }
}
