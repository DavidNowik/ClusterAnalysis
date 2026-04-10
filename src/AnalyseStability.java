import Analysis.ARI;
import Analysis.ClusterIO;
import Analysis.FileParser;
import KMeansHighDimensional.Cluster;

import java.util.List;

public class AnalyseStability {

    public static void main(String[] args){

        analyseStability("none");
        analyseStability("pca");


    }
    private static void analyseStability(String reductionTypeName){
        List<Cluster> base = ClusterIO.readClustersFromJSON("ListOfClusters1.json", "ClusterLists_pca");

        double sum = 0;
        int count = 0;

        for (int i = 1; i < 50; i++) {

            List<Cluster> current = ClusterIO.readClustersFromJSON(
                    "ListOfClusters" + i + ".json",
                    "ClusterLists_"+reductionTypeName
            );

            double ari = ARI.computeARI(base, current);
            sum += ari;
            count++;
        }

        System.out.println("Avg ARI = " + (sum / count)+ " ("+reductionTypeName+")");
    }

}
