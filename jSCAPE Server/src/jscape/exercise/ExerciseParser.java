/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise;

/**
 *
 * @author achantreau
 */
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.StringReader;
import org.xml.sax.InputSource;

public class ExerciseParser {
    public static Exercise parseXMLExercise(String xmlExercise) {
        String ldv = null;
        String ldv2 = null;
        String rdv = null;
        String rdv2 = null;
        String c1 = null;
        String c2 = null;
        String c3 = null;
        String c4 = null;
        String s = null;
        
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setExpandEntityReferences(false);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlExercise));
            Document doc = dBuilder.parse(inputSource);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("display");

            int temp = 0;
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                ldv = eElement.getElementsByTagName("view").item(0).getTextContent();
                ldv2 = eElement.getElementsByTagName("value").item(0).getTextContent();
                temp++;
            }

            nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                rdv = eElement.getElementsByTagName("view").item(0).getTextContent();
                rdv2 = eElement.getElementsByTagName("value").item(0).getTextContent();
                c1 = eElement.getElementsByTagName("choice0").item(0).getTextContent();
                c2 = eElement.getElementsByTagName("choice1").item(0).getTextContent();
                c3 = eElement.getElementsByTagName("choice2").item(0).getTextContent();
                c4 = eElement.getElementsByTagName("choice3").item(0).getTextContent();
                s = eElement.getElementsByTagName("solution").item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new Exercise(ldv, ldv2, rdv, rdv2, c1, c2, c3, c4, s);
    }
}
