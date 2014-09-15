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
package io.konik.carriage.itext.benchmark;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.Mode.Throughput;
import static org.openjdk.jmh.annotations.Scope.Thread;
import io.konik.carriage.itext.ITextInvoiceAppender;
import io.konik.harness.appender.DefaultAppendParameter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.RunnerException;

@SuppressWarnings("javadoc")
@State(Thread)
@BenchmarkMode(Throughput)
@OutputTimeUnit(SECONDS)
public class ITextPdfInvoiceAppenderBenchmark extends DefaultBenchmark {

  
   private final ITextInvoiceAppender appender = new ITextInvoiceAppender();

   private DefaultAppendParameter createParameter() {
      InputStream inputStreamXml = getClass().getResourceAsStream("/ZUGFeRD-invoice.xml");
      InputStream inputStreamPdf = getClass().getResourceAsStream("/acme_invoice-42.pdf");
      OutputStream resultingPdf = new ByteArrayOutputStream();
      DefaultAppendParameter parameter = new DefaultAppendParameter(inputStreamPdf, inputStreamXml, resultingPdf , "1.0", "BASIC");
      return parameter;
   }
   
   @Benchmark
   public void append() throws Exception {
      appender.append(createParameter());
   }

   @Benchmark
   @Threads(4)
   public void append_threaded() throws Exception {
      appender.append(createParameter());
   }
   
   @Test
   public void benchmark_iTextPdfInvoiceAppender() throws RunnerException {
      runDefault();
   }
   
}
