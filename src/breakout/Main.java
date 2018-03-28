package breakout;

/**
 * Start the game The call to startGame() in the model starts the actual play of
 * the game Note: Many issues of mutual exclusion on shared variables are
 * ignored.
 * 
 * @author Mike Smith University of Brighton
 */
public class Main {
	public static void main(String args[]) {
		Debug.trace("BreakOut");
		Debug.set(Constants.ENABLE_DEBUG); // Set true to get debug info

		Model model = new Model(Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT); // model
																				// of
																				// the
																				// Game
		View view = new View(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT); // View
																				// of
																				// the
																				// Game
		new Controller(model, view);

		model.createGameObjects(); // Ball, Bat & Bricks
		model.addObserver(view); // Add observer to the model
		model.startGame(); // Start playing the game
		
		view.setVisible(true); // Make visible
	}
}
