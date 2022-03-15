/*
Corner cases.  By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site: 
    Throw an IllegalArgumentException if any argument to open(), isOpen(), or isFull() is outside its prescribed range. 
    Throw an IllegalArgumentException in the constructor if n ≤ 0.

Performance requirements.  
    The constructor must take time proportional to n2; 
    all instance methods must take constant time plus a constant number of calls to union() and find().
*/
public class Percolation {
    private int[][] grid_id;
    private int[] is_opennings; /// ?
    private int[][] innitial_grid_id;
    private int[] sz;
    private int n;
    private int openned_sites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.openned_sites = 0;
        this.n = n;
        this.grid_id = new int[n][n];
        this.innitial_grid_id = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // System.out.println(i*n+j);
                this.grid_id[i][j] = i * n + j;
                this.innitial_grid_id[i][j] = i * n + j;
            }
        }

        this.is_opennings = new int[n * n + 2];
        this.sz = new int[n * n + 2];

        // all ordinary sites range from 0 to n*n -1 with values -5
        for (int i = 0; i < is_opennings.length; i++) {
            this.is_opennings[i] = -5;
        }

        // top_virtual_site at position n*n with values n*n
        this.is_opennings[n * n] = n * n;
        // bot_virtual_site at position n*n + 1 with values n * n + 1
        this.is_opennings[n * n + 1] = n * n + 1;

    }

    // opens the site (row, col) if it is not open already ??? What happens if it is
    // opened already?
    public void open(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IllegalArgumentException("row and col must be between 1 and n");
        } else {
            row--;
            col--;
        }

        if (this.is_opennings[row * n + col] >= 0) {
            // throw new IllegalStateException("This site is already open");
            return;
        }

        this.is_opennings[row * n + col] = row * n + col;
        this.sz[row * n + col] = 1;

        if (row == 0) {
            union(row, col, n - 1, n); // site(row, col) && top_virtual_site
            // System.out.println("Union (" + row + "," + col + ") to (" + (n-1) + "," + (n)
            // + ")");
            // print_states();
        }

        if ((row > 0) && (is_opennings[(row - 1) * n + col] >= 0)) {
            union(row, col, row - 1, col);
            // System.out.println("Union (" + row + "," + col + ") to (" + (row-1) + "," +
            // col + ")");
            // print_states();
        }
        if ((row < n - 1) && (is_opennings[(row + 1) * n + col] >= 0)) {
            union(row, col, row + 1, col);
            // System.out.println("Union (" + row + "," + col + ") to (" + (row+1) + "," +
            // col + ")");
            // print_states();
        }
        if ((col > 0) && (is_opennings[row * n + (col - 1)] >= 0)) {
            union(row, col, row, col - 1);
            // System.out.println("Union (" + row + "," + col + ") to (" + (row) + "," +
            // (col-1) + ")");
            // print_states();
        }
        if ((col < n - 1) && (is_opennings[row * n + (col + 1)] >= 0)) {
            union(row, col, row, col + 1);
            // System.out.println("Union (" + row + "," + col + ") to (" + (row) + "," +
            // (col+1) + ")");
            // print_states();
        }

        if (row == (n - 1)) {
            union(n - 1, n + 1, row, col); //
            // System.out.println("Union (" + (n-1) + "," + (n+1) + ") to (" + row + "," +
            // col + ")");
            // print_states();
        }

        this.openned_sites++;

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IllegalArgumentException("row and col must be between 1 and n");
        } else {
            row--;
            col--;
        }
        return this.is_opennings[row * n + col] >= 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IllegalArgumentException("row and col must be between 1 and n");
        } else if (!isOpen(row, col)) {
            return false;
        } else {
            row--;
            col--;
        }
        // return (connected(virtual_top_site, current_site(row, col)))
        return connected(row, col, n - 1, n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openned_sites;
    }

    // does the system percolate?
    public boolean percolates() {
        // return conneted(virtual_top_site, virtual_bottom_site);
        return connected(n - 1, n, n - 1, n + 1);
    }

    private void print_grid() {
        for (int i = 0; i < grid_id.length; i++) {
            for (int j = 0; j < grid_id[0].length; j++) {
                System.out.print(this.grid_id[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    private void print_innitial_grid() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(this.innitial_grid_id[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    private void print_opennings() {
        for (int i = 0; i < is_opennings.length; i++) {
            System.out.print(this.is_opennings[i] + "\t");
        }

        System.out.println("");
    }

    private void print_size() {
        for (int i = 0; i < this.sz.length; i++) {
            System.out.print(this.sz[i] + "\t");
        }

        System.out.println("");
    }

    private void print_states() {
        update_grid_id();
        System.out.println("GRID ID - CONNECTION:");
        print_grid();
        System.out.println("INITIAL GRID ID:");
        print_innitial_grid();
        System.out.println("OPENNINGS ARRAY:");
        print_opennings();
        System.out.println("TREE SIZE:");
        print_size();
        System.out.println("======================================================");

    }

    private void update_grid_id() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.grid_id[i][j] = this.is_opennings[i * n + j];
            }
        }
    }

    private int[] root(int row, int col) //// ?????????
    {
        // in: row, col -> index
        // out: index -> row, col
        int index = row * n + col;
        while (index != this.is_opennings[index]) {
            index = this.is_opennings[index];
        }

        int result_row = index / n;
        int result_col = index - result_row * n;
        // System.out.println("result_row: " + result_row + ", result_col: " +
        // result_col);
        return new int[] { result_row, result_col };
    }

    private boolean connected(int row_p, int col_p, int row_q, int col_q) {
        int[] root_p = root(row_p, col_p);
        int[] root_q = root(row_q, col_q);
        return this.is_opennings[root_p[0] * this.n + root_p[1]] == this.is_opennings[root_q[0] * this.n + root_q[1]];
    }

    private void union(int row_p, int col_p, int row_q, int col_q) {
        int[] root_p = root(row_p, col_p);
        int[] root_q = root(row_q, col_q);

        // System.out.println("root p: row:" + root_p[0] + " and col: " + root_p[1]);
        // System.out.println("n: " + this.n);
        // System.out.println("root_p[0]*n+root_p[1] = " + (root_p[0]*n+root_p[1]));
        int pid = this.is_opennings[root_p[0] * n + root_p[1]];

        // System.out.println("root q: row:" + root_q[0] + " and col: " + root_q[1]);
        // System.out.println("n: " + this.n);
        // System.out.println("root_q[0]*n+root_q[1] = " + (root_q[0]*n+root_q[1]));
        int qid = this.is_opennings[root_q[0] * n + root_q[1]];

        if (pid == qid)
            return;
        if (sz[pid] < sz[qid]) {
            this.is_opennings[pid] = qid;
            sz[qid] += sz[pid];
            // System.out.println("is_opennings[" + pid +"] = " + qid);
        } else {
            this.is_opennings[qid] = pid;
            sz[pid] += sz[qid];
            // System.out.println("is_opennings[" + qid +"] = " + pid);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation per = new Percolation(n);
        // per.print_states();

        // per.open(2,2);
        // boolean connected = per.connected(2,2,2,3);
        // System.out.println(connected);
        // System.out.println("-------------------------------------------");
        // per.print_states();

        // per.open(2,3);
        // per.union(2, 2, 2, 3);
        // per.connected(2, 2, 2, 3);
        // System.out.println("-------------------------------------------");
        // per.print_states();

        per.open(2, 1);
        // per.print_states();
        per.open(3, 1);
        // per.print_states();
        // per.open(1, 1);
        // per.print_states();
        per.open(4, 1);
        // per.print_states();
        // per.open(5,1);
        System.out.println("-------------------------------------------");
        // per.print_states();

        int num_open_sites = per.numberOfOpenSites();
        System.out.println(num_open_sites);
        System.out.println("-------------------------------------------");

        boolean check_full = per.isFull(3, 2);
        System.out.println(check_full);
        System.out.println("-------------------------------------------");

        boolean is_percolated = per.percolates();
        System.out.println(is_percolated);
        System.out.println("-------------------------------------------");

    }
}