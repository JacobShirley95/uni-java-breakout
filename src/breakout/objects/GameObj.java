package breakout.objects;
import breakout.Colour;
import breakout.Vec2D;

/**
 * An Object in the game, represented as a rectangle. Which holds details of
 * shape, plus possible local of travel. Would be better to use inheritance.
 * 
 * @author Mike Smith University of Brighton
 */
public class GameObj {
	// All the variables below are vital to the state of the object
	protected boolean canSee = true; // Can see
	protected Vec2D pos = null;
	protected float width = 0.0f; // Width of object
	protected float height = 0.0f; // Height of object
	protected Colour colour; // Colour of object
	protected Vec2D vel = new Vec2D(0, 0);
	protected Vec2D acceleration = new Vec2D(0, 0);
	protected double frictionConst = 1.0;

	/**
	 * Constructor for a game object (x,y width, height, colour)
	 * 
	 * @param x
	 *            co-ordinate of the game object
	 * @param y
	 *            co-ordinate of the game object
	 * @param widthIs
	 *            width of the game object
	 * @param heightIs
	 *            height of the game object
	 * @param c
	 *            Colour of the game object
	 */
	public GameObj(float x, float y, float widthIs, float heightIs, Colour c) {
		pos = new Vec2D(x, y);
		width = widthIs;
		height = heightIs;
		colour = c;
	}

	public GameObj(float x, float y, Colour c) {
		this(x, y, 0, 0, null);
	}

	public GameObj(float x, float y) {
		this(x, y, null);
	}

	/**
	 * Set the game object visibility
	 * 
	 * @param state
	 *            is visible true or false
	 */
	public void setVisibility(boolean state) {
		canSee = state;
	}

	/**
	 * Is the game object visible
	 * 
	 * @return visibility true/false
	 */
	public boolean isVisible() {
		return canSee;
	}

	/**
	 * The X co-ordinate of the top left hand corner of the Game Object
	 * 
	 * @return x co-ordinate of the game Object
	 */
	public Vec2D getPos() {
		return pos;
	}

	public void setPos(Vec2D pos) {
		this.pos = pos;
	}

	public void setVel(Vec2D vel) {
		this.vel = vel;
	}
	
	public Vec2D getCentre() {
		return this.pos.copy().add(new Vec2D(this.width / 2, this.height / 2));
	}

	public Vec2D getVel() {
		return vel;
	}

	/**
	 * The width of the game object
	 * 
	 * @return The width of the game Object
	 */

	public float getWidth() {
		return width;
	}

	/**
	 * The height of the game object
	 * 
	 * @return The height of the game Object
	 */

	public float getHeight() {
		return height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * The colour of the game object
	 * 
	 * @return The colour of the game object
	 */
	public Colour getColour() {
		return colour;
	}

	public void update() {
		this.vel.add(acceleration);
		this.vel.scale(frictionConst);
		this.pos.add(vel);
	}

	public Vec2D getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vec2D acceleration) {
		this.acceleration = acceleration;
	}

	public double getFrictionConst() {
		return frictionConst;
	}

	public void setFrictionConst(double frictionConst) {
		this.frictionConst = frictionConst;
	}

	/**
	 * Change local of future moves in the X local
	 */
	public void changelocalX() {
		this.vel.mul(new Vec2D(-1, 1));
	}

	/**
	 * Change local of future moves in the Y local
	 */
	public void changelocalY() {
		this.vel.mul(new Vec2D(1, -1));
	}

	/**
	 * Detect a collision between two GameObjects Would be good to know where
	 * the object is hit
	 * 
	 * @param obj
	 *            Game object to see if 'hit' by
	 * @return collision True/ False
	 */

	public Collision hitBy(GameObj obj) {
		if (!(pos.x >= obj.pos.x + obj.width || pos.x + width <= obj.pos.x || pos.y >= obj.pos.y + obj.height
				|| pos.y + height <= obj.pos.y)) {
			return new Collision(this, obj);
		} else
			return null;

	}
}
