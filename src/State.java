import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class State implements Comparable<State>, Serializable
{
	char[][]			grid;
	LinkedList<State>	children;
	int					payoff;
	int					oriY;          // The origin in Y that brought us to this state
	int					oriX;          // The origin in X that brought us to this state
	int					desY;          // The destination in Y that brought us to this state
	int					desX;          // The destination in X that brought us to this state
	String				turn;
	String				lastDirection;
	Set<Movement>		walkedPath;
	
	public State()
	{
		this.turn			= "agent";
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
	
	public void expansion(boolean agentMoves)
	{
		LinkedList<Movement> possibleMovements;
		
		children			= new LinkedList<>();
		possibleMovements	= getPossibleMovements(agentMoves);
		for (Movement possibleMovement : possibleMovements)
		{
			possibleMovement.perform(); // To get the state that this movement takes us to
			children.add(possibleMovement.currentState);
		}// end foreach
	}// end expansion
	
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
	
	public void payOffFunction()
	{
		int	utility			= 0;
		
		int	chipDifference	= getChipDifference();
		
		if (turn.equals("agent"))
			utility += 4;
		
		if (chipDifference > 0)
			utility += chipDifference;
		
		if (turn.equals("user") || turn.equals(""))
			utility -= 4;
		
		if (chipDifference < 0)
			utility += (chipDifference * -1);
		
		payoff = utility;
	}// end payOffFunction
	
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
