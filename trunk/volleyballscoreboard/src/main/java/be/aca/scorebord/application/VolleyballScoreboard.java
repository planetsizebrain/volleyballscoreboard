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
package be.aca.scorebord.application;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.FileInputStream;
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
			settings.load(new FileInputStream("settings.properties"));
			//settings.load(VolleyballScoreboard.class.getResourceAsStream("/settings2.properties"));
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
