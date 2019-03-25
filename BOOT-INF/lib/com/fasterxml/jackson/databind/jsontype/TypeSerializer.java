/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeSerializer
/*     */ {
/*     */   public abstract TypeSerializer forProperty(BeanProperty paramBeanProperty);
/*     */   
/*     */   public abstract JsonTypeInfo.As getTypeInclusion();
/*     */   
/*     */   public abstract String getPropertyName();
/*     */   
/*     */   public abstract TypeIdResolver getTypeIdResolver();
/*     */   
/*     */   public abstract void writeTypePrefixForScalar(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeTypePrefixForObject(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeTypePrefixForArray(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeTypeSuffixForScalar(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeTypeSuffixForObject(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeTypeSuffixForArray(Object paramObject, JsonGenerator paramJsonGenerator)
/*     */     throws IOException;
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/* 136 */     writeTypePrefixForScalar(value, g);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/* 145 */     writeTypePrefixForObject(value, g);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/* 154 */     writeTypePrefixForArray(value, g);
/*     */   }
/*     */   
/*     */   public abstract void writeCustomTypePrefixForScalar(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeCustomTypePrefixForObject(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeCustomTypePrefixForArray(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeCustomTypeSuffixForScalar(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeCustomTypeSuffixForObject(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void writeCustomTypeSuffixForArray(Object paramObject, JsonGenerator paramJsonGenerator, String paramString)
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\TypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */