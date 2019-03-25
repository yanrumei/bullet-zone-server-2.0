/*     */ package org.springframework.boot.loader.jar;
/*     */ 
/*     */ import java.nio.charset.Charset;
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
/*     */ final class AsciiBytes
/*     */ {
/*  30 */   private static final Charset UTF_8 = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */   private final byte[] bytes;
/*     */   
/*     */ 
/*     */   private final int offset;
/*     */   
/*     */   private final int length;
/*     */   
/*     */   private String string;
/*     */   
/*     */   private int hash;
/*     */   
/*     */ 
/*     */   AsciiBytes(String string)
/*     */   {
/*  47 */     this(string.getBytes(UTF_8));
/*  48 */     this.string = string;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AsciiBytes(byte[] bytes)
/*     */   {
/*  57 */     this(bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AsciiBytes(byte[] bytes, int offset, int length)
/*     */   {
/*  68 */     if ((offset < 0) || (length < 0) || (offset + length > bytes.length)) {
/*  69 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  71 */     this.bytes = bytes;
/*  72 */     this.offset = offset;
/*  73 */     this.length = length;
/*     */   }
/*     */   
/*     */   public int length() {
/*  77 */     return this.length;
/*     */   }
/*     */   
/*     */   public boolean startsWith(AsciiBytes prefix) {
/*  81 */     if (this == prefix) {
/*  82 */       return true;
/*     */     }
/*  84 */     if (prefix.length > this.length) {
/*  85 */       return false;
/*     */     }
/*  87 */     for (int i = 0; i < prefix.length; i++) {
/*  88 */       if (this.bytes[(i + this.offset)] != prefix.bytes[(i + prefix.offset)]) {
/*  89 */         return false;
/*     */       }
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   public boolean endsWith(AsciiBytes postfix) {
/*  96 */     if (this == postfix) {
/*  97 */       return true;
/*     */     }
/*  99 */     if (postfix.length > this.length) {
/* 100 */       return false;
/*     */     }
/* 102 */     for (int i = 0; i < postfix.length; i++) {
/* 103 */       if (this.bytes[(this.offset + (this.length - 1) - i)] != postfix.bytes[(postfix.offset + (postfix.length - 1) - i)])
/*     */       {
/* 105 */         return false;
/*     */       }
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */   
/*     */   public AsciiBytes substring(int beginIndex) {
/* 112 */     return substring(beginIndex, this.length);
/*     */   }
/*     */   
/*     */   public AsciiBytes substring(int beginIndex, int endIndex) {
/* 116 */     int length = endIndex - beginIndex;
/* 117 */     if (this.offset + length > this.bytes.length) {
/* 118 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 120 */     return new AsciiBytes(this.bytes, this.offset + beginIndex, length);
/*     */   }
/*     */   
/*     */   public AsciiBytes append(String string) {
/* 124 */     if ((string == null) || (string.isEmpty())) {
/* 125 */       return this;
/*     */     }
/* 127 */     return append(string.getBytes(UTF_8));
/*     */   }
/*     */   
/*     */   public AsciiBytes append(AsciiBytes asciiBytes) {
/* 131 */     if ((asciiBytes == null) || (asciiBytes.length() == 0)) {
/* 132 */       return this;
/*     */     }
/* 134 */     return append(asciiBytes.bytes);
/*     */   }
/*     */   
/*     */   public AsciiBytes append(byte[] bytes) {
/* 138 */     if ((bytes == null) || (bytes.length == 0)) {
/* 139 */       return this;
/*     */     }
/* 141 */     byte[] combined = new byte[this.length + bytes.length];
/* 142 */     System.arraycopy(this.bytes, this.offset, combined, 0, this.length);
/* 143 */     System.arraycopy(bytes, 0, combined, this.length, bytes.length);
/* 144 */     return new AsciiBytes(combined);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 149 */     if (this.string == null) {
/* 150 */       this.string = new String(this.bytes, this.offset, this.length, UTF_8);
/*     */     }
/* 152 */     return this.string;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 157 */     int hash = this.hash;
/* 158 */     if ((hash == 0) && (this.bytes.length > 0)) {
/* 159 */       for (int i = this.offset; i < this.offset + this.length; i++) {
/* 160 */         int b = this.bytes[i];
/* 161 */         if (b < 0) {
/* 162 */           b &= 0x7F;
/*     */           
/* 164 */           int excess = 128;
/* 165 */           int limit; if (b < 96) {
/* 166 */             int limit = 1;
/* 167 */             excess += 4096;
/*     */           }
/* 169 */           else if (b < 112) {
/* 170 */             int limit = 2;
/* 171 */             excess += 401408;
/*     */           }
/*     */           else {
/* 174 */             limit = 3;
/* 175 */             excess += 29892608;
/*     */           }
/* 177 */           for (int j = 0; j < limit; j++) {
/* 178 */             b = (b << 6) + (this.bytes[(++i)] & 0xFF);
/*     */           }
/* 180 */           b -= excess;
/*     */         }
/* 182 */         if (b <= 65535) {
/* 183 */           hash = 31 * hash + b;
/*     */         }
/*     */         else {
/* 186 */           hash = 31 * hash + ((b >> 10) + 55232);
/* 187 */           hash = 31 * hash + ((b & 0x3FF) + 56320);
/*     */         }
/*     */       }
/* 190 */       this.hash = hash;
/*     */     }
/* 192 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 197 */     if (obj == null) {
/* 198 */       return false;
/*     */     }
/* 200 */     if (this == obj) {
/* 201 */       return true;
/*     */     }
/* 203 */     if (obj.getClass().equals(AsciiBytes.class)) {
/* 204 */       AsciiBytes other = (AsciiBytes)obj;
/* 205 */       if (this.length == other.length) {
/* 206 */         for (int i = 0; i < this.length; i++) {
/* 207 */           if (this.bytes[(this.offset + i)] != other.bytes[(other.offset + i)]) {
/* 208 */             return false;
/*     */           }
/*     */         }
/* 211 */         return true;
/*     */       }
/*     */     }
/* 214 */     return false;
/*     */   }
/*     */   
/*     */   static String toString(byte[] bytes) {
/* 218 */     return new String(bytes, UTF_8);
/*     */   }
/*     */   
/*     */   public static int hashCode(String string)
/*     */   {
/* 223 */     return string.hashCode();
/*     */   }
/*     */   
/*     */   public static int hashCode(int hash, String string) {
/* 227 */     for (int i = 0; i < string.length(); i++) {
/* 228 */       hash = 31 * hash + string.charAt(i);
/*     */     }
/* 230 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\jar\AsciiBytes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */