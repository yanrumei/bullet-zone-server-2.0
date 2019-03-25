/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ public class MethodWrapper
/*    */ {
/* 23 */   private static final MethodWrapperKey KEY_FACTORY = (MethodWrapperKey)KeyFactory.create(MethodWrapperKey.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Object create(Method method)
/*    */   {
/* 34 */     return KEY_FACTORY.newInstance(method.getName(), 
/* 35 */       ReflectUtils.getNames(method.getParameterTypes()), method
/* 36 */       .getReturnType().getName());
/*    */   }
/*    */   
/*    */   public static Set createSet(Collection methods) {
/* 40 */     Set set = new HashSet();
/* 41 */     for (Iterator it = methods.iterator(); it.hasNext();) {
/* 42 */       set.add(create((Method)it.next()));
/*    */     }
/* 44 */     return set;
/*    */   }
/*    */   
/*    */   public static abstract interface MethodWrapperKey
/*    */   {
/*    */     public abstract Object newInstance(String paramString1, String[] paramArrayOfString, String paramString2);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\MethodWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */