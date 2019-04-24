import java.util.HashSet;
import java.util.Set;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game
{
	private State			currentState;
	private GameInterface	gameInterface;
	private Chip			selectedChip;
	private boolean			isStillUserTurn;
	private Set<Position>	walkedPath;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState		= initialState;
		this.gameInterface		= gameInterface;
		this.walkedPath			= new HashSet<>();
		this.isStillUserTurn	= false;
	}// end Game - constructor
	
	public State getCurrentState()
	{
		return currentState;
	}// end getCurrentState
	
	public GameInterface getGameInterface()
	{
		return gameInterface;
	}// end getGameInterface
	
	public void handlePositionClicks(MouseEvent event)
	{
		Position clickedPosition = ((Position) event.getSource());
		System.out.println(clickedPosition.getyCoordinate() + ", " + clickedPosition.getxCoordinate());
		
		if (selectedChip != null && !walkedPath.contains(clickedPosition))
		{
			Movement userMovement = new Movement(selectedChip, clickedPosition, this);
			
			if (userMovement.isValid())
			{
				walkedPath.add(selectedChip.getPosition());
				userMovement.perform();
				
				if (userMovement.captured && hasNextPossibleCaptures(selectedChip))
				{
					// User keeps playing
				}// end if
				else
				{
					walkedPath.clear();
					deselectSelectedChip();
					// Black Chip's Turn
				}
			}// end if
		}// end if
	}// end handlePositionClicks
	
	private boolean hasNextPossibleCaptures(Chip chip)
	{
		return true;
	}// end hasNextPossibleCaptures
	
	private void deselectSelectedChip()
	{
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end deselectSelectedChip
	
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (!isStillUserTurn)
		{
			if (selectedChip != null)
				selectedChip.setEffect(null);
			
			System.out.println("I'm a white chip");
			
			selectedChip = clickedChip;
			selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
		}// end if
	}// end handleWhiteChipClicks
	
	public void initializeInterface()
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
	}// end initializeInterface
}// end Game - class
