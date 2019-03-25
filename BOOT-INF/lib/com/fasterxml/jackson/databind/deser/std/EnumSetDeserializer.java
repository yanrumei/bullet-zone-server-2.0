/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
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
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
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
/*     */ public class EnumSetDeserializer
/*     */   extends StdDeserializer<EnumSet<?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _enumType;
/*     */   protected final Class<Enum> _enumClass;
/*     */   protected JsonDeserializer<Enum<?>> _enumDeserializer;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public EnumSetDeserializer(JavaType enumType, JsonDeserializer<?> deser)
/*     */   {
/*  49 */     super(EnumSet.class);
/*  50 */     this._enumType = enumType;
/*  51 */     this._enumClass = enumType.getRawClass();
/*     */     
/*  53 */     if (!this._enumClass.isEnum()) {
/*  54 */       throw new IllegalArgumentException("Type " + enumType + " not Java Enum type");
/*     */     }
/*  56 */     this._enumDeserializer = deser;
/*  57 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EnumSetDeserializer(EnumSetDeserializer base, JsonDeserializer<?> deser, Boolean unwrapSingle)
/*     */   {
/*  66 */     super(EnumSet.class);
/*  67 */     this._enumType = base._enumType;
/*  68 */     this._enumClass = base._enumClass;
/*  69 */     this._enumDeserializer = deser;
/*  70 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withDeserializer(JsonDeserializer<?> deser) {
/*  74 */     if (this._enumDeserializer == deser) {
/*  75 */       return this;
/*     */     }
/*  77 */     return new EnumSetDeserializer(this, deser, this._unwrapSingle);
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withResolved(JsonDeserializer<?> deser, Boolean unwrapSingle) {
/*  81 */     if ((this._unwrapSingle == unwrapSingle) && (this._enumDeserializer == deser)) {
/*  82 */       return this;
/*     */     }
/*  84 */     return new EnumSetDeserializer(this, deser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/*  94 */     if (this._enumType.getValueHandler() != null) {
/*  95 */       return false;
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 104 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, EnumSet.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/* 106 */     JsonDeserializer<?> deser = this._enumDeserializer;
/* 107 */     if (deser == null) {
/* 108 */       deser = ctxt.findContextualValueDeserializer(this._enumType, property);
/*     */     } else {
/* 110 */       deser = ctxt.handleSecondaryContextualization(deser, property, this._enumType);
/*     */     }
/* 112 */     return withResolved(deser, unwrapSingle);
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
/*     */   public EnumSet<?> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 126 */     if (!p.isExpectedStartArrayToken()) {
/* 127 */       return handleNonArray(p, ctxt);
/*     */     }
/* 129 */     EnumSet result = constructSet();
/*     */     try
/*     */     {
/*     */       JsonToken t;
/* 133 */       while ((t = p.nextToken()) != JsonToken.END_ARRAY)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */         if (t == JsonToken.VALUE_NULL) {
/* 140 */           return (EnumSet)ctxt.handleUnexpectedToken(this._enumClass, p);
/*     */         }
/* 142 */         Enum<?> value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/*     */         
/*     */ 
/*     */ 
/* 146 */         if (value != null) {
/* 147 */           result.add(value);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 151 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     }
/* 153 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 161 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private EnumSet constructSet()
/*     */   {
/* 168 */     return EnumSet.noneOf(this._enumClass);
/*     */   }
/*     */   
/*     */ 
/*     */   protected EnumSet<?> handleNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 175 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/*     */ 
/* 179 */     if (!canWrap) {
/* 180 */       return (EnumSet)ctxt.handleUnexpectedToken(EnumSet.class, p);
/*     */     }
/*     */     
/* 183 */     EnumSet result = constructSet();
/*     */     
/* 185 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 186 */       return (EnumSet)ctxt.handleUnexpectedToken(this._enumClass, p);
/*     */     }
/*     */     try {
/* 189 */       Enum<?> value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/* 190 */       if (value != null) {
/* 191 */         result.add(value);
/*     */       }
/*     */     } catch (Exception e) {
/* 194 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     }
/* 196 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\EnumSetDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */