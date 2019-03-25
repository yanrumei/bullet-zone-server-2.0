/*    */ package org.springframework.boot.context.embedded;
/*    */ 
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public class EmbeddedServletContainerInitializedEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private final EmbeddedWebApplicationContext applicationContext;
/*    */   
/*    */   public EmbeddedServletContainerInitializedEvent(EmbeddedWebApplicationContext applicationContext, EmbeddedServletContainer source)
/*    */   {
/* 37 */     super(source);
/* 38 */     this.applicationContext = applicationContext;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public EmbeddedServletContainer getEmbeddedServletContainer()
/*    */   {
/* 46 */     return getSource();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EmbeddedServletContainer getSource()
/*    */   {
/* 55 */     return (EmbeddedServletContainer)super.getSource();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EmbeddedWebApplicationContext getApplicationContext()
/*    */   {
/* 65 */     return this.applicationContext;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\EmbeddedServletContainerInitializedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */