/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.pattern.Converter;
/*    */ import ch.qos.logback.core.pattern.ConverterUtil;
/*    */ import ch.qos.logback.core.pattern.PostCompileProcessor;
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
/*    */ public class EnsureExceptionHandling
/*    */   implements PostCompileProcessor<ILoggingEvent>
/*    */ {
/*    */   public void process(Context context, Converter<ILoggingEvent> head)
/*    */   {
/* 41 */     if (head == null)
/*    */     {
/* 43 */       throw new IllegalArgumentException("cannot process empty chain");
/*    */     }
/* 45 */     if (!chainHandlesThrowable(head)) {
/* 46 */       Converter<ILoggingEvent> tail = ConverterUtil.findTail(head);
/* 47 */       Converter<ILoggingEvent> exConverter = null;
/* 48 */       LoggerContext loggerContext = (LoggerContext)context;
/* 49 */       if (loggerContext.isPackagingDataEnabled()) {
/* 50 */         exConverter = new ExtendedThrowableProxyConverter();
/*    */       } else {
/* 52 */         exConverter = new ThrowableProxyConverter();
/*    */       }
/* 54 */       tail.setNext(exConverter);
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
/*    */   public boolean chainHandlesThrowable(Converter<ILoggingEvent> head)
/*    */   {
/* 67 */     Converter<ILoggingEvent> c = head;
/* 68 */     while (c != null) {
/* 69 */       if ((c instanceof ThrowableHandlingConverter)) {
/* 70 */         return true;
/*    */       }
/* 72 */       c = c.getNext();
/*    */     }
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\pattern\EnsureExceptionHandling.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */