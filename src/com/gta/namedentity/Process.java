package com.gta.namedentity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

public class Process {
	
	public Process() {
		File file = new File("199801.txt");
		File wf = new File("199803.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			BufferedWriter bw = new BufferedWriter(new FileWriter(wf));
			String s = null;
			int len = 0;
			int i = 0;
			List<Character> list = new ArrayList<Character>();
			while ((s = br.readLine()) != null) {
				len = s.length();
				for (i = 0; i <len; i++) {
					char c = s.charAt(i);
					if (c == ']') {
						list.add(new Character(c));
						list.add(new Character('/'));
					}
					else {
						list.add(new Character(c));
					}
				}
				char [] array = new char[list.size()];
				
				for (int j = 0; j < list.size(); j++) {
					array[j] = list.get(j);
				}
				String t = new String(array);
				bw.write(t);
				bw.newLine();
				bw.flush();
				list.clear();
			}
			br.close();
			bw.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
