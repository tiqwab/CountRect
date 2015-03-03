package org.tiqwab.count.rect.image;

import java.util.Comparator;
import java.util.Set;

public class Point implements Cloneable {

    double x;
    double y;
    public static Comparator<Point> comparator = new PointComparatorX();

    
    public Point() {
        
    }
    
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    
    public double getX() {
        return x;
    }

    
    public double getY() {
        return y;
    }
    
    
    public void setX(double x) {
    	this.x = x;
    }
    
    
    public void setY(double y) {
    	this.y = y;
    }

    
    public static void setComparator(Comparator<Point> comp) {
    	comparator = comp;
    }
    
    
    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));       
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
         
        return true;
    }
    
    
    @Override
    public Point clone() {
    	Point p;
		try {
			p = (Point)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
    	p.x = this.x;
    	p.y = this.y;
    	return p;
    }
    
    
	public static Point rotate(Point p, Point base, double theta) {
		double x = p.x;
		double y = -1 * p.y;
		double bx = base.x;
		double by = -1 * base.y;
		
		double rotatedX = (x - bx) * Math.cos(theta) - (y - by) * Math.sin(theta) + bx;
		double rotatedY = -1.0 * ( (x - bx) * Math.sin(theta) + (y - by) * Math.cos(theta) + by );
		return new Point(rotatedX, rotatedY);
	}
	
	
	public static double getDistance(Point p1, Point p2){
		return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
	}
    
    
    public static int[][] generateCoordinates(Set<Point> points) {
        int[][] result = new int[points.size()][2];
        int i = 0;
        for (Point p : points) {
            result[i][0] = (int) p.getX();
            result[i][1] = (int) p.getY();
            i++;
        }
        return result;
    }
    
}
