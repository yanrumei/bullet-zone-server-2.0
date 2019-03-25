/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper.Base;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
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
/*     */ public class UnwrappingBeanPropertyWriter
/*     */   extends BeanPropertyWriter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanPropertyWriter(BeanPropertyWriter base, NameTransformer unwrapper)
/*     */   {
/*  43 */     super(base);
/*  44 */     this._nameTransformer = unwrapper;
/*     */   }
/*     */   
/*     */   protected UnwrappingBeanPropertyWriter(UnwrappingBeanPropertyWriter base, NameTransformer transformer, SerializedString name)
/*     */   {
/*  49 */     super(base, name);
/*  50 */     this._nameTransformer = transformer;
/*     */   }
/*     */   
/*     */ 
/*     */   public UnwrappingBeanPropertyWriter rename(NameTransformer transformer)
/*     */   {
/*  56 */     String oldName = this._name.getValue();
/*  57 */     String newName = transformer.transform(oldName);
/*     */     
/*     */ 
/*  60 */     transformer = NameTransformer.chainedTransformer(transformer, this._nameTransformer);
/*     */     
/*  62 */     return _new(transformer, new SerializedString(newName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UnwrappingBeanPropertyWriter _new(NameTransformer transformer, SerializedString newName)
/*     */   {
/*  72 */     return new UnwrappingBeanPropertyWriter(this, transformer, newName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUnwrapping()
/*     */   {
/*  83 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/*  90 */     Object value = get(bean);
/*  91 */     if (value == null)
/*     */     {
/*     */ 
/*  94 */       return;
/*     */     }
/*  96 */     JsonSerializer<Object> ser = this._serializer;
/*  97 */     if (ser == null) {
/*  98 */       Class<?> cls = value.getClass();
/*  99 */       PropertySerializerMap map = this._dynamicSerializers;
/* 100 */       ser = map.serializerFor(cls);
/* 101 */       if (ser == null) {
/* 102 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/* 105 */     if (this._suppressableValue != null) {
/* 106 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 107 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/* 110 */       else if (this._suppressableValue.equals(value)) {
/* 111 */         return;
/*     */       }
/*     */     }
/*     */     
/* 115 */     if ((value == bean) && 
/* 116 */       (_handleSelfReference(bean, gen, prov, ser))) {
/* 117 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 122 */     if (!ser.isUnwrappingSerializer()) {
/* 123 */       gen.writeFieldName(this._name);
/*     */     }
/*     */     
/* 126 */     if (this._typeSerializer == null) {
/* 127 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 129 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void assignSerializer(JsonSerializer<Object> ser)
/*     */   {
/* 137 */     super.assignSerializer(ser);
/* 138 */     if (this._serializer != null) {
/* 139 */       NameTransformer t = this._nameTransformer;
/* 140 */       if (this._serializer.isUnwrappingSerializer()) {
/* 141 */         t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)this._serializer)._nameTransformer);
/*     */       }
/* 143 */       this._serializer = this._serializer.unwrappingSerializer(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void depositSchemaProperty(final JsonObjectFormatVisitor visitor, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 157 */     JsonSerializer<Object> ser = provider.findValueSerializer(getType(), this).unwrappingSerializer(this._nameTransformer);
/*     */     
/*     */ 
/*     */ 
/* 161 */     if (ser.isUnwrappingSerializer()) {
/* 162 */       ser.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base(provider)
/*     */       {
/*     */ 
/*     */ 
/*     */         public JsonObjectFormatVisitor expectObjectFormat(JavaType type)
/*     */           throws JsonMappingException {
/* 168 */           return visitor; } }, getType());
/*     */     }
/*     */     else
/*     */     {
/* 172 */       super.depositSchemaProperty(visitor, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode)
/*     */   {
/* 180 */     JsonNode props = schemaNode.get("properties");
/* 181 */     if (props != null) {
/* 182 */       Iterator<Map.Entry<String, JsonNode>> it = props.fields();
/* 183 */       while (it.hasNext()) {
/* 184 */         Map.Entry<String, JsonNode> entry = (Map.Entry)it.next();
/* 185 */         String name = (String)entry.getKey();
/* 186 */         if (this._nameTransformer != null) {
/* 187 */           name = this._nameTransformer.transform(name);
/*     */         }
/* 189 */         propertiesNode.set(name, (JsonNode)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonSerializer<Object> serializer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 206 */     if (this._nonTrivialBaseType != null) {
/* 207 */       JavaType subtype = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/* 208 */       serializer = provider.findValueSerializer(subtype, this);
/*     */     } else {
/* 210 */       serializer = provider.findValueSerializer(type, this);
/*     */     }
/* 212 */     NameTransformer t = this._nameTransformer;
/* 213 */     if (serializer.isUnwrappingSerializer()) {
/* 214 */       t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)serializer)._nameTransformer);
/*     */     }
/* 216 */     JsonSerializer<Object> serializer = serializer.unwrappingSerializer(t);
/*     */     
/* 218 */     this._dynamicSerializers = this._dynamicSerializers.newWith(type, serializer);
/* 219 */     return serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */