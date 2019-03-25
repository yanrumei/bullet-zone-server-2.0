/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.NumberOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Writer;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ public final class WriterBasedJsonGenerator extends JsonGeneratorImpl
/*      */ {
/*      */   protected static final int SHORT_WRITE = 32;
/*   19 */   protected static final char[] HEX_CHARS = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Writer _writer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   35 */   protected char _quoteChar = '"';
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _outputBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int _outputHead;
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
/*      */   protected int _outputEnd;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char[] _entityBuffer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializableString _currentEscape;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public WriterBasedJsonGenerator(IOContext ctxt, int features, com.fasterxml.jackson.core.ObjectCodec codec, Writer w)
/*      */   {
/*   87 */     super(ctxt, features, codec);
/*   88 */     this._writer = w;
/*   89 */     this._outputBuffer = ctxt.allocConcatBuffer();
/*   90 */     this._outputEnd = this._outputBuffer.length;
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
/*  101 */     return this._writer;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getOutputBuffered()
/*      */   {
/*  107 */     int len = this._outputTail - this._outputHead;
/*  108 */     return Math.max(0, len);
/*      */   }
/*      */   
/*      */   public boolean canWriteFormattedNumbers()
/*      */   {
/*  113 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeFieldName(String name)
/*      */     throws IOException
/*      */   {
/*  124 */     int status = this._writeContext.writeFieldName(name);
/*  125 */     if (status == 4) {
/*  126 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  128 */     _writeFieldName(name, status == 1);
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeFieldName(SerializableString name)
/*      */     throws IOException
/*      */   {
/*  135 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  136 */     if (status == 4) {
/*  137 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  139 */     _writeFieldName(name, status == 1);
/*      */   }
/*      */   
/*      */   protected void _writeFieldName(String name, boolean commaBefore) throws IOException
/*      */   {
/*  144 */     if (this._cfgPrettyPrinter != null) {
/*  145 */       _writePPFieldName(name, commaBefore);
/*  146 */       return;
/*      */     }
/*      */     
/*  149 */     if (this._outputTail + 1 >= this._outputEnd) {
/*  150 */       _flushBuffer();
/*      */     }
/*  152 */     if (commaBefore) {
/*  153 */       this._outputBuffer[(this._outputTail++)] = ',';
/*      */     }
/*      */     
/*  156 */     if (this._cfgUnqNames) {
/*  157 */       _writeString(name);
/*  158 */       return;
/*      */     }
/*      */     
/*  161 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  163 */     _writeString(name);
/*      */     
/*  165 */     if (this._outputTail >= this._outputEnd) {
/*  166 */       _flushBuffer();
/*      */     }
/*  168 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   protected void _writeFieldName(SerializableString name, boolean commaBefore) throws IOException
/*      */   {
/*  173 */     if (this._cfgPrettyPrinter != null) {
/*  174 */       _writePPFieldName(name, commaBefore);
/*  175 */       return;
/*      */     }
/*      */     
/*  178 */     if (this._outputTail + 1 >= this._outputEnd) {
/*  179 */       _flushBuffer();
/*      */     }
/*  181 */     if (commaBefore) {
/*  182 */       this._outputBuffer[(this._outputTail++)] = ',';
/*      */     }
/*      */     
/*  185 */     char[] quoted = name.asQuotedChars();
/*  186 */     if (this._cfgUnqNames) {
/*  187 */       writeRaw(quoted, 0, quoted.length);
/*  188 */       return;
/*      */     }
/*      */     
/*  191 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  193 */     int qlen = quoted.length;
/*  194 */     if (this._outputTail + qlen + 1 >= this._outputEnd) {
/*  195 */       writeRaw(quoted, 0, qlen);
/*      */       
/*  197 */       if (this._outputTail >= this._outputEnd) {
/*  198 */         _flushBuffer();
/*      */       }
/*  200 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     } else {
/*  202 */       System.arraycopy(quoted, 0, this._outputBuffer, this._outputTail, qlen);
/*  203 */       this._outputTail += qlen;
/*  204 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeStartArray()
/*      */     throws IOException
/*      */   {
/*  217 */     _verifyValueWrite("start an array");
/*  218 */     this._writeContext = this._writeContext.createChildArrayContext();
/*  219 */     if (this._cfgPrettyPrinter != null) {
/*  220 */       this._cfgPrettyPrinter.writeStartArray(this);
/*      */     } else {
/*  222 */       if (this._outputTail >= this._outputEnd) {
/*  223 */         _flushBuffer();
/*      */       }
/*  225 */       this._outputBuffer[(this._outputTail++)] = '[';
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeEndArray()
/*      */     throws IOException
/*      */   {
/*  232 */     if (!this._writeContext.inArray()) {
/*  233 */       _reportError("Current context not Array but " + this._writeContext.typeDesc());
/*      */     }
/*  235 */     if (this._cfgPrettyPrinter != null) {
/*  236 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  238 */       if (this._outputTail >= this._outputEnd) {
/*  239 */         _flushBuffer();
/*      */       }
/*  241 */       this._outputBuffer[(this._outputTail++)] = ']';
/*      */     }
/*  243 */     this._writeContext = this._writeContext.clearAndGetParent();
/*      */   }
/*      */   
/*      */   public void writeStartObject(Object forValue)
/*      */     throws IOException
/*      */   {
/*  249 */     _verifyValueWrite("start an object");
/*  250 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext();
/*  251 */     this._writeContext = ctxt;
/*  252 */     if (forValue != null) {
/*  253 */       ctxt.setCurrentValue(forValue);
/*      */     }
/*  255 */     if (this._cfgPrettyPrinter != null) {
/*  256 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  258 */       if (this._outputTail >= this._outputEnd) {
/*  259 */         _flushBuffer();
/*      */       }
/*  261 */       this._outputBuffer[(this._outputTail++)] = '{';
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeStartObject()
/*      */     throws IOException
/*      */   {
/*  268 */     _verifyValueWrite("start an object");
/*  269 */     this._writeContext = this._writeContext.createChildObjectContext();
/*  270 */     if (this._cfgPrettyPrinter != null) {
/*  271 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  273 */       if (this._outputTail >= this._outputEnd) {
/*  274 */         _flushBuffer();
/*      */       }
/*  276 */       this._outputBuffer[(this._outputTail++)] = '{';
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeEndObject()
/*      */     throws IOException
/*      */   {
/*  283 */     if (!this._writeContext.inObject()) {
/*  284 */       _reportError("Current context not Object but " + this._writeContext.typeDesc());
/*      */     }
/*  286 */     if (this._cfgPrettyPrinter != null) {
/*  287 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  289 */       if (this._outputTail >= this._outputEnd) {
/*  290 */         _flushBuffer();
/*      */       }
/*  292 */       this._outputBuffer[(this._outputTail++)] = '}';
/*      */     }
/*  294 */     this._writeContext = this._writeContext.clearAndGetParent();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _writePPFieldName(String name, boolean commaBefore)
/*      */     throws IOException
/*      */   {
/*  303 */     if (commaBefore) {
/*  304 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  306 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */     
/*  309 */     if (this._cfgUnqNames) {
/*  310 */       _writeString(name);
/*      */     } else {
/*  312 */       if (this._outputTail >= this._outputEnd) {
/*  313 */         _flushBuffer();
/*      */       }
/*  315 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  316 */       _writeString(name);
/*  317 */       if (this._outputTail >= this._outputEnd) {
/*  318 */         _flushBuffer();
/*      */       }
/*  320 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _writePPFieldName(SerializableString name, boolean commaBefore) throws IOException
/*      */   {
/*  326 */     if (commaBefore) {
/*  327 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     } else {
/*  329 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */     
/*  332 */     char[] quoted = name.asQuotedChars();
/*  333 */     if (this._cfgUnqNames) {
/*  334 */       writeRaw(quoted, 0, quoted.length);
/*      */     } else {
/*  336 */       if (this._outputTail >= this._outputEnd) {
/*  337 */         _flushBuffer();
/*      */       }
/*  339 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  340 */       writeRaw(quoted, 0, quoted.length);
/*  341 */       if (this._outputTail >= this._outputEnd) {
/*  342 */         _flushBuffer();
/*      */       }
/*  344 */       this._outputBuffer[(this._outputTail++)] = this._quoteChar;
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
/*  357 */     _verifyValueWrite("write a string");
/*  358 */     if (text == null) {
/*  359 */       _writeNull();
/*  360 */       return;
/*      */     }
/*  362 */     if (this._outputTail >= this._outputEnd) {
/*  363 */       _flushBuffer();
/*      */     }
/*  365 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  366 */     _writeString(text);
/*      */     
/*  368 */     if (this._outputTail >= this._outputEnd) {
/*  369 */       _flushBuffer();
/*      */     }
/*  371 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeString(char[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  377 */     _verifyValueWrite("write a string");
/*  378 */     if (this._outputTail >= this._outputEnd) {
/*  379 */       _flushBuffer();
/*      */     }
/*  381 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  382 */     _writeString(text, offset, len);
/*      */     
/*  384 */     if (this._outputTail >= this._outputEnd) {
/*  385 */       _flushBuffer();
/*      */     }
/*  387 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeString(SerializableString sstr)
/*      */     throws IOException
/*      */   {
/*  393 */     _verifyValueWrite("write a string");
/*  394 */     if (this._outputTail >= this._outputEnd) {
/*  395 */       _flushBuffer();
/*      */     }
/*  397 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */     
/*  399 */     char[] text = sstr.asQuotedChars();
/*  400 */     int len = text.length;
/*      */     
/*  402 */     if (len < 32) {
/*  403 */       int room = this._outputEnd - this._outputTail;
/*  404 */       if (len > room) {
/*  405 */         _flushBuffer();
/*      */       }
/*  407 */       System.arraycopy(text, 0, this._outputBuffer, this._outputTail, len);
/*  408 */       this._outputTail += len;
/*      */     }
/*      */     else {
/*  411 */       _flushBuffer();
/*  412 */       this._writer.write(text, 0, len);
/*      */     }
/*  414 */     if (this._outputTail >= this._outputEnd) {
/*  415 */       _flushBuffer();
/*      */     }
/*  417 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  423 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */   public void writeUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  429 */     _reportUnsupportedOperation();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeRaw(String text)
/*      */     throws IOException
/*      */   {
/*  442 */     int len = text.length();
/*  443 */     int room = this._outputEnd - this._outputTail;
/*      */     
/*  445 */     if (room == 0) {
/*  446 */       _flushBuffer();
/*  447 */       room = this._outputEnd - this._outputTail;
/*      */     }
/*      */     
/*  450 */     if (room >= len) {
/*  451 */       text.getChars(0, len, this._outputBuffer, this._outputTail);
/*  452 */       this._outputTail += len;
/*      */     } else {
/*  454 */       writeRawLong(text);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeRaw(String text, int start, int len)
/*      */     throws IOException
/*      */   {
/*  462 */     int room = this._outputEnd - this._outputTail;
/*      */     
/*  464 */     if (room < len) {
/*  465 */       _flushBuffer();
/*  466 */       room = this._outputEnd - this._outputTail;
/*      */     }
/*      */     
/*  469 */     if (room >= len) {
/*  470 */       text.getChars(start, start + len, this._outputBuffer, this._outputTail);
/*  471 */       this._outputTail += len;
/*      */     } else {
/*  473 */       writeRawLong(text.substring(start, start + len));
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeRaw(SerializableString text)
/*      */     throws IOException
/*      */   {
/*  480 */     writeRaw(text.getValue());
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeRaw(char[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  487 */     if (len < 32) {
/*  488 */       int room = this._outputEnd - this._outputTail;
/*  489 */       if (len > room) {
/*  490 */         _flushBuffer();
/*      */       }
/*  492 */       System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
/*  493 */       this._outputTail += len;
/*  494 */       return;
/*      */     }
/*      */     
/*  497 */     _flushBuffer();
/*  498 */     this._writer.write(text, offset, len);
/*      */   }
/*      */   
/*      */   public void writeRaw(char c)
/*      */     throws IOException
/*      */   {
/*  504 */     if (this._outputTail >= this._outputEnd) {
/*  505 */       _flushBuffer();
/*      */     }
/*  507 */     this._outputBuffer[(this._outputTail++)] = c;
/*      */   }
/*      */   
/*      */   private void writeRawLong(String text) throws IOException
/*      */   {
/*  512 */     int room = this._outputEnd - this._outputTail;
/*      */     
/*  514 */     text.getChars(0, room, this._outputBuffer, this._outputTail);
/*  515 */     this._outputTail += room;
/*  516 */     _flushBuffer();
/*  517 */     int offset = room;
/*  518 */     int len = text.length() - room;
/*      */     
/*  520 */     while (len > this._outputEnd) {
/*  521 */       int amount = this._outputEnd;
/*  522 */       text.getChars(offset, offset + amount, this._outputBuffer, 0);
/*  523 */       this._outputHead = 0;
/*  524 */       this._outputTail = amount;
/*  525 */       _flushBuffer();
/*  526 */       offset += amount;
/*  527 */       len -= amount;
/*      */     }
/*      */     
/*  530 */     text.getChars(offset, offset + len, this._outputBuffer, 0);
/*  531 */     this._outputHead = 0;
/*  532 */     this._outputTail = len;
/*      */   }
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
/*  545 */     _verifyValueWrite("write a binary value");
/*      */     
/*  547 */     if (this._outputTail >= this._outputEnd) {
/*  548 */       _flushBuffer();
/*      */     }
/*  550 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  551 */     _writeBinary(b64variant, data, offset, offset + len);
/*      */     
/*  553 */     if (this._outputTail >= this._outputEnd) {
/*  554 */       _flushBuffer();
/*      */     }
/*  556 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  564 */     _verifyValueWrite("write a binary value");
/*      */     
/*  566 */     if (this._outputTail >= this._outputEnd) {
/*  567 */       _flushBuffer();
/*      */     }
/*  569 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  570 */     byte[] encodingBuffer = this._ioContext.allocBase64Buffer();
/*      */     int bytes;
/*      */     try { int bytes;
/*  573 */       if (dataLength < 0) {
/*  574 */         bytes = _writeBinary(b64variant, data, encodingBuffer);
/*      */       } else {
/*  576 */         int missing = _writeBinary(b64variant, data, encodingBuffer, dataLength);
/*  577 */         if (missing > 0) {
/*  578 */           _reportError("Too few bytes available: missing " + missing + " bytes (out of " + dataLength + ")");
/*      */         }
/*  580 */         bytes = dataLength;
/*      */       }
/*      */     } finally {
/*  583 */       this._ioContext.releaseBase64Buffer(encodingBuffer);
/*      */     }
/*      */     
/*  586 */     if (this._outputTail >= this._outputEnd) {
/*  587 */       _flushBuffer();
/*      */     }
/*  589 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  590 */     return bytes;
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
/*  602 */     _verifyValueWrite("write a number");
/*  603 */     if (this._cfgNumbersAsStrings) {
/*  604 */       _writeQuotedShort(s);
/*  605 */       return;
/*      */     }
/*      */     
/*  608 */     if (this._outputTail + 6 >= this._outputEnd) {
/*  609 */       _flushBuffer();
/*      */     }
/*  611 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private void _writeQuotedShort(short s) throws IOException {
/*  615 */     if (this._outputTail + 8 >= this._outputEnd) {
/*  616 */       _flushBuffer();
/*      */     }
/*  618 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  619 */     this._outputTail = NumberOutput.outputInt(s, this._outputBuffer, this._outputTail);
/*  620 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeNumber(int i)
/*      */     throws IOException
/*      */   {
/*  626 */     _verifyValueWrite("write a number");
/*  627 */     if (this._cfgNumbersAsStrings) {
/*  628 */       _writeQuotedInt(i);
/*  629 */       return;
/*      */     }
/*      */     
/*  632 */     if (this._outputTail + 11 >= this._outputEnd) {
/*  633 */       _flushBuffer();
/*      */     }
/*  635 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private void _writeQuotedInt(int i) throws IOException {
/*  639 */     if (this._outputTail + 13 >= this._outputEnd) {
/*  640 */       _flushBuffer();
/*      */     }
/*  642 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  643 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*  644 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeNumber(long l)
/*      */     throws IOException
/*      */   {
/*  650 */     _verifyValueWrite("write a number");
/*  651 */     if (this._cfgNumbersAsStrings) {
/*  652 */       _writeQuotedLong(l);
/*  653 */       return;
/*      */     }
/*  655 */     if (this._outputTail + 21 >= this._outputEnd)
/*      */     {
/*  657 */       _flushBuffer();
/*      */     }
/*  659 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*      */   }
/*      */   
/*      */   private void _writeQuotedLong(long l) throws IOException {
/*  663 */     if (this._outputTail + 23 >= this._outputEnd) {
/*  664 */       _flushBuffer();
/*      */     }
/*  666 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  667 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*  668 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeNumber(BigInteger value)
/*      */     throws IOException
/*      */   {
/*  676 */     _verifyValueWrite("write a number");
/*  677 */     if (value == null) {
/*  678 */       _writeNull();
/*  679 */     } else if (this._cfgNumbersAsStrings) {
/*  680 */       _writeQuotedRaw(value.toString());
/*      */     } else {
/*  682 */       writeRaw(value.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(double d)
/*      */     throws IOException
/*      */   {
/*  690 */     if ((this._cfgNumbersAsStrings) || ((isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)) && ((Double.isNaN(d)) || (Double.isInfinite(d)))))
/*      */     {
/*      */ 
/*  693 */       writeString(String.valueOf(d));
/*  694 */       return;
/*      */     }
/*      */     
/*  697 */     _verifyValueWrite("write a number");
/*  698 */     writeRaw(String.valueOf(d));
/*      */   }
/*      */   
/*      */   public void writeNumber(float f)
/*      */     throws IOException
/*      */   {
/*  704 */     if ((this._cfgNumbersAsStrings) || ((isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS)) && ((Float.isNaN(f)) || (Float.isInfinite(f)))))
/*      */     {
/*      */ 
/*  707 */       writeString(String.valueOf(f));
/*  708 */       return;
/*      */     }
/*      */     
/*  711 */     _verifyValueWrite("write a number");
/*  712 */     writeRaw(String.valueOf(f));
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeNumber(java.math.BigDecimal value)
/*      */     throws IOException
/*      */   {
/*  719 */     _verifyValueWrite("write a number");
/*  720 */     if (value == null) {
/*  721 */       _writeNull();
/*  722 */     } else if (this._cfgNumbersAsStrings) {
/*  723 */       _writeQuotedRaw(_asString(value));
/*      */     } else {
/*  725 */       writeRaw(_asString(value));
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException
/*      */   {
/*  732 */     _verifyValueWrite("write a number");
/*  733 */     if (this._cfgNumbersAsStrings) {
/*  734 */       _writeQuotedRaw(encodedValue);
/*      */     } else {
/*  736 */       writeRaw(encodedValue);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _writeQuotedRaw(String value) throws IOException
/*      */   {
/*  742 */     if (this._outputTail >= this._outputEnd) {
/*  743 */       _flushBuffer();
/*      */     }
/*  745 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*  746 */     writeRaw(value);
/*  747 */     if (this._outputTail >= this._outputEnd) {
/*  748 */       _flushBuffer();
/*      */     }
/*  750 */     this._outputBuffer[(this._outputTail++)] = this._quoteChar;
/*      */   }
/*      */   
/*      */   public void writeBoolean(boolean state)
/*      */     throws IOException
/*      */   {
/*  756 */     _verifyValueWrite("write a boolean value");
/*  757 */     if (this._outputTail + 5 >= this._outputEnd) {
/*  758 */       _flushBuffer();
/*      */     }
/*  760 */     int ptr = this._outputTail;
/*  761 */     char[] buf = this._outputBuffer;
/*  762 */     if (state) {
/*  763 */       buf[ptr] = 't';
/*  764 */       buf[(++ptr)] = 'r';
/*  765 */       buf[(++ptr)] = 'u';
/*  766 */       buf[(++ptr)] = 'e';
/*      */     } else {
/*  768 */       buf[ptr] = 'f';
/*  769 */       buf[(++ptr)] = 'a';
/*  770 */       buf[(++ptr)] = 'l';
/*  771 */       buf[(++ptr)] = 's';
/*  772 */       buf[(++ptr)] = 'e';
/*      */     }
/*  774 */     this._outputTail = (ptr + 1);
/*      */   }
/*      */   
/*      */   public void writeNull() throws IOException
/*      */   {
/*  779 */     _verifyValueWrite("write a null");
/*  780 */     _writeNull();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _verifyValueWrite(String typeMsg)
/*      */     throws IOException
/*      */   {
/*  792 */     int status = this._writeContext.writeValue();
/*  793 */     if (this._cfgPrettyPrinter != null)
/*      */     {
/*  795 */       _verifyPrettyValueWrite(typeMsg, status); return;
/*      */     }
/*      */     
/*      */     char c;
/*  799 */     switch (status) {
/*      */     case 0: case 4: 
/*      */     default: 
/*  802 */       return;
/*      */     case 1: 
/*  804 */       c = ',';
/*  805 */       break;
/*      */     case 2: 
/*  807 */       c = ':';
/*  808 */       break;
/*      */     case 3: 
/*  810 */       if (this._rootValueSeparator != null) {
/*  811 */         writeRaw(this._rootValueSeparator.getValue());
/*      */       }
/*  813 */       return;
/*      */     case 5: 
/*  815 */       _reportCantWriteValueExpectName(typeMsg);
/*  816 */       return;
/*      */     }
/*  818 */     if (this._outputTail >= this._outputEnd) {
/*  819 */       _flushBuffer();
/*      */     }
/*  821 */     this._outputBuffer[(this._outputTail++)] = c;
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
/*  833 */     _flushBuffer();
/*  834 */     if ((this._writer != null) && 
/*  835 */       (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))) {
/*  836 */       this._writer.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  844 */     super.close();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  850 */     if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT))) {
/*      */       for (;;)
/*      */       {
/*  853 */         com.fasterxml.jackson.core.JsonStreamContext ctxt = getOutputContext();
/*  854 */         if (ctxt.inArray()) {
/*  855 */           writeEndArray();
/*  856 */         } else { if (!ctxt.inObject()) break;
/*  857 */           writeEndObject();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  863 */     _flushBuffer();
/*  864 */     this._outputHead = 0;
/*  865 */     this._outputTail = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  873 */     if (this._writer != null) {
/*  874 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET))) {
/*  875 */         this._writer.close();
/*  876 */       } else if (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM))
/*      */       {
/*  878 */         this._writer.flush();
/*      */       }
/*      */     }
/*      */     
/*  882 */     _releaseBuffers();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */   {
/*  888 */     char[] buf = this._outputBuffer;
/*  889 */     if (buf != null) {
/*  890 */       this._outputBuffer = null;
/*  891 */       this._ioContext.releaseConcatBuffer(buf);
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
/*      */   private void _writeString(String text)
/*      */     throws IOException
/*      */   {
/*  908 */     int len = text.length();
/*  909 */     if (len > this._outputEnd) {
/*  910 */       _writeLongString(text);
/*  911 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  916 */     if (this._outputTail + len > this._outputEnd) {
/*  917 */       _flushBuffer();
/*      */     }
/*  919 */     text.getChars(0, len, this._outputBuffer, this._outputTail);
/*      */     
/*  921 */     if (this._characterEscapes != null) {
/*  922 */       _writeStringCustom(len);
/*  923 */     } else if (this._maximumNonEscapedChar != 0) {
/*  924 */       _writeStringASCII(len, this._maximumNonEscapedChar);
/*      */     } else {
/*  926 */       _writeString2(len);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _writeString2(int len)
/*      */     throws IOException
/*      */   {
/*  933 */     int end = this._outputTail + len;
/*  934 */     int[] escCodes = this._outputEscapes;
/*  935 */     int escLen = escCodes.length;
/*      */     
/*      */ 
/*  938 */     while (this._outputTail < end)
/*      */     {
/*      */       for (;;)
/*      */       {
/*  942 */         char c = this._outputBuffer[this._outputTail];
/*  943 */         if ((c < escLen) && (escCodes[c] != 0)) {
/*      */           break;
/*      */         }
/*  946 */         if (++this._outputTail >= end) {
/*      */           return;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  955 */       int flushLen = this._outputTail - this._outputHead;
/*  956 */       if (flushLen > 0) {
/*  957 */         this._writer.write(this._outputBuffer, this._outputHead, flushLen);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  962 */       char c = this._outputBuffer[(this._outputTail++)];
/*  963 */       _prependOrWriteCharacterEscape(c, escCodes[c]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _writeLongString(String text)
/*      */     throws IOException
/*      */   {
/*  974 */     _flushBuffer();
/*      */     
/*      */ 
/*  977 */     int textLen = text.length();
/*  978 */     int offset = 0;
/*      */     do {
/*  980 */       int max = this._outputEnd;
/*  981 */       int segmentLen = offset + max > textLen ? textLen - offset : max;
/*      */       
/*  983 */       text.getChars(offset, offset + segmentLen, this._outputBuffer, 0);
/*  984 */       if (this._characterEscapes != null) {
/*  985 */         _writeSegmentCustom(segmentLen);
/*  986 */       } else if (this._maximumNonEscapedChar != 0) {
/*  987 */         _writeSegmentASCII(segmentLen, this._maximumNonEscapedChar);
/*      */       } else {
/*  989 */         _writeSegment(segmentLen);
/*      */       }
/*  991 */       offset += segmentLen;
/*  992 */     } while (offset < textLen);
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
/*      */   private void _writeSegment(int end)
/*      */     throws IOException
/*      */   {
/* 1006 */     int[] escCodes = this._outputEscapes;
/* 1007 */     int escLen = escCodes.length;
/*      */     
/* 1009 */     int ptr = 0;
/* 1010 */     int start = ptr;
/*      */     
/*      */ 
/* 1013 */     while (ptr < end)
/*      */     {
/*      */       char c;
/*      */       for (;;) {
/* 1017 */         c = this._outputBuffer[ptr];
/* 1018 */         if ((c >= escLen) || (escCodes[c] == 0))
/*      */         {
/*      */ 
/* 1021 */           ptr++; if (ptr >= end) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1030 */       int flushLen = ptr - start;
/* 1031 */       if (flushLen > 0) {
/* 1032 */         this._writer.write(this._outputBuffer, start, flushLen);
/* 1033 */         if (ptr >= end) {
/*      */           break;
/*      */         }
/*      */       }
/* 1037 */       ptr++;
/*      */       
/* 1039 */       start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, escCodes[c]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _writeString(char[] text, int offset, int len)
/*      */     throws IOException
/*      */   {
/* 1049 */     if (this._characterEscapes != null) {
/* 1050 */       _writeStringCustom(text, offset, len);
/* 1051 */       return;
/*      */     }
/* 1053 */     if (this._maximumNonEscapedChar != 0) {
/* 1054 */       _writeStringASCII(text, offset, len, this._maximumNonEscapedChar);
/* 1055 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1062 */     len += offset;
/* 1063 */     int[] escCodes = this._outputEscapes;
/* 1064 */     int escLen = escCodes.length;
/* 1065 */     while (offset < len) {
/* 1066 */       int start = offset;
/*      */       for (;;)
/*      */       {
/* 1069 */         char c = text[offset];
/* 1070 */         if ((c < escLen) && (escCodes[c] != 0)) {
/*      */           break;
/*      */         }
/* 1073 */         offset++; if (offset >= len) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1079 */       int newAmount = offset - start;
/* 1080 */       if (newAmount < 32)
/*      */       {
/* 1082 */         if (this._outputTail + newAmount > this._outputEnd) {
/* 1083 */           _flushBuffer();
/*      */         }
/* 1085 */         if (newAmount > 0) {
/* 1086 */           System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
/* 1087 */           this._outputTail += newAmount;
/*      */         }
/*      */       } else {
/* 1090 */         _flushBuffer();
/* 1091 */         this._writer.write(text, start, newAmount);
/*      */       }
/*      */       
/* 1094 */       if (offset >= len) {
/*      */         break;
/*      */       }
/*      */       
/* 1098 */       char c = text[(offset++)];
/* 1099 */       _appendCharacterEscape(c, escCodes[c]);
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
/*      */   private void _writeStringASCII(int len, int maxNonEscaped)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1117 */     int end = this._outputTail + len;
/* 1118 */     int[] escCodes = this._outputEscapes;
/* 1119 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/* 1120 */     int escCode = 0;
/*      */     
/*      */ 
/* 1123 */     while (this._outputTail < end)
/*      */     {
/*      */       char c;
/*      */       do
/*      */       {
/* 1128 */         c = this._outputBuffer[this._outputTail];
/* 1129 */         if (c < escLimit) {
/* 1130 */           escCode = escCodes[c];
/* 1131 */           if (escCode != 0) {
/*      */             break;
/*      */           }
/* 1134 */         } else if (c > maxNonEscaped) {
/* 1135 */           escCode = -1;
/* 1136 */           break;
/*      */         }
/* 1138 */       } while (++this._outputTail < end);
/* 1139 */       break;
/*      */       
/*      */ 
/* 1142 */       int flushLen = this._outputTail - this._outputHead;
/* 1143 */       if (flushLen > 0) {
/* 1144 */         this._writer.write(this._outputBuffer, this._outputHead, flushLen);
/*      */       }
/* 1146 */       this._outputTail += 1;
/* 1147 */       _prependOrWriteCharacterEscape(c, escCode);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _writeSegmentASCII(int end, int maxNonEscaped)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1154 */     int[] escCodes = this._outputEscapes;
/* 1155 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/*      */     
/* 1157 */     int ptr = 0;
/* 1158 */     int escCode = 0;
/* 1159 */     int start = ptr;
/*      */     
/*      */ 
/* 1162 */     while (ptr < end)
/*      */     {
/*      */       char c;
/*      */       for (;;) {
/* 1166 */         c = this._outputBuffer[ptr];
/* 1167 */         if (c < escLimit) {
/* 1168 */           escCode = escCodes[c];
/* 1169 */           if (escCode != 0) {
/*      */             break;
/*      */           }
/* 1172 */         } else if (c > maxNonEscaped) {
/* 1173 */           escCode = -1;
/* 1174 */           break;
/*      */         }
/* 1176 */         ptr++; if (ptr >= end) {
/*      */           break;
/*      */         }
/*      */       }
/* 1180 */       int flushLen = ptr - start;
/* 1181 */       if (flushLen > 0) {
/* 1182 */         this._writer.write(this._outputBuffer, start, flushLen);
/* 1183 */         if (ptr >= end) {
/*      */           break;
/*      */         }
/*      */       }
/* 1187 */       ptr++;
/* 1188 */       start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, escCode);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void _writeStringASCII(char[] text, int offset, int len, int maxNonEscaped)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1196 */     len += offset;
/* 1197 */     int[] escCodes = this._outputEscapes;
/* 1198 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/*      */     
/* 1200 */     int escCode = 0;
/*      */     
/* 1202 */     while (offset < len) {
/* 1203 */       int start = offset;
/*      */       char c;
/*      */       for (;;)
/*      */       {
/* 1207 */         c = text[offset];
/* 1208 */         if (c < escLimit) {
/* 1209 */           escCode = escCodes[c];
/* 1210 */           if (escCode != 0) {
/*      */             break;
/*      */           }
/* 1213 */         } else if (c > maxNonEscaped) {
/* 1214 */           escCode = -1;
/* 1215 */           break;
/*      */         }
/* 1217 */         offset++; if (offset >= len) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1223 */       int newAmount = offset - start;
/* 1224 */       if (newAmount < 32)
/*      */       {
/* 1226 */         if (this._outputTail + newAmount > this._outputEnd) {
/* 1227 */           _flushBuffer();
/*      */         }
/* 1229 */         if (newAmount > 0) {
/* 1230 */           System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
/* 1231 */           this._outputTail += newAmount;
/*      */         }
/*      */       } else {
/* 1234 */         _flushBuffer();
/* 1235 */         this._writer.write(text, start, newAmount);
/*      */       }
/*      */       
/* 1238 */       if (offset >= len) {
/*      */         break;
/*      */       }
/*      */       
/* 1242 */       offset++;
/* 1243 */       _appendCharacterEscape(c, escCode);
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
/*      */   private void _writeStringCustom(int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1261 */     int end = this._outputTail + len;
/* 1262 */     int[] escCodes = this._outputEscapes;
/* 1263 */     int maxNonEscaped = this._maximumNonEscapedChar < 1 ? 65535 : this._maximumNonEscapedChar;
/* 1264 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/* 1265 */     int escCode = 0;
/* 1266 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/*      */ 
/* 1269 */     while (this._outputTail < end)
/*      */     {
/*      */       char c;
/*      */       do
/*      */       {
/* 1274 */         c = this._outputBuffer[this._outputTail];
/* 1275 */         if (c < escLimit) {
/* 1276 */           escCode = escCodes[c];
/* 1277 */           if (escCode != 0)
/*      */             break;
/*      */         } else {
/* 1280 */           if (c > maxNonEscaped) {
/* 1281 */             escCode = -1;
/* 1282 */             break;
/*      */           }
/* 1284 */           if ((this._currentEscape = customEscapes.getEscapeSequence(c)) != null) {
/* 1285 */             escCode = -2;
/* 1286 */             break;
/*      */           }
/*      */         }
/* 1289 */       } while (++this._outputTail < end);
/* 1290 */       break;
/*      */       
/*      */ 
/* 1293 */       int flushLen = this._outputTail - this._outputHead;
/* 1294 */       if (flushLen > 0) {
/* 1295 */         this._writer.write(this._outputBuffer, this._outputHead, flushLen);
/*      */       }
/* 1297 */       this._outputTail += 1;
/* 1298 */       _prependOrWriteCharacterEscape(c, escCode);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _writeSegmentCustom(int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1305 */     int[] escCodes = this._outputEscapes;
/* 1306 */     int maxNonEscaped = this._maximumNonEscapedChar < 1 ? 65535 : this._maximumNonEscapedChar;
/* 1307 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/* 1308 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/* 1310 */     int ptr = 0;
/* 1311 */     int escCode = 0;
/* 1312 */     int start = ptr;
/*      */     
/*      */ 
/* 1315 */     while (ptr < end)
/*      */     {
/*      */       char c;
/*      */       for (;;) {
/* 1319 */         c = this._outputBuffer[ptr];
/* 1320 */         if (c < escLimit) {
/* 1321 */           escCode = escCodes[c];
/* 1322 */           if (escCode != 0)
/*      */             break;
/*      */         } else {
/* 1325 */           if (c > maxNonEscaped) {
/* 1326 */             escCode = -1;
/* 1327 */             break;
/*      */           }
/* 1329 */           if ((this._currentEscape = customEscapes.getEscapeSequence(c)) != null) {
/* 1330 */             escCode = -2;
/* 1331 */             break;
/*      */           }
/*      */         }
/* 1334 */         ptr++; if (ptr >= end) {
/*      */           break;
/*      */         }
/*      */       }
/* 1338 */       int flushLen = ptr - start;
/* 1339 */       if (flushLen > 0) {
/* 1340 */         this._writer.write(this._outputBuffer, start, flushLen);
/* 1341 */         if (ptr >= end) {
/*      */           break;
/*      */         }
/*      */       }
/* 1345 */       ptr++;
/* 1346 */       start = _prependOrWriteCharacterEscape(this._outputBuffer, ptr, end, c, escCode);
/*      */     }
/*      */   }
/*      */   
/*      */   private void _writeStringCustom(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1353 */     len += offset;
/* 1354 */     int[] escCodes = this._outputEscapes;
/* 1355 */     int maxNonEscaped = this._maximumNonEscapedChar < 1 ? 65535 : this._maximumNonEscapedChar;
/* 1356 */     int escLimit = Math.min(escCodes.length, maxNonEscaped + 1);
/* 1357 */     CharacterEscapes customEscapes = this._characterEscapes;
/*      */     
/* 1359 */     int escCode = 0;
/*      */     
/* 1361 */     while (offset < len) {
/* 1362 */       int start = offset;
/*      */       char c;
/*      */       for (;;)
/*      */       {
/* 1366 */         c = text[offset];
/* 1367 */         if (c < escLimit) {
/* 1368 */           escCode = escCodes[c];
/* 1369 */           if (escCode != 0)
/*      */             break;
/*      */         } else {
/* 1372 */           if (c > maxNonEscaped) {
/* 1373 */             escCode = -1;
/* 1374 */             break;
/*      */           }
/* 1376 */           if ((this._currentEscape = customEscapes.getEscapeSequence(c)) != null) {
/* 1377 */             escCode = -2;
/* 1378 */             break;
/*      */           }
/*      */         }
/* 1381 */         offset++; if (offset >= len) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1387 */       int newAmount = offset - start;
/* 1388 */       if (newAmount < 32)
/*      */       {
/* 1390 */         if (this._outputTail + newAmount > this._outputEnd) {
/* 1391 */           _flushBuffer();
/*      */         }
/* 1393 */         if (newAmount > 0) {
/* 1394 */           System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
/* 1395 */           this._outputTail += newAmount;
/*      */         }
/*      */       } else {
/* 1398 */         _flushBuffer();
/* 1399 */         this._writer.write(text, start, newAmount);
/*      */       }
/*      */       
/* 1402 */       if (offset >= len) {
/*      */         break;
/*      */       }
/*      */       
/* 1406 */       offset++;
/* 1407 */       _appendCharacterEscape(c, escCode);
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
/*      */   protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1421 */     int safeInputEnd = inputEnd - 3;
/*      */     
/* 1423 */     int safeOutputEnd = this._outputEnd - 6;
/* 1424 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */ 
/* 1427 */     while (inputPtr <= safeInputEnd) {
/* 1428 */       if (this._outputTail > safeOutputEnd) {
/* 1429 */         _flushBuffer();
/*      */       }
/*      */       
/* 1432 */       int b24 = input[(inputPtr++)] << 8;
/* 1433 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 1434 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 1435 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1436 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*      */       {
/* 1438 */         this._outputBuffer[(this._outputTail++)] = '\\';
/* 1439 */         this._outputBuffer[(this._outputTail++)] = 'n';
/* 1440 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1445 */     int inputLeft = inputEnd - inputPtr;
/* 1446 */     if (inputLeft > 0) {
/* 1447 */       if (this._outputTail > safeOutputEnd) {
/* 1448 */         _flushBuffer();
/*      */       }
/* 1450 */       int b24 = input[(inputPtr++)] << 16;
/* 1451 */       if (inputLeft == 2) {
/* 1452 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*      */       }
/* 1454 */       this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer, int bytesLeft)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1463 */     int inputPtr = 0;
/* 1464 */     int inputEnd = 0;
/* 1465 */     int lastFullOffset = -3;
/*      */     
/*      */ 
/* 1468 */     int safeOutputEnd = this._outputEnd - 6;
/* 1469 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/* 1471 */     while (bytesLeft > 2) {
/* 1472 */       if (inputPtr > lastFullOffset) {
/* 1473 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1474 */         inputPtr = 0;
/* 1475 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1478 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1480 */       if (this._outputTail > safeOutputEnd) {
/* 1481 */         _flushBuffer();
/*      */       }
/* 1483 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1484 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1485 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1486 */       bytesLeft -= 3;
/* 1487 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1488 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1489 */         this._outputBuffer[(this._outputTail++)] = '\\';
/* 1490 */         this._outputBuffer[(this._outputTail++)] = 'n';
/* 1491 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1496 */     if (bytesLeft > 0) {
/* 1497 */       inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, bytesLeft);
/* 1498 */       inputPtr = 0;
/* 1499 */       if (inputEnd > 0) {
/* 1500 */         if (this._outputTail > safeOutputEnd) {
/* 1501 */           _flushBuffer();
/*      */         }
/* 1503 */         int b24 = readBuffer[(inputPtr++)] << 16;
/*      */         int amount;
/* 1505 */         int amount; if (inputPtr < inputEnd) {
/* 1506 */           b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1507 */           amount = 2;
/*      */         } else {
/* 1509 */           amount = 1;
/*      */         }
/* 1511 */         this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/* 1512 */         bytesLeft -= amount;
/*      */       }
/*      */     }
/* 1515 */     return bytesLeft;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int _writeBinary(Base64Variant b64variant, InputStream data, byte[] readBuffer)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1523 */     int inputPtr = 0;
/* 1524 */     int inputEnd = 0;
/* 1525 */     int lastFullOffset = -3;
/* 1526 */     int bytesDone = 0;
/*      */     
/*      */ 
/* 1529 */     int safeOutputEnd = this._outputEnd - 6;
/* 1530 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1534 */       if (inputPtr > lastFullOffset) {
/* 1535 */         inputEnd = _readMore(data, readBuffer, inputPtr, inputEnd, readBuffer.length);
/* 1536 */         inputPtr = 0;
/* 1537 */         if (inputEnd < 3) {
/*      */           break;
/*      */         }
/* 1540 */         lastFullOffset = inputEnd - 3;
/*      */       }
/* 1542 */       if (this._outputTail > safeOutputEnd) {
/* 1543 */         _flushBuffer();
/*      */       }
/*      */       
/* 1546 */       int b24 = readBuffer[(inputPtr++)] << 8;
/* 1547 */       b24 |= readBuffer[(inputPtr++)] & 0xFF;
/* 1548 */       b24 = b24 << 8 | readBuffer[(inputPtr++)] & 0xFF;
/* 1549 */       bytesDone += 3;
/* 1550 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1551 */       chunksBeforeLF--; if (chunksBeforeLF <= 0) {
/* 1552 */         this._outputBuffer[(this._outputTail++)] = '\\';
/* 1553 */         this._outputBuffer[(this._outputTail++)] = 'n';
/* 1554 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1559 */     if (inputPtr < inputEnd) {
/* 1560 */       if (this._outputTail > safeOutputEnd) {
/* 1561 */         _flushBuffer();
/*      */       }
/* 1563 */       int b24 = readBuffer[(inputPtr++)] << 16;
/* 1564 */       int amount = 1;
/* 1565 */       if (inputPtr < inputEnd) {
/* 1566 */         b24 |= (readBuffer[inputPtr] & 0xFF) << 8;
/* 1567 */         amount = 2;
/*      */       }
/* 1569 */       bytesDone += amount;
/* 1570 */       this._outputTail = b64variant.encodeBase64Partial(b24, amount, this._outputBuffer, this._outputTail);
/*      */     }
/* 1572 */     return bytesDone;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int _readMore(InputStream in, byte[] readBuffer, int inputPtr, int inputEnd, int maxRead)
/*      */     throws IOException
/*      */   {
/* 1580 */     int i = 0;
/* 1581 */     while (inputPtr < inputEnd) {
/* 1582 */       readBuffer[(i++)] = readBuffer[(inputPtr++)];
/*      */     }
/* 1584 */     inputPtr = 0;
/* 1585 */     inputEnd = i;
/* 1586 */     maxRead = Math.min(maxRead, readBuffer.length);
/*      */     do
/*      */     {
/* 1589 */       int length = maxRead - inputEnd;
/* 1590 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 1593 */       int count = in.read(readBuffer, inputEnd, length);
/* 1594 */       if (count < 0) {
/* 1595 */         return inputEnd;
/*      */       }
/* 1597 */       inputEnd += count;
/* 1598 */     } while (inputEnd < 3);
/* 1599 */     return inputEnd;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeNull()
/*      */     throws IOException
/*      */   {
/* 1610 */     if (this._outputTail + 4 >= this._outputEnd) {
/* 1611 */       _flushBuffer();
/*      */     }
/* 1613 */     int ptr = this._outputTail;
/* 1614 */     char[] buf = this._outputBuffer;
/* 1615 */     buf[ptr] = 'n';
/* 1616 */     buf[(++ptr)] = 'u';
/* 1617 */     buf[(++ptr)] = 'l';
/* 1618 */     buf[(++ptr)] = 'l';
/* 1619 */     this._outputTail = (ptr + 1);
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
/*      */   private void _prependOrWriteCharacterEscape(char ch, int escCode)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1636 */     if (escCode >= 0) {
/* 1637 */       if (this._outputTail >= 2) {
/* 1638 */         int ptr = this._outputTail - 2;
/* 1639 */         this._outputHead = ptr;
/* 1640 */         this._outputBuffer[(ptr++)] = '\\';
/* 1641 */         this._outputBuffer[ptr] = ((char)escCode);
/* 1642 */         return;
/*      */       }
/*      */       
/* 1645 */       char[] buf = this._entityBuffer;
/* 1646 */       if (buf == null) {
/* 1647 */         buf = _allocateEntityBuffer();
/*      */       }
/* 1649 */       this._outputHead = this._outputTail;
/* 1650 */       buf[1] = ((char)escCode);
/* 1651 */       this._writer.write(buf, 0, 2);
/* 1652 */       return;
/*      */     }
/* 1654 */     if (escCode != -2) {
/* 1655 */       if (this._outputTail >= 6) {
/* 1656 */         char[] buf = this._outputBuffer;
/* 1657 */         int ptr = this._outputTail - 6;
/* 1658 */         this._outputHead = ptr;
/* 1659 */         buf[ptr] = '\\';
/* 1660 */         buf[(++ptr)] = 'u';
/*      */         
/* 1662 */         if (ch > 'ÿ') {
/* 1663 */           int hi = ch >> '\b' & 0xFF;
/* 1664 */           buf[(++ptr)] = HEX_CHARS[(hi >> 4)];
/* 1665 */           buf[(++ptr)] = HEX_CHARS[(hi & 0xF)];
/* 1666 */           ch = (char)(ch & 0xFF);
/*      */         } else {
/* 1668 */           buf[(++ptr)] = '0';
/* 1669 */           buf[(++ptr)] = '0';
/*      */         }
/* 1671 */         buf[(++ptr)] = HEX_CHARS[(ch >> '\004')];
/* 1672 */         buf[(++ptr)] = HEX_CHARS[(ch & 0xF)];
/* 1673 */         return;
/*      */       }
/*      */       
/* 1676 */       char[] buf = this._entityBuffer;
/* 1677 */       if (buf == null) {
/* 1678 */         buf = _allocateEntityBuffer();
/*      */       }
/* 1680 */       this._outputHead = this._outputTail;
/* 1681 */       if (ch > 'ÿ') {
/* 1682 */         int hi = ch >> '\b' & 0xFF;
/* 1683 */         int lo = ch & 0xFF;
/* 1684 */         buf[10] = HEX_CHARS[(hi >> 4)];
/* 1685 */         buf[11] = HEX_CHARS[(hi & 0xF)];
/* 1686 */         buf[12] = HEX_CHARS[(lo >> 4)];
/* 1687 */         buf[13] = HEX_CHARS[(lo & 0xF)];
/* 1688 */         this._writer.write(buf, 8, 6);
/*      */       } else {
/* 1690 */         buf[6] = HEX_CHARS[(ch >> '\004')];
/* 1691 */         buf[7] = HEX_CHARS[(ch & 0xF)];
/* 1692 */         this._writer.write(buf, 2, 6);
/*      */       }
/*      */       return;
/*      */     }
/*      */     String escape;
/*      */     String escape;
/* 1698 */     if (this._currentEscape == null) {
/* 1699 */       escape = this._characterEscapes.getEscapeSequence(ch).getValue();
/*      */     } else {
/* 1701 */       escape = this._currentEscape.getValue();
/* 1702 */       this._currentEscape = null;
/*      */     }
/* 1704 */     int len = escape.length();
/* 1705 */     if (this._outputTail >= len) {
/* 1706 */       int ptr = this._outputTail - len;
/* 1707 */       this._outputHead = ptr;
/* 1708 */       escape.getChars(0, len, this._outputBuffer, ptr);
/* 1709 */       return;
/*      */     }
/*      */     
/* 1712 */     this._outputHead = this._outputTail;
/* 1713 */     this._writer.write(escape);
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
/*      */   private int _prependOrWriteCharacterEscape(char[] buffer, int ptr, int end, char ch, int escCode)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1727 */     if (escCode >= 0) {
/* 1728 */       if ((ptr > 1) && (ptr < end)) {
/* 1729 */         ptr -= 2;
/* 1730 */         buffer[ptr] = '\\';
/* 1731 */         buffer[(ptr + 1)] = ((char)escCode);
/*      */       } else {
/* 1733 */         char[] ent = this._entityBuffer;
/* 1734 */         if (ent == null) {
/* 1735 */           ent = _allocateEntityBuffer();
/*      */         }
/* 1737 */         ent[1] = ((char)escCode);
/* 1738 */         this._writer.write(ent, 0, 2);
/*      */       }
/* 1740 */       return ptr;
/*      */     }
/* 1742 */     if (escCode != -2) {
/* 1743 */       if ((ptr > 5) && (ptr < end)) {
/* 1744 */         ptr -= 6;
/* 1745 */         buffer[(ptr++)] = '\\';
/* 1746 */         buffer[(ptr++)] = 'u';
/*      */         
/* 1748 */         if (ch > 'ÿ') {
/* 1749 */           int hi = ch >> '\b' & 0xFF;
/* 1750 */           buffer[(ptr++)] = HEX_CHARS[(hi >> 4)];
/* 1751 */           buffer[(ptr++)] = HEX_CHARS[(hi & 0xF)];
/* 1752 */           ch = (char)(ch & 0xFF);
/*      */         } else {
/* 1754 */           buffer[(ptr++)] = '0';
/* 1755 */           buffer[(ptr++)] = '0';
/*      */         }
/* 1757 */         buffer[(ptr++)] = HEX_CHARS[(ch >> '\004')];
/* 1758 */         buffer[ptr] = HEX_CHARS[(ch & 0xF)];
/* 1759 */         ptr -= 5;
/*      */       }
/*      */       else {
/* 1762 */         char[] ent = this._entityBuffer;
/* 1763 */         if (ent == null) {
/* 1764 */           ent = _allocateEntityBuffer();
/*      */         }
/* 1766 */         this._outputHead = this._outputTail;
/* 1767 */         if (ch > 'ÿ') {
/* 1768 */           int hi = ch >> '\b' & 0xFF;
/* 1769 */           int lo = ch & 0xFF;
/* 1770 */           ent[10] = HEX_CHARS[(hi >> 4)];
/* 1771 */           ent[11] = HEX_CHARS[(hi & 0xF)];
/* 1772 */           ent[12] = HEX_CHARS[(lo >> 4)];
/* 1773 */           ent[13] = HEX_CHARS[(lo & 0xF)];
/* 1774 */           this._writer.write(ent, 8, 6);
/*      */         } else {
/* 1776 */           ent[6] = HEX_CHARS[(ch >> '\004')];
/* 1777 */           ent[7] = HEX_CHARS[(ch & 0xF)];
/* 1778 */           this._writer.write(ent, 2, 6);
/*      */         }
/*      */       }
/* 1781 */       return ptr; }
/*      */     String escape;
/*      */     String escape;
/* 1784 */     if (this._currentEscape == null) {
/* 1785 */       escape = this._characterEscapes.getEscapeSequence(ch).getValue();
/*      */     } else {
/* 1787 */       escape = this._currentEscape.getValue();
/* 1788 */       this._currentEscape = null;
/*      */     }
/* 1790 */     int len = escape.length();
/* 1791 */     if ((ptr >= len) && (ptr < end)) {
/* 1792 */       ptr -= len;
/* 1793 */       escape.getChars(0, len, buffer, ptr);
/*      */     } else {
/* 1795 */       this._writer.write(escape);
/*      */     }
/* 1797 */     return ptr;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _appendCharacterEscape(char ch, int escCode)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1807 */     if (escCode >= 0) {
/* 1808 */       if (this._outputTail + 2 > this._outputEnd) {
/* 1809 */         _flushBuffer();
/*      */       }
/* 1811 */       this._outputBuffer[(this._outputTail++)] = '\\';
/* 1812 */       this._outputBuffer[(this._outputTail++)] = ((char)escCode);
/* 1813 */       return;
/*      */     }
/* 1815 */     if (escCode != -2) {
/* 1816 */       if (this._outputTail + 5 >= this._outputEnd) {
/* 1817 */         _flushBuffer();
/*      */       }
/* 1819 */       int ptr = this._outputTail;
/* 1820 */       char[] buf = this._outputBuffer;
/* 1821 */       buf[(ptr++)] = '\\';
/* 1822 */       buf[(ptr++)] = 'u';
/*      */       
/* 1824 */       if (ch > 'ÿ') {
/* 1825 */         int hi = ch >> '\b' & 0xFF;
/* 1826 */         buf[(ptr++)] = HEX_CHARS[(hi >> 4)];
/* 1827 */         buf[(ptr++)] = HEX_CHARS[(hi & 0xF)];
/* 1828 */         ch = (char)(ch & 0xFF);
/*      */       } else {
/* 1830 */         buf[(ptr++)] = '0';
/* 1831 */         buf[(ptr++)] = '0';
/*      */       }
/* 1833 */       buf[(ptr++)] = HEX_CHARS[(ch >> '\004')];
/* 1834 */       buf[(ptr++)] = HEX_CHARS[(ch & 0xF)];
/* 1835 */       this._outputTail = ptr; return;
/*      */     }
/*      */     String escape;
/*      */     String escape;
/* 1839 */     if (this._currentEscape == null) {
/* 1840 */       escape = this._characterEscapes.getEscapeSequence(ch).getValue();
/*      */     } else {
/* 1842 */       escape = this._currentEscape.getValue();
/* 1843 */       this._currentEscape = null;
/*      */     }
/* 1845 */     int len = escape.length();
/* 1846 */     if (this._outputTail + len > this._outputEnd) {
/* 1847 */       _flushBuffer();
/* 1848 */       if (len > this._outputEnd) {
/* 1849 */         this._writer.write(escape);
/* 1850 */         return;
/*      */       }
/*      */     }
/* 1853 */     escape.getChars(0, len, this._outputBuffer, this._outputTail);
/* 1854 */     this._outputTail += len;
/*      */   }
/*      */   
/*      */   private char[] _allocateEntityBuffer()
/*      */   {
/* 1859 */     char[] buf = new char[14];
/*      */     
/* 1861 */     buf[0] = '\\';
/*      */     
/* 1863 */     buf[2] = '\\';
/* 1864 */     buf[3] = 'u';
/* 1865 */     buf[4] = '0';
/* 1866 */     buf[5] = '0';
/*      */     
/* 1868 */     buf[8] = '\\';
/* 1869 */     buf[9] = 'u';
/* 1870 */     this._entityBuffer = buf;
/* 1871 */     return buf;
/*      */   }
/*      */   
/*      */   protected void _flushBuffer() throws IOException
/*      */   {
/* 1876 */     int len = this._outputTail - this._outputHead;
/* 1877 */     if (len > 0) {
/* 1878 */       int offset = this._outputHead;
/* 1879 */       this._outputTail = (this._outputHead = 0);
/* 1880 */       this._writer.write(this._outputBuffer, offset, len);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\WriterBasedJsonGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */