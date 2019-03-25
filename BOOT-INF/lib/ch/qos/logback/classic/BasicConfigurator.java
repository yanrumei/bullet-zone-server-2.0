/*    */ package ch.qos.logback.classic;
/*    */ 
/*    */ import ch.qos.logback.classic.layout.TTLLLayout;
/*    */ import ch.qos.logback.classic.spi.Configurator;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.ConsoleAppender;
/*    */ import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*    */ public class BasicConfigurator
/*    */   extends ContextAwareBase
/*    */   implements Configurator
/*    */ {
/*    */   public void configure(LoggerContext lc)
/*    */   {
/* 36 */     addInfo("Setting up default configuration.");
/*    */     
/* 38 */     ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender();
/* 39 */     ca.setContext(lc);
/* 40 */     ca.setName("console");
/* 41 */     LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder();
/* 42 */     encoder.setContext(lc);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */     TTLLLayout layout = new TTLLLayout();
/*    */     
/* 50 */     layout.setContext(lc);
/* 51 */     layout.start();
/* 52 */     encoder.setLayout(layout);
/*    */     
/* 54 */     ca.setEncoder(encoder);
/* 55 */     ca.start();
/*    */     
/* 57 */     Logger rootLogger = lc.getLogger("ROOT");
/* 58 */     rootLogger.addAppender(ca);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\BasicConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */