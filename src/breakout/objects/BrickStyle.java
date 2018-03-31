package breakout.objects;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import breakout.Colour;
import breakout.Constants;
import breakout.Debug;

/**
 * A class defining various styles of the brick.
 * @author Jacob Shirley
 */
public enum BrickStyle {
	BASIC(Colour.BLUE, Colour.BLACK, 50),

	BREAKABLE(Colour.RED, Colour.BLACK, 20), 
	BREAK_STATE_1(Colour.RED, "resources/bricks/breakable/breakable_1.png", Colour.BLACK, 20), 
	BREAK_STATE_2(Colour.RED, "resources/bricks/breakable/breakable_2.png", Colour.BLACK, 20),

	UNBREAKABLE(Colour.GRAY, Colour.BLACK, 0);

	private Image image;
	private int hitScore;
	private Graphics2D g;

	BrickStyle(String file, int hitScore) {
		try {
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			Debug.error("Error loading image from file, message: %s", e.getMessage());

			e.printStackTrace();
		}
		
		g = (Graphics2D) image.getGraphics();
		this.hitScore = hitScore;
	}

	BrickStyle(String file, Colour outline, int hitScore) {
		this(file, hitScore);

		drawOutline(g, outline);
	}

	BrickStyle(Colour colour, String file, int hitScore) {
		this(colour, hitScore);

		try {
			g.drawImage(ImageIO.read(new File(file)), 0, 0, (int) Constants.BRICK_WIDTH, (int) Constants.BRICK_HEIGHT, null);
		} catch (IOException e) {
			Debug.error("Error loading image from file, message: %s", e.getMessage());

			e.printStackTrace();
		}
		
		this.hitScore = hitScore;
	}

	BrickStyle(Colour colour, String file, Colour outline, int hitScore) {
		this(colour, file, hitScore);

		drawOutline(g, outline);
	}

	BrickStyle(Colour colour, int hitScore) {
		this.hitScore = hitScore;
		image = new BufferedImage((int) Constants.BRICK_WIDTH, (int) Constants.BRICK_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		g = (Graphics2D) image.getGraphics();
		g.setColor(colour.forSwing());
		g.fillRect(0, 0, (int) Constants.BRICK_WIDTH, (int) Constants.BRICK_HEIGHT);
	}

	BrickStyle(Colour colour, Colour outline, int hitScore) {
		this(colour, hitScore);

		drawOutline(g, outline);
	}

	private void drawOutline(Graphics2D g, Colour c) {
		g.setColor(c.forSwing());
		g.setStroke(new BasicStroke(4));
		g.drawRect(0, 0, (int) Constants.BRICK_WIDTH, (int) Constants.BRICK_HEIGHT);
	}

	public Image getImage() {
		return image;
	}

	public int getHitScore() {
		return hitScore;
	}
}