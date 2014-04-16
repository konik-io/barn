package io.konik.itext.xmp;

import static org.assertj.core.api.Assertions.assertThat;
import io.konik.itext.ZfXmpMetaData;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;

public class XmpZfDataExtractorTest {

   XmpZfDataExtractor xmpCreator;
   InputStream zfPdfStream;
   InputStream acmePdfStream;
   ZfXmpMetaData metaDataReference;

   @Before
   public void setUp() throws Exception {
      xmpCreator = new XmpZfDataExtractor();
      zfPdfStream = getClass().getResourceAsStream("/Beispielrechnung_ZUGFeRD_RC_COMFORT.pdf");
      acmePdfStream = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      metaDataReference = new ZfXmpMetaData("BASIC", "ZUGFeRD-invoice.xml", "INVOICE", "RC");
   }

   @Test
   public void readZfMetaData_valid() throws Exception {
      //given
      PdfReader reader = new PdfReader(zfPdfStream);
      //when
      ZfXmpMetaData metaData = xmpCreator.readZfMetaData(reader);
      //then
      assertThat(metaData).isEqualToComparingFieldByField(metaDataReference);
   }
   
   @Test
   public void readZfMetaData_NoData() throws Exception {
      //given
      PdfReader reader = new PdfReader(acmePdfStream);
      //when
      ZfXmpMetaData metaData = xmpCreator.readZfMetaData(reader);
      //then
      assertThat(metaData.getConformanceLevel()).isEmpty();
      assertThat(metaData.getVersion()).isEmpty();
   }

}
