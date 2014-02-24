package io.konik.itext.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import io.konik.harness.InvoiceExtractionError;
import io.konik.zugferd.Invoice;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ITextPdfInvoiceExtractorTest {

   ITextPdfInvoiceExtractor invoiceExtractor;

   @Before
   public void setup() {
      invoiceExtractor = new ITextPdfInvoiceExtractor();
   }

   @Test
      public void extractByteArray() throws Exception {
         //setup
         URL resource = getClass().getResource("/Beispielrechnung_ZUGFeRD_RC_COMFORT.pdf");
         byte[] pdf = new byte[resource.openConnection().getContentLength()];
         DataInputStream dataIs = new DataInputStream(resource.openStream());
         dataIs.readFully(pdf);
   
         //exec
         Invoice invoice = invoiceExtractor.extract(pdf);
   
         //validate
         assertThat(invoice).isNotNull();
         assertThat(invoice.getHeader().getId().getValue()).isEqualTo("471102");
      }

   @Test
      public void extractInputStream() throws SAXException, IOException {
         //setup
         InputStream pdfStream = getClass().getResourceAsStream("/Beispielrechnung_ZUGFeRD_RC_COMFORT.pdf");
         
         //exec
         Invoice invoice = invoiceExtractor.extract(pdfStream);
   
         //very
         assertThat(invoice).isNotNull();
         assertThat(invoice.getHeader().getId().getValue()).isEqualTo("471102");
      }

   @Test(expected=InvoiceExtractionError.class)
      public void extractInputStream_Fail() {
         InputStream pdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
         invoiceExtractor.extract(pdfStream);
      }

}
