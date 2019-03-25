/*    */ package org.springframework.boot.context.event;
/*    */ 
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
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
/*    */ 
/*    */ 
/*    */ public class ApplicationPreparedEvent
/*    */   extends SpringApplicationEvent
/*    */ {
/*    */   private final ConfigurableApplicationContext context;
/*    */   
/*    */   public ApplicationPreparedEvent(SpringApplication application, String[] args, ConfigurableApplicationContext context)
/*    */   {
/* 44 */     super(application, args);
/* 45 */     this.context = context;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConfigurableApplicationContext getApplicationContext()
/*    */   {
/* 53 */     return this.context;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\ApplicationPreparedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */