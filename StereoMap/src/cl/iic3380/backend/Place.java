package cl.iic3380.backend;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.location.Location;

public class Place {
	private double latitud, longitud;
	private String name;
	private List<String> types;		
	private String audioFilePath;
	private Buffer buffer;
	private Source currentSource;
	
	public Place(double lat, double lng, String name, List<String> Types){
		this.latitud = lat;
		this.longitud = lng;
		this.name = name;
		this.types = Types;
	}
	public double getLatitud() {
		return latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public String getName() {
		return name;
	}
	public List<String> getTypes() {
		return types;
	}
	
	public String GetSpeakedString()
	{
		String result = name;
		for(String type : types)
		{
			result+="."+type;
		}
		return result;
	}
	public void setAudioFilePath(String path){
		this.audioFilePath = path;
	}
	public String getAudioFilePath(){
		return this.audioFilePath;
	}
	public void addBufferAndSource(SoundEnv env) {
		try {
			String totalFilePath = audioFilePath+name.split(" ")[0]+".wav";
			this.buffer = env.addBuffer(name, totalFilePath);
			this.currentSource = env.addSource(buffer);
			currentSource.setPosition(0, 0, 0);
			currentSource.setPitch(1.1f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void calculateSoundPosition(Location userLocation) {
		//TODO crear algoritmo para calcular la posicion en la que se tiene que escuchar el sonido
		//latitude norte-sur
		//longitude este-oeste
		double latitudeDifference = userLocation.getLatitude()-latitud;
		double longitudeDifference = userLocation.getLongitude()-longitud;
		double magnitude = Math.sqrt(Math.pow(latitudeDifference, 2)+Math.pow(longitudeDifference, 2));
		latitudeDifference /= magnitude;
		longitudeDifference /= magnitude;
		
		float newLatitude = (float)latitudeDifference*10;
		float newLongitude = (float)longitudeDifference*10;
		
		currentSource.setPosition(newLatitude, newLongitude, 0);
	}
	public Buffer getBuffer() {
		return buffer;
	}
	public Source getCurrentSource() {
		return currentSource;
	}
	

}
