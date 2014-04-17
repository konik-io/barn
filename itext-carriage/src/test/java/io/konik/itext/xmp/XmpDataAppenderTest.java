package io.konik.itext.xmp;

import io.konik.itext.ZfXmpMetaData;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;

public class XmpDataAppenderTest {

   private XmpDataAppender xmpAppender;
   private InputStream zfPdfStream;
   private InputStream acmePdfStream;
   private ZfXmpMetaData metaDataReference;

   @Before
   public void setUp() throws Exception {
      xmpAppender = new XmpDataAppender();
      zfPdfStream = getClass().getResourceAsStream("/Beispielrechnung_ZUGFeRD_RC_COMFORT.pdf");
      acmePdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      metaDataReference = new ZfXmpMetaData("BASIC", "ZUGFeRD-invoice.xml", "INVOICE", "RC");
   }

   @Test
   public void appendMetaData() throws Exception {
      //given
      PdfReader reader = new PdfReader(acmePdfStream);
      
      //when
      byte[] metaData = xmpAppender.appendMetaData(reader, metaDataReference);
      //then
   }

}
