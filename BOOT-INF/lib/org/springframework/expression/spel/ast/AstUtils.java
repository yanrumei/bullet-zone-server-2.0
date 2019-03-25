/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import org.springframework.expression.PropertyAccessor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AstUtils
/*    */ {
/*    */   public static List<PropertyAccessor> getPropertyAccessorsToTry(Class<?> targetType, List<PropertyAccessor> propertyAccessors)
/*    */   {
/* 48 */     List<PropertyAccessor> specificAccessors = new ArrayList();
/* 49 */     List<PropertyAccessor> generalAccessors = new ArrayList();
/* 50 */     for (PropertyAccessor resolver : propertyAccessors) {
/* 51 */       Class<?>[] targets = resolver.getSpecificTargetClasses();
/* 52 */       if (targets == null) {
/* 53 */         generalAccessors.add(resolver);
/*    */ 
/*    */       }
/* 56 */       else if (targetType != null) {
/* 57 */         int pos = 0;
/* 58 */         for (Class<?> clazz : targets) {
/* 59 */           if (clazz == targetType) {
/* 60 */             specificAccessors.add(pos++, resolver);
/*    */           }
/* 62 */           else if (clazz.isAssignableFrom(targetType))
/*    */           {
/* 64 */             generalAccessors.add(resolver);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 70 */     Object resolvers = new LinkedList();
/* 71 */     ((List)resolvers).addAll(specificAccessors);
/* 72 */     ((List)resolvers).addAll(generalAccessors);
/* 73 */     return (List<PropertyAccessor>)resolvers;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\AstUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */