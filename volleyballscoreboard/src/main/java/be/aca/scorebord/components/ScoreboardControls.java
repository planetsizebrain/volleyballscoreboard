package be.aca.scorebord.components;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.aca.scorebord.domain.Possesion;
import be.aca.scorebord.domain.ScoreboardModel;
import be.aca.scorebord.domain.Team;

public class ScoreboardControls extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = 1542641990398758757L;

	private static final String RESOURCE_BUNDLE = "/be/aca/scorebord/application/resources/VolleyballScoreboard.properties";
	private JButton homeupButton, homednButton, awayupButton,
			awaydnButton, clearPoints, startHomeTimeout,
			possesionButton, awaySetDown, gameUp, gameDown, homeSetUp, horn,
			awaySetUp, beep, clearSets, homeSetDown, stopHomeTimeout, reset,
			suspendSlidesButton, resumeSlidesButton, startAwayTimeout,
			homeMainColor, homeSubColor, awayMainColor, awaySubColor, stopAwayTimeout,
			startTimeout, stopTimeout;

	private JTextField homeTeam, awayTeam, slideTime,
			homePoints, awayPoints, homeSets, awaySets, games, 
			homeTimeouts, awayTimeouts, homeTimeout, awayTimeout, timeout;
	
	private JCheckBox juniors;
	
	private JLabel slide;
	private JPanel slideImage;
	
	private Timer timeoutTimer = new Timer();
	
	private Properties resources = new Properties();
	
	private Slideshow slideshow;

	public ScoreboardControls(Slideshow slideshow) {
		this.slideshow = slideshow;
		
		setLayout(new GridLayout(5, 3, 3, 3));
		setSize(600, 700);
		setLocation(0, 0);
		
		try {
			resources.load(ScoreboardControls.class.getResourceAsStream(RESOURCE_BUNDLE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buildDialog();
		
		ScoreboardModel.INSTANCE.addPropertyChangeListener(this);
		slideshow.addPropertyChangeListener(this);
	}
	
	private String getText(String key) {
		String text = resources.getProperty(key);
		if (text != null && !text.equals("")) {
			return text;
		} else {
			return key;
		}
	}
	
	private class AddPointListener implements ActionListener {

		private Team team;
		
		public AddPointListener(Team team) {
			this.team = team;
		}
		public void actionPerformed(ActionEvent e) {
			team.addPoint();
			
			final ScoreboardModel model = ScoreboardModel.INSTANCE;
			if (team.isHomeTeam() && model.getPossesion() == Possesion.AWAY) {
				model.switchPossession();
			}
			if (!team.isHomeTeam() && model.getPossesion() == Possesion.HOME) {
				model.switchPossession();
			}

			final Team home = model.getHomeTeam();
			final Team away = model.getAwayTeam();

			if (((home.getPoints() == 8 && away.getPoints() < 8) || (away.getPoints() == 8 && home.getPoints() < 8)) && (home.getSets() + away.getSets() <= 4) && !model.isFirstExtraTimeout()) {
				if (!juniors.getModel().isSelected()) {
					model.setFirstExtraTimeout(true);
					startTimeout(null, 60000);
				}
			}
			if (((home.getPoints() == 16 && away.getPoints() < 16) || (away.getPoints() == 16 && home.getPoints() < 16)) && (home.getSets() + away.getSets() <= 4) && !model.isSecondExtraTimeout()) {
				if (!juniors.getModel().isSelected()) {
					model.setSecondExtraTimeout(true);
					startTimeout(null, 60000);
				}
			}
			
			if (((home.getPoints() == 25 && away.getPoints() <= 23) && (home.getSets() <= 2 && away.getSets() <= 2)) ||
				((home.getPoints() >= 25 && Math.abs(home.getPoints() - away.getPoints()) >= 2) && (home.getSets() <= 2 && away.getSets() <= 2)) ||
				((home.getPoints() == 15 && away.getPoints() <= 13) && (home.getSets() + away.getSets() == 4)) ||
				((home.getPoints() >= 15 && Math.abs(home.getPoints() - away.getPoints()) >= 2) && (home.getSets() + away.getSets() == 4))) {
				final Timer endSetTimer = new Timer();
				endSetTimer.schedule(new TimerTask() {
		
					@Override
					public void run() {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								home.setPoints(0);
								home.setSets(home.getSets() + 1);
								away.setPoints(0);
								model.setFirstExtraTimeout(false);
								model.setSecondExtraTimeout(false);
								home.setTimouts(0);
								away.setTimouts(0);
								endSetTimer.cancel();
							}
						});
					}
				}, model.getAfterSetTimeout());
			}
			
			if (((away.getPoints() == 25 && home.getPoints() <= 23) && (away.getSets() <= 2 && home.getSets() <= 2)) ||
				((away.getPoints() >= 25 && Math.abs(away.getPoints() - home.getPoints()) >= 2) && (away.getSets() <= 2 && home.getSets() <= 2)) ||
				((away.getPoints() == 15 && home.getPoints() <= 13) && (away.getSets() + home.getSets() == 4)) ||
				((away.getPoints() >= 15 && Math.abs(away.getPoints() - home.getPoints()) >= 2) && (away.getSets() + home.getSets() == 4))) {
				final Timer endSetTimer = new Timer();
				endSetTimer.schedule(new TimerTask() {
		
					@Override
					public void run() {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								away.setPoints(0);
								away.setSets(away.getSets() + 1);
								home.setPoints(0);
								model.setFirstExtraTimeout(false);
								model.setSecondExtraTimeout(false);
								home.setTimouts(0);
								away.setTimouts(0);
								endSetTimer.cancel();
							}
						});
					}
				}, model.getAfterSetTimeout());
			}
		}
	}
	
	private void buildDialog() {
		JPanel homePanel = new JPanel(new GridBagLayout());
		homeTeam = new JTextField(20);
		homeTeam.setName("homeTeam");
		homeTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setName(homeTeam.getText());
			}
		});
		homeTeam.setText("HOME");
		homeTeam.setBackground(ScoreboardModel.INSTANCE.getPossesion() == Possesion.HOME ? Color.RED : Color.WHITE);
		homePanel.add(homeTeam, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		homeMainColor = new JButton("MAIN");
		homeMainColor.setName("homeMainColor");
		homeMainColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = ScoreboardModel.INSTANCE.getHomeTeam();
				team.setMainColor(JColorChooser.showDialog(ScoreboardControls.this, "Choose " + team.getName() + " color", team.getMainColor()));
			}
		});
		homeMainColor.setForeground(ScoreboardModel.INSTANCE.getHomeTeam().getMainColor());
		homePanel.add(homeMainColor, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		homeSubColor = new JButton("SUB");
		homeSubColor.setName("homeMainColor");
		homeSubColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = ScoreboardModel.INSTANCE.getHomeTeam();
				team.setSubColor(JColorChooser.showDialog(ScoreboardControls.this, "Choose " + team.getName() + " color", team.getSubColor()));
			}
		});
		homeSubColor.setForeground(ScoreboardModel.INSTANCE.getHomeTeam().getSubColor());
		homePanel.add(homeSubColor, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(homePanel);

		
		JPanel posPanel = new JPanel(new GridBagLayout());
		possesionButton = new JButton(getText("possesion.text"));
		possesionButton.setName("possesion");
		possesionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.switchPossession();
			}
		});
		posPanel.add(possesionButton, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		reset = new JButton(getText("reset.text"));
		reset.setName("reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.reset();
			}
		});
		posPanel.add(reset, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(posPanel);
		

		JPanel awayPanel = new JPanel(new GridBagLayout());
		awayTeam = new JTextField(20);
		awayTeam.setName("awayTeam");
		awayTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().setName(awayTeam.getText());
			}
		});
		awayTeam.setText("AWAY");
		awayTeam.setBackground(ScoreboardModel.INSTANCE.getPossesion() == Possesion.AWAY ? Color.RED : Color.WHITE);
		awayPanel.add(awayTeam, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		awayMainColor = new JButton("MAIN");
		awayMainColor.setName("awayMainColor");
		awayMainColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = ScoreboardModel.INSTANCE.getAwayTeam();
				team.setMainColor(JColorChooser.showDialog(ScoreboardControls.this, "Choose " + team.getName() + " color", team.getMainColor()));
			}
		});
		awayMainColor.setForeground(ScoreboardModel.INSTANCE.getAwayTeam().getMainColor());
		awayPanel.add(awayMainColor, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		awaySubColor = new JButton("SUB");
		awaySubColor.setName("awayMainColor");
		awaySubColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Team team = ScoreboardModel.INSTANCE.getAwayTeam();
				team.setSubColor(JColorChooser.showDialog(ScoreboardControls.this, "Choose " + team.getName() + " color", team.getSubColor()));
			}
		});
		awaySubColor.setForeground(ScoreboardModel.INSTANCE.getAwayTeam().getSubColor());
		awayPanel.add(awaySubColor, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(awayPanel);
		
		
		JPanel homePointPanel = new JPanel(new GridBagLayout());
		homePoints = new JTextField();
		homePoints.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getPoints()));
		homePoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setPoints(new Integer(homePoints.getText()));
			}
		});
		homePointPanel.add(homePoints, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		homeupButton = new JButton(getText("homeup.text"));
		homeupButton.setName("homeup");
		homeupButton.addActionListener(new AddPointListener(ScoreboardModel.INSTANCE.getHomeTeam()));
		homePointPanel.add(homeupButton, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(homePointPanel);
		
		homednButton = new JButton(getText("homedn.text"));
		homednButton.setName("homedn");
		homednButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().removePoint();
			}
		});
		homePointPanel.add(homednButton, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		
		JPanel soundPanel = new JPanel(new GridBagLayout());
		
		clearPoints = new JButton(getText("clearPoints.text"));
		clearPoints.setName("clearPoints");
		clearPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setPoints(0);
				ScoreboardModel.INSTANCE.getAwayTeam().setPoints(0);
			}
		});
		soundPanel.add(clearPoints, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		clearSets = new JButton(getText("clearSets.text"));
		clearSets.setName("clearSets");
		clearSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setSets(0);
				ScoreboardModel.INSTANCE.getAwayTeam().setSets(0);
			}
		});
		soundPanel.add(clearSets, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		
		beep = new JButton(getText("beep.text"));
		beep.setName("beep");
		beep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NoiseMaker.beep();
			}
		});
		soundPanel.add(beep, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		horn = new JButton(getText("horn.text"));
		horn.setName("horn");
		horn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NoiseMaker.horn();
			}
		});
		soundPanel.add(horn, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(soundPanel);
		

		JPanel awayPointPanel = new JPanel(new GridBagLayout());
		awayPoints = new JTextField();
		awayPoints.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getPoints()));
		awayPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().setPoints(new Integer(awayPoints.getText()));
			}
		});
		awayPointPanel.add(awayPoints, new GridBagConstraints(2, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		awayupButton = new JButton(getText("guestup.text"));
		awayupButton.setName("awayup");
		awayupButton.addActionListener(new AddPointListener(ScoreboardModel.INSTANCE.getAwayTeam()));
		awayPointPanel.add(awayupButton, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(awayPointPanel);
		
		awaydnButton = new JButton(getText("guestdn.text"));
		awaydnButton.setName("awaydn");
		awaydnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().removePoint();
			}
		});
		awayPointPanel.add(awaydnButton, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		
		JPanel homeSetPanel = new JPanel(new GridBagLayout());
		homeSets = new JTextField();
		homeSets.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getSets()));
		homeSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setSets(new Integer(homeSets.getText()));
			}
		});
		homeSetPanel.add(homeSets, new GridBagConstraints(0, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		

		homeSetUp = new JButton(getText("homeSetUp.text"));
		homeSetUp.setName("homeSetUp");
		homeSetUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().addSet();
			}
		});
		homeSetPanel.add(homeSetUp, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		homeSetDown = new JButton(getText("homeSetDown.text"));
		homeSetDown.setName("homeSetDown");
		homeSetDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().removeSet();
			}
		});
		homeSetPanel.add(homeSetDown, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(homeSetPanel);
		
		
		JPanel gamePanel = new JPanel(new GridBagLayout());
		gameUp = new JButton(getText("gameUp.text"));
		gameUp.setName("gameUp");
		gameUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int games = ScoreboardModel.INSTANCE.getGames();
				ScoreboardModel.INSTANCE.setGames(games > 0 ? games + 1 : games);
			}
		});
		gamePanel.add(gameUp, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		games = new JTextField();
		games.setText(Integer.toString(ScoreboardModel.INSTANCE.getGames()));
		games.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setGames(new Integer(games.getText()));
			}
		});
		gamePanel.add(games, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		gameDown = new JButton(getText("gameDown.text"));
		gameDown.setName("gameDown");
		gameDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int games = ScoreboardModel.INSTANCE.getGames();
				ScoreboardModel.INSTANCE.setGames(games > 0 ? games - 1 : games);
			}
		});
		gamePanel.add(gameDown, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(gamePanel);
		
		
		JPanel awaySetPanel = new JPanel(new GridBagLayout());
		awaySets = new JTextField();
		awaySets.setText(Integer.toString(ScoreboardModel.INSTANCE.getAwayTeam().getSets()));
		awaySets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().setSets(new Integer(awaySets.getText()));
			}
		});
		awaySetPanel.add(awaySets, new GridBagConstraints(2, 0, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		
		awaySetUp = new JButton(getText("awaySetUp.text"));
		awaySetUp.setName("awaySetUp");
		awaySetUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().addSet();
			}
		});
		add(awaySetUp);
		awaySetPanel.add(awaySetUp, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		

		awaySetDown = new JButton(getText("awaySetDown.text"));
		awaySetDown.setName("awaySetDown");
		awaySetDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().removeSet();
			}
		});
		add(awaySetDown);
		awaySetPanel.add(awaySetDown, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(awaySetPanel);
		

		JPanel homeTimeoutPanel = new JPanel(new GridBagLayout());
		homeTimeouts = new JTextField();
		homeTimeouts.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getTimouts()));
		homeTimeouts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getHomeTeam().setTimouts(new Integer(homeTimeouts.getText()));
			}
		});
		homeTimeoutPanel.add(homeTimeouts, new GridBagConstraints(0, 0, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		startHomeTimeout = new JButton(getText("startHomeTimeout.text"));
		startHomeTimeout.setName("startHomeTimeout");
		startHomeTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startTimeout(ScoreboardModel.INSTANCE.getHomeTeam(), ScoreboardModel.INSTANCE.getTimeout());
			}
		});
		homeTimeoutPanel.add(startHomeTimeout, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		homeTimeout = new JTextField();
		homeTimeout.setText(Integer.toString(ScoreboardModel.INSTANCE.getTimeout()));
		homeTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setTimeout(new Integer(homeTimeout.getText()));
				awayTimeout.setText(homeTimeout.getText());
			}
		});
		homeTimeoutPanel.add(homeTimeout, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		stopHomeTimeout = new JButton(getText("stopHomeTimeout.text"));
		stopHomeTimeout.setName("clearTimeout");
		stopHomeTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setTimeoutRunning(false);
				if (timeoutTimer != null) {
					timeoutTimer.cancel();
				}
			}
		});
		homeTimeoutPanel.add(stopHomeTimeout, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(homeTimeoutPanel);
		
		
		JPanel timeoutPanel = new JPanel(new GridBagLayout());
		startTimeout = new JButton(getText("startTimeout.text"));
		startTimeout.setName("startTimeout");
		startTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startTimeout(null, ScoreboardModel.INSTANCE.getGeneralTimeout());
			}
		});
		timeoutPanel.add(startTimeout, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		timeout = new JTextField();
		timeout.setText(Integer.toString(ScoreboardModel.INSTANCE.getGeneralTimeout()));
		timeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setGeneralTimeout(new Integer(timeout.getText()));
			}
		});
		timeoutPanel.add(timeout, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		stopTimeout = new JButton(getText("stopTimeout.text"));
		stopTimeout.setName("clearTimeout");
		stopTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setTimeoutRunning(false);
				if (timeoutTimer != null) {
					timeoutTimer.cancel();
					ScoreboardModel.INSTANCE.setGeneralTimeout(new Integer(timeout.getText()));
				}
			}
		});
		timeoutPanel.add(stopTimeout, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(timeoutPanel);
		
		
		
		JPanel awayTimeoutPanel = new JPanel(new GridBagLayout());
		awayTimeouts = new JTextField();
		awayTimeouts.setText(Integer.toString(ScoreboardModel.INSTANCE.getAwayTeam().getTimouts()));
		awayTimeouts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.getAwayTeam().setTimouts(new Integer(awayTimeouts.getText()));
			}
		});
		awayTimeoutPanel.add(awayTimeouts, new GridBagConstraints(2, 0, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		startAwayTimeout = new JButton(getText("startAwayTimeout.text"));
		startAwayTimeout.setName("startAwayTimeout");
		startAwayTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startTimeout(ScoreboardModel.INSTANCE.getAwayTeam(), ScoreboardModel.INSTANCE.getTimeout());
			}
		});
		awayTimeoutPanel.add(startAwayTimeout, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		awayTimeout = new JTextField();
		awayTimeout.setText(Integer.toString(ScoreboardModel.INSTANCE.getTimeout()));
		awayTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setTimeout(new Integer(awayTimeout.getText()));
				homeTimeout.setText(awayTimeout.getText());
			}
		});
		awayTimeoutPanel.add(awayTimeout, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		stopAwayTimeout = new JButton(getText("stopTimeout.text"));
		stopAwayTimeout.setName("clearTimeout");
		stopAwayTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setTimeoutRunning(false);
				if (timeoutTimer != null) {
					timeoutTimer.cancel();
				}
			}
		});
		awayTimeoutPanel.add(stopAwayTimeout, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(awayTimeoutPanel);
		
		
		JPanel slidePanel = new JPanel(new GridBagLayout());
		resumeSlidesButton = new JButton(getText("resumeSlides.text"));
		resumeSlidesButton.setName("resumeSlides");
		resumeSlidesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slideshow.start();
			}
		});
		slidePanel.add(resumeSlidesButton, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		slideTime = new JTextField(20);
		slideTime.setName("slideTime");
		slideTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreboardModel.INSTANCE.setSlideTime(Integer.parseInt(slideTime.getText()));
			}
		});
		slideTime.setText(Integer.toString(ScoreboardModel.INSTANCE.getSlideTime()));
		slidePanel.add(slideTime, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		suspendSlidesButton = new JButton(getText("suspendSlides.text"));
		suspendSlidesButton.setName("suspendSlides");
		suspendSlidesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slideshow.stop();
			}
		});
		slidePanel.add(suspendSlidesButton, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(slidePanel);
		
		slideImage = new JPanel();
		try {
			slide = new JLabel();
			slideImage.add(slide);
			//slide.setIcon(new ImageIcon(slideshow.getCurrentSlide().getScaledInstance(slideImage.getWidth(), slideImage.getHeight(), Image.SCALE_FAST)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(slideImage);
		
		juniors = new JCheckBox(getText("juniors.text"));
		juniors.setName("juniors");
		juniors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		add(juniors);
	}
	
	private void startTimeout(Team team, final int timeout) {
		final ScoreboardModel model = ScoreboardModel.INSTANCE;
		if (team != null) {
			if (team.getTimouts() < 2) {
				NoiseMaker.beep();
				
				team.setTimouts(team.getTimouts() + 1);
				model.setRunningTimeout(timeout);
				model.setTimeoutRunning(true);
				
				timeoutTimer = new Timer();
				timeoutTimer.scheduleAtFixedRate(new TimerTask() {
		
					@Override
					public void run() {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								if (model.getRunningTimeout() > 100) {
									model.setRunningTimeout(model.getRunningTimeout() - 100);
								} else {
									timeoutTimer.cancel();
									model.setTimeoutRunning(false);
									NoiseMaker.beep();
								}
							}
						});
					}
				}, 0, 100);
			}
		} else {
			NoiseMaker.beep();
			
			model.setRunningTimeout(timeout);
			model.setTimeoutRunning(true);
			
			timeoutTimer = new Timer();
			timeoutTimer.scheduleAtFixedRate(new TimerTask() {
	
				@Override
				public void run() {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							if (model.getRunningTimeout() > 100) {
								model.setRunningTimeout(model.getRunningTimeout() - 100);
							} else {
								timeoutTimer.cancel();
								model.setTimeoutRunning(false);
								NoiseMaker.beep();
							}
						}
					});
				}
			}, 0, 100);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		homeMainColor.setForeground(ScoreboardModel.INSTANCE.getHomeTeam().getMainColor());
		homeSubColor.setForeground(ScoreboardModel.INSTANCE.getHomeTeam().getSubColor());
		homePoints.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getPoints()));
		homeSets.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getSets()));
		homeTimeouts.setText(Integer.toString(ScoreboardModel.INSTANCE.getHomeTeam().getTimouts()));
		awayMainColor.setForeground(ScoreboardModel.INSTANCE.getAwayTeam().getMainColor());
		awaySubColor.setForeground(ScoreboardModel.INSTANCE.getAwayTeam().getSubColor());
		awayPoints.setText(Integer.toString(ScoreboardModel.INSTANCE.getAwayTeam().getPoints()));
		awaySets.setText(Integer.toString(ScoreboardModel.INSTANCE.getAwayTeam().getSets()));
		awayTimeouts.setText(Integer.toString(ScoreboardModel.INSTANCE.getAwayTeam().getTimouts()));
		if (ScoreboardModel.INSTANCE.getPossesion() == Possesion.HOME) {
			homeTeam.setBackground(Color.RED);
			awayTeam.setBackground(Color.WHITE);
		} else {
			homeTeam.setBackground(Color.WHITE);
			awayTeam.setBackground(Color.RED);
		}
		games.setText(Integer.toString(ScoreboardModel.INSTANCE.getGames()));
		try {
			slide.setIcon(new ImageIcon(slideshow.getCurrentSlide().getScaledInstance(slideImage.getWidth(), slideImage.getHeight(), Image.SCALE_FAST)));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}