package cl.iic3380.frontend;

import java.util.List;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.iic3380.backend.Place;
import cl.iic3380.backend.PlacesManager;
import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity {
	private String[] locations;
	private GoogleMap googleMap;
	private String userLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		locations = (String[]) getIntent().getSerializableExtra("Locations");
		userLocation = (String) getIntent().getSerializableExtra("UserLocation");
		CameraUpdate center = CameraUpdateFactory
				.newLatLng(new LatLng(Double.parseDouble(userLocation.split(":")[0]),
									  Double.parseDouble(userLocation.split(":")[1])));
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    googleMap.setMyLocationEnabled(true);
		googleMap.moveCamera(center);
	    googleMap.animateCamera(zoom);	
	    
	    addMarkers();
	}

	private void addMarkers(){
		for (String loc : locations){
			String[] params = loc.split(":");
			LatLng lt = new LatLng(Double.parseDouble(params[0]), Double.parseDouble(params[1]));
			String name = params[2];
			googleMap.addMarker(new MarkerOptions().position(lt).title(name));
		}
	}

}
