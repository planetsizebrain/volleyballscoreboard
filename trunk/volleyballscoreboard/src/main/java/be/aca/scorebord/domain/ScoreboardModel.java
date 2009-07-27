package be.aca.scorebord.domain;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Properties;

import org.jdesktop.application.Action;


public class ScoreboardModel {

	private Team homeTeam;
	private Team awayTeam;

	private Possesion possession = Possesion.HOME;
	private int games = 1;
	private int timeout;
	private int defaultTimeout;
	private int slideTime;
	private boolean timeoutRunning = false;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public ScoreboardModel(Properties properties) {
		Color main = Color.decode(properties.getProperty("hometeam.color.main", "0x000000"));
		Color sub = Color.decode(properties.getProperty("hometeam.color.sub", "0x000000"));
		homeTeam = new Team(properties.getProperty("hometeam", "HOME"), main, sub, true);
		main = Color.decode(properties.getProperty("awayteam.color.main", "0x000000"));
		sub = Color.decode(properties.getProperty("awayteam.color.sub", "0x000000"));
		awayTeam = new Team(properties.getProperty("awayteam", "GUEST"), main, sub, false);
		timeout = Integer.parseInt(properties.getProperty("timeout", "30000"));
		slideTime = Integer.parseInt(properties.getProperty("slideTime", "10000"));
		defaultTimeout = timeout;
	}
	
	public Possesion getPossesion() {
		return possession;
	}
	
	public Team getHomeTeam() {
		return homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}
	
	public Team getTeam(String id) {
		if (id.toLowerCase().contains("home")) {
			return homeTeam;
		} else {
			return awayTeam;
		}
	}
	
	@Action
	public void switchPossession() {
		if (Possesion.HOME == possession) {
			possession = Possesion.AWAY;
			this.pcs.firePropertyChange("possession", Possesion.HOME, Possesion.AWAY);
		} else {
			possession = Possesion.HOME;
			this.pcs.firePropertyChange("possession", Possesion.AWAY, Possesion.HOME);
		}
	}
	
	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		int old = this.games;
		this.games = games;
		this.pcs.firePropertyChange("games", old, games);
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		int old = this.timeout;
		this.timeout = timeout;
		this.pcs.firePropertyChange("timeout", old, timeout);
	}
	
	public boolean isTimeoutRunning() {
		return timeoutRunning;
	}

	public void setTimeoutRunning(boolean timeoutRunning) {
		boolean old = this.timeoutRunning;
		this.timeoutRunning = timeoutRunning;
		this.pcs.firePropertyChange("timeoutRunning", old, timeoutRunning);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
		homeTeam.addPropertyChangeListener(listener);
		awayTeam.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
		homeTeam.removePropertyChangeListener(listener);
		awayTeam.removePropertyChangeListener(listener);
	}

	public void reset() {
		homeTeam.reset();
		awayTeam.reset();
		
		setGames(1);
		timeout = defaultTimeout;
		
		this.pcs.firePropertyChange("reset", "old", "new");
	}
	
	public void setSlideTime(int slideTime) {
		this.slideTime = slideTime;
	}
	
	public int getSlideTime() {
		return slideTime;
	}
}
