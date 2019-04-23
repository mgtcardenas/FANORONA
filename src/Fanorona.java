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
		Board         board         = Board.instance();                  // Model
		Player        player        = new Player();                      // Model
		Player        aI            = new AI();                          // Model
		GameInterface gameInterface = new GameInterface();                        // GameInterface
		Game          game          = new Game(board, player, aI, gameInterface); // Controller
		
		gameInterface.setEventHandlersAndActionListeners(game);
		
		primaryStage.setTitle("Fanorona");
		primaryStage.setScene(new Scene(gameInterface, GameInterface.SCENE_WIDTH, GameInterface.SCENE_HEIGHT));
		primaryStage.setResizable(false);
		primaryStage.show();
	}// end start
}// end Fanorona - class
