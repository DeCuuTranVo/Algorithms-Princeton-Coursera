import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdArrayIO;

public class CircularSuffixArray {
    private String stringDataStructure;
    private CircularSuffix[] suffixArrayDataStructure;

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int offsetIndex;
        private int length;
        private String stringReference;

        public CircularSuffix(int intputOffset, int inputLength, String inputString) {
            CircularSuffix.this.offsetIndex = intputOffset;
            CircularSuffix.this.length = inputLength;
            CircularSuffix.this.stringReference = inputString;
        }

        public int getLength() {
            return length;
        }

        public char charAt(int i) {
            return stringReference.charAt(offsetIndex + i);
        }

        public int compareTo(CircularSuffix other) {
            for (int i = 0; i < this.length && i < other.length; i++) {
                char thisChar = this.stringReference.charAt(this.offsetIndex + i);
                char otherChar = other.stringReference.charAt(other.offsetIndex + i);
                if (thisChar != otherChar) {
                    if (thisChar < otherChar) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }

            if (this.length < other.length) {
                return -1;
            } else if (this.length > other.length) {
                return 1;
            } else {
                return 0;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = offsetIndex; i < offsetIndex + this.length; i++) {
                sb.append(stringReference.charAt(i));
            }
            return sb.toString();
        }
    }

    private String select(int i) {
        if (i < 0 || i >= suffixArrayDataStructure.length)
            throw new IllegalArgumentException();
        return suffixArrayDataStructure[i].toString();
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s must not be null");
        }

        // this.suffixArrayDataStructure = (CircularSuffix[]) new Object[s.length()];
        this.suffixArrayDataStructure = new CircularSuffix[s.length()];
        this.stringDataStructure = s.concat(s);
        // StdOut.println(this.stringDataStructure);

        // StdOut.println(s.length());
        // StdOut.println(this.stringDataStructure.length());

        // System.exit(0);
        for (int i = 0; i < s.length(); i++) {
            this.suffixArrayDataStructure[i] = new CircularSuffix(i, s.length(), this.stringDataStructure);
        }

        Arrays.sort(suffixArrayDataStructure);

        // for (int i = 0; i < this.suffixArrayDataStructure.length; i++) {
        // StdOut.println(suffixArrayDataStructure[i]);
        // }

        // StdOut.println(suffixArrayDataStructure.length);

    }

    // length of s
    public int length() {
        return this.suffixArrayDataStructure.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= this.length()) {
            throw new java.lang.IllegalArgumentException("index out of range");
        }

        return this.suffixArrayDataStructure[i].offsetIndex;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        String s = "ABRACADABRA!";
        CircularSuffixArray myCircularSuffixArray = new CircularSuffixArray(s);

        // StdOut.println("Length: " + myCircularSuffixArray.length());

        StdOut.println("  i ind select");
        StdOut.println("---------------------------");

        for (int i = 0; i < myCircularSuffixArray.length(); i++) {
            int originalIndex = myCircularSuffixArray.index(i);
            String ithString = "\"" + myCircularSuffixArray.suffixArrayDataStructure[i].toString() + "\"";
            StdOut.printf("%3d %3d %s\n", i, originalIndex, ithString);
        }
    }

    // Step 1: Construct the data structure and populate elements
    // Step 2: Sort suffix
}