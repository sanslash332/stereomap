package cl.iic3380.backend;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.content.Context;
import android.location.Location;

public class Place extends Thread
{
	//Variables de la clase
	private double latitud, longitud;
	private String audioFilePath;
	private String name;
	private String totalFilePath;
	private List<String> types;		
	private Buffer buffer;
	private Source currentSource;


	/** Constructor de la clase
	 * @param latitude Latitud
	 * @param longitude Longitud
	 * @param name Nombre
	 * @param types Tipos
	 */
	public Place(double latitude, double longitude, String name, List<String> types, Context context)
	{
		this.latitud = latitude;
		this.longitud = longitude;
		this.name = name;
		this.types = types;
	}

	@Override
	public void run()
	{
		currentSource.play(false);
	}
	
	public Place(Place p){
		this.audioFilePath = p.getAudioFilePath();
		this.latitud = p.getLatitud();
		this.longitud = p.getLongitud();
		this.name = p.getPlaceName();
		this.types = p.getTypes();
	}
	
	public String GetSpeakedString()
	{
		String result = name;
		for(String type : types)
		{
			result+=". "+type;
		}
		return result;
	}
	public void setAudioFilePath(String path){
		this.audioFilePath = path;
	}
	public String getAudioFilePath(){
		return this.audioFilePath;
	}
	
	/** A�ade buffer y source
	 * @param env Ambiente de sonido
	 */
	public void addBufferAndSource(SoundEnv env) 
	{
		try 
		{
			totalFilePath = audioFilePath+name.split(" ")[0]+".wav";
			this.buffer = env.addBuffer(name, totalFilePath);
			this.currentSource = env.addSource(buffer);
			currentSource.setGain(7.5f);
			currentSource.setPosition(0, 0, 0);
			currentSource.setPitch(1.1f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void calculateSoundPosition(Location userLocation, int angle) {
		//latitude norte-sur
		//longitude este-oeste
		double longitudeDifference = userLocation.getLongitude()-longitud;
		double latitudeDifference = userLocation.getLatitude()-latitud;
		double magnitude = Math.sqrt(Math.pow(latitudeDifference, 2)+Math.pow(longitudeDifference, 2));
		longitudeDifference /= magnitude;
		latitudeDifference /= magnitude;

		float unitLongitude = (float)longitudeDifference;
		float unitLatitude = (float)latitudeDifference;
		
		//Calculo de angulo en radianes
		double radianAngle = 2*(360-angle)*Math.PI/360;

		float finalLongitude = (float)(unitLatitude*Math.cos(radianAngle) - unitLongitude*Math.sin(radianAngle));
		float finalLatitude = (float)(unitLatitude*Math.cos(radianAngle) + unitLongitude*Math.sin(radianAngle));

		
		currentSource.setPosition(finalLongitude, finalLatitude, 0);
	}
	
	
	
	
	
	
	
	public double getDistanceToUser(Location userLocation){
		double latitudeDifference = userLocation.getLatitude()-latitud;
		double longitudeDifference = userLocation.getLongitude()-longitud;
		return Math.sqrt(Math.pow(latitudeDifference, 2)+Math.pow(longitudeDifference, 2));
	}
	public boolean IsPlayable()
	{
		//Ver si se puede tocar
		File file = new File(totalFilePath = audioFilePath+name.split(" ")[0]+".wav");
		if(file.exists())
			return true;
		else
			return false;
	}
	
	public void StopPlaying()
	{
		currentSource.stop();
	}
	
	@Override
	public String toString()
	{
		return name+" "+latitud+" "+longitud+"\n";
	}
	
	public Buffer getBuffer() {
		return buffer;
	}
	public Source getCurrentSource() {
		return currentSource;
	}

	public double getLatitud() {
		return latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public String getPlaceName() {
		return name;
	}
	public List<String> getTypes() {
		return types;
	}

}
