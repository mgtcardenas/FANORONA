/**
 * @author Marco Cárdenas
 *
 *         Class that manages exceptions when singnleton objects are tried to be created
 *         whilst they already exist
 */
public class SingletonException extends Exception
{
	/**
	 * Creates a new SingletonException with a given message
	 * @param message a message stating what was the mistake or problem
	 */
	public SingletonException(String message)
	{
		super(message);
	}// end SingletonException
}// end SingletonException - class
