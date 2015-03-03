package org.tiqwab.count.rect.process;

import java.util.Comparator;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

public class LineDividerD extends LineDivider{
	
	private static final Comparator<Point> compD = new Comparator<Point>() {
        @Override
        public int compare(Point p0, Point p1) {
        	int r;
        	r = Double.compare(p0.getX(), p1.getX());
        	if (r == 0) {
        		r = Double.compare(p0.getY(), p1.getY());
        	}
        	return r;
        }		
	};
	
	protected LineDividerD(Line line) {
		super(line, compD);
	}
}
