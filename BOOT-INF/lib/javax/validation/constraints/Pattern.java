/*     */ package javax.validation.constraints;
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
/*     */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @Documented
/*     */ @Constraint(validatedBy={})
/*     */ public @interface Pattern
/*     */ {
/*     */   String regexp();
/*     */   
/*     */   Flag[] flags() default {};
/*     */   
/*     */   String message() default "{javax.validation.constraints.Pattern.message}";
/*     */   
/*     */   Class<?>[] groups() default {};
/*     */   
/*     */   Class<? extends Payload>[] payload() default {};
/*     */   
/*     */   @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.PARAMETER})
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Documented
/*     */   public static @interface List
/*     */   {
/*     */     Pattern[] value();
/*     */   }
/*     */   
/*     */   public static enum Flag
/*     */   {
/*  82 */     UNIX_LINES(1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     CASE_INSENSITIVE(2), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     COMMENTS(4), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     MULTILINE(8), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     DOTALL(32), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     UNICODE_CASE(64), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     CANON_EQ(128);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Flag(int value)
/*     */     {
/* 130 */       this.value = value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int getValue()
/*     */     {
/* 137 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\constraints\Pattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */