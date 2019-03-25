/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ class FactoryBasedEnumDeserializer
/*     */   extends StdDeserializer<Object>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _inputType;
/*     */   protected final boolean _hasArgs;
/*     */   protected final AnnotatedMethod _factory;
/*     */   protected final JsonDeserializer<?> _deser;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final SettableBeanProperty[] _creatorProps;
/*     */   private transient PropertyBasedCreator _propCreator;
/*     */   
/*     */   public FactoryBasedEnumDeserializer(Class<?> cls, AnnotatedMethod f, JavaType paramType, ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps)
/*     */   {
/*  54 */     super(cls);
/*  55 */     this._factory = f;
/*  56 */     this._hasArgs = true;
/*     */     
/*  58 */     this._inputType = (paramType.hasRawClass(String.class) ? null : paramType);
/*  59 */     this._deser = null;
/*  60 */     this._valueInstantiator = valueInstantiator;
/*  61 */     this._creatorProps = creatorProps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FactoryBasedEnumDeserializer(Class<?> cls, AnnotatedMethod f)
/*     */   {
/*  69 */     super(cls);
/*  70 */     this._factory = f;
/*  71 */     this._hasArgs = false;
/*  72 */     this._inputType = null;
/*  73 */     this._deser = null;
/*  74 */     this._valueInstantiator = null;
/*  75 */     this._creatorProps = null;
/*     */   }
/*     */   
/*     */   protected FactoryBasedEnumDeserializer(FactoryBasedEnumDeserializer base, JsonDeserializer<?> deser)
/*     */   {
/*  80 */     super(base._valueClass);
/*  81 */     this._inputType = base._inputType;
/*  82 */     this._factory = base._factory;
/*  83 */     this._hasArgs = base._hasArgs;
/*  84 */     this._valueInstantiator = base._valueInstantiator;
/*  85 */     this._creatorProps = base._creatorProps;
/*     */     
/*  87 */     this._deser = deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  95 */     if ((this._deser == null) && (this._inputType != null) && (this._creatorProps == null)) {
/*  96 */       return new FactoryBasedEnumDeserializer(this, ctxt.findContextualValueDeserializer(this._inputType, property));
/*     */     }
/*     */     
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 105 */     Object value = null;
/* 106 */     if (this._deser != null) {
/* 107 */       value = this._deser.deserialize(p, ctxt);
/* 108 */     } else if (this._hasArgs) {
/* 109 */       JsonToken curr = p.getCurrentToken();
/*     */       
/*     */ 
/* 112 */       if ((curr == JsonToken.VALUE_STRING) || (curr == JsonToken.FIELD_NAME)) {
/* 113 */         value = p.getText();
/* 114 */       } else { if ((this._creatorProps != null) && (p.isExpectedStartObjectToken())) {
/* 115 */           if (this._propCreator == null) {
/* 116 */             this._propCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, this._creatorProps);
/*     */           }
/* 118 */           p.nextToken();
/* 119 */           return deserializeEnumUsingPropertyBased(p, ctxt, this._propCreator);
/*     */         }
/* 121 */         value = p.getValueAsString();
/*     */       }
/*     */     } else {
/* 124 */       p.skipChildren();
/*     */       try {
/* 126 */         return this._factory.call();
/*     */       } catch (Exception e) {
/* 128 */         Throwable t = ClassUtil.throwRootCauseIfIOE(e);
/* 129 */         return ctxt.handleInstantiationProblem(this._valueClass, null, t);
/*     */       }
/*     */     }
/*     */     try {
/* 133 */       return this._factory.callOnWith(this._valueClass, new Object[] { value });
/*     */     } catch (Exception e) {
/* 135 */       Throwable t = ClassUtil.throwRootCauseIfIOE(e);
/*     */       
/* 137 */       if ((ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) && ((t instanceof IllegalArgumentException)))
/*     */       {
/* 139 */         return null;
/*     */       }
/* 141 */       return ctxt.handleInstantiationProblem(this._valueClass, value, t);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException
/*     */   {
/* 147 */     if (this._deser == null) {
/* 148 */       return deserialize(p, ctxt);
/*     */     }
/* 150 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeEnumUsingPropertyBased(JsonParser p, DeserializationContext ctxt, PropertyBasedCreator creator)
/*     */     throws IOException
/*     */   {
/* 157 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
/*     */     
/* 159 */     for (JsonToken t = p.getCurrentToken(); 
/* 160 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 161 */       String propName = p.getCurrentName();
/* 162 */       p.nextToken();
/*     */       
/* 164 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 165 */       if (creatorProp != null) {
/* 166 */         buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp));
/*     */ 
/*     */       }
/* 169 */       else if (buffer.readIdProperty(propName)) {}
/*     */     }
/*     */     
/*     */ 
/* 173 */     return creator.build(ctxt, buffer);
/*     */   }
/*     */   
/*     */   protected final Object _deserializeWithErrorWrapping(JsonParser p, DeserializationContext ctxt, SettableBeanProperty prop)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 181 */       return prop.deserialize(p, ctxt);
/*     */     } catch (Exception e) {
/* 183 */       wrapAndThrow(e, this._valueClass.getClass(), prop.getName(), ctxt);
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 192 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
/*     */   }
/*     */   
/*     */   private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt) throws IOException
/*     */   {
/* 197 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 198 */       t = t.getCause();
/*     */     }
/*     */     
/* 201 */     if ((t instanceof Error)) {
/* 202 */       throw ((Error)t);
/*     */     }
/* 204 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*     */     
/*     */ 
/* 207 */     if ((t instanceof IOException)) {
/* 208 */       if ((!wrap) || (!(t instanceof JsonProcessingException))) {
/* 209 */         throw ((IOException)t);
/*     */       }
/* 211 */     } else if ((!wrap) && 
/* 212 */       ((t instanceof RuntimeException))) {
/* 213 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/* 216 */     return t;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\FactoryBasedEnumDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */