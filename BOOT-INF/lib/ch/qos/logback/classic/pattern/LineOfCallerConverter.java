/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
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
/*    */ public class LineOfCallerConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   public String convert(ILoggingEvent le)
/*    */   {
/* 22 */     StackTraceElement[] cda = le.getCallerData();
/* 23 */     if ((cda != null) && (cda.length > 0)) {
/* 24 */       return Integer.toString(cda[0].getLineNumber());
/*    */     }
/* 26 */     return "?";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\pattern\LineOfCallerConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */