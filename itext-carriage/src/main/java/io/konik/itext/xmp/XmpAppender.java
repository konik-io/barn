/*
 * Copyright (C) 2014 Konik.io
 *
 * This file is part of Konik library.
 *
 * Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Konik library.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.itext.xmp;

import static io.konik.itext.xmp.XmpZfNs.ZF_NS;
import static javax.xml.xpath.XPathConstants.NODE;
import io.konik.exception.TransformationWarning;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * The Xmp Appender.
 */
@Named
@Singleton
public class XmpAppender {

   private final XPath xpath;
   private DocumentBuilder documentBuilder;
   private Transformer transformer;

   /**
    * Instantiates a new XMP appender.
    */
   public XmpAppender() {
      XPathFactory factory = XPathFactory.newInstance();
      xpath = factory.newXPath();
      //java bug workaround
      System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
   }
   
   /**
    * Append the ZUGFeRD XMP info to the existing content.
    *
    * @param xmpData content to append to
    * @param info the xmp info entity
    * @return the resulting xmp content or xmpData if nothing needed to be added
    * @throws TransformationWarning the transformation warning
    */
   public byte[] append(final byte[] xmpData, final ZfXmpInfo info) throws TransformationWarning {
      try {
         Document xmpDocument = getDocument(xmpData);
         if (hasNoZfEntry(xmpDocument)) {
            appendZfExtensionToDocument(xmpDocument);
            appendZfInfoToDocumnt(xmpDocument, info);
            return transformToOutPutStream(xmpDocument).toByteArray();
         }
         return xmpData;
      } catch (Exception e) {
         throw new TransformationWarning("Could not append ZF information to PDFs XMP data: "+ e.getLocalizedMessage(), e);
      }
   }

   private Document getDocument(byte[] xmlContent) throws SAXException, IOException, ParserConfigurationException {
      return getDocumentBuilder().parse(new ByteArrayInputStream(xmlContent));
   }

   /**
    * Contains no ZUGFeRD entry in Document.
    * 
    * Check for:
    * [source,xml]
    * &lt;rdf:Description rdf:about=&quot;&quot; xmlns:zf=&quot;urn:ferd:pdfa:invoice:rc#&quot;&gt;
    * 
    * @param xmpDocument the xmp document we are checking on
    * @return true, if has no Zf Entry
    * @throws XPathExpressionException the x path expression exception
    */
   private boolean hasNoZfEntry(Document xmpDocument) throws XPathExpressionException  {
      String value = xpath.evaluate("/xmpmeta/RDF/Description/DocumentType", xmpDocument);
      return !"INVOICE".equalsIgnoreCase(value);
   }

   private Document appendZfExtensionToDocument(Document document) throws XPathExpressionException, DOMException, SAXException, IOException, ParserConfigurationException {
      Node rdfNode = (Node) xpath.evaluate("/xmpmeta/RDF/Description/schemas/Bag", document, NODE);
      Node importedZfNode = document.importNode(getExtensionNodeFromExtensionFile(), true);
      rdfNode.appendChild(importedZfNode);
      return document;
   }

   private Node getExtensionNodeFromExtensionFile() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException{
      InputStream zfExtensionIs = this.getClass().getResourceAsStream("/zfSchema/zf_extension.xmp");
      Document zfExtension = getDocumentBuilder().parse(zfExtensionIs);
      Node zfExtensionNode = (Node) xpath.evaluate("/RDF/Description/schemas/Bag/li", zfExtension, NODE);
      return zfExtensionNode;
   }

   /**
    * Append zf info.
    *
    * @param xmpDocument the xmp document
    * @param info the info
    * @return the byte array output stream
    * @throws TransformerConfigurationException the transformer configuration exception
    * @throws TransformerFactoryConfigurationError the transformer factory configuration error
    * @throws TransformerException the transformer exception
    * @throws ParserConfigurationException the parser configuration exception
    * @throws XPathExpressionException the x path expression exception
    */
   private ByteArrayOutputStream appendZfInfoToDocumnt(Document xmpDocument, ZfXmpInfo info)
         throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException,
         ParserConfigurationException, XPathExpressionException {

      Node descriptionNode = (Node) xpath.evaluate("/xmpmeta/RDF", xmpDocument, NODE);
      Node zfDescriptionNode = createZfDescriptionNode(xmpDocument, info);
      descriptionNode.appendChild(zfDescriptionNode);

      return transformToOutPutStream(xmpDocument);
   }

   private Node createZfDescriptionNode(Document xmp, ZfXmpInfo info) {
      Element node = xmp.createElement("rdf:Description");
      node.setAttribute("xmlns:zf", ZF_NS.value);
      node.setAttribute("rdf:about", "");
      node.appendChild(xmp.createElement("zf:ConformanceLevel")).setTextContent(info.getConformanceLevel());
      node.appendChild(xmp.createElement("zf:DocumentFileName")).setTextContent(info.getDocumentFileName());
      node.appendChild(xmp.createElement("zf:DocumentType")).setTextContent(info.getDocumentType());
      node.appendChild(xmp.createElement("zf:Version")).setTextContent(info.getVersion());
      return node;
   }

   private ByteArrayOutputStream transformToOutPutStream(Document doc) throws TransformerFactoryConfigurationError,
         TransformerConfigurationException, TransformerException {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Source src = new DOMSource(doc);
      Result dest = new StreamResult(outputStream);
      getTransformer().transform(src, dest);
      return outputStream;
   }

   private Transformer getTransformer() throws TransformerFactoryConfigurationError, TransformerConfigurationException {
      if (transformer == null) {
         TransformerFactory tranFactory = TransformerFactory.newInstance();
         transformer = tranFactory.newTransformer();
      }
      return transformer;
   }

   private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
      if (documentBuilder == null) {
         DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
         documentBuilder = docBuilderFactory.newDocumentBuilder();
      }
      return documentBuilder;
   }
}
