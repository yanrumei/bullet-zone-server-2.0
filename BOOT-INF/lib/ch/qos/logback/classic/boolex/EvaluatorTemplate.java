/*    */ package ch.qos.logback.classic.boolex;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import groovy.lang.GroovyObject;
/*    */ import groovy.lang.MetaClass;
/*    */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*    */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
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
/*    */ public class EvaluatorTemplate
/*    */   implements IEvaluator, GroovyObject
/*    */ {
/*    */   public EvaluatorTemplate()
/*    */   {
/*    */     EvaluatorTemplate this;
/*    */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*    */     MetaClass localMetaClass = $getStaticMetaClass();
/*    */     this.metaClass = localMetaClass;
/*    */   }
/*    */   
/*    */   public boolean doEvaluate(ILoggingEvent event)
/*    */   {
/* 34 */     CallSite[] arrayOfCallSite = $getCallSiteArray();ILoggingEvent e = event;return DefaultTypeTransformation.booleanUnbox(e);return DefaultTypeTransformation.booleanUnbox(Integer.valueOf(0));
/*    */   }
/*    */   
/*    */   static {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\boolex\EvaluatorTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */