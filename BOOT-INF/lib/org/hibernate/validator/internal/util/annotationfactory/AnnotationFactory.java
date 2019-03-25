/*    */ package org.hibernate.validator.internal.util.annotationfactory;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.ConstructorInstance;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*    */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;
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
/*    */ public class AnnotationFactory
/*    */ {
/*    */   public static <T extends Annotation> T create(AnnotationDescriptor<T> descriptor)
/*    */   {
/* 36 */     Class<T> proxyClass = Proxy.getProxyClass(
/* 37 */       (ClassLoader)run(GetClassLoader.fromClass(descriptor.type())), new Class[] {descriptor
/* 38 */       .type() });
/*    */     
/* 40 */     InvocationHandler handler = new AnnotationProxy(descriptor);
/*    */     try {
/* 42 */       return getProxyInstance(proxyClass, handler);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 45 */       throw e;
/*    */     }
/*    */     catch (Exception e) {
/* 48 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <T extends Annotation> T getProxyInstance(Class<T> proxyClass, InvocationHandler handler)
/*    */     throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
/*    */   {
/* 55 */     Constructor<T> constructor = (Constructor)run(GetDeclaredConstructor.action(proxyClass, new Class[] { InvocationHandler.class }));
/*    */     
/*    */ 
/*    */ 
/* 59 */     return (Annotation)run(ConstructorInstance.action(constructor, new Object[] { handler }));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static <T> T run(PrivilegedAction<T> action)
/*    */   {
/* 69 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\annotationfactory\AnnotationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */