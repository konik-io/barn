package io.konik.itext.playground;

import static com.itextpdf.text.pdf.AFRelationshipValue.Alternative;
import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.WINANSI;
import static com.itextpdf.text.pdf.PdfAConformanceLevel.PDF_A_3B;
import static com.itextpdf.text.pdf.PdfName.AFRELATIONSHIP;
import static com.itextpdf.text.pdf.PdfName.MODDATE;
import static com.itextpdf.text.pdf.PdfName.PARAMS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.stream.FileImageInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AFRelationshipValue;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPTable;
import io.konik.zugferd.Invoice;
import io.konik.zugferd.datatype.unqualified.ID;
import io.konik.zugferd.entity.Item;

public class CreateITextPDFWithInvoice {


	@Test
	public void  createPdf_with_iText() throws DocumentException, JAXBException, IOException{

		Document document = new Document();
		PdfAWriter writer = PdfAWriter.getInstance(document,new FileOutputStream("./target/pdfa3-invoice.itext.pdf"),PDF_A_3B);
		writer.createXmpMetadata();
		document.open();

		// Set output intent. PDF/A requirement.
		ICC_Profile icc = ICC_Profile.getInstance(getClass().getResourceAsStream("/sRGB Color Space Profile.icm"));
		writer.setOutputIntents("Custom", "", "http://www.color.org","sRGB IEC61966-2.1", icc);



		// Creating PDF/A-3 compliant attachment.
		PdfDictionary parameters = new PdfDictionary();
		parameters.put(PARAMS, new PdfName("ZUGFeRD-invoice.xml"));
		parameters.put(MODDATE, new PdfDate());
		PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, null,"ZUGFeRD-invoice.xml", readInvoiceFile(), "application/xml", parameters,0);
		fs.put(AFRELATIONSHIP, Alternative);
		writer.addFileAttachment(fs);
		
		PdfArray array = new PdfArray();
		array.add(fs.getReference());
		writer.getExtraCatalog().put(new PdfName("AF"), array);

		// From here on we can add content to the PDF just like we would do for
		// a regular PDF.
		fillDocument(document,loadXMLInvoice());
		writer.close();

	}

   private byte[] readInvoiceFile() {
      InputStream is = getClass().getResourceAsStream("/ZUGFeRD-invoice.xml");
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      try {

         int readCount;
         byte[] data = new byte[16384];

         while ((readCount = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, readCount);
         }

         buffer.flush();
      } catch (IOException e) {
         System.err.println(e);
      }

      return buffer.toByteArray();
   }

   public void fillDocument(Document document,Invoice invoice) throws DocumentException{

		// All fonts shall be embedded. PDF/A requirement.
		Font bold10 = FontFactory.getFont("/FreeSansBold.ttf",WINANSI, EMBEDDED, 10);
		Font normal9 = FontFactory.getFont("/FreeSans.ttf",WINANSI, EMBEDDED, 9);
		Font bold9 = FontFactory.getFont("/FreeSansBold.ttf",WINANSI, EMBEDDED, 9);
		Font normal8 = FontFactory.getFont("/FreeSans.ttf",WINANSI, EMBEDDED, 8);

		// Building header.
		document.add(new Paragraph("Invoice number: " + invoice.getHeader().getId().getValue() , bold10));
		document.add(new Paragraph("\n", normal8));
		document.add(new Paragraph("Dear " + invoice.getTrades().iterator().next().getAgreements().iterator().next().getBuyerTradeParty().getContact().getName() + ",", normal9));
		document.add(new Paragraph(invoice.getHeader().getNotes().iterator().next().getContent(),normal9));
		document.add(new Paragraph("\n", normal8));

		// Building Items
		PdfPTable table = new PdfPTable(new float[] { 40, 15, 15, 15, 15 });
		table.addCell(new Paragraph("Pos", bold9));
		table.addCell(new Paragraph("Amount", bold9));
		table.addCell(new Paragraph("Price (net)", bold9));
		table.addCell(new Paragraph("VAT", bold9));
		table.addCell(new Paragraph("Price (gross)", bold9));
		for (Item item : invoice.getTrades().get(0).getItems()) {
			ID lineId = item.getPosition().getItemNumber();
			table.addCell(new Paragraph(lineId==null?"":lineId.getValue() , normal9));
			table.addCell(new Paragraph(item.getDelivery()!=null?item.getDelivery().getBilledQuantity().toString():"", normal9));
			table.addCell(new Paragraph(item.getAgreement()!=null?item.getAgreement().getNetPriceProduct().getChargeAmount().getValue().toString():"" , normal9));
			table.addCell(new Paragraph(item.getSettlement()!=null?item.getSettlement().getTradeTax().get(0).getApplicablePercentage().toString():"", normal9));
			table.addCell(new Paragraph(item.getSettlement()!=null?item.getSettlement().getMonetarySummation().getNetTotal().toString():"", normal9));
		}
		table.addCell(new Paragraph("Subtotal ", bold9));
		table.addCell(new Paragraph(" ", bold9));
		table.addCell(new Paragraph(" ", bold9));
		table.addCell(new Paragraph(" ", bold9));
		table.addCell(new Paragraph(invoice.getTrades().get(0).getTradeSettlement().getMonetarySummation().getGrandTotal().getValue().toString() ,bold9));
		document.add(table);
		document.add(new Paragraph("\n", normal8));

		// Building "Total" table
		table = new PdfPTable(new float[] { 70, 15, 15 });
		table.addCell(new Paragraph("Total", bold9));
		table.addCell(new Paragraph("VAT", bold9));
		table.addCell(new Paragraph("Price (gross)", bold9));
		table.addCell(new Paragraph("Subtotal tickets", normal9));
		table.addCell(new Paragraph(" ", normal9));
		table.addCell(new Paragraph("123", normal9));
		table.addCell(new Paragraph("     Invoice amount (net)",normal8));
		table.addCell(new Paragraph("456",normal8));
		table.addCell(new Paragraph("789", normal8));
		table.addCell(new Paragraph("     Included VAT", normal8));
		table.addCell(new Paragraph("1011", normal8));
		table.addCell(new Paragraph("1213",normal8));
		table.addCell(new Paragraph("Invoice amount", bold10));
		table.addCell(new Paragraph(" ", bold10));
		table.addCell(new Paragraph("1415", bold10));
		document.add(table);

		document.close();
	}

	Invoice loadXMLInvoice() throws JAXBException {
		JAXBContext instance = JAXBContext.newInstance("io.konik.zugferd.validation");
		Unmarshaller unmarshaller = instance.createUnmarshaller();
		Source source = new StreamSource(getClass().getResourceAsStream("/ZUGFeRD-invoice.xml"));
		JAXBElement<Invoice> invoiceElem = unmarshaller.unmarshal(source,Invoice.class);
		return invoiceElem.getValue();
	}
}
