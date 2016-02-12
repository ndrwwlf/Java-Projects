import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

public class Balance extends MiniGame {
	private Ellipse2D ball;
	private double translationalVelocity;
	private double ballPosition;
	private double angle;
	private double angularVelocity;

	private Rectangle2D bar;
	private int barHeight = 12;
	private int barWidth = 200;

	public Balance() {
		bar = new Rectangle2D.Double(-barWidth / 2, -barHeight / 2, barWidth, barHeight);
		ball = new Ellipse2D.Double(-barHeight, -barHeight * 2.5, barHeight * 2, barHeight * 2);
		reset();

		setFocusable(false);
		setBackground(new Color(231, 179, 179));
	}

	public void updateAngularVelocity(double av) {
		angularVelocity += av * (1.0 + Math.abs(angle / 45));
		angularVelocity = clamp(angularVelocity, -1, 1);
	}

	public void reset() {
		setBackground(new Color(231, 179, 179));
		translationalVelocity = 0.001;
		ballPosition = 0.0;
		angle = 0.0;
		angularVelocity = 0.0;
	}

	public void update() {
		angle += angularVelocity;
		angle = clamp(angle, -45, 45);
		ballPosition += translationalVelocity;

		angularVelocity += ballPosition * 0.02;
		angularVelocity *= 0.98; // Angular velocity decay
		angularVelocity = clamp(angularVelocity, -1, 1);

		translationalVelocity = 0.0005 * angle;

		ball.setFrame(-barHeight + ballPosition * barWidth / 2, -barHeight * 2.5, barHeight * 2, barHeight * 2);
	}

	public boolean gameOver() {
		return ballPosition > 1 || ballPosition < -1;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		Graphics2D g2d = (Graphics2D) g.create();

		g2d.translate(centerX, centerY);
		g2d.rotate(Math.toRadians(angle), 0, 0);

		g2d.setColor(Color.BLACK);
		g2d.fill(bar);
		g2d.setColor(new Color(176, 0, 0));
		g2d.fill(ball);
		g2d.dispose();
		g.drawString("ballPosition: " + ballPosition, 0, 20);
		g.drawString("angularVelocity: " + angularVelocity, 0, 40);
		g.drawString("translationalVelocity: " + translationalVelocity, 0, 60);
	}
}