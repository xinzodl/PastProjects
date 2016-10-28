/*
 * COPYRIGHT 2015 Ginés García Avilés - Luis Antonio Pérez Mulero
 *      TADS_DOMINO_2015
 *      TADS
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

        private Dispatcher dispatcher;

        private List<GroupBox> PlayerGroupBoxes;

        private int myPosition = -1;

        public Domino()
        {
            InitializeComponent();
        }
        

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

        /* -- CALLBACK METHODS -- */

        public void PaintPlayerJoined (string PlayerName, int PlayerPosition)
        {
            PlayerGroupBoxes.ElementAt(PlayerPosition).Text = PlayerName;
            if (PlayerName == txtBoxPlayerName.Text)
            {
                myPosition = PlayerPosition;
                this.btnSentarse.Enabled = false;
            }
        }

        public void PlayerNotSat(string ErrorMessage)
        {
            txtBoxPlayerName.Enabled = true;
            MessageBox.Show(ErrorMessage);
        }

        

        public void paintPieceInPlayersHand(int PieceValue, int PiecePosition, int PlayerPosition)
        {
            //Se muestra al jugador una pieza que se encuentra en su poder
            string pictureName;
            Control[] pictureBoxPieces = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition, false);
            PictureBox pictureBoxPiece = (PictureBox)pictureBoxPieces[0];

            pictureName = selectImageFileNameToPaint(PieceValue, PlayerPosition);

            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            pictureBoxPiece.Image = this.imageListPieces.Images[picturePostion];
        }

        private string selectImageFileNameToPaint(int PieceValue, int PlayerPosition)
        {
            string pictureName;
            if (myPosition == PlayerPosition)
            {
                // se repiten en otro método. Sacar a una funcion.
                pictureName = IdentifyPiecePictureName(PieceValue);
            }
            else
            {
                pictureName = "HiddenPiece.png";
            }
            return pictureName;
        }

        private string IdentifyPiecePictureName(int PieceValue)
        {
            string pictureName;
            int FirstSquareValue = PieceValue / 10;
            int SecondSquareValue = PieceValue % 10;
            pictureName = FirstSquareValue + "-" + SecondSquareValue + ".png";
            return pictureName;
        }

        public void incrementPlayerAvailablePieces(int PlayerPosition)
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

        public void PaintPieceInTable(int StartPieceInt)
        {
            string pictureName;
            pictureName = IdentifyPiecePictureName(StartPieceInt);
            int picturePostion = this.imageListPieces.Images.IndexOfKey(pictureName);
            this.pictureBoxPiece0.Image = this.imageListPieces.Images[picturePostion];
        }

        public void HidePieceInHand(int PiecePosition, int PlayerPosition)
        {
            Control[] pictureBoxPieces = PlayerGroupBoxes.ElementAt(PlayerPosition).Controls.Find("pictureBoxPlayer" + PlayerPosition + "Piece" + PiecePosition, false);
            PictureBox pictureBoxPiece = (PictureBox)pictureBoxPieces[0];
            pictureBoxPiece.Image = null;
        }

        private void Domino_Load(object sender, EventArgs e)
        {

            PlayerGroupBoxes = new List<GroupBox>();
            PlayerGroupBoxes.Add(grpBoxJugador1);
            PlayerGroupBoxes.Add(grpBoxJugador2);
            PlayerGroupBoxes.Add(grpBoxJugador3);
            PlayerGroupBoxes.Add(grpBoxJugador4);
            Receiver.setInstance(this);
            dispatcher = new Dispatcher(Receiver.getInstance());
            dispatcher.OpenConnection();

        }

        private void Domino_FormClosed(object sender, FormClosedEventArgs e)
        {
            dispatcher.CloseConnection();
        }     
       
    }
}
