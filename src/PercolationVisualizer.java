// Main driver and animation for percolation simulation

import javax.swing.*;

import util.StdRandom;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PercolationVisualizer {

    private static final int DEFAULT_N = 30;
    private static final int MAX_N = 100;
    private static final int MIN_N = 1;
    private static final int DEFAULT_TRIALS = 1000;
    private static final int MAX_TRIALS = 10000;
    private static final int ANIMATION_SCALE = 1000;

    public static void main(String[] args) {
        int n = DEFAULT_N;
        int trials = DEFAULT_TRIALS;

        final String inputN = JOptionPane.showInputDialog(
                "Enter grid size n (recommended: " + DEFAULT_N + ", max: " + MAX_N + "):");

        if (inputN != null && !inputN.isBlank()) {
            try {
                n = Integer.parseInt(inputN.trim());
                if (n > MAX_N) {
                    JOptionPane.showMessageDialog(null,
                            "Grid too large for animation. Setting n = " + MAX_N + ".");
                    n = MAX_N;
                } else if (n < MIN_N) {
                    JOptionPane.showMessageDialog(null,
                            "Grid size must be at least " + MIN_N + ". Using n = " + MIN_N + ".");
                    n = MIN_N;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid number. Using default n = " + DEFAULT_N + ".");
                n = DEFAULT_N;
            }
        }

        String inputTrials = JOptionPane.showInputDialog(
                "Enter number of Monte Carlo trials (recommended: " + DEFAULT_TRIALS
                + ", max: " + MAX_TRIALS + "):");

        if (inputTrials != null && !inputTrials.isBlank()) {
            try {
                trials = Integer.parseInt(inputTrials.trim());
                if (trials > MAX_TRIALS) {
                    JOptionPane.showMessageDialog(null,
                            "Too many trials. Setting to " + MAX_TRIALS + ".");
                    trials = MAX_TRIALS;
                } else if (trials < 1) {
                    JOptionPane.showMessageDialog(null,
                            "Trials must be at least 1. Using " + DEFAULT_TRIALS + ".");
                    trials = DEFAULT_TRIALS;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid number. Using default " + DEFAULT_TRIALS + ".");
                trials = DEFAULT_TRIALS;
            }
        }

        JFrame frame = new JFrame("Percolation Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- Two-line status bar ---
        JLabel line1 = new JLabel();
        JLabel line2 = new JLabel();

        Font mono = new Font("Consolas", Font.PLAIN, 16);
        line1.setFont(mono);
        line2.setFont(mono);

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.Y_AXIS));
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusBar.add(line1);
        statusBar.add(Box.createVerticalStrut(3));
        statusBar.add(line2);

        // PercolationPanel Configuration
        PercolationPanel grid = new PercolationPanel(n, line1, line2, trials, ANIMATION_SCALE);

        frame.add(grid, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}

class PercolationPanel extends JPanel implements ActionListener {

    private final int n;
    private final int cellSize;
    private static final int RECOMMENDED_CELLSIZE = 20;
    private static final int MAX_GRID_SIZE_PIXLE = 800;
    private static final int MIN_GRID_SIZE_PIXLE = 3;

    private final Percolation perc;
    private final Timer timer;
    private final int delayMs;
    private final int trials;

    private final JLabel line1;
    private final JLabel line2;

    private boolean statsComputed = false;

    PercolationPanel(int n, JLabel line1, JLabel line2, int trials, int animationScale) {
        this.n = n;
        this.line1 = line1;
        this.line2 = line2;
        this.trials = trials;

        // Handle window sizing for large n
        if (n > RECOMMENDED_CELLSIZE) {
            this.cellSize = Math.max(MIN_GRID_SIZE_PIXLE, MAX_GRID_SIZE_PIXLE / n);
        } else {
            cellSize = 20;
        }

        this.perc = new Percolation(n);
        setPreferredSize(new Dimension(n * cellSize, n * cellSize));
        setBackground(Color.WHITE);

        // Scale animation speed
        delayMs = (int) Math.max(1, 8000 / Math.pow(n, 3));
        timer = new Timer(delayMs, this);
        timer.start();

        updateTopLine();
        line2.setText("Mean: -----   Stddev: -----      95% CI: [-----, -----]");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (perc.percolates()) {
            timer.stop();
            computeAndShowStats();
            repaint();
            return;
        }
        int row = StdRandom.uniformInt(1, n + 1);
        int col = StdRandom.uniformInt(1, n + 1);
        perc.open(row, col);

        updateTopLine();
        repaint();
    }

    private void updateTopLine() {
        line1.setText(String.format("Grid: %dx%d   Open sites: %-4d   Percolates: %-5s   Trials: %d",
                n, n, perc.numberOfOpenSites(), perc.percolates(), trials));
    }

    private void computeAndShowStats() {
        if (statsComputed) {
            return;
        }

        PercolationStatistics ps = new PercolationStatistics(n, trials);
        line2.setText(String.format("Mean: %.4f  Stddev: %.4f     95%% CI: [%.4f, %.4f]",
                ps.mean(), ps.stddev(), ps.confidenceLo(), ps.confidenceHi()));
        statsComputed = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= n; k++) {
                int x = (k - 1) * cellSize;
                int y = (i - 1) * cellSize;

                if (perc.isFull(i, k)) {
                    g.setColor(Color.CYAN);
                } else if (perc.isOpen(i, k)) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }

                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }
}
