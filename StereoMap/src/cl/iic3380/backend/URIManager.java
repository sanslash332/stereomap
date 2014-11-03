package cl.iic3380.backend;

public class URIManager {
	
	private static final String key = "AIzaSyAoTaT4xuVMA-Pb8dr_2V5DWjFUpQpIwEQ";
	
	public String createURI(String radius, String location){
		String URI = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
				+ "json?location="+ location + "&radius="+ radius + "&key=" + key;
		return URI;
	}

}
