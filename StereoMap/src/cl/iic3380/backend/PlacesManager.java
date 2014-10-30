
package cl.iic3380.backend;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pielot.openal.SoundEnv;

import android.location.Location;
import android.media.MediaPlayer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

public class PlacesManager extends Thread{
	
	private URIManager mUriManager;
	private List<Place> searchedPlaces;
	private TypeTransform typeTransformer;
	private Location userLocation;
	private SoundEnv env;
	private TextToSpeech tts;
	
	@Override
	public void run(){
		//ESCRIBIR EL AUDIO
		File file = Environment.getExternalStorageDirectory();	
		String externalStoragePath = file.getAbsolutePath();
		HashMap<String, String> myHashRender = new HashMap<String,String>();
		try 
		{

			File appTmpPath = new File(externalStoragePath + "/sounds/");
			appTmpPath.mkdirs();
			String destinationPath = "";
			for(Place p : searchedPlaces)
			{
				String fileName = p.getPlaceName().split(" ")[0]+".wav";
				destinationPath = appTmpPath.getAbsolutePath() + "/" + fileName;
				myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, p.GetSpeakedString());
				while(tts.isSpeaking()){
				}
				tts.synthesizeToFile(p.GetSpeakedString(), myHashRender, destinationPath);
				p.setAudioFilePath(appTmpPath.getAbsolutePath() + "/");
				myHashRender.clear();	


			}

			for(Place p : searchedPlaces)
			{
				p.addBufferAndSource(env);
				p.calculateSoundPosition(userLocation);
				p.start();
				Thread.sleep(getDuration(new File(p.getAudioFilePath() + p.getPlaceName().split(" ")[0] + ".wav")));
				p.join();
			}
		} 
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	public PlacesManager(SoundEnv env)
	{
		this.env = env;
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

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}
	
	public void setTTS(TextToSpeech texttospeech){
		tts = texttospeech;
	}

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


