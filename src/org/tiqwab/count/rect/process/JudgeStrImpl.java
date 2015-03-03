package org.tiqwab.count.rect.process;

import org.tiqwab.count.rect.image.Point;

public abstract class JudgeStrImpl implements JudgeStr{
	
	//private static final double ABOUTRANGE = 5;
	

	@Override
	public boolean judgeSameScalar(double a, double b) {
		return ErrorRangeUtil.equal(a, b);
	}

	
	@Override
	public boolean judgeSamePoint(Point p1, Point p2) {
		return ErrorRangeUtil.equal_point(p1, p2);
	}
	
}
