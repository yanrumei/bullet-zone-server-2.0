/*    */ package ch.qos.logback.core.joran.spi;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class DefaultNestedComponentRegistry
/*    */ {
/* 27 */   Map<HostClassAndPropertyDouble, Class<?>> defaultComponentMap = new HashMap();
/*    */   
/*    */   public void add(Class<?> hostClass, String propertyName, Class<?> componentClass) {
/* 30 */     HostClassAndPropertyDouble hpDouble = new HostClassAndPropertyDouble(hostClass, propertyName.toLowerCase());
/* 31 */     this.defaultComponentMap.put(hpDouble, componentClass);
/*    */   }
/*    */   
/*    */   public Class<?> findDefaultComponentType(Class<?> hostClass, String propertyName) {
/* 35 */     propertyName = propertyName.toLowerCase();
/* 36 */     while (hostClass != null) {
/* 37 */       Class<?> componentClass = oneShotFind(hostClass, propertyName);
/* 38 */       if (componentClass != null) {
/* 39 */         return componentClass;
/*    */       }
/* 41 */       hostClass = hostClass.getSuperclass();
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   private Class<?> oneShotFind(Class<?> hostClass, String propertyName) {
/* 47 */     HostClassAndPropertyDouble hpDouble = new HostClassAndPropertyDouble(hostClass, propertyName);
/* 48 */     return (Class)this.defaultComponentMap.get(hpDouble);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\spi\DefaultNestedComponentRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */