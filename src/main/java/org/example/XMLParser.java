package org.example;

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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries xySeries = new XYSeries("Result");
            XYSeries xySeries1 = new XYSeries("Report");
            XYSeries xySeries2 = new XYSeries("Report Abweichung");

            List<Object> coordinates = new ArrayList<>();
            for (Element result : rootNode.getChildren("Result")) {
                System.out.println(" ");
                double i = 10;
                for (Element value : result.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    Double v = Double.parseDouble(value.getAttributeValue("V"));
                    coordinates.add(scale);
                    coordinates.add(v);
                    System.out.println("Scale: "+scale+" V: "+v);

                    xySeries.add(i, v);
                    i--;
                }
            }

            for (Element report : rootNode.getChildren("Report")) {
                System.out.println(" ");
                double i = 10;
                for (Element value : report.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    Double av = Double.parseDouble(value.getAttributeValue("AV"));
                    Double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                    coordinates.add(scale);
                    coordinates.add(av);
                    coordinates.add(dev);
                    System.out.println("Scale: "+scale+" AV: "+av+" DEV: "+dev);

                    xySeries1.add(i, av);
                    xySeries2.add(i, av-dev);
                    i--;
                }
            }

            dataset.addSeries(xySeries);
            dataset.addSeries(xySeries1);
            dataset.addSeries(xySeries2);

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


            ChartFrame frame = new ChartFrame("Patient Values", chart);
            frame.pack();
            frame.setVisible(true);



        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }
}