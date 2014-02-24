package io.konik.itext.appender;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;
import io.konik.InvoiceHandler;
import io.konik.harness.InvoiceAppender;
import io.konik.zugferd.Invoice;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public class ITextPdfInvoiceAppenderTest {

   InvoiceAppender appender;
   InputStream isPdf;
   InputStream isXml;

   @Before
   public void setUp() throws Exception {
      appender = new ITextPdfInvoiceAppender();
      isPdf = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      isXml = getClass().getResourceAsStream("/ZUGFeRD-invoice.xml");
   }

   @Test
   public void appendInputStream() throws Exception {
      Invoice invoice = InvoiceHandler.unmarshall(isXml);
      byte[] pdfInput = toByteArray(isPdf);
      byte[] outPdf = appender.append(invoice, pdfInput);
      assertThat(outPdf).isNotNull();
   }

   @Test
   public void appendByteArray() throws Exception {
      Invoice invoice = InvoiceHandler.unmarshall(isXml);
      byte[] outPdf = appender.append(invoice, toByteArray(isPdf));
      assertThat(outPdf).isNotNull();
   }

}
