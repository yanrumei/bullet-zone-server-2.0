/*    */ package org.apache.catalina.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IOTools
/*    */ {
/*    */   protected static final int DEFAULT_BUFFER_SIZE = 4096;
/*    */   
/*    */   public static void flow(Reader reader, Writer writer, char[] buf)
/*    */     throws IOException
/*    */   {
/*    */     int numRead;
/* 50 */     while ((numRead = reader.read(buf)) >= 0) {
/* 51 */       writer.write(buf, 0, numRead);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void flow(Reader reader, Writer writer)
/*    */     throws IOException
/*    */   {
/* 66 */     char[] buf = new char['က'];
/* 67 */     flow(reader, writer, buf);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void flow(InputStream is, OutputStream os)
/*    */     throws IOException
/*    */   {
/* 82 */     byte[] buf = new byte['က'];
/*    */     int numRead;
/* 84 */     while ((numRead = is.read(buf)) >= 0) {
/* 85 */       os.write(buf, 0, numRead);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\IOTools.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */