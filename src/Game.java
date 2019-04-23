import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game
{
	private Board			board;
	private GameInterface	gameInterface;
	private Chip			selectedChip;
	
	public Game(Board board, GameInterface gameInterface)
	{
		this.board			= board;
		this.gameInterface	= gameInterface;
	}// end Game - constructor
	
	public void handleGridSpaceClicks(MouseEvent event)
	{
		Position clickedPosition = ((Position) event.getSource());
		System.out.println(clickedPosition.getyCoordinate() + ", " + clickedPosition.getxCoordinate());
	}// end handleGridSpaceClicks
	
	public void handleChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		System.out.println("I'm white chip");
		clickedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
	}// end handleChipClicks
}// end Game - class
