package breakout.levels;
import java.util.Enumeration;
import breakout.Constants;
import breakout.objects.Brick;
import breakout.objects.BrickStyle;

/**
 * This class enumerates through an array of characters, generating the appropriate brick from the character.
 * 
 * @author Jacob
 */
public class BrickCharsEnumator implements Enumeration<Brick> {
	private char[] chars;
	private int curIndex;
	private float x;
	private float y;

	/**
	 * Constructors the enumerator.
	 * @param chars
	 * 		The characters which will be used to generate the bricks.
	 */
	public BrickCharsEnumator(char[] chars) {
		this.chars = chars;
		this.curIndex = 0;
		this.x = (float) 0.0;
		this.y = (float) 0.0;
	}
	
	@Override
	public boolean hasMoreElements() {
		return this.curIndex < this.chars.length;
	}

	@Override
	public Brick nextElement() {
		if (x >= Constants.STAGE_WIDTH) {
			x = 0;
			y += Constants.BRICK_HEIGHT;
		}

		char c = chars[curIndex];
		curIndex++;

		Brick brick = null;

		switch (c) {
		case 'u':
			brick = new Brick(x, y, BrickStyle.BASIC);
			break;
		case 'b':
			brick = new Brick(x, y, BrickStyle.BREAKABLE);
			break;
		case 'x':
			brick = new Brick(x, y, BrickStyle.UNBREAKABLE);
			break;
		}

		x += Constants.BRICK_WIDTH;
		return brick == null ? null : brick;
	}
}
