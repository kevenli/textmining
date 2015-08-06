package com.gta.affective;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

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
			BufferedReader br = Files.newBufferedReader(file.toPath(), Charset.forName("GBK"));
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
