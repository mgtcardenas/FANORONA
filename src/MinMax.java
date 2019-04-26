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
		LinkedList<State>	grandChildren;
		State				minGrandChild;
		
		state.expansion(true);
		children = state.children; // expansion
		
		for (int i = 0; i < children.size(); i++) // Min
		{
			children.get(i).expansion(false);
			grandChildren = children.get(i).children; // expansion of each child
			
			for (int j = 0; j < grandChildren.size(); j++)
				grandChildren.get(j).payOffFunction(); // calculate the payoff of each grand child
				
			Collections.sort(grandChildren);
			minGrandChild			= grandChildren.get(0);
			children.get(i).payoff	= minGrandChild.payoff; // inheritance of the minimal payoff value
		}// end for - i
		
		return selectMaxChild(children);
	}// end miniMaxEasy
}// end MinMax - class
