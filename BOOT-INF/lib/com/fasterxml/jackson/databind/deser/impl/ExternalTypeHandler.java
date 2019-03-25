/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExternalTypeHandler
/*     */ {
/*     */   private final ExtTypedProperty[] _properties;
/*     */   private final HashMap<String, Integer> _nameToPropertyIndex;
/*     */   private final String[] _typeIds;
/*     */   private final TokenBuffer[] _tokens;
/*     */   
/*     */   protected ExternalTypeHandler(ExtTypedProperty[] properties, HashMap<String, Integer> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens)
/*     */   {
/*  31 */     this._properties = properties;
/*  32 */     this._nameToPropertyIndex = nameToPropertyIndex;
/*  33 */     this._typeIds = typeIds;
/*  34 */     this._tokens = tokens;
/*     */   }
/*     */   
/*     */   protected ExternalTypeHandler(ExternalTypeHandler h)
/*     */   {
/*  39 */     this._properties = h._properties;
/*  40 */     this._nameToPropertyIndex = h._nameToPropertyIndex;
/*  41 */     int len = this._properties.length;
/*  42 */     this._typeIds = new String[len];
/*  43 */     this._tokens = new TokenBuffer[len];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExternalTypeHandler start()
/*     */   {
/*  51 */     return new ExternalTypeHandler(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean handleTypePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean)
/*     */     throws IOException
/*     */   {
/*  64 */     Integer I = (Integer)this._nameToPropertyIndex.get(propName);
/*  65 */     if (I == null) {
/*  66 */       return false;
/*     */     }
/*  68 */     int index = I.intValue();
/*  69 */     ExtTypedProperty prop = this._properties[index];
/*  70 */     if (!prop.hasTypePropertyName(propName)) {
/*  71 */       return false;
/*     */     }
/*  73 */     String typeId = p.getText();
/*     */     
/*  75 */     boolean canDeserialize = (bean != null) && (this._tokens[index] != null);
/*     */     
/*  77 */     if (canDeserialize) {
/*  78 */       _deserializeAndSet(p, ctxt, bean, index, typeId);
/*     */       
/*  80 */       this._tokens[index] = null;
/*     */     } else {
/*  82 */       this._typeIds[index] = typeId;
/*     */     }
/*  84 */     return true;
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
/*     */   public boolean handlePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean)
/*     */     throws IOException
/*     */   {
/*  98 */     Integer I = (Integer)this._nameToPropertyIndex.get(propName);
/*  99 */     if (I == null) {
/* 100 */       return false;
/*     */     }
/* 102 */     int index = I.intValue();
/* 103 */     ExtTypedProperty prop = this._properties[index];
/*     */     boolean canDeserialize;
/* 105 */     boolean canDeserialize; if (prop.hasTypePropertyName(propName)) {
/* 106 */       this._typeIds[index] = p.getText();
/* 107 */       p.skipChildren();
/* 108 */       canDeserialize = (bean != null) && (this._tokens[index] != null);
/*     */     }
/*     */     else {
/* 111 */       TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 112 */       tokens.copyCurrentStructure(p);
/* 113 */       this._tokens[index] = tokens;
/* 114 */       canDeserialize = (bean != null) && (this._typeIds[index] != null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 119 */     if (canDeserialize) {
/* 120 */       String typeId = this._typeIds[index];
/*     */       
/* 122 */       this._typeIds[index] = null;
/* 123 */       _deserializeAndSet(p, ctxt, bean, index, typeId);
/* 124 */       this._tokens[index] = null;
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object complete(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 137 */     int i = 0; for (int len = this._properties.length; i < len; i++) {
/* 138 */       String typeId = this._typeIds[i];
/* 139 */       if (typeId == null) {
/* 140 */         TokenBuffer tokens = this._tokens[i];
/*     */         
/*     */ 
/* 143 */         if (tokens == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */ 
/* 148 */         JsonToken t = tokens.firstToken();
/* 149 */         if ((t != null) && (t.isScalarValue())) {
/* 150 */           JsonParser buffered = tokens.asParser(p);
/* 151 */           buffered.nextToken();
/* 152 */           SettableBeanProperty extProp = this._properties[i].getProperty();
/* 153 */           Object result = TypeDeserializer.deserializeIfNatural(buffered, ctxt, extProp.getType());
/* 154 */           if (result != null) {
/* 155 */             extProp.set(bean, result);
/* 156 */             continue;
/*     */           }
/*     */           
/* 159 */           if (!this._properties[i].hasDefaultType()) {
/* 160 */             ctxt.reportMappingException("Missing external type id property '%s'", new Object[] { this._properties[i].getTypePropertyName() });
/*     */           }
/*     */           else {
/* 163 */             typeId = this._properties[i].getDefaultTypeId();
/*     */           }
/*     */         }
/* 166 */       } else if (this._tokens[i] == null) {
/* 167 */         SettableBeanProperty prop = this._properties[i].getProperty();
/*     */         
/* 169 */         if ((prop.isRequired()) || (ctxt.isEnabled(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)))
/*     */         {
/* 171 */           ctxt.reportMappingException("Missing property '%s' for external type id '%s'", new Object[] { prop.getName(), this._properties[i].getTypePropertyName() });
/*     */         }
/*     */         
/* 174 */         return bean;
/*     */       }
/* 176 */       _deserializeAndSet(p, ctxt, bean, i, typeId);
/*     */     }
/* 178 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object complete(JsonParser p, DeserializationContext ctxt, PropertyValueBuffer buffer, PropertyBasedCreator creator)
/*     */     throws IOException
/*     */   {
/* 190 */     int len = this._properties.length;
/* 191 */     Object[] values = new Object[len];
/* 192 */     for (int i = 0; i < len; i++) {
/* 193 */       String typeId = this._typeIds[i];
/* 194 */       ExtTypedProperty extProp = this._properties[i];
/*     */       
/* 196 */       if (typeId == null)
/*     */       {
/* 198 */         if (this._tokens[i] == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */ 
/* 203 */         if (!extProp.hasDefaultType()) {
/* 204 */           ctxt.reportMappingException("Missing external type id property '%s'", new Object[] { extProp.getTypePropertyName() });
/*     */         }
/*     */         else {
/* 207 */           typeId = extProp.getDefaultTypeId();
/*     */         }
/* 209 */       } else if (this._tokens[i] == null) {
/* 210 */         SettableBeanProperty prop = extProp.getProperty();
/* 211 */         ctxt.reportMappingException("Missing property '%s' for external type id '%s'", new Object[] { prop.getName(), this._properties[i].getTypePropertyName() });
/*     */       }
/*     */       
/* 214 */       values[i] = _deserialize(p, ctxt, i, typeId);
/*     */       
/* 216 */       SettableBeanProperty prop = extProp.getProperty();
/*     */       
/* 218 */       if (prop.getCreatorIndex() >= 0) {
/* 219 */         buffer.assignParameter(prop, values[i]);
/*     */         
/*     */ 
/* 222 */         SettableBeanProperty typeProp = extProp.getTypeProperty();
/*     */         
/* 224 */         if ((typeProp != null) && (typeProp.getCreatorIndex() >= 0)) {
/* 225 */           buffer.assignParameter(typeProp, typeId);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 230 */     Object bean = creator.build(ctxt, buffer);
/*     */     
/* 232 */     for (int i = 0; i < len; i++) {
/* 233 */       SettableBeanProperty prop = this._properties[i].getProperty();
/* 234 */       if (prop.getCreatorIndex() < 0) {
/* 235 */         prop.set(bean, values[i]);
/*     */       }
/*     */     }
/* 238 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final Object _deserialize(JsonParser p, DeserializationContext ctxt, int index, String typeId)
/*     */     throws IOException
/*     */   {
/* 245 */     JsonParser p2 = this._tokens[index].asParser(p);
/* 246 */     JsonToken t = p2.nextToken();
/*     */     
/* 248 */     if (t == JsonToken.VALUE_NULL) {
/* 249 */       return null;
/*     */     }
/* 251 */     TokenBuffer merged = new TokenBuffer(p, ctxt);
/* 252 */     merged.writeStartArray();
/* 253 */     merged.writeString(typeId);
/* 254 */     merged.copyCurrentStructure(p2);
/* 255 */     merged.writeEndArray();
/*     */     
/*     */ 
/* 258 */     JsonParser mp = merged.asParser(p);
/* 259 */     mp.nextToken();
/* 260 */     return this._properties[index].getProperty().deserialize(mp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object bean, int index, String typeId)
/*     */     throws IOException
/*     */   {
/* 270 */     JsonParser p2 = this._tokens[index].asParser(p);
/* 271 */     JsonToken t = p2.nextToken();
/*     */     
/* 273 */     if (t == JsonToken.VALUE_NULL) {
/* 274 */       this._properties[index].getProperty().set(bean, null);
/* 275 */       return;
/*     */     }
/* 277 */     TokenBuffer merged = new TokenBuffer(p, ctxt);
/* 278 */     merged.writeStartArray();
/* 279 */     merged.writeString(typeId);
/*     */     
/* 281 */     merged.copyCurrentStructure(p2);
/* 282 */     merged.writeEndArray();
/*     */     
/* 284 */     JsonParser mp = merged.asParser(p);
/* 285 */     mp.nextToken();
/* 286 */     this._properties[index].getProperty().deserializeAndSet(mp, ctxt, bean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/* 297 */     private final ArrayList<ExternalTypeHandler.ExtTypedProperty> _properties = new ArrayList();
/* 298 */     private final HashMap<String, Integer> _nameToPropertyIndex = new HashMap();
/*     */     
/*     */     public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser)
/*     */     {
/* 302 */       Integer index = Integer.valueOf(this._properties.size());
/* 303 */       this._properties.add(new ExternalTypeHandler.ExtTypedProperty(property, typeDeser));
/* 304 */       this._nameToPropertyIndex.put(property.getName(), index);
/* 305 */       this._nameToPropertyIndex.put(typeDeser.getPropertyName(), index);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ExternalTypeHandler build(BeanPropertyMap otherProps)
/*     */     {
/* 317 */       int len = this._properties.size();
/* 318 */       ExternalTypeHandler.ExtTypedProperty[] extProps = new ExternalTypeHandler.ExtTypedProperty[len];
/* 319 */       for (int i = 0; i < len; i++) {
/* 320 */         ExternalTypeHandler.ExtTypedProperty extProp = (ExternalTypeHandler.ExtTypedProperty)this._properties.get(i);
/* 321 */         String typePropId = extProp.getTypePropertyName();
/* 322 */         SettableBeanProperty typeProp = otherProps.find(typePropId);
/* 323 */         if (typeProp != null) {
/* 324 */           extProp.linkTypeProperty(typeProp);
/*     */         }
/* 326 */         extProps[i] = extProp;
/*     */       }
/* 328 */       return new ExternalTypeHandler(extProps, this._nameToPropertyIndex, null, null);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public ExternalTypeHandler build() {
/* 333 */       return new ExternalTypeHandler((ExternalTypeHandler.ExtTypedProperty[])this._properties.toArray(new ExternalTypeHandler.ExtTypedProperty[this._properties.size()]), this._nameToPropertyIndex, null, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ExtTypedProperty
/*     */   {
/*     */     private final SettableBeanProperty _property;
/*     */     
/*     */     private final TypeDeserializer _typeDeserializer;
/*     */     
/*     */     private final String _typePropertyName;
/*     */     
/*     */     private SettableBeanProperty _typeProperty;
/*     */     
/*     */ 
/*     */     public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser)
/*     */     {
/* 351 */       this._property = property;
/* 352 */       this._typeDeserializer = typeDeser;
/* 353 */       this._typePropertyName = typeDeser.getPropertyName();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void linkTypeProperty(SettableBeanProperty p)
/*     */     {
/* 360 */       this._typeProperty = p;
/*     */     }
/*     */     
/*     */     public boolean hasTypePropertyName(String n) {
/* 364 */       return n.equals(this._typePropertyName);
/*     */     }
/*     */     
/*     */     public boolean hasDefaultType() {
/* 368 */       return this._typeDeserializer.getDefaultImpl() != null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getDefaultTypeId()
/*     */     {
/* 377 */       Class<?> defaultType = this._typeDeserializer.getDefaultImpl();
/* 378 */       if (defaultType == null) {
/* 379 */         return null;
/*     */       }
/* 381 */       return this._typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
/*     */     }
/*     */     
/* 384 */     public String getTypePropertyName() { return this._typePropertyName; }
/*     */     
/*     */     public SettableBeanProperty getProperty() {
/* 387 */       return this._property;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public SettableBeanProperty getTypeProperty()
/*     */     {
/* 394 */       return this._typeProperty;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\ExternalTypeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */