package cl.iic3380.backend;



/**
 * Clase que representa los datos del Jockey
 * @author Sandl
 * @dist: Distancia entre el Jockey y el objeto más cercano
 * @angle: ángulo actual en el cual se encuentra apuntando el compás
 * @x: posición x
 * @y: posición y
 * @z: posición z
 */
public class jockeyData {
	private int dist;
	private int angle;
	private int x;
	private int y;
	private int z;
	private boolean invalid;

	/**
	 * constructor
	 * @param cod: string para construir los datos
	 */
	public jockeyData(String cod)
	{
		cod= cod.trim();
		decodeString(cod);	
	}

	public int getDist()
	{
		return(this.dist);

	}

	public int getAngle()
	{
		return(this.angle);
	}
	public int getX ()
	{
		return(this.x);
	}
	public int getY()
	{
		return(this.y);
	}
	public int getZ ()
	{
		return(this.z);
	}

	public boolean isInvalid()
	{
		return(this.invalid);
	}

	/**
	 * 
	 * Método para generar los datos del Jockey a partir del string recibido por bluetooth
	 * @param code: el String en cuestión.
	 */
	private void decodeString(String code)
	{
		String[] partes = code.split(",");

		if (partes.length < 5)
		{
			this.dist = -1;
			this.angle = -1;
			this.x = -1;
			this.y = -1;
			this.z = -1;
			this.invalid = true; 
		}
		else
		{
			this.invalid = false;


			this.dist = Integer.parseInt(partes[0].split(":")[1].trim());
			this.angle =  Integer.parseInt(partes[1].split(":")[1].trim());
			this.x = Integer.parseInt(partes[2].split(":")[1].trim());
			this.y =  Integer.parseInt(partes[3].split(":")[1].trim());
			this.z = Integer.parseInt(partes[4].split(":")[1].trim());

		}

	}

}
