import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marco Cárdenas
 *
 *         A class that represents the individual movement a player (agent/user) may perform.
 *         This movement has a state which it will transform whenever the movement is performed.
 *         This class also has methods to know whether a movement is valid, it has next possible
 *         captures (so the player that performed it should keep playing), etc.
 */
public class Movement implements Serializable
{
	int		oriY;              // the y coordinate from which the movement is initiated (should have a chip)
	int		oriX;              // the x coordinate from which the movement is initiated (should have a chip)
	int		desY;              // the y coordinate in which the movement ends (should be empty)
	int		desX;              // the x coordinate in which the movement ends (should be empty)
	String	direction;         // the general direction this movement has represented as a String
	String	oppositeDirection; // the general opposite direction this movement has represented as a String
	State	currentState;      // the current state of the game this movement happens on
	boolean	captured;          // whether this movement captured an opponent's chip
	
	/**
	 * To create a Movement object only two pairs of coordinates and a state of the game ar needed
	 * 
	 * @param oriY         the y coordinate from which the movement is initiated (should have a chip)
	 * @param oriX         the x coordinate from which the movement is initiated (should have a chip)
	 * @param desY         the y coordinate in which the movement ends (should be empty)
	 * @param desX         the x coordinate in which the movement ends (should be empty)
	 * @param currentState the state of a game
	 */
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
	
	/**
	 * Whether or not this movement has next possible captures.
	 * Should not be used until the movement was performed
	 * 
	 * @return true if there are next possible captures, false if there are not
	 */
	public boolean hasNextPossibleCaptures()
	{
		if (hasApproachCapture())
			return true;
		
		if (hasWithdrawalCapture())
			return true;
		
		return false;
	}// end hasNextPossibleCaptures
	
	/**
	 * Modifies a list of movements so that it doesn't contain a movement
	 * in the same previous direction
	 * 
	 * @param possibleMovements a list of possible movements
	 */
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
	
	/**
	 * Modifies a list of movements so that it doesn't contain a movement
	 * if it will put the chip on a previous position of the connected play
	 * of a player since this is not permitted by the rules
	 * 
	 * @param possibleMovements a list of possible movements
	 */
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
	
	/**
	 * Whether the movement could capture an opponent's chip by moving 'forward'
	 * in other words, by approaching it
	 * 
	 * @return true if the movement could capture by approach, false if it could not
	 */
	private boolean hasApproachCapture()
	{
		List<Movement> possibleMovements = new LinkedList<>();
		
		for (int posY = desY - 2; posY <= desY + 2; posY += 2) // Check for all of opponent's chips on outer square of chip
			for (int posX = desX - 2; posX <= desX + 2; posX += 2) // Use desY & desX because we already moved by the time we ask this
				if (posY >= 0 && posY <= 4 && posX >= 0 && posX <= 8 && currentState.grid[posY][posX] != ' ') // There's no one there
					if (currentState.grid[posY][posX] == (currentState.grid[desY][desX] == 'X' ? 'O' : 'X')) // It's the opponent
						possibleMovements.add(new Movement(desY, desX, desY + (posY - desY) / 2, desX + (posX - desX) / 2, currentState));
					
		removePreviousPositions(possibleMovements); // You can't go back to a previous position
		removeConsecutiveDirection(possibleMovements); // It is not permitted to move twice consecutively in the same direction
		
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
	
	/**
	 * Whether the movement could capture an opponent's chip by moving 'away'
	 * in other words, by withdrawing from it
	 * 
	 * @return true if the movement could capture by withdrawal, false if it could not
	 */
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
	
	/**
	 * Trigger this movements consequences for the current state of the game it has,
	 * thus transforming it. Also, if it's the case, change whose turn is it
	 * and reset the attributes that help determine if a movement is valid such
	 * as the walkedPath or the direction
	 */
	public void perform()
	{
		captured = capture();
		moveChip();
		currentState.oriY			= oriY;
		currentState.oriX			= oriX;
		currentState.desY			= desY;
		currentState.desX			= desX;
		
		currentState.lastDirection	= direction; // add the last direction from the movement to the current state, It's important to calculating next possible captures
		currentState.walkedPath.add(this); // add the movement to the walked path. It's important to calculating next possible captures
		
		char symbol = currentState.grid[desY][desX]; // desY & desX because chip already moved
		
		if (captured && hasNextPossibleCaptures())
		{
			currentState.turn = (symbol == 'O') ? "agent-playing" : "user-playing"; // keeps playing
		}
		else
		{
			currentState.walkedPath.clear(); // clear the walked path
			currentState.lastDirection	= ""; // clear the last direction
			currentState.turn			= (symbol == 'O') ? "user" : "agent"; // change turns
		}// end if - else
	}// end perform
	
	/**
	 * If it is possible, remove all the opponent' connected chips
	 * in the fashion described by the rules of the game
	 * 
	 * @return true if at least one chip was removed, false if no chip was removed
	 */
	private boolean capture()
	{
		if (hasOpponentInAdjacentDirection(false))
			return removeInDirection(false);
		else if (hasOpponentInAdjacentDirection(true))
			return removeInDirection(true);
		else
			return false;
	}// end capture
	
	/**
	 * Determines whether this movement is valid or not. Checks
	 * whether the destination coordinates contain no chip and they are
	 * adjacent to the origin coordinates. Whether the movement will move
	 * through the drawn lines. Whether this movement is not in the same
	 * previous direction or goes to a previous position and finally
	 * whether the move should be connected, but it is not
	 * 
	 * @return true if the movement is valid, false if it is not
	 */
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
			
		if (currentState.turn.equals("agent-playing") && isNotConnected())
			return false;
		
		if (currentState.turn.equals("user-playing") && isNotConnected())
			return false;
		
		return true;
	}// end isValid
	
	/**
	 * If a player should keep moving the same chip because it is still playing,
	 * 
	 * @return true if it tried to move another chip, false if it did not try to move another chip
	 */
	private boolean isNotConnected()
	{
		return (currentState.desY != oriY || currentState.desX != oriX);
	}// end isNotConnected
	
	/**
	 * Remove all the opponent's chips in a direction so long as they are connected/ adjacent
	 * to each other. This method uses the boolean inOppositeDirection so another function
	 * with the same functionality need not be written.
	 * 
	 * @param  inOppositeDirection whether the chips should be removed in the opposite direction
	 * @return                     true if it removed at least one chip, false if it did not
	 */
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
	
	/**
	 * Remove all the opponent's chips in an incremental pattern described by the x & y
	 * coordinates and the increments. This method was written outside removeInDirection
	 * in order to be DRY (Don't Repeat Yourself)
	 * 
	 * @param  y          the y coordinate from which the chips are to be removed
	 * @param  x          the x coordinate from which the chips are to be removed
	 * @param  yIncrement the way the y coordinate should increment (should be between -1 and 1)
	 * @param  xIncrement the way the x coordinate should increment (should be between -1 and 1)
	 * @return            true if it removed at least one chip, false if it did not
	 */
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
	
	/**
	 * Determine whether there exists an opponent's chip in an adjacent position.
	 * It can also check in an opposite direction in order to be DRY (Don't Repeat Yourself)
	 * 
	 * @param  inOppositeDirection whether to check in the opposite direction or not
	 * @return                     true if it has an opponent's chip in an adjacent position, false if it does not
	 */
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
	
	/**
	 * Simply replace the destination with the origin and make the origin empty
	 */
	private void moveChip()
	{
		currentState.grid[desY][desX]	= currentState.grid[oriY][oriX];
		currentState.grid[oriY][oriX]	= ' ';
	}// end moveChip
	
	/**
	 * Whether the destination coordinates are diagonally placed with
	 * respect to the origin coordinates
	 * 
	 * @return true if the destination is placed diagonally, false if it is not
	 */
	private boolean isPlacedDiagonally()
	{
		return Math.abs(oriX - desX) == 1 && Math.abs(oriY - desY) == 1;
	}// end isPlacedDiagonally
	
	/**
	 * Whether the destination coordinates are adjacent to the
	 * origin coordinates
	 * 
	 * @return true if the destination is adjacent, false if it is not
	 */
	private boolean isAdjacent()
	{
		if (Math.abs(oriX - desX) == 1 && oriY == desY)
			return true;
		if (Math.abs(oriY - desY) == 1 && oriX == desX)
			return true;
		
		return Math.abs(oriX - desX) == 1 && Math.abs(oriY - desY) == 1;
	}// end isAdjacent
	
	/**
	 * Determine the direction from the 8 possible direction from the
	 * origin coordinates and the destination coordinates. This method
	 * receives the coordinates because this way, we can obtain the
	 * opposite direction by just placing the coordinates the other
	 * way around
	 * 
	 * @param  oriY the origin y coordinate
	 * @param  oriX the origin x coordinate
	 * @param  desY the destination y coordinate
	 * @param  desX the destination x coordinate
	 * @return      a String object that states the general direction of the movement
	 */
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
		
		return ""; // This should never happen to a valid movement
	}// end getDirection
}// end Movement - class
