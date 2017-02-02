import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A Travelling Salesman Problem (TSP)
 * @author David Robertson
 */
public class TSP {

    /**
     * A city/town/vertex/... in a TSP
     * @author David Robertson
     */
    public class City {
        public int x;
        public int y;

        /**
         * @param x the x-coord of the city
         * @param y the y-coord of the city
         */
        public City(int x, int y) { this.x = x; this.y = y; }

        /**
         * @return the distance to the other city
         */
        public double distanceTo(City other) {
            int dx = x - other.x;
            int dy = y - other.y;
            return Math.sqrt(dx*dx + dy*dy);
        }
    }

    private ArrayList<City> cities = new ArrayList<>();
    private int n = 0;
    private double[][] Cost;

    /**
     * Creates a new TSP from a file
     * @param fname the path of the TSP definition file
     * @throws IOException
     */
    public TSP(String fname) throws IOException {
        // Read in data file
        Scanner sc = new Scanner(new File(fname));
        while (sc.hasNext()) {
            City city = new City(sc.nextInt(), sc.nextInt());
            cities.add(city);
            n++;
        }
        sc.close();

        // Construct cost matrix
        Cost = new double[n][n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                City city_i = cities.get(i);
                City city_j = cities.get(j);
                Cost[i][j] = Cost[j][i] = city_i.distanceTo(city_j);
            }
        }
    }

    /**
     * @return the list of all cities in the TSP
     */
    public List<City> getCities() {
        return cities;
    }

    /**
     * @return the cost of travelling between two cities
     */
    public double cost(int city1, int city2) {
        return Cost[city1][city2];
    }

    /**
     * @param tour an integer array containing a permutation of the integers 0 to n-1
     * @return the total cost of the tour
     */
    public double cost(int[] tour) {
        if (tour.length < n) {
            return -999;
        }
        double cost = 0;
        for (int i = 0; i < n - 1; i++) {
            cost = cost + Cost[tour[i]][tour[i + 1]];
        }
        cost = cost + Cost[tour[0]][tour[n - 1]];
        return cost;
    }


    /**
     * @return the number of cities in the tsp
     */
    public int size() {
        return n;
    }
}
