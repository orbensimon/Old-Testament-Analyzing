package javafx.example;

import java.util.Random;

import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
public class MathematicalCalculations implements DistanceMeasure
{	
	public KMedoids kMedoids;// we tried also kmedoids clustering to receive a better silhouette value but decide to use PAM clustering algorithm
	public Random random=new Random();
	// the clustering procedure we use in our program
	public DefaultDataset[] partitionAroundMedoiods(int clusters,int iterations,DefaultDataset dataset)
	{
		Instance[] medoids=new Instance[clusters];
		DefaultDataset[] clusterdDataSet=new DefaultDataset[clusters];
		for(int i=0;i<clusters;i++)
			clusterdDataSet[i]=new DefaultDataset();
		int[] assignments = new int[dataset.size()];
		int index = 0;
		int iter=0;
		int j=0;
		boolean changed=true;
		boolean firstIteration=true;
		double minDis=Double.MAX_VALUE;
		double distance=0;
		double minSum=Double.MAX_VALUE;
		double sum=0;
		for(int i=0;i<clusters;i++)
			medoids[i]=dataset.get(random.nextInt(dataset.size()));
		//medoids[0]=dataset.get(8);
		//medoids[1]=dataset.get(1);
		while(changed && iter<iterations)
		{
			iter++;
			changed=false;
			for(Instance elem: dataset)
			{
				minDis=Double.MAX_VALUE;
				for(int i=0;i<clusters;i++)
				{
					distance = MainClass.dzvMatrix[elem.getID()][medoids[i].getID()];
					if(distance<minDis)
					{
						minDis=distance;
						index=i;
					}
					
				}
				assignments[elem.getID()]=index;
			}
			if(!firstIteration)
			{
				for(int i=0; i<clusters;i++)
					clusterdDataSet[i].removeAllElements();
			}
			firstIteration=false;
			for(int i=0;i<dataset.size();i++)
				clusterdDataSet[assignments[i]].add(dataset.get(i));
			j=0;
			for(Dataset data:clusterdDataSet)
			{
				minSum=Double.MAX_VALUE;
				for(Instance elem:data)
				{
					sum=0;
					for(Instance elem2:data)
						sum+=MainClass.dzvMatrix[elem.getID()][elem2.getID()];
					if(sum<minSum)
					{
						minSum=sum;
						if(!elem.equals(medoids[j]))
						{
							changed=true;
							medoids[j]=elem;
						}
					}
				}
				j++;
			}
		}
		
		return clusterdDataSet;
		
	}
	// calculate zv parameter using speramans coefficient
	public double ZVcalculation(int numOfPrecursors,int currentPortion,int indexOfFirstPrecursor,double[] currVector,double[][] precursorVectors)
	{
		if(numOfPrecursors==0)
			return 0;
		double ZV=0;
		int i;
		for(i=numOfPrecursors-1;i>=0;i--)
		{
			ZV+=SpearmansCoefficient(currVector,precursorVectors[indexOfFirstPrecursor]);
			indexOfFirstPrecursor--;
		}
		return ZV/numOfPrecursors;
	}
	// calculate spearmans value
	private double SpearmansCoefficient(double[] currVector,double[] vector) 
	{
		double difference=0;
		double var;
		int numOfElements=currVector.length;
		int i;
		for(i=0;i<numOfElements;i++)
			difference+=Math.pow(currVector[i]-vector[i],2);
		difference=difference*6;
		var=numOfElements*(Math.pow(numOfElements, 2)-1);
		difference=difference/var;
		
		return difference;
		
	}
	// for kmedoids algorithm
	public boolean compare(double arg0, double arg1) 
	{
		if(arg0>arg1)
			return false;
		return true;
	}
	// for kmedoids algorithm
	public double getMaxValue()
	{
		int i,j;
		double max=0;
		for(i=0;i<Portion.numberOfPortions;i++)
			for(j=i+1;j<Portion.numberOfPortions;j++)
			{
				if(MainClass.dzvMatrix[j][i]>max)
					max=MainClass.dzvMatrix[j][i];
			}
		return max;
	}
	// for kmedoids algorithm
	public double getMinValue()
	{
		int i,j;
		double min=1;
		for(i=0;i<Portion.numberOfPortions;i++)
			for(j=i+1;j<Portion.numberOfPortions;j++)
			{
				if(MainClass.dzvMatrix[j][i]<min)
					min=MainClass.dzvMatrix[j][i];
			}
		return min;
	}
	// for kmedoids algorithm
	public double measure(Instance arg0, Instance arg1)
	{
		int idArg0,idArg1;
		//idArg0=MainClass.dataSet.indexOf(arg0);
		//idArg1=MainClass.dataSet.indexOf(arg1);
		idArg0=arg0.getID()%Portion.numberOfPortions;
		idArg1=arg1.getID()%Portion.numberOfPortions;
		//System.out.println(arg0.getID() + " " +arg1.getID());
		//if(arg0.getID()==Portion.numberOfPortions)
			//return MainClass.dzvMatrix[arg0.getID()-1][arg1.getID()];
		//else if(arg1.getID()==Portion.numberOfPortions)
			//return MainClass.dzvMatrix[arg0.getID()][arg1.getID()-1];
		return MainClass.dzvMatrix[idArg0][idArg1];
	}
	// for kmedoids algorithm
	public Dataset[] clusteringKmedoids(int k,DefaultDataset dataSet)
	{
		kMedoids=new KMedoids(k, Portion.numberOfPortions, this);
		return kMedoids.cluster(dataSet);
		
	}
	// for kmedoids algorithm
	public int getIndexOfMaxValue(int[] arr) {
		int max=0;
		for(int i=1;i<arr.length;i++)
			if(arr[i]>max)
				max=i;
		return max;
		
		
	}


}
