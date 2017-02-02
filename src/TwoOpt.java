import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author David Robertson
 */
public class TwoOpt {
    private static int[] twoOptSwap(TSP tsp, int[] tour, int i, int k) {
        int newTour[] = new int[tsp.size()];
        int ni = 0;
        for(int j = 0; j < i; j++) {
            newTour[ni] = tour[j];
            ni++;
        }
        for(int j = k; j > i; j--) {
            newTour[ni] = tour[j];
            ni++;
        }
        for(int j = k+1; j < tsp.size(); j++) {
            newTour[ni] = tour[j];
            ni++;
        }
        return newTour;
    }

    public static int[] optimize(TSP tsp, int[] tour) {
        outerLoop: {
            while (true) {
                double bestDistance = tsp.cost(tour);
                for (int i = 0; i < tsp.size() - 1; i++) {
                    for (int k = i + 1; k < tsp.size(); k++) {
                        int newTour[] = twoOptSwap(tsp, tour, i, k);
                        double newDistance = tsp.cost(newTour);
                        if (newDistance < bestDistance) {
                            tour = newTour;
                        } else {
                            break outerLoop;
                        }

                    }
                }
            }
        }
        return tour;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException {
        // Read in tsp
        TSP tsp = new TSP(args[0]);

        // Read in tour
        String infile = args[1];
        Scanner sc = new Scanner(new File(infile));
        List<Integer> tour = new ArrayList<>();
        while (sc.hasNext()) {
            tour.add(sc.nextInt());
        }
        sc.close();

        System.out.println("Pre 2opt cost: " + tsp.cost(tour.stream().mapToInt(i->i).toArray()) );

        // Perform 2opt optimisation
        int optTour[] = optimize(tsp, tour.stream().mapToInt(i->i).toArray());

        System.out.println("Post 2opt cost: " + tsp.cost(optTour));

        // Output optimised tour to file
        String outfile = args[2];
        PrintWriter fh = new PrintWriter(new FileOutputStream(outfile));
        for (int city : optTour) {
            fh.println(city);
        }
        fh.close();
    }
}
