import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final int trials;
    private double meanValue;
    private double stddevValue;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "Both n and trials must be greater than 0");
        }

        this.trials = trials;
        this.thresholds = new double[trials];

        // Run trials
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            // Randomly open sites until system percolates
            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                perc.open(row, col);
            }

            // Calculate threshold for this trial
            thresholds[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

        // Calculate statistics
        this.meanValue = StdStats.mean(thresholds);
        this.stddevValue = StdStats.stddev(thresholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return meanValue - (CONFIDENCE_95 * stddevValue / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return meanValue + (CONFIDENCE_95 * stddevValue / Math.sqrt(trials));
    }

    // test client
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java PercolationStats n trials");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = ["
                + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}