package org.tiqwab.count.rect.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

public class LineDividerManager {
    
    public static void divideLines(Set<Line> Hlines, Set<Line> Vlines, Set<Line> resultLines) {    
        Map<Line, LineDivider> map = new HashMap<Line, LineDivider>();
        for (Line line : Hlines) {
            LineDivider ld = new LineDividerH(line);
            map.put(line, ld);
        }
        for (Line line : Vlines) {
            LineDivider ld = new LineDividerV(line);
            map.put(line, ld);
        }
        
        for (Line hline : Hlines) {
            for (Line vline : Vlines) {
                Point p = hline.getCrossPoint(vline);
                if (hline.containsPoint(p) && vline.containsPoint(p)) {
                    map.get(hline).add(p.clone());
                    map.get(vline).add(p.clone());
                }
            }
        }
        
        for (LineDivider ld : map.values()) {
            resultLines.addAll(ld.generate());
        }
        System.out.println("resultLines.size(): " + resultLines.size());
        return;
    }
    
    
    public static void divideLines(Set<Line> Hlines, Set<Line> Vlines, Set<Line> Dlines, Set<Line> resultLines) {    
        Map<Line, LineDivider> map = new HashMap<Line, LineDivider>();
        for (Line line : Hlines) {
            LineDivider ld = new LineDividerH(line);
            map.put(line, ld);
        }
        for (Line line : Vlines) {
            LineDivider ld = new LineDividerV(line);
            map.put(line, ld);
        }
        for (Line line : Dlines) {
        	LineDivider ld = new LineDividerD(line);
        	map.put(line, ld);
        }
        
        for (Line hline : Hlines) {
            for (Line vline : Vlines) {
                Point p = hline.getCrossPoint(vline);
                if (hline.containsPoint(p) && vline.containsPoint(p)) {
                    map.get(hline).add(p.clone());
                    map.get(vline).add(p.clone());
                }
                
                for (Line dline : Dlines) {
                    Point q = vline.getCrossPoint(dline);
                    if (vline.containsPoint(q) && dline.containsPoint(q)) {
                        map.get(vline).add(q.clone());
                        map.get(dline).add(q.clone());
                    }
                }
            }
            
            for (Line dline : Dlines) {
                Point p = hline.getCrossPoint(dline);
                if (hline.containsPoint(p) && dline.containsPoint(p)) {
                    map.get(hline).add(p.clone());
                    map.get(dline).add(p.clone());
                }
            }
        }
        
        List<Line> DlineList = new ArrayList<Line>(Dlines);
        for (int i = 0; i < DlineList.size(); i++) {
        	for (int j = i + 1; j < DlineList.size(); j++) {
        		Line li = DlineList.get(i);
        		Line lj = DlineList.get(j);
        		Point p = li.getCrossPoint(lj);
        		if (p != null) {
        			if (li.containsPoint(p) && lj.containsPoint(p)) {       				
        				map.get(li).add(p.clone());
        				map.get(lj).add(p.clone());
        			}
        		}
        	}
        }
        
        for (LineDivider ld : map.values()) {
            resultLines.addAll(ld.generate());
        }
        System.out.println("resultLines.size(): " + resultLines.size());
        return;
    }
    
}
