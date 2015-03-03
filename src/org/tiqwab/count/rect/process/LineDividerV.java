package org.tiqwab.count.rect.process;

import java.util.Comparator;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

public class LineDividerV extends LineDivider{
    
	private static final Comparator<Point> compV = new Comparator<Point>() {
        @Override
        public int compare(Point p0, Point p1) {
        	return (Double.compare(p0.getY(), p1.getY()));
        }
	};
	
    protected LineDividerV(Line line) {
    	super(line, compV);
    }

}
