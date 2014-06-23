/*
 * Copyright (C) 2014 Konik.io
 *
 * This file is part of Konik library.
 *
 * Konik library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Konik library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Konik library.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.konik.itext.xmp;

/**
 * 
 * The Enum Containing ZUGFeRD XMP namespace.
 */
public enum XmpZfNs {

   /** The zf ns. */
   ZF_NS("urn:ferd:pdfa:invoice:rc#"),
   /** The zf ns alt. */
   ZF_NS_ALT("urn:ferd:pdfa:invoice:rc");

   /** The value. */
   public final String value;

   private XmpZfNs(String namespaceUri) {
      this.value = namespaceUri;
   }

}