/*     */ package org.apache.tomcat.util.http.fileupload.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tomcat.util.http.fileupload.IOUtils;
/*     */ import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
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
/*     */ public final class Streams
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   
/*     */   public static long copy(InputStream inputStream, OutputStream outputStream, boolean closeOutputStream)
/*     */     throws IOException
/*     */   {
/*  68 */     return copy(inputStream, outputStream, closeOutputStream, new byte[' ']);
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
/*     */   public static long copy(InputStream inputStream, OutputStream outputStream, boolean closeOutputStream, byte[] buffer)
/*     */     throws IOException
/*     */   {
/*  93 */     OutputStream out = outputStream;
/*  94 */     InputStream in = inputStream;
/*     */     try {
/*  96 */       long total = 0L;
/*     */       int res;
/*  98 */       for (;;) { res = in.read(buffer);
/*  99 */         if (res == -1) {
/*     */           break;
/*     */         }
/* 102 */         if (res > 0) {
/* 103 */           total += res;
/* 104 */           if (out != null) {
/* 105 */             out.write(buffer, 0, res);
/*     */           }
/*     */         }
/*     */       }
/* 109 */       if (out != null) {
/* 110 */         if (closeOutputStream) {
/* 111 */           out.close();
/*     */         } else {
/* 113 */           out.flush();
/*     */         }
/* 115 */         out = null;
/*     */       }
/* 117 */       in.close();
/* 118 */       in = null;
/* 119 */       return total;
/*     */     } finally {
/* 121 */       IOUtils.closeQuietly(in);
/* 122 */       if (closeOutputStream) {
/* 123 */         IOUtils.closeQuietly(out);
/*     */       }
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
/*     */   public static String asString(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 140 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 141 */     copy(inputStream, baos, true);
/* 142 */     return baos.toString();
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
/*     */   public static String asString(InputStream inputStream, String encoding)
/*     */     throws IOException
/*     */   {
/* 157 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 158 */     copy(inputStream, baos, true);
/* 159 */     return baos.toString(encoding);
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
/*     */   public static String checkFileName(String fileName)
/*     */   {
/* 173 */     if ((fileName != null) && (fileName.indexOf(0) != -1))
/*     */     {
/* 175 */       StringBuilder sb = new StringBuilder();
/* 176 */       for (int i = 0; i < fileName.length(); i++) {
/* 177 */         char c = fileName.charAt(i);
/* 178 */         switch (c) {
/*     */         case '\000': 
/* 180 */           sb.append("\\0");
/* 181 */           break;
/*     */         default: 
/* 183 */           sb.append(c);
/*     */         }
/*     */         
/*     */       }
/* 187 */       throw new InvalidFileNameException(fileName, "Invalid file name: " + sb);
/*     */     }
/*     */     
/* 190 */     return fileName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\Streams.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */