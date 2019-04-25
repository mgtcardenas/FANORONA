import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Position extends Rectangle implements Serializable
{
	public static final double	POSITION_SIZE	= 100;
	private Chip				chip;
	private int					xCoordinate;
	private int					yCoordinate;
	private String				type;
	
	public Position(int y, int x, String type)
	{
		this.chip			= null;
		this.xCoordinate	= x;
		this.yCoordinate	= y;
		this.type			= type;
		
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
	
	public String getType()
	{
		return type;
	}// end getType
		// endregion Getters & Setters
}// end Position - class