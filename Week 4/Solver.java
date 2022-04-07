import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private MinPQ<SearchNode> priorQueue;
    private MinPQ<SearchNode> twinQueue;
    private boolean solvable;
    private Stack<Board> shortestPathQueue;
    private int numMoves;

    // define a search node of the game to be a board, the number of moves made to
    // reach the board, and the previous search node.
    private class SearchNode
            implements Comparable<SearchNode> {
        private final Board boardState;
        private final int numOfSteps;
        private final SearchNode previousNode;
        private final int costToGoal;
        private final int totalValue;

        public SearchNode(Board board, int step, SearchNode previous) {
            this.boardState = board;
            this.numOfSteps = step;
            this.previousNode = previous;
            this.costToGoal = board.manhattan();
            this.totalValue = this.numOfSteps + this.costToGoal;
        }

        public Board getBoardState() {
            return this.boardState;
        }

        public int getNumOfSteps() {
            return this.numOfSteps;
        }

        public int getCostToGoal() {
            return this.costToGoal;
        }

        public int getTotalValue() {
            return this.totalValue;
        }

        public SearchNode getPreviousNode() {
            return this.previousNode;
        }

        public int compareTo(Solver.SearchNode that) {
            int distanceThis = this.getTotalValue();
            int distanceThat = that.getTotalValue();

            if (distanceThis < distanceThat) {
                return -1;
            } else if (distanceThis > distanceThat) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial must not be null");
        }

        // Initialize the MinPQ;
        this.priorQueue = new MinPQ<SearchNode>();
        this.twinQueue = new MinPQ<SearchNode>();

        // insert the initial search node (the initial board, 0 moves, and a null
        // previous search node) into a priority queue.
        SearchNode smallestNode = new SearchNode(initial, 0, null);

        Board twinBoard = initial.twin();
        SearchNode twinSmallestNode = new SearchNode(twinBoard, 0, null);

        // Add that distance to the prioprity queue
        this.priorQueue.insert(smallestNode);
        this.twinQueue.insert(twinSmallestNode);

        // Repeat this procedure until the search node dequeued corresponds to the goal
        // board.
        while ((smallestNode.getCostToGoal() != 0) && (twinSmallestNode.getCostToGoal() != 0)) { // smallestNode.getCostToGoal()
                                                                                                 // != 0
            // delete from the priority queue the search node with the minimum priority
            smallestNode = this.priorQueue.delMin();
            twinSmallestNode = this.twinQueue.delMin();

            // insert onto the priority queue all neighboring search nodes (those that can
            // be reached in one move from the dequeued search node)
            for (Board neighborBoard : smallestNode.getBoardState().neighbors()) {
                // Dont enqueue the neigbor that resemble the previous node
                if ((smallestNode.getPreviousNode() != null)
                        && neighborBoard.equals(smallestNode.getPreviousNode().getBoardState())) {
                    continue;
                }
                SearchNode neighborNode = new SearchNode(neighborBoard, smallestNode.getNumOfSteps() + 1, smallestNode);
                this.priorQueue.insert(neighborNode);
            }

            for (Board twinNeighborBoard : twinSmallestNode.getBoardState().neighbors()) {
                // Dont enqueue the neigbor that resemble the previous node
                if ((twinSmallestNode.getPreviousNode() != null)
                        && twinNeighborBoard.equals(twinSmallestNode.getPreviousNode().getBoardState())) {
                    continue;
                }
                SearchNode twinNeighborNode = new SearchNode(twinNeighborBoard, twinSmallestNode.getNumOfSteps() + 1,
                        twinSmallestNode);
                this.twinQueue.insert(twinNeighborNode);
            }

            smallestNode = this.priorQueue.min();
            twinSmallestNode = this.twinQueue.min();
        }

        this.shortestPathQueue = new Stack<Board>();
        if (smallestNode.getCostToGoal() == 0) {
            this.solvable = true;
            // Smallest node when dequeued shouled be at goal board
            smallestNode = this.priorQueue.delMin();
            // Number of moves maked from the initial board
            this.numMoves = smallestNode.getNumOfSteps();

            // System.out.println("numMoves: " + this.numMoves);
            // Shortest Path
            while (smallestNode != null) {
                // System.out.println(smallestNode.getBoardState());
                this.shortestPathQueue.push(smallestNode.getBoardState());
                smallestNode = smallestNode.getPreviousNode();
            }
        } else if (twinSmallestNode.getCostToGoal() == 0) {
            this.solvable = false;
            this.numMoves = -1;
            // System.out.println("numMoves: " + this.numMoves);
            this.shortestPathQueue = null;
        } else {
            throw new IllegalStateException("Either smallestNode or its twin should be at goal state");
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.numMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.shortestPathQueue;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}