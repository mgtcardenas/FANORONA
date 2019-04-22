import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Marco Cárdenas
 *
 *         A class that represents each individual space/box on the board.
 *         Each gridspace is of a particular type and has a multiplier.
 *         It may also have a Chip associated to it and is represented
 *         as a StackPane with a rectangle that varies in color depending
 *         on the type attribute
 */
public class GridSpace extends Rectangle
{
	public static final double	GRIDSPACE_SIZE	= 100;
	private Chip				chip;
	private int					xCoordinate;
	private int					yCoordinate;
	
	/**
	 * To create a GridSpace it is only necessary to give the type and the coordinates on the Board
	 *
	 * @param y the row this chip is located at, counting from 0
	 * @param x the column this chip is located at, counting from 0
	 */
	public GridSpace(int y, int x)
	{
		this.chip			= null;
		this.xCoordinate	= x;
		this.yCoordinate	= y;
		
		this.setWidth(GRIDSPACE_SIZE);
		this.setHeight(GRIDSPACE_SIZE);
		this.setFill(Color.TRANSPARENT); // If we set visible to false, then actions are not taken into account
	}// end GridSpace - constructor
	
	// region Getters & Setters
	public Chip getChip()
	{
		return chip;
	}// end getChip
	
	public void setChip(Chip chip)
	{
		this.chip = chip;
	}// end setChip
	
	public int getxCoordinate()
	{
		return xCoordinate;
	}// end getxCoordinate
	
	public int getyCoordinate()
	{
		return yCoordinate;
	}// end getyCoordinate
		// endregion Getters & Setters
}// end GridSpace - class