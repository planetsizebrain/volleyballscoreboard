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