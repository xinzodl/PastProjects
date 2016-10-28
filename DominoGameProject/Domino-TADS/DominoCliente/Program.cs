/*
 * COPYRIGHT 2015 Ginés García Avilés - Luis Antonio Pérez Mulero
 *      TADS_DOMINO_2015
 *      TADS
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DominoCliente
{

    static class Program
    {
        /// <summary>
        /// Punto de entrada principal para la aplicación.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Domino());
        }
    }
}
