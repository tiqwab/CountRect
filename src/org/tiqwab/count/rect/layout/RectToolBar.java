package org.tiqwab.count.rect.layout;

import org.tiqwab.count.rect.image.ImgConfigManager;

import processing.core.PApplet;
import controlP5.Button;
import controlP5.CColor;
import controlP5.CheckBox;
import controlP5.ControlP5;

public class RectToolBar {
	
	private PApplet p;
	private ControlP5 cp5;
	
	private Button fileBtn;
	private Button nextBtn;
	private Button detectBtn;
	private CheckBox squareCheck;
	
	private int detect_lv = 0;
	
	
	public RectToolBar(PApplet p, ControlP5 cp5) {
		this.p = p;
		this.cp5 = cp5;
	}
	
	
	public void setFileBtn(Button btn) {
		this.fileBtn = btn;
	}
	
	
	public void setNextBtn(Button btn) {
		this.nextBtn = btn;
	}
	
	
	public void setDetectBtn(Button btn) {
		this.detectBtn = btn;
	}
	
	
	public void setSquareCheck(CheckBox chb) {
		this.squareCheck = chb;
	}
	
	
	public void setupFileBtn(float x, float y, int w, int h) {
		fileBtn = cp5.addButton("ClickOnReadImage").setValue(0);
		fileBtn.setPosition(x, y).setSize(w, h);
		fileBtn.getCaptionLabel().setSize(20).align(ControlP5.CENTER, ControlP5.CENTER);
	}
	
	
	public void setupNextBtn(float x, float y, int w, int h) {
		nextBtn = cp5.addButton("NextRect").setValue(0);
		nextBtn.setPosition(x, y).setSize(w, h);
		nextBtn.getCaptionLabel().setSize(20).align(ControlP5.CENTER, ControlP5.CENTER);
	}
	
	
	public void setupDetectBtn(float x, float y, int w, int h) {
		detectBtn = cp5.addButton("DetectionLevel").setValue(detect_lv);
		detectBtn.setPosition(x, y).setSize(w, h);
		detectBtn.getCaptionLabel().setSize(20).align(ControlP5.CENTER, ControlP5.CENTER);
		
		doDetect();
	}
	
	
	public void setupCheckBox(float x, float y, int w, int h) {
		squareCheck = cp5.addCheckBox("SquareCheck")
                .setPosition(x, y)
                .setColorForeground(p.color(0, 0, 100))
                .setColorActive(p.color(0, 0, 200))
                .setColorLabel(p.color(0))
                .setSize(w, h)
                .setItemsPerRow(3)
                .setSpacingColumn(30)
                .setSpacingRow(20)
                .addItem("Only Square", 0);
		squareCheck.getItem(0).getCaptionLabel().setSize(20);
	}
	
	
	public void doDetect() {
    	detect_lv = (detect_lv + 1) % 3;
    	
    	if (detect_lv == 0) {
    	    CColor red = new CColor();
    	    red.setForeground(p.color(200,0,0));
    	    red.setActive(p.color(200,0,0));
    	    red.setBackground(p.color(125,0,0));
    		detectBtn.setColor(red);
    		detectBtn.getCaptionLabel().setText("DetectLevel-H");
    		new ImgConfigManager.HoughConfig().hl_setHigh();
    		
    	} else if (detect_lv == 1) {
    	    CColor green = new CColor();
    	    green.setForeground(p.color(0,200,0));
    	    green.setActive(p.color(0,200,0));
    	    green.setBackground(p.color(0,125,0));
    		detectBtn.setColor(green);
    		detectBtn.getCaptionLabel().setText("DetectLevel-M");
    		new ImgConfigManager.HoughConfig().hl_setMid();
    		
    	} else {
    	    CColor blue = new CColor();
    	    blue.setForeground(p.color(0,0,200));
    	    blue.setActive(p.color(0,0,200));
    	    blue.setBackground(p.color(0,0,125));
    		detectBtn.setColor(blue);
    		detectBtn.getCaptionLabel().setText("DetectLevel-L");
    		new ImgConfigManager.HoughConfig().hl_setLow();
    	}
	}

}
