/*     */ package com.fasterxml.jackson.core.filter;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class FilteringGeneratorDelegate
/*     */   extends JsonGeneratorDelegate
/*     */ {
/*     */   protected TokenFilter rootFilter;
/*     */   protected boolean _allowMultipleMatches;
/*     */   protected boolean _includePath;
/*     */   @Deprecated
/*     */   protected boolean _includeImmediateParent;
/*     */   protected TokenFilterContext _filterContext;
/*     */   protected TokenFilter _itemFilter;
/*     */   protected int _matchCount;
/*     */   
/*     */   public FilteringGeneratorDelegate(JsonGenerator d, TokenFilter f, boolean includePath, boolean allowMultipleMatches)
/*     */   {
/*  96 */     super(d, false);
/*  97 */     this.rootFilter = f;
/*     */     
/*  99 */     this._itemFilter = f;
/* 100 */     this._filterContext = TokenFilterContext.createRootContext(f);
/* 101 */     this._includePath = includePath;
/* 102 */     this._allowMultipleMatches = allowMultipleMatches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenFilter getFilter()
/*     */   {
/* 111 */     return this.rootFilter;
/*     */   }
/*     */   
/* 114 */   public JsonStreamContext getFilterContext() { return this._filterContext; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMatchCount()
/*     */   {
/* 122 */     return this._matchCount;
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
/*     */   public JsonStreamContext getOutputContext()
/*     */   {
/* 137 */     return this._filterContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeStartArray()
/*     */     throws IOException
/*     */   {
/* 150 */     if (this._itemFilter == null) {
/* 151 */       this._filterContext = this._filterContext.createChildArrayContext(null, false);
/* 152 */       return;
/*     */     }
/* 154 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 155 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
/* 156 */       this.delegate.writeStartArray();
/* 157 */       return;
/*     */     }
/*     */     
/* 160 */     this._itemFilter = this._filterContext.checkValue(this._itemFilter);
/* 161 */     if (this._itemFilter == null) {
/* 162 */       this._filterContext = this._filterContext.createChildArrayContext(null, false);
/* 163 */       return;
/*     */     }
/* 165 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 166 */       this._itemFilter = this._itemFilter.filterStartArray();
/*     */     }
/* 168 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 169 */       _checkParentPath();
/* 170 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
/* 171 */       this.delegate.writeStartArray();
/*     */     } else {
/* 173 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeStartArray(int size)
/*     */     throws IOException
/*     */   {
/* 180 */     if (this._itemFilter == null) {
/* 181 */       this._filterContext = this._filterContext.createChildArrayContext(null, false);
/* 182 */       return;
/*     */     }
/* 184 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 185 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
/* 186 */       this.delegate.writeStartArray(size);
/* 187 */       return;
/*     */     }
/* 189 */     this._itemFilter = this._filterContext.checkValue(this._itemFilter);
/* 190 */     if (this._itemFilter == null) {
/* 191 */       this._filterContext = this._filterContext.createChildArrayContext(null, false);
/* 192 */       return;
/*     */     }
/* 194 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 195 */       this._itemFilter = this._itemFilter.filterStartArray();
/*     */     }
/* 197 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 198 */       _checkParentPath();
/* 199 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
/* 200 */       this.delegate.writeStartArray(size);
/*     */     } else {
/* 202 */       this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeEndArray()
/*     */     throws IOException
/*     */   {
/* 209 */     this._filterContext = this._filterContext.closeArray(this.delegate);
/*     */     
/* 211 */     if (this._filterContext != null) {
/* 212 */       this._itemFilter = this._filterContext.getFilter();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeStartObject()
/*     */     throws IOException
/*     */   {
/* 219 */     if (this._itemFilter == null) {
/* 220 */       this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, false);
/* 221 */       return;
/*     */     }
/* 223 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 224 */       this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, true);
/* 225 */       this.delegate.writeStartObject();
/* 226 */       return;
/*     */     }
/*     */     
/* 229 */     TokenFilter f = this._filterContext.checkValue(this._itemFilter);
/* 230 */     if (f == null) {
/* 231 */       return;
/*     */     }
/*     */     
/* 234 */     if (f != TokenFilter.INCLUDE_ALL) {
/* 235 */       f = f.filterStartObject();
/*     */     }
/* 237 */     if (f == TokenFilter.INCLUDE_ALL) {
/* 238 */       _checkParentPath();
/* 239 */       this._filterContext = this._filterContext.createChildObjectContext(f, true);
/* 240 */       this.delegate.writeStartObject();
/*     */     } else {
/* 242 */       this._filterContext = this._filterContext.createChildObjectContext(f, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeStartObject(Object forValue)
/*     */     throws IOException
/*     */   {
/* 249 */     if (this._itemFilter == null) {
/* 250 */       this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, false);
/* 251 */       return;
/*     */     }
/* 253 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 254 */       this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, true);
/* 255 */       this.delegate.writeStartObject(forValue);
/* 256 */       return;
/*     */     }
/*     */     
/* 259 */     TokenFilter f = this._filterContext.checkValue(this._itemFilter);
/* 260 */     if (f == null) {
/* 261 */       return;
/*     */     }
/*     */     
/* 264 */     if (f != TokenFilter.INCLUDE_ALL) {
/* 265 */       f = f.filterStartObject();
/*     */     }
/* 267 */     if (f == TokenFilter.INCLUDE_ALL) {
/* 268 */       _checkParentPath();
/* 269 */       this._filterContext = this._filterContext.createChildObjectContext(f, true);
/* 270 */       this.delegate.writeStartObject(forValue);
/*     */     } else {
/* 272 */       this._filterContext = this._filterContext.createChildObjectContext(f, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeEndObject()
/*     */     throws IOException
/*     */   {
/* 279 */     this._filterContext = this._filterContext.closeObject(this.delegate);
/* 280 */     if (this._filterContext != null) {
/* 281 */       this._itemFilter = this._filterContext.getFilter();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeFieldName(String name)
/*     */     throws IOException
/*     */   {
/* 288 */     TokenFilter state = this._filterContext.setFieldName(name);
/* 289 */     if (state == null) {
/* 290 */       this._itemFilter = null;
/* 291 */       return;
/*     */     }
/* 293 */     if (state == TokenFilter.INCLUDE_ALL) {
/* 294 */       this._itemFilter = state;
/* 295 */       this.delegate.writeFieldName(name);
/* 296 */       return;
/*     */     }
/* 298 */     state = state.includeProperty(name);
/* 299 */     this._itemFilter = state;
/* 300 */     if (state == TokenFilter.INCLUDE_ALL) {
/* 301 */       _checkPropertyParentPath();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeFieldName(SerializableString name)
/*     */     throws IOException
/*     */   {
/* 308 */     TokenFilter state = this._filterContext.setFieldName(name.getValue());
/* 309 */     if (state == null) {
/* 310 */       this._itemFilter = null;
/* 311 */       return;
/*     */     }
/* 313 */     if (state == TokenFilter.INCLUDE_ALL) {
/* 314 */       this._itemFilter = state;
/* 315 */       this.delegate.writeFieldName(name);
/* 316 */       return;
/*     */     }
/* 318 */     state = state.includeProperty(name.getValue());
/* 319 */     this._itemFilter = state;
/* 320 */     if (state == TokenFilter.INCLUDE_ALL) {
/* 321 */       _checkPropertyParentPath();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeString(String value)
/*     */     throws IOException
/*     */   {
/* 334 */     if (this._itemFilter == null) {
/* 335 */       return;
/*     */     }
/* 337 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 338 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 339 */       if (state == null) {
/* 340 */         return;
/*     */       }
/* 342 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 343 */         (!state.includeString(value))) {
/* 344 */         return;
/*     */       }
/*     */       
/* 347 */       _checkParentPath();
/*     */     }
/* 349 */     this.delegate.writeString(value);
/*     */   }
/*     */   
/*     */   public void writeString(char[] text, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 355 */     if (this._itemFilter == null) {
/* 356 */       return;
/*     */     }
/* 358 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 359 */       String value = new String(text, offset, len);
/* 360 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 361 */       if (state == null) {
/* 362 */         return;
/*     */       }
/* 364 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 365 */         (!state.includeString(value))) {
/* 366 */         return;
/*     */       }
/*     */       
/* 369 */       _checkParentPath();
/*     */     }
/* 371 */     this.delegate.writeString(text, offset, len);
/*     */   }
/*     */   
/*     */   public void writeString(SerializableString value)
/*     */     throws IOException
/*     */   {
/* 377 */     if (this._itemFilter == null) {
/* 378 */       return;
/*     */     }
/* 380 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 381 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 382 */       if (state == null) {
/* 383 */         return;
/*     */       }
/* 385 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 386 */         (!state.includeString(value.getValue()))) {
/* 387 */         return;
/*     */       }
/*     */       
/* 390 */       _checkParentPath();
/*     */     }
/* 392 */     this.delegate.writeString(value);
/*     */   }
/*     */   
/*     */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 398 */     if (_checkRawValueWrite()) {
/* 399 */       this.delegate.writeRawUTF8String(text, offset, length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeUTF8String(byte[] text, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 407 */     if (_checkRawValueWrite()) {
/* 408 */       this.delegate.writeUTF8String(text, offset, length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeRaw(String text)
/*     */     throws IOException
/*     */   {
/* 421 */     if (_checkRawValueWrite()) {
/* 422 */       this.delegate.writeRaw(text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRaw(String text, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 429 */     if (_checkRawValueWrite()) {
/* 430 */       this.delegate.writeRaw(text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRaw(SerializableString text)
/*     */     throws IOException
/*     */   {
/* 437 */     if (_checkRawValueWrite()) {
/* 438 */       this.delegate.writeRaw(text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRaw(char[] text, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 445 */     if (_checkRawValueWrite()) {
/* 446 */       this.delegate.writeRaw(text, offset, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRaw(char c)
/*     */     throws IOException
/*     */   {
/* 453 */     if (_checkRawValueWrite()) {
/* 454 */       this.delegate.writeRaw(c);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text)
/*     */     throws IOException
/*     */   {
/* 461 */     if (_checkRawValueWrite()) {
/* 462 */       this.delegate.writeRaw(text);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 469 */     if (_checkRawValueWrite()) {
/* 470 */       this.delegate.writeRaw(text, offset, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeRawValue(char[] text, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 477 */     if (_checkRawValueWrite()) {
/* 478 */       this.delegate.writeRaw(text, offset, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*     */     throws IOException
/*     */   {
/* 485 */     if (_checkBinaryWrite()) {
/* 486 */       this.delegate.writeBinary(b64variant, data, offset, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*     */     throws IOException
/*     */   {
/* 493 */     if (_checkBinaryWrite()) {
/* 494 */       return this.delegate.writeBinary(b64variant, data, dataLength);
/*     */     }
/* 496 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeNumber(short v)
/*     */     throws IOException
/*     */   {
/* 508 */     if (this._itemFilter == null) {
/* 509 */       return;
/*     */     }
/* 511 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 512 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 513 */       if (state == null) {
/* 514 */         return;
/*     */       }
/* 516 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 517 */         (!state.includeNumber(v))) {
/* 518 */         return;
/*     */       }
/*     */       
/* 521 */       _checkParentPath();
/*     */     }
/* 523 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(int v)
/*     */     throws IOException
/*     */   {
/* 529 */     if (this._itemFilter == null) {
/* 530 */       return;
/*     */     }
/* 532 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 533 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 534 */       if (state == null) {
/* 535 */         return;
/*     */       }
/* 537 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 538 */         (!state.includeNumber(v))) {
/* 539 */         return;
/*     */       }
/*     */       
/* 542 */       _checkParentPath();
/*     */     }
/* 544 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(long v)
/*     */     throws IOException
/*     */   {
/* 550 */     if (this._itemFilter == null) {
/* 551 */       return;
/*     */     }
/* 553 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 554 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 555 */       if (state == null) {
/* 556 */         return;
/*     */       }
/* 558 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 559 */         (!state.includeNumber(v))) {
/* 560 */         return;
/*     */       }
/*     */       
/* 563 */       _checkParentPath();
/*     */     }
/* 565 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(BigInteger v)
/*     */     throws IOException
/*     */   {
/* 571 */     if (this._itemFilter == null) {
/* 572 */       return;
/*     */     }
/* 574 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 575 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 576 */       if (state == null) {
/* 577 */         return;
/*     */       }
/* 579 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 580 */         (!state.includeNumber(v))) {
/* 581 */         return;
/*     */       }
/*     */       
/* 584 */       _checkParentPath();
/*     */     }
/* 586 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(double v)
/*     */     throws IOException
/*     */   {
/* 592 */     if (this._itemFilter == null) {
/* 593 */       return;
/*     */     }
/* 595 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 596 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 597 */       if (state == null) {
/* 598 */         return;
/*     */       }
/* 600 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 601 */         (!state.includeNumber(v))) {
/* 602 */         return;
/*     */       }
/*     */       
/* 605 */       _checkParentPath();
/*     */     }
/* 607 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(float v)
/*     */     throws IOException
/*     */   {
/* 613 */     if (this._itemFilter == null) {
/* 614 */       return;
/*     */     }
/* 616 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 617 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 618 */       if (state == null) {
/* 619 */         return;
/*     */       }
/* 621 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 622 */         (!state.includeNumber(v))) {
/* 623 */         return;
/*     */       }
/*     */       
/* 626 */       _checkParentPath();
/*     */     }
/* 628 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(BigDecimal v)
/*     */     throws IOException
/*     */   {
/* 634 */     if (this._itemFilter == null) {
/* 635 */       return;
/*     */     }
/* 637 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 638 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 639 */       if (state == null) {
/* 640 */         return;
/*     */       }
/* 642 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 643 */         (!state.includeNumber(v))) {
/* 644 */         return;
/*     */       }
/*     */       
/* 647 */       _checkParentPath();
/*     */     }
/* 649 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/*     */   public void writeNumber(String encodedValue)
/*     */     throws IOException, UnsupportedOperationException
/*     */   {
/* 655 */     if (this._itemFilter == null) {
/* 656 */       return;
/*     */     }
/* 658 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 659 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 660 */       if (state == null) {
/* 661 */         return;
/*     */       }
/* 663 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 664 */         (!state.includeRawValue())) {
/* 665 */         return;
/*     */       }
/*     */       
/* 668 */       _checkParentPath();
/*     */     }
/* 670 */     this.delegate.writeNumber(encodedValue);
/*     */   }
/*     */   
/*     */   public void writeBoolean(boolean v)
/*     */     throws IOException
/*     */   {
/* 676 */     if (this._itemFilter == null) {
/* 677 */       return;
/*     */     }
/* 679 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 680 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 681 */       if (state == null) {
/* 682 */         return;
/*     */       }
/* 684 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 685 */         (!state.includeBoolean(v))) {
/* 686 */         return;
/*     */       }
/*     */       
/* 689 */       _checkParentPath();
/*     */     }
/* 691 */     this.delegate.writeBoolean(v);
/*     */   }
/*     */   
/*     */   public void writeNull()
/*     */     throws IOException
/*     */   {
/* 697 */     if (this._itemFilter == null) {
/* 698 */       return;
/*     */     }
/* 700 */     if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
/* 701 */       TokenFilter state = this._filterContext.checkValue(this._itemFilter);
/* 702 */       if (state == null) {
/* 703 */         return;
/*     */       }
/* 705 */       if ((state != TokenFilter.INCLUDE_ALL) && 
/* 706 */         (!state.includeNull())) {
/* 707 */         return;
/*     */       }
/*     */       
/* 710 */       _checkParentPath();
/*     */     }
/* 712 */     this.delegate.writeNull();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeOmittedField(String fieldName)
/*     */     throws IOException
/*     */   {
/* 724 */     if (this._itemFilter != null) {
/* 725 */       this.delegate.writeOmittedField(fieldName);
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
/*     */   public void writeObjectId(Object id)
/*     */     throws IOException
/*     */   {
/* 740 */     if (this._itemFilter != null) {
/* 741 */       this.delegate.writeObjectId(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeObjectRef(Object id) throws IOException
/*     */   {
/* 747 */     if (this._itemFilter != null) {
/* 748 */       this.delegate.writeObjectRef(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypeId(Object id) throws IOException
/*     */   {
/* 754 */     if (this._itemFilter != null) {
/* 755 */       this.delegate.writeTypeId(id);
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
/*     */   protected void _checkParentPath()
/*     */     throws IOException
/*     */   {
/* 834 */     this._matchCount += 1;
/*     */     
/* 836 */     if (this._includePath) {
/* 837 */       this._filterContext.writePath(this.delegate);
/*     */     }
/*     */     
/* 840 */     if (!this._allowMultipleMatches)
/*     */     {
/* 842 */       this._filterContext.skipParentChecks();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _checkPropertyParentPath()
/*     */     throws IOException
/*     */   {
/* 853 */     this._matchCount += 1;
/* 854 */     if (this._includePath) {
/* 855 */       this._filterContext.writePath(this.delegate);
/* 856 */     } else if (this._includeImmediateParent)
/*     */     {
/*     */ 
/* 859 */       this._filterContext.writeImmediatePath(this.delegate);
/*     */     }
/*     */     
/*     */ 
/* 863 */     if (!this._allowMultipleMatches)
/*     */     {
/* 865 */       this._filterContext.skipParentChecks();
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean _checkBinaryWrite() throws IOException
/*     */   {
/* 871 */     if (this._itemFilter == null) {
/* 872 */       return false;
/*     */     }
/* 874 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 875 */       return true;
/*     */     }
/* 877 */     if (this._itemFilter.includeBinary()) {
/* 878 */       _checkParentPath();
/* 879 */       return true;
/*     */     }
/* 881 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean _checkRawValueWrite() throws IOException
/*     */   {
/* 886 */     if (this._itemFilter == null) {
/* 887 */       return false;
/*     */     }
/* 889 */     if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
/* 890 */       return true;
/*     */     }
/* 892 */     if (this._itemFilter.includeRawValue()) {
/* 893 */       _checkParentPath();
/* 894 */       return true;
/*     */     }
/* 896 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\filter\FilteringGeneratorDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */