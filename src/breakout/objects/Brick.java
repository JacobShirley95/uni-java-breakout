package breakout.objects;
import breakout.Constants;

/**
 * A class containing information about a brick.
 * @author Jacob
 */
public class Brick extends GameObj {
	private BrickStyle style;
	
	/**
	 * Constructs the brick.
	 * @param x
	 * 		The x coordinate of the brick.
	 * @param y
	 * 		The y coordinate of the brick.
	 * @param style
	 * 		The style of the brick.
	 */
	public Brick(float x, float y, BrickStyle style) {
		super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, null);

		this.style = style;
	}

	public BrickStyle getStyle() {
		return style;
	}

	public void setStyle(BrickStyle style) {
		this.style = style;
	}
}
