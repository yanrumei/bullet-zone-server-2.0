/*      */ package com.fasterxml.jackson.databind.util;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*      */ import com.fasterxml.jackson.core.json.JsonReadContext;
/*      */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JsonSerializable;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ public class TokenBuffer extends JsonGenerator
/*      */ {
/*   33 */   protected static final int DEFAULT_GENERATOR_FEATURES = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectCodec _objectCodec;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _generatorFeatures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _closed;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasNativeTypeIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasNativeObjectIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _mayHaveNativeIds;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _forceBigDecimal;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Segment _first;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Segment _last;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _appendAt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _typeId;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _objectId;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  119 */   protected boolean _hasNativeId = false;
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
/*      */   protected JsonWriteContext _writeContext;
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
/*      */   @Deprecated
/*      */   public TokenBuffer(ObjectCodec codec)
/*      */   {
/*  144 */     this(codec, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer(ObjectCodec codec, boolean hasNativeIds)
/*      */   {
/*  156 */     this._objectCodec = codec;
/*  157 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  158 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  160 */     this._first = (this._last = new Segment());
/*  161 */     this._appendAt = 0;
/*  162 */     this._hasNativeTypeIds = hasNativeIds;
/*  163 */     this._hasNativeObjectIds = hasNativeIds;
/*      */     
/*  165 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TokenBuffer(JsonParser p)
/*      */   {
/*  172 */     this(p, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer(JsonParser p, DeserializationContext ctxt)
/*      */   {
/*  180 */     this._objectCodec = p.getCodec();
/*  181 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  182 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  184 */     this._first = (this._last = new Segment());
/*  185 */     this._appendAt = 0;
/*  186 */     this._hasNativeTypeIds = p.canReadTypeId();
/*  187 */     this._hasNativeObjectIds = p.canReadObjectId();
/*  188 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*  189 */     this._forceBigDecimal = (ctxt == null ? false : ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer forceUseOfBigDecimal(boolean b)
/*      */   {
/*  197 */     this._forceBigDecimal = b;
/*  198 */     return this;
/*      */   }
/*      */   
/*      */   public Version version()
/*      */   {
/*  203 */     return PackageVersion.VERSION;
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
/*      */   public JsonParser asParser()
/*      */   {
/*  218 */     return asParser(this._objectCodec);
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
/*      */   public JsonParser asParser(ObjectCodec codec)
/*      */   {
/*  236 */     return new Parser(this._first, codec, this._hasNativeTypeIds, this._hasNativeObjectIds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser asParser(JsonParser src)
/*      */   {
/*  245 */     Parser p = new Parser(this._first, src.getCodec(), this._hasNativeTypeIds, this._hasNativeObjectIds);
/*  246 */     p.setLocation(src.getTokenLocation());
/*  247 */     return p;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonToken firstToken()
/*      */   {
/*  257 */     if (this._first != null) {
/*  258 */       return this._first.type(0);
/*      */     }
/*  260 */     return null;
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
/*      */   public TokenBuffer append(TokenBuffer other)
/*      */     throws IOException
/*      */   {
/*  280 */     if (!this._hasNativeTypeIds) {
/*  281 */       this._hasNativeTypeIds = other.canWriteTypeId();
/*      */     }
/*  283 */     if (!this._hasNativeObjectIds) {
/*  284 */       this._hasNativeObjectIds = other.canWriteObjectId();
/*      */     }
/*  286 */     this._mayHaveNativeIds = (this._hasNativeTypeIds | this._hasNativeObjectIds);
/*      */     
/*  288 */     JsonParser p = other.asParser();
/*  289 */     while (p.nextToken() != null) {
/*  290 */       copyCurrentStructure(p);
/*      */     }
/*  292 */     return this;
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
/*      */   public void serialize(JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  307 */     Segment segment = this._first;
/*  308 */     int ptr = -1;
/*      */     
/*  310 */     boolean checkIds = this._mayHaveNativeIds;
/*  311 */     boolean hasIds = (checkIds) && (segment.hasIds());
/*      */     for (;;)
/*      */     {
/*  314 */       ptr++; if (ptr >= 16) {
/*  315 */         ptr = 0;
/*  316 */         segment = segment.next();
/*  317 */         if (segment == null) break;
/*  318 */         hasIds = (checkIds) && (segment.hasIds());
/*      */       }
/*  320 */       JsonToken t = segment.type(ptr);
/*  321 */       if (t == null)
/*      */         break;
/*  323 */       if (hasIds) {
/*  324 */         Object id = segment.findObjectId(ptr);
/*  325 */         if (id != null) {
/*  326 */           gen.writeObjectId(id);
/*      */         }
/*  328 */         id = segment.findTypeId(ptr);
/*  329 */         if (id != null) {
/*  330 */           gen.writeTypeId(id);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  335 */       switch (t) {
/*      */       case START_OBJECT: 
/*  337 */         gen.writeStartObject();
/*  338 */         break;
/*      */       case END_OBJECT: 
/*  340 */         gen.writeEndObject();
/*  341 */         break;
/*      */       case START_ARRAY: 
/*  343 */         gen.writeStartArray();
/*  344 */         break;
/*      */       case END_ARRAY: 
/*  346 */         gen.writeEndArray();
/*  347 */         break;
/*      */       
/*      */ 
/*      */       case FIELD_NAME: 
/*  351 */         Object ob = segment.get(ptr);
/*  352 */         if ((ob instanceof SerializableString)) {
/*  353 */           gen.writeFieldName((SerializableString)ob);
/*      */         } else {
/*  355 */           gen.writeFieldName((String)ob);
/*      */         }
/*      */         
/*  358 */         break;
/*      */       
/*      */       case VALUE_STRING: 
/*  361 */         Object ob = segment.get(ptr);
/*  362 */         if ((ob instanceof SerializableString)) {
/*  363 */           gen.writeString((SerializableString)ob);
/*      */         } else {
/*  365 */           gen.writeString((String)ob);
/*      */         }
/*      */         
/*  368 */         break;
/*      */       
/*      */       case VALUE_NUMBER_INT: 
/*  371 */         Object n = segment.get(ptr);
/*  372 */         if ((n instanceof Integer)) {
/*  373 */           gen.writeNumber(((Integer)n).intValue());
/*  374 */         } else if ((n instanceof BigInteger)) {
/*  375 */           gen.writeNumber((BigInteger)n);
/*  376 */         } else if ((n instanceof Long)) {
/*  377 */           gen.writeNumber(((Long)n).longValue());
/*  378 */         } else if ((n instanceof Short)) {
/*  379 */           gen.writeNumber(((Short)n).shortValue());
/*      */         } else {
/*  381 */           gen.writeNumber(((Number)n).intValue());
/*      */         }
/*      */         
/*  384 */         break;
/*      */       
/*      */       case VALUE_NUMBER_FLOAT: 
/*  387 */         Object n = segment.get(ptr);
/*  388 */         if ((n instanceof Double)) {
/*  389 */           gen.writeNumber(((Double)n).doubleValue());
/*  390 */         } else if ((n instanceof BigDecimal)) {
/*  391 */           gen.writeNumber((BigDecimal)n);
/*  392 */         } else if ((n instanceof Float)) {
/*  393 */           gen.writeNumber(((Float)n).floatValue());
/*  394 */         } else if (n == null) {
/*  395 */           gen.writeNull();
/*  396 */         } else if ((n instanceof String)) {
/*  397 */           gen.writeNumber((String)n);
/*      */         } else {
/*  399 */           throw new JsonGenerationException(String.format("Unrecognized value type for VALUE_NUMBER_FLOAT: %s, can not serialize", new Object[] { n.getClass().getName() }), gen);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  404 */         break;
/*      */       case VALUE_TRUE: 
/*  406 */         gen.writeBoolean(true);
/*  407 */         break;
/*      */       case VALUE_FALSE: 
/*  409 */         gen.writeBoolean(false);
/*  410 */         break;
/*      */       case VALUE_NULL: 
/*  412 */         gen.writeNull();
/*  413 */         break;
/*      */       
/*      */       case VALUE_EMBEDDED_OBJECT: 
/*  416 */         Object value = segment.get(ptr);
/*      */         
/*      */ 
/*      */ 
/*  420 */         if ((value instanceof RawValue)) {
/*  421 */           ((RawValue)value).serialize(gen);
/*  422 */         } else if ((value instanceof JsonSerializable)) {
/*  423 */           gen.writeObject(value);
/*      */         } else {
/*  425 */           gen.writeEmbeddedObject(value);
/*      */         }
/*      */         
/*  428 */         break;
/*      */       default: 
/*  430 */         throw new RuntimeException("Internal error: should never end up through this code path");
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TokenBuffer deserialize(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/*  442 */     if (p.getCurrentTokenId() != JsonToken.FIELD_NAME.id()) {
/*  443 */       copyCurrentStructure(p);
/*  444 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  451 */     writeStartObject();
/*      */     JsonToken t;
/*  453 */     do { copyCurrentStructure(p);
/*  454 */     } while ((t = p.nextToken()) == JsonToken.FIELD_NAME);
/*  455 */     if (t != JsonToken.END_OBJECT) {
/*  456 */       ctxt.reportWrongTokenException(p, JsonToken.END_OBJECT, "Expected END_OBJECT after copying contents of a JsonParser into TokenBuffer, got " + t, new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*  460 */     writeEndObject();
/*  461 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  469 */     int MAX_COUNT = 100;
/*      */     
/*  471 */     StringBuilder sb = new StringBuilder();
/*  472 */     sb.append("[TokenBuffer: ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  479 */     JsonParser jp = asParser();
/*  480 */     int count = 0;
/*  481 */     boolean hasNativeIds = (this._hasNativeTypeIds) || (this._hasNativeObjectIds);
/*      */     for (;;)
/*      */     {
/*      */       try
/*      */       {
/*  486 */         JsonToken t = jp.nextToken();
/*  487 */         if (t == null)
/*      */           break;
/*  489 */         if (hasNativeIds) {
/*  490 */           _appendNativeIds(sb);
/*      */         }
/*      */         
/*  493 */         if (count < 100) {
/*  494 */           if (count > 0) {
/*  495 */             sb.append(", ");
/*      */           }
/*  497 */           sb.append(t.toString());
/*  498 */           if (t == JsonToken.FIELD_NAME) {
/*  499 */             sb.append('(');
/*  500 */             sb.append(jp.getCurrentName());
/*  501 */             sb.append(')');
/*      */           }
/*      */         }
/*      */       } catch (IOException ioe) {
/*  505 */         throw new IllegalStateException(ioe);
/*      */       }
/*  507 */       count++;
/*      */     }
/*      */     
/*  510 */     if (count >= 100) {
/*  511 */       sb.append(" ... (truncated ").append(count - 100).append(" entries)");
/*      */     }
/*  513 */     sb.append(']');
/*  514 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private final void _appendNativeIds(StringBuilder sb)
/*      */   {
/*  519 */     Object objectId = this._last.findObjectId(this._appendAt - 1);
/*  520 */     if (objectId != null) {
/*  521 */       sb.append("[objectId=").append(String.valueOf(objectId)).append(']');
/*      */     }
/*  523 */     Object typeId = this._last.findTypeId(this._appendAt - 1);
/*  524 */     if (typeId != null) {
/*  525 */       sb.append("[typeId=").append(String.valueOf(typeId)).append(']');
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator enable(JsonGenerator.Feature f)
/*      */   {
/*  537 */     this._generatorFeatures |= f.getMask();
/*  538 */     return this;
/*      */   }
/*      */   
/*      */   public JsonGenerator disable(JsonGenerator.Feature f)
/*      */   {
/*  543 */     this._generatorFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  544 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/*  551 */     return (this._generatorFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */   public int getFeatureMask()
/*      */   {
/*  556 */     return this._generatorFeatures;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public JsonGenerator setFeatureMask(int mask)
/*      */   {
/*  562 */     this._generatorFeatures = mask;
/*  563 */     return this;
/*      */   }
/*      */   
/*      */   public JsonGenerator overrideStdFeatures(int values, int mask)
/*      */   {
/*  568 */     int oldState = getFeatureMask();
/*  569 */     this._generatorFeatures = (oldState & (mask ^ 0xFFFFFFFF) | values & mask);
/*  570 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public JsonGenerator useDefaultPrettyPrinter()
/*      */   {
/*  576 */     return this;
/*      */   }
/*      */   
/*      */   public JsonGenerator setCodec(ObjectCodec oc)
/*      */   {
/*  581 */     this._objectCodec = oc;
/*  582 */     return this;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec() {
/*  586 */     return this._objectCodec;
/*      */   }
/*      */   
/*  589 */   public final JsonWriteContext getOutputContext() { return this._writeContext; }
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
/*      */   public boolean canWriteBinaryNatively()
/*      */   {
/*  602 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void flush()
/*      */     throws IOException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  616 */     this._closed = true;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*  620 */     return this._closed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException
/*      */   {
/*  631 */     this._writeContext.writeValue();
/*  632 */     _append(JsonToken.START_ARRAY);
/*  633 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */   
/*      */   public final void writeEndArray()
/*      */     throws IOException
/*      */   {
/*  639 */     _append(JsonToken.END_ARRAY);
/*      */     
/*  641 */     JsonWriteContext c = this._writeContext.getParent();
/*  642 */     if (c != null) {
/*  643 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeStartObject()
/*      */     throws IOException
/*      */   {
/*  650 */     this._writeContext.writeValue();
/*  651 */     _append(JsonToken.START_OBJECT);
/*  652 */     this._writeContext = this._writeContext.createChildObjectContext();
/*      */   }
/*      */   
/*      */   public void writeStartObject(Object forValue)
/*      */     throws IOException
/*      */   {
/*  658 */     this._writeContext.writeValue();
/*  659 */     _append(JsonToken.START_OBJECT);
/*  660 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext();
/*  661 */     this._writeContext = ctxt;
/*  662 */     if (forValue != null) {
/*  663 */       ctxt.setCurrentValue(forValue);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeEndObject()
/*      */     throws IOException
/*      */   {
/*  670 */     _append(JsonToken.END_OBJECT);
/*      */     
/*  672 */     JsonWriteContext c = this._writeContext.getParent();
/*  673 */     if (c != null) {
/*  674 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  681 */     this._writeContext.writeFieldName(name);
/*  682 */     _append(JsonToken.FIELD_NAME, name);
/*      */   }
/*      */   
/*      */   public void writeFieldName(SerializableString name)
/*      */     throws IOException
/*      */   {
/*  688 */     this._writeContext.writeFieldName(name.getValue());
/*  689 */     _append(JsonToken.FIELD_NAME, name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException
/*      */   {
/*  700 */     if (text == null) {
/*  701 */       writeNull();
/*      */     } else {
/*  703 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeString(char[] text, int offset, int len) throws IOException
/*      */   {
/*  709 */     writeString(new String(text, offset, len));
/*      */   }
/*      */   
/*      */   public void writeString(SerializableString text) throws IOException
/*      */   {
/*  714 */     if (text == null) {
/*  715 */       writeNull();
/*      */     } else {
/*  717 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  725 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  732 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(String text) throws IOException
/*      */   {
/*  737 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(String text, int offset, int len) throws IOException
/*      */   {
/*  742 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(SerializableString text) throws IOException
/*      */   {
/*  747 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(char[] text, int offset, int len) throws IOException
/*      */   {
/*  752 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRaw(char c) throws IOException
/*      */   {
/*  757 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeRawValue(String text) throws IOException
/*      */   {
/*  762 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */   
/*      */   public void writeRawValue(String text, int offset, int len) throws IOException
/*      */   {
/*  767 */     if ((offset > 0) || (len != text.length())) {
/*  768 */       text = text.substring(offset, offset + len);
/*      */     }
/*  770 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */   
/*      */   public void writeRawValue(char[] text, int offset, int len) throws IOException
/*      */   {
/*  775 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new String(text, offset, len));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeNumber(short i)
/*      */     throws IOException
/*      */   {
/*  786 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Short.valueOf(i));
/*      */   }
/*      */   
/*      */   public void writeNumber(int i) throws IOException
/*      */   {
/*  791 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
/*      */   }
/*      */   
/*      */   public void writeNumber(long l) throws IOException
/*      */   {
/*  796 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
/*      */   }
/*      */   
/*      */   public void writeNumber(double d) throws IOException
/*      */   {
/*  801 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
/*      */   }
/*      */   
/*      */   public void writeNumber(float f) throws IOException
/*      */   {
/*  806 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
/*      */   }
/*      */   
/*      */   public void writeNumber(BigDecimal dec) throws IOException
/*      */   {
/*  811 */     if (dec == null) {
/*  812 */       writeNull();
/*      */     } else {
/*  814 */       _appendValue(JsonToken.VALUE_NUMBER_FLOAT, dec);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeNumber(BigInteger v) throws IOException
/*      */   {
/*  820 */     if (v == null) {
/*  821 */       writeNull();
/*      */     } else {
/*  823 */       _appendValue(JsonToken.VALUE_NUMBER_INT, v);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException
/*      */   {
/*  832 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
/*      */   }
/*      */   
/*      */   public void writeBoolean(boolean state) throws IOException
/*      */   {
/*  837 */     _appendValue(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
/*      */   }
/*      */   
/*      */   public void writeNull() throws IOException
/*      */   {
/*  842 */     _appendValue(JsonToken.VALUE_NULL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeObject(Object value)
/*      */     throws IOException
/*      */   {
/*  854 */     if (value == null) {
/*  855 */       writeNull();
/*  856 */       return;
/*      */     }
/*  858 */     Class<?> raw = value.getClass();
/*  859 */     if ((raw == byte[].class) || ((value instanceof RawValue))) {
/*  860 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*  861 */       return;
/*      */     }
/*  863 */     if (this._objectCodec == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  868 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */     } else {
/*  870 */       this._objectCodec.writeValue(this, value);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeTree(TreeNode node)
/*      */     throws IOException
/*      */   {
/*  877 */     if (node == null) {
/*  878 */       writeNull();
/*  879 */       return;
/*      */     }
/*      */     
/*  882 */     if (this._objectCodec == null)
/*      */     {
/*  884 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, node);
/*      */     } else {
/*  886 */       this._objectCodec.writeTree(this, node);
/*      */     }
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
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  905 */     byte[] copy = new byte[len];
/*  906 */     System.arraycopy(data, offset, copy, 0, len);
/*  907 */     writeObject(copy);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*      */   {
/*  918 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canWriteTypeId()
/*      */   {
/*  929 */     return this._hasNativeTypeIds;
/*      */   }
/*      */   
/*      */   public boolean canWriteObjectId()
/*      */   {
/*  934 */     return this._hasNativeObjectIds;
/*      */   }
/*      */   
/*      */   public void writeTypeId(Object id)
/*      */   {
/*  939 */     this._typeId = id;
/*  940 */     this._hasNativeId = true;
/*      */   }
/*      */   
/*      */   public void writeObjectId(Object id)
/*      */   {
/*  945 */     this._objectId = id;
/*  946 */     this._hasNativeId = true;
/*      */   }
/*      */   
/*      */   public void writeEmbeddedObject(Object object) throws IOException
/*      */   {
/*  951 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, object);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyCurrentEvent(JsonParser p)
/*      */     throws IOException
/*      */   {
/*  963 */     if (this._mayHaveNativeIds) {
/*  964 */       _checkNativeIds(p);
/*      */     }
/*  966 */     switch (p.getCurrentToken()) {
/*      */     case START_OBJECT: 
/*  968 */       writeStartObject();
/*  969 */       break;
/*      */     case END_OBJECT: 
/*  971 */       writeEndObject();
/*  972 */       break;
/*      */     case START_ARRAY: 
/*  974 */       writeStartArray();
/*  975 */       break;
/*      */     case END_ARRAY: 
/*  977 */       writeEndArray();
/*  978 */       break;
/*      */     case FIELD_NAME: 
/*  980 */       writeFieldName(p.getCurrentName());
/*  981 */       break;
/*      */     case VALUE_STRING: 
/*  983 */       if (p.hasTextCharacters()) {
/*  984 */         writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */       } else {
/*  986 */         writeString(p.getText());
/*      */       }
/*  988 */       break;
/*      */     case VALUE_NUMBER_INT: 
/*  990 */       switch (p.getNumberType()) {
/*      */       case INT: 
/*  992 */         writeNumber(p.getIntValue());
/*  993 */         break;
/*      */       case BIG_INTEGER: 
/*  995 */         writeNumber(p.getBigIntegerValue());
/*  996 */         break;
/*      */       default: 
/*  998 */         writeNumber(p.getLongValue());
/*      */       }
/* 1000 */       break;
/*      */     case VALUE_NUMBER_FLOAT: 
/* 1002 */       if (this._forceBigDecimal)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1008 */         writeNumber(p.getDecimalValue());
/*      */       } else {
/* 1010 */         switch (p.getNumberType()) {
/*      */         case BIG_DECIMAL: 
/* 1012 */           writeNumber(p.getDecimalValue());
/* 1013 */           break;
/*      */         case FLOAT: 
/* 1015 */           writeNumber(p.getFloatValue());
/* 1016 */           break;
/*      */         default: 
/* 1018 */           writeNumber(p.getDoubleValue());
/*      */         }
/*      */       }
/* 1021 */       break;
/*      */     case VALUE_TRUE: 
/* 1023 */       writeBoolean(true);
/* 1024 */       break;
/*      */     case VALUE_FALSE: 
/* 1026 */       writeBoolean(false);
/* 1027 */       break;
/*      */     case VALUE_NULL: 
/* 1029 */       writeNull();
/* 1030 */       break;
/*      */     case VALUE_EMBEDDED_OBJECT: 
/* 1032 */       writeObject(p.getEmbeddedObject());
/* 1033 */       break;
/*      */     default: 
/* 1035 */       throw new RuntimeException("Internal error: should never end up through this code path");
/*      */     }
/*      */   }
/*      */   
/*      */   public void copyCurrentStructure(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1042 */     JsonToken t = p.getCurrentToken();
/*      */     
/*      */ 
/* 1045 */     if (t == JsonToken.FIELD_NAME) {
/* 1046 */       if (this._mayHaveNativeIds) {
/* 1047 */         _checkNativeIds(p);
/*      */       }
/* 1049 */       writeFieldName(p.getCurrentName());
/* 1050 */       t = p.nextToken();
/*      */     }
/*      */     
/*      */ 
/* 1054 */     if (this._mayHaveNativeIds) {
/* 1055 */       _checkNativeIds(p);
/*      */     }
/*      */     
/* 1058 */     switch (t) {
/*      */     case START_ARRAY: 
/* 1060 */       writeStartArray();
/* 1061 */       while (p.nextToken() != JsonToken.END_ARRAY) {
/* 1062 */         copyCurrentStructure(p);
/*      */       }
/* 1064 */       writeEndArray();
/* 1065 */       break;
/*      */     case START_OBJECT: 
/* 1067 */       writeStartObject();
/* 1068 */       while (p.nextToken() != JsonToken.END_OBJECT) {
/* 1069 */         copyCurrentStructure(p);
/*      */       }
/* 1071 */       writeEndObject();
/* 1072 */       break;
/*      */     default: 
/* 1074 */       copyCurrentEvent(p);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _checkNativeIds(JsonParser jp)
/*      */     throws IOException
/*      */   {
/* 1081 */     if ((this._typeId = jp.getTypeId()) != null) {
/* 1082 */       this._hasNativeId = true;
/*      */     }
/* 1084 */     if ((this._objectId = jp.getObjectId()) != null) {
/* 1085 */       this._hasNativeId = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _append(JsonToken type)
/*      */   {
/* 1097 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, this._objectId, this._typeId) : this._last.append(this._appendAt, type);
/*      */     
/*      */ 
/* 1100 */     if (next == null) {
/* 1101 */       this._appendAt += 1;
/*      */     } else {
/* 1103 */       this._last = next;
/* 1104 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void _append(JsonToken type, Object value)
/*      */   {
/* 1110 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, value, this._objectId, this._typeId) : this._last.append(this._appendAt, type, value);
/*      */     
/*      */ 
/* 1113 */     if (next == null) {
/* 1114 */       this._appendAt += 1;
/*      */     } else {
/* 1116 */       this._last = next;
/* 1117 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _appendValue(JsonToken type)
/*      */   {
/* 1129 */     this._writeContext.writeValue();
/* 1130 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, this._objectId, this._typeId) : this._last.append(this._appendAt, type);
/*      */     
/*      */ 
/* 1133 */     if (next == null) {
/* 1134 */       this._appendAt += 1;
/*      */     } else {
/* 1136 */       this._last = next;
/* 1137 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _appendValue(JsonToken type, Object value)
/*      */   {
/* 1149 */     this._writeContext.writeValue();
/* 1150 */     Segment next = this._hasNativeId ? this._last.append(this._appendAt, type, value, this._objectId, this._typeId) : this._last.append(this._appendAt, type, value);
/*      */     
/*      */ 
/* 1153 */     if (next == null) {
/* 1154 */       this._appendAt += 1;
/*      */     } else {
/* 1156 */       this._last = next;
/* 1157 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected final void _appendRaw(int rawType, Object value)
/*      */   {
/* 1163 */     Segment next = this._hasNativeId ? this._last.appendRaw(this._appendAt, rawType, value, this._objectId, this._typeId) : this._last.appendRaw(this._appendAt, rawType, value);
/*      */     
/*      */ 
/* 1166 */     if (next == null) {
/* 1167 */       this._appendAt += 1;
/*      */     } else {
/* 1169 */       this._last = next;
/* 1170 */       this._appendAt = 1;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _reportUnsupportedOperation()
/*      */   {
/* 1176 */     throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final class Parser
/*      */     extends ParserMinimalBase
/*      */   {
/*      */     protected ObjectCodec _codec;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeTypeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeObjectIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final boolean _hasNativeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected TokenBuffer.Segment _segment;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int _segmentPtr;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected JsonReadContext _parsingContext;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean _closed;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected transient ByteArrayBuilder _byteBuilder;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1234 */     protected JsonLocation _location = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds)
/*      */     {
/* 1246 */       super();
/* 1247 */       this._segment = firstSeg;
/* 1248 */       this._segmentPtr = -1;
/* 1249 */       this._codec = codec;
/* 1250 */       this._parsingContext = JsonReadContext.createRootContext(null);
/* 1251 */       this._hasNativeTypeIds = hasNativeTypeIds;
/* 1252 */       this._hasNativeObjectIds = hasNativeObjectIds;
/* 1253 */       this._hasNativeIds = (hasNativeTypeIds | hasNativeObjectIds);
/*      */     }
/*      */     
/*      */     public void setLocation(JsonLocation l) {
/* 1257 */       this._location = l;
/*      */     }
/*      */     
/*      */     public ObjectCodec getCodec() {
/* 1261 */       return this._codec;
/*      */     }
/*      */     
/* 1264 */     public void setCodec(ObjectCodec c) { this._codec = c; }
/*      */     
/*      */     public Version version()
/*      */     {
/* 1268 */       return PackageVersion.VERSION;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken peekNextToken()
/*      */       throws IOException
/*      */     {
/* 1280 */       if (this._closed) return null;
/* 1281 */       TokenBuffer.Segment seg = this._segment;
/* 1282 */       int ptr = this._segmentPtr + 1;
/* 1283 */       if (ptr >= 16) {
/* 1284 */         ptr = 0;
/* 1285 */         seg = seg == null ? null : seg.next();
/*      */       }
/* 1287 */       return seg == null ? null : seg.type(ptr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/* 1298 */       if (!this._closed) {
/* 1299 */         this._closed = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken nextToken()
/*      */       throws IOException
/*      */     {
/* 1313 */       if ((this._closed) || (this._segment == null)) { return null;
/*      */       }
/*      */       
/* 1316 */       if (++this._segmentPtr >= 16) {
/* 1317 */         this._segmentPtr = 0;
/* 1318 */         this._segment = this._segment.next();
/* 1319 */         if (this._segment == null) {
/* 1320 */           return null;
/*      */         }
/*      */       }
/* 1323 */       this._currToken = this._segment.type(this._segmentPtr);
/*      */       
/* 1325 */       if (this._currToken == JsonToken.FIELD_NAME) {
/* 1326 */         Object ob = _currentObject();
/* 1327 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1328 */         this._parsingContext.setCurrentName(name);
/* 1329 */       } else if (this._currToken == JsonToken.START_OBJECT) {
/* 1330 */         this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
/* 1331 */       } else if (this._currToken == JsonToken.START_ARRAY) {
/* 1332 */         this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
/* 1333 */       } else if ((this._currToken == JsonToken.END_OBJECT) || (this._currToken == JsonToken.END_ARRAY))
/*      */       {
/*      */ 
/* 1336 */         this._parsingContext = this._parsingContext.getParent();
/*      */         
/* 1338 */         if (this._parsingContext == null) {
/* 1339 */           this._parsingContext = JsonReadContext.createRootContext(null);
/*      */         }
/*      */       }
/* 1342 */       return this._currToken;
/*      */     }
/*      */     
/*      */ 
/*      */     public String nextFieldName()
/*      */       throws IOException
/*      */     {
/* 1349 */       if ((this._closed) || (this._segment == null)) { return null;
/*      */       }
/* 1351 */       int ptr = this._segmentPtr + 1;
/* 1352 */       if ((ptr < 16) && (this._segment.type(ptr) == JsonToken.FIELD_NAME)) {
/* 1353 */         this._segmentPtr = ptr;
/* 1354 */         Object ob = this._segment.get(ptr);
/* 1355 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1356 */         this._parsingContext.setCurrentName(name);
/* 1357 */         return name;
/*      */       }
/* 1359 */       return nextToken() == JsonToken.FIELD_NAME ? getCurrentName() : null;
/*      */     }
/*      */     
/*      */     public boolean isClosed() {
/* 1363 */       return this._closed;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public com.fasterxml.jackson.core.JsonStreamContext getParsingContext()
/*      */     {
/* 1372 */       return this._parsingContext;
/*      */     }
/*      */     
/* 1375 */     public JsonLocation getTokenLocation() { return getCurrentLocation(); }
/*      */     
/*      */     public JsonLocation getCurrentLocation()
/*      */     {
/* 1379 */       return this._location == null ? JsonLocation.NA : this._location;
/*      */     }
/*      */     
/*      */ 
/*      */     public String getCurrentName()
/*      */     {
/* 1385 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 1386 */         JsonReadContext parent = this._parsingContext.getParent();
/* 1387 */         return parent.getCurrentName();
/*      */       }
/* 1389 */       return this._parsingContext.getCurrentName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void overrideCurrentName(String name)
/*      */     {
/* 1396 */       JsonReadContext ctxt = this._parsingContext;
/* 1397 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 1398 */         ctxt = ctxt.getParent();
/*      */       }
/*      */       try {
/* 1401 */         ctxt.setCurrentName(name);
/*      */       } catch (IOException e) {
/* 1403 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getText()
/*      */     {
/* 1417 */       if ((this._currToken == JsonToken.VALUE_STRING) || (this._currToken == JsonToken.FIELD_NAME))
/*      */       {
/* 1419 */         Object ob = _currentObject();
/* 1420 */         if ((ob instanceof String)) {
/* 1421 */           return (String)ob;
/*      */         }
/* 1423 */         return ob == null ? null : ob.toString();
/*      */       }
/* 1425 */       if (this._currToken == null) {
/* 1426 */         return null;
/*      */       }
/* 1428 */       switch (TokenBuffer.1.$SwitchMap$com$fasterxml$jackson$core$JsonToken[this._currToken.ordinal()]) {
/*      */       case 7: 
/*      */       case 8: 
/* 1431 */         Object ob = _currentObject();
/* 1432 */         return ob == null ? null : ob.toString();
/*      */       }
/* 1434 */       return this._currToken.asString();
/*      */     }
/*      */     
/*      */ 
/*      */     public char[] getTextCharacters()
/*      */     {
/* 1440 */       String str = getText();
/* 1441 */       return str == null ? null : str.toCharArray();
/*      */     }
/*      */     
/*      */     public int getTextLength()
/*      */     {
/* 1446 */       String str = getText();
/* 1447 */       return str == null ? 0 : str.length();
/*      */     }
/*      */     
/*      */     public int getTextOffset() {
/* 1451 */       return 0;
/*      */     }
/*      */     
/*      */     public boolean hasTextCharacters()
/*      */     {
/* 1456 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public BigInteger getBigIntegerValue()
/*      */       throws IOException
/*      */     {
/* 1468 */       Number n = getNumberValue();
/* 1469 */       if ((n instanceof BigInteger)) {
/* 1470 */         return (BigInteger)n;
/*      */       }
/* 1472 */       if (getNumberType() == JsonParser.NumberType.BIG_DECIMAL) {
/* 1473 */         return ((BigDecimal)n).toBigInteger();
/*      */       }
/*      */       
/* 1476 */       return BigInteger.valueOf(n.longValue());
/*      */     }
/*      */     
/*      */     public BigDecimal getDecimalValue()
/*      */       throws IOException
/*      */     {
/* 1482 */       Number n = getNumberValue();
/* 1483 */       if ((n instanceof BigDecimal)) {
/* 1484 */         return (BigDecimal)n;
/*      */       }
/* 1486 */       switch (TokenBuffer.1.$SwitchMap$com$fasterxml$jackson$core$JsonParser$NumberType[getNumberType().ordinal()]) {
/*      */       case 1: 
/*      */       case 5: 
/* 1489 */         return BigDecimal.valueOf(n.longValue());
/*      */       case 2: 
/* 1491 */         return new BigDecimal((BigInteger)n);
/*      */       }
/*      */       
/*      */       
/* 1495 */       return BigDecimal.valueOf(n.doubleValue());
/*      */     }
/*      */     
/*      */     public double getDoubleValue() throws IOException
/*      */     {
/* 1500 */       return getNumberValue().doubleValue();
/*      */     }
/*      */     
/*      */     public float getFloatValue() throws IOException
/*      */     {
/* 1505 */       return getNumberValue().floatValue();
/*      */     }
/*      */     
/*      */ 
/*      */     public int getIntValue()
/*      */       throws IOException
/*      */     {
/* 1512 */       if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/* 1513 */         return ((Number)_currentObject()).intValue();
/*      */       }
/* 1515 */       return getNumberValue().intValue();
/*      */     }
/*      */     
/*      */     public long getLongValue() throws IOException
/*      */     {
/* 1520 */       return getNumberValue().longValue();
/*      */     }
/*      */     
/*      */     public JsonParser.NumberType getNumberType()
/*      */       throws IOException
/*      */     {
/* 1526 */       Number n = getNumberValue();
/* 1527 */       if ((n instanceof Integer)) return JsonParser.NumberType.INT;
/* 1528 */       if ((n instanceof Long)) return JsonParser.NumberType.LONG;
/* 1529 */       if ((n instanceof Double)) return JsonParser.NumberType.DOUBLE;
/* 1530 */       if ((n instanceof BigDecimal)) return JsonParser.NumberType.BIG_DECIMAL;
/* 1531 */       if ((n instanceof BigInteger)) return JsonParser.NumberType.BIG_INTEGER;
/* 1532 */       if ((n instanceof Float)) return JsonParser.NumberType.FLOAT;
/* 1533 */       if ((n instanceof Short)) return JsonParser.NumberType.INT;
/* 1534 */       return null;
/*      */     }
/*      */     
/*      */     public final Number getNumberValue() throws IOException
/*      */     {
/* 1539 */       _checkIsNumber();
/* 1540 */       Object value = _currentObject();
/* 1541 */       if ((value instanceof Number)) {
/* 1542 */         return (Number)value;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1547 */       if ((value instanceof String)) {
/* 1548 */         String str = (String)value;
/* 1549 */         if (str.indexOf('.') >= 0) {
/* 1550 */           return Double.valueOf(Double.parseDouble(str));
/*      */         }
/* 1552 */         return Long.valueOf(Long.parseLong(str));
/*      */       }
/* 1554 */       if (value == null) {
/* 1555 */         return null;
/*      */       }
/* 1557 */       throw new IllegalStateException("Internal error: entry should be a Number, but is of type " + value.getClass().getName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getEmbeddedObject()
/*      */     {
/* 1570 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 1571 */         return _currentObject();
/*      */       }
/* 1573 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public byte[] getBinaryValue(Base64Variant b64variant)
/*      */       throws IOException, JsonParseException
/*      */     {
/* 1581 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT)
/*      */       {
/* 1583 */         Object ob = _currentObject();
/* 1584 */         if ((ob instanceof byte[])) {
/* 1585 */           return (byte[])ob;
/*      */         }
/*      */       }
/*      */       
/* 1589 */       if (this._currToken != JsonToken.VALUE_STRING) {
/* 1590 */         throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), can not access as binary");
/*      */       }
/* 1592 */       String str = getText();
/* 1593 */       if (str == null) {
/* 1594 */         return null;
/*      */       }
/* 1596 */       ByteArrayBuilder builder = this._byteBuilder;
/* 1597 */       if (builder == null) {
/* 1598 */         this._byteBuilder = (builder = new ByteArrayBuilder(100));
/*      */       } else {
/* 1600 */         this._byteBuilder.reset();
/*      */       }
/* 1602 */       _decodeBase64(str, builder, b64variant);
/* 1603 */       return builder.toByteArray();
/*      */     }
/*      */     
/*      */     public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */       throws IOException
/*      */     {
/* 1609 */       byte[] data = getBinaryValue(b64variant);
/* 1610 */       if (data != null) {
/* 1611 */         out.write(data, 0, data.length);
/* 1612 */         return data.length;
/*      */       }
/* 1614 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean canReadObjectId()
/*      */     {
/* 1625 */       return this._hasNativeObjectIds;
/*      */     }
/*      */     
/*      */     public boolean canReadTypeId()
/*      */     {
/* 1630 */       return this._hasNativeTypeIds;
/*      */     }
/*      */     
/*      */     public Object getTypeId()
/*      */     {
/* 1635 */       return this._segment.findTypeId(this._segmentPtr);
/*      */     }
/*      */     
/*      */     public Object getObjectId()
/*      */     {
/* 1640 */       return this._segment.findObjectId(this._segmentPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final Object _currentObject()
/*      */     {
/* 1650 */       return this._segment.get(this._segmentPtr);
/*      */     }
/*      */     
/*      */     protected final void _checkIsNumber() throws JsonParseException
/*      */     {
/* 1655 */       if ((this._currToken == null) || (!this._currToken.isNumeric())) {
/* 1656 */         throw _constructError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
/*      */       }
/*      */     }
/*      */     
/*      */     protected void _handleEOF() throws JsonParseException
/*      */     {
/* 1662 */       _throwInternal();
/*      */     }
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
/*      */   protected static final class Segment
/*      */   {
/*      */     public static final int TOKENS_PER_SEGMENT = 16;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1684 */     private static final JsonToken[] TOKEN_TYPES_BY_INDEX = new JsonToken[16];
/* 1685 */     static { JsonToken[] t = JsonToken.values();
/*      */       
/* 1687 */       System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, t.length - 1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Segment _next;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected long _tokenTypes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1705 */     protected final Object[] _tokens = new Object[16];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected TreeMap<Integer, Object> _nativeIds;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JsonToken type(int index)
/*      */     {
/* 1718 */       long l = this._tokenTypes;
/* 1719 */       if (index > 0) {
/* 1720 */         l >>= index << 2;
/*      */       }
/* 1722 */       int ix = (int)l & 0xF;
/* 1723 */       return TOKEN_TYPES_BY_INDEX[ix];
/*      */     }
/*      */     
/*      */     public int rawType(int index)
/*      */     {
/* 1728 */       long l = this._tokenTypes;
/* 1729 */       if (index > 0) {
/* 1730 */         l >>= index << 2;
/*      */       }
/* 1732 */       int ix = (int)l & 0xF;
/* 1733 */       return ix;
/*      */     }
/*      */     
/*      */     public Object get(int index) {
/* 1737 */       return this._tokens[index];
/*      */     }
/*      */     
/* 1740 */     public Segment next() { return this._next; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasIds()
/*      */     {
/* 1747 */       return this._nativeIds != null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType)
/*      */     {
/* 1754 */       if (index < 16) {
/* 1755 */         set(index, tokenType);
/* 1756 */         return null;
/*      */       }
/* 1758 */       this._next = new Segment();
/* 1759 */       this._next.set(0, tokenType);
/* 1760 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType, Object objectId, Object typeId)
/*      */     {
/* 1766 */       if (index < 16) {
/* 1767 */         set(index, tokenType, objectId, typeId);
/* 1768 */         return null;
/*      */       }
/* 1770 */       this._next = new Segment();
/* 1771 */       this._next.set(0, tokenType, objectId, typeId);
/* 1772 */       return this._next;
/*      */     }
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value)
/*      */     {
/* 1777 */       if (index < 16) {
/* 1778 */         set(index, tokenType, value);
/* 1779 */         return null;
/*      */       }
/* 1781 */       this._next = new Segment();
/* 1782 */       this._next.set(0, tokenType, value);
/* 1783 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment append(int index, JsonToken tokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1789 */       if (index < 16) {
/* 1790 */         set(index, tokenType, value, objectId, typeId);
/* 1791 */         return null;
/*      */       }
/* 1793 */       this._next = new Segment();
/* 1794 */       this._next.set(0, tokenType, value, objectId, typeId);
/* 1795 */       return this._next;
/*      */     }
/*      */     
/*      */     public Segment appendRaw(int index, int rawTokenType, Object value)
/*      */     {
/* 1800 */       if (index < 16) {
/* 1801 */         set(index, rawTokenType, value);
/* 1802 */         return null;
/*      */       }
/* 1804 */       this._next = new Segment();
/* 1805 */       this._next.set(0, rawTokenType, value);
/* 1806 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */     public Segment appendRaw(int index, int rawTokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1812 */       if (index < 16) {
/* 1813 */         set(index, rawTokenType, value, objectId, typeId);
/* 1814 */         return null;
/*      */       }
/* 1816 */       this._next = new Segment();
/* 1817 */       this._next.set(0, rawTokenType, value, objectId, typeId);
/* 1818 */       return this._next;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void set(int index, JsonToken tokenType)
/*      */     {
/* 1826 */       long typeCode = tokenType.ordinal();
/* 1827 */       if (index > 0) {
/* 1828 */         typeCode <<= index << 2;
/*      */       }
/* 1830 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */ 
/*      */     private void set(int index, JsonToken tokenType, Object objectId, Object typeId)
/*      */     {
/* 1836 */       long typeCode = tokenType.ordinal();
/* 1837 */       if (index > 0) {
/* 1838 */         typeCode <<= index << 2;
/*      */       }
/* 1840 */       this._tokenTypes |= typeCode;
/* 1841 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value)
/*      */     {
/* 1846 */       this._tokens[index] = value;
/* 1847 */       long typeCode = tokenType.ordinal();
/* 1848 */       if (index > 0) {
/* 1849 */         typeCode <<= index << 2;
/*      */       }
/* 1851 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */ 
/*      */     private void set(int index, JsonToken tokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1857 */       this._tokens[index] = value;
/* 1858 */       long typeCode = tokenType.ordinal();
/* 1859 */       if (index > 0) {
/* 1860 */         typeCode <<= index << 2;
/*      */       }
/* 1862 */       this._tokenTypes |= typeCode;
/* 1863 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private void set(int index, int rawTokenType, Object value)
/*      */     {
/* 1868 */       this._tokens[index] = value;
/* 1869 */       long typeCode = rawTokenType;
/* 1870 */       if (index > 0) {
/* 1871 */         typeCode <<= index << 2;
/*      */       }
/* 1873 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */     
/*      */     private void set(int index, int rawTokenType, Object value, Object objectId, Object typeId)
/*      */     {
/* 1878 */       this._tokens[index] = value;
/* 1879 */       long typeCode = rawTokenType;
/* 1880 */       if (index > 0) {
/* 1881 */         typeCode <<= index << 2;
/*      */       }
/* 1883 */       this._tokenTypes |= typeCode;
/* 1884 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */     
/*      */     private final void assignNativeIds(int index, Object objectId, Object typeId)
/*      */     {
/* 1889 */       if (this._nativeIds == null) {
/* 1890 */         this._nativeIds = new TreeMap();
/*      */       }
/* 1892 */       if (objectId != null) {
/* 1893 */         this._nativeIds.put(Integer.valueOf(_objectIdIndex(index)), objectId);
/*      */       }
/* 1895 */       if (typeId != null) {
/* 1896 */         this._nativeIds.put(Integer.valueOf(_typeIdIndex(index)), typeId);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Object findObjectId(int index)
/*      */     {
/* 1904 */       return this._nativeIds == null ? null : this._nativeIds.get(Integer.valueOf(_objectIdIndex(index)));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Object findTypeId(int index)
/*      */     {
/* 1911 */       return this._nativeIds == null ? null : this._nativeIds.get(Integer.valueOf(_typeIdIndex(index)));
/*      */     }
/*      */     
/* 1914 */     private final int _typeIdIndex(int i) { return i + i; }
/* 1915 */     private final int _objectIdIndex(int i) { return i + i + 1; }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\TokenBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */