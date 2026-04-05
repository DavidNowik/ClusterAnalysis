package KMeansHighDimensional;

import java.util.Arrays;

public class DataPoint {
    private double[] features;
    private int label;

    public DataPoint(double[] features, int label) {
        this.features = features;
        this.label = label;
    }

    public double[] getFeatures() {
        return features;
    }

    public int getLabel() {
        return label;
    }

    public int size(){
        return features.length;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "features=" + Arrays.toString(features) +
                ", label=" + label +
                '}';
    }
    public double distanceTo(DataPoint other) {
        double[] a = this.features;
        double[] b = other.getFeatures();

        if (a.length != b.length) {
            throw new IllegalArgumentException("Points must have the same dimensionality.");
        }

        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}