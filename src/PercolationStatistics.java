// Tracks statistics of percolation simulation

import util.StdRandom;
import util.StdStats;

public class PercolationStatistics {

    // Stores percolation thresholds p* for T trials
    private double[] trialArgs;
    private int trials;

    // Perform independent trials on an n-by-n grid
    public PercolationStatistics(int n, int trials) {
        IllegalArgumentException e = new IllegalArgumentException(
                "argument(s) out of range");
        if (n <= 0 || trials <= 0) {
            throw e;
        }
        trialArgs = new double[trials];
        this.trials = trials;
        // Randomly open sites, store p* of ith trial (open/total sites)
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                // half open interval [..., ...) --> included, excluded
                int randomRow = StdRandom.uniformInt(1, n + 1);
                int randomCol = StdRandom.uniformInt(1, n + 1);
                perc.open(randomRow, randomCol);
            }
            double percThresh = perc.numberOfOpenSites() / (double) (n * n);
            trialArgs[i] = percThresh;
        }
    }

    // Sample mean of percolation threshold p*
    public double mean() {
        return StdStats.mean(trialArgs);
    }

    // Sample standard deviation (sharpness) of percolation threshold p*
    public double stddev() {
        return StdStats.stddev(trialArgs);
    }

    // Low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    // High endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }
}
