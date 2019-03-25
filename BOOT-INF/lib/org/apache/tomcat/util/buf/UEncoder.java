/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.BitSet;
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
/*     */ public final class UEncoder
/*     */ {
/*     */   public static enum SafeCharsSet
/*     */   {
/*  37 */     WITH_SLASH("/"),  DEFAULT("");
/*     */     
/*     */     private final BitSet safeChars;
/*     */     
/*  41 */     private BitSet getSafeChars() { return this.safeChars; }
/*     */     
/*     */     private SafeCharsSet(String additionalSafeChars)
/*     */     {
/*  45 */       this.safeChars = UEncoder.access$000();
/*  46 */       for (char c : additionalSafeChars.toCharArray()) {
/*  47 */         this.safeChars.set(c);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  54 */   private BitSet safeChars = null;
/*  55 */   private C2BConverter c2b = null;
/*  56 */   private ByteChunk bb = null;
/*  57 */   private CharChunk cb = null;
/*  58 */   private CharChunk output = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UEncoder(SafeCharsSet safeCharsSet)
/*     */   {
/*  66 */     this.safeChars = safeCharsSet.getSafeChars();
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
/*     */   public CharChunk encodeURL(String s, int start, int end)
/*     */     throws IOException
/*     */   {
/*  82 */     if (this.c2b == null) {
/*  83 */       this.bb = new ByteChunk(8);
/*  84 */       this.cb = new CharChunk(2);
/*  85 */       this.output = new CharChunk(64);
/*  86 */       this.c2b = new C2BConverter(StandardCharsets.UTF_8);
/*     */     } else {
/*  88 */       this.bb.recycle();
/*  89 */       this.cb.recycle();
/*  90 */       this.output.recycle();
/*     */     }
/*     */     
/*  93 */     for (int i = start; i < end; i++) {
/*  94 */       char c = s.charAt(i);
/*  95 */       if (this.safeChars.get(c)) {
/*  96 */         this.output.append(c);
/*     */       } else {
/*  98 */         this.cb.append(c);
/*  99 */         this.c2b.convert(this.cb, this.bb);
/*     */         
/*     */ 
/*     */ 
/* 103 */         if ((c >= 55296) && (c <= 56319) && 
/* 104 */           (i + 1 < end)) {
/* 105 */           char d = s.charAt(i + 1);
/* 106 */           if ((d >= 56320) && (d <= 57343)) {
/* 107 */             this.cb.append(d);
/* 108 */             this.c2b.convert(this.cb, this.bb);
/* 109 */             i++;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 114 */         urlEncode(this.output, this.bb);
/* 115 */         this.cb.recycle();
/* 116 */         this.bb.recycle();
/*     */       }
/*     */     }
/*     */     
/* 120 */     return this.output;
/*     */   }
/*     */   
/*     */   protected void urlEncode(CharChunk out, ByteChunk bb) throws IOException
/*     */   {
/* 125 */     byte[] bytes = bb.getBuffer();
/* 126 */     for (int j = bb.getStart(); j < bb.getEnd(); j++) {
/* 127 */       out.append('%');
/* 128 */       char ch = Character.forDigit(bytes[j] >> 4 & 0xF, 16);
/* 129 */       out.append(ch);
/* 130 */       ch = Character.forDigit(bytes[j] & 0xF, 16);
/* 131 */       out.append(ch);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static BitSet initialSafeChars()
/*     */   {
/* 138 */     BitSet initialSafeChars = new BitSet(128);
/*     */     
/* 140 */     for (int i = 97; i <= 122; i++) {
/* 141 */       initialSafeChars.set(i);
/*     */     }
/* 143 */     for (i = 65; i <= 90; i++) {
/* 144 */       initialSafeChars.set(i);
/*     */     }
/* 146 */     for (i = 48; i <= 57; i++) {
/* 147 */       initialSafeChars.set(i);
/*     */     }
/*     */     
/* 150 */     initialSafeChars.set(36);
/* 151 */     initialSafeChars.set(45);
/* 152 */     initialSafeChars.set(95);
/* 153 */     initialSafeChars.set(46);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */     initialSafeChars.set(33);
/* 160 */     initialSafeChars.set(42);
/* 161 */     initialSafeChars.set(39);
/* 162 */     initialSafeChars.set(40);
/* 163 */     initialSafeChars.set(41);
/* 164 */     initialSafeChars.set(44);
/* 165 */     return initialSafeChars;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\UEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */