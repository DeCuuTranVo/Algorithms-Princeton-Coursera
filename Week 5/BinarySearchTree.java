import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class BinarySearchTree<Key extends Comparable<Key>, Value> { // implement a binary search tree structure
    private Node root;

    private class Node { // modify this to accommodate 2D
        private Key key;
        private Value val;
        private Node left, right;
        private int count;

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
        }
    }

    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else // (cmp == 0)
                return x.val;
        }
        return null;
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null)
            return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            x.left = put(x.left, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else // if (cmp == 0)
            x.val = val;

        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        return x.count;
    }

    public Iterable<Key> keys() {
        Queue<Key> q = new Queue<Key>();
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
        BinarySearchTree myBinarySearchTree = new BinarySearchTree<Point2D, RectHV>();
        Point2D samplePoint1 = new Point2D(0, 0);
        RectHV sampleRect1 = new RectHV(-1, -1, 1, 1);
        myBinarySearchTree.put(samplePoint1, sampleRect1);

        Point2D samplePoint2 = new Point2D(-1, 0);
        RectHV sampleRect2 = new RectHV(-2, -1, 0, 1);
        myBinarySearchTree.put(samplePoint2, sampleRect2);

        Point2D samplePoint3 = new Point2D(1, 0);
        RectHV sampleRect3 = new RectHV(0, -1, 2, 1);
        myBinarySearchTree.put(samplePoint3, sampleRect3);

        for (Object pointItem : myBinarySearchTree.keys()) {
            pointItem = (Point2D) pointItem;
            System.out.print(pointItem.toString() + " ");
        }
        System.out.println("");

        System.out.println("Test Node:");
        System.out.println(myBinarySearchTree.root.key);
        System.out.println(myBinarySearchTree.root.val);
        System.out.println(myBinarySearchTree.root.left);
        System.out.println(myBinarySearchTree.root.right);
        System.out.println(myBinarySearchTree.root.left.key);
        System.out.println(myBinarySearchTree.root.right.key);
        System.out.println(myBinarySearchTree.root.count);
    }
}
