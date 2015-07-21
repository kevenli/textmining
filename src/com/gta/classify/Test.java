package com.gta.classify;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
        Test classify = new Test();
		String text = classify.getFileContent("ctest.txt");
		long startTime = System.currentTimeMillis();
		BayesClassifier classifier = new BayesClassifier();
		String result = classifier.classify(text);
		long endTime = System.currentTimeMillis();
		System.out.println(result);
		System.out.println(endTime - startTime + "ms");
		
		text = classify.getFileContent("ntest.txt");
		startTime = System.currentTimeMillis();
		result = classifier.classify(text);
		endTime = System.currentTimeMillis();
		System.out.println(result);
		System.out.println(endTime - startTime + "ms");
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
