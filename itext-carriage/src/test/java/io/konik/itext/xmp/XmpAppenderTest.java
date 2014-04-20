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

import static com.google.common.io.ByteStreams.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class XmpAppenderTest {

   private XmpAppender xmpAppender;
   private ZfXmpInfo metaDataReference;

   @Before
   public void setUp() throws Exception {
      xmpAppender = new XmpAppender();
      metaDataReference = new ZfXmpInfo("BASIC", "ZUGFeRD-invoice.xml", "INVOICE", "RC");
   }

   @Test
   public void append_toAdobeCreatedXmp() throws Exception {
      //given
      InputStream is = getClass().getResourceAsStream("/acme_xmp_sample.xml");
      byte[] xmpXml = toByteArray(is);

      //when
      byte[] metaData = xmpAppender.append(xmpXml, metaDataReference);

      //then
      assertThat(metaData).isNotEmpty();
      
      String xmpStr = new String(metaData, "UTF-8");
      assertThat(xmpStr).contains("<zf:ConformanceLevel>BASIC</zf:ConformanceLevel>");
      assertThat(xmpStr).contains("<zf:DocumentFileName>ZUGFeRD-invoice.xml</zf:DocumentFileName>");
      assertThat(xmpStr).contains("<zf:DocumentType>INVOICE</zf:DocumentType>");
      assertThat(xmpStr).contains("<zf:Version>RC</zf:Version>");
      assertThat(xmpStr).contains("<pdfaSchema:namespaceURI>urn:ferd:pdfa:invoice:rc#</pdfaSchema:namespaceURI>");

//      System.out.println(new String(metaData, "UTF-8"));
   }

   
   @Test
   public void append_xmpContainAlreadyZfEntry() throws Exception {
      //given
      InputStream is = getClass().getResourceAsStream("/pdflib_xmp_sample.xml");
      byte[] xmpXml = toByteArray(is);
      
      //when
      byte[] xmp = xmpAppender.append(xmpXml, metaDataReference);

      //then
      assertThat(xmp).isNotEmpty();
      assertThat(xmp).containsExactly(xmpXml);
   }

}
