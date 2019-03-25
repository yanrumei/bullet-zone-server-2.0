/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import java.io.IOException;
/*     */ import java.util.BitSet;
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
/*     */ public class PropertyValueBuffer
/*     */ {
/*     */   protected final JsonParser _parser;
/*     */   protected final DeserializationContext _context;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   protected final Object[] _creatorParameters;
/*     */   protected int _paramsNeeded;
/*     */   protected int _paramsSeen;
/*     */   protected final BitSet _paramsSeenBig;
/*     */   protected PropertyValue _buffered;
/*     */   protected Object _idValue;
/*     */   
/*     */   public PropertyValueBuffer(JsonParser p, DeserializationContext ctxt, int paramCount, ObjectIdReader oir)
/*     */   {
/*  88 */     this._parser = p;
/*  89 */     this._context = ctxt;
/*  90 */     this._paramsNeeded = paramCount;
/*  91 */     this._objectIdReader = oir;
/*  92 */     this._creatorParameters = new Object[paramCount];
/*  93 */     if (paramCount < 32) {
/*  94 */       this._paramsSeenBig = null;
/*     */     } else {
/*  96 */       this._paramsSeenBig = new BitSet();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasParameter(SettableBeanProperty prop)
/*     */   {
/* 108 */     if (this._paramsSeenBig == null) {
/* 109 */       return (this._paramsSeen >> prop.getCreatorIndex() & 0x1) == 1;
/*     */     }
/* 111 */     return this._paramsSeenBig.get(prop.getCreatorIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getParameter(SettableBeanProperty prop)
/*     */     throws JsonMappingException
/*     */   {
/*     */     Object value;
/*     */     
/*     */ 
/*     */ 
/*     */     Object value;
/*     */     
/*     */ 
/*     */ 
/* 128 */     if (hasParameter(prop)) {
/* 129 */       value = this._creatorParameters[prop.getCreatorIndex()];
/*     */     } else {
/* 131 */       value = this._creatorParameters[prop.getCreatorIndex()] = _findMissing(prop);
/*     */     }
/* 133 */     if ((value == null) && (this._context.isEnabled(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES))) {
/* 134 */       throw this._context.mappingException("Null value for creator property '%s'; DeserializationFeature.FAIL_ON_NULL_FOR_CREATOR_PARAMETERS enabled", new Object[] { prop.getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/*     */     
/*     */ 
/* 138 */     return value;
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
/*     */   public Object[] getParameters(SettableBeanProperty[] props)
/*     */     throws JsonMappingException
/*     */   {
/* 152 */     if (this._paramsNeeded > 0) {
/* 153 */       if (this._paramsSeenBig == null) {
/* 154 */         int mask = this._paramsSeen;
/*     */         
/*     */ 
/* 157 */         int ix = 0; for (int len = this._creatorParameters.length; ix < len; mask >>= 1) {
/* 158 */           if ((mask & 0x1) == 0) {
/* 159 */             this._creatorParameters[ix] = _findMissing(props[ix]);
/*     */           }
/* 157 */           ix++;
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 163 */         int len = this._creatorParameters.length;
/* 164 */         for (int ix = 0; (ix = this._paramsSeenBig.nextClearBit(ix)) < len; ix++) {
/* 165 */           this._creatorParameters[ix] = _findMissing(props[ix]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 170 */     if (this._context.isEnabled(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)) {
/* 171 */       for (int ix = 0; ix < props.length; ix++) {
/* 172 */         if (this._creatorParameters[ix] == null) {
/* 173 */           this._context.reportMappingException("Null value for creator property '%s'; DeserializationFeature.FAIL_ON_NULL_FOR_CREATOR_PARAMETERS enabled", new Object[] { props[ix].getName(), Integer.valueOf(props[ix].getCreatorIndex()) });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 179 */     return this._creatorParameters;
/*     */   }
/*     */   
/*     */   protected Object _findMissing(SettableBeanProperty prop)
/*     */     throws JsonMappingException
/*     */   {
/* 185 */     Object injectableValueId = prop.getInjectableValueId();
/* 186 */     if (injectableValueId != null) {
/* 187 */       return this._context.findInjectableValue(prop.getInjectableValueId(), prop, null);
/*     */     }
/*     */     
/*     */ 
/* 191 */     if (prop.isRequired()) {
/* 192 */       this._context.reportMappingException("Missing required creator property '%s' (index %d)", new Object[] { prop.getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/*     */     
/* 195 */     if (this._context.isEnabled(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)) {
/* 196 */       this._context.reportMappingException("Missing creator property '%s' (index %d); DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES enabled", new Object[] { prop.getName(), Integer.valueOf(prop.getCreatorIndex()) });
/*     */     }
/*     */     
/*     */ 
/* 200 */     JsonDeserializer<Object> deser = prop.getValueDeserializer();
/* 201 */     return deser.getNullValue(this._context);
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
/*     */   public boolean readIdProperty(String propName)
/*     */     throws IOException
/*     */   {
/* 218 */     if ((this._objectIdReader != null) && (propName.equals(this._objectIdReader.propertyName.getSimpleName()))) {
/* 219 */       this._idValue = this._objectIdReader.readObjectReference(this._parser, this._context);
/* 220 */       return true;
/*     */     }
/* 222 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object handleIdValue(DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 230 */     if (this._objectIdReader != null) {
/* 231 */       if (this._idValue != null) {
/* 232 */         ReadableObjectId roid = ctxt.findObjectId(this._idValue, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 233 */         roid.bindItem(bean);
/*     */         
/* 235 */         SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 236 */         if (idProp != null) {
/* 237 */           return idProp.setAndReturn(bean, this._idValue);
/*     */         }
/*     */       }
/*     */       else {
/* 241 */         ctxt.reportUnresolvedObjectId(this._objectIdReader, bean);
/*     */       }
/*     */     }
/* 244 */     return bean;
/*     */   }
/*     */   
/* 247 */   protected PropertyValue buffered() { return this._buffered; }
/*     */   
/* 249 */   public boolean isComplete() { return this._paramsNeeded <= 0; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean assignParameter(SettableBeanProperty prop, Object value)
/*     */   {
/* 261 */     int ix = prop.getCreatorIndex();
/* 262 */     this._creatorParameters[ix] = value;
/*     */     
/* 264 */     if (this._paramsSeenBig == null) {
/* 265 */       int old = this._paramsSeen;
/* 266 */       int newValue = old | 1 << ix;
/* 267 */       if (old != newValue) {
/* 268 */         this._paramsSeen = newValue;
/* 269 */         if (--this._paramsNeeded <= 0)
/*     */         {
/* 271 */           return (this._objectIdReader == null) || (this._idValue != null);
/*     */         }
/*     */       }
/*     */     }
/* 275 */     else if (!this._paramsSeenBig.get(ix)) {
/* 276 */       this._paramsSeenBig.set(ix);
/* 277 */       if (--this._paramsNeeded > 0) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   public void bufferProperty(SettableBeanProperty prop, Object value) {
/* 286 */     this._buffered = new PropertyValue.Regular(this._buffered, value, prop);
/*     */   }
/*     */   
/*     */   public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
/* 290 */     this._buffered = new PropertyValue.Any(this._buffered, value, prop, propName);
/*     */   }
/*     */   
/*     */   public void bufferMapProperty(Object key, Object value) {
/* 294 */     this._buffered = new PropertyValue.Map(this._buffered, value, key);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\PropertyValueBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */