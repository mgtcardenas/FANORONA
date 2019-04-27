package minimax;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Gerardo Ayala on 10/7/17.
 */
public class MinMax
{
	private static State selectMaxChild(LinkedList<State> children)
	{
		LinkedList<State>	candidates;
		State				maxChild;
		State				stateSelected;
		int					i;
		int					n;
		int					index;
		Random				random	= new Random(System.currentTimeMillis());
		
		Collections.sort(children);
		Collections.reverse(children);
		candidates	= new LinkedList<State>();
		maxChild	= children.get(0);
		candidates.add(maxChild);
		i = 1;
		while (i < children.size())
		{
			if (maxChild.payoff == children.get(i).payoff)
				candidates.add(children.get(i));
			else
				break;
			
			i = i + 1;
		}// end while
		n = candidates.size();
		if (n > 0)
			index = random.nextInt(n);
		else
			index = 0;
		
		stateSelected = candidates.get(index);
		return stateSelected;
	}// end selectMaxChild
	
	public static State miniMaxEasy(State state)
	{
		LinkedList<State>	children;
		LinkedList<State>	grandChildren;
		State				selectedState;
		State				minGrandChild;
		int					i;
		int					j;
		
		children	= state.children; // expansion
		
		// MIN ////////////////////////////////////////////
		i			= 0;
		while (i < children.size())
		{
			grandChildren	= state.children.get(i).children; // expansion of each child
			
			// calculate the payoff of each grand child
			j				= 0;
			while (j < grandChildren.size())
			{
				grandChildren.get(j).payOffFunction();
				j = j + 1;
			}// end while
			Collections.sort(grandChildren);
			minGrandChild			= grandChildren.get(0);
			children.get(i).payoff	= minGrandChild.payoff; // inheritance of the minimal payoff value
			System.out.println(children.get(i));
			i = i + 1;
		}// end while
		
		// MAX ///////////////////////////////////////////////
		selectedState = selectMaxChild(children);
		System.out.println("************* Selected State");
		System.out.println(selectedState);
		System.out.println("*****************************");
		return selectedState;
	}// end minMaxEasy
	
	public static State miniMaxModerate(State state)
	{
		LinkedList<State>	children;
		LinkedList<State>	grandChildren;
		LinkedList<State>	grandGrandChildren;
		LinkedList<State>	grandGrandGrandChildren;
		State				selectedState;
		int					i;
		int					j;
		int					k;
		int					l;
		
		if (state.getPossibleMoves().size() < 4)
			selectedState = miniMaxEasy(state);
		else
		{
			// expansion
			state.expansionAgentMoves();
			children	= state.children;
			
			// MIN ////////////////////////////////////////////
			i			= 0;
			while (i < children.size())
			{
				// expansion of each child
				children.get(i).expansionUserMoves();
				grandChildren	= state.children.get(i).children;
				j				= 0;
				while (j < grandChildren.size())
				{
					// expansion of each grand child
					grandChildren.get(j).expansionAgentMoves();
					grandGrandChildren	= grandChildren.get(j).children;
					//
					k					= 0;
					while (k < grandGrandChildren.size())
					{
						// expansion of each grand grand child
						grandGrandChildren.get(k).expansionUserMoves();
						grandGrandGrandChildren	= grandGrandChildren.get(k).children;
						l						= 0;
						while (l < grandGrandGrandChildren.size())
						{
							grandGrandGrandChildren.get(l).payOffFunction();
							l = l + 1;
						}// end while
						
						// MIN
						Collections.sort(grandGrandGrandChildren);
						grandGrandChildren.get(k).payoff	= grandGrandGrandChildren.get(0).payoff;
						k									= k + 1;
					}// end while
					
					// MAX
					Collections.sort(grandGrandChildren);
					Collections.reverse(grandGrandChildren);
					grandChildren.get(j).payoff	= grandGrandChildren.get(0).payoff;
					j							= j + 1;
				}// end while
				
				// MIN
				Collections.sort(grandChildren);
				children.get(i).payoff = grandChildren.get(0).payoff;
				System.out.println(children.get(i));
				i = i + 1;
			}// end while
			
			// MAX ///////////////////////////////////////////////
			selectedState = selectMaxChild(children);
			System.out.println("************* Selected State");
			System.out.println(selectedState);
			System.out.println("*****************************");
		}// end else
		return selectedState;
	}// end miniMaxModerate
}// end Minmax - class
