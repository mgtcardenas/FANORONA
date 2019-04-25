package failed;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Fanorona extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}// end main
	
	@Override
	public void start(Stage primaryStage)
	{
		State			initialState;
		GameInterface	gameInterface;
		Game			controller;
		
		initialState	= new State();
		gameInterface	= new GameInterface(); // GameInterface
		controller		= new Game(initialState, gameInterface); // Controller
		
		gameInterface.setEventHandlersAndActionListeners(controller);
		controller.initializeInterface();
		
		primaryStage.setTitle("Fanorona");
		primaryStage.setScene(new Scene(gameInterface, GameInterface.SCENE_WIDTH, GameInterface.SCENE_HEIGHT));
		primaryStage.setResizable(false);
		primaryStage.show();
	}// end start
}// end Fanorona - class
