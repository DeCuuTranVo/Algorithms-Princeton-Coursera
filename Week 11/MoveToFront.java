import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class MoveToFront {
    private static LinkedList<Character> orderedSequence;

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        orderedSequence = new LinkedList<Character>();
        // Initialize move-to-front data structure
        for (int i = 0; i < 256; i++) {
            MoveToFront.orderedSequence.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char elementChar = BinaryStdIn.readChar();
            char charIndex = (char) orderedSequence.indexOf(elementChar);
            BinaryStdOut.write(charIndex);

            Character charOfIndex = orderedSequence.remove(charIndex);
            orderedSequence.addFirst(charOfIndex);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        orderedSequence = new LinkedList<Character>();
        // Initialize move-to-front data structure
        for (int i = 0; i < 256; i++) {
            MoveToFront.orderedSequence.add((char) i);
        }

        while (!BinaryStdIn.isEmpty()) {
            char byteCharacterReadIn = BinaryStdIn.readChar();
            char correspondingCharaterInSequence = orderedSequence.remove(byteCharacterReadIn);
            // StdOut.print(correspondingCharaterInSequence);
            BinaryStdOut.write(correspondingCharaterInSequence);
            orderedSequence.addFirst(correspondingCharaterInSequence);

        }
        // StdOut.println("");
        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String operation = args[0];
        // Encoding
        if (operation.equals("-")) {
            encode();
        } else if (operation.equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("operation can only be '-' or '+'");
        }
    }
}

// java MoveToFront.java - < burrows/abra.txt | java
// edu.princeton.cs.algs4.HexDump 16
// java MoveToFront.java - < burrows/abra.txt | java MoveToFront.java +
