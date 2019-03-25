/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.base.ParserBase;
/*      */ import com.fasterxml.jackson.core.io.CharTypes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ 
/*      */ 
/*      */ public class ReaderBasedJsonParser
/*      */   extends ParserBase
/*      */ {
/*   24 */   protected static final int[] _icLatin1 = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Reader _reader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _inputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _bufferRecyclable;
/*      */   
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
/*      */ 
/*      */   protected final CharsToNameCanonicalizer _symbols;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _hashSeed;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _tokenIncomplete;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long _nameStartOffset;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _nameStartRow;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _nameStartCol;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*      */   {
/*  116 */     super(ctxt, features);
/*  117 */     this._reader = r;
/*  118 */     this._inputBuffer = inputBuffer;
/*  119 */     this._inputPtr = start;
/*  120 */     this._inputEnd = end;
/*  121 */     this._objectCodec = codec;
/*  122 */     this._symbols = st;
/*  123 */     this._hashSeed = st.hashSeed();
/*  124 */     this._bufferRecyclable = bufferRecyclable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st)
/*      */   {
/*  134 */     super(ctxt, features);
/*  135 */     this._reader = r;
/*  136 */     this._inputBuffer = ctxt.allocTokenBuffer();
/*  137 */     this._inputPtr = 0;
/*  138 */     this._inputEnd = 0;
/*  139 */     this._objectCodec = codec;
/*  140 */     this._symbols = st;
/*  141 */     this._hashSeed = st.hashSeed();
/*  142 */     this._bufferRecyclable = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  151 */   public ObjectCodec getCodec() { return this._objectCodec; }
/*  152 */   public void setCodec(ObjectCodec c) { this._objectCodec = c; }
/*      */   
/*      */   public int releaseBuffered(Writer w) throws IOException
/*      */   {
/*  156 */     int count = this._inputEnd - this._inputPtr;
/*  157 */     if (count < 1) { return 0;
/*      */     }
/*  159 */     int origPtr = this._inputPtr;
/*  160 */     w.write(this._inputBuffer, origPtr, count);
/*  161 */     return count;
/*      */   }
/*      */   
/*  164 */   public Object getInputSource() { return this._reader; }
/*      */   
/*      */   @Deprecated
/*      */   protected char getNextChar(String eofMsg) throws IOException {
/*  168 */     return getNextChar(eofMsg, null);
/*      */   }
/*      */   
/*      */   protected char getNextChar(String eofMsg, JsonToken forToken) throws IOException {
/*  172 */     if ((this._inputPtr >= this._inputEnd) && 
/*  173 */       (!_loadMore())) {
/*  174 */       _reportInvalidEOF(eofMsg, forToken);
/*      */     }
/*      */     
/*  177 */     return this._inputBuffer[(this._inputPtr++)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _closeInput()
/*      */     throws IOException
/*      */   {
/*  189 */     if (this._reader != null) {
/*  190 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/*  191 */         this._reader.close();
/*      */       }
/*  193 */       this._reader = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */     throws IOException
/*      */   {
/*  205 */     super._releaseBuffers();
/*      */     
/*  207 */     this._symbols.release();
/*      */     
/*  209 */     if (this._bufferRecyclable) {
/*  210 */       char[] buf = this._inputBuffer;
/*  211 */       if (buf != null) {
/*  212 */         this._inputBuffer = null;
/*  213 */         this._ioContext.releaseTokenBuffer(buf);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _loadMoreGuaranteed()
/*      */     throws IOException
/*      */   {
/*  225 */     if (!_loadMore()) _reportInvalidEOF();
/*      */   }
/*      */   
/*      */   protected boolean _loadMore() throws IOException
/*      */   {
/*  230 */     int bufSize = this._inputEnd;
/*      */     
/*  232 */     this._currInputProcessed += bufSize;
/*  233 */     this._currInputRowStart -= bufSize;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  238 */     this._nameStartOffset -= bufSize;
/*      */     
/*  240 */     if (this._reader != null) {
/*  241 */       int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
/*  242 */       if (count > 0) {
/*  243 */         this._inputPtr = 0;
/*  244 */         this._inputEnd = count;
/*  245 */         return true;
/*      */       }
/*      */       
/*  248 */       _closeInput();
/*      */       
/*  250 */       if (count == 0) {
/*  251 */         throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
/*      */       }
/*      */     }
/*  254 */     return false;
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
/*      */   public final String getText()
/*      */     throws IOException
/*      */   {
/*  272 */     JsonToken t = this._currToken;
/*  273 */     if (t == JsonToken.VALUE_STRING) {
/*  274 */       if (this._tokenIncomplete) {
/*  275 */         this._tokenIncomplete = false;
/*  276 */         _finishString();
/*      */       }
/*  278 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  280 */     return _getText2(t);
/*      */   }
/*      */   
/*      */   public int getText(Writer writer)
/*      */     throws IOException
/*      */   {
/*  286 */     JsonToken t = this._currToken;
/*  287 */     if (t == JsonToken.VALUE_STRING) {
/*  288 */       if (this._tokenIncomplete) {
/*  289 */         this._tokenIncomplete = false;
/*  290 */         _finishString();
/*      */       }
/*  292 */       return this._textBuffer.contentsToWriter(writer);
/*      */     }
/*  294 */     if (t == JsonToken.FIELD_NAME) {
/*  295 */       String n = this._parsingContext.getCurrentName();
/*  296 */       writer.write(n);
/*  297 */       return n.length();
/*      */     }
/*  299 */     if (t != null) {
/*  300 */       if (t.isNumeric()) {
/*  301 */         return this._textBuffer.contentsToWriter(writer);
/*      */       }
/*  303 */       char[] ch = t.asCharArray();
/*  304 */       writer.write(ch);
/*  305 */       return ch.length;
/*      */     }
/*  307 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getValueAsString()
/*      */     throws IOException
/*      */   {
/*  316 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  317 */       if (this._tokenIncomplete) {
/*  318 */         this._tokenIncomplete = false;
/*  319 */         _finishString();
/*      */       }
/*  321 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  323 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  324 */       return getCurrentName();
/*      */     }
/*  326 */     return super.getValueAsString(null);
/*      */   }
/*      */   
/*      */   public final String getValueAsString(String defValue)
/*      */     throws IOException
/*      */   {
/*  332 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  333 */       if (this._tokenIncomplete) {
/*  334 */         this._tokenIncomplete = false;
/*  335 */         _finishString();
/*      */       }
/*  337 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  339 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  340 */       return getCurrentName();
/*      */     }
/*  342 */     return super.getValueAsString(defValue);
/*      */   }
/*      */   
/*      */   protected final String _getText2(JsonToken t) {
/*  346 */     if (t == null) {
/*  347 */       return null;
/*      */     }
/*  349 */     switch (t.id()) {
/*      */     case 5: 
/*  351 */       return this._parsingContext.getCurrentName();
/*      */     
/*      */ 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*  357 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  359 */     return t.asString();
/*      */   }
/*      */   
/*      */ 
/*      */   public final char[] getTextCharacters()
/*      */     throws IOException
/*      */   {
/*  366 */     if (this._currToken != null) {
/*  367 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  369 */         if (!this._nameCopied) {
/*  370 */           String name = this._parsingContext.getCurrentName();
/*  371 */           int nameLen = name.length();
/*  372 */           if (this._nameCopyBuffer == null) {
/*  373 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  374 */           } else if (this._nameCopyBuffer.length < nameLen) {
/*  375 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  377 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  378 */           this._nameCopied = true;
/*      */         }
/*  380 */         return this._nameCopyBuffer;
/*      */       case 6: 
/*  382 */         if (this._tokenIncomplete) {
/*  383 */           this._tokenIncomplete = false;
/*  384 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  389 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*  391 */       return this._currToken.asCharArray();
/*      */     }
/*      */     
/*  394 */     return null;
/*      */   }
/*      */   
/*      */   public final int getTextLength()
/*      */     throws IOException
/*      */   {
/*  400 */     if (this._currToken != null) {
/*  401 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  403 */         return this._parsingContext.getCurrentName().length();
/*      */       case 6: 
/*  405 */         if (this._tokenIncomplete) {
/*  406 */           this._tokenIncomplete = false;
/*  407 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  412 */         return this._textBuffer.size();
/*      */       }
/*  414 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */     
/*  417 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int getTextOffset()
/*      */     throws IOException
/*      */   {
/*  424 */     if (this._currToken != null) {
/*  425 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  427 */         return 0;
/*      */       case 6: 
/*  429 */         if (this._tokenIncomplete) {
/*  430 */           this._tokenIncomplete = false;
/*  431 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  436 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */       
/*      */     }
/*  440 */     return 0;
/*      */   }
/*      */   
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/*  446 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  448 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  453 */     if (this._tokenIncomplete) {
/*      */       try {
/*  455 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  457 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  462 */       this._tokenIncomplete = false;
/*      */     }
/*  464 */     else if (this._binaryValue == null)
/*      */     {
/*  466 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  467 */       _decodeBase64(getText(), builder, b64variant);
/*  468 */       this._binaryValue = builder.toByteArray();
/*      */     }
/*      */     
/*  471 */     return this._binaryValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */     throws IOException
/*      */   {
/*  478 */     if ((!this._tokenIncomplete) || (this._currToken != JsonToken.VALUE_STRING)) {
/*  479 */       byte[] b = getBinaryValue(b64variant);
/*  480 */       out.write(b);
/*  481 */       return b.length;
/*      */     }
/*      */     
/*  484 */     byte[] buf = this._ioContext.allocBase64Buffer();
/*      */     try {
/*  486 */       return _readBinary(b64variant, out, buf);
/*      */     } finally {
/*  488 */       this._ioContext.releaseBase64Buffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */   protected int _readBinary(Base64Variant b64variant, OutputStream out, byte[] buffer) throws IOException
/*      */   {
/*  494 */     int outputPtr = 0;
/*  495 */     int outputEnd = buffer.length - 3;
/*  496 */     int outputCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  502 */       if (this._inputPtr >= this._inputEnd) {
/*  503 */         _loadMoreGuaranteed();
/*      */       }
/*  505 */       char ch = this._inputBuffer[(this._inputPtr++)];
/*  506 */       if (ch > ' ') {
/*  507 */         int bits = b64variant.decodeBase64Char(ch);
/*  508 */         if (bits < 0) {
/*  509 */           if (ch == '"') {
/*      */             break;
/*      */           }
/*  512 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/*  513 */           if (bits < 0) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  519 */           if (outputPtr > outputEnd) {
/*  520 */             outputCount += outputPtr;
/*  521 */             out.write(buffer, 0, outputPtr);
/*  522 */             outputPtr = 0;
/*      */           }
/*      */           
/*  525 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/*  529 */           if (this._inputPtr >= this._inputEnd) {
/*  530 */             _loadMoreGuaranteed();
/*      */           }
/*  532 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  533 */           bits = b64variant.decodeBase64Char(ch);
/*  534 */           if (bits < 0) {
/*  535 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/*  537 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/*  540 */           if (this._inputPtr >= this._inputEnd) {
/*  541 */             _loadMoreGuaranteed();
/*      */           }
/*  543 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  544 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/*  547 */           if (bits < 0) {
/*  548 */             if (bits != -2)
/*      */             {
/*  550 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/*  551 */                 decodedData >>= 4;
/*  552 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  553 */                 break;
/*      */               }
/*  555 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/*  557 */             if (bits == -2)
/*      */             {
/*  559 */               if (this._inputPtr >= this._inputEnd) {
/*  560 */                 _loadMoreGuaranteed();
/*      */               }
/*  562 */               ch = this._inputBuffer[(this._inputPtr++)];
/*  563 */               if (!b64variant.usesPaddingChar(ch)) {
/*  564 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/*  567 */               decodedData >>= 4;
/*  568 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  569 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  573 */           decodedData = decodedData << 6 | bits;
/*      */           
/*  575 */           if (this._inputPtr >= this._inputEnd) {
/*  576 */             _loadMoreGuaranteed();
/*      */           }
/*  578 */           ch = this._inputBuffer[(this._inputPtr++)];
/*  579 */           bits = b64variant.decodeBase64Char(ch);
/*  580 */           if (bits < 0) {
/*  581 */             if (bits != -2)
/*      */             {
/*  583 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/*  584 */                 decodedData >>= 2;
/*  585 */                 buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  586 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  587 */                 break;
/*      */               }
/*  589 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/*  591 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  598 */               decodedData >>= 2;
/*  599 */               buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  600 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  601 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  605 */           decodedData = decodedData << 6 | bits;
/*  606 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 16));
/*  607 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  608 */           buffer[(outputPtr++)] = ((byte)decodedData);
/*      */         } } }
/*  610 */     this._tokenIncomplete = false;
/*  611 */     if (outputPtr > 0) {
/*  612 */       outputCount += outputPtr;
/*  613 */       out.write(buffer, 0, outputPtr);
/*      */     }
/*  615 */     return outputCount;
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
/*      */   public final JsonToken nextToken()
/*      */     throws IOException
/*      */   {
/*  635 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  636 */       return _nextAfterName();
/*      */     }
/*      */     
/*      */ 
/*  640 */     this._numTypesValid = 0;
/*  641 */     if (this._tokenIncomplete) {
/*  642 */       _skipString();
/*      */     }
/*  644 */     int i = _skipWSOrEnd();
/*  645 */     if (i < 0)
/*      */     {
/*      */ 
/*  648 */       close();
/*  649 */       return this._currToken = null;
/*      */     }
/*      */     
/*  652 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  655 */     if (i == 93) {
/*  656 */       _updateLocation();
/*  657 */       if (!this._parsingContext.inArray()) {
/*  658 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  660 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  661 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  663 */     if (i == 125) {
/*  664 */       _updateLocation();
/*  665 */       if (!this._parsingContext.inObject()) {
/*  666 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  668 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  669 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */     
/*      */ 
/*  673 */     if (this._parsingContext.expectComma()) {
/*  674 */       i = _skipComma(i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  680 */     boolean inObject = this._parsingContext.inObject();
/*  681 */     if (inObject)
/*      */     {
/*  683 */       _updateNameLocation();
/*  684 */       String name = i == 34 ? _parseName() : _handleOddName(i);
/*  685 */       this._parsingContext.setCurrentName(name);
/*  686 */       this._currToken = JsonToken.FIELD_NAME;
/*  687 */       i = _skipColon();
/*      */     }
/*  689 */     _updateLocation();
/*      */     
/*      */ 
/*      */     JsonToken t;
/*      */     
/*      */ 
/*  695 */     switch (i) {
/*      */     case 34: 
/*  697 */       this._tokenIncomplete = true;
/*  698 */       t = JsonToken.VALUE_STRING;
/*  699 */       break;
/*      */     case 91: 
/*  701 */       if (!inObject) {
/*  702 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  704 */       t = JsonToken.START_ARRAY;
/*  705 */       break;
/*      */     case 123: 
/*  707 */       if (!inObject) {
/*  708 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  710 */       t = JsonToken.START_OBJECT;
/*  711 */       break;
/*      */     
/*      */ 
/*      */     case 125: 
/*  715 */       _reportUnexpectedChar(i, "expected a value");
/*      */     case 116: 
/*  717 */       _matchTrue();
/*  718 */       t = JsonToken.VALUE_TRUE;
/*  719 */       break;
/*      */     case 102: 
/*  721 */       _matchFalse();
/*  722 */       t = JsonToken.VALUE_FALSE;
/*  723 */       break;
/*      */     case 110: 
/*  725 */       _matchNull();
/*  726 */       t = JsonToken.VALUE_NULL;
/*  727 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 45: 
/*  734 */       t = _parseNegNumber();
/*  735 */       break;
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/*  746 */       t = _parsePosNumber(i);
/*  747 */       break;
/*      */     default: 
/*  749 */       t = _handleOddValue(i);
/*      */     }
/*      */     
/*      */     
/*  753 */     if (inObject) {
/*  754 */       this._nextToken = t;
/*  755 */       return this._currToken;
/*      */     }
/*  757 */     this._currToken = t;
/*  758 */     return t;
/*      */   }
/*      */   
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  763 */     this._nameCopied = false;
/*  764 */     JsonToken t = this._nextToken;
/*  765 */     this._nextToken = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  770 */     if (t == JsonToken.START_ARRAY) {
/*  771 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  772 */     } else if (t == JsonToken.START_OBJECT) {
/*  773 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  775 */     return this._currToken = t;
/*      */   }
/*      */   
/*      */   public void finishToken() throws IOException
/*      */   {
/*  780 */     if (this._tokenIncomplete) {
/*  781 */       this._tokenIncomplete = false;
/*  782 */       _finishString();
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
/*      */   public boolean nextFieldName(SerializableString sstr)
/*      */     throws IOException
/*      */   {
/*  798 */     this._numTypesValid = 0;
/*  799 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  800 */       _nextAfterName();
/*  801 */       return false;
/*      */     }
/*  803 */     if (this._tokenIncomplete) {
/*  804 */       _skipString();
/*      */     }
/*  806 */     int i = _skipWSOrEnd();
/*  807 */     if (i < 0) {
/*  808 */       close();
/*  809 */       this._currToken = null;
/*  810 */       return false;
/*      */     }
/*  812 */     this._binaryValue = null;
/*      */     
/*  814 */     if (i == 93) {
/*  815 */       _updateLocation();
/*  816 */       if (!this._parsingContext.inArray()) {
/*  817 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  819 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  820 */       this._currToken = JsonToken.END_ARRAY;
/*  821 */       return false;
/*      */     }
/*  823 */     if (i == 125) {
/*  824 */       _updateLocation();
/*  825 */       if (!this._parsingContext.inObject()) {
/*  826 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  828 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  829 */       this._currToken = JsonToken.END_OBJECT;
/*  830 */       return false;
/*      */     }
/*  832 */     if (this._parsingContext.expectComma()) {
/*  833 */       i = _skipComma(i);
/*      */     }
/*      */     
/*  836 */     if (!this._parsingContext.inObject()) {
/*  837 */       _updateLocation();
/*  838 */       _nextTokenNotInObject(i);
/*  839 */       return false;
/*      */     }
/*      */     
/*  842 */     _updateNameLocation();
/*  843 */     if (i == 34)
/*      */     {
/*  845 */       char[] nameChars = sstr.asQuotedChars();
/*  846 */       int len = nameChars.length;
/*      */       
/*      */ 
/*  849 */       if (this._inputPtr + len + 4 < this._inputEnd)
/*      */       {
/*  851 */         int end = this._inputPtr + len;
/*  852 */         if (this._inputBuffer[end] == '"') {
/*  853 */           int offset = 0;
/*  854 */           int ptr = this._inputPtr;
/*      */           for (;;) {
/*  856 */             if (ptr == end) {
/*  857 */               this._parsingContext.setCurrentName(sstr.getValue());
/*  858 */               _isNextTokenNameYes(_skipColonFast(ptr + 1));
/*  859 */               return true;
/*      */             }
/*  861 */             if (nameChars[offset] != this._inputBuffer[ptr]) {
/*      */               break;
/*      */             }
/*  864 */             offset++;
/*  865 */             ptr++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  870 */     return _isNextTokenNameMaybe(i, sstr.getValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String nextFieldName()
/*      */     throws IOException
/*      */   {
/*  878 */     this._numTypesValid = 0;
/*  879 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  880 */       _nextAfterName();
/*  881 */       return null;
/*      */     }
/*  883 */     if (this._tokenIncomplete) {
/*  884 */       _skipString();
/*      */     }
/*  886 */     int i = _skipWSOrEnd();
/*  887 */     if (i < 0) {
/*  888 */       close();
/*  889 */       this._currToken = null;
/*  890 */       return null;
/*      */     }
/*  892 */     this._binaryValue = null;
/*  893 */     if (i == 93) {
/*  894 */       _updateLocation();
/*  895 */       if (!this._parsingContext.inArray()) {
/*  896 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  898 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  899 */       this._currToken = JsonToken.END_ARRAY;
/*  900 */       return null;
/*      */     }
/*  902 */     if (i == 125) {
/*  903 */       _updateLocation();
/*  904 */       if (!this._parsingContext.inObject()) {
/*  905 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  907 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  908 */       this._currToken = JsonToken.END_OBJECT;
/*  909 */       return null;
/*      */     }
/*  911 */     if (this._parsingContext.expectComma()) {
/*  912 */       i = _skipComma(i);
/*      */     }
/*  914 */     if (!this._parsingContext.inObject()) {
/*  915 */       _updateLocation();
/*  916 */       _nextTokenNotInObject(i);
/*  917 */       return null;
/*      */     }
/*      */     
/*  920 */     _updateNameLocation();
/*  921 */     String name = i == 34 ? _parseName() : _handleOddName(i);
/*  922 */     this._parsingContext.setCurrentName(name);
/*  923 */     this._currToken = JsonToken.FIELD_NAME;
/*  924 */     i = _skipColon();
/*      */     
/*  926 */     _updateLocation();
/*  927 */     if (i == 34) {
/*  928 */       this._tokenIncomplete = true;
/*  929 */       this._nextToken = JsonToken.VALUE_STRING;
/*  930 */       return name;
/*      */     }
/*      */     
/*      */ 
/*      */     JsonToken t;
/*      */     
/*      */ 
/*  937 */     switch (i) {
/*      */     case 45: 
/*  939 */       t = _parseNegNumber();
/*  940 */       break;
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/*  951 */       t = _parsePosNumber(i);
/*  952 */       break;
/*      */     case 102: 
/*  954 */       _matchFalse();
/*  955 */       t = JsonToken.VALUE_FALSE;
/*  956 */       break;
/*      */     case 110: 
/*  958 */       _matchNull();
/*  959 */       t = JsonToken.VALUE_NULL;
/*  960 */       break;
/*      */     case 116: 
/*  962 */       _matchTrue();
/*  963 */       t = JsonToken.VALUE_TRUE;
/*  964 */       break;
/*      */     case 91: 
/*  966 */       t = JsonToken.START_ARRAY;
/*  967 */       break;
/*      */     case 123: 
/*  969 */       t = JsonToken.START_OBJECT;
/*  970 */       break;
/*      */     default: 
/*  972 */       t = _handleOddValue(i);
/*      */     }
/*      */     
/*  975 */     this._nextToken = t;
/*  976 */     return name;
/*      */   }
/*      */   
/*      */   private final void _isNextTokenNameYes(int i) throws IOException
/*      */   {
/*  981 */     this._currToken = JsonToken.FIELD_NAME;
/*  982 */     _updateLocation();
/*      */     
/*  984 */     switch (i) {
/*      */     case 34: 
/*  986 */       this._tokenIncomplete = true;
/*  987 */       this._nextToken = JsonToken.VALUE_STRING;
/*  988 */       return;
/*      */     case 91: 
/*  990 */       this._nextToken = JsonToken.START_ARRAY;
/*  991 */       return;
/*      */     case 123: 
/*  993 */       this._nextToken = JsonToken.START_OBJECT;
/*  994 */       return;
/*      */     case 116: 
/*  996 */       _matchToken("true", 1);
/*  997 */       this._nextToken = JsonToken.VALUE_TRUE;
/*  998 */       return;
/*      */     case 102: 
/* 1000 */       _matchToken("false", 1);
/* 1001 */       this._nextToken = JsonToken.VALUE_FALSE;
/* 1002 */       return;
/*      */     case 110: 
/* 1004 */       _matchToken("null", 1);
/* 1005 */       this._nextToken = JsonToken.VALUE_NULL;
/* 1006 */       return;
/*      */     case 45: 
/* 1008 */       this._nextToken = _parseNegNumber();
/* 1009 */       return;
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/* 1020 */       this._nextToken = _parsePosNumber(i);
/* 1021 */       return;
/*      */     }
/* 1023 */     this._nextToken = _handleOddValue(i);
/*      */   }
/*      */   
/*      */   protected boolean _isNextTokenNameMaybe(int i, String nameToMatch)
/*      */     throws IOException
/*      */   {
/* 1029 */     String name = i == 34 ? _parseName() : _handleOddName(i);
/* 1030 */     this._parsingContext.setCurrentName(name);
/* 1031 */     this._currToken = JsonToken.FIELD_NAME;
/* 1032 */     i = _skipColon();
/* 1033 */     _updateLocation();
/* 1034 */     if (i == 34) {
/* 1035 */       this._tokenIncomplete = true;
/* 1036 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1037 */       return nameToMatch.equals(name);
/*      */     }
/*      */     
/*      */     JsonToken t;
/* 1041 */     switch (i) {
/*      */     case 45: 
/* 1043 */       t = _parseNegNumber();
/* 1044 */       break;
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/* 1055 */       t = _parsePosNumber(i);
/* 1056 */       break;
/*      */     case 102: 
/* 1058 */       _matchFalse();
/* 1059 */       t = JsonToken.VALUE_FALSE;
/* 1060 */       break;
/*      */     case 110: 
/* 1062 */       _matchNull();
/* 1063 */       t = JsonToken.VALUE_NULL;
/* 1064 */       break;
/*      */     case 116: 
/* 1066 */       _matchTrue();
/* 1067 */       t = JsonToken.VALUE_TRUE;
/* 1068 */       break;
/*      */     case 91: 
/* 1070 */       t = JsonToken.START_ARRAY;
/* 1071 */       break;
/*      */     case 123: 
/* 1073 */       t = JsonToken.START_OBJECT;
/* 1074 */       break;
/*      */     default: 
/* 1076 */       t = _handleOddValue(i);
/*      */     }
/*      */     
/* 1079 */     this._nextToken = t;
/* 1080 */     return nameToMatch.equals(name);
/*      */   }
/*      */   
/*      */   private final JsonToken _nextTokenNotInObject(int i) throws IOException
/*      */   {
/* 1085 */     if (i == 34) {
/* 1086 */       this._tokenIncomplete = true;
/* 1087 */       return this._currToken = JsonToken.VALUE_STRING;
/*      */     }
/* 1089 */     switch (i) {
/*      */     case 91: 
/* 1091 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1092 */       return this._currToken = JsonToken.START_ARRAY;
/*      */     case 123: 
/* 1094 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/* 1095 */       return this._currToken = JsonToken.START_OBJECT;
/*      */     case 116: 
/* 1097 */       _matchToken("true", 1);
/* 1098 */       return this._currToken = JsonToken.VALUE_TRUE;
/*      */     case 102: 
/* 1100 */       _matchToken("false", 1);
/* 1101 */       return this._currToken = JsonToken.VALUE_FALSE;
/*      */     case 110: 
/* 1103 */       _matchToken("null", 1);
/* 1104 */       return this._currToken = JsonToken.VALUE_NULL;
/*      */     case 45: 
/* 1106 */       return this._currToken = _parseNegNumber();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 48: 
/*      */     case 49: 
/*      */     case 50: 
/*      */     case 51: 
/*      */     case 52: 
/*      */     case 53: 
/*      */     case 54: 
/*      */     case 55: 
/*      */     case 56: 
/*      */     case 57: 
/* 1121 */       return this._currToken = _parsePosNumber(i);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 44: 
/*      */     case 93: 
/* 1132 */       if (isEnabled(JsonParser.Feature.ALLOW_MISSING_VALUES)) {
/* 1133 */         this._inputPtr -= 1;
/* 1134 */         return this._currToken = JsonToken.VALUE_NULL;
/*      */       }
/*      */       break; }
/* 1137 */     return this._currToken = _handleOddValue(i);
/*      */   }
/*      */   
/*      */ 
/*      */   public final String nextTextValue()
/*      */     throws IOException
/*      */   {
/* 1144 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1145 */       this._nameCopied = false;
/* 1146 */       JsonToken t = this._nextToken;
/* 1147 */       this._nextToken = null;
/* 1148 */       this._currToken = t;
/* 1149 */       if (t == JsonToken.VALUE_STRING) {
/* 1150 */         if (this._tokenIncomplete) {
/* 1151 */           this._tokenIncomplete = false;
/* 1152 */           _finishString();
/*      */         }
/* 1154 */         return this._textBuffer.contentsAsString();
/*      */       }
/* 1156 */       if (t == JsonToken.START_ARRAY) {
/* 1157 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1158 */       } else if (t == JsonToken.START_OBJECT) {
/* 1159 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1161 */       return null;
/*      */     }
/*      */     
/* 1164 */     return nextToken() == JsonToken.VALUE_STRING ? getText() : null;
/*      */   }
/*      */   
/*      */ 
/*      */   public final int nextIntValue(int defaultValue)
/*      */     throws IOException
/*      */   {
/* 1171 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1172 */       this._nameCopied = false;
/* 1173 */       JsonToken t = this._nextToken;
/* 1174 */       this._nextToken = null;
/* 1175 */       this._currToken = t;
/* 1176 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1177 */         return getIntValue();
/*      */       }
/* 1179 */       if (t == JsonToken.START_ARRAY) {
/* 1180 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1181 */       } else if (t == JsonToken.START_OBJECT) {
/* 1182 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1184 */       return defaultValue;
/*      */     }
/*      */     
/* 1187 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public final long nextLongValue(long defaultValue)
/*      */     throws IOException
/*      */   {
/* 1194 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1195 */       this._nameCopied = false;
/* 1196 */       JsonToken t = this._nextToken;
/* 1197 */       this._nextToken = null;
/* 1198 */       this._currToken = t;
/* 1199 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1200 */         return getLongValue();
/*      */       }
/* 1202 */       if (t == JsonToken.START_ARRAY) {
/* 1203 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1204 */       } else if (t == JsonToken.START_OBJECT) {
/* 1205 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1207 */       return defaultValue;
/*      */     }
/*      */     
/* 1210 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public final Boolean nextBooleanValue()
/*      */     throws IOException
/*      */   {
/* 1217 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1218 */       this._nameCopied = false;
/* 1219 */       JsonToken t = this._nextToken;
/* 1220 */       this._nextToken = null;
/* 1221 */       this._currToken = t;
/* 1222 */       if (t == JsonToken.VALUE_TRUE) {
/* 1223 */         return Boolean.TRUE;
/*      */       }
/* 1225 */       if (t == JsonToken.VALUE_FALSE) {
/* 1226 */         return Boolean.FALSE;
/*      */       }
/* 1228 */       if (t == JsonToken.START_ARRAY) {
/* 1229 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1230 */       } else if (t == JsonToken.START_OBJECT) {
/* 1231 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1233 */       return null;
/*      */     }
/* 1235 */     JsonToken t = nextToken();
/* 1236 */     if (t != null) {
/* 1237 */       int id = t.id();
/* 1238 */       if (id == 9) return Boolean.TRUE;
/* 1239 */       if (id == 10) return Boolean.FALSE;
/*      */     }
/* 1241 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonToken _parsePosNumber(int ch)
/*      */     throws IOException
/*      */   {
/* 1272 */     int ptr = this._inputPtr;
/* 1273 */     int startPtr = ptr - 1;
/* 1274 */     int inputLen = this._inputEnd;
/*      */     
/*      */ 
/* 1277 */     if (ch == 48) {
/* 1278 */       return _parseNumber2(false, startPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1287 */     int intLen = 1;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1292 */       if (ptr >= inputLen) {
/* 1293 */         this._inputPtr = startPtr;
/* 1294 */         return _parseNumber2(false, startPtr);
/*      */       }
/* 1296 */       ch = this._inputBuffer[(ptr++)];
/* 1297 */       if ((ch < 48) || (ch > 57)) {
/*      */         break;
/*      */       }
/* 1300 */       intLen++;
/*      */     }
/* 1302 */     if ((ch == 46) || (ch == 101) || (ch == 69)) {
/* 1303 */       this._inputPtr = ptr;
/* 1304 */       return _parseFloat(ch, startPtr, ptr, false, intLen);
/*      */     }
/*      */     
/* 1307 */     ptr--;
/* 1308 */     this._inputPtr = ptr;
/*      */     
/* 1310 */     if (this._parsingContext.inRoot()) {
/* 1311 */       _verifyRootSpace(ch);
/*      */     }
/* 1313 */     int len = ptr - startPtr;
/* 1314 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 1315 */     return resetInt(false, intLen);
/*      */   }
/*      */   
/*      */   private final JsonToken _parseFloat(int ch, int startPtr, int ptr, boolean neg, int intLen)
/*      */     throws IOException
/*      */   {
/* 1321 */     int inputLen = this._inputEnd;
/* 1322 */     int fractLen = 0;
/*      */     
/*      */ 
/* 1325 */     if (ch == 46)
/*      */     {
/*      */       for (;;) {
/* 1328 */         if (ptr >= inputLen) {
/* 1329 */           return _parseNumber2(neg, startPtr);
/*      */         }
/* 1331 */         ch = this._inputBuffer[(ptr++)];
/* 1332 */         if ((ch < 48) || (ch > 57)) {
/*      */           break;
/*      */         }
/* 1335 */         fractLen++;
/*      */       }
/*      */       
/* 1338 */       if (fractLen == 0) {
/* 1339 */         reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/* 1342 */     int expLen = 0;
/* 1343 */     if ((ch == 101) || (ch == 69)) {
/* 1344 */       if (ptr >= inputLen) {
/* 1345 */         this._inputPtr = startPtr;
/* 1346 */         return _parseNumber2(neg, startPtr);
/*      */       }
/*      */       
/* 1349 */       ch = this._inputBuffer[(ptr++)];
/* 1350 */       if ((ch == 45) || (ch == 43)) {
/* 1351 */         if (ptr >= inputLen) {
/* 1352 */           this._inputPtr = startPtr;
/* 1353 */           return _parseNumber2(neg, startPtr);
/*      */         }
/* 1355 */         ch = this._inputBuffer[(ptr++)];
/*      */       }
/* 1357 */       while ((ch <= 57) && (ch >= 48)) {
/* 1358 */         expLen++;
/* 1359 */         if (ptr >= inputLen) {
/* 1360 */           this._inputPtr = startPtr;
/* 1361 */           return _parseNumber2(neg, startPtr);
/*      */         }
/* 1363 */         ch = this._inputBuffer[(ptr++)];
/*      */       }
/*      */       
/* 1366 */       if (expLen == 0) {
/* 1367 */         reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/* 1370 */     ptr--;
/* 1371 */     this._inputPtr = ptr;
/*      */     
/* 1373 */     if (this._parsingContext.inRoot()) {
/* 1374 */       _verifyRootSpace(ch);
/*      */     }
/* 1376 */     int len = ptr - startPtr;
/* 1377 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/*      */     
/* 1379 */     return resetFloat(neg, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */   protected final JsonToken _parseNegNumber() throws IOException
/*      */   {
/* 1384 */     int ptr = this._inputPtr;
/* 1385 */     int startPtr = ptr - 1;
/* 1386 */     int inputLen = this._inputEnd;
/*      */     
/* 1388 */     if (ptr >= inputLen) {
/* 1389 */       return _parseNumber2(true, startPtr);
/*      */     }
/* 1391 */     int ch = this._inputBuffer[(ptr++)];
/*      */     
/* 1393 */     if ((ch > 57) || (ch < 48)) {
/* 1394 */       this._inputPtr = ptr;
/* 1395 */       return _handleInvalidNumberStart(ch, true);
/*      */     }
/*      */     
/* 1398 */     if (ch == 48) {
/* 1399 */       return _parseNumber2(true, startPtr);
/*      */     }
/* 1401 */     int intLen = 1;
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1406 */       if (ptr >= inputLen) {
/* 1407 */         return _parseNumber2(true, startPtr);
/*      */       }
/* 1409 */       ch = this._inputBuffer[(ptr++)];
/* 1410 */       if ((ch < 48) || (ch > 57)) {
/*      */         break;
/*      */       }
/* 1413 */       intLen++;
/*      */     }
/*      */     
/* 1416 */     if ((ch == 46) || (ch == 101) || (ch == 69)) {
/* 1417 */       this._inputPtr = ptr;
/* 1418 */       return _parseFloat(ch, startPtr, ptr, true, intLen);
/*      */     }
/* 1420 */     ptr--;
/* 1421 */     this._inputPtr = ptr;
/* 1422 */     if (this._parsingContext.inRoot()) {
/* 1423 */       _verifyRootSpace(ch);
/*      */     }
/* 1425 */     int len = ptr - startPtr;
/* 1426 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 1427 */     return resetInt(true, intLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final JsonToken _parseNumber2(boolean neg, int startPtr)
/*      */     throws IOException
/*      */   {
/* 1439 */     this._inputPtr = (neg ? startPtr + 1 : startPtr);
/* 1440 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1441 */     int outPtr = 0;
/*      */     
/*      */ 
/* 1444 */     if (neg) {
/* 1445 */       outBuf[(outPtr++)] = '-';
/*      */     }
/*      */     
/*      */ 
/* 1449 */     int intLen = 0;
/* 1450 */     char c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("No digit following minus sign", JsonToken.VALUE_NUMBER_INT);
/*      */     
/* 1452 */     if (c == '0') {
/* 1453 */       c = _verifyNoLeadingZeroes();
/*      */     }
/* 1455 */     boolean eof = false;
/*      */     
/*      */ 
/*      */ 
/* 1459 */     while ((c >= '0') && (c <= '9')) {
/* 1460 */       intLen++;
/* 1461 */       if (outPtr >= outBuf.length) {
/* 1462 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1463 */         outPtr = 0;
/*      */       }
/* 1465 */       outBuf[(outPtr++)] = c;
/* 1466 */       if ((this._inputPtr >= this._inputEnd) && (!_loadMore()))
/*      */       {
/* 1468 */         c = '\000';
/* 1469 */         eof = true;
/* 1470 */         break;
/*      */       }
/* 1472 */       c = this._inputBuffer[(this._inputPtr++)];
/*      */     }
/*      */     
/* 1475 */     if (intLen == 0) {
/* 1476 */       return _handleInvalidNumberStart(c, neg);
/*      */     }
/*      */     
/* 1479 */     int fractLen = 0;
/*      */     
/* 1481 */     if (c == '.') {
/* 1482 */       if (outPtr >= outBuf.length) {
/* 1483 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1484 */         outPtr = 0;
/*      */       }
/* 1486 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */       for (;;)
/*      */       {
/* 1490 */         if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1491 */           eof = true;
/* 1492 */           break;
/*      */         }
/* 1494 */         c = this._inputBuffer[(this._inputPtr++)];
/* 1495 */         if ((c < '0') || (c > '9')) {
/*      */           break;
/*      */         }
/* 1498 */         fractLen++;
/* 1499 */         if (outPtr >= outBuf.length) {
/* 1500 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1501 */           outPtr = 0;
/*      */         }
/* 1503 */         outBuf[(outPtr++)] = c;
/*      */       }
/*      */       
/* 1506 */       if (fractLen == 0) {
/* 1507 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/* 1511 */     int expLen = 0;
/* 1512 */     if ((c == 'e') || (c == 'E')) {
/* 1513 */       if (outPtr >= outBuf.length) {
/* 1514 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1515 */         outPtr = 0;
/*      */       }
/* 1517 */       outBuf[(outPtr++)] = c;
/*      */       
/* 1519 */       c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*      */       
/*      */ 
/* 1522 */       if ((c == '-') || (c == '+')) {
/* 1523 */         if (outPtr >= outBuf.length) {
/* 1524 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1525 */           outPtr = 0;
/*      */         }
/* 1527 */         outBuf[(outPtr++)] = c;
/*      */         
/* 1529 */         c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1534 */       while ((c <= '9') && (c >= '0')) {
/* 1535 */         expLen++;
/* 1536 */         if (outPtr >= outBuf.length) {
/* 1537 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1538 */           outPtr = 0;
/*      */         }
/* 1540 */         outBuf[(outPtr++)] = c;
/* 1541 */         if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1542 */           eof = true;
/* 1543 */           break;
/*      */         }
/* 1545 */         c = this._inputBuffer[(this._inputPtr++)];
/*      */       }
/*      */       
/* 1548 */       if (expLen == 0) {
/* 1549 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1554 */     if (!eof) {
/* 1555 */       this._inputPtr -= 1;
/* 1556 */       if (this._parsingContext.inRoot()) {
/* 1557 */         _verifyRootSpace(c);
/*      */       }
/*      */     }
/* 1560 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1562 */     return reset(neg, intLen, fractLen, expLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final char _verifyNoLeadingZeroes()
/*      */     throws IOException
/*      */   {
/* 1572 */     if (this._inputPtr < this._inputEnd) {
/* 1573 */       char ch = this._inputBuffer[this._inputPtr];
/*      */       
/* 1575 */       if ((ch < '0') || (ch > '9')) {
/* 1576 */         return '0';
/*      */       }
/*      */     }
/*      */     
/* 1580 */     return _verifyNLZ2();
/*      */   }
/*      */   
/*      */   private char _verifyNLZ2() throws IOException
/*      */   {
/* 1585 */     if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1586 */       return '0';
/*      */     }
/* 1588 */     char ch = this._inputBuffer[this._inputPtr];
/* 1589 */     if ((ch < '0') || (ch > '9')) {
/* 1590 */       return '0';
/*      */     }
/* 1592 */     if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
/* 1593 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */     }
/*      */     
/* 1596 */     this._inputPtr += 1;
/* 1597 */     if (ch == '0') {
/* 1598 */       while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 1599 */         ch = this._inputBuffer[this._inputPtr];
/* 1600 */         if ((ch < '0') || (ch > '9')) {
/* 1601 */           return '0';
/*      */         }
/* 1603 */         this._inputPtr += 1;
/* 1604 */         if (ch != '0') {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1609 */     return ch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleInvalidNumberStart(int ch, boolean negative)
/*      */     throws IOException
/*      */   {
/* 1618 */     if (ch == 73) {
/* 1619 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1620 */         (!_loadMore())) {
/* 1621 */         _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
/*      */       }
/*      */       
/* 1624 */       ch = this._inputBuffer[(this._inputPtr++)];
/* 1625 */       if (ch == 78) {
/* 1626 */         String match = negative ? "-INF" : "+INF";
/* 1627 */         _matchToken(match, 3);
/* 1628 */         if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1629 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1631 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1632 */       } else if (ch == 110) {
/* 1633 */         String match = negative ? "-Infinity" : "+Infinity";
/* 1634 */         _matchToken(match, 3);
/* 1635 */         if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1636 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1638 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */       }
/*      */     }
/* 1641 */     reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/* 1642 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _verifyRootSpace(int ch)
/*      */     throws IOException
/*      */   {
/* 1655 */     this._inputPtr += 1;
/* 1656 */     switch (ch) {
/*      */     case 9: 
/*      */     case 32: 
/* 1659 */       return;
/*      */     case 13: 
/* 1661 */       _skipCR();
/* 1662 */       return;
/*      */     case 10: 
/* 1664 */       this._currInputRow += 1;
/* 1665 */       this._currInputRowStart = this._inputPtr;
/* 1666 */       return;
/*      */     }
/* 1668 */     _reportMissingRootWS(ch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String _parseName()
/*      */     throws IOException
/*      */   {
/* 1681 */     int ptr = this._inputPtr;
/* 1682 */     int hash = this._hashSeed;
/* 1683 */     int[] codes = _icLatin1;
/*      */     
/* 1685 */     while (ptr < this._inputEnd) {
/* 1686 */       int ch = this._inputBuffer[ptr];
/* 1687 */       if ((ch < codes.length) && (codes[ch] != 0)) {
/* 1688 */         if (ch != 34) break;
/* 1689 */         int start = this._inputPtr;
/* 1690 */         this._inputPtr = (ptr + 1);
/* 1691 */         return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */       }
/*      */       
/*      */ 
/* 1695 */       hash = hash * 33 + ch;
/* 1696 */       ptr++;
/*      */     }
/* 1698 */     int start = this._inputPtr;
/* 1699 */     this._inputPtr = ptr;
/* 1700 */     return _parseName2(start, hash, 34);
/*      */   }
/*      */   
/*      */   private String _parseName2(int startPtr, int hash, int endChar) throws IOException
/*      */   {
/* 1705 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1710 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1711 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     for (;;)
/*      */     {
/* 1714 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1715 */         (!_loadMore())) {
/* 1716 */         _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */       }
/*      */       
/* 1719 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 1720 */       int i = c;
/* 1721 */       if (i <= 92) {
/* 1722 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1727 */           c = _decodeEscaped();
/* 1728 */         } else if (i <= endChar) {
/* 1729 */           if (i == endChar) {
/*      */             break;
/*      */           }
/* 1732 */           if (i < 32) {
/* 1733 */             _throwUnquotedSpace(i, "name");
/*      */           }
/*      */         }
/*      */       }
/* 1737 */       hash = hash * 33 + c;
/*      */       
/* 1739 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */ 
/* 1742 */       if (outPtr >= outBuf.length) {
/* 1743 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1744 */         outPtr = 0;
/*      */       }
/*      */     }
/* 1747 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1749 */     TextBuffer tb = this._textBuffer;
/* 1750 */     char[] buf = tb.getTextBuffer();
/* 1751 */     int start = tb.getTextOffset();
/* 1752 */     int len = tb.size();
/* 1753 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _handleOddName(int i)
/*      */     throws IOException
/*      */   {
/* 1766 */     if ((i == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 1767 */       return _parseAposName();
/*      */     }
/*      */     
/* 1770 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/* 1771 */       _reportUnexpectedChar(i, "was expecting double-quote to start field name");
/*      */     }
/* 1773 */     int[] codes = CharTypes.getInputCodeLatin1JsNames();
/* 1774 */     int maxCode = codes.length;
/*      */     
/*      */     boolean firstOk;
/*      */     
/*      */     boolean firstOk;
/* 1779 */     if (i < maxCode) {
/* 1780 */       firstOk = codes[i] == 0;
/*      */     } else {
/* 1782 */       firstOk = Character.isJavaIdentifierPart((char)i);
/*      */     }
/* 1784 */     if (!firstOk) {
/* 1785 */       _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/* 1787 */     int ptr = this._inputPtr;
/* 1788 */     int hash = this._hashSeed;
/* 1789 */     int inputLen = this._inputEnd;
/*      */     
/* 1791 */     if (ptr < inputLen) {
/*      */       do {
/* 1793 */         int ch = this._inputBuffer[ptr];
/* 1794 */         if (ch < maxCode) {
/* 1795 */           if (codes[ch] != 0) {
/* 1796 */             int start = this._inputPtr - 1;
/* 1797 */             this._inputPtr = ptr;
/* 1798 */             return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */           }
/* 1800 */         } else if (!Character.isJavaIdentifierPart((char)ch)) {
/* 1801 */           int start = this._inputPtr - 1;
/* 1802 */           this._inputPtr = ptr;
/* 1803 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/* 1805 */         hash = hash * 33 + ch;
/* 1806 */         ptr++;
/* 1807 */       } while (ptr < inputLen);
/*      */     }
/* 1809 */     int start = this._inputPtr - 1;
/* 1810 */     this._inputPtr = ptr;
/* 1811 */     return _handleOddName2(start, hash, codes);
/*      */   }
/*      */   
/*      */   protected String _parseAposName()
/*      */     throws IOException
/*      */   {
/* 1817 */     int ptr = this._inputPtr;
/* 1818 */     int hash = this._hashSeed;
/* 1819 */     int inputLen = this._inputEnd;
/*      */     
/* 1821 */     if (ptr < inputLen) {
/* 1822 */       int[] codes = _icLatin1;
/* 1823 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/* 1826 */         int ch = this._inputBuffer[ptr];
/* 1827 */         if (ch == 39) {
/* 1828 */           int start = this._inputPtr;
/* 1829 */           this._inputPtr = (ptr + 1);
/* 1830 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/* 1832 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/*      */           break;
/*      */         }
/* 1835 */         hash = hash * 33 + ch;
/* 1836 */         ptr++;
/* 1837 */       } while (ptr < inputLen);
/*      */     }
/*      */     
/* 1840 */     int start = this._inputPtr;
/* 1841 */     this._inputPtr = ptr;
/*      */     
/* 1843 */     return _parseName2(start, hash, 39);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleOddValue(int i)
/*      */     throws IOException
/*      */   {
/* 1853 */     switch (i)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 39: 
/* 1860 */       if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
/* 1861 */         return _handleApos();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 93: 
/* 1869 */       if (!this._parsingContext.inArray()) {
/*      */         break;
/*      */       }
/*      */     
/*      */     case 44: 
/* 1874 */       if (isEnabled(JsonParser.Feature.ALLOW_MISSING_VALUES)) {
/* 1875 */         this._inputPtr -= 1;
/* 1876 */         return JsonToken.VALUE_NULL;
/*      */       }
/*      */       break;
/*      */     case 78: 
/* 1880 */       _matchToken("NaN", 1);
/* 1881 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1882 */         return resetAsNaN("NaN", NaN.0D);
/*      */       }
/* 1884 */       _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1885 */       break;
/*      */     case 73: 
/* 1887 */       _matchToken("Infinity", 1);
/* 1888 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 1889 */         return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
/*      */       }
/* 1891 */       _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1892 */       break;
/*      */     case 43: 
/* 1894 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1895 */         (!_loadMore())) {
/* 1896 */         _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
/*      */       }
/*      */       
/* 1899 */       return _handleInvalidNumberStart(this._inputBuffer[(this._inputPtr++)], false);
/*      */     }
/*      */     
/* 1902 */     if (Character.isJavaIdentifierStart(i)) {
/* 1903 */       _reportInvalidToken("" + (char)i, "('true', 'false' or 'null')");
/*      */     }
/*      */     
/* 1906 */     _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/* 1907 */     return null;
/*      */   }
/*      */   
/*      */   protected JsonToken _handleApos() throws IOException
/*      */   {
/* 1912 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1913 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     for (;;)
/*      */     {
/* 1916 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1917 */         (!_loadMore())) {
/* 1918 */         _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */       }
/*      */       
/*      */ 
/* 1922 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 1923 */       int i = c;
/* 1924 */       if (i <= 92) {
/* 1925 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1930 */           c = _decodeEscaped();
/* 1931 */         } else if (i <= 39) {
/* 1932 */           if (i == 39) {
/*      */             break;
/*      */           }
/* 1935 */           if (i < 32) {
/* 1936 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1941 */       if (outPtr >= outBuf.length) {
/* 1942 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1943 */         outPtr = 0;
/*      */       }
/*      */       
/* 1946 */       outBuf[(outPtr++)] = c;
/*      */     }
/* 1948 */     this._textBuffer.setCurrentLength(outPtr);
/* 1949 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */   
/*      */   private String _handleOddName2(int startPtr, int hash, int[] codes) throws IOException
/*      */   {
/* 1954 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/* 1955 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1956 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 1957 */     int maxCode = codes.length;
/*      */     
/*      */ 
/* 1960 */     while ((this._inputPtr < this._inputEnd) || 
/* 1961 */       (_loadMore()))
/*      */     {
/*      */ 
/*      */ 
/* 1965 */       char c = this._inputBuffer[this._inputPtr];
/* 1966 */       int i = c;
/* 1967 */       if (i <= maxCode ? 
/* 1968 */         codes[i] != 0 : 
/*      */         
/*      */ 
/* 1971 */         !Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 1974 */       this._inputPtr += 1;
/* 1975 */       hash = hash * 33 + i;
/*      */       
/* 1977 */       outBuf[(outPtr++)] = c;
/*      */       
/*      */ 
/* 1980 */       if (outPtr >= outBuf.length) {
/* 1981 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1982 */         outPtr = 0;
/*      */       }
/*      */     }
/* 1985 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1987 */     TextBuffer tb = this._textBuffer;
/* 1988 */     char[] buf = tb.getTextBuffer();
/* 1989 */     int start = tb.getTextOffset();
/* 1990 */     int len = tb.size();
/*      */     
/* 1992 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _finishString()
/*      */     throws IOException
/*      */   {
/* 2003 */     int ptr = this._inputPtr;
/* 2004 */     int inputLen = this._inputEnd;
/*      */     
/* 2006 */     if (ptr < inputLen) {
/* 2007 */       int[] codes = _icLatin1;
/* 2008 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/* 2011 */         int ch = this._inputBuffer[ptr];
/* 2012 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/* 2013 */           if (ch != 34) break;
/* 2014 */           this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 2015 */           this._inputPtr = (ptr + 1);
/*      */           
/* 2017 */           return;
/*      */         }
/*      */         
/*      */ 
/* 2021 */         ptr++;
/* 2022 */       } while (ptr < inputLen);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2028 */     this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 2029 */     this._inputPtr = ptr;
/* 2030 */     _finishString2();
/*      */   }
/*      */   
/*      */   protected void _finishString2() throws IOException
/*      */   {
/* 2035 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 2036 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 2037 */     int[] codes = _icLatin1;
/* 2038 */     int maxCode = codes.length;
/*      */     for (;;)
/*      */     {
/* 2041 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2042 */         (!_loadMore())) {
/* 2043 */         _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */       }
/*      */       
/*      */ 
/* 2047 */       char c = this._inputBuffer[(this._inputPtr++)];
/* 2048 */       int i = c;
/* 2049 */       if ((i < maxCode) && (codes[i] != 0)) {
/* 2050 */         if (i == 34)
/*      */           break;
/* 2052 */         if (i == 92)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2057 */           c = _decodeEscaped();
/* 2058 */         } else if (i < 32) {
/* 2059 */           _throwUnquotedSpace(i, "string value");
/*      */         }
/*      */       }
/*      */       
/* 2063 */       if (outPtr >= outBuf.length) {
/* 2064 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2065 */         outPtr = 0;
/*      */       }
/*      */       
/* 2068 */       outBuf[(outPtr++)] = c;
/*      */     }
/* 2070 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _skipString()
/*      */     throws IOException
/*      */   {
/* 2080 */     this._tokenIncomplete = false;
/*      */     
/* 2082 */     int inPtr = this._inputPtr;
/* 2083 */     int inLen = this._inputEnd;
/* 2084 */     char[] inBuf = this._inputBuffer;
/*      */     for (;;)
/*      */     {
/* 2087 */       if (inPtr >= inLen) {
/* 2088 */         this._inputPtr = inPtr;
/* 2089 */         if (!_loadMore()) {
/* 2090 */           _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */         }
/*      */         
/* 2093 */         inPtr = this._inputPtr;
/* 2094 */         inLen = this._inputEnd;
/*      */       }
/* 2096 */       char c = inBuf[(inPtr++)];
/* 2097 */       int i = c;
/* 2098 */       if (i <= 92) {
/* 2099 */         if (i == 92)
/*      */         {
/*      */ 
/* 2102 */           this._inputPtr = inPtr;
/* 2103 */           _decodeEscaped();
/* 2104 */           inPtr = this._inputPtr;
/* 2105 */           inLen = this._inputEnd;
/* 2106 */         } else if (i <= 34) {
/* 2107 */           if (i == 34) {
/* 2108 */             this._inputPtr = inPtr;
/* 2109 */             break;
/*      */           }
/* 2111 */           if (i < 32) {
/* 2112 */             this._inputPtr = inPtr;
/* 2113 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
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
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/* 2131 */     if (((this._inputPtr < this._inputEnd) || (_loadMore())) && 
/* 2132 */       (this._inputBuffer[this._inputPtr] == '\n')) {
/* 2133 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 2136 */     this._currInputRow += 1;
/* 2137 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */   
/*      */   private final int _skipColon() throws IOException
/*      */   {
/* 2142 */     if (this._inputPtr + 4 >= this._inputEnd) {
/* 2143 */       return _skipColon2(false);
/*      */     }
/* 2145 */     char c = this._inputBuffer[this._inputPtr];
/* 2146 */     if (c == ':') {
/* 2147 */       int i = this._inputBuffer[(++this._inputPtr)];
/* 2148 */       if (i > 32) {
/* 2149 */         if ((i == 47) || (i == 35)) {
/* 2150 */           return _skipColon2(true);
/*      */         }
/* 2152 */         this._inputPtr += 1;
/* 2153 */         return i;
/*      */       }
/* 2155 */       if ((i == 32) || (i == 9)) {
/* 2156 */         i = this._inputBuffer[(++this._inputPtr)];
/* 2157 */         if (i > 32) {
/* 2158 */           if ((i == 47) || (i == 35)) {
/* 2159 */             return _skipColon2(true);
/*      */           }
/* 2161 */           this._inputPtr += 1;
/* 2162 */           return i;
/*      */         }
/*      */       }
/* 2165 */       return _skipColon2(true);
/*      */     }
/* 2167 */     if ((c == ' ') || (c == '\t')) {
/* 2168 */       c = this._inputBuffer[(++this._inputPtr)];
/*      */     }
/* 2170 */     if (c == ':') {
/* 2171 */       int i = this._inputBuffer[(++this._inputPtr)];
/* 2172 */       if (i > 32) {
/* 2173 */         if ((i == 47) || (i == 35)) {
/* 2174 */           return _skipColon2(true);
/*      */         }
/* 2176 */         this._inputPtr += 1;
/* 2177 */         return i;
/*      */       }
/* 2179 */       if ((i == 32) || (i == 9)) {
/* 2180 */         i = this._inputBuffer[(++this._inputPtr)];
/* 2181 */         if (i > 32) {
/* 2182 */           if ((i == 47) || (i == 35)) {
/* 2183 */             return _skipColon2(true);
/*      */           }
/* 2185 */           this._inputPtr += 1;
/* 2186 */           return i;
/*      */         }
/*      */       }
/* 2189 */       return _skipColon2(true);
/*      */     }
/* 2191 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final int _skipColon2(boolean gotColon) throws IOException
/*      */   {
/* 2196 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2197 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 2198 */       if (i > 32) {
/* 2199 */         if (i == 47) {
/* 2200 */           _skipComment();
/*      */ 
/*      */         }
/* 2203 */         else if ((i != 35) || 
/* 2204 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2208 */           if (gotColon) {
/* 2209 */             return i;
/*      */           }
/* 2211 */           if (i != 58) {
/* 2212 */             _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */           }
/* 2214 */           gotColon = true;
/*      */         }
/*      */       }
/* 2217 */       else if (i < 32) {
/* 2218 */         if (i == 10) {
/* 2219 */           this._currInputRow += 1;
/* 2220 */           this._currInputRowStart = this._inputPtr;
/* 2221 */         } else if (i == 13) {
/* 2222 */           _skipCR();
/* 2223 */         } else if (i != 9) {
/* 2224 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2228 */     _reportInvalidEOF(" within/between " + this._parsingContext.typeDesc() + " entries", null);
/*      */     
/* 2230 */     return -1;
/*      */   }
/*      */   
/*      */   private final int _skipColonFast(int ptr)
/*      */     throws IOException
/*      */   {
/* 2236 */     int i = this._inputBuffer[(ptr++)];
/* 2237 */     if (i == 58) {
/* 2238 */       i = this._inputBuffer[(ptr++)];
/* 2239 */       if (i > 32) {
/* 2240 */         if ((i != 47) && (i != 35)) {
/* 2241 */           this._inputPtr = ptr;
/* 2242 */           return i;
/*      */         }
/* 2244 */       } else if ((i == 32) || (i == 9)) {
/* 2245 */         i = this._inputBuffer[(ptr++)];
/* 2246 */         if ((i > 32) && 
/* 2247 */           (i != 47) && (i != 35)) {
/* 2248 */           this._inputPtr = ptr;
/* 2249 */           return i;
/*      */         }
/*      */       }
/*      */       
/* 2253 */       this._inputPtr = (ptr - 1);
/* 2254 */       return _skipColon2(true);
/*      */     }
/* 2256 */     if ((i == 32) || (i == 9)) {
/* 2257 */       i = this._inputBuffer[(ptr++)];
/*      */     }
/* 2259 */     boolean gotColon = i == 58;
/* 2260 */     if (gotColon) {
/* 2261 */       i = this._inputBuffer[(ptr++)];
/* 2262 */       if (i > 32) {
/* 2263 */         if ((i != 47) && (i != 35)) {
/* 2264 */           this._inputPtr = ptr;
/* 2265 */           return i;
/*      */         }
/* 2267 */       } else if ((i == 32) || (i == 9)) {
/* 2268 */         i = this._inputBuffer[(ptr++)];
/* 2269 */         if ((i > 32) && 
/* 2270 */           (i != 47) && (i != 35)) {
/* 2271 */           this._inputPtr = ptr;
/* 2272 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2277 */     this._inputPtr = (ptr - 1);
/* 2278 */     return _skipColon2(gotColon);
/*      */   }
/*      */   
/*      */   private final int _skipComma(int i)
/*      */     throws IOException
/*      */   {
/* 2284 */     if (i != 44) {
/* 2285 */       _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
/*      */     }
/* 2287 */     while (this._inputPtr < this._inputEnd) {
/* 2288 */       i = this._inputBuffer[(this._inputPtr++)];
/* 2289 */       if (i > 32) {
/* 2290 */         if ((i == 47) || (i == 35)) {
/* 2291 */           this._inputPtr -= 1;
/* 2292 */           return _skipAfterComma2();
/*      */         }
/* 2294 */         return i;
/*      */       }
/* 2296 */       if (i < 32) {
/* 2297 */         if (i == 10) {
/* 2298 */           this._currInputRow += 1;
/* 2299 */           this._currInputRowStart = this._inputPtr;
/* 2300 */         } else if (i == 13) {
/* 2301 */           _skipCR();
/* 2302 */         } else if (i != 9) {
/* 2303 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2307 */     return _skipAfterComma2();
/*      */   }
/*      */   
/*      */   private final int _skipAfterComma2() throws IOException
/*      */   {
/* 2312 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2313 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 2314 */       if (i > 32) {
/* 2315 */         if (i == 47) {
/* 2316 */           _skipComment();
/*      */ 
/*      */         }
/* 2319 */         else if ((i != 35) || 
/* 2320 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2324 */           return i;
/*      */         }
/* 2326 */       } else if (i < 32) {
/* 2327 */         if (i == 10) {
/* 2328 */           this._currInputRow += 1;
/* 2329 */           this._currInputRowStart = this._inputPtr;
/* 2330 */         } else if (i == 13) {
/* 2331 */           _skipCR();
/* 2332 */         } else if (i != 9) {
/* 2333 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2337 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.typeDesc() + " entries");
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException
/*      */   {
/* 2344 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2345 */       (!_loadMore())) {
/* 2346 */       return _eofAsNextChar();
/*      */     }
/*      */     
/* 2349 */     int i = this._inputBuffer[(this._inputPtr++)];
/* 2350 */     if (i > 32) {
/* 2351 */       if ((i == 47) || (i == 35)) {
/* 2352 */         this._inputPtr -= 1;
/* 2353 */         return _skipWSOrEnd2();
/*      */       }
/* 2355 */       return i;
/*      */     }
/* 2357 */     if (i != 32) {
/* 2358 */       if (i == 10) {
/* 2359 */         this._currInputRow += 1;
/* 2360 */         this._currInputRowStart = this._inputPtr;
/* 2361 */       } else if (i == 13) {
/* 2362 */         _skipCR();
/* 2363 */       } else if (i != 9) {
/* 2364 */         _throwInvalidSpace(i);
/*      */       }
/*      */     }
/*      */     
/* 2368 */     while (this._inputPtr < this._inputEnd) {
/* 2369 */       i = this._inputBuffer[(this._inputPtr++)];
/* 2370 */       if (i > 32) {
/* 2371 */         if ((i == 47) || (i == 35)) {
/* 2372 */           this._inputPtr -= 1;
/* 2373 */           return _skipWSOrEnd2();
/*      */         }
/* 2375 */         return i;
/*      */       }
/* 2377 */       if (i != 32) {
/* 2378 */         if (i == 10) {
/* 2379 */           this._currInputRow += 1;
/* 2380 */           this._currInputRowStart = this._inputPtr;
/* 2381 */         } else if (i == 13) {
/* 2382 */           _skipCR();
/* 2383 */         } else if (i != 9) {
/* 2384 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2388 */     return _skipWSOrEnd2();
/*      */   }
/*      */   
/*      */   private int _skipWSOrEnd2() throws IOException
/*      */   {
/*      */     for (;;) {
/* 2394 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2395 */         (!_loadMore())) {
/* 2396 */         return _eofAsNextChar();
/*      */       }
/*      */       
/* 2399 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 2400 */       if (i > 32) {
/* 2401 */         if (i == 47) {
/* 2402 */           _skipComment();
/*      */ 
/*      */         }
/* 2405 */         else if ((i != 35) || 
/* 2406 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2410 */           return i; }
/* 2411 */       } else if (i != 32) {
/* 2412 */         if (i == 10) {
/* 2413 */           this._currInputRow += 1;
/* 2414 */           this._currInputRowStart = this._inputPtr;
/* 2415 */         } else if (i == 13) {
/* 2416 */           _skipCR();
/* 2417 */         } else if (i != 9) {
/* 2418 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _skipComment() throws IOException
/*      */   {
/* 2426 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/* 2427 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */     
/* 2430 */     if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 2431 */       _reportInvalidEOF(" in a comment", null);
/*      */     }
/* 2433 */     char c = this._inputBuffer[(this._inputPtr++)];
/* 2434 */     if (c == '/') {
/* 2435 */       _skipLine();
/* 2436 */     } else if (c == '*') {
/* 2437 */       _skipCComment();
/*      */     } else {
/* 2439 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */     }
/*      */   }
/*      */   
/*      */   private void _skipCComment()
/*      */     throws IOException
/*      */   {
/* 2446 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2447 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 2448 */       if (i <= 42) {
/* 2449 */         if (i == 42) {
/* 2450 */           if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/*      */             break;
/*      */           }
/* 2453 */           if (this._inputBuffer[this._inputPtr] == '/') {
/* 2454 */             this._inputPtr += 1;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/* 2459 */         else if (i < 32) {
/* 2460 */           if (i == 10) {
/* 2461 */             this._currInputRow += 1;
/* 2462 */             this._currInputRowStart = this._inputPtr;
/* 2463 */           } else if (i == 13) {
/* 2464 */             _skipCR();
/* 2465 */           } else if (i != 9) {
/* 2466 */             _throwInvalidSpace(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2471 */     _reportInvalidEOF(" in a comment", null);
/*      */   }
/*      */   
/*      */   private boolean _skipYAMLComment() throws IOException
/*      */   {
/* 2476 */     if (!isEnabled(JsonParser.Feature.ALLOW_YAML_COMMENTS)) {
/* 2477 */       return false;
/*      */     }
/* 2479 */     _skipLine();
/* 2480 */     return true;
/*      */   }
/*      */   
/*      */   private void _skipLine()
/*      */     throws IOException
/*      */   {
/* 2486 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2487 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 2488 */       if (i < 32) {
/* 2489 */         if (i == 10) {
/* 2490 */           this._currInputRow += 1;
/* 2491 */           this._currInputRowStart = this._inputPtr;
/* 2492 */           break; }
/* 2493 */         if (i == 13) {
/* 2494 */           _skipCR();
/* 2495 */           break; }
/* 2496 */         if (i != 9) {
/* 2497 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected char _decodeEscaped()
/*      */     throws IOException
/*      */   {
/* 2506 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2507 */       (!_loadMore())) {
/* 2508 */       _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */     }
/*      */     
/* 2511 */     char c = this._inputBuffer[(this._inputPtr++)];
/*      */     
/* 2513 */     switch (c)
/*      */     {
/*      */     case 'b': 
/* 2516 */       return '\b';
/*      */     case 't': 
/* 2518 */       return '\t';
/*      */     case 'n': 
/* 2520 */       return '\n';
/*      */     case 'f': 
/* 2522 */       return '\f';
/*      */     case 'r': 
/* 2524 */       return '\r';
/*      */     
/*      */ 
/*      */     case '"': 
/*      */     case '/': 
/*      */     case '\\': 
/* 2530 */       return c;
/*      */     
/*      */     case 'u': 
/*      */       break;
/*      */     
/*      */     default: 
/* 2536 */       return _handleUnrecognizedCharacterEscape(c);
/*      */     }
/*      */     
/*      */     
/* 2540 */     int value = 0;
/* 2541 */     for (int i = 0; i < 4; i++) {
/* 2542 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2543 */         (!_loadMore())) {
/* 2544 */         _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */       }
/*      */       
/* 2547 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 2548 */       int digit = CharTypes.charToHex(ch);
/* 2549 */       if (digit < 0) {
/* 2550 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 2552 */       value = value << 4 | digit;
/*      */     }
/* 2554 */     return (char)value;
/*      */   }
/*      */   
/*      */   private final void _matchTrue() throws IOException {
/* 2558 */     int ptr = this._inputPtr;
/* 2559 */     if (ptr + 3 < this._inputEnd) {
/* 2560 */       char[] b = this._inputBuffer;
/* 2561 */       if ((b[ptr] == 'r') && (b[(++ptr)] == 'u') && (b[(++ptr)] == 'e')) {
/* 2562 */         char c = b[(++ptr)];
/* 2563 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2564 */           this._inputPtr = ptr;
/* 2565 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2570 */     _matchToken("true", 1);
/*      */   }
/*      */   
/*      */   private final void _matchFalse() throws IOException {
/* 2574 */     int ptr = this._inputPtr;
/* 2575 */     if (ptr + 4 < this._inputEnd) {
/* 2576 */       char[] b = this._inputBuffer;
/* 2577 */       if ((b[ptr] == 'a') && (b[(++ptr)] == 'l') && (b[(++ptr)] == 's') && (b[(++ptr)] == 'e')) {
/* 2578 */         char c = b[(++ptr)];
/* 2579 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2580 */           this._inputPtr = ptr;
/* 2581 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2586 */     _matchToken("false", 1);
/*      */   }
/*      */   
/*      */   private final void _matchNull() throws IOException {
/* 2590 */     int ptr = this._inputPtr;
/* 2591 */     if (ptr + 3 < this._inputEnd) {
/* 2592 */       char[] b = this._inputBuffer;
/* 2593 */       if ((b[ptr] == 'u') && (b[(++ptr)] == 'l') && (b[(++ptr)] == 'l')) {
/* 2594 */         char c = b[(++ptr)];
/* 2595 */         if ((c < '0') || (c == ']') || (c == '}')) {
/* 2596 */           this._inputPtr = ptr;
/* 2597 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2602 */     _matchToken("null", 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final void _matchToken(String matchStr, int i)
/*      */     throws IOException
/*      */   {
/* 2610 */     int len = matchStr.length();
/*      */     do
/*      */     {
/* 2613 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2614 */         (!_loadMore())) {
/* 2615 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/*      */       
/* 2618 */       if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
/* 2619 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2621 */       this._inputPtr += 1;
/* 2622 */       i++; } while (i < len);
/*      */     
/*      */ 
/* 2625 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2626 */       (!_loadMore())) {
/* 2627 */       return;
/*      */     }
/*      */     
/* 2630 */     char c = this._inputBuffer[this._inputPtr];
/* 2631 */     if ((c < '0') || (c == ']') || (c == '}')) {
/* 2632 */       return;
/*      */     }
/*      */     
/* 2635 */     if (Character.isJavaIdentifierPart(c)) {
/* 2636 */       _reportInvalidToken(matchStr.substring(0, i));
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
/*      */   protected byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/* 2654 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2661 */       if (this._inputPtr >= this._inputEnd) {
/* 2662 */         _loadMoreGuaranteed();
/*      */       }
/* 2664 */       char ch = this._inputBuffer[(this._inputPtr++)];
/* 2665 */       if (ch > ' ') {
/* 2666 */         int bits = b64variant.decodeBase64Char(ch);
/* 2667 */         if (bits < 0) {
/* 2668 */           if (ch == '"') {
/* 2669 */             return builder.toByteArray();
/*      */           }
/* 2671 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/* 2672 */           if (bits < 0) {}
/*      */         }
/*      */         else
/*      */         {
/* 2676 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/* 2680 */           if (this._inputPtr >= this._inputEnd) {
/* 2681 */             _loadMoreGuaranteed();
/*      */           }
/* 2683 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2684 */           bits = b64variant.decodeBase64Char(ch);
/* 2685 */           if (bits < 0) {
/* 2686 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/* 2688 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/* 2691 */           if (this._inputPtr >= this._inputEnd) {
/* 2692 */             _loadMoreGuaranteed();
/*      */           }
/* 2694 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2695 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/* 2698 */           if (bits < 0) {
/* 2699 */             if (bits != -2)
/*      */             {
/* 2701 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/* 2702 */                 decodedData >>= 4;
/* 2703 */                 builder.append(decodedData);
/* 2704 */                 return builder.toByteArray();
/*      */               }
/* 2706 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/* 2708 */             if (bits == -2)
/*      */             {
/* 2710 */               if (this._inputPtr >= this._inputEnd) {
/* 2711 */                 _loadMoreGuaranteed();
/*      */               }
/* 2713 */               ch = this._inputBuffer[(this._inputPtr++)];
/* 2714 */               if (!b64variant.usesPaddingChar(ch)) {
/* 2715 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/* 2718 */               decodedData >>= 4;
/* 2719 */               builder.append(decodedData);
/* 2720 */               continue;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2725 */           decodedData = decodedData << 6 | bits;
/*      */           
/* 2727 */           if (this._inputPtr >= this._inputEnd) {
/* 2728 */             _loadMoreGuaranteed();
/*      */           }
/* 2730 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 2731 */           bits = b64variant.decodeBase64Char(ch);
/* 2732 */           if (bits < 0) {
/* 2733 */             if (bits != -2)
/*      */             {
/* 2735 */               if ((ch == '"') && (!b64variant.usesPadding())) {
/* 2736 */                 decodedData >>= 2;
/* 2737 */                 builder.appendTwoBytes(decodedData);
/* 2738 */                 return builder.toByteArray();
/*      */               }
/* 2740 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/* 2742 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2748 */               decodedData >>= 2;
/* 2749 */               builder.appendTwoBytes(decodedData);
/* 2750 */               continue;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2755 */           decodedData = decodedData << 6 | bits;
/* 2756 */           builder.appendThreeBytes(decodedData);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getTokenLocation()
/*      */   {
/* 2769 */     Object src = this._ioContext.getSourceReference();
/* 2770 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 2771 */       long total = this._currInputProcessed + (this._nameStartOffset - 1L);
/* 2772 */       return new JsonLocation(src, -1L, total, this._nameStartRow, this._nameStartCol);
/*      */     }
/*      */     
/* 2775 */     return new JsonLocation(src, -1L, this._tokenInputTotal - 1L, this._tokenInputRow, this._tokenInputCol);
/*      */   }
/*      */   
/*      */ 
/*      */   public JsonLocation getCurrentLocation()
/*      */   {
/* 2781 */     int col = this._inputPtr - this._currInputRowStart + 1;
/* 2782 */     return new JsonLocation(this._ioContext.getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, col);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _updateLocation()
/*      */   {
/* 2790 */     int ptr = this._inputPtr;
/* 2791 */     this._tokenInputTotal = (this._currInputProcessed + ptr);
/* 2792 */     this._tokenInputRow = this._currInputRow;
/* 2793 */     this._tokenInputCol = (ptr - this._currInputRowStart);
/*      */   }
/*      */   
/*      */ 
/*      */   private final void _updateNameLocation()
/*      */   {
/* 2799 */     int ptr = this._inputPtr;
/* 2800 */     this._nameStartOffset = ptr;
/* 2801 */     this._nameStartRow = this._currInputRow;
/* 2802 */     this._nameStartCol = (ptr - this._currInputRowStart);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportInvalidToken(String matchedPart)
/*      */     throws IOException
/*      */   {
/* 2812 */     _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
/*      */   }
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart, String msg) throws IOException
/*      */   {
/* 2817 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2822 */     int maxTokenLength = 256;
/* 2823 */     while ((sb.length() < 256) && (
/* 2824 */       (this._inputPtr < this._inputEnd) || 
/* 2825 */       (_loadMore())))
/*      */     {
/*      */ 
/*      */ 
/* 2829 */       char c = this._inputBuffer[this._inputPtr];
/* 2830 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 2833 */       this._inputPtr += 1;
/* 2834 */       sb.append(c);
/*      */     }
/* 2836 */     if (sb.length() == 256) {
/* 2837 */       sb.append("...");
/*      */     }
/* 2839 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\ReaderBasedJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */