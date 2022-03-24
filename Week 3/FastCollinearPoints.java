import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Font;
import java.util.Arrays;
import java.util.Iterator;

public class FastCollinearPoints {
    private Point[] pointStorage;
    private ResizingArrayQueue<Integer> startPointsIndexes;
    private ResizingArrayQueue<Integer> endPointsIndexes;
    private ResizingArrayQueue<Point> largestPointsInSegments;
    private ResizingArrayQueue<Point> smallestPointsInSegments;
    private ResizingArrayQueue<LineSegment> lineSegments;
    private LineSegment[] returnLineSegments;

    public FastCollinearPoints(Point[] points) {
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

        // Populate the point storage array
        pointStorage = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointStorage[i] = points[i];
        }

        this.startPointsIndexes = new ResizingArrayQueue<Integer>();
        this.endPointsIndexes = new ResizingArrayQueue<Integer>();
        this.largestPointsInSegments = new ResizingArrayQueue<Point>();
        this.smallestPointsInSegments = new ResizingArrayQueue<Point>();
        this.lineSegments = new ResizingArrayQueue<LineSegment>();

        // for each points:
        for (int i = 0; i < points.length; i++) {
            // Sort point storage array
            Arrays.sort(pointStorage, points[i].slopeOrder());

            // calculate the index of triplet of points that start and stop with the same
            // slope
            for (int j = 0; j < pointStorage.length - 2; j++) {
                double referenceSlope = points[i].slopeTo(pointStorage[j]);
                if ((startPointsIndexes.size() == endPointsIndexes.size())
                        && (referenceSlope == points[i].slopeTo(pointStorage[j + 1]))
                        && (referenceSlope == points[i].slopeTo(pointStorage[j + 2]))) {
                    startPointsIndexes.enqueue(j);
                }

                if ((startPointsIndexes.size() == endPointsIndexes.size() + 1)
                        && (j == pointStorage.length - 3)
                        && (referenceSlope == points[i].slopeTo(pointStorage[j + 1]))
                        && (referenceSlope == points[i].slopeTo(pointStorage[j + 2]))) {
                    endPointsIndexes.enqueue(pointStorage.length - 1);
                }

                if ((startPointsIndexes.size() == endPointsIndexes.size() + 1)
                        && (referenceSlope == points[i].slopeTo(pointStorage[j + 1]))
                        && (referenceSlope != points[i].slopeTo(pointStorage[j + 2]))) { //
                    endPointsIndexes.enqueue(j + 1);
                }
            }

            assert startPointsIndexes.size() == endPointsIndexes.size();

            // calculate largest point - smallest points in those segments
            for (int j = startPointsIndexes.size() - 1; j >= 0; j--) {
                int startIndex = startPointsIndexes.dequeue();
                int endIndex = endPointsIndexes.dequeue();

                Point maxPoint = pointStorage[0];
                Point minPoint = pointStorage[0];
                for (int k = startIndex; k <= endIndex; k++) {
                    if (pointStorage[k].compareTo(maxPoint) > 0) {
                        maxPoint = pointStorage[k];
                    }

                    if (pointStorage[k].compareTo(minPoint) < 0) {
                        minPoint = pointStorage[k];
                    }
                }

                // After findout largest points and smallest points, store them for repetition
                // checking
                assert largestPointsInSegments.size() == smallestPointsInSegments.size();
                boolean repeatedFlag = false;

                Iterator<Point> iterLargestPoints = largestPointsInSegments.iterator();
                Iterator<Point> iterSmallestPoints = smallestPointsInSegments.iterator();

                while (iterSmallestPoints.hasNext() && iterLargestPoints.hasNext()) {
                    Point maxPointStored = iterLargestPoints.next();
                    Point minPointStored = iterSmallestPoints.next();

                    if ((maxPointStored.compareTo(maxPoint) == 0) && (minPointStored.compareTo(minPoint) == 0)) {
                        repeatedFlag = true;
                    }
                }

                // System.out.println(repeatedFlag);
                // toggle repeated flag, save the points

                if (repeatedFlag == false) {
                    smallestPointsInSegments.enqueue(minPoint);
                    largestPointsInSegments.enqueue(maxPoint);
                } else {
                    // System.out.println("RepeatedFlag == true");
                    repeatedFlag = false;
                }

            }

            // // Print out information for debugging
            // for (int j = 0; j < pointStorage.length; j++) {
            // System.out.print(pointStorage[j] + " ");
            // }
            // System.out.println("");
            // for (int j = 0; j < pointStorage.length; j++) {
            // System.out.print(points[i].slopeTo(pointStorage[j]) + " ");
            // }
            // System.out.println("");
            // for (int startIndex : startPoints) {
            // System.out.print(startIndex + " ");
            // }
            // System.out.println("");
            // for (int endIndex : endPoints) {
            // System.out.print(endIndex + " ");
            // }
            // System.out.println("");

            // System.out.println("Line segments: ");
            // for (LineSegment toBePrintLineSegment : this.lineSegments) {
            // System.out.println(toBePrintLineSegment);
            // }
            // System.out.println("");
        }
        Iterator<Point> iterGlobalLargestPoints = largestPointsInSegments.iterator();
        Iterator<Point> iterGlobalSmallestPoints = smallestPointsInSegments.iterator();

        while (iterGlobalSmallestPoints.hasNext() && iterGlobalLargestPoints.hasNext()) {
            Point maxPointStoredGlobal = iterGlobalLargestPoints.next();
            Point minPointStoredGlobal = iterGlobalSmallestPoints.next();

            LineSegment toBeAddSegment = new LineSegment(minPointStoredGlobal, maxPointStoredGlobal);
            this.lineSegments.enqueue(toBeAddSegment);
        }
        // break;
    }

    // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return this.lineSegments.size();
    }
    // the number of line segments

    public LineSegment[] segments() {
        // Create an array of segments to return
        int counter = 0;
        returnLineSegments = new LineSegment[lineSegments.size()];
        for (LineSegment item : lineSegments) {
            this.returnLineSegments[counter] = item;
            counter++;
        }

        return this.returnLineSegments;
    } // the line segments

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}