package com.gta.namedentity;
/*
import java.io.IOException;

import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
*/
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] s = new String[3];
		s[0] = "仇和曾赞成反腐：只要按程序办就不会出落马";
		s[1] = "江西高考替考事件9人被抓 考生未进行身份识别";
		s[2] = "“东方之星”船舱内景曝光：航行时钟定格九点半";
/*
		String str1 = "同志们，先生们，大家好，中国共产党，华北电管局，仇和医生，3年时光不算长，高级工程师";
		IKAnalyzer analyzer = new IKAnalyzer(true);
		try {
			TokenStream stream = analyzer.tokenStream("", str1);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				System.out.println(cta.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
*/
		Corpus corpus = new Corpus();
		corpus.getResult(s[0]);
		corpus.getResult(s[1]);
		corpus.getResult(s[2]);
	}

}
