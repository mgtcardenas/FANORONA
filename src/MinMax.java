import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marco Cárdenas
 *
 *         This class holds the algorithm for performing the MinMax algorithm
 */
public class MinMax
{
	/**
	 * Select the best state from a list of states, but if there are two or more best states,
	 * select one of them randomly
	 * 
	 * @param  children a list of states
	 * @return          a single best state from the list of states
	 */
	private static State selectMaxChild(List<State> children)
	{
		LinkedList<State>	candidates;
		State				maxChild;
		
		Collections.sort(children);
		Collections.reverse(children);
		candidates	= new LinkedList<>();
		maxChild	= children.get(0);
		
		candidates.add(maxChild);
		
		for (int i = 1; i < children.size(); i++)
			if (maxChild.payoff == children.get(i).payoff)
				candidates.add(children.get(i));
			else
				break;
			
		return (candidates.size() > 1) ? candidates.get((int) (Math.random() * candidates.size())) : candidates.get(0);
	}// end selectMaxChild
	
	/**
	 * Select the most convenient next possible state given a state given how
	 * the payoff function of a state calculates the payoff of a state
	 * 
	 * @param  state a state of the game, usually the current state of the game
	 * @return       the most convenient next possible state
	 */
	public static State minMaxEasy(State state)
	{
		LinkedList<State>	children;
		LinkedList<State>	goodGrandChildren;
		LinkedList<State>	badGrandChildren;
		State				grandChild;
		
		state.expansion(true);
		children = state.children;
		
		for (int i = 0; i < children.size(); i++)
		{
			if (children.get(i).turn.equals("agent-playing")) // the agent would keep playing in this possible move
			{
				children.get(i).expansion(true);
				goodGrandChildren = children.get(i).children;
				
				for (int j = 0; j < goodGrandChildren.size(); j++)
					goodGrandChildren.get(j).payOffFunction(); // calculate the payoff of each good grand child
					
				Collections.reverse(goodGrandChildren); // The best child is the first
				if (goodGrandChildren.size() > 0)
				{
					grandChild				= goodGrandChildren.get(0);
					children.get(i).payoff	= grandChild.payoff;
				}
				else
					children.get(i).payOffFunction();
			}
			else // the user will play in this possible move
			{
				children.get(i).expansion(false);
				badGrandChildren = children.get(i).children;
				
				for (int j = 0; j < badGrandChildren.size(); j++)
					badGrandChildren.get(j).payOffFunction(); // calculate the payoff of each good grand child
					
				Collections.sort(badGrandChildren); // The least bad child is the first
				
				if (badGrandChildren.size() > 0)
				{
					grandChild				= badGrandChildren.get(0);
					children.get(i).payoff	= grandChild.payoff; // inheritance of the minimal payoff value
				}
				else
					children.get(i).payOffFunction();
			}// end if - else
		}// end for - i
		
		return selectMaxChild(children);
	}// end minMaxEasy
}// end MinMax - class
