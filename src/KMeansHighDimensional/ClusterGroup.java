package KMeansHighDimensional;

import java.util.ArrayList;
import java.util.List;

public class ClusterGroup {
    //For Serialisation and python reading purposes

    private String reductionType;
    private int attempt;
    private List<Integer> clusterSizes;

    public ClusterGroup(String reductionType, int attempt) {
        this.reductionType = reductionType;
        this.attempt = attempt;
        this.clusterSizes = new ArrayList<>();
    }

    public void addCluster(Cluster cluster) {
        clusterSizes.add(cluster.size());
    }

    public String getReductionType() {
        return reductionType;
    }

    public int getAttempt() {
        return attempt;
    }

    public List<Integer> getClusterSizes() {
        return clusterSizes;
    }
}