package KMeansSimple;

import java.util.ArrayList;
import java.util.List;

public class MainKMeansSimple {

    public static void main(String[] args) {

        List<Point> points = new ArrayList<>();

        // Cluster 1
        points.add(new Point(1, 2));
        points.add(new Point(2, 1));
        points.add(new Point(1.5, 1.8));
        points.add(new Point(2.2, 1.7));

        // Cluster 2
        points.add(new Point(8, 9));
        points.add(new Point(9, 8));
        points.add(new Point(8.5, 9.1));
        points.add(new Point(9.2, 8.7));

        // Cluster 3
        points.add(new Point(4, 7));
        points.add(new Point(4.5, 6.8));
        points.add(new Point(5, 7.5));

        int k = 2;
        int iterations = 20;

        KMeans.run(points, k, iterations);

        System.out.println("Ergebnis:");

        for (Point p : points) {
            System.out.println("(" + p.x + "," + p.y + ") -> Cluster " + p.cluster);
        }
    }
}