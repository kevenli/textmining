package com.gta.affective;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Segment sg = new Segment();
		Test test = new Test();
		String text = test.getFileContent("text1.txt");
		System.out.println(sg.analysis(text));
	}
	
	
	public String getFileContent(String fileName)
	{
		String text = null;
		try {
			File file= new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			StringBuilder sb = new StringBuilder();
			while ((s = br.readLine()) != null) 
			{
				sb.append(s);
			}
			text = sb.toString();
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
}
