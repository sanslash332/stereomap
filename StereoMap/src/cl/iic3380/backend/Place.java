package cl.iic3380.backend;

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
	
}
