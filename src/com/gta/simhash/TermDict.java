package com.gta.simhash;

public class TermDict {
	private String term;
	private int freq;
	
	public TermDict(String term, int freq) 
	{
		this.term = term;
		this.freq = freq;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	

}
