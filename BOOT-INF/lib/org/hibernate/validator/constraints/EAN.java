/*    */ package org.hibernate.validator.constraints;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import javax.validation.Constraint;
/*    */ import javax.validation.Payload;
/*    */ import javax.validation.ReportAsSingleViolation;
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
/*    */ @Documented
/*    */ @Constraint(validatedBy={})
/*    */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @ReportAsSingleViolation
/*    */ @Mod10Check
/*    */ public @interface EAN
/*    */ {
/*    */   String message() default "{org.hibernate.validator.constraints.EAN.message}";
/*    */   
/*    */   Class<?>[] groups() default {};
/*    */   
/*    */   Class<? extends Payload>[] payload() default {};
/*    */   
/*    */   Type type() default Type.EAN13;
/*    */   
/*    */   public static enum Type
/*    */   {
/* 60 */     EAN13, 
/* 61 */     EAN8;
/*    */     
/*    */     private Type() {}
/*    */   }
/*    */   
/*    */   @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   @Documented
/*    */   public static @interface List
/*    */   {
/*    */     EAN[] value();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\EAN.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */