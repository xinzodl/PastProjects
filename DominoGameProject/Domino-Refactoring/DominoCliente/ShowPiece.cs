using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.Windows.Forms;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;

namespace DominoCliente
{
    class ShowPiece : PictureBox 
    {

      
        //si es 0 a la iqda, si es 1 a la derecha,2 arriba y 3 abajo
        private int vUbicacion;
        //el valor de la ficha
        private int vFicha;
        //valor de arriba, valor de abajo
        private int vup;
        private int vdown;
        //valor de izqda, valor de derecha
        private int vLeft;
        private int vRight;

        private bool Doble;
        private bool Vertical;
        
        private Bitmap imagen;
        private Bitmap auxImage;

        private bool move = false;
        private bool pMove = false;
        private long posX = 0;
        private long posY = 0;

        private int rotaciones=0;

        public int getValue() { 
            return this.vFicha;
        }
        /// <summary>
        /// constructor Posicion ficha en Group Box
        /// </summary>
        public ShowPiece(int pieceValue, int playerPosition, int piecePosition, Bitmap img)
        {
            //genera una ficha y la mete en el groupbox del jugador
            try
            {            
                imagen = img;
                this.SizeMode = PictureBoxSizeMode.AutoSize;
                this.Image = imagen;
                auxImage = imagen;
                vFicha = pieceValue;
                vup = pieceValue / 10;
                vdown = pieceValue % 10;


                Vertical = true;
                if (vup == vdown)
                {
                    Doble = true;
                    vLeft = vup;
                    vRight = vup;
                }
                else
                {
                    Doble = false;
                    vLeft = -1;
                    vRight = -1;
                }
                //se asigna la posicion de la fcha en el groupBox del jugador actual
                PosiciónFichaJugador(playerPosition, piecePosition);

            }
            catch (System.IO.FileNotFoundException)
            {
                Console.WriteLine("Error, No se creo el objeto");   
            }

        }


        /// <summary>
        /// constructor Posición Ficha Inicial Doble
        /// </summary>
        public ShowPiece(int pieceValue, Panel panel, Bitmap img)
        {

            try
            {
                imagen = img;
                this.SizeMode = PictureBoxSizeMode.AutoSize;
                this.Image = imagen;
                auxImage = imagen;
                vFicha = pieceValue;
                vup = pieceValue / 10;
                vdown = pieceValue % 10;
                Vertical = true;
                if (vup == vdown)
                {
                    Doble = true;
                    vLeft = vup;
                    vRight = vup;
                }
                else
                {
                    Doble = false;
                    vLeft = -1;
                    vRight = -1;
                }

                PosiciónCentroPanel(panel);

            }
            catch (System.IO.FileNotFoundException)
            {
                Console.WriteLine("Error, No se creo el objeto");
            }

        }

        /// <summary>
        /// constructor colocar ficha en el tablero.
        /// </summary>
        /// //numero de rotaciones que ha hecho la ficha, para poder mandarselo a otro jugador para ponerlo en el panel
        public ShowPiece(int pieceValue, Bitmap img, int rotaciones)
        {

            try
            {
                imagen = img;
                this.SizeMode = PictureBoxSizeMode.AutoSize;
                this.Image = imagen;
                auxImage = imagen;
                vFicha = pieceValue;
                vup = pieceValue / 10;
                vdown = pieceValue % 10;
                Vertical = true;
                if (vup == vdown)
                {
                    Doble = true;
                    vLeft = vup;
                    vRight = vup;
                }
                else
                {
                    Doble = false;
                    vLeft = -1;
                    vRight = -1;
                }
                
                establecerRotaciones(rotaciones);

            }
            catch (System.IO.FileNotFoundException)
            {
                Console.WriteLine("Error, No se creo el objeto");
            }

        }


        /// <summary>
        /// el valor de la ficha
        /// </summary>
        public int  valorFicha
        {
            get
            {
                return vFicha;
            }
            set
            {
                vFicha=value;
            }

        }

        /// <summary>
        /// si la ficha tiene valor doble
        /// </summary>
        public bool  isDoble
        {
            get
            {
                return Doble ;
            }

            set
            {
                Doble = value;
            }
        }

        /// <summary>
        /// si la ficha esta en posición vertical
        /// </summary>
        public bool isVertical
        {
            get
            {
                return Vertical;
            }

            set
            {
                Vertical = value;
            }
        }

        /// <summary>
        /// valor superior de la ficha.
        /// </summary>
        public int up
        {
            get
            {
                return vup;
            }

            set
            {
                vup = value;
            }
        }

        /// <summary>
        /// valor inferior de la ficha.
        /// </summary>
        public int down
        {
            get
            {
                return vdown;
            }

            set
            {
                vdown = value;
            }
        }

        /// <summary>
        /// valor Izquierda de la ficha.
        /// </summary>
        public int Izquierda
        {
            get
            {
                return vLeft;
            }

            set
            {
                vLeft = value;
            }
        }

        /// <summary>
        /// valor Derecha de la ficha.
        /// </summary>
        public int Derecha
        {
            get
            {
                return vRight;
            }

            set
            {
                vRight = value;
            }
        }

        /// <summary>
        /// retorna la ubicación de la ficha, 
        /// 0=izquierda, 1=derecha, 2= arriba, 3=abajo
        /// </summary>

        public int Ubicación
        {
            get
            {
                return vUbicacion;
            }

            set
            {
                vUbicacion = value;
            }
        }


        /// <summary>
        /// posición de la ficha.
        /// </summary>
        public void Position(int x, int y)
        {          
            this.Location = new Point(x, y);
            
        }

       
        /// <summary>
        /// giramos la ficha 90 grados.
        /// </summary>
        public void girarFicha()
        {

            if (imagen != null)
            {
                imagen.RotateFlip(RotateFlipType.Rotate90FlipNone);
                this.Image = imagen;

                if (rotaciones==0)
                {
                    primeraRotacion();
                    rotaciones++;
                }
                else if (rotaciones==1)
                {
                    segundaRotacion();
                    rotaciones++;
                }
                else if (rotaciones == 2)
                {
                    primeraRotacion();
                    rotaciones++;
                }
                else if (rotaciones == 3)
                {
                    segundaRotacion();
                    rotaciones = 0;
                }

            }

        }

        private void primeraRotacion()
        {
            int aux1 = vRight;
            int aux2 = vLeft;

            vRight = vup;
            vLeft = vdown;
            vup = aux1;
            vdown = aux2;
            Vertical = false;
            
        }

        private void segundaRotacion()
        {
            int aux1 = vdown;
            int aux2 = vup;

            vdown = vRight;
            vup = vLeft;

            vRight = aux2;
            vLeft = aux1;
            Vertical = true;
        }

        public int GetRotaciones()
        {
            return rotaciones;
        }

        private void establecerRotaciones(int rotate)
        {


            for (int x = 0; x < rotate; x++)
            {

                this.girarFicha();

            }

        }

      

        private void PosiciónFichaJugador(int playerPosition,int piecePosition)
        {
            int locationX;
            int locationY;

            locationY = 34;

            if ((playerPosition == 1)||(playerPosition == 3))
            {

                if (piecePosition > 3)
                {
                    piecePosition = piecePosition - 4;
                    locationY = 120;
                }
            
            }

            locationX = (11 + (piecePosition * 36));
            this.Location = new Point(locationX, locationY);


        }

        private void PosiciónCentroPanel(Panel panel)
        {

            this.Left = (panel.Width - this.Width) / 2;
            this.Top = (panel.Height - this.Height) /2;
        }

        /// <summary>
        /// restablece la imagen original de la ficha
        /// </summary>
        public void RestablecerImagenBitmap()
        {
            this.Image = imagen;
        }


        /// <summary>
        /// modifica la escala de grises de la imagen ficha
        /// </summary>
        public void ModificarImagenBitmap()
        {
            Bitmap target = new Bitmap(auxImage.Width, auxImage.Height, auxImage.PixelFormat);
            BitmapData targetData, sourceData;

            byte[] sourceBytes = GetImageBytes(auxImage, ImageLockMode.ReadOnly, out sourceData);
            byte[] targetBytes = GetImageBytes(target, ImageLockMode.ReadWrite, out targetData);

            //recorrer los pixeles
            for (int i = 0; i < sourceBytes.Length; i += 3)
            {
                //ignorar el padding, es decir solo procesar los bytes necesarios
                if ((i + 3) % (auxImage.Width * 3) > 0)
                {
                    //Hallar tono gris
                    //byte y = (byte)(sourceBytes[i + 2] * 0.3f
                    //             + sourceBytes[i + 1] * 0.59f
                    //             + sourceBytes[i] * 0.11f);


                    //Hallar tono gris
                    byte y = (byte)(sourceBytes[i + 2] * 0.3f
                                 + sourceBytes[i + 1] * 0.59f
                                 + sourceBytes[i] * 0.11f);

                    //Asignar tono gris a cada byte del pixel
                    targetBytes[i + 2] = targetBytes[i + 1] = targetBytes[i] = y;
                }
            }

            Marshal.Copy(targetBytes, 0, targetData.Scan0, targetBytes.Length);

            auxImage.UnlockBits(sourceData);
            target.UnlockBits(targetData);

            this.Image = target;
        }


        /// <summary>
        /// obtiene los bytes de la imagen
        /// </summary>
        private byte[] GetImageBytes(Bitmap image, ImageLockMode lockMode, out BitmapData bmpData)
        {
            bmpData = image.LockBits(new Rectangle(0, 0, image.Width, image.Height),
                                     lockMode, image.PixelFormat);

            byte[] imageBytes = new byte[bmpData.Stride * image.Height];
            Marshal.Copy(bmpData.Scan0, imageBytes, 0, imageBytes.Length);

            return imageBytes;
        }


       

       


    }
}
