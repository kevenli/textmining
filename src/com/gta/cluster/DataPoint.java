package com.gta.cluster;

import java.util.List;

import com.gta.cosine.ElementDict;

public class DataPoint {
	private List<ElementDict> terms;
	private double initDistance;
	private double coreDistance;
	private double reachableDistance;
	private boolean isVisited;
	
	
	public DataPoint(List<ElementDict> terms) {
		this.terms = terms;
		this.initDistance = -1;
		this.coreDistance = -1;
		this.reachableDistance = -1;
		this.isVisited = false;
	}
	
	
	public double getCoreDistance() {
		return coreDistance;
	}


	public void setCoreDistance(double coreDistance) {
		this.coreDistance = coreDistance;
	}


	public double getReachableDistance() {
		return reachableDistance;
	}


	public void setReachableDistance(double reachableDistance) {
		this.reachableDistance = reachableDistance;
	}
	
	
	public boolean getIsVisitLabel() {
		return isVisited;
	}
	
	
	public void setIsVisitLabel(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	
	public double getInitDistance() {
		return initDistance;
	}


	public void setInitDistance(double initDistance) {
		this.initDistance = initDistance;
	}


	public List<ElementDict> getAllElements() {
		return terms;
	}
	
	
	public ElementDict getElement(int index) {
		return terms.get(index);
	}
	
	
	public boolean equals(DataPoint dp)
	{
		List<ElementDict> ed1 = getAllElements();
		List<ElementDict> ed2 = dp.getAllElements();
		int len = ed1.size();
		
		if (len != ed2.size())
		{
			return false;
		}
		
		for (int i = 0; i < len; i++)
		{
			if (!ed1.get(i).equals(ed2.get(i)))
			{
				return false;
			}
		}
		return true;
	}
	
}
