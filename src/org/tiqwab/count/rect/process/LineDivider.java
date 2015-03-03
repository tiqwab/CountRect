package org.tiqwab.count.rect.process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;

abstract class LineDivider {
    
    protected Line line;
    protected NavigableSet<Point> points;
    protected Comparator<Point> comparator;
    
    protected LineDivider(Line line) {
        this.line = line;
        points = new TreeSet<Point>(Point.comparator);
        points.add(line.start);
        points.add(line.end);
    }
    
    
    protected LineDivider(Line line, Comparator<Point> comparator) {
        this.line = line;
        points = new TreeSet<Point>(comparator);
        points.add(line.start);
        points.add(line.end);
    }
    
    
    void add(Point p) {
        if (line.containsPoint(p)) {
            points.add(p);
        }      
    }
    
    
    protected NavigableSet<Point> getPoints() {
        return points;
    }
    
    
    protected List<Line> generate() {
        assert (comparator != null);
        
        List<Line> result = new ArrayList<Line>();
        List<Point> pointList = new ArrayList<Point>(points);
        
        for (int i = 0; i < pointList.size() - 1; i++) {
            for (int j = i + 1; j < pointList.size(); j++) {
                Line line = new Line(pointList.get(i).clone(), pointList.get(j).clone());
                result.add(line);
            }
        }
        
        return result;
    }

}
