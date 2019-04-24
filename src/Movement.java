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
		this.direction			= getDirection(chip.getPosition(), destinationPos);
		this.oppositeDirection	= getDirection(destinationPos, chip.getPosition());
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
		int	x	= inOppositeDirection ? chip.getPosition().getxCoordinate() : destinationPos.getxCoordinate();
		int	y	= inOppositeDirection ? chip.getPosition().getyCoordinate() : destinationPos.getyCoordinate();
		
		switch (inOppositeDirection ? oppositeDirection : direction)
		{
			case "up":
				return removeIncrementally(y, x, -1, 0);
			case "up-right":
				return removeIncrementally(y, x, -1, +1);
			case "right":
				return removeIncrementally(y, x, 0, +1);
			case "down-right":
				return removeIncrementally(y, x, +1, +1);
			case "down":
				return removeIncrementally(y, x, +1, 0);
			case "down-left":
				return removeIncrementally(y, x, +1, -1);
			case "left":
				return removeIncrementally(y, x, 0, -1);
			case "up-left":
				return removeIncrementally(y, x, -1, -1);
			default:
				System.out.println("Something is wrong");
				break;
		}// end switch direction
		
		return false;
	}// end removeInDirection
	
	private boolean removeIncrementally(int y, int x, int yIncrement, int xIncrement)
	{
		y	+= yIncrement;
		x	+= xIncrement;
		State		currentState	= game.getCurrentState();
		
		Position	removalPosition;
		
		while (y >= 0 && y <= 4 && x >= 0 && x <= 8)
		{
			removalPosition = currentState.getGrid()[y][x];
			if (removalPosition.getChip() != null && removalPosition.getChip().getFill() == deletionColor)
				remove(removalPosition.getChip());
			else
				break;
			y	+= yIncrement;
			x	+= xIncrement;
		}// end while
		
		return true;
	}// end removeIncrementally
	
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
	
	private String getDirection(Position origin, Position destination)
	{
		int	origX	= origin.getxCoordinate();
		int	origY	= origin.getyCoordinate();
		int	destX	= destination.getxCoordinate();
		int	destY	= destination.getyCoordinate();
		
		if (destX == origX && destY == origY - 1)
			return "up";
		if (destX == origX + 1 && destY == origY - 1)
			return "up-right";
		if (destX == origX + 1 && destY == origY)
			return "right";
		if (destX == origX + 1 && destY == origY + 1)
			return "down-right";
		if (destX == origX && destY == origY + 1)
			return "down";
		if (destX == origX - 1 && destY == origY + 1)
			return "down-left";
		if (destX == origX - 1 && destY == origY)
			return "left";
		if (destX == origX - 1 && destY == origY - 1)
			return "up-left";
		
		return "";
	}// end getDirection
}// end Movement - class
