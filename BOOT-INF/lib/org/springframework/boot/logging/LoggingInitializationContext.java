/*    */ package org.springframework.boot.logging;
/*    */ 
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoggingInitializationContext
/*    */ {
/*    */   private final ConfigurableEnvironment environment;
/*    */   
/*    */   public LoggingInitializationContext(ConfigurableEnvironment environment)
/*    */   {
/* 37 */     this.environment = environment;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Environment getEnvironment()
/*    */   {
/* 45 */     return this.environment;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\LoggingInitializationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */