import javafx.scene.paint.Color;

public class Player
{
	Chip[] chips;
	
	/**
	 * A player is only a holder of chips of a certain color
	 */
	public Player()
	{
		this.chips = new Chip[22];
		for (Chip c : this.chips)
			c = new Chip(Color.WHITE);
	}// end Player - constructor
	
	/**
	 * Give access to the player's Chips
	 * 
	 * @return this player's Chips
	 */
	public Chip[] getChips()
	{
		return chips;
	}// end getChips
	
}// end Player - class
