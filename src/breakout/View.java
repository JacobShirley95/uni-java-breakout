package breakout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import breakout.objects.Ball;
import breakout.objects.Brick;
import breakout.objects.GameObj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Displays a graphical view of the game of breakout. Uses Graphics2D would need
 * to be re-implemented for Android.
 * 
 * @author Mike Smith University of Brighton
 */
public class View extends JFrame implements Observer {
	private Controller controller;
	private GameObj bat; // The bat
	private GameObj ball; // The ball
	private List<Brick> bricks; // The bricks
	
	private int currentLevel = 0; // The current level index
	private int lives = 0; // The number of lives remaining
	private int score = 0; // The score
	private int frames = 0; // Frames output
	private JPanel contentPane;
	
	private Ball[] lifeBalls = new Ball[Constants.MAX_LIVES];

	public final int width; // Size of screen Width
	public final int height; // Size of screen Height

	/**
	 * Construct the view of the game
	 * 
	 * @param width
	 *            Width of the view pixels
	 * @param height
	 *            Height of the view pixels
	 */
	public View(int width, int height) {
		super("BreakOut");
		this.width = width;
		this.height = height;

		contentPane = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
		
				drawActualPicture((Graphics2D) g);
			}
			
			@Override
			public void update(Graphics g) {
				drawActualPicture((Graphics2D) g);
				super.update(g);
		
				
			}
		};

		contentPane.setDoubleBuffered(true);
		contentPane.setPreferredSize(new Dimension(width, height));
		setContentPane(contentPane);
		
		setResizable(false);
		// setPreferredSize(new Dimension(width, height)); // Size of window
		pack();
		setLocationRelativeTo(null);

		addKeyListener(new Transaction()); // Called when key press
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Timer.startTimer();
		
		int startPos = (int) (Constants.BALL_SIZE * Constants.MAX_LIVES);
		for (int i = 0; i < Constants.MAX_LIVES; i++)
			lifeBalls[i] = new Ball(width - 15 - startPos + (i * (Constants.BALL_SIZE + 5)), Constants.STAGE_HEIGHT, Constants.BALL_SIZE, Colour.RED);
	}

	/**
	 * Code called to draw the current state of the game Uses draw: Draw a shape
	 * fill: Fill the shape setPaint: Colour used drawString: Write string on
	 * display
	 * 
	 * @param g
	 *            Graphics context to use
	 */
	public void drawActualPicture(Graphics2D g) {
		final int RESET_AFTER = 200; // Movements
		frames++;
		
		synchronized (Model.class) // Make thread safe
		{
			// White background
			g.setPaint(Color.WHITE);
			g.fill(new Rectangle2D.Float(0, 0, width, height));

			Font font = new Font("Monospaced", Font.BOLD, 18);
			g.setFont(font);
	
			if (lives >= 0) {
				if (currentLevel < Constants.LEVELS.length) {
					displayGameObj(g, ball, true); // Display the Ball
					displayGameObj(g, bat, false); // Display the Bat
		
					for (Brick brick : bricks)
						displayBrick(g, brick);
					
					for (int i = Constants.MAX_LIVES - lives; i < Constants.MAX_LIVES; i++)
						displayGameObj(g, lifeBalls[i], true);
		
					// Display state of game
					g.setPaint(Color.black);
		
					String fmt = "BreakOut: Score = [%6d] fps=%5.1f";
					String text = String.format(fmt, score, frames / (Timer.timeTaken() / 1000.0));
					if (frames > RESET_AFTER) {
						frames = 0;
						Timer.startTimer();
					}
		
					g.drawString(text, 17, Constants.STAGE_HEIGHT);
				} else {
					g.setPaint(Color.black);
					drawCentreString(g, Constants.WIN_MESSAGE, font);
				}
			} else {
				g.setPaint(Color.black);
				drawCentreString(g, Constants.LOSE_MESSAGE, font);
			}
		}
	}
	
	private void drawCentreString(Graphics g, String str, Font font) {
		//Copied from stackexchange (http://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java)
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = (width - metrics.stringWidth(Constants.LOSE_MESSAGE)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
	    
	    g.drawString(str, x, y);
	}

	private void displayGameObj(Graphics2D g, GameObj go, boolean circle) {
		g.setColor(go.getColour().forSwing());

		Shape shape = null;
		Vec2D pos = go.getPos();
		if (circle) {
			shape = new Ellipse2D.Double(pos.x, pos.y, go.getWidth(), go.getHeight());
		} else {
			shape = new Rectangle2D.Double(pos.x, pos.y, go.getWidth(), go.getHeight());
		}
		g.fill(shape);
	}

	private void displayBrick(Graphics2D g, Brick brick) {
		Image image = brick.getStyle().getImage();

		Vec2D pos = brick.getPos();
		g.drawImage(image, (int) pos.x, (int) pos.y, (int) Constants.BRICK_WIDTH, (int) Constants.BRICK_HEIGHT, null);
	}

	/**
	 * Called indirectly from the model when its state has changed
	 * 
	 * @param aModel
	 *            Model to be displayed
	 * @param arg
	 *            Any arguments (Not used)
	 */
	@Override
	public void update(Observable aModel, Object arg) {
		Model model = (Model) aModel;
		// Get from the model the ball, bat, bricks & score
		ball = model.getBall(); // Ball
		bricks = model.getBricks(); // Bricks
		bat = model.getBat(); // Bat
		score = model.getScore(); // Score
		lives = model.getLives(); // Lives
		currentLevel = model.getCurrentLevel(); // The current level
		
		// Debug.trace("Update");
		repaint(); // Re draw game
		
	}

	private BufferedImage theAI; // Alternate Image
	private Graphics2D theAG; // Alternate Graphics

	/**
	 * Double buffer graphics output to avoid flicker
	 * 
	 * @param g
	 *            The graphics context
	 */
	private void drawPicture(Graphics2D g) // Double buffer
	{ // to avoid flicker
		if (bricks == null)
			return; // Race condition
		if (theAG == null) {
			Dimension d = getSize(); // Size of curr. image
			theAI = (BufferedImage) createImage(d.width, d.height);
			theAG = theAI.createGraphics();
		}
		drawActualPicture(theAG); // Draw Actual Picture
		g.drawImage(theAI, 0, 0, this); // Display on screen
	}

	/**
	 * Need to be told where the controller is
	 * 
	 * @param aPongController
	 *            The controller used
	 */
	public void setController(Controller aPongController) {
		controller = aPongController;
	}

	/**
	 * Methods Called on a key press calls the controller to process
	 */
	private class Transaction implements KeyListener // When character typed
	{
		@Override
		public void keyPressed(KeyEvent e) // Obey this method
		{
			// Make -ve so not confused with normal characters
			controller.userKeyPressed(-e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Called on key release including specials
			controller.userKeyReleased(-e.getKeyCode());
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// Send internal code for key
			controller.userKeyPressed(e.getKeyChar());
		}
	}
}
