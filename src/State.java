import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Marco Cárdenas
 *
 *         A class that represents the state of the game. It implements serializable so it
 *         is easy to create a deep clone of it. It implements comparable based on the payoff
 *         attribute, which is how good/convenient this state is for the agent
 */
public class State implements Comparable<State>, Serializable
{
	char[][]			grid;          // The board which holds the black ('O') & white ('X') chips as chars
	LinkedList<State>	children;      // The possible next states
	int					payoff;        // How good/convenient this state is for the agent
	int					oriY;          // The origin in Y that brought us to this state
	int					oriX;          // The origin in X that brought us to this state
	int					desY;          // The destination in Y that brought us to this state
	int					desX;          // The destination in X that brought us to this state
	String				turn;          // Who goes next & who still has the turn
	String				lastDirection; // Used to prevent playing twice in the same direction as the rules state that's not valid
	Set<Movement>		walkedPath;    // Used to prevent going back to a previous position on tho board as the rules state that's not valid
	
	/**
	 * Constructor to get the initial state of the game
	 */
	public State()
	{
		this.turn			= "user"; // The user plays the white chips, so he goes first
		this.walkedPath		= new HashSet<>();
		this.lastDirection	= "";
		this.grid			= new char[5][9];
		
		grid[2][4]			= ' '; // Setting the only Blank Space
		
		for (int y = 0; y <= 1; y++) // Setting Black Chips / 'O'
			for (int x = 0; x < grid[y].length; x++)
				grid[y][x] = 'O';
			
		grid[2][0]	= 'O'; // Setting Black Chips / 'O'
		grid[2][2]	= 'O'; // Setting Black Chips / 'O'
		grid[2][5]	= 'O'; // Setting Black Chips / 'O'
		grid[2][7]	= 'O'; // Setting Black Chips / 'O'
		
		for (int y = 3; y <= 4; y++) // Setting White Chips / 'X'
			for (int x = 0; x < grid[y].length; x++)
				grid[y][x] = 'X';
			
		grid[2][1]	= 'X'; // Setting White Chips / 'X'
		grid[2][3]	= 'X'; // Setting White Chips / 'X'
		grid[2][6]	= 'X'; // Setting White Chips / 'X'
		grid[2][8]	= 'X'; // Setting White Chips / 'X'
	}// end State - constructor
	
	/**
	 * Returns all the possible movements a player (agent or user) could perform
	 * whether they are valid or not (in the case it's the same direction or a previous position)
	 * 
	 * @param  agentMovements to know who makes the movements, the agent or the user
	 * @return                a list with all the possible movements
	 */
	public LinkedList<Movement> getPossibleMovements(boolean agentMovements)
	{
		LinkedList<Movement> possibleMovements;
		
		possibleMovements = new LinkedList<>();
		
		for (int y = 0; y < grid.length; y++) // Set the Positions in place
			for (int x = 0; x < grid[y].length; x++)
				if (grid[y][x] == (agentMovements ? 'O' : 'X'))
					possibleMovements.addAll(getMovements(y, x));
				
		return possibleMovements;
	}// end getPossibleMovements
	
	/**
	 * Get all the adjacent movements given the coordinates of a chip.
	 * This method has the caution to not trigger an IndexOutOfBoundsException
	 * because trying to get a position outside of the board
	 * 
	 * @param  chipY the y coordinate where a chip is supposed to be found
	 * @param  chipX the x coordinate where a chip is supposed to be found
	 * @return       a list of all possible adjacent movements
	 */
	private List<Movement> getMovements(int chipY, int chipX)
	{
		LinkedList<Movement> movements = new LinkedList<>();
		
		if ((chipY + chipX) % 2 != 0) // It's a weak position
		{
			if (chipX - 1 >= 0 && grid[chipY][chipX - 1] == ' ')
				movements.add(new Movement(chipY, chipX, chipY, chipX - 1, State.deepClone(this)));
			if (chipX + 1 <= 8 && grid[chipY][chipX + 1] == ' ')
				movements.add(new Movement(chipY, chipX, chipY, chipX + 1, State.deepClone(this)));
			if (chipY - 1 >= 0 && grid[chipY - 1][chipX] == ' ')
				movements.add(new Movement(chipY, chipX, chipY - 1, chipX, State.deepClone(this)));
			if (chipY + 1 <= 4 && grid[chipY + 1][chipX] == ' ')
				movements.add(new Movement(chipY, chipX, chipY + 1, chipX, State.deepClone(this)));
		}
		else
		{
			for (int posY = chipY - 1; posY <= chipY + 1; posY++) // Check all adjacent positions
				for (int posX = chipX - 1; posX <= chipX + 1; posX++)
					if (posY >= 0 && posY <= 4 && posX >= 0 && posX <= 8 && grid[posY][posX] == ' ')
						movements.add(new Movement(chipY, chipX, posY, posX, State.deepClone(this)));
		}// end if - else
		
		return movements;
	}// end getMovements
	
	/**
	 * Initializes this object's children attribute by getting all possible movements,
	 * then removing the invalid movements and performing said movements to get the
	 * next possible States, that is, the children
	 *
	 * @param agentMoves whether the agent moves in this turn or not
	 */
	public void expansion(boolean agentMoves)
	{
		LinkedList<Movement> possibleMovements;
		
		children			= new LinkedList<>();
		possibleMovements	= getPossibleMovements(agentMoves);
		
		for (int i = possibleMovements.size() - 1; i >= 0; i--) // Remove invalid movements
			if (!possibleMovements.get(i).isValid())
				possibleMovements.remove(i);
			
		for (Movement possibleMovement : possibleMovements)
		{
			possibleMovement.perform(); // To get the state that this movement takes us to
			children.add(possibleMovement.currentState);
		}// end foreach
	}// end expansion
	
	/**
	 * Gets the difference between the number of black chips and white chips on the board
	 * so it can be used on the payoff function
	 * 
	 * @return the difference between black chips and white chips
	 */
	private int getChipDifference()
	{
		int	numO	= 0;
		int	numX	= 0;
		
		for (int y = 0; y < grid.length; y++) // Set the Positions in place
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				numO	+= (grid[y][x] == 'O' ? 1 : 0);
				numX	+= (grid[y][x] == 'X' ? 1 : 0);
			}// end for - x
		}// end for - y
		
		return numO - numX;
	}// end getChipDifference
	
	/**
	 * The function that calculates how good/convenient this state is for the agent.
	 * If there are more black chips, then this is good for the agent and viceversa.
	 * IF the agent is still playing, this is very good and viceversa.
	 */
	public void payOffFunction()
	{
		int	utility			= 0;
		
		int	chipDifference	= getChipDifference();
		
		if (turn.equals("agent-playing"))
			utility += 4;
		
		if (chipDifference > 0)
			utility += chipDifference;
		
		if (turn.equals("user-playing"))
			utility -= 4;
		
		if (chipDifference < 0)
			utility += (chipDifference * -1);
		
		payoff = utility;
	}// end payOffFunction
	
	/**
	 * This method is for debugging purposes only, since there exists a GameInterface
	 * 
	 * @return a String representing the board
	 */
	@Override
	public String toString()
	{
		String board = "";
		
		board += "";
		
		for (int y = 0; y < grid.length; y++) // Set the Positions in place
		{
			for (int x = 0; x < grid[y].length; x++)
				board += grid[y][x] + " ";
			board += "\n";
		}// end for - i
		
		return board;
	}// end toString
	
	/**
	 * Method to comply with the comparable interface and be able to sort
	 * the states from best to worst
	 * 
	 * @param  otherState the state to compare this state to
	 * @return            1 if this state is better, -1 if this state is worse, 0 if they are the same
	 */
	@Override
	public int compareTo(State otherState)
	{
		int result = 0;
		
		if (this.payoff > otherState.payoff)
			result = 1;
		if (this.payoff < otherState.payoff)
			result = -1;
		
		return result;
	}// end compareTo
	
	/**
	 * This method helps get a deep clone of a state. Since arrays are objects in Java
	 * and this state is a complex object because it is composed of Strings and the like,
	 * in order to get a copy that doesn't keep the references to the actual board of the game,
	 * we need to get a deep copy/clone, a clone of the state which will have clones of the objects
	 * that compose this state. If these objects are complex too, they too require more clones.
	 * Since this is cumbersome, we can use serialization to get a quick deep clone of it, this this
	 * method came to be
	 * 
	 * @param  object the State we want a deep copy/clone of
	 * @return        a deep copy/clone of a state
	 */
	public static State deepClone(State object)
	{
		try
		{
			ByteArrayOutputStream	baos	= new ByteArrayOutputStream();
			ObjectOutputStream		oos		= new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream	bais	= new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream		ois		= new ObjectInputStream(bais);
			return (State) ois.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}// end try - catch
	}// end deepClone
}// end State - class
