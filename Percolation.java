/****************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    API only. Must be called from another class.
 *  Dependencies: java.lang, stdlib.jar, WeightedQuickUnionUF.java
 *
 *  This program estimates the value of the percolation 
 *  threshold via Monte Carlo simulation.
 * 
 *  Written by: Justin M. Vallely
 * 
 *  Example usage:
 *  java-algs4 PercolationVisualizer percolation-testing/input10.txt
 ****************************************************************************/
public class Percolation {
    
    private int gridsize;              // array input size N-by-N
    private int M;                     // 2D array dimension
    private boolean[] booleangrid;     // array to keep track of open/close
    private WeightedQuickUnionUF UF_A; // union find object A (for percolation)
    private WeightedQuickUnionUF UF_B; // union find object B (for backwash)
    private boolean percolate = false; // return value for percolates method
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        // initialize a grid for storing a boolean value indicating if site is 
        // Initialize a WeightedQuickUnionUF objects
        if (N <= 0) throw new java.lang.IllegalArgumentException();
        M = N;
        gridsize = M*M+2;
        booleangrid = new boolean[gridsize];
        UF_A = new WeightedQuickUnionUF(gridsize);
        UF_B = new WeightedQuickUnionUF(gridsize);
        for (int j = 1; j <= M; j++) {
            // connect top row to top virtual site
            UF_A.union(0, xyTo1D(1, j));
            UF_B.union(0, xyTo1D(1, j));
            // connect bottom row to bottom virtual site
            UF_A.union(xyTo1D(M, j), gridsize-1);
        }
    }
    
    // convert xy coordinate to a 1D array ID
    private int xyTo1D(int i, int j) {
        return (M*(i-1))+j;
    }
    
    // verify the input coordinates are within the grid size
    private void validate(int i, int j) {
        if (i <= 0 || i > M) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        else if (j <= 0 || j > M) 
            throw new IndexOutOfBoundsException("column index j out of bounds");
        else return;
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        validate(i, j);
        if (!isOpen(i, j)) { // if the site is not open
            booleangrid[xyTo1D(i, j)] = true; // mark the site as open
            if (j < M) { //right
                if (isOpen(i, j+1)) {
                    UF_A.union(xyTo1D(i, j), (xyTo1D(i, j))+1);
                    UF_B.union(xyTo1D(i, j), (xyTo1D(i, j))+1);
                }
            }
            if (j > 1) { //left
                if (isOpen(i, j-1)) {
                    UF_A.union(xyTo1D(i, j), (xyTo1D(i, j))-1);
                    UF_B.union(xyTo1D(i, j), (xyTo1D(i, j))-1);
                }
            }
            if (i < M) { //down
                if (isOpen(i+1, j)) {
                    UF_A.union(xyTo1D(i, j), (xyTo1D(i, j))+M);
                    UF_B.union(xyTo1D(i, j), (xyTo1D(i, j))+M);
                }
            }
            if (i > 1) { //up
                if (isOpen(i-1, j)) {
                    UF_A.union(xyTo1D(i, j), (xyTo1D(i, j))-M);
                    UF_B.union(xyTo1D(i, j), (xyTo1D(i, j))-M);
                }
            }
        }
        isFull(i, j);
        //else return;
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return booleangrid[xyTo1D(i, j)];
    }
    
    // is site (row i, column j) full?    
    public boolean isFull(int i, int j) {
        //If site is connected to top virtual root, if so, then yes
        validate(i, j);
        boolean full = false;
        if (isOpen(i, j) && UF_B.connected(0, xyTo1D(i, j))) full = true;
        return full;
    }
    
    // does the system percolate?
    public boolean percolates() {
        //Percolates if bottom virtual site is connected to top virtual site
        if (!percolate)
            if (UF_A.connected(0, gridsize-1)) percolate = true;
        // special case for 1x1 grid
        if (M == 1 && !isOpen(1, 1))
            percolate = false;
        return percolate;
    }
}
