/* Copyright (C) 2014 konik.io
 *
 * This file is part of the Konik library.
 *
 * The Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the Konik library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.itext.xmp;

import static io.konik.itext.xmp.XmpZfNs.ZF_NS_ALT;
import static io.konik.itext.xmp.XmpZfNs.ZF_NS;
import io.konik.exception.TransformationWarning;

import java.io.ByteArrayInputStream;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.itextpdf.text.pdf.PdfReader;

/**
 * This Class is responsible for extracting {@link ZfXmpInfo} from a given iText {@link PdfReader}.
 */
@Named
@Singleton
public class XmpExtractor {

   private static final String ZF = "zf";

   /**
    * Extract ZUGFeRD info.
    *
    * @param xmpContent the xmp xml conent
    * @return the extracted information from xmp content
    * @throws TransformationWarning the transformation warning
    */
   public ZfXmpInfo extract(byte[] xmpContent) throws TransformationWarning {
      try {
         return extractIntern(xmpContent);
      } catch (XMLStreamException e) {
        throw new TransformationWarning("Could not handle XML: "+ e.getLocalizedMessage(),e);
      }
   }

   private ZfXmpInfo extractIntern(byte[] xmpConent) throws TransformationWarning, XMLStreamException {
      ZfXmpInfo zfInfo = new ZfXmpInfo();;
      
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(new ByteArrayInputStream(xmpConent));
    
      while (xmlEventReader.hasNext()) {
         XMLEvent event = xmlEventReader.nextEvent();
         if (event.isStartElement() && hasZfNsContext(event)) {
            String localPart = event.asStartElement().getName().getLocalPart();
            String content = xmlEventReader.nextEvent().asCharacters().toString();
            
            if (localPart.equals("ConformanceLevel")) {
               zfInfo.setConformanceLevel(content);
            }
            if (localPart.equals("DocumentFileName")) {
               zfInfo.setDocumentFileName(content);
            }
            if (localPart.equals("DocumentType")) {
               zfInfo.setDocumentType(content);
            }
            if (localPart.equals("Version")) {
               zfInfo.setVersion(content);
            }
         }
      }
      if (false == zfInfo.isValid()) {
         throw new TransformationWarning("No ZF information availible within XMP data");
      }
     return zfInfo;
   }

   private boolean hasZfNsContext(XMLEvent event) {
      return hasZfNsContext(event,ZF_NS) || hasZfNsContext(event,ZF_NS_ALT);
   }
   
   private boolean hasZfNsContext(XMLEvent event, XmpZfNs nsPrexix) {
      return ZF.equals(event.asStartElement().getNamespaceContext().getPrefix(nsPrexix.value));
   }
}
