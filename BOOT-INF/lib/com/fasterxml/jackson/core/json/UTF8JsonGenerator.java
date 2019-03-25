/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.NumberOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ public class UTF8JsonGenerator extends JsonGeneratorImpl
/*      */ {
/*      */   private static final byte BYTE_u = 117;
/*      */   private static final byte BYTE_0 = 48;
/*      */   private static final byte BYTE_LBRACKET = 91;
/*      */   private static final byte BYTE_RBRACKET = 93;
/*      */   private static final byte BYTE_LCURLY = 123;
/*      */   private static final byte BYTE_RCURLY = 125;
/*      */   private static final byte BYTE_BACKSLASH = 92;
/*      */   private static final byte BYTE_COMMA = 44;
/*      */   private static final byte BYTE_COLON = 58;
/*      */   private static final int MAX_BYTES_TO_BUFFER = 512;
/*   29 */   private static final byte[] HEX_CHARS = ;
/*      */   
/*   31 */   private static final byte[] NULL_BYTES = { 110, 117, 108, 108 };
/*   32 */   private static final byte[] TRUE_BYTES = { 116, 114, 117, 101 };
/*   33 */   private static final byte[] FALSE_BYTES = { 102, 97, 108, 115, 101 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final OutputStream _outputStream;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   52 */   protected byte _quoteChar = 34;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _outputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _outputTail;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _outputEnd;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _outputMaxContiguous;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _charBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _charBufferLength;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected byte[] _entityBuffer;
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
/*      */   public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out)
/*      */   {
/*  116 */     super(ctxt, features, codec);
/*  117 */     this._outputStream = out;
/*  118 */     this._bufferRecyclable = true;
/*  119 */     this._outputBuffer = ctxt.allocWriteEncodingBuffer();
/*  120 */     this._outputEnd = this._outputBuffer.length;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  127 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  128 */     this._charBufferLength = this._charBuffer.length;
/*      */     
/*      */ 
/*  131 */     if (isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII)) {
/*  132 */       setHighestNonEscapedChar(127);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public UTF8JsonGenerator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable)
/*      */   {
/*  141 */     super(ctxt, features, codec);
/*  142 */     this._outputStream = out;
/*  143 */     this._bufferRecyclable = bufferRecyclable;
/*  144 */     this._outputTail = outputOffset;
/*  145 */     this._outputBuffer = outputBuffer;
/*  146 */     this._outputEnd = this._outputBuffer.length;
/*      */     
/*  148 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  149 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  150 */     this._charBufferLength = this._charBuffer.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getOutputTarget()
/*      */   {
/*  161 */     return this._outputStream;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getOutputBuffered()
/*      */   {
/*  167 */     return this._outputTail;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  179 */     if (this._cfgPrettyPrinter != null) {
/*  180 */       _writePPFieldName(name);
/*  181 */       return;
/*      */     }
/*  183 */     int status = this._writeContext.writeFieldName(name);
/*  184 */     if (status == 4) {
/*  185 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  187 */     if (status == 1) {
/*  188 */       if (this._outputTail >= this._outputEnd) {
/*  189 */         _flushBuffer();
/*      */       }
/*  191 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  196 */     if (this._cfgUnqNames) {
/*  197 */       _writeStringSegments(name, false);
/*  198 */       return;
/*      */     }
/*  200 */     int len = name.length();
/*      */     
/*  202 */     if (len > this._charBufferLength) {
/*  203 */       _writeStringSegments(name, true);
/*  204 */       return;
/*      */     }
/*  206 */     if (this._outputTail >= this._outputEnd) {
/*  207 */       _flushBuffer();
/*      */     }
/*  209 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  211 */     if (len <= this._outputMaxContiguous) {
/*  212 */       if (this._outputTail + len > this._outputEnd) {
/*  213 */         _flushBuffer();
/*      */       }
/*  215 */       _writeStringSegment(name, 0, len);
/*      */     } else {
/*  217 */       _writeStringSegments(name, 0, len);
/*      */     }
/*      */     
/*  220 */     if (this._outputTail >= this._outputEnd) {
/*  221 */       _flushBuffer();
/*      */     }
/*  223 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeFieldName(SerializableString name)
/*      */     throws IOException
/*      */   {
/*  229 */     if (this._cfgPrettyPrinter != null) {
/*  230 */       _writePPFieldName(name);
/*  231 */       return;
/*      */     }
/*  233 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  234 */     if (status == 4) {
/*  235 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  237 */     if (status == 1) {
/*  238 */       if (this._outputTail >= this._outputEnd) {
/*  239 */         _flushBuffer();
/*      */       }
/*  241 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*  243 */     if (this._cfgUnqNames) {
/*  244 */       _writeUnq(name);
/*  245 */       return;
/*      */     }
/*  247 */     if (this._outputTail >= this._outputEnd) {
/*  248 */       _flushBuffer();
/*      */     }
/*  250 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  251 */     int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  252 */     if (len < 0) {
/*  253 */       _writeBytes(name.asQuotedUTF8());
/*      */     } else {
/*  255 */       this._outputTail += len;
/*      */     }
/*  257 */     if (this._outputTail >= this._outputEnd) {
/*  258 */       _flushBuffer();
/*      */     }
/*  260 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   private final void _writeUnq(SerializableString name) throws IOException {
/*  264 */     int len = name.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  265 */     if (len < 0) {
/*  266 */       _writeBytes(name.asQuotedUTF8());
/*      */     } else {
/*  268 */       this._outputTail += len;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException
/*      */   {
/*  281 */     _verifyValueWrite("start an array");
/*  282 */     this._writeContext = this._writeContext.createChildArrayContext();
/*  283 */     if (this._cfgPrettyPrinter != null) {
/*  284 */       this._cfgPrettyPrinter.writeStartArray(this);
/*      */     } else {
/*  286 */       if (this._outputTail >= this._outputEnd) {
/*  287 */         _flushBuffer();
/*      */       }
/*  289 */       this._outputBuffer[(this._outputTail++)] = 91;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeEndArray()
/*      */     throws IOException
/*      */   {
/*  296 */     if (!this._writeContext.inArray()) {
/*  297 */       _reportError("Current context not Array but " + this._writeContext.typeDesc());
/*      */     }
/*  299 */     if (this._cfgPrettyPrinter != null) {
/*  300 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  302 */       if (this._outputTail >= this._outputEnd) {
/*  303 */         _flushBuffer();
/*      */       }
/*  305 */       this._outputBuffer[(this._outputTail++)] = 93;
/*      */     }
/*  307 */     this._writeContext = this._writeContext.clearAndGetParent();
/*      */   }
/*      */   
/*      */   public final void writeStartObject()
/*      */     throws IOException
/*      */   {
/*  313 */     _verifyValueWrite("start an object");
/*  314 */     this._writeContext = this._writeContext.createChildObjectContext();
/*  315 */     if (this._cfgPrettyPrinter != null) {
/*  316 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  318 */       if (this._outputTail >= this._outputEnd) {
/*  319 */         _flushBuffer();
/*      */       }
/*  321 */       this._outputBuffer[(this._outputTail++)] = 123;
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeStartObject(Object forValue)
/*      */     throws IOException
/*      */   {
/*  328 */     _verifyValueWrite("start an object");
/*  329 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext();
/*  330 */     this._writeContext = ctxt;
/*  331 */     if (forValue != null) {
/*  332 */       ctxt.setCurrentValue(forValue);
/*      */     }
/*  334 */     if (this._cfgPrettyPrinter != null) {
/*  335 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  337 */       if (this._outputTail >= this._outputEnd) {
/*  338 */         _flushBuffer();
/*      */       }
/*  340 */       this._outputBuffer[(this._outputTail++)] = 123;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void writeEndObject()
/*      */     throws IOException
/*      */   {
/*  347 */     if (!this._writeContext.inObject()) {
/*  348 */       _reportError("Current context not Object but " + this._writeContext.typeDesc());
/*      */     }
/*  350 */     if (this._cfgPrettyPrinter != null) {
/*  351 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  353 */       if (this._outputTail >= this._outputEnd) {
/*  354 */         _flushBuffer();
/*      */       }
/*  356 */       this._outputBuffer[(this._outputTail++)] = 125;
/*      */     }
/*  358 */     this._writeContext = this._writeContext.clearAndGetParent();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _writePPFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  367 */     int status = this._writeContext.writeFieldName(name);
/*  368 */     if (status == 4) {
/*  369 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  371 */     if (status == 1) {
/*  372 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  374 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*  376 */     if (this._cfgUnqNames) {
/*  377 */       _writeStringSegments(name, false);
/*  378 */       return;
/*      */     }
/*  380 */     int len = name.length();
/*  381 */     if (len > this._charBufferLength) {
/*  382 */       _writeStringSegments(name, true);
/*  383 */       return;
/*      */     }
/*  385 */     if (this._outputTail >= this._outputEnd) {
/*  386 */       _flushBuffer();
/*      */     }
/*  388 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  389 */     name.getChars(0, len, this._charBuffer, 0);
/*      */     
/*  391 */     if (len <= this._outputMaxContiguous) {
/*  392 */       if (this._outputTail + len > this._outputEnd) {
/*  393 */         _flushBuffer();
/*      */       }
/*  395 */       _writeStringSegment(this._charBuffer, 0, len);
/*      */     } else {
/*  397 */       _writeStringSegments(this._charBuffer, 0, len);
/*      */     }
/*  399 */     if (this._outputTail >= this._outputEnd) {
/*  400 */       _flushBuffer();
/*      */     }
/*  402 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   protected final void _writePPFieldName(SerializableString name) throws IOException
/*      */   {
/*  407 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  408 */     if (status == 4) {
/*  409 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  411 */     if (status == 1) {
/*  412 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  414 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */     
/*  417 */     boolean addQuotes = !this._cfgUnqNames;
/*  418 */     if (addQuotes) {
/*  419 */       if (this._outputTail >= this._outputEnd) {
/*  420 */         _flushBuffer();
/*      */       }
/*  422 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*  424 */     _writeBytes(name.asQuotedUTF8());
/*  425 */     if (addQuotes) {
/*  426 */       if (this._outputTail >= this._outputEnd) {
/*  427 */         _flushBuffer();
/*      */       }
/*  429 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException
/*      */   {
/*  442 */     _verifyValueWrite("write a string");
/*  443 */     if (text == null) {
/*  444 */       _writeNull();
/*  445 */       return;
/*      */     }
/*      */     
/*  448 */     int len = text.length();
/*  449 */     if (len > this._outputMaxContiguous) {
/*  450 */       _writeStringSegments(text, true);
/*  451 */       return;
/*      */     }
/*  453 */     if (this._outputTail + len >= this._outputEnd) {
/*  454 */       _flushBuffer();
/*      */     }
/*  456 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  457 */     _writeStringSegment(text, 0, len);
/*  458 */     if (this._outputTail >= this._outputEnd) {
/*  459 */       _flushBuffer();
/*      */     }
/*  461 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeString(char[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  467 */     _verifyValueWrite("write a string");
/*  468 */     if (this._outputTail >= this._outputEnd) {
/*  469 */       _flushBuffer();
/*      */     }
/*  471 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  473 */     if (len <= this._outputMaxContiguous) {
/*  474 */       if (this._outputTail + len > this._outputEnd) {
/*  475 */         _flushBuffer();
/*      */       }
/*  477 */       _writeStringSegment(text, offset, len);
/*      */     } else {
/*  479 */       _writeStringSegments(text, offset, len);
/*      */     }
/*      */     
/*  482 */     if (this._outputTail >= this._outputEnd) {
/*  483 */       _flushBuffer();
/*      */     }
/*  485 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public final void writeString(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  491 */     _verifyValueWrite("write a string");
/*  492 */     if (this._outputTail >= this._outputEnd) {
/*  493 */       _flushBuffer();
/*      */     }
/*  495 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  496 */     int len = text.appendQuotedUTF8(this._outputBuffer, this._outputTail);
/*  497 */     if (len < 0) {
/*  498 */       _writeBytes(text.asQuotedUTF8());
/*      */     } else {
/*  500 */       this._outputTail += len;
/*      */     }
/*  502 */     if (this._outputTail >= this._outputEnd) {
/*  503 */       _flushBuffer();
/*      */     }
/*  505 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  511 */     _verifyValueWrite("write a string");
/*  512 */     if (this._outputTail >= this._outputEnd) {
/*  513 */       _flushBuffer();
/*      */     }
/*  515 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  516 */     _writeBytes(text, offset, length);
/*  517 */     if (this._outputTail >= this._outputEnd) {
/*  518 */       _flushBuffer();
/*      */     }
/*  520 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeUTF8String(byte[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  526 */     _verifyValueWrite("write a string");
/*  527 */     if (this._outputTail >= this._outputEnd) {
/*  528 */       _flushBuffer();
/*      */     }
/*  530 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  532 */     if (len <= this._outputMaxContiguous) {
/*  533 */       _writeUTF8Segment(text, offset, len);
/*      */     } else {
/*  535 */       _writeUTF8Segments(text, offset, len);
/*      */     }
/*  537 */     if (this._outputTail >= this._outputEnd) {
/*  538 */       _flushBuffer();
/*      */     }
/*  540 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeRaw(String text)
/*      */     throws IOException
/*      */   {
/*  551 */     int len = text.length();
/*  552 */     char[] buf = this._charBuffer;
/*  553 */     if (len <= buf.length) {
/*  554 */       text.getChars(0, len, buf, 0);
/*  555 */       writeRaw(buf, 0, len);
/*      */     } else {
/*  557 */       writeRaw(text, 0, len);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRaw(String text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  564 */     char[] buf = this._charBuffer;
/*  565 */     int cbufLen = buf.length;
/*      */     
/*      */ 
/*  568 */     if (len <= cbufLen) {
/*  569 */       text.getChars(offset, offset + len, buf, 0);
/*  570 */       writeRaw(buf, 0, len);
/*  571 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  577 */     int maxChunk = Math.min(cbufLen, (this._outputEnd >> 2) + (this._outputEnd >> 4));
/*      */     
/*  579 */     int maxBytes = maxChunk * 3;
/*      */     
/*  581 */     while (len > 0) {
/*  582 */       int len2 = Math.min(maxChunk, len);
/*  583 */       text.getChars(offset, offset + len2, buf, 0);
/*  584 */       if (this._outputTail + maxBytes > this._outputEnd) {
/*  585 */         _flushBuffer();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */       if (len2 > 1) {
/*  594 */         char ch = buf[(len2 - 1)];
/*  595 */         if ((ch >= 55296) && (ch <= 56319)) {
/*  596 */           len2--;
/*      */         }
/*      */       }
/*  599 */       _writeRawSegment(buf, 0, len2);
/*  600 */       offset += len2;
/*  601 */       len -= len2;
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRaw(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  608 */     byte[] raw = text.asUnquotedUTF8();
/*  609 */     if (raw.length > 0) {
/*  610 */       _writeBytes(raw);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRawValue(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  617 */     _verifyValueWrite("write a raw (unencoded) value");
/*  618 */     byte[] raw = text.asUnquotedUTF8();
/*  619 */     if (raw.length > 0) {
/*  620 */       _writeBytes(raw);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  630 */     int len3 = len + len + len;
/*  631 */     if (this._outputTail + len3 > this._outputEnd)
/*      */     {
/*  633 */       if (this._outputEnd < len3) {
/*  634 */         _writeSegmentedRaw(cbuf, offset, len);
/*  635 */         return;
/*      */       }
/*      */       
/*  638 */       _flushBuffer();
/*      */     }
/*      */     
/*  641 */     len += offset;
/*      */     
/*      */ 
/*      */ 
/*  645 */     while (offset < len)
/*      */     {
/*      */       for (;;) {
/*  648 */         int ch = cbuf[offset];
/*  649 */         if (ch > 127) {
/*      */           break;
/*      */         }
/*  652 */         this._outputBuffer[(this._outputTail++)] = ((byte)ch);
/*  653 */         offset++; if (offset >= len) {
/*      */           return;
/*      */         }
/*      */       }
/*  657 */       char ch = cbuf[(offset++)];
/*  658 */       if (ch < 'ࠀ') {
/*  659 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  660 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/*  662 */         offset = _outputRawMultiByteChar(ch, cbuf, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRaw(char ch)
/*      */     throws IOException
/*      */   {
/*  670 */     if (this._outputTail + 3 >= this._outputEnd) {
/*  671 */       _flushBuffer();
/*      */     }
/*  673 */     byte[] bbuf = this._outputBuffer;
/*  674 */     if (ch <= '') {
/*  675 */       bbuf[(this._outputTail++)] = ((byte)ch);
/*  676 */     } else if (ch < 'ࠀ') {
/*  677 */       bbuf[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  678 */       bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */     } else {
/*  680 */       _outputRawMultiByteChar(ch, null, 0, 0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeSegmentedRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  690 */     int end = this._outputEnd;
/*  691 */     byte[] bbuf = this._outputBuffer;
/*  692 */     int inputEnd = offset + len;
/*      */     
/*      */ 
/*  695 */     while (offset < inputEnd)
/*      */     {
/*      */       for (;;) {
/*  698 */         int ch = cbuf[offset];
/*  699 */         if (ch >= 128) {
/*      */           break;
/*      */         }
/*      */         
/*  703 */         if (this._outputTail >= end) {
/*  704 */           _flushBuffer();
/*      */         }
/*  706 */         bbuf[(this._outputTail++)] = ((byte)ch);
/*  707 */         offset++; if (offset >= inputEnd) {
/*      */           return;
/*      */         }
/*      */       }
/*  711 */       if (this._outputTail + 3 >= this._outputEnd) {
/*  712 */         _flushBuffer();
/*      */       }
/*  714 */       char ch = cbuf[(offset++)];
/*  715 */       if (ch < 'ࠀ') {
/*  716 */         bbuf[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  717 */         bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/*  719 */         offset = _outputRawMultiByteChar(ch, cbuf, offset, inputEnd);
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
/*      */   private void _writeRawSegment(char[] cbuf, int offset, int end)
/*      */     throws IOException
/*      */   {
/*  736 */     while (offset < end)
/*      */     {
/*      */       for (;;) {
/*  739 */         int ch = cbuf[offset];
/*  740 */         if (ch > 127) {
/*      */           break;
/*      */         }
/*  743 */         this._outputBuffer[(this._outputTail++)] = ((byte)ch);
/*  744 */         offset++; if (offset >= end) {
/*      */           return;
/*      */         }
/*      */       }
/*  748 */       char ch = cbuf[(offset++)];
/*  749 */       if (ch < 'ࠀ') {
/*  750 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0xC0 | ch >> '\006'));
/*  751 */         this._outputBuffer[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/*  753 */         offset = _outputRawMultiByteChar(ch, cbuf, offset, end);
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
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  769 */     _verifyValueWrite("write a binary value");
/*      */     
/*  771 */     if (this._outputTail >= this._outputEnd) {
/*  772 */       _flushBuffer();
/*      */     }
/*  774 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  775 */     _writeBinary(b64variant, data, offset, offset + len);
/*      */     
/*  777 */     if (this._outputTail >= this._outputEnd) {
/*  778 */       _flushBuffer();
/*      */     }
/*  780 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  788 */     _verifyValueWrite("write a binary value");
/*      */     
/*  790 */     if (this._outputTail >= this._outputEnd) {
/*  791 */       _flushBuffer();
/*      */     }
/*  793 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  794 */     byte[] encodingBuffer = this._ioContext.allocBase64Buffer();
/*      */     int bytes;
/*      */     try { int bytes;
/*  797 */       if (dataLength < 0) {
/*  798 */         bytes = _writeBinary(b64variant, data, encodingBuffer);
/*      */       } else {
/*  800 */         int missing = _writeBinary(b64variant, data, encodingBuffer, dataLength);
/*  801 */         if (missing > 0) {
/*  802 */           _reportError("Too few bytes available: missing " + missing + " bytes (out of " + dataLength + ")");
/*      */         }
/*  804 */         bytes = dataLength;
/*      */       }
/*      */     } finally {
/*  807 */       this._ioContext.releaseBase64Buffer(encodingBuffer);
/*      */     }
/*      */     
/*  810 */     if (this._outputTail >= this._outputEnd) {
/*  811 */       _flushBuffer();
/*      */     }
/*  813 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  814 */     return bytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeNumber(short s)
/*      */     throws IOException
/*      */   {
/*  826 */     _verifyValueWrite("write a number");
/*      */     
/*  828 */     if (this._outputTail + 6 >= this._outputEnd) {
/*  829 */       _flushBuffer();
/*      */     }
/*  831 */     if (this._cfgNumbersAsStrings) {
/*  832 */       _writeQuotedShort(s);
/*  833 */       return;
/*      */     }
/*  835 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedShort(short s) throws IOException {
/*  839 */     if (this._outputTail + 8 >= this._outputEnd) {
/*  840 */       _flushBuffer();
/*      */     }
/*  842 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  843 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*  844 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeNumber(int i)
/*      */     throws IOException
/*      */   {
/*  850 */     _verifyValueWrite("write a number");
/*      */     
/*  852 */     if (this._outputTail + 11 >= this._outputEnd) {
/*  853 */       _flushBuffer();
/*      */     }
/*  855 */     if (this._cfgNumbersAsStrings) {
/*  856 */       _writeQuotedInt(i);
/*  857 */       return;
/*      */     }
/*  859 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedInt(int i) throws IOException
/*      */   {
/*  864 */     if (this._outputTail + 13 >= this._outputEnd) {
/*  865 */       _flushBuffer();
/*      */     }
/*  867 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  868 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*  869 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeNumber(long l)
/*      */     throws IOException
/*      */   {
/*  875 */     _verifyValueWrite("write a number");
/*  876 */     if (this._cfgNumbersAsStrings) {
/*  877 */       _writeQuotedLong(l);
/*  878 */       return;
/*      */     }
/*  880 */     if (this._outputTail + 21 >= this._outputEnd)
/*      */     {
/*  882 */       _flushBuffer();
/*      */     }
/*  884 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private final void _writeQuotedLong(long l) throws IOException
/*      */   {
/*  889 */     if (this._outputTail + 23 >= this._outputEnd) {
/*  890 */       _flushBuffer();
/*      */     }
/*  892 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  893 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*  894 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeNumber(BigInteger value)
/*      */     throws IOException
/*      */   {
/*  900 */     _verifyValueWrite("write a number");
/*  901 */     if (value == null) {
/*  902 */       _writeNull();
/*  903 */     } else if (this._cfgNumbersAsStrings) {
/*  904 */       _writeQuotedRaw(value.toString());
/*      */     } else {
/*  906 */       writeRaw(value.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(double d)
/*      */     throws IOException
/*      */   {
/*  914 */     if ((this._cfgNumbersAsStrings) || (((Double.isNaN(d)) || (Double.isInfinite(d))) && (JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS.enabledIn(this._features))))
/*      */     {
/*      */ 
/*  917 */       writeString(String.valueOf(d));
/*  918 */       return;
/*      */     }
/*      */     
/*  921 */     _verifyValueWrite("write a number");
/*  922 */     writeRaw(String.valueOf(d));
/*      */   }
/*      */   
/*      */   public void writeNumber(float f)
/*      */     throws IOException
/*      */   {
/*  928 */     if ((this._cfgNumbersAsStrings) || (((Float.isNaN(f)) || (Float.isInfinite(f))) && (JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS.enabledIn(this._features))))
/*      */     {
/*      */ 
/*      */ 
/*  932 */       writeString(String.valueOf(f));
/*  933 */       return;
/*      */     }
/*      */     
/*  936 */     _verifyValueWrite("write a number");
/*  937 */     writeRaw(String.valueOf(f));
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(java.math.BigDecimal value)
/*      */     throws IOException
/*      */   {
/*  944 */     _verifyValueWrite("write a number");
/*  945 */     if (value == null) {
/*  946 */       _writeNull();
/*  947 */     } else if (this._cfgNumbersAsStrings) {
/*  948 */       _writeQuotedRaw(_asString(value));
/*      */     } else {
/*  950 */       writeRaw(_asString(value));
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException
/*      */   {
/*  957 */     _verifyValueWrite("write a number");
/*  958 */     if (this._cfgNumbersAsStrings) {
/*  959 */       _writeQuotedRaw(encodedValue);
/*      */     } else {
/*  961 */       writeRaw(encodedValue);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void _writeQuotedRaw(String value) throws IOException
/*      */   {
/*  967 */     if (this._outputTail >= this._outputEnd) {
/*  968 */       _flushBuffer();
/*      */     }
/*  970 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  971 */     writeRaw(value);
/*  972 */     if (this._outputTail >= this._outputEnd) {
/*  973 */       _flushBuffer();
/*      */     }
/*  975 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeBoolean(boolean state)
/*      */     throws IOException
/*      */   {
/*  981 */     _verifyValueWrite("write a boolean value");
/*  982 */     if (this._outputTail + 5 >= this._outputEnd) {
/*  983 */       _flushBuffer();
/*      */     }
/*  985 */     byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
/*  986 */     int len = keyword.length;
/*  987 */     System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
/*  988 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   public void writeNull()
/*      */     throws IOException
/*      */   {
/*  994 */     _verifyValueWrite("write a null");
/*  995 */     _writeNull();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _verifyValueWrite(String typeMsg)
/*      */     throws IOException
/*      */   {
/* 1007 */     int status = this._writeContext.writeValue();
/* 1008 */     if (this._cfgPrettyPrinter != null)
/*      */     {
/* 1010 */       _verifyPrettyValueWrite(typeMsg, status); return;
/*      */     }
/*      */     
/*      */     byte b;
/* 1014 */     switch (status) {
/*      */     case 0: case 4: 
/*      */     default: 
/* 1017 */       return;
/*      */     case 1: 
/* 1019 */       b = 44;
/* 1020 */       break;
/*      */     case 2: 
/* 1022 */       b = 58;
/* 1023 */       break;
/*      */     case 3: 
/* 1025 */       if (this._rootValueSeparator != null) {
/* 1026 */         byte[] raw = this._rootValueSeparator.asUnquotedUTF8();
/* 1027 */         if (raw.length > 0) {
/* 1028 */           _writeBytes(raw);
/*      */         }
/*      */       }
/* 1031 */       return;
/*      */     case 5: 
/* 1033 */       _reportCantWriteValueExpectName(typeMsg);
/* 1034 */       return;
/*      */     }
/* 1036 */     if (this._outputTail >= this._outputEnd) {
/* 1037 */       _flushBuffer();
/*      */     }
/* 1039 */     this._outputBuffer[(this._outputTail++)] = b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void flush()
/*      */     throws IOException
/*      */   {
/* 1051 */     _flushBuffer();
/* 1052 */     if ((this._outputStream != null) && 
/* 1053 */       (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))) {
/* 1054 */       this._outputStream.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/* 1062 */     super.close();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1068 */     if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT))) {
/*      */       for (;;)
/*      */       {
/* 1071 */         com.fasterxml.jackson.core.JsonStreamContext ctxt = getOutputContext();
/* 1072 */         if (ctxt.inArray()) {
/* 1073 */           writeEndArray();
/* 1074 */         } else { if (!ctxt.inObject()) break;
/* 1075 */           writeEndObject();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1081 */     _flushBuffer();
/* 1082 */     this._outputTail = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1090 */     if (this._outputStream != null) {
/* 1091 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET))) {
/* 1092 */         this._outputStream.close();
/* 1093 */       } else if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))
/*      */       {
/* 1095 */         this._outputStream.flush();
/*      */       }
/*      */     }
/*      */     
/* 1099 */     _releaseBuffers();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */   {
/* 1105 */     byte[] buf = this._outputBuffer;
/* 1106 */     if ((buf != null) && (this._bufferRecyclable)) {
/* 1107 */       this._outputBuffer = null;
/* 1108 */       this._ioContext.releaseWriteEncodingBuffer(buf);
/*      */     }
/* 1110 */     char[] cbuf = this._charBuffer;
/* 1111 */     if (cbuf != null) {
/* 1112 */       this._charBuffer = null;
/* 1113 */       this._ioContext.releaseConcatBuffer(cbuf);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeBytes(byte[] bytes)
/*      */     throws IOException
/*      */   {
/* 1125 */     int len = bytes.length;
/* 1126 */     if (this._outputTail + len > this._outputEnd) {
/* 1127 */       _flushBuffer();
/*      */       
/* 1129 */       if (len > 512) {
/* 1130 */         this._outputStream.write(bytes, 0, len);
/* 1131 */         return;
/*      */       }
/*      */     }
/* 1134 */     System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
/* 1135 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException
/*      */   {
/* 1140 */     if (this._outputTail + len > this._outputEnd) {
/* 1141 */       _flushBuffer();
/*      */       
/* 1143 */       if (len > 512) {
/* 1144 */         this._outputStream.write(bytes, offset, len);
/* 1145 */         return;
/*      */       }
/*      */     }
/* 1148 */     System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
/* 1149 */     this._outputTail += len;
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
/*      */   private final void _writeStringSegments(String text, boolean addQuotes)
/*      */     throws IOException
/*      */   {
/* 1167 */     if (addQuotes) {
/* 1168 */       if (this._outputTail >= this._outputEnd) {
/* 1169 */         _flushBuffer();
/*      */       }
/* 1171 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*      */     
/* 1174 */     int left = text.length();
/* 1175 */     int offset = 0;
/*      */     
/* 1177 */     while (left > 0) {
/* 1178 */       int len = Math.min(this._outputMaxContiguous, left);
/* 1179 */       if (this._outputTail + len > this._outputEnd) {
/* 1180 */         _flushBuffer();
/*      */       }
/* 1182 */       _writeStringSegment(text, offset, len);
/* 1183 */       offset += len;
/* 1184 */       left -= len;
/*      */     }
/*      */     
/* 1187 */     if (addQuotes) {
/* 1188 */       if (this._outputTail >= this._outputEnd) {
/* 1189 */         _flushBuffer();
/*      */       }
/* 1191 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeStringSegments(char[] cbuf, int offset, int totalLen)
/*      */     throws IOException
/*      */   {
/*      */     do
/*      */     {
/* 1204 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1205 */       if (this._outputTail + len > this._outputEnd) {
/* 1206 */         _flushBuffer();
/*      */       }
/* 1208 */       _writeStringSegment(cbuf, offset, len);
/* 1209 */       offset += len;
/* 1210 */       totalLen -= len;
/* 1211 */     } while (totalLen > 0);
/*      */   }
/*      */   
/*      */   private final void _writeStringSegments(String text, int offset, int totalLen) throws IOException
/*      */   {
/*      */     do {
/* 1217 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1218 */       if (this._outputTail + len > this._outputEnd) {
/* 1219 */         _flushBuffer();
/*      */       }
/* 1221 */       _writeStringSegment(text, offset, len);
/* 1222 */       offset += len;
/* 1223 */       totalLen -= len;
/* 1224 */     } while (totalLen > 0);
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
/*      */   private final void _writeStringSegment(char[] cbuf, int offset, int len)
/*      */     throws IOException
/*      */   {
/* 1247 */     len += offset;
/*      */     
/* 1249 */     int outputPtr = this._outputTail;
/* 1250 */     byte[] outputBuffer = this._outputBuffer;
/* 1251 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1253 */     while (offset < len) {
/* 1254 */       int ch = cbuf[offset];
/*      */       
/* 1256 */       if ((ch > 127) || (escCodes[ch] != 0)) {
/*      */         break;
/*      */       }
/* 1259 */       outputBuffer[(outputPtr++)] = ((byte)ch);
/* 1260 */       offset++;
/*      */     }
/* 1262 */     this._outputTail = outputPtr;
/* 1263 */     if (offset < len)
/*      */     {
/* 1265 */       if (this._characterEscapes != null) {
/* 1266 */         _writeCustomStringSegment2(cbuf, offset, len);
/*      */       }
/* 1268 */       else if (this._maximumNonEscapedChar == 0) {
/* 1269 */         _writeStringSegment2(cbuf, offset, len);
/*      */       } else {
/* 1271 */         _writeStringSegmentASCII2(cbuf, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final void _writeStringSegment(String text, int offset, int len)
/*      */     throws IOException
/*      */   {
/* 1281 */     len += offset;
/*      */     
/* 1283 */     int outputPtr = this._outputTail;
/* 1284 */     byte[] outputBuffer = this._outputBuffer;
/* 1285 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1287 */     while (offset < len) {
/* 1288 */       int ch = text.charAt(offset);
/*      */       
/* 1290 */       if ((ch > 127) || (escCodes[ch] != 0)) {
/*      */         break;
/*      */       }
/* 1293 */       outputBuffer[(outputPtr++)] = ((byte)ch);
/* 1294 */       offset++;
/*      */     }
/* 1296 */     this._outputTail = outputPtr;
/* 1297 */     if (offset < len) {
/* 1298 */       if (this._characterEscapes != null) {
/* 1299 */         _writeCustomStringSegment2(text, offset, len);
/* 1300 */       } else if (this._maximumNonEscapedChar == 0) {
/* 1301 */         _writeStringSegment2(text, offset, len);
/*      */       } else {
/* 1303 */         _writeStringSegmentASCII2(text, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeStringSegment2(char[] cbuf, int offset, int end)
/*      */     throws IOException
/*      */   {
/* 1315 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1316 */       _flushBuffer();
/*      */     }
/*      */     
/* 1319 */     int outputPtr = this._outputTail;
/*      */     
/* 1321 */     byte[] outputBuffer = this._outputBuffer;
/* 1322 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1324 */     while (offset < end) {
/* 1325 */       int ch = cbuf[(offset++)];
/* 1326 */       if (ch <= 127) {
/* 1327 */         if (escCodes[ch] == 0) {
/* 1328 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1331 */           int escape = escCodes[ch];
/* 1332 */           if (escape > 0) {
/* 1333 */             outputBuffer[(outputPtr++)] = 92;
/* 1334 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1337 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1341 */       else if (ch <= 2047) {
/* 1342 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1343 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1345 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1348 */     this._outputTail = outputPtr;
/*      */   }
/*      */   
/*      */   private final void _writeStringSegment2(String text, int offset, int end) throws IOException
/*      */   {
/* 1353 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1354 */       _flushBuffer();
/*      */     }
/*      */     
/* 1357 */     int outputPtr = this._outputTail;
/*      */     
/* 1359 */     byte[] outputBuffer = this._outputBuffer;
/* 1360 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1362 */     while (offset < end) {
/* 1363 */       int ch = text.charAt(offset++);
/* 1364 */       if (ch <= 127) {
/* 1365 */         if (escCodes[ch] == 0) {
/* 1366 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1369 */           int escape = escCodes[ch];
/* 1370 */           if (escape > 0) {
/* 1371 */             outputBuffer[(outputPtr++)] = 92;
/* 1372 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1375 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1379 */       else if (ch <= 2047) {
/* 1380 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1381 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1383 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1386 */     this._outputTail = outputPtr;
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
/*      */   private final void _writeStringSegmentASCII2(char[] cbuf, int offset, int end)
/*      */     throws IOException
/*      */   {
/* 1403 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1404 */       _flushBuffer();
/*      */     }
/*      */     
/* 1407 */     int outputPtr = this._outputTail;
/*      */     
/* 1409 */     byte[] outputBuffer = this._outputBuffer;
/* 1410 */     int[] escCodes = this._outputEscapes;
/* 1411 */     int maxUnescaped = this._maximumNonEscapedChar;
/*      */     
/* 1413 */     while (offset < end) {
/* 1414 */       int ch = cbuf[(offset++)];
/* 1415 */       if (ch <= 127) {
/* 1416 */         if (escCodes[ch] == 0) {
/* 1417 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1420 */           int escape = escCodes[ch];
/* 1421 */           if (escape > 0) {
/* 1422 */             outputBuffer[(outputPtr++)] = 92;
/* 1423 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1426 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1430 */       else if (ch > maxUnescaped) {
/* 1431 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */ 
/*      */       }
/* 1434 */       else if (ch <= 2047) {
/* 1435 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1436 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1438 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1441 */     this._outputTail = outputPtr;
/*      */   }
/*      */   
/*      */   private final void _writeStringSegmentASCII2(String text, int offset, int end)
/*      */     throws IOException
/*      */   {
/* 1447 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1448 */       _flushBuffer();
/*      */     }
/*      */     
/* 1451 */     int outputPtr = this._outputTail;
/*      */     
/* 1453 */     byte[] outputBuffer = this._outputBuffer;
/* 1454 */     int[] escCodes = this._outputEscapes;
/* 1455 */     int maxUnescaped = this._maximumNonEscapedChar;
/*      */     
/* 1457 */     while (offset < end) {
/* 1458 */       int ch = text.charAt(offset++);
/* 1459 */       if (ch <= 127) {
/* 1460 */         if (escCodes[ch] == 0) {
/* 1461 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1464 */           int escape = escCodes[ch];
/* 1465 */           if (escape > 0) {
/* 1466 */             outputBuffer[(outputPtr++)] = 92;
/* 1467 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */           }
/*      */           else {
/* 1470 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1474 */       else if (ch > maxUnescaped) {
/* 1475 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */ 
/*      */       }
/* 1478 */       else if (ch <= 2047) {
/* 1479 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1480 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */       } else {
/* 1482 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1485 */     this._outputTail = outputPtr;
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
/*      */   private final void _writeCustomStringSegment2(char[] cbuf, int offset, int end)
/*      */     throws IOException
/*      */   {
/* 1502 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1503 */       _flushBuffer();
/*      */     }
/* 1505 */     int outputPtr = this._outputTail;
/*      */     
/* 1507 */     byte[] outputBuffer = this._outputBuffer;
/* 1508 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1510 */     int maxUnescaped = this._maximumNonEscapedChar <= 0 ? 65535 : this._maximumNonEscapedChar;
/* 1511 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/* 1513 */     while (offset < end) {
/* 1514 */       int ch = cbuf[(offset++)];
/* 1515 */       if (ch <= 127) {
/* 1516 */         if (escCodes[ch] == 0) {
/* 1517 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1520 */           int escape = escCodes[ch];
/* 1521 */           if (escape > 0) {
/* 1522 */             outputBuffer[(outputPtr++)] = 92;
/* 1523 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/* 1524 */           } else if (escape == -2) {
/* 1525 */             SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1526 */             if (esc == null) {
/* 1527 */               _reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
/*      */             }
/*      */             
/* 1530 */             outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */           }
/*      */           else {
/* 1533 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1537 */       else if (ch > maxUnescaped) {
/* 1538 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */       }
/*      */       else {
/* 1541 */         SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1542 */         if (esc != null) {
/* 1543 */           outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */ 
/*      */         }
/* 1546 */         else if (ch <= 2047) {
/* 1547 */           outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1548 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */         } else {
/* 1550 */           outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */         }
/*      */       } }
/* 1553 */     this._outputTail = outputPtr;
/*      */   }
/*      */   
/*      */   private final void _writeCustomStringSegment2(String text, int offset, int end)
/*      */     throws IOException
/*      */   {
/* 1559 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1560 */       _flushBuffer();
/*      */     }
/* 1562 */     int outputPtr = this._outputTail;
/*      */     
/* 1564 */     byte[] outputBuffer = this._outputBuffer;
/* 1565 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1567 */     int maxUnescaped = this._maximumNonEscapedChar <= 0 ? 65535 : this._maximumNonEscapedChar;
/* 1568 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/* 1570 */     while (offset < end) {
/* 1571 */       int ch = text.charAt(offset++);
/* 1572 */       if (ch <= 127) {
/* 1573 */         if (escCodes[ch] == 0) {
/* 1574 */           outputBuffer[(outputPtr++)] = ((byte)ch);
/*      */         }
/*      */         else {
/* 1577 */           int escape = escCodes[ch];
/* 1578 */           if (escape > 0) {
/* 1579 */             outputBuffer[(outputPtr++)] = 92;
/* 1580 */             outputBuffer[(outputPtr++)] = ((byte)escape);
/* 1581 */           } else if (escape == -2) {
/* 1582 */             SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1583 */             if (esc == null) {
/* 1584 */               _reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(ch) + ", although was supposed to have one");
/*      */             }
/*      */             
/* 1587 */             outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */           }
/*      */           else {
/* 1590 */             outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */           }
/*      */         }
/*      */       }
/* 1594 */       else if (ch > maxUnescaped) {
/* 1595 */         outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */       }
/*      */       else {
/* 1598 */         SerializableString esc = customEscapes.getEscapeSequence(ch);
/* 1599 */         if (esc != null) {
/* 1600 */           outputPtr = _writeCustomEscape(outputBuffer, outputPtr, esc, end - offset);
/*      */ 
/*      */         }
/* 1603 */         else if (ch <= 2047) {
/* 1604 */           outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 1605 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */         } else {
/* 1607 */           outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */         }
/*      */       } }
/* 1610 */     this._outputTail = outputPtr;
/*      */   }
/*      */   
/*      */   private final int _writeCustomEscape(byte[] outputBuffer, int outputPtr, SerializableString esc, int remainingChars)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1616 */     byte[] raw = esc.asUnquotedUTF8();
/* 1617 */     int len = raw.length;
/* 1618 */     if (len > 6) {
/* 1619 */       return _handleLongCustomEscape(outputBuffer, outputPtr, this._outputEnd, raw, remainingChars);
/*      */     }
/*      */     
/* 1622 */     System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
/* 1623 */     return outputPtr + len;
/*      */   }
/*      */   
/*      */ 
/*      */   private final int _handleLongCustomEscape(byte[] outputBuffer, int outputPtr, int outputEnd, byte[] raw, int remainingChars)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1630 */     int len = raw.length;
/* 1631 */     if (outputPtr + len > outputEnd) {
/* 1632 */       this._outputTail = outputPtr;
/* 1633 */       _flushBuffer();
/* 1634 */       outputPtr = this._outputTail;
/* 1635 */       if (len > outputBuffer.length) {
/* 1636 */         this._outputStream.write(raw, 0, len);
/* 1637 */         return outputPtr;
/*      */       }
/* 1639 */       System.arraycopy(raw, 0, outputBuffer, outputPtr, len);
/* 1640 */       outputPtr += len;
/*      */     }
/*      */     
/* 1643 */     if (outputPtr + 6 * remainingChars > outputEnd) {
/* 1644 */       _flushBuffer();
/* 1645 */       return this._outputTail;
/*      */     }
/* 1647 */     return outputPtr;
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
/*      */   private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*      */     do
/*      */     {
/* 1665 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1666 */       _writeUTF8Segment(utf8, offset, len);
/* 1667 */       offset += len;
/* 1668 */       totalLen -= len;
/* 1669 */     } while (totalLen > 0);
/*      */   }
/*      */   
/*      */ 
/*      */   private final void _writeUTF8Segment(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1676 */     int[] escCodes = this._outputEscapes;
/*      */     
/* 1678 */     int ptr = offset; for (int end = offset + len; ptr < end;)
/*      */     {
/* 1680 */       int ch = utf8[(ptr++)];
/* 1681 */       if ((ch >= 0) && (escCodes[ch] != 0)) {
/* 1682 */         _writeUTF8Segment2(utf8, offset, len);
/* 1683 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1688 */     if (this._outputTail + len > this._outputEnd) {
/* 1689 */       _flushBuffer();
/*      */     }
/* 1691 */     System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
/* 1692 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   private final void _writeUTF8Segment2(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1698 */     int outputPtr = this._outputTail;
/*      */     
/*      */ 
/* 1701 */     if (outputPtr + len * 6 > this._outputEnd) {
/* 1702 */       _flushBuffer();
/* 1703 */       outputPtr = this._outputTail;
/*      */     }
/*      */     
/* 1706 */     byte[] outputBuffer = this._outputBuffer;
/* 1707 */     int[] escCodes = this._outputEscapes;
/* 1708 */     len += offset;
/*      */     
/* 1710 */     while (offset < len) {
/* 1711 */       byte b = utf8[(offset++)];
/* 1712 */       int ch = b;
/* 1713 */       if ((ch < 0) || (escCodes[ch] == 0)) {
/* 1714 */         outputBuffer[(outputPtr++)] = b;
/*      */       }
/*      */       else {
/* 1717 */         int escape = escCodes[ch];
/* 1718 */         if (escape > 0) {
/* 1719 */           outputBuffer[(outputPtr++)] = 92;
/* 1720 */           outputBuffer[(outputPtr++)] = ((byte)escape);
/*      */         }
/*      */         else {
/* 1723 */           outputPtr = _writeGenericEscape(ch, outputPtr);
/*      */         }
/*      */       } }
/* 1726 */     this._outputTail = outputPtr;
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
/*      */   protected final void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1740 */     int safeInputEnd = inputEnd - 3;
/*      */     
/* 1742 */     int safeOutputEnd = this._outputEnd - 6;
/* 1743 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */ 
/* 1746 */     while (inputPtr <= safeInputEnd) {
/* 1747 */       if (this._outputTail > safeOutputEnd) {
/* 1748 */         _flushBuffer();
/*      */       }
/*      */       
/* 1751 */       int b24 = input[(inputPtr++)] << 8;
/* 1752 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 1753 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 1754 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1755 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*      */       {
/* 1757 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1758 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1759 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1764 */     int inputLeft = inputEnd - inputPtr;
/* 1765 */     if (inputLeft > 0) {
/* 1766 */       if (this._outputTail > safeOutputEnd) {
/* 1767 */         _flushBuffer();
/*      */       }
/* 1769 */       int b24 = input[(inputPtr++)] << 16;
/* 1770 */       if (inputLeft == 2) {
/* 1771 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*      */       }
/* 1773 */       this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer, int bytesLeft)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1782 */     int inputPtr = 0;
/* 1783 */     int inputEnd = 0;
/* 1784 */     int lastFullOffset = -3;
/*      */     
/*      */ 
/* 1787 */     int safeOutputEnd = this._outputEnd - 6;
/* 1788 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/* 1790 */     while (bytesLeft > 2) {
/* 1791 */       if (inputPtr > lastFullOffset) {
/* 1792 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1793 */         inputPtr = 0;
/* 1794 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1797 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1799 */       if (this._outputTail > safeOutputEnd) {
/* 1800 */         _flushBuffer();
/*      */       }
/* 1802 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1803 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1804 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1805 */       bytesLeft -= 3;
/* 1806 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1807 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1808 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1809 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1810 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1815 */     if (bytesLeft > 0) {
/* 1816 */       inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1817 */       inputPtr = 0;
/* 1818 */       if (inputEnd > 0) {
/* 1819 */         if (this._outputTail > safeOutputEnd) {
/* 1820 */           _flushBuffer();
/*      */         }
/* 1822 */         int b24 = readBuffer[(inputPtr++)] << 16;
/*      */         int amount;
/* 1824 */         int amount; if (inputPtr < inputEnd) {
/* 1825 */           b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1826 */           amount = 2;
/*      */         } else {
/* 1828 */           amount = 1;
/*      */         }
/* 1830 */         this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/* 1831 */         bytesLeft -= amount;
/*      */       }
/*      */     }
/* 1834 */     return bytesLeft;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1842 */     int inputPtr = 0;
/* 1843 */     int inputEnd = 0;
/* 1844 */     int lastFullOffset = -3;
/* 1845 */     int bytesDone = 0;
/*      */     
/*      */ 
/* 1848 */     int safeOutputEnd = this._outputEnd - 6;
/* 1849 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1853 */       if (inputPtr > lastFullOffset) {
/* 1854 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, readBuffer.length);
/* 1855 */         inputPtr = 0;
/* 1856 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1859 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1861 */       if (this._outputTail > safeOutputEnd) {
/* 1862 */         _flushBuffer();
/*      */       }
/*      */       
/* 1865 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1866 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1867 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1868 */       bytesDone += 3;
/* 1869 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1870 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1871 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1872 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1873 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1878 */     if (inputPtr < inputEnd) {
/* 1879 */       if (this._outputTail > safeOutputEnd) {
/* 1880 */         _flushBuffer();
/*      */       }
/* 1882 */       int b24 = readBuffer[(inputPtr++)] << 16;
/* 1883 */       int amount = 1;
/* 1884 */       if (inputPtr < inputEnd) {
/* 1885 */         b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1886 */         amount = 2;
/*      */       }
/* 1888 */       bytesDone += amount;
/* 1889 */       this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/*      */     }
/* 1891 */     return bytesDone;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final int _readMore(InputStream in, byte[] readBuffer, int inputPtr, int inputEnd, int maxRead)
/*      */     throws IOException
/*      */   {
/* 1899 */     int i = 0;
/* 1900 */     while (inputPtr < inputEnd) {
/* 1901 */       readBuffer[(i++)] = readBuffer[(inputPtr++)];
/*      */     }
/* 1903 */     inputPtr = 0;
/* 1904 */     inputEnd = i;
/* 1905 */     maxRead = Math.min(maxRead, readBuffer.length);
/*      */     do
/*      */     {
/* 1908 */       int length = maxRead - inputEnd;
/* 1909 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 1912 */       int count = in.read(readBuffer, inputEnd, length);
/* 1913 */       if (count < 0) {
/* 1914 */         return inputEnd;
/*      */       }
/* 1916 */       inputEnd += count;
/* 1917 */     } while (inputEnd < 3);
/* 1918 */     return inputEnd;
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
/*      */   private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputEnd)
/*      */     throws IOException
/*      */   {
/* 1936 */     if ((ch >= 55296) && 
/* 1937 */       (ch <= 57343))
/*      */     {
/* 1939 */       if ((inputOffset >= inputEnd) || (cbuf == null)) {
/* 1940 */         _reportError(String.format("Split surrogate on writeRaw() input (last character): first character 0x%4x", new Object[] { Integer.valueOf(ch) }));
/*      */       }
/*      */       
/* 1943 */       _outputSurrogates(ch, cbuf[inputOffset]);
/* 1944 */       return inputOffset + 1;
/*      */     }
/*      */     
/* 1947 */     byte[] bbuf = this._outputBuffer;
/* 1948 */     bbuf[(this._outputTail++)] = ((byte)(0xE0 | ch >> 12));
/* 1949 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 1950 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | ch & 0x3F));
/* 1951 */     return inputOffset;
/*      */   }
/*      */   
/*      */   protected final void _outputSurrogates(int surr1, int surr2) throws IOException
/*      */   {
/* 1956 */     int c = _decodeSurrogate(surr1, surr2);
/* 1957 */     if (this._outputTail + 4 > this._outputEnd) {
/* 1958 */       _flushBuffer();
/*      */     }
/* 1960 */     byte[] bbuf = this._outputBuffer;
/* 1961 */     bbuf[(this._outputTail++)] = ((byte)(0xF0 | c >> 18));
/* 1962 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c >> 12 & 0x3F));
/* 1963 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c >> 6 & 0x3F));
/* 1964 */     bbuf[(this._outputTail++)] = ((byte)(0x80 | c & 0x3F));
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
/*      */   private final int _outputMultiByteChar(int ch, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 1978 */     byte[] bbuf = this._outputBuffer;
/* 1979 */     if ((ch >= 55296) && (ch <= 57343))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1984 */       bbuf[(outputPtr++)] = 92;
/* 1985 */       bbuf[(outputPtr++)] = 117;
/*      */       
/* 1987 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 12 & 0xF)];
/* 1988 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 8 & 0xF)];
/* 1989 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 4 & 0xF)];
/* 1990 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch & 0xF)];
/*      */     }
/*      */     else {
/* 1993 */       bbuf[(outputPtr++)] = ((byte)(0xE0 | ch >> 12));
/* 1994 */       bbuf[(outputPtr++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 1995 */       bbuf[(outputPtr++)] = ((byte)(0x80 | ch & 0x3F));
/*      */     }
/* 1997 */     return outputPtr;
/*      */   }
/*      */   
/*      */   private final void _writeNull() throws IOException
/*      */   {
/* 2002 */     if (this._outputTail + 4 >= this._outputEnd) {
/* 2003 */       _flushBuffer();
/*      */     }
/* 2005 */     System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
/* 2006 */     this._outputTail += 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int _writeGenericEscape(int charToEscape, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 2016 */     byte[] bbuf = this._outputBuffer;
/* 2017 */     bbuf[(outputPtr++)] = 92;
/* 2018 */     bbuf[(outputPtr++)] = 117;
/* 2019 */     if (charToEscape > 255) {
/* 2020 */       int hi = charToEscape >> 8 & 0xFF;
/* 2021 */       bbuf[(outputPtr++)] = HEX_CHARS[(hi >> 4)];
/* 2022 */       bbuf[(outputPtr++)] = HEX_CHARS[(hi & 0xF)];
/* 2023 */       charToEscape &= 0xFF;
/*      */     } else {
/* 2025 */       bbuf[(outputPtr++)] = 48;
/* 2026 */       bbuf[(outputPtr++)] = 48;
/*      */     }
/*      */     
/* 2029 */     bbuf[(outputPtr++)] = HEX_CHARS[(charToEscape >> 4)];
/* 2030 */     bbuf[(outputPtr++)] = HEX_CHARS[(charToEscape & 0xF)];
/* 2031 */     return outputPtr;
/*      */   }
/*      */   
/*      */   protected final void _flushBuffer() throws IOException
/*      */   {
/* 2036 */     int len = this._outputTail;
/* 2037 */     if (len > 0) {
/* 2038 */       this._outputTail = 0;
/* 2039 */       this._outputStream.write(this._outputBuffer, 0, len);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\UTF8JsonGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */