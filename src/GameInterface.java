import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 * @author Marco Cárdenas
 *
 *         This class is the View of the applciation, it only contains the methods
 *         for initializing it, that is, to draw the board of the game (the lines)
 */
public class GameInterface extends AnchorPane
{
	public static final double	SCENE_WIDTH		= 900;
	public static final double	SCENE_HEIGHT	= 500;
	
	Line[]						verticalLines;
	Line[]						horizontalLines;
	Line[]						longDiagonalLines;
	Line[]						shortDiagonalLines;
	
	/**
	 * Creating the GameInterface is just building it's components
	 */
	public GameInterface()
	{
		buildComponents();
	}// end GameInterface
	
	/**
	 * Place all of the visual elements where they should go
	 */
	private void buildComponents()
	{
		this.verticalLines		= new Line[9];
		this.horizontalLines	= new Line[5];
		this.longDiagonalLines	= new Line[6];
		this.shortDiagonalLines	= new Line[4];
		
		drawVerticalLines(this.verticalLines);
		drawHorizontalLines(this.horizontalLines);
		drawLongDiagonalLines(this.longDiagonalLines);
		drawSmallDiagonalLines(this.shortDiagonalLines);
		
		displayLines(this.verticalLines);
		displayLines(this.horizontalLines);
		displayLines(this.longDiagonalLines);
		displayLines(this.shortDiagonalLines);
	}// end buildComponents
	
	/**
	 * Creates vertical lines with the correct coordinates on the GameInterface
	 *
	 * @param verticalLines the vertical lines array
	 */
	private void drawVerticalLines(Line[] verticalLines)
	{
		int	startY	= 50;
		int	endY	= 450;
		int	x		= 50;
		
		for (int i = 0; i < verticalLines.length; i++)
		{
			verticalLines[i]	= new Line(x, startY, x, endY);
			x					+= 100;
		}// end for - x
	}// end drawVerticalLines
	
	/**
	 * Creates horizontal lines with the correct coordinates on the GameInterface
	 *
	 * @param horizontalLines the horizontal lines array
	 */
	private void drawHorizontalLines(Line[] horizontalLines)
	{
		int	startX	= 50;
		int	endX	= 850;
		int	y		= 50;
		
		for (int i = 0; i < horizontalLines.length; i++)
		{
			horizontalLines[i]	= new Line(startX, y, endX, y);
			y					+= 100;
		}// end for - x
	}// end drawHorizontalLines
	
	/**
	 * Creates long diagonal lines with the correct coordinates on the GameInterface
	 *
	 * @param longDiagonalLines the long diagonal lines array
	 */
	private void drawLongDiagonalLines(Line[] longDiagonalLines)
	{
		int	startX	= 50;
		int	startY	= 50;
		int	endX	= 450;
		int	endY	= 450;
		
		for (int i = 0; i < longDiagonalLines.length / 2; i++)
		{
			longDiagonalLines[i]	= new Line(startX, startY, endX, endY);
			startX					+= 200;
			endX					+= 200;
		}// end for - i
		
		startX	= 850;
		startY	= 50;
		endX	= 450;
		endY	= 450;
		
		for (int i = longDiagonalLines.length / 2; i < longDiagonalLines.length; i++)
		{
			longDiagonalLines[i]	= new Line(startX, startY, endX, endY);
			startX					-= 200;
			endX					-= 200;
		}// end for - i
	}// end drawLongDiagonalLines
	
	/**
	 * Creates short diagonal lines with the correct coordinates on the GameInterface
	 *
	 * @param shortDiagonalLines the short diagonal lines array
	 */
	private void drawSmallDiagonalLines(Line[] shortDiagonalLines)
	{
		shortDiagonalLines[0]	= new Line(50, 250, 250, 50);
		shortDiagonalLines[1]	= new Line(50, 250, 250, 450);
		shortDiagonalLines[2]	= new Line(850, 250, 650, 50);
		shortDiagonalLines[3]	= new Line(850, 250, 650, 450);
	}// end drawSmallDiagonalLines
	
	/**
	 * Add all the lines to the AnchorPane for each Line Array
	 *
	 * @param lines the lines to be added
	 */
	private void displayLines(Line[] lines)
	{
		for (Line l : lines)
			getChildren().add(l);
	}// end displayLines
}// end GameInterface - class
