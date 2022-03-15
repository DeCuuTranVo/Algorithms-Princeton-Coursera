
/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> { // //Slide 7, section 2.1 // Slide 77, section 2.1
    // // Slide 44,45, section 2.2//

    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that.y == this.y && that.x == this.x) {
            return Double.NEGATIVE_INFINITY;
        } else if (that.y == this.y) {
            return 0.0;
        } else if (that.x == this.x) {
            return Double.POSITIVE_INFINITY;
        } else {
            return Double.valueOf(that.y - this.y) / Double.valueOf(that.x - this.x);
        }
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) { // If different y, compare y
            return -1;
        } else if (this.y > that.y) {
            return 1;
        } else if (this.x < that.x) { // If same y, compare x
            return -1;
        } else if (this.x > that.x) {
            return 1;
        } else { // If same y and same x, return 0
            return 0;
        }
    }

    public final Comparator<Point> SLOPE_ORDER = slopeOrder();

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point q1, Point q2) {
            double slope1 = Point.this.slopeTo(q1);
            double slope2 = Point.this.slopeTo(q2);

            if (slope1 < slope2) { // slope 1 > slope 2 <=> point 1 < point 2
                return -1;
            } else if (slope1 > slope2) { // slope 1 > slope 2 <=> point 1 > point 2
                return 1;
            } else { // slope 1 == slope 2 <=> point 1 2 point 2
                return 0;
            }
        }
    }

    // /**
    // * Compares two points by the slope they make with this point.
    // * The slope is defined as in the slopeTo() method.
    // *
    // * @return the Comparator that defines this ordering on points
    // */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeOrder();
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);
        Point p3 = new Point(1, 1);
        Point p4 = new Point(1, 0);
        Point p5 = new Point(2, 1);

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 4);
        StdDraw.setYscale(0, 4);
        p1.drawTo(p2);
        p1.drawTo(p3);
        p1.drawTo(p4);
        p1.drawTo(p5);
        p1.drawTo(p1);
        StdDraw.show();

        // Test compareTo method
        System.out.println("Compare " + p1 + " to " + p2 + " : " + p1.compareTo(p2));
        System.out.println("Compare " + p1 + " to " + p3 + " : " + p1.compareTo(p3));
        System.out.println("Compare " + p1 + " to " + p4 + " : " + p1.compareTo(p4));
        System.out.println("Compare " + p1 + " to " + p5 + " : " + p1.compareTo(p5));

        System.out.println("Compare " + p5 + " to " + p1 + " : " + p5.compareTo(p1));
        System.out.println("Compare " + p5 + " to " + p2 + " : " + p5.compareTo(p2));
        System.out.println("Compare " + p5 + " to " + p3 + " : " + p5.compareTo(p3));
        System.out.println("Compare " + p5 + " to " + p4 + " : " + p5.compareTo(p4));

        // Test slopeTo method
        System.out.println("Slope from " + p1 + " to " + p2 + " is: " + p1.slopeTo(p2));
        System.out.println("Slope from " + p1 + " to " + p3 + " is: " + p1.slopeTo(p3));
        System.out.println("Slope from " + p1 + " to " + p1 + " is: " + p1.slopeTo(p1));
        System.out.println("Slope from " + p1 + " to " + p4 + " is: " + p1.slopeTo(p4));
        System.out.println("Slope from " + p1 + " to " + p5 + " is: " + p1.slopeTo(p5));
        // Test some cases of comparators
        System.out.println("Slope order object: " + p1.slopeOrder());
    }
}