package test;

import cl.iic3380.backend.Place;
import cl.iic3380.frontend.MainActivity;
import cl.iic3380.frontend.MapActivity;
import cl.iic3380.frontend.R;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.os.SystemClock;
import android.provider.Browser;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.*;
import android.view.MotionEvent;
import android.widget.Button;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity>{

	private MainActivity mainActivity;

	public TestMainActivity() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		final MainActivity a = getActivity();
		assertNotNull(a);
		mainActivity=a;
	}

	@SmallTest
	public void testLocationNotNull()
	{
		assertNotNull("Localizaci�n no encontrada", mainActivity.GetLocation());
	}

	@SmallTest
	public void testRadiusUp()
	{
		int firstRadius = mainActivity.GetRadius();
		swipeUpSimulation();
		int secondRadius = mainActivity.GetRadius();
		assertNotSame("El radio es el mismo",firstRadius,secondRadius);
	}
	
	@SmallTest
	public void testRadiusDown()
	{
		int firstRadius = mainActivity.GetRadius();
		swipeDownSimulation();
		int secondRadius = mainActivity.GetRadius();
		assertNotSame("El radio es el mismo",firstRadius,secondRadius);
	}
	
	@SmallTest
	public void testNextPlay()
	{
		Place firstPlace = mainActivity.GetPlacesManager().GetLastPlayed();
		swipeRightSimulation();
		Place secondPlace = mainActivity.GetPlacesManager().GetLastPlayed();
		assertNotSame("El lugar es el mismo",firstPlace,secondPlace);
	}
	
	
	@SmallTest 
	public void testPreviousPlay()
	{
		Place firstPlace = mainActivity.GetPlacesManager().GetLastPlayed();
		swipeLeftSimulation();
		Place secondPlace = mainActivity.GetPlacesManager().GetLastPlayed();
		assertNotSame("El lugar es el mismo",firstPlace,secondPlace);
	}
	
	@MediumTest
	public void testMapActivityLaunch() 
	{
		// Se saca el instrumentation
		Instrumentation instrumentation = getInstrumentation();
		instrumentation.waitForIdleSync();
		// Se registra la actividad a monitorear
		ActivityMonitor mapActivityMonitor = instrumentation.addMonitor(MapActivity.class.getName(), null, false);
		// Se simula la accion
		doubleTapOneFingerSimulation();
		// Se saca la siguiente activity que debreia generarse
		MapActivity nextActivity = (MapActivity) getInstrumentation().waitForMonitorWithTimeout(mapActivityMonitor, 12);
		// Se revisa el estado
		assertNotNull("No se abrio MapActivity", nextActivity);
		nextActivity.finish();
	}
	
	private void doubleTapOneFingerSimulation()
	{
		TouchUtils.tapView(this, mainActivity.findViewById(R.id.main_view));
		TouchUtils.tapView(this, mainActivity.findViewById(R.id.main_view));
	}
	
	private void swipeUpSimulation()
	{
		TouchUtils.dragQuarterScreenUp(this,mainActivity);
	}
	
	private void swipeDownSimulation()
	{
		TouchUtils.dragQuarterScreenDown(this,mainActivity);
	}
	
	private void swipeLeftSimulation()
	{
		//Instrumentation
		Instrumentation instrumentation = getInstrumentation();
		//Posiciones de inicio y termino
		float xStart = 300;
		float yStart = 300;
		float xFinish = 200;
		
		//Tiempos
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		// Primer click
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
		instrumentation.sendPointerSync(event);
		// Se mueve
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xFinish, yStart, 0);
		instrumentation.sendPointerSync(event);
		// Se suelta
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xFinish, yStart, 0);
		instrumentation.sendPointerSync(event);
	}
	private void swipeRightSimulation()
	{
		//Instrumentation
		Instrumentation instrumentation = getInstrumentation();
		//Posiciones de inicio y termino
		float xStart = 300;
		float yStart = 300;
		float xFinish = 200;
		
		//Tiempos
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		// Primer click
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
		instrumentation.sendPointerSync(event);
		// Se mueve
		eventTime = SystemClock.uptimeMillis();
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xFinish, yStart, 0);
		instrumentation.sendPointerSync(event);
		// Se suelta
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xFinish, yStart, 0);
		instrumentation.sendPointerSync(event);
	}

	
	private void doubleTapTwoFingersSimulation() 
	{
		Instrumentation instrumentation = getInstrumentation(); 
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		float xFingerOne = 200;
		float yFingerOne = 200;
		float xFingerTwo = 200;
		float yFingerTwo = 400;

		MotionEvent fingerOneFirstTap = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xFingerOne, yFingerOne, 0);
		MotionEvent fingerTwoFirstTap = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xFingerTwo, yFingerTwo, 0);
		instrumentation.sendPointerSync(fingerOneFirstTap);
		instrumentation.sendPointerSync(fingerTwoFirstTap);

		//Aumentamos el tiempo
		eventTime +=500;
		MotionEvent fingerOneSecondTap = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xFingerOne, yFingerOne, 0);
		MotionEvent fingerTwoSecondTap = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xFingerTwo, yFingerTwo, 0);
		instrumentation.sendPointerSync(fingerOneSecondTap);
		instrumentation.sendPointerSync(fingerTwoSecondTap);

		//	    //Se hace doble tap
		//	    TouchUtils.tapView(this, mainActivity.findViewById(R.id.main_view));
		//	    TouchUtils.tapView(this, mainActivity.findViewById(R.id.main_view));


	}
}
