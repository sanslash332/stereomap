package cl.iic3380.backend;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocationListener implements LocationListener {

	private PlacesManager placesManager;
	private TextView textView;
	private String radius;

	public MyLocationListener (PlacesManager pm, TextView tv, String r)
	{
		placesManager=pm;
		textView=tv;
		radius=r;
	}

	@Override
	public void onLocationChanged(Location location) {

		String s = "";
		List<Place> places;
		try 
		{
			places = placesManager.parsePlaces(radius, location);
			for(Place p : places)
			{
				s+="Name: "+p.getName()+" - Type: "+p.getTypes().toString()+"\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textView.setText(s);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
