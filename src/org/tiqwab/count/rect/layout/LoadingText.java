package org.tiqwab.count.rect.layout;

import processing.core.PApplet;

public class LoadingText extends DynamicText {

	private String originalText;
	private final String dot = ".";
	private final int frameCount;
	private int frame = 0;
	private int index = 0;
	
	
	public LoadingText(PApplet p, String text, double x, double y) {
		super(p, text, x, y);
		originalText = text;
		frameCount = p.frameCount;
	}
	
	
	@Override
	public void show() {
		frame %= frameCount;
		if (frame == 0) {
			index = (index + 1) % 4;
			
			StringBuilder sb = new StringBuilder();
			int dotNum = index % 4;
			int spaceNum = 4 - dotNum;
			for (int i = 0; i < dotNum; i++) {
				sb.append(dot);
			}
			for (int i = 0; i < spaceNum; i++) {
				sb.append(" ");
			}
			
			text = originalText + sb.toString();
		}
		frame++;
		super.show();
	}

}
