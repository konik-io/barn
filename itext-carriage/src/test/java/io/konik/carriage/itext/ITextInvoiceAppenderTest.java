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
import io.konik.carriage.itext.ITextInvoiceAppender;
import io.konik.harness.AppendParameter;
import io.konik.harness.FileAppender;
import io.konik.harness.appender.DefaultAppendParameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ITextInvoiceAppenderTest {
   
   private static final String MUSTERRECHNUNG_EINFACH_XML = "/Musterrechnung_Einfach.xml";
   private static final String ACME_INVOICE_42_PDF = "/acme_invoice-42.pdf";
   
   private static final String TARGET_ACME_INVOICE_42_PDF = "target/acme_invoice-42.pdf";
   
   FileAppender appender;
   InputStream isPdf;
   InputStream isXml;

   @Before
   public void setUp() throws Exception {
      appender = new ITextInvoiceAppender();
      isPdf = getClass().getResourceAsStream(ACME_INVOICE_42_PDF);
      isXml = getClass().getResourceAsStream(MUSTERRECHNUNG_EINFACH_XML);
   }

   @Test
   public void append() throws Exception {
      File file = new File(TARGET_ACME_INVOICE_42_PDF);
      FileOutputStream outPdf = new FileOutputStream(file);
      AppendParameter appendable = new DefaultAppendParameter(isPdf, isXml, outPdf,"1.0","COMFORT");
      appender.append(appendable);
      assertThat(file).exists();
   }
}
