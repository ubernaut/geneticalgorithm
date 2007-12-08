
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {
	
	public void run()
	{		
		String thirtyBits = new String(), // holds three, 10-bit signed 2's 
		                                  // complement binary integers
		       bestGenomeOfGen,  // best of generation genome
		       worstGenomeOfGen; // worst of generation genome
		String[] population;  // holds fitness of each individual in population
		int numA, numB, numC, // represents the constants A, B, & C
		    maxGenerations,   // maximum # of generations 
		    m;                // the number of candidate solutions
		double fitness = -1,  // value of function after evaluation
		       bestFitOfGen,  // fitness of best-of-generation individual
		       worstFitOfGen, // fitness of worst-of-generation individual
		       avgFitOfGen = 0,   // average fitness of this generation
		       bestFitOfRun;  // fitness of best-of-run individual
		
		
		// Get variables A, B, & C
		
		Scanner read = new Scanner(System.in);
		
		do{		
			System.out.println("Enter three signed integers between -25 & 25: ");
			numA = read.nextInt();
			numB = read.nextInt();
			numC = read.nextInt();
		}while((numA > 25 ) || (numA < -25) || (numB > 25) || (numB < -25)
			|| (numC > 25) || (numC < -25));
		
		// Get maximum number of generations
		
		do{
			System.out.println("Enter the max number of generations: ");
			maxGenerations = read.nextInt();
		}while(maxGenerations < 0);
		
		// Randomly generate the number of candidate solutions then generate
		// three, 10-bit signed 2's complement binary integer values for 
		// each candidate solution
		
		Random randNum = new Random();
		m = randNum.nextInt(100);
		population = new String[m];
		double[] popFitness = new double[m];
		for(int i = 0; i < m; i++)
		{
			thirtyBits = generateTenBits(thirtyBits);
			thirtyBits = generateTenBits(thirtyBits);
			thirtyBits = generateTenBits(thirtyBits);
			population[i] = thirtyBits;
//			System.out.println("             012345678901234567890123456789");
			System.out.println("thirtybits[" +i+ "] = " +thirtyBits);
			thirtyBits = "";
		}		
		
		// initialize values to first genome of initial generation to keep 
		// track of various parameters 
		
	    int currentGen = 0;
	    bestGenomeOfGen = population[0];  
	    worstGenomeOfGen = population[0]; 
	    bestFitOfGen = evaluateFitness(numA, numB, numC, population[0]); 
	    worstFitOfGen = evaluateFitness(numA, numB, numC, population[0]);
	    bestFitOfRun = evaluateFitness(numA, numB, numC, population[0]);
	    int bestGenomeOfGenNum = 0;
	    int worstGenomeOfGenNum = 0;
	    int bestGenomeOfRunNum = 0;
	      
	    // Get the fitnesses, keeping track of various parameters 
	    
		while((currentGen < maxGenerations) && (bestFitOfGen != 0))
		{
			System.out.println("Generation " +currentGen);
			
			// crossover, mutation, & reproduction functions should probably go in here somewhere
							    
			// Runs through this generation's population and evaluates the fitness
			// of each genome
			double runningSum= 0.0;
			for(int i = 0; i < m; i++)    
		    {
				popFitness[i] = evaluateFitness(numA, numB, numC, population[i]);
				
				
				if(popFitness[i]==0)popFitness[i]=1/(popFitness[i]+0.0001f);
				else
				{
					popFitness[i]=1/popFitness[i];
					System.out.println("popFitness= "+i+' '+popFitness[i]+' '+population[i]);
					runningSum +=popFitness[i];
				}
		    }
			Random rando = new Random();
			
			String[] nextGen = new String[m];
			double theChoice = 0;
			String[] crossHolder = new String[2];
			int counter =0;
			int it=0;
			for(int genIndex=0; genIndex<m; genIndex++)
			{
				System.out.println("genIndex= "+genIndex);
				System.out.println("running sum= "+runningSum);
				theChoice = runningSum*rando.nextDouble();
				
				double anotherRunningSum=0.0;
				
				for(it =0; it < m; it++)
				{
					anotherRunningSum+=popFitness[it];
					if(theChoice<=anotherRunningSum+popFitness[it]){
						nextGen[genIndex]=population[it];
						System.out.println("ONE GOT IN! at position "+ genIndex);
						System.out.println (population[it]);
						counter++;
						
						break;}
					else;				
				}
				
				
				
			}
			if(counter<m)return;
			for(int genIndex=0; genIndex<m; genIndex++)
			{
				if(genIndex%3 ==0 &&genIndex<m-1)
				{
					System.out.println("genIndex= "+genIndex);
					crossHolder = crossOver(nextGen[genIndex],nextGen[genIndex+1]);
					population[genIndex]=crossHolder[0];
					population[genIndex+1]=crossHolder[1];
					genIndex++;
				}
				else{
					if(genIndex%2==0)
					{
						population[genIndex]=mutate(nextGen[genIndex]);
					}
					else population[genIndex]=reproduce(nextGen[genIndex]);
					
				}
				
			}
			
			
		    for(int i = 0; i < m; i++)    
		    {
		    	double currentFit = evaluateFitness(numA, numB, numC, population[i]);
		    	
		    	if(bestFitOfGen > currentFit)
		    	{
		    		bestGenomeOfGenNum = i;
		    		bestFitOfGen = currentFit;
		    		bestGenomeOfGen = population[i];
		    		if(bestFitOfRun > currentFit)
		    		{
		    			bestFitOfRun = currentFit;
		    			bestGenomeOfRunNum = i;
		    		}
		    	}
		    	
		    	if(currentFit > worstFitOfGen)
		    	{
		    		worstGenomeOfGenNum = i;
		    		worstFitOfGen = currentFit;
		    		worstGenomeOfGen = population[i];
		    	}
		    	
		    	avgFitOfGen = avgFitOfGen + 
		    	              evaluateFitness(numA, numB, numC, population[i]);
		    }
		    avgFitOfGen = avgFitOfGen / m;
			
			if((currentGen == 0) || ((currentGen % 5) == 0))
			{
				System.out.println("Best genome of generation's number: " +bestGenomeOfGenNum);
				System.out.println("Best genome of generation = " +bestGenomeOfGen);
				System.out.println("Best genome of generation's fitness = " +bestFitOfGen);
				System.out.println("Worst genome of generation's number: " +worstGenomeOfGenNum);
				System.out.println("Worst genome of generation = " +worstGenomeOfGen);
				System.out.println("Worst genome of generation's fitness = " +worstFitOfGen);
				System.out.println("Average fitness of current generation's genomes = "
						            + avgFitOfGen);
//				System.out.println("Fitness of this x,y,z for "
//						           + "|A * (x^3) + B * (y^2) + C * z| = " +fitness);
			}
			avgFitOfGen = 0;  // reset for next gen
			currentGen++;
		}
/*		
		if(currentFit > worstFitOfGen)
    	{
    		worstGenomeOfGenNum = i;
    		worstFitOfGen = currentFit;
    		worstGenomeOfGen = population[i];
    	}
*/		
		// Printout results of run here
		System.out.println("Best-of-run genome's number: " +bestGenomeOfRunNum);
		System.out.println("Best-of-run genome's fitness: " +bestFitOfRun);
		System.out.println("Number of generations: " +currentGen);
		
	}
	
	// Generates and returns a signed 10-bit 2's complement binary integer value
	public String[] crossOver(String alphaString, String betaString)
	{
		Random randNum = new Random();
		randNum.nextInt();
		String[] bothStrings=new String[2];
		String alphaPrimeString = new String();
		String betaPrimeString = new String();
		int whichBit = Math.abs(randNum.nextInt(29));
		if((whichBit>0)||(whichBit<29)){
			System.out.println("wbit"+whichBit);
			System.out.println("bstring "+betaString);
			System.out.println("astring "+alphaString);
						
			alphaPrimeString = alphaString.substring(0, whichBit).concat(betaString.substring(whichBit));
			betaPrimeString = betaString.substring(0, whichBit).concat(alphaString.substring(whichBit));
			
			bothStrings[0]=alphaPrimeString;
			bothStrings[1]= betaPrimeString;
		}
		else {
			bothStrings[0]=alphaString;
			bothStrings[1]= betaString;}
		return bothStrings;
	}
	public String mutate(String alphaString)
	{
		Random randNum = new Random();
		randNum.nextInt();
		char[] stringArray = alphaString.toCharArray();
		int whichBit = Math.abs(randNum.nextInt()%29);
		if(stringArray[whichBit]=='1')stringArray[whichBit]='0';
		
		else stringArray[whichBit]='1';
		
		alphaString = new String(stringArray);
		return alphaString;
	}
	public String reproduce(String alphaString)
	{
		
		return alphaString;
	}
	public String generateTenBits(String bits)
	{
		Random randNum = new Random(); // creates random int between 0 and 1
		
		for(int i = 0; i < 10; i++)
		{
			int binDigit = randNum.nextInt(2);  // generate random binary value
			
			// converts int to String
			if (binDigit == 1)
				bits = bits + "1";
			else 
				bits = bits + "0";   
		}	
		return bits;
	}
	
	// Plugs in and evaluates values for function
	// f(x,y,z) = |A * (x^3) + B * (y^2) + C * z|. 
	
	public double evaluateFitness(int a, int b, int c, String bits)
	{
		int index = 0;      // keeps track of string position
		   
		double  x, y, z,    // decimal values of binary strings
			    result = 0; // holds total value 
				
		x = twosComplementValue(bits, index);  
//		System.out.println("x's value in decimal = " +x);
		result = a * (x * x * x);
		
		y = twosComplementValue(bits, index + 10);
//		System.out.println("y's value in decimal = " +y);
		result = result + (b * (y * y));

		z = twosComplementValue(bits, index + 20);
//		System.out.println("z's value in decimal = " +z);
		result = result + (c * z);
		
		// Get absolute value
		
		if(result < 0)   
			result = result * -1;
		
		return result;			
	}
	
	// Gets decimal value from 10-bit signed 2's complement binary integers. 

	private double twosComplementValue(String binaryNum, int i)
	{
		int factor = 256,   // mult. factor for each binary position (1,2,4,8,16,32,etc.)
		    limit = i + 10,
		    binDigit;
		double value = 0;   // decimal value of the 10-bit signed 2's complement number
		
		if(binaryNum.charAt(i) == '1')  // if sign bit is 1
		{
			for(i = i + 1; i < limit; i++)  // "flip" the remaining bits
			{
				if(binaryNum.charAt(i) == '1')	
					binDigit = 0;
				else
					binDigit = 1;
				value = value + (binDigit * factor);
				factor = factor / 2;
			}
			
			// add 1 to flipped bits, then change to negative
			
			value = -1 * (value + 1);  
			
		} else   // if sign bit is 0
		{
			for(i = i + 1; i < limit; i++)  // no flipping needed for positive value
			{
				if(binaryNum.charAt(i) == '1')	
					binDigit = 1;
				else
					binDigit = 0;
				value = value + (binDigit * factor);
				factor = factor / 2;
			}	
		}
		return value;
	}
}
