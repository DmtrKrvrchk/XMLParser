package org.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;



public class XMLParser {

    public static void main(String[] args) {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("values.xml");

        try {

            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();


            List<String> scaleNames = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            for (Element result : rootNode.getChildren("Result")) {
                for (Element value : result.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    double v = Double.parseDouble(value.getAttributeValue("V"));
                    scaleNames.add(scale);
                    values.add(v);
                }
            }

        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }
}