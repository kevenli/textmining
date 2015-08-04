package com.gta.simhash;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
		Test content = new Test();
		String s1 = content.getFileData("text1.txt");
		String s2 = content.getFileData("text2.txt");

		String s3 = "北京，上海，天津，重庆，大连，长春，哈尔滨，南京，杭州，宁波，厦门，济南，青岛，武汉，广州，深圳，成都，西安，太原，长沙，桂林，南宁，郑州，洛阳，无锡，乌鲁木齐，兰州，银川，合肥，芜湖";
		String s4 = "北京，上海，天津，重庆，大连，长春，哈尔滨，南京，杭州，宁波，厦门，济南，青岛，武汉，广州，深圳，成都，西安，太原，长沙，桂林，南宁，郑州，洛阳";
		SimHash hash1 = new SimHash(s1, 64, 8);
		SimHash hash2 = new SimHash(s2, 64, 8);
		hash1.getResult(hash2);
	}
	
	
	public String getFileData(String fileName)
	{
		try {
			File file = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			StringBuilder sb = new StringBuilder();
			while ((s= br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
