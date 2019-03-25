/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variants;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.ByteBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.DoubleBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.FloatBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.IntBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.LongBuilder;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders.ShortBuilder;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class PrimitiveArrayDeserializers<T> extends StdDeserializer<T> implements ContextualDeserializer
/*     */ {
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   protected PrimitiveArrayDeserializers(Class<T> cls)
/*     */   {
/*  31 */     super(cls);
/*  32 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PrimitiveArrayDeserializers(PrimitiveArrayDeserializers<?> base, Boolean unwrapSingle)
/*     */   {
/*  40 */     super(base._valueClass);
/*  41 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */   public static JsonDeserializer<?> forType(Class<?> rawType)
/*     */   {
/*  47 */     if (rawType == Integer.TYPE) {
/*  48 */       return IntDeser.instance;
/*     */     }
/*  50 */     if (rawType == Long.TYPE) {
/*  51 */       return LongDeser.instance;
/*     */     }
/*     */     
/*  54 */     if (rawType == Byte.TYPE) {
/*  55 */       return new ByteDeser();
/*     */     }
/*  57 */     if (rawType == Short.TYPE) {
/*  58 */       return new ShortDeser();
/*     */     }
/*  60 */     if (rawType == Float.TYPE) {
/*  61 */       return new FloatDeser();
/*     */     }
/*  63 */     if (rawType == Double.TYPE) {
/*  64 */       return new DoubleDeser();
/*     */     }
/*  66 */     if (rawType == Boolean.TYPE) {
/*  67 */       return new BooleanDeser();
/*     */     }
/*  69 */     if (rawType == Character.TYPE) {
/*  70 */       return new CharDeser();
/*     */     }
/*  72 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract PrimitiveArrayDeserializers<?> withResolved(Boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  84 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, this._valueClass, com.fasterxml.jackson.annotation.JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/*  86 */     if (unwrapSingle == this._unwrapSingle) {
/*  87 */       return this;
/*     */     }
/*  89 */     return withResolved(unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/*  99 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */   protected T handleNonArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 106 */     if ((p.hasToken(JsonToken.VALUE_STRING)) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)))
/*     */     {
/* 108 */       if (p.getText().length() == 0) {
/* 109 */         return null;
/*     */       }
/*     */     }
/* 112 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/* 115 */     if (canWrap) {
/* 116 */       return (T)handleSingleElementUnwrapped(p, ctxt);
/*     */     }
/* 118 */     return (T)ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract T handleSingleElementUnwrapped(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class CharDeser
/*     */     extends PrimitiveArrayDeserializers<char[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/* 136 */     public CharDeser() { super(); }
/*     */     
/* 138 */     protected CharDeser(CharDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 144 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public char[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 154 */       JsonToken t = p.getCurrentToken();
/* 155 */       if (t == JsonToken.VALUE_STRING)
/*     */       {
/* 157 */         char[] buffer = p.getTextCharacters();
/* 158 */         int offset = p.getTextOffset();
/* 159 */         int len = p.getTextLength();
/*     */         
/* 161 */         char[] result = new char[len];
/* 162 */         System.arraycopy(buffer, offset, result, 0, len);
/* 163 */         return result;
/*     */       }
/* 165 */       if (p.isExpectedStartArrayToken())
/*     */       {
/* 167 */         StringBuilder sb = new StringBuilder(64);
/* 168 */         while ((t = p.nextToken()) != JsonToken.END_ARRAY) { String str;
/*     */           String str;
/* 170 */           if (t == JsonToken.VALUE_STRING) {
/* 171 */             str = p.getText();
/*     */           } else {
/* 173 */             CharSequence cs = (CharSequence)ctxt.handleUnexpectedToken(Character.TYPE, p);
/* 174 */             str = cs.toString();
/*     */           }
/* 176 */           if (str.length() != 1) {
/* 177 */             ctxt.reportMappingException("Can not convert a JSON String of length %d into a char element of char array", new Object[] { Integer.valueOf(str.length()) });
/*     */           }
/*     */           
/* 180 */           sb.append(str.charAt(0));
/*     */         }
/* 182 */         return sb.toString().toCharArray();
/*     */       }
/*     */       
/* 185 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 186 */         Object ob = p.getEmbeddedObject();
/* 187 */         if (ob == null) return null;
/* 188 */         if ((ob instanceof char[])) {
/* 189 */           return (char[])ob;
/*     */         }
/* 191 */         if ((ob instanceof String)) {
/* 192 */           return ((String)ob).toCharArray();
/*     */         }
/*     */         
/* 195 */         if ((ob instanceof byte[])) {
/* 196 */           return Base64Variants.getDefaultVariant().encode((byte[])ob, false).toCharArray();
/*     */         }
/*     */       }
/*     */       
/* 200 */       return (char[])ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */     }
/*     */     
/*     */ 
/*     */     protected char[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 207 */       return (char[])ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class BooleanDeser
/*     */     extends PrimitiveArrayDeserializers<boolean[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 223 */     public BooleanDeser() { super(); }
/*     */     
/* 225 */     protected BooleanDeser(BooleanDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 230 */       return new BooleanDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 237 */       if (!p.isExpectedStartArrayToken()) {
/* 238 */         return (boolean[])handleNonArray(p, ctxt);
/*     */       }
/* 240 */       ArrayBuilders.BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
/* 241 */       boolean[] chunk = (boolean[])builder.resetAndStart();
/* 242 */       int ix = 0;
/*     */       try
/*     */       {
/* 245 */         while (p.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 247 */           boolean value = _parseBooleanPrimitive(p, ctxt);
/* 248 */           if (ix >= chunk.length) {
/* 249 */             chunk = (boolean[])builder.appendCompletedChunk(chunk, ix);
/* 250 */             ix = 0;
/*     */           }
/* 252 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 255 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 257 */       return (boolean[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected boolean[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 263 */       return new boolean[] { _parseBooleanPrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class ByteDeser
/*     */     extends PrimitiveArrayDeserializers<byte[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/* 277 */     public ByteDeser() { super(); }
/*     */     
/* 279 */     protected ByteDeser(ByteDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 284 */       return new ByteDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */     public byte[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 290 */       JsonToken t = p.getCurrentToken();
/*     */       
/*     */ 
/* 293 */       if (t == JsonToken.VALUE_STRING) {
/* 294 */         return p.getBinaryValue(ctxt.getBase64Variant());
/*     */       }
/*     */       
/* 297 */       if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 298 */         Object ob = p.getEmbeddedObject();
/* 299 */         if (ob == null) return null;
/* 300 */         if ((ob instanceof byte[])) {
/* 301 */           return (byte[])ob;
/*     */         }
/*     */       }
/* 304 */       if (!p.isExpectedStartArrayToken()) {
/* 305 */         return (byte[])handleNonArray(p, ctxt);
/*     */       }
/* 307 */       ArrayBuilders.ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
/* 308 */       byte[] chunk = (byte[])builder.resetAndStart();
/* 309 */       int ix = 0;
/*     */       try
/*     */       {
/* 312 */         while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */           byte value;
/*     */           byte value;
/* 315 */           if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*     */           {
/* 317 */             value = p.getByteValue();
/*     */           } else {
/*     */             byte value;
/* 320 */             if (t == JsonToken.VALUE_NULL) {
/* 321 */               value = 0;
/*     */             } else {
/* 323 */               Number n = (Number)ctxt.handleUnexpectedToken(this._valueClass.getComponentType(), p);
/* 324 */               value = n.byteValue();
/*     */             }
/*     */           }
/* 327 */           if (ix >= chunk.length) {
/* 328 */             chunk = (byte[])builder.appendCompletedChunk(chunk, ix);
/* 329 */             ix = 0;
/*     */           }
/* 331 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 334 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 336 */       return (byte[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected byte[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 344 */       JsonToken t = p.getCurrentToken();
/* 345 */       byte value; byte value; if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*     */       {
/* 347 */         value = p.getByteValue();
/*     */       }
/*     */       else {
/* 350 */         if (t == JsonToken.VALUE_NULL) {
/* 351 */           return null;
/*     */         }
/* 353 */         Number n = (Number)ctxt.handleUnexpectedToken(this._valueClass.getComponentType(), p);
/* 354 */         value = n.byteValue();
/*     */       }
/* 356 */       return new byte[] { value };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class ShortDeser
/*     */     extends PrimitiveArrayDeserializers<short[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 366 */     public ShortDeser() { super(); }
/*     */     
/* 368 */     protected ShortDeser(ShortDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 373 */       return new ShortDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */     public short[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 379 */       if (!p.isExpectedStartArrayToken()) {
/* 380 */         return (short[])handleNonArray(p, ctxt);
/*     */       }
/* 382 */       ArrayBuilders.ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
/* 383 */       short[] chunk = (short[])builder.resetAndStart();
/* 384 */       int ix = 0;
/*     */       try
/*     */       {
/* 387 */         while (p.nextToken() != JsonToken.END_ARRAY) {
/* 388 */           short value = _parseShortPrimitive(p, ctxt);
/* 389 */           if (ix >= chunk.length) {
/* 390 */             chunk = (short[])builder.appendCompletedChunk(chunk, ix);
/* 391 */             ix = 0;
/*     */           }
/* 393 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 396 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 398 */       return (short[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected short[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 404 */       return new short[] { _parseShortPrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class IntDeser
/*     */     extends PrimitiveArrayDeserializers<int[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 414 */     public static final IntDeser instance = new IntDeser();
/*     */     
/* 416 */     public IntDeser() { super(); }
/*     */     
/* 418 */     protected IntDeser(IntDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 423 */       return new IntDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */     public int[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 429 */       if (!p.isExpectedStartArrayToken()) {
/* 430 */         return (int[])handleNonArray(p, ctxt);
/*     */       }
/* 432 */       ArrayBuilders.IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
/* 433 */       int[] chunk = (int[])builder.resetAndStart();
/* 434 */       int ix = 0;
/*     */       try
/*     */       {
/* 437 */         while (p.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 439 */           int value = _parseIntPrimitive(p, ctxt);
/* 440 */           if (ix >= chunk.length) {
/* 441 */             chunk = (int[])builder.appendCompletedChunk(chunk, ix);
/* 442 */             ix = 0;
/*     */           }
/* 444 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 447 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 449 */       return (int[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected int[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 455 */       return new int[] { _parseIntPrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class LongDeser
/*     */     extends PrimitiveArrayDeserializers<long[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 465 */     public static final LongDeser instance = new LongDeser();
/*     */     
/* 467 */     public LongDeser() { super(); }
/*     */     
/* 469 */     protected LongDeser(LongDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 474 */       return new LongDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */     public long[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 480 */       if (!p.isExpectedStartArrayToken()) {
/* 481 */         return (long[])handleNonArray(p, ctxt);
/*     */       }
/* 483 */       ArrayBuilders.LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
/* 484 */       long[] chunk = (long[])builder.resetAndStart();
/* 485 */       int ix = 0;
/*     */       try
/*     */       {
/* 488 */         while (p.nextToken() != JsonToken.END_ARRAY) {
/* 489 */           long value = _parseLongPrimitive(p, ctxt);
/* 490 */           if (ix >= chunk.length) {
/* 491 */             chunk = (long[])builder.appendCompletedChunk(chunk, ix);
/* 492 */             ix = 0;
/*     */           }
/* 494 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 497 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 499 */       return (long[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected long[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 505 */       return new long[] { _parseLongPrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class FloatDeser
/*     */     extends PrimitiveArrayDeserializers<float[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 515 */     public FloatDeser() { super(); }
/*     */     
/* 517 */     protected FloatDeser(FloatDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 522 */       return new FloatDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */ 
/*     */     public float[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 529 */       if (!p.isExpectedStartArrayToken()) {
/* 530 */         return (float[])handleNonArray(p, ctxt);
/*     */       }
/* 532 */       ArrayBuilders.FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
/* 533 */       float[] chunk = (float[])builder.resetAndStart();
/* 534 */       int ix = 0;
/*     */       try
/*     */       {
/* 537 */         while (p.nextToken() != JsonToken.END_ARRAY)
/*     */         {
/* 539 */           float value = _parseFloatPrimitive(p, ctxt);
/* 540 */           if (ix >= chunk.length) {
/* 541 */             chunk = (float[])builder.appendCompletedChunk(chunk, ix);
/* 542 */             ix = 0;
/*     */           }
/* 544 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 547 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 549 */       return (float[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected float[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 555 */       return new float[] { _parseFloatPrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   static final class DoubleDeser
/*     */     extends PrimitiveArrayDeserializers<double[]>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 565 */     public DoubleDeser() { super(); }
/*     */     
/* 567 */     protected DoubleDeser(DoubleDeser base, Boolean unwrapSingle) { super(unwrapSingle); }
/*     */     
/*     */ 
/*     */     protected PrimitiveArrayDeserializers<?> withResolved(Boolean unwrapSingle)
/*     */     {
/* 572 */       return new DoubleDeser(this, unwrapSingle);
/*     */     }
/*     */     
/*     */     public double[] deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 578 */       if (!p.isExpectedStartArrayToken()) {
/* 579 */         return (double[])handleNonArray(p, ctxt);
/*     */       }
/* 581 */       ArrayBuilders.DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
/* 582 */       double[] chunk = (double[])builder.resetAndStart();
/* 583 */       int ix = 0;
/*     */       try
/*     */       {
/* 586 */         while (p.nextToken() != JsonToken.END_ARRAY) {
/* 587 */           double value = _parseDoublePrimitive(p, ctxt);
/* 588 */           if (ix >= chunk.length) {
/* 589 */             chunk = (double[])builder.appendCompletedChunk(chunk, ix);
/* 590 */             ix = 0;
/*     */           }
/* 592 */           chunk[(ix++)] = value;
/*     */         }
/*     */       } catch (Exception e) {
/* 595 */         throw JsonMappingException.wrapWithPath(e, chunk, builder.bufferedSize() + ix);
/*     */       }
/* 597 */       return (double[])builder.completeAndClearBuffer(chunk, ix);
/*     */     }
/*     */     
/*     */     protected double[] handleSingleElementUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 603 */       return new double[] { _parseDoublePrimitive(p, ctxt) };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\PrimitiveArrayDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */