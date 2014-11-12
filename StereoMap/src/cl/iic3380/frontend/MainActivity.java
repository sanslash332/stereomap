package cl.iic3380.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.pielot.openal.SoundEnv;

import cl.iic3380.backend.*;
import cl.iic3380.frontend.R;
import cl.iic3380.utils.Utils;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.GestureDetectorCompat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnInitListener, OnGestureListener, OnDoubleTapListener {

	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private String bestProvider;
	private PlacesManager placesManager;
	private Location userLocation;
	private TextView tv;
	private SoundEnv env;
	private TextToSpeech tts;
	private int radius= 500;
	private static final int MY_DATA_CHECK_CODE=1234;
<<<<<<< HEAD
	private Bluetooth bt = new Bluetooth();

	/*
	 * Codigo de respaldo librerï¿½a OPENAL
	 */


=======
	
	//Deteccion de Gestos
    private GestureDetectorCompat mDetector; 
    private SimpleTwoFingerDoubleTapDetector multiTouchListener = new SimpleTwoFingerDoubleTapDetector() {
        @Override
        public void onTwoFingerDoubleTap() {
            //TODO Generar el Mapa
        }
    };
    
>>>>>>> origin/develop
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		this.env = SoundEnv.getInstance(this);
		
		//Primero sacamos una localizacion que siempre devuelva algo
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		userLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		//Luego cambiamos al bestProvider
		bestProvider = locationManager.getBestProvider(Utils.GetFineCriteria(),true);		
		//Definir el Places Manager
		placesManager = new PlacesManager(this,env);
		placesManager.setUserLocation(userLocation);
		
		
//		while(true)
//		{
//			if(placesManager.getUserLocation()!=null)
//				break;
//		}
		
		//No se si esto va
		tv = (TextView)findViewById(R.id.textView1);
		tv.setText(String.valueOf(radius));
		//Definimos el Location Listener
		locationListener = new MyLocationListener(placesManager,tv,String.valueOf(radius));
	
		//Definimos la posición y todos los updates necesarios
		placesManager.setUserLocation(userLocation);
		locationManager.requestSingleUpdate(bestProvider, locationListener, this.getMainLooper());


		// Vemos si el TTs esta instalado
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
		
		//Bluetooth
		//Pedir encenderlo
		if (!bt.get_adapter().isEnabled()) {
		    Intent enableBtIntent = new Intent(bt.get_adapter().ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 1);
		}
		
		//Mostrar paired
		List <String> bt_pairs = bt.get_paired();

		Toast toast = Toast.makeText(getApplicationContext(), bt_pairs.get(0), Toast.LENGTH_LONG);
		toast.show();

		//Detector de gestos
		 mDetector = new GestureDetectorCompat(this,this);
		
	}




	@Override
	public void onInit(int status) {
		//		tts.speak("Hello folks, welcome to my little demo on Text To Speech.",
		//				TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		//				null);

		//Luego de verificar el TTS hacemos el Parse de Places
		
		try 
		{
			placesManager.ParsePlaces(String.valueOf(radius), userLocation);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		placesManager.start();
		
		/*
		 * PRUEBAS
		 */
		
		Button b1 = (Button)findViewById(R.id.button1);
		
//		b1.setOnClickListener(new View.OnClickListener() {
//		    @Override
//		    public void onClick(View v) {
//		    	TextView t2 = (TextView)findViewById(R.id.textView2);
//		    	t2.setText("");
//		        for(Place p : placesManager.searchedPlaces)
//		        {
//		        	if(p.IsPlayable())
//		        	{
//		        		t2.append(p.toString());
//		        	}
//		        }
//		    }
//		});
//		




	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//Instalar dependencias
		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
				placesManager.setTTS(tts);
			}
			else
			{
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(
						TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	/**
	 * Be kind, once you've finished with the TTS engine, shut it down so other
	 * applications can use it without us interfering with it :)
	 */
	@Override
	public void onDestroy()
	{
		// Don't forget to shutdown!
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	/**
	 * Controladores de Gestures
	 */
	
	@Override 
    public boolean onTouchEvent(MotionEvent event){ 
		if(multiTouchListener.onTouchEvent(event))
            return true;
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(velocityX > 0 && Math.abs(velocityX) > Math.abs(velocityY)){
			tv.setText("Derecha"); //Aumentar radio
			placesManager.PlayNext();
		}
		if(velocityX < 0 && Math.abs(velocityX) > Math.abs(velocityY)){
			tv.setText("Izquierda"); //Disminuir radio
			placesManager.PlayPrevious();
		}
		if(velocityY > 0 && Math.abs(velocityX) <= Math.abs(velocityY)){
			radius -= 50;
			if(radius < 0)
				radius = 0;
			tv.setText(String.valueOf(radius));
		}
		if(velocityY < 0 && Math.abs(velocityX) <= Math.abs(velocityY)){
			tv.setText("Arriba"); //Comenzar reproducción de audio (ParsePlaces si no hay ninguno)
			radius += 50;
			if (radius > 1000)
				radius = 1000;
			tv.setText(String.valueOf(radius));
		}
		return false;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		// TODO Auto-generated method stub
		tv.setText("double tap");
		try 
		{
			placesManager.ParsePlaces(String.valueOf(radius), userLocation);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//placesManager.start(); //QUE HACEMOS PARA PODER INICIAR DE NUEVO EL THREAD?
		return false;
	}




	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
