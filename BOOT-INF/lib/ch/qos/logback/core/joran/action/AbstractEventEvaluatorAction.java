/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEventEvaluatorAction
/*     */   extends Action
/*     */ {
/*     */   EventEvaluator<?> evaluator;
/*  29 */   boolean inError = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*     */   {
/*  36 */     this.inError = false;
/*  37 */     this.evaluator = null;
/*     */     
/*  39 */     String className = attributes.getValue("class");
/*  40 */     if (OptionHelper.isEmpty(className)) {
/*  41 */       className = defaultClassName();
/*  42 */       addInfo("Assuming default evaluator class [" + className + "]");
/*     */     }
/*     */     
/*  45 */     if (OptionHelper.isEmpty(className)) {
/*  46 */       className = defaultClassName();
/*  47 */       this.inError = true;
/*  48 */       addError("Mandatory \"class\" attribute not set for <evaluator>");
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     String evaluatorName = attributes.getValue("name");
/*  53 */     if (OptionHelper.isEmpty(evaluatorName)) {
/*  54 */       this.inError = true;
/*  55 */       addError("Mandatory \"name\" attribute not set for <evaluator>");
/*  56 */       return;
/*     */     }
/*     */     try {
/*  59 */       this.evaluator = ((EventEvaluator)OptionHelper.instantiateByClassName(className, EventEvaluator.class, this.context));
/*     */       
/*  61 */       this.evaluator.setContext(this.context);
/*  62 */       this.evaluator.setName(evaluatorName);
/*     */       
/*  64 */       ec.pushObject(this.evaluator);
/*  65 */       addInfo("Adding evaluator named [" + evaluatorName + "] to the object stack");
/*     */     }
/*     */     catch (Exception oops) {
/*  68 */       this.inError = true;
/*  69 */       addError("Could not create evaluator of type " + className + "].", oops);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String defaultClassName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void end(InterpretationContext ec, String e)
/*     */   {
/*  86 */     if (this.inError) {
/*  87 */       return;
/*     */     }
/*     */     
/*  90 */     if ((this.evaluator instanceof LifeCycle)) {
/*  91 */       this.evaluator.start();
/*  92 */       addInfo("Starting evaluator named [" + this.evaluator.getName() + "]");
/*     */     }
/*     */     
/*  95 */     Object o = ec.peekObject();
/*     */     
/*  97 */     if (o != this.evaluator) {
/*  98 */       addWarn("The object on the top the of the stack is not the evaluator pushed earlier.");
/*     */     } else {
/* 100 */       ec.popObject();
/*     */       try
/*     */       {
/* 103 */         Map<String, EventEvaluator<?>> evaluatorMap = (Map)this.context.getObject("EVALUATOR_MAP");
/* 104 */         if (evaluatorMap == null) {
/* 105 */           addError("Could not find EvaluatorMap");
/*     */         } else {
/* 107 */           evaluatorMap.put(this.evaluator.getName(), this.evaluator);
/*     */         }
/*     */       } catch (Exception ex) {
/* 110 */         addError("Could not set evaluator named [" + this.evaluator + "].", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void finish(InterpretationContext ec) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\action\AbstractEventEvaluatorAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */