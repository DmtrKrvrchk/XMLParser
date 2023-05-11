package org.example;

public class XMLParser {
//TODO Bitte in mehrere Klassen aufteilen, MVC?
    public static void main(String[] args) {


        ChartDaten chartDaten = new ChartDaten();
        ChartFabrik chart = new ChartFabrik("Patienten Values", "Value", "Scale", chartDaten.getDataset());


    }
}