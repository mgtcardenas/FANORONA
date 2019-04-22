import javafx.scene.paint.Color;

public class AI extends Player
{
	/**
	 * An AI is a Player, only that it will have all the MiniMax logic
	 */
	public AI()
	{
		this.chips = new Chip[22];
		for (Chip c : this.chips)
			c = new Chip(Color.BLACK);
	}// end AI - constructor
}// end AI - class
