import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

/*
 * Step 1: Create a tree with only keys, simulate insert -> Done
 * Step 2: add values, simulate get 
 * Step 2.5: implement contains, isEmpty, size 
 * Step 3: add dimension, compare according to dimension -> Done
 * Step 4: Compute rect -> Done
 * Step 4.5: Compute draw
 * Step 5: implement range, and nearest
 * 
 */

public class KdTree {
    // Static constants
    private static final boolean VERTICAL = true; // vertical is the first dimension
    private static final boolean HORIZONTAL = false; // horizontal is the second dimension

    // Instance variables
    private Node root;
    private int nodeCount;

    private class Node {
        private Point2D key;
        private RectHV value;
        private Node smaller, greater, parent;
        private boolean dimension;

        public Node(Point2D keyInput, Node parentInput) {
            key = keyInput;

            smaller = null;
            greater = null;
            parent = parentInput;

            if (parent == null) {
                dimension = VERTICAL;
            } else { // if (parent != null)
                dimension = !parent.dimension;
            }

            if (parent == null) {
                value = new RectHV(0, 0, 1, 1);
            } else { // if (parent != null)
                // How to calculate xmin, ymin, xmax, ymax? -> use the value of the previous
                // node
                RectHV previousValue = parent.value;
                Point2D previousKey = parent.key;
                boolean previousDimension = parent.dimension;

                int cmp = compareKey(Node.this.key, previousKey, previousDimension);
                if (previousDimension == VERTICAL) {
                    if (cmp < 0) { // If new point is to the left of previous point: new rect have length: xmin ->
                                   // x_key, height: ymin -> ymax
                        value = new RectHV(previousValue.xmin(), previousValue.ymin(), previousKey.x(),
                                previousValue.ymax()); // format xmmin, ymin, xmax, ymax
                    } else if (cmp > 0) { // If new point is to the right of previous point: new rect have length: x_key
                                          // -> xmax, height: ymin -> ymax
                        value = new RectHV(previousKey.x(), previousValue.ymin(), previousValue.xmax(),
                                previousValue.ymax()); // format xmmin, ymin, xmax, ymax
                    } else {
                        throw new IllegalStateException("The two points can not aligned vertically");
                    }
                } else { // (previousDimension == HORIZONTAL)
                    if (cmp < 0) { // If new point is at the bottom of previous point: new rect have length: xmin
                                   // -> xmax, height: ymin -> y_key
                        value = new RectHV(previousValue.xmin(), previousValue.ymin(), previousValue.xmax(),
                                previousKey.y()); // format xmmin, ymin, xmax, ymax
                    } else if (cmp > 0) { // If new point is at the top of previous point: new rect have length: xmin ->
                                          // xmax, height: y_key -> ymax
                        value = new RectHV(previousValue.xmin(), previousKey.y(), previousValue.xmax(),
                                previousValue.ymax()); // format xmmin, ymin, xmax, ymax
                    } else {
                        throw new IllegalStateException("The two points can not aligned horizontally");
                    }
                }
            }

            // For debugging
            // if (parent != null) {
            // System.out.println("Create node: " + key + ", from parent: " + parent.key +
            // ", dimension: " + dimension
            // + ", value: " + value);
            // } else {
            // System.out.println("Create node: " + key + ", from parent: " + null + ",
            // dimension: " + dimension
            // + ", value: " + value);
            // }

        }
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return this.nodeCount;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p should not be null");
        }

        return get(p) != null;
    }

    public void draw() {
        drawInOrder(root);
    }

    private void drawInOrder(Node x) {
        if (x == null) {
            return;
        }

        drawInOrder(x.smaller);
        StdDraw.setPenRadius(0.004);
        if (x.dimension == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.key.x(), x.value.ymin(), x.key.x(), x.value.ymax());
        } else { // x.dimension == HORIZONTAL
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.value.xmin(), x.key.y(), x.value.xmax(), x.key.y());
        }
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.key.draw();
        drawInOrder(x.greater);
    }

    private RectHV get(Point2D p) {
        Node x = root;

        while (x != null) {
            int cmp = this.compareKey(p, x.key, x.dimension);

            if (cmp < 0) {
                x = x.smaller;
            } else if (cmp > 0) {
                x = x.greater;
            } else {
                return x.value;
            }
        }

        // System.out.println("No value associated with the searched key!");
        return null;
    }

    private int compareKey(Point2D thisPoint, Point2D thatPoint, boolean compareDimension) {
        if (compareDimension == VERTICAL) {
            if (thisPoint.x() > thatPoint.x()) {
                return 1;
            } else if (thisPoint.x() < thatPoint.x()) {
                return -1;
            } else { // if (thisPoint.x() == thatPoint.x())
                if (thisPoint.y() > thatPoint.y()) {
                    return 1;
                } else if (thisPoint.y() < thatPoint.y()) {
                    return -1;
                } else { // if (thisPoint.y() == thatPoint.y())
                    return 0;
                }
            }
        } else { // (compareDimension == HORIZONTAL)
            if (thisPoint.y() > thatPoint.y()) {
                return 1;
            } else if (thisPoint.y() < thatPoint.y()) {
                return -1;
            } else { // if (thisPoint.y() == thatPoint.y())
                if (thisPoint.x() > thatPoint.x()) {
                    return 1;
                } else if (thisPoint.x() < thatPoint.x()) {
                    return -1;
                } else { // if (thisPoint.x() == thatPoint.x())
                    return 0;
                }
            }
        }
    }

    public void insert(Point2D key) {
        if (key == null) {
            throw new IllegalArgumentException("key should not be null");
        }
        root = insert(root, key, null);
    }

    private Node insert(Node x, Point2D p, Node parentNode) {
        if (x == null) {
            nodeCount++;
            return new Node(p, parentNode);
        }
        // System.out.println("Node number: " + nodeCount);
        // int cmp = p.compareTo(x.key);
        int cmp;
        if (parentNode != null) {
            // System.out.println("Parent Node: " + p + ", dimension: " +
            // parentNode.dimension);
            cmp = this.compareKey(p, x.key, !parentNode.dimension);
        } else {
            // System.out.println("Parent Node: " + p + ", dimension: " + VERTICAL);
            cmp = this.compareKey(p, x.key, VERTICAL);
        }

        if (cmp < 0) {
            // System.out.println("Point: " + p + " go left of :" + x.key);
            x.smaller = insert(x.smaller, p, x);
        } else if (cmp > 0) {
            // System.out.println("Point: " + p + " go right of :" + x.key);
            x.greater = insert(x.greater, p, x);
        } else {
            ;
        }

        return x;

    }

    private Iterable<Point2D> keys() {
        Queue<Point2D> q = new Queue<Point2D>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Point2D> q) {
        if (x == null)
            return;

        inorder(x.smaller, q);
        // System.out.print(x.key + " ");
        q.enqueue(x.key);
        inorder(x.greater, q);
    }

    /*
     * To find all points contained in a given query rectangle,
     * start at the root
     * and recursively search for points in both subtrees using the following
     * pruning rule: if the query rectangle does not intersect the rectangle
     * corresponding to a node, there is no need to explore that node (or its
     * subtrees). A subtree is searched only if it might contain a point contained
     * in the query rectangle.
     */
    public Iterable<Point2D> range(RectHV rect) { //// all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException("rect should not be null");
        }

        Queue<Point2D> q = new Queue<Point2D>();
        rangeSearch(root, q, rect);
        return q;
    }

    private void rangeSearch(Node x, Queue<Point2D> q, RectHV rect) {
        if (x == null) {
            return;
        }

        // recursively check left subtrees
        rangeSearch(x.smaller, q, rect);

        // Find the desired node and add the key to queue
        if (!x.value.intersects(rect)) {
            return;
        }

        // If the point is inside the rect, add it the the queue to return
        if (rect.contains(x.key)) {
            q.enqueue(x.key);
        }

        // recursively check right subtres
        rangeSearch(x.greater, q, rect);
    }

    /*
     * To find a closest point to a given query point, start at the root and
     * recursively search in both subtrees using the following pruning rule: if the
     * closest point discovered so far is closer than the distance between the query
     * point and the rectangle corresponding to a node, there is no need to explore
     * that node (or its subtrees).
     * 
     * That is, search a node only only if it might
     * contain a point that is closer than the best one found so far. The
     * effectiveness of the pruning rule depends on quickly finding a nearby point.
     * To do this, organize the recursive method so that when there are two possible
     * subtrees to go down, you always choose the subtree that is on the same side
     * of the splitting line as the query point as the first subtree to explore—the
     * closest point found while exploring the first subtree may enable pruning of
     * the second subtree.
     */
    private double minDistance;
    private Point2D nearestPoint;

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException("p should not be null");
        }
        // System.out.println("Node traversal: null ");
        minDistance = Double.POSITIVE_INFINITY;
        nearestPoint = null;
        nearestSearch(root, p);
        return nearestPoint;
    }

    private void nearestSearch(Node x, Point2D p) {
        if (x == null)
            return;

        /*
         * if the
         * closest point discovered so far is closer than the distance between the query
         * point and the rectangle corresponding to a node, there is no need to explore
         * that node (or its subtrees).
         */
        if (minDistance < x.value.distanceTo(p)) {
            return;
        }

        // System.out.print(" -> " + x.key); //*** */

        // choose the subtree that is on the same side of the splitting line as the
        // query point

        // Get dimension at this node, use dimension to comapre the query point with the
        // node key -> choose subtree (smaller/greater)
        int cmp = this.compareKey(p, x.key, x.dimension);
        if (cmp <= 0) { // follow smaller subtree first, if equal happens: both subtrees are ok!

            // Find the node with a smallest distance
            double currentDistance = x.key.distanceTo(p);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = x.key;
            }

            // Search left subtrees recursively
            nearestSearch(x.smaller, p);

            // Search right subtrees recursively
            nearestSearch(x.greater, p);
        } else { // } if (cmp > 0) { // follow larger subtree first

            // Find the node with a smallest distance
            double currentDistance = x.key.distanceTo(p);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = x.key;
            }

            // Search right subtrees recursively
            nearestSearch(x.greater, p);

            // Search left subtrees recursively
            nearestSearch(x.smaller, p);
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // System.out.println("Hello world!");

        KdTree myKdTree = new KdTree();
        Point2D samplePoint1 = new Point2D(0.372, 0.497);
        Point2D samplePoint2 = new Point2D(0.564, 0.413);
        Point2D samplePoint3 = new Point2D(0.226, 0.577);
        Point2D samplePoint4 = new Point2D(0.144, 0.179);
        Point2D samplePoint5 = new Point2D(0.083, 0.51);
        Point2D samplePoint6 = new Point2D(0.32, 0.708);
        Point2D samplePoint7 = new Point2D(0.417, 0.362);
        Point2D samplePoint8 = new Point2D(0.862, 0.825);
        Point2D samplePoint9 = new Point2D(0.785, 0.725);
        Point2D samplePoint10 = new Point2D(0.499, 0.208);

        myKdTree.insert(samplePoint1);
        myKdTree.insert(samplePoint2);
        myKdTree.insert(samplePoint3);
        myKdTree.insert(samplePoint4);
        myKdTree.insert(samplePoint5);
        myKdTree.insert(samplePoint6);
        myKdTree.insert(samplePoint7);
        myKdTree.insert(samplePoint8);
        myKdTree.insert(samplePoint9);
        myKdTree.insert(samplePoint10);

        System.out.println("Size: " + myKdTree.size());
        // System.out.println(myKdTree.root);

        for (Object pointItem : myKdTree.keys()) {
            // pointItem = (Point2D) pointItem;
            System.out.print(((Point2D) pointItem).toString()); // + " " + myKdTree.get((Point2D) pointItem));
        }
        System.out.println("");

        System.out.println("Contain sample 0: " + myKdTree.contains(new Point2D(0.51, 0.51)));
        // System.out.println("Contain sample 3: " +
        // myKdTree.contains(samplePoint3));
        // System.out.println("Contain sample 4: " +
        // myKdTree.contains(samplePoint4));
        // System.out.println("Contain sample 5: " +
        // myKdTree.contains(samplePoint5));

        StdDraw.enableDoubleBuffering();

        // Check range function:
        RectHV rangeRect = new RectHV(0.3, 0.2, 0.6, 0.7);
        StdDraw.setPenColor(StdDraw.PINK);
        rangeRect.draw();

        System.out.print("Points in range " + rangeRect + ": ");
        for (Object rangePoint : myKdTree.range(rangeRect)) {
            System.out.print(((Point2D) rangePoint).toString() + " ");
        }
        System.out.println("");

        // Check nearest function:
        Point2D sampleNearestPoint = new Point2D(0.27, 0.5);
        Point2D nearestPoint = myKdTree.nearest(sampleNearestPoint);
        System.out.println("The point: " + nearestPoint + " is nearest to: " + sampleNearestPoint + " with distance "
                + myKdTree.minDistance);

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        StdDraw.line(sampleNearestPoint.x(), sampleNearestPoint.y(), nearestPoint.x(), nearestPoint.y());
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.02);
        sampleNearestPoint.draw();

        myKdTree.draw();
        StdDraw.show();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime); // divide by 1000000 to get milliseconds.
        System.out.println("Execution time in ms: " + duration / 1000000);

    }
}