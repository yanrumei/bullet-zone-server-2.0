/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
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
/*    */ public class DefaultEventListenerFactory
/*    */   implements EventListenerFactory, Ordered
/*    */ {
/* 34 */   private int order = Integer.MAX_VALUE;
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 38 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 42 */     this.order = order;
/*    */   }
/*    */   
/*    */   public boolean supportsMethod(Method method) {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method)
/*    */   {
/* 51 */     return new ApplicationListenerMethodAdapter(beanName, type, method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\DefaultEventListenerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */