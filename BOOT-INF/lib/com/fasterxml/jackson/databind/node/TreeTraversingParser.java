/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public class TreeTraversingParser
/*     */   extends ParserMinimalBase
/*     */ {
/*     */   protected ObjectCodec _objectCodec;
/*     */   protected NodeCursor _nodeCursor;
/*     */   protected JsonToken _nextToken;
/*     */   protected boolean _startContainer;
/*     */   protected boolean _closed;
/*     */   
/*     */   public TreeTraversingParser(JsonNode n)
/*     */   {
/*  65 */     this(n, null);
/*     */   }
/*     */   
/*     */   public TreeTraversingParser(JsonNode n, ObjectCodec codec) {
/*  69 */     super(0);
/*  70 */     this._objectCodec = codec;
/*  71 */     if (n.isArray()) {
/*  72 */       this._nextToken = JsonToken.START_ARRAY;
/*  73 */       this._nodeCursor = new NodeCursor.ArrayCursor(n, null);
/*  74 */     } else if (n.isObject()) {
/*  75 */       this._nextToken = JsonToken.START_OBJECT;
/*  76 */       this._nodeCursor = new NodeCursor.ObjectCursor(n, null);
/*     */     } else {
/*  78 */       this._nodeCursor = new NodeCursor.RootCursor(n, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCodec(ObjectCodec c)
/*     */   {
/*  84 */     this._objectCodec = c;
/*     */   }
/*     */   
/*     */   public ObjectCodec getCodec()
/*     */   {
/*  89 */     return this._objectCodec;
/*     */   }
/*     */   
/*     */   public Version version()
/*     */   {
/*  94 */     return PackageVersion.VERSION;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 106 */     if (!this._closed) {
/* 107 */       this._closed = true;
/* 108 */       this._nodeCursor = null;
/* 109 */       this._currToken = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken nextToken()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 122 */     if (this._nextToken != null) {
/* 123 */       this._currToken = this._nextToken;
/* 124 */       this._nextToken = null;
/* 125 */       return this._currToken;
/*     */     }
/*     */     
/* 128 */     if (this._startContainer) {
/* 129 */       this._startContainer = false;
/*     */       
/* 131 */       if (!this._nodeCursor.currentHasChildren()) {
/* 132 */         this._currToken = (this._currToken == JsonToken.START_OBJECT ? JsonToken.END_OBJECT : JsonToken.END_ARRAY);
/*     */         
/* 134 */         return this._currToken;
/*     */       }
/* 136 */       this._nodeCursor = this._nodeCursor.iterateChildren();
/* 137 */       this._currToken = this._nodeCursor.nextToken();
/* 138 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 139 */         this._startContainer = true;
/*     */       }
/* 141 */       return this._currToken;
/*     */     }
/*     */     
/* 144 */     if (this._nodeCursor == null) {
/* 145 */       this._closed = true;
/* 146 */       return null;
/*     */     }
/*     */     
/* 149 */     this._currToken = this._nodeCursor.nextToken();
/* 150 */     if (this._currToken != null) {
/* 151 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 152 */         this._startContainer = true;
/*     */       }
/* 154 */       return this._currToken;
/*     */     }
/*     */     
/* 157 */     this._currToken = this._nodeCursor.endToken();
/* 158 */     this._nodeCursor = this._nodeCursor.getParent();
/* 159 */     return this._currToken;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParser skipChildren()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 168 */     if (this._currToken == JsonToken.START_OBJECT) {
/* 169 */       this._startContainer = false;
/* 170 */       this._currToken = JsonToken.END_OBJECT;
/* 171 */     } else if (this._currToken == JsonToken.START_ARRAY) {
/* 172 */       this._startContainer = false;
/* 173 */       this._currToken = JsonToken.END_ARRAY;
/*     */     }
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isClosed()
/*     */   {
/* 180 */     return this._closed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCurrentName()
/*     */   {
/* 191 */     return this._nodeCursor == null ? null : this._nodeCursor.getCurrentName();
/*     */   }
/*     */   
/*     */ 
/*     */   public void overrideCurrentName(String name)
/*     */   {
/* 197 */     if (this._nodeCursor != null) {
/* 198 */       this._nodeCursor.overrideCurrentName(name);
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonStreamContext getParsingContext()
/*     */   {
/* 204 */     return this._nodeCursor;
/*     */   }
/*     */   
/*     */   public JsonLocation getTokenLocation()
/*     */   {
/* 209 */     return JsonLocation.NA;
/*     */   }
/*     */   
/*     */   public JsonLocation getCurrentLocation()
/*     */   {
/* 214 */     return JsonLocation.NA;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getText()
/*     */   {
/* 226 */     if (this._closed) {
/* 227 */       return null;
/*     */     }
/*     */     
/* 230 */     switch (this._currToken) {
/*     */     case FIELD_NAME: 
/* 232 */       return this._nodeCursor.getCurrentName();
/*     */     case VALUE_STRING: 
/* 234 */       return currentNode().textValue();
/*     */     case VALUE_NUMBER_INT: 
/*     */     case VALUE_NUMBER_FLOAT: 
/* 237 */       return String.valueOf(currentNode().numberValue());
/*     */     case VALUE_EMBEDDED_OBJECT: 
/* 239 */       JsonNode n = currentNode();
/* 240 */       if ((n != null) && (n.isBinary()))
/*     */       {
/* 242 */         return n.asText(); }
/*     */       break;
/*     */     }
/* 245 */     return this._currToken == null ? null : this._currToken.asString();
/*     */   }
/*     */   
/*     */   public char[] getTextCharacters()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 251 */     return getText().toCharArray();
/*     */   }
/*     */   
/*     */   public int getTextLength() throws IOException, JsonParseException
/*     */   {
/* 256 */     return getText().length();
/*     */   }
/*     */   
/*     */   public int getTextOffset() throws IOException, JsonParseException
/*     */   {
/* 261 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasTextCharacters()
/*     */   {
/* 267 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParser.NumberType getNumberType()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 280 */     JsonNode n = currentNumericNode();
/* 281 */     return n == null ? null : n.numberType();
/*     */   }
/*     */   
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 287 */     return currentNumericNode().bigIntegerValue();
/*     */   }
/*     */   
/*     */   public BigDecimal getDecimalValue() throws IOException, JsonParseException
/*     */   {
/* 292 */     return currentNumericNode().decimalValue();
/*     */   }
/*     */   
/*     */   public double getDoubleValue() throws IOException, JsonParseException
/*     */   {
/* 297 */     return currentNumericNode().doubleValue();
/*     */   }
/*     */   
/*     */   public float getFloatValue() throws IOException, JsonParseException
/*     */   {
/* 302 */     return (float)currentNumericNode().doubleValue();
/*     */   }
/*     */   
/*     */   public long getLongValue() throws IOException, JsonParseException
/*     */   {
/* 307 */     return currentNumericNode().longValue();
/*     */   }
/*     */   
/*     */   public int getIntValue() throws IOException, JsonParseException
/*     */   {
/* 312 */     return currentNumericNode().intValue();
/*     */   }
/*     */   
/*     */   public Number getNumberValue() throws IOException, JsonParseException
/*     */   {
/* 317 */     return currentNumericNode().numberValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getEmbeddedObject()
/*     */   {
/* 323 */     if (!this._closed) {
/* 324 */       JsonNode n = currentNode();
/* 325 */       if (n != null) {
/* 326 */         if (n.isPojo()) {
/* 327 */           return ((POJONode)n).getPojo();
/*     */         }
/* 329 */         if (n.isBinary()) {
/* 330 */           return ((BinaryNode)n).binaryValue();
/*     */         }
/*     */       }
/*     */     }
/* 334 */     return null;
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
/*     */   public byte[] getBinaryValue(Base64Variant b64variant)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 348 */     JsonNode n = currentNode();
/* 349 */     if (n != null) {
/* 350 */       byte[] data = n.binaryValue();
/*     */       
/* 352 */       if (data != null) {
/* 353 */         return data;
/*     */       }
/*     */       
/* 356 */       if (n.isPojo()) {
/* 357 */         Object ob = ((POJONode)n).getPojo();
/* 358 */         if ((ob instanceof byte[])) {
/* 359 */           return (byte[])ob;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 364 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 372 */     byte[] data = getBinaryValue(b64variant);
/* 373 */     if (data != null) {
/* 374 */       out.write(data, 0, data.length);
/* 375 */       return data.length;
/*     */     }
/* 377 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonNode currentNode()
/*     */   {
/* 387 */     if ((this._closed) || (this._nodeCursor == null)) {
/* 388 */       return null;
/*     */     }
/* 390 */     return this._nodeCursor.currentNode();
/*     */   }
/*     */   
/*     */   protected JsonNode currentNumericNode()
/*     */     throws JsonParseException
/*     */   {
/* 396 */     JsonNode n = currentNode();
/* 397 */     if ((n == null) || (!n.isNumber())) {
/* 398 */       JsonToken t = n == null ? null : n.asToken();
/* 399 */       throw _constructError("Current token (" + t + ") not numeric, can not use numeric value accessors");
/*     */     }
/* 401 */     return n;
/*     */   }
/*     */   
/*     */   protected void _handleEOF() throws JsonParseException
/*     */   {
/* 406 */     _throwInternal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\TreeTraversingParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */