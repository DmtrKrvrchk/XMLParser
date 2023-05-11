package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import javax.swing.*;
import java.awt.*;

public class ChartFabrik {
    private final String chartTitle;
    private final String xAxeLabel;
    private final String yAxeLabel;
    private final JFreeChart chart;
    private final YIntervalSeriesCollection dataset;
    private final YIntervalSeries resultYSeries;
    private final YIntervalSeries stationaerYSeries;
    private final YIntervalSeries hausGesamtYSeries;
    private final YIntervalSeries hausTeilYSeries;

    public ChartFabrik (String chartTitle, String xAxeLabel, String yAxeLabel, YIntervalSeriesCollection dataset) {
        chart = ChartFactory.createXYLineChart(
                this.chartTitle = chartTitle,
                this.xAxeLabel = xAxeLabel,
                this.yAxeLabel = yAxeLabel,
                this.dataset = dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();

        DeviationRenderer renderer = new DeviationRenderer(true, false);
        plot.setRenderer(renderer);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Referenzwerte stationärer Psychotherapiepatienten");
        comboBox.addItem("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)");
        comboBox.addItem("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)");
        comboBox.addItem("Alle Graphen");

        resultYSeries = dataset.getSeries(0);
        stationaerYSeries = dataset.getSeries(1);
        hausGesamtYSeries = dataset.getSeries(2);
        hausTeilYSeries = dataset.getSeries(3);

        comboBox.addActionListener(e -> {
            switch (comboBox.getSelectedIndex()) {
                case 0 -> {
                    dataset.removeAllSeries();
                    dataset.addSeries(resultYSeries);
                    dataset.addSeries(stationaerYSeries);
                    renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesFillPaint(1, new Color(0, 71, 171));
                }
                case 1 -> {
                    dataset.removeAllSeries();
                    dataset.addSeries(resultYSeries);
                    dataset.addSeries(hausGesamtYSeries);
                    renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesFillPaint(1, new Color(0, 71, 171));
                }
                case 2 -> {
                    dataset.removeAllSeries();
                    dataset.addSeries(resultYSeries);
                    dataset.addSeries(hausTeilYSeries);
                    renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesFillPaint(1, new Color(0, 71, 171));
                }
                default -> {
                    dataset.removeAllSeries();
                    dataset.addSeries(resultYSeries);
                    dataset.addSeries(stationaerYSeries);
                    dataset.addSeries(hausGesamtYSeries);
                    dataset.addSeries(hausTeilYSeries);
                    renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(2, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesStroke(3, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    renderer.setSeriesFillPaint(1, new Color(0, 71, 171));
                    renderer.setSeriesFillPaint(2, new Color(34, 139, 34));
                    renderer.setSeriesFillPaint(3, new Color(255, 255, 0));
                }
            }
        });

        ChartFrame frame = new ChartFrame(chartTitle, chart);
        JPanel panel = new JPanel();

        panel.add(comboBox);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}