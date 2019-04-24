import java.util.LinkedList;

import javafx.scene.paint.Color;

public class State implements Comparable<State>
{
	private Position[][]		grid;
	private LinkedList<State>	children;
	private int					payoff;
	private Chip				chip;
	private Position			position;
	
	public State()
	{
		this.grid = new Position[5][9];
		for (int y = 0; y < grid.length; y++) // Set the Positions in place
		{
			for (int x = 0; x < grid[y].length; x++)
			{
				grid[y][x] = new Position(y, x, ((y + x) % 2 == 0) ? "strong" : "weak");
				grid[y][x].setLayoutX(x * Position.POSITION_SIZE);
				grid[y][x].setLayoutY(y * Position.POSITION_SIZE);
			}// end for - j
		}// end for - i
		
		setInitialWhiteChips();
		setInitialBlackChips();
		
		this.chip		= null;
		this.position	= null;
	}// end State - constructor
	
	// region Getters
	public Position[][] getGrid()
	{
		return grid;
	}// end getGrid
	
	public LinkedList<State> getChildren() // aka Expansion
	{
		return children;
	}// end getChildren
	
	public int getPayoff()
	{
		return payoff;
	}// end getPayoff
	
	public Chip getChip()
	{
		return chip;
	}// end getChip
	
	public Position getPosition()
	{
		return position;
	}// end getPosition
		// endregion Getters
	
	private void setInitialWhiteChips()
	{
		for (int y = 3; y <= 4; y++)
			for (int x = 0; x < grid[y].length; x++)
				grid[y][x].setChip(new Chip(Color.WHITE));
			
		grid[2][1].setChip(new Chip(Color.WHITE));
		grid[2][3].setChip(new Chip(Color.WHITE));
		grid[2][6].setChip(new Chip(Color.WHITE));
		grid[2][8].setChip(new Chip(Color.WHITE));
	}// end setInitialWhiteChips
	
	private void setInitialBlackChips()
	{
		for (int y = 0; y <= 1; y++)
			for (int x = 0; x < grid[y].length; x++)
				grid[y][x].setChip(new Chip(Color.BLACK));
			
		grid[2][0].setChip(new Chip(Color.BLACK));
		grid[2][2].setChip(new Chip(Color.BLACK));
		grid[2][5].setChip(new Chip(Color.BLACK));
		grid[2][7].setChip(new Chip(Color.BLACK));
	}// end setInitialBlackChips
	
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
}// end State - class
