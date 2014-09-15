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
package io.konik.carriage.itext;

import static com.itextpdf.text.pdf.PdfName.EF;
import static com.itextpdf.text.pdf.PdfName.F;
import static com.itextpdf.text.pdf.PdfReader.getStreamBytes;
import io.konik.harness.FileExtractor;
import io.konik.harness.exception.InvoiceExtractionError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;
import javax.inject.Singleton;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

/**
 * The Class iText Pdf Invoice Extractor.
 */
@Named
@Singleton
public class ITextInvoiceExtractor implements FileExtractor {

   private static final PdfName AF = new PdfName("AF");

   @Override
   public byte[] extract(InputStream pdfInput) {
      
      PdfReader reader = getPdfReader(pdfInput);
      PdfArray af = getValidAf(reader.getCatalog());
      PdfDictionary fileSpec = getValidFileSpec(af); 
      PdfDictionary ef = getValidEf(fileSpec);
      return getFStream(ef);
   }
   
   @Override
   public InputStream extractToStream(InputStream pdfInput) {
     return new ByteArrayInputStream(extract(pdfInput));
   }
   

   private static PdfReader getPdfReader(InputStream pdfStream) {
      try {
         return new PdfReader(pdfStream);
      } catch (IOException e) {
         throw new InvoiceExtractionError("Could not read or open pdf.",e);
      }
   }

   private static PdfArray getValidAf(PdfDictionary catalog) {
      if (catalog.contains(AF)) {
         PdfArray af = catalog.getAsArray(AF);
         if (!af.isEmpty() && af.getDirectObject(0).isDictionary()) {
            return af;
         }
      }
      throw new InvoiceExtractionError("Pdf catalog does not contain Valid AF Entry");
   }
   
   private static PdfDictionary getValidFileSpec(PdfArray af) {
      if (af.isEmpty() || af.getAsDict(0) == null) {
         throw new InvoiceExtractionError("Pdf does not contain a FileSpec Entry");
      }
      return af.getAsDict(0);
   }
   
   private static PdfDictionary getValidEf(PdfDictionary fileSpec) {
      if (fileSpec.contains(EF)) {
         return fileSpec.getAsDict(EF);
      }
      throw new InvoiceExtractionError("Pdf catalog does not contain Valid EF Entry");
   }

   private static byte[] getFStream(PdfDictionary ef){
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


}
