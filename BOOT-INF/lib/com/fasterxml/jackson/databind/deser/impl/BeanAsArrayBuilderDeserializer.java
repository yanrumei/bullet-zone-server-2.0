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
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class BeanAsArrayBuilderDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanDeserializerBase _delegate;
/*     */   protected final SettableBeanProperty[] _orderedProperties;
/*     */   protected final AnnotatedMethod _buildMethod;
/*     */   
/*     */   public BeanAsArrayBuilderDeserializer(BeanDeserializerBase delegate, SettableBeanProperty[] ordered, AnnotatedMethod buildMethod)
/*     */   {
/*  44 */     super(delegate);
/*  45 */     this._delegate = delegate;
/*  46 */     this._orderedProperties = ordered;
/*  47 */     this._buildMethod = buildMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  57 */     return this._delegate.unwrappingDeserializer(unwrapper);
/*     */   }
/*     */   
/*     */   public BeanDeserializerBase withObjectIdReader(ObjectIdReader oir)
/*     */   {
/*  62 */     return new BeanAsArrayBuilderDeserializer(this._delegate.withObjectIdReader(oir), this._orderedProperties, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanDeserializerBase withIgnorableProperties(Set<String> ignorableProps)
/*     */   {
/*  68 */     return new BeanAsArrayBuilderDeserializer(this._delegate.withIgnorableProperties(ignorableProps), this._orderedProperties, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props)
/*     */   {
/*  74 */     return new BeanAsArrayBuilderDeserializer(this._delegate.withBeanProperties(props), this._orderedProperties, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanDeserializerBase asArrayDeserializer()
/*     */   {
/*  80 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object finishBuild(DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  93 */       return this._buildMethod.getMember().invoke(builder, new Object[0]);
/*     */     } catch (Exception e) {
/*  95 */       return wrapInstantiationProblem(e, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 104 */     if (!p.isExpectedStartArrayToken()) {
/* 105 */       return finishBuild(ctxt, _deserializeFromNonArray(p, ctxt));
/*     */     }
/* 107 */     if (!this._vanillaProcessing) {
/* 108 */       return finishBuild(ctxt, _deserializeNonVanilla(p, ctxt));
/*     */     }
/* 110 */     Object builder = this._valueInstantiator.createUsingDefault(ctxt);
/* 111 */     SettableBeanProperty[] props = this._orderedProperties;
/* 112 */     int i = 0;
/* 113 */     int propCount = props.length;
/*     */     for (;;) {
/* 115 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 116 */         return finishBuild(ctxt, builder);
/*     */       }
/* 118 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 121 */       SettableBeanProperty prop = props[i];
/* 122 */       if (prop != null) {
/*     */         try {
/* 124 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 126 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 129 */         p.skipChildren();
/*     */       }
/* 131 */       i++;
/*     */     }
/*     */     
/* 134 */     if (!this._ignoreAllUnknown) {
/* 135 */       ctxt.reportMappingException("Unexpected JSON values; expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 140 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 141 */       p.skipChildren();
/*     */     }
/* 143 */     return finishBuild(ctxt, builder);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/* 153 */     if (this._injectables != null) {
/* 154 */       injectValues(ctxt, builder);
/*     */     }
/* 156 */     SettableBeanProperty[] props = this._orderedProperties;
/* 157 */     int i = 0;
/* 158 */     int propCount = props.length;
/*     */     for (;;) {
/* 160 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 161 */         return finishBuild(ctxt, builder);
/*     */       }
/* 163 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 166 */       SettableBeanProperty prop = props[i];
/* 167 */       if (prop != null) {
/*     */         try {
/* 169 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 171 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       } else {
/* 174 */         p.skipChildren();
/*     */       }
/* 176 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 180 */     if (!this._ignoreAllUnknown) {
/* 181 */       ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Unexpected JSON values; expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 188 */       p.skipChildren();
/* 189 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/* 190 */     return finishBuild(ctxt, builder);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 197 */     return _deserializeFromNonArray(p, ctxt);
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
/*     */   protected Object _deserializeNonVanilla(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 216 */     if (this._nonStandardCreation) {
/* 217 */       return deserializeFromObjectUsingNonDefault(p, ctxt);
/*     */     }
/* 219 */     Object builder = this._valueInstantiator.createUsingDefault(ctxt);
/* 220 */     if (this._injectables != null) {
/* 221 */       injectValues(ctxt, builder);
/*     */     }
/* 223 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 224 */     SettableBeanProperty[] props = this._orderedProperties;
/* 225 */     int i = 0;
/* 226 */     int propCount = props.length;
/*     */     for (;;) {
/* 228 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 229 */         return builder;
/*     */       }
/* 231 */       if (i == propCount) {
/*     */         break;
/*     */       }
/* 234 */       SettableBeanProperty prop = props[i];
/* 235 */       i++;
/* 236 */       if ((prop != null) && (
/* 237 */         (activeView == null) || (prop.visibleInView(activeView)))) {
/*     */         try {
/* 239 */           prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 241 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 247 */         p.skipChildren();
/*     */       }
/*     */     }
/* 250 */     if (!this._ignoreAllUnknown) {
/* 251 */       ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Unexpected JSON value(s); expected at most %d properties (in JSON Array)", new Object[] { Integer.valueOf(propCount) });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 257 */     while (p.nextToken() != JsonToken.END_ARRAY) {
/* 258 */       p.skipChildren();
/*     */     }
/* 260 */     return builder;
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 276 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 277 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 279 */     SettableBeanProperty[] props = this._orderedProperties;
/* 280 */     int propCount = props.length;
/* 281 */     int i = 0;
/* 282 */     Object builder = null;
/* 284 */     for (; 
/* 284 */         p.nextToken() != JsonToken.END_ARRAY; i++) {
/* 285 */       SettableBeanProperty prop = i < propCount ? props[i] : null;
/* 286 */       if (prop == null) {
/* 287 */         p.skipChildren();
/*     */ 
/*     */ 
/*     */       }
/* 291 */       else if (builder != null) {
/*     */         try {
/* 293 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 295 */           wrapAndThrow(e, builder, prop.getName(), ctxt);
/*     */         }
/*     */       }
/*     */       else {
/* 299 */         String propName = prop.getName();
/*     */         
/* 301 */         SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 302 */         if (creatorProp != null)
/*     */         {
/* 304 */           if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
/*     */             try {
/* 306 */               builder = creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 308 */               wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 309 */               continue;
/*     */             }
/*     */             
/* 312 */             if (builder.getClass() != this._beanType.getRawClass())
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/* 317 */               ctxt.reportMappingException("Can not support implicit polymorphic deserialization for POJOs-as-Arrays style: nominal type %s, actual type %s", new Object[] { this._beanType.getRawClass().getName(), builder.getClass().getName() });
/*     */               
/*     */ 
/* 320 */               return null;
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/* 326 */         else if (!buffer.readIdProperty(propName))
/*     */         {
/*     */ 
/*     */ 
/* 330 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */         }
/*     */       }
/*     */     }
/* 334 */     if (builder == null) {
/*     */       try {
/* 336 */         builder = creator.build(ctxt, buffer);
/*     */       } catch (Exception e) {
/* 338 */         return wrapInstantiationProblem(e, ctxt);
/*     */       }
/*     */     }
/* 341 */     return builder;
/*     */   }
/*     */   
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
/* 354 */     return ctxt.handleUnexpectedToken(handledType(), p.getCurrentToken(), p, "Can not deserialize a POJO (of type %s) from non-Array representation (token: %s): type/property designed to be serialized as JSON Array", new Object[] { this._beanType.getRawClass().getName(), p.getCurrentToken() });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\BeanAsArrayBuilderDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */