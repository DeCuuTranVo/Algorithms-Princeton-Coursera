import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdArrayIO;
import java.util.Arrays;
import java.util.Iterator;
import java.lang.StringBuffer;

public class BoggleSolver {
    private AZTrieSET wordDictionary;
    private AZTrieSET boardWordSet;
    private boolean[][] marked;
    private StringBuffer stringValue;
    // private int[][][] previous;
    private BoggleBoard onFocusBoard;

    private static final int[][] directionDictionary = {
            { 0, 1 }, // right
            { 1, 1 }, // bottom right,
            { 1, 0 }, // bottom,
            { 1, -1 }, // bottom left
            { 0, -1 }, // left
            { -1, -1 }, // top left
            { -1, 0 }, // top
            { -1, 1 } // top right
    };

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // Create and populate word dictionary
        wordDictionary = new AZTrieSET();
        for (String word : dictionary) {
            wordDictionary.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.boardWordSet = new AZTrieSET();
        this.onFocusBoard = board;
        this.stringValue = new StringBuffer();
        this.marked = new boolean[this.onFocusBoard.rows()][this.onFocusBoard.cols()];
        // this.previous = new
        // int[this.onFocusBoard.rows()][this.onFocusBoard.cols()][2];

        // StdOut.println(onFocusBoard.toString());

        // Loop through all the position in the board
        outerLoop: for (int i = 0; i < this.onFocusBoard.rows(); i++) {
            for (int j = 0; j < this.onFocusBoard.cols(); j++) {
                depthFirstSearch(i, j);
                // break outerLoop;
            }
        }
        // System.out.println("boardWordSet size: " + boardWordSet.size());
        // for (String wordString : boardWordSet) {
        // // System.out.println("+++++++++++++++++++++++++++++++");
        // System.out.println(wordString + " ");
        // }

        return this.boardWordSet;
    }

    private void depthFirstSearch(int argRow, int argCol) {
        this.marked[argRow][argCol] = true;

        char currentLetter = this.onFocusBoard.getLetter(argRow, argCol);

        // Consider "Q" as "QU" and append to stringValue
        if (currentLetter == 'Q') {
            this.stringValue.append("QU");
        } else {
            this.stringValue.append(currentLetter);
        }

        String currentString = this.stringValue.toString();
        // when the current path corresponds to a string that is not a prefix of any
        // word in the dictionary, there is no need to expand the path further
        if (currentString.length() >= 3) {
            // System.out.println("current String at BoggleSolver: " + currentString);
            Iterator<String> iter = this.wordDictionary.keysWithPrefix(currentString).iterator();
            // System.out.println("return queue at BoggleSolver is non null: " +
            // iter.hasNext());
            // System.exit(0);
            if (!iter.hasNext()) {
                this.marked[argRow][argCol] = false;

                if ((this.stringValue.length() >= 2) && (this.stringValue.charAt(this.stringValue.length() - 2) == 'Q')
                        && (this.stringValue.charAt(this.stringValue.length() - 1) == 'U')) {
                    this.stringValue.deleteCharAt(this.stringValue.length() - 1);
                    this.stringValue.deleteCharAt(this.stringValue.length() - 1);
                } else {
                    this.stringValue.deleteCharAt(this.stringValue.length() - 1);
                }
                return;
            }
        }

        // A valid word must contain at least three letters
        if (currentString.length() >= 3) {
            // If the words is in dictionary -> it is valid -> save to returned trie set
            if (this.wordDictionary.contains(currentString)) {
                boardWordSet.add(currentString);
            }
        }

        // StdArrayIO.print(this.marked);
        for (int adjacentPosition : this.adjacentPositions(argRow, argCol)) {
            int[] adjacentRowCol = this.positionToRowCol(adjacentPosition);
            int adjacentRow = adjacentRowCol[0];
            int adjacentCol = adjacentRowCol[1];

            // System.out.print("adjacentRow: " + adjacentRow);
            // System.out.println(", adjacentCol: " + adjacentCol);
            if (!this.marked[adjacentRow][adjacentCol]) {
                depthFirstSearch(adjacentRow, adjacentCol);
                // previous[adjacentRow][adjacentCol][0] = argRow;
                // previous[adjacentRow][adjacentCol][1] = argCol;
            }
        }
        // System.exit(0);

        this.marked[argRow][argCol] = false;
        if ((this.stringValue.length() >= 2) && (this.stringValue.charAt(this.stringValue.length() - 2) == 'Q')
                && (this.stringValue.charAt(this.stringValue.length() - 1) == 'U')) {
            this.stringValue.deleteCharAt(this.stringValue.length() - 1);
            this.stringValue.deleteCharAt(this.stringValue.length() - 1);
        } else {
            this.stringValue.deleteCharAt(this.stringValue.length() - 1);
        }
    }

    private Queue<Integer> adjacentPositions(int currentRow, int currentCol) {
        Queue<Integer> adjacentQueue = new Queue<Integer>();
        for (int i = 0; i < directionDictionary.length; i++) {
            int nextRow = currentRow + directionDictionary[i][0];
            int nextCol = currentCol + directionDictionary[i][1];

            if (!(((0 <= nextRow) && (nextRow < onFocusBoard.rows()))
                    && ((0 <= nextCol) && (nextCol < onFocusBoard.cols())))) {
                continue;
            } else {
                // StdOut.println("nextRow: " + nextRow + ", nextCol: " + nextCol);
            }

            // if (this.marked[nextRow][nextCol]) {
            // continue;
            // }

            int nextPosition = this.rowColToPosition(nextRow, nextCol);
            // StdOut.println("nextPosition: " + nextPosition);
            // StdArrayIO.print(positionToRowCol(nextPosition));
            adjacentQueue.enqueue(nextPosition);
        }
        return adjacentQueue;
    }

    private int rowColToPosition(int argRow, int argCol) {
        return argRow * this.onFocusBoard.cols() + argCol;
    }

    private int[] positionToRowCol(int argPosition) {
        int row = argPosition / this.onFocusBoard.cols();
        int col = argPosition % this.onFocusBoard.cols();
        // StdOut.println("row: " + row + " col: " + col);
        int[] result = { row, col };
        return result;
    }

    private int rowColToPosition(BoggleBoard argBoard, int argRow, int argCol) {
        return argRow * argBoard.rows() + argCol;
    }

    private int[] positionToRowCol(BoggleBoard argBoard, int argPosition) {
        int row = argPosition / argBoard.rows();
        int col = argPosition % argBoard.rows();
        int[] result = { row, col };
        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word must not be null");
        }

        int wordLength = word.length();

        if (!this.wordDictionary.contains(word)) {
            return 0;
        } else {
            if (wordLength <= 2) {
                return 0;
            } else if (wordLength <= 4) {
                return 1;
            } else if (wordLength == 5) {
                return 2;
            } else if (wordLength == 6) {
                return 3;
            } else if (wordLength == 7) {
                return 5;
            } else {
                return 11;
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        // StdOut.println(board.rows() + "x" + board.cols());

        // long startTime = System.currentTimeMillis();
        // for (int i = 0; i < 10; i++) {
        // BoggleBoard board = new BoggleBoard();

        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        // }
        // long stopTime = System.currentTimeMillis();
        // System.out.println((float) (stopTime - startTime) / 1000 + "s");
    }

    /////////////////////////////////// Utility functions
    /////////////////////////////////// /////////////////////////////////////////////
    private boolean[][] copy2DArray(boolean[][] sourceArray) {
        if (sourceArray == null) {
            throw new IllegalArgumentException("sourceArray must not be null!");
        }
        boolean[][] copiedArray = new boolean[sourceArray.length][sourceArray[0].length];
        for (int rowIndex = 0; rowIndex < sourceArray.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sourceArray[0].length; columnIndex++) {
                copiedArray[rowIndex][columnIndex] = sourceArray[rowIndex][columnIndex];
            }
        }
        return copiedArray;
    }

    private int[][] copy2DArray(int[][] sourceArray) {
        if (sourceArray == null) {
            throw new IllegalArgumentException("sourceArray must not be null!");
        }
        int[][] copiedArray = new int[sourceArray.length][sourceArray[0].length];
        for (int rowIndex = 0; rowIndex < sourceArray.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sourceArray[0].length; columnIndex++) {
                copiedArray[rowIndex][columnIndex] = sourceArray[rowIndex][columnIndex];
            }
        }
        return copiedArray;
    }

    private int[][][] copy3DArray(int[][][] sourceArray) {
        if (sourceArray == null) {
            throw new IllegalArgumentException("sourceArray must not be null!");
        }
        int[][][] copiedArray = new int[sourceArray.length][sourceArray[0].length][sourceArray[0][0].length];
        for (int rowIndex = 0; rowIndex < sourceArray.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sourceArray[0].length; columnIndex++) {
                for (int channelIndex = 0; channelIndex < sourceArray[0][0].length; channelIndex++) {
                    copiedArray[rowIndex][columnIndex][channelIndex] = sourceArray[rowIndex][columnIndex][channelIndex];
                }
            }
        }
        return copiedArray;
    }

    private void print3DArray(int[][][] sourceArray) {
        if (sourceArray == null) {
            throw new IllegalArgumentException("sourceArray must not be null!");
        }

        for (int rowIndex = 0; rowIndex < sourceArray.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < sourceArray[0].length; columnIndex++) {
                // StdOut.print("(");
                for (int channelIndex = 0; channelIndex < sourceArray[0][0].length; channelIndex++) {
                    if (channelIndex % 2 == 0) {
                        StdOut.printf("(%2d,", sourceArray[rowIndex][columnIndex][channelIndex]);
                    } else {
                        StdOut.printf("%2d)", sourceArray[rowIndex][columnIndex][channelIndex]);
                    }

                }
                StdOut.print("\t\t");
            }
            StdOut.println();
        }
    }
}
