import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture currentPicture;
    private double[][] currentEnergyMatrix;
    private double[][] sumWeightTo;
    private int[][][] lastVertexTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture cannot be null");
        }

        this.currentPicture = new Picture(picture);
        this.currentEnergyMatrix = null;

        // System.out.println("currentMatrix shapes: [" +
        // this.currentEnergyMatrix.length + "x"
        // + this.currentEnergyMatrix[0].length + "]");
    }

    // current picture
    public Picture picture() {
        Picture returnPicture = new Picture(this.currentPicture);
        return returnPicture;
    }

    // width of current picture
    public int width() {
        return currentPicture.width();
    }

    // height of current picture
    public int height() {
        return currentPicture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // Safety check
        if (x < 0 || x > this.currentPicture.width() - 1 || y < 0 || y > this.currentPicture.height() - 1) {
            throw new IllegalArgumentException("x and y must be in matrix dimensions' range");
        }

        // Energy of pixels at image border is defined to be 1000
        if ((x == 0) || (y == 0) || (x == currentPicture.width() - 1) || (y == currentPicture.height() - 1)) {
            return 1000.0;
        }

        // Get RGB values of current pixel
        // Color centralPixelColor = currentPicture.get(x, y); // column: x, row: y
        Color rightPixelColor = currentPicture.get(x + 1, y); // column: x + 1, row: y
        Color leftPixelColor = currentPicture.get(x - 1, y); // column: x - 1, row: y
        Color bottomPixelColor = currentPicture.get(x, y + 1); // column: x, row: y + 1
        Color topPixelColor = currentPicture.get(x, y - 1); // column: x, row: y - 1

        // // Calculate x-oriented gradient
        // Compute in R channel
        int xGradientRed = rightPixelColor.getRed() - leftPixelColor.getRed(); //

        // Compute in G channel
        int xGradientGreen = rightPixelColor.getGreen() - leftPixelColor.getGreen();

        // Compute in B channel
        int xGradientBlue = rightPixelColor.getBlue() - leftPixelColor.getBlue();

        int deltaXSquare = xGradientRed * xGradientRed + xGradientGreen * xGradientGreen
                + xGradientBlue * xGradientBlue;

        // // Calculate y-oriented gradient
        // Compute in R channel
        int yGradientRed = bottomPixelColor.getRed() - topPixelColor.getRed();

        // Compute in G channel
        int yGradientGreen = bottomPixelColor.getGreen() - topPixelColor.getGreen();

        // Compute in B channel
        int yGradientBlue = bottomPixelColor.getBlue() - topPixelColor.getBlue();

        // //Calculate combined energy and take square root
        int deltaYSquare = yGradientRed * yGradientRed + yGradientGreen * yGradientGreen
                + yGradientBlue * yGradientBlue;

        double pixelEnergy = Math.sqrt((double) deltaXSquare + (double) deltaYSquare);

        // Print out for debugging
        // System.out.println("At pixel (" + x + "," + y + "):");
        // System.out.println("deltaXSquare: " + deltaXSquare);
        // System.out.println("deltaYSquare: " + deltaYSquare);

        // System.out.println("pixelEnergy: " + pixelEnergy);
        // System.out.println("");
        return pixelEnergy;

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // Transpose image
        this.transposePicture();

        // Call findVerticalSeam()
        int[] horizontalSeam = findVerticalSeam();

        // Transpose image back
        this.transposePicture();

        return horizontalSeam;
    }

    private void transposePicture() {
        Picture tranposedPicture = new Picture(this.currentPicture.height(), this.currentPicture.width());
        for (int row = 0; row < tranposedPicture.height(); row++) {
            for (int col = 0; col < tranposedPicture.width(); col++) {
                Color tempColor = this.currentPicture.get(row, col);
                tranposedPicture.set(col, row, tempColor);
            }
        }
        this.currentPicture = tranposedPicture;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] verticalSeam = new int[this.currentPicture.height()];
        this.currentEnergyMatrix = computeEnergyMatrix();

        this.sumWeightTo = new double[this.currentPicture.height()][this.currentPicture.width()];
        // (x,y): 0 -> x; 1 -> y
        this.lastVertexTo = new int[this.currentPicture.height()][this.currentPicture.width()][2];

        // Initialize values for topological shortest path's data structures
        for (int row = 0; row < this.currentPicture.height(); row++) {
            for (int col = 0; col < this.currentPicture.width(); col++) {
                this.sumWeightTo[row][col] = 0; // -1001 * this.currentPicture.height();
                this.lastVertexTo[row][col][0] = Integer.MIN_VALUE;
                this.lastVertexTo[row][col][1] = Integer.MIN_VALUE;
            }
        }

        // following topological order
        for (int row = 0; row < this.currentPicture.height(); row++) {
            for (int col = 0; col < this.currentPicture.width(); col++) {
                if (row == this.currentPicture.height() - 1) {
                    continue;
                } else {
                    if (row == 0) {
                        this.sumWeightTo[row][col] = this.currentEnergyMatrix[row][col];
                        this.lastVertexTo[row][col][0] = -1;
                        this.lastVertexTo[row][col][1] = -1;
                    }
                    // relax edges from every vertex
                    if (col == 0) {
                        // consider (row + 1, col) and (row + 1, col + 1)
                        relax(row, col, row + 1, col);
                        if (this.currentPicture.width() == 1) {
                            ;
                        } else {
                            relax(row, col, row + 1, col + 1);
                        }
                    } else if (col == this.currentPicture.width() - 1) {
                        // consider (row + 1, col - 1) and (row + 1, col)
                        relax(row, col, row + 1, col - 1);
                        relax(row, col, row + 1, col);
                    } else {
                        // consider (row + 1, col - 1) and (row + 1, col) and (row + 1, col + 1)
                        relax(row, col, row + 1, col - 1);
                        relax(row, col, row + 1, col);
                        relax(row, col, row + 1, col + 1);
                    }
                }
            }
        }

        // printEnergyMatrix();
        // printSumWeightTo();
        // printLastVetexTo();

        // Search through the last row of this.sumWeightTo -> find the smallest one
        double minSumWeight = Double.POSITIVE_INFINITY;
        int[] seamVertexAtBottom = { this.currentPicture.height() - 1, -1 };
        for (int col = 0; col < this.currentPicture.width(); col++) {
            if (this.sumWeightTo[this.currentPicture.height() - 1][col] < minSumWeight) {
                minSumWeight = this.sumWeightTo[this.currentPicture.height() - 1][col];
                seamVertexAtBottom[1] = col;
            }
        }

        // StdOut.println("seamVertexAtBottom: (" + seamVertexAtBottom[0] + "," +
        // seamVertexAtBottom[1] + ")");

        int rowConsider = seamVertexAtBottom[0];
        int colConsider = seamVertexAtBottom[1];
        // traceback the line from the bottom vertex to find the seam
        while (rowConsider >= 0) {
            // StdOut.println("vertexTraceBack: (" + rowConsider + "," + colConsider + ")");
            verticalSeam[rowConsider] = colConsider;
            // StdOut.println("(" + colConsider + ", " + rowConsider + ")");
            if (rowConsider == 0) {
                break;
            }
            int tempRowConsider = this.lastVertexTo[rowConsider][colConsider][0];
            int tempColConsider = this.lastVertexTo[rowConsider][colConsider][1];
            rowConsider = tempRowConsider;
            colConsider = tempColConsider;
        }

        // Clear computed results
        this.currentEnergyMatrix = null;
        this.sumWeightTo = null;
        this.lastVertexTo = null;

        // Print verticalSeam for checking
        // for (int row = 0; row < verticalSeam.length; row++) {
        // StdOut.print(verticalSeam[row] + " ");
        // }
        // StdOut.println("");

        // Return
        return verticalSeam;
    }

    private void relax(int thisRow, int thisCol, int thatRow, int thatCol) {
        // StdOut.print("Relax: ");
        // StdOut.print("from (" + thisRow + "," + thisCol + ") to (" + thatRow + "," +
        // thatCol + ") :");
        // StdOut.print(this.sumWeightTo[thisRow][thisCol] +
        // " + " + this.currentEnergyMatrix[thatRow][thatCol]);
        // StdOut.print(" -> ");
        // StdOut.println(this.sumWeightTo[thatRow][thatCol]);

        // If first visit : check the lastVertexTo[][][] array or if the relax condition
        // is satisfied -> relax that edge
        if ((this.lastVertexTo[thatRow][thatCol][0] == Integer.MIN_VALUE) || (this.sumWeightTo[thisRow][thisCol]
                + this.currentEnergyMatrix[thatRow][thatCol] < this.sumWeightTo[thatRow][thatCol])) {
            // StdOut.println("True");
            this.sumWeightTo[thatRow][thatCol] = this.sumWeightTo[thisRow][thisCol]
                    + this.currentEnergyMatrix[thatRow][thatCol];
            this.lastVertexTo[thatRow][thatCol][0] = thisRow;
            this.lastVertexTo[thatRow][thatCol][1] = thisCol;
        }
    }

    private double[][] computeEnergyMatrix() {
        double[][] energyMatrix = new double[this.currentPicture.height()][this.currentPicture.width()];

        for (int row = 0; row < this.currentPicture.height(); row++) {
            for (int col = 0; col < this.currentPicture.width(); col++) {
                energyMatrix[row][col] = this.energy(col, row);
            }
        }
        return energyMatrix;
    }

    private void printEnergyMatrix() {
        StdOut.println("Values of energyMatrix:");
        for (int row = 0; row < this.currentEnergyMatrix.length; row++) {
            for (int col = 0; col < this.currentEnergyMatrix[0].length; col++)
                StdOut.printf("%9.2f ", this.currentEnergyMatrix[row][col]);
            StdOut.println();
        }
    }

    private void printSumWeightTo() {
        StdOut.println("Values of sumWeightTo:");
        for (int row = 0; row < this.sumWeightTo.length; row++) {
            for (int col = 0; col < this.sumWeightTo[0].length; col++) {
                StdOut.printf("%9.2f ", this.sumWeightTo[row][col]);
            }
            StdOut.println();
        }
    }

    private void printLastVetexTo() {
        StdOut.println("Values of lastVertexTo:");
        for (int row = 0; row < this.lastVertexTo.length; row++) {
            for (int col = 0; col < this.lastVertexTo[0].length; col++) {
                StdOut.printf("(%d,%d)\t", this.lastVertexTo[row][col][0], this.lastVertexTo[row][col][1]);
            }
            StdOut.println();
        }
    }

    private void showPicture() {
        this.currentPicture.show();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // Safety check
        if (seam == null) {
            throw new IllegalArgumentException("seam must not be null");
        }

        if (this.currentPicture.height() <= 1) {
            throw new IllegalArgumentException("picture must be higher than 1");
        }
        if (seam.length != currentPicture.width()) {
            throw new IllegalArgumentException("seam length must be equal to current picture width");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= this.currentPicture.height()) {
                throw new IllegalArgumentException("seam element's index should be between 0 and height of picture");
            }

            if ((i != seam.length - 1) && (Math.abs(seam[i] - seam[i + 1]) > 1)) {
                throw new IllegalArgumentException("two adjacent entries must not differ by more than 1");
            }
        }

        transposePicture();
        removeVerticalSeam(seam);
        transposePicture();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // Safety check
        if (seam == null) {
            throw new IllegalArgumentException("seam must not be null");
        }
        if (this.currentPicture.width() <= 1) {
            throw new IllegalArgumentException("picture must be wider than 1");
        }
        if (seam.length != currentPicture.height()) {
            throw new IllegalArgumentException("seam length must be equal to current picture height");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= this.currentPicture.width()) {
                throw new IllegalArgumentException("seam element's index should be between 0 and the width of picture");
            }

            if ((i != seam.length - 1) && (Math.abs(seam[i] - seam[i + 1]) > 1)) {
                throw new IllegalArgumentException("two adjacent entries must not differ by more than 1");
            }
        }

        // Create new picture
        Picture removedPicture = new Picture(this.currentPicture.width() - 1, this.currentPicture.height());

        // Copy all pixels except the ones in seam[]
        for (int row = 0; row < removedPicture.height(); row++) {
            for (int col = 0; col < removedPicture.width(); col++) {
                Color tempColor;
                if (col >= seam[row]) {
                    tempColor = this.currentPicture.get(col + 1, row);
                } else {
                    tempColor = this.currentPicture.get(col, row);
                }

                removedPicture.set(col, row, tempColor);
            }
        }

        // reassign this.currentPicture
        this.currentPicture = removedPicture;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);
        sc.computeEnergyMatrix();
        sc.printEnergyMatrix();
        // StdOut.printf("Printing energy calculated for each pixel.\n");
        // for (int row = 0; row < sc.height(); row++) {
        // for (int col = 0; col < sc.width(); col++)
        // StdOut.printf("%9.2f ", sc.energy(col, row));
        // StdOut.println();
        // }

        // sc.findVerticalSeam();

        // Test transposePicture
        // sc.showPicture();
        // sc.transposePicture();
        // sc.showPicture();

        // For debug findVerticalSeam
        // javac PrintSeams.java
        // java PrintSeams seam/6x5.png
    }

}