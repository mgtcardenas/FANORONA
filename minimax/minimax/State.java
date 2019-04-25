package minimax;
/**
 * Created by Gerardo Ayala on 10/7/17.
 */

import java.util.LinkedList;

public class State implements Comparable
{
	char[][]			grid;
	LinkedList<State>	children;
	int					payoff;
	int					row;
	int					column;
	
	public State()
	{
		int			i;
		int			j;
		final char	emptySpace	= ' ';
		
		grid	= new char[3][3];
		i		= 0;
		while (i < 3)
		{
			j = 0;
			while (j < 3)
			{
				grid[i][j]	= emptySpace;
				j			= j + 1;
			}// end while
			i = i + 1;
		}// end while
		row		= -1;
		column	= -1;
	}// end constructor
	
	public LinkedList<Position> getPossibleMoves()
	{
		LinkedList<Position>	possibleMoves;
		Position				position;
		int						i;
		int						j;
		final char				emptySpace	= ' ';
		
		possibleMoves	= new LinkedList<Position>();
		i				= 0;
		while (i < 3)
		{
			j = 0;
			while (j < 3)
			{
				if (grid[i][j] == emptySpace)
				{
					position = new Position(i, j);
					possibleMoves.add(position);
				}// end if
				j = j + 1;
			}// end while
			i = i + 1;
		}// end while
		return possibleMoves;
	}// end getPossibleMoves
	
	private void expansion(char symbol)
	{
		State					child;
		LinkedList<Position>	possibleMoves;
		int						i;
		int						row;
		int						column;
		Position				position;
		
		children		= new LinkedList<State>();
		
		possibleMoves	= getPossibleMoves();
		i				= 0;
		while (i < possibleMoves.size())
		{
			child	= new State(); // begin cloning state
			row		= 0;
			while (row < 3)
			{
				column = 0;
				while (column < 3)
				{
					child.grid[row][column]	= this.grid[row][column];
					column					= column + 1;
				}// end while
				row = row + 1;
			}// end while; end cloning state
			position									= possibleMoves.get(i);
			child.grid[position.row][position.column]	= symbol;
			child.row									= position.row;
			child.column								= position.column;
			children.add(child);
			i = i + 1;
		}// end while
	}// end expansion
	
	public void expansionAgentMoves()
	{
		expansion('X');
	}// end expansionAgentMoves
	
	public void expansionUserMoves()
	{
		expansion('O');
	}// end expansionUserMoves
	
	// region State Analysis Functions
	public boolean diagonalWin(char symbol)
	{
		boolean		isDiagonal;
		final char	emptySpace	= ' ';
		
		isDiagonal = false;
		if ((grid[0][0] == symbol) && (grid[0][0] == grid[1][1]) && (grid[0][0] == grid[2][2]) && (grid[0][0] != emptySpace))
			isDiagonal = true;
		else if ((grid[0][2] == symbol) && (grid[0][2] == grid[1][1]) && (grid[0][2] == grid[2][0]) && (grid[0][2] != emptySpace))
			isDiagonal = true;
		
		return isDiagonal;
	}// end diagonalWin
	
	public boolean horizontalWin(char symbol)
	{
		boolean		isHorizontal;
		final char	emptySpace	= ' ';
		
		isHorizontal = false;
		
		if ((grid[0][0] == symbol) && (grid[0][0] == grid[0][1]) && (grid[0][0] == grid[0][2]) && (grid[0][0] != emptySpace))
			isHorizontal = true;
		else if ((grid[1][0] == symbol) && (grid[1][0] == grid[1][1]) && (grid[1][0] == grid[1][2]) && (grid[1][0] != emptySpace))
			isHorizontal = true;
		// end if
		else if ((grid[2][0] == symbol) && (grid[2][0] == grid[2][1]) && (grid[2][0] == grid[2][2]) && (grid[2][0] != emptySpace))
			isHorizontal = true;
		
		return isHorizontal;
	}// end horizontalWin
	
	public boolean verticalWin(char symbol)
	{
		boolean		isVertical;
		final char	emptySpace	= ' ';
		
		isVertical = false;
		if ((grid[0][0] == symbol) && (grid[0][0] == grid[1][0]) && (grid[0][0] == grid[2][0]) && (grid[0][0] != emptySpace))
			isVertical = true;
		else if ((grid[0][1] == symbol) && (grid[0][1] == grid[1][1]) && (grid[0][1] == grid[2][1]) && (grid[0][1] != emptySpace))
			isVertical = true;
		else if ((grid[0][2] == symbol) && (grid[0][2] == grid[1][2]) && (grid[0][2] == grid[2][2]) && (grid[0][2] != emptySpace))
			isVertical = true;
		
		return isVertical;
	}// end verticalWin
	
	private boolean almostVerticalWin(char symbol)
	{
		boolean		isAlmostVertical;
		int			i;
		final char	emptySpace	= ' ';
		
		isAlmostVertical	= false;
		i					= 0;
		while ((i < 3) && !isAlmostVertical)
		{
			if (((grid[0][i] == symbol) && (grid[1][i] == symbol) && (grid[2][i] == emptySpace)) || ((grid[0][i] == symbol) && (grid[2][i] == symbol) && (grid[1][i] == emptySpace)) || ((grid[1][i] == symbol) && (grid[2][i] == symbol) && (grid[0][i] == emptySpace)))
				isAlmostVertical = true;
			i = i + 1;
		}// end while
		
		return isAlmostVertical;
	}// end almostVertical
	
	private boolean almostHorizontalWin(char symbol)
	{
		boolean		isAlmostHorizontal;
		int			i;
		final char	emptySpace	= ' ';
		
		isAlmostHorizontal	= false;
		i					= 0;
		while ((i < 3) && !isAlmostHorizontal)
		{
			if (((grid[i][0] == symbol) && (grid[i][1] == symbol) && (grid[i][2] == emptySpace)) || ((grid[i][0] == symbol) && (grid[i][2] == symbol) && (grid[i][1] == emptySpace)) || ((grid[i][1] == symbol) && (grid[i][2] == symbol) && (grid[i][0] == emptySpace)))
				isAlmostHorizontal = true;
			// end if
			i = i + 1;
		}// end while
		return isAlmostHorizontal;
	}// end almostHorizontal
	
	private boolean almostRightDiagonalWin(char symbol)
	{
		boolean		isAlmostRightDiagonal;
		final char	emptySpace	= ' ';
		
		isAlmostRightDiagonal = false;
		if (((grid[0][0] == symbol) && (grid[1][1] == symbol) && (grid[2][2] == emptySpace)) || ((grid[0][0] == symbol) && (grid[1][1] == emptySpace) && (grid[2][2] == symbol)) || ((grid[0][0] == emptySpace) && (grid[1][1] == symbol) && (grid[2][2] == symbol)))
			isAlmostRightDiagonal = true;
		
		return isAlmostRightDiagonal;
	}// end almostRightDiagonalWin
	
	private boolean almostLeftDiagonalWin(char symbol)
	{
		boolean		isAlmostLeftDiagonal;
		final char	emptySpace	= ' ';
		
		isAlmostLeftDiagonal = false;
		if (((grid[0][2] == symbol) && (grid[1][1] == symbol) && (grid[2][0] == emptySpace)) || ((grid[0][2] == symbol) && (grid[1][1] == emptySpace) && (grid[2][0] == symbol)) || ((grid[0][2] == emptySpace) && (grid[1][1] == symbol) && (grid[2][0] == symbol)))
			isAlmostLeftDiagonal = true;
		// end if
		return isAlmostLeftDiagonal;
	}// end almostLeftDiagonalWin
		// endregion State Analysis Functions
	
	public void payOffFunction()
	{
		int utility;
		
		utility = 0;
		
		if (verticalWin('X'))
			utility = utility + 4;
		if (horizontalWin('X'))
			utility = utility + 4;
		if (diagonalWin('X'))
			utility = utility + 4;
		if (almostVerticalWin('X'))
			utility = utility + 2;
		if (almostHorizontalWin('X'))
			utility = utility + 2;
		if (almostRightDiagonalWin('X'))
			utility = utility + 2;
		if (almostLeftDiagonalWin('X'))
			utility = utility + 2;
		
		// y para las 'O' (del usuario)
		if (verticalWin('O'))
			utility = utility - 3;
		if (horizontalWin('O'))
			utility = utility - 3;
		if (diagonalWin('O'))
			utility = utility - 3;
		if (almostVerticalWin('O'))
			utility = utility - 1;
		if (almostHorizontalWin('O'))
			utility = utility - 1;
		if (almostRightDiagonalWin('O'))
			utility = utility - 1;
		if (almostLeftDiagonalWin('O'))
			utility = utility - 1;
		
		payoff = utility;
	}// payoffFunction
	
	public int compareTo(Object anObject)
	{
		int		result;
		State	anState;
		
		anState	= (State) anObject;
		result	= 0;
		if (payoff > anState.payoff)
			result = 1;
		else if (payoff < anState.payoff)
			result = -1;
		
		return result;
	}// end compareTo
	
	public String toString()
	{
		return "PAYOFF = " + payoff + "\n" + grid[0][0] + "|" + grid[0][1] + "|" + grid[0][2] + "\n" + grid[1][0] + "|" + grid[1][1] + "|" + grid[1][2] + "\n" + grid[2][0] + "|" + grid[2][1] + "|" + grid[2][2] + "\n" + "\n";
	}// end toString
}// end State - class
