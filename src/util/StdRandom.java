package util;

import java.util.Random;

public class StdRandom {

    private static final Random rand = new Random();

    public static int uniformInt(int lo, int hi) {
        return rand.nextInt(hi - lo) + lo; // half-open interval [lo, hi)
    }

    public static double uniformDouble() {
        return rand.nextDouble(); // [0, 1)
    }
}
