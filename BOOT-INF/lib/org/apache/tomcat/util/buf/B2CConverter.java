/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class B2CConverter
/*     */ {
/*  40 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.buf");
/*     */   
/*  42 */   private static final Map<String, Charset> encodingToCharsetCache = new HashMap();
/*     */   
/*     */   protected static final int LEFTOVER_SIZE = 9;
/*     */   private final CharsetDecoder decoder;
/*     */   
/*     */   static
/*     */   {
/*  49 */     for (Iterator localIterator1 = Charset.availableCharsets().values().iterator(); localIterator1.hasNext();) { charset = (Charset)localIterator1.next();
/*  50 */       encodingToCharsetCache.put(charset
/*  51 */         .name().toLowerCase(Locale.ENGLISH), charset);
/*  52 */       for (String alias : charset.aliases()) {
/*  53 */         encodingToCharsetCache.put(alias
/*  54 */           .toLowerCase(Locale.ENGLISH), charset);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Charset charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Charset getCharset(String enc)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  74 */     String lowerCaseEnc = enc.toLowerCase(Locale.ENGLISH);
/*     */     
/*  76 */     return getCharsetLower(lowerCaseEnc);
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
/*     */   @Deprecated
/*     */   public static Charset getCharsetLower(String lowerCaseEnc)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  96 */     Charset charset = (Charset)encodingToCharsetCache.get(lowerCaseEnc);
/*     */     
/*  98 */     if (charset == null)
/*     */     {
/*     */ 
/* 101 */       throw new UnsupportedEncodingException(sm.getString("b2cConverter.unknownEncoding", new Object[] { lowerCaseEnc }));
/*     */     }
/* 103 */     return charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 108 */   private ByteBuffer bb = null;
/* 109 */   private CharBuffer cb = null;
/*     */   
/*     */ 
/*     */   private final ByteBuffer leftovers;
/*     */   
/*     */ 
/*     */   public B2CConverter(Charset charset)
/*     */   {
/* 117 */     this(charset, false);
/*     */   }
/*     */   
/*     */   public B2CConverter(Charset charset, boolean replaceOnError) {
/* 121 */     byte[] left = new byte[9];
/* 122 */     this.leftovers = ByteBuffer.wrap(left);
/*     */     CodingErrorAction action;
/* 124 */     CodingErrorAction action; if (replaceOnError) {
/* 125 */       action = CodingErrorAction.REPLACE;
/*     */     } else {
/* 127 */       action = CodingErrorAction.REPORT;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 132 */     if (charset.equals(StandardCharsets.UTF_8)) {
/* 133 */       this.decoder = new Utf8Decoder();
/*     */     } else {
/* 135 */       this.decoder = charset.newDecoder();
/*     */     }
/* 137 */     this.decoder.onMalformedInput(action);
/* 138 */     this.decoder.onUnmappableCharacter(action);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 145 */     this.decoder.reset();
/* 146 */     this.leftovers.position(0);
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
/*     */   public void convert(ByteChunk bc, CharChunk cc, boolean endOfInput)
/*     */     throws IOException
/*     */   {
/* 160 */     if ((this.bb == null) || (this.bb.array() != bc.getBuffer()))
/*     */     {
/* 162 */       this.bb = ByteBuffer.wrap(bc.getBuffer(), bc.getStart(), bc.getLength());
/*     */     }
/*     */     else {
/* 165 */       this.bb.limit(bc.getEnd());
/* 166 */       this.bb.position(bc.getStart());
/*     */     }
/* 168 */     if ((this.cb == null) || (this.cb.array() != cc.getBuffer()))
/*     */     {
/* 170 */       this.cb = CharBuffer.wrap(cc.getBuffer(), cc.getEnd(), cc
/* 171 */         .getBuffer().length - cc.getEnd());
/*     */     }
/*     */     else {
/* 174 */       this.cb.limit(cc.getBuffer().length);
/* 175 */       this.cb.position(cc.getEnd());
/*     */     }
/* 177 */     CoderResult result = null;
/*     */     
/* 179 */     if (this.leftovers.position() > 0) {
/* 180 */       int pos = this.cb.position();
/*     */       do
/*     */       {
/* 183 */         this.leftovers.put(bc.substractB());
/* 184 */         this.leftovers.flip();
/* 185 */         result = this.decoder.decode(this.leftovers, this.cb, endOfInput);
/* 186 */         this.leftovers.position(this.leftovers.limit());
/* 187 */         this.leftovers.limit(this.leftovers.array().length);
/* 188 */       } while ((result.isUnderflow()) && (this.cb.position() == pos));
/* 189 */       if ((result.isError()) || (result.isMalformed())) {
/* 190 */         result.throwException();
/*     */       }
/* 192 */       this.bb.position(bc.getStart());
/* 193 */       this.leftovers.position(0);
/*     */     }
/*     */     
/*     */ 
/* 197 */     result = this.decoder.decode(this.bb, this.cb, endOfInput);
/* 198 */     if ((result.isError()) || (result.isMalformed())) {
/* 199 */       result.throwException();
/* 200 */     } else if (result.isOverflow())
/*     */     {
/*     */ 
/* 203 */       bc.setOffset(this.bb.position());
/* 204 */       cc.setEnd(this.cb.position());
/* 205 */     } else if (result.isUnderflow())
/*     */     {
/* 207 */       bc.setOffset(this.bb.position());
/* 208 */       cc.setEnd(this.cb.position());
/*     */       
/* 210 */       if (bc.getLength() > 0) {
/* 211 */         this.leftovers.limit(this.leftovers.array().length);
/* 212 */         this.leftovers.position(bc.getLength());
/* 213 */         bc.substract(this.leftovers.array(), 0, bc.getLength());
/*     */       }
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
/*     */   public void convert(ByteBuffer bc, CharBuffer cc, ByteChunk.ByteInputChannel ic, boolean endOfInput)
/*     */     throws IOException
/*     */   {
/* 230 */     if ((this.bb == null) || (this.bb.array() != bc.array()))
/*     */     {
/* 232 */       this.bb = ByteBuffer.wrap(bc.array(), bc.arrayOffset() + bc.position(), bc.remaining());
/*     */     }
/*     */     else {
/* 235 */       this.bb.limit(bc.limit());
/* 236 */       this.bb.position(bc.position());
/*     */     }
/* 238 */     if ((this.cb == null) || (this.cb.array() != cc.array()))
/*     */     {
/* 240 */       this.cb = CharBuffer.wrap(cc.array(), cc.limit(), cc.capacity() - cc.limit());
/*     */     }
/*     */     else {
/* 243 */       this.cb.limit(cc.capacity());
/* 244 */       this.cb.position(cc.limit());
/*     */     }
/* 246 */     CoderResult result = null;
/*     */     
/* 248 */     if (this.leftovers.position() > 0) {
/* 249 */       int pos = this.cb.position();
/*     */       do {
/*     */         byte chr;
/*     */         byte chr;
/* 253 */         if (bc.remaining() == 0) {
/* 254 */           int n = ic.realReadBytes();
/* 255 */           chr = n < 0 ? -1 : bc.get();
/*     */         } else {
/* 257 */           chr = bc.get();
/*     */         }
/* 259 */         this.leftovers.put(chr);
/* 260 */         this.leftovers.flip();
/* 261 */         result = this.decoder.decode(this.leftovers, this.cb, endOfInput);
/* 262 */         this.leftovers.position(this.leftovers.limit());
/* 263 */         this.leftovers.limit(this.leftovers.array().length);
/* 264 */       } while ((result.isUnderflow()) && (this.cb.position() == pos));
/* 265 */       if ((result.isError()) || (result.isMalformed())) {
/* 266 */         result.throwException();
/*     */       }
/* 268 */       this.bb.position(bc.position());
/* 269 */       this.leftovers.position(0);
/*     */     }
/*     */     
/*     */ 
/* 273 */     result = this.decoder.decode(this.bb, this.cb, endOfInput);
/* 274 */     if ((result.isError()) || (result.isMalformed())) {
/* 275 */       result.throwException();
/* 276 */     } else if (result.isOverflow())
/*     */     {
/*     */ 
/* 279 */       bc.position(this.bb.position());
/* 280 */       cc.limit(this.cb.position());
/* 281 */     } else if (result.isUnderflow())
/*     */     {
/* 283 */       bc.position(this.bb.position());
/* 284 */       cc.limit(this.cb.position());
/*     */       
/* 286 */       if (bc.remaining() > 0) {
/* 287 */         this.leftovers.limit(this.leftovers.array().length);
/* 288 */         this.leftovers.position(bc.remaining());
/* 289 */         bc.get(this.leftovers.array(), 0, bc.remaining());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/* 296 */     return this.decoder.charset();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\B2CConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */