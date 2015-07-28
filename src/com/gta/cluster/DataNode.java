package com.gta.cluster;

import java.util.List;

import com.gta.cosine.ElementDict;

public class DataNode {
	private List<ElementDict> terms;
	private boolean isVisited;
	private int category;
	
	public DataNode(List<ElementDict> terms)
	{
		this.terms = terms;
		this.isVisited = false;
		this.category = 0;
	}
	
	
	public void setVisitLabel(boolean isVisited)
	{
		this.isVisited = isVisited;
	}
	
	
	public void setCatagory(int category)
	{
		this.category = category;
	}
	
	
	public boolean getVisitLabel()
	{
		return isVisited;
	}
	
	
	public int getCategory()
	{
		return category;
	}
	
	
	public List<ElementDict> getAllElements()
	{
		return terms;
	}
	
	
	public ElementDict getElement(int index)
	{
		if (index < terms.size()) 
		{
			return terms.get(index);
		}
		
		return null;
	}

}
