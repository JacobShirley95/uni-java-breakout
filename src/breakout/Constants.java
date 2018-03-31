package breakout;

/**
 * A class defining constants for the game.
 * @author Jacob Shirley
 */
public final class Constants {
	// Settings
	public static final String LEVELS_ROOT = "resources/levels/";
	public static final String[] LEVELS = new String[] {"level1.xml", "level2.xml", "level3.xml"};
	public static final int MAX_LIVES = 3;
	public static final String LOSE_MESSAGE = "You lost!";
	public static final String WIN_MESSAGE = "You win!";
	public static final double BAT_FRICTION_CONST = 0.9;
	public static final double SIDE_COLLISION_CONST = 0.9;
	public static final double BRICK_COLLISION_CONST = 0.8;
	public static final double SWERVE_CONST = 0.003;
	public static final boolean ENABLE_DEBUG = false;
	
	// Size of things
	public static final float BALL_SIZE = 15; // Ball side
	public static final float BRICK_WIDTH = 70; // Brick size
	public static final float BRICK_HEIGHT = 20;
	public static final int BRICK_ROW_LEN = 15;
	public static final float BAT_WIDTH_INIT = 100;

	// Offsets
	public static final int HUD_HEIGHT = 20; // HUD height

	// Speeds and accelerations
	public static final double BAT_ACCELERATION = 1.0;
	public static final int MAX_BAT_SPEED = 10;

	// Scores
	public static final int HIT_BRICK = 50; // Score
	public static final int HIT_BOTTOM = -500;// Score

	// Stage dimensions
	public static final int STAGE_WIDTH = (int) (BRICK_WIDTH * BRICK_ROW_LEN); // Width of
																	// stage
	public static final int STAGE_HEIGHT = 500; // Height of stage

	// Window dimensions
	public static final int WINDOW_WIDTH = STAGE_WIDTH;
	public static final int WINDOW_HEIGHT = STAGE_HEIGHT + HUD_HEIGHT;
}
