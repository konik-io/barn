package io.konik.itext.extractor;

import static com.itextpdf.text.pdf.PdfName.EF;
import static com.itextpdf.text.pdf.PdfName.F;
import static com.itextpdf.text.pdf.PdfReader.getStreamBytes;
import static javax.xml.bind.JAXBContext.newInstance;
import io.konik.harness.InvoiceExtractionError;
import io.konik.harness.InvoiceExtractor;
import io.konik.zugferd.Invoice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

public class ITextPdfInvoiceExtractor implements InvoiceExtractor {

   private static PdfName AF = new PdfName("AF");

   public Invoice extract(byte[] pdfIn) {
      return extract(new ByteArrayInputStream(pdfIn));
   }
   
   public Invoice extract(InputStream pdfStream) {
         PdfReader reader = getPdfReader(pdfStream);
         PdfArray af = getValidAf(reader.getCatalog());
         PdfDictionary fileSpec = getValidFileSpec(af); 
         PdfDictionary ef = getValidEf(fileSpec);
         byte[] invoiceXmlContent = getFStream(ef);
         return covertToObjectModel(invoiceXmlContent);
   }

   private PdfReader getPdfReader(InputStream pdfStream) {
      try {
         return new PdfReader(pdfStream);
      } catch (IOException e) {
         throw new InvoiceExtractionError("Could not read or open pdf.",e);
      }
   }

   private byte[] getFStream(PdfDictionary ef){
      if (ef.contains(F)) {
         PdfStream xmlStream = ef.getAsStream(F);
         try {
            return getStreamBytes((PRStream) xmlStream);
         } catch (IOException e) {
            throw new InvoiceExtractionError("Could not extrac xml content form pdf.",e);
         }
      }
      throw new InvoiceExtractionError("Pdf catalog does not contain Valid F Entry");
   }
   


   private PdfDictionary getValidEf(PdfDictionary fileSpec) {
      if (fileSpec.contains(EF)) {
         return fileSpec.getAsDict(EF);
      }
      throw new InvoiceExtractionError("Pdf catalog does not contain Valid EF Entry");
   }

   private PdfDictionary getValidFileSpec(PdfArray af) {
      if (af.isEmpty() || af.getAsDict(0) == null) {
         throw new InvoiceExtractionError("Pdf does not contain a FileSpec Entry");
      }
      return af.getAsDict(0);
   }

   private PdfArray getValidAf(PdfDictionary catalog) {
      if (catalog.contains(AF)) {
         PdfArray af = catalog.getAsArray(AF);
         if (!af.isEmpty() && af.getDirectObject(0).isDictionary())
            return af;
      }
      throw new InvoiceExtractionError("Pdf catalog does not contain Valid AF Entry");
   }
   
   public static Invoice covertToObjectModel(byte[] xmlContent){
      try {
         Unmarshaller unmarshaller = newInstance("io.konik.zugferd").createUnmarshaller();
         Source s = new StreamSource(new ByteArrayInputStream(xmlContent));
         JAXBElement<Invoice> invoice = unmarshaller.unmarshal(s, Invoice.class);
         return invoice.getValue();
      } catch (JAXBException e) {
         throw new InvoiceExtractionError("Could not read parse xml content",e);
      }
   }
}
