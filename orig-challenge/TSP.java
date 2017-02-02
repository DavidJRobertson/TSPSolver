package orig;
import java.util.*;
import java.io.*;

/**
 * A TSP object is a representation of a traveling salesman problem
 * and provides methods to display the TSP, display a tour of that tsp
 * and to get inter-city distances and the total cost of a tour
 * where a tour is an integer array containg a permutation of the integers 0 to n-1
 */
public class TSP {

    private ArrayList<Integer> X,Y;
    private double[][] Cost;
    private int xMin,xMax,yMin,yMax;
    private int n;
    private double pointSize;
    private String fname;

    public TSP(String fname) throws IOException {
	X           = new ArrayList<Integer>();
	Y           = new ArrayList<Integer>();
	this.fname  = fname;
	pointSize   = 1.0;
	Scanner sc  = new Scanner(new File(fname));
        xMin = yMin = Integer.MAX_VALUE;
        xMax = yMax = Integer.MIN_VALUE;
        n           = 0;
	while (sc.hasNext()){
            int x = sc.nextInt();
            int y = sc.nextInt();
	    xMin = Math.min(xMin,x);
	    xMax = Math.max(xMax,x);
	    yMin = Math.min(yMin,y);
	    yMax = Math.max(yMax,y);
	    X.add(x);
	    Y.add(y);
	    n++;
        }
	sc.close();
	makeCostMatrix();
    }


    private void makeCostMatrix(){
	Cost = new double[n][n];
	double x,y;
	for (int i=0;i<n-1;i++)
	    for (int j = i+1;j<n;j++){
		x = X.get(i) - X.get(j); 
		y = Y.get(i) - Y.get(j);
		Cost[i][j] = Cost[j][i] = Math.sqrt(x*x + y*y);
	    }
    }

    /**
     * plot the cities in the tsp
     */
    public void plot() {
	StdDraw.clear(StdDraw.LIGHT_GRAY);
	StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        StdDraw.show(0);	
	for (int i=0;i<X.size();i++) StdDraw.circle(X.get(i),Y.get(i),pointSize);
        StdDraw.show(0);
	StdDraw.textLeft(xMin,yMin,fname);
    }
    //
    // plot cities
    //

    /**
     * plot the cities and the tour
     */
    public void plot(int[] tour){
	StdDraw.clear(StdDraw.LIGHT_GRAY);
	StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        StdDraw.show(0);
	for (int i=0;i<X.size();i++) StdDraw.circle(X.get(i),Y.get(i),pointSize);
	for (int i=0;i<n-1;i++) drawEdge(tour[i],tour[i+1]);
	drawEdge(tour[0],tour[n-1]);
        StdDraw.show(0);
	StdDraw.textLeft(xMin,yMin,fname); 
	StdDraw.textRight(xMax,yMin,String.format("%.2f",cost(tour)));
    }
    //
    // plot cities and tour
    //

    /**
     * deliver the inter-city cost between two cities
     */
    public double cost(int city1,int city2){return Cost[city1][city2];}
    //
    // get cost between 2 cities
    //

    /**
     * deliver the cost of a tour, where a tour is an integer array containing
     * a permutation of the integers 0 to n-1
     */
    public double cost(int[] tour){
	if (tour.length < n) return -999;
	double cost = 0;
	for (int i=0;i<n-1;i++) cost = cost + Cost[tour[i]][tour[i+1]];
	cost = cost + Cost[tour[0]][tour[n-1]];
	return cost;
    }
    //
    // get cost of a tour
    //

    /**
     * the size is the number of cities in the tsp
     */
    public int size(){return n;}
    //
    // number of cities in tsp
    //

    private void drawEdge(int city0,int city1,java.awt.Color color){
	StdDraw.setPenColor(color);
	StdDraw.line(X.get(city0),Y.get(city0),X.get(city1),Y.get(city1));
    }

    private void unDrawEdge(int city0,int city1){
	StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
	StdDraw.line(X.get(city0),Y.get(city0),X.get(city1),Y.get(city1));
    }

    private void drawEdge(int city0,int city1){
	StdDraw.setPenColor();
	StdDraw.line(X.get(city0),Y.get(city0),X.get(city1),Y.get(city1));
    }	

    /**
     * change the point size
     */
    public void setPointSize(int i){pointSize = 1.0 * i;}

    /**
     * allows a pause, in milliseconds, useful in animations
     */
    public void pause(long inteval){
	try{Thread.sleep(inteval);}
	catch (Exception e) {e.printStackTrace();}
    }
    //
    // sleep for interval milliseconds
    //
}
