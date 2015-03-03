package org.tiqwab.count.rect.image;

import java.util.Comparator;

import org.tiqwab.count.rect.image.Point;

public class PointComparatorY implements Comparator<Point>{

	@Override
	public int compare(Point lhs, Point rhs) {
		int result = Double.compare(lhs.getY(), rhs.getY());
		if (result == 0) {
			result = Double.compare(lhs.getX(), rhs.getX());
		}
		return result;
		
		/*
        if (lhs.getY() < rhs.getY()) {
            return -1;
        }
        if (lhs.getY() > rhs.getY()) {
            return 1;
        }
        if (lhs.getX() < rhs.getX()) {
            return -1;
        }
        if (lhs.getX() > rhs.getX()) {
            return 1;
        }
        return 0;
        */
	}

}
