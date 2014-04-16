package io.konik.itext.xmp;

import io.konik.itext.ZfXmpMetaData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.itextpdf.text.pdf.PdfReader;

public class XmpZfDataExtractor {

   
  public ZfXmpMetaData readZfMetaData(PdfReader reader) throws IOException, XMLStreamException{
     ZfXmpMetaData zfXmpMetaData = new ZfXmpMetaData();
      byte xmp_MetaData[] = reader.getMetadata();
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(new ByteArrayInputStream(xmp_MetaData));
    
      while (xmlEventReader.hasNext()) {
         XMLEvent event = xmlEventReader.nextEvent();
         if(!event.isStartElement()){
            continue;   
         }
         if (!("zf".equals(event.asStartElement().getNamespaceContext().getPrefix("urn:ferd:pdfa:invoice:rc#")) || 
               "zf".equals(event.asStartElement().getNamespaceContext().getPrefix("urn:ferd:pdfa:invoice:rc")))) {
            continue;
         }
        
         String localPart = event.asStartElement().getName().getLocalPart();
         String content = xmlEventReader.nextEvent().asCharacters().toString();
         
         if (localPart.equals("ConformanceLevel")){
            zfXmpMetaData.setConformanceLevel(content);
         }
         if (localPart.equals("DocumentFileName")){
            zfXmpMetaData.setDocumentFileName(content);
         }
         if (localPart.equals("DocumentType")){
            zfXmpMetaData.setDocumentType(content);
         }
         if (localPart.equals("Version")){
           zfXmpMetaData.setVersion(content);
         }
      }
      return zfXmpMetaData;
   }
}
