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
package io.konik.itext.appender;

import static com.itextpdf.text.pdf.AFRelationshipValue.Alternative;
import static com.itextpdf.text.pdf.PdfName.AFRELATIONSHIP;
import static com.itextpdf.text.pdf.PdfName.MODDATE;
import static com.itextpdf.text.pdf.PdfName.PARAMS;
import io.konik.InvoiceTransformer;
import io.konik.exception.TransformationWarning;
import io.konik.harness.InvoiceAppendError;
import io.konik.harness.InvoiceAppender;
import io.konik.itext.xmp.XmpAppender;
import io.konik.itext.xmp.ZfXmpInfo;
import io.konik.zugferd.Invoice;
import io.konik.zugferd.profile.Profile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
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

/**
 * The Class IText PDF Invoice Appender.
 *
 * For now we expect a compliant PDF/A-3B.
 *
 */
@Named
@Singleton
public class ITextPdfInvoiceAppender implements InvoiceAppender {

   private static final String INVOICE = "INVOICE";

   private static final String ZF_FILE_NAME = "ZUGFeRD-invoice.xml";

   private final XmpAppender xmp;

   private final InvoiceTransformer transformer;

   /**
    * Instantiates a new i-text pdf invoice appender.
    */
   public ITextPdfInvoiceAppender() {
      this(new XmpAppender(),new InvoiceTransformer());
   }
   
   /**
    * Instantiates a new i-text pdf invoice appender.
    *
    * @param xmpAppender the xmp appender
    * @param invoiceTransformer the invoice transformer
    */
   @Inject
   public ITextPdfInvoiceAppender(XmpAppender xmpAppender, InvoiceTransformer invoiceTransformer) {
      this.xmp = xmpAppender;
      this.transformer = invoiceTransformer;
   }

   /**
    * Append invoice.
    *
    * @param invoice the invoice
    * @param pdf the in PDF byte array
    * @return the byte[]
    */
   @Override
   public byte[] append(final Invoice invoice, final byte[] pdf) {
      ByteArrayInputStream isPdf = new ByteArrayInputStream(pdf);
      ByteArrayOutputStream osPdf = new ByteArrayOutputStream(pdf.length);

      append(invoice, isPdf, osPdf);

      return osPdf.toByteArray();
   }

   /**
    * Append invoice.
    *
    * @param invoice the invoice
    * @param inputPdf the input pdf
    * @param resultingPdf the resulting pdf
    */
   @Override
   public void append(final Invoice invoice, InputStream inputPdf, OutputStream resultingPdf) {
      try {
         appendInvoiceIntern(invoice, inputPdf, resultingPdf);
      } catch (DocumentException e) {
         throw new InvoiceAppendError("Could not open PD for modification or to close it", e);
      } catch (IOException e) {
         throw new InvoiceAppendError("PDF IO Error", e);
      }
   }

   /**
    * Append invoice intern.
    *
    * @param invoice the invoice
    * @param inPdf the in pdf
    * @param output the output
    * @throws IOException Signals that an I/O exception has occurred.
    * @throws DocumentException the document exception
    */
   private void appendInvoiceIntern(Invoice invoice, InputStream inPdf, OutputStream output) throws IOException,
         DocumentException {

      byte[] content = transformer.from(invoice);

      PdfReader reader = new PdfReader(inPdf);

      PdfAStamper stamper = new PdfAStamper(reader, output, PdfAConformanceLevel.PDF_A_3B);

      appendZfContentToXmp(stamper,invoice);

      // Creating PDF/A-3 compliant attachment.
      PdfDictionary embeddedFileParams = new PdfDictionary();
      embeddedFileParams.put(PARAMS, new PdfName(ZF_FILE_NAME));
      embeddedFileParams.put(MODDATE, new PdfDate());
      PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), null, ZF_FILE_NAME, content,
            "text/xml", embeddedFileParams, 0);
      fs.put(AFRELATIONSHIP, Alternative);
      stamper.addFileAttachment(ZF_FILE_NAME, fs);

      //AF
      PdfArray array = new PdfArray();
      array.add(fs.getReference());
      stamper.getWriter().getExtraCatalog().put(new PdfName("AF"), array);

      stamper.close();
      reader.close();
   }

   private void appendZfContentToXmp(PdfAStamper stamper, Invoice invoice) throws IOException {
      Profile profile = invoice.getContext().getProfile();
      ZfXmpInfo info = new ZfXmpInfo(profile, ZF_FILE_NAME, INVOICE);
      try {
         byte[] newXmpMetadata = xmp.append(stamper.getReader().getMetadata(), info);
         stamper.setXmpMetadata(newXmpMetadata);
      } catch (TransformationWarning e) {
         // TODO if we don't rethrow we should provide a result object with a warning Msg.
         throw new InvoiceAppendError("Error Appending XMP to PDF", e.getCause());
      }
   }
}
