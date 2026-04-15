package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import java.util.*;

public class ARI {

    public static double computeARI(List<Cluster> a, List<Cluster> b) {

        //Speichert Punkt-Label -> ClusterZugehörigkeit
        Map<Integer, Integer> labelA = buildMap(a);
        Map<Integer, Integer> labelB = buildMap(b);

        List<Integer> points = new ArrayList<>(labelA.keySet());

        int sameSame = 0;
        int sameDiff = 0;
        int diffSame = 0;
        int diffDiff = 0;

        int n = points.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int p1 = points.get(i);
                int p2 = points.get(j);

                boolean aSame = labelA.get(p1).equals(labelA.get(p2));
                boolean bSame = labelB.get(p1).equals(labelB.get(p2));

                if (aSame && bSame) sameSame++;
                else if (aSame) sameDiff++;
                else if (bSame) diffSame++;
                else diffDiff++;
            }
        }

        double numerator = sameSame + diffDiff;
        double denominator = sameSame + sameDiff + diffSame + diffDiff;

        return numerator / denominator;
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