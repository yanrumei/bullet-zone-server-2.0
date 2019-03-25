/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*    */ import ch.qos.logback.classic.spi.ThrowableProxyUtil;
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
/*    */ public class ExtendedThrowableProxyConverter
/*    */   extends ThrowableProxyConverter
/*    */ {
/*    */   protected void extraData(StringBuilder builder, StackTraceElementProxy step)
/*    */   {
/* 24 */     ThrowableProxyUtil.subjoinPackagingData(builder, step);
/*    */   }
/*    */   
/*    */   protected void prepareLoggingEvent(ILoggingEvent event) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\pattern\ExtendedThrowableProxyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */