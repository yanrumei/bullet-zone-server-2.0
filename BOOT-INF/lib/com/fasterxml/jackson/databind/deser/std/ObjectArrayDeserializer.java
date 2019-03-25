/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
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
/*     */ @JacksonStdImpl
/*     */ public class ObjectArrayDeserializer
/*     */   extends ContainerDeserializerBase<Object[]>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ArrayType _arrayType;
/*     */   protected final boolean _untyped;
/*     */   protected final Class<?> _elementClass;
/*     */   protected JsonDeserializer<Object> _elementDeserializer;
/*     */   protected final TypeDeserializer _elementTypeDeserializer;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public ObjectArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser)
/*     */   {
/*  75 */     super(arrayType);
/*  76 */     this._arrayType = arrayType;
/*  77 */     this._elementClass = arrayType.getContentType().getRawClass();
/*  78 */     this._untyped = (this._elementClass == Object.class);
/*  79 */     this._elementDeserializer = elemDeser;
/*  80 */     this._elementTypeDeserializer = elemTypeDeser;
/*  81 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ObjectArrayDeserializer(ObjectArrayDeserializer base, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser, Boolean unwrapSingle)
/*     */   {
/*  88 */     super(base._arrayType);
/*  89 */     this._arrayType = base._arrayType;
/*  90 */     this._elementClass = base._elementClass;
/*  91 */     this._untyped = base._untyped;
/*     */     
/*  93 */     this._elementDeserializer = elemDeser;
/*  94 */     this._elementTypeDeserializer = elemTypeDeser;
/*  95 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectArrayDeserializer withDeserializer(TypeDeserializer elemTypeDeser, JsonDeserializer<?> elemDeser)
/*     */   {
/* 104 */     return withResolved(elemTypeDeser, elemDeser, this._unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectArrayDeserializer withResolved(TypeDeserializer elemTypeDeser, JsonDeserializer<?> elemDeser, Boolean unwrapSingle)
/*     */   {
/* 114 */     if ((unwrapSingle == this._unwrapSingle) && (elemDeser == this._elementDeserializer) && (elemTypeDeser == this._elementTypeDeserializer))
/*     */     {
/*     */ 
/* 117 */       return this;
/*     */     }
/* 119 */     return new ObjectArrayDeserializer(this, elemDeser, elemTypeDeser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 127 */     JsonDeserializer<?> deser = this._elementDeserializer;
/* 128 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, this._arrayType.getRawClass(), JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/*     */ 
/* 131 */     deser = findConvertingContentDeserializer(ctxt, property, deser);
/* 132 */     JavaType vt = this._arrayType.getContentType();
/* 133 */     if (deser == null) {
/* 134 */       deser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 136 */       deser = ctxt.handleSecondaryContextualization(deser, property, vt);
/*     */     }
/* 138 */     TypeDeserializer elemTypeDeser = this._elementTypeDeserializer;
/* 139 */     if (elemTypeDeser != null) {
/* 140 */       elemTypeDeser = elemTypeDeser.forProperty(property);
/*     */     }
/* 142 */     return withResolved(elemTypeDeser, deser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 148 */     return (this._elementDeserializer == null) && (this._elementTypeDeserializer == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 159 */     return this._arrayType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 164 */     return this._elementDeserializer;
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
/*     */   public Object[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 178 */     if (!p.isExpectedStartArrayToken()) {
/* 179 */       return handleNonArray(p, ctxt);
/*     */     }
/*     */     
/* 182 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 183 */     Object[] chunk = buffer.resetAndStart();
/* 184 */     int ix = 0;
/*     */     
/* 186 */     TypeDeserializer typeDeser = this._elementTypeDeserializer;
/*     */     try {
/*     */       JsonToken t;
/* 189 */       while ((t = p.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/*     */         Object value;
/*     */         Object value;
/* 193 */         if (t == JsonToken.VALUE_NULL) {
/* 194 */           value = this._elementDeserializer.getNullValue(ctxt); } else { Object value;
/* 195 */           if (typeDeser == null) {
/* 196 */             value = this._elementDeserializer.deserialize(p, ctxt);
/*     */           } else
/* 198 */             value = this._elementDeserializer.deserializeWithType(p, ctxt, typeDeser);
/*     */         }
/* 200 */         if (ix >= chunk.length) {
/* 201 */           chunk = buffer.appendCompletedChunk(chunk);
/* 202 */           ix = 0;
/*     */         }
/* 204 */         chunk[(ix++)] = value;
/*     */       }
/*     */     } catch (Exception e) {
/* 207 */       throw JsonMappingException.wrapWithPath(e, chunk, buffer.bufferedSize() + ix);
/*     */     }
/*     */     
/*     */     Object[] result;
/*     */     Object[] result;
/* 212 */     if (this._untyped) {
/* 213 */       result = buffer.completeAndClearBuffer(chunk, ix);
/*     */     } else {
/* 215 */       result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
/*     */     }
/* 217 */     ctxt.returnObjectBuffer(buffer);
/* 218 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 229 */     return (Object[])typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Byte[] deserializeFromBase64(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 242 */     byte[] b = p.getBinaryValue(ctxt.getBase64Variant());
/*     */     
/* 244 */     Byte[] result = new Byte[b.length];
/* 245 */     int i = 0; for (int len = b.length; i < len; i++) {
/* 246 */       result[i] = Byte.valueOf(b[i]);
/*     */     }
/* 248 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object[] handleNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 255 */     if ((p.hasToken(JsonToken.VALUE_STRING)) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */     {
/* 257 */       String str = p.getText();
/* 258 */       if (str.length() == 0) {
/* 259 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 264 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/* 267 */     if (!canWrap)
/*     */     {
/* 269 */       JsonToken t = p.getCurrentToken();
/* 270 */       if ((t == JsonToken.VALUE_STRING) && (this._elementClass == Byte.class))
/*     */       {
/*     */ 
/* 273 */         return deserializeFromBase64(p, ctxt);
/*     */       }
/* 275 */       return (Object[])ctxt.handleUnexpectedToken(this._arrayType.getRawClass(), p);
/*     */     }
/* 277 */     JsonToken t = p.getCurrentToken();
/*     */     Object value;
/*     */     Object value;
/* 280 */     if (t == JsonToken.VALUE_NULL) {
/* 281 */       value = this._elementDeserializer.getNullValue(ctxt); } else { Object value;
/* 282 */       if (this._elementTypeDeserializer == null) {
/* 283 */         value = this._elementDeserializer.deserialize(p, ctxt);
/*     */       } else {
/* 285 */         value = this._elementDeserializer.deserializeWithType(p, ctxt, this._elementTypeDeserializer);
/*     */       }
/*     */     }
/*     */     Object[] result;
/*     */     Object[] result;
/* 290 */     if (this._untyped) {
/* 291 */       result = new Object[1];
/*     */     } else {
/* 293 */       result = (Object[])Array.newInstance(this._elementClass, 1);
/*     */     }
/* 295 */     result[0] = value;
/* 296 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\ObjectArrayDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */