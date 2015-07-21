package com.gta.classify;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class BayesClassifier {
	private TrainingDataManager dm;
	private double zoomFactor = 10.0F;
	
	public BayesClassifier()
	{
		dm = new TrainingDataManager("classify");
        dm.setProbability();
        dm.setConditionalProbability();
	}
	
	
	float calcProbability(List<String> terms, int index) 
	{
		float ret = 1.0F;
		for (String key : terms) 
		{
			ret *= dm.getConditionalProbability(index, key) * zoomFactor;
		}
		ret *= dm.getProbability(index);
		return ret;
	}
	
	
	public String classify(String text)
	{
		List<String> terms = new ArrayList<String>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		try 
		{
			TokenStream stream = analyzer.tokenStream("", text);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while (stream.incrementToken()) 
			{
				terms.add(cta.toString());
			}
			analyzer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String[] classes = dm.getTrainClassifications();
		float probability = 0.0F;
		List<ClassifyResult> cr = new ArrayList<ClassifyResult>();
		for (int i = 0; i < classes.length; i++) 
		{
			String C = classes[i];
			probability = calcProbability(terms, i);
			ClassifyResult crs = new ClassifyResult();
			crs.setClassification(C);
			crs.setProbability(probability);
			cr.add(crs);
		}
		
		Collections.sort(cr, new Comparator<ClassifyResult>()
		{
			public int compare(ClassifyResult ret1, ClassifyResult ret2)
			{
				double ret = ret1.getProbability() - ret2.getProbability();
				if (ret < 0)
				{
					return 1;
				}
				else 
				{
					return -1;
				}
			}
		});
		return cr.get(0).getClassification();
	}
	
}
