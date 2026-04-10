import Analysis.FileParser;
import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.ClusterGroup;
import KMeansHighDimensional.DataPoint;
import KMeansHighDimensional.KMeans;
import Analysis.ConsoleLogger;
import PCA_and_MDS.MDS;
import PCA_and_MDS.PCA;

import java.util.ArrayList;
import java.util.List;

public class CreateClusterLists {

    public static void main(String[] args) {
        execute("none", 10, 10);
        execute("pca", 10, 5);
    }


    /**
     * Executes a K-Means clustering analysis on the dataset using an optional dimensionality
     * reduction technique.
     *
     * <p>Supported reduction types:
     * <ul>
     *     <li>"PCA" – Principal Component Analysis</li>
     *     <li>"MDS" – Multidimensional Scaling</li>
     * </ul>
     *
     * <p>The method uses the configured number of clusters (k), target dimension for
     * reduction, number of K-Means iterations, and number of repeated analyses.
     * The console output or log file will be automatically named based on the
     * reduction type, k, and target dimension for clarity.
     *
     * @param reductionType The type of dimensionality reduction to apply before clustering.
     *                      Must be either "PCA" or "MDS". Use null or empty string for no reduction.
     */
    private static void execute(String reductionType, int k, int targetDimension){

        int iterations = 50;//K-Means-spezifisch
        int NUMBER_OF_ANALYSES = 50;

        sizesIndex = 0;
        listsIndex = 0;

        if(reductionType.toLowerCase() == "none"){
            targetDimension = 147;
        }

        String logName = String.format("%s_k%d_dim%d",
                reductionType.toLowerCase(), k, targetDimension);
        ConsoleLogger.redirectConsoleOutput(logName);


        System.out.println("Executing:");
        System.out.println("k = " + k);
        System.out.println("iterations = " + iterations);
        System.out.println("NUMBER_OF_ANALYSES = " + NUMBER_OF_ANALYSES);
        System.out.println("PCA target dimension = " + targetDimension);

        for (int j = 0; j < NUMBER_OF_ANALYSES; j++) {

            List<DataPoint> data = FileParser.loadCSVorData("training_labeled.csv", true);
            List<DataPoint> workingData;
            if(reductionType.toLowerCase() == "pca"){
                System.out.println("Working with PCA");
                workingData = PCA.reduce(data, targetDimension);
            }
            else if(reductionType.toLowerCase() == "mds"){
                System.out.println("Working with MDS");
                workingData = MDS.reduce(data, targetDimension);
            }
            else {
                System.out.println("Working with no Reduction");
                workingData = data;
            }

            List<Cluster> clusters = KMeans.run(workingData, k, iterations);
            ClusterGroup group = new ClusterGroup(reductionType, j);

            String outPutSubFolder = "ClusterLists_"+reductionType;
            FileParser.writeClustersToJSON(clusters, "ListOfClusters"+(sizesIndex++)+".json", outPutSubFolder);

            for (Cluster c : clusters) {
                group.addCluster(c);
            }

            results.add(group);

            System.out.println("ATTEMPT: " + (j + 1));

            int i = 1;
            for (Cluster c : clusters) {
                System.out.println("Cluster " + i + " size: " + c.size());
                i++;
            }
            System.out.println();

        }

        String outPutSubFolder = "ClusterSizes_"+reductionType;
        FileParser.writeClusterSizesToJSON(results,  logName+(sizesIndex++)+".json", outPutSubFolder);
        results.clear();
    }
    private static int sizesIndex = 0;
    private static int listsIndex = 0;
    static List<ClusterGroup> results = new ArrayList<>();
}