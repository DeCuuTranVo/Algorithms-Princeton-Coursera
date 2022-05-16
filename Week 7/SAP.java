import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private final Digraph diGraphStructure;
    // private final Digraph reverseDiGraphStructure;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("G must not be null");
        }

        // Use the original graph to find the source vertex
        this.diGraphStructure = new Digraph(G);
        // System.out.println(this.diGraphStructure);

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > this.diGraphStructure.V() - 1) {
            throw new IllegalArgumentException("v must be between 1 and V - 1 vertex");
        }

        int minSumPathLength = Integer.MAX_VALUE;
        // Two breadth first search to learn connection information from each node
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(this.diGraphStructure, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(this.diGraphStructure, w);

        for (int i = 0; i < this.diGraphStructure.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int pathLengthFromV = pathV.distTo(i);
                int pathLengthFromW = pathW.distTo(i);
                int currrentSumPathLength = pathLengthFromV + pathLengthFromW;
                if (currrentSumPathLength < minSumPathLength) {
                    minSumPathLength = currrentSumPathLength;
                }
            }
        }

        // If there is no such (shortest) path, no change will happens on
        // minSumPathLength, this return -1
        if (minSumPathLength == Integer.MAX_VALUE) {
            return -1;
        }

        return minSumPathLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > this.diGraphStructure.V() - 1) {
            throw new IllegalArgumentException("v must be between 0 and V - 1 vertex");
        }

        int minCommonAncestor = -1;
        int minSumPathLength = Integer.MAX_VALUE;
        // Two breadth first search to learn connection information from each node
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(this.diGraphStructure, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(this.diGraphStructure, w);

        for (int i = 0; i < this.diGraphStructure.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int pathLengthFromV = pathV.distTo(i);
                int pathLengthFromW = pathW.distTo(i);
                int currrentSumPathLength = pathLengthFromV + pathLengthFromW;
                if (currrentSumPathLength < minSumPathLength) {
                    minSumPathLength = currrentSumPathLength;
                    minCommonAncestor = i;
                }
            }
        }

        // If there is no such (shortest) path, no change will happens on
        // minSumPathLength and minCommonAncestor, this return -1
        if (minSumPathLength == Integer.MAX_VALUE) {
            return -1;
        }

        return minCommonAncestor;

    }

    // // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("v and w must not be null");
        }

        int countV = 0;
        for (Integer itemV : v) {
            if (itemV == null) {
                throw new IllegalArgumentException("item in v must not be null");
                // return -1;
            }
            countV++;
        }
        if (countV == 0) {
            // throw new IllegalArgumentException("v must not have zero length");
            return -1;
        }

        int countW = 0;
        for (Integer itemW : w) {
            if (itemW == null) {
                throw new IllegalArgumentException("item in w must not be null");
                // return -1;
            }
            countW++;
        }
        if (countW == 0) {
            // throw new IllegalArgumentException("w must not have zero length");
            return -1;
        }

        int minSumPathLength = Integer.MAX_VALUE;
        // Two breadth first search to learn connection information from each node
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(this.diGraphStructure, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(this.diGraphStructure, w);

        for (int i = 0; i < this.diGraphStructure.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int pathLengthFromV = pathV.distTo(i);
                int pathLengthFromW = pathW.distTo(i);
                int currrentSumPathLength = pathLengthFromV + pathLengthFromW;
                if (currrentSumPathLength < minSumPathLength) {
                    minSumPathLength = currrentSumPathLength;
                }
            }
        }

        // If there is no such (shortest) path, no change will happens on
        // minSumPathLength, this return -1
        if (minSumPathLength == Integer.MAX_VALUE) {
            return -1;
        }

        return minSumPathLength;
    }

    // // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("v and w must not be null");
        }

        int countV = 0;
        for (Integer itemV : v) {
            if (itemV == null) {
                throw new IllegalArgumentException("item in v must not be null");
                // return -1;
            }
            countV++;
        }
        if (countV == 0) {
            // throw new IllegalArgumentException("v must not have zero length");
            return -1;
        }

        int countW = 0;
        for (Integer itemW : w) {
            if (itemW == null) {
                throw new IllegalArgumentException("item in w must not be null");
                // return -1;
            }
            countW++;
        }
        if (countW == 0) {
            // throw new IllegalArgumentException("w must not have zero length");
            return -1;
        }

        int minCommonAncestor = -1;
        int minSumPathLength = Integer.MAX_VALUE;
        // Two breadth first search to learn connection information from each node
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(this.diGraphStructure, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(this.diGraphStructure, w);

        for (int i = 0; i < this.diGraphStructure.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i)) {
                int pathLengthFromV = pathV.distTo(i);
                int pathLengthFromW = pathW.distTo(i);
                int currrentSumPathLength = pathLengthFromV + pathLengthFromW;
                if (currrentSumPathLength < minSumPathLength) {
                    minSumPathLength = currrentSumPathLength;
                    minCommonAncestor = i;
                }
            }
        }

        // If there is no such (shortest) path, no change will happens on
        // minSumPathLength and minCommonAncestor, this return -1
        if (minSumPathLength == Integer.MAX_VALUE) {
            return -1;
        }
        return minCommonAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        // In case of 2 points: digraph1.txt
        // while (!StdIn.isEmpty()) {
        // int v = StdIn.readInt();
        // int w = StdIn.readInt();
        // int length = sap.length(v, w);
        // // System.out.printf("length = %d\n", length);
        // int ancestor = sap.ancestor(v, w);
        // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }

        // In case of 2 iterables of points: digraph25.txt

        // 13 23 24
        Queue<Integer> pointsV = new Queue<Integer>();
        pointsV.enqueue(13);
        pointsV.enqueue(23);
        pointsV.enqueue(24);

        // 6 16 17
        Queue<Integer> pointsW = new Queue<Integer>();
        pointsW.enqueue(6);
        pointsW.enqueue(16);
        pointsW.enqueue(17);

        int length = sap.length(pointsV, pointsW);
        int ancestor = sap.ancestor(pointsV, pointsW);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

    }
}
