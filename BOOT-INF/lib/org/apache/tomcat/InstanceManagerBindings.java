/*    */ package org.apache.tomcat;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public final class InstanceManagerBindings
/*    */ {
/* 24 */   private static final Map<ClassLoader, InstanceManager> bindings = new ConcurrentHashMap();
/*    */   
/*    */   public static final void bind(ClassLoader classLoader, InstanceManager instanceManager) {
/* 27 */     bindings.put(classLoader, instanceManager);
/*    */   }
/*    */   
/* 30 */   public static final void unbind(ClassLoader classLoader) { bindings.remove(classLoader); }
/*    */   
/*    */   public static final InstanceManager get(ClassLoader classLoader) {
/* 33 */     return (InstanceManager)bindings.get(classLoader);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\InstanceManagerBindings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */