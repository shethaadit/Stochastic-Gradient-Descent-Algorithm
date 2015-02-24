import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class GD
{
	//MAIN METHOD
	public static void main(String[] args) throws IOException 
    {
    	//TRAIN-FILE AS FIRST ARGUMENT
        String trainFile = args[0];
        //TEST-FILE AS SECOND ARGUMENT
        String testFile = args[1];
        //LEARNING RATE AS THIRD ARGUMENT
        double learningRate = Double.parseDouble(args[2]);
        //TOTAL ITERATION AS FOURTH ARGUMENT
        int totalIteration = Integer.parseInt(args[3]);
        
        String sTrain;
        String sTest;
        
        int tempIterationCount;
        
        double countTrueTrain = 0.0;
        double countFalseTrain = 0.0;
        double countTrueTest = 0.0;
        double countFalseTest = 0.0;
        
        //DECIMALFORMAT CLASS TO PRINT UP TO 2 DECIMAL VALUES 
        DecimalFormat df = new DecimalFormat("#.###");
        
        double trainAccuracy = 0.0;
        double testAccuracy = 0.0;
        
        //TO CHECK VALID 4 ARGUMENTS
        if(args.length != 4)
		{
			System.out.println("Please correct arguments");
			System.exit(0);
		}
        
        //ARRAYLIST STRUCTURE TO STORE COLLECTION OF ARRAYS FROM TRAIN FILE
        ArrayList<int[]> trainData = new ArrayList<int[]>();
               
        //TO READ DATA FROM TRAIN FILE
        FileReader frTrain = new FileReader(new File(trainFile));
        FileReader frTest = new FileReader(new File(testFile));

        BufferedReader brTrain = new BufferedReader(frTrain);
        BufferedReader brTest = new BufferedReader(frTest);
        
        //SKIPPING FIRST LINE AS IT CONTAINS ATTRIBUTE NAMES (X1, X2,...)
        brTest.readLine();
        
        String tempRow = brTrain.readLine();
        String[] first_row = tempRow.split("\t");
        double[] w = new double[first_row.length];
        
        //INITIALIZING WEIGHTS TO 0
        for(int i = 0;i < w.length;i++)
            w[i] = 0;
        
        while((sTrain=brTrain.readLine()) != null)    
        {   
            if(sTrain.length()==0)
            {
                continue;
            }
            String[] temp = sTrain.split("\t");
            int[] aTrain = new int[temp.length];
            
            for(int i = 0;i < temp.length; i++)
            {
                aTrain[i] = (Integer.parseInt(temp[i]));
            }
            trainData.add(aTrain);
        }
        
        //ARRAYLIST STRUCTURE TO STORE COLLECTION OF ARRAYS FROM TEST FILE
        ArrayList<int[]> testData = new ArrayList<int[]>();
        
        //TO READ DATA FROM TRAIN FILE
        while((sTest=brTest.readLine()) != null)    
        {   
            if(sTest.length() == 0)
            {
                continue;
            }
            String[] temp = sTest.split("\t");
            int[] aTest = new int[temp.length];
            
            for(int i=0;i<temp.length;i++)
            {
                aTest[i] = (Integer.parseInt(temp[i]));
            }
            testData.add(aTest);
        }
     
        tempIterationCount = totalIteration;
        while(tempIterationCount != 0)
        {   
        	//ITERATING ROW BY ROW FROM TRAINING DATA
	        for(int[] temp1:trainData)
	        {
	            int weightedSumTrain = 0;
	            int i;
	            
	            //CALCULATING WEIGHTED SUM FOR ALL ROWS IN TRAIN DATA
	            for(i = 0; i < w.length; i++)
	            {
	                weightedSumTrain += (w[i]*temp1[i]);
	            }
	            //CALCULATING OUTPUT VALUE
	            double o = 1/(1+Math.exp(-weightedSumTrain));
	            int t = temp1[i];
	            
	            //CALCULATING ERROR VALUE
	            double err = learningRate*o*(1-o)*(t-o);
	            
	            //UPDATE WEIGHT 
	            for(i = 0;i < w.length; i++)
	            {
	                double newWeight = err*temp1[i];
	                w[i] += newWeight;
	                
	            }
	            tempIterationCount--;
	            if(tempIterationCount == 0)
	            {
	                break;
	            }
	        }
	  
        }
        
        //FINDING ACCURACY OF TRAIN DATA
        for(int[] train:trainData)
        {
            
            int q;
            double sumTrain = 0;
            for(q = 0; q < w.length; q++)
            {
            	//WEIGHTED SUMMATION
                sumTrain+=(w[q]*train[q]);
            }
            
            //OUTPUT VALUE OF FUNCTION
            double o1 = 1/(1+Math.exp(-sumTrain));
            double o;
            
            //USE 0.5 AS THRESHOLD 
            if(o1 >= 0.5)
            {
                o = 1.0;
            }
            else
            {
                o = 0.0;
            }
            if(o == train[q])
            {
            	//COUNTER FOR CORRECTLY CLASSIFIED INSTANCES TRAIN FILE
                countTrueTrain++;
            }
            else
            {
            	//COUNTER FOR WRONGLY CLASSFIED INSTANCES IN TEST FILE
                countFalseTrain++;
            }
            
        }
        
        //PRINTING TRAIN FILE ACCURACY
        trainAccuracy = (countTrueTrain/(countTrueTrain+countFalseTrain))*100;
        System.out.println();
        System.out.println("Train File");
        System.out.println("Correctly Identified Instances: " + countTrueTrain );
        System.out.println("Not Correctly Identified Instances: " + countFalseTrain );
        System.out.println("Total Instances: " + (countTrueTrain+countFalseTrain));
        System.out.println("Accuracy of Trained Data is: "+ df.format(trainAccuracy) + "%");
        
        //CALCULATING TEST FILE ACCURACY 
        for(int[] train:testData)
        {
        	int q;
        	double weightedSumTest = 0;
            double o2;
            
            //WEIGHTED SUMMATION FROM TEST FILE
            for(q=0;q<w.length;q++)
            {
                weightedSumTest += (w[q]*train[q]);   
            }
            
            //CALCULATING VALUE OF OUTPUT FUNCTION
            double out = 1/(1+Math.exp(-weightedSumTest));
            
            //USING 0.5 AS THRESHOLD
            if(out >= 0.5)
            {
                o2 = 1.0;
            }
            else
            {
                o2 = 0.0;
            }
            if(o2 == train[q])
            {
            	//COUNTER FOR CORRECTLY CLASSFIED INSTANCES IN TEST FILE
                countTrueTest++;
            }
            else
            {
            	//COUNTER FOR WRONGLY CLASSFIED INSTANCES IN TEST FILE
                countFalseTest++;
            }    
        }
        
        //PRINTING TEST FILE ACCURACY
        testAccuracy = countTrueTest/(countTrueTest+countFalseTest)*100;
        System.out.println();
        System.out.println("*************************************");
        System.out.println();
        System.out.println("Test File");
        System.out.println("Correctly Identified Instances: " + countTrueTest );
        System.out.println("Not Correctly Identified Instances: " + countFalseTest );
        System.out.println("Total Instances: " + (countTrueTest+countFalseTest));
        System.out.println("Accuracy of Test Data is: " + df.format(testAccuracy) + "%");
    }
}

