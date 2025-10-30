package util;

public class StdStats {

    public static double mean(double[] a) {
        double sum = 0.0;
        for (double v : a) {
            sum += v;
        }
        return sum / a.length;
    }

    public static double stddev(double[] a) {
        double mu = mean(a);
        double sumSq = 0.0;
        for (double v : a) {
            sumSq += (v - mu) * (v - mu);
        }
        return Math.sqrt(sumSq / (a.length - 1));
    }
}
