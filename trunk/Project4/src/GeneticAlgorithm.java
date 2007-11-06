
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {
	
	public void run()
	{		
		String thirtyBits = new String(); // holds three, 10-bit signed 2's 
		                                  // comp binary integers
		int numA, numB, numC, // represents the constants A, B, & C
		    maxGenerations;   // maximum # of generations  
		double fitness = -1;  // value of function after evaluation
		
		
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
		
		// Generate three, 10-bit signed 2's complement binary integer values,
		// store in single String
		
		thirtyBits = generateTenBits(thirtyBits);
		thirtyBits = generateTenBits(thirtyBits);
		thirtyBits = generateTenBits(thirtyBits);
		System.out.println("             012345678901234567890123456789");
		System.out.println("thirtyBits = " +thirtyBits);
		
		// Get the fitness
		
	    int currentGen = 0;
		while((currentGen < maxGenerations) && (fitness != 0))
		{
			fitness = evaluateFitness(numA, numB, numC, thirtyBits);

// crossover, mutation, & reproduction functions should probably go in here somewhere
			
			if((currentGen == 0) || ((currentGen % 5) == 0))
			{
				System.out.println("Fitness of this x,y,z for "
						           + "|A * (x^3) + B * (y^2) + C * z| = " +fitness);
			}
			currentGen++;
		}
		
		// Printout results of run here
		
	}
	
	// Generates and returns a signed 10-bit 2's complement binary integer value
	
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
		System.out.println("x's value in decimal = " +x);
		result = a * (x * x * x);
		
		y = twosComplementValue(bits, index + 10);
		System.out.println("y's value in decimal = " +y);
		result = result + (b * (y * y));

		z = twosComplementValue(bits, index + 20);
		System.out.println("z's value in decimal = " +z);
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
