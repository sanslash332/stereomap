package cl.iic3380.frontend;

import java.util.concurrent.ExecutionException;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

<<<<<<< HEAD:StereoMap/src/cl/iic3380/frontend/MainActivity.java
import cl.iic3380.backend.MyLocationListener;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.location.LocationManager;
=======
>>>>>>> origin/GooglePlaces:StereoMap/src/cl/IIC3380/stereomap/MainActivity.java
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends ActionBarActivity {

	private final static String	TAG	= "HelloOpenAL4Android";

	private SoundEnv			env;

	private Source				lake1;
	private Source				lake2;
	private Source				park1;

	private int lastPositionX;
	private int lastPositionY;
	private int lastPositionZ;
	private int delta;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PlacesManager pm = new PlacesManager();
		try {
			pm.parsePlaces("500", "-33.8670522,151.1957362");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.i(TAG, "onCreate()");
		this.setContentView(R.layout.main);

		//Localización
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//Instaciamos nuestro location listener
		MyLocationListener locationListener = new MyLocationListener();
		//Registramos
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		try {
			
			/* First we obtain the instance of the sound environment. */
			this.env = SoundEnv.getInstance(this);
			/*
			 * Now we load the sounds into the memory that we want to play
			 * later. Each sound has to be buffered once only. To add new sound
			 * copy them into the assets folder of the Android project.
			 * Currently only mono .wav files are supported.
			 */
			Buffer lake = env.addBuffer("lake");
			Buffer park = env.addBuffer("park");

			/*
			 * To actually play a sound and place it somewhere in the sound
			 * environment, we have to create sources. Each source has its own
			 * parameters, such as 3D position or pitch. Several sources can
			 * share a single buffer.
			 */
			this.lake1 = env.addSource(lake);
			this.lake2 = env.addSource(lake);
			this.park1 = env.addSource(park);

			// Now we spread the sounds throughout the sound room.
			this.lake1.setPosition(0, 0, -10);
			this.lake2.setPosition(-6, 0, 4);
			this.park1.setPosition(0, 0, 0);

			// and change the pitch of the second lake.
			this.lake2.setPitch(1.1f);

			/*
			 * These sounds are perceived from the perspective of a virtual
			 * listener. Initially the position of this listener is 0,0,0. The
			 * position and the orientation of the virtual listener can be
			 * adjusted via the SoundEnv class.
			 */
			this.env.setListenerOrientation(20);
			lastPositionX=0;
			lastPositionY=0;
			lastPositionZ=0;
			
			SeekBar sliderX = (SeekBar)findViewById(R.id.sliderX);
			SeekBar sliderY = (SeekBar)findViewById(R.id.sliderY);
			SeekBar sliderZ = (SeekBar)findViewById(R.id.sliderZ);
			delta = 25;
			sliderX.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					ChangeXPosition(seekBar.getProgress());
					// TODO Auto-generated method stub
					
				}
			});
			
			sliderY.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					ChangeYPosition(seekBar.getProgress());
					// TODO Auto-generated method stub
					
				}
			});
			sliderZ.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					ChangeZPosition(seekBar.getProgress());
					// TODO Auto-generated method stub
					
				}
			});
			
		} catch (Exception e) {
			Log.e(TAG, "could not initialise OpenAL4Android", e);
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");

		/*
		 * Start playing all sources. 'true' as parameter specifies that the
		 * sounds shall be played as a loop.
		 */
		//this.lake1.play(true);
		//this.lake2.play(true);
		this.park1.play(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause()");

		// Stop all sounds
		this.lake1.stop();
		this.lake2.stop();
		this.park1.stop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");

		// Be nice with the system and release all resources
		this.env.stopAllSources();
		this.env.release();
	}

	@Override
	public void onLowMemory() {
		this.env.onLowMemory();
	}
	
	
	
	private void ChangeXPosition(int currentPos)
	{
		this.park1.setPosition(currentPos-delta, lastPositionY, lastPositionZ);
		lastPositionX=currentPos-delta;
	}
	private void ChangeYPosition(int currentPos)
	{
		this.park1.setPosition(lastPositionX, currentPos-delta, lastPositionZ);
		lastPositionY=currentPos-delta;
	}
	private void ChangeZPosition(int currentPos)
	{
		this.park1.setPosition(lastPositionX, lastPositionY, currentPos-delta);
		lastPositionZ=currentPos-delta;
	}
}
