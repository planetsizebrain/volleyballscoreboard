package be.aca.scorebord.components;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;

import be.aca.scorebord.domain.Possesion;
import be.aca.scorebord.domain.ScoreboardModel;
import be.aca.scorebord.domain.Team;

public class ScoreboardControls extends JPanel {

	private JButton homeupButton, homednButton, homesetButton, guestupButton,
			guestdnButton, guestsetButton, clearPoints, startHomeTimeout,
			possesionButton, awaySetDown, gameUp, gameDown, homeSetUp, horn,
			awaySetUp, beep, clearSets, homeSetDown, clearTimeout, reset,
			suspendSlidesButton, resumeSlidesButton, startAwayTimeout;

	private JTextField homeTeam, awayTeam, points, timeout, slideTime;

	private ApplicationContext context;

	private ScoreboardModel model;

	private Timer timeoutTimer = new Timer();

	public ScoreboardControls(ApplicationContext context, ScoreboardModel model, Slideshow slideshow) {
		this.context = context;
		this.model = model;

		setLayout(new GridLayout(10, 3, 3, 3));

		// Row 1
		homeTeam = new JTextField(20);
		homeTeam.setName("homeTeam");
		homeTeam.setAction(context.getActionMap(this).get("setHomeTeam"));
		homeTeam.setText(model.getHomeTeam().getName());
		add(homeTeam);

		reset = new JButton();
		reset.setName("reset");
		reset.setAction(context.getActionMap(this).get("reset"));
		add(reset);

		awayTeam = new JTextField(20);
		awayTeam.setName("awayTeam");
		awayTeam.setAction(context.getActionMap(this).get("setAwayTeam"));
		awayTeam.setText(model.getAwayTeam().getName());
		add(awayTeam);

		// Row 2
		homeupButton = new JButton();
		homeupButton.setName("homeup");
		homeupButton.setAction(context.getActionMap(this).get("addPoint"));
		add(homeupButton);

		possesionButton = new JButton();
		possesionButton.setName("possesion");
		possesionButton.setAction(context.getActionMap(model).get(
				"switchPossession"));
		add(possesionButton);

		guestupButton = new JButton();
		guestupButton.setName("guestup");
		guestupButton.setAction(context.getActionMap(this).get("addPoint"));
		add(guestupButton);

		// Row 3
		homednButton = new JButton();
		homednButton.setName("homedn");
		homednButton.setAction(context.getActionMap(this).get("removePoint"));
		add(homednButton);

		clearPoints = new JButton();
		clearPoints.setName("clearPoints");
		clearPoints.setAction(context.getActionMap(this).get("clearPoints"));
		add(clearPoints);

		guestdnButton = new JButton();
		guestdnButton.setName("guestdn");
		guestdnButton.setAction(context.getActionMap(this).get("removePoint"));
		add(guestdnButton);

		// Row 5
		homesetButton = new JButton();
		homesetButton.setName("homeset");
		homesetButton.setAction(context.getActionMap(this).get("setPoints"));
		add(homesetButton);

		points = new JTextField(20);
		points.setName("points");
		add(points);

		guestsetButton = new JButton();
		guestsetButton.setName("guestset");
		guestsetButton.setAction(context.getActionMap(this).get("setPoints"));
		add(guestsetButton);

		// Row 6
		homeSetUp = new JButton();
		homeSetUp.setName("homeSetUp");
		homeSetUp.setAction(context.getActionMap(this).get("addSet"));
		add(homeSetUp);

		clearSets = new JButton();
		clearSets.setName("clearSets");
		clearSets.setAction(context.getActionMap(this).get("clearSets"));
		add(clearSets);

		awaySetUp = new JButton();
		awaySetUp.setName("awaySetUp");
		awaySetUp.setAction(context.getActionMap(this).get("addSet"));
		add(awaySetUp);

		//
		homeSetDown = new JButton();
		homeSetDown.setName("homeSetDown");
		homeSetDown.setAction(context.getActionMap(this).get("removeSet"));
		add(homeSetDown);

		beep = new JButton();
		beep.setName("beep");
		beep.setAction(context.getActionMap(this).get("makeNoise"));
		add(beep);

		awaySetDown = new JButton();
		awaySetDown.setName("awaySetDown");
		awaySetDown.setAction(context.getActionMap(this).get("removeSet"));
		add(awaySetDown);

		//
		gameUp = new JButton();
		gameUp.setName("gameUp");
		gameUp.setAction(context.getActionMap(this).get("gameUp"));
		add(gameUp);

		horn = new JButton();
		horn.setName("horn");
		horn.setAction(context.getActionMap(this).get("makeNoise"));
		add(horn);

		gameDown = new JButton();
		gameDown.setName("gameDown");
		gameDown.setAction(context.getActionMap(this).get("gameDown"));
		add(gameDown);

		// Timeouts
		startHomeTimeout = new JButton();
		startHomeTimeout.setName("startHomeTimeout");
		startHomeTimeout.setAction(context.getActionMap(this).get("startTimeout"));
		add(startHomeTimeout);

		slideTime = new JTextField(20);
		slideTime.setName("slideTime");
		slideTime.setAction(context.getActionMap(this).get("setSlideTime"));
		slideTime.setText(Integer.toString(model.getTimeout()));
		add(slideTime);
		
		startAwayTimeout = new JButton();
		startAwayTimeout.setName("startAwayTimeout");
		startAwayTimeout.setAction(context.getActionMap(this).get("startTimeout"));
		add(startAwayTimeout);

		clearTimeout = new JButton();
		clearTimeout.setName("clearTimeout");
		clearTimeout.setAction(context.getActionMap(this).get("stopTimeout"));
		add(clearTimeout);

		resumeSlidesButton = new JButton();
		resumeSlidesButton.setName("resumeSlides");
		resumeSlidesButton.setAction(context.getActionMap(slideshow).get(
				"start"));
		add(resumeSlidesButton);

		suspendSlidesButton = new JButton();
		suspendSlidesButton.setName("suspendSlides");
		suspendSlidesButton.setAction(context.getActionMap(slideshow).get(
				"stop"));
		add(suspendSlidesButton);
	}

	@Action
	public void setHomeTeam() {
		Team team = model.getHomeTeam();
		team.setName(homeTeam.getText());
	}

	@Action
	public void setAwayTeam() {
		Team team = model.getAwayTeam();
		team.setName(awayTeam.getText());
	}

	@Action
	public void reset() {
		model.reset();
	}

	@Action
	public void addPoint(ActionEvent ae) {
		Team team = model.getTeam(ae.getActionCommand());
		team.addPoint();
		
		if (team.isHomeTeam() && model.getPossesion() == Possesion.AWAY) {
			model.switchPossession();
		}
		if (!team.isHomeTeam() && model.getPossesion() == Possesion.HOME) {
			model.switchPossession();
		}

		Team home = model.getHomeTeam();
		Team away = model.getAwayTeam();

		if (((home.getPoints() == 8 && away.getPoints() < 8) || (away.getPoints() == 8 && home.getPoints() < 8)) && (home.getSets() + away.getSets() <= 4)) {
			model.setTimeout(60000);
			startTimeout(null);
		}
		if (((home.getPoints() == 16 && away.getPoints() < 16) || (away.getPoints() == 16 && home.getPoints() < 16)) && (home.getSets() + away.getSets() <= 4)) {
			model.setTimeout(60000);
			startTimeout(null);
		}
		
		if (((home.getPoints() == 25 && away.getPoints() <= 23) && (home.getSets() <= 2 && away.getSets() <= 2)) ||
			((home.getPoints() >= 25 && Math.abs(home.getPoints() - away.getPoints()) >= 2) && (home.getSets() <= 2 && away.getSets() <= 2)) ||
			((home.getPoints() == 15 && away.getPoints() <= 13) && (home.getSets() + away.getSets() == 4)) ||
			((home.getPoints() >= 15 && Math.abs(home.getPoints() - away.getPoints()) >= 2) && (home.getSets() + away.getSets() == 4))) {
			home.setPoints(0);
			home.setSets(home.getSets() + 1);
			away.setPoints(0);
		}
		
		if (((away.getPoints() == 25 && home.getPoints() <= 23) && (away.getSets() <= 2 && home.getSets() <= 2)) ||
			((away.getPoints() >= 25 && Math.abs(away.getPoints() - home.getPoints()) >= 2) && (away.getSets() <= 2 && home.getSets() <= 2)) ||
			((away.getPoints() == 15 && home.getPoints() <= 13) && (away.getSets() + home.getSets() == 4)) ||
			((away.getPoints() >= 15 && Math.abs(away.getPoints() - home.getPoints()) >= 2) && (away.getSets() + home.getSets() == 4))) {
			away.setPoints(0);
			away.setSets(away.getSets() + 1);
			home.setPoints(0);
		}
	}

	@Action
	public void addSet(ActionEvent ae) {
		Team team = model.getTeam(((JButton) ae.getSource()).getName());
		team.addSet();
	}

	@Action
	public void makeNoise(ActionEvent ae) {
		if (ae.getActionCommand().toLowerCase().contains("beep")) {
			NoiseMaker.beep();
		} else {
			NoiseMaker.horn();
		}
	}

	@Action
	public void removePoint(ActionEvent ae) {
		Team team = model.getTeam(ae.getActionCommand());
		team.removePoint();
	}

	@Action
	public void removeSet(ActionEvent ae) {
		Team team = model.getTeam(ae.getActionCommand());
		team.removeSet();
	}

	@Action
	public void clearPoints(ActionEvent ae) {
		model.getHomeTeam().setPoints(0);
		model.getAwayTeam().setPoints(0);
	}

	@Action
	public void clearSets(ActionEvent ae) {
		model.getHomeTeam().setSets(0);
		model.getAwayTeam().setSets(0);
	}

	@Action
	public void gameUp(ActionEvent ae) {
		model.setGames(model.getGames() + 1);
	}

	@Action
	public void gameDown(ActionEvent ae) {
		if (model.getGames() > 1) {
			model.setGames(model.getGames() - 1);
		}
	}

	@Action
	public void startTimeout(ActionEvent ae) {
		model.setTimeoutRunning(true);
		
		if (ae != null) {
			Team team = model.getTeam(ae.getActionCommand());
			team.setTimouts(team.getTimouts() + 1);
		}
		
		timeoutTimer = new Timer();
		timeoutTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						int timeout = model.getTimeout();
						if (timeout > 100) {
							model.setTimeout(timeout - 100);
						} else {
							timeoutTimer.cancel();
							model.setTimeoutRunning(false);
							NoiseMaker.beep();
							model.setTimeout(30000);
						}
					}
				});
			}
		}, 0, 100);
	}

	@Action
	public void stopTimeout() {
		model.setTimeoutRunning(false);
		if (timeoutTimer != null) {
			timeoutTimer.cancel();
			model.setTimeout(30000);
		}
	}
	
	@Action
	public void setTimeout() {
		model.setTimeout(Integer.parseInt(timeout.getText()));
	}
	
	@Action
	public void setSlideTime() {
		model.setSlideTime(Integer.parseInt(slideTime.getText()));
	}
	
	@Action
	public void setPoints(ActionEvent ae) {
		if (ae != null) {
			Team team = model.getTeam(((JButton) ae.getSource()).getName());
			team.setPoints(Integer.parseInt(points.getText()));
		}
	}
	
	@Action
	public void setColor(ActionEvent ae) {
		if (ae != null) {
			Team team = model.getTeam(ae.getActionCommand());
			Color color = JColorChooser.showDialog(this, "Choose " + team.getName() + " color", team.getMainColor());
		    if (color != null) {
		    	team.setMainColor(color);
		    }
		}
	}

	private javax.swing.Action getAction(String actionName) {
		return context.getActionMap(model).get(actionName);
	}
}
