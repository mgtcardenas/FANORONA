package failed;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game implements Serializable
{
	private State					currentState;
	private transient GameInterface	gameInterface;
	private Chip					selectedChip;
	private boolean					userTurn;
	private Set<Position>			walkedPath;
	private String					lastDirection;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState	= initialState;
		this.gameInterface	= gameInterface;
		this.walkedPath		= new HashSet<>();
		this.userTurn		= false;
		
		initialState.setGame(this);
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
					userTurn = true;
				else // Agent/Black Chip's Turn
				{
					walkedPath.clear();
					lastDirection = "";
					deselectSelectedChip();
					userTurn = false;
					agentMoves();
				}// end if - else
			}// end if
		}// end if
	}// end handleSpaceClicks
	
	private void agentMoves()
	{
		State		selectedState	= MinMax.miniMaxEasy(currentState);
		Movement	agentMovement	= new Movement(currentState.getChip(), currentState.getPosition(), this);
		lastDirection = agentMovement.getDirection();
		agentMovement.perform();
		// if (agentMovement.didCapture() && agentMovement.hasNextPossibleCaptures())
		// {
		// walkedPath.add(currentState.getChip().getPosition());
		// agentMoves(); // recursively keep playing
		// }// end if
		// else
		// {
		// walkedPath.clear();
		// lastDirection = "";
		// userTurn = true;
		// }// end if
	}// end agentMoves
	
	private void deselectSelectedChip()
	{
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end deselectSelectedChip
	
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (!userTurn) // If the user is still in turn, he/she cannot change the selected chip
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
	}// end updateInterface
}// end Game - class
