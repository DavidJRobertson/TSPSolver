package orig;

import java.util.*;
import java.io.*;
/**
 * An example of use of the TSP class. The program takes as a command line argument
 * the name of a tsp file. The program then goes through a number of iterations
 * producing a random tour via a Knuth shuffle. This is costed and if better than
 * the best found so far, it is saved. The user is then prompted to continue. On
 * quiting the best tour is saved in the file bestTour.txt
 */
public class RandomSearch {
    
    static void shuffle(int[] tour,Random generator){
	for (int i=tour.length-1;i>0;i--){
	    int j = generator.nextInt(i+1);
	    int temp = tour[i];
	    tour[i] = tour[j];
	    tour[j] = temp;
	}
    }
    //
    // Knuth shuffle
    //	

    public static void main(String[] args) throws IOException {

	String commands = "\nRandomSearch (version 20160217) \n" +
	                  "help, go, point, quit";
	System.out.println(commands);
	Scanner sc = new Scanner(System.in);

	TSP tsp            = new TSP(args[0]);
        int n              = tsp.size();
	int[] tour         = new int[n];
	int[] bestTour     = new int[n];
	double tourCost    = 0.0;
	double bestCost    = Double.MAX_VALUE;
	Random gen         = new Random();
	PrintWriter fout   = new PrintWriter(new FileOutputStream("bestTour.txt"));
	
	for (int i=0;i<n;i++) tour[i] = i;
	// 
	// create a random tour
	//	    

	tsp.plot();
	//
	// plot the problem, cities only
	//

	System.out.print("> ");
	String command = sc.next();

	while (!command.equals("quit") && !command.equals("q")){
	    if (command.equals("help") || command.equals("h")) System.out.println(commands);
	    if (command.equals("point")){
		 System.out.print("point size> ");
		 tsp.setPointSize(sc.nextInt());
	    }
	    if (command.equals("go")){
		for (int iterations=0;iterations<10000;iterations++){
		    shuffle(tour,gen);
		    tourCost = tsp.cost(tour);
		    if (tourCost < bestCost){
			bestCost = tourCost;
			for (int i=0;i<n;i++) bestTour[i] = tour[i];
		    }
		    tsp.plot(tour);
		    tsp.pause(10);
		}
		System.out.println(String.format("%.2f",bestCost));
	    }
	    System.out.print("> ");
	    command = sc.next();
	}
	for (int i=0;i<n;i++) fout.println(bestTour[i]);
	fout.close();
	System.exit(0);
    }
}
