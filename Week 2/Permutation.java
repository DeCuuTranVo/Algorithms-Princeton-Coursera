import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        // Number of elements to be printed
        Integer k = Integer.parseInt(args[0]);

        RandomizedQueue<String> ramdomQueue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String string = StdIn.readString();
            ramdomQueue.enqueue(string);
            // StdOut.println(string);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(ramdomQueue.dequeue());
        }

    }
}
