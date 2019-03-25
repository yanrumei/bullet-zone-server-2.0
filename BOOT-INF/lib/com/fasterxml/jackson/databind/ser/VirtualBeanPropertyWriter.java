/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class VirtualBeanPropertyWriter
/*     */   extends BeanPropertyWriter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType)
/*     */   {
/*  35 */     this(propDef, contextAnnotations, declaredType, null, null, null, propDef.findInclusion());
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
/*     */   protected VirtualBeanPropertyWriter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VirtualBeanPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, JsonInclude.Value inclusion)
/*     */   {
/*  57 */     super(propDef, propDef.getPrimaryMember(), contextAnnotations, declaredType, ser, typeSer, serType, _suppressNulls(inclusion), _suppressableValue(inclusion));
/*     */   }
/*     */   
/*     */ 
/*     */   protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base)
/*     */   {
/*  63 */     super(base);
/*     */   }
/*     */   
/*     */   protected VirtualBeanPropertyWriter(VirtualBeanPropertyWriter base, PropertyName name) {
/*  67 */     super(base, name);
/*     */   }
/*     */   
/*     */   protected static boolean _suppressNulls(JsonInclude.Value inclusion) {
/*  71 */     if (inclusion == null) {
/*  72 */       return false;
/*     */     }
/*  74 */     JsonInclude.Include incl = inclusion.getValueInclusion();
/*  75 */     return (incl != JsonInclude.Include.ALWAYS) && (incl != JsonInclude.Include.USE_DEFAULTS);
/*     */   }
/*     */   
/*     */   protected static Object _suppressableValue(JsonInclude.Value inclusion) {
/*  79 */     if (inclusion == null) {
/*  80 */       return Boolean.valueOf(false);
/*     */     }
/*  82 */     JsonInclude.Include incl = inclusion.getValueInclusion();
/*  83 */     if ((incl == JsonInclude.Include.ALWAYS) || (incl == JsonInclude.Include.NON_NULL) || (incl == JsonInclude.Include.USE_DEFAULTS))
/*     */     {
/*     */ 
/*  86 */       return null;
/*     */     }
/*  88 */     return MARKER_FOR_EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVirtual()
/*     */   {
/*  98 */     return true;
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
/*     */   protected abstract Object value(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws Exception;
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
/*     */   public abstract VirtualBeanPropertyWriter withConfig(MapperConfig<?> paramMapperConfig, AnnotatedClass paramAnnotatedClass, BeanPropertyDefinition paramBeanPropertyDefinition, JavaType paramJavaType);
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
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 141 */     Object value = value(bean, gen, prov);
/*     */     
/* 143 */     if (value == null) {
/* 144 */       if (this._nullSerializer != null) {
/* 145 */         gen.writeFieldName(this._name);
/* 146 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       }
/* 148 */       return;
/*     */     }
/* 150 */     JsonSerializer<Object> ser = this._serializer;
/* 151 */     if (ser == null) {
/* 152 */       Class<?> cls = value.getClass();
/* 153 */       PropertySerializerMap m = this._dynamicSerializers;
/* 154 */       ser = m.serializerFor(cls);
/* 155 */       if (ser == null) {
/* 156 */         ser = _findAndAddDynamic(m, cls, prov);
/*     */       }
/*     */     }
/* 159 */     if (this._suppressableValue != null) {
/* 160 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 161 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/* 164 */       else if (this._suppressableValue.equals(value)) {
/* 165 */         return;
/*     */       }
/*     */     }
/* 168 */     if (value == bean)
/*     */     {
/* 170 */       if (_handleSelfReference(bean, gen, prov, ser)) {
/* 171 */         return;
/*     */       }
/*     */     }
/* 174 */     gen.writeFieldName(this._name);
/* 175 */     if (this._typeSerializer == null) {
/* 176 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 178 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 190 */     Object value = value(bean, gen, prov);
/*     */     
/* 192 */     if (value == null) {
/* 193 */       if (this._nullSerializer != null) {
/* 194 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } else {
/* 196 */         gen.writeNull();
/*     */       }
/* 198 */       return;
/*     */     }
/* 200 */     JsonSerializer<Object> ser = this._serializer;
/* 201 */     if (ser == null) {
/* 202 */       Class<?> cls = value.getClass();
/* 203 */       PropertySerializerMap map = this._dynamicSerializers;
/* 204 */       ser = map.serializerFor(cls);
/* 205 */       if (ser == null) {
/* 206 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/* 209 */     if (this._suppressableValue != null) {
/* 210 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 211 */         if (ser.isEmpty(prov, value)) {
/* 212 */           serializeAsPlaceholder(bean, gen, prov);
/*     */         }
/*     */       }
/* 215 */       else if (this._suppressableValue.equals(value)) {
/* 216 */         serializeAsPlaceholder(bean, gen, prov);
/* 217 */         return;
/*     */       }
/*     */     }
/* 220 */     if ((value == bean) && 
/* 221 */       (_handleSelfReference(bean, gen, prov, ser))) {
/* 222 */       return;
/*     */     }
/*     */     
/* 225 */     if (this._typeSerializer == null) {
/* 226 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 228 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\VirtualBeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */