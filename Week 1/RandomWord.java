import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
            String cur_string = "";
            String champion = "";
            int counter = 0;
            while (!StdIn.isEmpty()) {
                cur_string = StdIn.readString();
                String[] arr = cur_string.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    counter += 1;
                    if (StdRandom.bernoulli(1.0/counter)) {
                        champion = arr[i];
                    }
                    
                }
    
            }
            StdOut.println(champion);     
    }
}
