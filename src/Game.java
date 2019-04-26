import java.io.Serializable;

import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game implements Serializable
{
	State			currentState;
	GameInterface	gameInterface;
	Chip			selectedChip;
	String			winner;
	
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState	= initialState;
		this.gameInterface	= gameInterface;
		this.winner			= "";
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
	
	private boolean gameIsOver()
	{
		int	numO	= 0;
		int	numX	= 0;
		
		for (int y = 0; y < currentState.grid.length; y++) // Set the Positions in place
		{
			for (int x = 0; x < currentState.grid[y].length; x++)
			{
				numO	+= (currentState.grid[y][x] == 'O' ? 1 : 0);
				numX	+= (currentState.grid[y][x] == 'X' ? 1 : 0);
			}// end for - x
		}// end for - y
		
		if (numO == 0 || numX == 0)
			winner = (numO == 0) ? "user" : "agent";
		
		return numO == 0 || numX == 0;
	}// end gameIsOver
	
	private void alertWinner()
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("We Have a Winner!");
		alert.setHeaderText("And the Winner Is...");
		alert.setContentText(winner);
		alert.showAndWait();
	}// end alertWinner
	
	private void agentMoves()
	{
		State		selectedState	= MinMax.miniMaxEasy(currentState);
		Movement	agentMovement	= new Movement(selectedState.oriY, selectedState.oriX, selectedState.desY, selectedState.desX, currentState);
		agentMovement.perform();
		if (gameIsOver())
		{
			alertWinner();
		}
		else // if(currentState.turn.equals("agent))
		{
			//
		}// end if
	}// end agentMoves
	
	private void deselectSelectedChip()
	{
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end deselectSelectedChip
	
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (!currentState.turn.equals("user")) // If the user is still in turn, he/she cannot change the selected chip
		{
			if (selectedChip != null)
				selectedChip.setEffect(null);
			
			System.out.println("I'm a white chip");
			
			selectedChip = clickedChip;
			selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
		}// end if
	}// end handleWhiteChipClicks
	
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
						gameInterface.getChildren().add(chip);
						break;
					case 'X':
						chip.setStroke(Color.BLACK);
						chip.setFill(Color.WHITE);
						if (currentState.desY == y && currentState.desX == x)
						{
							selectedChip = chip;
							selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
						}// end if
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
