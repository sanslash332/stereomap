package cl.iic3380.backend;

/**
 * Clase que representa los datos del Jockey
 * @author Sandl
 * @dist: Distancia entre el Jockey y el objeto m�s cercano
 * @angle: �ngulo actual en el cual se encuentra apuntando el comp�s
 * @x: posici�n x
 * @y: posici�n y
 * @z: posici�n z
 */
public class jockeyData {
private final int dist;
private final int angle;
private final int x;
private final int y;
private final int z;
/**
 * constructor
 * @param cod: string para construir los datos
 */
public jockeyData(string cod)
{
decodeString(cod);	
}

/**
 * M�todo para generar los datos del Jockey a partir del string recibido por bluetooth
 * @param code: el String en cuesti�n.
 */
private void decodeString(string code)
{
	
}

}
