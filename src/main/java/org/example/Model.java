package org.example;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import java.io.File;
import java.io.IOException;

/**
 * Class to define a model-component
 * @author d.krivoruchko
 */
public class Model {
    /** A dataset from XML-file to build a graph **/
    private YIntervalSeriesCollection dataset;

    /** A list of data with XML-Tag "Result" **/
    private YIntervalSeries resultYSeries;

    /** A list of data with XML-Tag "Referenzwerte stationärer Psychotherapiepatienten" with its deviation **/
    private YIntervalSeries stationaerYSeries;

    /** A list of data with XML-Tag "Referenzwerte von Hausarztpatienten (Gesamtstichprobe)" with its deviation **/
    private YIntervalSeries hausGesamtYSeries;

    /** A list of data with XML-Tag "Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)" with its deviation **/
    private YIntervalSeries hausTeilYSeries;

    /**
     * Constructor to create a model-object
     * @param xmlFile XML-file needed to generate a dataset
     */
    public Model(File xmlFile) {
        setDataset(xmlFile);
    }

    /**
     * Method to set the dataset
     * @param xmlFile XML-file needed to set a dateset
     */
    public void setDataset(File xmlFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            dataset = new YIntervalSeriesCollection();
            resultYSeries = new YIntervalSeries("Result");

            rootNode.getChildren("Result").forEach(result -> result.getChildren("Value").forEach(value -> {
                double v = Double.parseDouble(value.getAttributeValue("V"));
                resultYSeries.add(result.indexOf(value) / 2, v, 0, 0);
            }));

            stationaerYSeries = new YIntervalSeries("Referenzwerte stationärer Psychotherapiepatienten");
            hausGesamtYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)");
            hausTeilYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)");
            rootNode.getChildren("Report").forEach(report -> report.getChildren("Value").forEach(value -> {
                double av = Double.parseDouble(value.getAttributeValue("AV"));
                double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                if (report.getAttributeValue("title").equals("Referenzwerte stationärer Psychotherapiepatienten")) {
                    stationaerYSeries.add(report.indexOf(value) /2, av, av-dev, av+dev);
                } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)")) {
                    hausGesamtYSeries.add(report.indexOf(value)/2, av, av-dev, av+dev);
                } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)")) {
                    hausTeilYSeries.add(report.indexOf(value)/2, av, av-dev, av+dev);
                }
            }));

            dataset.addSeries(resultYSeries);
            dataset.addSeries(stationaerYSeries);
            dataset.addSeries(hausGesamtYSeries);
            dataset.addSeries(hausTeilYSeries);

        } catch (IOException | JDOMException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Method to get the dataset
     * @return the dateset used to create a model-object
     */
    public YIntervalSeriesCollection getDataset() {
        return dataset;
    }
}