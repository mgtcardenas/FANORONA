import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MinMax
{
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
	
	public static State miniMaxEasy(State state)
	{
		LinkedList<State>	children;
		LinkedList<State>	goodGrandChildren;
		LinkedList<State>	badGrandChildren;
		State				grandChild;
		
		state.expansion(true);
		children = state.children; // expansion
		
		for (int i = 0; i < children.size(); i++) // Min
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
	}// end miniMaxEasy
}// end MinMax - class
