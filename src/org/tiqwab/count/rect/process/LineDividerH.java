package org.tiqwab.count.rect.process;

import java.util.Comparator;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

public class LineDividerH extends LineDivider{

	private static final Comparator<Point> compH = new Comparator<Point>() {
        @Override
        public int compare(Point p0, Point p1) {
        	return (Double.compare(p0.getX(), p1.getX()));
        }
	};
	
    protected LineDividerH(Line line) {
    	super(line, compH);
    }

}
