package com.gta.classify;

public class ClassifyResult {
	private double probability;
	private String classification;
	
	public ClassifyResult() 
	{
		this.probability = 0;
		this.classification = null;
	}
	
		
	public double getProbability() {
		return probability;
	}
	
		
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
		
	public String getClassification() {
		return classification;
	}
	
		
	public void setClassification(String classification) {
		this.classification = classification;
	}
}
