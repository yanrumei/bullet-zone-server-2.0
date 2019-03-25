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
/*    */ 
/*    */ public class ClassOfCallerConverter
/*    */   extends NamedConverter
/*    */ {
/*    */   protected String getFullyQualifiedName(ILoggingEvent event)
/*    */   {
/* 23 */     StackTraceElement[] cda = event.getCallerData();
/* 24 */     if ((cda != null) && (cda.length > 0)) {
/* 25 */       return cda[0].getClassName();
/*    */     }
/* 27 */     return "?";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\pattern\ClassOfCallerConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */