import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdArrayIO;
import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // while (!BinaryStdIn.isEmpty()) {
        String inputString = BinaryStdIn.readString();
        String processString = inputString + inputString;

        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(inputString);

        int sortedIndexOfOriginalFirst = -1;
        StringBuilder sb = new StringBuilder();
        for (int sortedIndex = 0; sortedIndex < circularSuffixArray.length(); sortedIndex++) {
            int originalIndex = circularSuffixArray.index(sortedIndex);
            // StdOut.println(originalIndex);

            char tempChar = processString.charAt(originalIndex + circularSuffixArray.length() - 1);
            sb.append(tempChar);

            if (originalIndex == 0) {
                sortedIndexOfOriginalFirst = sortedIndex;
            }
            // break;
        }

        BinaryStdOut.write(sortedIndexOfOriginalFirst);
        BinaryStdOut.write(sb.toString());
        BinaryStdIn.close();
        BinaryStdOut.close();
        // System.exit(0);
        // StdOut.println(inputString);
        // }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int[] next = new int[t.length()];

        int R = 256;
        int N = t.length();
        char[] aux = new char[t.length()];

        // count frequencies
        int[] count = new int[R + 1];
        for (int i = 0; i < N; i++) {
            count[t.charAt(i) + 1]++;
        }

        // compute cumulates
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // // print count
        // for (int i = 0; i < R; i++) {
        // System.out.println("char: " + (char) i + "---count: " + count[i + 1]);
        // }

        // move items
        for (int i = 0; i < N; i++) {
            // StdOut.println("Move: character " + t.charAt(i) + " from t[" + i + "] to
            // aux[" + count[t.charAt(i)] + "]");
            next[count[t.charAt(i)]] = i;
            aux[count[t.charAt(i)]] = t.charAt(i);
            count[t.charAt(i)]++;
        }

        // Decode with BWT
        StringBuilder decodeStringBuilder = new StringBuilder();
        int iterIndex = first;
        int progressCount = 0;
        do {
            char currentChar = aux[iterIndex];
            decodeStringBuilder.append(currentChar);
            iterIndex = next[iterIndex];
            progressCount++;
        } while (progressCount != N);

        String decodeString = decodeStringBuilder.toString();

        // StdOut.println("i" + "\t" + "aux[i]" + "\t" + "t[i]" + "\t" + "next[i]");
        // for (int i = 0; i < N; i++) {
        // StdOut.println(i + "\t" + aux[i] + "\t" + t.charAt(i) + "\t" + next[i]);
        // }

        // StdOut.println(t);
        // StdArrayIO.print(next);
        // StdOut.println(new String(aux));
        // StdOut.println(first);
        // StdOut.println(t);
        BinaryStdOut.write(decodeString);

        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        String operation = args[0];
        // Encoding
        if (operation.equals("-")) {
            transform();
        } else if (operation.equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("operation can only be '-' or '+'");
        }
    }

}

// java BurrowsWheeler.java - < burrows/abra.txt | java
// edu.princeton.cs.algs4.HexDump 16
// java BurrowsWheeler.java - < burrows/abra.txt

// java BurrowsWheeler.java - < burrows/abra.txt | java BurrowsWheeler.java +