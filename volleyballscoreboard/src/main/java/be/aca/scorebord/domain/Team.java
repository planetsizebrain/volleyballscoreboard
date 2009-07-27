package be.aca.scorebord.domain;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Team {

	private String name;
	private int points;
	private int fouls;
	private int timouts;
	private int sets;
	private boolean home = false;
	private Color main;
	private Color sub;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public Team(String name) {
		this.name = name;
	}
	
	public Team(String name, Color main, Color sub, boolean home) {
		this(name);
		this.home = home;
		this.main = main;
		this.sub = sub;
	}
	
	public boolean isHomeTeam() {
		return home;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		String old = this.name;
		this.name = name;
		this.pcs.firePropertyChange("name", old, name);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		int old = this.points;
		this.points = points;
		this.pcs.firePropertyChange("points", old, points);
	}
	
	public int getSets() {
		return sets;
	}

	public void setSets(int sets) {
		int old = this.sets;
		this.sets = sets;
		this.pcs.firePropertyChange("sets", old, sets);
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public int getTimouts() {
		return timouts;
	}

	public void setTimouts(int timouts) {
		this.timouts = timouts;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public void reset() {
		setPoints(0);
		setTimouts(0);
		setSets(0);
	}
	
	public void addPoint() {
		setPoints(getPoints() + 1);
	}
	
	public void removePoint() {
		if (getPoints() > 0) {
			setPoints(getPoints() - 1);
		}
	}
	
	public void addSet() {
		setSets(getSets() + 1);
	}
	
	public void removeSet() {
		if (getSets() > 0) {
			setSets(getSets() - 1);
		}
	}

	public Color getMainColor() {
		return main;
	}

	public Color getSubColor() {
		return sub;
	}
	
	public void setMainColor(Color color) {
		Color old = main;
		this.main = color;
		this.pcs.firePropertyChange("maincolor", old, main);
	}
	
	public void setSubColor(Color color) {
		Color old = sub;
		this.sub = color;
		this.pcs.firePropertyChange("subcolor", old, main);
	}
}