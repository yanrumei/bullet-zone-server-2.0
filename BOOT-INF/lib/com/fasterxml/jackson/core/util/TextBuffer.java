/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ public final class TextBuffer
/*     */ {
/*  29 */   static final char[] NO_CHARS = new char[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int MIN_SEGMENT_LEN = 1000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int MAX_SEGMENT_LEN = 262144;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final BufferRecycler _allocator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _inputBuffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _inputStart;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _inputLen;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<char[]> _segments;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean _hasSegments;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _segmentSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _currentSegment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _currentSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String _resultString;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] _resultArray;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextBuffer(BufferRecycler allocator)
/*     */   {
/* 122 */     this._allocator = allocator;
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
/*     */   public void releaseBuffers()
/*     */   {
/* 136 */     if (this._allocator == null) {
/* 137 */       resetWithEmpty();
/*     */     }
/* 139 */     else if (this._currentSegment != null)
/*     */     {
/* 141 */       resetWithEmpty();
/*     */       
/* 143 */       char[] buf = this._currentSegment;
/* 144 */       this._currentSegment = null;
/* 145 */       this._allocator.releaseCharBuffer(2, buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetWithEmpty()
/*     */   {
/* 156 */     this._inputStart = -1;
/* 157 */     this._currentSize = 0;
/* 158 */     this._inputLen = 0;
/*     */     
/* 160 */     this._inputBuffer = null;
/* 161 */     this._resultString = null;
/* 162 */     this._resultArray = null;
/*     */     
/*     */ 
/* 165 */     if (this._hasSegments) {
/* 166 */       clearSegments();
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
/*     */   public void resetWithShared(char[] buf, int start, int len)
/*     */   {
/* 179 */     this._resultString = null;
/* 180 */     this._resultArray = null;
/*     */     
/*     */ 
/* 183 */     this._inputBuffer = buf;
/* 184 */     this._inputStart = start;
/* 185 */     this._inputLen = len;
/*     */     
/*     */ 
/* 188 */     if (this._hasSegments) {
/* 189 */       clearSegments();
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetWithCopy(char[] buf, int start, int len)
/*     */   {
/* 195 */     this._inputBuffer = null;
/* 196 */     this._inputStart = -1;
/* 197 */     this._inputLen = 0;
/*     */     
/* 199 */     this._resultString = null;
/* 200 */     this._resultArray = null;
/*     */     
/*     */ 
/* 203 */     if (this._hasSegments) {
/* 204 */       clearSegments();
/* 205 */     } else if (this._currentSegment == null) {
/* 206 */       this._currentSegment = buf(len);
/*     */     }
/* 208 */     this._currentSize = (this._segmentSize = 0);
/* 209 */     append(buf, start, len);
/*     */   }
/*     */   
/*     */   public void resetWithString(String value)
/*     */   {
/* 214 */     this._inputBuffer = null;
/* 215 */     this._inputStart = -1;
/* 216 */     this._inputLen = 0;
/*     */     
/* 218 */     this._resultString = value;
/* 219 */     this._resultArray = null;
/*     */     
/* 221 */     if (this._hasSegments) {
/* 222 */       clearSegments();
/*     */     }
/* 224 */     this._currentSize = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private char[] buf(int needed)
/*     */   {
/* 234 */     if (this._allocator != null) {
/* 235 */       return this._allocator.allocCharBuffer(2, needed);
/*     */     }
/* 237 */     return new char[Math.max(needed, 1000)];
/*     */   }
/*     */   
/*     */   private void clearSegments()
/*     */   {
/* 242 */     this._hasSegments = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */     this._segments.clear();
/* 251 */     this._currentSize = (this._segmentSize = 0);
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
/*     */   public int size()
/*     */   {
/* 264 */     if (this._inputStart >= 0) {
/* 265 */       return this._inputLen;
/*     */     }
/* 267 */     if (this._resultArray != null) {
/* 268 */       return this._resultArray.length;
/*     */     }
/* 270 */     if (this._resultString != null) {
/* 271 */       return this._resultString.length();
/*     */     }
/*     */     
/* 274 */     return this._segmentSize + this._currentSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTextOffset()
/*     */   {
/* 282 */     return this._inputStart >= 0 ? this._inputStart : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasTextAsCharacters()
/*     */   {
/* 292 */     if ((this._inputStart >= 0) || (this._resultArray != null)) { return true;
/*     */     }
/* 294 */     if (this._resultString != null) return false;
/* 295 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] getTextBuffer()
/*     */   {
/* 306 */     if (this._inputStart >= 0) return this._inputBuffer;
/* 307 */     if (this._resultArray != null) return this._resultArray;
/* 308 */     if (this._resultString != null) {
/* 309 */       return this._resultArray = this._resultString.toCharArray();
/*     */     }
/*     */     
/* 312 */     if (!this._hasSegments) {
/* 313 */       return this._currentSegment == null ? NO_CHARS : this._currentSegment;
/*     */     }
/*     */     
/* 316 */     return contentsAsArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String contentsAsString()
/*     */   {
/* 327 */     if (this._resultString == null)
/*     */     {
/* 329 */       if (this._resultArray != null) {
/* 330 */         this._resultString = new String(this._resultArray);
/*     */ 
/*     */       }
/* 333 */       else if (this._inputStart >= 0) {
/* 334 */         if (this._inputLen < 1) {
/* 335 */           return this._resultString = "";
/*     */         }
/* 337 */         this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
/*     */       }
/*     */       else {
/* 340 */         int segLen = this._segmentSize;
/* 341 */         int currLen = this._currentSize;
/*     */         
/* 343 */         if (segLen == 0) {
/* 344 */           this._resultString = (currLen == 0 ? "" : new String(this._currentSegment, 0, currLen));
/*     */         } else {
/* 346 */           StringBuilder sb = new StringBuilder(segLen + currLen);
/*     */           
/* 348 */           if (this._segments != null) {
/* 349 */             int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 350 */               char[] curr = (char[])this._segments.get(i);
/* 351 */               sb.append(curr, 0, curr.length);
/*     */             }
/*     */           }
/*     */           
/* 355 */           sb.append(this._currentSegment, 0, this._currentSize);
/* 356 */           this._resultString = sb.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 361 */     return this._resultString;
/*     */   }
/*     */   
/*     */   public char[] contentsAsArray() {
/* 365 */     char[] result = this._resultArray;
/* 366 */     if (result == null) {
/* 367 */       this._resultArray = (result = resultArray());
/*     */     }
/* 369 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigDecimal contentsAsDecimal()
/*     */     throws NumberFormatException
/*     */   {
/* 379 */     if (this._resultArray != null) {
/* 380 */       return NumberInput.parseBigDecimal(this._resultArray);
/*     */     }
/*     */     
/* 383 */     if ((this._inputStart >= 0) && (this._inputBuffer != null)) {
/* 384 */       return NumberInput.parseBigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
/*     */     }
/*     */     
/* 387 */     if ((this._segmentSize == 0) && (this._currentSegment != null)) {
/* 388 */       return NumberInput.parseBigDecimal(this._currentSegment, 0, this._currentSize);
/*     */     }
/*     */     
/* 391 */     return NumberInput.parseBigDecimal(contentsAsArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double contentsAsDouble()
/*     */     throws NumberFormatException
/*     */   {
/* 399 */     return NumberInput.parseDouble(contentsAsString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int contentsToWriter(Writer w)
/*     */     throws IOException
/*     */   {
/* 407 */     if (this._resultArray != null) {
/* 408 */       w.write(this._resultArray);
/* 409 */       return this._resultArray.length;
/*     */     }
/* 411 */     if (this._resultString != null) {
/* 412 */       w.write(this._resultString);
/* 413 */       return this._resultString.length();
/*     */     }
/*     */     
/* 416 */     if (this._inputStart >= 0) {
/* 417 */       int len = this._inputLen;
/* 418 */       if (len > 0) {
/* 419 */         w.write(this._inputBuffer, this._inputStart, len);
/*     */       }
/* 421 */       return len;
/*     */     }
/*     */     
/* 424 */     int total = 0;
/* 425 */     if (this._segments != null) {
/* 426 */       int i = 0; for (int end = this._segments.size(); i < end; i++) {
/* 427 */         char[] curr = (char[])this._segments.get(i);
/* 428 */         int currLen = curr.length;
/* 429 */         w.write(curr, 0, currLen);
/* 430 */         total += currLen;
/*     */       }
/*     */     }
/* 433 */     int len = this._currentSize;
/* 434 */     if (len > 0) {
/* 435 */       w.write(this._currentSegment, 0, len);
/* 436 */       total += len;
/*     */     }
/* 438 */     return total;
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
/*     */   public void ensureNotShared()
/*     */   {
/* 452 */     if (this._inputStart >= 0) {
/* 453 */       unshare(16);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(char c)
/*     */   {
/* 459 */     if (this._inputStart >= 0) {
/* 460 */       unshare(16);
/*     */     }
/* 462 */     this._resultString = null;
/* 463 */     this._resultArray = null;
/*     */     
/* 465 */     char[] curr = this._currentSegment;
/* 466 */     if (this._currentSize >= curr.length) {
/* 467 */       expand(1);
/* 468 */       curr = this._currentSegment;
/*     */     }
/* 470 */     curr[(this._currentSize++)] = c;
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(char[] c, int start, int len)
/*     */   {
/* 476 */     if (this._inputStart >= 0) {
/* 477 */       unshare(len);
/*     */     }
/* 479 */     this._resultString = null;
/* 480 */     this._resultArray = null;
/*     */     
/*     */ 
/* 483 */     char[] curr = this._currentSegment;
/* 484 */     int max = curr.length - this._currentSize;
/*     */     
/* 486 */     if (max >= len) {
/* 487 */       System.arraycopy(c, start, curr, this._currentSize, len);
/* 488 */       this._currentSize += len;
/* 489 */       return;
/*     */     }
/*     */     
/* 492 */     if (max > 0) {
/* 493 */       System.arraycopy(c, start, curr, this._currentSize, max);
/* 494 */       start += max;
/* 495 */       len -= max;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 502 */       expand(len);
/* 503 */       int amount = Math.min(this._currentSegment.length, len);
/* 504 */       System.arraycopy(c, start, this._currentSegment, 0, amount);
/* 505 */       this._currentSize += amount;
/* 506 */       start += amount;
/* 507 */       len -= amount;
/* 508 */     } while (len > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(String str, int offset, int len)
/*     */   {
/* 514 */     if (this._inputStart >= 0) {
/* 515 */       unshare(len);
/*     */     }
/* 517 */     this._resultString = null;
/* 518 */     this._resultArray = null;
/*     */     
/*     */ 
/* 521 */     char[] curr = this._currentSegment;
/* 522 */     int max = curr.length - this._currentSize;
/* 523 */     if (max >= len) {
/* 524 */       str.getChars(offset, offset + len, curr, this._currentSize);
/* 525 */       this._currentSize += len;
/* 526 */       return;
/*     */     }
/*     */     
/* 529 */     if (max > 0) {
/* 530 */       str.getChars(offset, offset + max, curr, this._currentSize);
/* 531 */       len -= max;
/* 532 */       offset += max;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     do
/*     */     {
/* 539 */       expand(len);
/* 540 */       int amount = Math.min(this._currentSegment.length, len);
/* 541 */       str.getChars(offset, offset + amount, this._currentSegment, 0);
/* 542 */       this._currentSize += amount;
/* 543 */       offset += amount;
/* 544 */       len -= amount;
/* 545 */     } while (len > 0);
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
/*     */   public char[] getCurrentSegment()
/*     */   {
/* 560 */     if (this._inputStart >= 0) {
/* 561 */       unshare(1);
/*     */     } else {
/* 563 */       char[] curr = this._currentSegment;
/* 564 */       if (curr == null) {
/* 565 */         this._currentSegment = buf(0);
/* 566 */       } else if (this._currentSize >= curr.length)
/*     */       {
/* 568 */         expand(1);
/*     */       }
/*     */     }
/* 571 */     return this._currentSegment;
/*     */   }
/*     */   
/*     */ 
/*     */   public char[] emptyAndGetCurrentSegment()
/*     */   {
/* 577 */     this._inputStart = -1;
/* 578 */     this._currentSize = 0;
/* 579 */     this._inputLen = 0;
/*     */     
/* 581 */     this._inputBuffer = null;
/* 582 */     this._resultString = null;
/* 583 */     this._resultArray = null;
/*     */     
/*     */ 
/* 586 */     if (this._hasSegments) {
/* 587 */       clearSegments();
/*     */     }
/* 589 */     char[] curr = this._currentSegment;
/* 590 */     if (curr == null) {
/* 591 */       this._currentSegment = (curr = buf(0));
/*     */     }
/* 593 */     return curr;
/*     */   }
/*     */   
/* 596 */   public int getCurrentSegmentSize() { return this._currentSize; }
/* 597 */   public void setCurrentLength(int len) { this._currentSize = len; }
/*     */   
/*     */ 
/*     */ 
/*     */   public String setCurrentAndReturn(int len)
/*     */   {
/* 603 */     this._currentSize = len;
/*     */     
/* 605 */     if (this._segmentSize > 0) {
/* 606 */       return contentsAsString();
/*     */     }
/*     */     
/* 609 */     int currLen = this._currentSize;
/* 610 */     String str = currLen == 0 ? "" : new String(this._currentSegment, 0, currLen);
/* 611 */     this._resultString = str;
/* 612 */     return str;
/*     */   }
/*     */   
/*     */   public char[] finishCurrentSegment() {
/* 616 */     if (this._segments == null) {
/* 617 */       this._segments = new ArrayList();
/*     */     }
/* 619 */     this._hasSegments = true;
/* 620 */     this._segments.add(this._currentSegment);
/* 621 */     int oldLen = this._currentSegment.length;
/* 622 */     this._segmentSize += oldLen;
/* 623 */     this._currentSize = 0;
/*     */     
/*     */ 
/* 626 */     int newLen = oldLen + (oldLen >> 1);
/* 627 */     if (newLen < 1000) {
/* 628 */       newLen = 1000;
/* 629 */     } else if (newLen > 262144) {
/* 630 */       newLen = 262144;
/*     */     }
/* 632 */     char[] curr = carr(newLen);
/* 633 */     this._currentSegment = curr;
/* 634 */     return curr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] expandCurrentSegment()
/*     */   {
/* 644 */     char[] curr = this._currentSegment;
/*     */     
/* 646 */     int len = curr.length;
/* 647 */     int newLen = len + (len >> 1);
/*     */     
/* 649 */     if (newLen > 262144) {
/* 650 */       newLen = len + (len >> 2);
/*     */     }
/* 652 */     return this._currentSegment = Arrays.copyOf(curr, newLen);
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
/*     */   public char[] expandCurrentSegment(int minSize)
/*     */   {
/* 665 */     char[] curr = this._currentSegment;
/* 666 */     if (curr.length >= minSize) return curr;
/* 667 */     this._currentSegment = (curr = Arrays.copyOf(curr, minSize));
/* 668 */     return curr;
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
/*     */   public String toString()
/*     */   {
/* 682 */     return contentsAsString();
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
/*     */   private void unshare(int needExtra)
/*     */   {
/* 696 */     int sharedLen = this._inputLen;
/* 697 */     this._inputLen = 0;
/* 698 */     char[] inputBuf = this._inputBuffer;
/* 699 */     this._inputBuffer = null;
/* 700 */     int start = this._inputStart;
/* 701 */     this._inputStart = -1;
/*     */     
/*     */ 
/* 704 */     int needed = sharedLen + needExtra;
/* 705 */     if ((this._currentSegment == null) || (needed > this._currentSegment.length)) {
/* 706 */       this._currentSegment = buf(needed);
/*     */     }
/* 708 */     if (sharedLen > 0) {
/* 709 */       System.arraycopy(inputBuf, start, this._currentSegment, 0, sharedLen);
/*     */     }
/* 711 */     this._segmentSize = 0;
/* 712 */     this._currentSize = sharedLen;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void expand(int minNewSegmentSize)
/*     */   {
/* 722 */     if (this._segments == null) {
/* 723 */       this._segments = new ArrayList();
/*     */     }
/* 725 */     char[] curr = this._currentSegment;
/* 726 */     this._hasSegments = true;
/* 727 */     this._segments.add(curr);
/* 728 */     this._segmentSize += curr.length;
/* 729 */     this._currentSize = 0;
/* 730 */     int oldLen = curr.length;
/*     */     
/*     */ 
/* 733 */     int newLen = oldLen + (oldLen >> 1);
/* 734 */     if (newLen < 1000) {
/* 735 */       newLen = 1000;
/* 736 */     } else if (newLen > 262144) {
/* 737 */       newLen = 262144;
/*     */     }
/* 739 */     this._currentSegment = carr(newLen);
/*     */   }
/*     */   
/*     */   private char[] resultArray()
/*     */   {
/* 744 */     if (this._resultString != null) {
/* 745 */       return this._resultString.toCharArray();
/*     */     }
/*     */     
/* 748 */     if (this._inputStart >= 0) {
/* 749 */       int len = this._inputLen;
/* 750 */       if (len < 1) {
/* 751 */         return NO_CHARS;
/*     */       }
/* 753 */       int start = this._inputStart;
/* 754 */       if (start == 0) {
/* 755 */         return Arrays.copyOf(this._inputBuffer, len);
/*     */       }
/* 757 */       return Arrays.copyOfRange(this._inputBuffer, start, start + len);
/*     */     }
/*     */     
/* 760 */     int size = size();
/* 761 */     if (size < 1) {
/* 762 */       return NO_CHARS;
/*     */     }
/* 764 */     int offset = 0;
/* 765 */     char[] result = carr(size);
/* 766 */     if (this._segments != null) {
/* 767 */       int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 768 */         char[] curr = (char[])this._segments.get(i);
/* 769 */         int currLen = curr.length;
/* 770 */         System.arraycopy(curr, 0, result, offset, currLen);
/* 771 */         offset += currLen;
/*     */       }
/*     */     }
/* 774 */     System.arraycopy(this._currentSegment, 0, result, offset, this._currentSize);
/* 775 */     return result;
/*     */   }
/*     */   
/* 778 */   private char[] carr(int len) { return new char[len]; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\TextBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */