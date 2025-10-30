// Core percolation logic

public class Percolation {

    private final int n;
    private final int virtualTop;
    private final int virtualBottom;
    private final boolean[] bool;
    private final WeightedQuickUnionFind ufPercolation;
    private final WeightedQuickUnionFind ufFullness;
    private int openCount;

    // Creates n-by-n grid, with all sites initially unopened
    public Percolation(int n) {
        this.n = n;
        virtualTop = n * n; // Includes 1st extra index
        virtualBottom = n * n + 1; // Includes 2nd extra index

        // Avoid backwash after percolation by tracking two union-finds
        ufPercolation = new WeightedQuickUnionFind(n * n + 2); // Both virtual sites
        ufFullness = new WeightedQuickUnionFind(n * n + 1);    // Only top virtual site

        bool = new boolean[n * n];

        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be > 0");
        }

        // Using virtual sites reduces percolation check from O(n^2) to O(1),
        // and overall runtime from O(n^4 log n) to O(n^2 log n).
        for (int i = 0; i < n; i++) {
            // Union the virtual top site to each element of the top row
            ufPercolation.union(i, virtualTop);
            ufFullness.union(i, virtualTop);

            // Union each element of the bottom row with virtualBottom
            ufPercolation.union(n * (n - 1) + i, virtualBottom);
        }
    }

    // Translates 2d position into 1d array index
    private int convertToIndex(int row, int col) {
        return n * (row - 1) + (col - 1);
    }

    // Opens the site if it is not open already
    public void open(int row, int col) {
        columnRowOutOfBoundsCheck(row, col);

        int pos = convertToIndex(row, col);

        if (!bool[pos]) {
            bool[pos] = true;
            openCount++;
        }

        boolean leftEdge = (col == 1);
        boolean rightEdge = (col == n);
        boolean topEdge = (row == 1);
        boolean bottomEdge = (row == n);

        // If the current site and any adjacent site is open, union them
        if (!leftEdge && bool[pos - 1]) {
            linkIfOpen(pos, pos - 1);   // Left

        }
        if (!rightEdge && bool[pos + 1]) {
            linkIfOpen(pos, pos + 1);   // Right 

        }
        if (!topEdge && bool[pos - n]) {
            linkIfOpen(pos, pos - n);   // Up

        }
        if (!bottomEdge && bool[pos + n]) {
            linkIfOpen(pos, pos + n);   // Down

        }
    }

    public boolean isOpen(int row, int col) {
        columnRowOutOfBoundsCheck(row, col);
        int pos = convertToIndex(row, col);
        return bool[pos];
    }

    public boolean isFull(int row, int col) {
        columnRowOutOfBoundsCheck(row, col);
        int pos = convertToIndex(row, col);
        if (bool[pos]) { // If site is open, check if site's root equals virtual top
            return ufFullness.find(pos) == ufFullness.find(0);
        } else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        // Special case n = 1. Only 1 row so percolates if open
        if (n == 1) {
            return bool[0];
        } else {
            return ufPercolation.find(virtualBottom) == ufPercolation.find(virtualTop);
        }
    }

    // Helper methods
    private void linkIfOpen(int a, int b) {
        ufPercolation.union(a, b);
        ufFullness.union(a, b);
    }

    private void columnRowOutOfBoundsCheck(int row, int col) {
        if (row < 1) {
            throw new IllegalArgumentException(
                    String.format("Illegal argument: row %d is out bounds.", row));
        } else if (col < 1) {
            throw new IllegalArgumentException(
                    String.format("Illegal argument: col %d is out bounds.", col));
        }
    }

}
