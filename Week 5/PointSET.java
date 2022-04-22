import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointSetDataStructure;
    private SET<Point2D> pointSetInsideRectange;

    public PointSET() {
        pointSetDataStructure = new SET<Point2D>();
    } // construct an empty set of points

    public boolean isEmpty() {
        return pointSetDataStructure.isEmpty();
    } // is the set empty?

    public int size() {
        return pointSetDataStructure.size();
    } // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p should not be null");
        }

        pointSetDataStructure.add(p);
    } // add the point to the set (if it is not
      // already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p should not be null");
        }

        return pointSetDataStructure.contains(p);
    } // does the set contain point p?

    public void draw() {
        for (Point2D pointItem : pointSetDataStructure) {
            pointItem.draw();
        }
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("rect should not be null");
        }

        pointSetInsideRectange = new SET<Point2D>();

        for (Point2D pointItem : pointSetDataStructure) {
            if ((pointItem.x() >= rect.xmin()) && (pointItem.x() <= rect.xmax())
                    && (pointItem.y() >= rect.ymin()) && (pointItem.y() <= rect.ymax())) {
                pointSetInsideRectange.add(pointItem);
            }
        }

        return pointSetInsideRectange;
    } // all points that are inside the
      // rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p should not be null");
        }

        if (this.isEmpty()) {
            return null;
        }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;

        for (Point2D pointItem : pointSetDataStructure) {
            double currentDistance = pointItem.distanceTo(p);
            if (currentDistance < minDistance) {
                nearestPoint = pointItem;
                minDistance = currentDistance;
            }
        }

        return nearestPoint;
    } // a nearest neighbor in the set to point
      // p; null if the set is empty

    public static void main(String[] args) {

    } // unit testing of the methods (optional)
}
