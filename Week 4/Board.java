import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] boardDataStructure;
    private Queue<Board> boardQueue;
    private int zeroRow;
    private int zeroCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // initialize and populate board data structure
        boardDataStructure = new int[tiles.length][tiles[0].length];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                boardDataStructure[i][j] = tiles[i][j];

                // find the position of zero tile
                if (boardDataStructure[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(boardDataStructure.length + "\n");
        // stringBuilder.append(boardDataStructure.toString());
        for (int i = 0; i < boardDataStructure.length; i++) {
            for (int j = 0; j < boardDataStructure[0].length; j++) {
                stringBuilder.append(String.format("%2d ", boardDataStructure[i][j]));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return boardDataStructure.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingCount = 0;
        for (int i = 0; i < boardDataStructure.length; i++) {
            for (int j = 0; j < boardDataStructure[0].length; j++) {
                if (boardDataStructure[i][j] == i * this.dimension() + j + 1) {
                    ;
                } else if ((i == this.dimension() - 1) && (j == this.dimension() - 1)) {
                    ;
                } else {
                    hammingCount++;
                }
            }
        }
        return hammingCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mahattanCount = 0; //

        // System.out.println("Pos\tDes\tDRow\tDCol\tPRow\tPCol\tEMah");
        for (int i = 0; i < boardDataStructure.length; i++) {
            for (int j = 0; j < boardDataStructure[0].length; j++) {
                int elementDestination = boardDataStructure[i][j] - 1;
                if (elementDestination == -1) {
                    continue; //
                }
                int elementRow = elementDestination / this.dimension();
                int elementColumn = elementDestination - elementRow * this.dimension();

                int elementMahattan = Math.abs(i - elementRow) + Math.abs(j - elementColumn);
                mahattanCount += elementMahattan;

                // System.out.print((i * this.dimension() + j + 1) + "\t");
                // System.out.print(elementDestination + 1 + "\t");
                // System.out.print(elementRow + "\t");
                // System.out.print(elementColumn + "\t");
                // System.out.print(i + "\t");
                // System.out.print(j + "\t");
                // System.out.println(elementMahattan);
            }
        }

        return mahattanCount;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }

        if (!(that instanceof Board)) {
            return false;
        }

        if (this.dimension() != ((Board) that).dimension()) {
            return false;
        }

        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.boardDataStructure[i][j] != ((Board) that).boardDataStructure[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        boardQueue = new Queue<Board>();

        // System.out.println("Zero position: " + zeroPosition[0] + ", " +
        // zeroPosition[1]);

        // For each neighbors
        if (zeroRow == 0 && zeroCol == 0) {
            // proceed with 2 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
        } else if (zeroRow == this.dimension() - 1 && zeroCol == 0) {
            // proceed with 2 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
        } else if (zeroRow == 0 && zeroCol == this.dimension() - 1) {
            // proceed with 2 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
        } else if (zeroRow == this.dimension() - 1 && zeroCol == this.dimension() - 1) {
            // proceed with 2 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
        } else if (zeroRow == 0) {
            // proceed with 3 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
        } else if (zeroRow == this.dimension() - 1) {
            // proceed with 3 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
        } else if (zeroCol == 0) {
            // proceed with 3 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);

        } else if (zeroCol == this.dimension() - 1) {
            // proceed with 3 neighbors
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);
        } else {
            // proceed with 4 neighbors
            // System.out.println("zeroRow: " + zeroRow + " zeroCol: " + zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            addElementIterable(this.boardDataStructure, zeroRow, zeroCol, zeroRow, zeroCol - 1);
        }

        // Return the queue iterator
        return boardQueue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Copy immutable matrix to a new matrix
        int[][] copiedMatrix = copyMatrix(this.boardDataStructure);
        // Swap value at that matrix

        if ((this.zeroRow == 0) && (this.zeroCol == 0)) { // If zero tile has row == 0
            // Swap [row0, col0 + 1] and [row0 +1, col0]
            swap(copiedMatrix, zeroRow + 1, zeroCol, zeroRow, zeroCol + 1);
        } else if ((this.zeroRow == 0) && (this.zeroCol == this.dimension() - 1)) {
            swap(copiedMatrix, zeroRow + 1, zeroCol, zeroRow, zeroCol - 1);
        } else if ((this.zeroRow == this.dimension() - 1) && (this.zeroCol == 0)) {
            swap(copiedMatrix, zeroRow - 1, zeroCol, zeroRow, zeroCol + 1);
        } else if ((this.zeroRow == this.dimension() - 1) && (this.zeroCol == this.dimension() - 1)) {
            swap(copiedMatrix, zeroRow - 1, zeroCol, zeroRow, zeroCol - 1);
        } else if ((this.zeroRow == 0) || (this.zeroRow == this.dimension() - 1)) {
            swap(copiedMatrix, zeroRow, zeroCol - 1, zeroRow, zeroCol + 1);
        } else if ((this.zeroCol == 0) || (this.zeroCol == this.dimension() - 1)) {
            swap(copiedMatrix, zeroRow - 1, zeroCol, zeroRow + 1, zeroCol);
        } else {
            swap(copiedMatrix, zeroRow + 1, zeroCol, zeroRow, zeroCol + 1);
        }

        // Create a new board
        Board twinBoard = new Board(copiedMatrix);
        return twinBoard;
    }

    private void addElementIterable(int[][] matrix, int row1, int col1, int row2, int col2) {
        // Copy immutable matrix to a new matrix
        int[][] copiedMatrix = copyMatrix(matrix);
        // Swap value at that matrix
        swap(copiedMatrix, row1, col1, row2, col2);
        // Create a new board
        Board iterBoard = new Board(copiedMatrix);
        // Add that board to boardQueue
        this.boardQueue.enqueue(iterBoard);
    }

    private int[][] copyMatrix(int[][] tiles) {
        int[][] resultMatrix = new int[tiles.length][tiles[0].length];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                resultMatrix[i][j] = tiles[i][j];
            }
        }

        return resultMatrix;
    }

    private void exchange(int[][] matrix, int index1, int index2) { // index1, index2 can not be zero // exchange using
                                                                    // 1D index
        // Convert 1D order to 2D position
        int elementRow1 = get2DPosition(index1)[0];
        int elementColumn1 = get2DPosition(index1)[1];
        // Convert 1D order to 2D position
        int elementRow2 = get2DPosition(index2)[0];
        int elementColumn2 = get2DPosition(index2)[1];
        // Swap values
        int temp = matrix[elementRow1][elementColumn1];
        matrix[elementRow1][elementColumn1] = matrix[elementRow2][elementColumn2];
        matrix[elementRow2][elementColumn2] = temp;
    }

    private void swap(int[][] matrix, int row1, int col1, int row2, int col2) {
        // Swap values
        int temp = matrix[row1][col1];
        matrix[row1][col1] = matrix[row2][col2];
        matrix[row2][col2] = temp;
    }

    private int[] get2DPosition(int orderNumber) {
        int elementNumber = orderNumber - 1;
        int elementRow = elementNumber / this.dimension();
        int elementCol = elementNumber - elementRow * this.dimension();

        int[] elementPosition = { elementRow, elementCol };
        return elementPosition;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] diffTiles = { { 1, 8, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        // Test constructor
        Board testBoard = new Board(tiles);
        Board diffTestBoard = new Board(diffTiles);
        // Test toString() method
        System.out.println(testBoard.toString());
        // Test hamming() method
        System.out.println(testBoard.hamming());
        // Test manhattan() method
        System.out.println(testBoard.manhattan());
        // Test isGoal() method
        System.out.println(testBoard.isGoal());
        // Test equal() method
        System.out.println(testBoard.equals((Object) diffTestBoard));
        System.out.println(testBoard.equals((Object) testBoard));
        // Test neighbors() method
        for (Board neighbor : testBoard.neighbors()) {
            System.out.println(neighbor.toString());
        }
        // Test twin() method
        System.out.println(testBoard.twin());
    }
}