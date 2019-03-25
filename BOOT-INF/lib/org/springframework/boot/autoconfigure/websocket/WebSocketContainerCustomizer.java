/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ public abstract class WebSocketContainerCustomizer<T extends EmbeddedServletContainerFactory>
/*    */   implements EmbeddedServletContainerCustomizer, Ordered
/*    */ {
/*    */   public int getOrder()
/*    */   {
/* 40 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void customize(ConfigurableEmbeddedServletContainer container)
/*    */   {
/* 46 */     if (getContainerType().isAssignableFrom(container.getClass())) {
/* 47 */       doCustomize((EmbeddedServletContainerFactory)container);
/*    */     }
/*    */   }
/*    */   
/*    */   protected Class<?> getContainerType() {
/* 52 */     return 
/* 53 */       ResolvableType.forClass(WebSocketContainerCustomizer.class, getClass()).resolveGeneric(new int[0]);
/*    */   }
/*    */   
/*    */   protected abstract void doCustomize(T paramT);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\WebSocketContainerCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */