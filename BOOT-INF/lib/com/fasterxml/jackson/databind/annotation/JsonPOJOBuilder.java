/*    */ package com.fasterxml.jackson.databind.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JacksonAnnotation;
/*    */ import java.lang.annotation.Annotation;
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
/*    */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @JacksonAnnotation
/*    */ public @interface JsonPOJOBuilder
/*    */ {
/*    */   String buildMethodName() default "build";
/*    */   
/*    */   String withPrefix() default "with";
/*    */   
/*    */   public static class Value
/*    */   {
/*    */     public final String buildMethodName;
/*    */     public final String withPrefix;
/*    */     
/*    */     public Value(JsonPOJOBuilder ann)
/*    */     {
/* 78 */       this.buildMethodName = ann.buildMethodName();
/* 79 */       this.withPrefix = ann.withPrefix();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\annotation\JsonPOJOBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */