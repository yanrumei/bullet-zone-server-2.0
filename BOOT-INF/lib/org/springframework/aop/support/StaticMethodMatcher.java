/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.MethodMatcher;
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
/*    */ public abstract class StaticMethodMatcher
/*    */   implements MethodMatcher
/*    */ {
/*    */   public final boolean isRuntime()
/*    */   {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public final boolean matches(Method method, Class<?> targetClass, Object... args)
/*    */   {
/* 37 */     throw new UnsupportedOperationException("Illegal MethodMatcher usage");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\support\StaticMethodMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */