import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Movement implements Serializable
{
	int		oriY;
	int		oriX;
	int		desY;
	int		desX;
	String	direction;
	String	oppositeDirection;
	State	currentState;
	boolean	captured;
	
	public Movement(int oriY, int oriX, int desY, int desX, State currentState)
	{
		this.oriY				= oriY;
		this.oriX				= oriX;
		this.desY				= desY;
		this.desX				= desX;
		this.currentState		= currentState;
		this.direction			= getDirection(oriY, oriX, desY, desX);
		this.oppositeDirection	= getDirection(desY, desX, oriY, oriX);
	}// end Movement - constructor
	
	public boolean hasNextPossibleCaptures()
	{
		if (hasApproachCapture())
			return true;
		
		if (hasWithdrawalCapture())
			return true;
		
		return false;
	}// end hasNextPossibleCaptures
	
	private void removeConsecutiveDirection(List<Movement> possibleMovements)
	{
		for (int i = possibleMovements.size() - 1; i >= 0; i--)
		{
			Movement	mov			= possibleMovements.get(i);
			String		direction	= getDirection(mov.oriY, mov.oriX, mov.desY, mov.desX);
			if (direction.equals(currentState.lastDirection))
				possibleMovements.remove(i);
		}// end for - i
	}// end removeConsecutiveDirection
	
	private void removePreviousPositions(List<Movement> possibleMovements)
	{
		for (int i = possibleMovements.size() - 1; i >= 0; i--)
		{
			Movement possibleMov = possibleMovements.get(i);
			for (Movement pastMov : currentState.walkedPath)
			{
				if (possibleMov.desY == pastMov.oriY && possibleMov.desX == pastMov.oriX)
					possibleMovements.remove(i);
			}// end foreach
		}// end for - i
	}// end removePreviousPositions
	
	private boolean hasApproachCapture()
	{
		List<Movement> possibleMovements = new LinkedList<>();
		
		for (int posY = desY - 2; posY <= desY + 2; posY += 2) // Check for all of opponent's chips on outer square of chip
			for (int posX = desX - 2; posX <= desX + 2; posX += 2) // Use desY & desX because we already moved by the time we ask this
				if (posY >= 0 && posY <= 4 && posX >= 0 && posX <= 8 && currentState.grid[posY][posX] != ' ') // There's no one there
					if (currentState.grid[posY][posX] == (currentState.grid[desY][desX] == 'X' ? 'O' : 'X')) // It's the opponent
						possibleMovements.add(new Movement(desY, desX, desY + (posY - desY) / 2, desX + (posX - desX) / 2, currentState));
					
		removePreviousPositions(possibleMovements); // You can't go back to a previous position
		removeConsecutiveDirection(possibleMovements);        // It is not permitted to move twice consecutively in the same direction
		
		if ((desY + desX) % 2 != 0) // It's a weak position
		{
			for (Movement movement : possibleMovements)
			{
				if (Math.abs(movement.desX - desX) == 1 && movement.desY == desY && currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
				if (Math.abs(movement.desY - desY) == 1 && movement.desX == desX && currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
			}// end foreach
		}
		else
		{
			for (Movement movement : possibleMovements)
				if (currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
		}// end if - else
		
		return false;
	}// end hasApproachCapture
	
	private boolean hasWithdrawalCapture()
	{
		List<Movement> possibleMovements = new LinkedList<>();
		
		for (int posY = desY - 1; posY <= desY + 1; posY++) // Check for all of opponent's chips on outer square of chip
			for (int posX = desX - 1; posX <= desX + 1; posX++) // Use desY & desX because we already moved by the time we ask this
				if (posY >= 0 && posY <= 4 && posX >= 0 && posX <= 8 && currentState.grid[posY][posX] != ' ') // Don't select out of bounds
					if (currentState.grid[posY][posX] == (currentState.grid[desY][desX] == 'X' ? 'O' : 'X')) // It's the opponent
						if ((desY + (posY - desY) * -1) >= 0 && (desY + (posY - desY) * -1) <= 4 && (desX + (posX - desX) * -1) >= 0 && (desX + (posX - desX) * -1) <= 8) // Don't select out of bounds
							possibleMovements.add(new Movement(desY, desX, desY + (posY - desY) * -1, desX + (posX - desX) * -1, currentState));
						
		removePreviousPositions(possibleMovements); // You can't go back to a previous position
		
		if ((desY + desX) % 2 != 0) // It's a weak position
		{
			for (Movement movement : possibleMovements)
			{
				if (Math.abs(movement.desX - desX) == 1 && movement.desY == desY && currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
				if (Math.abs(movement.desY - desY) == 1 && movement.desX == desX && currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
			}// end foreach
		}
		else
		{
			for (Movement movement : possibleMovements)
				if (currentState.grid[movement.desY][movement.desX] == ' ')
					return true;
		}// end if - else
		
		return false;
	}// end hasWithdrawalCapture
	
	public void perform()
	{
		captured = capture();
		moveChip();
		currentState.oriY	= oriY;
		currentState.oriX	= oriX;
		currentState.desY	= desY;
		currentState.desX	= desX;
		
		char symbol = currentState.grid[desY][desX]; // desY & desX because chip already moved
		
		if (captured && hasNextPossibleCaptures())
		{
			currentState.turn = (symbol == 'O') ? "agent-playing" : "user-playing"; // keeps playing
			currentState.walkedPath.add(this); // add the movement to the walked path
			currentState.lastDirection = direction; // add the last direction from the movement to the current state
		}
		else
		{
			currentState.walkedPath.clear(); // clear the walked path
			currentState.lastDirection	= ""; // clear the last direction
			currentState.turn			= (symbol == 'O') ? "user" : "agent"; // change turns
		}// end if - else
	}// end perform
	
	private boolean capture()
	{
		if (hasOpponentInAdjacentDirection(false))
			return removeInDirection(false);
		else if (hasOpponentInAdjacentDirection(true))
			return removeInDirection(true);
		else
			return false;
	}// end capture
	
	public boolean isValid()
	{
		if (currentState.grid[desY][desX] != ' ')
			return false;
		
		if (!isAdjacent())
			return false;
		
		if ((oriY + oriX) % 2 != 0 && isPlacedDiagonally())
			return false;
		
		if (currentState.lastDirection.equals(direction))
			return false;
		
		for (Movement pastMov : currentState.walkedPath)
			if (desY == pastMov.oriY && desX == pastMov.oriX)
				return false;
			
		return true;
	}// end isValid
	
	private boolean removeInDirection(boolean inOppositeDirection)
	{
		int	x	= inOppositeDirection ? oriX : desX;
		int	y	= inOppositeDirection ? oriY : desY;
		
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
		
		while (y >= 0 && y <= 4 && x >= 0 && x <= 8)
		{
			if (currentState.grid[y][x] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X'))
				currentState.grid[y][x] = ' ';
			else
				break;
			y	+= yIncrement;
			x	+= xIncrement;
		}// end while
		
		return true;
	}// end removeIncrementally
	
	private boolean hasOpponentInAdjacentDirection(boolean inOppositeDirection)
	{
		int	posX	= inOppositeDirection ? oriX : desX;
		int	posY	= inOppositeDirection ? oriY : desY;
		
		switch (inOppositeDirection ? oppositeDirection : direction)
		{
			case "up":
				if ((posY - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.grid[posY - 1][posX] != ' ')
					return currentState.grid[posY - 1][posX] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "up-right":
				if ((posY - 1) == -1 || (posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.grid[posY - 1][posX + 1] != ' ')
					return currentState.grid[posY - 1][posX + 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "right":
				if ((posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.grid[posY][posX + 1] != ' ')
					return currentState.grid[posY][posX + 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "down-right":
				if ((posY + 1) == 5 || (posX + 1) == 9) // It's out of bounds
					return false;
				else if (currentState.grid[posY + 1][posX + 1] != ' ')
					return currentState.grid[posY + 1][posX + 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "down":
				if ((posY + 1) == 5) // It's out of bounds
					return false;
				else if (currentState.grid[posY + 1][posX] != ' ')
					return currentState.grid[posY + 1][posX] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "down-left":
				if ((posY + 1) == 5 || (posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.grid[posY + 1][posX - 1] != ' ')
					return currentState.grid[posY + 1][posX - 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "left":
				if ((posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.grid[posY][posX - 1] != ' ')
					return currentState.grid[posY][posX - 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			case "up-left":
				if ((posY - 1) == -1 || (posX - 1) == -1) // It's out of bounds
					return false;
				else if (currentState.grid[posY - 1][posX - 1] != ' ')
					return currentState.grid[posY - 1][posX - 1] == (currentState.grid[oriY][oriX] == 'X' ? 'O' : 'X');
				break;
			
			default:
				System.out.println("Something wrong happened");
				break;
		}// end switch direction
		
		return false;
	}// end hasOpponentInAdjacentDirection
	
	private void moveChip()
	{
		currentState.grid[desY][desX]	= currentState.grid[oriY][oriX];
		currentState.grid[oriY][oriX]	= ' ';
	}// end moveChip
	
	private boolean isPlacedDiagonally()
	{
		return Math.abs(oriX - desX) == 1 && Math.abs(oriY - desY) == 1;
	}// end isPlacedDiagonally
	
	private boolean isAdjacent()
	{
		if (Math.abs(oriX - desX) == 1 && oriY == desY)
			return true;
		if (Math.abs(oriY - desY) == 1 && oriX == desX)
			return true;
		
		return Math.abs(oriX - desX) == 1 && Math.abs(oriY - desY) == 1;
	}// end isAdjacent
	
	private String getDirection(int oriY, int oriX, int desY, int desX)
	{
		if (desX == oriX && desY == oriY - 1)
			return "up";
		if (desX == oriX + 1 && desY == oriY - 1)
			return "up-right";
		if (desX == oriX + 1 && desY == oriY)
			return "right";
		if (desX == oriX + 1 && desY == oriY + 1)
			return "down-right";
		if (desX == oriX && desY == oriY + 1)
			return "down";
		if (desX == oriX - 1 && desY == oriY + 1)
			return "down-left";
		if (desX == oriX - 1 && desY == oriY)
			return "left";
		if (desX == oriX - 1 && desY == oriY - 1)
			return "up-left";
		
		return "";
	}// end getDirection
}// end Movement - class
