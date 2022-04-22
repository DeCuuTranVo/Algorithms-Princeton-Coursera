import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class RedBlackTree<Key extends Comparable<Key>, Value> { // implement a binary search tree structure
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Queue<Key> q;
    private int nodeCount;
    private Node root;

    private class Node { // modify this to accommodate 2D
        private Key key;
        private Value val;
        private Node left, right;
        boolean color; // color of parent link

        public Node(Key key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.left = null;
            this.right = null;
        }
    }

    public RedBlackTree() {
        nodeCount = 0;
        root = null;
    }

    private boolean isRed(Node x) {
        if (x == null)
            return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h) {
        assert isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        assert isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        assert !isRed(h);
        assert isRed(h.left);
        assert isRed(h.right);
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else if (cmp == 0)
                return x.val;
        }
        return null;
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
        nodeCount++;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null)
            return new Node(key, val, RED);
        int cmp = key.compareTo(h.key);
        if (cmp < 0)
            h.left = put(h.left, key, val);
        else if (cmp > 0)
            h.right = put(h.right, key, val);
        else if (cmp == 0)
            h.val = val;
        if (isRed(h.right) && !isRed(h.left))
            h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left))
            h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))
            flipColors(h);

        return h;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public int size() {
        return nodeCount;
    }

    public Iterable<Key> keys() {
        q = new Queue<Key>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Key> q) {
        if (x == null)
            return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }

    public static void main(String[] args) {
        RedBlackTree myRedBlackTree = new RedBlackTree<Point2D, RectHV>();

        Point2D samplePoint1 = new Point2D(0, 0);
        RectHV sampleRect1 = new RectHV(-1, -1, 1, 1);
        myRedBlackTree.put(samplePoint1, sampleRect1);

        Point2D samplePoint2 = new Point2D(-1, 0);
        RectHV sampleRect2 = new RectHV(-2, -1, 0, 1);
        myRedBlackTree.put(samplePoint2, sampleRect2);

        Point2D samplePoint3 = new Point2D(1, 0);
        RectHV sampleRect3 = new RectHV(0, -1, 2, 1);
        myRedBlackTree.put(samplePoint3, sampleRect3);

        for (Object point : myRedBlackTree.keys()) {
            point = (Point2D) point;
            System.out.print(point.toString() + " ");
        }
        System.out.println("");
    }
}
