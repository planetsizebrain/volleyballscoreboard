package be.aca.scorebord.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import be.aca.scorebord.domain.Possesion;
import be.aca.scorebord.domain.ScoreboardModel;
import be.aca.scorebord.domain.Team;

public class ScoreboardPanel extends JPanel implements PropertyChangeListener {

	private static BufferedImage frame;
	private static BufferedImage ball;
	private static BufferedImage ballsmall;
	private Font normalFont = new Font("Helvetica", Font.BOLD, 50);
	private Font largeFont = new Font("Helvetica", Font.BOLD, 150);
	
	private ScoreboardModel model;
	private Slideshow slideshow;
	
	static {
		try {
			frame = (BufferedImage) ImageIO.read(ScoreboardPanel.class.getResourceAsStream("/frame.png"));
			ball = (BufferedImage) ImageIO.read(ScoreboardPanel.class.getResourceAsStream("/ball.png"));
			ballsmall = (BufferedImage) ImageIO.read(ScoreboardPanel.class.getResourceAsStream("/ballsmall.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public ScoreboardPanel(final JFrame parent, final ScoreboardModel model, Slideshow slideshow) {
		this.model = model;
		this.slideshow = slideshow;
		
		setSize(1024, 800);
		setDoubleBuffered(true);
		ScoreboardModel.INSTANCE.addPropertyChangeListener(this);
		slideshow.addPropertyChangeListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
		Insets i = getInsets();
		
		Graphics2D g2d = (Graphics2D) g.create();
		
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    Toolkit tk = Toolkit.getDefaultToolkit(); 
	    Map desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints")); 
	    if (desktopHints != null) { 
		    g2d.addRenderingHints(desktopHints); 
		} 
		
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(i.left, i.top, getWidth() - i.left - i.right, getHeight() - i.top - i.bottom);
		
		drawTeam(ScoreboardModel.INSTANCE.getHomeTeam(), g2d);
		g2d.translate(740, 0);
		drawTeam(ScoreboardModel.INSTANCE.getAwayTeam(), g2d);
		g2d.translate(-740, 0);
		
		drawTimeout(g2d);
		
		drawPossesionAndGames(g2d);
		
		drawSlideshow(g2d);
		
		drawBalls(g2d);
		
		drawOverlays(g2d);
		
		g2d.dispose();
	}	
	
	private void drawTimeout(Graphics2D g2d) {
		FontMetrics metrics = getFontMetrics(normalFont);
		
		ScoreboardModel model = ScoreboardModel.INSTANCE;
		if (model.isTimeoutRunning()) {
			String timeout = String.format("%2.1f", (double) ((double) model.getRunningTimeout() / 1000d));
			int timeoutWidth = (int) metrics.getStringBounds(timeout, g2d).getWidth();
			g2d.drawString(timeout, (getWidth() - timeoutWidth) / 2, 428);
		} else {
			int timeoutWidth = (int) metrics.getStringBounds("TIMEOUT", g2d).getWidth();
			g2d.drawString("TIMEOUT", (getWidth() - timeoutWidth) / 2, 428);
			
			g2d.setColor(Color.RED);
			int timeout = model.getHomeTeam().getTimouts();
			if (timeout >= 1) {
				g2d.fillOval(300, 390, 40, 40);
			}
			if (timeout == 2) {
				g2d.fillOval(350, 390, 40, 40);
			}
			timeout = model.getAwayTeam().getTimouts();
			if (timeout >= 1) {
				g2d.fillOval(getWidth() - 340, 390, 40, 40);
			}
			if (timeout == 2) {
				g2d.fillOval(getWidth() - 390, 390, 40, 40);
			}
			g2d.setColor(Color.WHITE);
		}
	}

	private void drawBalls(Graphics2D g2d) {
		g2d.setColor(ScoreboardModel.INSTANCE.getHomeTeam().getMainColor());
		g2d.fillArc(300 + 2, 27, 170, 170, 30, 180);
		g2d.setColor(ScoreboardModel.INSTANCE.getHomeTeam().getSubColor());
		g2d.fillArc(300 + 2, 27, 170, 170, 210, 180);
		
		g2d.setColor(ScoreboardModel.INSTANCE.getAwayTeam().getMainColor());
		g2d.fillArc(getWidth() - 300 - 175 + 2, 27, 170, 170, 30, 180);
		g2d.setColor(ScoreboardModel.INSTANCE.getAwayTeam().getSubColor());
		g2d.fillArc(getWidth() - 300 - 175 + 2, 27, 170, 170, 210, 180);
	}

	private void drawOverlays(Graphics2D g2d) {
		Insets i = getInsets();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2d.drawImage(frame, i.left, i.top, null);
		
		g2d.drawImage(ball, 300, 25, 175, 175, this);
		g2d.drawImage(ball, getWidth() - 300 - 175, 25, 175, 175, this);

		if (ScoreboardModel.INSTANCE.getPossesion() == Possesion.HOME) {
			g2d.drawImage(ballsmall, 300, 314, 40, 40, this);
		} else {
			g2d.drawImage(ballsmall, getWidth() - 340, 314, 40, 40, this);
		}
	}

	private void drawPossesionAndGames(Graphics2D g2d) {
		Font homeFont = new Font("Helvetica", Font.BOLD, 50);
		
		int gameWidth = (int)  getFontMetrics(homeFont).getStringBounds("SET # " + ScoreboardModel.INSTANCE.getGames(), g2d).getWidth();
		g2d.drawString("SET # " + ScoreboardModel.INSTANCE.getGames(), (getWidth() - gameWidth) / 2, 274);
		
		int servingWidth = (int) getFontMetrics(homeFont).getStringBounds("SERVING", g2d).getWidth();
		g2d.drawString("SERVING", (getWidth() - servingWidth) / 2, 352);
		g2d.setColor(Color.RED);
		if (ScoreboardModel.INSTANCE.getPossesion() == Possesion.HOME) {
			g2d.fillOval(300, 314, 40, 40);
		} else {
			g2d.fillOval(getWidth() - 340, 314, 40, 40);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
//		if (evt.getPropertyName().contains("possession")) {
//			repaint(300, 314, getWidth() - 340, 354);
//		} else if (evt.getPropertyName().contains("timeout")) {
//			repaint(290, 380, getWidth() - 380, 440);
//		} else if (evt.getPropertyName().contains("currentSlide")) {
//			repaint(0, 380, getWidth(), 400);
//		} else {
//			if (evt.getPropertyName().contains("home")) {
//				repaint(0, 0, 512, 800);
//			} else {
//				repaint(512, 0, 512, 800);
//			}
//		}
		repaint();
	}
	
	private void drawSlideshow(Graphics2D g2d) {
		if (slideshow != null) {
			try {
				Image image = slideshow.getCurrentSlide();
				int width = image.getWidth(this);
				int height = image.getHeight(this);
				float factor = (float) getWidth() / (float) width;
				int newHeight = (int) (height * factor);
				g2d.drawImage(slideshow.getCurrentSlide(), 0, getHeight() - newHeight, getWidth(), newHeight, this);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void drawTeam(Team team, Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.setFont(normalFont);
		
		// Home + Away
		// TODO calculate name length, resize when needed
		int fontSize = getMaximumFontSize(team.getName(), 250, g2d);
		Font homeFont = new Font("Helvetica", Font.BOLD, fontSize);
		g2d.setFont(homeFont);
		int homeWidth = (int) getFontMetrics(homeFont).getStringBounds(team.getName(), g2d).getWidth();
		int homeHeight = (int) getFontMetrics(homeFont).getStringBounds(team.getName(), g2d).getHeight();
		
		g2d.drawString(team.getName(), 129 - (homeWidth / 2) + 12, 45 + (homeHeight / 2));
		
		// POINTS
		g2d.setFont(largeFont);
		String points = String.format("%02d", team.getPoints());
		int pointsWidth = (int) getFontMetrics(largeFont).getStringBounds(points, g2d).getWidth();
		g2d.drawString(points, 140 - (pointsWidth / 2), 250);
		
		// SETS
		g2d.setFont(normalFont);
		int setsWidth = (int) getFontMetrics(normalFont).getStringBounds("SETS", g2d).getWidth();
		int setsValueWidth = (int) getFontMetrics(normalFont).getStringBounds(Integer.toString(team.getSets()), g2d).getWidth();
		g2d.drawString("SETS", 129 - (setsWidth / 2) + 12, 352);
		g2d.drawString(Integer.toString(team.getSets()), 129 - (setsValueWidth / 2) + 12, 428);
	}
	
	private int getMaximumFontSize(String text, int width, Graphics2D g2d) {
		int size = 50;
		Font font = new Font("Helvetica", Font.BOLD, size);
		FontMetrics metrics = getFontMetrics(font);
		
		int servingWidth = (int) metrics.getStringBounds(text, g2d).getWidth();
		while (servingWidth >= width) {
			font = new Font("Helvetica", Font.BOLD, --size);
			metrics = getFontMetrics(font);
			
			servingWidth = (int) metrics.getStringBounds(text, g2d).getWidth();
		}
		
		return size;
	}
}
