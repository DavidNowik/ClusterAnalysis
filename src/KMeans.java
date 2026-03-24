import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {

    public static List<Point> centroids = new ArrayList<>();

    public static void run(List<Point> points, int k, int iterations) {

        Random random = new Random();

        // zufällige Start-Zentren
        for (int i = 0; i < k; i++) {
            Point p = points.get(random.nextInt(points.size()));
            centroids.add(new Point(p.x, p.y));
        }

        for (int it = 0; it < iterations; it++) {

            // Punkte dem nächsten Zentrum zuweisen
            for (Point p : points) {

                double minDist = Double.MAX_VALUE;
                int bestCluster = 0;

                for (int i = 0; i < centroids.size(); i++) {

                    double dist = Point.distance(p, centroids.get(i));

                    if (dist < minDist) {
                        minDist = dist;
                        bestCluster = i;
                    }
                }

                p.cluster = bestCluster;
            }

            // neue Zentren berechnen
            for (int i = 0; i < k; i++) {

                double sumX = 0;
                double sumY = 0;
                int count = 0;

                for (Point p : points) {

                    if (p.cluster == i) {
                        sumX += p.x;
                        sumY += p.y;
                        count++;
                    }
                }

                if (count > 0) {
                    centroids.get(i).x = sumX / count;
                    centroids.get(i).y = sumY / count;
                }
            }
        }
    }
}