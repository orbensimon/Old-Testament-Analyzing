package javafx.example;

import java.util.*;

//import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.*;
//import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;
//import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;

import java.io.IOException;
import java.nio.charset.Charset;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;



public class MainClass{
	
	public static String rev = null;//string to show in the gui frame
	public static int splitDoc;//number of characters to split the document
	public static int numOfClusters;// number of clusters
	public static String textFromPageOnlyLetters = null;// extarcting only letters from the document
	public static Portion portion[];// arrays of portion class
	public static MathematicalCalculations calc=new MathematicalCalculations();// class for mathematical calculations
	public static DefaultDataset dataSet=new DefaultDataset();// dataset to cluster
	public static DefaultDataset[] dataSetClusterd;//clusterd dataset
	public static ArrayList<String> consNgrams= new ArrayList<String>();//constant ngrams from the most common words in the old testament
	public static ArrayList<Integer>[] chapterClusters;// chapters clusters
	public static ArrayList<Double> clusterSilhoueteValuesArray=new ArrayList<Double>();//silhouette value for each cluster
	public static double rankVectorssArray [][];// matrix of all ranked vectors
	public static double dzvMatrix[][];//distance matrix
	public static double clusteringSilhouettevalue=0;//clustering process silhouette value 
	public MainClass(int charactersNumber1,int clustersNumber1)
	{

		int i=1;
		String textFromPage = null;
    	//most common words at the Old testament
    	consNgrams.add("את");
    	consNgrams.add("כי");
    	consNgrams.add("אל");
    	consNgrams.add("על");
    	consNgrams.add("לא");
    	consNgrams.add("כל");
    	consNgrams.add("בן");
    	consNgrams.add("עד");
    	consNgrams.add("לא");
    	consNgrams.add("אם");
    	consNgrams.add("מן");
    	consNgrams.add("לי");
    	consNgrams.add("כה");
    	consNgrams.add("עם");
    	consNgrams.add("גם");
    	consNgrams.add("לך");
    	consNgrams.add("שם");
    	consNgrams.add("כן");
    	consNgrams.add("מי");
    	consNgrams.add("נא");
    	consNgrams.add("או");
    	consNgrams.add("די");
    	consNgrams.add("זה");
    	consNgrams.add("מה");
    	consNgrams.add("בן");
    	consNgrams.add("בת");
    	consNgrams.add("בא");
    	consNgrams.add("לה");
    	consNgrams.add("אף");
    	consNgrams.add("לב");
    	consNgrams.add("הם");
    	consNgrams.add("פן");
    	consNgrams.add("אש");
    	consNgrams.add("אך");
    	consNgrams.add("הר");
    	consNgrams.add("רב");
    	consNgrams.add("לו");
    	consNgrams.add("רק");
    	consNgrams.add("בה");
    	consNgrams.add("פי");
    	consNgrams.add("אז");
    	consNgrams.add("שר");
    	consNgrams.add("יד");
    	consNgrams.add("חי");
    	consNgrams.add("רע");
    	consNgrams.add("שם");
    	consNgrams.add("בי");
    	consNgrams.add("עת");
    	consNgrams.add("חן");
    	consNgrams.add("ים");
    	consNgrams.add("לי");
    	consNgrams.add("יש");
    	consNgrams.add("בו");
    	consNgrams.add("מת");
    	consNgrams.add("בם");
    	consNgrams.add("נח");
    	consNgrams.add("גד");
    	consNgrams.add("בל");
    	consNgrams.add("כח");
    	consNgrams.add("פר");
    	consNgrams.add("שב");
    	ConstatNgrams Ngrams=new ConstatNgrams(consNgrams);

        try {
        	
        	File file = new File("F:/פרוייקט סוף/old_testament.txt"); 
        	  
        	BufferedReader br = new BufferedReader(new FileReader(file)); 
        	String st;
			while ((st = br.readLine()) != null) 
			{
				textFromPage +=st;
        	    
			} 

            textFromPageOnlyLetters=textFromPage.replaceAll("[^א-ת]","");

            
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        splitDoc=1000;// number of characters to split the document if number wasnt enter by the user
        docSplitEqual(charactersNumber1);// number of characters to split the document
        rankVectorssArray= new double [Portion.numberOfPortions][Portion.numberOfNgrams];// ranked vector array containing each portion as ngrams vector
        dzvMatrix=new double [Portion.numberOfPortions][Portion.numberOfPortions];//distance matrix
        rev=String.format("Number of N-grams in each portion: %d", Portion.numberOfNgrams);
        rev+="\n";
        System.out.println("Number of N-grams in each portion: "+Portion.numberOfNgrams);
        System.out.println("Number of Portions: "+Portion.numberOfPortions);
        rev+=String.format("Number of Portions: %d", Portion.numberOfPortions);
        rev+="\n";
        rankVectors();// calculate rank vector to each portion
        zvCalculation();// calculate zv parameter to each portion using Spearmans correlation 
        dzvCalculation();// calculate distance matrix
        numOfClusters=clustersNumber1;// clusters number
        dataSetClusterd=new DefaultDataset[numOfClusters];// data set to cluster
        dataSetClusterd= calc.partitionAroundMedoiods(numOfClusters, 10, dataSet);//clusterd data set
        
        for(i=0;i<numOfClusters;i++)
        	System.out.println(dataSetClusterd[i].size());

        i=1;
        int j=0;
        // priniting each cluster elements
        for(Dataset data:dataSetClusterd)
        {
        	System.out.println("Cluster #" + i + "contains portions number:");
        	rev+=String.format("Cluster # %d contains portions number: \n", i);
        	for(Instance ins:data)
        	{
        		j++;
        		if(j%30==0)
        			rev+="\n";
        		rev+=String.format("%d, ", ins.getID());
        		System.out.print(ins.getID()+",  ");
        	}
        	i++;
        	rev+="\n";
        	System.out.println();
        }
        silhouetteAlgorithm();//calculate silhouette value for the clustering process 
        for(Portion por:portion)
        	clusteringSilhouettevalue+=por.getSilhouetteValue();// suming all silhouette values
        clusteringSilhouettevalue/=(double)Portion.numberOfPortions;//silhouette value for the clustering process
        System.out.println("Silhouete value for the clustering process is: " + clusteringSilhouettevalue);
        rev+=String.format("Silhouete value for the clustering process is: %f", clusteringSilhouettevalue);
        rev+="\n";
        //clustering the old testament portions according to the majority votes of each portion
        if(charactersNumber1<OldTestament.avgCharactersInChapter)
        {
        	chapterClusters= chaptersClustering(charactersNumber1);//array of chapters clusters
        	System.out.println("Clustering chapters according to the majority votes of the portions:");
        	for(i=0;i<chapterClusters.length;i++)
        	{

        		System.out.println("Cluster #" + i + "contains chapters number:" + chapterClusters[i]);;
        
        	}
        }
	}
	// calculate silhouette value for the clustering process
	private static void silhouetteAlgorithm() 
	{
		double minDistance=Double.MAX_VALUE;
		double inClusterDistance=0;
		double aiValue=0,biValue=0,siValue=0;
		double outClusterDistance=0;
		double clusterSilhouette=0;
		for(Dataset data: dataSetClusterd)
		{
			clusterSilhouette=0;
			for(Instance elem: data)
			{
				inClusterDistance=0;
				minDistance=Double.MAX_VALUE;
				for(Instance elemInSameCluster: data)
				{
					inClusterDistance+=dzvMatrix[elem.getID()][elemInSameCluster.getID()];
				}
				aiValue=inClusterDistance/(double)data.size();
				for(Dataset data2: dataSetClusterd)
				{
					outClusterDistance=0;
					if(!data2.contains(dataSet.get(elem.getID())))
					{
						for(Instance elemOutsideCluster: data2)
							outClusterDistance+=dzvMatrix[elem.getID()][elemOutsideCluster.getID()];
						outClusterDistance/=(double)data2.size();
						if(outClusterDistance<minDistance)
							minDistance=outClusterDistance;
					}
					
				}
				biValue=minDistance;
				siValue=biValue-aiValue/Math.max(aiValue, biValue);
				portion[elem.getID()].setSilhouetteValue(siValue);
				clusterSilhouette+=siValue;
			}
			clusterSilhoueteValuesArray.add(clusterSilhouette/(double)data.size());
		}
	}
	// cluster gensis book according to the majority votes of its portions
	public static int gensisCluster(int cluster)
	{
		int counter=0;
		for(int i=0;i<OldTestament.GensisChapters;i++)
		{
			if(chapterClusters[cluster].contains(i))
				counter++;
		}
		return counter;
	}
	// cluster exodus book according to the majority votes of its portions
	public static int exodusCluster(int cluster)
	{
		int counter=0;
		for(int i=50;i<OldTestament.GensisChapters+OldTestament.ExodusChapters;i++)
		{
			if(chapterClusters[cluster].contains(i))
				counter++;
		}
		return counter;
	}
	// cluster levicitus book according to the majority votes of its portions
	public static int leviticusCluster(int cluster)
	{
		int counter=0;
		for(int i=90;i<OldTestament.GensisChapters+OldTestament.ExodusChapters+OldTestament.LeviticusChapters;i++)
		{
			if(chapterClusters[cluster].contains(i))
				counter++;
		}
		return counter;
	}
	// cluster numbers book according to the majority votes of its portions
	public static int numbersCluster(int cluster)
	{
		int counter=0;
		for(int i=117;i<OldTestament.GensisChapters+OldTestament.ExodusChapters+OldTestament.LeviticusChapters+OldTestament.NumbersChapters;i++)
		{
			if(chapterClusters[cluster].contains(i))
				counter++;
		}
		return counter;
	}
	// cluster deuteronomy book according to the majority votes of its portions
	public static int deuteronomyCluster(int cluster)
	{
		int counter=0;
		for(int i=153;i<OldTestament.GensisChapters+OldTestament.ExodusChapters+OldTestament.LeviticusChapters+OldTestament.NumbersChapters+OldTestament.DeuteronomyChapters;i++)
		{
			if(chapterClusters[cluster].contains(i))
				counter++;
		}
		return counter;
	}
	// clusters the chapters according to the majority votes of its portions
	private static ArrayList<Integer>[] chaptersClustering(int charactersNumber1)
	{
		int i,j,k,n,m=0;
		int countersArray[]=new int[numOfClusters];
		ArrayList<Integer>[] chapterClusters=new ArrayList[numOfClusters];
		for(i=0;i<numOfClusters;i++)
			chapterClusters[i]=new ArrayList<Integer>();
		int chapters;
		
			chapters=OldTestament.avgCharactersInChapter/charactersNumber1;
			for(i=0;i<Portion.numberOfPortions;i+=chapters,m++)
			{
				for(j=0,k=i;j<chapters;j++,k++)
				{
					if (k==Portion.numberOfPortions)
					{
						break;
					}
					n=0;
					for(Dataset data:dataSetClusterd)
			        {
						if(data.contains(dataSet.get(k)))
						{
							countersArray[n]++;
							break;
						}
						else
							n++;
			        
			        }
				}
				int maxIndex=calc.getIndexOfMaxValue(countersArray);
				chapterClusters[maxIndex].add(m);
				Arrays.fill(countersArray, 0);
			}
			
		
		return chapterClusters;
	}
	//calculate the distance matrix
	private static void dzvCalculation() 
	{
		int i=0,j=0;
		double ij,ji;
		double[] iijjArray=new double[Portion.numberOfPortions];
		for(Portion por:portion)
		{
			iijjArray[i]=calc.ZVcalculation(i+1,i,i,por.getRankedVector(),rankVectorssArray);
			i++;
		}
		
		for(i=0;i<Portion.numberOfPortions;i++)
			for(j=i+1;j<Portion.numberOfPortions;j++)
			{
				 ij=calc.ZVcalculation(j-i, j, j-1, portion[j].getRankedVector(), rankVectorssArray);
				 ji=calc.ZVcalculation(j-i, i, j, portion[i].getRankedVector(), rankVectorssArray);
				 dzvMatrix[j][i]=dzvMatrix[i][j]=Math.abs(iijjArray[i]+iijjArray[j]-ij-ji);
			}
	/*	for (i = 0; i < dzvMatrix.length; i++) {
		    for (j = 0; j < dzvMatrix[i].length; j++) {
		        System.out.print(dzvMatrix[i][j] + " ");
		    }
		    System.out.println();
		}*/
	}
	// calculate zv parameter to each portion
	public static void zvCalculation() 
	{
		int i=1;
		for(Portion por:portion)
		{
			if(por.getPortionNumber()!=0)
			{
				por.setZV(calc.ZVcalculation(i,i, i-1, por.getRankedVector(), rankVectorssArray));
				i++;
			}
		}
		
	}
	//divide the document according to the requested number
	public static void docSplitEqual(int size)
	{
		int i,j;
		int numOfLetters=textFromPageOnlyLetters.length();
		int divider=size;
        double fractionalNumOfPortions=numOfLetters/divider;
        int numOfPortions=(int)(fractionalNumOfPortions);
        double fraction=fractionalNumOfPortions-numOfPortions;
        if(fraction==0)
        	portion=new Portion[numOfPortions];
        else
        	portion=new Portion[numOfPortions+1];
        for(i=0,j=0;i<portion.length;i++,j+=divider)
        {
        	portion[i]=new Portion(textFromPageOnlyLetters.substring(j,j+divider),i,2);
        	Portion.numberOfPortions++;
        }
        
	}
	//calculate rank vector to each portion
	public static void rankVectors()
	{
		int i=0;
		for(Portion por:portion)
		{
			por.CalculateRankVector(consNgrams);
			rankVectorssArray[i]=por.getRankedVector();
			dataSet.add(new DenseInstance(por.getRankedVector(),por));
			i++;
		}
	
	}

}
