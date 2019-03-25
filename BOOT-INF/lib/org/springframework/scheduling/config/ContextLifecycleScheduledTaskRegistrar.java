/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.beans.factory.SmartInitializingSingleton;
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
/*    */ public class ContextLifecycleScheduledTaskRegistrar
/*    */   extends ScheduledTaskRegistrar
/*    */   implements SmartInitializingSingleton
/*    */ {
/*    */   public void afterPropertiesSet() {}
/*    */   
/*    */   public void afterSingletonsInstantiated()
/*    */   {
/* 37 */     scheduleTasks();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\ContextLifecycleScheduledTaskRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */