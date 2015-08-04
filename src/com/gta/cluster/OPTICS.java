package com.gta.cluster;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.PriorityQueue;

import com.gta.cosine.ElementDict;
import com.gta.cosine.TextCosine;

public class OPTICS {
	private double            eps;
	private int               minPts;
	private TextCosine        cosine;
	private List<DataPoint>   dataPoints;
	private List<DataPoint>   orderList;
	
	public OPTICS(double eps, int minPts)
	{
		this.eps = eps;
		this.minPts = minPts;
		this.cosine = new TextCosine();
		this.dataPoints = new ArrayList<DataPoint>();
		this.orderList = new ArrayList<DataPoint>();
	}
	
	
	public void addPoint(String s)
	{
		List<ElementDict> ed = cosine.tokenizer(s);
		dataPoints.add(new DataPoint(ed));
	}
	
	
	public double coreDistance(List<DataPoint> neighbors)
	{
		double ret = -1;
		if (neighbors.size() >= minPts)
		{
			Collections.sort(neighbors, new Comparator<DataPoint>() {
				        public int compare(DataPoint dp1, DataPoint dp2) {
				        	double cd = dp1.getInitDistance() - dp2.getInitDistance();
				        	if (cd < 0) {
				        		return 1;
				        	} else {
				        		return -1;
				        	}
				        }
					});
			
			ret = neighbors.get(minPts-1).getInitDistance();
		}
		return ret;
	}
	
	
	public double cosineDistance(DataPoint p, DataPoint q)
	{
		List<ElementDict> vec1 = p.getAllElements();
		List<ElementDict> vec2 = q.getAllElements();
		List<String> mergeList = cosine.mergeTerms(vec1, vec2);
		List<Integer> list1 = cosine.assignWeight(mergeList, vec1);
		List<Integer> list2 = cosine.assignWeight(mergeList, vec2);
		return cosine.countCosSimilariry(list1, list2);
	}
	

	public List<DataPoint> getNeighbors(DataPoint p, List<DataPoint> points)
	{
		List<DataPoint> neighbors = new ArrayList<DataPoint>();
		double countDistance = -1;
		for (DataPoint q : points)
		{
			countDistance = cosineDistance(p, q);
			if (countDistance >= eps)
			{
				q.setInitDistance(countDistance);
				neighbors.add(q);
			}
		}
		return neighbors;
	}
	
	
	public void cluster(List<DataPoint> points)
	{
		for (DataPoint point : points)
		{
			if (!point.getIsVisitLabel())
			{
				List<DataPoint> neighbors = getNeighbors(point, points);
				point.setIsVisitLabel(true);
				orderList.add(point);
				double cd = coreDistance(neighbors);
				if (cd != -1)
				{
					point.setCoreDistance(cd);
					Queue<DataPoint> seeds = new PriorityQueue<DataPoint>(new Comparator<DataPoint>() {
						    public int compare (DataPoint dp1, DataPoint dp2) {
						    	double rd = dp1.getReachableDistance() - dp2.getReachableDistance();
						    	if (rd < 0) {
						    		return 1;
						    	} else {
						    		return -1;
						    	}
						    }
						});
					
					update(point, neighbors, seeds, orderList);
					while (!seeds.isEmpty()) 
					{
						DataPoint q = seeds.poll();
						List<DataPoint> newNeighbors = getNeighbors(q, points);
						q.setIsVisitLabel(true);
						orderList.add(q);
						if (coreDistance(newNeighbors) != -1)
						{
							update(q, newNeighbors, seeds, orderList);
						}
					}
				}
			}
		}
	}
	
	
	public void update(DataPoint p, List<DataPoint> neighbors, Queue<DataPoint> seeds, List<DataPoint> seqList)
	{
		double coreDistance = coreDistance(neighbors);
		for (DataPoint point : neighbors)
		{
			double cosineDistance = cosineDistance(p, point);
			double reachableDistance = coreDistance > cosineDistance ? coreDistance : cosineDistance;
			if (!point.getIsVisitLabel())
			{
				if (point.getReachableDistance() == -1)
				{
					point.setReachableDistance(reachableDistance);
					seeds.add(point);
				}
				else
				{
					if (point.getReachableDistance() > reachableDistance)
					{
						if (seeds.remove(point)) 
						{
							point.setReachableDistance(reachableDistance);
							seeds.add(point);
						}
					}
				}
			}
			else 
			{
				if (point.getReachableDistance() == -1)
				{
					point.setReachableDistance(reachableDistance);
					if (seqList.remove(point))
					{
						seeds.add(point);
					}
				}
			}
		}
	}
	
	
	public void showCluster()
	{
		for (DataPoint point : orderList)
		{
			
			List<ElementDict> ed = point.getAllElements();
			for (ElementDict e : ed)
			{
				System.out.print(e.getTerm() + "  ");
			}
			System.out.println();
			System.out.println("core:  " + point.getCoreDistance());
			System.out.println("reach: " + point.getReachableDistance());
			System.out.println("***************************************");
        }
	}
	
	
	public void analysis()
	{
		cluster(dataPoints);
		showCluster();
	}
	
	
	public int IndexOfList(DataPoint o, Queue<DataPoint> points)
	{
		int index = 0;
		for (DataPoint p : points)
		{
			if (o.equals(p))
			{
				break;
			}
			index ++;
		}
		return index;
	}

}
