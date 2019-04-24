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
					userIsStillInTurn = true;
				}// end if
				else
				{
					walkedPath.clear();
					deselectSelectedChip();
					userIsStillInTurn = false;
					// Black Chip's Turn
				}
			}// end if
		}// end if
	}// end handlePositionClicks
	
	private boolean hasNextPossibleCaptures(Chip chip)
	{
		// int x = chip.getPosition().getxCoordinate();
		// int y = chip.getPosition().getyCoordinate();
		// Color oppositeColor = chip.getFill() == Color.WHITE ? Color.BLACK : Color.WHITE;
		// List<String> directions = new LinkedList<>();
		//
		// // TODO: También, qué pasa si te quedaste adyacente a una ficha y te puedes mover en direccion contraria cuando esa dirección no está en el walkedPath
		//
		// for (int posY = y - 2; posY <= y + 2; posY += 2)
		// {
		// for (int posX = x - 2; posX <= x + 2; posX += 2)
		// {
		// if (y >= 0 && y <= 4 && x >= 0 && x <= 8 && currentState.getGrid()[y][x].getChip() != null)
		// if (currentState.getGrid()[y][x].getChip().getFill() == oppositeColor)
		// directions.add(getDirection(chip, currentState.getGrid()[y][x]));
		// }// end for - posX
		// }// end for - posY
		
		/*
		 * Para cada direccion, si no hay
		 */
		
		return true;
	}// end hasNextPossibleCaptures
	
	private String getDirection(Chip chip, Position position)
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= position.getxCoordinate();
		int	posY	= position.getyCoordinate();
		
		if (posX == chipX && posY == chipY - 1)
			return "up";
		if (posX == chipX + 1 && posY == chipY - 1)
			return "up-right";
		if (posX == chipX + 1 && posY == chipY)
			return "right";
		if (posX == chipX + 1 && posY == chipY + 1)
			return "down-right";
		if (posX == chipX && posY == chipY + 1)
			return "down";
		if (posX == chipX - 1 && posY == chipY + 1)
			return "down-left";
		if (posX == chipX - 1 && posY == chipY)
			return "left";
		if (posX == chipX - 1 && posY == chipY - 1)
			return "up-left";
		
		return "";
	}// end getDirection
	
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
