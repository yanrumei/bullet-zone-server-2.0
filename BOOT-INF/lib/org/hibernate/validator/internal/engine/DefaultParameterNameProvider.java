/*    */ package org.hibernate.validator.internal.engine;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.List;
/*    */ import javax.validation.ParameterNameProvider;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*    */ public class DefaultParameterNameProvider
/*    */   implements ParameterNameProvider
/*    */ {
/*    */   public List<String> getParameterNames(Constructor<?> constructor)
/*    */   {
/* 28 */     return getParameterNames(constructor.getParameterTypes().length);
/*    */   }
/*    */   
/*    */   public List<String> getParameterNames(Method method)
/*    */   {
/* 33 */     return getParameterNames(method.getParameterTypes().length);
/*    */   }
/*    */   
/*    */   private List<String> getParameterNames(int parameterCount) {
/* 37 */     List<String> parameterNames = CollectionHelper.newArrayList();
/*    */     
/* 39 */     for (int i = 0; i < parameterCount; i++) {
/* 40 */       parameterNames.add(getPrefix() + i);
/*    */     }
/*    */     
/* 43 */     return parameterNames;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getPrefix()
/*    */   {
/* 53 */     return "arg";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\DefaultParameterNameProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */