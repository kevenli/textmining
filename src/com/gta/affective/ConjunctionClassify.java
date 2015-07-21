package com.gta.affective;

public class ConjunctionClassify {
	private String prefix;    // 前一个连接词
	private String middle;    // 后一个连接词
	private int classify;     // 连词类型
	
	public ConjunctionClassify(String prefix, String middle, int classify) {
		this.prefix = prefix;
		this.middle = middle;
		this.classify = classify;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getMiddle() {
		return middle;
	}

	public int getClassify() {
		return classify;
	}
}
