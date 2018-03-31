package breakout;

/**
 * A class for doing basic vector math.
 * @author Jacob Shirley
 */
public class Vec2D {
	public double x;
	public double y;

	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double magnitudeSqr() {
		return (this.x * this.x) + (this.y * this.y);
	}

	public double magnitude() {
		return Math.sqrt(magnitudeSqr());
	}

	public Vec2D copy() {
		return new Vec2D(this.x, this.y);
	}
	
	public double distance(Vec2D vec) {
		double dx = this.x - vec.x;
		double dy = this.y - vec.y;
		
		return Math.sqrt((dx * dx) + (dy * dy));
	}

	public Vec2D rotate(double rad) {
		double x = this.x;
		double y = this.y;

		this.x = (x * Math.cos(rad)) - (y * Math.sin(rad));
		this.y = (x * Math.sin(rad)) + (y * Math.cos(rad));

		return this;
	}

	public Vec2D add(Vec2D vec) {
		this.x += vec.x;
		this.y += vec.y;

		return this;
	}

	public Vec2D sub(Vec2D vec) {
		this.x -= vec.x;
		this.y -= vec.y;

		return this;
	}

	public Vec2D mul(Vec2D vec) {
		this.x *= vec.x;
		this.y *= vec.y;

		return this;
	}

	public Vec2D scale(double val) {
		this.x *= val;
		this.y *= val;

		return this;
	}

	public double local() {
		return Math.atan2(this.y, this.x);
	}

	public Vec2D normalise() {
		return this.scale(1 / magnitude());
	}

	public Vec2D invert() {
		return this.scale(-1);
	}
}
