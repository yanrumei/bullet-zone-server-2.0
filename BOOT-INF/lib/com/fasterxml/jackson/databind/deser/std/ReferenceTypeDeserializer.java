/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
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
/*     */ public abstract class ReferenceTypeDeserializer<T>
/*     */   extends StdDeserializer<T>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _fullType;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final JsonDeserializer<?> _valueDeserializer;
/*     */   
/*     */   public ReferenceTypeDeserializer(JavaType fullType, TypeDeserializer typeDeser, JsonDeserializer<?> deser)
/*     */   {
/*  46 */     super(fullType);
/*  47 */     this._fullType = fullType;
/*  48 */     this._valueDeserializer = deser;
/*  49 */     this._valueTypeDeserializer = typeDeser;
/*     */   }
/*     */   
/*     */ 
/*     */   public ReferenceTypeDeserializer(JavaType fullType, ValueInstantiator inst, TypeDeserializer typeDeser, JsonDeserializer<?> deser)
/*     */   {
/*  55 */     this(fullType, typeDeser, deser);
/*     */   }
/*     */   
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  61 */     JsonDeserializer<?> deser = this._valueDeserializer;
/*  62 */     if (deser == null) {
/*  63 */       deser = ctxt.findContextualValueDeserializer(this._fullType.getReferencedType(), property);
/*     */     } else {
/*  65 */       deser = ctxt.handleSecondaryContextualization(deser, property, this._fullType.getReferencedType());
/*     */     }
/*  67 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*  68 */     if (typeDeser != null) {
/*  69 */       typeDeser = typeDeser.forProperty(property);
/*     */     }
/*  71 */     if ((deser == this._valueDeserializer) && (typeDeser == this._valueTypeDeserializer)) {
/*  72 */       return this;
/*     */     }
/*  74 */     return withResolved(typeDeser, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ReferenceTypeDeserializer<T> withResolved(TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T getNullValue(DeserializationContext paramDeserializationContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T referenceValue(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getValueType()
/*     */   {
/*  97 */     return this._fullType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 107 */     Object contents = this._valueTypeDeserializer == null ? this._valueDeserializer.deserialize(p, ctxt) : this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     
/*     */ 
/* 110 */     return (T)referenceValue(contents);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 117 */     JsonToken t = p.getCurrentToken();
/* 118 */     if (t == JsonToken.VALUE_NULL) {
/* 119 */       return getNullValue(ctxt);
/*     */     }
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
/* 133 */     if (this._valueTypeDeserializer == null) {
/* 134 */       return deserialize(p, ctxt);
/*     */     }
/* 136 */     return referenceValue(this._valueTypeDeserializer.deserializeTypedFromAny(p, ctxt));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\ReferenceTypeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */