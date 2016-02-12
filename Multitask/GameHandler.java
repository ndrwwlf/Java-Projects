import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameHandler extends JPanel implements ActionListener, KeyListener/*, JavaArcade */ {
	private javax.swing.Timer timer; // Controls how often stats are checked
	private int width, height;
	private Font font;
	private FontMetrics fontMetrics;

	private long startTime = 0;
	private boolean gameActive = false;
	private ArrayList<MiniGame> games;
	private int points;
	private int highScore = 0;

	public GameHandler() {
		font = new Font("Courier", Font.PLAIN, 14);
		fontMetrics = this.getFontMetrics(font);

		points = 0;
		games = new ArrayList<MiniGame>(4);
		games.add(new Balance());
		games.add(new Balance());
		// games.add(new Dodge());
		// games.add(new Squares());
		// games.add(new Helicopter());

		width = 600;
		height = 450;
		timer = new javax.swing.Timer((int) (1000 / 60), this); // 60 ticks per second clock

		addKeyListener(this); // Key controls

		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.GRAY);
		setPreferredSize(new Dimension(width, height));

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		this.add(Box.createVerticalStrut(fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent()), c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;

		for(int i = 0; i < games.size(); i++) {
			if(games.size() == 2) {
				c.gridx = i;
				c.gridy = 1;
			}
			else if(games.size() == 3) {
				if(i == 0) {
					c.gridheight = 2;
					c.gridx = 0;
					c.gridy = 1;
				}
				else {
					c.gridheight = 1;
					c.gridx = 1;
					c.gridy = i;
				}
			}
			else if(games.size() == 4) {
				c.gridx = i % 2;
				c.gridy = (i == 0 ? 1 : i);
			}
			this.add(games.get(i), c);
		}
	}

	public void startGame() {
		timer.start();
		gameActive = true;
	}

	public void endGame() {
		timer.stop();
		gameActive = false;
	}

	public void pauseGame() {
		timer.stop();
		gameActive = false;
	}

	public String getGameName() {
		return "Multitask";
	}

	public int getHighScore() {
		return highScore;
	}

	public String getInstructions() {
		return "";
	}

	public String getCredits() {
		return "";
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, width, fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent());

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(Integer.toString(points), 5, fontMetrics.getMaxAscent());
	}

	public void actionPerformed(ActionEvent e) {
		if(gameActive) {
			points = (int) ((e.getWhen() - startTime) / 1000);
			for(MiniGame game : games) {
				game.update();
			}
			repaint();
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				((Balance) games.get(0)).updateAngularVelocity(0.5);
				break;
			case KeyEvent.VK_LEFT:
				((Balance) games.get(0)).updateAngularVelocity(-0.5);
				break;
			case KeyEvent.VK_ENTER:
				System.out.println("Enter");
				timer.start();
				startTime = System.currentTimeMillis();
				gameActive = true;
				break;
			case KeyEvent.VK_SPACE:
				System.out.println("Space");
				timer.stop();
				gameActive = false;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
	}
}