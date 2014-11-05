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
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnInitListener, OnGestureListener {

	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private String bestProvider;
	private PlacesManager placesManager;
	private List<Place> places;
	private Location userLocation;
	private TextView tv;
	private SoundEnv env;
	private GestureDetector gestureDetector;
	private TextToSpeech tts;
	private static final String RADIUS="500";
	private static final int MY_DATA_CHECK_CODE=1234;
	private Bluetooth bt = new Bluetooth();

	/*
	 * Codigo de respaldo librerï¿½a OPENAL
	 */


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
		//Detector de gestos
		gestureDetector = new GestureDetector(this, this);
		
		//Definimos los places y el placesManager
		places = new ArrayList<Place>();
		placesManager = new PlacesManager(places,this);
		placesManager.setUserLocation(userLocation);
		
		
//		while(true)
//		{
//			if(placesManager.getUserLocation()!=null)
//				break;
//		}
		
		//No se si esto va
		tv = (TextView)findViewById(R.id.textView1);
		tv.setText(RADIUS);
		//Definimos el Location Listener
		locationListener = new MyLocationListener(placesManager,tv,RADIUS);
	
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


	}


	@Override
	public boolean onTouchEvent(MotionEvent event){
		this.gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	@Override
	public void onInit(int status) {
		//		tts.speak("Hello folks, welcome to my little demo on Text To Speech.",
		//				TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		//				null);

		//Luego de verificar el TTS hacemos el Parse de Places
		
		try 
		{
			places = placesManager.ParsePlaces(RADIUS, userLocation);
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
		
		b1.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	TextView t2 = (TextView)findViewById(R.id.textView2);
		    	t2.setText("");
		        for(Place p : places)
		        {
		        	if(p.IsPlayable())
		        	{
		        		t2.append(p.toString());
		        	}
		        }
		    }
		});
		
//		for(Place p : searchedPlaces)
//		{
//			p.addBufferAndSource(env);
//			p.calculateSoundPosition(userLocation);
//			p.start();
//			Thread.sleep(Utils.GetDuration(new File(p.getAudioFilePath() + p.getPlaceName().split(" ")[0] + ".wav")));
//			p.join();
//		}



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


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(velocityX > 0 && Math.abs(velocityX) >= Math.abs(velocityY)) //Movimiento hacia la derecha
		{
			tv.setText("derecha");
			return true;
		}
		else if (velocityX < 0 && Math.abs(velocityX) >= Math.abs(velocityY)) //Movimiento hacia la izquierda
		{
			tv.setText("izquierda");
			return true;
		}
		else if (velocityY > 0 && Math.abs(velocityX) < Math.abs(velocityY)) //Movimiento hacia arriba
		{
			tv.setText("abajo");
			return true;
		}
		else if (velocityY < 0 && Math.abs(velocityX) < Math.abs(velocityY)) //Movimiento hacia abajo
		{
			tv.setText("arriba");
			return true;
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

}
