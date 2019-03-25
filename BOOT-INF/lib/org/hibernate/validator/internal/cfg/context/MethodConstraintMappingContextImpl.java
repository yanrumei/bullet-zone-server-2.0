/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
/*    */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*    */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MethodConstraintMappingContextImpl
/*    */   extends ExecutableConstraintMappingContextImpl
/*    */   implements MethodConstraintMappingContext
/*    */ {
/*    */   MethodConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Method method)
/*    */   {
/* 21 */     super(typeContext, method);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public MethodConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations)
/*    */   {
/* 28 */     this.typeContext.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember(this.executable.getMember(), Boolean.valueOf(ignoreAnnotations));
/*    */     
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\MethodConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */