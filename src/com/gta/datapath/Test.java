package com.gta.datapath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.gta.simhash.SimHash;
import com.gta.affective.Segment;
import com.gta.namedentity.Corpus;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Corpus corpus = new Corpus();
		Test test = new Test();
		String str = test.getFileContent("text1.txt");
		SimHash hash = new SimHash(str, 64, 8);
//		long startTime = System.currentTimeMillis();
		Segment segment = new Segment();
		new DataPath(segment, corpus);
/*	
		FDSAPI api = new FDSAPI();
		api.getElement(api.getThread(101678));
		String title = api.getTitle();
		String content = api.getContent();
		System.out.println(title);
		System.out.println(content);
*/		
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
