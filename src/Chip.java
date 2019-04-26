import javafx.scene.shape.Circle;

public class Chip extends Circle
{
	public static final double	RADIUS	= 25;
	int							y;
	int							x;
	
	public Chip(int y, int x)
	{
		this.y	= y;
		this.x	= x;
		this.setLayoutX(x * 100 + 50);
		this.setLayoutY(y * 100 + 50);
		this.setRadius(RADIUS);
	}// end Chip - constructor
}// end Chip - class