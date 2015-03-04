package org.tiqwab.count.rect.layout;

import processing.core.PApplet;

public class DynamicText {

	private PApplet p;
	protected String text;
	private boolean visible = false;
	private double x;
	private double y;
	
	public DynamicText(PApplet p, String text, double x, double y) {
		this.p = p;
		this.text = text;
		this.x = x;
		this.y = y;
	}
	
	
	public boolean isVisible() {
		return visible;
	}
	
	
	public void startShow() {
		visible = true;
	}
	
	
	public void show() {
		if (!visible) {
			return;
		}
		
		p.fill(0, 0, 0);
		p.textSize(20);
		p.textAlign(PApplet.CENTER);
		p.text(text, (float)x, (float)y);
	}
	
	
	public void endShow() {
		visible = false;
	}
}
