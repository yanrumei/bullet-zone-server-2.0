/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ContainerSerializer<T>
/*     */   extends StdSerializer<T>
/*     */ {
/*     */   protected ContainerSerializer(Class<T> t)
/*     */   {
/*  25 */     super(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ContainerSerializer(JavaType fullType)
/*     */   {
/*  32 */     super(fullType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ContainerSerializer(Class<?> t, boolean dummy)
/*     */   {
/*  42 */     super(t, dummy);
/*     */   }
/*     */   
/*     */   protected ContainerSerializer(ContainerSerializer<?> src) {
/*  46 */     super(src._handledType, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContainerSerializer<?> withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  60 */     if (vts == null) return this;
/*  61 */     return _withValueTypeSerializer(vts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JavaType getContentType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonSerializer<?> getContentSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isEmpty(T value)
/*     */   {
/*  99 */     return isEmpty(null, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean hasSingleElement(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected boolean hasContentTypeAnnotation(SerializerProvider provider, BeanProperty property)
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\ContainerSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */