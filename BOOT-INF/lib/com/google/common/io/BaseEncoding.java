/*      */ package com.google.common.io;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.CharMatcher;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Arrays;
/*      */ import javax.annotation.Nullable;
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
/*      */ @GwtCompatible(emulated=true)
/*      */ public abstract class BaseEncoding
/*      */ {
/*      */   public static final class DecodingException
/*      */     extends IOException
/*      */   {
/*      */     DecodingException(String message)
/*      */     {
/*  132 */       super();
/*      */     }
/*      */     
/*      */     DecodingException(Throwable cause) {
/*  136 */       super();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String encode(byte[] bytes)
/*      */   {
/*  144 */     return encode(bytes, 0, bytes.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String encode(byte[] bytes, int off, int len)
/*      */   {
/*  152 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  153 */     StringBuilder result = new StringBuilder(maxEncodedSize(len));
/*      */     try {
/*  155 */       encodeTo(result, bytes, off, len);
/*      */     } catch (IOException impossible) {
/*  157 */       throw new AssertionError(impossible);
/*      */     }
/*  159 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public abstract OutputStream encodingStream(Writer paramWriter);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public final ByteSink encodingSink(final CharSink encodedSink)
/*      */   {
/*  175 */     Preconditions.checkNotNull(encodedSink);
/*  176 */     new ByteSink()
/*      */     {
/*      */       public OutputStream openStream() throws IOException {
/*  179 */         return BaseEncoding.this.encodingStream(encodedSink.openStream());
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */   private static byte[] extract(byte[] result, int length)
/*      */   {
/*  187 */     if (length == result.length) {
/*  188 */       return result;
/*      */     }
/*  190 */     byte[] trunc = new byte[length];
/*  191 */     System.arraycopy(result, 0, trunc, 0, length);
/*  192 */     return trunc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean canDecode(CharSequence paramCharSequence);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final byte[] decode(CharSequence chars)
/*      */   {
/*      */     try
/*      */     {
/*  213 */       return decodeChecked(chars);
/*      */     } catch (DecodingException badInput) {
/*  215 */       throw new IllegalArgumentException(badInput);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final byte[] decodeChecked(CharSequence chars)
/*      */     throws BaseEncoding.DecodingException
/*      */   {
/*  227 */     chars = padding().trimTrailingFrom(chars);
/*  228 */     byte[] tmp = new byte[maxDecodedSize(chars.length())];
/*  229 */     int len = decodeTo(tmp, chars);
/*  230 */     return extract(tmp, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public abstract InputStream decodingStream(Reader paramReader);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public final ByteSource decodingSource(final CharSource encodedSource)
/*      */   {
/*  247 */     Preconditions.checkNotNull(encodedSource);
/*  248 */     new ByteSource()
/*      */     {
/*      */       public InputStream openStream() throws IOException {
/*  251 */         return BaseEncoding.this.decodingStream(encodedSource.openStream());
/*      */       }
/*      */     };
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
/*      */   abstract int maxEncodedSize(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract void encodeTo(Appendable paramAppendable, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract int maxDecodedSize(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract int decodeTo(byte[] paramArrayOfByte, CharSequence paramCharSequence)
/*      */     throws BaseEncoding.DecodingException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   abstract CharMatcher padding();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  315 */   private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 
/*      */   
/*  317 */     Character.valueOf('='));
/*      */   
/*      */ 
/*      */   public abstract BaseEncoding omitPadding();
/*      */   
/*      */ 
/*      */   public abstract BaseEncoding withPadChar(char paramChar);
/*      */   
/*      */   public abstract BaseEncoding withSeparator(String paramString, int paramInt);
/*      */   
/*      */   public abstract BaseEncoding upperCase();
/*      */   
/*      */   public abstract BaseEncoding lowerCase();
/*      */   
/*      */   public static BaseEncoding base64()
/*      */   {
/*  333 */     return BASE64; }
/*      */   
/*      */ 
/*  336 */   private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 
/*      */   
/*  338 */     Character.valueOf('='));
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
/*      */   public static BaseEncoding base64Url()
/*      */   {
/*  355 */     return BASE64_URL;
/*      */   }
/*      */   
/*  358 */   private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 
/*  359 */     Character.valueOf('='));
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
/*      */   public static BaseEncoding base32()
/*      */   {
/*  374 */     return BASE32;
/*      */   }
/*      */   
/*  377 */   private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", 
/*  378 */     Character.valueOf('='));
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
/*      */   public static BaseEncoding base32Hex()
/*      */   {
/*  393 */     return BASE32_HEX;
/*      */   }
/*      */   
/*  396 */   private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
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
/*      */   public static BaseEncoding base16()
/*      */   {
/*  412 */     return BASE16;
/*      */   }
/*      */   
/*      */   private static final class Alphabet extends CharMatcher
/*      */   {
/*      */     private final String name;
/*      */     private final char[] chars;
/*      */     final int mask;
/*      */     final int bitsPerChar;
/*      */     final int charsPerChunk;
/*      */     final int bytesPerChunk;
/*      */     private final byte[] decodabet;
/*      */     private final boolean[] validPadding;
/*      */     
/*      */     Alphabet(String name, char[] chars) {
/*  427 */       this.name = ((String)Preconditions.checkNotNull(name));
/*  428 */       this.chars = ((char[])Preconditions.checkNotNull(chars));
/*      */       try {
/*  430 */         this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
/*      */       } catch (ArithmeticException e) {
/*  432 */         throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  439 */       int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
/*      */       try {
/*  441 */         this.charsPerChunk = (8 / gcd);
/*  442 */         this.bytesPerChunk = (this.bitsPerChar / gcd);
/*      */       } catch (ArithmeticException e) {
/*  444 */         throw new IllegalArgumentException("Illegal alphabet " + new String(chars), e);
/*      */       }
/*      */       
/*  447 */       this.mask = (chars.length - 1);
/*      */       
/*  449 */       byte[] decodabet = new byte[''];
/*  450 */       Arrays.fill(decodabet, (byte)-1);
/*  451 */       for (int i = 0; i < chars.length; i++) {
/*  452 */         char c = chars[i];
/*  453 */         Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
/*  454 */         Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", c);
/*  455 */         decodabet[c] = ((byte)i);
/*      */       }
/*  457 */       this.decodabet = decodabet;
/*      */       
/*  459 */       boolean[] validPadding = new boolean[this.charsPerChunk];
/*  460 */       for (int i = 0; i < this.bytesPerChunk; i++) {
/*  461 */         validPadding[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
/*      */       }
/*  463 */       this.validPadding = validPadding;
/*      */     }
/*      */     
/*      */     char encode(int bits) {
/*  467 */       return this.chars[bits];
/*      */     }
/*      */     
/*      */     boolean isValidPaddingStartPosition(int index) {
/*  471 */       return this.validPadding[(index % this.charsPerChunk)];
/*      */     }
/*      */     
/*      */     boolean canDecode(char ch) {
/*  475 */       return (ch <= '') && (this.decodabet[ch] != -1);
/*      */     }
/*      */     
/*      */     int decode(char ch) throws BaseEncoding.DecodingException {
/*  479 */       if ((ch > '') || (this.decodabet[ch] == -1))
/*      */       {
/*      */ 
/*  482 */         throw new BaseEncoding.DecodingException("Unrecognized character: " + (CharMatcher.invisible().matches(ch) ? "0x" + Integer.toHexString(ch) : Character.valueOf(ch)));
/*      */       }
/*  484 */       return this.decodabet[ch];
/*      */     }
/*      */     
/*      */     private boolean hasLowerCase() {
/*  488 */       for (char c : this.chars) {
/*  489 */         if (Ascii.isLowerCase(c)) {
/*  490 */           return true;
/*      */         }
/*      */       }
/*  493 */       return false;
/*      */     }
/*      */     
/*      */     private boolean hasUpperCase() {
/*  497 */       for (char c : this.chars) {
/*  498 */         if (Ascii.isUpperCase(c)) {
/*  499 */           return true;
/*      */         }
/*      */       }
/*  502 */       return false;
/*      */     }
/*      */     
/*      */     Alphabet upperCase() {
/*  506 */       if (!hasLowerCase()) {
/*  507 */         return this;
/*      */       }
/*  509 */       Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
/*  510 */       char[] upperCased = new char[this.chars.length];
/*  511 */       for (int i = 0; i < this.chars.length; i++) {
/*  512 */         upperCased[i] = Ascii.toUpperCase(this.chars[i]);
/*      */       }
/*  514 */       return new Alphabet(this.name + ".upperCase()", upperCased);
/*      */     }
/*      */     
/*      */     Alphabet lowerCase()
/*      */     {
/*  519 */       if (!hasUpperCase()) {
/*  520 */         return this;
/*      */       }
/*  522 */       Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
/*  523 */       char[] lowerCased = new char[this.chars.length];
/*  524 */       for (int i = 0; i < this.chars.length; i++) {
/*  525 */         lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
/*      */       }
/*  527 */       return new Alphabet(this.name + ".lowerCase()", lowerCased);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean matches(char c)
/*      */     {
/*  533 */       return (CharMatcher.ascii().matches(c)) && (this.decodabet[c] != -1);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  538 */       return this.name;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object other)
/*      */     {
/*  543 */       if ((other instanceof Alphabet)) {
/*  544 */         Alphabet that = (Alphabet)other;
/*  545 */         return Arrays.equals(this.chars, that.chars);
/*      */       }
/*  547 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  552 */       return Arrays.hashCode(this.chars);
/*      */     }
/*      */   }
/*      */   
/*      */   static class StandardBaseEncoding extends BaseEncoding {
/*      */     final BaseEncoding.Alphabet alphabet;
/*      */     @Nullable
/*      */     final Character paddingChar;
/*      */     private transient BaseEncoding upperCase;
/*      */     private transient BaseEncoding lowerCase;
/*      */     
/*  563 */     StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) { this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar); }
/*      */     
/*      */     StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
/*      */     {
/*  567 */       this.alphabet = ((BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet));
/*  568 */       Preconditions.checkArgument((paddingChar == null) || 
/*  569 */         (!alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", paddingChar);
/*      */       
/*      */ 
/*  572 */       this.paddingChar = paddingChar;
/*      */     }
/*      */     
/*      */     CharMatcher padding()
/*      */     {
/*  577 */       return this.paddingChar == null ? CharMatcher.none() : CharMatcher.is(this.paddingChar.charValue());
/*      */     }
/*      */     
/*      */     int maxEncodedSize(int bytes)
/*      */     {
/*  582 */       return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(final Writer out)
/*      */     {
/*  588 */       Preconditions.checkNotNull(out);
/*  589 */       new OutputStream() {
/*  590 */         int bitBuffer = 0;
/*  591 */         int bitBufferLength = 0;
/*  592 */         int writtenChars = 0;
/*      */         
/*      */         public void write(int b) throws IOException
/*      */         {
/*  596 */           this.bitBuffer <<= 8;
/*  597 */           this.bitBuffer |= b & 0xFF;
/*  598 */           this.bitBufferLength += 8;
/*  599 */           while (this.bitBufferLength >= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar) {
/*  600 */             int charIndex = this.bitBuffer >> this.bitBufferLength - BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  601 */             out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  602 */             this.writtenChars += 1;
/*  603 */             this.bitBufferLength -= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */           }
/*      */         }
/*      */         
/*      */         public void flush() throws IOException
/*      */         {
/*  609 */           out.flush();
/*      */         }
/*      */         
/*      */         public void close() throws IOException
/*      */         {
/*  614 */           if (this.bitBufferLength > 0) {
/*  615 */             int charIndex = this.bitBuffer << BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  616 */             out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  617 */             this.writtenChars += 1;
/*  618 */             if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null) {
/*  619 */               while (this.writtenChars % BaseEncoding.StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
/*  620 */                 out.write(BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue());
/*  621 */                 this.writtenChars += 1;
/*      */               }
/*      */             }
/*      */           }
/*  625 */           out.close();
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
/*      */     {
/*  632 */       Preconditions.checkNotNull(target);
/*  633 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  634 */       for (int i = 0; i < len; i += this.alphabet.bytesPerChunk) {
/*  635 */         encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
/*      */       }
/*      */     }
/*      */     
/*      */     void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  640 */       Preconditions.checkNotNull(target);
/*  641 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  642 */       Preconditions.checkArgument(len <= this.alphabet.bytesPerChunk);
/*  643 */       long bitBuffer = 0L;
/*  644 */       for (int i = 0; i < len; i++) {
/*  645 */         bitBuffer |= bytes[(off + i)] & 0xFF;
/*  646 */         bitBuffer <<= 8;
/*      */       }
/*      */       
/*  649 */       int bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;
/*  650 */       int bitsProcessed = 0;
/*  651 */       while (bitsProcessed < len * 8) {
/*  652 */         int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
/*  653 */         target.append(this.alphabet.encode(charIndex));
/*  654 */         bitsProcessed += this.alphabet.bitsPerChar;
/*      */       }
/*  656 */       if (this.paddingChar != null) {
/*  657 */         while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
/*  658 */           target.append(this.paddingChar.charValue());
/*  659 */           bitsProcessed += this.alphabet.bitsPerChar;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     int maxDecodedSize(int chars)
/*      */     {
/*  666 */       return (int)((this.alphabet.bitsPerChar * chars + 7L) / 8L);
/*      */     }
/*      */     
/*      */     public boolean canDecode(CharSequence chars)
/*      */     {
/*  671 */       chars = padding().trimTrailingFrom(chars);
/*  672 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  673 */         return false;
/*      */       }
/*  675 */       for (int i = 0; i < chars.length(); i++) {
/*  676 */         if (!this.alphabet.canDecode(chars.charAt(i))) {
/*  677 */           return false;
/*      */         }
/*      */       }
/*  680 */       return true;
/*      */     }
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
/*      */     {
/*  685 */       Preconditions.checkNotNull(target);
/*  686 */       chars = padding().trimTrailingFrom(chars);
/*  687 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  688 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  690 */       int bytesWritten = 0;
/*  691 */       for (int charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
/*  692 */         long chunk = 0L;
/*  693 */         int charsProcessed = 0;
/*  694 */         for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
/*  695 */           chunk <<= this.alphabet.bitsPerChar;
/*  696 */           if (charIdx + i < chars.length()) {
/*  697 */             chunk |= this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
/*      */           }
/*      */         }
/*  700 */         int minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar;
/*  701 */         for (int offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
/*  702 */           target[(bytesWritten++)] = ((byte)(int)(chunk >>> offset & 0xFF));
/*      */         }
/*      */       }
/*  705 */       return bytesWritten;
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(final Reader reader)
/*      */     {
/*  711 */       Preconditions.checkNotNull(reader);
/*  712 */       new InputStream() {
/*  713 */         int bitBuffer = 0;
/*  714 */         int bitBufferLength = 0;
/*  715 */         int readChars = 0;
/*  716 */         boolean hitPadding = false;
/*  717 */         final CharMatcher paddingMatcher = BaseEncoding.StandardBaseEncoding.this.padding();
/*      */         
/*      */         public int read() throws IOException
/*      */         {
/*      */           for (;;) {
/*  722 */             int readChar = reader.read();
/*  723 */             if (readChar == -1) {
/*  724 */               if ((!this.hitPadding) && (!BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars))) {
/*  725 */                 throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars);
/*      */               }
/*  727 */               return -1;
/*      */             }
/*  729 */             this.readChars += 1;
/*  730 */             char ch = (char)readChar;
/*  731 */             if (this.paddingMatcher.matches(ch)) {
/*  732 */               if ((!this.hitPadding) && ((this.readChars == 1) || 
/*  733 */                 (!BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1)))) {
/*  734 */                 throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars);
/*      */               }
/*  736 */               this.hitPadding = true;
/*  737 */             } else { if (this.hitPadding) {
/*  738 */                 throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
/*      */               }
/*      */               
/*  741 */               this.bitBuffer <<= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*  742 */               this.bitBuffer |= BaseEncoding.StandardBaseEncoding.this.alphabet.decode(ch);
/*  743 */               this.bitBufferLength += BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */               
/*  745 */               if (this.bitBufferLength >= 8) {
/*  746 */                 this.bitBufferLength -= 8;
/*  747 */                 return this.bitBuffer >> this.bitBufferLength & 0xFF;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */         public void close() throws IOException
/*      */         {
/*  755 */           reader.close();
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */     public BaseEncoding omitPadding()
/*      */     {
/*  762 */       return this.paddingChar == null ? this : newInstance(this.alphabet, null);
/*      */     }
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar)
/*      */     {
/*  767 */       if ((8 % this.alphabet.bitsPerChar == 0) || ((this.paddingChar != null) && 
/*  768 */         (this.paddingChar.charValue() == padChar))) {
/*  769 */         return this;
/*      */       }
/*  771 */       return newInstance(this.alphabet, Character.valueOf(padChar));
/*      */     }
/*      */     
/*      */ 
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars)
/*      */     {
/*  777 */       Preconditions.checkArgument(
/*  778 */         padding().or(this.alphabet).matchesNoneOf(separator), "Separator (%s) cannot contain alphabet or padding characters", separator);
/*      */       
/*      */ 
/*  781 */       return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public BaseEncoding upperCase()
/*      */     {
/*  789 */       BaseEncoding result = this.upperCase;
/*  790 */       if (result == null) {
/*  791 */         BaseEncoding.Alphabet upper = this.alphabet.upperCase();
/*      */         
/*  793 */         result = this.upperCase = upper == this.alphabet ? this : newInstance(upper, this.paddingChar);
/*      */       }
/*  795 */       return result;
/*      */     }
/*      */     
/*      */     public BaseEncoding lowerCase()
/*      */     {
/*  800 */       BaseEncoding result = this.lowerCase;
/*  801 */       if (result == null) {
/*  802 */         BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
/*      */         
/*  804 */         result = this.lowerCase = lower == this.alphabet ? this : newInstance(lower, this.paddingChar);
/*      */       }
/*  806 */       return result;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  810 */       return new StandardBaseEncoding(alphabet, paddingChar);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  815 */       StringBuilder builder = new StringBuilder("BaseEncoding.");
/*  816 */       builder.append(this.alphabet.toString());
/*  817 */       if (8 % this.alphabet.bitsPerChar != 0) {
/*  818 */         if (this.paddingChar == null) {
/*  819 */           builder.append(".omitPadding()");
/*      */         } else {
/*  821 */           builder.append(".withPadChar('").append(this.paddingChar).append("')");
/*      */         }
/*      */       }
/*  824 */       return builder.toString();
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object other)
/*      */     {
/*  829 */       if ((other instanceof StandardBaseEncoding)) {
/*  830 */         StandardBaseEncoding that = (StandardBaseEncoding)other;
/*  831 */         return (this.alphabet.equals(that.alphabet)) && 
/*  832 */           (Objects.equal(this.paddingChar, that.paddingChar));
/*      */       }
/*  834 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  839 */       return this.alphabet.hashCode() ^ Objects.hashCode(new Object[] { this.paddingChar });
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base16Encoding extends BaseEncoding.StandardBaseEncoding {
/*  844 */     final char[] encoding = new char['Ȁ'];
/*      */     
/*      */     Base16Encoding(String name, String alphabetChars) {
/*  847 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
/*      */     }
/*      */     
/*      */     private Base16Encoding(BaseEncoding.Alphabet alphabet) {
/*  851 */       super(null);
/*  852 */       Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 16);
/*  853 */       for (int i = 0; i < 256; i++) {
/*  854 */         this.encoding[i] = alphabet.encode(i >>> 4);
/*  855 */         this.encoding[(i | 0x100)] = alphabet.encode(i & 0xF);
/*      */       }
/*      */     }
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
/*      */     {
/*  861 */       Preconditions.checkNotNull(target);
/*  862 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  863 */       for (int i = 0; i < len; i++) {
/*  864 */         int b = bytes[(off + i)] & 0xFF;
/*  865 */         target.append(this.encoding[b]);
/*  866 */         target.append(this.encoding[(b | 0x100)]);
/*      */       }
/*      */     }
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
/*      */     {
/*  872 */       Preconditions.checkNotNull(target);
/*  873 */       if (chars.length() % 2 == 1) {
/*  874 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  876 */       int bytesWritten = 0;
/*  877 */       for (int i = 0; i < chars.length(); i += 2) {
/*  878 */         int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
/*  879 */         target[(bytesWritten++)] = ((byte)decoded);
/*      */       }
/*  881 */       return bytesWritten;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
/*      */     {
/*  886 */       return new Base16Encoding(alphabet);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base64Encoding extends BaseEncoding.StandardBaseEncoding {
/*      */     Base64Encoding(String name, String alphabetChars, @Nullable Character paddingChar) {
/*  892 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     private Base64Encoding(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar) {
/*  896 */       super(paddingChar);
/*  897 */       Preconditions.checkArgument(BaseEncoding.Alphabet.access$000(alphabet).length == 64);
/*      */     }
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
/*      */     {
/*  902 */       Preconditions.checkNotNull(target);
/*  903 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  904 */       int i = off;
/*  905 */       for (int remaining = len; remaining >= 3; remaining -= 3) {
/*  906 */         int chunk = (bytes[(i++)] & 0xFF) << 16 | (bytes[(i++)] & 0xFF) << 8 | bytes[(i++)] & 0xFF;
/*  907 */         target.append(this.alphabet.encode(chunk >>> 18));
/*  908 */         target.append(this.alphabet.encode(chunk >>> 12 & 0x3F));
/*  909 */         target.append(this.alphabet.encode(chunk >>> 6 & 0x3F));
/*  910 */         target.append(this.alphabet.encode(chunk & 0x3F));
/*      */       }
/*  912 */       if (i < off + len) {
/*  913 */         encodeChunkTo(target, bytes, i, off + len - i);
/*      */       }
/*      */     }
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
/*      */     {
/*  919 */       Preconditions.checkNotNull(target);
/*  920 */       chars = padding().trimTrailingFrom(chars);
/*  921 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  922 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  924 */       int bytesWritten = 0;
/*  925 */       for (int i = 0; i < chars.length();) {
/*  926 */         int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
/*  927 */         chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
/*  928 */         target[(bytesWritten++)] = ((byte)(chunk >>> 16));
/*  929 */         if (i < chars.length()) {
/*  930 */           chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
/*  931 */           target[(bytesWritten++)] = ((byte)(chunk >>> 8 & 0xFF));
/*  932 */           if (i < chars.length()) {
/*  933 */             chunk |= this.alphabet.decode(chars.charAt(i++));
/*  934 */             target[(bytesWritten++)] = ((byte)(chunk & 0xFF));
/*      */           }
/*      */         }
/*      */       }
/*  938 */       return bytesWritten;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @Nullable Character paddingChar)
/*      */     {
/*  943 */       return new Base64Encoding(alphabet, paddingChar);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static Reader ignoringReader(Reader delegate, final CharMatcher toIgnore) {
/*  949 */     Preconditions.checkNotNull(delegate);
/*  950 */     Preconditions.checkNotNull(toIgnore);
/*  951 */     new Reader()
/*      */     {
/*      */       public int read() throws IOException {
/*      */         int readChar;
/*      */         do {
/*  956 */           readChar = this.val$delegate.read();
/*  957 */         } while ((readChar != -1) && (toIgnore.matches((char)readChar)));
/*  958 */         return readChar;
/*      */       }
/*      */       
/*      */       public int read(char[] cbuf, int off, int len) throws IOException
/*      */       {
/*  963 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       public void close() throws IOException
/*      */       {
/*  968 */         this.val$delegate.close();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   static Appendable separatingAppendable(final Appendable delegate, final String separator, int afterEveryChars)
/*      */   {
/*  975 */     Preconditions.checkNotNull(delegate);
/*  976 */     Preconditions.checkNotNull(separator);
/*  977 */     Preconditions.checkArgument(afterEveryChars > 0);
/*  978 */     new Appendable() {
/*  979 */       int charsUntilSeparator = this.val$afterEveryChars;
/*      */       
/*      */       public Appendable append(char c) throws IOException
/*      */       {
/*  983 */         if (this.charsUntilSeparator == 0) {
/*  984 */           delegate.append(separator);
/*  985 */           this.charsUntilSeparator = this.val$afterEveryChars;
/*      */         }
/*  987 */         delegate.append(c);
/*  988 */         this.charsUntilSeparator -= 1;
/*  989 */         return this;
/*      */       }
/*      */       
/*      */       public Appendable append(CharSequence chars, int off, int len) throws IOException
/*      */       {
/*  994 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       public Appendable append(CharSequence chars) throws IOException
/*      */       {
/*  999 */         throw new UnsupportedOperationException();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */   @GwtIncompatible
/*      */   static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars)
/*      */   {
/* 1008 */     Appendable seperatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
/* 1009 */     new Writer()
/*      */     {
/*      */       public void write(int c) throws IOException {
/* 1012 */         this.val$seperatingAppendable.append((char)c);
/*      */       }
/*      */       
/*      */       public void write(char[] chars, int off, int len) throws IOException
/*      */       {
/* 1017 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       
/*      */       public void flush() throws IOException
/*      */       {
/* 1022 */         delegate.flush();
/*      */       }
/*      */       
/*      */       public void close() throws IOException
/*      */       {
/* 1027 */         delegate.close();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   static final class SeparatedBaseEncoding extends BaseEncoding {
/*      */     private final BaseEncoding delegate;
/*      */     private final String separator;
/*      */     private final int afterEveryChars;
/*      */     private final CharMatcher separatorChars;
/*      */     
/*      */     SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
/* 1039 */       this.delegate = ((BaseEncoding)Preconditions.checkNotNull(delegate));
/* 1040 */       this.separator = ((String)Preconditions.checkNotNull(separator));
/* 1041 */       this.afterEveryChars = afterEveryChars;
/* 1042 */       Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
/*      */       
/* 1044 */       this.separatorChars = CharMatcher.anyOf(separator).precomputed();
/*      */     }
/*      */     
/*      */     CharMatcher padding()
/*      */     {
/* 1049 */       return this.delegate.padding();
/*      */     }
/*      */     
/*      */     int maxEncodedSize(int bytes)
/*      */     {
/* 1054 */       int unseparatedSize = this.delegate.maxEncodedSize(bytes);
/* 1055 */       return unseparatedSize + this.separator
/* 1056 */         .length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(Writer output)
/*      */     {
/* 1062 */       return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
/*      */     }
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException
/*      */     {
/* 1067 */       this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
/*      */     }
/*      */     
/*      */     int maxDecodedSize(int chars)
/*      */     {
/* 1072 */       return this.delegate.maxDecodedSize(chars);
/*      */     }
/*      */     
/*      */     public boolean canDecode(CharSequence chars)
/*      */     {
/* 1077 */       return this.delegate.canDecode(this.separatorChars.removeFrom(chars));
/*      */     }
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException
/*      */     {
/* 1082 */       return this.delegate.decodeTo(target, this.separatorChars.removeFrom(chars));
/*      */     }
/*      */     
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(Reader reader)
/*      */     {
/* 1088 */       return this.delegate.decodingStream(ignoringReader(reader, this.separatorChars));
/*      */     }
/*      */     
/*      */     public BaseEncoding omitPadding()
/*      */     {
/* 1093 */       return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar)
/*      */     {
/* 1098 */       return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars)
/*      */     {
/* 1103 */       throw new UnsupportedOperationException("Already have a separator");
/*      */     }
/*      */     
/*      */     public BaseEncoding upperCase()
/*      */     {
/* 1108 */       return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */     
/*      */     public BaseEncoding lowerCase()
/*      */     {
/* 1113 */       return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1118 */       return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\BaseEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */