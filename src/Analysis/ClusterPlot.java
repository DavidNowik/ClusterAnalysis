package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ClusterPlot extends JPanel {

    private List<Cluster> clusters;
    private double scale = 20;        // zoom factor
    private int offsetX = 0, offsetY = 0; // panning offset
    private Point lastDrag;           // track mouse drag

    public ClusterPlot(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        int colorIndex = 0;

        for (Cluster cluster : clusters) {

            // Use the field, fallback to default if null
            if (colors != null && colors.length > 0) {
                g.setColor(colors[colorIndex % colors.length]);
            } else {
                g.setColor(Color.BLACK); // fallback color
            }
            for (DataPoint p : cluster.getPoints()) {

                double[] f = p.getFeatures();

                int x = (int) (f[0] * scale + getWidth() / 2 + offsetX);
                int y = (int) (f[1] * scale + getHeight() / 2 + offsetY);

                g.fillOval(x, y, 6, 6);
            }

            colorIndex++;
        }
    }

    private Color[] colors; // field to hold cluster colors
    public void setColors(Color[] clusterColors) {
        if (clusterColors.length >= clusters.size()) {
            this.colors = clusterColors; // assign new colors
        } else {
            // if not enough colors, generate or repeat
            this.colors = new Color[clusters.size()];
            for (int i = 0; i < clusters.size(); i++) {
                this.colors[i] = clusterColors[i % clusterColors.length];
            }
        }
    }
    public static void show(List<Cluster> clusters) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("KMeans Visualization");
            frame.setSize(1000, 900); // slightly smaller than full HD
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ClusterPlot plot = new ClusterPlot(clusters);

            // Generate distinct colors for each cluster
            Color[] customColors = new Color[clusters.size()];
            for (int i = 0; i < clusters.size(); i++) {
                float hue = i / (float) clusters.size();       // evenly spaced hues
                customColors[i] = Color.getHSBColor(hue, 0.8f, 0.8f);
            }

            plot.setColors(customColors);  // assign colors
            plot.repaint();                // ensure panel redraws with new colors

            frame.add(plot);
            frame.setVisible(true);

            plot.addMouseWheelListener(e -> {
                if (e.getPreciseWheelRotation() < 0) {
                    plot.scale *= 1.1; // zoom in
                } else {
                    plot.scale /= 1.1; // zoom out
                }
                plot.repaint();
            });
            plot.addMouseWheelListener(e -> {
                if (e.getPreciseWheelRotation() < 0) {
                    plot.scale *= 1.1;
                } else {
                    plot.scale /= 1.1;
                }
                plot.repaint();
            });

            plot.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        plot.lastDrag = e.getPoint();
                    }
                }
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    plot.lastDrag = null;
                }
            });

            plot.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent e) {
                    if (plot.lastDrag != null) {
                        plot.offsetX += e.getX() - plot.lastDrag.x;
                        plot.offsetY += e.getY() - plot.lastDrag.y;
                        plot.lastDrag = e.getPoint();
                        plot.repaint();
                    }
                }
            });


        });
    }
}