package com.gta.namedentity;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Corpus {
	private List<String> phraseEnum;
	private List<String> charEnum;
	private List<String> corPhraseEnum;
	private List<String> corCharEnum;
	private double[] PI;
	private double[][] A;
	private double[][] B;
	private int eof = 0;
	private final String[] state = {"a", "an", "ad", "Ag", "b", "c", "d", "Dg", "f", 
	          						"i", "j", "l", "m", "mq", "n", "Ng", "nk", "nr",
	          						"ns", "nt", "nx", "nz", "p", "q", "r", "s", "t", 
	          						"Tg", "u", "v", "vd", "Vg", "vn", "w", "y", "z"};
	private int[] stateCount;
	private List<String> obSeq;
	
	public Corpus() {
		phraseEnum = new ArrayList<String>();
		charEnum = new ArrayList<String>();
		corPhraseEnum = new ArrayList<String>();
		corCharEnum = new ArrayList<String>();
		List<String> phraseList = new ArrayList<String>();
		List<String> charList = new ArrayList<String>();
		try {
			File file = new File("199803.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
			//  String[] text = s.split("\\s{1,}");
				String[] phrase = s.split("(/[a-zA-Z]*\\s{0,})");
				String[] characters = s.split("[0-9|-]*/|\\s{1,}[^a-zA-Z]*");
				phraseList = preProcessString(phrase, 1);
				charList = preProcessString(characters, 2);
				preProcess(charList, phraseList);
				corpusProcess();
			}
			br.close();
			processCharEnum();
			getStateMatrix();
			getStateTransform();
			getEmitter();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	
	public void getResult(String s)
	{
		Viterbi(s);
	}
	
	
	public void corpusProcess() {
		corPhraseEnum.addAll(phraseEnum);
		corCharEnum.addAll(charEnum);
		phraseEnum.clear();
		charEnum.clear();
	}
	
	/**
	 * 语料库词性预处理
	 * @param charEnum
	 * @param start
	 * @return
	 */
	public List<String> preProcessString(String[] charEnum, int start) {
		List<String> list = new ArrayList<String>();
		int len = charEnum.length;
		for (int i = start; i < len; i++) {
			list.add(charEnum[i]);
		}
		list.add("QQQQ");  // EOF
		eof ++;
		return list;
	}
	
	/**
	 * 语料库字符串预处理
	 * @param list
	 * @param phrase
	 */
	public void preProcess(List<String> list, List<String> phrase) {
		if (list.size() > 0) 
		{
			int index = 0;
			int pre = 0;
			String prelement = "";
			for (String e : list) 
			{
				if (matchRules(prelement, e)) 
				{
					String enew = prelement + e;
					charEnum.set(charEnum.size()-1, enew);
					String phnew = phrase.get(pre) + phrase.get(index);
					phraseEnum.set(phraseEnum.size()-1, phnew);
				}
				else 
				{
					charEnum.add(e);
					phraseEnum.add(phrase.get(index));
				}
				prelement = e;
				pre = index;
				index ++;
			}
		}
	}
	
	/**
	 * 针对语料库中某些特殊词汇进行预处理
	 * @param pre
	 * @param adj
	 * @return
	 */
	public boolean matchRules(String pre, String adj) {
		if (pre.equals( "m") && adj.equals("q")) 
		{
			return true;
		} 
		else if (pre.equals("n") && adj.equals("k")) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 字符预处理
	 */
	public void processCharEnum() {
		int index = 0;
		for (String t : corCharEnum) {
			if (t.contains("]")) {
				corCharEnum.set(index, (t.substring(0, t.indexOf(']'))));
			}
			index ++;
		}
	}
	
	/**
	 * 获取状态初始化矩阵
	 */
	public void getStateMatrix() {
		PI = new double[state.length];
		stateCount = new int[state.length];
		for (int i = 0; i < state.length; i++) {
			for (String t : corCharEnum) {
				if (t.equals(state[i]))
					stateCount[i] ++;
			}
		}
		
		int size = corCharEnum.size() - eof/2;
		for (int i = 0; i < PI.length; i++) {
			PI[i] = (double)stateCount[i] / size; 
		}
	}
	
	/**
	 * 获取状态转移矩阵
	 */
	public void getStateTransform() {
		int[][]tran = new int[state.length][state.length];
		String pre = "";
		int pos = 0;
		for (int i = 0; i < state.length; i++) {
			for (String t : corCharEnum) {
				if ((pos = getPosition(t, state)) >= 0) {
					if (state[i].equals(pre)) {
						tran[i][pos] ++;
					}
				}
				pre = t;
			}
		}

		A = new double[state.length][state.length];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				A[i][j] = (double)tran[i][j] /stateCount[i];
			}
		}		
	}
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public int getPosition(String s1, String []s2) {
		int index = 0;
		for (String s : s2) {
			if (s.equals(s1)) {
				return index;
			}
			index ++;
		}
		return -1;
	}
	
	/**
	 * 获取语料集中的词语
	 * @return
	 */
	public List<String> getToken() {
		List<String> list = new ArrayList<String>();
		for (String t : corPhraseEnum) {
			if (!list.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}
	
	/**
	 * 获取发射矩阵
	 */
	public void getEmitter() {
		obSeq = getToken();
		int[][]matrix = new int[state.length][obSeq.size()];
		int pos = 0;
		for (int i = 0; i < state.length; i++) {
			int index = 0;
			for (String t : corCharEnum) {
				if (state[i].equals(t)) {
					pos = obSeq.indexOf(corPhraseEnum.get(index));
					matrix[i][pos] ++;
				}
				index ++;
			}
		}
		
		B = new double[state.length][obSeq.size()];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < obSeq.size(); j++) {
				B[i][j] = (double)matrix[i][j] / stateCount[i];
			}
		}
	}
	
	/**
	 * 获取观察者序列
	 * @param s 待处理字符串
	 * @return 观察者序列标注
	 */
	public int[] getObSeq(String s) {
		List<String> preString = new ArrayList<String>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		try {
			TokenStream stream = analyzer.tokenStream("", s);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) {
				preString.add(cta.toString());
			}
			analyzer.close();
			
			int size = preString.size();
			
			if (preString.size() == 0) {
				size += 1;
				int []newList = new int[size];
				newList[0] = 0;
				return newList;
			}
			int []list = new int[size];
			System.out.println(preString.size());
			int index = 0;
			for (String t : preString) {
				System.out.print(t);
				System.out.print("\t");
				for (String ob : obSeq) {
					if (ob.equals(t)) {
						list[index] = obSeq.indexOf(ob);
					} 
				}
				index ++;
			}
			System.out.print("\n");
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 词性标注译码
	 * @param s 待处理字符串
	 */
	public void Viterbi(String s) {
		int []list = getObSeq(s);
		double [][]result = new double[list.length][state.length];
		int [][]max = new int[list.length][state.length];
		double tmp;
		
		// Initialization 
		for (int i = 0; i < state.length; i++) {
			result[0][i] = PI[i] * B[i][list[0]];
		}
		
		// Iteration
		for (int i = 1; i < list.length; i++) {
        	for (int j = 0; j < state.length; j++) {
        		tmp = result[i-1][0] * A[0][j] * B[j][list[i]];
        		max[i][j] = 0;
        		for (int k = 1; k < state.length; k++) {
        			if (result[i-1][k] * A[k][j] * B[j][list[i]] > tmp) {
        				tmp = result[i-1][k] * A[k][j] * B[j][list[i]];
        				max[i][j] = k;
        			}
        			result[i][j] = tmp;
        		}
        	}
        }
		
		List<Integer> adjList = new ArrayList<Integer>();
		// Over 
		double max_v = result[list.length-1][0];
		int max_node = 0;
		for (int k = 1; k < state.length; k++) {
			if (result[list.length-1][k] > max_v) {
				max_v = result[list.length-1][k];
				max_node = k;
			}
		}
		adjList.add(max_node);
		
		// Back
		for (int i = list.length-1; i > 0; i--) {
			max_node = max[i][max_node];
			adjList.add(max_node);
		}
		
		// Adjust
		List<Integer> retList = new ArrayList<Integer>();
		for (int i = adjList.size()-1; i >= 0; i--) {
			retList.add(adjList.get(i));
		}
		
		// Result
		for (Integer i : retList) {
			System.out.print(state[i]);
			System.out.print("\t");
		}
		System.out.print("\n");
	}
}
