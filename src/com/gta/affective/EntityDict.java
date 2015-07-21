package com.gta.affective;

public class EntityDict {
	private int uid;
	private String productId;
	private String productName;
	
	public EntityDict(int uid, String productId, String productName) {
		this.uid = uid;
		this.productId = productId;
		this.productName = productName;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
