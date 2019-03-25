/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
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
/*     */ public final class PropertyBasedCreator
/*     */ {
/*     */   protected final int _propertyCount;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final HashMap<String, SettableBeanProperty> _propertyLookup;
/*     */   protected final SettableBeanProperty[] _allProperties;
/*     */   
/*     */   protected PropertyBasedCreator(ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps, boolean caseInsensitive)
/*     */   {
/*  60 */     this._valueInstantiator = valueInstantiator;
/*  61 */     if (caseInsensitive) {
/*  62 */       this._propertyLookup = new CaseInsensitiveMap();
/*     */     } else {
/*  64 */       this._propertyLookup = new HashMap();
/*     */     }
/*  66 */     int len = creatorProps.length;
/*  67 */     this._propertyCount = len;
/*  68 */     this._allProperties = new SettableBeanProperty[len];
/*  69 */     for (int i = 0; i < len; i++) {
/*  70 */       SettableBeanProperty prop = creatorProps[i];
/*  71 */       this._allProperties[i] = prop;
/*  72 */       this._propertyLookup.put(prop.getName(), prop);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyBasedCreator construct(DeserializationContext ctxt, ValueInstantiator valueInstantiator, SettableBeanProperty[] srcProps)
/*     */     throws JsonMappingException
/*     */   {
/*  84 */     int len = srcProps.length;
/*  85 */     SettableBeanProperty[] creatorProps = new SettableBeanProperty[len];
/*  86 */     for (int i = 0; i < len; i++) {
/*  87 */       SettableBeanProperty prop = srcProps[i];
/*  88 */       if (!prop.hasValueDeserializer()) {
/*  89 */         prop = prop.withValueDeserializer(ctxt.findContextualValueDeserializer(prop.getType(), prop));
/*     */       }
/*  91 */       creatorProps[i] = prop;
/*     */     }
/*  93 */     return new PropertyBasedCreator(valueInstantiator, creatorProps, ctxt.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<SettableBeanProperty> properties()
/*     */   {
/* 104 */     return this._propertyLookup.values();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(String name) {
/* 108 */     return (SettableBeanProperty)this._propertyLookup.get(name);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findCreatorProperty(int propertyIndex) {
/* 112 */     for (SettableBeanProperty prop : this._propertyLookup.values()) {
/* 113 */       if (prop.getPropertyIndex() == propertyIndex) {
/* 114 */         return prop;
/*     */       }
/*     */     }
/* 117 */     return null;
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
/*     */   public PropertyValueBuffer startBuilding(JsonParser p, DeserializationContext ctxt, ObjectIdReader oir)
/*     */   {
/* 133 */     return new PropertyValueBuffer(p, ctxt, this._propertyCount, oir);
/*     */   }
/*     */   
/*     */   public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException
/*     */   {
/* 138 */     Object bean = this._valueInstantiator.createFromObjectWith(ctxt, this._allProperties, buffer);
/*     */     
/*     */ 
/* 141 */     if (bean != null)
/*     */     {
/* 143 */       bean = buffer.handleIdValue(ctxt, bean);
/*     */       
/*     */ 
/* 146 */       for (PropertyValue pv = buffer.buffered(); pv != null; pv = pv.next) {
/* 147 */         pv.assign(bean);
/*     */       }
/*     */     }
/* 150 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class CaseInsensitiveMap
/*     */     extends HashMap<String, SettableBeanProperty>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public SettableBeanProperty get(Object key0)
/*     */     {
/* 171 */       String key = (String)key0;
/* 172 */       return (SettableBeanProperty)super.get(key.toLowerCase());
/*     */     }
/*     */     
/*     */     public SettableBeanProperty put(String key, SettableBeanProperty value)
/*     */     {
/* 177 */       key = key.toLowerCase();
/* 178 */       return (SettableBeanProperty)super.put(key, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyBasedCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */