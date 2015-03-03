package org.tiqwab.count.rect.image;

public class LineGroup extends Group<Line>{
	private Condition condition;
	private Line product = null;
	
	
	public LineGroup(Line leader, Condition condition) {
		super(leader);
		this.condition = condition;
	}

	
	@Override
	public boolean similarTo(Line candidate) {
		double theta = leader.getAngle(candidate);
		return (Math.sin(theta) < Math.sin(condition.angle) && isOverlapped(candidate));
	}
	
	
	private double calcDistance(Line candidate) {
		double distance = 0;		
		distance = Math.min(leader.getDistance(candidate.start), leader.getDistance(candidate.end));		
		return distance;
	}
	
	
	private boolean isOverlapped(Line candidate) {
		boolean b = false;
		b = (leader.containsPoint(candidate.start) || leader.containsPoint(candidate.end));
		if (!b) {
			b = (candidate.containsPoint(leader.start) || candidate.containsPoint(leader.end));
		}
		return b;
	}
	
	
	@Override
	public void merge(){
		Point p1 = leader.start;
		Point p2 = leader.end;
		
		for (Line line : list){
			double distance = Point.getDistance(p1, p2);
			double distanceA = Point.getDistance(p1, line.start);
			double distanceB = Point.getDistance(line.start, p2);
			if (distanceA > distanceB){
				if (distanceA > distance){
					p2 = line.start;
				}
			} else {
				if (distanceB > distance){
					p1 = line.start;
				}
			}
			
			distance = Point.getDistance(p1, p2);
			distanceA = Point.getDistance(p1, line.end);
			distanceB = Point.getDistance(line.end, p2);
			if (distanceA > distanceB){
				if (distanceA > distance){
					p2 = line.end;
				}
			} else {
				if (distanceB > distance){
					p1 = line.end;
				}
			}
		}
		
		product = new Line(p1.clone(), p2.clone());
	}
	
	
	//Should call after merge()
	@Override
	public Line getProduct(){
		assert (product != null);
		return product;
	}
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("leader: " + leader.toString() + "\n");
		sb.append("candidate: ");
		for (Line line : list){
			sb.append(line.toString()).append(",");
		}		
		return sb.toString();
	}
}
