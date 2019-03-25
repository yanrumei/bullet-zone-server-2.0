/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.joran.GenericConfigurator;
/*    */ import ch.qos.logback.core.joran.action.DefinePropertyAction;
/*    */ import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
/*    */ import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
/*    */ import ch.qos.logback.core.joran.action.PropertyAction;
/*    */ import ch.qos.logback.core.joran.action.TimestampAction;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.Interpreter;
/*    */ import ch.qos.logback.core.joran.spi.JoranException;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public abstract class SiftingJoranConfiguratorBase<E>
/*    */   extends GenericConfigurator
/*    */ {
/*    */   protected final String key;
/*    */   protected final String value;
/*    */   protected final Map<String, String> parentPropertyMap;
/*    */   static final String ONE_AND_ONLY_ONE_URL = "http://logback.qos.ch/codes.html#1andOnly1";
/*    */   
/*    */   protected SiftingJoranConfiguratorBase(String key, String value, Map<String, String> parentPropertyMap)
/*    */   {
/* 37 */     this.key = key;
/* 38 */     this.value = value;
/* 39 */     this.parentPropertyMap = parentPropertyMap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void addImplicitRules(Interpreter interpreter)
/*    */   {
/* 46 */     NestedComplexPropertyIA nestedComplexIA = new NestedComplexPropertyIA(getBeanDescriptionCache());
/* 47 */     nestedComplexIA.setContext(this.context);
/* 48 */     interpreter.addImplicitAction(nestedComplexIA);
/*    */     
/* 50 */     NestedBasicPropertyIA nestedSimpleIA = new NestedBasicPropertyIA(getBeanDescriptionCache());
/* 51 */     nestedSimpleIA.setContext(this.context);
/* 52 */     interpreter.addImplicitAction(nestedSimpleIA);
/*    */   }
/*    */   
/*    */   protected void addInstanceRules(RuleStore rs)
/*    */   {
/* 57 */     rs.addRule(new ElementSelector("configuration/property"), new PropertyAction());
/* 58 */     rs.addRule(new ElementSelector("configuration/timestamp"), new TimestampAction());
/* 59 */     rs.addRule(new ElementSelector("configuration/define"), new DefinePropertyAction());
/*    */   }
/*    */   
/*    */   public abstract Appender<E> getAppender();
/*    */   
/* 64 */   int errorEmmissionCount = 0;
/*    */   
/*    */   protected void oneAndOnlyOneCheck(Map<?, ?> appenderMap) {
/* 67 */     String errMsg = null;
/* 68 */     if (appenderMap.size() == 0) {
/* 69 */       this.errorEmmissionCount += 1;
/* 70 */       errMsg = "No nested appenders found within the <sift> element in SiftingAppender.";
/* 71 */     } else if (appenderMap.size() > 1) {
/* 72 */       this.errorEmmissionCount += 1;
/* 73 */       errMsg = "Only and only one appender can be nested the <sift> element in SiftingAppender. See also http://logback.qos.ch/codes.html#1andOnly1";
/*    */     }
/*    */     
/* 76 */     if ((errMsg != null) && (this.errorEmmissionCount < 4)) {
/* 77 */       addError(errMsg);
/*    */     }
/*    */   }
/*    */   
/*    */   public void doConfigure(List<SaxEvent> eventList) throws JoranException {
/* 82 */     super.doConfigure(eventList);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 87 */     return getClass().getName() + "{" + this.key + "=" + this.value + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\sift\SiftingJoranConfiguratorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */