/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
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
/*     */ public class BeanSerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = -3618164443537292758L;
/*     */   
/*     */   public BeanSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/*  45 */     super(type, builder, properties, filteredProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializer(BeanSerializerBase src)
/*     */   {
/*  54 */     super(src);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter)
/*     */   {
/*  59 */     super(src, objectIdWriter);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/*  64 */     super(src, objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializer(BeanSerializerBase src, Set<String> toIgnore) {
/*  68 */     super(src, toIgnore);
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
/*     */   public static BeanSerializer createDummy(JavaType forType)
/*     */   {
/*  83 */     return new BeanSerializer(forType, null, NO_PROPS, null);
/*     */   }
/*     */   
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer unwrapper)
/*     */   {
/*  88 */     return new UnwrappingBeanSerializer(this, unwrapper);
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  93 */     return new BeanSerializer(this, objectIdWriter, this._propertyFilterId);
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/*  98 */     return new BeanSerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase withIgnorals(Set<String> toIgnore)
/*     */   {
/* 103 */     return new BeanSerializer(this, toIgnore);
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
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/* 121 */     if ((this._objectIdWriter == null) && (this._anyGetterWriter == null) && (this._propertyFilterId == null))
/*     */     {
/*     */ 
/*     */ 
/* 125 */       return new BeanAsArraySerializer(this);
/*     */     }
/*     */     
/* 128 */     return this;
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 146 */     if (this._objectIdWriter != null) {
/* 147 */       gen.setCurrentValue(bean);
/* 148 */       _serializeWithObjectId(bean, gen, provider, true);
/* 149 */       return;
/*     */     }
/* 151 */     gen.writeStartObject(bean);
/* 152 */     if (this._propertyFilterId != null) {
/* 153 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 155 */       serializeFields(bean, gen, provider);
/*     */     }
/* 157 */     gen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 167 */     return "BeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\BeanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */