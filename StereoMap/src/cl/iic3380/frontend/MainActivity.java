package cl.iic3380.frontend;

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
import android.widget.TextView;


public class MainActivity extends Activity implements OnInitListener, OnGestureListener {

	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private String bestProvider;
	private PlacesManager placesManager;
	private String radius;

	private TextView tv;
	
	private SoundEnv env;
	
	private List<Place> places;
	
	private GestureDetector gestureDetector;

	private TextToSpeech tts;
	// This code can be any value you want, its just a checksum.
	private static final int MY_DATA_CHECK_CODE = 1234;

	/*
	 * Codigo de respaldo librer�a OPENAL
	 */


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		this.env = SoundEnv.getInstance(this);

		//Localizaci�n
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		//Sacar mejor provider y location
		bestProvider = locationManager.getBestProvider(Utils.GetFineCriteria(),true);
		bestProvider = LocationManager.PASSIVE_PROVIDER;

		radius="500";

		tv = (TextView)findViewById(R.id.textView1);
		tv.setText(radius);
		placesManager = new PlacesManager(env);
		locationListener = new MyLocationListener(placesManager,tv,radius);
		
		//Detector de gestos
		gestureDetector = new GestureDetector(this, this);
		
		/**
		 * ACA VA EL HARDCODEO DEL PROVIDER
		 */
		//bestProvider=LocationManager.GPS_PROVIDER;
		/**
		 * ACA TERMINA
		 */

		placesManager.setUserLocation(locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER));

		locationManager.requestSingleUpdate(bestProvider, locationListener, this.getMainLooper());


		// Fire off an intent to check if a TTS engine is installed
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);


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


		while(true)
		{
			if(placesManager.getUserLocation()!=null)
				break;
		}
		

		Location userLocation = locationManager.getLastKnownLocation(bestProvider);
		try {
			places = placesManager.parsePlaces(radius, userLocation);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		placesManager.start();



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
