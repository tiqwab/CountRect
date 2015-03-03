package org.tiqwab.count.rect.process;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

public class JudgeImplHVD extends JudgeStrImpl implements JudgeStr {
	
    @Override
    public boolean judgeTetragon(Point ld, Point lu, Point rd, Point ru) {
    	/* x-, y-axis */
    	if (doJudge(ld, lu, rd, ru)) {
    		return true;
    	}
        
        /* Rotate axis 45 degree and judge */
        //Get center of the four points
        Line l1 = new Line(ld, ru);
        Line l2 = new Line(lu, rd);
        Point center = l1.getCrossPoint(l2);
        if (center == null) {
        	return false;
        }
        
        //Rotate
        Point newLd = Point.rotate(ld, center, (Math.PI / 4));
        Point newLu = Point.rotate(lu, center, (Math.PI / 4));
        Point newRd = Point.rotate(rd, center, (Math.PI / 4));
        Point newRu = Point.rotate(ru, center, (Math.PI / 4));

        if (doJudge(newLd, newLu, newRd, newRu)) {
        	return true;
        }
        
        return false;
    }
    
    
    private boolean doJudge(Point ld, Point lu, Point rd, Point ru) {
        if (judgeSameScalar(ld.getX(), lu.getX())) {
        	if (judgeSameScalar(ld.getY(), rd.getY())) {
                Point expectedRu = new Point(rd.getX(), lu.getY());
                if (judgeSamePoint(ru, expectedRu)) {
                    return true;
                }
        	}
        }
        return false;
    }
}
