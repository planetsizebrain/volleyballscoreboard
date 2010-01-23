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
import java.util.Properties;

public enum ScoreboardModel {

	INSTANCE;
	
	private Team homeTeam;
	private Team awayTeam;

	private Possesion possession = Possesion.HOME;
	private int games = 1;
	private int timeout;
	private int generalTimeout;
	private int runningTimeout;
	private int slideTime;
	private boolean timeoutRunning = false;
	private boolean firstExtraTimeout = false;
	private boolean secondExtraTimeout = false;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private ScoreboardModel() {
		// Deny instantiation
	};
	
	public void init(Properties properties) {
		Color main = Color.decode(properties.getProperty("hometeam.color.main", "0x000000"));
		Color sub = Color.decode(properties.getProperty("hometeam.color.sub", "0x000000"));
		homeTeam = new Team(properties.getProperty("hometeam", "HOME"), main, sub, true);
		main = Color.decode(properties.getProperty("awayteam.color.main", "0x000000"));
		sub = Color.decode(properties.getProperty("awayteam.color.sub", "0x000000"));
		awayTeam = new Team(properties.getProperty("awayteam", "GUEST"), main, sub, false);
		timeout = Integer.parseInt(properties.getProperty("timeout", "30000"));
		slideTime = Integer.parseInt(properties.getProperty("slideTime", "10000"));
		generalTimeout = Integer.parseInt(properties.getProperty("generaltimeout", "30000"));;
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
	
	public int getRunningTimeout() {
		return runningTimeout;
	}

	public void setRunningTimeout(int runningTimeout) {
		int old = this.runningTimeout;
		this.runningTimeout = runningTimeout;
		this.pcs.firePropertyChange("timeout", old, runningTimeout);
	}
	
	public int getGeneralTimeout() {
		return generalTimeout;
	}

	public void setGeneralTimeout(int generalTimeout) {
		int old = this.generalTimeout;
		this.generalTimeout = generalTimeout;
		this.pcs.firePropertyChange("generalTimeout", old, generalTimeout);
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
		
		firstExtraTimeout = false;
		secondExtraTimeout = false;
		
		this.pcs.firePropertyChange("reset", "old", "new");
	}
	
	public void setSlideTime(int slideTime) {
		this.slideTime = slideTime;
	}
	
	public int getSlideTime() {
		return slideTime;
	}
	
	public boolean isFirstExtraTimeout() {
		return firstExtraTimeout;
	}

	public void setFirstExtraTimeout(boolean firstExtraTimeout) {
		this.firstExtraTimeout = firstExtraTimeout;
	}

	public boolean isSecondExtraTimeout() {
		return secondExtraTimeout;
	}

	public void setSecondExtraTimeout(boolean secondExtraTimeout) {
		this.secondExtraTimeout = secondExtraTimeout;
	}
}