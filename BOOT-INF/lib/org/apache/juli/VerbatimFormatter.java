/*    */ package org.apache.juli;
/*    */ 
/*    */ import java.util.logging.Formatter;
/*    */ import java.util.logging.LogRecord;
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
/*    */ public class VerbatimFormatter
/*    */   extends Formatter
/*    */ {
/*    */   public String format(LogRecord record)
/*    */   {
/* 35 */     StringBuilder sb = new StringBuilder(record.getMessage());
/*    */     
/*    */ 
/* 38 */     sb.append(System.lineSeparator());
/*    */     
/* 40 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\VerbatimFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */