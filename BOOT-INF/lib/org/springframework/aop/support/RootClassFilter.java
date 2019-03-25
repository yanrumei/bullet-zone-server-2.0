/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.aop.ClassFilter;
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
/*    */ public class RootClassFilter
/*    */   implements ClassFilter, Serializable
/*    */ {
/*    */   private Class<?> clazz;
/*    */   
/*    */   public RootClassFilter(Class<?> clazz)
/*    */   {
/* 34 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean matches(Class<?> candidate)
/*    */   {
/* 40 */     return this.clazz.isAssignableFrom(candidate);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\support\RootClassFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */