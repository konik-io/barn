package io.konik.itext.appender;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;

public class XmpCreator {

   
  public void readMetaData(PdfReader reader) throws IOException{
      byte XMP_MetaData[] = reader.getMetadata();
   }
}
