package javafx.example;

import java.util.ArrayList;

public class ConstatNgrams {

	public ArrayList<String> ngramsArray;// arraylist of most common words in the Old testament
	
	public ConstatNgrams (ArrayList<String> arr)
	{
		ngramsArray=new ArrayList<String>();
		ngramsArray.addAll(arr);
	}
	public String toString()
	{
		String str2 = null;
		for(String str:ngramsArray)
			str2+=str+' ';
		System.out.println(str2);
		return str2;
	}
}
