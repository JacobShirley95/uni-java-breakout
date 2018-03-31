package breakout;
import java.awt.event.KeyEvent;

/**
 * BreakOut controller, handles user interactions
 * 
 * @author Jacob Shirley & Mike Smith University of Brighton
 */
public class Controller {

	private Model model; // Model of game
	private View view; // View of game

	private boolean leftDown = false;
	private boolean rightDown = false;

	public Controller(Model aBreakOutModel, View aBreakOutView) {
		model = aBreakOutModel;
		view = aBreakOutView;
		view.setController(this); // View could talk to controller
	}

	/**
	 * Decide what to do for each interaction from the user Called from the
	 * interaction code in the view
	 * 
	 * @param keyCode
	 *            The key pressed
	 */
	public void userKeyPressed(int keyCode) {
		// Key typed includes specials, -ve
		// Char is ASCII value
		switch (keyCode) // Character is
		{
		case -KeyEvent.VK_LEFT: // Left Arrow
			model.getBat().setAcceleration(new Vec2D(-Constants.BAT_ACCELERATION, 0));
			leftDown = true;
			break;
		case -KeyEvent.VK_RIGHT: // Right arrow
			model.getBat().setAcceleration(new Vec2D(Constants.BAT_ACCELERATION, 0));
			rightDown = true;
			break;
		case 'f':
			// Very fast ball movement now
			model.setFast(true);
			break;
		case 'n':
			// Normal speed
			model.setFast(false);
			break;
		case 'l':
			// Got to next level
			model.nextLevel();
			break;
		default:
			Debug.trace("Ch typed = %3d [%c]", keyCode, (char) keyCode);
		}
	}

	public void userKeyReleased(int keyCode) {
		// Key typed includes specials, -ve
		// Char is ASCII value
		switch (keyCode) // Character is
		{
		case -KeyEvent.VK_LEFT: // Left Arrow
			leftDown = false;
			if (!rightDown)
				model.getBat().setAcceleration(new Vec2D(0, 0));
			break;
		case -KeyEvent.VK_RIGHT: // Right arrow
			rightDown = false;
			if (!leftDown)
				model.getBat().setAcceleration(new Vec2D(0, 0));
			break;
		default:
			Debug.trace("Ch typed = %3d [%c]", keyCode, (char) keyCode);
		}
	}

}
