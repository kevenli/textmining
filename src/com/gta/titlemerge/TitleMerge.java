package com.gta.titlemerge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TitleMerge {
	private Map<String, String> map= null; 
	
	public TitleMerge() {
		map = new HashMap<String, String>();
		try {
			File file = new File("synonyms.dict");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) !=null) {
				String []synonymsEnum = s.split("¡ú");
				map.put(synonymsEnum[0], synonymsEnum[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<TitleDict> tokenizer(String str) {
		List<TitleDict> list = new ArrayList<TitleDict>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		try {
			TokenStream stream = analyzer.tokenStream("", str);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			int index = -1;
			while (stream.incrementToken()) {
				if ((index = isContain(cta.toString(), list)) >= 0) {
					list.get(index).setFreq(list.get(index).getFreq() + 1);
				}
				else {
					list.add(new TitleDict(cta.toString(), 1));
				}
			}
			analyzer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	
	public int isContain(String str, List<TitleDict> list) {
		for (TitleDict td : list) {
			if (td.getTerm().equals(str)) {
				return list.indexOf(td);
			} else if (map.get(td.getTerm())!= null && map.get(td.getTerm()).equals(str)) {
				return list.indexOf(td);
			}
		}
		return -1;
	}
	
	
	public List<String> mergeTerms(List<TitleDict> list1, List<TitleDict> list2) {
		List<String> list = new ArrayList<String>();
		for (TitleDict td : list1)
			list.add(td.getTerm());
	
		for (TitleDict td2 : list2) {
			if (!list.contains(td2.getTerm())) {
				list.add(td2.getTerm());
			} else if (!list.contains(map.get(td2.getTerm()))) {
				list.add(td2.getTerm());
			}
		}
		return list;
	}
	
	
	public int anslysisTerms(List<TitleDict> list1, List<TitleDict> list2) {
		int len1 = list1.size();
		int len2 = list2.size();
		if (len2 >= len1 * 1.5) {
			List<TitleDict> newList = new ArrayList<TitleDict>();
			for (int i = 0; i + len1 <= len2; i++) {
				for (int j = 0; j < len1; j++) 
					newList.add(list2.get(i+j));
				
				newList = adjustList(newList, list2, len2, len1, i);
				if (getResult(analysis(list1, newList))) 
					return 1;
				else 
					newList.clear();
			}
		} else if (len1 >= len2 * 1.5) {
			List<TitleDict> newList = new ArrayList<TitleDict>();
			for (int i = 0; i + len2 <= len1; i++) {
				for (int j = 0; j < len2; j++)
					newList.add(list1.get(i+j));
				
				newList = adjustList(newList, list1, len1, len2, i);
				if (getResult(analysis(newList, list2))) 
					return 1;
				else 
					newList.clear();
			}
		} else {
			if (getEasyResult(analysis(list1, list2))) 
				return 1;
		}
		return 0;
	}
	
	
	public List<TitleDict> adjustList(List<TitleDict> newList, List<TitleDict> list, int lenBig, int lenSmall, int index) {
		int gap = lenBig -lenSmall;
		int size = (gap/2 > 2) ? 2: gap/2;
		if (index < gap/2) {
			for (int i = 0; i < size; i++) {
				newList.add(list.get(lenSmall+index+i));
			}
		} else {
			for (int i = 0; i > size; i++) {
				newList.add(list.get(lenBig-index-i));
			}
		}
		return newList;
	}
	
	
	public double analysis(List<TitleDict> list1, List<TitleDict> list2) {
		List<String> list = mergeTerms(list1, list2);
		List<Integer> weightList1 = assignWeight(list, list1);
		List<Integer> weightList2 = assignWeight(list, list2);
		return countCosSimilariry(weightList1, weightList2);
	}
	
	
	public List<Integer> assignWeight(List<String> list, List<TitleDict> list1) {
		List<Integer> vecList = new ArrayList<Integer>(list.size());
		boolean isEqual = false;
		for (String str : list) {
			for (TitleDict td : list1) {
				if (td.getTerm().equals(str)) {
					isEqual = true;
					vecList.add(new Integer(td.getFreq()));
				} else if (map.get(td.getTerm())!= null && map.get(td.getTerm()).equals(str)) {
					isEqual = true;
					vecList.add(new Integer(td.getFreq()));
				}
			}
			
			if (!isEqual) {
				vecList.add(new Integer(0));
			}
			isEqual = false;
		}
		return vecList;
	}
	
	
	public double countCosSimilariry(List<Integer> list1, List<Integer> list2) {
		double countScores = 0;
		int element = 0;
		int denominator1 = 0;
		int denominator2 = 0;
		int index = -1;
		for (Integer it : list1) {
			index ++;
			int left = it.intValue();
			int right = list2.get(index).intValue();
			element += left * right;
			denominator1 += left * left;
			denominator2 += right * right;
		}
		try {
			countScores = (double)element / Math.sqrt(denominator1 * denominator2);
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
		return countScores;
	}
	
	
	public boolean getResult(double scores) {
		if (scores >= 0.75)
			return true;
		else 
			return false;
	}
	
	
	public boolean getEasyResult(double scores) {
		if (scores >= 0.75)
			return true;
		else 
			return false;
	}

}
