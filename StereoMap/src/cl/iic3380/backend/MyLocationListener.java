package cl.iic3380.backend;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

	private PlacesManager placesManager;

	public MyLocationListener (PlacesManager pm)
	{
		placesManager=pm;
	}

	@Override
	public void onLocationChanged(Location location) {
		placesManager.setUserLocation(location);

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
