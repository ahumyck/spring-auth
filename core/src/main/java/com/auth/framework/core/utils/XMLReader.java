package com.auth.framework.core.utils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class XMLReader {

    private final String resource;

    public XMLReader(String resource) {
        this.resource = resource;
    }


    public Map<String, Collection<String>> getActionMapRules() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileReader(resource));
        Map<String, Collection<String>> resultMap = new HashMap<>();

        boolean key = false;
        boolean values = false;

        String keyValue = null;
        List<String> resourceValues = null;

        while (xmlEventReader.hasNext()) {
            XMLEvent event = xmlEventReader.nextEvent();

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String elementName = startElement.getName().getLocalPart();

                    if (elementName.equalsIgnoreCase("attribute")) {
                        keyValue = "";
                        resourceValues = new ArrayList<>();
                    } else if (elementName.equalsIgnoreCase("authorityName")) {
                        key = true;
                    } else if (elementName.equalsIgnoreCase("resource")) {
                        values = true;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (key) {
                        if (keyValue == null)
                            throw new NullPointerException();
                        keyValue = characters.toString();
                        key = false;
                    }

                    if (values) {
                        if (resourceValues == null) throw new NullPointerException();
                        resourceValues.add(characters.toString());
                        values = false;
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("attribute")) {
                        resultMap.put(keyValue, resourceValues);
                    }
                    break;
            }
        }
        return resultMap;
    }
}
