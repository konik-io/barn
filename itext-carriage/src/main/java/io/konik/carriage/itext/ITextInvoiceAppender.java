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

import static com.itextpdf.text.pdf.AFRelationshipValue.Alternative;
import static com.itextpdf.text.pdf.PdfName.AFRELATIONSHIP;
import static com.itextpdf.text.pdf.PdfName.MODDATE;
import static com.itextpdf.text.pdf.PdfName.PARAMS;
import io.konik.harness.AppendParameter;
import io.konik.harness.FileAppender;
import io.konik.harness.exception.InvoiceAppendError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;
import javax.inject.Singleton;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamper;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;
import com.itextpdf.xmp.XMPUtils;

/**
 * The Class IText PDF Invoice Appender.
 *
 * For now we expect a compliant PDF/A-3B.
 *
 */
@Named
@Singleton
public class ITextInvoiceAppender implements FileAppender {

   private static final String MIME_TYPE = "text/xml";
   private static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";
   private static final String ZF_NS = "urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#";

   @Override
   public void append(AppendParameter appendable) {
      try {
         appendInvoiceIntern(appendable);
      } catch (DocumentException e) {
         throw new InvoiceAppendError("Could not open PD for modification or to close it", e);
      } catch (IOException e) {
         throw new InvoiceAppendError("PDF IO Error", e);
      } catch (XMPException e) {
         throw new InvoiceAppendError("Error with XMP Extension", e);
      }
   }

   /**
    * Append invoice intern.
    *
    * @param appendable the appendable
    * @throws IOException Signals that an I/O exception has occurred.
    * @throws DocumentException the document exception
    * @throws XMPException the XMP exception
    */
   private void appendInvoiceIntern(AppendParameter appendable) throws IOException, DocumentException, XMPException {
      byte[] attachmentFile = convertToByteArray(appendable.attachmentFile());
      PdfReader reader = new PdfReader(appendable.inputPdf());
      PdfAStamper stamper = new PdfAStamper(reader, appendable.resultingPdf(), PdfAConformanceLevel.PDF_A_3B);

      appendZfMetadata(stamper, appendable.zugferdConformanceLevel(), appendable.zugferdVersion());
      attachFile(attachmentFile, stamper);

      stamper.close();
      reader.close();
   }

   private static void attachFile(byte[] attachmentFile, PdfAStamper stamper) throws IOException {
      PdfDictionary embeddedFileParams = new PdfDictionary();
      embeddedFileParams.put(PARAMS, new PdfName(ZF_FILE_NAME));
      embeddedFileParams.put(MODDATE, new PdfDate());

      PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, ZF_FILE_NAME,
            attachmentFile, MIME_TYPE, embeddedFileParams, 0);
      fs.put(AFRELATIONSHIP, Alternative);
      stamper.addFileAttachment(ZF_FILE_NAME, fs);

      PdfArray array = new PdfArray();
      array.add(fs.getReference());
      stamper.getWriter().getExtraCatalog().put(new PdfName("AF"), array);
   }

   private void appendZfMetadata(PdfAStamper stamper, String conformanceLevel, String zfVersion) throws XMPException {
      stamper.createXmpMetadata();
      XmpWriter xmpWriter = stamper.getXmpWriter();
      XMPMeta xmpMeta = xmpWriter.getXmpMeta();
      InputStream zfExtensionIs = this.getClass().getResourceAsStream("/zf_extension.xmp");
      XMPMeta zfExtensionMetadata = XMPMetaFactory.parse(zfExtensionIs);
      XMPUtils.appendProperties(zfExtensionMetadata, xmpMeta, true, false);
      xmpWriter.setProperty(ZF_NS, "ConformanceLevel", conformanceLevel);
      xmpWriter.setProperty(ZF_NS, "Version", zfVersion);
   }

   private static byte[] convertToByteArray(InputStream is) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[65536];
      try {
         for (int length; (length = is.read(buffer)) != -1;) {
            baos.write(buffer, 0, length);
         }
         is.close();
         baos.close();
      } catch (IOException e) {
         throw new InvoiceAppendError("Was not possible to read Invoice Content stream", e);
      }
      return baos.toByteArray();
   }

}
