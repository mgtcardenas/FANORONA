import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Chip extends Circle
{
	public static final double	RADIUS	= 25; // for the view
	private GridSpace			gridSpace;
	
	/**
	 * To create a Chip, only a the color of it
	 * 
	 * @param color the color of the chip
	 */
	public Chip(Color color)
	{
		this.setFill(color);
		this.gridSpace = null;
		
		this.setRadius(RADIUS);
		this.setStroke(Color.BLACK);
	}// end Chip - constructor
	
	// region Getters & Setters
	public GridSpace getGridSpace()
	{
		return gridSpace;
	}// end getGridSpace
	
	public void setGridSpace(GridSpace gridSpace)
	{
		this.gridSpace = gridSpace;
	}// end setGridSpace
		// endregion Getters & Setters
}// end Chip - class