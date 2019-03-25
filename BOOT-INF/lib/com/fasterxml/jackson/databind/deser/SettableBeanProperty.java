/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.deser.impl.FailingDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ConcreteBeanPropertyBase;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ViewMatcher;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SettableBeanProperty
/*     */   extends ConcreteBeanPropertyBase
/*     */   implements Serializable
/*     */ {
/*  34 */   protected static final JsonDeserializer<Object> MISSING_VALUE_DESERIALIZER = new FailingDeserializer("No _valueDeserializer assigned");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _propName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _type;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _wrapperName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final transient Annotations _contextAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _managedReferenceName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ViewMatcher _viewMatcher;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   protected int _propertyIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations)
/*     */   {
/* 126 */     this(propDef.getFullName(), type, propDef.getWrapperName(), typeDeser, contextAnnotations, propDef.getMetadata());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected SettableBeanProperty(String propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, boolean isRequired)
/*     */   {
/* 135 */     this(new PropertyName(propName), type, wrapper, typeDeser, contextAnnotations, PropertyMetadata.construct(Boolean.valueOf(isRequired), null, null, null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyName wrapper, TypeDeserializer typeDeser, Annotations contextAnnotations, PropertyMetadata metadata)
/*     */   {
/* 143 */     super(metadata);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */     if (propName == null) {
/* 150 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 152 */       this._propName = propName.internSimpleName();
/*     */     }
/* 154 */     this._type = type;
/* 155 */     this._wrapperName = wrapper;
/* 156 */     this._contextAnnotations = contextAnnotations;
/* 157 */     this._viewMatcher = null;
/*     */     
/*     */ 
/* 160 */     if (typeDeser != null) {
/* 161 */       typeDeser = typeDeser.forProperty(this);
/*     */     }
/* 163 */     this._valueTypeDeserializer = typeDeser;
/* 164 */     this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(PropertyName propName, JavaType type, PropertyMetadata metadata, JsonDeserializer<Object> valueDeser)
/*     */   {
/* 175 */     super(metadata);
/*     */     
/* 177 */     if (propName == null) {
/* 178 */       this._propName = PropertyName.NO_NAME;
/*     */     } else {
/* 180 */       this._propName = propName.internSimpleName();
/*     */     }
/* 182 */     this._type = type;
/* 183 */     this._wrapperName = null;
/* 184 */     this._contextAnnotations = null;
/* 185 */     this._viewMatcher = null;
/* 186 */     this._valueTypeDeserializer = null;
/* 187 */     this._valueDeserializer = valueDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src)
/*     */   {
/* 195 */     super(src);
/* 196 */     this._propName = src._propName;
/* 197 */     this._type = src._type;
/* 198 */     this._wrapperName = src._wrapperName;
/* 199 */     this._contextAnnotations = src._contextAnnotations;
/* 200 */     this._valueDeserializer = src._valueDeserializer;
/* 201 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 202 */     this._managedReferenceName = src._managedReferenceName;
/* 203 */     this._propertyIndex = src._propertyIndex;
/* 204 */     this._viewMatcher = src._viewMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src, JsonDeserializer<?> deser)
/*     */   {
/* 213 */     super(src);
/* 214 */     this._propName = src._propName;
/* 215 */     this._type = src._type;
/* 216 */     this._wrapperName = src._wrapperName;
/* 217 */     this._contextAnnotations = src._contextAnnotations;
/* 218 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 219 */     this._managedReferenceName = src._managedReferenceName;
/* 220 */     this._propertyIndex = src._propertyIndex;
/*     */     
/* 222 */     if (deser == null) {
/* 223 */       this._valueDeserializer = MISSING_VALUE_DESERIALIZER;
/*     */     } else {
/* 225 */       this._valueDeserializer = deser;
/*     */     }
/* 227 */     this._viewMatcher = src._viewMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty(SettableBeanProperty src, PropertyName newName)
/*     */   {
/* 235 */     super(src);
/* 236 */     this._propName = newName;
/* 237 */     this._type = src._type;
/* 238 */     this._wrapperName = src._wrapperName;
/* 239 */     this._contextAnnotations = src._contextAnnotations;
/* 240 */     this._valueDeserializer = src._valueDeserializer;
/* 241 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 242 */     this._managedReferenceName = src._managedReferenceName;
/* 243 */     this._propertyIndex = src._propertyIndex;
/* 244 */     this._viewMatcher = src._viewMatcher;
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
/*     */   public abstract SettableBeanProperty withValueDeserializer(JsonDeserializer<?> paramJsonDeserializer);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract SettableBeanProperty withName(PropertyName paramPropertyName);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettableBeanProperty withSimpleName(String simpleName)
/*     */   {
/* 275 */     PropertyName n = this._propName == null ? new PropertyName(simpleName) : this._propName.withSimpleName(simpleName);
/*     */     
/* 277 */     return n == this._propName ? this : withName(n);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public SettableBeanProperty withName(String simpleName) {
/* 282 */     return withName(new PropertyName(simpleName));
/*     */   }
/*     */   
/*     */   public void setManagedReferenceName(String n) {
/* 286 */     this._managedReferenceName = n;
/*     */   }
/*     */   
/*     */   public void setObjectIdInfo(ObjectIdInfo objectIdInfo) {
/* 290 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */   
/*     */   public void setViews(Class<?>[] views) {
/* 294 */     if (views == null) {
/* 295 */       this._viewMatcher = null;
/*     */     } else {
/* 297 */       this._viewMatcher = ViewMatcher.construct(views);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void assignIndex(int index)
/*     */   {
/* 305 */     if (this._propertyIndex != -1) {
/* 306 */       throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + index);
/*     */     }
/* 308 */     this._propertyIndex = index;
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
/*     */   public void fixAccess(DeserializationConfig config) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 330 */     return this._propName.getSimpleName();
/*     */   }
/*     */   
/*     */   public PropertyName getFullName()
/*     */   {
/* 335 */     return this._propName;
/*     */   }
/*     */   
/*     */   public JavaType getType() {
/* 339 */     return this._type;
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 343 */     return this._wrapperName;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract AnnotatedMember getMember();
/*     */   
/*     */ 
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */   
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */   {
/* 354 */     return this._contextAnnotations.get(acls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 362 */     if (isRequired()) {
/* 363 */       objectVisitor.property(this);
/*     */     } else {
/* 365 */       objectVisitor.optionalProperty(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> getDeclaringClass()
/*     */   {
/* 376 */     return getMember().getDeclaringClass();
/*     */   }
/*     */   
/* 379 */   public String getManagedReferenceName() { return this._managedReferenceName; }
/*     */   
/* 381 */   public ObjectIdInfo getObjectIdInfo() { return this._objectIdInfo; }
/*     */   
/*     */   public boolean hasValueDeserializer() {
/* 384 */     return (this._valueDeserializer != null) && (this._valueDeserializer != MISSING_VALUE_DESERIALIZER);
/*     */   }
/*     */   
/* 387 */   public boolean hasValueTypeDeserializer() { return this._valueTypeDeserializer != null; }
/*     */   
/*     */   public JsonDeserializer<Object> getValueDeserializer() {
/* 390 */     JsonDeserializer<Object> deser = this._valueDeserializer;
/* 391 */     if (deser == MISSING_VALUE_DESERIALIZER) {
/* 392 */       return null;
/*     */     }
/* 394 */     return deser;
/*     */   }
/*     */   
/* 397 */   public TypeDeserializer getValueTypeDeserializer() { return this._valueTypeDeserializer; }
/*     */   
/*     */   public boolean visibleInView(Class<?> activeView) {
/* 400 */     return (this._viewMatcher == null) || (this._viewMatcher.isVisibleForView(activeView));
/*     */   }
/*     */   
/* 403 */   public boolean hasViews() { return this._viewMatcher != null; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPropertyIndex()
/*     */   {
/* 412 */     return this._propertyIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCreatorIndex()
/*     */   {
/* 422 */     throw new IllegalStateException(String.format("Internal error: no creator index for property '%s' (of type %s)", new Object[] { getName(), getClass().getName() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getInjectableValueId()
/*     */   {
/* 431 */     return null;
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
/*     */   public abstract void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
/*     */     throws IOException;
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
/*     */   public abstract Object deserializeSetAndReturn(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
/*     */     throws IOException;
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
/*     */   public abstract void set(Object paramObject1, Object paramObject2)
/*     */     throws IOException;
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
/*     */   public abstract Object setAndReturn(Object paramObject1, Object paramObject2)
/*     */     throws IOException;
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
/*     */   public final Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 496 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 498 */     if (t == JsonToken.VALUE_NULL) {
/* 499 */       return this._valueDeserializer.getNullValue(ctxt);
/*     */     }
/* 501 */     if (this._valueTypeDeserializer != null) {
/* 502 */       return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 504 */     return this._valueDeserializer.deserialize(p, ctxt);
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
/*     */   protected void _throwAsIOE(JsonParser p, Exception e, Object value)
/*     */     throws IOException
/*     */   {
/* 519 */     if ((e instanceof IllegalArgumentException)) {
/* 520 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 521 */       StringBuilder msg = new StringBuilder("Problem deserializing property '").append(getName());
/* 522 */       msg.append("' (expected type: ").append(getType());
/* 523 */       msg.append("; actual type: ").append(actType).append(")");
/* 524 */       String origMsg = e.getMessage();
/* 525 */       if (origMsg != null) {
/* 526 */         msg.append(", problem: ").append(origMsg);
/*     */       } else {
/* 528 */         msg.append(" (no error message provided)");
/*     */       }
/* 530 */       throw JsonMappingException.from(p, msg.toString(), e);
/*     */     }
/* 532 */     _throwAsIOE(p, e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected IOException _throwAsIOE(JsonParser p, Exception e)
/*     */     throws IOException
/*     */   {
/* 540 */     if ((e instanceof IOException)) {
/* 541 */       throw ((IOException)e);
/*     */     }
/* 543 */     if ((e instanceof RuntimeException)) {
/* 544 */       throw ((RuntimeException)e);
/*     */     }
/*     */     
/* 547 */     Throwable th = e;
/* 548 */     while (th.getCause() != null) {
/* 549 */       th = th.getCause();
/*     */     }
/* 551 */     throw JsonMappingException.from(p, th.getMessage(), th);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected IOException _throwAsIOE(Exception e) throws IOException {
/* 556 */     return _throwAsIOE((JsonParser)null, e);
/*     */   }
/*     */   
/*     */   protected void _throwAsIOE(Exception e, Object value)
/*     */     throws IOException
/*     */   {
/* 562 */     _throwAsIOE((JsonParser)null, e, value);
/*     */   }
/*     */   
/* 565 */   public String toString() { return "[property '" + getName() + "']"; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\SettableBeanProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */