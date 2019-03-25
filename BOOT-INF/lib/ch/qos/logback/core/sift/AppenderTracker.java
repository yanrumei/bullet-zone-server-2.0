/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.helpers.NOPAppender;
/*    */ import ch.qos.logback.core.joran.spi.JoranException;
/*    */ import ch.qos.logback.core.spi.AbstractComponentTracker;
/*    */ import ch.qos.logback.core.spi.ContextAwareImpl;
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
/*    */ public class AppenderTracker<E>
/*    */   extends AbstractComponentTracker<Appender<E>>
/*    */ {
/* 34 */   int nopaWarningCount = 0;
/*    */   
/*    */   final Context context;
/*    */   final AppenderFactory<E> appenderFactory;
/*    */   final ContextAwareImpl contextAware;
/*    */   
/*    */   public AppenderTracker(Context context, AppenderFactory<E> appenderFactory)
/*    */   {
/* 42 */     this.context = context;
/* 43 */     this.appenderFactory = appenderFactory;
/* 44 */     this.contextAware = new ContextAwareImpl(context, this);
/*    */   }
/*    */   
/*    */   protected void processPriorToRemoval(Appender<E> component)
/*    */   {
/* 49 */     component.stop();
/*    */   }
/*    */   
/*    */   protected Appender<E> buildComponent(String key)
/*    */   {
/* 54 */     Appender<E> appender = null;
/*    */     try {
/* 56 */       appender = this.appenderFactory.buildAppender(this.context, key);
/*    */     } catch (JoranException je) {
/* 58 */       this.contextAware.addError("Error while building appender with discriminating value [" + key + "]");
/*    */     }
/* 60 */     if (appender == null) {
/* 61 */       appender = buildNOPAppender(key);
/*    */     }
/*    */     
/* 64 */     return appender;
/*    */   }
/*    */   
/*    */   private NOPAppender<E> buildNOPAppender(String key) {
/* 68 */     if (this.nopaWarningCount < 4) {
/* 69 */       this.nopaWarningCount += 1;
/* 70 */       this.contextAware.addError("Building NOPAppender for discriminating value [" + key + "]");
/*    */     }
/* 72 */     NOPAppender<E> nopa = new NOPAppender();
/* 73 */     nopa.setContext(this.context);
/* 74 */     nopa.start();
/* 75 */     return nopa;
/*    */   }
/*    */   
/*    */   protected boolean isComponentStale(Appender<E> appender)
/*    */   {
/* 80 */     return !appender.isStarted();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\sift\AppenderTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */