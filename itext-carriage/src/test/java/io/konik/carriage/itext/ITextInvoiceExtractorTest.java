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
package io.konik.carriage.itext;

import static org.assertj.core.api.Assertions.assertThat;
import io.konik.harness.exception.InvoiceExtractionError;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ITextInvoiceExtractorTest {

   private static final String PDF_INVOICE_LOCATION = "/Musterrechnung_Einfach.pdf";
   ITextInvoiceExtractor invoiceExtractor;

   @Before
   public void setup() {
      invoiceExtractor = new ITextInvoiceExtractor();
   }

   @Test
   public void extract() {
      //setup
      InputStream pdfStream = getClass().getResourceAsStream(PDF_INVOICE_LOCATION);

      //exec
      byte[] xmlInvoice = invoiceExtractor.extract(pdfStream);

      //very
      assertThat(xmlInvoice).isNotNull();
      assertThat(new String(xmlInvoice)).contains("471102");
   }

   @Test(expected = InvoiceExtractionError.class)
   public void extractInputStream_Fail() {
      InputStream pdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      invoiceExtractor.extract(pdfStream);
   }

}
