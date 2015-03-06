/****************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java-algs4 PercolationStats N T
 *  Dependencies: java.lang, stdlib.jar, WeightedQuickUnionUF.java, 
 *                Percolation.java
 *
 *  This program estimates the value of the percolation 
 *  threshold by running a Monte Carlo simulation of N size, T times.
 * 
 *  N and T args must be integers greater than 0
 * 
 *  Written by: Justin M. Vallely
 *
 ****************************************************************************/
public class PercolationStats {
    
    private double[] opensites;    // stores # of open sites at percolation
    private double opencount = 0;  // counts number of open sites
    private int times;             // stores arg T for use throughout
    private int index = 0;         // cycles through the opensites array
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new java.lang.IllegalArgumentException();
        times = T;
        opensites = new double[T];
        int randomi = 0;
        int randomj = 0;
        double gridsize = N*N;
        // open random sites until the system percolates
        for (int j = 0; j < T; j++) {
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                randomi = StdRandom.uniform(N) + 1;
                randomj = StdRandom.uniform(N) + 1;
                if (!perc.isOpen(randomi, randomj)) {
                    perc.open(randomi, randomj);
                    opencount++;
                }
            }
            opensites[index] = (opencount / gridsize);
            index++;
            opencount = 0;
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(opensites);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        if (times == 1) return Double.NaN;
        else return StdStats.stddev(opensites);
    }
    
    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        if (times == 1) return Double.NaN;
        else return mean() - (1.96 * (stddev())) / (Math.sqrt(times));
    }
    
    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        if (times == 1) return Double.NaN;
        else return mean() + (1.96 * (stddev())) / (Math.sqrt(times));
    }
    
    // test client
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percstats = new PercolationStats(N, T);
        //new PercolationStats(N, T);
        StdOut.println("mean                    = " + percstats.mean());
        StdOut.println("stddev                  = " + percstats.stddev());
        StdOut.println("95% confidence interval = " + percstats.confidenceLo() 
                           + ", " + percstats.confidenceHi());
    }
}
