package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NMI {

    public static double computeNMI(List<Cluster> a, List<Cluster> b) {

        Map<Integer, Integer> labelA = buildMap(a);
        Map<Integer, Integer> labelB = buildMap(b);

        List<Integer> points = new ArrayList<>(labelA.keySet());
        int n = points.size();

        // Count occurrences
        Map<Integer, Integer> countA = new HashMap<>();
        Map<Integer, Integer> countB = new HashMap<>();
        Map<String, Integer> countAB = new HashMap<>();

        for (int p : points) {

            int ca = labelA.get(p);
            int cb = labelB.get(p);

            countA.put(ca, countA.getOrDefault(ca, 0) + 1);
            countB.put(cb, countB.getOrDefault(cb, 0) + 1);

            String key = ca + "_" + cb;
            countAB.put(key, countAB.getOrDefault(key, 0) + 1);
        }

        double mi = 0.0;

        for (String key : countAB.keySet()) {

            String[] parts = key.split("_");
            int ca = Integer.parseInt(parts[0]);
            int cb = Integer.parseInt(parts[1]);

            double pab = countAB.get(key) / (double) n;
            double pa = countA.get(ca) / (double) n;
            double pb = countB.get(cb) / (double) n;

            mi += pab * Math.log(pab / (pa * pb));
        }

        double ha = entropy(countA, n);
        double hb = entropy(countB, n);

        return mi / Math.sqrt(ha * hb);
    }

    private static double entropy(Map<Integer, Integer> counts, int n) {

        double h = 0.0;

        for (int c : counts.values()) {
            double p = c / (double) n;
            h -= p * Math.log(p);
        }

        return h;
    }

    private static Map<Integer, Integer> buildMap(List<Cluster> clusters) {

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < clusters.size(); i++) {
            for (DataPoint p : clusters.get(i).getPoints()) {
                map.put(p.getLabel(), i);
            }
        }

        return map;
    }
}