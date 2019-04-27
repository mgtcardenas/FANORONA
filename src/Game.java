import java.io.Serializable;

import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * @author Marco Cárdenas
 *
 *         This class is the controller of the whole application, it manages the interaction
 *         between the View (GameInterface) and the Model (the rest of the classes)
 */
public class Game implements Serializable
{
	State			currentState;  // The state of the game
	GameInterface	gameInterface; // The View
	Chip			selectedChip;  // The chip selected by the user
	String			winner;        // The winner of the game
	
	/**
	 * To create a game a game state and a game interface are needed
	 * 
	 * @param initialState  the state the game begins with
	 * @param gameInterface the view of the application
	 */
	public Game(State initialState, GameInterface gameInterface)
	{
		this.currentState	= initialState;
		this.gameInterface	= gameInterface;
		this.winner			= "";
	}// end Game - constructor
	
	/**
	 * Manage the set of actions to take when the user clicks on a position that has no chip
	 * 
	 * @param event the clicked mouse event
	 */
	public void handleSpaceClicks(MouseEvent event)
	{
		Chip clickedSpace = ((Chip) event.getSource());
		
		if (selectedChip != null)
		{
			Movement userMovement = new Movement(selectedChip.y, selectedChip.x, clickedSpace.y, clickedSpace.x, currentState);
			if (userMovement.isValid())
			{
				userMovement.perform();
				if (gameIsOver())
					alertWinner();
				else if (currentState.turn.equals("agent"))
				{
					deselectSelectedChip();
					agentMoves();
				}// end if - else
			}// end if
		}// end if
		
		updateInterface(false);
	}// end handleSpaceClicks
	
	/**
	 * The movement of the agent, determined by the MinMax algorithm
	 */
	private void agentMoves()
	{
		State		selectedState	= MinMax.minMaxEasy(currentState);
		Movement	agentMovement	= new Movement(selectedState.oriY, selectedState.oriX, selectedState.desY, selectedState.desX, currentState);
		agentMovement.perform();
		
		if (gameIsOver())
			alertWinner();
		else if (currentState.turn.equals("agent-playing"))
			agentMoves(); // Recursively keep playing
	}// end agentMoves
	
	/**
	 * Remove the effect from the selected chip and stop having a selected chip
	 */
	private void deselectSelectedChip()
	{
		this.selectedChip.setEffect(null); // Deselect the Selected Chip
		this.selectedChip = null;
	}// end deselectSelectedChip
	
	/**
	 * Manage the set of actions to take when the user clicks on one of his/her chips
	 * 
	 * @param event the clicked mouse event
	 */
	public void handleWhiteChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		
		if (!currentState.turn.equals("user-playing")) // If the user is still in turn, he/she cannot change the selected chip
		{
			if (selectedChip != null)
				selectedChip.setEffect(null);
			
			selectedChip = clickedChip;
			selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
		}// end if
	}// end handleWhiteChipClicks
	
	/**
	 * Update the View with the Model. First time boolean is important because the elements
	 * are removed entirely. If we remove the 45 elements before they are there, we will
	 * inevitably remove some of the lines of the board from the View
	 * 
	 * @param firstTime whether it's the fist time the View is updated
	 */
	public void updateInterface(boolean firstTime)
	{
		if (!firstTime) // remove the previous Chips
			for (int i = 0; i < 45; i++)
				gameInterface.getChildren().remove(gameInterface.getChildren().size() - 1);
			
		for (int y = 0; y < currentState.grid.length; y++) // set the positions in place
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
						if (currentState.desY == y && currentState.desX == x) // the chip was part of a connected play
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
				}// end switch
			}// end for x
		}// end for - i
	}// end updateInterface
	
	/**
	 * Show an information alert dialog to inform the user who is the winner
	 */
	private void alertWinner()
	{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("We Have a Winner!");
		alert.setHeaderText("And the Winner Is...");
		alert.setContentText(winner);
		alert.showAndWait();
	}// end alertWinner
	
	/**
	 * Determine if the game should stop by realizing if there are no more chips of either color
	 * 
	 * @return true if the game ended, false if it has not
	 */
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
			winner = (numO == 0) ? "The User" : "The Agent";
		
		return numO == 0 || numX == 0;
	}// end gameIsOver
}// end Game - class
