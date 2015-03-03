package org.tiqwab.count.rect.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Mat;
import org.tiqwab.count.rect.image.Condition;
import org.tiqwab.count.rect.image.Line;
import org.tiqwab.count.rect.image.LineGroup;
import org.tiqwab.count.rect.process.ErrorRangeUtil;

import processing.core.PApplet;

public class LineHelper {

	public static Mat detectLines(Mat matgr, Condition condition, List<Line> Hlines, List<Line>Vlines) {
		Mat lineMat = ImageHelper.getHoughLines(matgr);
		List<LineGroup> groupedLines = groupLine(lineMat, condition);
		List<Line> allLines = new ArrayList<Line>();
		for (LineGroup lp : groupedLines) {
			allLines.add(lp.getProduct());
			System.out.println(lp.getProduct());
		}
		
		System.out.println("LineGroupNum : " + groupedLines.size());
		
		double[] v_x = {1, 0};
		double[] v_y = {0, 1};
		getSameAngleLines(v_x, allLines, Hlines);
		getSameAngleLines(v_y, allLines, Vlines);
		System.out.println(String.format("Hlines, Vlines, Dlines=%d, %d", Hlines.size(), Vlines.size()));
		return lineMat;
	}
	
	
	public static Mat detectLines(Mat matgr, Condition condition, List<Line> Hlines, List<Line>Vlines, List<Line> Dlines) {
		Mat lineMat = ImageHelper.getHoughLines(matgr);
		List<LineGroup> groupedLines = groupLine(lineMat, condition);
		List<Line> allLines = new ArrayList<Line>();
		for (LineGroup lp : groupedLines) {
			allLines.add(lp.getProduct());
			System.out.println(lp.getProduct());
		}
		
		System.out.println("LineGroupNum : " + groupedLines.size());
		
		double[] v_x = {1, 0};
		double[] v_y = {0, 1};
		double[] v_dx = {1.0 / Math.sqrt(2), 1.0 / Math.sqrt(2)};
		double[] v_dy = {-1.0 / Math.sqrt(2), 1.0 / Math.sqrt(2)};
		getSameAngleLines(v_x, allLines, Hlines);
		getSameAngleLines(v_y, allLines, Vlines);
		getSameAngleLines(v_dx, allLines, Dlines);
		getSameAngleLines(v_dy, allLines, Dlines);
		System.out.println(String.format("Hlines, Vlines, Dlines=%d, %d, %d", Hlines.size(), Vlines.size(), Dlines.size()));
		
		return lineMat;
	}
	
	
	private static List<LineGroup> groupLine(Mat lin, Condition condition){
		if (lin.cols() == 0) return null;
		
		List<LineGroup> result = new ArrayList<LineGroup>();				
		List<Line> lins = convertMatToLine(lin);
		result.add(new LineGroup(lins.get(0).clone(), condition));
		
		for (int i = 1; i < lins.size(); i++) {
		    boolean hasSimilarLine = false;
		    Line l = lins.get(i);
		    for (LineGroup list : result) {
		    	if (list.similarTo(l)){
		    		list.add(l.clone());
		    		hasSimilarLine = true;
		    		break;
		    	}
		    }
		    if (!hasSimilarLine) {
		    	result.add(new LineGroup(l.clone(), condition));
		    }
		}
		
		for (LineGroup list : result) {
			list.merge();
		}
		
		return result;
	}
	
	
	public static List<Line> convertMatToLine(Mat lin) {
		List<Line> result = new ArrayList<Line>();
		for (int i = 0; i <lin.cols(); i++) {
			double[] data = lin.get(0, i);
			result.add(new Line(data[0], data[1], data[2], data[3]));
		}
		
		sortDirection(result);
		Collections.sort(result, new LineComparator());
		
		for (Line l : result) {
			System.out.println("sortedResult: " + l);
		}		
		return result;
	}
	
	
	public static void sortDirection(List<Line> list) {
		Comparator<Point> comp = Point.comparator;
		for (Line line : list) {
			Point p1 = line.start.clone();
			Point p2 = line.end.clone();
			if (comp.compare(p1, p2) > 0) {
				line.start = p2;
				line.end = p1;
			}
		}
	}
	
	
	private static class LineComparator implements Comparator<Line> {

		@Override
		public int compare(Line l1, Line l2) {
			Point p1, p2;
			
			if ((Math.pow(l1.start.getX(), 2) + Math.pow(l1.start.getY(), 2)) < 
					(Math.pow(l1.end.getX(), 2) + Math.pow(l1.end.getY(), 2))) {
				p1 = l1.start;
			} else {
				p1 = l1.end;
			}
			
			if ((Math.pow(l2.start.getX(), 2) + Math.pow(l2.start.getY(), 2)) < 
					(Math.pow(l2.end.getX(), 2) + Math.pow(l2.end.getY(), 2))) {
				p2 = l2.start;
			} else {
				p2 = l2.end;
			}
			
			return (new PointComparatorX().compare(p1, p2));
		}
		
	}
	
	
	private static void getSameAngleLines(double[] vec, List<Line> allLines, List<Line> result) {
		Line stLine = new Line(0, 0, vec[0], vec[1]);
		for (Line line : allLines) {
			//System.out.println(line);
			//System.out.println(Math.sin(stLine.getAngle(line)) + ", " + stLine.getAngle(line));
			
			if (ErrorRangeUtil.equal_triFunc(Math.sin(stLine.getAngle(line)), 0)) {
			//if (Math.sin(stLine.getAngle(line)) < ErrorRangeUtil.TRIFUNC_RANGE) {			
				result.add(line);
			}
		}
	}
}
