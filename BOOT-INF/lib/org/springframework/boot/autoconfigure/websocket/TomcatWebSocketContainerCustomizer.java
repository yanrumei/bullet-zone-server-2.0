/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.apache.catalina.Context;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
/*    */ import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ public class TomcatWebSocketContainerCustomizer
/*    */   extends WebSocketContainerCustomizer<TomcatEmbeddedServletContainerFactory>
/*    */ {
/*    */   private static final String TOMCAT_7_LISTENER_TYPE = "org.apache.catalina.deploy.ApplicationListener";
/*    */   private static final String TOMCAT_8_LISTENER_TYPE = "org.apache.tomcat.util.descriptor.web.ApplicationListener";
/*    */   private static final String WS_LISTENER = "org.apache.tomcat.websocket.server.WsContextListener";
/*    */   
/*    */   public void doCustomize(TomcatEmbeddedServletContainerFactory tomcatContainer)
/*    */   {
/* 48 */     tomcatContainer.addContextCustomizers(new TomcatContextCustomizer[] { new TomcatContextCustomizer()
/*    */     {
/*    */       public void customize(Context context) {
/* 51 */         TomcatWebSocketContainerCustomizer.this.addListener(context, TomcatWebSocketContainerCustomizer.access$000(TomcatWebSocketContainerCustomizer.this));
/*    */       }
/*    */     } });
/*    */   }
/*    */   
/*    */   private Class<?> findListenerType() {
/* 57 */     if (ClassUtils.isPresent("org.apache.catalina.deploy.ApplicationListener", null)) {
/* 58 */       return ClassUtils.resolveClassName("org.apache.catalina.deploy.ApplicationListener", null);
/*    */     }
/* 60 */     if (ClassUtils.isPresent("org.apache.tomcat.util.descriptor.web.ApplicationListener", null)) {
/* 61 */       return ClassUtils.resolveClassName("org.apache.tomcat.util.descriptor.web.ApplicationListener", null);
/*    */     }
/*    */     
/* 64 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void addListener(Context context, Class<?> listenerType)
/*    */   {
/* 76 */     Class<? extends Context> contextClass = context.getClass();
/* 77 */     if (listenerType == null) {
/* 78 */       ReflectionUtils.invokeMethod(ClassUtils.getMethod(contextClass, "addApplicationListener", new Class[] { String.class }), context, new Object[] { "org.apache.tomcat.websocket.server.WsContextListener" });
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/* 84 */       Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(listenerType, new Class[] { String.class, Boolean.TYPE });
/* 85 */       Object instance = BeanUtils.instantiateClass(constructor, new Object[] { "org.apache.tomcat.websocket.server.WsContextListener", Boolean.valueOf(false) });
/* 86 */       ReflectionUtils.invokeMethod(ClassUtils.getMethod(contextClass, "addApplicationListener", new Class[] { listenerType }), context, new Object[] { instance });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\TomcatWebSocketContainerCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */