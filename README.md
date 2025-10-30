# Percolation Simulation 
### Connor Melton | Java | 2024

A Java implementation of a percolation system modeled with a Weighted Quick Union–Find data structure and algorithm, Monte Carlo simulations, statistical analysis, and real-time visualization.

**Features**
- Custom union–find algorithm
- Monte Carlo simulation to estimate percolation threshold
- Visualizer handles user input and process animation
- Modular and organized code

**Usage**
```bash
javac -d bin src\util\*.java src/*.java
java -cp bin PercolationVisualizer
```

## Demo

**Process Overview**

- The system percolates when the top connects to the bottom.
- All sites begin closed (black) and are randomly opened (white).
- When a site is opened, it is unioned with any adjacent open sites.
- If the newly opened site becomes connected to the virtual top site (root), 
  all sites in that connected component are colored blue.
- The simulation continues until the system percolates.

**Goal**

- Simulate percolatsion to estimate the percolation threshold p* (the fraction of open sites when the system percolates, averaged over t trials).

**Significance**

- Currently there is no known way to calculate the percolation threshold p∗ for a square lattice theoretically. Experimentally its estimated as p* ≈ 0.592746.
  https://mathworld.wolfram.com/PercolationThreshold.html

**Outcome**

- I was able to successfully estimate mean p* ~= 0.592... accurately to three decimal places.

### Example 1

<img src="exampleOutput/perc_example1_in_progress.png" width="600">

<img src="exampleOutput/perc_example1_finished.png" width="600">

### Example 2

<img src="exampleOutput/perc_example2_in_progress.png" width="600">

<img src="exampleOutput/perc_example2_finished.png" width="600">

