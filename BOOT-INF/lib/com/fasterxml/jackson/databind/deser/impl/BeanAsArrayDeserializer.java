/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
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
/*     */ public class BeanAsArrayDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanDeserializerBase _delegate;
/*     */   protected final SettableBeanProperty[] _orderedProperties;
/*     */   
/*     */   public BeanAsArrayDeserializer(BeanDeserializerBase delegate, SettableBeanProperty[] ordered)
/*     */   {
/*  47 */     super(delegate);
/*  48 */     this._delegate = delegate;
/*  49 */     this._orderedProperties = ordered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  59 */     return this._delegate.unwrappingDeserializer(unwrapper);
/*     */   }
/*     */   
/*     */   public BeanDeserializerBase withObjectIdReader(ObjectIdReader oir)
/*     */   {
/*  64 */     return new BeanAsArrayDeserializer(this._delegate.withObjectIdReader(oir), this._orderedProperties);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanDeserializerBase withIgnorableProperties(Set<String> ignorableProps)
/*     */   {
/*  70 */     return new BeanAsArrayDeserializer(this._delegate.withIgnorableProperties(ignorableProps), this._orderedProperties);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props)
/*     */   {
/*  76 */     return new BeanAsArrayDeserializer(this._delegate.withBeanProperties(props), this._orderedProperties);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanDeserializerBase asArrayDeserializer()
/*     */   {
/*  82 */     return this;
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  96 */     if (!p.isExpectedStartArrayToken()) {
/*  97 */       return _deserializeFromNonArray(p, ctxt);
/*     */     }
/*  99 */     if (!this._vanillaProcessing) {
/* 100 */       return _deserializeNonVanilla(p, ctxt);
/*     */     }
/* 102 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 104 */     p.setCurrentValue(bean);
/*     */     
/* 106 */     SettableBeanProperty[] props = this._orderedProperties;
/* 107 */     int i = 0;
/* 108 */     int propCount = props.length;
/*     */     for (;;) {
/* 110 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 111 */         return bean;
/*     */       }
/* 113 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 116 */       SettableBeanProperty prop = props[i];
/* 117 */       if (prop != null) {
/*     */         try {
/* 119 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 121 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 124 */         p.skipChildren();
/*     */       }
/* 126 */       i++;
/*     */     }
/*     */     
/* 129 */     if (!this._ignoreAllUnknown) {
/* 130 */       ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Unexpected JSON values; expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 137 */       p.skipChildren();
/* 138 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/* 139 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 147 */     p.setCurrentValue(bean);
/*     */     
/*     */ 
/*     */ 
/* 151 */     if (this._injectables != null) {
/* 152 */       injectValues(ctxt, bean);
/*     */     }
/* 154 */     SettableBeanProperty[] props = this._orderedProperties;
/* 155 */     int i = 0;
/* 156 */     int propCount = props.length;
/*     */     for (;;) {
/* 158 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 159 */         return bean;
/*     */       }
/* 161 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 164 */       SettableBeanProperty prop = props[i];
/* 165 */       if (prop != null) {
/*     */         try {
/* 167 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 169 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 172 */         p.skipChildren();
/*     */       }
/* 174 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 178 */     if (!this._ignoreAllUnknown) {
/* 179 */       ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Unexpected JSON values; expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 186 */       p.skipChildren();
/* 187 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/* 188 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 196 */     return _deserializeFromNonArray(p, ctxt);
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
/*     */   protected Object _deserializeNonVanilla(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 212 */     if (this._nonStandardCreation) {
/* 213 */       return deserializeFromObjectUsingNonDefault(p, ctxt);
/*     */     }
/* 215 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 217 */     p.setCurrentValue(bean);
/* 218 */     if (this._injectables != null) {
/* 219 */       injectValues(ctxt, bean);
/*     */     }
/* 221 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 222 */     SettableBeanProperty[] props = this._orderedProperties;
/* 223 */     int i = 0;
/* 224 */     int propCount = props.length;
/*     */     for (;;) {
/* 226 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 227 */         return bean;
/*     */       }
/* 229 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 232 */       SettableBeanProperty prop = props[i];
/* 233 */       i++;
/* 234 */       if ((prop != null) && (
/* 235 */         (activeView == null) || (prop.visibleInView(activeView)))) {
/*     */         try {
/* 237 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 239 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 245 */         p.skipChildren();
/*     */       }
/*     */     }
/* 248 */     if (!this._ignoreAllUnknown) {
/* 249 */       ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Unexpected JSON values; expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 256 */       p.skipChildren();
/* 257 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/* 258 */     return bean;
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 273 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 274 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 276 */     SettableBeanProperty[] props = this._orderedProperties;
/* 277 */     int propCount = props.length;
/* 278 */     int i = 0;
/* 279 */     Object bean = null;
/* 281 */     for (; 
/* 281 */         p.nextToken() != JsonToken.END_ARRAY; i++) {
/* 282 */       SettableBeanProperty prop = i < propCount ? props[i] : null;
/* 283 */       if (prop == null) {
/* 284 */         p.skipChildren();
/*     */ 
/*     */ 
/*     */       }
/* 288 */       else if (bean != null) {
/*     */         try {
/* 290 */           prop.deserializeAndSet(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 292 */           wrapAndThrow(e, bean, prop.getName(), ctxt);
/*     */         }
/*     */       }
/*     */       else {
/* 296 */         String propName = prop.getName();
/*     */         
/* 298 */         SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 299 */         if (creatorProp != null)
/*     */         {
/* 301 */           if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
/*     */             try {
/* 303 */               bean = creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 305 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 306 */               continue;
/*     */             }
/*     */             
/* 309 */             p.setCurrentValue(bean);
/*     */             
/*     */ 
/* 312 */             if (bean.getClass() != this._beanType.getRawClass())
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 317 */               ctxt.reportMappingException("Can not support implicit polymorphic deserialization for POJOs-as-Arrays style: nominal type %s, actual type %s", new Object[] { this._beanType.getRawClass().getName(), bean.getClass().getName() });
/*     */ 
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 325 */         else if (!buffer.readIdProperty(propName))
/*     */         {
/*     */ 
/*     */ 
/* 329 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */         }
/*     */       }
/*     */     }
/* 333 */     if (bean == null) {
/*     */       try {
/* 335 */         bean = creator.build(ctxt, buffer);
/*     */       } catch (Exception e) {
/* 337 */         return wrapInstantiationProblem(e, ctxt);
/*     */       }
/*     */     }
/* 340 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeFromNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 352 */     return ctxt.handleUnexpectedToken(handledType(), p.getCurrentToken(), p, "Can not deserialize a POJO (of type %s) from non-Array representation (token: %s): type/property designed to be serialized as JSON Array", new Object[] { this._beanType.getRawClass().getName(), p.getCurrentToken() });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\BeanAsArrayDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */