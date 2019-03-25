/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtils
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*     */   
/*     */   public static void closeQuietly(Closeable closeable)
/*     */   {
/*     */     try
/*     */     {
/* 115 */       if (closeable != null) {
/* 116 */         closeable.close();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int copy(InputStream input, OutputStream output)
/*     */     throws IOException
/*     */   {
/* 145 */     long count = copyLarge(input, output);
/* 146 */     if (count > 2147483647L) {
/* 147 */       return -1;
/*     */     }
/* 149 */     return (int)count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long copyLarge(InputStream input, OutputStream output)
/*     */     throws IOException
/*     */   {
/* 171 */     byte[] buffer = new byte['က'];
/* 172 */     long count = 0L;
/* 173 */     int n = 0;
/* 174 */     while (-1 != (n = input.read(buffer))) {
/* 175 */       output.write(buffer, 0, n);
/* 176 */       count += n;
/*     */     }
/* 178 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int read(InputStream input, byte[] buffer, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 197 */     if (length < 0) {
/* 198 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*     */     }
/* 200 */     int remaining = length;
/* 201 */     while (remaining > 0) {
/* 202 */       int location = length - remaining;
/* 203 */       int count = input.read(buffer, offset + location, remaining);
/* 204 */       if (-1 == count) {
/*     */         break;
/*     */       }
/* 207 */       remaining -= count;
/*     */     }
/* 209 */     return length - remaining;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void readFully(InputStream input, byte[] buffer, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 230 */     int actual = read(input, buffer, offset, length);
/* 231 */     if (actual != length) {
/* 232 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void readFully(InputStream input, byte[] buffer)
/*     */     throws IOException
/*     */   {
/* 251 */     readFully(input, buffer, 0, buffer.length);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\IOUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */