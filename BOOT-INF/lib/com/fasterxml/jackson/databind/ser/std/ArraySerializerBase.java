/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
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
/*     */ public abstract class ArraySerializerBase<T>
/*     */   extends ContainerSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   protected ArraySerializerBase(Class<T> cls)
/*     */   {
/*  35 */     super(cls);
/*  36 */     this._property = null;
/*  37 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected ArraySerializerBase(Class<T> cls, BeanProperty property)
/*     */   {
/*  49 */     super(cls);
/*  50 */     this._property = property;
/*  51 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */   protected ArraySerializerBase(ArraySerializerBase<?> src)
/*     */   {
/*  56 */     super(src._handledType, false);
/*  57 */     this._property = src._property;
/*  58 */     this._unwrapSingle = src._unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArraySerializerBase(ArraySerializerBase<?> src, BeanProperty property, Boolean unwrapSingle)
/*     */   {
/*  67 */     super(src._handledType, false);
/*  68 */     this._property = property;
/*  69 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected ArraySerializerBase(ArraySerializerBase<?> src, BeanProperty property)
/*     */   {
/*  78 */     super(src._handledType, false);
/*  79 */     this._property = property;
/*  80 */     this._unwrapSingle = src._unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonSerializer<?> _withResolved(BeanProperty paramBeanProperty, Boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  93 */     Boolean unwrapSingle = null;
/*     */     
/*     */ 
/*  96 */     if (property != null) {
/*  97 */       JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/*  98 */       if (format != null) {
/*  99 */         unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/* 100 */         if (unwrapSingle != this._unwrapSingle) {
/* 101 */           return _withResolved(property, unwrapSingle);
/*     */         }
/*     */       }
/*     */     }
/* 105 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 114 */     if (((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE))
/*     */     {
/*     */ 
/* 117 */       if (hasSingleElement(value)) {
/* 118 */         serializeContents(value, gen, provider);
/* 119 */         return;
/*     */       }
/*     */     }
/* 122 */     gen.setCurrentValue(value);
/* 123 */     gen.writeStartArray();
/*     */     
/* 125 */     serializeContents(value, gen, provider);
/* 126 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 134 */     typeSer.writeTypePrefixForArray(value, gen);
/*     */     
/* 136 */     gen.setCurrentValue(value);
/* 137 */     serializeContents(value, gen, provider);
/* 138 */     typeSer.writeTypeSuffixForArray(value, gen);
/*     */   }
/*     */   
/*     */   protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\ArraySerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */