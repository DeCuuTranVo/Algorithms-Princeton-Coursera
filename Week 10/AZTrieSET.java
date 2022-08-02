import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class AZTrieSET implements Iterable<String> {
    private static final int R = 26; // extended ASCII

    private Node root; // root of trie
    private int n; // number of keys in trie

    // R-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes an empty set of strings.
     */
    public AZTrieSET() {
    }

    /**
     * Returns the number of strings in the set.
     * 
     * @return the number of strings in the set
     */
    public int size() {
        return n;
    }

    /**
     * Is the set empty?
     * 
     * @return {@code true} if the set is empty, and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public static int getR() {
        return R;
    }

    /**
     * Adds the key to the set if it is not already present.
     * 
     * @param key the key to add
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void add(String key) {
        if (key == null)
            throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null)
            x = new Node();
        if (d == key.length()) {
            if (!x.isString)
                n++;
            x.isString = true;
        } else {
            char c = key.charAt(d);
            // int charPosition = this.convertCharToPosition(c);
            // x.next[charPosition] = add(x.next[charPosition], key, d + 1);
            x.next[c - 65] = add(x.next[c - 65], key, d + 1);
        }
        return x;
    }

    public boolean contains(String key) {
        if (key == null)
            throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null)
            return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null)
            return null;
        if (d == key.length())
            return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d + 1);
    }

    private int convertCharToPosition(char c) {
        return c - 65;
    }

    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    private StringBuilder previousPrefix;
    private Queue<String> previousResults;

    public Iterable<String> keysWithPrefix(String prefix) {
        // System.out.println("current String at AZTrieSET: " + prefix);
        if (previousPrefix == null) {
            previousPrefix = new StringBuilder();
        }

        if (previousResults == null) {
            previousResults = new Queue<String>();
        }

        Queue<String> results = new Queue<String>();
        // System.out.println("previousPrefix: " + previousPrefix.toString() + ",prefix:
        // " + prefix + ", boolean: "
        // + prefix.startsWith(previousPrefix.toString()));

        if (previousPrefix.length() > 0 && prefix.startsWith(previousPrefix.toString())) {
            for (String item : previousResults) {
                if (item.startsWith(prefix)) {
                    results.enqueue(item);
                }
            }

            previousPrefix = new StringBuilder(prefix);
            previousResults = results;
            // System.out.println("return queue at AZTrieSET 127 is non null: " +
            // results.iterator().hasNext());

            // for (String key : results) {
            // StdOut.print(key + " ");
            // }
            // StdOut.println();
            return results;
        }

        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        // System.out.println("return queue at AZTrieSET 133 is non null: " +
        // results.iterator().hasNext());
        // for (String key : results) {
        // StdOut.print(key + " ");
        // }
        // StdOut.println();
        // collectNonRecursive(x, new StringBuilder(prefix), results);
        previousPrefix = new StringBuilder(prefix);
        previousResults = results;
        return results;
    }

    // private void exploitConsecutiveQuery() {
    // // if the previous String is less than the current String by one character
    // and
    // // is the same for previous charaters

    // if ((previousPrefix.length() + 1 == prefix.length())
    // && (previousPrefix.substring(0, previousPrefix.length() -
    // 1).equals(prefix.toString()))) {

    // for (String item : previousResults) {
    // if (item.startsWith(prefix.toString())) {
    // results.enqueue(item);
    // }
    // }
    // previousPrefix = new StringBuilder(prefix);
    // previousResults = results;
    // return;
    // }
    // }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        // if ((previousPrefix.length() != 0) &&
        // (prefix.toString().startsWith(previousPrefix.toString()))) {
        // StdOut.print(previousPrefix + " ");
        // StdOut.println(prefix);
        // // Do as normal
        // }

        if (x == null)
            return;
        if (x.isString)
            results.enqueue(prefix.toString());
        for (char c = 65; c < R + 65; c++) {
            // previousPrefix = new StringBuilder(prefix);
            prefix.append(c);
            collect(x.next[c - 65], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private void collectNonRecursive(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) {
            throw new IllegalArgumentException("x must not be null");
        }
        if (prefix == null) {
            throw new IllegalArgumentException("prefix must not be null");
        }
        if (results == null) {
            throw new IllegalArgumentException("results must not be null");
        }

        Stack<Node> nodeStack = new Stack<Node>();
        Stack<Character> charStack = new Stack<Character>();
        Stack<Integer> depthStack = new Stack<Integer>();
        StringBuilder fromXPrefix = new StringBuilder();
        nodeStack.push(x);
        depthStack.push(0);
        charStack.push(' ');

        while (!nodeStack.isEmpty()) {
            Node currentNode = nodeStack.pop();
            int currentDepth = depthStack.pop();
            char currentChar = charStack.pop();

            StdOut.println();
            System.out.println(currentDepth + " " +
                    +fromXPrefix.length());

            if (currentDepth > fromXPrefix.length()) {
                fromXPrefix.append(currentChar);
            } else {
                // fromXPrefix.delete(currentDepth, fromXPrefix.length());
                fromXPrefix.deleteCharAt(fromXPrefix.length() - 1);
            }
            System.out.println(currentDepth + " " +
                    +fromXPrefix.length());

            System.out.println(currentChar + " " + currentDepth + " " + prefix.toString() + fromXPrefix.toString());

            for (char c = 65; c < R + 65; c++) {
                // previousPrefix = new StringBuilder(prefix);
                Node newNode = currentNode.next[c - 65];
                int newDepth = currentDepth + 1;
                if (newNode != null) {
                    depthStack.push(newDepth);
                    charStack.push(c);
                    nodeStack.push(newNode);
                } else {
                    continue;
                }
            }
        }
        // System.exit(0);
    }

    public static void main(String[] args) {
        AZTrieSET set = new AZTrieSET();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();

        // StdOut.println(set.convertCharToPosition('A'));
        // StdOut.println(set.convertCharToPosition('Z'));
        // String key = "ABCDE";
        // set.add(key);
        // StdOut.println(set.contains(key));
        // StdOut.println(set.contains("ABCD"));

        // set.add("ABCDF");
        // set.add("ABCDG");
        // set.add("ABCDH");
        // set.add("ABCDI");
        for (int i = 0; i < dictionary.length; i++) {
            set.add(dictionary[i]);
        }

        StdOut.println(set.size());

        // Print current content of AZTrieSET
        // int count = 0;
        // for (String setItem : set) {
        // StdOut.print(setItem + " ");
        // count += 1;

        // if (count == 10) {
        // break;
        // }
        // }
        // StdOut.println();

        // Find prefix
        String prefix = "AB";
        for (String key : set.keysWithPrefix(prefix)) {
            StdOut.print(key + " ");
        }
        StdOut.println();

        // System.out.println("previousPrefix: " + set.previousPrefix);
        // System.out.print("previousResults: ");
        // for (String key : set.previousResults) {
        // StdOut.print(key + " ");
        // }
        // StdOut.println();

        String newPrefix = "ABA";
        for (String key : set.keysWithPrefix(newPrefix)) {
            StdOut.print(key + " ");
        }
        StdOut.println();

        // StdOut.println(set.root);
        // for (int i = 0; i < AZTrieSET.R; i++) {
        // StdOut.print(set.root.next[i] + " ");
        // }
        // ;
        // StdOut.println();

        // StdOut.println(set.root.next[set.convertCharToPosition(key.charAt(0))]);
        // for (int i = 0; i < AZTrieSET.R; i++) {
        // StdOut.print(set.root.next[set.convertCharToPosition(key.charAt(0))].next[i]
        // + " ");
        // }
        // ;
        // StdOut.println();

        // while (!StdIn.isEmpty()) {
        // String key = StdIn.readString();
        // set.add(key);
        // }

        // // print results
        // if (set.size() < 100) {
        // StdOut.println("keys(\"\"):");
        // for (String key : set) {
        // StdOut.println(key);
        // }
        // StdOut.println();
        // }

        // StdOut.println("keysWithPrefix(\"shor\"):");
        // for (String s : set.keysWithPrefix("shor"))
        // StdOut.println(s);
        // StdOut.println();

        // StdOut.println("keysWithPrefix(\"shortening\"):");
        // for (String s : set.keysWithPrefix("shortening"))
        // StdOut.println(s);
        // StdOut.println();
    }
}