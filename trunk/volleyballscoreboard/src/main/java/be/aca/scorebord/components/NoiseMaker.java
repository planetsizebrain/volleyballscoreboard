package be.aca.scorebord.components;

import java.applet.Applet;
import java.applet.AudioClip;


public class NoiseMaker {

	private static AudioClip horn, beep;
	
	static {
		horn = Applet.newAudioClip(NoiseMaker.class.getResource("/horn.au"));
		beep = Applet.newAudioClip(NoiseMaker.class.getResource("/beep.au"));
	}
	
	public static void horn() {
		horn.play();
	}
	
	public static void beep() {
		beep.play();
	}
}
