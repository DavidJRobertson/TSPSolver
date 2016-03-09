package yt.robertson.david.tsp;

import java.util.*;
import java.io.*;


/**
 * Solves a TSP using a simulated ant colony
 * @see <a href="http://www.agent.ai/doc/upload/200302/dori96.pdf">The Ant System: Optimization by a colony of cooperating agents</a>
 * @see <a href="https://github.com/lukedodd/ant-tsp/blob/master/AntTsp.java">Luke Dodd's implementation of the previous algorithm</a>
 *
 * @author David Robertson
 */
public class AntSolver {
    //// Algorithm Parameters ////
    /** Initial trail value (c) */
    public double p_initTrail =  1.0;

    /** Pheromone trail weight */
    public double p_alpha     =  1.0;

    /** Inverse distance weight [not sure if this should be negative or not] */
    public double p_beta      =  5.0;

    /** Evaporation factor */
    public double p_evap      =  0.5;

    /** Ant factor (number of ants as a fraction of number of no. of cities) */
    public double p_antfac    =  0.8;

    /** Trail deposition coefficient */
    public double p_q = 500.0;


    //// Private variables ////
    private TSP tsp;
    private ArrayList<Ant> ants = new ArrayList<>();
    private double         trail[][]; // Pheromone trail matrix
    private int            bestTour[];
    private double         bestTourCost = Double.MAX_VALUE;
    private Random         random = new Random();
    private int            cycle = 0;


    /**
     * An ant in the simulated colony
     */
    private class Ant {
        private TSP    tsp;
        private double trail[][];

        public int     tour[];
        public boolean tabu[];
        public int     tourIndex = 1;
        public int     startingCity;

        public Ant(TSP tsp, double[][] trail) {
            this.tsp   = tsp;
            this.trail = trail;

            tour = new int[tsp.size()];
            tabu = new boolean[tsp.size()];
            startingCity = random.nextInt(tsp.size());

            tour[0] = startingCity;
            tabu[startingCity] = true;
        }

        public void doTour() {
            for (int i = tourIndex-1; i < tsp.size()-1; i++) {
                move();
            }
        }
        private void move() {
            // Calculate the probability of visiting each city
            int currentCity = tour[tourIndex-1];
            double probability[] = new double[tsp.size()];
            double sum = 0.0;
            for (int city = 0; city < tsp.size(); city++) {
                if (tabu[city]) { // Never visit a city we already have
                    probability[city] = 0.0;
                } else {
                    double trailStrength = trail[currentCity][city];
                    double visibility    = 1.0 / tsp.cost(currentCity, city);
                    double p = pow(trailStrength, p_alpha) * pow(visibility, p_beta);
                    sum += p;
                    probability[city] = p;
                }
            }
            for (int city = 0; city < tsp.size(); city++) {
                probability[city] /= sum;
            }

            // Then choose which city based on these probabilities
            int destCity = 0;
            double p = random.nextDouble();
            double cumulativeProbability = 0.0;
            for (int city = 0; city < tsp.size(); city++) {
                cumulativeProbability += probability[city];
                if (cumulativeProbability >= p) {
                    destCity = city;
                    break;
                }
            }

            // Then move to the chosen city
            tour[tourIndex] = destCity;
            tabu[destCity]  = true;
            tourIndex++;
        }

        public double getTourCost() {
            return tsp.cost(tour);
        }
    }



    //// Constructor ////
    public AntSolver(TSP tsp) {
        this.tsp = tsp;
        trail = new double[tsp.size()][tsp.size()];
    }

    //// Public methods ////

    /**
     * Initialises the solver
     */
    public void solveInit() {
        cycle = 0;

        // Clear trails
        for (int i = 0; i < tsp.size(); i++) {
            for (int j = 0; j < tsp.size(); j++) {
                trail[i][j] = p_initTrail;
            }
        }
    }

    /**
     * Performs one solve cycle
     */
    public void solveCycle() {
        cycle++;
        setupAnts();
        doAntTours();
        doPheromoneTrailEvaporation();
        applyAntPheromoneTrail();
        updateBestTour();
    }

    /**
     * Saves the best tour to a file
     * @param path the path of the file to save to
     * @throws IOException
     */
    public void saveBestTour(String path) throws IOException {
        PrintWriter fh = new PrintWriter(new FileOutputStream(path));
        for (int city : bestTour) {
            fh.println(city);
        }
        fh.close();
    }



    //// Private methods ////
    private void setupAnts() {
        int numAnts = (int) (p_antfac * tsp.size());

        // Place ants on cities randomly
        ants = new ArrayList<>();
        for (int k = 0; k < numAnts; k++) {
            Ant ant = new Ant(tsp, trail);
            ants.add(ant);
        }
    }

    private void doAntTours() {
        // Move all ants until they've all completed a full tour
        for (Ant ant : ants) {
            ant.doTour();
        }
    }

    private void doPheromoneTrailEvaporation() {
        // Pheromone trail evaporation
        for (int i = 0; i < tsp.size(); i++) {
            for (int j = 0; j < tsp.size(); j++) {
                trail[i][j] *= (1 - p_evap);
            }
        }
    }

    private void applyAntPheromoneTrail() {
        // Ant path contribution to pheromone trail
        for (Ant ant : ants) {
            double contribution = p_q/ant.getTourCost(); //p_tdc / ant.getTourCost();
            depositTrail(ant.tour, contribution);
        }

        // Apply extra pheromone to best route
        //if (bestTour != null && p_brc > 0) {
        //    depositTrail(bestTour, p_brc * ants.size() * p_q/bestTourCost);
        //}
    }

    private void depositTrail(int[] tour, double amount) {
        for (int i = 0; i < tsp.size()-1; i++) {
            trail[tour[i]][tour[i+1]] += amount;
            //trail[tour[i+1]][tour[i]] += amount; // TODO: should this be symmetric?
        }
        trail[tour[tsp.size()-1]][tour[0]] += amount;
        //trail[tour[0]][tour[tsp.size()-1]] += amount;
    }

    private void updateBestTour() {
        // Update the best tour
        for (Ant ant : ants) {
            if (ant.getTourCost() < bestTourCost) {
                bestTourCost = ant.getTourCost();
                bestTour     = ant.tour.clone();

                System.out.print(bestTourCost);
                System.out.print(" -> ");
                System.out.println(Arrays.toString(bestTour));
            }
        }
    }



    // Fast approximate pow
    // See http://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
    private static double pow(final double a, final double b) {
        final long tmp  = Double.doubleToLongBits(a);
        final long tmp2 = (long)(b * (tmp - 4606921280493453312L)) + 4606921280493453312L;
        return Double.longBitsToDouble(tmp2);
    }









    //// GETTERS ////
    public int[] getBestTour() {
        if (bestTourCost < Double.MAX_VALUE) {
            return bestTour;
        } else {
            return null;
        }
    }
    public double getTrail(int i, int j) {
        return trail[i][j];
    }
    public int getCycle() {
        return cycle;
    }
    public TSP getTsp() {
        return tsp;
    }


    //// SIMPLE MAIN METHOD ////
    public static void main(String[] args) throws IOException {
        TSP          tsp = new TSP(args[0]);
        AntSolver solver = new AntSolver(tsp);

        int maxCycles = 50000;

        solver.solveInit();
        while (solver.getCycle() < maxCycles) {
            solver.solveCycle();
        }

        solver.saveBestTour("cli-best.tour");
    }
}
