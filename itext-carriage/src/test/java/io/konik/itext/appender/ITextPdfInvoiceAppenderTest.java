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
 * You should have received a copy of the GNU Affero General  Public License
 * along with the Konik library. If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.itext.appender;

import static com.google.common.io.ByteStreams.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;
import io.konik.InvoiceTransformer;
import io.konik.harness.InvoiceAppender;
import io.konik.zugferd.Invoice;

import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

@SuppressWarnings("javadoc")
public class ITextPdfInvoiceAppenderTest {

   InvoiceAppender appender;
   InputStream isPdf;
   InputStream isXml;
   InvoiceTransformer transformer = new InvoiceTransformer();
   private InputStream isRandomXml;

   @Before
   public void setUp() throws Exception {
      appender = new ITextPdfInvoiceAppender();
      isPdf = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      isXml = getClass().getResourceAsStream("/ZUGFeRD-invoice.xml");
      isRandomXml = getClass().getResourceAsStream("/zf_random_invoice.xml");
   }

   @Test
   public void appendInputStream() throws Exception {
      Invoice invoice = transformer.from(isXml);
      byte[] pdfInput = toByteArray(isPdf);
      byte[] outPdf = appender.append(invoice, pdfInput);
      assertThat(outPdf).isNotNull();

      Files.write(outPdf , new File("target/acme_invoice-42.pdf"));
   }

   @Test
   public void appendInputStream_random() throws Exception {
      Invoice invoice = transformer.from(isRandomXml);
      byte[] pdfInput = toByteArray(isPdf);
      byte[] outPdf = appender.append(invoice, pdfInput);
      assertThat(outPdf).isNotNull();

      Files.write(outPdf , new File("target/acme_invoice-42_random.pdf"));
   }
   
   @Test
   public void appendByteArray() throws Exception {
      Invoice invoice = transformer.from(isXml);
      byte[] outPdf = appender.append(invoice, toByteArray(isPdf));
      assertThat(outPdf).isNotNull();
   }
   
}
