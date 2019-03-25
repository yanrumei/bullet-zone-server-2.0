/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException.Reference;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
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
/*     */ public class BeanAsArraySerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanSerializerBase _defaultSerializer;
/*     */   
/*     */   public BeanAsArraySerializer(BeanSerializerBase src)
/*     */   {
/*  62 */     super(src, (ObjectIdWriter)null);
/*  63 */     this._defaultSerializer = src;
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, Set<String> toIgnore) {
/*  67 */     super(src, toIgnore);
/*  68 */     this._defaultSerializer = src;
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, ObjectIdWriter oiw, Object filterId)
/*     */   {
/*  73 */     super(src, oiw, filterId);
/*  74 */     this._defaultSerializer = src;
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
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer)
/*     */   {
/*  88 */     return this._defaultSerializer.unwrappingSerializer(transformer);
/*     */   }
/*     */   
/*     */   public boolean isUnwrappingSerializer()
/*     */   {
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter)
/*     */   {
/*  99 */     return this._defaultSerializer.withObjectIdWriter(objectIdWriter);
/*     */   }
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId)
/*     */   {
/* 104 */     return new BeanAsArraySerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer withIgnorals(Set<String> toIgnore)
/*     */   {
/* 109 */     return new BeanAsArraySerializer(this, toIgnore);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanSerializerBase asArraySerializer()
/*     */   {
/* 115 */     return this;
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
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 133 */     if (this._objectIdWriter != null) {
/* 134 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/* 135 */       return;
/*     */     }
/* 137 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 138 */     if (typeStr == null) {
/* 139 */       typeSer.writeTypePrefixForArray(bean, gen);
/*     */     } else {
/* 141 */       typeSer.writeCustomTypePrefixForArray(bean, gen, typeStr);
/*     */     }
/* 143 */     serializeAsArray(bean, gen, provider);
/* 144 */     if (typeStr == null) {
/* 145 */       typeSer.writeTypeSuffixForArray(bean, gen);
/*     */     } else {
/* 147 */       typeSer.writeCustomTypeSuffixForArray(bean, gen, typeStr);
/*     */     }
/*     */   }
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
/* 160 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(provider)))
/*     */     {
/* 162 */       serializeAsArray(bean, gen, provider);
/* 163 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     gen.writeStartArray();
/*     */     
/* 171 */     gen.setCurrentValue(bean);
/* 172 */     serializeAsArray(bean, gen, provider);
/* 173 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasSingleElement(SerializerProvider provider)
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */     BeanPropertyWriter[] props;
/*     */     
/* 184 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 185 */       props = this._filteredProps;
/*     */     } else {
/* 187 */       props = this._props;
/*     */     }
/* 189 */     return props.length == 1;
/*     */   }
/*     */   
/*     */   protected final void serializeAsArray(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     BeanPropertyWriter[] props;
/* 196 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 197 */       props = this._filteredProps;
/*     */     } else {
/* 199 */       props = this._props;
/*     */     }
/*     */     
/* 202 */     int i = 0;
/*     */     try {
/* 204 */       for (int len = props.length; i < len; i++) {
/* 205 */         BeanPropertyWriter prop = props[i];
/* 206 */         if (prop == null) {
/* 207 */           gen.writeNull();
/*     */         } else {
/* 209 */           prop.serializeAsElement(bean, gen, provider);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 217 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 218 */       wrapAndThrow(provider, e, bean, name);
/*     */     } catch (StackOverflowError e) {
/* 220 */       JsonMappingException mapE = JsonMappingException.from(gen, "Infinite recursion (StackOverflowError)", e);
/* 221 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 222 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 223 */       throw mapE;
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
/* 234 */     return "BeanAsArraySerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\BeanAsArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */