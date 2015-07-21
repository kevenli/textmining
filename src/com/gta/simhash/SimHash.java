package com.gta.simhash;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class SimHash {
	private String tokens;
    private int hashBits = 64;
    private int distance = 5;
	
	public SimHash(String tokens)
	{
		this.tokens = tokens;
	}
	
	
	public SimHash(String tokens, int hashBits, int distance)
	{
		this.tokens = tokens;
		this.hashBits = hashBits;
		this.distance = distance;
	}
	
	
	public List<TermDict> tokenizer()
	{
		List<TermDict> terms = new ArrayList<TermDict>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		try {
			TokenStream stream = analyzer.tokenStream("", this.tokens);
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			int index = -1;
			while (stream.incrementToken()) 
			{
				if ((index = isContain(cta.toString(), terms)) >= 0)
				{
					terms.get(index).setFreq(terms.get(index).getFreq()+1);
				}
				else 
				{
					terms.add(new TermDict(cta.toString(), 1));
				}
			}
			analyzer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return terms;
	}
	
	
	public int isContain(String str, List<TermDict> terms)
	{
		for (TermDict td : terms)
		{
			if (str.equals(td.getTerm()))
			{
				return terms.indexOf(td);
			}
		}
		return -1;
	}
	
	
	public BigInteger simHash(List<TermDict> terms)
	{
		int []v = new int[hashBits];
		for (TermDict td : terms)
		{
			String str = td.getTerm();
			int weight = td.getFreq();
			BigInteger bt = shiftHash(str);
			for (int i = 0; i < hashBits; i++)
			{
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				if ( bt.and(bitmask).signum() != 0)
				{
					v[i] += weight;
				}
				else
				{
					v[i] -= weight;
				}
			}
		}
		
		BigInteger fingerPrint = new BigInteger("0");
		for (int i = 0; i < hashBits; i++)
		{
			if (v[i] >= 0)
			{
				fingerPrint = fingerPrint.add(new BigInteger("1").shiftLeft(i));   // update the correct fingerPrint
			}
		}
		return fingerPrint;
	}
	
	
	public BigInteger shiftHash(String str)
	{
		if (str == null || str.length() == 0)
		{
			return new BigInteger("0");
		}
		else 
		{
			char[] sourceArray = str.toCharArray();
			BigInteger x = BigInteger.valueOf((long) sourceArray[0] << 7);
			BigInteger m = new BigInteger("131313");
			for (char item : sourceArray)
			{
				x = x.multiply(m).add(BigInteger.valueOf((long)item));
			}
			BigInteger mask = new BigInteger("2").pow(hashBits).subtract(new BigInteger("1"));
			boolean flag = true;
			for (char item : sourceArray)
			{
				if (flag)
				{
					BigInteger tmp = BigInteger.valueOf((long)item << 3);
					x = x.multiply(m).xor(tmp).and(mask);
				}
				else
				{
					BigInteger tmp = BigInteger.valueOf((long)item >> 3);
					x = x.multiply(m).xor(tmp).and(mask);
				}
				flag = !flag;
			}
			
			if (x.equals(new BigInteger("-1")))
			{
				x = new BigInteger("-2");
			}
			return x;
		}
	}
	
	
	public BigInteger getSimHash()
	{
		return simHash(tokenizer());
	}
	
	
	public int getHammingDistance(SimHash hashData)
	{
		BigInteger m = new BigInteger("1").shiftLeft(hashBits).subtract(new BigInteger("1"));
		System.out.println(getFingerPrint(getSimHash().toString(2)));
		System.out.println(getFingerPrint(hashData.getSimHash().toString(2)));
		BigInteger x = getSimHash().xor(hashData.getSimHash()).and(m);
		int tot = 0;
		while (x.signum() != 0)
		{
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		System.out.println(tot);
		return tot;
	}
	
	public String getFingerPrint(String str)
	{
		int len = str.length();
		for (int i = 0; i < hashBits; i++)
		{
			if (i >= len)
			{
				str = "0" + str;
			}
		}
		return str;
	}
	
	
	public void getResult(SimHash hashData)
	{
		if (getHammingDistance(hashData) <= distance)
		{
			System.out.println("match");
		}
		else
		{
			System.out.println("false");
		}
	}
	
}
