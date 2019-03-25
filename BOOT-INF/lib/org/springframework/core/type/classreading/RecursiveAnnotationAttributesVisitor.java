/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.annotation.AnnotationUtils;
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
/*    */ class RecursiveAnnotationAttributesVisitor
/*    */   extends AbstractRecursiveAnnotationVisitor
/*    */ {
/*    */   protected final String annotationType;
/*    */   
/*    */   public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, ClassLoader classLoader)
/*    */   {
/* 35 */     super(classLoader, attributes);
/* 36 */     this.annotationType = annotationType;
/*    */   }
/*    */   
/*    */ 
/*    */   public void visitEnd()
/*    */   {
/* 42 */     AnnotationUtils.registerDefaultValues(this.attributes);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\RecursiveAnnotationAttributesVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */