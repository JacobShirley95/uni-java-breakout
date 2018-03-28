package breakout;
import java.util.List;
import java.util.Observable;

import javax.xml.parsers.ParserConfigurationException;

import breakout.levels.Level;
import breakout.levels.loader.LevelLoader;
import breakout.levels.loader.XMLLevelLoader;
import breakout.objects.Ball;
import breakout.objects.Brick;
import breakout.objects.BrickStyle;
import breakout.objects.Collision;
import breakout.objects.GameObj;
import breakout.objects.Collision.CollisionType;

/**
 * Model of the game of breakout
 * 
 * @author Mike Smith University of Brighton
 */

public class Model extends Observable {

	private Ball ball; // The ball
	private List<Brick> bricks; // The bricks
	private GameObj bat; // The bat
	private Level level;
	private LevelLoader levelLoader;

	private boolean runGame = true; // Game running
	private boolean fast = false; // Sleep in run loop

	private int currentLevel = 0;
	private int score = 0;
	private int lives = Constants.MAX_LIVES;
	private float batWidth = Constants.BAT_WIDTH_INIT;

	private final float width; // Width of area
	private final float height; // Height of area

	public Model(int width, int height) {
		this.width = width;
		this.height = height;
		
		try {
			levelLoader = new XMLLevelLoader();
		} catch (ParserConfigurationException e) {
			Debug.error("Failed to create XML Loader, message: %s", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the number of destructible bricks remaining (any brick that is not a grey one).
	 * @return The number of destructible bricks.
	 */
	public int countDestructibles() {
		int r = 0;
		for (Brick b : bricks)
			if (!b.getStyle().equals(BrickStyle.UNBREAKABLE))
				r++;
		return 1;
	}

	/**
	 * Create in the model the objects that form the game
	 */

	public void createGameObjects() {
		synchronized (Model.class) {
			//load the level
			level = levelLoader.load(Constants.LEVELS_ROOT + Constants.LEVELS[currentLevel]);
			
			//build the level
			bricks = level.build();
			
			//initialise the ball
			ball = new Ball((width / 2) - (Constants.BALL_SIZE / 2), (height / 2) + 100, Constants.BALL_SIZE, Colour.RED);
			ball.setVel(new Vec2D(0, 1).scale(level.getMinBallSpeed()).scale(1.2));
			
			//initialise the bat
			bat = new GameObj((width / 2) - (batWidth / 2), height - Constants.BRICK_HEIGHT * 1.5f, (float) level.getBatWidth(), Constants.BRICK_HEIGHT / 3, Colour.GRAY);
			bat.setVel(new Vec2D(0, 0));
			bat.setFrictionConst(Constants.BAT_FRICTION_CONST);
		}
	}

	private ActivePart active = null;

	/**
	 * Start the continuous updates to the game
	 */
	public void startGame() {
		synchronized (Model.class) {
			stopGame();
			active = new ActivePart();
			Thread t = new Thread(active::runAsSeparateThread);
			t.setDaemon(true); // So may die when program exits
			t.start();
		}
	}

	/**
	 * Stop the continuous updates to the game Will freeze the game, and let the
	 * thread die.
	 */
	public void stopGame() {
		synchronized (Model.class) {
			if (active != null) {
				active.stop();
				active = null;
			}
		}
	}

	public GameObj getBat() {
		return bat;
	}

	public GameObj getBall() {
		return ball;
	}

	public List<Brick> getBricks() {
		return bricks;
	}

	/**
	 * Add to score n units
	 * 
	 * @param n
	 *            units to add to score
	 */
	protected void addToScore(int n) {
		score += n;
	}

	public int getScore() {
		return score;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Set speed of ball to be fast (true/ false)
	 * 
	 * @param fast
	 *            Set to true if require fast moving ball
	 */
	public void setFast(boolean fast) {
		this.fast = fast;
	}

	/**
	 * Move the bat. (-1) is left or (+1) is right
	 * 
	 * @param local
	 *            The local to move
	 */
	public void moveBat() {
		// *[2]******************************************************[2]*
		// * Fill in code to prevent the bat being moved off the screen *
		// **************************************************************
		double dist = bat.getVel().x; // Actual distance to move
		Vec2D pos = bat.getPos();

		if (Math.abs(dist) >= Constants.MAX_BAT_SPEED) {
			bat.setAcceleration(new Vec2D(0, 0));
		}

		bat.update();

		if (pos.x + dist <= 0) {
			pos.x = 0;
		} else if (pos.x + bat.getWidth() + dist >= width) {
			pos.x = width - bat.getWidth();
		}
		//Debug.trace("Model: Move bat = %6.2f", dist);
	}
	
	public void nextLevel() {
		currentLevel++;
		if (currentLevel < Constants.LEVELS.length)
			createGameObjects();
	}

	public boolean handleBallCollision(GameObj ball, GameObj collisionObj) {
		Collision collision = collisionObj.hitBy(ball);
		if (collision != null) {
			CollisionType type = collision.getType();
			Vec2D sepVec = collision.getSeparationVector();
			ball.getPos().add(sepVec);

			switch (type) {
			case CORNER: 
				Debug.trace("Corner collision");

				ball.changelocalX();
				ball.changelocalY();
				
				break;
			case HORIZ:
				Debug.trace("Horiz collision");

				ball.changelocalY();
				
				break;
			case VERT:
				Debug.trace("Vertical collision");

				ball.changelocalX();
				
				break;
			}

			return true;
		}
		return false;
	}

	/**
	 * This method is run in a separate thread Consequence: Potential concurrent
	 * access to shared variables in the class
	 */
	class ActivePart {
		private boolean runGame = true;

		public void stop() {
			runGame = false;
		}

		public void runAsSeparateThread() {
			try {
				synchronized (Model.class) // Make thread safe
				{
					GameObj ball = getBall(); // Ball in game
					GameObj bat = getBat(); // Bat
					List<Brick> bricks = getBricks(); // Bricks
				}

				final float MIN_X = 0;
				final float MAX_X = width - Constants.BALL_SIZE;

				final float MIN_Y = 0;
				final float MAX_Y = height - Constants.BALL_SIZE;

				while (runGame) {
					synchronized (Model.class) // Make thread safe
					{
						if (lives == -1 || currentLevel >= Constants.LEVELS.length) {
							stopGame();
						}

						Vec2D pos = ball.getPos();
						double x = pos.x; // Current x,y position
						double y = pos.y;

						boolean edgeCol = false;
						// Deal with possible edge of board hit
						if (x >= MAX_X) {
							edgeCol = true;
							ball.changelocalX();
							pos.x = MAX_X;
						} else if (x <= MIN_X) {
							edgeCol = true;
							ball.changelocalX();
							pos.x = MIN_X;
						} else if (y >= MAX_Y) {
							edgeCol = true;
							ball.changelocalY();
							pos.y = MAX_Y;

							addToScore(Constants.HIT_BOTTOM);
							
							if (score < 0) {
								score = 0;
								lives--;
								createGameObjects(); //restart the level
							}
						} else if (y <= MIN_Y) {
							edgeCol = true;
							ball.changelocalY();

							pos.y = MIN_Y;
						}
						

						if (edgeCol) {
							ball.setSwerve(0);
							if (ball.getVel().magnitude() > level.getMinBallSpeed())
								ball.getVel().scale(Constants.SIDE_COLLISION_CONST);
						}
						
						if (!bricks.isEmpty()) {
							//get closest brick to the ball to check for collisions with
							//initialise the variables
							
							Vec2D ballPos = ball.getCentre();
							double minDist = bricks.get(0).getCentre().distance(ballPos); //get the first distance to the brick
							int closestBrickIndex = 0;
							Brick closestBrick = bricks.get(0);
							
							
							//loop through the bricks
							for (int i = 1; i < bricks.size(); i++) {
								Brick brick = bricks.get(i);
								Vec2D brickPos = brick.getCentre(); //get the centre coordinates of the brick
								
								double temp = brickPos.distance(ballPos); //get the distance from the current brick
								if (temp < minDist) { //if it's less than the last recorded brick distance
									minDist = temp; //make this the new brick with the least distance away from the ball
									closestBrick = brick;
									closestBrickIndex = i; //set the closest brick index
								}
							}
							
							//check if the ball has collided with the closest brick
							if (handleBallCollision(ball, closestBrick)) {
								//set the swerve constant to zero
								ball.setSwerve(0);
								
								//check if the ball can slow down anymore (if it's not less than its minimum speed, scale it by the brick collision const)
								if (ball.getVel().magnitude() > level.getMinBallSpeed())
									ball.getVel().scale(Constants.BRICK_COLLISION_CONST);
	
								//get the current brick style
								BrickStyle type = closestBrick.getStyle();
								
								//increase the score
								addToScore(type.getHitScore());
								
								//handle each brick style correctly
								switch (type) {
								case BASIC: //if it's basic just remove it
									bricks.remove(closestBrickIndex);
									break;
								case BREAKABLE: //if it's unbroken set it's break state to 1
									closestBrick.setStyle(BrickStyle.BREAK_STATE_1);
									break;
								case BREAK_STATE_1: //if it's slightly broken make it very broken :)
									closestBrick.setStyle(BrickStyle.BREAK_STATE_2);
									break;
								case BREAK_STATE_2: //if it's it very broken destroy it
									bricks.remove(closestBrickIndex);
									break;
								case UNBREAKABLE: //if it's unbreakable do nothing
									break;
								}
							}
						}

						//handle the ball and bat collision
						if (handleBallCollision(ball, bat)) {
							//do bat-ball collision
							//add some of the bat's velocity to the ball
							ball.getVel().add(bat.getVel().copy().scale(level.getBatSpeedTransferMod()));

							double ballVel = ball.getVel().x;
							double spinDir = Math.signum(ballVel);
							
							//get the bat velocity
							double vel = Math.abs(ballVel);
							
							//do some magic (0.003 was just trial-and-error,
							ball.setSwerve(Constants.SWERVE_CONST * -vel * spinDir);
						}
						
						//if the number of destructible bricks is zero, go to next level
						if (countDestructibles() == 0) {
							nextLevel();
							continue;
						}
					}
					modelChanged(); // Model changed refresh screen
					Thread.sleep(fast ? 2 : 20);
					ball.update();

					moveBat();
				}
			} catch (Exception e) {
				Debug.error("Model.runAsSeparateThread - Error\n%s", e.getMessage());
			}
		}
	}

	/**
	 * Model has changed so notify observers so that they can redraw the current
	 * state of the game
	 */
	public void modelChanged() {
		setChanged();
		notifyObservers();
	}

}
