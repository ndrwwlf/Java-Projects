import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

public class Capture extends MiniGame {
	private FontMetrics fontMetrics;
	private AffineTransform at;
	private static final Color bgColor = new Color(192, 230, 192);

	private long nextBox;

	private Rectangle2D box;
	private Point2D boxPosition;
	private static final int boxSide = 32;

	private Square[] squares; // Contains active squares

	public Capture(FontMetrics fm) {
		fontMetrics = fm;

		// Store base graphics
		box = new Rectangle2D.Double(-boxSide / 2, -boxSide / 2, boxSide, boxSide);
		nextBox = (long) (70.5 * 1000.0);

		squares = new Square[5];

		reset();

		setFocusable(false);
		setBackground(bgColor);
	}

	public Square createSquare() {
		double x = (Math.random() - 0.5) * (getWidth() - boxSide);
		double y = (Math.random() - 0.5) * (getHeight() - boxSide);
		return new Square(x, y, boxSide);
	}

	public void reset() {
		setBackground(bgColor);
		boxPosition = new Point2D.Double(0, 0);
	}

	public void moveBox(double x, double y) {
		boxPosition.setLocation(clamp(boxPosition.getX() + x, -getWidth() / 2 + boxSide / 2, getWidth() / 2 - boxSide / 2),
								clamp(boxPosition.getY() + y, -getHeight() / 2 + boxSide / 2, getHeight() / 2 - boxSide / 2));
	}

	public void update(long elapsedms) {
		for(int i = 0; i < squares.length; i++) {
			if(squares[i] == null && elapsedms >= nextBox) {
				squares[i] = createSquare();
				nextBox += Math.random() * 4000 + 1000;
			}
			else if(squares[i] != null && squares[i].getBox().intersects(box.getX(), box.getY(), box.getWidth(), box.getHeight())) {
				squares[i].stopCountdown();
				squares[i] = null;
			}
		}
	}

	public boolean gameOver() {
		for(Square square : squares) {
			if(square != null && square.isDead()) {
				return true;
			}
		}
		return false;
	}

	public void pause(boolean paused) {
		if(paused) {
			for(Square square : squares) {
				if(square != null) {
					square.stopCountdown();
				}
			}
		}
		else {
			for(Square square : squares) {
				if(square != null) {
					square.startCountdown();
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		Graphics2D g2d = (Graphics2D) g.create();

		g2d.translate(centerX, centerY); // Set Graphics2D transform origin to center of panel

		g2d.setColor(new Color(0, 153, 0));
		box.setFrame(-boxSide / 2 + boxPosition.getX(), -boxSide / 2 + boxPosition.getY(), boxSide, boxSide);
		g2d.fill(box);
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(3));
		for(Square square : squares) {
			if(square != null) {
				g2d.draw(square.getBox());
				String num = Integer.toString(square.getCountdown());
				Rectangle2D bounds = fontMetrics.getStringBounds(num, g2d);
				double w = square.getBox().getWidth() / 2;
				int x = (int) (square.getBox().getX() + w + bounds.getX() - bounds.getWidth() / 2);
				int y = (int) (square.getBox().getY() + w - bounds.getY() - bounds.getHeight() / 2);
				g2d.drawString(num, x, y);
			}
		}
		g2d.dispose();
	}
}