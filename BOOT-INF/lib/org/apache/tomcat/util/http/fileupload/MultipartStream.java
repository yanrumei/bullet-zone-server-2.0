/*      */ package org.apache.tomcat.util.http.fileupload;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import org.apache.tomcat.util.http.fileupload.util.Closeable;
/*      */ import org.apache.tomcat.util.http.fileupload.util.Streams;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MultipartStream
/*      */ {
/*      */   public static final byte CR = 13;
/*      */   public static final byte LF = 10;
/*      */   public static final byte DASH = 45;
/*      */   public static final int HEADER_PART_SIZE_MAX = 10240;
/*      */   protected static final int DEFAULT_BUFSIZE = 4096;
/*      */   
/*      */   public static class ProgressNotifier
/*      */   {
/*      */     private final ProgressListener listener;
/*      */     private final long contentLength;
/*      */     private long bytesRead;
/*      */     private int items;
/*      */     
/*      */     ProgressNotifier(ProgressListener pListener, long pContentLength)
/*      */     {
/*  119 */       this.listener = pListener;
/*  120 */       this.contentLength = pContentLength;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     void noteBytesRead(int pBytes)
/*      */     {
/*  132 */       this.bytesRead += pBytes;
/*  133 */       notifyListener();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void noteItem()
/*      */     {
/*  140 */       this.items += 1;
/*  141 */       notifyListener();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void notifyListener()
/*      */     {
/*  148 */       if (this.listener != null) {
/*  149 */         this.listener.update(this.bytesRead, this.contentLength, this.items);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  187 */   protected static final byte[] HEADER_SEPARATOR = { 13, 10, 13, 10 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  193 */   protected static final byte[] FIELD_SEPARATOR = { 13, 10 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  199 */   protected static final byte[] STREAM_TERMINATOR = { 45, 45 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  204 */   protected static final byte[] BOUNDARY_PREFIX = { 13, 10, 45, 45 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final InputStream input;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int boundaryLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int keepRegion;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final byte[] boundary;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] boundaryTable;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int bufSize;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final byte[] buffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int head;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int tail;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String headerEncoding;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ProgressNotifier notifier;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MultipartStream(InputStream input, byte[] boundary, int bufSize, ProgressNotifier pNotifier)
/*      */   {
/*  294 */     if (boundary == null) {
/*  295 */       throw new IllegalArgumentException("boundary may not be null");
/*      */     }
/*      */     
/*      */ 
/*  299 */     this.boundaryLength = (boundary.length + BOUNDARY_PREFIX.length);
/*  300 */     if (bufSize < this.boundaryLength + 1) {
/*  301 */       throw new IllegalArgumentException("The buffer size specified for the MultipartStream is too small");
/*      */     }
/*      */     
/*      */ 
/*  305 */     this.input = input;
/*  306 */     this.bufSize = Math.max(bufSize, this.boundaryLength * 2);
/*  307 */     this.buffer = new byte[this.bufSize];
/*  308 */     this.notifier = pNotifier;
/*      */     
/*  310 */     this.boundary = new byte[this.boundaryLength];
/*  311 */     this.boundaryTable = new int[this.boundaryLength + 1];
/*  312 */     this.keepRegion = this.boundary.length;
/*      */     
/*  314 */     System.arraycopy(BOUNDARY_PREFIX, 0, this.boundary, 0, BOUNDARY_PREFIX.length);
/*      */     
/*  316 */     System.arraycopy(boundary, 0, this.boundary, BOUNDARY_PREFIX.length, boundary.length);
/*      */     
/*  318 */     computeBoundaryTable();
/*      */     
/*  320 */     this.head = 0;
/*  321 */     this.tail = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   MultipartStream(InputStream input, byte[] boundary, ProgressNotifier pNotifier)
/*      */   {
/*  338 */     this(input, boundary, 4096, pNotifier);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getHeaderEncoding()
/*      */   {
/*  351 */     return this.headerEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHeaderEncoding(String encoding)
/*      */   {
/*  362 */     this.headerEncoding = encoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte readByte()
/*      */     throws IOException
/*      */   {
/*  375 */     if (this.head == this.tail) {
/*  376 */       this.head = 0;
/*      */       
/*  378 */       this.tail = this.input.read(this.buffer, this.head, this.bufSize);
/*  379 */       if (this.tail == -1)
/*      */       {
/*  381 */         throw new IOException("No more data is available");
/*      */       }
/*  383 */       if (this.notifier != null) {
/*  384 */         this.notifier.noteBytesRead(this.tail);
/*      */       }
/*      */     }
/*  387 */     return this.buffer[(this.head++)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean readBoundary()
/*      */     throws FileUploadBase.FileUploadIOException, MultipartStream.MalformedStreamException
/*      */   {
/*  403 */     byte[] marker = new byte[2];
/*  404 */     boolean nextChunk = false;
/*      */     
/*  406 */     this.head += this.boundaryLength;
/*      */     try {
/*  408 */       marker[0] = readByte();
/*  409 */       if (marker[0] == 10)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  416 */         return true;
/*      */       }
/*      */       
/*  419 */       marker[1] = readByte();
/*  420 */       if (arrayequals(marker, STREAM_TERMINATOR, 2)) {
/*  421 */         nextChunk = false;
/*  422 */       } else if (arrayequals(marker, FIELD_SEPARATOR, 2)) {
/*  423 */         nextChunk = true;
/*      */       } else {
/*  425 */         throw new MalformedStreamException("Unexpected characters follow a boundary");
/*      */       }
/*      */     }
/*      */     catch (FileUploadBase.FileUploadIOException e)
/*      */     {
/*  430 */       throw e;
/*      */     } catch (IOException e) {
/*  432 */       throw new MalformedStreamException("Stream ended unexpectedly");
/*      */     }
/*  434 */     return nextChunk;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBoundary(byte[] boundary)
/*      */     throws MultipartStream.IllegalBoundaryException
/*      */   {
/*  458 */     if (boundary.length != this.boundaryLength - BOUNDARY_PREFIX.length) {
/*  459 */       throw new IllegalBoundaryException("The length of a boundary token cannot be changed");
/*      */     }
/*      */     
/*  462 */     System.arraycopy(boundary, 0, this.boundary, BOUNDARY_PREFIX.length, boundary.length);
/*      */     
/*  464 */     computeBoundaryTable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void computeBoundaryTable()
/*      */   {
/*  471 */     int position = 2;
/*  472 */     int candidate = 0;
/*      */     
/*  474 */     this.boundaryTable[0] = -1;
/*  475 */     this.boundaryTable[1] = 0;
/*      */     
/*  477 */     while (position <= this.boundaryLength) {
/*  478 */       if (this.boundary[(position - 1)] == this.boundary[candidate]) {
/*  479 */         this.boundaryTable[position] = (candidate + 1);
/*  480 */         candidate++;
/*  481 */         position++;
/*  482 */       } else if (candidate > 0) {
/*  483 */         candidate = this.boundaryTable[candidate];
/*      */       } else {
/*  485 */         this.boundaryTable[position] = 0;
/*  486 */         position++;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String readHeaders()
/*      */     throws FileUploadBase.FileUploadIOException, MultipartStream.MalformedStreamException
/*      */   {
/*  508 */     int i = 0;
/*      */     
/*      */ 
/*  511 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  512 */     int size = 0;
/*  513 */     while (i < HEADER_SEPARATOR.length) {
/*      */       try {
/*  515 */         b = readByte();
/*      */       } catch (FileUploadBase.FileUploadIOException e) {
/*      */         byte b;
/*  518 */         throw e;
/*      */       } catch (IOException e) {
/*  520 */         throw new MalformedStreamException("Stream ended unexpectedly"); }
/*      */       byte b;
/*  522 */       size++; if (size > 10240) {
/*  523 */         throw new MalformedStreamException(String.format("Header section has more than %s bytes (maybe it is not properly terminated)", new Object[] {
/*      */         
/*  525 */           Integer.valueOf(10240) }));
/*      */       }
/*  527 */       if (b == HEADER_SEPARATOR[i]) {
/*  528 */         i++;
/*      */       } else {
/*  530 */         i = 0;
/*      */       }
/*  532 */       baos.write(b);
/*      */     }
/*      */     
/*  535 */     String headers = null;
/*  536 */     if (this.headerEncoding != null) {
/*      */       try {
/*  538 */         headers = baos.toString(this.headerEncoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException e)
/*      */       {
/*  542 */         headers = baos.toString();
/*      */       }
/*      */     } else {
/*  545 */       headers = baos.toString();
/*      */     }
/*      */     
/*  548 */     return headers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readBodyData(OutputStream output)
/*      */     throws MultipartStream.MalformedStreamException, IOException
/*      */   {
/*  572 */     return (int)Streams.copy(newInputStream(), output, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   ItemInputStream newInputStream()
/*      */   {
/*  580 */     return new ItemInputStream();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int discardBodyData()
/*      */     throws MultipartStream.MalformedStreamException, IOException
/*      */   {
/*  596 */     return readBodyData(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean skipPreamble()
/*      */     throws IOException
/*      */   {
/*  609 */     System.arraycopy(this.boundary, 2, this.boundary, 0, this.boundary.length - 2);
/*  610 */     this.boundaryLength = (this.boundary.length - 2);
/*  611 */     computeBoundaryTable();
/*      */     try
/*      */     {
/*  614 */       discardBodyData();
/*      */       
/*      */ 
/*      */ 
/*  618 */       return readBoundary();
/*      */     } catch (MalformedStreamException e) {
/*  620 */       return false;
/*      */     }
/*      */     finally {
/*  623 */       System.arraycopy(this.boundary, 0, this.boundary, 2, this.boundary.length - 2);
/*  624 */       this.boundaryLength = this.boundary.length;
/*  625 */       this.boundary[0] = 13;
/*  626 */       this.boundary[1] = 10;
/*  627 */       computeBoundaryTable();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean arrayequals(byte[] a, byte[] b, int count)
/*      */   {
/*  645 */     for (int i = 0; i < count; i++) {
/*  646 */       if (a[i] != b[i]) {
/*  647 */         return false;
/*      */       }
/*      */     }
/*  650 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int findByte(byte value, int pos)
/*      */   {
/*  665 */     for (int i = pos; i < this.tail; i++) {
/*  666 */       if (this.buffer[i] == value) {
/*  667 */         return i;
/*      */       }
/*      */     }
/*      */     
/*  671 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int findSeparator()
/*      */   {
/*  684 */     int bufferPos = this.head;
/*  685 */     int tablePos = 0;
/*      */     
/*  687 */     while (bufferPos < this.tail) {
/*  688 */       while ((tablePos >= 0) && (this.buffer[bufferPos] != this.boundary[tablePos])) {
/*  689 */         tablePos = this.boundaryTable[tablePos];
/*      */       }
/*  691 */       bufferPos++;
/*  692 */       tablePos++;
/*  693 */       if (tablePos == this.boundaryLength) {
/*  694 */         return bufferPos - this.boundaryLength;
/*      */       }
/*      */     }
/*  697 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class MalformedStreamException
/*      */     extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = 6466926458059796677L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MalformedStreamException() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MalformedStreamException(String message)
/*      */     {
/*  726 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class IllegalBoundaryException
/*      */     extends IOException
/*      */   {
/*      */     private static final long serialVersionUID = -161533165102632918L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public IllegalBoundaryException() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public IllegalBoundaryException(String message)
/*      */     {
/*  756 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class ItemInputStream
/*      */     extends InputStream
/*      */     implements Closeable
/*      */   {
/*      */     private long total;
/*      */     
/*      */ 
/*      */ 
/*      */     private int pad;
/*      */     
/*      */ 
/*      */ 
/*      */     private int pos;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean closed;
/*      */     
/*      */ 
/*      */ 
/*      */     private static final int BYTE_POSITIVE_OFFSET = 256;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     ItemInputStream()
/*      */     {
/*  791 */       findSeparator();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void findSeparator()
/*      */     {
/*  798 */       this.pos = MultipartStream.this.findSeparator();
/*  799 */       if (this.pos == -1) {
/*  800 */         if (MultipartStream.this.tail - MultipartStream.this.head > MultipartStream.this.keepRegion) {
/*  801 */           this.pad = MultipartStream.this.keepRegion;
/*      */         } else {
/*  803 */           this.pad = (MultipartStream.this.tail - MultipartStream.this.head);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public long getBytesRead()
/*      */     {
/*  815 */       return this.total;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int available()
/*      */       throws IOException
/*      */     {
/*  827 */       if (this.pos == -1) {
/*  828 */         return MultipartStream.this.tail - MultipartStream.this.head - this.pad;
/*      */       }
/*  830 */       return this.pos - MultipartStream.this.head;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int read()
/*      */       throws IOException
/*      */     {
/*  847 */       if (this.closed) {
/*  848 */         throw new FileItemStream.ItemSkippedException();
/*      */       }
/*  850 */       if ((available() == 0) && (makeAvailable() == 0)) {
/*  851 */         return -1;
/*      */       }
/*  853 */       this.total += 1L;
/*  854 */       int b = MultipartStream.this.buffer[MultipartStream.access$108(MultipartStream.this)];
/*  855 */       if (b >= 0) {
/*  856 */         return b;
/*      */       }
/*  858 */       return b + 256;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int read(byte[] b, int off, int len)
/*      */       throws IOException
/*      */     {
/*  873 */       if (this.closed) {
/*  874 */         throw new FileItemStream.ItemSkippedException();
/*      */       }
/*  876 */       if (len == 0) {
/*  877 */         return 0;
/*      */       }
/*  879 */       int res = available();
/*  880 */       if (res == 0) {
/*  881 */         res = makeAvailable();
/*  882 */         if (res == 0) {
/*  883 */           return -1;
/*      */         }
/*      */       }
/*  886 */       res = Math.min(res, len);
/*  887 */       System.arraycopy(MultipartStream.this.buffer, MultipartStream.this.head, b, off, res);
/*  888 */       MultipartStream.this.head = (MultipartStream.this.head + res);
/*  889 */       this.total += res;
/*  890 */       return res;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/*  900 */       close(false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void close(boolean pCloseUnderlying)
/*      */       throws IOException
/*      */     {
/*  911 */       if (this.closed) {
/*  912 */         return;
/*      */       }
/*  914 */       if (pCloseUnderlying) {
/*  915 */         this.closed = true;
/*  916 */         MultipartStream.this.input.close();
/*      */       } else {
/*      */         for (;;) {
/*  919 */           int av = available();
/*  920 */           if (av == 0) {
/*  921 */             av = makeAvailable();
/*  922 */             if (av == 0) {
/*      */               break;
/*      */             }
/*      */           }
/*  926 */           skip(av);
/*      */         }
/*      */       }
/*  929 */       this.closed = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public long skip(long bytes)
/*      */       throws IOException
/*      */     {
/*  942 */       if (this.closed) {
/*  943 */         throw new FileItemStream.ItemSkippedException();
/*      */       }
/*  945 */       int av = available();
/*  946 */       if (av == 0) {
/*  947 */         av = makeAvailable();
/*  948 */         if (av == 0) {
/*  949 */           return 0L;
/*      */         }
/*      */       }
/*  952 */       long res = Math.min(av, bytes);
/*  953 */       MultipartStream.this.head = ((int)(MultipartStream.this.head + res));
/*  954 */       return res;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int makeAvailable()
/*      */       throws IOException
/*      */     {
/*  964 */       if (this.pos != -1) {
/*  965 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  969 */       this.total += MultipartStream.this.tail - MultipartStream.this.head - this.pad;
/*  970 */       System.arraycopy(MultipartStream.this.buffer, MultipartStream.this.tail - this.pad, MultipartStream.this.buffer, 0, this.pad);
/*      */       
/*      */ 
/*  973 */       MultipartStream.this.head = 0;
/*  974 */       MultipartStream.this.tail = this.pad;
/*      */       for (;;)
/*      */       {
/*  977 */         int bytesRead = MultipartStream.this.input.read(MultipartStream.this.buffer, MultipartStream.this.tail, MultipartStream.this.bufSize - MultipartStream.this.tail);
/*  978 */         if (bytesRead == -1)
/*      */         {
/*      */ 
/*      */ 
/*  982 */           String msg = "Stream ended unexpectedly";
/*  983 */           throw new MultipartStream.MalformedStreamException("Stream ended unexpectedly");
/*      */         }
/*  985 */         if (MultipartStream.this.notifier != null) {
/*  986 */           MultipartStream.this.notifier.noteBytesRead(bytesRead);
/*      */         }
/*  988 */         MultipartStream.this.tail = (MultipartStream.this.tail + bytesRead);
/*      */         
/*  990 */         findSeparator();
/*  991 */         int av = available();
/*      */         
/*  993 */         if ((av > 0) || (this.pos != -1)) {
/*  994 */           return av;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isClosed()
/*      */     {
/* 1006 */       return this.closed;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\MultipartStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */