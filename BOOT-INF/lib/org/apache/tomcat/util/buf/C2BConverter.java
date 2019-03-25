/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ public final class C2BConverter
/*     */ {
/*     */   private final CharsetEncoder encoder;
/*  33 */   private ByteBuffer bb = null;
/*  34 */   private CharBuffer cb = null;
/*     */   
/*     */ 
/*     */   private final CharBuffer leftovers;
/*     */   
/*     */ 
/*     */   public C2BConverter(Charset charset)
/*     */   {
/*  42 */     this.encoder = charset.newEncoder();
/*  43 */     this.encoder.onUnmappableCharacter(CodingErrorAction.REPLACE)
/*  44 */       .onMalformedInput(CodingErrorAction.REPLACE);
/*  45 */     char[] left = new char[4];
/*  46 */     this.leftovers = CharBuffer.wrap(left);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/*  53 */     this.encoder.reset();
/*  54 */     this.leftovers.position(0);
/*     */   }
/*     */   
/*     */   public boolean isUndeflow() {
/*  58 */     return this.leftovers.position() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void convert(CharChunk cc, ByteChunk bc)
/*     */     throws IOException
/*     */   {
/*  69 */     if ((this.bb == null) || (this.bb.array() != bc.getBuffer()))
/*     */     {
/*  71 */       this.bb = ByteBuffer.wrap(bc.getBuffer(), bc.getEnd(), bc.getBuffer().length - bc.getEnd());
/*     */     }
/*     */     else {
/*  74 */       this.bb.limit(bc.getBuffer().length);
/*  75 */       this.bb.position(bc.getEnd());
/*     */     }
/*  77 */     if ((this.cb == null) || (this.cb.array() != cc.getBuffer()))
/*     */     {
/*  79 */       this.cb = CharBuffer.wrap(cc.getBuffer(), cc.getStart(), cc.getLength());
/*     */     }
/*     */     else {
/*  82 */       this.cb.limit(cc.getEnd());
/*  83 */       this.cb.position(cc.getStart());
/*     */     }
/*  85 */     CoderResult result = null;
/*     */     
/*  87 */     if (this.leftovers.position() > 0) {
/*  88 */       int pos = this.bb.position();
/*     */       do
/*     */       {
/*  91 */         this.leftovers.put((char)cc.substract());
/*  92 */         this.leftovers.flip();
/*  93 */         result = this.encoder.encode(this.leftovers, this.bb, false);
/*  94 */         this.leftovers.position(this.leftovers.limit());
/*  95 */         this.leftovers.limit(this.leftovers.array().length);
/*  96 */       } while ((result.isUnderflow()) && (this.bb.position() == pos));
/*  97 */       if ((result.isError()) || (result.isMalformed())) {
/*  98 */         result.throwException();
/*     */       }
/* 100 */       this.cb.position(cc.getStart());
/* 101 */       this.leftovers.position(0);
/*     */     }
/*     */     
/*     */ 
/* 105 */     result = this.encoder.encode(this.cb, this.bb, false);
/* 106 */     if ((result.isError()) || (result.isMalformed())) {
/* 107 */       result.throwException();
/* 108 */     } else if (result.isOverflow())
/*     */     {
/* 110 */       bc.setEnd(this.bb.position());
/* 111 */       cc.setOffset(this.cb.position());
/* 112 */     } else if (result.isUnderflow())
/*     */     {
/* 114 */       bc.setEnd(this.bb.position());
/* 115 */       cc.setOffset(this.cb.position());
/*     */       
/* 117 */       if (cc.getLength() > 0) {
/* 118 */         this.leftovers.limit(this.leftovers.array().length);
/* 119 */         this.leftovers.position(cc.getLength());
/* 120 */         cc.substract(this.leftovers.array(), 0, cc.getLength());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void convert(CharBuffer cc, ByteBuffer bc)
/*     */     throws IOException
/*     */   {
/* 133 */     if ((this.bb == null) || (this.bb.array() != bc.array()))
/*     */     {
/* 135 */       this.bb = ByteBuffer.wrap(bc.array(), bc.limit(), bc.capacity() - bc.limit());
/*     */     }
/*     */     else {
/* 138 */       this.bb.limit(bc.capacity());
/* 139 */       this.bb.position(bc.limit());
/*     */     }
/* 141 */     if ((this.cb == null) || (this.cb.array() != cc.array()))
/*     */     {
/* 143 */       this.cb = CharBuffer.wrap(cc.array(), cc.arrayOffset() + cc.position(), cc.remaining());
/*     */     }
/*     */     else {
/* 146 */       this.cb.limit(cc.limit());
/* 147 */       this.cb.position(cc.position());
/*     */     }
/* 149 */     CoderResult result = null;
/*     */     
/* 151 */     if (this.leftovers.position() > 0) {
/* 152 */       int pos = this.bb.position();
/*     */       do
/*     */       {
/* 155 */         this.leftovers.put(cc.get());
/* 156 */         this.leftovers.flip();
/* 157 */         result = this.encoder.encode(this.leftovers, this.bb, false);
/* 158 */         this.leftovers.position(this.leftovers.limit());
/* 159 */         this.leftovers.limit(this.leftovers.array().length);
/* 160 */       } while ((result.isUnderflow()) && (this.bb.position() == pos));
/* 161 */       if ((result.isError()) || (result.isMalformed())) {
/* 162 */         result.throwException();
/*     */       }
/* 164 */       this.cb.position(cc.position());
/* 165 */       this.leftovers.position(0);
/*     */     }
/*     */     
/*     */ 
/* 169 */     result = this.encoder.encode(this.cb, this.bb, false);
/* 170 */     if ((result.isError()) || (result.isMalformed())) {
/* 171 */       result.throwException();
/* 172 */     } else if (result.isOverflow())
/*     */     {
/* 174 */       bc.limit(this.bb.position());
/* 175 */       cc.position(this.cb.position());
/* 176 */     } else if (result.isUnderflow())
/*     */     {
/* 178 */       bc.limit(this.bb.position());
/* 179 */       cc.position(this.cb.position());
/*     */       
/* 181 */       if (cc.remaining() > 0) {
/* 182 */         this.leftovers.limit(this.leftovers.array().length);
/* 183 */         this.leftovers.position(cc.remaining());
/* 184 */         cc.get(this.leftovers.array(), 0, cc.remaining());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 190 */     return this.encoder.charset();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\C2BConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */