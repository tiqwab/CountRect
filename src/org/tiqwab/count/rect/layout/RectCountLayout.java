package org.tiqwab.count.rect.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.tiqwab.count.rect.image.ImageHelper;
import org.tiqwab.count.rect.image.Point;
import org.tiqwab.count.rect.process.Rect;
import org.tiqwab.count.rect.process.RectCountThread;

import processing.core.PApplet;
import processing.core.PImage;

public class RectCountLayout {
	
	Mat baseMat;
	Mat resultMat;
	PImage basePI;
	final Scalar basePIScalar;    //basePImage's x, y
	PImage resultPI;
	final Scalar resultPIScalar;  //resultPImage's x, y
	
	List<Rect> resultRects = new ArrayList<Rect>();
	Rect resultRect;
	Set<Point> resultVertices = new TreeSet<Point>(Point.comparator);
	int ind_rect = 0;
	int ind_rectNo = -1;
	
	DynamicText loadingText;
	
	final PApplet p;
	
	RectCountThread counterTh;
	
	double baseX, baseY, resultX, resultY;
	
	final double stageMinX, stageMaxX, stageMinY, stageMaxY;
	
	boolean isLoading = false;
	
	
	public RectCountLayout(PApplet p) {
		this.p = p;
		stageMinX = 0;
		stageMaxX = p.displayWidth;
		stageMinY = 0;
		stageMaxY = p.displayHeight * 7 / 8;
		basePIScalar = new Scalar((stageMaxX + stageMinX) / 2, (stageMaxY + 3 * stageMinY) / 4);
		resultPIScalar = new Scalar((stageMaxX + stageMinX) / 2, (3 * stageMaxY + stageMinY) / 4);
		
		loadingText = new LoadingText(p, "Loading", p.displayWidth - 100, p.displayHeight - 110);
	}
	
	
	public void setBaseMat(Mat src) {
		baseMat = ImageHelper.resizeMatWithSameRate(src, (stageMaxX - stageMinX), (stageMaxY - stageMinY) / 2);
	}
	
	
	public Mat getBaseMat() {
		return baseMat;
	}
	
	
	public void showBaseImage() {		
		PImage pi = ImageHelper.showImageToCenter(p, baseMat, basePIScalar.val[0], basePIScalar.val[1]);
		setBaseImage(pi);
	}
	
	
	public void setBaseImage(PImage pi) {
		basePI = pi;
	}
	
	
	public PImage getBaseImage() {		
		return basePI;
	}
	
	
	public void setResultMat(Mat src) {
		resultMat = ImageHelper.resizeMatWithSameRate(src, (stageMaxX - stageMinX), (stageMaxY - stageMinY) / 2);
	}
	
	
	public Mat getResultMat() {
		return resultMat;
	}
	
	
	public void showResultImage() {		
		PImage pi = ImageHelper.showImageToCenter(p, resultMat, resultPIScalar.val[0], resultPIScalar.val[1]);
		setResultImage(pi);
	}
	
	
	public void setResultImage(PImage pi) {
		resultPI = pi;
	}
	
	
	public PImage getResultImage() {
		return resultPI;
	}
	
	
	private void calcXY() {
		double diffX = resultPIScalar.val[0] - basePIScalar.val[0];
		double diffY = resultPIScalar.val[1] - basePIScalar.val[1];
		
		baseX = (basePIScalar.val[0] - (basePI.width / 2)) + stageMinX;
		baseY = (basePIScalar.val[1] - (basePI.height / 2)) + stageMinY;
		resultX = diffX + baseX;
		resultY = diffY + baseY;
	}
	
	
	public void setRect(Rect rect) {
		calcXY();
		
		Point newLeftDown, newLeftUp, newRightDown, newRightUp;
		newLeftDown = new Point(rect.getLeftDown().getX() + resultX, rect.getLeftDown().getY() + resultY);
		newLeftUp = new Point(rect.getLeftUp().getX() + resultX, rect.getLeftUp().getY() + resultY);
		newRightDown = new Point(rect.getRightDown().getX() + resultX, rect.getRightDown().getY() + resultY);
		newRightUp = new Point(rect.getRightUp().getX() + resultX, rect.getRightUp().getY() + resultY);
		
		Rect drawedRect = new Rect(newLeftDown, newLeftUp, newRightDown, newRightUp);
		this.resultRect = drawedRect;
	}
	
	
	public void showRect() {
		resultRect.draw(p, new Scalar(0, 255, 0, 50));
	}
	
	
	public void setResultRects(List<Rect> list) {
		this.resultRects = list;
	}
	
	
	public void showVertices() {
		calcXY();
		
		p.noStroke();
		p.fill(0, 255, 0);		
		for (Point point : resultVertices) {
			p.ellipse((float)(point.getX() + resultX), (float)(point.getY() + resultY), 10, 10);
		}
		p.stroke(1);
	}
	
	
	public void setVertices(Set<Point> points) {
		this.resultVertices = points;
	}
	
	
	public void showRectText() {
		String str = (ind_rectNo >= 0) ? 
				"Rect " + (ind_rectNo) + "/" + resultRects.size() : 
				"Rect " + (0) + "/" + resultRects.size();
			
		p.fill(0, 0, 0);
		p.textSize(20);
		p.textAlign(PApplet.CENTER);
		p.text(str, p.displayWidth / 2, p.displayHeight - 110);
	}
	
	
	public void showLoadingText() {
		/*
		String str = "Loading...";
		if (!loadingText.isVisible()) {
			loadingText.startShow(p.displayWidth - 100, p.displayHeight - 110);
		}
		
		p.fill(0, 0, 0);
		p.textSize(20);
		p.textAlign(PApplet.CENTER);
		p.text(str, p.displayWidth - 100, p.displayHeight - 110);
		*/
		loadingText.show();
	}
	
	
	public void draw() {
		refresh();
		
		if (getBaseImage() != null) {
			this.showBaseImage();
		}
		
		if (getResultImage() != null) {
			this.showResultImage();
		}
		
		if (resultRect != null) {
			this.showRect();
		}
		
		if (resultVertices.size() > 0) {
			this.showVertices();
		}
		
		if (isLoading) {
			showLoadingText();
			if (counterTh.getRects() != null) {
				setVertices(new TreeSet<Point>(counterTh.getVertices()));
				setResultRects(new ArrayList<Rect>(counterTh.getRects()));
				finishDetect();
			}
		}
		
		showRectText();
	}
	
	
	public void nextRect() {
		if (resultRects.size() > 0) {
			ind_rectNo = ind_rect + 1;
			setRect(resultRects.get(ind_rect++));			
			if (ind_rect > (resultRects.size() - 1)) {
				ind_rect = 0;
			}			
		}
	}
	
	
	public void beginDetect() {
		isLoading = true;
		loadingText.startShow();
	}
	
	
	public void finishDetect() {
		isLoading = false;
		loadingText.endShow();
		if (counterTh != null) {
			counterTh.cancel();
			counterTh = null;
		}
	}
	
	
	public void setupToolBar(RectToolBar tool) {
		tool.setupFileBtn(0, (p.displayHeight - 100), 200, 100);
		tool.setupNextBtn((p.displayWidth - 200) / 2, (p.displayHeight - 100), 200, 100);
		tool.setupDetectBtn((p.displayWidth - 200), (p.displayHeight - 100), 200, 100);
		tool.setupCheckBox(10, p.displayHeight - 140, 30, 30);
	}
	
	
	public void setCounter(RectCountThread countTh) {
		this.counterTh = countTh;
	}
	
	
	public void refresh() {
    	p.fill(255);
    	p.rect(0, 0, p.displayWidth, p.displayHeight);
    	p.fill(0);
    	p.line((float)stageMinX, (float)((stageMinY + stageMaxY) / 2), (float)stageMaxX, (float)((stageMinY + stageMaxY) / 2));
    	p.line((float)stageMinX, (float)(stageMaxY), (float)stageMaxX, (float)(stageMaxY));
	}
	
	
    public void clear() {
    	basePI = null;
    	resultPI = null;
    	resultRect = null;
    	resultRects.clear();
    	resultVertices.clear();
    	ind_rect = 0;
    	ind_rectNo = -1;
		
    	finishDetect();
    	refresh();
    }

}
