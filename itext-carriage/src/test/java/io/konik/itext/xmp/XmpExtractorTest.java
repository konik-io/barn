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
import io.konik.exception.TransformationWarning;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class XmpExtractorTest {

   XmpExtractor xmpCreator = new XmpExtractor();

   @Test
   public void extract_fromValidFerdnetSample() throws Exception {
      //given
      byte[] ferdNetXmpSample = toByteArray(getClass().getResourceAsStream("/ferdnet_xmp_sample.xml"));
      //when
      ZfXmpInfo metaData = xmpCreator.extract(ferdNetXmpSample);
      //then
      ZfXmpInfo expectedResult = new ZfXmpInfo("BASIC", "ZUGFeRD-invoice.xml", "INVOICE", "RC");
      assertThat(metaData).isEqualToComparingFieldByField(expectedResult);
   }
   
   @Test
   public void extract_fromValidPdfLib() throws Exception {
      //given
      byte[] pdfLibSample = toByteArray(getClass().getResourceAsStream("/pdflib_xmp_sample.xml"));
      //when
      ZfXmpInfo metaData = xmpCreator.extract(pdfLibSample);
     
      //then
      ZfXmpInfo expect = new ZfXmpInfo("COMFORT", "ZUGFeRD-invoice.xml", "INVOICE", "RC");
      assertThat(metaData).isEqualToComparingFieldByField(expect);
   }
   
   @Test(expected=TransformationWarning.class)
   public void extract_givenCorruptData() throws Exception {
      xmpCreator.extract("This is not a correct xml".getBytes());
   }

   @Test(expected=TransformationWarning.class)
   public void extract_NoZfDataInside() throws Exception {
      //given
      byte[] acmeXmpSample = toByteArray(getClass().getResourceAsStream("/acme_xmp_sample.xml"));

      //when
      xmpCreator.extract(acmeXmpSample);
   }

}
