
package cl.iic3380.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

public class PlacesManager {
	
	private URIManager mUriManager;
	private List<Place> searchedPlaces;
	private TypeTransform typeTransformer;
	
	public PlacesManager()
	{
		mUriManager = new URIManager();
		searchedPlaces = new ArrayList<Place>();
		typeTransformer = new TypeTransform();
	}
	
	public List<Place> parsePlaces(String radius, Location location) throws InterruptedException, ExecutionException
	{
		String position = location.getLatitude()+","+location.getLongitude();
		searchedPlaces.clear();
		String JSON = new HttpRequest().execute(mUriManager.createURI(radius, position)).get();
		try 
		{
			JSONParsing(JSON);
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchedPlaces;

	}
	private void JSONParsing(String JSON) throws JSONException{
		JSONObject returnedJSON = new JSONObject(JSON);
		JSONArray results = returnedJSON.getJSONArray("results");
		String name = "";
		List<String> types = null;
		double lat = 0;
		double lng = 0;
		for (int i = 0; i < results.length(); i++)
		{
			JSONObject particularResult = results.getJSONObject(i);
			name = particularResult.getString("name");
			lat = particularResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
			lng = particularResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
			types = getStringArrayFromJSONArray(particularResult.getJSONArray("types"));
			Place newPlace = new Place(lat, lng, name, types);
			searchedPlaces.add(newPlace);
		}


	}
	private List<String> getStringArrayFromJSONArray(JSONArray jsonArray) throws JSONException {
		List<String> convertedArray = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++){
			convertedArray.add(typeTransformer.Transform(jsonArray.getString(i)));
		}
		return convertedArray;
	}

}


