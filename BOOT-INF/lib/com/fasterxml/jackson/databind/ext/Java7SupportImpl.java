/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.beans.Transient;
/*    */ import java.nio.file.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Java7SupportImpl
/*    */   extends Java7Support
/*    */ {
/*    */   private final Class<?> _bogus;
/*    */   
/*    */   public Java7SupportImpl()
/*    */   {
/* 24 */     Class<?> cls = Transient.class;
/* 25 */     cls = ConstructorProperties.class;
/* 26 */     this._bogus = cls;
/*    */   }
/*    */   
/*    */   public Class<?> getClassJavaNioFilePath()
/*    */   {
/* 31 */     return Path.class;
/*    */   }
/*    */   
/*    */   public JsonDeserializer<?> getDeserializerForJavaNioFilePath(Class<?> rawType)
/*    */   {
/* 36 */     if (rawType == Path.class) {
/* 37 */       return new NioPathDeserializer();
/*    */     }
/* 39 */     return null;
/*    */   }
/*    */   
/*    */   public JsonSerializer<?> getSerializerForJavaNioFilePath(Class<?> rawType)
/*    */   {
/* 44 */     if (Path.class.isAssignableFrom(rawType)) {
/* 45 */       return new NioPathSerializer();
/*    */     }
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public Boolean findTransient(Annotated a)
/*    */   {
/* 52 */     Transient t = (Transient)a.getAnnotation(Transient.class);
/* 53 */     if (t != null) {
/* 54 */       return Boolean.valueOf(t.value());
/*    */     }
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   public Boolean hasCreatorAnnotation(Annotated a)
/*    */   {
/* 61 */     ConstructorProperties props = (ConstructorProperties)a.getAnnotation(ConstructorProperties.class);
/*    */     
/*    */ 
/* 64 */     if (props != null) {
/* 65 */       return Boolean.TRUE;
/*    */     }
/* 67 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public PropertyName findConstructorName(AnnotatedParameter p)
/*    */   {
/* 73 */     AnnotatedWithParams ctor = p.getOwner();
/* 74 */     if (ctor != null) {
/* 75 */       ConstructorProperties props = (ConstructorProperties)ctor.getAnnotation(ConstructorProperties.class);
/* 76 */       if (props != null) {
/* 77 */         String[] names = props.value();
/* 78 */         int ix = p.getIndex();
/* 79 */         if (ix < names.length) {
/* 80 */           return PropertyName.construct(names[ix]);
/*    */         }
/*    */       }
/*    */     }
/* 84 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ext\Java7SupportImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */