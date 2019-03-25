/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ public class AbstractDeserializer
/*     */   extends JsonDeserializer<Object>
/*     */   implements ContextualDeserializer, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _baseType;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   protected final Map<String, SettableBeanProperty> _backRefProperties;
/*     */   protected final boolean _acceptString;
/*     */   protected final boolean _acceptBoolean;
/*     */   protected final boolean _acceptInt;
/*     */   protected final boolean _acceptDouble;
/*     */   
/*     */   public AbstractDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, Map<String, SettableBeanProperty> backRefProps)
/*     */   {
/*  54 */     this._baseType = beanDesc.getType();
/*  55 */     this._objectIdReader = builder.getObjectIdReader();
/*  56 */     this._backRefProperties = backRefProps;
/*  57 */     Class<?> cls = this._baseType.getRawClass();
/*  58 */     this._acceptString = cls.isAssignableFrom(String.class);
/*  59 */     this._acceptBoolean = ((cls == Boolean.TYPE) || (cls.isAssignableFrom(Boolean.class)));
/*  60 */     this._acceptInt = ((cls == Integer.TYPE) || (cls.isAssignableFrom(Integer.class)));
/*  61 */     this._acceptDouble = ((cls == Double.TYPE) || (cls.isAssignableFrom(Double.class)));
/*     */   }
/*     */   
/*     */   protected AbstractDeserializer(BeanDescription beanDesc)
/*     */   {
/*  66 */     this._baseType = beanDesc.getType();
/*  67 */     this._objectIdReader = null;
/*  68 */     this._backRefProperties = null;
/*  69 */     Class<?> cls = this._baseType.getRawClass();
/*  70 */     this._acceptString = cls.isAssignableFrom(String.class);
/*  71 */     this._acceptBoolean = ((cls == Boolean.TYPE) || (cls.isAssignableFrom(Boolean.class)));
/*  72 */     this._acceptInt = ((cls == Integer.TYPE) || (cls.isAssignableFrom(Integer.class)));
/*  73 */     this._acceptDouble = ((cls == Double.TYPE) || (cls.isAssignableFrom(Double.class)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractDeserializer(AbstractDeserializer base, ObjectIdReader objectIdReader)
/*     */   {
/*  82 */     this._baseType = base._baseType;
/*  83 */     this._backRefProperties = base._backRefProperties;
/*  84 */     this._acceptString = base._acceptString;
/*  85 */     this._acceptBoolean = base._acceptBoolean;
/*  86 */     this._acceptInt = base._acceptInt;
/*  87 */     this._acceptDouble = base._acceptDouble;
/*     */     
/*  89 */     this._objectIdReader = objectIdReader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AbstractDeserializer constructForNonPOJO(BeanDescription beanDesc)
/*     */   {
/*  99 */     return new AbstractDeserializer(beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 107 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 108 */     AnnotatedMember accessor = (property == null) || (intr == null) ? null : property.getMember();
/*     */     
/* 110 */     if ((accessor != null) && (intr != null)) {
/* 111 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
/* 112 */       if (objectIdInfo != null)
/*     */       {
/* 114 */         objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
/*     */         
/* 116 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */         
/*     */ 
/* 119 */         if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 120 */           ctxt.reportMappingException("Invalid Object Id definition for abstract type %s: can not use `PropertyGenerator` on polymorphic types using property annotation", new Object[] { handledType().getName() });
/*     */         }
/*     */         
/*     */ 
/* 124 */         ObjectIdResolver resolver = ctxt.objectIdResolverInstance(accessor, objectIdInfo);
/* 125 */         JavaType type = ctxt.constructType(implClass);
/* 126 */         JavaType idType = ctxt.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 127 */         SettableBeanProperty idProp = null;
/* 128 */         ObjectIdGenerator<?> idGen = ctxt.objectIdGeneratorInstance(accessor, objectIdInfo);
/* 129 */         JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/* 130 */         ObjectIdReader oir = ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), idGen, deser, idProp, resolver);
/*     */         
/* 132 */         return new AbstractDeserializer(this, oir);
/*     */       }
/*     */     }
/*     */     
/* 136 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> handledType()
/*     */   {
/* 147 */     return this._baseType.getRawClass();
/*     */   }
/*     */   
/*     */   public boolean isCachable() {
/* 151 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdReader getObjectIdReader()
/*     */   {
/* 160 */     return this._objectIdReader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty findBackReference(String logicalName)
/*     */   {
/* 169 */     return this._backRefProperties == null ? null : (SettableBeanProperty)this._backRefProperties.get(logicalName);
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
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 185 */     if (this._objectIdReader != null) {
/* 186 */       JsonToken t = p.getCurrentToken();
/* 187 */       if (t != null)
/*     */       {
/* 189 */         if (t.isScalarValue()) {
/* 190 */           return _deserializeFromObjectId(p, ctxt);
/*     */         }
/*     */         
/* 193 */         if (t == JsonToken.START_OBJECT) {
/* 194 */           t = p.nextToken();
/*     */         }
/* 196 */         if ((t == JsonToken.FIELD_NAME) && (this._objectIdReader.maySerializeAsObject()) && (this._objectIdReader.isValidReferencePropertyName(p.getCurrentName(), p)))
/*     */         {
/* 198 */           return _deserializeFromObjectId(p, ctxt);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 205 */     Object result = _deserializeIfNatural(p, ctxt);
/* 206 */     if (result != null) {
/* 207 */       return result;
/*     */     }
/* 209 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 216 */     return ctxt.handleMissingInstantiator(this._baseType.getRawClass(), p, "abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information", new Object[0]);
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
/*     */   protected Object _deserializeIfNatural(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 234 */     switch (p.getCurrentTokenId()) {
/*     */     case 6: 
/* 236 */       if (this._acceptString) {
/* 237 */         return p.getText();
/*     */       }
/*     */       break;
/*     */     case 7: 
/* 241 */       if (this._acceptInt) {
/* 242 */         return Integer.valueOf(p.getIntValue());
/*     */       }
/*     */       break;
/*     */     case 8: 
/* 246 */       if (this._acceptDouble) {
/* 247 */         return Double.valueOf(p.getDoubleValue());
/*     */       }
/*     */       break;
/*     */     case 9: 
/* 251 */       if (this._acceptBoolean) {
/* 252 */         return Boolean.TRUE;
/*     */       }
/*     */       break;
/*     */     case 10: 
/* 256 */       if (this._acceptBoolean) {
/* 257 */         return Boolean.FALSE;
/*     */       }
/*     */       break;
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeFromObjectId(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 270 */     Object id = this._objectIdReader.readObjectReference(p, ctxt);
/* 271 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*     */     
/* 273 */     Object pojo = roid.resolve();
/* 274 */     if (pojo == null) {
/* 275 */       throw new UnresolvedForwardReference(p, "Could not resolve Object Id [" + id + "] -- unresolved forward-reference?", p.getCurrentLocation(), roid);
/*     */     }
/*     */     
/* 278 */     return pojo;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\AbstractDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */