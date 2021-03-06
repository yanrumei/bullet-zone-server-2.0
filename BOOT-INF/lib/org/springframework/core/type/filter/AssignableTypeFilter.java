/*    */ package org.springframework.core.type.filter;
/*    */ 
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class AssignableTypeFilter
/*    */   extends AbstractTypeHierarchyTraversingFilter
/*    */ {
/*    */   private final Class<?> targetType;
/*    */   
/*    */   public AssignableTypeFilter(Class<?> targetType)
/*    */   {
/* 39 */     super(true, true);
/* 40 */     this.targetType = targetType;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean matchClassName(String className)
/*    */   {
/* 46 */     return this.targetType.getName().equals(className);
/*    */   }
/*    */   
/*    */   protected Boolean matchSuperClass(String superClassName)
/*    */   {
/* 51 */     return matchTargetType(superClassName);
/*    */   }
/*    */   
/*    */   protected Boolean matchInterface(String interfaceName)
/*    */   {
/* 56 */     return matchTargetType(interfaceName);
/*    */   }
/*    */   
/*    */   protected Boolean matchTargetType(String typeName) {
/* 60 */     if (this.targetType.getName().equals(typeName)) {
/* 61 */       return Boolean.valueOf(true);
/*    */     }
/* 63 */     if (Object.class.getName().equals(typeName)) {
/* 64 */       return Boolean.valueOf(false);
/*    */     }
/* 66 */     if (typeName.startsWith("java")) {
/*    */       try {
/* 68 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 69 */         return Boolean.valueOf(this.targetType.isAssignableFrom(clazz));
/*    */       }
/*    */       catch (Throwable localThrowable) {}
/*    */     }
/*    */     
/*    */ 
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\filter\AssignableTypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */