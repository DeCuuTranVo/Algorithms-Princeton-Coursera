import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;
// import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    private double[] fraction_array;
    private double mean_value;
    private double stddev_value;
    private double low_confidence_value;
    private double high_confidence_value;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        if (trials <= 0)
            throw new IllegalArgumentException("trials must be positive");

        this.fraction_array = new double[trials];

        // Repeat <trials> time
        for (int i = 0; i < trials; i++) {
            // Initialize all sites to be blocked.
            Percolation single_per = new Percolation(n);

            // Repeat the following until the system percolates:
            while (!single_per.percolates()) {
                // Choose a site uniformly at random among all blocked sites.
                int rand_num = StdRandom.uniform(n * n);
                // Open the site.
                // WHAT HAPPENS IF THE SIZE IS ALREADY OPENNED???
                int rand_row = rand_num / n;
                int rand_col = rand_num - rand_row * n;

                // System.out.println(rand_row + " " + rand_col);
                single_per.open(rand_row + 1, rand_col + 1);
            }

            // The fraction of sites that are opened when the system percolates provides an
            // estimate of the percolation threshold.
            double num_open_sites = (double) single_per.numberOfOpenSites();
            double single_faction = num_open_sites / (n * n);
            this.fraction_array[i] = single_faction;
        }

        this.mean_value = StdStats.mean(this.fraction_array);
        this.stddev_value = StdStats.stddev(this.fraction_array);
        this.low_confidence_value = (this.mean_value) - ((1.96 * stddev_value) / Math.sqrt(trials));
        this.high_confidence_value = (this.mean_value) + ((1.96 * stddev_value) / Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean_value;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev_value;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.low_confidence_value;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.high_confidence_value;
    }

    private void print_faction_array() {
        for (int i = 0; i < fraction_array.length; i++) {
            System.out.print(fraction_array[i] + " ");
        }
        System.out.println("");
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        // System.out.println("Experimenting on (" + n + "x" + n + ") for " + T + "
        // trials");

        PercolationStats myPercolationStats = new PercolationStats(n, T);
        // myPercolationStats.print_faction_array();
        System.out.println("mean                    = " + myPercolationStats.mean());
        System.out.println("stddev                  = " + myPercolationStats.stddev());
        System.out.println("95% confidence interval = [" + myPercolationStats.confidenceLo() + ", "
                + myPercolationStats.confidenceHi() + "]");
    }

}
