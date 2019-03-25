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
/*     */ @Documented
/*     */ @Constraint(validatedBy={})
/*     */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ public @interface SafeHtml
/*     */ {
/*     */   String message() default "{org.hibernate.validator.constraints.SafeHtml.message}";
/*     */   
/*     */   Class<?>[] groups() default {};
/*     */   
/*     */   Class<? extends Payload>[] payload() default {};
/*     */   
/*     */   WhiteListType whitelistType() default WhiteListType.RELAXED;
/*     */   
/*     */   String[] additionalTags() default {};
/*     */   
/*     */   Tag[] additionalTagsWithAttributes() default {};
/*     */   
/*     */   public static enum WhiteListType
/*     */   {
/*  97 */     NONE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     SIMPLE_TEXT, 
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
/* 115 */     BASIC, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     BASIC_WITH_IMAGES, 
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
/* 134 */     RELAXED;
/*     */     
/*     */     private WhiteListType() {}
/*     */   }
/*     */   
/*     */   @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Documented
/*     */   public static @interface List
/*     */   {
/*     */     SafeHtml[] value();
/*     */   }
/*     */   
/*     */   @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Documented
/*     */   public static @interface Tag
/*     */   {
/*     */     String name();
/*     */     
/*     */     String[] attributes() default {};
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraints\SafeHtml.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */