package cl.IIC3380.stereomap;

import java.util.List;

public class Place {
	double latitud, longitud;
	String name;
	List<String> types;			
	public Place(double lat, double lng, String name, List<String> Types){
		this.latitud = lat;
		this.longitud = lng;
		this.name = name;
		this.types = Types;
	}
}
