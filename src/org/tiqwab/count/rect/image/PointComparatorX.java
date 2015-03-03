package org.tiqwab.count.rect.image;

import java.util.Comparator;

public class PointComparatorX implements Comparator<Point> {

	@Override
	public int compare(Point lhs, Point rhs) {
		int result = Double.compare(lhs.getX(), rhs.getX());
		if (result == 0) {
			result = Double.compare(lhs.getY(), rhs.getY());
		}
		return result;
		
		/*
        if (lhs.getX() < rhs.getX()) {
            return -1;
        }
        if (lhs.getX() > rhs.getX()) {
            return 1;
        }
        if (lhs.getY() < rhs.getY()) {
            return -1;
        }
        if (lhs.getY() > rhs.getY()) {
            return 1;
        }
        return 0;
        */
	}
}
