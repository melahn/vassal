/*
 * Copyright (c) 2020 by Joel Uckelman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package VASSAL.tools.io;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter implements Closeable {
  private final ZipOutputStream zout;

  public ZipWriter(File f) throws IOException {
    this(Objects.requireNonNull(f).toPath());
  }

  public ZipWriter(Path p) throws IOException {
    this(new BufferedOutputStream(Files.newOutputStream(Objects.requireNonNull(p))));
  } 

  public ZipWriter(OutputStream out) {
    zout = new ZipOutputStream(Objects.requireNonNull(out));
  }

  public void write(File src, String dst) throws IOException {
    write(src.toPath(), dst); 
  }

  public void write(Path src, String dst) throws IOException {
    final ZipEntry e = makeEntry(dst);
    e.setTime(Files.getLastModifiedTime(src).toMillis());
    zout.putNextEntry(e);

    try (InputStream in = Files.newInputStream(src)) {
      in.transferTo(zout);
    }
  }

  public void write(InputStream src, String dst) throws IOException {
    zout.putNextEntry(makeEntry(dst));
    src.transferTo(zout);
  }

  public void write(byte[] src, String dst) throws IOException {
    zout.putNextEntry(makeEntry(dst));
    zout.write(src, 0, src.length);
  }

  public OutputStream write(String dst) throws IOException {
    zout.putNextEntry(makeEntry(dst));
    return new FilterOutputStream(zout) {
      @Override
      public void close() throws IOException {
        // flush zout, but don't actually close it, since there may be
        // more entries to write after this
        zout.flush();
      }
    };
  }

  @Override
  public void close() throws IOException {
    zout.close();
  }

  private static ZipEntry makeEntry(String path) {
    final ZipEntry e = new ZipEntry(path);
    e.setMethod(ZipEntry.DEFLATED);
    return e;
  }
}
