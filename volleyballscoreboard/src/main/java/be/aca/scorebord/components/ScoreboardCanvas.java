package be.aca.scorebord.components;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import be.aca.scorebord.domain.Possesion;
import be.aca.scorebord.domain.ScoreboardModel;

public class ScoreboardCanvas extends Canvas {

	private ScoreboardModel model;
	private Slideshow slideshow;
	private BufferedImage frame;
	private BufferedImage ball;
	private BufferedImage ballsmall;
	
	private int previousIndex = 1; //The FPS and the middle counter for them
    private long[] previousTimes = new long[128];
    private boolean previousFilled = false;
    private double frameRate;
	
	private Point point = new Point(0, 0);
	
	public ScoreboardCanvas(final JFrame parent, final ScoreboardModel model, Slideshow slideshow) {
		this.model = model;
		this.slideshow = slideshow;

		//setIgnoreRepaint(true);
		previousTimes[0] = System.currentTimeMillis();
		
		setSize(1024, 768);
		setBackground(Color.BLACK);
		setLocation(0, 0);
		
		try {
			frame = (BufferedImage) ImageIO.read(ScoreboardCanvas.class.getResourceAsStream("/frame.png"));
			ball = (BufferedImage) ImageIO.read(ScoreboardCanvas.class.getResourceAsStream("/ball.png"));
			ballsmall = (BufferedImage) ImageIO.read(ScoreboardCanvas.class.getResourceAsStream("/ballsmall.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		addMouseListener(new MouseAdapter() {  
			public void mousePressed(MouseEvent e) {  
				if (!e.isMetaDown()) {  
					point.x = e.getX();  
					point.y = e.getY();  
				}  
				
				Arc2D.Double homeMain = new Arc2D.Double(300 + 2, 27, 170, 170, 30, 180, Arc2D.CHORD);
				if (homeMain.contains(e.getX(), e.getY())) {
					model.getHomeTeam().setMainColor(JColorChooser.showDialog(ScoreboardCanvas.this, "Choose " + model.getHomeTeam() + " color", model.getHomeTeam().getMainColor()));
				}
				
				Arc2D.Double homeSub = new Arc2D.Double(300 + 2, 27, 170, 170, 210, 180, Arc2D.CHORD);
				if (homeSub.contains(e.getX(), e.getY())) {
					model.getHomeTeam().setSubColor(JColorChooser.showDialog(ScoreboardCanvas.this, "Choose " + model.getHomeTeam() + " color", model.getHomeTeam().getSubColor()));
				}
				
				Arc2D.Double awayMain = new Arc2D.Double(getWidth() - 300 - 175 + 2, 27, 170, 170, 30, 180, Arc2D.CHORD);
				if (awayMain.contains(e.getX(), e.getY())) {
					model.getAwayTeam().setMainColor(JColorChooser.showDialog(ScoreboardCanvas.this, "Choose " + model.getAwayTeam() + " color", model.getAwayTeam().getMainColor()));
				}
				
				Arc2D.Double awaySub = new Arc2D.Double(getWidth() - 300 - 175 + 2, 27, 170, 170, 210, 180, Arc2D.CHORD);
				if (awaySub.contains(e.getX(), e.getY())) {
					model.getAwayTeam().setSubColor(JColorChooser.showDialog(ScoreboardCanvas.this, "Choose " + model.getAwayTeam() + " color", model.getAwayTeam().getSubColor()));
				}
			}  
		});  
			 
		addMouseMotionListener(new MouseMotionAdapter() {  
			 public void mouseDragged(MouseEvent e) {  
				 if (!e.isMetaDown()) {  
					 Point p = parent.getLocation();  
					 parent.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);  
				 }  
			 }  
		});  
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		long now = System.currentTimeMillis();
		int nbrOfFrames = previousTimes.length;
		double newRate;
		if (previousFilled) {
			newRate = (double) nbrOfFrames / (double) (now - previousTimes[previousIndex]) * 1000.0;
		} else {
			newRate = 1000.0 / (double) (now - previousTimes[nbrOfFrames - 1]);
		}
		
		frameRate = newRate;
		previousTimes[previousIndex] = now;
		previousIndex++;
		if (previousIndex >= nbrOfFrames) {
			previousIndex = 0;
			previousFilled = true;
		}
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		Font normalFont = new Font("Helvetica", Font.BOLD, 50);
		Font largeFont = new Font("Helvetica", Font.BOLD, 150);
		FontMetrics metrics = getFontMetrics(normalFont);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(normalFont);
		
		// Home + Away
		// TODO calculate name length, resize when needed
		int fontSize = getMaximumFontSize(model.getHomeTeam().getName(), 250, g2d);
		Font homeFont = new Font("Helvetica", Font.BOLD, fontSize);
		g2d.setFont(homeFont);
		int homeWidth = (int) getFontMetrics(homeFont).getStringBounds(model.getHomeTeam().getName(), g2d).getWidth();
		int homeHeight = (int) getFontMetrics(homeFont).getStringBounds(model.getHomeTeam().getName(), g2d).getHeight();
		
		// Middle V = (130 - 15) / 2 = 67
		// Middle H = (270 - 12) / 2 = 129, (1012 - 756) = 256
		g2d.drawString(model.getHomeTeam().getName(), 129 - (homeWidth / 2) + 12, 45 + (homeHeight / 2));
		
		fontSize = getMaximumFontSize(model.getAwayTeam().getName(), 250, g2d);
		Font awayFont = new Font("Helvetica", Font.BOLD, fontSize);
		g2d.setFont(awayFont);
		int awayWidth = (int) getFontMetrics(awayFont).getStringBounds(model.getAwayTeam().getName(), g2d).getWidth();
		int awayHeight = (int) getFontMetrics(awayFont).getStringBounds(model.getAwayTeam().getName(), g2d).getHeight();
		g2d.drawString(model.getAwayTeam().getName(), getWidth() - 128 - (awayWidth / 2) - 12, 45 + (awayHeight / 2));
		
		g2d.setColor(model.getHomeTeam().getMainColor());
		g2d.fillArc(300 + 2, 27, 170, 170, 30, 180);
		g2d.setColor(model.getHomeTeam().getSubColor());
		g2d.fillArc(300 + 2, 27, 170, 170, 210, 180);
		
		g2d.setColor(model.getAwayTeam().getMainColor());
		g2d.fillArc(getWidth() - 300 - 175 + 2, 27, 170, 170, 30, 180);
		g2d.setColor(model.getAwayTeam().getSubColor());
		g2d.fillArc(getWidth() - 300 - 175 + 2, 27, 170, 170, 210, 180);
		
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(largeFont);
		String points = String.format("%02d", model.getHomeTeam().getPoints());
		int pointsWidth = (int) metrics.getStringBounds(points, g2d).getWidth();
		g2d.drawString(points, 129 - pointsWidth - 18, 250);
		points = String.format("%02d", model.getAwayTeam().getPoints());
		pointsWidth = (int) metrics.getStringBounds(points, g2d).getWidth();
		g2d.drawString(points, getWidth() - 258 + pointsWidth - 22, 250);
		
		
		// Serving + sets
		// Middle V = 365 <-> 303 = 334 
		g2d.setFont(normalFont);
		int servingWidth = (int) metrics.getStringBounds("SERVING", g2d).getWidth();
		int gameWidth = (int) metrics.getStringBounds("SET # " + model.getGames(), g2d).getWidth();
		
		g2d.drawString("SET # " + model.getGames(), (getWidth() - gameWidth) / 2, 274);
		g2d.drawString("SERVING", (getWidth() - servingWidth) / 2, 352);
		g2d.setColor(Color.RED);
		if (model.getPossesion() == Possesion.HOME) {
			g2d.fillOval(300, 314, 40, 40);
		} else {
			g2d.fillOval(getWidth() - 340, 314, 40, 40);
		}
		
		g2d.setColor(Color.WHITE);
		int setsWidth = (int) metrics.getStringBounds("SETS", g2d).getWidth();
		int setsValueWidth = (int) metrics.getStringBounds("0", g2d).getWidth();
		g2d.drawString("SETS", 129 - (setsWidth / 2) + 12, 352);
		g2d.drawString(Integer.toString(model.getHomeTeam().getSets()), 129 - (setsValueWidth / 2) + 12, 428);
		g2d.drawString("SETS", getWidth() - 128 - (setsWidth / 2) - 12, 352);
		g2d.drawString(Integer.toString(model.getAwayTeam().getSets()), getWidth() - 128 - (setsValueWidth / 2) - 12, 428);
		
		
		// Timeout
		if (model.isTimeoutRunning()) {
			String timeout = String.format("%2.1f", (double) ((double) model.getTimeout() / 1000d));
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
		}
		
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
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g2d.drawImage(frame, 0, 0, frame.getWidth(this), frame.getHeight(this), this);
		
		g2d.drawImage(ball, 300, 25, 175, 175, this);
		g2d.drawImage(ball, getWidth() - 300 - 175, 25, 175, 175, this);

		if (model.getPossesion() == Possesion.HOME) {
			g2d.drawImage(ballsmall, 300, 314, 40, 40, this);
		} else {
			g2d.drawImage(ballsmall, getWidth() - 340, 314, 40, 40, this);
		}
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(normalFont);
		String fps = String.format("%2.1f", frameRate);
		
		g2d.drawString(fps, 0, 50);
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
