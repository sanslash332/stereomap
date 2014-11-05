//------------------------------------------------------------------------------
// <auto-generated>
//     Este código fue generado por una herramienta.
//     Versión de runtime:4.0.30319.18444
//
//     Los cambios en este archivo podrían causar un comportamiento incorrecto y se perderán si
//     se vuelve a generar el código.
// </auto-generated>
//------------------------------------------------------------------------------

namespace proximity_senzor_4._2 {
    using Gadgeteer;
    using GTM = Gadgeteer.Modules;
    
    
    public partial class Program : Gadgeteer.Program {
        
        /// <summary>The Distance US3 module using socket 7 of the mainboard.</summary>
        private Gadgeteer.Modules.GHIElectronics.DistanceUS3 dist;
        
        /// <summary>The Tunes module using socket 6 of the mainboard.</summary>
        private Gadgeteer.Modules.GHIElectronics.Tunes tunes;
        
        /// <summary>The Bluetooth module using socket 2 of the mainboard.</summary>
        private Gadgeteer.Modules.GHIElectronics.Bluetooth bt;
        
        /// <summary>The Button module using socket 5 of the mainboard.</summary>
        private Gadgeteer.Modules.GHIElectronics.Button pairModeButton;
        
        /// <summary>The Button module using socket 4 of the mainboard.</summary>
        private Gadgeteer.Modules.GHIElectronics.Button restButton;
        
        /// <summary>The Compass module using socket 1 of the mainboard.</summary>
        private Gadgeteer.Modules.Seeed.Compass compass;
        
        /// <summary>This property provides access to the Mainboard API. This is normally not necessary for an end user program.</summary>
        protected new static GHIElectronics.Gadgeteer.FEZCerberus Mainboard {
            get {
                return ((GHIElectronics.Gadgeteer.FEZCerberus)(Gadgeteer.Program.Mainboard));
            }
            set {
                Gadgeteer.Program.Mainboard = value;
            }
        }
        
        /// <summary>This method runs automatically when the device is powered, and calls ProgramStarted.</summary>
        public static void Main() {
            // Important to initialize the Mainboard first
            Program.Mainboard = new GHIElectronics.Gadgeteer.FEZCerberus();
            Program p = new Program();
            p.InitializeModules();
            p.ProgramStarted();
            // Starts Dispatcher
            p.Run();
        }
        
        private void InitializeModules() {
            this.dist = new GTM.GHIElectronics.DistanceUS3(7);
            this.tunes = new GTM.GHIElectronics.Tunes(6);
            this.bt = new GTM.GHIElectronics.Bluetooth(2);
            this.pairModeButton = new GTM.GHIElectronics.Button(5);
            this.restButton = new GTM.GHIElectronics.Button(4);
            this.compass = new GTM.Seeed.Compass(1);
        }
    }
}
