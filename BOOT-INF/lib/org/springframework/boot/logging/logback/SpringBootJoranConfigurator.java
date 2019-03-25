/*    */ package org.springframework.boot.logging.logback;
/*    */ 
/*    */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*    */ import ch.qos.logback.core.joran.action.NOPAction;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import org.springframework.boot.logging.LoggingInitializationContext;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ class SpringBootJoranConfigurator
/*    */   extends JoranConfigurator
/*    */ {
/*    */   private LoggingInitializationContext initializationContext;
/*    */   
/*    */   SpringBootJoranConfigurator(LoggingInitializationContext initializationContext)
/*    */   {
/* 38 */     this.initializationContext = initializationContext;
/*    */   }
/*    */   
/*    */   public void addInstanceRules(RuleStore rs)
/*    */   {
/* 43 */     super.addInstanceRules(rs);
/* 44 */     Environment environment = this.initializationContext.getEnvironment();
/* 45 */     rs.addRule(new ElementSelector("configuration/springProperty"), new SpringPropertyAction(environment));
/*    */     
/* 47 */     rs.addRule(new ElementSelector("*/springProfile"), new SpringProfileAction(this.initializationContext
/* 48 */       .getEnvironment()));
/* 49 */     rs.addRule(new ElementSelector("*/springProfile/*"), new NOPAction());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\SpringBootJoranConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */