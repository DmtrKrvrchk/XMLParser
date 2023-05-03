package org.example;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


/**
 *
 */
public class XMLParser {

    public static void main(String[] args) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("C:\\Users\\d.krivoruchko\\IdeaProjects\\XMLParser\\values.xml");
        try {

            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
            YIntervalSeries xySeries = new YIntervalSeries("Result");
            YIntervalSeries xySeries1 = new YIntervalSeries("Report1");
            YIntervalSeries xySeries2 = new YIntervalSeries("Report2");
            YIntervalSeries xySeries3 = new YIntervalSeries("Report3");


            String[] scaleValue = {"SOZB", "SOZU", "A_P", "SELB", "INT", "WOHL", "PSB", "PHO", "DEP", "SOM"};


            List<Object> coordinates = new ArrayList<>();
            for (Element result : rootNode.getChildren("Result")) {
                System.out.println(" ");
                double i = 9;
                for (Element value : result.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    Double v = Double.parseDouble(value.getAttributeValue("V"));
                    coordinates.add(scale);
                    coordinates.add(v);
                    System.out.println("Scale: "+scale+" V: "+v);


                    xySeries.add(i, v, 0, 0);
                    i--;
                }
            }



            for (Element report : rootNode.getChildren("Report")) {
                System.out.println(" ");
                double i = 9;
                for (Element value : report.getChildren("Value")) {

                    String scale = value.getAttributeValue("Scale");
                    Double av = Double.parseDouble(value.getAttributeValue("AV"));
                    Double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                    coordinates.add(scale);
                    coordinates.add(av);
                    coordinates.add(dev);
                    System.out.println("Scale: "+scale+" AV: "+av+" DEV: "+dev);

                    if (report.getAttributeValue("title").equals("Referenzwerte stationärer Psychotherapiepatienten")) {
                        xySeries1.add(i, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)")) {
                        xySeries2.add(i, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)")) {
                        xySeries3.add(i, av, av-dev, av+dev);
                    }
                    i--;
                }
            }

            dataset.addSeries(xySeries);
            dataset.addSeries(xySeries1);
            dataset.addSeries(xySeries2);
            dataset.addSeries(xySeries3);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Patient Values",
                    "Scale",
                    "Value",
                    dataset,
                    PlotOrientation.HORIZONTAL,
                    false,
                    true,
                    false
            );

            XYPlot plot = (XYPlot) chart.getPlot();


            DeviationRenderer renderer = new DeviationRenderer(true, false);
            renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesStroke(2, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesStroke(3, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            renderer.setSeriesFillPaint(1, new Color(34, 139, 34));
            renderer.setSeriesFillPaint(2, new Color(0, 71, 171));
            renderer.setSeriesFillPaint(3, new Color(136, 8, 8));
            plot.setRenderer(renderer);

            SymbolAxis rangeAxis = new SymbolAxis("Value", scaleValue);
            rangeAxis.setTickUnit(new NumberTickUnit(1));
            rangeAxis.setRange(0,9);
            plot.setRangeAxis(rangeAxis);

            ChartFrame frame = new ChartFrame("Patient Values", chart);
            frame.pack();
            frame.setVisible(true);


        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }
}