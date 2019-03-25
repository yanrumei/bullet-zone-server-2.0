/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.hook.ShutdownHookBase;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
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
/*    */ public class ShutdownHookAction
/*    */   extends Action
/*    */ {
/*    */   ShutdownHookBase hook;
/*    */   private boolean inError;
/*    */   
/*    */   public void begin(InterpretationContext ic, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 42 */     this.hook = null;
/* 43 */     this.inError = false;
/*    */     
/* 45 */     String className = attributes.getValue("class");
/* 46 */     if (OptionHelper.isEmpty(className)) {
/* 47 */       addError("Missing class name for shutdown hook. Near [" + name + "] line " + getLineNumber(ic));
/* 48 */       this.inError = true;
/* 49 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 53 */       addInfo("About to instantiate shutdown hook of type [" + className + "]");
/*    */       
/* 55 */       this.hook = ((ShutdownHookBase)OptionHelper.instantiateByClassName(className, ShutdownHookBase.class, this.context));
/* 56 */       this.hook.setContext(this.context);
/*    */       
/* 58 */       ic.pushObject(this.hook);
/*    */     } catch (Exception e) {
/* 60 */       this.inError = true;
/* 61 */       addError("Could not create a shutdown hook of type [" + className + "].", e);
/* 62 */       throw new ActionException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void end(InterpretationContext ic, String name)
/*    */     throws ActionException
/*    */   {
/* 72 */     if (this.inError) {
/* 73 */       return;
/*    */     }
/*    */     
/* 76 */     Object o = ic.peekObject();
/* 77 */     if (o != this.hook) {
/* 78 */       addWarn("The object at the of the stack is not the hook pushed earlier.");
/*    */     } else {
/* 80 */       ic.popObject();
/*    */       
/* 82 */       Thread hookThread = new Thread(this.hook, "Logback shutdown hook [" + this.context.getName() + "]");
/*    */       
/* 84 */       this.context.putObject("SHUTDOWN_HOOK", hookThread);
/* 85 */       Runtime.getRuntime().addShutdownHook(hookThread);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\action\ShutdownHookAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */