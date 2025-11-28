import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull; // To avoid backwash
    private int openSites;
    private final int virtualTop;
    private final int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.n = n;
        this.grid = new boolean[n][n];
        this.openSites = 0;

        // Create UF with n*n sites plus 2 virtual sites (top and bottom)
        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.ufFull = new WeightedQuickUnionUF(n * n + 1); // No virtual bottom to avoid backwash
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) {
            return;
        }

        // Open the site
        grid[row - 1][col - 1] = true;
        openSites++;

        int index = xyTo1D(row, col);

        // Connect to virtual top if in top row
        if (row == 1) {
            uf.union(index, virtualTop);
            ufFull.union(index, virtualTop);
        }

        // Connect to virtual bottom if in bottom row
        if (row == n) {
            uf.union(index, virtualBottom);
        }

        // Connect to open neighbors
        // Top neighbor
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(index, xyTo1D(row - 1, col));
            ufFull.union(index, xyTo1D(row - 1, col));
        }

        // Bottom neighbor
        if (row < n && isOpen(row + 1, col)) {
            uf.union(index, xyTo1D(row + 1, col));
            ufFull.union(index, xyTo1D(row + 1, col));
        }

        // Left neighbor
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(index, xyTo1D(row, col - 1));
            ufFull.union(index, xyTo1D(row, col - 1));
        }

        // Right neighbor
        if (col < n && isOpen(row, col + 1)) {
            uf.union(index, xyTo1D(row, col + 1));
            ufFull.union(index, xyTo1D(row, col + 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        // Use ufFull to avoid backwash problem
        return ufFull.find(xyTo1D(row, col)) == ufFull.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    // Convert 2D coordinates to 1D index
    private int xyTo1D(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // Validate that indices are in range
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException(
                    "Index out of bounds: row=" + row + ", col=" + col + ", n=" + n);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // Test case 1: Small 3x3 grid
        System.out.println("Test 1: 3x3 grid");
        Percolation perc = new Percolation(3);

        System.out.println("Initial state - percolates: " + perc.percolates());
        System.out.println("Open sites: " + perc.numberOfOpenSites());

        perc.open(1, 1);
        System.out.println("After opening (1,1) - isFull(1,1): " + perc.isFull(1, 1));

        perc.open(2, 1);
        perc.open(3, 1);
        System.out.println("After opening column 1 - percolates: " + perc.percolates());
        System.out.println("Open sites: " + perc.numberOfOpenSites());

        // Test case 2: Check corner cases
        System.out.println("\nTest 2: Exception handling");
        try {
            Percolation perc2 = new Percolation(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception for n=0: " + e.getMessage());
        }

        try {
            Percolation perc3 = new Percolation(5);
            perc3.open(0, 1); // Invalid row
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception for invalid index: " + e.getMessage());
        }

        // Test case 3: Check if already open
        System.out.println("\nTest 3: Opening same site twice");
        Percolation perc4 = new Percolation(2);
        perc4.open(1, 1);
        System.out.println("Open sites after first open: " + perc4.numberOfOpenSites());
        perc4.open(1, 1);
        System.out.println("Open sites after second open: " + perc4.numberOfOpenSites());
    }
}