package breakout.levels;
import java.util.List;

import breakout.objects.Brick;

/**
 * An interface describing how to build a brick map.
 * @author Jacob Shirley
 */
public interface BrickMapBuilder {
	/**
	 * Generate an array of bricks.
	 * @return 
	 * 		An array of bricks.
	 */
	public List<Brick> build();
}
