/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberDeserializers
/*     */ {
/*  22 */   private static final HashSet<String> _classNames = new HashSet();
/*     */   
/*     */   static {
/*  25 */     Class<?>[] numberTypes = { Boolean.class, Byte.class, Short.class, Character.class, Integer.class, Long.class, Float.class, Double.class, Number.class, BigDecimal.class, BigInteger.class };
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
/*  37 */     for (Class<?> cls : numberTypes) {
/*  38 */       _classNames.add(cls.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static JsonDeserializer<?> find(Class<?> rawType, String clsName) {
/*  43 */     if (rawType.isPrimitive()) {
/*  44 */       if (rawType == Integer.TYPE) {
/*  45 */         return IntegerDeserializer.primitiveInstance;
/*     */       }
/*  47 */       if (rawType == Boolean.TYPE) {
/*  48 */         return BooleanDeserializer.primitiveInstance;
/*     */       }
/*  50 */       if (rawType == Long.TYPE) {
/*  51 */         return LongDeserializer.primitiveInstance;
/*     */       }
/*  53 */       if (rawType == Double.TYPE) {
/*  54 */         return DoubleDeserializer.primitiveInstance;
/*     */       }
/*  56 */       if (rawType == Character.TYPE) {
/*  57 */         return CharacterDeserializer.primitiveInstance;
/*     */       }
/*  59 */       if (rawType == Byte.TYPE) {
/*  60 */         return ByteDeserializer.primitiveInstance;
/*     */       }
/*  62 */       if (rawType == Short.TYPE) {
/*  63 */         return ShortDeserializer.primitiveInstance;
/*     */       }
/*  65 */       if (rawType == Float.TYPE) {
/*  66 */         return FloatDeserializer.primitiveInstance;
/*     */       }
/*  68 */     } else if (_classNames.contains(clsName))
/*     */     {
/*  70 */       if (rawType == Integer.class) {
/*  71 */         return IntegerDeserializer.wrapperInstance;
/*     */       }
/*  73 */       if (rawType == Boolean.class) {
/*  74 */         return BooleanDeserializer.wrapperInstance;
/*     */       }
/*  76 */       if (rawType == Long.class) {
/*  77 */         return LongDeserializer.wrapperInstance;
/*     */       }
/*  79 */       if (rawType == Double.class) {
/*  80 */         return DoubleDeserializer.wrapperInstance;
/*     */       }
/*  82 */       if (rawType == Character.class) {
/*  83 */         return CharacterDeserializer.wrapperInstance;
/*     */       }
/*  85 */       if (rawType == Byte.class) {
/*  86 */         return ByteDeserializer.wrapperInstance;
/*     */       }
/*  88 */       if (rawType == Short.class) {
/*  89 */         return ShortDeserializer.wrapperInstance;
/*     */       }
/*  91 */       if (rawType == Float.class) {
/*  92 */         return FloatDeserializer.wrapperInstance;
/*     */       }
/*  94 */       if (rawType == Number.class) {
/*  95 */         return NumberDeserializer.instance;
/*     */       }
/*  97 */       if (rawType == BigDecimal.class) {
/*  98 */         return BigDecimalDeserializer.instance;
/*     */       }
/* 100 */       if (rawType == BigInteger.class) {
/* 101 */         return BigIntegerDeserializer.instance;
/*     */       }
/*     */     } else {
/* 104 */       return null;
/*     */     }
/*     */     
/* 107 */     throw new IllegalArgumentException("Internal error: can't find deserializer for " + rawType.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static abstract class PrimitiveOrWrapperDeserializer<T>
/*     */     extends StdScalarDeserializer<T>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */     protected final T _nullValue;
/*     */     
/*     */ 
/*     */     protected final boolean _primitive;
/*     */     
/*     */ 
/*     */     protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl)
/*     */     {
/* 126 */       super();
/* 127 */       this._nullValue = nvl;
/* 128 */       this._primitive = vc.isPrimitive();
/*     */     }
/*     */     
/*     */     public final T getNullValue(DeserializationContext ctxt)
/*     */       throws JsonMappingException
/*     */     {
/* 134 */       if ((this._primitive) && (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES))) {
/* 135 */         ctxt.reportMappingException("Can not map JSON null into type %s (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)", new Object[] { handledType().toString() });
/*     */       }
/*     */       
/*     */ 
/* 139 */       return (T)this._nullValue;
/*     */     }
/*     */     
/*     */ 
/*     */     public T getEmptyValue(DeserializationContext ctxt)
/*     */       throws JsonMappingException
/*     */     {
/* 146 */       if ((this._primitive) && (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES))) {
/* 147 */         ctxt.reportMappingException("Can not map Empty String as null into type %s (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)", new Object[] { handledType().toString() });
/*     */       }
/*     */       
/*     */ 
/* 151 */       return (T)this._nullValue;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class BooleanDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Boolean>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/* 167 */     static final BooleanDeserializer primitiveInstance = new BooleanDeserializer(Boolean.TYPE, Boolean.FALSE);
/* 168 */     static final BooleanDeserializer wrapperInstance = new BooleanDeserializer(Boolean.class, null);
/*     */     
/*     */     public BooleanDeserializer(Class<Boolean> cls, Boolean nvl)
/*     */     {
/* 172 */       super(nvl);
/*     */     }
/*     */     
/*     */     public Boolean deserialize(JsonParser j, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 178 */       return _parseBoolean(j, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Boolean deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 188 */       return _parseBoolean(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ByteDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Byte>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 198 */     static final ByteDeserializer primitiveInstance = new ByteDeserializer(Byte.TYPE, Byte.valueOf((byte)0));
/* 199 */     static final ByteDeserializer wrapperInstance = new ByteDeserializer(Byte.class, null);
/*     */     
/*     */     public ByteDeserializer(Class<Byte> cls, Byte nvl)
/*     */     {
/* 203 */       super(nvl);
/*     */     }
/*     */     
/*     */     public Byte deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 209 */       return _parseByte(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ShortDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Short>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 219 */     static final ShortDeserializer primitiveInstance = new ShortDeserializer(Short.TYPE, Short.valueOf((short)0));
/* 220 */     static final ShortDeserializer wrapperInstance = new ShortDeserializer(Short.class, null);
/*     */     
/*     */     public ShortDeserializer(Class<Short> cls, Short nvl)
/*     */     {
/* 224 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Short deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 231 */       return _parseShort(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CharacterDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Character>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 241 */     static final CharacterDeserializer primitiveInstance = new CharacterDeserializer(Character.TYPE, Character.valueOf('\000'));
/* 242 */     static final CharacterDeserializer wrapperInstance = new CharacterDeserializer(Character.class, null);
/*     */     
/*     */     public CharacterDeserializer(Class<Character> cls, Character nvl)
/*     */     {
/* 246 */       super(nvl);
/*     */     }
/*     */     
/*     */ 
/*     */     public Character deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 253 */       switch (p.getCurrentTokenId()) {
/*     */       case 7: 
/* 255 */         int value = p.getIntValue();
/* 256 */         if ((value >= 0) && (value <= 65535)) {
/* 257 */           return Character.valueOf((char)value);
/*     */         }
/*     */         
/*     */         break;
/*     */       case 6: 
/* 262 */         String text = p.getText();
/* 263 */         if (text.length() == 1) {
/* 264 */           return Character.valueOf(text.charAt(0));
/*     */         }
/*     */         
/* 267 */         if (text.length() == 0) {
/* 268 */           return (Character)getEmptyValue(ctxt);
/*     */         }
/*     */         break;
/*     */       case 3: 
/* 272 */         return (Character)_deserializeFromArray(p, ctxt);
/*     */       }
/*     */       
/* 275 */       return (Character)ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntegerDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 285 */     static final IntegerDeserializer primitiveInstance = new IntegerDeserializer(Integer.TYPE, Integer.valueOf(0));
/* 286 */     static final IntegerDeserializer wrapperInstance = new IntegerDeserializer(Integer.class, null);
/*     */     
/*     */     public IntegerDeserializer(Class<Integer> cls, Integer nvl) {
/* 289 */       super(nvl);
/*     */     }
/*     */     
/*     */     public boolean isCachable()
/*     */     {
/* 294 */       return true;
/*     */     }
/*     */     
/*     */     public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 298 */       if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/* 299 */         return Integer.valueOf(p.getIntValue());
/*     */       }
/* 301 */       return _parseInteger(p, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Integer deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 310 */       if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/* 311 */         return Integer.valueOf(p.getIntValue());
/*     */       }
/* 313 */       return _parseInteger(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class LongDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Long>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 323 */     static final LongDeserializer primitiveInstance = new LongDeserializer(Long.TYPE, Long.valueOf(0L));
/* 324 */     static final LongDeserializer wrapperInstance = new LongDeserializer(Long.class, null);
/*     */     
/*     */     public LongDeserializer(Class<Long> cls, Long nvl) {
/* 327 */       super(nvl);
/*     */     }
/*     */     
/*     */     public boolean isCachable()
/*     */     {
/* 332 */       return true;
/*     */     }
/*     */     
/*     */     public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 336 */       if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/* 337 */         return Long.valueOf(p.getLongValue());
/*     */       }
/* 339 */       return _parseLong(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class FloatDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Float>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 349 */     static final FloatDeserializer primitiveInstance = new FloatDeserializer(Float.TYPE, Float.valueOf(0.0F));
/* 350 */     static final FloatDeserializer wrapperInstance = new FloatDeserializer(Float.class, null);
/*     */     
/*     */     public FloatDeserializer(Class<Float> cls, Float nvl) {
/* 353 */       super(nvl);
/*     */     }
/*     */     
/*     */     public Float deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 359 */       return _parseFloat(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class DoubleDeserializer
/*     */     extends NumberDeserializers.PrimitiveOrWrapperDeserializer<Double>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 369 */     static final DoubleDeserializer primitiveInstance = new DoubleDeserializer(Double.TYPE, Double.valueOf(0.0D));
/* 370 */     static final DoubleDeserializer wrapperInstance = new DoubleDeserializer(Double.class, null);
/*     */     
/*     */     public DoubleDeserializer(Class<Double> cls, Double nvl) {
/* 373 */       super(nvl);
/*     */     }
/*     */     
/*     */     public Double deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 378 */       return _parseDouble(jp, ctxt);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 387 */       return _parseDouble(jp, ctxt);
/*     */     }
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
/*     */   @JacksonStdImpl
/*     */   public static class NumberDeserializer
/*     */     extends StdScalarDeserializer<Object>
/*     */   {
/* 406 */     public static final NumberDeserializer instance = new NumberDeserializer();
/*     */     
/*     */     public NumberDeserializer() {
/* 409 */       super();
/*     */     }
/*     */     
/*     */     public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 415 */       switch (p.getCurrentTokenId()) {
/*     */       case 7: 
/* 417 */         if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
/* 418 */           return _coerceIntegral(p, ctxt);
/*     */         }
/* 420 */         return p.getNumberValue();
/*     */       
/*     */       case 8: 
/* 423 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 424 */           return p.getDecimalValue();
/*     */         }
/* 426 */         return p.getNumberValue();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       case 6: 
/* 432 */         String text = p.getText().trim();
/* 433 */         if (text.length() == 0) {
/* 434 */           return getEmptyValue(ctxt);
/*     */         }
/* 436 */         if (_hasTextualNull(text)) {
/* 437 */           return getNullValue(ctxt);
/*     */         }
/* 439 */         if (_isPosInf(text)) {
/* 440 */           return Double.valueOf(Double.POSITIVE_INFINITY);
/*     */         }
/* 442 */         if (_isNegInf(text)) {
/* 443 */           return Double.valueOf(Double.NEGATIVE_INFINITY);
/*     */         }
/* 445 */         if (_isNaN(text)) {
/* 446 */           return Double.valueOf(NaN.0D);
/*     */         }
/*     */         try {
/* 449 */           if (!_isIntNumber(text)) {
/* 450 */             if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 451 */               return new BigDecimal(text);
/*     */             }
/* 453 */             return new Double(text);
/*     */           }
/* 455 */           if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 456 */             return new BigInteger(text);
/*     */           }
/* 458 */           long value = Long.parseLong(text);
/* 459 */           if ((!ctxt.isEnabled(DeserializationFeature.USE_LONG_FOR_INTS)) && 
/* 460 */             (value <= 2147483647L) && (value >= -2147483648L)) {
/* 461 */             return Integer.valueOf((int)value);
/*     */           }
/*     */           
/* 464 */           return Long.valueOf(value);
/*     */         } catch (IllegalArgumentException iae) {
/* 466 */           return ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid number", new Object[0]);
/*     */         }
/*     */       
/*     */       case 3: 
/* 470 */         return _deserializeFromArray(p, ctxt);
/*     */       }
/*     */       
/* 473 */       return ctxt.handleUnexpectedToken(this._valueClass, p);
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
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 487 */       switch (jp.getCurrentTokenId())
/*     */       {
/*     */       case 6: 
/*     */       case 7: 
/*     */       case 8: 
/* 492 */         return deserialize(jp, ctxt);
/*     */       }
/* 494 */       return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
/*     */     }
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
/*     */   @JacksonStdImpl
/*     */   public static class BigIntegerDeserializer
/*     */     extends StdScalarDeserializer<BigInteger>
/*     */   {
/* 514 */     public static final BigIntegerDeserializer instance = new BigIntegerDeserializer();
/*     */     
/* 516 */     public BigIntegerDeserializer() { super(); }
/*     */     
/*     */ 
/*     */     public BigInteger deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 522 */       switch (p.getCurrentTokenId()) {
/*     */       case 7: 
/* 524 */         switch (NumberDeserializers.1.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[p.getNumberType().ordinal()]) {
/*     */         case 1: 
/*     */         case 2: 
/*     */         case 3: 
/* 528 */           return p.getBigIntegerValue();
/*     */         }
/* 530 */         break;
/*     */       case 8: 
/* 532 */         if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/* 533 */           _failDoubleToIntCoercion(p, ctxt, "java.math.BigInteger");
/*     */         }
/* 535 */         return p.getDecimalValue().toBigInteger();
/*     */       case 3: 
/* 537 */         return (BigInteger)_deserializeFromArray(p, ctxt);
/*     */       case 6: 
/* 539 */         String text = p.getText().trim();
/* 540 */         if (text.length() == 0) {
/* 541 */           return null;
/*     */         }
/*     */         try {
/* 544 */           return new BigInteger(text);
/*     */         } catch (IllegalArgumentException iae) {
/* 546 */           return (BigInteger)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid representation", new Object[0]);
/*     */         }
/*     */       }
/*     */       
/*     */       
/* 551 */       return (BigInteger)ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class BigDecimalDeserializer
/*     */     extends StdScalarDeserializer<BigDecimal>
/*     */   {
/* 560 */     public static final BigDecimalDeserializer instance = new BigDecimalDeserializer();
/*     */     
/* 562 */     public BigDecimalDeserializer() { super(); }
/*     */     
/*     */ 
/*     */     public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 568 */       switch (p.getCurrentTokenId()) {
/*     */       case 7: 
/*     */       case 8: 
/* 571 */         return p.getDecimalValue();
/*     */       case 6: 
/* 573 */         String text = p.getText().trim();
/* 574 */         if (text.length() == 0) {
/* 575 */           return null;
/*     */         }
/*     */         try {
/* 578 */           return new BigDecimal(text);
/*     */         } catch (IllegalArgumentException iae) {
/* 580 */           return (BigDecimal)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid representation", new Object[0]);
/*     */         }
/*     */       
/*     */       case 3: 
/* 584 */         return (BigDecimal)_deserializeFromArray(p, ctxt);
/*     */       }
/*     */       
/* 587 */       return (BigDecimal)ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\NumberDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */