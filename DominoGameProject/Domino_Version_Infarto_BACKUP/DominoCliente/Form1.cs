/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  Domino
 *  Descripción de la clase:
 *  Clase que implementa logica del cliente, acciones y actualiza su vista.
 */
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using DominoCliente.DominoServiceReference;


namespace DominoCliente
{
    public partial class Domino : Form
    {
        private List<GroupBox> playerGroupBoxes;
        Controlador control;
        private PieceBox [] pieceBoxes = new PieceBox [55];
        private PieceBox[] pieceBoxesPlayer1 = new PieceBox[7];
        private PieceBox[] pieceBoxesPlayer2 = new PieceBox[7];
        private PieceBox[] pieceBoxesPlayer3 = new PieceBox[7];
        private PieceBox[] pieceBoxesPlayer4 = new PieceBox[7];
        Label[] marcadores = new Label[2];
        private int myposition;
        int rightIndex = 27;
        int leftIndex = 27;
        //controlo si tengo el turno con esto, true es que si lo tengo, y por tanto puedo poner ficha
        bool tengoTurno = false;
        private PieceBox Ficha_Click;//control de si he hecho click en una ficha


        public Domino()
        {
            InitializeComponent();
        }
        
        private void BtnSentarse_Click(object sender, EventArgs e)
        {
            control.BtnSentarse_Click(sender, e, txtBoxPlayerName);
        }

        public void NewPlayerJoined(PlayerData jugador)
        {
            string PlayerName = jugador.nombre;
            if (PlayerName.Equals(txtBoxPlayerName.Text)){
                this.myposition = jugador.posicion;
            }
            playerGroupBoxes.ElementAt(jugador.posicion).Text = PlayerName;
        }

        public void PlayerNotSat(string errorMessage)
        {
            txtBoxPlayerName.Enabled = true;
            MessageBox.Show(errorMessage);
        }

        public void ShowPiece(PieceData pieza, int myPosition)
        {
            int PieceValue = pieza.valor;
            int PiecePosition = pieza.posicionEnMano;
            int PlayerPosition = pieza.jugador;
            //Se muestra al jugador una pieza que se encuentra en su poder
            PaintPiece(PieceValue, PiecePosition, PlayerPosition, myPosition);
            // Se actualiza el valor de las piezas de las que dispone el jugador
            UpdatePlayerAvailablesPiecesInformacion(PlayerPosition);
        }

        private void PaintPiece(int pieceValue, int piecePosition, int playerPosition, int myPosition)
        {
            string pictureName;
            PieceBox elegida;
            elegida = ObtenerBoxElegida(piecePosition, playerPosition);
            elegida.SetValue(pieceValue);
            pictureName = SelectImageFileNameToPaint(pieceValue, playerPosition, myPosition);
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            elegida.GetBox().Image = this.imageListPieces.Images[picturePostion];
            if (this.myposition == playerPosition) elegida.GetBox().MouseClick += new MouseEventHandler(ClickEnFichaJugador);
        }

        private PieceBox ObtenerBoxElegida(int piecePosition, int playerPosition)
        {
            PieceBox elegida;
            switch (playerPosition)
            {
                case 0:
                    elegida = pieceBoxesPlayer1[piecePosition];
                    break;
                case 1:
                    elegida = pieceBoxesPlayer2[piecePosition];
                    break;
                case 2:
                    elegida = pieceBoxesPlayer3[piecePosition];
                    break;
                case 3:
                    elegida = pieceBoxesPlayer4[piecePosition];
                    break;
                default:
                    elegida = null;
                    break;
            }
            return elegida;
        }

        private string SelectImageFileNameToPaint(int pieceValue, int playerPosition, int myPosition)
        {
            string pictureName;
            if (myPosition == playerPosition)
            {
                pictureName = IdentifyPiecePictureName(pieceValue);
            }
            else
            {
                pictureName = "HiddenPiece.png";
            }
            return pictureName;
        }

        private static string IdentifyPiecePictureName(int pieceValue)
        {
            string pictureName;
            int FirstSquareValue = pieceValue / 10;
            int SecondSquareValue = pieceValue % 10;
            pictureName = FirstSquareValue + "-" + SecondSquareValue + ".png";
            return pictureName;
        }

        private void UpdatePlayerAvailablesPiecesInformacion(int playerPosition)
        {
            Control[] Labels = playerGroupBoxes.ElementAt(playerPosition).Controls.Find("txtLblPiecesCountJugador" + playerPosition, false);
            Label RemainingPieces = (Label)Labels[0];
            int NewValue = Convert.ToInt32(RemainingPieces.Text) + 1;
            RemainingPieces.Text = NewValue.ToString();
        }

        public void PutPiece(PieceData pieza)
        {
            int StartPieceInt = pieza.valor;
            int PiecePosition = pieza.posicionEnMano;
            int PlayerPosition = pieza.jugador;
            HidePieceInHand(PiecePosition, PlayerPosition);
            ShowPieceInTable(StartPieceInt);
            DecrementPlayerAvailablepPieces(PlayerPosition);
        }

        private void DecrementPlayerAvailablepPieces(int playerPosition)
        {
            // Se actualiza el valor de las piezas de las que dispone el jugador
            Control[] Labels = playerGroupBoxes.ElementAt(playerPosition).Controls.Find("txtLblPiecesCountJugador" + playerPosition, false);
            Label RemainingPieces = (Label)Labels[0];
            int NewValue = Convert.ToInt32(RemainingPieces.Text) - 1;
            RemainingPieces.Text = NewValue.ToString();
        }

        private void ShowPieceInTable(int startPieceInt)
        {
            // Pone en la mesa la Pieza con la que tiene que empezar el Jugador.
            string pictureName = IdentifyPiecePictureName(startPieceInt);
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            this.pieceBoxes[rightIndex].GetBox().Image = this.imageListPieces.Images[picturePostion];
            this.pieceBoxes[rightIndex].SetValue(startPieceInt);
            this.pieceBoxes[rightIndex].GetBox().MouseClick += new MouseEventHandler(ClickEnFichaMesa);
        }

        private void HidePieceInHand(int piecePosition, int playerPosition)
        {
            // Deja de mostrar la Pieza en el jugador que la acaba de poner
            PieceBox[] Aux;
            switch (playerPosition)
            {
                case 0:
                    Aux = this.pieceBoxesPlayer1;
                    break;
                case 1:
                    Aux = this.pieceBoxesPlayer2;
                    break;
                case 2:
                    Aux = this.pieceBoxesPlayer3;
                    break;
                case 3:
                    Aux = this.pieceBoxesPlayer4;
                    break;
                default:
                    Aux = null;
                    break;
            }
            Aux[piecePosition].GetBox().Visible = false;
            Aux[piecePosition].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaJugador);
        }


        private void Domino_Load(object sender, EventArgs e)
        {
            InstanceContext miContexto = CreatePlayerGroupBoxes();
            marcadores[0]=this.marcadorEquipo1;
            marcadores[1]=this.marcadorEquipo2;
            CreatePieceBoxes();
            InitilizePieceBox();
            control = new Controlador(this, miContexto);
        }

        private void InitilizePieceBox()
        {
            for (int i = 0; i < 4; i++)
            {
                GroupBox Auxiliar = this.Controls.Find("grpBoxJugador" + (i + 1), false)[0] as GroupBox;

                for (int j = 0; j < 7; j++)
                {
                    PieceBox[] Aux;
                    switch (i)
                    {
                        case 0:
                            Aux = this.pieceBoxesPlayer1;
                            break;
                        case 1:
                            Aux = this.pieceBoxesPlayer2;
                            break;
                        case 2:
                            Aux = this.pieceBoxesPlayer3;
                            break;
                        case 3:
                            Aux = this.pieceBoxesPlayer4;
                            break;
                        default:
                            Aux = null;
                            break;
                    }
                    Aux[j] = new PieceBox(-1, (Auxiliar.Controls.Find("pictureBoxPlayer" + i + "Piece" + j, false)[0] as PictureBox));
                }
            }
        }

        private InstanceContext CreatePlayerGroupBoxes()
        {
            playerGroupBoxes = new List<GroupBox>();
            playerGroupBoxes.Add(grpBoxJugador1);
            playerGroupBoxes.Add(grpBoxJugador2);
            playerGroupBoxes.Add(grpBoxJugador3);
            playerGroupBoxes.Add(grpBoxJugador4);
            InstanceContext miContexto = new InstanceContext(this);
            return miContexto;
        }

        private void CreatePieceBoxes()
        {
            for (int j = 0; j < 55; j++)
            {
                pieceBoxes[j] = new PieceBox(-1, (this.pnlTable.Controls.Find("pictureBoxPiece" + j, false)[0]) as PictureBox);
            }
            for (int j = 0; j < 55; j++)
            {
                if (j == 0) pieceBoxes[j].SetNextBox(pieceBoxes[j + 1]);
                if (j == 54) pieceBoxes[j].SetPrevBox(pieceBoxes[j - 1]);
                if (j > 0 && j < 54)
                {
                    pieceBoxes[j].SetPrevBox(pieceBoxes[j - 1]);
                    pieceBoxes[j].SetNextBox(pieceBoxes[j + 1]);
                }
            }
        }

        private void Domino_FormClosed(object sender, FormClosedEventArgs e)
        {
            control.closeServer();
        }

        public void DisableTxtBoxPlayerName()
        {
            this.txtBoxPlayerName.Enabled = false;
        }

        public void NombreVacio()
        {
            MessageBox.Show("Es necesario que el nombre del jugador no esté vacío");
        }

        public void DisableBoton()
        {
            this.btnSentarse.Enabled = false;
        }

        public TextBox GetTextBoxPlayerName()
        {
            return txtBoxPlayerName;
        }




        /*PARA PINTAR LAS COSAS*/

        //Al hacer click en imagenes, se ejecuta este metodo:
        private void ClickEnFichaMesa(object sender, EventArgs e)
        {
            if (this.tengoTurno == false) {  return; }//controlar que tengo el turno

            if (Ficha_Click == null) { MessageBox.Show("Elige una Ficha."); return; } //controlar que tengo ficha seleccionada al intentar poner en mesa
            PictureBox Clicked = (sender as PictureBox);
            PieceBox Table_clicked = null;
            Table_clicked = ObtainPieceBoxClicked(Clicked, Table_clicked);
            int selector = 0;
            int clicked_firstValue = Table_clicked.GetValue() / 10;
            int clicked_secondValue = Table_clicked.GetValue() % 10;
            int usedIndex;
            PieceBox Destination = null;
            string pictureName = IdentifyPiecePictureName(Ficha_Click.GetValue());
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            if (rightIndex == leftIndex)
            {
                usedIndex = CasoSeisDoble(Table_clicked, ref selector, clicked_firstValue, clicked_secondValue, ref Destination, picturePostion);//colocar por la derecha
                if (usedIndex == 999) return;
            }
            else
            {
                if (String.ReferenceEquals(Table_clicked.GetBox().Name,pieceBoxes[rightIndex].GetBox().Name))
                {
                    usedIndex = ClickDerecha(Table_clicked, ref selector, clicked_firstValue, clicked_secondValue, ref Destination, picturePostion);
                    if (usedIndex == 999) return;
                }
                else if (String.ReferenceEquals(Table_clicked.GetBox().Name, pieceBoxes[leftIndex].GetBox().Name))
                {
                    usedIndex = ClickIzquierda(Table_clicked, ref selector, clicked_firstValue, clicked_secondValue, ref Destination, picturePostion);
                    if (usedIndex == 999) return;
                }
                else//si no se hace click en los extremos, no se pone la ficha.
                {
                    return;
                }
            }
            PonerLaFichaYEnviarAServidor(selector, usedIndex, Destination);
        }

        private void PonerLaFichaYEnviarAServidor(int selector, int usedIndex, PieceBox Destination)
        {
            Destination.GetBox().MouseClick += new MouseEventHandler(ClickEnFichaMesa);
            Destination.SetValue(Ficha_Click.GetValue());
            Ficha_Click.GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaJugador);
            Ficha_Click.GetBox().BorderStyle = BorderStyle.None;
            Ficha_Click.GetBox().Visible = false;
            DecrementPlayerAvailablepPieces(this.myposition);
            PieceData data = new PieceData();
            data.jugador = this.myposition;
            data.valor = Destination.GetValue();
            data.posicionEnMano = (int)Char.GetNumericValue(Ficha_Click.GetBox().Name.ToCharArray()[Ficha_Click.GetBox().Name.ToCharArray().Length - 1]);
            data.selector = selector;
            data.index = usedIndex;
            control.envia.RequestPutPiece(data);
            Ficha_Click = null;
        }

        private int ClickIzquierda(PieceBox Table_clicked, ref int selector, int clicked_firstValue, int clicked_secondValue, ref PieceBox Destination, int picturePostion)
        {
            int usedIndex;
            PieceBox prev = Table_clicked.GetNextBox();
            int table_firstValue = prev.GetValue() / 10;
            int table_secondValue = prev.GetValue() % 10;

            int value_free = -1;
            if (clicked_firstValue == clicked_secondValue)
            {
                value_free = clicked_secondValue;
            }
            else
            {
                if (clicked_firstValue == table_firstValue || clicked_firstValue == table_secondValue)
                {
                    value_free = clicked_secondValue;
                }
                else if (clicked_secondValue == table_firstValue || clicked_secondValue == table_secondValue)
                {
                    value_free = clicked_firstValue;
                }
            }
            int hand_firstValue = Ficha_Click.GetValue() / 10;
            int hand_secondValue = Ficha_Click.GetValue() % 10;
            if (hand_firstValue != value_free & hand_secondValue != value_free)
            {
                MessageBox.Show("No es posible colocar la ficha (" + hand_firstValue + "|" + hand_secondValue + ") al lado de la ficha (" + clicked_firstValue + "|" + clicked_secondValue + ").");
                return 999;
            }
            else if (hand_firstValue == value_free)
            {
                selector = 1;
            }
            else if (hand_secondValue == value_free)
            {
                selector = 2;
            }
            this.pieceBoxes[leftIndex].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaMesa);
            leftIndex--;
            usedIndex = leftIndex;
            Destination = this.pieceBoxes[leftIndex];
            Destination.GetBox().Image = this.imageListPieces.Images[picturePostion];
            GirarFicha(Destination.GetBox().Image, usedIndex, selector);
            return usedIndex;
        }

        private int ClickDerecha(PieceBox Table_clicked, ref int selector, int clicked_firstValue, int clicked_secondValue, ref PieceBox Destination, int picturePostion)
        {
            int usedIndex;
            PieceBox prev = Table_clicked.GetPrevBox();
            int table_firstValue = prev.GetValue() / 10;
            int table_secondValue = prev.GetValue() % 10;

            int value_free = -1;
            if (clicked_firstValue == clicked_secondValue)
            {
                value_free = clicked_secondValue;
            }
            else
            {
                if (clicked_firstValue == table_firstValue || clicked_firstValue == table_secondValue)
                {
                    value_free = clicked_secondValue;
                }
                else if (clicked_secondValue == table_firstValue || clicked_secondValue == table_secondValue)
                {
                    value_free = clicked_firstValue;
                }
            }
            int hand_firstValue = Ficha_Click.GetValue() / 10;
            int hand_secondValue = Ficha_Click.GetValue() % 10;
            if (hand_firstValue != value_free & hand_secondValue != value_free)
            {
                MessageBox.Show("No es posible colocar la ficha (" + hand_firstValue + "|" + hand_secondValue + ") al lado de la ficha (" + clicked_firstValue + "|" + clicked_secondValue + ").");
                return 999;
            }
            else if (hand_firstValue == value_free)
            {
                selector = 1;
            }
            else if (hand_secondValue == value_free)
            {
                selector = 2;
            }
            this.pieceBoxes[rightIndex].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaMesa);
            rightIndex++;
            usedIndex = rightIndex;
            Destination = this.pieceBoxes[rightIndex];
            Destination.GetBox().Image = this.imageListPieces.Images[picturePostion];
            GirarFicha(Destination.GetBox().Image, usedIndex, selector);
            return usedIndex;
        }

        private int CasoSeisDoble(PieceBox Table_clicked, ref int selector, int clicked_firstValue, int clicked_secondValue, ref PieceBox Destination, int picturePostion)
        {
            int usedIndex;
            PieceBox prev = Table_clicked.GetPrevBox();
            int table_firstValue = prev.GetValue() / 10;
            int table_secondValue = prev.GetValue() % 10;

            int value_free = -1;
            if (clicked_firstValue == clicked_secondValue)
            {
                value_free = clicked_secondValue;
            }
            else
            {
                if (clicked_firstValue == table_firstValue || clicked_firstValue == table_secondValue)
                {
                    value_free = clicked_secondValue;
                }
                else if (clicked_secondValue == table_firstValue || clicked_secondValue == table_secondValue)
                {
                    value_free = clicked_firstValue;
                }
            }
            int hand_firstValue = Ficha_Click.GetValue() / 10;
            int hand_secondValue = Ficha_Click.GetValue() % 10;
            if (hand_firstValue != value_free & hand_secondValue != value_free)
            {
                MessageBox.Show("No es posible colocar la ficha (" + hand_firstValue + "|" + hand_secondValue + ") al lado de la ficha (" + clicked_firstValue + "|" + clicked_secondValue + ").");
                return 999;
            }
            else if (hand_firstValue == value_free)
            {
                selector = 1;
            }
            else if (hand_secondValue == value_free)
            {
                selector = 2;
            }
            rightIndex++;
            usedIndex = rightIndex;
            Destination = this.pieceBoxes[rightIndex];
            Destination.GetBox().Image = this.imageListPieces.Images[picturePostion];
            GirarFicha(Destination.GetBox().Image, usedIndex, selector);
            return usedIndex;
        }

        private PieceBox ObtainPieceBoxClicked(PictureBox Clicked, PieceBox Table_clicked)
        {
            foreach (PieceBox piece in this.pieceBoxes)
            {
                if (Clicked.Name.Equals(piece.GetBox().Name))
                {
                    Table_clicked = piece;
                    break;
                }
            }
            return Table_clicked;
        }


        private void ClickEnFichaJugador(object sender, EventArgs e)
        {
            if (Ficha_Click != null) Ficha_Click.GetBox().BorderStyle = BorderStyle.None;
            if (this.tengoTurno == false) return;
            PieceBox[] Aux=null;
            PictureBox Clicked = (sender as PictureBox);
            switch (this.myposition) { 
                case 0:
                    Aux = this.pieceBoxesPlayer1;
                    break;
                case 1:
                    Aux = this.pieceBoxesPlayer2;
                    break;
                case 2:
                    Aux = this.pieceBoxesPlayer3;
                break;
                case 3:
                    Aux = this.pieceBoxesPlayer4;
                break;
            }
            foreach (PieceBox piece in Aux) {
                if (Clicked.Name.Equals(piece.GetBox().Name)) {
                    this.Ficha_Click = piece;
                    this.Ficha_Click.GetBox().BorderStyle = BorderStyle.Fixed3D;
                    break;
                }
            }
            
            //esto de momento tamopc vale para naad
        }

        public void ShowPieceOnIndex(int value,int index,int selector)
        {
            // Pone en la mesa la Pieza con la que tiene que empezar el Jugador.
            string pictureName = IdentifyPiecePictureName(value);
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            this.pieceBoxes[index].GetBox().Image = this.imageListPieces.Images[picturePostion];
            this.pieceBoxes[index].SetValue(value);

            if (index==28) { 
                rightIndex++;
                GirarFicha(this.pieceBoxes[index].GetBox().Image, index, selector);

            }else if(this.rightIndex+1==index){
                this.pieceBoxes[rightIndex].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaMesa);
                rightIndex++;
                GirarFicha(this.pieceBoxes[index].GetBox().Image, index, selector);

            }
            else if (this.leftIndex - 1 == index) {
                this.pieceBoxes[leftIndex].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaMesa);
                leftIndex--;
                GirarFicha(this.pieceBoxes[index].GetBox().Image, index, selector);

            }
            this.pieceBoxes[index].GetBox().MouseClick += new MouseEventHandler(ClickEnFichaMesa);

        }

        public void HidePieceOfAnotherPlayer(int playerPosition, int piecePosition)
        {
            PieceBox[] Aux = null;
            switch (playerPosition)
            {
                case 0:
                    Aux = this.pieceBoxesPlayer1;
                    break;
                case 1:
                    Aux = this.pieceBoxesPlayer2;
                    break;
                case 2:
                    Aux = this.pieceBoxesPlayer3;
                    break;
                case 3:
                    Aux = this.pieceBoxesPlayer4;
                    break;
            }
            Aux[piecePosition].GetBox().MouseClick -= new MouseEventHandler(ClickEnFichaJugador);
            Aux[piecePosition].GetBox().Visible = false;
            DecrementPlayerAvailablepPieces(playerPosition);
            
        }
        private void GirarFicha(Image img, int index, int selector) {
            if (selector == 1) {
                if ((index > 21 && index < 27) || (index > 33 && index < 44) || (index > 44 && index <= 54))
                {
                    img.RotateFlip(RotateFlipType.Rotate90FlipNone);
                }
                else if ((index > 10 && index < 21) || (index > 27 && index < 33) || (index >= 0 && index < 10)){
                    img.RotateFlip(RotateFlipType.Rotate270FlipNone);
                }else if (index == 44 || index == 33) {
                    img.RotateFlip(RotateFlipType.RotateNoneFlipY);
                }

            }
            if (selector == 2){
                if ((index > 21 && index < 27) || (index > 33 && index < 44) || (index > 44 && index <= 54)){
                    img.RotateFlip(RotateFlipType.Rotate90FlipX);
                }
                else if ((index > 10 && index < 21) || (index > 27 && index < 33) || (index >= 0 && index < 10)){
                    img.RotateFlip(RotateFlipType.Rotate270FlipX);
                }else if (index == 21 || index == 10){
                    img.RotateFlip(RotateFlipType.RotateNoneFlipY);
                }
            }
        }

        private void PasoTurno(object sender, EventArgs e)//solo sera posible cuando el boton se active, esto solo pasa cuando no puedo poner
        {
            OcultaBotonPasar();
            if (Ficha_Click != null) { 
                Ficha_Click.GetBox().BorderStyle = BorderStyle.None;
                Ficha_Click = null;
            }
            control.envia.PasoTurno();
        }

        private void OcultaBotonPasar()
        {
            BotonPasa0.Visible = false;
            BotonPasa1.Visible = false;
            BotonPasa2.Visible = false;
            BotonPasa3.Visible = false;
        }

        public void ActualizaTurno(int posicionJugadorConTurno, bool puedePoner, int puedo1, int puedo2)
        {
            //en cualquier caso actualizo la ayuda visual para saber quen lleva el turno
            pictureTurn0.BackColor = Color.FromArgb(0,155,166);
            pictureTurn1.BackColor = Color.FromArgb(0, 155, 166);
            pictureTurn2.BackColor = Color.FromArgb(0, 155, 166);
            pictureTurn3.BackColor = Color.FromArgb(0, 155, 166);
            OcultaBotonPasar();
            tengoTurno = false;
            MuestraQuienTieneTurno(posicionJugadorConTurno);
            //ahora, si tengo turno entro
            if (posicionJugadorConTurno == this.myposition)//el turno es mio, activo controles
            {
                tengoTurno = true;
                if (!puedePoner)//si no puedo poner activo el boton de pasar
                {
                    MuestraBotonDePasar(posicionJugadorConTurno);
                }
            }

        }

        private void MuestraBotonDePasar(int posicionJugadorConTurno)
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

        private void MuestraQuienTieneTurno(int posicionJugadorConTurno)
        {
            switch (posicionJugadorConTurno)
            {
                case 0:
                    pictureTurn0.BackColor = Color.Red;
                    break;
                case 1:
                    pictureTurn1.BackColor = Color.Red;
                    break;
                case 2:
                    pictureTurn2.BackColor = Color.Red;
                    break;
                case 3:
                    pictureTurn3.BackColor = Color.Red;
                    break;
            }
        }
        public void ActualizarMarcador(int [] puntos) {
            if (Convert.ToInt32(marcadores[0].Text) == puntos[0])MessageBox.Show("Mano Ganada por el Equipo 2.");
            if (Convert.ToInt32(marcadores[1].Text) == puntos[1]) MessageBox.Show("Mano Ganada por el Equipo 1.");

            String campo;
            for (int i = 0; i < 2; i++){
                if (puntos[i] / 100 != 0){
                    campo = puntos[i].ToString();
                }else if (puntos[i] / 10 != 0){
                    campo = "0" + puntos[i].ToString();
                }else{
                    campo = "00" + puntos[i].ToString();
                }
                marcadores[i].Text = campo;
            }
        }

        public void LimpiarCliente() {
            OcultaBotonPasar();
            ResetPieceBox();
            this.txtLblPiecesCountJugador0.Text="0";
            this.txtLblPiecesCountJugador1.Text="0";
            this.txtLblPiecesCountJugador2.Text="0";
            this.txtLblPiecesCountJugador3.Text = "0";
            pictureTurn0.BackColor = Color.Transparent;
            pictureTurn1.BackColor = Color.Transparent;
            pictureTurn2.BackColor = Color.Transparent;
            pictureTurn3.BackColor = Color.Transparent;
            rightIndex = 27;
            leftIndex = 27;
            tengoTurno = false;

        }

        private void ResetPieceBox()
        {
            foreach (PieceBox Box in pieceBoxes)
            {
                Box.GetBox().Image = null;
                Box.SetValue(-1);
            }
            foreach (PieceBox Box in pieceBoxesPlayer1)
            {
                Box.GetBox().Image = null;
                Box.GetBox().Visible = true;
                Box.SetValue(-1);
            }
            foreach (PieceBox Box in pieceBoxesPlayer2)
            {
                Box.GetBox().Image = null;
                Box.GetBox().Visible = true;
                Box.SetValue(-1);
            }
            foreach (PieceBox Box in pieceBoxesPlayer3)
            {
                Box.GetBox().Image = null;
                Box.GetBox().Visible = true;
                Box.SetValue(-1);
            }
            foreach (PieceBox Box in pieceBoxesPlayer4)
            {
                Box.GetBox().Image = null;
                Box.GetBox().Visible = true;
                Box.SetValue(-1);
            }
        }

        public void EndGame(int [] marcadores) {
            String Mensaje = null;
            if (marcadores[0] > marcadores[1]){
                Mensaje = "Ha ganado el Equipo 1 con " + marcadores[0] + " puntos. \n El Equipo 2 ha perdido con " + marcadores[1] + " puntos";
            }else {
                Mensaje = "Ha ganado el Equipo 2 con " + marcadores[1] + " puntos. \n El Equipo 1 ha perdido con " + marcadores[0] + " puntos";
            }
            DialogResult dlgresult = MessageBox.Show(Mensaje,
                                "EndGame",
                                MessageBoxButtons.OK,
                                MessageBoxIcon.Information);
            if (dlgresult == DialogResult.OK) {
                this.Close();
            }
        }

        public void ColocaBot(int[] valors, int posicion) {
            PieceBox[] Aux = null;
            Aux = SelectPieceBox(posicion, Aux);
            BotHaceClickEnMano(valors, Aux);
            BotPoneLaFichaEnLaMesa(valors);
        }

        private void BotPoneLaFichaEnLaMesa(int[] valors)
        {
            foreach (PieceBox piece in pieceBoxes)
            {
                if (piece.GetValue() == valors[1])
                {
                    DialogResult dlgresult = MessageBox.Show("El jugador " + this.txtBoxPlayerName.Text + " va a colocar la ficha (" + (Ficha_Click.GetValue() / 10) + "|" + (Ficha_Click.GetValue() % 10) + ") al lado de la ficha (" + (valors[1] / 10) + "|" + (valors[1] % 10) + ").",
                               "Acción de Bot",
                               MessageBoxButtons.OK,
                               MessageBoxIcon.Information);
                    ClickEnFichaMesa(piece.GetBox(), new EventArgs());
                    break;
                }
            }
        }

        private void BotHaceClickEnMano(int[] valors, PieceBox[] Aux)
        {
            foreach (PieceBox piece in Aux)
            {
                if (piece.GetValue() == valors[0])
                {
                    ClickEnFichaJugador(piece.GetBox(), new EventArgs());
                    break;
                }
            }
        }

        private PieceBox[] SelectPieceBox(int posicion, PieceBox[] Aux)
        {
            switch (posicion)
            {
                case 0:
                    Aux = this.pieceBoxesPlayer1;
                    break;
                case 1:
                    Aux = this.pieceBoxesPlayer2;
                    break;
                case 2:
                    Aux = this.pieceBoxesPlayer3;
                    break;
                case 3:
                    Aux = this.pieceBoxesPlayer4;
                    break;
            }
            return Aux;
        }

    }
}
