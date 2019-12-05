package javafx.example;

import java.util.ArrayList;
//import java.util.Vector;

public class Portion implements Comparable<Portion>{

	private String portionString;
	private String originalText;
	private int portionNumber;// portion identifier\
	public static int sizeOfNgram;
	public static int numberOfNgrams;// number of ngrams in portion vector
	public static int numberOfPortions=0;
	private ArrayList<String> ngramsArray=new ArrayList<String>();// array of portion ngrams
	private double[] rankedVector;// portion rank vector
	private double ZV;
	private double silhouetteValue;// silhouette value for the portion
	
	
	public Portion(String str,int index, int size)
	{
		portionNumber=index;
		portionString=str;
		sizeOfNgram=size;
		StringtoNgrams();
	}
	// converting the portion string to ngrams
	private void StringtoNgrams()
	{
		int i;
		for(i=0;i<portionString.length()-sizeOfNgram;i++)
			ngramsArray.add(portionString.substring(i,i+sizeOfNgram));
		numberOfNgrams=ngramsArray.size();
		rankedVector= new double[numberOfNgrams];
	}
	//calculate rank vector to the portion
	public void CalculateRankVector(ArrayList<String> arr)
	{
		double counter;
		int i=0;
		for(String constNgram: arr)
		{
			counter=0;
			for(String portionNgram: ngramsArray)
			{
				if(constNgram.equals(portionNgram))
					counter++;
			}
			rankedVector[i]=counter;
			i++;
		}
	}
	public void setOriginalText(String text)
	{
		originalText=text;
	}
	public String getOriginalText()
	{
		return originalText;
	}
	
	public String toString()
	{
		return String.format("portion number" + portionNumber + "/n" + portionString);
	}
	
	public ArrayList<String> getNgramsArray()
	{
		return ngramsArray;
	}
	
	public String getPortionString()
	{
		return portionString;
	}
	
	public int getPortionNumber()
	{
		return portionNumber;
	}
	
	public double[] getRankedVector()
	{
		return rankedVector;
	}
	public double getZV()
	{
		return ZV;
	}
	public void setZV(double zv)
	{
		ZV=zv;
	}

	public double getSilhouetteValue() 
	{
		return silhouetteValue;
	}

	public void setSilhouetteValue(double silhouetteValue)
	{
		this.silhouetteValue = silhouetteValue;
	}

	public int compareTo(Portion o)
	{
		
		return 0;
	}
}
