package io.konik.itext.xmp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

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
