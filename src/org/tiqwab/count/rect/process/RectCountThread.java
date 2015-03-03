package org.tiqwab.count.rect.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.Point;
import org.tiqwab.count.rect.image.PointComparatorX;
import org.tiqwab.count.rect.image.PointComparatorY;

public class RectCountThread extends Thread {
	
	private NavigableSet<Point> vertices;
	private List<Rect> rects;
	
	private List<Line> sampleLines;
	private JudgeStr strategy;
	
	private volatile boolean isCancelled = false;
	
	
	public RectCountThread(List<Line> lines, JudgeStr strategy) {
		super();
		this.sampleLines = lines;
		this.strategy = strategy;
	}
	
	
	public NavigableSet<Point> getVertices() {
		return vertices;
	}
	
	
	private void setVertices(NavigableSet<Point> set) {
		this.vertices = set;
	}
	
	
	public List<Rect> getRects() {
		return rects;
	}
	
	
	private void setRects(List<Rect> rects) {
		this.rects = rects;
	}
	
	
	public void setLines(List<Line> lines) {
		this.sampleLines = lines;
	}
	
	
	public void setStrategy(JudgeStr str) {
		this.strategy = str;
	}
	
	
	public void cancel() {
		isCancelled = true;
	}
	
	
	public NavigableSet<Point> detectVertices(List<Line> lines) {
    	NavigableSet<Point> points = formatPoints((TreeSet<Point>) Line.convertLinesToPoints(lines));       
        setVertices(points);
        System.out.println("points.size(): " + points.size());
        for (Point p : points) {
        	System.out.println(p);
        }
        return points;
	}
	
	
	@Override
	public void run() {
		assert(sampleLines != null && strategy != null);
		
		detectVertices(sampleLines);
		
    	NavigableSet<Point> points = vertices;
    	
        List<Rect> rects = new ArrayList<Rect>();
        if (points.size() < 4) {
            setRects(rects);
            return;
        }
        
        int count = 0;
        for (Point leftDown : points) {
            NavigableSet<Point> potentialLeftUp = points.tailSet(leftDown,
                    false);

            for (Point leftUp : potentialLeftUp) {
                NavigableSet<Point> potentialRightDown = potentialLeftUp
                        .tailSet(leftUp, false);

                for (Point rightDown : potentialRightDown) {
                    NavigableSet<Point> potentialRightUp = potentialRightDown
                            .tailSet(rightDown, false);

                    for (Point rightUp : potentialRightUp) {
                    	if (!isCancelled) {
                            if (strategy.judgeTetragon(leftDown, leftUp, rightDown, rightUp)) {
                                if (hasLine(sampleLines, leftDown, leftUp, rightDown, rightUp, strategy)) {
                                    count++;
                                    rects.add(new Rect(leftDown, leftUp, rightDown, rightUp));
                                    System.out.println("rect" + count);
                                    System.out.println(leftDown);
                                    System.out.println(leftUp);
                                    System.out.println(rightDown);
                                    System.out.println(rightUp);
                                }
                            }
                    	} else { /* isCancelled */
                    		setRects(null);
                    		return;
                    	}
                    }
                }
            }
        }

        setRects(rects);
        return;
	}
    
    
    private void formatPointsX(NavigableSet<Point> points) {
    	if (points.size() < 2) {
    		return;
    	}
    	
    	double average = 0;
    	for (Point p : points) {
    		average += p.getX();
    	}
    	average /= points.size();
    	for (Point p : points) {
    		p.setX(average);
    	}
    }
    
    
    private void formatPointsY(NavigableSet<Point> points) {
    	if (points.size() < 2) {
    		return;
    	}
    	
    	double average = 0;
    	for (Point p : points) {
    		average += p.getY();
    	}
    	average /= points.size();
    	for (Point p : points) {
    		p.setY(average);
    	}
    }
    
    
    private NavigableSet<Point> formatPoints(TreeSet<Point> points) {
    	if (points.size() < 2) {
    		return points;
    	}
    	
    	Comparator<Point> initComp = (Comparator<Point>) points.comparator();
    	
    	//Alignment of x
    	NavigableSet<Point> sortedPoints = new TreeSet<Point>(new PointComparatorX());
    	sortedPoints.addAll(points);
    	Point start = sortedPoints.first();
    	Point end = null;
    	for (Point p : sortedPoints) {
    		if (ErrorRangeUtil.isDiff(start.getX(), p.getX())) {
    			end = sortedPoints.lower(p);
    			formatPointsX(sortedPoints.subSet(start, true, end, true));
    			start = p;
    			end = null;
    		}
    	}
    	if (start != sortedPoints.last()) {
			end = sortedPoints.last();
			formatPointsX(sortedPoints.subSet(start, true, end, true));
    	}
    	
    	//Alignment of y
    	sortedPoints = new TreeSet<Point>(new PointComparatorY());
    	sortedPoints.addAll(points);
    	start = sortedPoints.first();
    	end = null;
    	for (Point p : sortedPoints) {
    		if (ErrorRangeUtil.isDiff(start.getY(), p.getY())) {
    			end = sortedPoints.lower(p);
    			formatPointsY(sortedPoints.subSet(start, true, end, true));
    			start = p;
    			end = null;
    		}
    	}
    	if (start != sortedPoints.last()) {
			end = sortedPoints.last();
			formatPointsY(sortedPoints.subSet(start, true, end, true));
    	}
    	
    	sortedPoints = new TreeSet<Point>(initComp);
    	sortedPoints.addAll(points);
    	List<Point> sortedPointList = new ArrayList<Point>(sortedPoints);
    	System.out.println("Before remove");
    	for (Point p : sortedPointList) {
    		System.out.println(p);
    	}
    	
    	List<Point> removes = new ArrayList<Point>();
    	for (int i = 0; i < sortedPointList.size(); i++) {
    		for (int j = i + 1; j < sortedPointList.size(); j++) {
    			Point p = sortedPointList.get(i);
    			Point q = sortedPointList.get(j);
        		if (ErrorRangeUtil.equal_point(p, q)) {
        			removes.add(q);
        		}
    		}
    	}
    	sortedPoints.removeAll(removes);
    	return sortedPoints;
    }
    
    
    private boolean hasLine(List<Line> lines, Point ld, Point lu, Point rd, Point ru, JudgeStr strategy) {
        List<Line> candidates = new ArrayList<Line>();
        candidates.add(new Line(ld, lu));
        candidates.add(new Line(lu, ru));
        candidates.add(new Line(ru, rd));
        candidates.add(new Line(rd, ld));
        
        for (Line cand : candidates) {
        	boolean ismatch = false;
        	for (Line line : lines) {
        		if (strategy.judgeSamePoint(cand.start, line.start)) {
        			if (strategy.judgeSamePoint(cand.end, line.end)) {
        				ismatch = true;
        			}
        		} else if (strategy.judgeSamePoint(cand.start, line.end)) {
        			if (strategy.judgeSamePoint(cand.end, line.start)) {
        				ismatch = true;
        			}
        		}
        	}
        	if (!ismatch) {
        		return false;
        	}
        }
        
        return true;
    }
}
