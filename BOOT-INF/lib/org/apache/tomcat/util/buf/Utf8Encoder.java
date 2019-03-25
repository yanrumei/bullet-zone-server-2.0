/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetEncoder;
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
/*     */ public class Utf8Encoder
/*     */   extends CharsetEncoder
/*     */ {
/*     */   public Utf8Encoder()
/*     */   {
/*  32 */     super(StandardCharsets.UTF_8, 1.1F, 4.0F);
/*     */   }
/*     */   
/*     */   protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out)
/*     */   {
/*  37 */     if ((in.hasArray()) && (out.hasArray())) {
/*  38 */       return encodeHasArray(in, out);
/*     */     }
/*  40 */     return encodeNotHasArray(in, out);
/*     */   }
/*     */   
/*     */   private CoderResult encodeHasArray(CharBuffer in, ByteBuffer out) {
/*  44 */     int outRemaining = out.remaining();
/*  45 */     int pos = in.position();
/*  46 */     int limit = in.limit();
/*     */     
/*     */ 
/*  49 */     int x = pos;
/*  50 */     byte[] bArr = out.array();
/*  51 */     char[] cArr = in.array();
/*  52 */     int outPos = out.position();
/*  53 */     int rem = in.remaining();
/*  54 */     for (x = pos; x < pos + rem; x++) {
/*  55 */       int jchar = cArr[x] & 0xFFFF;
/*     */       
/*  57 */       if (jchar <= 127) {
/*  58 */         if (outRemaining < 1) {
/*  59 */           in.position(x);
/*  60 */           out.position(outPos);
/*  61 */           return CoderResult.OVERFLOW;
/*     */         }
/*  63 */         bArr[(outPos++)] = ((byte)(jchar & 0xFF));
/*  64 */         outRemaining--;
/*  65 */       } else if (jchar <= 2047)
/*     */       {
/*  67 */         if (outRemaining < 2) {
/*  68 */           in.position(x);
/*  69 */           out.position(outPos);
/*  70 */           return CoderResult.OVERFLOW;
/*     */         }
/*  72 */         bArr[(outPos++)] = ((byte)(192 + (jchar >> 6 & 0x1F)));
/*  73 */         bArr[(outPos++)] = ((byte)(128 + (jchar & 0x3F)));
/*  74 */         outRemaining -= 2;
/*     */       }
/*  76 */       else if ((jchar >= 55296) && (jchar <= 57343))
/*     */       {
/*     */ 
/*  79 */         if (limit <= x + 1) {
/*  80 */           in.position(x);
/*  81 */           out.position(outPos);
/*  82 */           return CoderResult.UNDERFLOW;
/*     */         }
/*     */         
/*  85 */         if (outRemaining < 4) {
/*  86 */           in.position(x);
/*  87 */           out.position(outPos);
/*  88 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/*     */ 
/*  92 */         if (jchar >= 56320) {
/*  93 */           in.position(x);
/*  94 */           out.position(outPos);
/*  95 */           return CoderResult.malformedForLength(1);
/*     */         }
/*     */         
/*  98 */         int jchar2 = cArr[(x + 1)] & 0xFFFF;
/*     */         
/*     */ 
/* 101 */         if (jchar2 < 56320) {
/* 102 */           in.position(x);
/* 103 */           out.position(outPos);
/* 104 */           return CoderResult.malformedForLength(1);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */         int n = (jchar << 10) + jchar2 + -56613888;
/*     */         
/* 114 */         bArr[(outPos++)] = ((byte)(240 + (n >> 18 & 0x7)));
/* 115 */         bArr[(outPos++)] = ((byte)(128 + (n >> 12 & 0x3F)));
/* 116 */         bArr[(outPos++)] = ((byte)(128 + (n >> 6 & 0x3F)));
/* 117 */         bArr[(outPos++)] = ((byte)(128 + (n & 0x3F)));
/* 118 */         outRemaining -= 4;
/* 119 */         x++;
/*     */       }
/*     */       else
/*     */       {
/* 123 */         if (outRemaining < 3) {
/* 124 */           in.position(x);
/* 125 */           out.position(outPos);
/* 126 */           return CoderResult.OVERFLOW;
/*     */         }
/* 128 */         bArr[(outPos++)] = ((byte)(224 + (jchar >> 12 & 0xF)));
/* 129 */         bArr[(outPos++)] = ((byte)(128 + (jchar >> 6 & 0x3F)));
/* 130 */         bArr[(outPos++)] = ((byte)(128 + (jchar & 0x3F)));
/* 131 */         outRemaining -= 3;
/*     */       }
/* 133 */       if (outRemaining == 0) {
/* 134 */         in.position(x + 1);
/* 135 */         out.position(outPos);
/*     */         
/* 137 */         if (x + 1 == limit) {
/* 138 */           return CoderResult.UNDERFLOW;
/*     */         }
/* 140 */         return CoderResult.OVERFLOW;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 145 */     if (rem != 0) {
/* 146 */       in.position(x);
/* 147 */       out.position(outPos);
/*     */     }
/* 149 */     return CoderResult.UNDERFLOW;
/*     */   }
/*     */   
/*     */   private CoderResult encodeNotHasArray(CharBuffer in, ByteBuffer out) {
/* 153 */     int outRemaining = out.remaining();
/* 154 */     int pos = in.position();
/* 155 */     int limit = in.limit();
/*     */     try {
/* 157 */       while (pos < limit) {
/* 158 */         if (outRemaining == 0) {
/* 159 */           return CoderResult.OVERFLOW;
/*     */         }
/*     */         
/* 162 */         int jchar = in.get() & 0xFFFF;
/*     */         CoderResult localCoderResult2;
/* 164 */         if (jchar <= 127)
/*     */         {
/* 166 */           if (outRemaining < 1) {
/* 167 */             return CoderResult.OVERFLOW;
/*     */           }
/* 169 */           out.put((byte)jchar);
/* 170 */           outRemaining--;
/*     */         }
/* 172 */         else if (jchar <= 2047)
/*     */         {
/* 174 */           if (outRemaining < 2) {
/* 175 */             return CoderResult.OVERFLOW;
/*     */           }
/* 177 */           out.put((byte)(192 + (jchar >> 6 & 0x1F)));
/* 178 */           out.put((byte)(128 + (jchar & 0x3F)));
/* 179 */           outRemaining -= 2;
/*     */         } else { int jchar2;
/* 181 */           if ((jchar >= 55296) && (jchar <= 57343))
/*     */           {
/*     */ 
/* 184 */             if (limit <= pos + 1) {
/* 185 */               return CoderResult.UNDERFLOW;
/*     */             }
/*     */             
/* 188 */             if (outRemaining < 4) {
/* 189 */               return CoderResult.OVERFLOW;
/*     */             }
/*     */             
/*     */ 
/* 193 */             if (jchar >= 56320) {
/* 194 */               return CoderResult.malformedForLength(1);
/*     */             }
/*     */             
/* 197 */             jchar2 = in.get() & 0xFFFF;
/*     */             
/*     */ 
/* 200 */             if (jchar2 < 56320) {
/* 201 */               return CoderResult.malformedForLength(1);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */             int n = (jchar << 10) + jchar2 + -56613888;
/*     */             
/* 211 */             out.put((byte)(240 + (n >> 18 & 0x7)));
/* 212 */             out.put((byte)(128 + (n >> 12 & 0x3F)));
/* 213 */             out.put((byte)(128 + (n >> 6 & 0x3F)));
/* 214 */             out.put((byte)(128 + (n & 0x3F)));
/* 215 */             outRemaining -= 4;
/* 216 */             pos++;
/*     */           }
/*     */           else
/*     */           {
/* 220 */             if (outRemaining < 3) {
/* 221 */               return CoderResult.OVERFLOW;
/*     */             }
/* 223 */             out.put((byte)(224 + (jchar >> 12 & 0xF)));
/* 224 */             out.put((byte)(128 + (jchar >> 6 & 0x3F)));
/* 225 */             out.put((byte)(128 + (jchar & 0x3F)));
/* 226 */             outRemaining -= 3;
/*     */           } }
/* 228 */         pos++;
/*     */       }
/*     */     } finally {
/* 231 */       in.position(pos);
/*     */     }
/* 233 */     return CoderResult.UNDERFLOW;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\Utf8Encoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */