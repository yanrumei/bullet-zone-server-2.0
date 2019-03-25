/*    */ package org.apache.catalina.ssi;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import javax.servlet.ServletOutputStream;
/*    */ import javax.servlet.WriteListener;
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
/*    */ public class ByteArrayServletOutputStream
/*    */   extends ServletOutputStream
/*    */ {
/*    */   protected final ByteArrayOutputStream buf;
/*    */   
/*    */   public ByteArrayServletOutputStream()
/*    */   {
/* 44 */     this.buf = new ByteArrayOutputStream();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] toByteArray()
/*    */   {
/* 52 */     return this.buf.toByteArray();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void write(int b)
/*    */   {
/* 63 */     this.buf.write(b);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isReady()
/*    */   {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public void setWriteListener(WriteListener listener) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\ByteArrayServletOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */