/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.Problem;
/*    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.MethodMetadata;
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
/*    */ final class BeanMethod
/*    */   extends ConfigurationMethod
/*    */ {
/*    */   public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass)
/*    */   {
/* 37 */     super(metadata, configurationClass);
/*    */   }
/*    */   
/*    */   public void validate(ProblemReporter problemReporter)
/*    */   {
/* 42 */     if (getMetadata().isStatic())
/*    */     {
/* 44 */       return;
/*    */     }
/*    */     
/* 47 */     if ((this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName())) && 
/* 48 */       (!getMetadata().isOverridable()))
/*    */     {
/* 50 */       problemReporter.error(new NonOverridableMethodError());
/*    */     }
/*    */   }
/*    */   
/*    */   private class NonOverridableMethodError
/*    */     extends Problem
/*    */   {
/*    */     public NonOverridableMethodError()
/*    */     {
/* 59 */       super(BeanMethod.this
/* 60 */         .getResourceLocation());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\BeanMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */