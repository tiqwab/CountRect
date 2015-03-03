package org.tiqwab.count.rect.process;

import org.tiqwab.count.rect.image.Point;

public interface JudgeStr {
	
	public boolean judgeTetragon(Point p1, Point p2, Point p3, Point p4);
	
	public boolean judgeSameScalar(double a, double b);
	
	public boolean judgeSamePoint(Point p1, Point p2);

}
