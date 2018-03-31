package breakout.levels;
import java.util.ArrayList;
import java.util.List;

import breakout.objects.Brick;

/**
 * A brick map builder class that uses an array of characters to describe the configuration of bricks.
 * @author Jacob Shirley
 */
public class BrickCharsMapBuilder implements BrickMapBuilder {
	private char[] mapData;
	
	/**
	 * Constructs the char array map builder.
	 * @param mapData
	 * 		The characters used to generate the brick array.
	 */
	public BrickCharsMapBuilder(char[] mapData) {
		this.mapData = mapData;
	}

	/* (non-Javadoc)
	 * @see tech.conexus.breakout.levels.BrickMapBuilder#build()
	 */
	@Override
	public List<Brick> build() {
		BrickCharsEnumator enuma = new BrickCharsEnumator(this.mapData);
		List<Brick> bricks = new ArrayList<>();

		while (enuma.hasMoreElements()) {
			Brick brick = enuma.nextElement();
			if (brick != null)
				bricks.add(brick);
			
		}

		return bricks;
	}
}
