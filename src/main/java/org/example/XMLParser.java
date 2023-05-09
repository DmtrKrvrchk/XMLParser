package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import javax.swing.*;


public class XMLParser {

    public static void main(String[] args) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("C:\\Users\\d.krivoruchko\\IdeaProjects\\XMLParser\\values.xml");

        try {
            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            String[] scalesName = new String[10];

            YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
            YIntervalSeries resultYSeries = new YIntervalSeries("Result");
            YIntervalSeries stationaerYSeries = new YIntervalSeries("Referenzwerte stationärer Psychotherapiepatienten");
            YIntervalSeries hausGesamtYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)");
            YIntervalSeries hausTeilYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)");

            for (Element result : rootNode.getChildren("Result")) {
                int yWert = 9;
                for (Element value : result.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    scalesName[yWert] = scale;
                    Double v = Double.parseDouble(value.getAttributeValue("V"));
                    resultYSeries.add(yWert, v, 0, 0);
                    yWert--;
                }
            }

            for (Element report : rootNode.getChildren("Report")) {
                int yWert = 9;
                for (Element value : report.getChildren("Value")) {
                    Double av = Double.parseDouble(value.getAttributeValue("AV"));
                    Double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                    if (report.getAttributeValue("title").equals("Referenzwerte stationärer Psychotherapiepatienten")) {
                        stationaerYSeries.add(yWert, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)")) {
                        hausGesamtYSeries.add(yWert, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)")) {
                        hausTeilYSeries.add(yWert, av, av-dev, av+dev);
                    }
                    yWert--;
                }
            }

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Patient Values",
                    "Value",
                    "Scale",
                    dataset,
                    PlotOrientation.HORIZONTAL,
                    false,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();

            DeviationRenderer renderer = new DeviationRenderer(true, false);
            plot.setRenderer(renderer);

            JComboBox<String> comboBox = new JComboBox<>();
            for (Element report : rootNode.getChildren("Report")) {
                comboBox.addItem(report.getAttributeValue("title"));
            }
            comboBox.addItem("Alle Graphen");

            comboBox.addActionListener(e -> {
                switch (comboBox.getSelectedIndex()) {
                    case 0 -> {
                        dataset.removeAllSeries();
                        dataset.addSeries(resultYSeries);
                        dataset.addSeries(stationaerYSeries);
                        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesFillPaint(1, new Color(136, 8, 8));
                    }
                    case 1 -> {
                        dataset.removeAllSeries();
                        dataset.addSeries(resultYSeries);
                        dataset.addSeries(hausGesamtYSeries);
                        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesFillPaint(1, new Color(136, 8, 8));
                    }
                    case 2 -> {
                        dataset.removeAllSeries();
                        dataset.addSeries(resultYSeries);
                        dataset.addSeries(hausTeilYSeries);
                        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesFillPaint(1, new Color(136, 8, 8));
                    }
                    default -> {
                        dataset.removeAllSeries();
                        dataset.addSeries(resultYSeries);
                        dataset.addSeries(stationaerYSeries);
                        dataset.addSeries(hausGesamtYSeries);
                        dataset.addSeries(hausTeilYSeries);
                        renderer.setSeriesFillPaint(0, new Color(0, 71, 171));
                        renderer.setSeriesFillPaint(1, new Color(136, 8, 8));
                        renderer.setSeriesFillPaint(2, new Color(34, 139, 34));
                        renderer.setSeriesFillPaint(3, new Color(255, 255, 0));
                        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(2, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        renderer.setSeriesStroke(3, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    }
                }
            });

            SymbolAxis rangeAxis = new SymbolAxis("Scale", scalesName);
            rangeAxis.setTickUnit(new NumberTickUnit(1));
            rangeAxis.setRange(0,9);
            plot.setRangeAxis(rangeAxis);

            ChartFrame frame = new ChartFrame("Patient Values", chart);
            JPanel panel = new JPanel();

            panel.add(comboBox);
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);

        } catch (IOException | JDOMException exception) {
            exception.printStackTrace();
        }
    }
}