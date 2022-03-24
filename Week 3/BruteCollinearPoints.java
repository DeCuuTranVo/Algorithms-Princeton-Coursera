import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Font;

public class BruteCollinearPoints {
    // private static final Point[] pointsFromInput;
    private Point[] pointsInFocus;
    private ResizingArrayQueue<LineSegment> lineSegments;
    private LineSegment[] returnLineSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        // Check the input
        if (points == null) {
            throw new IllegalArgumentException("Argument to the constructor is null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("A point in the array is null");
            }
        }

        for (int i = 0; i < points.length; i++) {
            Point samplePoint = points[i];
            for (int j = i + 1; j < points.length; j++) {
                if (samplePoint.compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Two point with the same coordinates");
                }
            }
        }

        this.lineSegments = new ResizingArrayQueue<LineSegment>();
        this.pointsInFocus = new Point[4];
        // System.out.println("Input Ok!");

        for (int i = 0; i < points.length; i++) { // Choose first point
            for (int j = i + 1; j < points.length; j++) { // Choose second point
                for (int k = j + 1; k < points.length; k++) { // Choose third point
                    for (int l = k + 1; l < points.length; l++) { // Choose fourth point
                        // Assign the points
                        pointsInFocus[0] = points[i];
                        pointsInFocus[1] = points[j];
                        pointsInFocus[2] = points[k];
                        pointsInFocus[3] = points[l];

                        // Calculate the slope
                        double slope01 = pointsInFocus[0].slopeTo(pointsInFocus[1]);
                        double slope02 = pointsInFocus[0].slopeTo(pointsInFocus[2]);
                        double slope03 = pointsInFocus[0].slopeTo(pointsInFocus[3]);

                        // Check if the points stand in the same line
                        if (slope01 == slope02 && slope01 == slope03) {
                            // Calculate the largest and smallest points
                            int smallestIndex = 0;
                            int largestIndex = 0;
                            for (int m = 0; m < pointsInFocus.length; m++) {

                                if (pointsInFocus[m].compareTo(pointsInFocus[smallestIndex]) < 0) {
                                    smallestIndex = m;
                                }
                                if (pointsInFocus[m].compareTo(pointsInFocus[largestIndex]) > 0) {
                                    largestIndex = m;
                                }

                                // if (pointsInFocus[m].compareTo(pointsInFocus[smallestIndex]) == 0) {
                                // throw new IllegalStateException("Wrong logic");
                                // }
                                // if (pointsInFocus[m].compareTo(pointsInFocus[largestIndex]) == 0) {
                                // throw new IllegalStateException("Wrong logic");
                                // }
                            }

                            // if (smallestIndex == largestIndex) {
                            // System.out.println("smallestIndex: " + smallestIndex);
                            // System.out.println("largestIndex: " + largestIndex);
                            // for (int m = 0; m < pointsInFocus.length; m++) {
                            // System.out.print(pointsInFocus[m] + " ");
                            // }
                            // System.out.println("");
                            // }
                            // Create a line segment of largest and smallest points
                            LineSegment segment = new LineSegment(pointsInFocus[smallestIndex],
                                    pointsInFocus[largestIndex]);

                            lineSegments.enqueue(segment);

                            // for (int m = 0; m < pointsInFocus.length; m++) {
                            // System.out.print(pointsInFocus[m] + " ");
                            // }
                            // System.out.println("");

                        } else {
                            ;
                        }
                    }
                }
            }
        }

        returnLineSegments = null;
    }

    public int numberOfSegments() { // the number of line segments
        // return returnLineSegments.length;
        return lineSegments.size();
    }

    public LineSegment[] segments() { // the line segments
        // return returnLineSegments; // null

        // Create an array of segments
        int counter = 0;
        returnLineSegments = new LineSegment[lineSegments.size()];
        for (LineSegment item : lineSegments) {
            this.returnLineSegments[counter] = item;
            counter++;
        }

        return returnLineSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        // StdDraw.setPenRadius(0.01);

        // Font font = new Font("Arial", Font.BOLD, 10);
        // StdDraw.setFont(font);
        for (Point p : points) {
            // StdDraw.text(p.getX() + 2000, p.getY() + 1000, "(" + p.getX() + "," +
            // p.getY() + ")");
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
