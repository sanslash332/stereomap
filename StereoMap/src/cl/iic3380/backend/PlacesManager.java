
package cl.iic3380.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pielot.openal.SoundEnv;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;

public class PlacesManager extends Thread
{

	private URIManager mUriManager;
	private List<Place> searchedPlaces;
	private TypeTransform typeTransformer;
	private Location userLocation;
	private TextToSpeech tts;
	private Context context;
	private int currentPosition;
	private Place lastPlayed;
	private SoundEnv soundEnvironment;
	//private FileCreator fileCreator;

	@Override
	public void run(){

		//Sacamos la ruta para utilizar en el dispositivo
		File file = Environment.getExternalStorageDirectory();	
		String externalStoragePath = file.getAbsolutePath();
		//Creamos el HashMap para almacenar
		HashMap<String, String> myHashRender = new HashMap<String,String>();
		try 
		{
			File appTmpPath = new File(externalStoragePath + "/sounds/");
			appTmpPath.mkdirs();
			String destinationPath = "";
			//Recorremos los lugares y escribimos los archivos
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
		} 
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		if(searchedPlaces.size() > 0)
			tts.speak("Lugares actualizados", TextToSpeech.QUEUE_FLUSH, null);
	}
	/** Constructor de la clase
	 * @param env Ambiente de sonido
	 * @param places Listado de lugares
	 */
	public PlacesManager(Context context, SoundEnv soundEnvironment)
	{
		this.mUriManager = new URIManager();
		this.searchedPlaces = new ArrayList<Place>();;
		this.typeTransformer = new TypeTransform();
		this.context=context;
		this.currentPosition=-1;
		this.lastPlayed=null;
		this.soundEnvironment = soundEnvironment;
	}

	public void createFiles(){

	}

	/**
	 * Obtiene la lista de Lugares (solo posicion)
	 * @param radius Radio de Busqueda
	 * @param location Localizacion inicial
	 * @return Lista de lugares encontrados
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void ParsePlaces(String radius, Location location) throws InterruptedException, ExecutionException
	{
		String position = location.getLatitude()+","+location.getLongitude();
		searchedPlaces.clear();

		//Armamos y parseamos el Json
		String JSON = new HttpRequest().execute(mUriManager.createURI(radius, position)).get();
		try 
		{
			JSONParsing(JSON);
			Collections.sort(searchedPlaces, new DistanceComparator(userLocation));
			String hola = "kjnk";
			hola.charAt(1);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

	}

	/** Parsea el JSON
	 * @param JSON Json inicial
	 * @throws JSONException
	 */
	private void JSONParsing(String JSON) throws JSONException{
		if(JSON != null){
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
				Place newPlace = new Place(lat, lng, name, types, context);
				searchedPlaces.add(newPlace);
			}
		}
		else tts.speak("No tiene conexión, conéctese a la red e intente más tarde", TextToSpeech.QUEUE_FLUSH, null);
	}
	/** Obtiene String Array de un JSON array
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
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

	public void PlayNext()
	{
		List<Place> availablePlaces = new ArrayList<Place>();

		for(Place place : searchedPlaces)
		{
			if(place.IsPlayable())
				availablePlaces.add(place);
		}

		if(availablePlaces.size()>0)
		{
			currentPosition++;
			//Paramos de tocar el anterior
			if(lastPlayed!=null)
				lastPlayed.StopPlaying();

			//Revisamos si se pasa
			if(currentPosition>=availablePlaces.size())
				currentPosition=0;
			lastPlayed = new Place(availablePlaces.get(currentPosition));
			lastPlayed.addBufferAndSource(soundEnvironment);
			lastPlayed.calculateSoundPosition(userLocation);
			lastPlayed.start();

		}

	}	

	public void PlayPrevious()
	{

		List<Place> availablePlaces = new ArrayList<Place>();
		for(Place place : searchedPlaces)
		{
			if(place.IsPlayable())
				availablePlaces.add(place);
		}
		if(availablePlaces.size()>0)
		{
			currentPosition --;
			//Paramos de tocar el anterior
			if(lastPlayed!=null)
				lastPlayed.StopPlaying();

			//Revisamos si se pasa
			if(currentPosition < 0 )
				currentPosition = availablePlaces.size() - 1;

			lastPlayed = new Place(availablePlaces.get(currentPosition));
			lastPlayed.addBufferAndSource(soundEnvironment);
			lastPlayed.calculateSoundPosition(userLocation);
			lastPlayed.start();
		}

	}	
	public String[] getLocations(){
		String[] locations = new String[searchedPlaces.size()];
		int i = 0;
		for (Place p : searchedPlaces){
			locations[i] = (p.getLatitud()+":"+p.getLongitud()+":"+p.getPlaceName());
			i++;
		}
		return locations;
	}
	public String getStringUserLocation(){
		String result = userLocation.getLatitude() + ":" + userLocation.getLongitude();
		return result;
	}
}
class DistanceComparator implements Comparator<Place> {
	private Location userLocation;
	public DistanceComparator(Location uLoc){
		this.userLocation = uLoc;
	}
	@Override
	public int compare(Place a, Place b) {
		return a.getDistanceToUser(userLocation) < b.getDistanceToUser(userLocation) ? -1 : a.getDistanceToUser(userLocation) == b.getDistanceToUser(userLocation) ? 0 : 1;
	}
}


