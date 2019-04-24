import javafx.scene.paint.Color;

public class Movement
{
	Game		game;
	Chip		chip;
	Position	destinationPos;
	Color		deletionColor;
	String		direction;
	String		oppositeDirection;
	boolean		captured;
	
	public Movement(Chip chip, Position destinationPos, Game game)
	{
		this.chip				= chip;
		this.destinationPos		= destinationPos;
		this.game				= game;
		this.deletionColor		= chip.getFill() == Color.WHITE ? Color.BLACK : Color.WHITE;
		this.direction			= getDirection();
		this.oppositeDirection	= getOppositeDirection();
	}// end Movement - constructor
	
	public void perform()
	{
		captured = capture();
		moveChip();
	}// end perform
	
	private boolean capture()
	{
		if (hasOppositeColorInAdjacentDirection(false))
			return removeInDirection(false);
		else if (hasOppositeColorInAdjacentDirection(true))
			return removeInDirection(true);
		else
			return false;
	}// end capture
	
	public boolean isValid()
	{
		if (destinationPos.getChip() != null)
			return false;
		if (!isAdjacent())
			return false;
		if (chip.getPosition().getType().equals("weak") && isPlacedDiagonally())
			return false;
		
		return true;
	}// end isValid
	
	private boolean removeInDirection(boolean inOppositeDirection)
	{
		int			x				= inOppositeDirection ? chip.getPosition().getxCoordinate() : destinationPos.getxCoordinate();
		int			y				= inOppositeDirection ? chip.getPosition().getyCoordinate() : destinationPos.getyCoordinate();
		State		currentState	= game.getCurrentState();
		
		Position	removalPosition;
		
		switch (inOppositeDirection ? oppositeDirection : direction)
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
	}// end removeInDirection
	
	private boolean hasOppositeColorInAdjacentDirection(boolean inOppositeDirection)
	{
		int		posX			= inOppositeDirection ? chip.getPosition().getxCoordinate() : destinationPos.getxCoordinate();
		int		posY			= inOppositeDirection ? chip.getPosition().getyCoordinate() : destinationPos.getyCoordinate();
		State	currentState	= game.getCurrentState();
		
		switch (inOppositeDirection ? oppositeDirection : direction)
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
	
	private void remove(Chip chip)
	{
		Position tmpPosition;
		
		tmpPosition = chip.getPosition();
		
		game.getGameInterface().getChildren().remove(chip); // Remove the chip from the GameInterface
		chip.setPosition(null);
		tmpPosition.setChip(null); // Remove the selected Chip from the previous position
	}// end remove
	
	private void moveChip()
	{
		Position previousPosition;
		
		previousPosition = chip.getPosition();
		
		destinationPos.setChip(chip);
		previousPosition.setChip(null); // Remove the selected Chip from the previous position
		
		chip.toFront(); // Vital, else the Chip will sometimes get stuck behind the Position and be impossible to be clicked
	}// end moveChip
	
	private boolean isPlacedDiagonally()
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= destinationPos.getxCoordinate();
		int	posY	= destinationPos.getyCoordinate();
		
		return Math.abs(chipX - posX) == 1 && Math.abs(chipY - posY) == 1;
	}// end isPlacedDiagonally
	
	private boolean isAdjacent()
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= destinationPos.getxCoordinate();
		int	posY	= destinationPos.getyCoordinate();
		
		if (Math.abs(chipX - posX) == 1 && chipY == posY)
			return true;
		
		if (Math.abs(chipY - posY) == 1 && chipX == posX)
			return true;
		
		return Math.abs(chipX - posX) == 1 && Math.abs(chipY - posY) == 1;
	}// end isAdjacent
	
	private String getDirection()
	{
		int	chipX	= chip.getPosition().getxCoordinate();
		int	chipY	= chip.getPosition().getyCoordinate();
		int	posX	= destinationPos.getxCoordinate();
		int	posY	= destinationPos.getyCoordinate();
		
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
	
	private String getOppositeDirection()
	{
		switch (this.direction)
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
	
}// end Movement - class
