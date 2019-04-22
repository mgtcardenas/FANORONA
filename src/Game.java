import javafx.scene.input.MouseEvent;

public class Game
{
	private Board	board;
	private View	view;
	private Player	user;
	private Player	aI;
	private Player	currentPlayer;
	private Chip	selectedChip;
	
	/**
	 * Some of these arguments are not strictly necessary, but they are there because they make sense.
	 * The important arguments are the user, the aI and the view
	 * 
	 * @param board the Board of the game
	 * @param user  the user, a player
	 * @param aI    the artificial intelligence, a player
	 * @param view  a pane with all the visual elements on it
	 */
	public Game(Board board, Player user, Player aI, View view)
	{
		this.board	= board;
		this.view	= view;
		this.user	= user;
		this.aI		= aI;
	}// end Game - constructor
	
	// region Getters
	public Player getUser()
	{
		return user;
	}// end getUser
	
	public Player getaI()
	{
		return aI;
	}// end getaI
		// endregion Getters
	
	public void handleGridSpaceClicks(MouseEvent event)
	{
		GridSpace clickedGridSpace = ((GridSpace) event.getSource());
		System.out.println(clickedGridSpace.getyCoordinate() + ", " + clickedGridSpace.getxCoordinate());
	}// end handleGridSpaceClicks
	
	public void handleChipClicks(MouseEvent event)
	{
		Chip clickedChip = ((Chip) event.getSource());
		System.out.println("I'm white chip");
	}// end handleChipClicks
}// end Game - class
