import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Game
{
	private State			currentState;
	private GameInterface	gameInterface;
	private Chip			selectedChip;
	private boolean			isUserTurn;
	
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
			if (isValidMovement(selectedChip, clickedPosition))
			{
				Color deletionColor = selectedChip.getFill() == Color.WHITE ? Color.BLACK : Color.WHITE;
				
				/*
				 * if(capture(direction)
				 * {
				 * thereMoreMovements = true;
				 * put previous Position in walkedPath stack
				 * }//end if
				 */
				
				capture(selectedChip, clickedPosition, deletionColor);
				moveChip(selectedChip, clickedPosition);
				
				if (false) // TODO: if (there are more movements) also, there are only more movements if you captured
				{
					this.selectedChip.setEffect(null); // Deselect the Selected Chip
					this.selectedChip = null;
				}// end if
			}// end if
		}// end if
	}// end handlePositionClicks
	
	private boolean capture(Chip chip, Position clickedPosition, Color deletionColor)
	{
		String direction = getDirection(chip, clickedPosition);
		System.out.println(direction);
		
		if (hasOppositeColorInAdjacentDirection(clickedPosition, direction, deletionColor))
			return removeIndDirection(clickedPosition, direction, deletionColor);
		else if (hasOppositeColorInAdjacentDirection(chip.getPosition(), getOppositeDirection(direction), deletionColor))
			return removeIndDirection(chip.getPosition(), getOppositeDirection(direction), deletionColor);
		else
			return false;
	}// end capture
	
	private boolean removeIndDirection(Position position, String direction, Color deletionColor)
	{
		int			x	= position.getxCoordinate();
		int			y	= position.getyCoordinate();
		
		Position	removalPosition;
		
		switch (direction)
		{
			case "up":
				y--; // We are sure that this position has a black chip
				
				while (y != -1)
				{
					removalPosition = currentState.getGrid()[y--][x];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "up-right":
				y--; // We are sure that this position has a black chip
				x++; // We are sure that this position has a black chip
				
				while (y != -1 && x != 9)
				{
					removalPosition = currentState.getGrid()[y--][x++];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "right":
				x++; // We are sure that this position has a black chip
				
				while (x != 9)
				{
					removalPosition = currentState.getGrid()[y][x++];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "down-right":
				y++; // We are sure that this position has a black chip
				x++; // We are sure that this position has a black chip
				
				while (y != 5 && x != 9)
				{
					removalPosition = currentState.getGrid()[y++][x++];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "down":
				y++; // We are sure that this position has a black chip
				
				while (y != 5)
				{
					removalPosition = currentState.getGrid()[y++][x];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "down-left":
				y++; // We are sure that this position has a black chip
				x--; // We are sure that this position has a black chip
				
				while (y != 5 && x != -1)
				{
					removalPosition = currentState.getGrid()[y++][x--];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "left":
				x--; // We are sure that this position has a black chip
				
				while (x != -1)
				{
					removalPosition = currentState.getGrid()[y][x--];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			case "up-left":
				y--; // We are sure that this position has a black chip
				x--; // We are sure that this position has a black chip
				
				while (y != -1 && x != -1)
				{
					removalPosition = currentState.getGrid()[y--][x--];
					if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
						remove(removalPosition.getChip());
					else
						break;
				}// end while
				return true;
			
			default:
				System.out.println("Something is wrong");
				break;
		}// end switch direction
		
		return false;
	}// end removeIndDirection
	
	private void remove(Chip chip)
	{
		Position tmpPosition;
		
		tmpPosition = chip.getPosition();
		gameInterface.getChildren().remove(chip); // Remove the chip from the GameInterface
		chip.setPosition(null);
		tmpPosition.setChip(null); // Remove the selected Chip from the previous position
	}// end remove
	
	private boolean hasOppositeColorInAdjacentDirection(Position clickedPosition, String direction, Color deletionColor)
	{
		int	posX	= clickedPosition.getxCoordinate();
		int	posY	= clickedPosition.getyCoordinate();
		
		switch (direction)
		{
			case "up":
				
				if ((posY - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY - 1][posX].getChip() != null)
					return currentState.getGrid()[posY - 1][posX].getChip().getFill() == deletionColor;
				break;
			
			case "up-right":
				if ((posY - 1) == -1 || (posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY - 1][posX + 1].getChip() != null)
					return currentState.getGrid()[posY - 1][posX + 1].getChip().getFill() == deletionColor;
				break;
			
			case "right":
				if ((posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY][posX + 1].getChip() != null)
					return currentState.getGrid()[posY][posX + 1].getChip().getFill() == deletionColor;
				break;
			
			case "down-right":
				if ((posY + 1) == 5 || (posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY + 1][posX + 1].getChip() != null)
					return currentState.getGrid()[posY + 1][posX + 1].getChip().getFill() == deletionColor;
				break;
			
			case "down":
				if ((posY + 1) == 5) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY + 1][posX].getChip() != null)
					return currentState.getGrid()[posY + 1][posX].getChip().getFill() == deletionColor;
				break;
			
			case "down-left":
				if ((posY + 1) == 5 || (posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY + 1][posX - 1].getChip() != null)
					return currentState.getGrid()[posY + 1][posX - 1].getChip().getFill() == deletionColor;
				break;
			
			case "left":
				if ((posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY][posX - 1].getChip() != null)
					return currentState.getGrid()[posY][posX - 1].getChip().getFill() == deletionColor;
				break;
			
			case "up-left":
				if ((posY - 1) == -1 || (posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.getGrid()[posY - 1][posX - 1].getChip() != null)
					return currentState.getGrid()[posY - 1][posX - 1].getChip().getFill() == deletionColor;
				break;
			
			default:
				System.out.println("Something wrong happened");
				break;
		}// end switch direction
		
		return false;
	}// end hasOppositeColorInAdjacentDirection
	
	private String getOppositeDirection(String direction)
	{
		switch (direction)
		{
			case "up":
				return "down";
			case "up-right":
				return "down-left";
			case "right":
				return "left";
			case "down-right":
				return "up-left";
			case "down":
				return "up";
			case "down-left":
				return "up-right";
			case "left":
				return "right";
			case "up-left":
				return "down-right";
			default:
				System.out.println("Something is wrong");
				break;
		}// end switch
		
		return "";
	}// end getOppositeDirection
	
	private String getDirection(Chip chip, Position clickedPosition)
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= clickedPosition.getxCoordinate();
		int	posY	= clickedPosition.getyCoordinate();
		
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
	
	private boolean isValidMovement(Chip chip, Position clickedPosition)
	{
		// TODO: if (walkedPath.peek == clickedPosition) return false;
		
		if (!isAdjacent(chip, clickedPosition))
			return false;
		
		if (chip.getPosition().getType().equals("weak") && isPlacedDiagonally(chip, clickedPosition))
			return false;
		
		return true;
	}// end isValidMovement
	
	private boolean isPlacedDiagonally(Chip chip, Position clickedPosition)
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= clickedPosition.getxCoordinate();
		int	posY	= clickedPosition.getyCoordinate();
		
		return Math.abs(chipX - posX) == 1 && Math.abs(chipY - posY) == 1;
	}// end isPlacedDiagonally
	
	private boolean isAdjacent(Chip chip, Position clickedPosition)
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= clickedPosition.getxCoordinate();
		int	posY	= clickedPosition.getyCoordinate();
		
		if (Math.abs(chipX - posX) == 1 && chipY == posY)
			return true;
		
		if (Math.abs(chipY - posY) == 1 && chipX == posX)
			return true;
		
		return Math.abs(chipX - posX) == 1 && Math.abs(chipY - posY) == 1;
	}// end isAdjacent
	
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
		
		if (!isUserTurn)
		{
			if (selectedChip != null)
				selectedChip.setEffect(null);
			
			System.out.println("I'm a white chip");
			
			selectedChip = clickedChip;
			selectedChip.setEffect(new DropShadow(10, 0f, 0d, Color.DEEPSKYBLUE));
		}// end if
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
