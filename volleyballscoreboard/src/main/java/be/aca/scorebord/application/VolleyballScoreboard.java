package be.aca.scorebord.application;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import be.aca.scorebord.components.ScoreboardControls;
import be.aca.scorebord.components.ScoreboardDialog;
import be.aca.scorebord.components.Slideshow;
import be.aca.scorebord.domain.ScoreboardModel;

public class VolleyballScoreboard extends SingleFrameApplication {

	private ScoreboardControls controls;
	private ScoreboardDialog scoreboard;
	private ScoreboardModel model;
	private Slideshow slideshow;
	
	private MediaTracker tracker;

	@Override
	protected void startup() {
		getMainFrame().setTitle("Volleyball scoreboard");
		
		Properties settings = new Properties();
		try {
			settings.load(VolleyballScoreboard.class.getResourceAsStream("/settings.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int nbrOfMessages = Integer.parseInt(settings.getProperty("psa.message.total", "0"));
		List<String> messages = new ArrayList<String>(nbrOfMessages);
		for (int i = 0; i < nbrOfMessages; i++) {
			messages.add(settings.getProperty("psa.message." + (i + 1)));
		}
		
		model = new ScoreboardModel(settings);
		
		try {
			slideshow = new Slideshow(model, "/slides/", messages);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controls = new ScoreboardControls(getContext(), model, slideshow);
		show(controls);
		
		scoreboard = new ScoreboardDialog(model, slideshow);
		scoreboard.setLocation(300, 300);
		scoreboard.setVisible(true);
		slideshow.addPropertyChangeListener(scoreboard);
		
		tracker = new MediaTracker(scoreboard);
		try {
			int index = 0;
			List<Image> slides = slideshow.getSlides();
			for (Image image : slides) {
				tracker.addImage(image, index++);
			}
			
			tracker.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addPropertyChangeListener(scoreboard);
	}

	public static void main(String[] args) {
		Application.launch(VolleyballScoreboard.class, args);
	}
}
