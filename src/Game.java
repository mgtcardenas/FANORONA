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
	private boolean			userIsStillInTurn;
	private Set<Position>	walkedPath;
	private String			lastDirection;
	private State			selectedState;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState		= initialState;
		this.gameInterface		= gameInterface;
		this.walkedPath			= new HashSet<>();
		this.userIsStillInTurn	= false;
	}// end Game - constructor
	
	public State getCurrentState()
	{
		return currentState;
	}// end getCurrentState
	
	public GameInterface getGameInterface()
	{
		return gameInterface;
	}// end getGameInterface
	
	public Set<Position> getWalkedPath()
	{
		return walkedPath;
	}// end getWalkedPath
	
	public String getLastDirection()
	{
		return lastDirection;
	}// end getLastDirection
	
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
				lastDirection = userMovement.getDirection();
				userMovement.perform();
				
				if (userMovement.didCapture() && userMovement.hasNextPossibleCaptures()) // User keeps playing
					userIsStillInTurn = true;
				else // Agent/Black Chip's Turn
				{
					walkedPath.clear();
					deselectSelectedChip();
					userIsStillInTurn = false;
					agentMoves();
				}// end if - else
			}// end if
		}// end if
	}// end handlePositionClicks
	
	private void agentMoves()
	{
		selectedState = MinMax.miniMaxEasy(currentState);
		Movement agentMovement = new Movement(selectedState.getChip(), selectedState.getPosition(), this);
		agentMovement.perform();
		if (agentMovement.didCapture() && agentMovement.hasNextPossibleCaptures())
			userIsStillInTurn = false;
		else
			userIsStillInTurn = true;
	}// end agentMoves
	
	private void deselectSelectedChip()
	{
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end deselectSelectedChip
	
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (!userIsStillInTurn) // If the user is still in turn, he/she cannot change the selected chip
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
