import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Marco Cárdenas
 *
 *         This class is pretty much the Main class. It represents the whole application and
 *         is the one that should be complied.
 */
public class Fanorona extends Application
{
	/**
	 * Where the control of the program begins
	 * 
	 * @param args the arguments of the program
	 */
	public static void main(String[] args)
	{
		launch(args);
	}// end main
	
	/**
	 * Overridden method to comply with the JavaFX GUI framework
	 * 
	 * @param primaryStage the first 'window'
	 */
	@Override
	public void start(Stage primaryStage)
	{
		State			initialState;
		GameInterface	gameInterface;
		Game			controller;
		
		initialState	= new State();
		gameInterface	= new GameInterface(); // GameInterface
		controller		= new Game(initialState, gameInterface); // Controller
		
		controller.updateInterface(true);
		
		primaryStage.setTitle("Fanorona");
		primaryStage.setScene(new Scene(gameInterface, GameInterface.SCENE_WIDTH, GameInterface.SCENE_HEIGHT));
		primaryStage.setResizable(false);
		primaryStage.show();
	}// end start
}// end Fanorona - class
