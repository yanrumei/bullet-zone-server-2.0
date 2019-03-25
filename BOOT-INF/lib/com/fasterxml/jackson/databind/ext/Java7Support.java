/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Java7Support
/*    */ {
/*    */   private static final Java7Support IMPL;
/*    */   
/*    */   static
/*    */   {
/* 21 */     Java7Support impl = null;
/*    */     try {
/* 23 */       Class<?> cls = Class.forName("com.fasterxml.jackson.databind.ext.Java7SupportImpl");
/* 24 */       impl = (Java7Support)cls.newInstance();
/*    */     }
/*    */     catch (Throwable t) {
/* 27 */       Logger.getLogger(Java7Support.class.getName()).warning("Unable to load JDK7 types (annotations, java.nio.file.Path): no Java7 support added");
/*    */     }
/*    */     
/* 30 */     IMPL = impl; }
/*    */   public abstract JsonSerializer<?> getSerializerForJavaNioFilePath(Class<?> paramClass);
/*    */   public abstract JsonDeserializer<?> getDeserializerForJavaNioFilePath(Class<?> paramClass);
/*    */   public abstract Class<?> getClassJavaNioFilePath();
/* 34 */   public abstract PropertyName findConstructorName(AnnotatedParameter paramAnnotatedParameter); public abstract Boolean hasCreatorAnnotation(Annotated paramAnnotated); public abstract Boolean findTransient(Annotated paramAnnotated); public static Java7Support instance() { return IMPL; }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ext\Java7Support.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */