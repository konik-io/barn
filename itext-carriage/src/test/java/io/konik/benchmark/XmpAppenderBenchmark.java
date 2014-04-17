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
package io.konik.benchmark;

import static com.google.common.io.ByteStreams.toByteArray;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openjdk.jmh.annotations.Mode.Throughput;
import static org.openjdk.jmh.annotations.Scope.Thread;
import io.konik.itext.xmp.XmpAppender;
import io.konik.itext.xmp.ZfXmpInfo;

import java.io.IOException;

import org.junit.Test;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.RunnerException;

@State(Thread)
@BenchmarkMode(Throughput)
@OutputTimeUnit(SECONDS)
public class XmpAppenderBenchmark extends DefaultBenchmark {

   private final XmpAppender xmpAppender = new XmpAppender();
   private final ZfXmpInfo metaDataReference = new ZfXmpInfo("BASIC", "ZUGFeRD-invoice.xml", "INVOICE", "RC");

   private byte[] acmeXmpContent;

   @Setup(Level.Iteration)
   public void setup() {
      try {
         acmeXmpContent = toByteArray(getClass().getResourceAsStream("/acme_xmp_sample.xml"));
      } catch (IOException e) {
         e.printStackTrace();
      }
      assertThat(acmeXmpContent).isNotEmpty();
   }
   
   @GenerateMicroBenchmark
   public void append() throws Exception {
      xmpAppender.append(acmeXmpContent, metaDataReference);
   }
   
   @GenerateMicroBenchmark
   @Threads(4)
   public void append_with4Threads3() throws Exception {
      xmpAppender.append(acmeXmpContent, metaDataReference);
   }

   @Test
   public void benchmark_XmpAppender() throws RunnerException {
      runDefault();
   }
}
