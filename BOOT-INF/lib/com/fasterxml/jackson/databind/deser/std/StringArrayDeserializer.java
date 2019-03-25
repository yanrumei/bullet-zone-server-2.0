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
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class StringArrayDeserializer
/*     */   extends StdDeserializer<String[]>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  25 */   public static final StringArrayDeserializer instance = new StringArrayDeserializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<String> _elementDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringArrayDeserializer()
/*     */   {
/*  42 */     this(null, null);
/*     */   }
/*     */   
/*     */   protected StringArrayDeserializer(JsonDeserializer<?> deser, Boolean unwrapSingle)
/*     */   {
/*  47 */     super(String[].class);
/*  48 */     this._elementDeserializer = deser;
/*  49 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  59 */     JsonDeserializer<?> deser = this._elementDeserializer;
/*     */     
/*  61 */     deser = findConvertingContentDeserializer(ctxt, property, deser);
/*  62 */     JavaType type = ctxt.constructType(String.class);
/*  63 */     if (deser == null) {
/*  64 */       deser = ctxt.findContextualValueDeserializer(type, property);
/*     */     } else {
/*  66 */       deser = ctxt.handleSecondaryContextualization(deser, property, type);
/*     */     }
/*     */     
/*  69 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, String[].class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/*     */ 
/*  72 */     if ((deser != null) && (isDefaultDeserializer(deser))) {
/*  73 */       deser = null;
/*     */     }
/*  75 */     if ((this._elementDeserializer == deser) && (this._unwrapSingle == unwrapSingle)) {
/*  76 */       return this;
/*     */     }
/*  78 */     return new StringArrayDeserializer(deser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  85 */     if (!p.isExpectedStartArrayToken()) {
/*  86 */       return handleNonArray(p, ctxt);
/*     */     }
/*  88 */     if (this._elementDeserializer != null) {
/*  89 */       return _deserializeCustom(p, ctxt);
/*     */     }
/*     */     
/*  92 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/*  93 */     Object[] chunk = buffer.resetAndStart();
/*     */     
/*  95 */     int ix = 0;
/*     */     try
/*     */     {
/*     */       for (;;) {
/*  99 */         String value = p.nextTextValue();
/* 100 */         if (value == null) {
/* 101 */           JsonToken t = p.getCurrentToken();
/* 102 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/* 105 */           if (t != JsonToken.VALUE_NULL) {
/* 106 */             value = _parseString(p, ctxt);
/*     */           }
/*     */         }
/* 109 */         if (ix >= chunk.length) {
/* 110 */           chunk = buffer.appendCompletedChunk(chunk);
/* 111 */           ix = 0;
/*     */         }
/* 113 */         chunk[(ix++)] = value;
/*     */       }
/*     */     } catch (Exception e) {
/* 116 */       throw JsonMappingException.wrapWithPath(e, chunk, buffer.bufferedSize() + ix);
/*     */     }
/* 118 */     String[] result = (String[])buffer.completeAndClearBuffer(chunk, ix, String.class);
/* 119 */     ctxt.returnObjectBuffer(buffer);
/* 120 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final String[] _deserializeCustom(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 128 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 129 */     Object[] chunk = buffer.resetAndStart();
/* 130 */     JsonDeserializer<String> deser = this._elementDeserializer;
/*     */     
/* 132 */     int ix = 0;
/*     */     
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */         String value;
/*     */         
/*     */         String value;
/*     */         
/* 142 */         if (p.nextTextValue() == null) {
/* 143 */           JsonToken t = p.getCurrentToken();
/* 144 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/*     */           
/* 148 */           value = t == JsonToken.VALUE_NULL ? (String)deser.getNullValue(ctxt) : (String)deser.deserialize(p, ctxt);
/*     */         } else {
/* 150 */           value = (String)deser.deserialize(p, ctxt);
/*     */         }
/* 152 */         if (ix >= chunk.length) {
/* 153 */           chunk = buffer.appendCompletedChunk(chunk);
/* 154 */           ix = 0;
/*     */         }
/* 156 */         chunk[(ix++)] = value;
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 160 */       throw JsonMappingException.wrapWithPath(e, String.class, ix);
/*     */     }
/* 162 */     String[] result = (String[])buffer.completeAndClearBuffer(chunk, ix, String.class);
/* 163 */     ctxt.returnObjectBuffer(buffer);
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException
/*     */   {
/* 169 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */   
/*     */   private final String[] handleNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 175 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/* 178 */     if (canWrap)
/* 179 */       return new String[] { p.hasToken(JsonToken.VALUE_NULL) ? null : _parseString(p, ctxt) };
/* 180 */     if ((p.hasToken(JsonToken.VALUE_STRING)) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */     {
/* 182 */       String str = p.getText();
/* 183 */       if (str.length() == 0) {
/* 184 */         return null;
/*     */       }
/*     */     }
/* 187 */     return (String[])ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StringArrayDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */