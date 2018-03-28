package breakout.objects;
import breakout.Colour;

/**
 * A class describing a ball.
 * @author Jacob
 */
public class Ball extends GameObj {
	public double vortexAngleInc = 0.0;
	
	
	/**
	 * Constructs a ball object.
	 * @param x
	 * 		The x coordinate of the ball.
	 * @param y
	 * 		The y coordinate of the ball.
	 * @param radius
	 * 		The radius of the ball.
	 * @param colour
	 * 		The colour of the ball.
	 */
	public Ball(float x, float y, float radius, Colour colour) {
		super(x, y, radius, radius, colour);
	}

	@Override
	public void update() {
		//Rotate the velocity by a vortex angle inc.
		vel.rotate(vortexAngleInc);
		
		super.update();
	}

	public void setSwerve(double i) {
		this.vortexAngleInc = i;
	}
}
