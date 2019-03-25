/*     */ package org.hibernate.validator.constraints;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import javax.validation.Constraint;
/*     */ import javax.validation.Payload;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Documented
/*     */ @Constraint(validatedBy={})
/*     */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ public @interface Mod11Check
/*     */ {
/*     */   String message() default "{org.hibernate.validator.constraints.Mod11Check.message}";
/*     */   
/*     */   Class<?>[] groups() default {};
/*     */   
/*     */   Class<? extends Payload>[] payload() default {};
/*     */   
/*     */   int threshold() default Integer.MAX_VALUE;
/*     */   
/*     */   int startIndex() default 0;
/*     */   
/*     */   int endIndex() default Integer.MAX_VALUE;
/*     */   
/*     */   int checkDigitIndex() default -1;
/*     */   
/*     */   boolean ignoreNonDigitCharacters() default false;
/*     */   
/*     */   char treatCheck10As() default 'X';
/*     */   
/*     */   char treatCheck11As() default '0';
/*     */   
/*     */   ProcessingDirection processingDirection() default ProcessingDirection.RIGHT_TO_LEFT;
/*     */   
/*     */   public static enum ProcessingDirection
/*     */   {
/* 129 */     RIGHT_TO_LEFT, 
/* 130 */     LEFT_TO_RIGHT;
/*     */     
/*     */     private ProcessingDirection() {}
/*     */   }
/*     */   
/*     */   @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Documented
/*     */   public static @interface List
/*     */   {
/*     */     Mod11Check[] value();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\Mod11Check.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */