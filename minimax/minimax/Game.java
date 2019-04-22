package minimax;
/**
 * Created by Gerardo Ayala on 10/7/17.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Controller
public class Game implements ActionListener
{
	State			currentState;
	GameInterface	myInterface;
	
	public Game(State aState, GameInterface anInterface)
	{
		currentState	= aState;
		myInterface		= anInterface;
	}// end Game - constructor
	
	private void updateInterface(String player, Button aButton)
	{
		if (player.equals("MIN"))
			aButton.setLabel("O");
		else
			aButton.setLabel("X");
	}// end updateInterface
	
	private void updateState(String player, Button aButton)
	{
		int i, j;
		
		i = 0;
		while (i < 3)
		{
			j = 0;
			while (j < 3)
			{
				if (aButton == myInterface.buttons[i][j])
				{
					if (player.equals("MIN"))
						currentState.grid[i][j] = 'O';
					else
						currentState.grid[i][j] = 'X';
				}// end if
				j = j + 1;
			}// end while
			i = i + 1;
		}// end while
	}// end updateState
	
	private Button agentMove(int x, int y)
	{
		Button theButton;
		
		theButton = myInterface.buttons[x][y];
		return theButton;
	}// end agentMove
	
	private boolean canMove(char symbol)
	{
		boolean itCanMove;
		
		itCanMove = true;
		if ((winner('X')) || (winner('O')) || gridIsFull())
		{
			itCanMove = false;
			System.out.println("GAME OVER");
		}// end if
		return itCanMove;
	}// end canMove
	
	public boolean winner(char symbol)
	{
		boolean isWinner;
		
		isWinner = false;
		
		if (currentState.diagonalWin(symbol) || currentState.horizontalWin(symbol) || currentState.verticalWin(symbol))
			isWinner = true;
		
		return isWinner;
	}// end winner
	
	public boolean gridIsFull()
	{
		boolean		isFull;
		int			i, j;
		final char	emptySpace	= ' ';
		
		isFull	= true;
		i		= 0;
		while (i < 3)
		{
			j = 0;
			while (j < 3)
			{
				if (currentState.grid[i][j] == emptySpace)
				{
					isFull = false;
					break;
				}// end if
				j = j + 1;
			}// end while
			i = i + 1;
		}// end while
		
		return isFull;
	}// end gridIsFull
	
	public boolean gameOver()
	{
		boolean isOver;
		
		isOver = false;
		
		if (winner('X') || winner('O') || gridIsFull())
			isOver = true;
		// end if
		return isOver;
	}// end gameOver
	
	public void actionPerformed(ActionEvent event)
	{
		Button	clickedButton;
		State	estadoSeleccionado;
		
		if (canMove('O'))
		{
			// User plays
			clickedButton = (Button) event.getSource();
			updateState("MIN", clickedButton);
			updateInterface("MIN", clickedButton);
			if (gameOver())
			{
				if (winner('O'))
					System.out.println("USER WINS!");
				else
					System.out.println("GOOD GAME!");
			}// end if
		}// end if
		if (canMove('X'))
		{
			// Agent plays
			estadoSeleccionado	= MinMax.miniMaxModerate(currentState);
			clickedButton		= agentMove(estadoSeleccionado.row, estadoSeleccionado.column);
			updateState("MAX", clickedButton);
			updateInterface("MAX", clickedButton);
			if (gameOver())
			{
				if (winner('X'))
					System.out.println("AGENT WINS!");
				else
					System.out.println("GOOD GAME!");
			}// end if
		}// end if
	}// end actionPerformed
}// end Game - class
