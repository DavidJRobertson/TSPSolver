import java.awt.*;
import java.io.IOException;

/**
 * GUI for the TSP solver
 */

public class SolverUI {
    private AntSolver solver;
    private TSP tsp;
    private double pointSize = 5.0;

    public SolverUI(AntSolver solver) {
        this.solver = solver;
        this.tsp    = solver.getTsp();
    }

    private void drawEdge(int city0, int city1) {
        TSP.City a = tsp.getCities().get(city0);
        TSP.City b = tsp.getCities().get(city1);
        StdDraw.line(a.x, a.y, b.x, b.y);
    }

    public void plot() {
        // Calculate bounding box and set scaling
        int xMin, xMax, yMin, yMax;
        xMin = yMin = Integer.MAX_VALUE;
        xMax = yMax = Integer.MIN_VALUE;
        for (TSP.City city : tsp.getCities()) {
            xMin = Math.min(xMin, city.x);
            xMax = Math.max(xMax, city.x);
            yMin = Math.min(yMin, city.y);
            yMax = Math.max(yMax, city.y);
        }
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);

        // Clear the window
        StdDraw.clear(StdDraw.LIGHT_GRAY);

        // Draw pheromone trails
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        double maxTrailVal = 1.0;
        for (int i = 0; i < tsp.size(); i++) {
            for (int j = 0; j < tsp.size(); j++) {
                maxTrailVal = Math.max(maxTrailVal, solver.getTrail(i, j));
            }
        }
        for (int i = 0; i < tsp.size(); i++) {
            for (int j = i+1; j < tsp.size(); j++) {
                double trailVal = solver.getTrail(i, j);
                float opacity = (float) (trailVal/maxTrailVal);
                if (opacity > 0.025) {
                    StdDraw.setPenColor(new Color(1.0f, 1.0f, 0.0f, opacity));
                    drawEdge(i, j);
                }
            }
        }

        // Draw best tour (if we have one)
        StdDraw.setPenColor();
        StdDraw.setPenRadius();

        int tour[] = solver.getBestTour();
        if (tour != null) {
            for (int i = 0; i < tsp.size() - 1; i++) {
                drawEdge(tour[i], tour[i + 1]);
            }
            drawEdge(tour[0], tour[tsp.size() - 1]);
            StdDraw.textLeft(xMin, yMin, Integer.toString(solver.getCycle()));
            StdDraw.textRight(xMax, yMin, String.format("%.2f", tsp.cost(tour)));
        }

        // Draw cities
        //StdDraw.setPenColor(StdDraw.BLACK);
        //for (TSP.City city : tsp.getCities()) {
        //    StdDraw.circle(city.x, city.y, pointSize);
        //}

        // Finally, display all in the window
        StdDraw.show(0);
    }

    public void solve(int maxCycles) throws IOException {
        plot();

        solver.solveInit();
        while (solver.getCycle() < maxCycles) {
            solver.solveCycle();

            if (solver.getCycle() % 10 == 0) {
                plot();
            }

            if (solver.getCycle() % 50 == 0) {
                int cost = (int) tsp.cost(solver.getBestTour());
                if (cost < 12500) {
                    solver.saveBestTour("best." + cost + ".tour");
                }
            }
        }

        solver.saveBestTour("best."+((int)tsp.cost(solver.getBestTour()))+".tour");
    }

    ///////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        TSP       tsp    = new TSP(args[0]);
        AntSolver solver = new AntSolver(tsp);
        SolverUI  ui     = new SolverUI(solver);
        ui.solve(50000);
    }
}
