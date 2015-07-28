package com.gta.cluster;

import java.util.List;
import java.util.ArrayList;

import com.gta.cosine.TextCosine;
import com.gta.cosine.ElementDict;

public class DBScan {
	private double         eps;
	private int            minPts;
	private TextCosine     cosine;
	private int            threshold;
	private List<DataNode> dataNodes;
	private int            delta;
	
	public DBScan()
	{
		this.eps = 0.20;
		this.minPts = 3;
		this.threshold = 10000;
		this.cosine = new TextCosine();
		this.delta = 0;
		dataNodes = new ArrayList<DataNode>();
	}
	
	
	public DBScan(double eps, int minPts, int threshold) 
	{
		this.eps = eps;
		this.minPts = minPts;
		this.threshold = threshold;
		this.cosine = new TextCosine();
		this.delta = 0;
		dataNodes = new ArrayList<DataNode>();
	}
	
	
	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}
	
	
	public int getThreshold()
	{
		return threshold;
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
			System.out.println(countDistance);
			if (countDistance >= eps)
			{
				neighbors.add(node);
			}
		}
		return neighbors;
	}
	
	
	public List<DataNode> cluster(List<DataNode> nodes)
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
					nodes = expandCluster(node, neighbors, category, nodes);
				}
			}
			category ++;
		}
		
		return nodes;
	}
	
	
	public List<DataNode> expandCluster(DataNode p, List<DataNode> neighbors, int category, List<DataNode> nodes)
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
			
			if (node.getCategory() <= 0) 
			{
				node.setCatagory(category);
			}
		}
		
		return nodes;
	}
	
	
	public void showCluster(List<DataNode> nodes)
	{
		for (DataNode node : nodes) 
		{
			for (ElementDict e: node.getAllElements())
			{
				System.out.print(e.getTerm() + "  ");
			}
			System.out.println(node.getCategory());
		}
	}
	
	
	public void addDataNode(String s) 
	{   
		List<ElementDict> ed = cosine.tokenizer(s);
		DataNode dataNode = new DataNode(ed);
		dataNodes.add(dataNode);
		delta ++;
	}
	
	
	public void analysis() 
	{
		if (delta == threshold)
		{
			dataNodes = cluster(dataNodes);
			showCluster(dataNodes);
			delta = 0;
		}
	}
	

}
