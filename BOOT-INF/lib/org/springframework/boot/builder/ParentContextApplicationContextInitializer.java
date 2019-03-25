/*    */ package org.springframework.boot.builder;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextInitializer;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ import org.springframework.context.event.ContextRefreshedEvent;
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
/*    */ public class ParentContextApplicationContextInitializer
/*    */   implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered
/*    */ {
/* 37 */   private int order = Integer.MIN_VALUE;
/*    */   private final ApplicationContext parent;
/*    */   
/*    */   public ParentContextApplicationContextInitializer(ApplicationContext parent)
/*    */   {
/* 42 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 46 */     this.order = order;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 51 */     return this.order;
/*    */   }
/*    */   
/*    */   public void initialize(ConfigurableApplicationContext applicationContext)
/*    */   {
/* 56 */     if (applicationContext != this.parent) {
/* 57 */       applicationContext.setParent(this.parent);
/* 58 */       applicationContext.addApplicationListener(EventPublisher.INSTANCE);
/*    */     }
/*    */   }
/*    */   
/*    */   private static class EventPublisher
/*    */     implements ApplicationListener<ContextRefreshedEvent>, Ordered
/*    */   {
/* 65 */     private static EventPublisher INSTANCE = new EventPublisher();
/*    */     
/*    */     public int getOrder()
/*    */     {
/* 69 */       return Integer.MIN_VALUE;
/*    */     }
/*    */     
/*    */     public void onApplicationEvent(ContextRefreshedEvent event)
/*    */     {
/* 74 */       ApplicationContext context = event.getApplicationContext();
/* 75 */       if (((context instanceof ConfigurableApplicationContext)) && 
/* 76 */         (context == event.getSource())) {
/* 77 */         context.publishEvent(new ParentContextApplicationContextInitializer.ParentContextAvailableEvent((ConfigurableApplicationContext)context));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class ParentContextAvailableEvent
/*    */     extends ApplicationEvent
/*    */   {
/*    */     public ParentContextAvailableEvent(ConfigurableApplicationContext applicationContext)
/*    */     {
/* 92 */       super();
/*    */     }
/*    */     
/*    */     public ConfigurableApplicationContext getApplicationContext() {
/* 96 */       return (ConfigurableApplicationContext)getSource();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\builder\ParentContextApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */