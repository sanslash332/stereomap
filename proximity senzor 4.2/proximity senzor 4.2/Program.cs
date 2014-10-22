using System;
using System.Collections;
using System.Threading;
using Microsoft.SPOT;
using Microsoft.SPOT.Presentation;
using Microsoft.SPOT.Presentation.Controls;
using Microsoft.SPOT.Presentation.Media;
using Microsoft.SPOT.Presentation.Shapes;
using Microsoft.SPOT.Touch;

using Gadgeteer.Networking;
using GT = Gadgeteer;
using GTM = Gadgeteer.Modules;
using Gadgeteer.Modules.GHIElectronics;
using Gadgeteer.Modules.Seeed;

namespace proximity_senzor_4._2
{
    public partial class Program
    {
        private GT.Timer mytimer;
        private Bluetooth.Client client;
        private bool conected;
        private bool continuing = true;
        private int frames = 10;
        



        // This method is run when the mainboard is powered up or reset.   
        void ProgramStarted()
        {
            /*******************************************************************************************
            Modules added in the Program.gadgeteer designer view are used by typing 
            their name followed by a period, e.g.  button.  or  camera.
            
            Many modules generate useful events. Type +=<tab><tab> to add a handler to an event, e.g.:
                button.ButtonPressed +=<tab><tab>
            
            If you want to do something periodically, use a GT.Timer and handle its Tick event, e.g.:
                GT.Timer timer = new GT.Timer(1000); // every second (1000ms)
                timer.Tick +=<tab><tab>
                timer.Start();
            *******************************************************************************************/


            // Use Debug.Print to show messages in Visual Studio's "Output" window during debugging.
            Debug.Print("Program Started");
            Debug.Print("inicializando partes");
            mytimer = new GT.Timer(500);
            mytimer.Behavior = GT.Timer.BehaviorType.RunContinuously;
            compass.StartContinuousMeasurements();
            compass.MeasurementComplete += compass_MeasurementComplete;
            mytimer.Tick += timer_Tick;
            bt.SetDeviceName("StereomapJockey");
            bt.SetPinCode("0000");

            client = bt.ClientMode;
            
            bt.DataReceived += bt_DataReceived;
            bt.BluetoothStateChanged += bt_BluetoothStateChanged;
            bt.DeviceInquired += bt_DeviceInquired;
            bt.PinRequested += bt_PinRequested;
            Tunes.Melody musc = new Tunes.Melody();
            musc.Add(Tunes.Tone.C4, 200);
            musc.Add(Tunes.Tone.E4, 100);
            musc.Add(Tunes.Tone.F4, 100);
            musc.Add(Tunes.Tone.G4, 400);
            musc.Add(Tunes.Tone.E4, 200);
            musc.Add(Tunes.Tone.G4, 600);
            

            tunes.Play(musc);
            restButton.ButtonPressed += restButton_ButtonPressed;
            restButton.TurnLEDOn();
            pairModeButton.ButtonPressed += pairModeButton_ButtonPressed;
            System.Threading.Thread.Sleep(2000);
            mytimer.Start();

            
        }

        void compass_MeasurementComplete(Compass sender, Compass.SensorData sensorData)
        {
            int frec = (int)sensorData.Angle * 10+30;
            tunes.Play(frec);
        }

        void pairModeButton_ButtonPressed(Button sender, Button.ButtonState state)
        {
            client.EnterPairingMode();
            pairModeButton.TurnLEDOn();
            Tunes.Melody coin = new Tunes.Melody();
            coin.Add(Tunes.Tone.C4,200);
            coin.Add(Tunes.Tone.C5, 400);
            tunes.Play(coin);

        }

        void restButton_ButtonPressed(Button sender, Button.ButtonState state)
        {
            continuing = true;
            frames += 10;
            tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(3000), 500));
            tunes.Play();

            sender.TurnLEDOn();

        }

        void bt_PinRequested(Bluetooth sender)
        {
            Debug.Print("ping solicitado");
        }

        void bt_DeviceInquired(Bluetooth sender, string macAddress, string name)
        {
            Debug.Print("Dispositivo encontrado");
            
        }

        void bt_BluetoothStateChanged(Bluetooth sender, Bluetooth.BluetoothState btState)
        {
            Debug.Print("cambio de estado " + btState.ToString());

            switch (btState)
            {
                case Bluetooth.BluetoothState.Connecting:
                    Debug.Print("modo: conectando dispositivo");
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(2000),150));
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(3000), 150));
                    tunes.Play();
                    
                    break;
                case Bluetooth.BluetoothState.Connected:
                    Debug.Print("modo: bt conectado!");
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(3000),150));
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(3000), 150));
                    tunes.Play();
                    pairModeButton.TurnLEDOff();
                    break;
                case Bluetooth.BluetoothState.Disconnected:
                    Debug.Print("modo: bluetooth desconectado");
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(3000),150));
                    tunes.AddNote(new Tunes.MusicNote(new Tunes.Tone(2000), 150));
                    tunes.Play();
                    
                    break;
                case Bluetooth.BluetoothState.Ready:
                    Debug.Print("modo: bt preparado");
                    
                    
                    break;
                default:
                    Debug.Print("accion defecto");

                    break;

            }

            
         

        }

        void bt_DataReceived(Bluetooth sender, string data)
        {
            Debug.Print("recibiendo datos vía bt");
            Debug.Print("dato: " + data);
        }

        void timer_Tick(GT.Timer timer)
        {
            if (!continuing)
            {
                
                return;
            }

            //Debug.Print("Distancia a un objeto, con error "  + dist.SENSOR_ERROR);
            //Debug.Print("distancia: " + dist.GetDistanceInCentimeters().ToString());
            frames=frames-1;
            int currentDist = dist.GetDistanceInCentimeters();
            client.Send(currentDist.ToString());
            if (currentDist < 0)
            {
                tunes.Stop();
            }
                          else  if (currentDist <= 5)
                            {
                tunes.Play(new Tunes.Tone(5000));
            }
            else if (currentDist <= 10)
            {
                tunes.Play(new Tunes.Tone(3000));
            }
            else if (currentDist <= 20)
            {
                tunes.Play(new Tunes.Tone(2000));
            }
            else if (currentDist <= 45)
            {
                tunes.Play(new Tunes.Tone(1000));
            }
            if (frames == 0)
            {
                continuing = false;
                tunes.Stop();
                restButton.TurnLEDOff();
            }

        }
    }
}
