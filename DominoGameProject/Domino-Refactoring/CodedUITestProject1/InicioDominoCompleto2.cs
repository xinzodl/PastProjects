using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Windows.Input;
using System.Windows.Forms;
using System.Drawing;
using Microsoft.VisualStudio.TestTools.UITesting;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Microsoft.VisualStudio.TestTools.UITest.Extension;
using Keyboard = Microsoft.VisualStudio.TestTools.UITesting.Keyboard;


namespace CodedUITestProject1
{
    /// <summary>
    /// Summary description for CodedUITest1
    /// </summary>
    [CodedUITest]
    public class InicioDominoCompleto2
    {
        public InicioDominoCompleto2()
        {
        }

        [TestMethod]
        public void CodedUITestMethod1()
        {
            // To generate code for this test, select "Generate Code for Coded UI Test" from the shortcut menu and select one of the menu items.
            // For more information on generated code, see http://go.microsoft.com/fwlink/?LinkId=179463
            this.UIMap.PruebaMesaDomino1();
            this.UIMap.PruebaSentarJugador1();
            this.UIMap.PruebaAssertJugador1();
            this.UIMap.PruebaMesaDomino2();
            this.UIMap.PruebaSentarJugador2();
            this.UIMap.PruebaAssertJugador2();
            this.UIMap.PruebaAssertJugador2_1();
            this.UIMap.PruebaMesaDomino3();
            this.UIMap.PruebaSentarJugador3();
            this.UIMap.PruebaAssertJugador3();
            this.UIMap.PruebaAssertJugador3_2();
            this.UIMap.PruebaAssertJugador3_1();
            this.UIMap.PruebaMesaDomino4();
            this.UIMap.PruebaAssertJugador4();
            this.UIMap.PruebaAssertJugador4_3();
            this.UIMap.PruebaAssertJugador4_2();
            this.UIMap.PruebaAssertJugador4_1();
            this.UIMap.PruebaAsserNroFichar4();
            this.UIMap.PruebaAsserNroFichar4_3();
            this.UIMap.PruebaAsserNroFichar4_2();
            this.UIMap.PruebaAsserNroFichar4_1();
            this.UIMap.PruebaCerrarMesa4();
            this.UIMap.PruebaCerrarMesa3();
            this.UIMap.PruebaCerrarMesa2();
            this.UIMap.PruebaCerrarMesa1();
        }

        #region Additional test attributes

        // You can use the following additional attributes as you write your tests:

        ////Use TestInitialize to run code before running each test 
        //[TestInitialize()]
        //public void MyTestInitialize()
        //{        
        //    // To generate code for this test, select "Generate Code for Coded UI Test" from the shortcut menu and select one of the menu items.
        //    // For more information on generated code, see http://go.microsoft.com/fwlink/?LinkId=179463
        //}

        ////Use TestCleanup to run code after each test has run
        //[TestCleanup()]
        //public void MyTestCleanup()
        //{        
        //    // To generate code for this test, select "Generate Code for Coded UI Test" from the shortcut menu and select one of the menu items.
        //    // For more information on generated code, see http://go.microsoft.com/fwlink/?LinkId=179463
        //}

        #endregion

        /// <summary>
        ///Gets or sets the test context which provides
        ///information about and functionality for the current test run.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }
        private TestContext testContextInstance;

        public UIMap UIMap
        {
            get
            {
                if ((this.map == null))
                {
                    this.map = new UIMap();
                }

                return this.map;
            }
        }

        private UIMap map;
    }
}
