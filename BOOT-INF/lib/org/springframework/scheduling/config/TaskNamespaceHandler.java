/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
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
/*    */ public class TaskNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init()
/*    */   {
/* 31 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 32 */     registerBeanDefinitionParser("executor", new ExecutorBeanDefinitionParser());
/* 33 */     registerBeanDefinitionParser("scheduled-tasks", new ScheduledTasksBeanDefinitionParser());
/* 34 */     registerBeanDefinitionParser("scheduler", new SchedulerBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\TaskNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */