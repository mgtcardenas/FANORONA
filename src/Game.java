import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game
{
	private State			currentState;
	private GameInterface	gameInterface;
	private Chip			selectedChip;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState	= initialState;
		this.gameInterface	= gameInterface;
	}// end Game - constructor
	
	public State getCurrentState()
	{
		return currentState;
	}// end getCurrentState
	
	public void handlePositionClicks(MouseEvent event)
	{
		Position clickedPosition = ((Position) event.getSource());
		System.out.println(clickedPosition.getyCoordinate() + ", " + clickedPosition.getxCoordinate());
		
		if (selectedChip != null && clickedPosition.getChip() == null)
		{
			/*
			 * Depending on the type of the selected chip's position and on the clicked position's
			 * location respecting to the selected chip's position, the movement may not be valid
			 */
			moveChip(selectedChip, clickedPosition);
		}// end if
	}// end handlePositionClicks
	
	private void moveChip(Chip chip, Position clickedPosition)
	{
		Position previousPosition;
		
		previousPosition = chip.getPosition();
		
		clickedPosition.setChip(chip);
		previousPosition.setChip(null); // Remove the selected Chip from the previous position
		
		chip.toFront(); // Vital, else the Chip will sometimes get stuck behind the Position and be impossible to be clicked
		
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end moveChip
	
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (selectedChip != null)
			selectedChip.setEffect(null);
		
		System.out.println("I'm a white chip");
		
		selectedChip = clickedChip;
		selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
	}// end handleWhiteChipClicks
	
	public void updateInterface()
	{
		for (Position[] row : currentState.getGrid())
		{
			for (Position p : row)
			{
				gameInterface.getChildren().add(p);
				if (p.getChip() != null)
					gameInterface.getChildren().add(p.getChip());
			}// end foreach
		}// end foreach
	}// end updateInterface
}// end Game - class
