package core;

// import java.util.Comparator;

public class Point implements Comparable<Point> {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;

    }
    @Override
    public int compareTo(Point o) {
        if (this.x == o.x && this.y == o.y) {
            return 0;
        }
        return 1;
    }
}
