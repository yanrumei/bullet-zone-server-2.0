/*    */ package org.hibernate.validator.parameternameprovider;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Parameter;
/*    */ import java.util.List;
/*    */ import javax.validation.ParameterNameProvider;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
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
/*    */ @IgnoreJava6Requirement
/*    */ public class ReflectionParameterNameProvider
/*    */   implements ParameterNameProvider
/*    */ {
/*    */   public List<String> getParameterNames(Constructor<?> constructor)
/*    */   {
/* 36 */     return getParameterNames(constructor.getParameters());
/*    */   }
/*    */   
/*    */   public List<String> getParameterNames(Method method)
/*    */   {
/* 41 */     return getParameterNames(method.getParameters());
/*    */   }
/*    */   
/*    */   private List<String> getParameterNames(Parameter[] parameters) {
/* 45 */     List<String> parameterNames = CollectionHelper.newArrayList();
/*    */     
/* 47 */     for (Parameter parameter : parameters)
/*    */     {
/* 49 */       parameterNames.add(parameter.getName());
/*    */     }
/*    */     
/* 52 */     return parameterNames;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\parameternameprovider\ReflectionParameterNameProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */