/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheInterceptor
/*    */   extends CacheAspectSupport
/*    */   implements MethodInterceptor, Serializable
/*    */ {
/*    */   public Object invoke(final MethodInvocation invocation)
/*    */     throws Throwable
/*    */   {
/* 46 */     Method method = invocation.getMethod();
/*    */     
/* 48 */     CacheOperationInvoker aopAllianceInvoker = new CacheOperationInvoker()
/*    */     {
/*    */       public Object invoke() {
/*    */         try {
/* 52 */           return invocation.proceed();
/*    */         }
/*    */         catch (Throwable ex) {
/* 55 */           throw new CacheOperationInvoker.ThrowableWrapper(ex);
/*    */         }
/*    */       }
/*    */     };
/*    */     try
/*    */     {
/* 61 */       return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
/*    */     }
/*    */     catch (CacheOperationInvoker.ThrowableWrapper th) {
/* 64 */       throw th.getOriginal();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */