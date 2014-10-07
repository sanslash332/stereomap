package cl.IIC3380.stereomap;

import java.util.HashMap;

public class TypeTransform {

	HashMap<String, String> typesHash;
	public TypeTransform(){
		typesHash = new HashMap<String, String>();
		fillHash();	
	}
	
	private void fillHash(){
		typesHash.put("accounting", "Contador");
		typesHash.put("airport", "Aeropuerto");
		typesHash.put("amusement_park", "Parque de entretenciones");
		typesHash.put("aquarium", "Acuario");
		typesHash.put("art_gallery", "Galería de arte");
		typesHash.put("atm", "Cajero automático");
		typesHash.put("bakery", "Panadería y pastelería");
		typesHash.put("bank", "Banco");
		typesHash.put("bar", "Bar");
		typesHash.put("beauty_salon", "Salón de belleza");
		typesHash.put("bicycle_store", "Tienda de bicicletas");
		typesHash.put("book_store", "Tienda de libros");
		typesHash.put("bowling_alley", "Local de bolos");
		typesHash.put("bus_station", "Estación de buses");
		typesHash.put("cafe", "Café");
		typesHash.put("campground", "Camping");
		typesHash.put("car_dealer", "Vendedores de autos");
		typesHash.put("car_rental", "Arriendo de autos");
		typesHash.put("car_repair", "Reparación de autos");
		typesHash.put("car_wash", "Lavado de autos");
		typesHash.put("casino", "Casino");
		typesHash.put("cemetery", "Cementerio");
		typesHash.put("church", "Iglesia");
		typesHash.put("city_hall", "Municipalidad");
		typesHash.put("clothing_store", "Tienda de ropa");
		typesHash.put("convenience_store", "Tienda de conveniencia");
		typesHash.put("courthouse", "Palacio de justicia");
		typesHash.put("dentist", "Dentista");
		typesHash.put("department_store", "Tienda por departamento");
		typesHash.put("doctor", "Médico");
		typesHash.put("electrician", "Electricista");
		typesHash.put("electronics_store", "Tienda de electrónicos");
		typesHash.put("embassy", "Embajada");
		typesHash.put("establishment", "Establecimiento");
		typesHash.put("finance", "Finanzas");
		typesHash.put("fire_station", "Estación de bomberos");
		typesHash.put("florist", "Florería");
		typesHash.put("food", "Comida");
		typesHash.put("funeral_home", "Funeraria");
		typesHash.put("furniture_store", "Tienda de muebles");
		typesHash.put("gas_station", "Gasolinera");
		typesHash.put("general_contractor", "Contratista General");
		typesHash.put("grocery_or_supermarket", "Supermercado");
		typesHash.put("gym", "Gimnasio");
		typesHash.put("hair_care", "Peluquería");
		typesHash.put("hardware_store", "Tienda de hardware");
		typesHash.put("health", "Salud");
		typesHash.put("hindu_temple", "Templo Hindú");
		typesHash.put("home_goods_store", "Tienda de artículos para el hogar");
		typesHash.put("hospital", "Hospital");
		typesHash.put("insurance_agency", "Agencia de seguros");
		typesHash.put("jewelry_store", "Joyería");
		typesHash.put("laundry", "Lavandería");
		typesHash.put("lawyer", "Abogado");
		typesHash.put("library", "Librería");
		typesHash.put("liquor_store", "Botillería");
		typesHash.put("local_government_office", "Oficina de gobierno local");
		typesHash.put("locksmith", "Cerraduras y llaves");
		typesHash.put("lodging", "Alojamiento");
		typesHash.put("meal_delivery", "Entrega de comida");
		typesHash.put("meal_takeaway", "Comida para llevar");
		typesHash.put("mosque", "Mezquita");
		typesHash.put("movie_rental", "Arriendo de películas");
		typesHash.put("movie_theater", "Cine");
		typesHash.put("moving_company", "Compañía de mudanzas");
		typesHash.put("museum", "Museo");
		typesHash.put("night_club", "Club nocturno");
		typesHash.put("painter", "Pintor");
		typesHash.put("park", "Parque");
		typesHash.put("parking", "Estacionamiento");
		typesHash.put("pet_store", "Tienda de mascotas");
		typesHash.put("pharmacy", "Farmacia");
		typesHash.put("physioterapist", "Fisioterapeuta");
		typesHash.put("place_of_worship", "Lugar de culto");
		typesHash.put("plumber", "Plomero");
		typesHash.put("police", "Policía");
		typesHash.put("post_office", "Oficina de correos");
		typesHash.put("real_estate_agency", "Agencia de bienes raíces");
		typesHash.put("restaurant", "Restaurante");
		typesHash.put("roofing_contractor", "Instaladores de techo");
		typesHash.put("rv_park", "Estacionamiento de casas rodantes");
		typesHash.put("school", "Colegio");
		typesHash.put("shoe_store", "Tienda de zapatos");
		typesHash.put("shopping_mall", "Centro comercial");
		typesHash.put("spa", "Spa");
		typesHash.put("stadium", "Estadio");
		typesHash.put("storage", "Depósito");
		typesHash.put("store", "Tienda");
		typesHash.put("subway_station", "Estación de metro");
		typesHash.put("synagogue", "Sinagoga");
		typesHash.put("taxi_stand", "Parada de taxi");
		typesHash.put("train_station", "Estación de trenes");
		typesHash.put("travel_agency", "Agencia de viajes");
		typesHash.put("university", "Universidad");
		typesHash.put("veterinary_care", "Veterinario");
		typesHash.put("zoo", "Zoologico");
		typesHash.put("administrative_area_level_1", "Área administrativa nivel 1");
		typesHash.put("administrative_area_level_2", "Área administrativa nivel 2");
		typesHash.put("administrative_area_level_3", "Área administrativa nivel 3");
		typesHash.put("colloquial_area", "Área coloquial");
		typesHash.put("country", "País");
		typesHash.put("floor", "Piso");
		typesHash.put("geocode", "Código geográfico");
		typesHash.put("intersection", "Intersección");
		typesHash.put("locality", "Localidad");
		typesHash.put("natural_feature", "Característica natural");
		typesHash.put("neighborhood", "Barrio");
		typesHash.put("political", "Politico");
		typesHash.put("point_of_interest", "Punto de interés");
		typesHash.put("post_box", "Buzón de correos");
		typesHash.put("postal_code", "Código postal");
		typesHash.put("postal_code_prefix", "Préfijo de código postal");
		typesHash.put("postal_town", "Pueblo postal");
		typesHash.put("premise", "Premisa");
		typesHash.put("room", "Habitación");
		typesHash.put("route", "Ruta");
		typesHash.put("street_adress", "Dirección de calle");
		typesHash.put("street_number", "Número de calle");
		typesHash.put("sublocality", "Sub localidad");
		typesHash.put("sublocality_level_1", "Sub localidad nivel 1");
		typesHash.put("sublocality_level_2", "Sub localidad nivel 2");
		typesHash.put("sublocality_level_3", "Sub localidad nivel 3");
		typesHash.put("sublocality_level_4", "Sub localidad nivel 4");
		typesHash.put("sublocality_level_5", "Sub localidad nivel 5");
		typesHash.put("subpremise", "Sub premisa");
		typesHash.put("transit_station", "Estación de tránsito");
	}
	
	public String Transform(String typeToTransform){	
		return typesHash.get(typeToTransform) + " ";
	}
}
