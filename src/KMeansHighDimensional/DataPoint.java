package KMeansHighDimensional;

import java.util.Arrays;

public class DataPoint {
    private double[] features;
    private int label;

    public DataPoint(double[] features, boolean hasLabel) {

        if (hasLabel) {
            this.features = new double[features.length - 1];

            for (int i = 0; i < features.length - 1; i++) {
                this.features[i] = features[i];
            }

            label = (int) features[features.length - 1];
        } else {
            this.features = features;
            autogenerateLabel();
        }
    }
    public DataPoint(double[] features, int label) {
        this.features = new double[features.length - 1];
        for (int i = 0; i < features.length - 1; i++) {
            this.features[i] = features[i];
        }
        this.label = label;
    }
    private static int labelIndex = 0;
    private void autogenerateLabel(){
        label = labelIndex++;
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
    public String getFeaturesString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < features.length; i++) {
            sb.append(features[i]);
            if (i < features.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}