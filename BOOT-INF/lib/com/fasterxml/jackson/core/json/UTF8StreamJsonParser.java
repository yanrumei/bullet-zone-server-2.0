/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.base.ParserBase;
/*      */ import com.fasterxml.jackson.core.io.CharTypes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ public class UTF8StreamJsonParser extends ParserBase
/*      */ {
/*      */   static final byte BYTE_LF = 10;
/*   25 */   private static final int[] _icUTF8 = ;
/*      */   
/*      */ 
/*      */ 
/*   29 */   protected static final int[] _icLatin1 = CharTypes.getInputCodeLatin1();
/*      */   
/*      */ 
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
/*      */ 
/*      */ 
/*      */   protected final ByteQuadsCanonicalizer _symbols;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   58 */   protected int[] _quadBuffer = new int[16];
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
/*      */   private int _quad1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _nameStartOffset;
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
/*      */   protected InputStream _inputStream;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _inputBuffer;
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
/*      */ 
/*      */   public UTF8StreamJsonParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, ByteQuadsCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*      */   {
/*  133 */     super(ctxt, features);
/*  134 */     this._inputStream = in;
/*  135 */     this._objectCodec = codec;
/*  136 */     this._symbols = sym;
/*  137 */     this._inputBuffer = inputBuffer;
/*  138 */     this._inputPtr = start;
/*  139 */     this._inputEnd = end;
/*  140 */     this._currInputRowStart = start;
/*      */     
/*  142 */     this._currInputProcessed = (-start);
/*  143 */     this._bufferRecyclable = bufferRecyclable;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec()
/*      */   {
/*  148 */     return this._objectCodec;
/*      */   }
/*      */   
/*      */   public void setCodec(ObjectCodec c)
/*      */   {
/*  153 */     this._objectCodec = c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int releaseBuffered(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  165 */     int count = this._inputEnd - this._inputPtr;
/*  166 */     if (count < 1) {
/*  167 */       return 0;
/*      */     }
/*      */     
/*  170 */     int origPtr = this._inputPtr;
/*  171 */     out.write(this._inputBuffer, origPtr, count);
/*  172 */     return count;
/*      */   }
/*      */   
/*      */   public Object getInputSource()
/*      */   {
/*  177 */     return this._inputStream;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _loadMore()
/*      */     throws IOException
/*      */   {
/*  188 */     int bufSize = this._inputEnd;
/*      */     
/*  190 */     this._currInputProcessed += this._inputEnd;
/*  191 */     this._currInputRowStart -= this._inputEnd;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  196 */     this._nameStartOffset -= bufSize;
/*      */     
/*  198 */     if (this._inputStream != null) {
/*  199 */       int space = this._inputBuffer.length;
/*  200 */       if (space == 0) {
/*  201 */         return false;
/*      */       }
/*      */       
/*  204 */       int count = this._inputStream.read(this._inputBuffer, 0, space);
/*  205 */       if (count > 0) {
/*  206 */         this._inputPtr = 0;
/*  207 */         this._inputEnd = count;
/*  208 */         return true;
/*      */       }
/*      */       
/*  211 */       _closeInput();
/*      */       
/*  213 */       if (count == 0) {
/*  214 */         throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
/*      */       }
/*      */     }
/*  217 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _loadToHaveAtLeast(int minAvailable)
/*      */     throws IOException
/*      */   {
/*  227 */     if (this._inputStream == null) {
/*  228 */       return false;
/*      */     }
/*      */     
/*  231 */     int amount = this._inputEnd - this._inputPtr;
/*  232 */     if ((amount > 0) && (this._inputPtr > 0)) {
/*  233 */       int ptr = this._inputPtr;
/*      */       
/*  235 */       this._currInputProcessed += ptr;
/*  236 */       this._currInputRowStart -= ptr;
/*      */       
/*      */ 
/*  239 */       this._nameStartOffset -= ptr;
/*      */       
/*  241 */       System.arraycopy(this._inputBuffer, ptr, this._inputBuffer, 0, amount);
/*  242 */       this._inputEnd = amount;
/*      */     } else {
/*  244 */       this._inputEnd = 0;
/*      */     }
/*  246 */     this._inputPtr = 0;
/*  247 */     while (this._inputEnd < minAvailable) {
/*  248 */       int count = this._inputStream.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
/*  249 */       if (count < 1)
/*      */       {
/*  251 */         _closeInput();
/*      */         
/*  253 */         if (count == 0) {
/*  254 */           throw new IOException("InputStream.read() returned 0 characters when trying to read " + amount + " bytes");
/*      */         }
/*  256 */         return false;
/*      */       }
/*  258 */       this._inputEnd += count;
/*      */     }
/*  260 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _closeInput()
/*      */     throws IOException
/*      */   {
/*  270 */     if (this._inputStream != null) {
/*  271 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/*  272 */         this._inputStream.close();
/*      */       }
/*  274 */       this._inputStream = null;
/*      */     }
/*      */   }
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
/*  287 */     super._releaseBuffers();
/*      */     
/*  289 */     this._symbols.release();
/*  290 */     if (this._bufferRecyclable) {
/*  291 */       byte[] buf = this._inputBuffer;
/*  292 */       if (buf != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  297 */         this._inputBuffer = ByteArrayBuilder.NO_BYTES;
/*  298 */         this._ioContext.releaseReadIOBuffer(buf);
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
/*      */   public String getText()
/*      */     throws IOException
/*      */   {
/*  312 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  313 */       if (this._tokenIncomplete) {
/*  314 */         this._tokenIncomplete = false;
/*  315 */         return _finishAndReturnString();
/*      */       }
/*  317 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  319 */     return _getText2(this._currToken);
/*      */   }
/*      */   
/*      */   public int getText(Writer writer)
/*      */     throws IOException
/*      */   {
/*  325 */     JsonToken t = this._currToken;
/*  326 */     if (t == JsonToken.VALUE_STRING) {
/*  327 */       if (this._tokenIncomplete) {
/*  328 */         this._tokenIncomplete = false;
/*  329 */         _finishString();
/*      */       }
/*  331 */       return this._textBuffer.contentsToWriter(writer);
/*      */     }
/*  333 */     if (t == JsonToken.FIELD_NAME) {
/*  334 */       String n = this._parsingContext.getCurrentName();
/*  335 */       writer.write(n);
/*  336 */       return n.length();
/*      */     }
/*  338 */     if (t != null) {
/*  339 */       if (t.isNumeric()) {
/*  340 */         return this._textBuffer.contentsToWriter(writer);
/*      */       }
/*  342 */       char[] ch = t.asCharArray();
/*  343 */       writer.write(ch);
/*  344 */       return ch.length;
/*      */     }
/*  346 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getValueAsString()
/*      */     throws IOException
/*      */   {
/*  355 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  356 */       if (this._tokenIncomplete) {
/*  357 */         this._tokenIncomplete = false;
/*  358 */         return _finishAndReturnString();
/*      */       }
/*  360 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  362 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  363 */       return getCurrentName();
/*      */     }
/*  365 */     return super.getValueAsString(null);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getValueAsString(String defValue)
/*      */     throws IOException
/*      */   {
/*  372 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  373 */       if (this._tokenIncomplete) {
/*  374 */         this._tokenIncomplete = false;
/*  375 */         return _finishAndReturnString();
/*      */       }
/*  377 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  379 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  380 */       return getCurrentName();
/*      */     }
/*  382 */     return super.getValueAsString(defValue);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getValueAsInt()
/*      */     throws IOException
/*      */   {
/*  389 */     JsonToken t = this._currToken;
/*  390 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*      */     {
/*  392 */       if ((this._numTypesValid & 0x1) == 0) {
/*  393 */         if (this._numTypesValid == 0) {
/*  394 */           return _parseIntValue();
/*      */         }
/*  396 */         if ((this._numTypesValid & 0x1) == 0) {
/*  397 */           convertNumberToInt();
/*      */         }
/*      */       }
/*  400 */       return this._numberInt;
/*      */     }
/*  402 */     return super.getValueAsInt(0);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getValueAsInt(int defValue)
/*      */     throws IOException
/*      */   {
/*  409 */     JsonToken t = this._currToken;
/*  410 */     if ((t == JsonToken.VALUE_NUMBER_INT) || (t == JsonToken.VALUE_NUMBER_FLOAT))
/*      */     {
/*  412 */       if ((this._numTypesValid & 0x1) == 0) {
/*  413 */         if (this._numTypesValid == 0) {
/*  414 */           return _parseIntValue();
/*      */         }
/*  416 */         if ((this._numTypesValid & 0x1) == 0) {
/*  417 */           convertNumberToInt();
/*      */         }
/*      */       }
/*  420 */       return this._numberInt;
/*      */     }
/*  422 */     return super.getValueAsInt(defValue);
/*      */   }
/*      */   
/*      */   protected final String _getText2(JsonToken t)
/*      */   {
/*  427 */     if (t == null) {
/*  428 */       return null;
/*      */     }
/*  430 */     switch (t.id()) {
/*      */     case 5: 
/*  432 */       return this._parsingContext.getCurrentName();
/*      */     
/*      */ 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*  438 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  440 */     return t.asString();
/*      */   }
/*      */   
/*      */ 
/*      */   public char[] getTextCharacters()
/*      */     throws IOException
/*      */   {
/*  447 */     if (this._currToken != null) {
/*  448 */       switch (this._currToken.id())
/*      */       {
/*      */       case 5: 
/*  451 */         if (!this._nameCopied) {
/*  452 */           String name = this._parsingContext.getCurrentName();
/*  453 */           int nameLen = name.length();
/*  454 */           if (this._nameCopyBuffer == null) {
/*  455 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  456 */           } else if (this._nameCopyBuffer.length < nameLen) {
/*  457 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  459 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  460 */           this._nameCopied = true;
/*      */         }
/*  462 */         return this._nameCopyBuffer;
/*      */       
/*      */       case 6: 
/*  465 */         if (this._tokenIncomplete) {
/*  466 */           this._tokenIncomplete = false;
/*  467 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  472 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*      */       
/*  475 */       return this._currToken.asCharArray();
/*      */     }
/*      */     
/*  478 */     return null;
/*      */   }
/*      */   
/*      */   public int getTextLength()
/*      */     throws IOException
/*      */   {
/*  484 */     if (this._currToken != null) {
/*  485 */       switch (this._currToken.id())
/*      */       {
/*      */       case 5: 
/*  488 */         return this._parsingContext.getCurrentName().length();
/*      */       case 6: 
/*  490 */         if (this._tokenIncomplete) {
/*  491 */           this._tokenIncomplete = false;
/*  492 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  497 */         return this._textBuffer.size();
/*      */       }
/*      */       
/*  500 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */     
/*  503 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getTextOffset()
/*      */     throws IOException
/*      */   {
/*  510 */     if (this._currToken != null) {
/*  511 */       switch (this._currToken.id()) {
/*      */       case 5: 
/*  513 */         return 0;
/*      */       case 6: 
/*  515 */         if (this._tokenIncomplete) {
/*  516 */           this._tokenIncomplete = false;
/*  517 */           _finishString();
/*      */         }
/*      */       
/*      */       case 7: 
/*      */       case 8: 
/*  522 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */       
/*      */     }
/*  526 */     return 0;
/*      */   }
/*      */   
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/*  532 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  534 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  539 */     if (this._tokenIncomplete) {
/*      */       try {
/*  541 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  543 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  548 */       this._tokenIncomplete = false;
/*      */     }
/*  550 */     else if (this._binaryValue == null)
/*      */     {
/*  552 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  553 */       _decodeBase64(getText(), builder, b64variant);
/*  554 */       this._binaryValue = builder.toByteArray();
/*      */     }
/*      */     
/*  557 */     return this._binaryValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public int readBinaryValue(Base64Variant b64variant, OutputStream out)
/*      */     throws IOException
/*      */   {
/*  564 */     if ((!this._tokenIncomplete) || (this._currToken != JsonToken.VALUE_STRING)) {
/*  565 */       byte[] b = getBinaryValue(b64variant);
/*  566 */       out.write(b);
/*  567 */       return b.length;
/*      */     }
/*      */     
/*  570 */     byte[] buf = this._ioContext.allocBase64Buffer();
/*      */     try {
/*  572 */       return _readBinary(b64variant, out, buf);
/*      */     } finally {
/*  574 */       this._ioContext.releaseBase64Buffer(buf);
/*      */     }
/*      */   }
/*      */   
/*      */   protected int _readBinary(Base64Variant b64variant, OutputStream out, byte[] buffer)
/*      */     throws IOException
/*      */   {
/*  581 */     int outputPtr = 0;
/*  582 */     int outputEnd = buffer.length - 3;
/*  583 */     int outputCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/*  589 */       if (this._inputPtr >= this._inputEnd) {
/*  590 */         _loadMoreGuaranteed();
/*      */       }
/*  592 */       int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  593 */       if (ch > 32) {
/*  594 */         int bits = b64variant.decodeBase64Char(ch);
/*  595 */         if (bits < 0) {
/*  596 */           if (ch == 34) {
/*      */             break;
/*      */           }
/*  599 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/*  600 */           if (bits < 0) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  606 */           if (outputPtr > outputEnd) {
/*  607 */             outputCount += outputPtr;
/*  608 */             out.write(buffer, 0, outputPtr);
/*  609 */             outputPtr = 0;
/*      */           }
/*      */           
/*  612 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/*  616 */           if (this._inputPtr >= this._inputEnd) {
/*  617 */             _loadMoreGuaranteed();
/*      */           }
/*  619 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  620 */           bits = b64variant.decodeBase64Char(ch);
/*  621 */           if (bits < 0) {
/*  622 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/*  624 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/*  627 */           if (this._inputPtr >= this._inputEnd) {
/*  628 */             _loadMoreGuaranteed();
/*      */           }
/*  630 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  631 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/*  634 */           if (bits < 0) {
/*  635 */             if (bits != -2)
/*      */             {
/*  637 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/*  638 */                 decodedData >>= 4;
/*  639 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  640 */                 break;
/*      */               }
/*  642 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/*  644 */             if (bits == -2)
/*      */             {
/*  646 */               if (this._inputPtr >= this._inputEnd) {
/*  647 */                 _loadMoreGuaranteed();
/*      */               }
/*  649 */               ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  650 */               if (!b64variant.usesPaddingChar(ch)) {
/*  651 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/*  654 */               decodedData >>= 4;
/*  655 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  656 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  660 */           decodedData = decodedData << 6 | bits;
/*      */           
/*  662 */           if (this._inputPtr >= this._inputEnd) {
/*  663 */             _loadMoreGuaranteed();
/*      */           }
/*  665 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  666 */           bits = b64variant.decodeBase64Char(ch);
/*  667 */           if (bits < 0) {
/*  668 */             if (bits != -2)
/*      */             {
/*  670 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/*  671 */                 decodedData >>= 2;
/*  672 */                 buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  673 */                 buffer[(outputPtr++)] = ((byte)decodedData);
/*  674 */                 break;
/*      */               }
/*  676 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/*  678 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  685 */               decodedData >>= 2;
/*  686 */               buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  687 */               buffer[(outputPtr++)] = ((byte)decodedData);
/*  688 */               continue;
/*      */             }
/*      */           }
/*      */           
/*  692 */           decodedData = decodedData << 6 | bits;
/*  693 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 16));
/*  694 */           buffer[(outputPtr++)] = ((byte)(decodedData >> 8));
/*  695 */           buffer[(outputPtr++)] = ((byte)decodedData);
/*      */         } } }
/*  697 */     this._tokenIncomplete = false;
/*  698 */     if (outputPtr > 0) {
/*  699 */       outputCount += outputPtr;
/*  700 */       out.write(buffer, 0, outputPtr);
/*      */     }
/*  702 */     return outputCount;
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
/*      */   public JsonToken nextToken()
/*      */     throws IOException
/*      */   {
/*  722 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  723 */       return _nextAfterName();
/*      */     }
/*      */     
/*      */ 
/*  727 */     this._numTypesValid = 0;
/*  728 */     if (this._tokenIncomplete) {
/*  729 */       _skipString();
/*      */     }
/*  731 */     int i = _skipWSOrEnd();
/*  732 */     if (i < 0)
/*      */     {
/*  734 */       close();
/*  735 */       return this._currToken = null;
/*      */     }
/*      */     
/*  738 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  741 */     if (i == 93) {
/*  742 */       _updateLocation();
/*  743 */       if (!this._parsingContext.inArray()) {
/*  744 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  746 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  747 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  749 */     if (i == 125) {
/*  750 */       _updateLocation();
/*  751 */       if (!this._parsingContext.inObject()) {
/*  752 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  754 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  755 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */     
/*      */ 
/*  759 */     if (this._parsingContext.expectComma()) {
/*  760 */       if (i != 44) {
/*  761 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
/*      */       }
/*  763 */       i = _skipWS();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  770 */     if (!this._parsingContext.inObject()) {
/*  771 */       _updateLocation();
/*  772 */       return _nextTokenNotInObject(i);
/*      */     }
/*      */     
/*  775 */     _updateNameLocation();
/*  776 */     String n = _parseName(i);
/*  777 */     this._parsingContext.setCurrentName(n);
/*  778 */     this._currToken = JsonToken.FIELD_NAME;
/*      */     
/*  780 */     i = _skipColon();
/*  781 */     _updateLocation();
/*      */     
/*      */ 
/*  784 */     if (i == 34) {
/*  785 */       this._tokenIncomplete = true;
/*  786 */       this._nextToken = JsonToken.VALUE_STRING;
/*  787 */       return this._currToken;
/*      */     }
/*      */     
/*      */     JsonToken t;
/*  791 */     switch (i) {
/*      */     case 45: 
/*  793 */       t = _parseNegNumber();
/*  794 */       break;
/*      */     
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
/*  810 */       t = _parsePosNumber(i);
/*  811 */       break;
/*      */     case 102: 
/*  813 */       _matchToken("false", 1);
/*  814 */       t = JsonToken.VALUE_FALSE;
/*  815 */       break;
/*      */     case 110: 
/*  817 */       _matchToken("null", 1);
/*  818 */       t = JsonToken.VALUE_NULL;
/*  819 */       break;
/*      */     case 116: 
/*  821 */       _matchToken("true", 1);
/*  822 */       t = JsonToken.VALUE_TRUE;
/*  823 */       break;
/*      */     case 91: 
/*  825 */       t = JsonToken.START_ARRAY;
/*  826 */       break;
/*      */     case 123: 
/*  828 */       t = JsonToken.START_OBJECT;
/*  829 */       break;
/*      */     
/*      */     default: 
/*  832 */       t = _handleUnexpectedValue(i);
/*      */     }
/*  834 */     this._nextToken = t;
/*  835 */     return this._currToken;
/*      */   }
/*      */   
/*      */   private final JsonToken _nextTokenNotInObject(int i) throws IOException
/*      */   {
/*  840 */     if (i == 34) {
/*  841 */       this._tokenIncomplete = true;
/*  842 */       return this._currToken = JsonToken.VALUE_STRING;
/*      */     }
/*  844 */     switch (i) {
/*      */     case 91: 
/*  846 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  847 */       return this._currToken = JsonToken.START_ARRAY;
/*      */     case 123: 
/*  849 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*  850 */       return this._currToken = JsonToken.START_OBJECT;
/*      */     case 116: 
/*  852 */       _matchToken("true", 1);
/*  853 */       return this._currToken = JsonToken.VALUE_TRUE;
/*      */     case 102: 
/*  855 */       _matchToken("false", 1);
/*  856 */       return this._currToken = JsonToken.VALUE_FALSE;
/*      */     case 110: 
/*  858 */       _matchToken("null", 1);
/*  859 */       return this._currToken = JsonToken.VALUE_NULL;
/*      */     case 45: 
/*  861 */       return this._currToken = _parseNegNumber();
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
/*  876 */       return this._currToken = _parsePosNumber(i);
/*      */     }
/*  878 */     return this._currToken = _handleUnexpectedValue(i);
/*      */   }
/*      */   
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  883 */     this._nameCopied = false;
/*  884 */     JsonToken t = this._nextToken;
/*  885 */     this._nextToken = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  890 */     if (t == JsonToken.START_ARRAY) {
/*  891 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  892 */     } else if (t == JsonToken.START_OBJECT) {
/*  893 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  895 */     return this._currToken = t;
/*      */   }
/*      */   
/*      */   public void finishToken() throws IOException
/*      */   {
/*  900 */     if (this._tokenIncomplete) {
/*  901 */       this._tokenIncomplete = false;
/*  902 */       _finishString();
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
/*      */   public boolean nextFieldName(SerializableString str)
/*      */     throws IOException
/*      */   {
/*  916 */     this._numTypesValid = 0;
/*  917 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  918 */       _nextAfterName();
/*  919 */       return false;
/*      */     }
/*  921 */     if (this._tokenIncomplete) {
/*  922 */       _skipString();
/*      */     }
/*  924 */     int i = _skipWSOrEnd();
/*  925 */     if (i < 0) {
/*  926 */       close();
/*  927 */       this._currToken = null;
/*  928 */       return false;
/*      */     }
/*  930 */     this._binaryValue = null;
/*      */     
/*      */ 
/*  933 */     if (i == 93) {
/*  934 */       _updateLocation();
/*  935 */       if (!this._parsingContext.inArray()) {
/*  936 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  938 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  939 */       this._currToken = JsonToken.END_ARRAY;
/*  940 */       return false;
/*      */     }
/*  942 */     if (i == 125) {
/*  943 */       _updateLocation();
/*  944 */       if (!this._parsingContext.inObject()) {
/*  945 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  947 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/*  948 */       this._currToken = JsonToken.END_OBJECT;
/*  949 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  953 */     if (this._parsingContext.expectComma()) {
/*  954 */       if (i != 44) {
/*  955 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
/*      */       }
/*  957 */       i = _skipWS();
/*      */     }
/*      */     
/*  960 */     if (!this._parsingContext.inObject()) {
/*  961 */       _updateLocation();
/*  962 */       _nextTokenNotInObject(i);
/*  963 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  967 */     _updateNameLocation();
/*  968 */     if (i == 34)
/*      */     {
/*  970 */       byte[] nameBytes = str.asQuotedUTF8();
/*  971 */       int len = nameBytes.length;
/*      */       
/*      */ 
/*  974 */       if (this._inputPtr + len + 4 < this._inputEnd)
/*      */       {
/*  976 */         int end = this._inputPtr + len;
/*  977 */         if (this._inputBuffer[end] == 34) {
/*  978 */           int offset = 0;
/*  979 */           int ptr = this._inputPtr;
/*      */           for (;;) {
/*  981 */             if (ptr == end) {
/*  982 */               this._parsingContext.setCurrentName(str.getValue());
/*  983 */               i = _skipColonFast(ptr + 1);
/*  984 */               _isNextTokenNameYes(i);
/*  985 */               return true;
/*      */             }
/*  987 */             if (nameBytes[offset] != this._inputBuffer[ptr]) {
/*      */               break;
/*      */             }
/*  990 */             offset++;
/*  991 */             ptr++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  996 */     return _isNextTokenNameMaybe(i, str);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String nextFieldName()
/*      */     throws IOException
/*      */   {
/* 1004 */     this._numTypesValid = 0;
/* 1005 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1006 */       _nextAfterName();
/* 1007 */       return null;
/*      */     }
/* 1009 */     if (this._tokenIncomplete) {
/* 1010 */       _skipString();
/*      */     }
/* 1012 */     int i = _skipWSOrEnd();
/* 1013 */     if (i < 0) {
/* 1014 */       close();
/* 1015 */       this._currToken = null;
/* 1016 */       return null;
/*      */     }
/* 1018 */     this._binaryValue = null;
/*      */     
/* 1020 */     if (i == 93) {
/* 1021 */       _updateLocation();
/* 1022 */       if (!this._parsingContext.inArray()) {
/* 1023 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/* 1025 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/* 1026 */       this._currToken = JsonToken.END_ARRAY;
/* 1027 */       return null;
/*      */     }
/* 1029 */     if (i == 125) {
/* 1030 */       _updateLocation();
/* 1031 */       if (!this._parsingContext.inObject()) {
/* 1032 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/* 1034 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/* 1035 */       this._currToken = JsonToken.END_OBJECT;
/* 1036 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1040 */     if (this._parsingContext.expectComma()) {
/* 1041 */       if (i != 44) {
/* 1042 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
/*      */       }
/* 1044 */       i = _skipWS();
/*      */     }
/* 1046 */     if (!this._parsingContext.inObject()) {
/* 1047 */       _updateLocation();
/* 1048 */       _nextTokenNotInObject(i);
/* 1049 */       return null;
/*      */     }
/*      */     
/* 1052 */     _updateNameLocation();
/* 1053 */     String nameStr = _parseName(i);
/* 1054 */     this._parsingContext.setCurrentName(nameStr);
/* 1055 */     this._currToken = JsonToken.FIELD_NAME;
/*      */     
/* 1057 */     i = _skipColon();
/* 1058 */     _updateLocation();
/* 1059 */     if (i == 34) {
/* 1060 */       this._tokenIncomplete = true;
/* 1061 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1062 */       return nameStr;
/*      */     }
/*      */     JsonToken t;
/* 1065 */     switch (i) {
/*      */     case 45: 
/* 1067 */       t = _parseNegNumber();
/* 1068 */       break;
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
/* 1079 */       t = _parsePosNumber(i);
/* 1080 */       break;
/*      */     case 102: 
/* 1082 */       _matchToken("false", 1);
/* 1083 */       t = JsonToken.VALUE_FALSE;
/* 1084 */       break;
/*      */     case 110: 
/* 1086 */       _matchToken("null", 1);
/* 1087 */       t = JsonToken.VALUE_NULL;
/* 1088 */       break;
/*      */     case 116: 
/* 1090 */       _matchToken("true", 1);
/* 1091 */       t = JsonToken.VALUE_TRUE;
/* 1092 */       break;
/*      */     case 91: 
/* 1094 */       t = JsonToken.START_ARRAY;
/* 1095 */       break;
/*      */     case 123: 
/* 1097 */       t = JsonToken.START_OBJECT;
/* 1098 */       break;
/*      */     
/*      */     default: 
/* 1101 */       t = _handleUnexpectedValue(i);
/*      */     }
/* 1103 */     this._nextToken = t;
/* 1104 */     return nameStr;
/*      */   }
/*      */   
/*      */   private final int _skipColonFast(int ptr)
/*      */     throws IOException
/*      */   {
/* 1110 */     int i = this._inputBuffer[(ptr++)];
/* 1111 */     if (i == 58) {
/* 1112 */       i = this._inputBuffer[(ptr++)];
/* 1113 */       if (i > 32) {
/* 1114 */         if ((i != 47) && (i != 35)) {
/* 1115 */           this._inputPtr = ptr;
/* 1116 */           return i;
/*      */         }
/* 1118 */       } else if ((i == 32) || (i == 9)) {
/* 1119 */         i = this._inputBuffer[(ptr++)];
/* 1120 */         if ((i > 32) && 
/* 1121 */           (i != 47) && (i != 35)) {
/* 1122 */           this._inputPtr = ptr;
/* 1123 */           return i;
/*      */         }
/*      */       }
/*      */       
/* 1127 */       this._inputPtr = (ptr - 1);
/* 1128 */       return _skipColon2(true);
/*      */     }
/* 1130 */     if ((i == 32) || (i == 9)) {
/* 1131 */       i = this._inputBuffer[(ptr++)];
/*      */     }
/* 1133 */     if (i == 58) {
/* 1134 */       i = this._inputBuffer[(ptr++)];
/* 1135 */       if (i > 32) {
/* 1136 */         if ((i != 47) && (i != 35)) {
/* 1137 */           this._inputPtr = ptr;
/* 1138 */           return i;
/*      */         }
/* 1140 */       } else if ((i == 32) || (i == 9)) {
/* 1141 */         i = this._inputBuffer[(ptr++)];
/* 1142 */         if ((i > 32) && 
/* 1143 */           (i != 47) && (i != 35)) {
/* 1144 */           this._inputPtr = ptr;
/* 1145 */           return i;
/*      */         }
/*      */       }
/*      */       
/* 1149 */       this._inputPtr = (ptr - 1);
/* 1150 */       return _skipColon2(true);
/*      */     }
/* 1152 */     this._inputPtr = (ptr - 1);
/* 1153 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final void _isNextTokenNameYes(int i) throws IOException
/*      */   {
/* 1158 */     this._currToken = JsonToken.FIELD_NAME;
/* 1159 */     _updateLocation();
/*      */     
/* 1161 */     switch (i) {
/*      */     case 34: 
/* 1163 */       this._tokenIncomplete = true;
/* 1164 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1165 */       return;
/*      */     case 91: 
/* 1167 */       this._nextToken = JsonToken.START_ARRAY;
/* 1168 */       return;
/*      */     case 123: 
/* 1170 */       this._nextToken = JsonToken.START_OBJECT;
/* 1171 */       return;
/*      */     case 116: 
/* 1173 */       _matchToken("true", 1);
/* 1174 */       this._nextToken = JsonToken.VALUE_TRUE;
/* 1175 */       return;
/*      */     case 102: 
/* 1177 */       _matchToken("false", 1);
/* 1178 */       this._nextToken = JsonToken.VALUE_FALSE;
/* 1179 */       return;
/*      */     case 110: 
/* 1181 */       _matchToken("null", 1);
/* 1182 */       this._nextToken = JsonToken.VALUE_NULL;
/* 1183 */       return;
/*      */     case 45: 
/* 1185 */       this._nextToken = _parseNegNumber();
/* 1186 */       return;
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
/* 1197 */       this._nextToken = _parsePosNumber(i);
/* 1198 */       return;
/*      */     }
/* 1200 */     this._nextToken = _handleUnexpectedValue(i);
/*      */   }
/*      */   
/*      */ 
/*      */   private final boolean _isNextTokenNameMaybe(int i, SerializableString str)
/*      */     throws IOException
/*      */   {
/* 1207 */     String n = _parseName(i);
/* 1208 */     this._parsingContext.setCurrentName(n);
/* 1209 */     boolean match = n.equals(str.getValue());
/* 1210 */     this._currToken = JsonToken.FIELD_NAME;
/* 1211 */     i = _skipColon();
/* 1212 */     _updateLocation();
/*      */     
/*      */ 
/* 1215 */     if (i == 34) {
/* 1216 */       this._tokenIncomplete = true;
/* 1217 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1218 */       return match;
/*      */     }
/*      */     
/*      */     JsonToken t;
/* 1222 */     switch (i) {
/*      */     case 91: 
/* 1224 */       t = JsonToken.START_ARRAY;
/* 1225 */       break;
/*      */     case 123: 
/* 1227 */       t = JsonToken.START_OBJECT;
/* 1228 */       break;
/*      */     case 116: 
/* 1230 */       _matchToken("true", 1);
/* 1231 */       t = JsonToken.VALUE_TRUE;
/* 1232 */       break;
/*      */     case 102: 
/* 1234 */       _matchToken("false", 1);
/* 1235 */       t = JsonToken.VALUE_FALSE;
/* 1236 */       break;
/*      */     case 110: 
/* 1238 */       _matchToken("null", 1);
/* 1239 */       t = JsonToken.VALUE_NULL;
/* 1240 */       break;
/*      */     case 45: 
/* 1242 */       t = _parseNegNumber();
/* 1243 */       break;
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
/* 1254 */       t = _parsePosNumber(i);
/* 1255 */       break;
/*      */     default: 
/* 1257 */       t = _handleUnexpectedValue(i);
/*      */     }
/* 1259 */     this._nextToken = t;
/* 1260 */     return match;
/*      */   }
/*      */   
/*      */ 
/*      */   public String nextTextValue()
/*      */     throws IOException
/*      */   {
/* 1267 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1268 */       this._nameCopied = false;
/* 1269 */       JsonToken t = this._nextToken;
/* 1270 */       this._nextToken = null;
/* 1271 */       this._currToken = t;
/* 1272 */       if (t == JsonToken.VALUE_STRING) {
/* 1273 */         if (this._tokenIncomplete) {
/* 1274 */           this._tokenIncomplete = false;
/* 1275 */           return _finishAndReturnString();
/*      */         }
/* 1277 */         return this._textBuffer.contentsAsString();
/*      */       }
/* 1279 */       if (t == JsonToken.START_ARRAY) {
/* 1280 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1281 */       } else if (t == JsonToken.START_OBJECT) {
/* 1282 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1284 */       return null;
/*      */     }
/*      */     
/* 1287 */     return nextToken() == JsonToken.VALUE_STRING ? getText() : null;
/*      */   }
/*      */   
/*      */ 
/*      */   public int nextIntValue(int defaultValue)
/*      */     throws IOException
/*      */   {
/* 1294 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1295 */       this._nameCopied = false;
/* 1296 */       JsonToken t = this._nextToken;
/* 1297 */       this._nextToken = null;
/* 1298 */       this._currToken = t;
/* 1299 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1300 */         return getIntValue();
/*      */       }
/* 1302 */       if (t == JsonToken.START_ARRAY) {
/* 1303 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1304 */       } else if (t == JsonToken.START_OBJECT) {
/* 1305 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1307 */       return defaultValue;
/*      */     }
/*      */     
/* 1310 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public long nextLongValue(long defaultValue)
/*      */     throws IOException
/*      */   {
/* 1317 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1318 */       this._nameCopied = false;
/* 1319 */       JsonToken t = this._nextToken;
/* 1320 */       this._nextToken = null;
/* 1321 */       this._currToken = t;
/* 1322 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1323 */         return getLongValue();
/*      */       }
/* 1325 */       if (t == JsonToken.START_ARRAY) {
/* 1326 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1327 */       } else if (t == JsonToken.START_OBJECT) {
/* 1328 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1330 */       return defaultValue;
/*      */     }
/*      */     
/* 1333 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */   public Boolean nextBooleanValue()
/*      */     throws IOException
/*      */   {
/* 1340 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1341 */       this._nameCopied = false;
/* 1342 */       JsonToken t = this._nextToken;
/* 1343 */       this._nextToken = null;
/* 1344 */       this._currToken = t;
/* 1345 */       if (t == JsonToken.VALUE_TRUE) {
/* 1346 */         return Boolean.TRUE;
/*      */       }
/* 1348 */       if (t == JsonToken.VALUE_FALSE) {
/* 1349 */         return Boolean.FALSE;
/*      */       }
/* 1351 */       if (t == JsonToken.START_ARRAY) {
/* 1352 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1353 */       } else if (t == JsonToken.START_OBJECT) {
/* 1354 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/* 1356 */       return null;
/*      */     }
/*      */     
/* 1359 */     JsonToken t = nextToken();
/* 1360 */     if (t == JsonToken.VALUE_TRUE) {
/* 1361 */       return Boolean.TRUE;
/*      */     }
/* 1363 */     if (t == JsonToken.VALUE_FALSE) {
/* 1364 */       return Boolean.FALSE;
/*      */     }
/* 1366 */     return null;
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
/*      */   protected JsonToken _parsePosNumber(int c)
/*      */     throws IOException
/*      */   {
/* 1392 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*      */     
/* 1394 */     if (c == 48) {
/* 1395 */       c = _verifyNoLeadingZeroes();
/*      */     }
/*      */     
/* 1398 */     outBuf[0] = ((char)c);
/* 1399 */     int intLen = 1;
/* 1400 */     int outPtr = 1;
/*      */     
/*      */ 
/* 1403 */     int end = this._inputPtr + outBuf.length - 1;
/* 1404 */     if (end > this._inputEnd) {
/* 1405 */       end = this._inputEnd;
/*      */     }
/*      */     for (;;)
/*      */     {
/* 1409 */       if (this._inputPtr >= end) {
/* 1410 */         return _parseNumber2(outBuf, outPtr, false, intLen);
/*      */       }
/* 1412 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1413 */       if ((c < 48) || (c > 57)) {
/*      */         break;
/*      */       }
/* 1416 */       intLen++;
/* 1417 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 1419 */     if ((c == 46) || (c == 101) || (c == 69)) {
/* 1420 */       return _parseFloat(outBuf, outPtr, c, false, intLen);
/*      */     }
/* 1422 */     this._inputPtr -= 1;
/* 1423 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1425 */     if (this._parsingContext.inRoot()) {
/* 1426 */       _verifyRootSpace(c);
/*      */     }
/*      */     
/* 1429 */     return resetInt(false, intLen);
/*      */   }
/*      */   
/*      */   protected JsonToken _parseNegNumber() throws IOException
/*      */   {
/* 1434 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1435 */     int outPtr = 0;
/*      */     
/*      */ 
/* 1438 */     outBuf[(outPtr++)] = '-';
/*      */     
/* 1440 */     if (this._inputPtr >= this._inputEnd) {
/* 1441 */       _loadMoreGuaranteed();
/*      */     }
/* 1443 */     int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     
/* 1445 */     if ((c < 48) || (c > 57)) {
/* 1446 */       return _handleInvalidNumberStart(c, true);
/*      */     }
/*      */     
/*      */ 
/* 1450 */     if (c == 48) {
/* 1451 */       c = _verifyNoLeadingZeroes();
/*      */     }
/*      */     
/*      */ 
/* 1455 */     outBuf[(outPtr++)] = ((char)c);
/* 1456 */     int intLen = 1;
/*      */     
/*      */ 
/*      */ 
/* 1460 */     int end = this._inputPtr + outBuf.length - outPtr;
/* 1461 */     if (end > this._inputEnd) {
/* 1462 */       end = this._inputEnd;
/*      */     }
/*      */     
/*      */     for (;;)
/*      */     {
/* 1467 */       if (this._inputPtr >= end)
/*      */       {
/* 1469 */         return _parseNumber2(outBuf, outPtr, true, intLen);
/*      */       }
/* 1471 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1472 */       if ((c < 48) || (c > 57)) {
/*      */         break;
/*      */       }
/* 1475 */       intLen++;
/* 1476 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 1478 */     if ((c == 46) || (c == 101) || (c == 69)) {
/* 1479 */       return _parseFloat(outBuf, outPtr, c, true, intLen);
/*      */     }
/*      */     
/* 1482 */     this._inputPtr -= 1;
/* 1483 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1485 */     if (this._parsingContext.inRoot()) {
/* 1486 */       _verifyRootSpace(c);
/*      */     }
/*      */     
/*      */ 
/* 1490 */     return resetInt(true, intLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final JsonToken _parseNumber2(char[] outBuf, int outPtr, boolean negative, int intPartLength)
/*      */     throws IOException
/*      */   {
/*      */     for (;;)
/*      */     {
/* 1502 */       if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1503 */         this._textBuffer.setCurrentLength(outPtr);
/* 1504 */         return resetInt(negative, intPartLength);
/*      */       }
/* 1506 */       int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1507 */       if ((c > 57) || (c < 48)) {
/* 1508 */         if ((c != 46) && (c != 101) && (c != 69)) break;
/* 1509 */         return _parseFloat(outBuf, outPtr, c, negative, intPartLength);
/*      */       }
/*      */       
/*      */ 
/* 1513 */       if (outPtr >= outBuf.length) {
/* 1514 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1515 */         outPtr = 0;
/*      */       }
/* 1517 */       outBuf[(outPtr++)] = ((char)c);
/* 1518 */       intPartLength++;
/*      */     }
/* 1520 */     this._inputPtr -= 1;
/* 1521 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1523 */     if (this._parsingContext.inRoot()) {
/* 1524 */       _verifyRootSpace(this._inputBuffer[(this._inputPtr++)] & 0xFF);
/*      */     }
/*      */     
/*      */ 
/* 1528 */     return resetInt(negative, intPartLength);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _verifyNoLeadingZeroes()
/*      */     throws IOException
/*      */   {
/* 1539 */     if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1540 */       return 48;
/*      */     }
/* 1542 */     int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/*      */     
/* 1544 */     if ((ch < 48) || (ch > 57)) {
/* 1545 */       return 48;
/*      */     }
/*      */     
/* 1548 */     if (!isEnabled(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
/* 1549 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */     }
/*      */     
/* 1552 */     this._inputPtr += 1;
/* 1553 */     if (ch == 48) {
/* 1554 */       while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 1555 */         ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 1556 */         if ((ch < 48) || (ch > 57)) {
/* 1557 */           return 48;
/*      */         }
/* 1559 */         this._inputPtr += 1;
/* 1560 */         if (ch != 48) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1565 */     return ch;
/*      */   }
/*      */   
/*      */   private final JsonToken _parseFloat(char[] outBuf, int outPtr, int c, boolean negative, int integerPartLength)
/*      */     throws IOException
/*      */   {
/* 1571 */     int fractLen = 0;
/* 1572 */     boolean eof = false;
/*      */     
/*      */ 
/* 1575 */     if (c == 46) {
/* 1576 */       if (outPtr >= outBuf.length) {
/* 1577 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1578 */         outPtr = 0;
/*      */       }
/* 1580 */       outBuf[(outPtr++)] = ((char)c);
/*      */       
/*      */       for (;;)
/*      */       {
/* 1584 */         if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1585 */           eof = true;
/* 1586 */           break;
/*      */         }
/* 1588 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1589 */         if ((c < 48) || (c > 57)) {
/*      */           break;
/*      */         }
/* 1592 */         fractLen++;
/* 1593 */         if (outPtr >= outBuf.length) {
/* 1594 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1595 */           outPtr = 0;
/*      */         }
/* 1597 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/*      */       
/* 1600 */       if (fractLen == 0) {
/* 1601 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/* 1605 */     int expLen = 0;
/* 1606 */     if ((c == 101) || (c == 69)) {
/* 1607 */       if (outPtr >= outBuf.length) {
/* 1608 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1609 */         outPtr = 0;
/*      */       }
/* 1611 */       outBuf[(outPtr++)] = ((char)c);
/*      */       
/* 1613 */       if (this._inputPtr >= this._inputEnd) {
/* 1614 */         _loadMoreGuaranteed();
/*      */       }
/* 1616 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       
/* 1618 */       if ((c == 45) || (c == 43)) {
/* 1619 */         if (outPtr >= outBuf.length) {
/* 1620 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1621 */           outPtr = 0;
/*      */         }
/* 1623 */         outBuf[(outPtr++)] = ((char)c);
/*      */         
/* 1625 */         if (this._inputPtr >= this._inputEnd) {
/* 1626 */           _loadMoreGuaranteed();
/*      */         }
/* 1628 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */       
/*      */ 
/* 1632 */       while ((c <= 57) && (c >= 48)) {
/* 1633 */         expLen++;
/* 1634 */         if (outPtr >= outBuf.length) {
/* 1635 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1636 */           outPtr = 0;
/*      */         }
/* 1638 */         outBuf[(outPtr++)] = ((char)c);
/* 1639 */         if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 1640 */           eof = true;
/* 1641 */           break;
/*      */         }
/* 1643 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */       
/* 1646 */       if (expLen == 0) {
/* 1647 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1652 */     if (!eof) {
/* 1653 */       this._inputPtr -= 1;
/*      */       
/* 1655 */       if (this._parsingContext.inRoot()) {
/* 1656 */         _verifyRootSpace(c);
/*      */       }
/*      */     }
/* 1659 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/*      */ 
/* 1662 */     return resetFloat(negative, integerPartLength, fractLen, expLen);
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
/* 1675 */     this._inputPtr += 1;
/*      */     
/* 1677 */     switch (ch) {
/*      */     case 9: 
/*      */     case 32: 
/* 1680 */       return;
/*      */     case 13: 
/* 1682 */       _skipCR();
/* 1683 */       return;
/*      */     case 10: 
/* 1685 */       this._currInputRow += 1;
/* 1686 */       this._currInputRowStart = this._inputPtr;
/* 1687 */       return;
/*      */     }
/* 1689 */     _reportMissingRootWS(ch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final String _parseName(int i)
/*      */     throws IOException
/*      */   {
/* 1700 */     if (i != 34) {
/* 1701 */       return _handleOddName(i);
/*      */     }
/*      */     
/* 1704 */     if (this._inputPtr + 13 > this._inputEnd) {
/* 1705 */       return slowParseName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1714 */     byte[] input = this._inputBuffer;
/* 1715 */     int[] codes = _icLatin1;
/*      */     
/* 1717 */     int q = input[(this._inputPtr++)] & 0xFF;
/*      */     
/* 1719 */     if (codes[q] == 0) {
/* 1720 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1721 */       if (codes[i] == 0) {
/* 1722 */         q = q << 8 | i;
/* 1723 */         i = input[(this._inputPtr++)] & 0xFF;
/* 1724 */         if (codes[i] == 0) {
/* 1725 */           q = q << 8 | i;
/* 1726 */           i = input[(this._inputPtr++)] & 0xFF;
/* 1727 */           if (codes[i] == 0) {
/* 1728 */             q = q << 8 | i;
/* 1729 */             i = input[(this._inputPtr++)] & 0xFF;
/* 1730 */             if (codes[i] == 0) {
/* 1731 */               this._quad1 = q;
/* 1732 */               return parseMediumName(i);
/*      */             }
/* 1734 */             if (i == 34) {
/* 1735 */               return findName(q, 4);
/*      */             }
/* 1737 */             return parseName(q, i, 4);
/*      */           }
/* 1739 */           if (i == 34) {
/* 1740 */             return findName(q, 3);
/*      */           }
/* 1742 */           return parseName(q, i, 3);
/*      */         }
/* 1744 */         if (i == 34) {
/* 1745 */           return findName(q, 2);
/*      */         }
/* 1747 */         return parseName(q, i, 2);
/*      */       }
/* 1749 */       if (i == 34) {
/* 1750 */         return findName(q, 1);
/*      */       }
/* 1752 */       return parseName(q, i, 1);
/*      */     }
/* 1754 */     if (q == 34) {
/* 1755 */       return "";
/*      */     }
/* 1757 */     return parseName(0, q, 0);
/*      */   }
/*      */   
/*      */   protected final String parseMediumName(int q2) throws IOException
/*      */   {
/* 1762 */     byte[] input = this._inputBuffer;
/* 1763 */     int[] codes = _icLatin1;
/*      */     
/*      */ 
/* 1766 */     int i = input[(this._inputPtr++)] & 0xFF;
/* 1767 */     if (codes[i] != 0) {
/* 1768 */       if (i == 34) {
/* 1769 */         return findName(this._quad1, q2, 1);
/*      */       }
/* 1771 */       return parseName(this._quad1, q2, i, 1);
/*      */     }
/* 1773 */     q2 = q2 << 8 | i;
/* 1774 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1775 */     if (codes[i] != 0) {
/* 1776 */       if (i == 34) {
/* 1777 */         return findName(this._quad1, q2, 2);
/*      */       }
/* 1779 */       return parseName(this._quad1, q2, i, 2);
/*      */     }
/* 1781 */     q2 = q2 << 8 | i;
/* 1782 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1783 */     if (codes[i] != 0) {
/* 1784 */       if (i == 34) {
/* 1785 */         return findName(this._quad1, q2, 3);
/*      */       }
/* 1787 */       return parseName(this._quad1, q2, i, 3);
/*      */     }
/* 1789 */     q2 = q2 << 8 | i;
/* 1790 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1791 */     if (codes[i] != 0) {
/* 1792 */       if (i == 34) {
/* 1793 */         return findName(this._quad1, q2, 4);
/*      */       }
/* 1795 */       return parseName(this._quad1, q2, i, 4);
/*      */     }
/* 1797 */     return parseMediumName2(i, q2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final String parseMediumName2(int q3, int q2)
/*      */     throws IOException
/*      */   {
/* 1805 */     byte[] input = this._inputBuffer;
/* 1806 */     int[] codes = _icLatin1;
/*      */     
/*      */ 
/* 1809 */     int i = input[(this._inputPtr++)] & 0xFF;
/* 1810 */     if (codes[i] != 0) {
/* 1811 */       if (i == 34) {
/* 1812 */         return findName(this._quad1, q2, q3, 1);
/*      */       }
/* 1814 */       return parseName(this._quad1, q2, q3, i, 1);
/*      */     }
/* 1816 */     q3 = q3 << 8 | i;
/* 1817 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1818 */     if (codes[i] != 0) {
/* 1819 */       if (i == 34) {
/* 1820 */         return findName(this._quad1, q2, q3, 2);
/*      */       }
/* 1822 */       return parseName(this._quad1, q2, q3, i, 2);
/*      */     }
/* 1824 */     q3 = q3 << 8 | i;
/* 1825 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1826 */     if (codes[i] != 0) {
/* 1827 */       if (i == 34) {
/* 1828 */         return findName(this._quad1, q2, q3, 3);
/*      */       }
/* 1830 */       return parseName(this._quad1, q2, q3, i, 3);
/*      */     }
/* 1832 */     q3 = q3 << 8 | i;
/* 1833 */     i = input[(this._inputPtr++)] & 0xFF;
/* 1834 */     if (codes[i] != 0) {
/* 1835 */       if (i == 34) {
/* 1836 */         return findName(this._quad1, q2, q3, 4);
/*      */       }
/* 1838 */       return parseName(this._quad1, q2, q3, i, 4);
/*      */     }
/* 1840 */     return parseLongName(i, q2, q3);
/*      */   }
/*      */   
/*      */   protected final String parseLongName(int q, int q2, int q3) throws IOException
/*      */   {
/* 1845 */     this._quadBuffer[0] = this._quad1;
/* 1846 */     this._quadBuffer[1] = q2;
/* 1847 */     this._quadBuffer[2] = q3;
/*      */     
/*      */ 
/* 1850 */     byte[] input = this._inputBuffer;
/* 1851 */     int[] codes = _icLatin1;
/* 1852 */     int qlen = 3;
/*      */     
/* 1854 */     while (this._inputPtr + 4 <= this._inputEnd) {
/* 1855 */       int i = input[(this._inputPtr++)] & 0xFF;
/* 1856 */       if (codes[i] != 0) {
/* 1857 */         if (i == 34) {
/* 1858 */           return findName(this._quadBuffer, qlen, q, 1);
/*      */         }
/* 1860 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 1);
/*      */       }
/*      */       
/* 1863 */       q = q << 8 | i;
/* 1864 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1865 */       if (codes[i] != 0) {
/* 1866 */         if (i == 34) {
/* 1867 */           return findName(this._quadBuffer, qlen, q, 2);
/*      */         }
/* 1869 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 2);
/*      */       }
/*      */       
/* 1872 */       q = q << 8 | i;
/* 1873 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1874 */       if (codes[i] != 0) {
/* 1875 */         if (i == 34) {
/* 1876 */           return findName(this._quadBuffer, qlen, q, 3);
/*      */         }
/* 1878 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 3);
/*      */       }
/*      */       
/* 1881 */       q = q << 8 | i;
/* 1882 */       i = input[(this._inputPtr++)] & 0xFF;
/* 1883 */       if (codes[i] != 0) {
/* 1884 */         if (i == 34) {
/* 1885 */           return findName(this._quadBuffer, qlen, q, 4);
/*      */         }
/* 1887 */         return parseEscapedName(this._quadBuffer, qlen, q, i, 4);
/*      */       }
/*      */       
/*      */ 
/* 1891 */       if (qlen >= this._quadBuffer.length) {
/* 1892 */         this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
/*      */       }
/* 1894 */       this._quadBuffer[(qlen++)] = q;
/* 1895 */       q = i;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1902 */     return parseEscapedName(this._quadBuffer, qlen, 0, q, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String slowParseName()
/*      */     throws IOException
/*      */   {
/* 1912 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1913 */       (!_loadMore())) {
/* 1914 */       _reportInvalidEOF(": was expecting closing '\"' for name", JsonToken.FIELD_NAME);
/*      */     }
/*      */     
/* 1917 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1918 */     if (i == 34) {
/* 1919 */       return "";
/*      */     }
/* 1921 */     return parseEscapedName(this._quadBuffer, 0, 0, i, 0);
/*      */   }
/*      */   
/*      */   private final String parseName(int q1, int ch, int lastQuadBytes) throws IOException {
/* 1925 */     return parseEscapedName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final String parseName(int q1, int q2, int ch, int lastQuadBytes) throws IOException {
/* 1929 */     this._quadBuffer[0] = q1;
/* 1930 */     return parseEscapedName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final String parseName(int q1, int q2, int q3, int ch, int lastQuadBytes) throws IOException {
/* 1934 */     this._quadBuffer[0] = q1;
/* 1935 */     this._quadBuffer[1] = q2;
/* 1936 */     return parseEscapedName(this._quadBuffer, 2, q3, ch, lastQuadBytes);
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
/*      */   protected final String parseEscapedName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes)
/*      */     throws IOException
/*      */   {
/* 1953 */     int[] codes = _icLatin1;
/*      */     for (;;)
/*      */     {
/* 1956 */       if (codes[ch] != 0) {
/* 1957 */         if (ch == 34) {
/*      */           break;
/*      */         }
/*      */         
/* 1961 */         if (ch != 92)
/*      */         {
/* 1963 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/* 1966 */           ch = _decodeEscaped();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1973 */         if (ch > 127)
/*      */         {
/* 1975 */           if (currQuadBytes >= 4) {
/* 1976 */             if (qlen >= quads.length) {
/* 1977 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/* 1979 */             quads[(qlen++)] = currQuad;
/* 1980 */             currQuad = 0;
/* 1981 */             currQuadBytes = 0;
/*      */           }
/* 1983 */           if (ch < 2048) {
/* 1984 */             currQuad = currQuad << 8 | 0xC0 | ch >> 6;
/* 1985 */             currQuadBytes++;
/*      */           }
/*      */           else {
/* 1988 */             currQuad = currQuad << 8 | 0xE0 | ch >> 12;
/* 1989 */             currQuadBytes++;
/*      */             
/* 1991 */             if (currQuadBytes >= 4) {
/* 1992 */               if (qlen >= quads.length) {
/* 1993 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/* 1995 */               quads[(qlen++)] = currQuad;
/* 1996 */               currQuad = 0;
/* 1997 */               currQuadBytes = 0;
/*      */             }
/* 1999 */             currQuad = currQuad << 8 | 0x80 | ch >> 6 & 0x3F;
/* 2000 */             currQuadBytes++;
/*      */           }
/*      */           
/* 2003 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 2007 */       if (currQuadBytes < 4) {
/* 2008 */         currQuadBytes++;
/* 2009 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 2011 */         if (qlen >= quads.length) {
/* 2012 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 2014 */         quads[(qlen++)] = currQuad;
/* 2015 */         currQuad = ch;
/* 2016 */         currQuadBytes = 1;
/*      */       }
/* 2018 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2019 */         (!_loadMore())) {
/* 2020 */         _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */       }
/*      */       
/* 2023 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/*      */     
/* 2026 */     if (currQuadBytes > 0) {
/* 2027 */       if (qlen >= quads.length) {
/* 2028 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 2030 */       quads[(qlen++)] = pad(currQuad, currQuadBytes);
/*      */     }
/* 2032 */     String name = this._symbols.findName(quads, qlen);
/* 2033 */     if (name == null) {
/* 2034 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 2036 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _handleOddName(int ch)
/*      */     throws IOException
/*      */   {
/* 2048 */     if ((ch == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 2049 */       return _parseAposName();
/*      */     }
/*      */     
/* 2052 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/* 2053 */       char c = (char)_decodeCharForError(ch);
/* 2054 */       _reportUnexpectedChar(c, "was expecting double-quote to start field name");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2060 */     int[] codes = CharTypes.getInputCodeUtf8JsNames();
/*      */     
/* 2062 */     if (codes[ch] != 0) {
/* 2063 */       _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2070 */     int[] quads = this._quadBuffer;
/* 2071 */     int qlen = 0;
/* 2072 */     int currQuad = 0;
/* 2073 */     int currQuadBytes = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 2077 */       if (currQuadBytes < 4) {
/* 2078 */         currQuadBytes++;
/* 2079 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 2081 */         if (qlen >= quads.length) {
/* 2082 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 2084 */         quads[(qlen++)] = currQuad;
/* 2085 */         currQuad = ch;
/* 2086 */         currQuadBytes = 1;
/*      */       }
/* 2088 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2089 */         (!_loadMore())) {
/* 2090 */         _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */       }
/*      */       
/* 2093 */       ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 2094 */       if (codes[ch] != 0) {
/*      */         break;
/*      */       }
/* 2097 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 2100 */     if (currQuadBytes > 0) {
/* 2101 */       if (qlen >= quads.length) {
/* 2102 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 2104 */       quads[(qlen++)] = currQuad;
/*      */     }
/* 2106 */     String name = this._symbols.findName(quads, qlen);
/* 2107 */     if (name == null) {
/* 2108 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 2110 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _parseAposName()
/*      */     throws IOException
/*      */   {
/* 2120 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2121 */       (!_loadMore())) {
/* 2122 */       _reportInvalidEOF(": was expecting closing ''' for field name", JsonToken.FIELD_NAME);
/*      */     }
/*      */     
/* 2125 */     int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2126 */     if (ch == 39) {
/* 2127 */       return "";
/*      */     }
/* 2129 */     int[] quads = this._quadBuffer;
/* 2130 */     int qlen = 0;
/* 2131 */     int currQuad = 0;
/* 2132 */     int currQuadBytes = 0;
/*      */     
/*      */ 
/*      */ 
/* 2136 */     int[] codes = _icLatin1;
/*      */     
/*      */ 
/* 2139 */     while (ch != 39)
/*      */     {
/*      */ 
/*      */ 
/* 2143 */       if ((ch != 34) && (codes[ch] != 0)) {
/* 2144 */         if (ch != 92)
/*      */         {
/*      */ 
/* 2147 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/* 2150 */           ch = _decodeEscaped();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2157 */         if (ch > 127)
/*      */         {
/* 2159 */           if (currQuadBytes >= 4) {
/* 2160 */             if (qlen >= quads.length) {
/* 2161 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/* 2163 */             quads[(qlen++)] = currQuad;
/* 2164 */             currQuad = 0;
/* 2165 */             currQuadBytes = 0;
/*      */           }
/* 2167 */           if (ch < 2048) {
/* 2168 */             currQuad = currQuad << 8 | 0xC0 | ch >> 6;
/* 2169 */             currQuadBytes++;
/*      */           }
/*      */           else {
/* 2172 */             currQuad = currQuad << 8 | 0xE0 | ch >> 12;
/* 2173 */             currQuadBytes++;
/*      */             
/* 2175 */             if (currQuadBytes >= 4) {
/* 2176 */               if (qlen >= quads.length) {
/* 2177 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/* 2179 */               quads[(qlen++)] = currQuad;
/* 2180 */               currQuad = 0;
/* 2181 */               currQuadBytes = 0;
/*      */             }
/* 2183 */             currQuad = currQuad << 8 | 0x80 | ch >> 6 & 0x3F;
/* 2184 */             currQuadBytes++;
/*      */           }
/*      */           
/* 2187 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */       
/* 2191 */       if (currQuadBytes < 4) {
/* 2192 */         currQuadBytes++;
/* 2193 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 2195 */         if (qlen >= quads.length) {
/* 2196 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 2198 */         quads[(qlen++)] = currQuad;
/* 2199 */         currQuad = ch;
/* 2200 */         currQuadBytes = 1;
/*      */       }
/* 2202 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2203 */         (!_loadMore())) {
/* 2204 */         _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */       }
/*      */       
/* 2207 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/*      */     
/* 2210 */     if (currQuadBytes > 0) {
/* 2211 */       if (qlen >= quads.length) {
/* 2212 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 2214 */       quads[(qlen++)] = pad(currQuad, currQuadBytes);
/*      */     }
/* 2216 */     String name = this._symbols.findName(quads, qlen);
/* 2217 */     if (name == null) {
/* 2218 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 2220 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String findName(int q1, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2231 */     q1 = pad(q1, lastQuadBytes);
/*      */     
/* 2233 */     String name = this._symbols.findName(q1);
/* 2234 */     if (name != null) {
/* 2235 */       return name;
/*      */     }
/*      */     
/* 2238 */     this._quadBuffer[0] = q1;
/* 2239 */     return addName(this._quadBuffer, 1, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final String findName(int q1, int q2, int lastQuadBytes) throws JsonParseException
/*      */   {
/* 2244 */     q2 = pad(q2, lastQuadBytes);
/*      */     
/* 2246 */     String name = this._symbols.findName(q1, q2);
/* 2247 */     if (name != null) {
/* 2248 */       return name;
/*      */     }
/*      */     
/* 2251 */     this._quadBuffer[0] = q1;
/* 2252 */     this._quadBuffer[1] = q2;
/* 2253 */     return addName(this._quadBuffer, 2, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final String findName(int q1, int q2, int q3, int lastQuadBytes) throws JsonParseException
/*      */   {
/* 2258 */     q3 = pad(q3, lastQuadBytes);
/* 2259 */     String name = this._symbols.findName(q1, q2, q3);
/* 2260 */     if (name != null) {
/* 2261 */       return name;
/*      */     }
/* 2263 */     int[] quads = this._quadBuffer;
/* 2264 */     quads[0] = q1;
/* 2265 */     quads[1] = q2;
/* 2266 */     quads[2] = pad(q3, lastQuadBytes);
/* 2267 */     return addName(quads, 3, lastQuadBytes);
/*      */   }
/*      */   
/*      */   private final String findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes) throws JsonParseException
/*      */   {
/* 2272 */     if (qlen >= quads.length) {
/* 2273 */       this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */     }
/* 2275 */     quads[(qlen++)] = pad(lastQuad, lastQuadBytes);
/* 2276 */     String name = this._symbols.findName(quads, qlen);
/* 2277 */     if (name == null) {
/* 2278 */       return addName(quads, qlen, lastQuadBytes);
/*      */     }
/* 2280 */     return name;
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
/*      */   private final String addName(int[] quads, int qlen, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 2296 */     int byteLen = (qlen << 2) - 4 + lastQuadBytes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     int lastQuad;
/*      */     
/*      */ 
/*      */ 
/* 2305 */     if (lastQuadBytes < 4) {
/* 2306 */       int lastQuad = quads[(qlen - 1)];
/*      */       
/* 2308 */       quads[(qlen - 1)] = (lastQuad << (4 - lastQuadBytes << 3));
/*      */     } else {
/* 2310 */       lastQuad = 0;
/*      */     }
/*      */     
/*      */ 
/* 2314 */     char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 2315 */     int cix = 0;
/*      */     
/* 2317 */     for (int ix = 0; ix < byteLen;) {
/* 2318 */       int ch = quads[(ix >> 2)];
/* 2319 */       int byteIx = ix & 0x3;
/* 2320 */       ch = ch >> (3 - byteIx << 3) & 0xFF;
/* 2321 */       ix++;
/*      */       
/* 2323 */       if (ch > 127) { int needed;
/*      */         int needed;
/* 2325 */         if ((ch & 0xE0) == 192) {
/* 2326 */           ch &= 0x1F;
/* 2327 */           needed = 1; } else { int needed;
/* 2328 */           if ((ch & 0xF0) == 224) {
/* 2329 */             ch &= 0xF;
/* 2330 */             needed = 2; } else { int needed;
/* 2331 */             if ((ch & 0xF8) == 240) {
/* 2332 */               ch &= 0x7;
/* 2333 */               needed = 3;
/*      */             } else {
/* 2335 */               _reportInvalidInitial(ch);
/* 2336 */               needed = ch = 1;
/*      */             } } }
/* 2338 */         if (ix + needed > byteLen) {
/* 2339 */           _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */         }
/*      */         
/*      */ 
/* 2343 */         int ch2 = quads[(ix >> 2)];
/* 2344 */         byteIx = ix & 0x3;
/* 2345 */         ch2 >>= 3 - byteIx << 3;
/* 2346 */         ix++;
/*      */         
/* 2348 */         if ((ch2 & 0xC0) != 128) {
/* 2349 */           _reportInvalidOther(ch2);
/*      */         }
/* 2351 */         ch = ch << 6 | ch2 & 0x3F;
/* 2352 */         if (needed > 1) {
/* 2353 */           ch2 = quads[(ix >> 2)];
/* 2354 */           byteIx = ix & 0x3;
/* 2355 */           ch2 >>= 3 - byteIx << 3;
/* 2356 */           ix++;
/*      */           
/* 2358 */           if ((ch2 & 0xC0) != 128) {
/* 2359 */             _reportInvalidOther(ch2);
/*      */           }
/* 2361 */           ch = ch << 6 | ch2 & 0x3F;
/* 2362 */           if (needed > 2) {
/* 2363 */             ch2 = quads[(ix >> 2)];
/* 2364 */             byteIx = ix & 0x3;
/* 2365 */             ch2 >>= 3 - byteIx << 3;
/* 2366 */             ix++;
/* 2367 */             if ((ch2 & 0xC0) != 128) {
/* 2368 */               _reportInvalidOther(ch2 & 0xFF);
/*      */             }
/* 2370 */             ch = ch << 6 | ch2 & 0x3F;
/*      */           }
/*      */         }
/* 2373 */         if (needed > 2) {
/* 2374 */           ch -= 65536;
/* 2375 */           if (cix >= cbuf.length) {
/* 2376 */             cbuf = this._textBuffer.expandCurrentSegment();
/*      */           }
/* 2378 */           cbuf[(cix++)] = ((char)(55296 + (ch >> 10)));
/* 2379 */           ch = 0xDC00 | ch & 0x3FF;
/*      */         }
/*      */       }
/* 2382 */       if (cix >= cbuf.length) {
/* 2383 */         cbuf = this._textBuffer.expandCurrentSegment();
/*      */       }
/* 2385 */       cbuf[(cix++)] = ((char)ch);
/*      */     }
/*      */     
/*      */ 
/* 2389 */     String baseName = new String(cbuf, 0, cix);
/*      */     
/* 2391 */     if (lastQuadBytes < 4) {
/* 2392 */       quads[(qlen - 1)] = lastQuad;
/*      */     }
/* 2394 */     return this._symbols.addName(baseName, quads, qlen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _loadMoreGuaranteed()
/*      */     throws IOException
/*      */   {
/* 2404 */     if (!_loadMore()) { _reportInvalidEOF();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _finishString()
/*      */     throws IOException
/*      */   {
/* 2411 */     int ptr = this._inputPtr;
/* 2412 */     if (ptr >= this._inputEnd) {
/* 2413 */       _loadMoreGuaranteed();
/* 2414 */       ptr = this._inputPtr;
/*      */     }
/* 2416 */     int outPtr = 0;
/* 2417 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 2418 */     int[] codes = _icUTF8;
/*      */     
/* 2420 */     int max = Math.min(this._inputEnd, ptr + outBuf.length);
/* 2421 */     byte[] inputBuffer = this._inputBuffer;
/* 2422 */     while (ptr < max) {
/* 2423 */       int c = inputBuffer[ptr] & 0xFF;
/* 2424 */       if (codes[c] != 0) {
/* 2425 */         if (c != 34) break;
/* 2426 */         this._inputPtr = (ptr + 1);
/* 2427 */         this._textBuffer.setCurrentLength(outPtr);
/* 2428 */         return;
/*      */       }
/*      */       
/*      */ 
/* 2432 */       ptr++;
/* 2433 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2435 */     this._inputPtr = ptr;
/* 2436 */     _finishString2(outBuf, outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _finishAndReturnString()
/*      */     throws IOException
/*      */   {
/* 2445 */     int ptr = this._inputPtr;
/* 2446 */     if (ptr >= this._inputEnd) {
/* 2447 */       _loadMoreGuaranteed();
/* 2448 */       ptr = this._inputPtr;
/*      */     }
/* 2450 */     int outPtr = 0;
/* 2451 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 2452 */     int[] codes = _icUTF8;
/*      */     
/* 2454 */     int max = Math.min(this._inputEnd, ptr + outBuf.length);
/* 2455 */     byte[] inputBuffer = this._inputBuffer;
/* 2456 */     while (ptr < max) {
/* 2457 */       int c = inputBuffer[ptr] & 0xFF;
/* 2458 */       if (codes[c] != 0) {
/* 2459 */         if (c != 34) break;
/* 2460 */         this._inputPtr = (ptr + 1);
/* 2461 */         return this._textBuffer.setCurrentAndReturn(outPtr);
/*      */       }
/*      */       
/*      */ 
/* 2465 */       ptr++;
/* 2466 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2468 */     this._inputPtr = ptr;
/* 2469 */     _finishString2(outBuf, outPtr);
/* 2470 */     return this._textBuffer.contentsAsString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _finishString2(char[] outBuf, int outPtr)
/*      */     throws IOException
/*      */   {
/* 2479 */     int[] codes = _icUTF8;
/* 2480 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2487 */       int ptr = this._inputPtr;
/* 2488 */       if (ptr >= this._inputEnd) {
/* 2489 */         _loadMoreGuaranteed();
/* 2490 */         ptr = this._inputPtr;
/*      */       }
/* 2492 */       if (outPtr >= outBuf.length) {
/* 2493 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2494 */         outPtr = 0;
/*      */       }
/* 2496 */       int max = Math.min(this._inputEnd, ptr + (outBuf.length - outPtr));
/* 2497 */       while (ptr < max) {
/* 2498 */         int c = inputBuffer[(ptr++)] & 0xFF;
/* 2499 */         if (codes[c] != 0) {
/* 2500 */           this._inputPtr = ptr;
/*      */           break label124;
/*      */         }
/* 2503 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/* 2505 */       this._inputPtr = ptr;
/* 2506 */       continue;
/*      */       label124:
/* 2508 */       int c; if (c == 34) {
/*      */         break;
/*      */       }
/*      */       
/* 2512 */       switch (codes[c]) {
/*      */       case 1: 
/* 2514 */         c = _decodeEscaped();
/* 2515 */         break;
/*      */       case 2: 
/* 2517 */         c = _decodeUtf8_2(c);
/* 2518 */         break;
/*      */       case 3: 
/* 2520 */         if (this._inputEnd - this._inputPtr >= 2) {
/* 2521 */           c = _decodeUtf8_3fast(c);
/*      */         } else {
/* 2523 */           c = _decodeUtf8_3(c);
/*      */         }
/* 2525 */         break;
/*      */       case 4: 
/* 2527 */         c = _decodeUtf8_4(c);
/*      */         
/* 2529 */         outBuf[(outPtr++)] = ((char)(0xD800 | c >> 10));
/* 2530 */         if (outPtr >= outBuf.length) {
/* 2531 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 2532 */           outPtr = 0;
/*      */         }
/* 2534 */         c = 0xDC00 | c & 0x3FF;
/*      */         
/* 2536 */         break;
/*      */       default: 
/* 2538 */         if (c < 32)
/*      */         {
/* 2540 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else {
/* 2543 */           _reportInvalidChar(c);
/*      */         }
/*      */         break;
/*      */       }
/* 2547 */       if (outPtr >= outBuf.length) {
/* 2548 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2549 */         outPtr = 0;
/*      */       }
/*      */       
/* 2552 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2554 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _skipString()
/*      */     throws IOException
/*      */   {
/* 2564 */     this._tokenIncomplete = false;
/*      */     
/*      */ 
/* 2567 */     int[] codes = _icUTF8;
/* 2568 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2576 */       int ptr = this._inputPtr;
/* 2577 */       int max = this._inputEnd;
/* 2578 */       if (ptr >= max) {
/* 2579 */         _loadMoreGuaranteed();
/* 2580 */         ptr = this._inputPtr;
/* 2581 */         max = this._inputEnd;
/*      */       }
/* 2583 */       while (ptr < max) {
/* 2584 */         int c = inputBuffer[(ptr++)] & 0xFF;
/* 2585 */         if (codes[c] != 0) {
/* 2586 */           this._inputPtr = ptr;
/*      */           break label92;
/*      */         }
/*      */       }
/* 2590 */       this._inputPtr = ptr;
/* 2591 */       continue;
/*      */       label92:
/* 2593 */       int c; if (c == 34) {
/*      */         break;
/*      */       }
/*      */       
/* 2597 */       switch (codes[c]) {
/*      */       case 1: 
/* 2599 */         _decodeEscaped();
/* 2600 */         break;
/*      */       case 2: 
/* 2602 */         _skipUtf8_2();
/* 2603 */         break;
/*      */       case 3: 
/* 2605 */         _skipUtf8_3();
/* 2606 */         break;
/*      */       case 4: 
/* 2608 */         _skipUtf8_4(c);
/* 2609 */         break;
/*      */       default: 
/* 2611 */         if (c < 32) {
/* 2612 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else {
/* 2615 */           _reportInvalidChar(c);
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonToken _handleUnexpectedValue(int c)
/*      */     throws IOException
/*      */   {
/* 2629 */     switch (c)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 93: 
/* 2639 */       if (!this._parsingContext.inArray()) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 44: 
/* 2648 */       if (isEnabled(JsonParser.Feature.ALLOW_MISSING_VALUES)) {
/* 2649 */         this._inputPtr -= 1;
/* 2650 */         return JsonToken.VALUE_NULL;
/*      */       }
/*      */     
/*      */ 
/*      */ 
/*      */     case 125: 
/* 2656 */       _reportUnexpectedChar(c, "expected a value");
/*      */     case 39: 
/* 2658 */       if (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
/* 2659 */         return _handleApos();
/*      */       }
/*      */       break;
/*      */     case 78: 
/* 2663 */       _matchToken("NaN", 1);
/* 2664 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2665 */         return resetAsNaN("NaN", NaN.0D);
/*      */       }
/* 2667 */       _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 2668 */       break;
/*      */     case 73: 
/* 2670 */       _matchToken("Infinity", 1);
/* 2671 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2672 */         return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
/*      */       }
/* 2674 */       _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 2675 */       break;
/*      */     case 43: 
/* 2677 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2678 */         (!_loadMore())) {
/* 2679 */         _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
/*      */       }
/*      */       
/* 2682 */       return _handleInvalidNumberStart(this._inputBuffer[(this._inputPtr++)] & 0xFF, false);
/*      */     }
/*      */     
/* 2685 */     if (Character.isJavaIdentifierStart(c)) {
/* 2686 */       _reportInvalidToken("" + (char)c, "('true', 'false' or 'null')");
/*      */     }
/*      */     
/* 2689 */     _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/* 2690 */     return null;
/*      */   }
/*      */   
/*      */   protected JsonToken _handleApos()
/*      */     throws IOException
/*      */   {
/* 2696 */     int c = 0;
/*      */     
/* 2698 */     int outPtr = 0;
/* 2699 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*      */     
/*      */ 
/* 2702 */     int[] codes = _icUTF8;
/* 2703 */     byte[] inputBuffer = this._inputBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 2710 */       if (this._inputPtr >= this._inputEnd) {
/* 2711 */         _loadMoreGuaranteed();
/*      */       }
/* 2713 */       if (outPtr >= outBuf.length) {
/* 2714 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2715 */         outPtr = 0;
/*      */       }
/* 2717 */       int max = this._inputEnd;
/*      */       
/* 2719 */       int max2 = this._inputPtr + (outBuf.length - outPtr);
/* 2720 */       if (max2 < max) {
/* 2721 */         max = max2;
/*      */       }
/*      */       
/* 2724 */       while (this._inputPtr < max) {
/* 2725 */         c = inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2726 */         if ((c == 39) || (codes[c] != 0)) {
/*      */           break label140;
/*      */         }
/* 2729 */         outBuf[(outPtr++)] = ((char)c);
/*      */       }
/* 2731 */       continue;
/*      */       
/*      */       label140:
/* 2734 */       if (c == 39) {
/*      */         break;
/*      */       }
/*      */       
/* 2738 */       switch (codes[c]) {
/*      */       case 1: 
/* 2740 */         c = _decodeEscaped();
/* 2741 */         break;
/*      */       case 2: 
/* 2743 */         c = _decodeUtf8_2(c);
/* 2744 */         break;
/*      */       case 3: 
/* 2746 */         if (this._inputEnd - this._inputPtr >= 2) {
/* 2747 */           c = _decodeUtf8_3fast(c);
/*      */         } else {
/* 2749 */           c = _decodeUtf8_3(c);
/*      */         }
/* 2751 */         break;
/*      */       case 4: 
/* 2753 */         c = _decodeUtf8_4(c);
/*      */         
/* 2755 */         outBuf[(outPtr++)] = ((char)(0xD800 | c >> 10));
/* 2756 */         if (outPtr >= outBuf.length) {
/* 2757 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 2758 */           outPtr = 0;
/*      */         }
/* 2760 */         c = 0xDC00 | c & 0x3FF;
/*      */         
/* 2762 */         break;
/*      */       default: 
/* 2764 */         if (c < 32) {
/* 2765 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         
/* 2768 */         _reportInvalidChar(c);
/*      */       }
/*      */       
/* 2771 */       if (outPtr >= outBuf.length) {
/* 2772 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2773 */         outPtr = 0;
/*      */       }
/*      */       
/* 2776 */       outBuf[(outPtr++)] = ((char)c);
/*      */     }
/* 2778 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 2780 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonToken _handleInvalidNumberStart(int ch, boolean neg)
/*      */     throws IOException
/*      */   {
/* 2790 */     while (ch == 73) {
/* 2791 */       if ((this._inputPtr >= this._inputEnd) && 
/* 2792 */         (!_loadMore())) {
/* 2793 */         _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_FLOAT);
/*      */       }
/*      */       
/* 2796 */       ch = this._inputBuffer[(this._inputPtr++)];
/*      */       String match;
/* 2798 */       String match; if (ch == 78) {
/* 2799 */         match = neg ? "-INF" : "+INF";
/* 2800 */       } else { if (ch != 110) break;
/* 2801 */         match = neg ? "-Infinity" : "+Infinity";
/*      */       }
/*      */       
/*      */ 
/* 2805 */       _matchToken(match, 3);
/* 2806 */       if (isEnabled(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
/* 2807 */         return resetAsNaN(match, neg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */       }
/* 2809 */       _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */     }
/* 2811 */     reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/* 2812 */     return null;
/*      */   }
/*      */   
/*      */   protected final void _matchToken(String matchStr, int i) throws IOException
/*      */   {
/* 2817 */     int len = matchStr.length();
/* 2818 */     if (this._inputPtr + len >= this._inputEnd) {
/* 2819 */       _matchToken2(matchStr, i);
/*      */     }
/*      */     else {
/*      */       do {
/* 2823 */         if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
/* 2824 */           _reportInvalidToken(matchStr.substring(0, i));
/*      */         }
/* 2826 */         this._inputPtr += 1;
/* 2827 */         i++; } while (i < len);
/*      */       
/* 2829 */       int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 2830 */       if ((ch >= 48) && (ch != 93) && (ch != 125)) {
/* 2831 */         _checkMatchEnd(matchStr, i, ch);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _matchToken2(String matchStr, int i) throws IOException {
/* 2837 */     int len = matchStr.length();
/*      */     do {
/* 2839 */       if (((this._inputPtr >= this._inputEnd) && (!_loadMore())) || (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)))
/*      */       {
/* 2841 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2843 */       this._inputPtr += 1;
/* 2844 */       i++; } while (i < len);
/*      */     
/*      */ 
/* 2847 */     if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 2848 */       return;
/*      */     }
/* 2850 */     int ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 2851 */     if ((ch >= 48) && (ch != 93) && (ch != 125)) {
/* 2852 */       _checkMatchEnd(matchStr, i, ch);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _checkMatchEnd(String matchStr, int i, int ch) throws IOException
/*      */   {
/* 2858 */     char c = (char)_decodeCharForError(ch);
/* 2859 */     if (Character.isJavaIdentifierPart(c)) {
/* 2860 */       _reportInvalidToken(matchStr.substring(0, i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _skipWS()
/*      */     throws IOException
/*      */   {
/* 2872 */     while (this._inputPtr < this._inputEnd) {
/* 2873 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2874 */       if (i > 32) {
/* 2875 */         if ((i == 47) || (i == 35)) {
/* 2876 */           this._inputPtr -= 1;
/* 2877 */           return _skipWS2();
/*      */         }
/* 2879 */         return i;
/*      */       }
/* 2881 */       if (i != 32) {
/* 2882 */         if (i == 10) {
/* 2883 */           this._currInputRow += 1;
/* 2884 */           this._currInputRowStart = this._inputPtr;
/* 2885 */         } else if (i == 13) {
/* 2886 */           _skipCR();
/* 2887 */         } else if (i != 9) {
/* 2888 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2892 */     return _skipWS2();
/*      */   }
/*      */   
/*      */   private final int _skipWS2() throws IOException
/*      */   {
/* 2897 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2898 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2899 */       if (i > 32) {
/* 2900 */         if (i == 47) {
/* 2901 */           _skipComment();
/*      */ 
/*      */         }
/* 2904 */         else if ((i != 35) || 
/* 2905 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2909 */           return i;
/*      */         }
/* 2911 */       } else if (i != 32) {
/* 2912 */         if (i == 10) {
/* 2913 */           this._currInputRow += 1;
/* 2914 */           this._currInputRowStart = this._inputPtr;
/* 2915 */         } else if (i == 13) {
/* 2916 */           _skipCR();
/* 2917 */         } else if (i != 9) {
/* 2918 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2922 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.typeDesc() + " entries");
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException
/*      */   {
/* 2929 */     if ((this._inputPtr >= this._inputEnd) && 
/* 2930 */       (!_loadMore())) {
/* 2931 */       return _eofAsNextChar();
/*      */     }
/*      */     
/* 2934 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2935 */     if (i > 32) {
/* 2936 */       if ((i == 47) || (i == 35)) {
/* 2937 */         this._inputPtr -= 1;
/* 2938 */         return _skipWSOrEnd2();
/*      */       }
/* 2940 */       return i;
/*      */     }
/* 2942 */     if (i != 32) {
/* 2943 */       if (i == 10) {
/* 2944 */         this._currInputRow += 1;
/* 2945 */         this._currInputRowStart = this._inputPtr;
/* 2946 */       } else if (i == 13) {
/* 2947 */         _skipCR();
/* 2948 */       } else if (i != 9) {
/* 2949 */         _throwInvalidSpace(i);
/*      */       }
/*      */     }
/*      */     
/* 2953 */     while (this._inputPtr < this._inputEnd) {
/* 2954 */       i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2955 */       if (i > 32) {
/* 2956 */         if ((i == 47) || (i == 35)) {
/* 2957 */           this._inputPtr -= 1;
/* 2958 */           return _skipWSOrEnd2();
/*      */         }
/* 2960 */         return i;
/*      */       }
/* 2962 */       if (i != 32) {
/* 2963 */         if (i == 10) {
/* 2964 */           this._currInputRow += 1;
/* 2965 */           this._currInputRowStart = this._inputPtr;
/* 2966 */         } else if (i == 13) {
/* 2967 */           _skipCR();
/* 2968 */         } else if (i != 9) {
/* 2969 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 2973 */     return _skipWSOrEnd2();
/*      */   }
/*      */   
/*      */   private final int _skipWSOrEnd2() throws IOException
/*      */   {
/* 2978 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 2979 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2980 */       if (i > 32) {
/* 2981 */         if (i == 47) {
/* 2982 */           _skipComment();
/*      */ 
/*      */         }
/* 2985 */         else if ((i != 35) || 
/* 2986 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 2990 */           return i; }
/* 2991 */       } else if (i != 32) {
/* 2992 */         if (i == 10) {
/* 2993 */           this._currInputRow += 1;
/* 2994 */           this._currInputRowStart = this._inputPtr;
/* 2995 */         } else if (i == 13) {
/* 2996 */           _skipCR();
/* 2997 */         } else if (i != 9) {
/* 2998 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3003 */     return _eofAsNextChar();
/*      */   }
/*      */   
/*      */   private final int _skipColon() throws IOException
/*      */   {
/* 3008 */     if (this._inputPtr + 4 >= this._inputEnd) {
/* 3009 */       return _skipColon2(false);
/*      */     }
/*      */     
/* 3012 */     int i = this._inputBuffer[this._inputPtr];
/* 3013 */     if (i == 58) {
/* 3014 */       i = this._inputBuffer[(++this._inputPtr)];
/* 3015 */       if (i > 32) {
/* 3016 */         if ((i == 47) || (i == 35)) {
/* 3017 */           return _skipColon2(true);
/*      */         }
/* 3019 */         this._inputPtr += 1;
/* 3020 */         return i;
/*      */       }
/* 3022 */       if ((i == 32) || (i == 9)) {
/* 3023 */         i = this._inputBuffer[(++this._inputPtr)];
/* 3024 */         if (i > 32) {
/* 3025 */           if ((i == 47) || (i == 35)) {
/* 3026 */             return _skipColon2(true);
/*      */           }
/* 3028 */           this._inputPtr += 1;
/* 3029 */           return i;
/*      */         }
/*      */       }
/* 3032 */       return _skipColon2(true);
/*      */     }
/* 3034 */     if ((i == 32) || (i == 9)) {
/* 3035 */       i = this._inputBuffer[(++this._inputPtr)];
/*      */     }
/* 3037 */     if (i == 58) {
/* 3038 */       i = this._inputBuffer[(++this._inputPtr)];
/* 3039 */       if (i > 32) {
/* 3040 */         if ((i == 47) || (i == 35)) {
/* 3041 */           return _skipColon2(true);
/*      */         }
/* 3043 */         this._inputPtr += 1;
/* 3044 */         return i;
/*      */       }
/* 3046 */       if ((i == 32) || (i == 9)) {
/* 3047 */         i = this._inputBuffer[(++this._inputPtr)];
/* 3048 */         if (i > 32) {
/* 3049 */           if ((i == 47) || (i == 35)) {
/* 3050 */             return _skipColon2(true);
/*      */           }
/* 3052 */           this._inputPtr += 1;
/* 3053 */           return i;
/*      */         }
/*      */       }
/* 3056 */       return _skipColon2(true);
/*      */     }
/* 3058 */     return _skipColon2(false);
/*      */   }
/*      */   
/*      */   private final int _skipColon2(boolean gotColon) throws IOException
/*      */   {
/* 3063 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 3064 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       
/* 3066 */       if (i > 32) {
/* 3067 */         if (i == 47) {
/* 3068 */           _skipComment();
/*      */ 
/*      */         }
/* 3071 */         else if ((i != 35) || 
/* 3072 */           (!_skipYAMLComment()))
/*      */         {
/*      */ 
/*      */ 
/* 3076 */           if (gotColon) {
/* 3077 */             return i;
/*      */           }
/* 3079 */           if (i != 58) {
/* 3080 */             _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */           }
/* 3082 */           gotColon = true;
/* 3083 */         } } else if (i != 32) {
/* 3084 */         if (i == 10) {
/* 3085 */           this._currInputRow += 1;
/* 3086 */           this._currInputRowStart = this._inputPtr;
/* 3087 */         } else if (i == 13) {
/* 3088 */           _skipCR();
/* 3089 */         } else if (i != 9) {
/* 3090 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 3094 */     _reportInvalidEOF(" within/between " + this._parsingContext.typeDesc() + " entries", null);
/*      */     
/* 3096 */     return -1;
/*      */   }
/*      */   
/*      */   private final void _skipComment() throws IOException
/*      */   {
/* 3101 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/* 3102 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */     
/* 3105 */     if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/* 3106 */       _reportInvalidEOF(" in a comment", null);
/*      */     }
/* 3108 */     int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3109 */     if (c == 47) {
/* 3110 */       _skipLine();
/* 3111 */     } else if (c == 42) {
/* 3112 */       _skipCComment();
/*      */     } else {
/* 3114 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _skipCComment()
/*      */     throws IOException
/*      */   {
/* 3121 */     int[] codes = CharTypes.getInputCodeComment();
/*      */     
/*      */ 
/*      */ 
/* 3125 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 3126 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3127 */       int code = codes[i];
/* 3128 */       if (code != 0)
/* 3129 */         switch (code) {
/*      */         case 42: 
/* 3131 */           if ((this._inputPtr >= this._inputEnd) && (!_loadMore())) {
/*      */             break label216;
/*      */           }
/* 3134 */           if (this._inputBuffer[this._inputPtr] == 47) {
/* 3135 */             this._inputPtr += 1; return;
/*      */           }
/*      */           
/*      */           break;
/*      */         case 10: 
/* 3140 */           this._currInputRow += 1;
/* 3141 */           this._currInputRowStart = this._inputPtr;
/* 3142 */           break;
/*      */         case 13: 
/* 3144 */           _skipCR();
/* 3145 */           break;
/*      */         case 2: 
/* 3147 */           _skipUtf8_2();
/* 3148 */           break;
/*      */         case 3: 
/* 3150 */           _skipUtf8_3();
/* 3151 */           break;
/*      */         case 4: 
/* 3153 */           _skipUtf8_4(i);
/* 3154 */           break;
/*      */         
/*      */         default: 
/* 3157 */           _reportInvalidChar(i);
/*      */         }
/*      */     }
/*      */     label216:
/* 3161 */     _reportInvalidEOF(" in a comment", null);
/*      */   }
/*      */   
/*      */   private final boolean _skipYAMLComment() throws IOException
/*      */   {
/* 3166 */     if (!isEnabled(JsonParser.Feature.ALLOW_YAML_COMMENTS)) {
/* 3167 */       return false;
/*      */     }
/* 3169 */     _skipLine();
/* 3170 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _skipLine()
/*      */     throws IOException
/*      */   {
/* 3180 */     int[] codes = CharTypes.getInputCodeComment();
/* 3181 */     while ((this._inputPtr < this._inputEnd) || (_loadMore())) {
/* 3182 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3183 */       int code = codes[i];
/* 3184 */       if (code != 0) {
/* 3185 */         switch (code) {
/*      */         case 10: 
/* 3187 */           this._currInputRow += 1;
/* 3188 */           this._currInputRowStart = this._inputPtr;
/* 3189 */           return;
/*      */         case 13: 
/* 3191 */           _skipCR(); return;
/*      */         case 42: 
/*      */           break;
/*      */         
/*      */         case 2: 
/* 3196 */           _skipUtf8_2();
/* 3197 */           break;
/*      */         case 3: 
/* 3199 */           _skipUtf8_3();
/* 3200 */           break;
/*      */         case 4: 
/* 3202 */           _skipUtf8_4(i);
/* 3203 */           break;
/*      */         default: 
/* 3205 */           if (code < 0)
/*      */           {
/* 3207 */             _reportInvalidChar(i);
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected char _decodeEscaped() throws IOException
/*      */   {
/* 3217 */     if ((this._inputPtr >= this._inputEnd) && 
/* 3218 */       (!_loadMore())) {
/* 3219 */       _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */     }
/*      */     
/* 3222 */     int c = this._inputBuffer[(this._inputPtr++)];
/*      */     
/* 3224 */     switch (c)
/*      */     {
/*      */     case 98: 
/* 3227 */       return '\b';
/*      */     case 116: 
/* 3229 */       return '\t';
/*      */     case 110: 
/* 3231 */       return '\n';
/*      */     case 102: 
/* 3233 */       return '\f';
/*      */     case 114: 
/* 3235 */       return '\r';
/*      */     
/*      */ 
/*      */     case 34: 
/*      */     case 47: 
/*      */     case 92: 
/* 3241 */       return (char)c;
/*      */     
/*      */     case 117: 
/*      */       break;
/*      */     
/*      */     default: 
/* 3247 */       return _handleUnrecognizedCharacterEscape((char)_decodeCharForError(c));
/*      */     }
/*      */     
/*      */     
/* 3251 */     int value = 0;
/* 3252 */     for (int i = 0; i < 4; i++) {
/* 3253 */       if ((this._inputPtr >= this._inputEnd) && 
/* 3254 */         (!_loadMore())) {
/* 3255 */         _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */       }
/*      */       
/* 3258 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 3259 */       int digit = CharTypes.charToHex(ch);
/* 3260 */       if (digit < 0) {
/* 3261 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 3263 */       value = value << 4 | digit;
/*      */     }
/* 3265 */     return (char)value;
/*      */   }
/*      */   
/*      */   protected int _decodeCharForError(int firstByte) throws IOException
/*      */   {
/* 3270 */     int c = firstByte & 0xFF;
/* 3271 */     if (c > 127)
/*      */     {
/*      */       int needed;
/*      */       int needed;
/* 3275 */       if ((c & 0xE0) == 192) {
/* 3276 */         c &= 0x1F;
/* 3277 */         needed = 1; } else { int needed;
/* 3278 */         if ((c & 0xF0) == 224) {
/* 3279 */           c &= 0xF;
/* 3280 */           needed = 2; } else { int needed;
/* 3281 */           if ((c & 0xF8) == 240)
/*      */           {
/* 3283 */             c &= 0x7;
/* 3284 */             needed = 3;
/*      */           } else {
/* 3286 */             _reportInvalidInitial(c & 0xFF);
/* 3287 */             needed = 1;
/*      */           }
/*      */         } }
/* 3290 */       int d = nextByte();
/* 3291 */       if ((d & 0xC0) != 128) {
/* 3292 */         _reportInvalidOther(d & 0xFF);
/*      */       }
/* 3294 */       c = c << 6 | d & 0x3F;
/*      */       
/* 3296 */       if (needed > 1) {
/* 3297 */         d = nextByte();
/* 3298 */         if ((d & 0xC0) != 128) {
/* 3299 */           _reportInvalidOther(d & 0xFF);
/*      */         }
/* 3301 */         c = c << 6 | d & 0x3F;
/* 3302 */         if (needed > 2) {
/* 3303 */           d = nextByte();
/* 3304 */           if ((d & 0xC0) != 128) {
/* 3305 */             _reportInvalidOther(d & 0xFF);
/*      */           }
/* 3307 */           c = c << 6 | d & 0x3F;
/*      */         }
/*      */       }
/*      */     }
/* 3311 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _decodeUtf8_2(int c)
/*      */     throws IOException
/*      */   {
/* 3322 */     if (this._inputPtr >= this._inputEnd) {
/* 3323 */       _loadMoreGuaranteed();
/*      */     }
/* 3325 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3326 */     if ((d & 0xC0) != 128) {
/* 3327 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3329 */     return (c & 0x1F) << 6 | d & 0x3F;
/*      */   }
/*      */   
/*      */   private final int _decodeUtf8_3(int c1) throws IOException
/*      */   {
/* 3334 */     if (this._inputPtr >= this._inputEnd) {
/* 3335 */       _loadMoreGuaranteed();
/*      */     }
/* 3337 */     c1 &= 0xF;
/* 3338 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3339 */     if ((d & 0xC0) != 128) {
/* 3340 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3342 */     int c = c1 << 6 | d & 0x3F;
/* 3343 */     if (this._inputPtr >= this._inputEnd) {
/* 3344 */       _loadMoreGuaranteed();
/*      */     }
/* 3346 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3347 */     if ((d & 0xC0) != 128) {
/* 3348 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3350 */     c = c << 6 | d & 0x3F;
/* 3351 */     return c;
/*      */   }
/*      */   
/*      */   private final int _decodeUtf8_3fast(int c1) throws IOException
/*      */   {
/* 3356 */     c1 &= 0xF;
/* 3357 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3358 */     if ((d & 0xC0) != 128) {
/* 3359 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3361 */     int c = c1 << 6 | d & 0x3F;
/* 3362 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3363 */     if ((d & 0xC0) != 128) {
/* 3364 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3366 */     c = c << 6 | d & 0x3F;
/* 3367 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int _decodeUtf8_4(int c)
/*      */     throws IOException
/*      */   {
/* 3376 */     if (this._inputPtr >= this._inputEnd) {
/* 3377 */       _loadMoreGuaranteed();
/*      */     }
/* 3379 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3380 */     if ((d & 0xC0) != 128) {
/* 3381 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3383 */     c = (c & 0x7) << 6 | d & 0x3F;
/*      */     
/* 3385 */     if (this._inputPtr >= this._inputEnd) {
/* 3386 */       _loadMoreGuaranteed();
/*      */     }
/* 3388 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3389 */     if ((d & 0xC0) != 128) {
/* 3390 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3392 */     c = c << 6 | d & 0x3F;
/* 3393 */     if (this._inputPtr >= this._inputEnd) {
/* 3394 */       _loadMoreGuaranteed();
/*      */     }
/* 3396 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3397 */     if ((d & 0xC0) != 128) {
/* 3398 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3404 */     return (c << 6 | d & 0x3F) - 65536;
/*      */   }
/*      */   
/*      */   private final void _skipUtf8_2() throws IOException
/*      */   {
/* 3409 */     if (this._inputPtr >= this._inputEnd) {
/* 3410 */       _loadMoreGuaranteed();
/*      */     }
/* 3412 */     int c = this._inputBuffer[(this._inputPtr++)];
/* 3413 */     if ((c & 0xC0) != 128) {
/* 3414 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final void _skipUtf8_3()
/*      */     throws IOException
/*      */   {
/* 3423 */     if (this._inputPtr >= this._inputEnd) {
/* 3424 */       _loadMoreGuaranteed();
/*      */     }
/*      */     
/* 3427 */     int c = this._inputBuffer[(this._inputPtr++)];
/* 3428 */     if ((c & 0xC0) != 128) {
/* 3429 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/* 3431 */     if (this._inputPtr >= this._inputEnd) {
/* 3432 */       _loadMoreGuaranteed();
/*      */     }
/* 3434 */     c = this._inputBuffer[(this._inputPtr++)];
/* 3435 */     if ((c & 0xC0) != 128) {
/* 3436 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _skipUtf8_4(int c) throws IOException
/*      */   {
/* 3442 */     if (this._inputPtr >= this._inputEnd) {
/* 3443 */       _loadMoreGuaranteed();
/*      */     }
/* 3445 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 3446 */     if ((d & 0xC0) != 128) {
/* 3447 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3449 */     if (this._inputPtr >= this._inputEnd) {
/* 3450 */       _loadMoreGuaranteed();
/*      */     }
/* 3452 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3453 */     if ((d & 0xC0) != 128) {
/* 3454 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 3456 */     if (this._inputPtr >= this._inputEnd) {
/* 3457 */       _loadMoreGuaranteed();
/*      */     }
/* 3459 */     d = this._inputBuffer[(this._inputPtr++)];
/* 3460 */     if ((d & 0xC0) != 128) {
/* 3461 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
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
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/* 3477 */     if (((this._inputPtr < this._inputEnd) || (_loadMore())) && 
/* 3478 */       (this._inputBuffer[this._inputPtr] == 10)) {
/* 3479 */       this._inputPtr += 1;
/*      */     }
/*      */     
/* 3482 */     this._currInputRow += 1;
/* 3483 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */   
/*      */   private int nextByte() throws IOException
/*      */   {
/* 3488 */     if (this._inputPtr >= this._inputEnd) {
/* 3489 */       _loadMoreGuaranteed();
/*      */     }
/* 3491 */     return this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportInvalidToken(String matchedPart)
/*      */     throws IOException
/*      */   {
/* 3502 */     _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
/*      */   }
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart, String msg) throws IOException
/*      */   {
/* 3507 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3513 */     int maxTokenLength = 256;
/* 3514 */     while ((sb.length() < 256) && (
/* 3515 */       (this._inputPtr < this._inputEnd) || (_loadMore())))
/*      */     {
/*      */ 
/* 3518 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 3519 */       char c = (char)_decodeCharForError(i);
/* 3520 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 3523 */       sb.append(c);
/*      */     }
/* 3525 */     if (sb.length() == 256) {
/* 3526 */       sb.append("...");
/*      */     }
/* 3528 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting " + msg);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _reportInvalidChar(int c)
/*      */     throws JsonParseException
/*      */   {
/* 3535 */     if (c < 32) {
/* 3536 */       _throwInvalidSpace(c);
/*      */     }
/* 3538 */     _reportInvalidInitial(c);
/*      */   }
/*      */   
/*      */   protected void _reportInvalidInitial(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 3544 */     _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */   
/*      */   protected void _reportInvalidOther(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 3550 */     _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */   
/*      */   protected void _reportInvalidOther(int mask, int ptr)
/*      */     throws JsonParseException
/*      */   {
/* 3556 */     this._inputPtr = ptr;
/* 3557 */     _reportInvalidOther(mask);
/*      */   }
/*      */   
/*      */   public static int[] growArrayBy(int[] arr, int more)
/*      */   {
/* 3562 */     if (arr == null) {
/* 3563 */       return new int[more];
/*      */     }
/* 3565 */     return Arrays.copyOf(arr, arr.length + more);
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
/*      */   protected final byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException
/*      */   {
/* 3581 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     for (;;)
/*      */     {
/* 3588 */       if (this._inputPtr >= this._inputEnd) {
/* 3589 */         _loadMoreGuaranteed();
/*      */       }
/* 3591 */       int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3592 */       if (ch > 32) {
/* 3593 */         int bits = b64variant.decodeBase64Char(ch);
/* 3594 */         if (bits < 0) {
/* 3595 */           if (ch == 34) {
/* 3596 */             return builder.toByteArray();
/*      */           }
/* 3598 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/* 3599 */           if (bits < 0) {}
/*      */         }
/*      */         else
/*      */         {
/* 3603 */           int decodedData = bits;
/*      */           
/*      */ 
/*      */ 
/* 3607 */           if (this._inputPtr >= this._inputEnd) {
/* 3608 */             _loadMoreGuaranteed();
/*      */           }
/* 3610 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3611 */           bits = b64variant.decodeBase64Char(ch);
/* 3612 */           if (bits < 0) {
/* 3613 */             bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */           }
/* 3615 */           decodedData = decodedData << 6 | bits;
/*      */           
/*      */ 
/* 3618 */           if (this._inputPtr >= this._inputEnd) {
/* 3619 */             _loadMoreGuaranteed();
/*      */           }
/* 3621 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3622 */           bits = b64variant.decodeBase64Char(ch);
/*      */           
/*      */ 
/* 3625 */           if (bits < 0) {
/* 3626 */             if (bits != -2)
/*      */             {
/* 3628 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/* 3629 */                 decodedData >>= 4;
/* 3630 */                 builder.append(decodedData);
/* 3631 */                 return builder.toByteArray();
/*      */               }
/* 3633 */               bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */             }
/* 3635 */             if (bits == -2)
/*      */             {
/* 3637 */               if (this._inputPtr >= this._inputEnd) {
/* 3638 */                 _loadMoreGuaranteed();
/*      */               }
/* 3640 */               ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3641 */               if (!b64variant.usesPaddingChar(ch)) {
/* 3642 */                 throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */               }
/*      */               
/* 3645 */               decodedData >>= 4;
/* 3646 */               builder.append(decodedData);
/* 3647 */               continue;
/*      */             }
/*      */           }
/*      */           
/* 3651 */           decodedData = decodedData << 6 | bits;
/*      */           
/* 3653 */           if (this._inputPtr >= this._inputEnd) {
/* 3654 */             _loadMoreGuaranteed();
/*      */           }
/* 3656 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 3657 */           bits = b64variant.decodeBase64Char(ch);
/* 3658 */           if (bits < 0) {
/* 3659 */             if (bits != -2)
/*      */             {
/* 3661 */               if ((ch == 34) && (!b64variant.usesPadding())) {
/* 3662 */                 decodedData >>= 2;
/* 3663 */                 builder.appendTwoBytes(decodedData);
/* 3664 */                 return builder.toByteArray();
/*      */               }
/* 3666 */               bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */             }
/* 3668 */             if (bits == -2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3675 */               decodedData >>= 2;
/* 3676 */               builder.appendTwoBytes(decodedData);
/* 3677 */               continue;
/*      */             }
/*      */           }
/*      */           
/* 3681 */           decodedData = decodedData << 6 | bits;
/* 3682 */           builder.appendThreeBytes(decodedData);
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
/*      */   public JsonLocation getTokenLocation()
/*      */   {
/* 3696 */     Object src = this._ioContext.getSourceReference();
/* 3697 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 3698 */       long total = this._currInputProcessed + (this._nameStartOffset - 1);
/* 3699 */       return new JsonLocation(src, total, -1L, this._nameStartRow, this._nameStartCol);
/*      */     }
/*      */     
/* 3702 */     return new JsonLocation(src, this._tokenInputTotal - 1L, -1L, this._tokenInputRow, this._tokenInputCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonLocation getCurrentLocation()
/*      */   {
/* 3710 */     int col = this._inputPtr - this._currInputRowStart + 1;
/* 3711 */     return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + this._inputPtr, -1L, this._currInputRow, col);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _updateLocation()
/*      */   {
/* 3719 */     this._tokenInputRow = this._currInputRow;
/* 3720 */     int ptr = this._inputPtr;
/* 3721 */     this._tokenInputTotal = (this._currInputProcessed + ptr);
/* 3722 */     this._tokenInputCol = (ptr - this._currInputRowStart);
/*      */   }
/*      */   
/*      */ 
/*      */   private final void _updateNameLocation()
/*      */   {
/* 3728 */     this._nameStartRow = this._currInputRow;
/* 3729 */     int ptr = this._inputPtr;
/* 3730 */     this._nameStartOffset = ptr;
/* 3731 */     this._nameStartCol = (ptr - this._currInputRowStart);
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
/*      */   private static final int pad(int q, int bytes)
/*      */   {
/* 3744 */     return bytes == 4 ? q : q | -1 << (bytes << 3);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\UTF8StreamJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */