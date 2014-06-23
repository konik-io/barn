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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ZfXmpInfoTest {

   @Before
   public void setUp() throws Exception {
   }

   @Test
   public void isValid_No() throws Exception {

      ZfXmpInfo xmpInfo = new ZfXmpInfo();

      assertThat(xmpInfo.isValid()).isFalse();
   }
   
   @Test
   public void isValid_SomeIsMissing() throws Exception {

      ZfXmpInfo xmpInfo = new ZfXmpInfo("a", "b", "c", null);

      assertThat(xmpInfo.isValid()).isTrue();
   }

   @Test
   public void isValid_Yes() throws Exception {

      ZfXmpInfo xmpInfo = new ZfXmpInfo("a", "b", "c", "d");

      assertThat(xmpInfo.isValid()).isTrue();
   }
   

}
