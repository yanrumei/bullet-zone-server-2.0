/*    */ package org.springframework.boot.logging.logback;
/*    */ 
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.action.ActionUtil;
/*    */ import ch.qos.logback.core.joran.action.ActionUtil.Scope;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.xml.sax.Attributes;
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
/*    */ class SpringPropertyAction
/*    */   extends Action
/*    */ {
/*    */   private static final String SOURCE_ATTRIBUTE = "source";
/*    */   private static final String DEFAULT_VALUE_ATTRIBUTE = "defaultValue";
/*    */   private final Environment environment;
/*    */   
/*    */   SpringPropertyAction(Environment environment)
/*    */   {
/* 46 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   public void begin(InterpretationContext ic, String elementName, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 52 */     String name = attributes.getValue("name");
/* 53 */     String source = attributes.getValue("source");
/* 54 */     ActionUtil.Scope scope = ActionUtil.stringToScope(attributes.getValue("scope"));
/* 55 */     String defaultValue = attributes.getValue("defaultValue");
/* 56 */     if ((OptionHelper.isEmpty(name)) || (OptionHelper.isEmpty(source))) {
/* 57 */       addError("The \"name\" and \"source\" attributes of <springProperty> must be set");
/*    */     }
/*    */     
/* 60 */     ActionUtil.setProperty(ic, name, getValue(source, defaultValue), scope);
/*    */   }
/*    */   
/*    */   private String getValue(String source, String defaultValue) {
/* 64 */     if (this.environment == null) {
/* 65 */       addWarn("No Spring Environment available to resolve " + source);
/* 66 */       return defaultValue;
/*    */     }
/* 68 */     String value = this.environment.getProperty(source);
/* 69 */     if (value != null) {
/* 70 */       return value;
/*    */     }
/* 72 */     int lastDot = source.lastIndexOf(".");
/* 73 */     if (lastDot > 0) {
/* 74 */       String prefix = source.substring(0, lastDot + 1);
/* 75 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(this.environment, prefix);
/*    */       
/* 77 */       return resolver.getProperty(source.substring(lastDot + 1), defaultValue);
/*    */     }
/* 79 */     return defaultValue;
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ic, String name)
/*    */     throws ActionException
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\SpringPropertyAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */