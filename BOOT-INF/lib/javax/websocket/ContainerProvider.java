/*    */ package javax.websocket;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
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
/*    */ public abstract class ContainerProvider
/*    */ {
/*    */   private static final String DEFAULT_PROVIDER_CLASS_NAME = "org.apache.tomcat.websocket.WsWebSocketContainer";
/*    */   
/*    */   public static WebSocketContainer getWebSocketContainer()
/*    */   {
/* 38 */     WebSocketContainer result = null;
/*    */     
/*    */ 
/* 41 */     ServiceLoader<ContainerProvider> serviceLoader = ServiceLoader.load(ContainerProvider.class);
/* 42 */     Iterator<ContainerProvider> iter = serviceLoader.iterator();
/* 43 */     while ((result == null) && (iter.hasNext())) {
/* 44 */       result = ((ContainerProvider)iter.next()).getContainer();
/*    */     }
/*    */     
/*    */ 
/* 48 */     if (result == null)
/*    */     {
/*    */       try
/*    */       {
/* 52 */         Class<WebSocketContainer> clazz = Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
/*    */         
/* 54 */         result = (WebSocketContainer)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */       }
/*    */       catch (ClassNotFoundException|InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException localClassNotFoundException) {}
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 61 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract WebSocketContainer getContainer();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\ContainerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */