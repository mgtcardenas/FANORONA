package failed;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.paint.Color;

public class State implements Comparable<State>, Serializable
{
	private Position[][]		grid;
	private LinkedList<State>	children;
	private int					payoff;
	private Chip				chip;     // The chip to make the next best move for the agent
	private Position			position; // The position to put the chip for the next best move for the agent
	private Movement			movement; // For the PayOffFunction to know if the movement has nextPossibleCaptures
	private Game				game;
	
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
	
	// region Getters & Setters
	public void setGame(Game game)
	{
		this.game = game;
	}// end setGame
	
	public Position[][] getGrid()
	{
		return grid;
	}// end getGrid
	
	public LinkedList<State> getChildren()
	{
		return children;
	}// end getChildren
	
	public int getPayoff()
	{
		return payoff;
	}// end getPayoff
	
	public void setPayoff(int payoff)
	{
		this.payoff = payoff;
	}// end setPayoff
	
	public Chip getChip()
	{
		return chip;
	}// end getChip
	
	public void setChip(Chip chip)
	{
		this.chip = chip;
	}// end setChip
	
	public Position getPosition()
	{
		return position;
	}// end getPosition
	
	public void setPosition(Position position)
	{
		this.position = position;
	}// end setPosition
		// endregion Getters & Setters
	
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
	
	public LinkedList<Movement> getPossibleMovements(boolean agentMovements)
	{
		LinkedList<Movement>	possibleMovements;
		String					colorType;
		
		colorType			= agentMovements ? "black" : "white";
		possibleMovements	= new LinkedList<>();
		
		for (int y = 0; y < grid.length; y++) // Set the Positions in place
			for (int x = 0; x < grid[y].length; x++)
				if (grid[y][x].getChip() != null && grid[y][x].getChip().getColorType().equals(colorType))
					possibleMovements.addAll(getMovements(grid[y][x].getChip()));
				
		return possibleMovements;
	}// end getPossibleMovements
	
	private List<Movement> getMovements(Chip chip)
	{
		LinkedList<Movement>	movements	= new LinkedList<>();
		int						chipX		= chip.getPosition().getxCoordinate();
		int						chipY		= chip.getPosition().getyCoordinate();
		
		for (int posY = chipY - 1; posY <= chipY + 1; posY++) // Check all adjacent positions
			for (int posX = chipX - 1; posX <= chipX + 1; posX++)
				if (posY >= 0 && posY <= 4 && posX >= 0 && posX <= 8 && grid[posY][posX].getChip() == null)
					movements.add(new Movement(chip, grid[posY][posX], game));
				
		return movements;
	}// end getMovements
	
	public void expansion(boolean agentMoves)
	{
		LinkedList<Movement>	possibleMovements;
		LinkedList<Movement>	possibleChildrenMovements;
		LinkedList<State>		possibleChildren;
		
		possibleChildren	= new LinkedList<>();
		possibleMovements	= getPossibleMovements(agentMoves);
		
		for (int i = 0; i < possibleMovements.size(); i++)
			possibleChildren.add(i, deepClone(this)); // with this deep clone even the game object of this state
			
		for (int i = 0; i < possibleMovements.size(); i++)
		{
			this.chip					= possibleMovements.get(i).getChip();
			this.position				= possibleMovements.get(i).getDestinationPos();
			
			possibleChildrenMovements	= possibleChildren.get(i).getPossibleMovements(agentMoves);
			possibleChildren.get(i).setChip(possibleChildrenMovements.get(i).getChip());
			possibleChildren.get(i).setPosition(possibleChildrenMovements.get(i).getDestinationPos());
			possibleChildren.get(i).performMovement();
		}// end foreach
		
		this.children = possibleChildren;
	}// end expansion
	
	private void performMovement()
	{
		this.movement = new Movement(this.chip, this.position, this.game);
		this.movement.perform();
	}// end performMovement
	
	public void payOffFunction()
	{
		int utility = 0;
		
		if (this.movement.didCapture() && this.movement.hasNextPossibleCaptures())
			utility += 4;
		else if (this.movement.didCapture())
			utility += 2;
		
		this.payoff = utility;
	}// end payOffFunction
	
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
