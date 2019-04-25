import java.util.LinkedList;

public class State
{
	char[][]			grid;
	LinkedList<State>	children;
	int					payoff;
	int					oriRow;
	int					oriCol;
	int					desRow;
	int					desCol;
	
	public State()
	{
		this.grid	= new char[5][9];
		
		grid[2][4]	= ' '; // Setting the only Blank Space
		
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
}// end State - class
