package breakout.levels.loader;

import breakout.levels.Level;

/**
 * An interface defining how levels are loaded.
 * @author Jacob
 */
public interface LevelLoader {
	/**
	 * Loads a level from a path.
	 * @param path
	 * 		The path to the level file.
	 * @return 
	 * 		A level object.
	 */
	public Level load(String path);
}
