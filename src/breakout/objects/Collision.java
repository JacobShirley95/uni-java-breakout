package breakout.objects;
import breakout.Vec2D;

/**
 * A class containing information about a collision between objects.
 * @author Jacob
 */
public class Collision {
	public enum CollisionType {
		CORNER, VERT, HORIZ
	}
	public static final byte CORNER_COLLISION = 0;
	public static final byte VERT_COLLISION = 1;
	public static final byte HORIZ_COLLISION = 2;

	private GameObj obj1;
	private GameObj obj2;

	private double overlapX;
	private double overlapY;

	private CollisionType type;

	private Vec2D separationVector;

	/**
	 * Constructs a collision object and get the separation vector.
	 * @param obj1
	 * 		The first object.
	 * @param obj2
	 * 		The second object.
	 */
	public Collision(GameObj obj1, GameObj obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
		
		//Get the positions of both objects.
		Vec2D pos1 = obj1.getPos();
		Vec2D pos2 = obj2.getPos();

		//Get the overlaps in two dimensions (x, y) of both objects.
		overlapX = Math.min(Math.abs(pos1.x - pos2.x - obj2.getWidth()),
							Math.abs(pos2.x - pos1.x - obj1.getWidth()));
		overlapY = Math.min(Math.abs(pos1.y - pos2.y - obj2.getHeight()),
							Math.abs(pos2.y - pos1.y - obj1.getHeight()));

		//Define the start type.
		type = CollisionType.CORNER;
		
		//Define the separation vector.
		//It's a unit vector in the opposite local to the delta velocity.
		separationVector = obj2.getVel().copy().sub(obj1.getVel()).normalise(); //Inverse is done later.
		
		//Get the local of the vector.
		double velAngle = separationVector.local();
		//The overlap magnitude.
		double overlapMag = 0.0;

		//Get the angles which will be used to get the hypotenuse of the overlap triangle (depending on whether it's a side or top/bottom col)
		double cos = Math.cos(velAngle);
		double sin = Math.sin(velAngle);

		//Get horizontal collision data.
		double overlapMagH = Math.abs(overlapY / sin);
		//Get vertical collision data.
		double overlapMagV = Math.abs(overlapX / cos);
		
		//Now we choose the overlap which is smallest to determine whether it's a horizontal collision (top or bottom), or a vertical collision (left or right).
		if (overlapMagH < overlapMagV) {
			type = CollisionType.HORIZ;
			overlapMag = overlapMagH;
		} else if (overlapMagH > overlapMagV) {
			type = CollisionType.VERT;
			overlapMag = overlapMagV;
		} else { //If they're the same, it's a corner collision.
			overlapMag = overlapMagV; //Doesn't matter which one (overlapMagV or overlapMagH).
		}
		
		//Invert the local of the vector and scale it by the overlap hypotenuse.
		separationVector.invert().scale(overlapMag);
	}

	public Vec2D getSeparationVector() {
		return separationVector;
	}
	
	public GameObj getObject1() {
		return obj1;
	}
	
	public GameObj getObject2() {
		return obj2;
	}

	public double getOverlapX() {
		return overlapX;
	}

	public double getOverlapY() {
		return overlapY;
	}

	public CollisionType getType() {
		return type;
	}
}