/*      */ package com.fasterxml.jackson.core.base;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.core.json.DupDetector;
/*      */ import com.fasterxml.jackson.core.json.JsonReadContext;
/*      */ import com.fasterxml.jackson.core.json.PackageVersion;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ParserBase
/*      */   extends ParserMinimalBase
/*      */ {
/*      */   protected final IOContext _ioContext;
/*      */   protected boolean _closed;
/*      */   protected int _inputPtr;
/*      */   protected int _inputEnd;
/*      */   protected long _currInputProcessed;
/*   76 */   protected int _currInputRow = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _currInputRowStart;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long _tokenInputTotal;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  105 */   protected int _tokenInputRow = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _tokenInputCol;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonReadContext _parsingContext;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _nextToken;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TextBuffer _textBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _nameCopyBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _nameCopied;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ByteArrayBuilder _byteArrayBuilder;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _binaryValue;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_UNKNOWN = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_INT = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_LONG = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_BIGINT = 4;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_DOUBLE = 8;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int NR_BIGDECIMAL = 16;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  194 */   static final BigInteger BI_MIN_INT = BigInteger.valueOf(-2147483648L);
/*  195 */   static final BigInteger BI_MAX_INT = BigInteger.valueOf(2147483647L);
/*      */   
/*  197 */   static final BigInteger BI_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/*  198 */   static final BigInteger BI_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*      */   
/*  200 */   static final BigDecimal BD_MIN_LONG = new BigDecimal(BI_MIN_LONG);
/*  201 */   static final BigDecimal BD_MAX_LONG = new BigDecimal(BI_MAX_LONG);
/*      */   
/*  203 */   static final BigDecimal BD_MIN_INT = new BigDecimal(BI_MIN_INT);
/*  204 */   static final BigDecimal BD_MAX_INT = new BigDecimal(BI_MAX_INT);
/*      */   
/*      */ 
/*      */   static final long MIN_INT_L = -2147483648L;
/*      */   
/*      */ 
/*      */   static final long MAX_INT_L = 2147483647L;
/*      */   
/*      */ 
/*      */   static final double MIN_LONG_D = -9.223372036854776E18D;
/*      */   
/*      */ 
/*      */   static final double MAX_LONG_D = 9.223372036854776E18D;
/*      */   
/*      */ 
/*      */   static final double MIN_INT_D = -2.147483648E9D;
/*      */   
/*      */   static final double MAX_INT_D = 2.147483647E9D;
/*      */   
/*      */   protected static final int INT_0 = 48;
/*      */   
/*      */   protected static final int INT_9 = 57;
/*      */   
/*      */   protected static final int INT_MINUS = 45;
/*      */   
/*      */   protected static final int INT_PLUS = 43;
/*      */   
/*      */   protected static final char CHAR_NULL = '\000';
/*      */   
/*  233 */   protected int _numTypesValid = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _numberInt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long _numberLong;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected double _numberDouble;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BigInteger _numberBigInt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BigDecimal _numberBigDecimal;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _numberNegative;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _intLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _fractLength;
/*      */   
/*      */ 
/*      */ 
/*      */   protected int _expLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ParserBase(IOContext ctxt, int features)
/*      */   {
/*  284 */     super(features);
/*  285 */     this._ioContext = ctxt;
/*  286 */     this._textBuffer = ctxt.constructTextBuffer();
/*  287 */     DupDetector dups = JsonParser.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(features) ? DupDetector.rootDetector(this) : null;
/*      */     
/*  289 */     this._parsingContext = JsonReadContext.createRootContext(dups);
/*      */   }
/*      */   
/*  292 */   public Version version() { return PackageVersion.VERSION; }
/*      */   
/*      */   public Object getCurrentValue()
/*      */   {
/*  296 */     return this._parsingContext.getCurrentValue();
/*      */   }
/*      */   
/*      */   public void setCurrentValue(Object v)
/*      */   {
/*  301 */     this._parsingContext.setCurrentValue(v);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser enable(JsonParser.Feature f)
/*      */   {
/*  312 */     this._features |= f.getMask();
/*  313 */     if ((f == JsonParser.Feature.STRICT_DUPLICATE_DETECTION) && 
/*  314 */       (this._parsingContext.getDupDetector() == null)) {
/*  315 */       this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
/*      */     }
/*      */     
/*  318 */     return this;
/*      */   }
/*      */   
/*      */   public JsonParser disable(JsonParser.Feature f)
/*      */   {
/*  323 */     this._features &= (f.getMask() ^ 0xFFFFFFFF);
/*  324 */     if (f == JsonParser.Feature.STRICT_DUPLICATE_DETECTION) {
/*  325 */       this._parsingContext = this._parsingContext.withDupDetector(null);
/*      */     }
/*  327 */     return this;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public JsonParser setFeatureMask(int newMask)
/*      */   {
/*  333 */     int changes = this._features ^ newMask;
/*  334 */     if (changes != 0) {
/*  335 */       this._features = newMask;
/*  336 */       _checkStdFeatureChanges(newMask, changes);
/*      */     }
/*  338 */     return this;
/*      */   }
/*      */   
/*      */   public JsonParser overrideStdFeatures(int values, int mask)
/*      */   {
/*  343 */     int oldState = this._features;
/*  344 */     int newState = oldState & (mask ^ 0xFFFFFFFF) | values & mask;
/*  345 */     int changed = oldState ^ newState;
/*  346 */     if (changed != 0) {
/*  347 */       this._features = newState;
/*  348 */       _checkStdFeatureChanges(newState, changed);
/*      */     }
/*  350 */     return this;
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
/*      */   protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures)
/*      */   {
/*  364 */     int f = JsonParser.Feature.STRICT_DUPLICATE_DETECTION.getMask();
/*      */     
/*  366 */     if (((changedFeatures & f) != 0) && 
/*  367 */       ((newFeatureFlags & f) != 0)) {
/*  368 */       if (this._parsingContext.getDupDetector() == null) {
/*  369 */         this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
/*      */       } else {
/*  371 */         this._parsingContext = this._parsingContext.withDupDetector(null);
/*      */       }
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
/*      */   public String getCurrentName()
/*      */     throws IOException
/*      */   {
/*  389 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/*  390 */       JsonReadContext parent = this._parsingContext.getParent();
/*  391 */       if (parent != null) {
/*  392 */         return parent.getCurrentName();
/*      */       }
/*      */     }
/*  395 */     return this._parsingContext.getCurrentName();
/*      */   }
/*      */   
/*      */   public void overrideCurrentName(String name)
/*      */   {
/*  400 */     JsonReadContext ctxt = this._parsingContext;
/*  401 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/*  402 */       ctxt = ctxt.getParent();
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  408 */       ctxt.setCurrentName(name);
/*      */     } catch (IOException e) {
/*  410 */       throw new IllegalStateException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void close() throws IOException {
/*  415 */     if (!this._closed) {
/*  416 */       this._closed = true;
/*      */       try {
/*  418 */         _closeInput();
/*      */       }
/*      */       finally
/*      */       {
/*  422 */         _releaseBuffers();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  427 */   public boolean isClosed() { return this._closed; }
/*  428 */   public JsonReadContext getParsingContext() { return this._parsingContext; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getTokenLocation()
/*      */   {
/*  437 */     return new JsonLocation(this._ioContext.getSourceReference(), -1L, getTokenCharacterOffset(), getTokenLineNr(), getTokenColumnNr());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getCurrentLocation()
/*      */   {
/*  449 */     int col = this._inputPtr - this._currInputRowStart + 1;
/*  450 */     return new JsonLocation(this._ioContext.getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, col);
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
/*      */   public boolean hasTextCharacters()
/*      */   {
/*  463 */     if (this._currToken == JsonToken.VALUE_STRING) return true;
/*  464 */     if (this._currToken == JsonToken.FIELD_NAME) return this._nameCopied;
/*  465 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public byte[] getBinaryValue(Base64Variant variant)
/*      */     throws IOException
/*      */   {
/*  472 */     if (this._binaryValue == null) {
/*  473 */       if (this._currToken != JsonToken.VALUE_STRING) {
/*  474 */         _reportError("Current token (" + this._currToken + ") not VALUE_STRING, can not access as binary");
/*      */       }
/*  476 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  477 */       _decodeBase64(getText(), builder, variant);
/*  478 */       this._binaryValue = builder.toByteArray();
/*      */     }
/*  480 */     return this._binaryValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  489 */   public long getTokenCharacterOffset() { return this._tokenInputTotal; }
/*  490 */   public int getTokenLineNr() { return this._tokenInputRow; }
/*      */   
/*      */   public int getTokenColumnNr() {
/*  493 */     int col = this._tokenInputCol;
/*  494 */     return col < 0 ? col : col + 1;
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
/*      */   protected abstract void _closeInput()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */     throws IOException
/*      */   {
/*  518 */     this._textBuffer.releaseBuffers();
/*  519 */     char[] buf = this._nameCopyBuffer;
/*  520 */     if (buf != null) {
/*  521 */       this._nameCopyBuffer = null;
/*  522 */       this._ioContext.releaseNameCopyBuffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _handleEOF()
/*      */     throws JsonParseException
/*      */   {
/*  533 */     if (!this._parsingContext.inRoot()) {
/*  534 */       String marker = this._parsingContext.inArray() ? "Array" : "Object";
/*  535 */       _reportInvalidEOF(String.format(": expected close marker for %s (start marker at %s)", new Object[] { marker, this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) }), null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _eofAsNextChar()
/*      */     throws JsonParseException
/*      */   {
/*  547 */     _handleEOF();
/*  548 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportMismatchedEndMarker(int actCh, char expCh)
/*      */     throws JsonParseException
/*      */   {
/*  558 */     String startDesc = "" + this._parsingContext.getStartLocation(this._ioContext.getSourceReference());
/*  559 */     _reportError("Unexpected close marker '" + (char)actCh + "': expected '" + expCh + "' (for " + this._parsingContext.typeDesc() + " starting at " + startDesc + ")");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteArrayBuilder _getByteArrayBuilder()
/*      */   {
/*  570 */     if (this._byteArrayBuilder == null) {
/*  571 */       this._byteArrayBuilder = new ByteArrayBuilder();
/*      */     } else {
/*  573 */       this._byteArrayBuilder.reset();
/*      */     }
/*  575 */     return this._byteArrayBuilder;
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
/*      */   protected final JsonToken reset(boolean negative, int intLen, int fractLen, int expLen)
/*      */   {
/*  588 */     if ((fractLen < 1) && (expLen < 1)) {
/*  589 */       return resetInt(negative, intLen);
/*      */     }
/*  591 */     return resetFloat(negative, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */   protected final JsonToken resetInt(boolean negative, int intLen)
/*      */   {
/*  596 */     this._numberNegative = negative;
/*  597 */     this._intLength = intLen;
/*  598 */     this._fractLength = 0;
/*  599 */     this._expLength = 0;
/*  600 */     this._numTypesValid = 0;
/*  601 */     return JsonToken.VALUE_NUMBER_INT;
/*      */   }
/*      */   
/*      */   protected final JsonToken resetFloat(boolean negative, int intLen, int fractLen, int expLen)
/*      */   {
/*  606 */     this._numberNegative = negative;
/*  607 */     this._intLength = intLen;
/*  608 */     this._fractLength = fractLen;
/*  609 */     this._expLength = expLen;
/*  610 */     this._numTypesValid = 0;
/*  611 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*      */   }
/*      */   
/*      */   protected final JsonToken resetAsNaN(String valueStr, double value)
/*      */   {
/*  616 */     this._textBuffer.resetWithString(valueStr);
/*  617 */     this._numberDouble = value;
/*  618 */     this._numTypesValid = 8;
/*  619 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Number getNumberValue()
/*      */     throws IOException
/*      */   {
/*  631 */     if (this._numTypesValid == 0) {
/*  632 */       _parseNumericValue(0);
/*      */     }
/*      */     
/*  635 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  636 */       if ((this._numTypesValid & 0x1) != 0) {
/*  637 */         return Integer.valueOf(this._numberInt);
/*      */       }
/*  639 */       if ((this._numTypesValid & 0x2) != 0) {
/*  640 */         return Long.valueOf(this._numberLong);
/*      */       }
/*  642 */       if ((this._numTypesValid & 0x4) != 0) {
/*  643 */         return this._numberBigInt;
/*      */       }
/*      */       
/*  646 */       return this._numberBigDecimal;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  652 */     if ((this._numTypesValid & 0x10) != 0) {
/*  653 */       return this._numberBigDecimal;
/*      */     }
/*  655 */     if ((this._numTypesValid & 0x8) == 0) {
/*  656 */       _throwInternal();
/*      */     }
/*  658 */     return Double.valueOf(this._numberDouble);
/*      */   }
/*      */   
/*      */   public JsonParser.NumberType getNumberType()
/*      */     throws IOException
/*      */   {
/*  664 */     if (this._numTypesValid == 0) {
/*  665 */       _parseNumericValue(0);
/*      */     }
/*  667 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  668 */       if ((this._numTypesValid & 0x1) != 0) {
/*  669 */         return JsonParser.NumberType.INT;
/*      */       }
/*  671 */       if ((this._numTypesValid & 0x2) != 0) {
/*  672 */         return JsonParser.NumberType.LONG;
/*      */       }
/*  674 */       return JsonParser.NumberType.BIG_INTEGER;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  683 */     if ((this._numTypesValid & 0x10) != 0) {
/*  684 */       return JsonParser.NumberType.BIG_DECIMAL;
/*      */     }
/*  686 */     return JsonParser.NumberType.DOUBLE;
/*      */   }
/*      */   
/*      */   public int getIntValue()
/*      */     throws IOException
/*      */   {
/*  692 */     if ((this._numTypesValid & 0x1) == 0) {
/*  693 */       if (this._numTypesValid == 0) {
/*  694 */         return _parseIntValue();
/*      */       }
/*  696 */       if ((this._numTypesValid & 0x1) == 0) {
/*  697 */         convertNumberToInt();
/*      */       }
/*      */     }
/*  700 */     return this._numberInt;
/*      */   }
/*      */   
/*      */   public long getLongValue()
/*      */     throws IOException
/*      */   {
/*  706 */     if ((this._numTypesValid & 0x2) == 0) {
/*  707 */       if (this._numTypesValid == 0) {
/*  708 */         _parseNumericValue(2);
/*      */       }
/*  710 */       if ((this._numTypesValid & 0x2) == 0) {
/*  711 */         convertNumberToLong();
/*      */       }
/*      */     }
/*  714 */     return this._numberLong;
/*      */   }
/*      */   
/*      */   public BigInteger getBigIntegerValue()
/*      */     throws IOException
/*      */   {
/*  720 */     if ((this._numTypesValid & 0x4) == 0) {
/*  721 */       if (this._numTypesValid == 0) {
/*  722 */         _parseNumericValue(4);
/*      */       }
/*  724 */       if ((this._numTypesValid & 0x4) == 0) {
/*  725 */         convertNumberToBigInteger();
/*      */       }
/*      */     }
/*  728 */     return this._numberBigInt;
/*      */   }
/*      */   
/*      */   public float getFloatValue()
/*      */     throws IOException
/*      */   {
/*  734 */     double value = getDoubleValue();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  743 */     return (float)value;
/*      */   }
/*      */   
/*      */   public double getDoubleValue()
/*      */     throws IOException
/*      */   {
/*  749 */     if ((this._numTypesValid & 0x8) == 0) {
/*  750 */       if (this._numTypesValid == 0) {
/*  751 */         _parseNumericValue(8);
/*      */       }
/*  753 */       if ((this._numTypesValid & 0x8) == 0) {
/*  754 */         convertNumberToDouble();
/*      */       }
/*      */     }
/*  757 */     return this._numberDouble;
/*      */   }
/*      */   
/*      */   public BigDecimal getDecimalValue()
/*      */     throws IOException
/*      */   {
/*  763 */     if ((this._numTypesValid & 0x10) == 0) {
/*  764 */       if (this._numTypesValid == 0) {
/*  765 */         _parseNumericValue(16);
/*      */       }
/*  767 */       if ((this._numTypesValid & 0x10) == 0) {
/*  768 */         convertNumberToBigDecimal();
/*      */       }
/*      */     }
/*  771 */     return this._numberBigDecimal;
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
/*      */   protected void _parseNumericValue(int expType)
/*      */     throws IOException
/*      */   {
/*  792 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  793 */       char[] buf = this._textBuffer.getTextBuffer();
/*  794 */       int offset = this._textBuffer.getTextOffset();
/*  795 */       int len = this._intLength;
/*  796 */       if (this._numberNegative) {
/*  797 */         offset++;
/*      */       }
/*  799 */       if (len <= 9) {
/*  800 */         int i = NumberInput.parseInt(buf, offset, len);
/*  801 */         this._numberInt = (this._numberNegative ? -i : i);
/*  802 */         this._numTypesValid = 1;
/*  803 */         return;
/*      */       }
/*  805 */       if (len <= 18) {
/*  806 */         long l = NumberInput.parseLong(buf, offset, len);
/*  807 */         if (this._numberNegative) {
/*  808 */           l = -l;
/*      */         }
/*      */         
/*  811 */         if (len == 10) {
/*  812 */           if (this._numberNegative) {
/*  813 */             if (l >= -2147483648L) {
/*  814 */               this._numberInt = ((int)l);
/*  815 */               this._numTypesValid = 1;
/*      */             }
/*      */             
/*      */           }
/*  819 */           else if (l <= 2147483647L) {
/*  820 */             this._numberInt = ((int)l);
/*  821 */             this._numTypesValid = 1;
/*  822 */             return;
/*      */           }
/*      */         }
/*      */         
/*  826 */         this._numberLong = l;
/*  827 */         this._numTypesValid = 2;
/*  828 */         return;
/*      */       }
/*  830 */       _parseSlowInt(expType, buf, offset, len);
/*  831 */       return;
/*      */     }
/*  833 */     if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
/*  834 */       _parseSlowFloat(expType);
/*  835 */       return;
/*      */     }
/*  837 */     _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _parseIntValue()
/*      */     throws IOException
/*      */   {
/*  847 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/*  848 */       char[] buf = this._textBuffer.getTextBuffer();
/*  849 */       int offset = this._textBuffer.getTextOffset();
/*  850 */       int len = this._intLength;
/*  851 */       if (this._numberNegative) {
/*  852 */         offset++;
/*      */       }
/*  854 */       if (len <= 9) {
/*  855 */         int i = NumberInput.parseInt(buf, offset, len);
/*  856 */         if (this._numberNegative) {
/*  857 */           i = -i;
/*      */         }
/*  859 */         this._numberInt = i;
/*  860 */         this._numTypesValid = 1;
/*  861 */         return i;
/*      */       }
/*      */     }
/*  864 */     _parseNumericValue(1);
/*  865 */     if ((this._numTypesValid & 0x1) == 0) {
/*  866 */       convertNumberToInt();
/*      */     }
/*  868 */     return this._numberInt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _parseSlowFloat(int expType)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  881 */       if (expType == 16) {
/*  882 */         this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
/*  883 */         this._numTypesValid = 16;
/*      */       }
/*      */       else {
/*  886 */         this._numberDouble = this._textBuffer.contentsAsDouble();
/*  887 */         this._numTypesValid = 8;
/*      */       }
/*      */     }
/*      */     catch (NumberFormatException nex) {
/*  891 */       _wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", nex);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _parseSlowInt(int expType, char[] buf, int offset, int len) throws IOException
/*      */   {
/*  897 */     String numStr = this._textBuffer.contentsAsString();
/*      */     try
/*      */     {
/*  900 */       if (NumberInput.inLongRange(buf, offset, len, this._numberNegative))
/*      */       {
/*  902 */         this._numberLong = Long.parseLong(numStr);
/*  903 */         this._numTypesValid = 2;
/*      */       }
/*      */       else {
/*  906 */         this._numberBigInt = new BigInteger(numStr);
/*  907 */         this._numTypesValid = 4;
/*      */       }
/*      */     }
/*      */     catch (NumberFormatException nex) {
/*  911 */       _wrapError("Malformed numeric value '" + numStr + "'", nex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToInt()
/*      */     throws IOException
/*      */   {
/*  924 */     if ((this._numTypesValid & 0x2) != 0)
/*      */     {
/*  926 */       int result = (int)this._numberLong;
/*  927 */       if (result != this._numberLong) {
/*  928 */         _reportError("Numeric value (" + getText() + ") out of range of int");
/*      */       }
/*  930 */       this._numberInt = result;
/*  931 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  932 */       if ((BI_MIN_INT.compareTo(this._numberBigInt) > 0) || (BI_MAX_INT.compareTo(this._numberBigInt) < 0))
/*      */       {
/*  934 */         reportOverflowInt();
/*      */       }
/*  936 */       this._numberInt = this._numberBigInt.intValue();
/*  937 */     } else if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*  939 */       if ((this._numberDouble < -2.147483648E9D) || (this._numberDouble > 2.147483647E9D)) {
/*  940 */         reportOverflowInt();
/*      */       }
/*  942 */       this._numberInt = ((int)this._numberDouble);
/*  943 */     } else if ((this._numTypesValid & 0x10) != 0) {
/*  944 */       if ((BD_MIN_INT.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_INT.compareTo(this._numberBigDecimal) < 0))
/*      */       {
/*  946 */         reportOverflowInt();
/*      */       }
/*  948 */       this._numberInt = this._numberBigDecimal.intValue();
/*      */     } else {
/*  950 */       _throwInternal();
/*      */     }
/*  952 */     this._numTypesValid |= 0x1;
/*      */   }
/*      */   
/*      */   protected void convertNumberToLong() throws IOException
/*      */   {
/*  957 */     if ((this._numTypesValid & 0x1) != 0) {
/*  958 */       this._numberLong = this._numberInt;
/*  959 */     } else if ((this._numTypesValid & 0x4) != 0) {
/*  960 */       if ((BI_MIN_LONG.compareTo(this._numberBigInt) > 0) || (BI_MAX_LONG.compareTo(this._numberBigInt) < 0))
/*      */       {
/*  962 */         reportOverflowLong();
/*      */       }
/*  964 */       this._numberLong = this._numberBigInt.longValue();
/*  965 */     } else if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*  967 */       if ((this._numberDouble < -9.223372036854776E18D) || (this._numberDouble > 9.223372036854776E18D)) {
/*  968 */         reportOverflowLong();
/*      */       }
/*  970 */       this._numberLong = (this._numberDouble);
/*  971 */     } else if ((this._numTypesValid & 0x10) != 0) {
/*  972 */       if ((BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0))
/*      */       {
/*  974 */         reportOverflowLong();
/*      */       }
/*  976 */       this._numberLong = this._numberBigDecimal.longValue();
/*      */     } else {
/*  978 */       _throwInternal();
/*      */     }
/*  980 */     this._numTypesValid |= 0x2;
/*      */   }
/*      */   
/*      */   protected void convertNumberToBigInteger() throws IOException
/*      */   {
/*  985 */     if ((this._numTypesValid & 0x10) != 0)
/*      */     {
/*  987 */       this._numberBigInt = this._numberBigDecimal.toBigInteger();
/*  988 */     } else if ((this._numTypesValid & 0x2) != 0) {
/*  989 */       this._numberBigInt = BigInteger.valueOf(this._numberLong);
/*  990 */     } else if ((this._numTypesValid & 0x1) != 0) {
/*  991 */       this._numberBigInt = BigInteger.valueOf(this._numberInt);
/*  992 */     } else if ((this._numTypesValid & 0x8) != 0) {
/*  993 */       this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
/*      */     } else {
/*  995 */       _throwInternal();
/*      */     }
/*  997 */     this._numTypesValid |= 0x4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToDouble()
/*      */     throws IOException
/*      */   {
/* 1008 */     if ((this._numTypesValid & 0x10) != 0) {
/* 1009 */       this._numberDouble = this._numberBigDecimal.doubleValue();
/* 1010 */     } else if ((this._numTypesValid & 0x4) != 0) {
/* 1011 */       this._numberDouble = this._numberBigInt.doubleValue();
/* 1012 */     } else if ((this._numTypesValid & 0x2) != 0) {
/* 1013 */       this._numberDouble = this._numberLong;
/* 1014 */     } else if ((this._numTypesValid & 0x1) != 0) {
/* 1015 */       this._numberDouble = this._numberInt;
/*      */     } else {
/* 1017 */       _throwInternal();
/*      */     }
/* 1019 */     this._numTypesValid |= 0x8;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void convertNumberToBigDecimal()
/*      */     throws IOException
/*      */   {
/* 1030 */     if ((this._numTypesValid & 0x8) != 0)
/*      */     {
/*      */ 
/*      */ 
/* 1034 */       this._numberBigDecimal = NumberInput.parseBigDecimal(getText());
/* 1035 */     } else if ((this._numTypesValid & 0x4) != 0) {
/* 1036 */       this._numberBigDecimal = new BigDecimal(this._numberBigInt);
/* 1037 */     } else if ((this._numTypesValid & 0x2) != 0) {
/* 1038 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
/* 1039 */     } else if ((this._numTypesValid & 0x1) != 0) {
/* 1040 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
/*      */     } else {
/* 1042 */       _throwInternal();
/*      */     }
/* 1044 */     this._numTypesValid |= 0x10;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reportUnexpectedNumberChar(int ch, String comment)
/*      */     throws JsonParseException
/*      */   {
/* 1054 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ") in numeric value";
/* 1055 */     if (comment != null) {
/* 1056 */       msg = msg + ": " + comment;
/*      */     }
/* 1058 */     _reportError(msg);
/*      */   }
/*      */   
/*      */   protected void reportInvalidNumber(String msg) throws JsonParseException {
/* 1062 */     _reportError("Invalid numeric value: " + msg);
/*      */   }
/*      */   
/*      */   protected void reportOverflowInt() throws IOException {
/* 1066 */     _reportError(String.format("Numeric value (%s) out of range of int (%d - %s)", new Object[] { getText(), Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE) }));
/*      */   }
/*      */   
/*      */   protected void reportOverflowLong() throws IOException
/*      */   {
/* 1071 */     _reportError(String.format("Numeric value (%s) out of range of long (%d - %s)", new Object[] { getText(), Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE) }));
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
/*      */   protected char _decodeEscaped()
/*      */     throws IOException
/*      */   {
/* 1087 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   protected final int _decodeBase64Escape(Base64Variant b64variant, int ch, int index)
/*      */     throws IOException
/*      */   {
/* 1093 */     if (ch != 92) {
/* 1094 */       throw reportInvalidBase64Char(b64variant, ch, index);
/*      */     }
/* 1096 */     int unescaped = _decodeEscaped();
/*      */     
/* 1098 */     if ((unescaped <= 32) && 
/* 1099 */       (index == 0)) {
/* 1100 */       return -1;
/*      */     }
/*      */     
/*      */ 
/* 1104 */     int bits = b64variant.decodeBase64Char(unescaped);
/* 1105 */     if (bits < 0) {
/* 1106 */       throw reportInvalidBase64Char(b64variant, unescaped, index);
/*      */     }
/* 1108 */     return bits;
/*      */   }
/*      */   
/*      */   protected final int _decodeBase64Escape(Base64Variant b64variant, char ch, int index) throws IOException
/*      */   {
/* 1113 */     if (ch != '\\') {
/* 1114 */       throw reportInvalidBase64Char(b64variant, ch, index);
/*      */     }
/* 1116 */     char unescaped = _decodeEscaped();
/*      */     
/* 1118 */     if ((unescaped <= ' ') && 
/* 1119 */       (index == 0)) {
/* 1120 */       return -1;
/*      */     }
/*      */     
/*      */ 
/* 1124 */     int bits = b64variant.decodeBase64Char(unescaped);
/* 1125 */     if (bits < 0) {
/* 1126 */       throw reportInvalidBase64Char(b64variant, unescaped, index);
/*      */     }
/* 1128 */     return bits;
/*      */   }
/*      */   
/*      */   protected IllegalArgumentException reportInvalidBase64Char(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException {
/* 1132 */     return reportInvalidBase64Char(b64variant, ch, bindex, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidBase64Char(Base64Variant b64variant, int ch, int bindex, String msg)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     String base;
/*      */     String base;
/* 1141 */     if (ch <= 32) {
/* 1142 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units"; } else { String base;
/* 1143 */       if (b64variant.usesPaddingChar(ch)) {
/* 1144 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character"; } else { String base;
/* 1145 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*      */         {
/* 1147 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */         } else
/* 1149 */           base = "Illegal character '" + (char)ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */       } }
/* 1151 */     if (msg != null) {
/* 1152 */       base = base + ": " + msg;
/*      */     }
/* 1154 */     return new IllegalArgumentException(base);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected void loadMoreGuaranteed()
/*      */     throws IOException
/*      */   {
/* 1166 */     if (!loadMore()) _reportInvalidEOF();
/*      */   }
/*      */   
/*      */   @Deprecated
/* 1170 */   protected boolean loadMore() throws IOException { return false; }
/*      */   
/*      */   protected void _finishString()
/*      */     throws IOException
/*      */   {}
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\base\ParserBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */