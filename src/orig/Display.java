package orig;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
/**
 * Display a tour for a given tsp. Takes two command line arguments:
 * a tsp file and a tour file where a tour file is permutation of integers 0 to n-1
 * The program displays the tour and returns the cost of that tour or throws an exception if the file is not
 * a valid tour
 */
public class Display {

    public static void main(String[] args) throws IOException {

	String tspFile     = args[0];
	String tourFile    = args[1];
	TSP tsp            = new TSP(tspFile);
        int n              = tsp.size();
	int[] tour         = new int[n];
	int[] occurs       = new int[n];
	Scanner sc         = new Scanner(new File(tourFile));

	for (int i=0;i<n;i++){
	    tour[i] = sc.nextInt();
	    if (tour[i] < 0 || tour[i] >= n) throw new TSPException("Not a tour: city out of range");
	    occurs[tour[i]]++;
	}
	sc.close();
	for (int i=0;i<n;i++){
	    if (occurs[i] == 0) throw new TSPException("Not a tour: city omission");
	    if (occurs[i] > 1)  throw new TSPException("Not a tour: city duplicated");
	}

	tsp.plot(tour);
	sc = new Scanner(System.in);
	System.out.print("q to quit> ");
	sc.next();
	System.exit(0);
    }
}
