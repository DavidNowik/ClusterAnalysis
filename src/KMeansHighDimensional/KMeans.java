package KMeansHighDimensional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {

    public static List<Cluster> run(List<DataPoint> data, int k, int iterations) {

        Random random = new Random();
        List<Cluster> clusters = new ArrayList<>();

        int dimension = data.get(0).size();

        //Step 1 - Random assignment of initial Clusters
        for (int i = 0; i < k; i++) {
            DataPoint randomPoint = data.get(random.nextInt(data.size()));
            clusters.add(new Cluster(randomPoint.getFeatures().clone()));
        }

        for (int it = 0; it < iterations; it++) {

            for (Cluster c : clusters) {
                c.clearPoints();
            }

            //Step 2 - Assignment of points to next-closest centroid
            for (DataPoint point : data) {

                Cluster bestCluster = null;
                double bestDistance = Double.MAX_VALUE;

                for (Cluster cluster : clusters) {

                    double distance = cluster.distanceTo(point);

                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestCluster = cluster;
                    }
                }

                bestCluster.addPoint(point);
            }

            //Step 3 - Repositioning of centroids
            for (Cluster cluster : clusters) {

                if (cluster.size() == 0) continue;

                double[] newCentroid = new double[dimension];

                for (DataPoint p : cluster.getPoints()) {

                    double[] features = p.getFeatures();

                    for (int d = 0; d < dimension; d++) {
                        newCentroid[d] += features[d];
                    }
                }

                for (int d = 0; d < dimension; d++) {
                    newCentroid[d] /= cluster.size();
                }

                cluster.setCentroid(newCentroid);
            }
        }

        return clusters;
    }
}