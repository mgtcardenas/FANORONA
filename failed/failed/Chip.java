package failed;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Chip extends Circle implements Serializable
{
	public static final double	RADIUS	= 25; // for the view
	private Position			position;
	private String				colorType;
	
	public Chip(Color color)
	{
		this.position	= null;
		this.colorType	= color == Color.WHITE ? "white" : "black";
		this.setFill(color);
		this.setRadius(RADIUS);
		this.setStroke(Color.BLACK);
	}// end Chip - constructor
	
	// region Getters & Setters
	public Position getPosition()
	{
		return position;
	}// end getPosition
	
	public void setPosition(Position position)
	{
		this.position = position;
	}// end setPosition
	
	public String getColorType()
	{
		return colorType;
	}// end getColorType
		// endregion Getters & Setters
}// end Chip - class