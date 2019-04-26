import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game implements Serializable
{
	State			currentState;
	GameInterface	gameInterface;
	Chip			selectedChip;
	Set<Movement>	walkedPath;
	String			lastDirection;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState	= initialState;
		this.gameInterface	= gameInterface;
		this.walkedPath		= new HashSet<>();
	}// end Game - constructor
	
	public void handleSpaceClicks(MouseEvent event)
	{
		Chip clickedSpace = ((Chip) event.getSource());
		System.out.println(clickedSpace.y + ", " + clickedSpace.x);
		
		if (selectedChip != null)
		{
			Movement userMovement = new Movement(selectedChip.y, selectedChip.x, clickedSpace.y, clickedSpace.x, currentState);
			
			if (userMovement.isValid())
			{
				userMovement.perform();
				
				if (currentState.turn.equals("agent"))
				{
					deselectSelectedChip();
					agentMoves();
				}// end if
				
			}// end if
		}// end if
		
		updateInterface(false);
	}// end handleSpaceClicks
	
	private void agentMoves()
	{
		State		selectedState	= MinMax.miniMaxEasy(currentState);
		Movement	agentMovement	= new Movement(selectedState.oriY, selectedState.oriX, selectedState.desY, selectedState.desX, currentState);
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
		
		// if (!currentState.turn.equals("user")) // If the user is still in turn, he/she cannot change the selected chip
		// {
		if (selectedChip != null)
			selectedChip.setEffect(null);
		
		System.out.println("I'm a white chip");
		
		selectedChip = clickedChip;
		selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
		// }// end if
	}// end handleWhiteChipClicks
	
	public void handleBlackChipClicks(MouseEvent event)
	{
		updateInterface(false);
	}// end handleBlackChipClicks
	
	public void updateInterface(boolean firstTime)
	{
		if (!firstTime) // remove the previous Chips
			for (int i = 0; i < 45; i++)
				gameInterface.getChildren().remove(gameInterface.getChildren().size() - 1);
			
		for (int y = 0; y < currentState.grid.length; y++) // Set the Positions in place
		{
			for (int x = 0; x < currentState.grid[y].length; x++)
			{
				Chip chip = new Chip(y, x);
				
				switch (currentState.grid[y][x])
				{
					case 'O':
						chip.setStroke(Color.BLACK);
						chip.setFill(Color.BLACK);
						chip.setOnMouseClicked(this::handleBlackChipClicks);
						gameInterface.getChildren().add(chip);
						break;
					case 'X':
						chip.setStroke(Color.BLACK);
						chip.setFill(Color.WHITE);
						chip.setOnMouseClicked(this::handleWhiteChipClicks);
						gameInterface.getChildren().add(chip);
						break;
					default:
						chip.setFill(Color.TRANSPARENT);
						chip.setOnMouseClicked(this::handleSpaceClicks);
						gameInterface.getChildren().add(chip);
						break;
				}// end switch currentState.grid[y][x]
			}// end for x
		}// end for - i
	}// end updateInterface
}// end Game - class
