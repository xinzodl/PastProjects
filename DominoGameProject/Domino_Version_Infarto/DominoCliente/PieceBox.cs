/*   Carlos Contreras Sanz		    100303562
 *   Álvaro Gómez Ramos		        100307009
 *   Luis Antonio Pérez Mulero	    100303519
 *   Jessica Zamora Castillo	    100077349
 *   Ginés García Avilés		    100333480
 * 
 * 
 *  Clase:  PieceBox
 *  Descripción de la clase:
 *  Clase de datos de ficha para poder pintar correctamente en mesa
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DominoCliente
{
    class PieceBox{
        private int value;
        private PictureBox box;
        private PieceBox next;
        private PieceBox prev;

        public PieceBox(int value, PictureBox box) {
            this.value = value;
            this.box = box;
        }
        public int GetValue() {
            return this.value;
        }
        public void SetValue(int value){
            this.value = value;
        }
        public PictureBox GetBox() {
            return this.box;
        }
        public void SetPrevBox(PieceBox piece)
        {
            this.prev = piece;
        }
        public void SetNextBox(PieceBox piece)
        {
            this.next = piece;
        }
        public PieceBox GetPrevBox()
        {
            return this.prev;
        }
        public PieceBox GetNextBox()
        {
            return this.next;
        }
    }
}
