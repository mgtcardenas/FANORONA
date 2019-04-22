import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class View extends AnchorPane
{
	public static final double	SCENE_WIDTH		= 900;
	public static final double	SCENE_HEIGHT	= 500;
	
	Board						board;
	
	Line[]						verticalLines;
	Line[]						horizontalLines;
	Line[]						longDiagonalLines;
	Line[]						shortDiagonalLines;
	
	Chip[]						whiteChips;
	Chip[]						blackChips;
	
	/**
	 * It is not necessary to give the Board as arguments since it is a singleton, but the
	 * View does use them to build itself
	 */
	public View()
	{
		board = Board.instance();
		buildComponents();
	}// end View
	
	/**
	 * Place all of the visual elements where they should go
	 */
	private void buildComponents()
	{
		this.verticalLines		= new Line[9];
		this.horizontalLines	= new Line[5];
		this.longDiagonalLines	= new Line[6];
		this.shortDiagonalLines	= new Line[4];
		
		this.whiteChips			= new Chip[22];
		this.blackChips			= new Chip[22];
		
		drawVerticalLines(this.verticalLines);
		drawHorizontalLines(this.horizontalLines);
		drawLongDiagonalLines(this.longDiagonalLines);
		drawSmallDiagonalLines(this.shortDiagonalLines);
		
		displayLines(this.verticalLines);
		displayLines(this.horizontalLines);
		displayLines(this.longDiagonalLines);
		displayLines(this.shortDiagonalLines);
		
		placeGridSpaces(this.board);
		
		drawWhiteChips(this.whiteChips);
		drawBlackChips(this.blackChips);
		
		displayChips(this.whiteChips);
		displayChips(this.blackChips);
	}// end buildComponents
	
	/**
	 * Puts all the GridSpaces that form part of the Board on the View in their correct coordinate
	 *
	 * @param board the game board
	 */
	private void placeGridSpaces(Board board)
	{
		GridSpace gridSpace;
		
		for (int y = 0; y < board.getGrid().length; y++)
		{
			for (int x = 0; x < board.getGrid()[y].length; x++)
			{
				gridSpace = board.getGrid()[y][x];
				gridSpace.setLayoutX(x * GridSpace.GRIDSPACE_SIZE);
				gridSpace.setLayoutY(y * GridSpace.GRIDSPACE_SIZE);
				getChildren().add(gridSpace);
			}// end for - x
		}// end for - y
	}// end placeGridSpaces
	
	/**
	 * Gives functionality to the visual elements by placing action listeners or event handlers
	 *
	 * @param controller a Game of Scrabble
	 */
	public void setEventHandlersAndActionListeners(Game controller)
	{
		for (int y = 0; y < board.getGrid().length; y++) // Handle GridSpaces
			for (int x = 0; x < board.getGrid()[y].length; x++)
				board.getGrid()[y][x].setOnMouseClicked(controller::handleGridSpaceClicks);
			
		for (Chip c : this.whiteChips)
			c.setOnMouseClicked(controller::handleChipClicks);
	}// end setEventHandlersAndActionListeners
	
	/**
	 * Creates white chips with the correct coordinates on the View
	 *
	 * @param whiteChips the white chips that belong to the player
	 */
	private void drawWhiteChips(Chip[] whiteChips)
	{
		int x, y, i;
		
		x	= 50;
		y	= 450;
		
		i	= 0;
		while (i < 9)
		{
			whiteChips[i] = new Chip(Color.WHITE);
			whiteChips[i].setLayoutX(x);
			whiteChips[i].setLayoutY(y);
			x += 100;
			i++;
		}// end while
		
		x	= 50;
		y	= 350;
		
		while (i < 18)
		{
			whiteChips[i] = new Chip(Color.WHITE);
			whiteChips[i].setLayoutX(x);
			whiteChips[i].setLayoutY(y);
			x += 100;
			i++;
		}// end while
		
		whiteChips[i] = new Chip(Color.WHITE);
		whiteChips[i].setLayoutX(150);
		whiteChips[i].setLayoutY(250);
		i++;
		
		whiteChips[i] = new Chip(Color.WHITE);
		whiteChips[i].setLayoutX(350);
		whiteChips[i].setLayoutY(250);
		i++;
		
		whiteChips[i] = new Chip(Color.WHITE);
		whiteChips[i].setLayoutX(650);
		whiteChips[i].setLayoutY(250);
		i++;
		
		whiteChips[i] = new Chip(Color.WHITE);
		whiteChips[i].setLayoutX(850);
		whiteChips[i].setLayoutY(250);
	}// end drawWhiteChips
	
	/**
	 * Creates black chips with the correct coordinates no the View
	 *
	 * @param blackChips the black chips that belong to the A.I.
	 */
	private void drawBlackChips(Chip[] blackChips)
	{
		int x, y, i;
		
		x	= 50;
		y	= 50;
		
		i	= 0;
		while (i < 9)
		{
			blackChips[i] = new Chip(Color.BLACK);
			blackChips[i].setLayoutX(x);
			blackChips[i].setLayoutY(y);
			x += 100;
			i++;
		}// end while
		
		x	= 50;
		y	= 150;
		
		while (i < 18)
		{
			blackChips[i] = new Chip(Color.BLACK);
			blackChips[i].setLayoutX(x);
			blackChips[i].setLayoutY(y);
			x += 100;
			i++;
		}// end while
		
		blackChips[i] = new Chip(Color.BLACK);
		blackChips[i].setLayoutX(50);
		blackChips[i].setLayoutY(250);
		i++;
		
		blackChips[i] = new Chip(Color.BLACK);
		blackChips[i].setLayoutX(250);
		blackChips[i].setLayoutY(250);
		i++;
		
		blackChips[i] = new Chip(Color.BLACK);
		blackChips[i].setLayoutX(550);
		blackChips[i].setLayoutY(250);
		i++;
		
		blackChips[i] = new Chip(Color.BLACK);
		blackChips[i].setLayoutX(750);
		blackChips[i].setLayoutY(250);
	}// end drawBlackChips
	
	/**
	 * Add all the chips to the AnchorPane for each Chip Array
	 *
	 * @param chips the chips to be added
	 */
	private void displayChips(Chip[] chips)
	{
		for (Chip c : chips)
			getChildren().add(c);
	}// end displayChips
	
	/**
	 * Creates vertical lines with the correct coordinates on the View
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
	 * Creates horizontal lines with the correct coordinates on the View
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
	 * Creates long diagonal lines with the correct coordinates on the View
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
	 * Creates short diagonal lines with the correct coordinates on the View
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
}// end View - class
