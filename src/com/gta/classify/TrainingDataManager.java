package com.gta.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;


import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TrainingDataManager 
{
	private String[] trainClassifications;
	private File trainTextDir;
	private float[][] classifyProbability;
	private float[] priProbability;
	private List<String> termsALL;
	private final float M = 0F;
	
	public TrainingDataManager(String s)
	{
		trainTextDir = new File(s);
		if (!trainTextDir.isDirectory()) 
		{
			throw new IllegalArgumentException("has no corpus");
		}
		this.trainClassifications = trainTextDir.list();
		
		getAllTerms();
	}
	
	
	public String[] getTrainClassifications() 
	{
		return this.trainClassifications;
	}
	
/**
 * 	
 * @param classification
 * @return the name of the text in the directory
 */
	public String[] getFilesPath(String classification) 
	{
		File classDir = new File(this.trainTextDir.getPath() + File.separator + classification);
		String[] ret = classDir.list();
		int len = ret.length;
		for (int i = 0; i < len; i++) 
		{
			ret[i] = this.trainTextDir.getPath() + File.separator + classification + File.separator + ret[i];  
		}
		return ret;
	}
	
/**
 * 	
 * @param filePath
 * @return obtain the content of the text
 * @throws FileNotFoundException
 * @throws IOException
 */
	public List<String> getText(String filePath) throws FileNotFoundException, IOException 
	{
		InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader br = new BufferedReader(isReader);
		String s;
		StringBuilder sb = new StringBuilder();
		while ((s = br.readLine()) != null) 
		{
			sb.append(s);
		}
		isReader.close();
		br.close();
		String text = sb.toString();
		
		List<String> corpus = new ArrayList<String>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("", text);
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while (stream.incrementToken()) 
		{
			corpus.add(cta.toString());
		}
		analyzer.close();
		return corpus;
	}

	
/**
 * 	
 * @return the sum of the file trained
 */
	public int getTrainFileCount()
	{
		int ret = 0;
		for (int i = 0; i < this.trainClassifications.length; i++) 
		{
			ret += getTrainFileCountOfClassification(trainClassifications[i]);
		}
		return ret;
	}

	
/**
 * 	
 * @param classification
 * @return the sum of the text in the classification
 */
	public int getTrainFileCountOfClassification(String classification) 
	{
		File classDir = new File(trainTextDir.getPath() + File.separator + classification);
		return classDir.list().length;
	}


/**
 * 
 * @param classification
 * @param key
 * @return the sum of text contain that key
 */
	public int getKeyCountOfClassification(String classification, String key) 
	{
		int ret = 0;
		try {
			String[] filePath = getFilesPath(classification);
			for (int j = 0; j < filePath.length; j ++) 
			{
				List<String> text = getText(filePath[j]);
				if (text.contains(key)) 
				{
					ret ++;
				}
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
		}
		return ret;
	}
	

	public void getAllTerms()
	{
		termsALL = new ArrayList<String>();
	    for (String classify : trainClassifications)
	    {
	    	try {
	    		String[] filePath = getFilesPath(classify);
	    		for (int j = 0; j < filePath.length; j++) 
	    		{
	    			List<String> text = getText(filePath[j]);
	    			termsALL = mergeText(termsALL, text);
	    		}
	    		
	    	} catch (FileNotFoundException e) {
				Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, e);
			}
	    }
	}
	
	
	public List<String> mergeText(List<String> list, List<String> sub)
	{
		for (String s : sub)
		{
			if (!isContain(s, list))
			{
				list.add(s);
			}
		}
		return list;
	}
	
	
	public boolean isContain(String key, List<String> list)
	{
		boolean ret = false;
		if (list.size() == 0)
		{
			return ret;
		}
		
		for (String s : list)
		{
			if (key.equals(s)) 
			{
				ret = true;
			}
		}
		return ret;
	}
	
	
	public void setProbability()
	{
		int len = trainClassifications.length;
		float trainFileCount = (float)getTrainFileCount();
		priProbability = new float[len];
		float priProbabilityValue = 0F;
		for (int i = 0; i < len; i++)
		{
			String classify = trainClassifications[i];
			float fileCountOfClassification = (float)getTrainFileCountOfClassification(classify);
			priProbabilityValue = fileCountOfClassification / trainFileCount;
			priProbability[i] = priProbabilityValue;
		}
	}
	
	
	public float getProbability(int index)
	{
		return priProbability[index];
	}
	
	
	public void setConditionalProbability()
	{
		int len = termsALL.size();
		int row = trainClassifications.length;
		classifyProbability = new float[row][len];
		for (int i = 0; i < row; i++)
		{
			String classify = trainClassifications[i]; 
			System.out.println(classify);
			for (int j = 0; j < len; j++)
			{
				float keyCountOfClassification = getKeyCountOfClassification(classify, termsALL.get(j));
				float fileCountOfClassification = (float)getTrainFileCountOfClassification(classify);
				classifyProbability[i][j] = (keyCountOfClassification + 1) / (fileCountOfClassification + M);
			}
		}
	}
	
		
	public float getConditionalProbability(int index, String key)
	{
		int col = 0;
		if (isContain(key, termsALL))
		{
			col = termsALL.indexOf(key);
			return classifyProbability[index][col];
		}
		else
		{
			String classify = trainClassifications[index];
			return (float)(col + 1) / ((float)getTrainFileCountOfClassification(classify) + M);   // smoothing
		}
	}
	
}
