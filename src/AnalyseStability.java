import Analysis.*;
import KMeansHighDimensional.Cluster;

import java.util.List;

public class AnalyseStability {

    public static void main(String[] args){

    }
    public static void analyseStability(String reductionTypeName){

        List<Cluster> base = ClusterIO.readClustersFromJSON(
                "ListOfClusters1.json",
                "ClusterLists_" + reductionTypeName
        );

        double sumARI = 0;
        double sumAMI = 0;
        double sumSil = 0;
        int count = 0;

        for (int i = 1; i < 50; i++) {

            List<Cluster> current = ClusterIO.readClustersFromJSON(
                    "ListOfClusters" + i + ".json",
                    "ClusterLists_" + reductionTypeName
            );

            double ari = ARI.computeARI(base, current);
            double ami = computeAMI(base, current);
            double silhouette = Silhouette.computeSilhouette(current);

            sumARI += ari;
            sumAMI += ami;
            sumSil += silhouette;
            count++;
        }

        double avgARI = sumARI / count;
        double avgAMI = sumAMI / count;
        double avgSIL = sumSil / count;

        System.out.println(
                "Avg ARI = " + avgARI +
                        " | Avg AMI = " + avgAMI +
                        " | Avg SIL = " + avgSIL +
                        " (" + reductionTypeName + ")"
        );

        FileParser.appendAnalysisResult(
                reductionTypeName,
                avgARI,
                avgAMI,
                avgSIL
        );
        System.out.println("\tFrom File: " + CreateClusterLists.fileToRead);
    }

    public static double computeAMI(List<Cluster> a, List<Cluster> b) {
        return NMI.computeNMI(a, b);
    }

}
