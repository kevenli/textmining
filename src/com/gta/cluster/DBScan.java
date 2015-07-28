package com.gta.cluster;

import java.util.List;
import java.util.ArrayList;

import com.gta.cosine.TextCosine;
import com.gta.cosine.ElementDict;

public class DBScan {
	private double  eps;
	private int     minPts;
	private TextCosine cosine;
	
	
	public DBScan()
	{
		this.eps = 0.20;
		this.minPts = 3;
		this.cosine = new TextCosine();
	}
	
	
	public DBScan(double eps, int minPts) 
	{
		this.eps = eps;
		this.minPts = minPts;
		this.cosine = new TextCosine();
	}
	
	
	public double getEps()
	{
		return eps;
	}
	
	
	public int getMinPts()
	{
		return minPts;
	}
	
		
	public List<DataNode> getNeighbors(DataNode p, List<DataNode> nodes)
	{
		List<DataNode> neighbors = new ArrayList<DataNode>();
		List<ElementDict> vec1 = p.getAllElements();
		List<ElementDict> vec2 = null;
		List<String> mergeList = null;
		List<Integer> list1 = null;
		List<Integer> list2 = null;
		double countDistance = 0;
		for (DataNode node : nodes)
		{
			vec2 = node.getAllElements();
			mergeList = cosine.mergeTerms(vec1, vec2);
			list1 = cosine.assignWeight(mergeList, vec1);
			list2 = cosine.assignWeight(mergeList, vec2);
			countDistance = cosine.countCosSimilariry(list1, list2);
			if (countDistance >= eps)
			{
				neighbors.add(node);
			}
		}
		return neighbors;
	}
	
	
	public int cluster(List<DataNode> nodes)
	{
		int category = 1;
		for (DataNode node : nodes)
		{
			if (!node.getVisitLabel())
			{
				node.setVisitLabel(true);
				List<DataNode> neighbors = getNeighbors(node, nodes);
				if (neighbors.size() < minPts)
				{
					node.setCatagory(-1);
				}
				else
				{
					expandCluster(node, neighbors, category, nodes);
				}
			}
			category ++;
		}
		return category;
	}
	
	
	public void expandCluster(DataNode p, List<DataNode> neighbors, int category, List<DataNode> nodes)
	{
		p.setCatagory(category);
		for (DataNode node : neighbors) 
		{
			if (!node.getVisitLabel()) 
			{
				node.setVisitLabel(true);
				List<DataNode> newNeighbors = getNeighbors(node, nodes);
				if (newNeighbors.size() >= minPts)
				{
					neighbors.addAll(newNeighbors);
				}
			}
			
			if (node.getCategory() < 0) 
			{
				node.setCatagory(category);
			}
		}
	}
	
	
	
	

}
