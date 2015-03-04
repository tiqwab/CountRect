package org.tiqwab.count.rect.process;

import org.tiqwab.count.rect.image.Point;

public class ErrorRangeUtil {

    public static final double ERR_RANGE = 10;
    public static final double TRIFUNC_RANGE = 0.05;
    public static final double LINE_EFFECTIVE_XY_RANGE = 5;
	
	
    public static boolean equal(double a, double b) {
        return (Math.abs(a - b) <= ERR_RANGE);
    }
    
	
    public static boolean isDiff(double a, double b) {
    	return (Math.abs(a - b) > ERR_RANGE);
    }
    
    
    public static boolean isGE(double a, double b) {
    	return (a - b) >= ERR_RANGE;
    }
    
    
    public static boolean isLE(double a, double b) {
    	return (b - a) >= ERR_RANGE;
    }
    
    
    public static boolean equal_triFunc(double a, double b) {
    	return (Math.abs(a - b) <= TRIFUNC_RANGE);
    }
    
    
    public static boolean equal_point(Point p, Point q) {
    	return (ErrorRangeUtil.equal(p.getX(), q.getX()) && ErrorRangeUtil.equal(p.getY(), q.getY()));
    }

}
