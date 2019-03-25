/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.UndeclaredThrowableException;
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
/*    */ public class ScheduledMethodRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final Object target;
/*    */   private final Method method;
/*    */   
/*    */   public ScheduledMethodRunnable(Object target, Method method)
/*    */   {
/* 42 */     this.target = target;
/* 43 */     this.method = method;
/*    */   }
/*    */   
/*    */   public ScheduledMethodRunnable(Object target, String methodName) throws NoSuchMethodException {
/* 47 */     this.target = target;
/* 48 */     this.method = target.getClass().getMethod(methodName, new Class[0]);
/*    */   }
/*    */   
/*    */   public Object getTarget()
/*    */   {
/* 53 */     return this.target;
/*    */   }
/*    */   
/*    */   public Method getMethod() {
/* 57 */     return this.method;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 64 */       ReflectionUtils.makeAccessible(this.method);
/* 65 */       this.method.invoke(this.target, new Object[0]);
/*    */     }
/*    */     catch (InvocationTargetException ex) {
/* 68 */       ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
/*    */     }
/*    */     catch (IllegalAccessException ex) {
/* 71 */       throw new UndeclaredThrowableException(ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\support\ScheduledMethodRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */