package be.aca.scorebord.application;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import be.aca.scorebord.components.ScoreboardControls;
import be.aca.scorebord.components.ScoreboardPanel;
import be.aca.scorebord.components.Slideshow;
import be.aca.scorebord.domain.ScoreboardModel;
import be.aca.scorebord.domain.Team;

public class ScoreBoard extends JFrame {

	private ScoreboardModel model;
	private Slideshow slideshow;
	private Point point = new Point(0, 0);
	
	public ScoreBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLocation(50, 50);
		setUndecorated(true);
		
		addMouseListener(new MouseAdapter() {  
			public void mousePressed(MouseEvent e) {  
				if (!e.isMetaDown()) {  
					point.x = e.getX();  
					point.y = e.getY();  
				}  
				
				Arc2D.Double homeMain = new Arc2D.Double(300 + 2, 27, 170, 170, 30, 180, Arc2D.CHORD);
				if (homeMain.contains(e.getX(), e.getY())) {
					Team team = ScoreboardModel.INSTANCE.getHomeTeam();
					team.setMainColor(JColorChooser.showDialog(ScoreBoard.this, "Choose " + team.getName() + " color", team.getMainColor()));
				}
				
				Arc2D.Double homeSub = new Arc2D.Double(300 + 2, 27, 170, 170, 210, 180, Arc2D.CHORD);
				if (homeSub.contains(e.getX(), e.getY())) {
					Team team = ScoreboardModel.INSTANCE.getHomeTeam();
					team.setSubColor(JColorChooser.showDialog(ScoreBoard.this, "Choose " + team.getName() + " color", team.getSubColor()));
				}
				
				Arc2D.Double awayMain = new Arc2D.Double(getWidth() - 300 - 175 + 2, 27, 170, 170, 30, 180, Arc2D.CHORD);
				if (awayMain.contains(e.getX(), e.getY())) {
					Team team = ScoreboardModel.INSTANCE.getAwayTeam();
					team.setMainColor(JColorChooser.showDialog(ScoreBoard.this, "Choose " + team.getName() + " color", team.getMainColor()));
				}
				
				Arc2D.Double awaySub = new Arc2D.Double(getWidth() - 300 - 175 + 2, 27, 170, 170, 210, 180, Arc2D.CHORD);
				if (awaySub.contains(e.getX(), e.getY())) {
					Team team = ScoreboardModel.INSTANCE.getAwayTeam();
					team.setSubColor(JColorChooser.showDialog(ScoreBoard.this, "Choose " + team.getName() + " color", team.getSubColor()));
				}
			}  
		});  
			 
		addMouseMotionListener(new MouseMotionAdapter() {  
			 public void mouseDragged(MouseEvent e) {  
				 if (!e.isMetaDown()) {  
					 Point p = getLocation();  
					 setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);  
				 }  
			 }  
		});  
		
//		tracker = new MediaTracker(scoreboard);
//		try {
//			int index = 0;
//			List<Image> slides = slideshow.getSlides();
//			for (Image image : slides) {
//				tracker.addImage(image, index++);
//			}
//			
//			tracker.waitForAll();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Properties settings = new Properties();
		try {
			settings.load(new FileInputStream("settings.properties"));
			ScoreboardModel.INSTANCE.init(settings);
		
			int nbrOfMessages = Integer.parseInt(settings.getProperty("psa.message.total", "0"));
			List<String> messages = new ArrayList<String>(nbrOfMessages);
			for (int i = 0; i < nbrOfMessages; i++) {
				messages.add(settings.getProperty("psa.message." + (i + 1)));
			}
			slideshow = new Slideshow("/slides/", messages);
			//slideshow.addPropertyChangeListener(this);
			
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(new ScoreboardPanel(this, model, slideshow), BorderLayout.CENTER);
			
			JDialog controls = new ScoreboardControls(slideshow);
			controls.setVisible(true);
			
//			JDialog hud = new JDialog();
//			hud.setSize(1024, 800);
//			hud.getContentPane().setLayout(new BorderLayout());
//			hud.getContentPane().add(new ScoreboardPanel(this, model, slideshow), BorderLayout.CENTER);
//			hud.setVisible(true);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ScoreBoard scoreboard = new ScoreBoard();
				scoreboard.setVisible(true);
				scoreboard.setLocation(0, 0);
			}
		});
	}
}