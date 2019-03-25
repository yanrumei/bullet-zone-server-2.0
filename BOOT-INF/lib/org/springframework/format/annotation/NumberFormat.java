/*    */ package org.springframework.format.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.ANNOTATION_TYPE})
/*    */ public @interface NumberFormat
/*    */ {
/*    */   Style style() default Style.DEFAULT;
/*    */   
/*    */   String pattern() default "";
/*    */   
/*    */   public static enum Style
/*    */   {
/* 80 */     DEFAULT, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 85 */     NUMBER, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 90 */     PERCENT, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 95 */     CURRENCY;
/*    */     
/*    */     private Style() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\annotation\NumberFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */