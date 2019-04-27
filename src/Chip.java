import javafx.scene.shape.Circle;

/**
 * @author Marco Cárdenas
 *
 *         A class that represents a chip as a circle, only used for the View
 */
public class Chip extends Circle
{
	public static final double	RADIUS	= 25; // the radius of the circle on the View
	int							y;           // the y coordinate it takes on the Model
	int							x;           // the x coordinate it takes on the Model
	
	/**
	 * To create a chip only the x & y coordinates it has on the model are needed
	 * 
	 * @param y the y coordinate it takes on the Model
	 * @param x the x coordinate it takes on the Model
	 */
	public Chip(int y, int x)
	{
		this.y	= y;
		this.x	= x;
		this.setLayoutX(x * 100 + 50);
		this.setLayoutY(y * 100 + 50);
		this.setRadius(RADIUS);
	}// end Chip - constructor
}// end Chip - class