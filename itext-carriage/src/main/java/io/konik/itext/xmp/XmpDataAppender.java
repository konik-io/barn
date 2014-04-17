package io.konik.itext.xmp;

import io.konik.itext.ZfXmpMetaData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;

public class XmpDataAppender {

   public byte[] appendMetaData(PdfReader reader, ZfXmpMetaData zfXmpMetaData) throws IOException, XMLStreamException,
         ParserConfigurationException, SAXException, XPathExpressionException, DocumentException, XMPException {
      
      OutputStream output = new ByteArrayOutputStream();
      PdfAStamper stamper = new PdfAStamper(reader, output , PdfAConformanceLevel.PDF_A_3B);
      byte xmpMetaData[] = reader.getMetadata();
      byte xmpResult[] = null;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

//      stamper.getWriter().setXmpMetadata();
//      stamper.createXmpMetadata();
//      XMPMeta xmpMeta = stamper.getXmpWriter().getXmpMeta();
//      XMPMeta parsed = XMPMetaFactory.parse(new ByteArrayInputStream(xmpMetaData));
//      XMPMetaFactory.getSchemaRegistry().registerNamespace("urn:ferd:pdfa:invoice:rc#", "zf");
//      parsed.appendArrayItem("urn:ferd:pdfa:invoice:rc#", "name", "value");
//      parsed.
//      System.out.println(parsed.getObjectName());
//      stamper.getXmpWriter().
//      stamper.getXmpWriter().appendArrayItem("urn:ferd:pdfa:invoice:rc#", "name", "value");
//    stamper.getXmpWriter().
//      stamper.getXmpWriter().serialize(System.out);
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath
            .compile("/*[local-name() = 'xmpmeta']/*[local-name() = 'RDF']");

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(new ByteArrayInputStream(xmpMetaData));

      Object result = expr.evaluate(document, XPathConstants.NODESET);
      NodeList nodes = (NodeList) result;
      nodes.getLength();
      nodes.item(0).getNodeName();

      Element element = document.getDocumentElement();
      //      nodeList.
      //      nodeList.

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      XMLEventWriter writer = outputFactory.createXMLEventWriter(outputStream);

      //      while (writer.hashCode() {
      //         XMLEvent event = writer.nextEvent();
      //         if(!event.isStartElement()){
      //            continue;   
      //         }
      //         if (!("zf".equals(event.asStartElement().getNamespaceContext().getPrefix("urn:ferd:pdfa:invoice:rc#")) || 
      //               "zf".equals(event.asStartElement().getNamespaceContext().getPrefix("urn:ferd:pdfa:invoice:rc")))) {
      //            continue;
      //         }
      //        
      //         String localPart = event.asStartElement().getName().getLocalPart();
      //         String content = writer.nextEvent().asCharacters().toString();
      //         
      //         if (localPart.equals("ConformanceLevel")){
      //            zfXmpMetaData.setConformanceLevel(content);
      //         }
      //         if (localPart.equals("DocumentFileName")){
      //            zfXmpMetaData.setDocumentFileName(content);
      //         }
      //         if (localPart.equals("DocumentType")){
      //            zfXmpMetaData.setDocumentType(content);
      //         }
      //         if (localPart.equals("Version")){
      //           zfXmpMetaData.setVersion(content);
      //         }
      //      }
      return xmpResult;
   }
}
