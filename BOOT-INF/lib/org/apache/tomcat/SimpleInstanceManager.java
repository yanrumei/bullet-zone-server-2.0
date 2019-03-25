/*    */ package org.apache.tomcat;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import javax.naming.NamingException;
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
/*    */ public class SimpleInstanceManager
/*    */   implements InstanceManager
/*    */ {
/*    */   public Object newInstance(Class<?> clazz)
/*    */     throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, NoSuchMethodException
/*    */   {
/* 36 */     return prepareInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*    */   }
/*    */   
/*    */ 
/*    */   public Object newInstance(String className)
/*    */     throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException, NoSuchMethodException
/*    */   {
/* 43 */     Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
/* 44 */     return prepareInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*    */   }
/*    */   
/*    */ 
/*    */   public Object newInstance(String fqcn, ClassLoader classLoader)
/*    */     throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException, NoSuchMethodException
/*    */   {
/* 51 */     Class<?> clazz = classLoader.loadClass(fqcn);
/* 52 */     return prepareInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*    */   }
/*    */   
/*    */ 
/*    */   public void newInstance(Object o)
/*    */     throws IllegalAccessException, InvocationTargetException, NamingException
/*    */   {}
/*    */   
/*    */   public void destroyInstance(Object o)
/*    */     throws IllegalAccessException, InvocationTargetException
/*    */   {}
/*    */   
/*    */   private Object prepareInstance(Object o)
/*    */   {
/* 66 */     return o;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomcat\SimpleInstanceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */