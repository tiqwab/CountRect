package org.tiqwab.count.rect.process;

import org.opencv.core.Scalar;
import org.tiqwab.count.rect.image.Point;

import processing.core.PApplet;

public class Rect {
	
	Point leftDown;
	Point leftUp;
	Point rightDown;
	Point rightUp;
	
	
	public Rect(Point leftDown, Point leftUp, Point rightDown, Point rightUp) {
		this.leftDown = leftDown;
		this.leftUp = leftUp;
		this.rightDown = rightDown;
		this.rightUp = rightUp;
	}	
	
	
	public Point getLeftDown() {
		return leftDown;
	}
	

	public Point getLeftUp() {
		return leftUp;
	}

	
	public Point getRightDown() {
		return rightDown;
	}


	public Point getRightUp() {
		return rightUp;
	}


	/*
	public void draw(Mat mat, Scalar scalar) {
		List<Line> list = new ArrayList<Line>();
		list.add(new Line(leftDown, leftUp));
		list.add(new Line(leftUp, rightUp));
		list.add(new Line(rightUp, rightDown));
		list.add(new Line(rightDown, leftDown));
		
		for (Line line : list) {
			Core.line(mat, 
					  new org.opencv.core.Point(line.start.getX(), line.start.getY()), 
					  new org.opencv.core.Point(line.end.getX(), line.end.getY()), 
					  scalar,
					  1
					  );
		}
	}
	*/
	
	
	public void draw(PApplet p, Scalar scalar) {
		double[] val = scalar.val;
		p.fill((float)val[2], (float)val[1], (float)val[0], (float)val[3]);  /* convert BGRA to RGBA */
		p.noStroke();
		p.beginShape();
		    p.vertex((float)leftDown.getX(), (float)leftDown.getY());
		    p.vertex((float)leftUp.getX(), (float)leftUp.getY());
		    p.vertex((float)rightUp.getX(), (float)rightUp.getY());
		    p.vertex((float)rightDown.getX(), (float)rightDown.getY());
		p.endShape(PApplet.CLOSE);
		p.stroke(1);
	}

}
