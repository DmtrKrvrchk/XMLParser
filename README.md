# XMLParser

Ziel der Aufgabe ist es, ein Java-Programm zu schreiben, welches Werte aus einer XML-Datei einliest und diese grafisch auf dem Bildschirm anzeigt. Die Wahl der verwendeten Bibliotheken, Frameworks, etc. ist dabei völlig frei.


1. Einlesen der gegebenen XML-Datei

Die vorgegebene XML-Datei enthält Messwerte (XML-Tag „Result“) zu 10 verschiedenen Skalen. 
Außerdem sind drei Sets mit Referenzwerten zum Vergleich in der Datei enthalten (XML-Tag „Report“), diese jeweils mit dem Durchschnittswert und der Standardabweichung. 
Diese Werte sind einzulesen und in geeigneter Form im Speicher abzulegen.


2. Anzeige der eingelesenen Werte als Grafik

![image](https://user-images.githubusercontent.com/68115186/233604849-d3183f41-de92-49b4-9686-e260cf7db741.png)


Die Werte aus der XML-Datei sollen dem Nutzer als Grafik angezeigt werden. Die X-Achse gibt dabei den gemessenen Wert an, die Y-Achse die Skala. 
Die Werte des Patienten sollen als rote, die Vergleichswerte (Durchschnitt) als blaue Linie dargestellt werden. 
Die Standardabweichung soll als hellblauer, gefüllter Bereich gezeichnet werden. 
Außerdem soll der Nutzer noch über eine Combobox das darzustellende Set mit den Referenzwerten wählen können.
