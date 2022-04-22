import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

/* 
Step 1: Create a tree with only keys, simulate insert -> Done
Step 2: add values, simulate get -> Done
Step 2.5: implement contains, isEmpty, size -> Done
Step 3: add dimension, compare according to dimension -> Done
Step 4: Compute rect
Step 4.5: Compute draw
Step 5: implement range, and nearest

*/
public class KdTreeSimple {
    // Static constances
    private static final boolean VERTICAL = true; // vertical is the first dimension
    private static final boolean HORIZONTAL = false; // horizontal is the second dimension

    // Instance variables
    private Node root;
    private int nodeCount;
    // private Queue<Point2D> iterQueue;

    private class Node { // Make this class static to save memory?
        private Point2D key;
        private Node smaller, greater;
        private RectHV val;
        private int nodeDepth;
        private boolean dimension;

        public Node(Point2D keyInput, RectHV valInput, int nodeDepthInput) {
            key = keyInput;
            smaller = null;
            greater = null;
            val = valInput;
            nodeDepth = nodeDepthInput;
            if (nodeDepth % 2 == 0) {
                dimension = VERTICAL;
            } else {
                dimension = HORIZONTAL;
            }

            // Printout node properties for debugging
            System.out.println(
                    "Key: " + key + ", Val: " + val + ", nodeDepth: " + nodeDepth + ", dimension: " + dimension);

        }
    }

    public void draw() {
        for (Point2D pointItem : this.keys()) {
            pointItem.draw();
        }
    }

    public int size() {
        return this.nodeCount;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(Point2D key) {
        root = insert(root, key, 0);
    }

    private RectHV computeValue(Point2D key) {
        return new RectHV(key.x() - 1, key.y() - 1, key.x() + 1, key.y() + 1);
    }

    private int compareKey(Point2D thisPoint, Point2D thatPoint, int compareDepth) {
        if ((thisPoint == null) || (thatPoint == null))
            throw new IllegalArgumentException("thisPoint and thatPoint cannot be null");

        if (compareDepth % 2 == 0) {
            // Compare vertically
            if (thisPoint.x() < thatPoint.x()) {
                return -1;
            } else if (thisPoint.x() > thatPoint.x()) {
                return 1;
            } else {
                return 0;
            }
        } else { // (compareDepth %2 == 1)
            // Compare horizontally
            if (thisPoint.y() < thatPoint.y()) {
                return -1;
            } else if (thisPoint.y() > thatPoint.y()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private Node insert(Node x, Point2D p, int currentNodeDepth) {
        if (x == null) {
            nodeCount++;
            return new Node(p, computeValue(p), currentNodeDepth);
        }

        // int cmp = p.compareTo(x.key);
        int cmp = this.compareKey(p, x.key, currentNodeDepth);

        if (cmp < 0) {
            x.smaller = insert(x.smaller, p, currentNodeDepth + 1);
        } else if (cmp > 0) {
            x.greater = insert(x.greater, p, currentNodeDepth + 1);
        } else {
            ; // Do nothing
        }
        return x;
    }

    public boolean contains(Point2D key) {
        return get(key) != null;
    }

    public RectHV get(Point2D p) {
        Node x = root;
        int currentNodeDepth = 0;
        while (x != null) {
            // int cmp = key.compareTo(x.key);
            int cmp = this.compareKey(p, x.key, currentNodeDepth);
            if (cmp < 0) {
                x = x.smaller;
            } else if (cmp > 0) {
                x = x.greater;
            } else {
                return x.val;
            }
            currentNodeDepth++;
        }

        // System.out.println("No value associated with the searched key!");
        return null;
    }

    public Iterable<Point2D> keys() {
        Queue<Point2D> q = new Queue<Point2D>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Point2D> q) {
        if (x == null)
            return;
        inorder(x.smaller, q);
        q.enqueue(x.key);
        inorder(x.greater, q);
    }

    public static void main(String[] args) {
        KdTreeSimple myKdTreeSimple = new KdTreeSimple();
        Point2D samplePoint1 = new Point2D(0.7, 0.2);
        Point2D samplePoint2 = new Point2D(0.5, 0.4);
        Point2D samplePoint3 = new Point2D(0.2, 0.3);
        Point2D samplePoint4 = new Point2D(0.4, 0.7);
        Point2D samplePoint5 = new Point2D(0.9, 0.6);

        // Point2D samplePoint1 = new Point2D(7, 2);
        // Point2D samplePoint2 = new Point2D(5, 4);
        // Point2D samplePoint3 = new Point2D(2, 3);
        // Point2D samplePoint4 = new Point2D(4, 7);
        // Point2D samplePoint5 = new Point2D(9, 6);

        // Point2D samplePoint1 = new Point2D(1, 1);
        // Point2D samplePoint2 = new Point2D(-1, -1);
        // Point2D samplePoint3 = new Point2D(6, 6);
        // Point2D samplePoint4 = new Point2D(-3, -3);
        // Point2D samplePoint5 = new Point2D(0, 0);

        myKdTreeSimple.insert(samplePoint1);
        myKdTreeSimple.insert(samplePoint2);
        myKdTreeSimple.insert(samplePoint3);
        myKdTreeSimple.insert(samplePoint4);
        myKdTreeSimple.insert(samplePoint5);

        for (Object pointItem : myKdTreeSimple.keys()) {
            // pointItem = (Point2D) pointItem;
            System.out.println(((Point2D) pointItem).toString() + " " + myKdTreeSimple.get((Point2D) pointItem));
        }
        System.out.println("");

        System.out.println("Contain sample 3: " + myKdTreeSimple.contains(samplePoint3));
        System.out.println("Contain sample 4: " + myKdTreeSimple.contains(samplePoint4));
        System.out.println("Contain sample 5: " + myKdTreeSimple.contains(samplePoint5));

    }

}
