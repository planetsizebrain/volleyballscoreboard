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

import java.applet.Applet;
import java.awt.Color;

public class ScoreBoardSettings {

	private Color bgColor = Color.black;
	private Color timeColor = Color.yellow;
	private Color lastMinuteTimeColor = Color.red;
	private Color scoreColor = Color.green;
	private Color textColor = Color.yellow;
	private Color fillColor = Color.lightGray;
	private Color bonusLightColor = Color.red;
	private Color possesionArrowColor = Color.red;

	private String preferredFont = "Helvetica";
	private String slideShowDir;

	private int slideDelay = 15;
	private int numSlides = 3;
	private int scoreFontSize = 90;
	private int buttonFontSize = 14;
	private int timeFontSize = 100;
	private int framePositionX = 340;
	private int framePositionY = 0;
	private int maxPeriods = 4;
	private int timeout = 30;

	private int scoreboardMode; // 0 = Basketball, 1 = Volleyball

	public ScoreBoardSettings(Applet applet) {
		// bgColor = getParameterAsColor("bgColor", applet);
		// timeColor = getParameterAsColor("timeColor", applet);
		// lastMinuteTimeColor = getParameterAsColor("lastMinuteTimeColor",
		// applet);
		// scoreColor = getParameterAsColor("scoreColor", applet);
		// textColor = getParameterAsColor("textColor", applet);
		// fillColor = getParameterAsColor("fillColor", applet);
		// fillColor = getParameterAsColor("fillColor", applet);
		// fillColor = getParameterAsColor("fillColor", applet);
		//
		// preferredFont = getParameter("preferredFont", applet);
		// slideShowDir = getParameter("slideShowDir", applet);
		//
		// slideDelay = getParameterAsInt("slideDelay", applet);
		// numSlides = getParameterAsInt("numSlides", applet);
		// framePositionX = getParameterAsInt("framePositionX", applet);
		// framePositionY = getParameterAsInt("framePositionY", applet);
		// timeFontSize = getParameterAsInt("timeFontSize", applet);
		// scoreFontSize = getParameterAsInt("scoreFontSize", applet);
		// buttonFontSize = getParameterAsInt("buttonFontSize", applet);
		// maxPeriods = getParameterAsInt("maxPeriods", applet);
	}

	public Color getBgColor() {
		return bgColor;
	}

	public Color getTimeColor() {
		return timeColor;
	}

	public Color getLastMinuteTimeColor() {
		return lastMinuteTimeColor;
	}

	public Color getScoreColor() {
		return scoreColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getBonusLightColor() {
		return bonusLightColor;
	}

	public Color getPossesionArrowColor() {
		return possesionArrowColor;
	}

	public String getPreferredFont() {
		return preferredFont;
	}

	public String getSlideShowDir() {
		return slideShowDir;
	}

	public int getSlideDelay() {
		return slideDelay;
	}

	public int getNumSlides() {
		return numSlides;
	}

	public int getScoreFontSize() {
		return scoreFontSize;
	}

	public int getButtonFontSize() {
		return buttonFontSize;
	}

	public int getTimeFontSize() {
		return timeFontSize;
	}

	public int getFramePositionX() {
		return framePositionX;
	}

	public int getFramePositionY() {
		return framePositionY;
	}

	public int getMaxPeriods() {
		return maxPeriods;
	}

	public int getScoreboardMode() {
		return scoreboardMode;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	private String getParameter(String key, Applet applet) {
		return applet.getParameter(key);
	}

	private Color getParameterAsColor(String key, Applet applet) {
		String value = getParameter(key, applet);
		if (value != null) {
			return new Color(hexValue(value));
		} else {
			return Color.black;
		}
	}

	private int getParameterAsInt(String key, Applet applet) {
		String value = getParameter(key, applet);
		if (value != null) {
			return intValue(value);
		} else {
			return 0;
		}
	}

	private int hexValue(String str) {
		try {
			return (int) Integer.valueOf(str, 16).intValue();
		} catch (java.lang.NumberFormatException e) {
			return 0;
		}
	}

	private int intValue(String str) {
		try {
			return (int) Integer.valueOf(str).intValue();
		} catch (java.lang.NumberFormatException e) {
			return 0;
		}
	}
}