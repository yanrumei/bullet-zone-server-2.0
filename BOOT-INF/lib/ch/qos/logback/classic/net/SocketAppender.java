/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.net.AbstractSocketAppender;
/*    */ import ch.qos.logback.core.spi.PreSerializationTransformer;
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
/*    */ public class SocketAppender
/*    */   extends AbstractSocketAppender<ILoggingEvent>
/*    */ {
/* 36 */   private static final PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
/*    */   
/* 38 */   private boolean includeCallerData = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void postProcessEvent(ILoggingEvent event)
/*    */   {
/* 45 */     if (this.includeCallerData) {
/* 46 */       event.getCallerData();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setIncludeCallerData(boolean includeCallerData) {
/* 51 */     this.includeCallerData = includeCallerData;
/*    */   }
/*    */   
/*    */   public PreSerializationTransformer<ILoggingEvent> getPST() {
/* 55 */     return pst;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\SocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */