/*      */ package com.fasterxml.jackson.databind.deser.std;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.Date;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class StdDeserializer<T>
/*      */   extends JsonDeserializer<T>
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   34 */   protected static final int F_MASK_INT_COERCIONS = DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.getMask() | DeserializationFeature.USE_LONG_FOR_INTS.getMask();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _valueClass;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected StdDeserializer(Class<?> vc)
/*      */   {
/*   47 */     this._valueClass = vc;
/*      */   }
/*      */   
/*      */   protected StdDeserializer(JavaType valueType) {
/*   51 */     this._valueClass = (valueType == null ? null : valueType.getRawClass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected StdDeserializer(StdDeserializer<?> src)
/*      */   {
/*   61 */     this._valueClass = src._valueClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> handledType()
/*      */   {
/*   71 */     return this._valueClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public final Class<?> getValueClass()
/*      */   {
/*   83 */     return this._valueClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JavaType getValueType()
/*      */   {
/*   90 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer)
/*      */   {
/*   99 */     return ClassUtil.isJacksonStdImpl(deserializer);
/*      */   }
/*      */   
/*      */   protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
/*  103 */     return ClassUtil.isJacksonStdImpl(keyDeser);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */     throws IOException
/*      */   {
/*  120 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _parseBooleanPrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  133 */     JsonToken t = p.getCurrentToken();
/*  134 */     if (t == JsonToken.VALUE_TRUE) return true;
/*  135 */     if (t == JsonToken.VALUE_FALSE) return false;
/*  136 */     if (t == JsonToken.VALUE_NULL) { return false;
/*      */     }
/*      */     
/*  139 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  140 */       return _parseBooleanFromInt(p, ctxt);
/*      */     }
/*      */     
/*  143 */     if (t == JsonToken.VALUE_STRING) {
/*  144 */       String text = p.getText().trim();
/*      */       
/*  146 */       if (("true".equals(text)) || ("True".equals(text))) {
/*  147 */         return true;
/*      */       }
/*  149 */       if (("false".equals(text)) || ("False".equals(text)) || (text.length() == 0)) {
/*  150 */         return false;
/*      */       }
/*  152 */       if (_hasTextualNull(text)) {
/*  153 */         return false;
/*      */       }
/*  155 */       Boolean b = (Boolean)ctxt.handleWeirdStringValue(this._valueClass, text, "only \"true\" or \"false\" recognized", new Object[0]);
/*      */       
/*  157 */       return b == null ? false : b.booleanValue();
/*      */     }
/*      */     
/*  160 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  161 */       p.nextToken();
/*  162 */       boolean parsed = _parseBooleanPrimitive(p, ctxt);
/*  163 */       t = p.nextToken();
/*  164 */       if (t != JsonToken.END_ARRAY) {
/*  165 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  167 */       return parsed;
/*      */     }
/*      */     
/*  170 */     return ((Boolean)ctxt.handleUnexpectedToken(this._valueClass, p)).booleanValue();
/*      */   }
/*      */   
/*      */   protected final Boolean _parseBoolean(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  176 */     JsonToken t = p.getCurrentToken();
/*  177 */     if (t == JsonToken.VALUE_TRUE) {
/*  178 */       return Boolean.TRUE;
/*      */     }
/*  180 */     if (t == JsonToken.VALUE_FALSE) {
/*  181 */       return Boolean.FALSE;
/*      */     }
/*      */     
/*  184 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  185 */       return Boolean.valueOf(_parseBooleanFromInt(p, ctxt));
/*      */     }
/*  187 */     if (t == JsonToken.VALUE_NULL) {
/*  188 */       return (Boolean)getNullValue(ctxt);
/*      */     }
/*      */     
/*  191 */     if (t == JsonToken.VALUE_STRING) {
/*  192 */       String text = p.getText().trim();
/*      */       
/*  194 */       if (("true".equals(text)) || ("True".equals(text))) {
/*  195 */         return Boolean.TRUE;
/*      */       }
/*  197 */       if (("false".equals(text)) || ("False".equals(text))) {
/*  198 */         return Boolean.FALSE;
/*      */       }
/*  200 */       if (text.length() == 0) {
/*  201 */         return (Boolean)getEmptyValue(ctxt);
/*      */       }
/*  203 */       if (_hasTextualNull(text)) {
/*  204 */         return (Boolean)getNullValue(ctxt);
/*      */       }
/*  206 */       return (Boolean)ctxt.handleWeirdStringValue(this._valueClass, text, "only \"true\" or \"false\" recognized", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  210 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  211 */       p.nextToken();
/*  212 */       Boolean parsed = _parseBoolean(p, ctxt);
/*  213 */       t = p.nextToken();
/*  214 */       if (t != JsonToken.END_ARRAY) {
/*  215 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  217 */       return parsed;
/*      */     }
/*      */     
/*  220 */     return (Boolean)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _parseBooleanFromInt(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  233 */     return !"0".equals(p.getText());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected boolean _parseBooleanFromOther(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  240 */     return _parseBooleanFromInt(p, ctxt);
/*      */   }
/*      */   
/*      */   protected Byte _parseByte(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  246 */     JsonToken t = p.getCurrentToken();
/*  247 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  248 */       return Byte.valueOf(p.getByteValue());
/*      */     }
/*  250 */     if (t == JsonToken.VALUE_STRING) {
/*  251 */       String text = p.getText().trim();
/*  252 */       if (_hasTextualNull(text)) {
/*  253 */         return (Byte)getNullValue(ctxt);
/*      */       }
/*      */       int value;
/*      */       try {
/*  257 */         int len = text.length();
/*  258 */         if (len == 0) {
/*  259 */           return (Byte)getEmptyValue(ctxt);
/*      */         }
/*  261 */         value = NumberInput.parseInt(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  263 */         return (Byte)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Byte value", new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  268 */       if ((value < -128) || (value > 255)) {
/*  269 */         return (Byte)ctxt.handleWeirdStringValue(this._valueClass, text, "overflow, value can not be represented as 8-bit value", new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  273 */       return Byte.valueOf((byte)value);
/*      */     }
/*  275 */     if (t == JsonToken.VALUE_NUMBER_FLOAT) {
/*  276 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  277 */         _failDoubleToIntCoercion(p, ctxt, "Byte");
/*      */       }
/*  279 */       return Byte.valueOf(p.getByteValue());
/*      */     }
/*  281 */     if (t == JsonToken.VALUE_NULL) {
/*  282 */       return (Byte)getNullValue(ctxt);
/*      */     }
/*      */     
/*  285 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  286 */       p.nextToken();
/*  287 */       Byte parsed = _parseByte(p, ctxt);
/*  288 */       t = p.nextToken();
/*  289 */       if (t != JsonToken.END_ARRAY) {
/*  290 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  292 */       return parsed;
/*      */     }
/*  294 */     return (Byte)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */   protected Short _parseShort(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  300 */     JsonToken t = p.getCurrentToken();
/*  301 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  302 */       return Short.valueOf(p.getShortValue());
/*      */     }
/*  304 */     if (t == JsonToken.VALUE_STRING) {
/*  305 */       String text = p.getText().trim();
/*      */       int value;
/*      */       try {
/*  308 */         int len = text.length();
/*  309 */         if (len == 0) {
/*  310 */           return (Short)getEmptyValue(ctxt);
/*      */         }
/*  312 */         if (_hasTextualNull(text)) {
/*  313 */           return (Short)getNullValue(ctxt);
/*      */         }
/*  315 */         value = NumberInput.parseInt(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  317 */         return (Short)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Short value", new Object[0]);
/*      */       }
/*      */       
/*      */ 
/*  321 */       if ((value < 32768) || (value > 32767)) {
/*  322 */         return (Short)ctxt.handleWeirdStringValue(this._valueClass, text, "overflow, value can not be represented as 16-bit value", new Object[0]);
/*      */       }
/*      */       
/*  325 */       return Short.valueOf((short)value);
/*      */     }
/*  327 */     if (t == JsonToken.VALUE_NUMBER_FLOAT) {
/*  328 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  329 */         _failDoubleToIntCoercion(p, ctxt, "Short");
/*      */       }
/*  331 */       return Short.valueOf(p.getShortValue());
/*      */     }
/*  333 */     if (t == JsonToken.VALUE_NULL) {
/*  334 */       return (Short)getNullValue(ctxt);
/*      */     }
/*      */     
/*  337 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  338 */       p.nextToken();
/*  339 */       Short parsed = _parseShort(p, ctxt);
/*  340 */       t = p.nextToken();
/*  341 */       if (t != JsonToken.END_ARRAY) {
/*  342 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  344 */       return parsed;
/*      */     }
/*  346 */     return (Short)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */   protected final short _parseShortPrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  352 */     int value = _parseIntPrimitive(p, ctxt);
/*      */     
/*  354 */     if ((value < 32768) || (value > 32767)) {
/*  355 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, String.valueOf(value), "overflow, value can not be represented as 16-bit value", new Object[0]);
/*      */       
/*  357 */       return v == null ? 0 : v.shortValue();
/*      */     }
/*  359 */     return (short)value;
/*      */   }
/*      */   
/*      */   protected final int _parseIntPrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  365 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  366 */       return p.getIntValue();
/*      */     }
/*  368 */     JsonToken t = p.getCurrentToken();
/*  369 */     if (t == JsonToken.VALUE_STRING) {
/*  370 */       String text = p.getText().trim();
/*  371 */       if (_hasTextualNull(text)) {
/*  372 */         return 0;
/*      */       }
/*      */       try {
/*  375 */         int len = text.length();
/*  376 */         if (len > 9) {
/*  377 */           long l = Long.parseLong(text);
/*  378 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/*  379 */             Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "Overflow: numeric value (%s) out of range of int (%d -%d)", new Object[] { text, Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE) });
/*      */             
/*      */ 
/*  382 */             return v == null ? 0 : v.intValue();
/*      */           }
/*  384 */           return (int)l;
/*      */         }
/*  386 */         if (len == 0) {
/*  387 */           return 0;
/*      */         }
/*  389 */         return NumberInput.parseInt(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  391 */         Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid int value", new Object[0]);
/*      */         
/*  393 */         return v == null ? 0 : v.intValue();
/*      */       }
/*      */     }
/*  396 */     if (t == JsonToken.VALUE_NUMBER_FLOAT) {
/*  397 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  398 */         _failDoubleToIntCoercion(p, ctxt, "int");
/*      */       }
/*  400 */       return p.getValueAsInt();
/*      */     }
/*  402 */     if (t == JsonToken.VALUE_NULL) {
/*  403 */       return 0;
/*      */     }
/*  405 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  406 */       p.nextToken();
/*  407 */       int parsed = _parseIntPrimitive(p, ctxt);
/*  408 */       t = p.nextToken();
/*  409 */       if (t != JsonToken.END_ARRAY) {
/*  410 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  412 */       return parsed;
/*      */     }
/*      */     
/*  415 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).intValue();
/*      */   }
/*      */   
/*      */   protected final Integer _parseInteger(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  421 */     switch (p.getCurrentTokenId())
/*      */     {
/*      */     case 7: 
/*  424 */       return Integer.valueOf(p.getIntValue());
/*      */     case 8: 
/*  426 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  427 */         _failDoubleToIntCoercion(p, ctxt, "Integer");
/*      */       }
/*  429 */       return Integer.valueOf(p.getValueAsInt());
/*      */     case 6: 
/*  431 */       String text = p.getText().trim();
/*      */       try {
/*  433 */         int len = text.length();
/*  434 */         if (_hasTextualNull(text)) {
/*  435 */           return (Integer)getNullValue(ctxt);
/*      */         }
/*  437 */         if (len > 9) {
/*  438 */           long l = Long.parseLong(text);
/*  439 */           if ((l < -2147483648L) || (l > 2147483647L)) {
/*  440 */             return (Integer)ctxt.handleWeirdStringValue(this._valueClass, text, "Overflow: numeric value (" + text + ") out of range of Integer (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")", new Object[0]);
/*      */           }
/*      */           
/*      */ 
/*  444 */           return Integer.valueOf((int)l);
/*      */         }
/*  446 */         if (len == 0) {
/*  447 */           return (Integer)getEmptyValue(ctxt);
/*      */         }
/*  449 */         return Integer.valueOf(NumberInput.parseInt(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  451 */         return (Integer)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Integer value", new Object[0]);
/*      */       }
/*      */     
/*      */ 
/*      */     case 11: 
/*  456 */       return (Integer)getNullValue(ctxt);
/*      */     case 3: 
/*  458 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  459 */         p.nextToken();
/*  460 */         Integer parsed = _parseInteger(p, ctxt);
/*  461 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/*  462 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  464 */         return parsed;
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  469 */     return (Integer)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */   protected final Long _parseLong(JsonParser p, DeserializationContext ctxt) throws IOException
/*      */   {
/*  474 */     switch (p.getCurrentTokenId())
/*      */     {
/*      */     case 7: 
/*  477 */       return Long.valueOf(p.getLongValue());
/*      */     case 8: 
/*  479 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  480 */         _failDoubleToIntCoercion(p, ctxt, "Long");
/*      */       }
/*  482 */       return Long.valueOf(p.getValueAsLong());
/*      */     
/*      */ 
/*      */     case 6: 
/*  486 */       String text = p.getText().trim();
/*  487 */       if (text.length() == 0) {
/*  488 */         return (Long)getEmptyValue(ctxt);
/*      */       }
/*  490 */       if (_hasTextualNull(text)) {
/*  491 */         return (Long)getNullValue(ctxt);
/*      */       }
/*      */       try {
/*  494 */         return Long.valueOf(NumberInput.parseLong(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  496 */         return (Long)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Long value", new Object[0]);
/*      */       }
/*      */     
/*      */     case 11: 
/*  500 */       return (Long)getNullValue(ctxt);
/*      */     case 3: 
/*  502 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  503 */         p.nextToken();
/*  504 */         Long parsed = _parseLong(p, ctxt);
/*  505 */         JsonToken t = p.nextToken();
/*  506 */         if (t != JsonToken.END_ARRAY) {
/*  507 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  509 */         return parsed;
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  514 */     return (Long)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */   protected final long _parseLongPrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  520 */     switch (p.getCurrentTokenId()) {
/*      */     case 7: 
/*  522 */       return p.getLongValue();
/*      */     case 8: 
/*  524 */       if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  525 */         _failDoubleToIntCoercion(p, ctxt, "long");
/*      */       }
/*  527 */       return p.getValueAsLong();
/*      */     case 6: 
/*  529 */       String text = p.getText().trim();
/*  530 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/*  531 */         return 0L;
/*      */       }
/*      */       try {
/*  534 */         return NumberInput.parseLong(text);
/*      */       }
/*      */       catch (IllegalArgumentException iae) {
/*  537 */         Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid long value", new Object[0]);
/*      */         
/*  539 */         return v == null ? 0L : v.longValue();
/*      */       }
/*      */     case 11: 
/*  542 */       return 0L;
/*      */     case 3: 
/*  544 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  545 */         p.nextToken();
/*  546 */         long parsed = _parseLongPrimitive(p, ctxt);
/*  547 */         JsonToken t = p.nextToken();
/*  548 */         if (t != JsonToken.END_ARRAY) {
/*  549 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  551 */         return parsed;
/*      */       }
/*      */       break;
/*      */     }
/*  555 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).longValue();
/*      */   }
/*      */   
/*      */ 
/*      */   protected final Float _parseFloat(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  562 */     JsonToken t = p.getCurrentToken();
/*      */     
/*  564 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  565 */       return Float.valueOf(p.getFloatValue());
/*      */     }
/*      */     
/*  568 */     if (t == JsonToken.VALUE_STRING) {
/*  569 */       String text = p.getText().trim();
/*  570 */       if (text.length() == 0) {
/*  571 */         return (Float)getEmptyValue(ctxt);
/*      */       }
/*  573 */       if (_hasTextualNull(text)) {
/*  574 */         return (Float)getNullValue(ctxt);
/*      */       }
/*  576 */       switch (text.charAt(0)) {
/*      */       case 'I': 
/*  578 */         if (_isPosInf(text)) {
/*  579 */           return Float.valueOf(Float.POSITIVE_INFINITY);
/*      */         }
/*      */         break;
/*      */       case 'N': 
/*  583 */         if (_isNaN(text)) {
/*  584 */           return Float.valueOf(NaN.0F);
/*      */         }
/*      */         break;
/*      */       case '-': 
/*  588 */         if (_isNegInf(text)) {
/*  589 */           return Float.valueOf(Float.NEGATIVE_INFINITY);
/*      */         }
/*      */         break;
/*      */       }
/*      */       try {
/*  594 */         return Float.valueOf(Float.parseFloat(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  596 */         return (Float)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Float value", new Object[0]);
/*      */       }
/*      */     }
/*  599 */     if (t == JsonToken.VALUE_NULL) {
/*  600 */       return (Float)getNullValue(ctxt);
/*      */     }
/*  602 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  603 */       p.nextToken();
/*  604 */       Float parsed = _parseFloat(p, ctxt);
/*  605 */       t = p.nextToken();
/*  606 */       if (t != JsonToken.END_ARRAY) {
/*  607 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  609 */       return parsed;
/*      */     }
/*      */     
/*  612 */     return (Float)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */   protected final float _parseFloatPrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  618 */     JsonToken t = p.getCurrentToken();
/*      */     
/*  620 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  621 */       return p.getFloatValue();
/*      */     }
/*  623 */     if (t == JsonToken.VALUE_STRING) {
/*  624 */       String text = p.getText().trim();
/*  625 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/*  626 */         return 0.0F;
/*      */       }
/*  628 */       switch (text.charAt(0)) {
/*      */       case 'I': 
/*  630 */         if (_isPosInf(text)) {
/*  631 */           return Float.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N': 
/*  635 */         if (_isNaN(text)) return NaN.0F;
/*      */         break;
/*      */       case '-': 
/*  638 */         if (_isNegInf(text)) {
/*  639 */           return Float.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       }
/*      */       try {
/*  644 */         return Float.parseFloat(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  646 */         Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid float value", new Object[0]);
/*      */         
/*  648 */         return v == null ? 0.0F : v.floatValue();
/*      */       } }
/*  650 */     if (t == JsonToken.VALUE_NULL) {
/*  651 */       return 0.0F;
/*      */     }
/*  653 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  654 */       p.nextToken();
/*  655 */       float parsed = _parseFloatPrimitive(p, ctxt);
/*  656 */       t = p.nextToken();
/*  657 */       if (t != JsonToken.END_ARRAY) {
/*  658 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  660 */       return parsed;
/*      */     }
/*      */     
/*  663 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).floatValue();
/*      */   }
/*      */   
/*      */   protected final Double _parseDouble(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  669 */     JsonToken t = p.getCurrentToken();
/*      */     
/*  671 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  672 */       return Double.valueOf(p.getDoubleValue());
/*      */     }
/*  674 */     if (t == JsonToken.VALUE_STRING) {
/*  675 */       String text = p.getText().trim();
/*  676 */       if (text.length() == 0) {
/*  677 */         return (Double)getEmptyValue(ctxt);
/*      */       }
/*  679 */       if (_hasTextualNull(text)) {
/*  680 */         return (Double)getNullValue(ctxt);
/*      */       }
/*  682 */       switch (text.charAt(0)) {
/*      */       case 'I': 
/*  684 */         if (_isPosInf(text)) {
/*  685 */           return Double.valueOf(Double.POSITIVE_INFINITY);
/*      */         }
/*      */         break;
/*      */       case 'N': 
/*  689 */         if (_isNaN(text)) {
/*  690 */           return Double.valueOf(NaN.0D);
/*      */         }
/*      */         break;
/*      */       case '-': 
/*  694 */         if (_isNegInf(text)) {
/*  695 */           return Double.valueOf(Double.NEGATIVE_INFINITY);
/*      */         }
/*      */         break;
/*      */       }
/*      */       try {
/*  700 */         return Double.valueOf(parseDouble(text));
/*      */       } catch (IllegalArgumentException iae) {
/*  702 */         return (Double)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid Double value", new Object[0]);
/*      */       }
/*      */     }
/*  705 */     if (t == JsonToken.VALUE_NULL) {
/*  706 */       return (Double)getNullValue(ctxt);
/*      */     }
/*  708 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  709 */       p.nextToken();
/*  710 */       Double parsed = _parseDouble(p, ctxt);
/*  711 */       t = p.nextToken();
/*  712 */       if (t != JsonToken.END_ARRAY) {
/*  713 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  715 */       return parsed;
/*      */     }
/*      */     
/*  718 */     return (Double)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final double _parseDoublePrimitive(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  725 */     JsonToken t = p.getCurrentToken();
/*      */     
/*  727 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/*  728 */       return p.getDoubleValue();
/*      */     }
/*      */     
/*  731 */     if (t == JsonToken.VALUE_STRING) {
/*  732 */       String text = p.getText().trim();
/*  733 */       if ((text.length() == 0) || (_hasTextualNull(text))) {
/*  734 */         return 0.0D;
/*      */       }
/*  736 */       switch (text.charAt(0)) {
/*      */       case 'I': 
/*  738 */         if (_isPosInf(text)) {
/*  739 */           return Double.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N': 
/*  743 */         if (_isNaN(text)) {
/*  744 */           return NaN.0D;
/*      */         }
/*      */         break;
/*      */       case '-': 
/*  748 */         if (_isNegInf(text)) {
/*  749 */           return Double.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       }
/*      */       try {
/*  754 */         return parseDouble(text);
/*      */       } catch (IllegalArgumentException iae) {
/*  756 */         Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid double value", new Object[0]);
/*      */         
/*  758 */         return v == null ? 0.0D : v.doubleValue();
/*      */       } }
/*  760 */     if (t == JsonToken.VALUE_NULL) {
/*  761 */       return 0.0D;
/*      */     }
/*      */     
/*  764 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  765 */       p.nextToken();
/*  766 */       double parsed = _parseDoublePrimitive(p, ctxt);
/*  767 */       t = p.nextToken();
/*  768 */       if (t != JsonToken.END_ARRAY) {
/*  769 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  771 */       return parsed;
/*      */     }
/*      */     
/*  774 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).doubleValue();
/*      */   }
/*      */   
/*      */   protected Date _parseDate(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  780 */     JsonToken t = p.getCurrentToken();
/*  781 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  782 */       return new Date(p.getLongValue());
/*      */     }
/*  784 */     if (t == JsonToken.VALUE_NULL) {
/*  785 */       return (Date)getNullValue(ctxt);
/*      */     }
/*  787 */     if (t == JsonToken.VALUE_STRING) {
/*  788 */       return _parseDate(p.getText().trim(), ctxt);
/*      */     }
/*      */     
/*  791 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  792 */       p.nextToken();
/*  793 */       Date parsed = _parseDate(p, ctxt);
/*  794 */       t = p.nextToken();
/*  795 */       if (t != JsonToken.END_ARRAY) {
/*  796 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  798 */       return parsed;
/*      */     }
/*  800 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Date _parseDate(String value, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  811 */       if (value.length() == 0) {
/*  812 */         return (Date)getEmptyValue(ctxt);
/*      */       }
/*  814 */       if (_hasTextualNull(value)) {
/*  815 */         return (Date)getNullValue(ctxt);
/*      */       }
/*  817 */       return ctxt.parseDate(value);
/*      */     } catch (IllegalArgumentException iae) {}
/*  819 */     return (Date)ctxt.handleWeirdStringValue(this._valueClass, value, "not a valid representation (error: %s)", tmp52_49);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final double parseDouble(String numStr)
/*      */     throws NumberFormatException
/*      */   {
/*  831 */     if ("2.2250738585072012e-308".equals(numStr)) {
/*  832 */       return 2.2250738585072014E-308D;
/*      */     }
/*  834 */     return Double.parseDouble(numStr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String _parseString(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  845 */     JsonToken t = p.getCurrentToken();
/*  846 */     if (t == JsonToken.VALUE_STRING) {
/*  847 */       return p.getText();
/*      */     }
/*      */     
/*  850 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/*  851 */       p.nextToken();
/*  852 */       String parsed = _parseString(p, ctxt);
/*  853 */       if (p.nextToken() != JsonToken.END_ARRAY) {
/*  854 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/*  856 */       return parsed;
/*      */     }
/*  858 */     String value = p.getValueAsString();
/*  859 */     if (value != null) {
/*  860 */       return value;
/*      */     }
/*  862 */     return (String)ctxt.handleUnexpectedToken(String.class, p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected T _deserializeFromEmpty(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  875 */     JsonToken t = p.getCurrentToken();
/*  876 */     if (t == JsonToken.START_ARRAY) {
/*  877 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  878 */         t = p.nextToken();
/*  879 */         if (t == JsonToken.END_ARRAY) {
/*  880 */           return null;
/*      */         }
/*  882 */         return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */       }
/*  884 */     } else if ((t == JsonToken.VALUE_STRING) && 
/*  885 */       (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT))) {
/*  886 */       String str = p.getText().trim();
/*  887 */       if (str.isEmpty()) {
/*  888 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  892 */     return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasTextualNull(String value)
/*      */   {
/*  903 */     return "null".equals(value);
/*      */   }
/*      */   
/*      */   protected final boolean _isNegInf(String text) {
/*  907 */     return ("-Infinity".equals(text)) || ("-INF".equals(text));
/*      */   }
/*      */   
/*      */   protected final boolean _isPosInf(String text) {
/*  911 */     return ("Infinity".equals(text)) || ("INF".equals(text));
/*      */   }
/*      */   
/*  914 */   protected final boolean _isNaN(String text) { return "NaN".equals(text); }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  935 */     int feats = ctxt.getDeserializationFeatures();
/*  936 */     if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
/*  937 */       return p.getBigIntegerValue();
/*      */     }
/*  939 */     if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
/*  940 */       return Long.valueOf(p.getLongValue());
/*      */     }
/*  942 */     return p.getBigIntegerValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  964 */     return ctxt.findContextualValueDeserializer(type, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _isIntNumber(String text)
/*      */   {
/*  973 */     int len = text.length();
/*  974 */     if (len > 0) {
/*  975 */       char c = text.charAt(0);
/*      */       
/*  977 */       for (int i = (c == '-') || (c == '+') ? 1 : 0; 
/*  978 */           i < len; i++) {
/*  979 */         int ch = text.charAt(i);
/*  980 */         if ((ch > 57) || (ch < 48)) {
/*  981 */           return false;
/*      */         }
/*      */       }
/*  984 */       return true;
/*      */     }
/*  986 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/* 1009 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1010 */     if ((intr != null) && (prop != null)) {
/* 1011 */       AnnotatedMember member = prop.getMember();
/* 1012 */       if (member != null) {
/* 1013 */         Object convDef = intr.findDeserializationContentConverter(member);
/* 1014 */         if (convDef != null) {
/* 1015 */           Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
/* 1016 */           JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 1017 */           if (existingDeserializer == null) {
/* 1018 */             existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
/*      */           }
/* 1020 */           return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
/*      */         }
/*      */       }
/*      */     }
/* 1024 */     return existingDeserializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonFormat.Value findFormatOverrides(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults)
/*      */   {
/* 1039 */     if (prop != null) {
/* 1040 */       return prop.findPropertyFormat(ctxt.getConfig(), typeForDefaults);
/*      */     }
/*      */     
/* 1043 */     return ctxt.getDefaultPropertyFormat(typeForDefaults);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Boolean findFormatFeature(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat)
/*      */   {
/* 1059 */     JsonFormat.Value format = findFormatOverrides(ctxt, prop, typeForDefaults);
/* 1060 */     if (format != null) {
/* 1061 */       return format.getFeature(feat);
/*      */     }
/* 1063 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object instanceOrClass, String propName)
/*      */     throws IOException
/*      */   {
/* 1089 */     if (instanceOrClass == null) {
/* 1090 */       instanceOrClass = handledType();
/*      */     }
/*      */     
/* 1093 */     if (ctxt.handleUnknownProperty(p, this, instanceOrClass, propName)) {
/* 1094 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1099 */     p.skipChildren();
/*      */   }
/*      */   
/*      */   protected void handleMissingEndArrayForSingle(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1105 */     ctxt.reportWrongTokenException(p, JsonToken.END_ARRAY, "Attempted to unwrap single value array for single '%s' value but there was more than a single value in the array", new Object[] { handledType().getName() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _failDoubleToIntCoercion(JsonParser p, DeserializationContext ctxt, String type)
/*      */     throws IOException
/*      */   {
/* 1115 */     ctxt.reportMappingException("Can not coerce a floating-point value ('%s') into %s; enable `DeserializationFeature.ACCEPT_FLOAT_AS_INT` to allow", new Object[] { p.getValueAsString(), type });
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StdDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */