package org.tiqwab.count.rect.image;

import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.tiqwab.count.rect.process.ErrorRangeUtil;

public class Line implements Cloneable{
	
	public Point start;
	public Point end;
	
	
	public Line(Point start, Point end){
		this.start = start;
		this.end = end;
	}
	
	
	public Line(double x1, double y1, double x2, double y2){
		this.start = new Point(x1, y1);
		this.end = new Point(x2, y2);
	}
	
	
    public Point getStart() {
        return start;
    }


    public Point getEnd() {
        return end;
    }
	
    
	public double getLength(){
		return Math.sqrt(Math.pow(end.y-start.y, 2) + Math.pow(end.x-start.x, 2));
	}
	
	
	public double getSlope() {
		if (end.x - start.x == 0) {
			return Double.MAX_VALUE;
		}
		return (end.y-start.y)/(end.x-start.x);
	}
	
	
	public double getDistance(Point pt){
		double[] vec1 = new double[2];
		double[] vec2 = new double[2];
		vec1[0] = end.x-start.x;
		vec1[1] = end.y-start.y;
		vec2[0] = pt.x-start.x;
		vec2[1] = pt.y-start.y;		
		double cross_vector = vec1[0]*vec2[1]-vec1[1]*vec2[0];
		double area = Math.abs(cross_vector);
		return(area/getLength());
	}
	
	
	public double getAngle(Line li){
		double[] vec1 = {(end.getX() - start.getX()), (end.getY() - start.getY())};
		double[] vec2 = {(li.end.getX() - li.start.getX()), (li.end.getY() - li.start.getY())};
		
		double dot_product = (vec1[0] * vec2[0]) + (vec1[1] * vec2[1]);
		double cos = dot_product / (getLength() * li.getLength());
		if (ErrorRangeUtil.equal_triFunc(cos, 1)) {
			cos = 1;
		} else if (ErrorRangeUtil.equal_triFunc(cos, -1)) {
			cos = -1;
		}
		
		return Math.acos(cos);
	}
	
	
	public Line getEvLine(){
		double[] vec1 = new double[2];
		vec1[0] = end.x-start.x;
		vec1[1] = end.y-start.y;
		vec1[0] /= getLength();
		vec1[1] /= getLength();
		
		if (vec1[0] == 0) {
			return new Line(new Point(0, 0), new Point(1, 0));
		}
		if (vec1[1] == 0) {
			return new Line(new Point(0, 0), new Point(0, 1));
		}
		
		double evSlope = -1.0 * vec1[0] / vec1[1];		
		double theta = Math.atan(evSlope);
		return new Line(new Point(0,0), new Point(Math.cos(theta), Math.sin(theta)));
	}
	
	
	public Point getCrossPoint(Line line) {
		Point cp = new Point();
		Point p1 = this.start;
		Point p2 = this.end;
		Point p3 = line.start;
		Point p4 = line.end;
		double dev = (p2.y-p1.y) * (p4.x-p3.x) - (p2.x-p1.x) * (p4.y-p3.y);
		if (dev == 0) {
			//throw new IllegalArgumentException("Line1: " + toString() + ", " + "Line2: " + line.toString());
			return null;
		}
		
		double d1, d2;
		d1 = (p3.y*p4.x-p3.x*p4.y);
		d2 = (p1.y*p2.x-p1.x*p2.y);

		cp.x = d1*(p2.x-p1.x) - d2*(p4.x-p3.x);
		cp.x /= dev;
		cp.y = d1*(p2.y-p1.y) - d2*(p4.y-p3.y);
		cp.y /= dev;
		return cp;
	}
	
	
	public Line getRotatedLine(double theta) {
		Point mp = new Point((start.x+end.x)/2.0, (start.y+end.y)/2.0);
		return new Line(Point.rotate(start, mp, theta), Point.rotate(end, mp, theta));
	}
	
	
	@Override
	public Line clone(){
		Line l;
		try {
			l = (Line)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		l.start = this.start.clone();
		l.end = this.end.clone();
		return l;
	}
	
	
	@Override
	public String toString(){
		return ("(" + start.x + "," + start.y + ")->(" + end.x + "," + end.y + ")");
	}
	
	
    @Override
    public int hashCode() {
        return (start.hashCode() + end.hashCode());
        //Reference: AbstractSet#hashCode()
        /*
        public int hashCode() {
            int h = 0;
            Iterator<E> i = iterator();
            while (i.hasNext()) {
                E obj = i.next();
                if (obj != null)
                    h += obj.hashCode();
            }
            return h;
        }
        */
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Line other = (Line) obj;
        
        //Lineのstart, endにはnullを許容しない。
        //nullの場合はNullPointerExceptionを投げる。
        if (end == null || start == null || other.end == null || other.start == null) {
            throw new NullPointerException("Line does not accept null as Point.");
        }
        
        if (end.equals(other.end)) {
            if (start.equals(other.start)) {
                return true;
            }
        } else if (end.equals(other.start)) {
            if (start.equals(other.end)) {
                return true;
            }
        }
        
        return false;
    }
    
    
    public static NavigableSet<Point> convertLineToPoint(Line line) {
        NavigableSet<Point> points = new TreeSet<Point>(Point.comparator);
        points.add(new Point(line.getStart().getX(), line.getStart().getY()));
        points.add(new Point(line.getEnd().getX(), line.getEnd().getY()));
        return points;
    }
    
    
    public static NavigableSet<Point> convertLinesToPoints(List<Line> lines) {
        NavigableSet<Point> points = new TreeSet<Point>(Point.comparator);
        for (Line line : lines) {
            Set<Point> set = convertLineToPoint(line);
            points.addAll(set);
        }
        
        return points;
    }
    
    
    public boolean containsPoint(Point point) {
        if (ErrorRangeUtil.isDiff(this.getDistance(point), 0)) {
            return false;
        }
        
        if ((Math.abs(this.end.x - this.start.x)) > ErrorRangeUtil.LINE_EFFECTIVE_XY_RANGE) {
            double px = point.x;
            double minx = Math.min(this.end.x, this.start.x);
            double maxx = Math.max(this.end.x, this.start.x);
            if (ErrorRangeUtil.isGE(minx, px) || ErrorRangeUtil.isGE(px, maxx)) {
            	return false;
            }
        }
        
        if ((Math.abs(this.end.y - this.start.y)) > ErrorRangeUtil.LINE_EFFECTIVE_XY_RANGE) {
            double py = point.y;
            double miny = Math.min(this.end.y, this.start.y);
            double maxy = Math.max(this.end.y, this.start.y);
            if (ErrorRangeUtil.isGE(miny, py) || ErrorRangeUtil.isGE(py, maxy)) {
            	return false;
            }

        }

        return true;
    }
  
}
