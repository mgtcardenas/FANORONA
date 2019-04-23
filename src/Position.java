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
public class Position extends Rectangle
{
	public static final double	POSITION_SIZE	= 100;
	private Chip				chip;
	private int					xCoordinate;
	private int					yCoordinate;
	
	/**
	 * To create a Position it is only necessary to give the type and the coordinates on the Board
	 *
	 * @param y the row this chip is located at, counting from 0
	 * @param x the column this chip is located at, counting from 0
	 */
	public Position(int y, int x)
	{
		this.chip			= null;
		this.xCoordinate	= x;
		this.yCoordinate	= y;
		
		this.setWidth(POSITION_SIZE);
		this.setHeight(POSITION_SIZE);
		this.setFill(Color.TRANSPARENT); // If we set visible to false, then actions are not taken into account
	}// end Position - constructor
	
	// region Getters & Setters
	public Chip getChip()
	{
		return chip;
	}// end getChip
	
	public void setChip(Chip chip)
	{
		if (chip != null)
		{
			chip.setLayoutX(this.getLayoutX() + POSITION_SIZE / 2);
			chip.setLayoutY(this.getLayoutY() + POSITION_SIZE / 2);
			chip.setPosition(this);
		}// end if
		
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
}// end Position - class