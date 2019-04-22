/**
 * Created by Gerardo Ayala on 10/7/17.
 */

public class TicTacToe
{
	public static void main(String[] any)
	{
		State			initialState;
		GameInterface	myInterface;
		Game			controller;
		
		initialState	= new State();
		myInterface		= new GameInterface();
		controller		= new Game(initialState, myInterface);
		myInterface.setActionListener(controller);
		myInterface.startGame();
	}// end main
}// end TicTacToe - class
