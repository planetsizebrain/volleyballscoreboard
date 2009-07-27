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
package be.aca.scorebord.components;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jdesktop.application.Action;

import be.aca.scorebord.domain.ScoreboardModel;

public class Slideshow {

	private String path;
	private List<Image> images;
	
	private int currentSlideIndex = 0;
	private boolean stopTimer = false;
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Timer timer = new Timer();
	private ScoreboardModel model;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public Slideshow(ScoreboardModel model, String path, List<String> messages) throws IOException {
		this.model = model;
		this.path = path;
		
		File base = new File("slides.tmp");
		base.deleteOnExit();
		File root = new File(base.getAbsolutePath().substring(0, base.getAbsolutePath().length() - 10) + path);
		
		File[] files = root.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".gif");
			}});
		
		images = new ArrayList<Image>(files.length);
		for (int i = 0; i < files.length; i++) {
			images.add(toolkit.createImage(files[i].getAbsolutePath()));
		}
		
		for (String message : messages) {
			BufferedImage messageImage = new BufferedImage(1024, 320, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = (Graphics2D) messageImage.getGraphics();
			
			String[] parts = message.split("\n");
			String longest = "";
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].length() > longest.length()) {
					longest = parts[i];
				}
			}
			
			int fontSize = getMaximumFontSize(longest, 1024, 320 / parts.length, g2d);
			Font font = new Font("Helvetica", Font.BOLD, fontSize);
			g2d.setFont(font);
			
			for (int i = 0; i < parts.length; i++) {
				g2d.drawString(parts[i], 25, 5 + fontSize + (i * (275 / parts.length)));
			}
			
			images.add(messageImage);
		}
	}
	
	public List<Image> getSlides() {
		return Collections.unmodifiableList(images);
	}
	
	public Image getCurrentSlide() throws MalformedURLException {
		return images.get(currentSlideIndex);
	}
	
	@Action
	public void start() {
		stopTimer = false;
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				if (stopTimer) {
					cancel();
				} else {
					int old = currentSlideIndex;
					if (currentSlideIndex < (images.size() - 1)) {
						currentSlideIndex++;
					} else {
						currentSlideIndex = 0;
					}
					
					pcs.firePropertyChange("currentSlide", old, currentSlideIndex);
				}
			}}, 0, model.getSlideTime());
	}
	
	@Action
	public void stop() {
		stopTimer = true;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
	private int getMaximumFontSize(String text, int width, int maxHeight, Graphics2D g2d) {
		int size = 50;
		Font font = new Font("Helvetica", Font.BOLD, size);
		FontMetrics metrics = g2d.getFontMetrics(font);
		
		int servingWidth = (int) metrics.getStringBounds(text, g2d).getWidth();
		while (servingWidth >= width && metrics.getHeight() <= maxHeight) {
			font = new Font("Helvetica", Font.BOLD, --size);
			metrics = g2d.getFontMetrics(font);
			
			servingWidth = (int) metrics.getStringBounds(text, g2d).getWidth();
		}
		
		return size;
	}
}
