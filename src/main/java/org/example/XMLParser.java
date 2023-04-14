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
        File xmlFile = new File("C:\\Users\\d.krivoruchko\\Documents\\Epikur\\Übungsaufgaben\\values.xml");
        try {

            Document document = builder.build(xmlFile);
            Element rootNode = document.getRootElement();

            List<Object> coordinates = new ArrayList<>();
            for (Element result : rootNode.getChildren("Result")) {
                System.out.println(" ");
                for (Element value : result.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    Double v = Double.parseDouble(value.getAttributeValue("V"));
                    coordinates.add(scale);
                    coordinates.add(v);
                    System.out.println("Scale: "+scale+" V: "+v);
                }
            }

            for (Element report : rootNode.getChildren("Report")) {
                System.out.println(" ");
                for (Element value : report.getChildren("Value")) {
                    String scale = value.getAttributeValue("Scale");
                    Double av = Double.parseDouble(value.getAttributeValue("AV"));
                    Double dev = Double.parseDouble(value.getAttributeValue("DEV"));
                    coordinates.add(scale);
                    coordinates.add(av);
                    coordinates.add(dev);
                    System.out.println("Scale: "+scale+" AV: "+av+" DEV: "+dev);
                }
            }



        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }
}