/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnwrappingBeanSerializer
/*     */   extends BeanSerializerBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanSerializer(BeanSerializerBase src, NameTransformer transformer)
/*     */   {
/*  36 */     super(src, transformer);
/*  37 */     this._nameTransformer = transformer;
/*     */   }
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter)
/*     */   {
/*  42 */     super(src, objectIdWriter);
/*  43 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */   public UnwrappingBeanSerializer(UnwrappingBeanSerializer src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/*  48 */     super(src, objectIdWriter, filterId);
/*  49 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */   protected UnwrappingBeanSerializer(UnwrappingBeanSerializer src, Set<String> toIgnore) {
/*  53 */     super(src, toIgnore);
/*  54 */     this._nameTransformer = src._nameTransformer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer)
/*     */   {
/*  66 */     return new UnwrappingBeanSerializer(this, transformer);
/*     */   }
/*     */   
/*     */   public boolean isUnwrappingSerializer()
/*     */   {
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  76 */     return new UnwrappingBeanSerializer(this, objectIdWriter);
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/*  81 */     return new UnwrappingBeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(Set<String> toIgnore)
/*     */   {
/*  86 */     return new UnwrappingBeanSerializer(this, toIgnore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/*  95 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 112 */     gen.setCurrentValue(bean);
/* 113 */     if (this._objectIdWriter != null) {
/* 114 */       _serializeWithObjectId(bean, gen, provider, false);
/* 115 */       return;
/*     */     }
/* 117 */     if (this._propertyFilterId != null) {
/* 118 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 120 */       serializeFields(bean, gen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 128 */     if (provider.isEnabled(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS)) {
/* 129 */       provider.reportMappingProblem("Unwrapped property requires use of type information: can not serialize without disabling `SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS`", new Object[0]);
/*     */     }
/* 131 */     gen.setCurrentValue(bean);
/* 132 */     if (this._objectIdWriter != null) {
/* 133 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/* 134 */       return;
/*     */     }
/* 136 */     if (this._propertyFilterId != null) {
/* 137 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 139 */       serializeFields(bean, gen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 150 */     return "UnwrappingBeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */