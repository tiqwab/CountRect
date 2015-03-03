package org.tiqwab.count.rect.process;

import org.tiqwab.count.rect.image.Point;

public class JudgeImplHV extends JudgeStrImpl implements JudgeStr {

    @Override
    public boolean judgeTetragon(Point ld, Point lu, Point rd, Point ru) {
        if (!judgeSameScalar(ld.getX(), lu.getX())) {
            return false;
        }

        if (!judgeSameScalar(ld.getY(), rd.getY())) {
            return false;
        }
        
        Point expectedRu = new Point(rd.getX(), lu.getY());
        if (!judgeSamePoint(ru, expectedRu)) {
            return false;
        }

        return true;
    }
}
