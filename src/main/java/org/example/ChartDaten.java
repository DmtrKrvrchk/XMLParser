package org.example;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import java.io.File;
import java.io.IOException;

public class ChartDaten {
    private YIntervalSeriesCollection dataset;
    private YIntervalSeries resultYSeries;
    private YIntervalSeries stationaerYSeries;
    private YIntervalSeries hausGesamtYSeries;
    private YIntervalSeries hausTeilYSeries;
    private SAXBuilder builder;
    private Document document;
    private Element rootNode;

    private final File dateiPath = new File("src/main/resources/values.xml");

    public ChartDaten() {
        dataset = setDataset(this.dateiPath);
    }

    public YIntervalSeriesCollection setDataset(File xmlFile) {
        builder = new SAXBuilder();
        try {
            document = builder.build(xmlFile);
            rootNode = document.getRootElement();
            dataset = new YIntervalSeriesCollection();
            resultYSeries = new YIntervalSeries("Result");
            rootNode.getChildren("Result").forEach(result -> {
                result.getChildren("Value").forEach(value -> {
                    double v = Double.parseDouble(value.getAttributeValue("V"));
                    resultYSeries.add(result.indexOf(value) / 2, v, 0, 0);
                });
            });

            stationaerYSeries = new YIntervalSeries("Referenzwerte stationärer Psychotherapiepatienten");
            hausGesamtYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)");
            hausTeilYSeries = new YIntervalSeries("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)");
            rootNode.getChildren("Report").forEach(report -> {
                report.getChildren("Value").forEach(value -> {
                    double av = Double.parseDouble(value.getAttributeValue("AV"));
                    double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                    if (report.getAttributeValue("title").equals("Referenzwerte stationärer Psychotherapiepatienten")) {
                        stationaerYSeries.add(report.indexOf(value) /2, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Gesamtstichprobe)")) {
                        hausGesamtYSeries.add(report.indexOf(value)/2, av, av-dev, av+dev);
                    } else if (report.getAttributeValue("title").equals("Referenzwerte von Hausarztpatienten (Teilstichprobe Gesunde)")) {
                        hausTeilYSeries.add(report.indexOf(value)/2, av, av-dev, av+dev);
                    }
                });
            });

            dataset.addSeries(resultYSeries);
            dataset.addSeries(stationaerYSeries);
            dataset.addSeries(hausGesamtYSeries);
            dataset.addSeries(hausTeilYSeries);

        } catch (IOException | JDOMException exception) {
            exception.printStackTrace();
        }
        return dataset;
    }
    public YIntervalSeriesCollection getDataset() {
        return dataset;
    }

}
