/*    */ package ch.qos.logback.classic.joran;
/*    */ 
/*    */ import ch.qos.logback.classic.joran.action.ConfigurationAction;
/*    */ import ch.qos.logback.classic.joran.action.ConsolePluginAction;
/*    */ import ch.qos.logback.classic.joran.action.ContextNameAction;
/*    */ import ch.qos.logback.classic.joran.action.EvaluatorAction;
/*    */ import ch.qos.logback.classic.joran.action.InsertFromJNDIAction;
/*    */ import ch.qos.logback.classic.joran.action.JMXConfiguratorAction;
/*    */ import ch.qos.logback.classic.joran.action.LevelAction;
/*    */ import ch.qos.logback.classic.joran.action.LoggerAction;
/*    */ import ch.qos.logback.classic.joran.action.LoggerContextListenerAction;
/*    */ import ch.qos.logback.classic.joran.action.ReceiverAction;
/*    */ import ch.qos.logback.classic.joran.action.RootLoggerAction;
/*    */ import ch.qos.logback.classic.sift.SiftAction;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.PlatformInfo;
/*    */ import ch.qos.logback.classic.util.DefaultNestedComponentRules;
/*    */ import ch.qos.logback.core.joran.JoranConfiguratorBase;
/*    */ import ch.qos.logback.core.joran.action.AppenderRefAction;
/*    */ import ch.qos.logback.core.joran.action.IncludeAction;
/*    */ import ch.qos.logback.core.joran.action.NOPAction;
/*    */ import ch.qos.logback.core.joran.conditional.ElseAction;
/*    */ import ch.qos.logback.core.joran.conditional.IfAction;
/*    */ import ch.qos.logback.core.joran.conditional.ThenAction;
/*    */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JoranConfigurator
/*    */   extends JoranConfiguratorBase<ILoggingEvent>
/*    */ {
/*    */   public void addInstanceRules(RuleStore rs)
/*    */   {
/* 42 */     super.addInstanceRules(rs);
/*    */     
/* 44 */     rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());
/*    */     
/* 46 */     rs.addRule(new ElementSelector("configuration/contextName"), new ContextNameAction());
/* 47 */     rs.addRule(new ElementSelector("configuration/contextListener"), new LoggerContextListenerAction());
/* 48 */     rs.addRule(new ElementSelector("configuration/insertFromJNDI"), new InsertFromJNDIAction());
/* 49 */     rs.addRule(new ElementSelector("configuration/evaluator"), new EvaluatorAction());
/*    */     
/* 51 */     rs.addRule(new ElementSelector("configuration/appender/sift"), new SiftAction());
/* 52 */     rs.addRule(new ElementSelector("configuration/appender/sift/*"), new NOPAction());
/*    */     
/* 54 */     rs.addRule(new ElementSelector("configuration/logger"), new LoggerAction());
/* 55 */     rs.addRule(new ElementSelector("configuration/logger/level"), new LevelAction());
/*    */     
/* 57 */     rs.addRule(new ElementSelector("configuration/root"), new RootLoggerAction());
/* 58 */     rs.addRule(new ElementSelector("configuration/root/level"), new LevelAction());
/* 59 */     rs.addRule(new ElementSelector("configuration/logger/appender-ref"), new AppenderRefAction());
/* 60 */     rs.addRule(new ElementSelector("configuration/root/appender-ref"), new AppenderRefAction());
/*    */     
/*    */ 
/* 63 */     rs.addRule(new ElementSelector("*/if"), new IfAction());
/* 64 */     rs.addRule(new ElementSelector("*/if/then"), new ThenAction());
/* 65 */     rs.addRule(new ElementSelector("*/if/then/*"), new NOPAction());
/* 66 */     rs.addRule(new ElementSelector("*/if/else"), new ElseAction());
/* 67 */     rs.addRule(new ElementSelector("*/if/else/*"), new NOPAction());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 72 */     if (PlatformInfo.hasJMXObjectName()) {
/* 73 */       rs.addRule(new ElementSelector("configuration/jmxConfigurator"), new JMXConfiguratorAction());
/*    */     }
/* 75 */     rs.addRule(new ElementSelector("configuration/include"), new IncludeAction());
/*    */     
/* 77 */     rs.addRule(new ElementSelector("configuration/consolePlugin"), new ConsolePluginAction());
/*    */     
/* 79 */     rs.addRule(new ElementSelector("configuration/receiver"), new ReceiverAction());
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry)
/*    */   {
/* 85 */     DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\joran\JoranConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */