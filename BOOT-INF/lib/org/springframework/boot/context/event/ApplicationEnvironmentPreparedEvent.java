/*    */ package org.springframework.boot.context.event;
/*    */ 
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ApplicationEnvironmentPreparedEvent
/*    */   extends SpringApplicationEvent
/*    */ {
/*    */   private final ConfigurableEnvironment environment;
/*    */   
/*    */   public ApplicationEnvironmentPreparedEvent(SpringApplication application, String[] args, ConfigurableEnvironment environment)
/*    */   {
/* 42 */     super(application, args);
/* 43 */     this.environment = environment;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConfigurableEnvironment getEnvironment()
/*    */   {
/* 51 */     return this.environment;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\ApplicationEnvironmentPreparedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */