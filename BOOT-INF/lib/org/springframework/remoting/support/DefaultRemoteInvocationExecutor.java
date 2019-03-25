/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class DefaultRemoteInvocationExecutor
/*    */   implements RemoteInvocationExecutor
/*    */ {
/*    */   public Object invoke(RemoteInvocation invocation, Object targetObject)
/*    */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*    */   {
/* 37 */     Assert.notNull(invocation, "RemoteInvocation must not be null");
/* 38 */     Assert.notNull(targetObject, "Target object must not be null");
/* 39 */     return invocation.invoke(targetObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\support\DefaultRemoteInvocationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */