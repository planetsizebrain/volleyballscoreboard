/**
 * Copyright (c) 2009, Jan Eerdekens
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
 *      in the documentation and/or other materials provided with the distribution.
 *    * Neither the name of the Squared IT Solutions nor the names of its contributors may be used to endorse or promote products derived 
 *      from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package be.aca.scorebord.domain;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Team {

	private String name;
	private int points;
	private int fouls;
	private int timeouts;
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
	
	private String getPropertyName(String name) {
		return (home ? "home" : "away") + name;
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
		this.pcs.firePropertyChange(getPropertyName("name"), old, name);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		int old = this.points;
		this.points = points;
		this.pcs.firePropertyChange(getPropertyName("points"), old, points);
	}
	
	public int getSets() {
		return sets;
	}

	public void setSets(int sets) {
		int old = this.sets;
		this.sets = sets;
		this.pcs.firePropertyChange(getPropertyName("sets"), old, sets);
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public int getTimouts() {
		return timeouts;
	}

	public void setTimouts(int timeouts) {
		int old = this.timeouts;
		this.timeouts = timeouts;
		this.pcs.firePropertyChange("timeout", old, timeouts);
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
		this.pcs.firePropertyChange(getPropertyName("maincolor"), old, main);
	}
	
	public void setSubColor(Color color) {
		Color old = sub;
		this.sub = color;
		this.pcs.firePropertyChange(getPropertyName("subcolor"), old, main);
	}
}