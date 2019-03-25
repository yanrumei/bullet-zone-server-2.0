/*    */ package org.apache.tomcat.util.log;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.PrintStream;
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
/*    */ class CaptureLog
/*    */ {
/*    */   private final ByteArrayOutputStream baos;
/*    */   private final PrintStream ps;
/*    */   
/*    */   protected CaptureLog()
/*    */   {
/* 31 */     this.baos = new ByteArrayOutputStream();
/* 32 */     this.ps = new PrintStream(this.baos);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected PrintStream getStream()
/*    */   {
/* 39 */     return this.ps;
/*    */   }
/*    */   
/*    */   protected void reset() {
/* 43 */     this.baos.reset();
/*    */   }
/*    */   
/*    */   protected String getCapture() {
/* 47 */     return this.baos.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\log\CaptureLog.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */