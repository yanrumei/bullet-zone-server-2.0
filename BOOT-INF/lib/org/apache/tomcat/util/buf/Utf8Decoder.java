/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public class Utf8Decoder
/*     */   extends CharsetDecoder
/*     */ {
/*  46 */   private static final int[] remainingBytes = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
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
/*  61 */   private static final int[] remainingNumbers = { 0, 4224, 401536, 29892736 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private static final int[] lowerEncodingLimit = { -1, 128, 2048, 65536 };
/*     */   
/*     */   public Utf8Decoder()
/*     */   {
/*  71 */     super(StandardCharsets.UTF_8, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */   protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out)
/*     */   {
/*  77 */     if ((in.hasArray()) && (out.hasArray())) {
/*  78 */       return decodeHasArray(in, out);
/*     */     }
/*  80 */     return decodeNotHasArray(in, out);
/*     */   }
/*     */   
/*     */   private CoderResult decodeNotHasArray(ByteBuffer in, CharBuffer out)
/*     */   {
/*  85 */     int outRemaining = out.remaining();
/*  86 */     int pos = in.position();
/*  87 */     int limit = in.limit();
/*     */     try { int jchar;
/*  89 */       while (pos < limit) {
/*  90 */         if (outRemaining == 0) {
/*  91 */           return CoderResult.OVERFLOW;
/*     */         }
/*  93 */         jchar = in.get();
/*  94 */         int tail; if (jchar < 0) {
/*  95 */           jchar &= 0x7F;
/*  96 */           tail = remainingBytes[jchar];
/*  97 */           CoderResult localCoderResult3; if (tail == -1) {
/*  98 */             return CoderResult.malformedForLength(1);
/*     */           }
/* 100 */           if (limit - pos < 1 + tail)
/*     */           {
/*     */ 
/* 103 */             return CoderResult.UNDERFLOW;
/*     */           }
/*     */           
/* 106 */           for (int i = 0; i < tail; i++) {
/* 107 */             int nextByte = in.get() & 0xFF;
/* 108 */             if ((nextByte & 0xC0) != 128) {
/* 109 */               return CoderResult.malformedForLength(1 + i);
/*     */             }
/* 111 */             jchar = (jchar << 6) + nextByte;
/*     */           }
/* 113 */           jchar -= remainingNumbers[tail];
/* 114 */           if (jchar < lowerEncodingLimit[tail])
/*     */           {
/* 116 */             return CoderResult.malformedForLength(1);
/*     */           }
/* 118 */           pos += tail;
/*     */         }
/*     */         
/* 121 */         if ((jchar >= 55296) && (jchar <= 57343)) {
/* 122 */           return CoderResult.unmappableForLength(3);
/*     */         }
/*     */         
/* 125 */         if (jchar > 1114111) {
/* 126 */           return CoderResult.unmappableForLength(4);
/*     */         }
/* 128 */         if (jchar <= 65535) {
/* 129 */           out.put((char)jchar);
/* 130 */           outRemaining--;
/*     */         } else {
/* 132 */           if (outRemaining < 2) {
/* 133 */             return CoderResult.OVERFLOW;
/*     */           }
/* 135 */           out.put((char)((jchar >> 10) + 55232));
/* 136 */           out.put((char)((jchar & 0x3FF) + 56320));
/* 137 */           outRemaining -= 2;
/*     */         }
/* 139 */         pos++;
/*     */       }
/* 141 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 143 */       in.position(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private CoderResult decodeHasArray(ByteBuffer in, CharBuffer out)
/*     */   {
/* 149 */     int outRemaining = out.remaining();
/* 150 */     int pos = in.position();
/* 151 */     int limit = in.limit();
/* 152 */     byte[] bArr = in.array();
/* 153 */     char[] cArr = out.array();
/* 154 */     int inIndexLimit = limit + in.arrayOffset();
/* 155 */     int inIndex = pos + in.arrayOffset();
/* 156 */     int outIndex = out.position() + out.arrayOffset();
/* 159 */     for (; 
/*     */         
/* 159 */         (inIndex < inIndexLimit) && (outRemaining > 0); inIndex++) {
/* 160 */       int jchar = bArr[inIndex];
/* 161 */       if (jchar < 0) {
/* 162 */         jchar &= 0x7F;
/*     */         
/* 164 */         int tail = remainingBytes[jchar];
/* 165 */         if (tail == -1) {
/* 166 */           in.position(inIndex - in.arrayOffset());
/* 167 */           out.position(outIndex - out.arrayOffset());
/* 168 */           return CoderResult.malformedForLength(1);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 173 */         int tailAvailable = inIndexLimit - inIndex - 1;
/* 174 */         if (tailAvailable > 0)
/*     */         {
/* 176 */           if ((jchar > 65) && (jchar < 96) && ((bArr[(inIndex + 1)] & 0xC0) != 128))
/*     */           {
/* 178 */             in.position(inIndex - in.arrayOffset());
/* 179 */             out.position(outIndex - out.arrayOffset());
/* 180 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 183 */           if ((jchar == 96) && ((bArr[(inIndex + 1)] & 0xE0) != 160)) {
/* 184 */             in.position(inIndex - in.arrayOffset());
/* 185 */             out.position(outIndex - out.arrayOffset());
/* 186 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 189 */           if ((jchar > 96) && (jchar < 109) && ((bArr[(inIndex + 1)] & 0xC0) != 128))
/*     */           {
/* 191 */             in.position(inIndex - in.arrayOffset());
/* 192 */             out.position(outIndex - out.arrayOffset());
/* 193 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 196 */           if ((jchar == 109) && ((bArr[(inIndex + 1)] & 0xE0) != 128)) {
/* 197 */             in.position(inIndex - in.arrayOffset());
/* 198 */             out.position(outIndex - out.arrayOffset());
/* 199 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 202 */           if ((jchar > 109) && (jchar < 112) && ((bArr[(inIndex + 1)] & 0xC0) != 128))
/*     */           {
/* 204 */             in.position(inIndex - in.arrayOffset());
/* 205 */             out.position(outIndex - out.arrayOffset());
/* 206 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 209 */           if ((jchar == 112) && (((bArr[(inIndex + 1)] & 0xFF) < 144) || ((bArr[(inIndex + 1)] & 0xFF) > 191)))
/*     */           {
/*     */ 
/* 212 */             in.position(inIndex - in.arrayOffset());
/* 213 */             out.position(outIndex - out.arrayOffset());
/* 214 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 217 */           if ((jchar > 112) && (jchar < 116) && ((bArr[(inIndex + 1)] & 0xC0) != 128))
/*     */           {
/* 219 */             in.position(inIndex - in.arrayOffset());
/* 220 */             out.position(outIndex - out.arrayOffset());
/* 221 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */           
/* 224 */           if ((jchar == 116) && ((bArr[(inIndex + 1)] & 0xF0) != 128))
/*     */           {
/* 226 */             in.position(inIndex - in.arrayOffset());
/* 227 */             out.position(outIndex - out.arrayOffset());
/* 228 */             return CoderResult.malformedForLength(1);
/*     */           }
/*     */         }
/*     */         
/* 232 */         if ((tailAvailable > 1) && (tail > 1) && 
/* 233 */           ((bArr[(inIndex + 2)] & 0xC0) != 128)) {
/* 234 */           in.position(inIndex - in.arrayOffset());
/* 235 */           out.position(outIndex - out.arrayOffset());
/* 236 */           return CoderResult.malformedForLength(2);
/*     */         }
/*     */         
/*     */ 
/* 240 */         if ((tailAvailable > 2) && (tail > 2) && 
/* 241 */           ((bArr[(inIndex + 3)] & 0xC0) != 128)) {
/* 242 */           in.position(inIndex - in.arrayOffset());
/* 243 */           out.position(outIndex - out.arrayOffset());
/* 244 */           return CoderResult.malformedForLength(3);
/*     */         }
/*     */         
/* 247 */         if (tailAvailable < tail) {
/*     */           break;
/*     */         }
/* 250 */         for (int i = 0; i < tail; i++) {
/* 251 */           int nextByte = bArr[(inIndex + i + 1)] & 0xFF;
/* 252 */           if ((nextByte & 0xC0) != 128) {
/* 253 */             in.position(inIndex - in.arrayOffset());
/* 254 */             out.position(outIndex - out.arrayOffset());
/* 255 */             return CoderResult.malformedForLength(1 + i);
/*     */           }
/* 257 */           jchar = (jchar << 6) + nextByte;
/*     */         }
/* 259 */         jchar -= remainingNumbers[tail];
/* 260 */         if (jchar < lowerEncodingLimit[tail])
/*     */         {
/* 262 */           in.position(inIndex - in.arrayOffset());
/* 263 */           out.position(outIndex - out.arrayOffset());
/* 264 */           return CoderResult.malformedForLength(1);
/*     */         }
/* 266 */         inIndex += tail;
/*     */       }
/*     */       
/* 269 */       if ((jchar >= 55296) && (jchar <= 57343)) {
/* 270 */         return CoderResult.unmappableForLength(3);
/*     */       }
/*     */       
/* 273 */       if (jchar > 1114111) {
/* 274 */         return CoderResult.unmappableForLength(4);
/*     */       }
/* 276 */       if (jchar <= 65535) {
/* 277 */         cArr[(outIndex++)] = ((char)jchar);
/* 278 */         outRemaining--;
/*     */       } else {
/* 280 */         if (outRemaining < 2) {
/* 281 */           return CoderResult.OVERFLOW;
/*     */         }
/* 283 */         cArr[(outIndex++)] = ((char)((jchar >> 10) + 55232));
/* 284 */         cArr[(outIndex++)] = ((char)((jchar & 0x3FF) + 56320));
/* 285 */         outRemaining -= 2;
/*     */       }
/*     */     }
/* 288 */     in.position(inIndex - in.arrayOffset());
/* 289 */     out.position(outIndex - out.arrayOffset());
/* 290 */     return (outRemaining == 0) && (inIndex < inIndexLimit) ? CoderResult.OVERFLOW : CoderResult.UNDERFLOW;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\Utf8Decoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */