package cl.iic3380.frontend;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import org.pielot.openal.SoundEnv;

import cl.iic3380.backend.*;
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
import android.widget.TextView;


public class MainActivity extends Activity implements OnInitListener {

	private LocationManager locationManager;
	private MyLocationListener locationListener;
	private String bestProvider;
	private PlacesManager placesManager;
	private String radius;

	private SoundEnv env;

	private String currentFileName;
	private String[] synthResult;
	private List<Place> places;

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
		//Localizaci�n
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		//Sacar mejor provider y location
		bestProvider = locationManager.getBestProvider(Utils.GetFineCriteria(),true);

		radius="500";

		TextView tv = (TextView)findViewById(R.id.textView1);
		placesManager = new PlacesManager();
		locationListener = new MyLocationListener(placesManager,tv,radius);



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
	public void onInit(int status) {
		//		tts.speak("Hello folks, welcome to my little demo on Text To Speech.",
		//				TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		//				null);


		while(true)
		{
			if(placesManager.getUserLocation()!=null)
				break;
		}

		//ESCRIBIR EL AUDIO
		File file = Environment.getExternalStorageDirectory();	
		String externalStoragePath = file.getAbsolutePath();
		HashMap<String, String> myHashRender = new HashMap<String,String>();
		try 
		{
			this.env = SoundEnv.getInstance(this);
			Location userLocation = locationManager.getLastKnownLocation(bestProvider);
			places = placesManager.parsePlaces(radius, userLocation);
			File appTmpPath = new File(externalStoragePath + "/sounds/");
			appTmpPath.mkdirs();
			for(Place p : places)
			{
				String fileName = p.getPlaceName().split(" ")[0]+".wav";
				String destinationPath = appTmpPath.getAbsolutePath() + "/" + fileName;
				myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, p.GetSpeakedString());
				tts.synthesizeToFile(p.GetSpeakedString(), myHashRender, destinationPath);
				p.setAudioFilePath(appTmpPath.getAbsolutePath() + "/");
				myHashRender.clear();	

				while(tts.isSpeaking()){}

				p.addBufferAndSource(env);
				p.calculateSoundPosition(userLocation);
				
				p.start();
				p.sleep(getDuration(new File(destinationPath)));




			}

			//			for(Place p : places)
			//			{
			//				p.addBufferAndSource(env);
			//				p.calculateSoundPosition(userLocation);
			//			}
			//tts.speak(result,TextToSpeech.QUEUE_FLUSH, null);
			//Utils.SpeakText(tts, result);
			//readTextWithOpen4Al();
		} 
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		//synthResult = ttsSynth.speakText(result);


	}

	//				try {
	//		
	//					/* First we obtain the instance of the sound environment. */
	//					/*
	//					 * Now we load the sounds into the memory that we want to play
	//					 * later. Each sound has to be buffered once only. To add new sound
	//					 * copy them into the assets folder of the Android project.
	//					 * Currently only mono .wav files are supported.
	//					 */
	//					//	Buffer placeBuffer = env.addBuffer(currentFileName);
	//		
	//					/*
	//					 * To actually play a sound and place it somewhere in the sound
	//					 * environment, we have to create sources. Each source has its own
	//					 * parameters, such as 3D position or pitch. Several sources can
	//					 * share a single buffer.
	//					 */
	//		
	//					// Now we spread the sounds throughout the sound room.
	//					currentSource.setPosition(0, 0, 0);
	//		
	//					// and change the pitch of the second lake.
	//					currentSource.setPitch(1.1f);
	//		
	//					/*
	//					 * These sounds are perceived from the perspective of a virtual
	//					 * listener. Initially the position of this listener is 0,0,0. The
	//					 * position and the orientation of the virtual listener can be
	//					 * adjusted via the SoundEnv class.
	//					 */
	//					
	//		
	//		
	//		
	//		
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}



	private void readTextWithOpen4Al() {
		for(Place p : places){
			p.start();

			try 
			{
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	//	LIBRER�A
	//	@Override
	//	public void onResume() {
	//		super.onResume();
	//		Log.i(TAG, "onResume()");
	//
	//		/*
	//		 * Start playing all sources. 'true' as parameter specifies that the
	//		 * sounds shall be played as a loop.
	//		 */
	//		//this.lake1.play(true);
	//		//this.lake2.play(true);
	//		this.park1.play(true);
	//	}
	//
	//		@Override
	//		public void onPause() {
	//			super.onPause();
	//	
	//			// Stop all sounds
	//			this.env.stopAllSources();
	//	
	//		}
	//
	//	@Override
	//	public void onDestroy() {
	//		super.onDestroy();
	//		Log.i(TAG, "onDestroy()");
	//
	//		// Be nice with the system and release all resources
	//		this.env.stopAllSources();
	//		this.env.release();
	//	}
	//
	//	@Override
	//	public void onLowMemory() {
	//		this.env.onLowMemory();
	//	}
	//


	//	private void ChangeXPosition(int currentPos)
	//	{
	//		this.park1.setPosition(currentPos-delta, lastPositionY, lastPositionZ);
	//		lastPositionX=currentPos-delta;
	//	}
	//	private void ChangeYPosition(int currentPos)
	//	{
	//		this.park1.setPosition(lastPositionX, currentPos-delta, lastPositionZ);
	//		lastPositionY=currentPos-delta;
	//	}
	//	private void ChangeZPosition(int currentPos)
	//	{
	//		this.park1.setPosition(lastPositionX, lastPositionY, currentPos-delta);
	//		lastPositionZ=currentPos-delta;
	//	}

	private int getDuration(File file)
	{
		int length = -1;
		try
		{
			MediaPlayer mp = new MediaPlayer();
			FileInputStream fs = new FileInputStream(file);
			FileDescriptor fd = fs.getFD();
			mp.setDataSource(fd);
			mp.prepare();
			length = mp.getDuration();
			mp.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return length;

	}
}
