package org.tiqwab.count.rect.image;

import java.util.ArrayList;
import java.util.List;

public abstract class Group <T>{
	protected T leader;
	protected List<T> list;
	
	public Group(T leader){
		this.leader = leader;
		list = new ArrayList<T>();
	}
	
	
	public void add(T candidate){
		list.add(candidate);
	}
	
	
	public abstract boolean similarTo(T candidate);
	
	
	public abstract void merge();
	
	
	public abstract T getProduct();
}
