package cl.iic3380.backend;

import java.io.IOException;
import java.util.List;

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
			this.buffer = env.addBuffer(audioFilePath, name);
			this.currentSource = env.addSource(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void calculateSoundPosition(Location userLocation) {
		//TODO crear algoritmo para calcular la posicion en la que se tiene que escuchar el sonido
	}

}
