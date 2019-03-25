/*     */ package org.apache.tomcat.util.http.fileupload.util.mime;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ final class QuotedPrintableDecoder
/*     */ {
/*     */   private static final int UPPER_NIBBLE_SHIFT = 4;
/*     */   
/*     */   public static int decode(byte[] data, OutputStream out)
/*     */     throws IOException
/*     */   {
/*  51 */     int off = 0;
/*  52 */     int length = data.length;
/*  53 */     int endOffset = off + length;
/*  54 */     int bytesWritten = 0;
/*     */     
/*  56 */     while (off < endOffset) {
/*  57 */       byte ch = data[(off++)];
/*     */       
/*     */ 
/*  60 */       if (ch == 95) {
/*  61 */         out.write(32);
/*  62 */       } else if (ch == 61)
/*     */       {
/*     */ 
/*  65 */         if (off + 1 >= endOffset) {
/*  66 */           throw new IOException("Invalid quoted printable encoding; truncated escape sequence");
/*     */         }
/*     */         
/*  69 */         byte b1 = data[(off++)];
/*  70 */         byte b2 = data[(off++)];
/*     */         
/*     */ 
/*  73 */         if (b1 == 13) {
/*  74 */           if (b2 != 10) {
/*  75 */             throw new IOException("Invalid quoted printable encoding; CR must be followed by LF");
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/*  81 */           int c1 = hexToBinary(b1);
/*  82 */           int c2 = hexToBinary(b2);
/*  83 */           out.write(c1 << 4 | c2);
/*     */           
/*  85 */           bytesWritten++;
/*     */         }
/*     */       }
/*     */       else {
/*  89 */         out.write(ch);
/*  90 */         bytesWritten++;
/*     */       }
/*     */     }
/*     */     
/*  94 */     return bytesWritten;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int hexToBinary(byte b)
/*     */     throws IOException
/*     */   {
/* 106 */     int i = Character.digit((char)b, 16);
/* 107 */     if (i == -1) {
/* 108 */       throw new IOException("Invalid quoted printable encoding: not a valid hex digit: " + b);
/*     */     }
/* 110 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\mime\QuotedPrintableDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */