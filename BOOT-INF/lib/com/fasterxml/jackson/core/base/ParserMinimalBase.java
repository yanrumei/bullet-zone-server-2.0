/*     */ package com.fasterxml.jackson.core.base;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.JsonEOFException;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
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
/*     */ public abstract class ParserMinimalBase
/*     */   extends JsonParser
/*     */ {
/*     */   protected static final int INT_TAB = 9;
/*     */   protected static final int INT_LF = 10;
/*     */   protected static final int INT_CR = 13;
/*     */   protected static final int INT_SPACE = 32;
/*     */   protected static final int INT_LBRACKET = 91;
/*     */   protected static final int INT_RBRACKET = 93;
/*     */   protected static final int INT_LCURLY = 123;
/*     */   protected static final int INT_RCURLY = 125;
/*     */   protected static final int INT_QUOTE = 34;
/*     */   protected static final int INT_BACKSLASH = 92;
/*     */   protected static final int INT_SLASH = 47;
/*     */   protected static final int INT_COLON = 58;
/*     */   protected static final int INT_COMMA = 44;
/*     */   protected static final int INT_HASH = 35;
/*     */   protected static final int INT_PERIOD = 46;
/*     */   protected static final int INT_e = 101;
/*     */   protected static final int INT_E = 69;
/*     */   protected JsonToken _currToken;
/*     */   protected JsonToken _lastClearedToken;
/*     */   
/*     */   protected ParserMinimalBase() {}
/*     */   
/*     */   protected ParserMinimalBase(int features)
/*     */   {
/*  74 */     super(features);
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
/*     */   public abstract JsonToken nextToken()
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
/* 100 */   public JsonToken currentToken() { return this._currToken; }
/*     */   
/* 102 */   public int currentTokenId() { JsonToken t = this._currToken;
/* 103 */     return t == null ? 0 : t.id();
/*     */   }
/*     */   
/* 106 */   public JsonToken getCurrentToken() { return this._currToken; }
/*     */   
/* 108 */   public int getCurrentTokenId() { JsonToken t = this._currToken;
/* 109 */     return t == null ? 0 : t.id();
/*     */   }
/*     */   
/* 112 */   public boolean hasCurrentToken() { return this._currToken != null; }
/*     */   
/* 114 */   public boolean hasTokenId(int id) { JsonToken t = this._currToken;
/* 115 */     if (t == null) {
/* 116 */       return 0 == id;
/*     */     }
/* 118 */     return t.id() == id;
/*     */   }
/*     */   
/*     */   public boolean hasToken(JsonToken t) {
/* 122 */     return this._currToken == t;
/*     */   }
/*     */   
/* 125 */   public boolean isExpectedStartArrayToken() { return this._currToken == JsonToken.START_ARRAY; }
/* 126 */   public boolean isExpectedStartObjectToken() { return this._currToken == JsonToken.START_OBJECT; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken nextValue()
/*     */     throws IOException
/*     */   {
/* 134 */     JsonToken t = nextToken();
/* 135 */     if (t == JsonToken.FIELD_NAME) {
/* 136 */       t = nextToken();
/*     */     }
/* 138 */     return t;
/*     */   }
/*     */   
/*     */   public JsonParser skipChildren()
/*     */     throws IOException
/*     */   {
/* 144 */     if ((this._currToken != JsonToken.START_OBJECT) && (this._currToken != JsonToken.START_ARRAY))
/*     */     {
/* 146 */       return this;
/*     */     }
/* 148 */     int open = 1;
/*     */     
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/* 154 */       JsonToken t = nextToken();
/* 155 */       if (t == null) {
/* 156 */         _handleEOF();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 161 */         return this;
/*     */       }
/* 163 */       if (t.isStructStart()) {
/* 164 */         open++;
/* 165 */       } else if (t.isStructEnd()) {
/* 166 */         open--; if (open == 0) {
/* 167 */           return this;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void _handleEOF()
/*     */     throws JsonParseException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getCurrentName()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean isClosed();
/*     */   
/*     */ 
/*     */   public abstract JsonStreamContext getParsingContext();
/*     */   
/*     */ 
/*     */   public void clearCurrentToken()
/*     */   {
/* 198 */     if (this._currToken != null) {
/* 199 */       this._lastClearedToken = this._currToken;
/* 200 */       this._currToken = null;
/*     */     }
/*     */   }
/*     */   
/* 204 */   public JsonToken getLastClearedToken() { return this._lastClearedToken; }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void overrideCurrentName(String paramString);
/*     */   
/*     */ 
/*     */   public abstract String getText()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract char[] getTextCharacters()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract boolean hasTextCharacters();
/*     */   
/*     */ 
/*     */   public abstract int getTextLength()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract int getTextOffset()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract byte[] getBinaryValue(Base64Variant paramBase64Variant)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */     throws IOException
/*     */   {
/* 237 */     JsonToken t = this._currToken;
/* 238 */     if (t != null) {
/* 239 */       switch (t.id()) {
/*     */       case 6: 
/* 241 */         String str = getText().trim();
/* 242 */         if ("true".equals(str)) {
/* 243 */           return true;
/*     */         }
/* 245 */         if ("false".equals(str)) {
/* 246 */           return false;
/*     */         }
/* 248 */         if (_hasTextualNull(str)) {
/* 249 */           return false;
/*     */         }
/*     */         break;
/*     */       case 7: 
/* 253 */         return getIntValue() != 0;
/*     */       case 9: 
/* 255 */         return true;
/*     */       case 10: 
/*     */       case 11: 
/* 258 */         return false;
/*     */       case 12: 
/* 260 */         Object value = getEmbeddedObject();
/* 261 */         if ((value instanceof Boolean)) {
/* 262 */           return ((Boolean)value).booleanValue();
/*     */         }
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/* 268 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int getValueAsInt()
/*     */     throws IOException
/*     */   {
/* 274 */     JsonToken t = this._currToken;
/* 275 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 276 */       return getIntValue();
/*     */     }
/* 278 */     return getValueAsInt(0);
/*     */   }
/*     */   
/*     */   public int getValueAsInt(int defaultValue)
/*     */     throws IOException
/*     */   {
/* 284 */     JsonToken t = this._currToken;
/* 285 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 286 */       return getIntValue();
/*     */     }
/* 288 */     if (t != null) {
/* 289 */       switch (t.id()) {
/*     */       case 6: 
/* 291 */         String str = getText();
/* 292 */         if (_hasTextualNull(str)) {
/* 293 */           return 0;
/*     */         }
/* 295 */         return NumberInput.parseAsInt(str, defaultValue);
/*     */       case 9: 
/* 297 */         return 1;
/*     */       case 10: 
/* 299 */         return 0;
/*     */       case 11: 
/* 301 */         return 0;
/*     */       case 12: 
/* 303 */         Object value = getEmbeddedObject();
/* 304 */         if ((value instanceof Number))
/* 305 */           return ((Number)value).intValue();
/*     */         break;
/*     */       }
/*     */     }
/* 309 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public long getValueAsLong()
/*     */     throws IOException
/*     */   {
/* 315 */     JsonToken t = this._currToken;
/* 316 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 317 */       return getLongValue();
/*     */     }
/* 319 */     return getValueAsLong(0L);
/*     */   }
/*     */   
/*     */   public long getValueAsLong(long defaultValue)
/*     */     throws IOException
/*     */   {
/* 325 */     JsonToken t = this._currToken;
/* 326 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT)) {
/* 327 */       return getLongValue();
/*     */     }
/* 329 */     if (t != null) {
/* 330 */       switch (t.id()) {
/*     */       case 6: 
/* 332 */         String str = getText();
/* 333 */         if (_hasTextualNull(str)) {
/* 334 */           return 0L;
/*     */         }
/* 336 */         return NumberInput.parseAsLong(str, defaultValue);
/*     */       case 9: 
/* 338 */         return 1L;
/*     */       case 10: 
/*     */       case 11: 
/* 341 */         return 0L;
/*     */       case 12: 
/* 343 */         Object value = getEmbeddedObject();
/* 344 */         if ((value instanceof Number))
/* 345 */           return ((Number)value).longValue();
/*     */         break;
/*     */       }
/*     */     }
/* 349 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public double getValueAsDouble(double defaultValue)
/*     */     throws IOException
/*     */   {
/* 355 */     JsonToken t = this._currToken;
/* 356 */     if (t != null) {
/* 357 */       switch (t.id()) {
/*     */       case 6: 
/* 359 */         String str = getText();
/* 360 */         if (_hasTextualNull(str)) {
/* 361 */           return 0.0D;
/*     */         }
/* 363 */         return NumberInput.parseAsDouble(str, defaultValue);
/*     */       case 7: 
/*     */       case 8: 
/* 366 */         return getDoubleValue();
/*     */       case 9: 
/* 368 */         return 1.0D;
/*     */       case 10: 
/*     */       case 11: 
/* 371 */         return 0.0D;
/*     */       case 12: 
/* 373 */         Object value = getEmbeddedObject();
/* 374 */         if ((value instanceof Number))
/* 375 */           return ((Number)value).doubleValue();
/*     */         break;
/*     */       }
/*     */     }
/* 379 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public String getValueAsString() throws IOException
/*     */   {
/* 384 */     if (this._currToken == JsonToken.VALUE_STRING) {
/* 385 */       return getText();
/*     */     }
/* 387 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 388 */       return getCurrentName();
/*     */     }
/* 390 */     return getValueAsString(null);
/*     */   }
/*     */   
/*     */   public String getValueAsString(String defaultValue) throws IOException
/*     */   {
/* 395 */     if (this._currToken == JsonToken.VALUE_STRING) {
/* 396 */       return getText();
/*     */     }
/* 398 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 399 */       return getCurrentName();
/*     */     }
/* 401 */     if ((this._currToken == null) || (this._currToken == JsonToken.VALUE_NULL) || (!this._currToken.isScalarValue())) {
/* 402 */       return defaultValue;
/*     */     }
/* 404 */     return getText();
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
/*     */   protected void _decodeBase64(String str, ByteArrayBuilder builder, Base64Variant b64variant)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 420 */       b64variant.decode(str, builder);
/*     */     } catch (IllegalArgumentException e) {
/* 422 */       _reportError(e.getMessage());
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
/*     */   protected boolean _hasTextualNull(String value)
/*     */   {
/* 439 */     return "null".equals(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _reportUnexpectedChar(int ch, String comment)
/*     */     throws JsonParseException
/*     */   {
/* 449 */     if (ch < 0) {
/* 450 */       _reportInvalidEOF();
/*     */     }
/* 452 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
/* 453 */     if (comment != null) {
/* 454 */       msg = msg + ": " + comment;
/*     */     }
/* 456 */     _reportError(msg);
/*     */   }
/*     */   
/*     */   protected void _reportInvalidEOF() throws JsonParseException {
/* 460 */     _reportInvalidEOF(" in " + this._currToken, this._currToken);
/*     */   }
/*     */   
/*     */   protected void _reportInvalidEOFInValue(JsonToken type)
/*     */     throws JsonParseException
/*     */   {
/*     */     String msg;
/*     */     String msg;
/* 468 */     if (type == JsonToken.VALUE_STRING) {
/* 469 */       msg = " in a String value"; } else { String msg;
/* 470 */       if ((type == JsonToken.VALUE_NUMBER_INT) || (type == JsonToken.VALUE_NUMBER_FLOAT))
/*     */       {
/* 472 */         msg = " in a Number value";
/*     */       } else
/* 474 */         msg = " in a value";
/*     */     }
/* 476 */     _reportInvalidEOF(msg, type);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void _reportInvalidEOF(String msg, JsonToken currToken)
/*     */     throws JsonParseException
/*     */   {
/* 483 */     throw new JsonEOFException(this, currToken, "Unexpected end-of-input" + msg);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   protected void _reportInvalidEOFInValue()
/*     */     throws JsonParseException
/*     */   {
/* 491 */     _reportInvalidEOF(" in a value");
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   protected void _reportInvalidEOF(String msg)
/*     */     throws JsonParseException
/*     */   {
/* 499 */     throw new JsonEOFException(this, null, "Unexpected end-of-input" + msg);
/*     */   }
/*     */   
/*     */   protected void _reportMissingRootWS(int ch) throws JsonParseException {
/* 503 */     _reportUnexpectedChar(ch, "Expected space separating root-level values");
/*     */   }
/*     */   
/*     */   protected void _throwInvalidSpace(int i) throws JsonParseException {
/* 507 */     char c = (char)i;
/* 508 */     String msg = "Illegal character (" + _getCharDesc(c) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
/* 509 */     _reportError(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _throwUnquotedSpace(int i, String ctxtDesc)
/*     */     throws JsonParseException
/*     */   {
/* 519 */     if ((!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)) || (i > 32)) {
/* 520 */       char c = (char)i;
/* 521 */       String msg = "Illegal unquoted character (" + _getCharDesc(c) + "): has to be escaped using backslash to be included in " + ctxtDesc;
/* 522 */       _reportError(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   protected char _handleUnrecognizedCharacterEscape(char ch) throws JsonProcessingException
/*     */   {
/* 528 */     if (isEnabled(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
/* 529 */       return ch;
/*     */     }
/*     */     
/* 532 */     if ((ch == '\'') && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 533 */       return ch;
/*     */     }
/* 535 */     _reportError("Unrecognized character escape " + _getCharDesc(ch));
/* 536 */     return ch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String _getCharDesc(int ch)
/*     */   {
/* 547 */     char c = (char)ch;
/* 548 */     if (Character.isISOControl(c)) {
/* 549 */       return "(CTRL-CHAR, code " + ch + ")";
/*     */     }
/* 551 */     if (ch > 255) {
/* 552 */       return "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")";
/*     */     }
/* 554 */     return "'" + c + "' (code " + ch + ")";
/*     */   }
/*     */   
/*     */   protected final void _reportError(String msg) throws JsonParseException {
/* 558 */     throw _constructError(msg);
/*     */   }
/*     */   
/*     */   protected final void _wrapError(String msg, Throwable t) throws JsonParseException {
/* 562 */     throw _constructError(msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void _throwInternal() {}
/*     */   
/*     */   protected final JsonParseException _constructError(String msg, Throwable t)
/*     */   {
/* 570 */     return new JsonParseException(this, msg, t);
/*     */   }
/*     */   
/*     */   protected static byte[] _asciiBytes(String str) {
/* 574 */     byte[] b = new byte[str.length()];
/* 575 */     int i = 0; for (int len = str.length(); i < len; i++) {
/* 576 */       b[i] = ((byte)str.charAt(i));
/*     */     }
/* 578 */     return b;
/*     */   }
/*     */   
/*     */   protected static String _ascii(byte[] b) {
/*     */     try {
/* 583 */       return new String(b, "US-ASCII");
/*     */     } catch (IOException e) {
/* 585 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\base\ParserMinimalBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */