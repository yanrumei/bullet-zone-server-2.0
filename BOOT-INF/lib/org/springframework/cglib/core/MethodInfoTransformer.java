/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class MethodInfoTransformer
/*    */   implements Transformer
/*    */ {
/* 22 */   private static final MethodInfoTransformer INSTANCE = new MethodInfoTransformer();
/*    */   
/*    */   public static MethodInfoTransformer getInstance() {
/* 25 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public Object transform(Object value) {
/* 29 */     if ((value instanceof Method))
/* 30 */       return ReflectUtils.getMethodInfo((Method)value);
/* 31 */     if ((value instanceof Constructor)) {
/* 32 */       return ReflectUtils.getMethodInfo((Constructor)value);
/*    */     }
/* 34 */     throw new IllegalArgumentException("cannot get method info for " + value);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\MethodInfoTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */