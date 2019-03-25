/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.spi.Interpreter;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import ch.qos.logback.core.util.OptionHelper;
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
/*    */ public class NewRuleAction
/*    */   extends Action
/*    */ {
/* 23 */   boolean inError = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*    */   {
/* 30 */     this.inError = false;
/*    */     
/* 32 */     String pattern = attributes.getValue("pattern");
/* 33 */     String actionClass = attributes.getValue("actionClass");
/*    */     
/* 35 */     if (OptionHelper.isEmpty(pattern)) {
/* 36 */       this.inError = true;
/* 37 */       String errorMsg = "No 'pattern' attribute in <newRule>";
/* 38 */       addError(errorMsg);
/* 39 */       return;
/*    */     }
/*    */     
/* 42 */     if (OptionHelper.isEmpty(actionClass)) {
/* 43 */       this.inError = true;
/* 44 */       String errorMsg = "No 'actionClass' attribute in <newRule>";
/* 45 */       addError(errorMsg);
/* 46 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 50 */       addInfo("About to add new Joran parsing rule [" + pattern + "," + actionClass + "].");
/* 51 */       ec.getJoranInterpreter().getRuleStore().addRule(new ElementSelector(pattern), actionClass);
/*    */     } catch (Exception oops) {
/* 53 */       this.inError = true;
/* 54 */       String errorMsg = "Could not add new Joran parsing rule [" + pattern + "," + actionClass + "]";
/* 55 */       addError(errorMsg);
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String n) {}
/*    */   
/*    */   public void finish(InterpretationContext ec) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\action\NewRuleAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */