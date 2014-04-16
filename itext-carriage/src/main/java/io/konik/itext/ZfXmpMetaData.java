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
package io.konik.itext;

/**
 * XMP ZUGFeRD PDF meta data.
 *
 */
public class ZfXmpMetaData {
   
   /** The conformance level. */
   private String conformanceLevel;
   
   /** The document file name. */
   private String documentFileName;
   
   /** The document type. */
   private String documentType;
   
   /** The version. */
   private String version;

   /**
    * Instantiates a new zf xmp meta data filled with default data;
    */
   public ZfXmpMetaData() {
      this("", "ZUGFeRD-invoice.xml", "INVOICE", "");
   }

   /**
    * Instantiates a new zf xmp meta data.
    *
    * @param conformanceLevel the conformance level
    * @param documentFileName the document file name
    * @param documentType the document type
    * @param version the version
    */
   public ZfXmpMetaData(String conformanceLevel, String documentFileName, String documentType, String version) {
      super();
      this.conformanceLevel = conformanceLevel;
      this.documentFileName = documentFileName;
      this.documentType = documentType;
      this.version = version;
   }

   /**
    * Gets the conformance level.
    *
    * @return the conformance level
    */
   public String getConformanceLevel() {
      return conformanceLevel;
   }

   /**
    * Sets the conformance level.
    *
    * @param conformanceLevel the new conformance level
    */
   public void setConformanceLevel(String conformanceLevel) {
      this.conformanceLevel = conformanceLevel;
   }

   /**
    * Gets the document file name.
    *
    * @return the document file name
    */
   public String getDocumentFileName() {
      return documentFileName;
   }

   /**
    * Sets the document file name.
    *
    * @param documentFileName the new document file name
    */
   public void setDocumentFileName(String documentFileName) {
      this.documentFileName = documentFileName;
   }

   /**
    * Gets the document type.
    *
    * @return the document type
    */
   public String getDocumentType() {
      return documentType;
   }

   /**
    * Sets the document type.
    *
    * @param documentType the new document type
    */
   public void setDocumentType(String documentType) {
      this.documentType = documentType;
   }

   /**
    * Gets the version.
    *
    * @return the version
    */
   public String getVersion() {
      return version;
   }

   /**
    * Sets the version.
    *
    * @param version the new version
    */
   public void setVersion(String version) {
      this.version = version;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((conformanceLevel == null) ? 0 : conformanceLevel.hashCode());
      result = prime * result + ((documentFileName == null) ? 0 : documentFileName.hashCode());
      result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
      result = prime * result + ((version == null) ? 0 : version.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      ZfXmpMetaData other = (ZfXmpMetaData) obj;
      if (conformanceLevel == null) {
         if (other.conformanceLevel != null) return false;
      } else if (!conformanceLevel.equals(other.conformanceLevel)) return false;
      if (documentFileName == null) {
         if (other.documentFileName != null) return false;
      } else if (!documentFileName.equals(other.documentFileName)) return false;
      if (documentType == null) {
         if (other.documentType != null) return false;
      } else if (!documentType.equals(other.documentType)) return false;
      if (version == null) {
         if (other.version != null) return false;
      } else if (!version.equals(other.version)) return false;
      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ZfXmpMetaData [conformanceLevel=").append(conformanceLevel).append(", documentFileName=")
            .append(documentFileName).append(", documentType=").append(documentType).append(", version=")
            .append(version).append("]");
      return builder.toString();
   }
   
   
   
   
   

}
