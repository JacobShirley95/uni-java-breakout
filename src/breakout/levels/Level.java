package breakout.levels;

import java.util.List;

import breakout.objects.Brick;

/**
 * A class containing information about a level
 * @author Jacob
 */
public class Level {
	private BrickMapBuilder mapBuilder;

	private double minBallSpeed;

	private double batWidth;
	private double batSpeedTransferMod;

	
	
	/**
	 * Constructs the level.
	 * @param mapBuilder
	 * 		The mapBuilder will construct the bricks.
	 * @param minBallSpeed
	 * 		The minimum speed the ball can go at (and the speed at which it starts).
	 * @param batWidth
	 * 		The width of the bat.
	 * @param batSpeedTransferMod
	 * 		The modifier of the bat speed applied to the ball after they have collided.
	 */
	public Level(BrickMapBuilder mapBuilder, double minBallSpeed, double batWidth,
			double batSpeedTransferMod) {
		this.mapBuilder = mapBuilder;

		this.minBallSpeed = minBallSpeed;

		this.batWidth = batWidth;
		this.batSpeedTransferMod = batSpeedTransferMod;
	}

	/**
	 * Builds the brick map.
	 * @return
	 * 		The bricks.
	 */
	public List<Brick> build() {
		return this.mapBuilder.build();
	}
	
	/**
	 * @return
	 * 		Get the minimum ball speed.
	 */
	public double getMinBallSpeed() {
		return minBallSpeed;
	}

	/**
	 * @return
	 * 		Get the bat width.
	 */
	public double getBatWidth() {
		return batWidth;
	}
	
	/**
	 * @return
	 * 		Get the bat speed transfer modifier.
	 */
	public double getBatSpeedTransferMod() {
		return batSpeedTransferMod;
	}
}
