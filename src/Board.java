/**
 * @author Marco Cárdenas
 *
 *         A class that represents the game board, which implements
 *         the Singleton design pattern, since there can only be one board for a game.
 *
 *         The board is represented by a grid of spaces which is treated as a bidimensional
 *         array of type Position.
 */
public class Board
{
	private static Board uniqueInstance;
	private Position[][] grid;
	
	/**
	 * Gives access to the grid on the board
	 */
	public Position[][] getGrid()
	{
		return this.grid;
	}// end getGrid
	
	/**
	 * There can only be one single board in a game.
	 * This constructor adds the 225 (15x15) grid spaces
	 * to their correct locations with their correct
	 * types and bonuses.
	 *
	 * @throws SingletonException if someone tries to create another bag
	 */
	private Board() throws SingletonException
	{
		if (uniqueInstance != null)
			throw new SingletonException("THERE CAN ONLY BE ONE BOARD!");
		
		this.grid = new Position[5][9]; // Create a board of 15 by 15 spaces (255 in total)
		
		for (int y = 0; y < 5; y++) // Fill the board with simple grid spaces
			for (int x = 0; x < 9; x++)
				this.grid[y][x] = new Position(y, x);
	}// end Board - constructor
	
	/**
	 * Returns the unique instance of the board and if it doesn't exist yet, it creates it
	 *
	 * @return the unique instance of the bag
	 */
	public static synchronized Board instance()
	{
		if (uniqueInstance == null)
		{
			try
			{
				uniqueInstance = new Board(); // we try to create the Board
			}
			catch (SingletonException e)
			{
				e.printStackTrace();
			}// end try - catch
		}// end if
		
		return uniqueInstance;
	}// end instance
}// end Board - class
