import edu.princeton.cs.algs4.*;

public class BruteCollinearPoints {
    Point pointP;
    Point pointQ;
    Point pointR;
    Point pointS;
    LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
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

        pointP = points[0];
        pointR = points[1];
        pointQ = points[2];
        pointS = points[3];

        double slopePR = pointP.slopeTo(pointR);
        double slopePQ = pointP.slopeTo(pointQ);
        double slopePS = pointP.slopeTo(pointS);

        if (slopePR == slopePQ && slopePR == slopePS) {
            // compareTo to find the largest and smalles points
        }
    }

    public int numberOfSegments() { // the number of line segments
        return 0;
    }

    public LineSegment[] segments() { // the line segments
        return null; // null
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
        for (Point p : points) {
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
