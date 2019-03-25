/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractCompositeHashFunction
/*    */   extends AbstractStreamingHashFunction
/*    */ {
/*    */   final HashFunction[] functions;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   AbstractCompositeHashFunction(HashFunction... functions)
/*    */   {
/* 32 */     for (HashFunction function : functions) {
/* 33 */       Preconditions.checkNotNull(function);
/*    */     }
/* 35 */     this.functions = functions;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   abstract HashCode makeHash(Hasher[] paramArrayOfHasher);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Hasher newHasher()
/*    */   {
/* 48 */     final Hasher[] hashers = new Hasher[this.functions.length];
/* 49 */     for (int i = 0; i < hashers.length; i++) {
/* 50 */       hashers[i] = this.functions[i].newHasher();
/*    */     }
/* 52 */     new Hasher()
/*    */     {
/*    */       public Hasher putByte(byte b) {
/* 55 */         for (Hasher hasher : hashers) {
/* 56 */           hasher.putByte(b);
/*    */         }
/* 58 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBytes(byte[] bytes)
/*    */       {
/* 63 */         for (Hasher hasher : hashers) {
/* 64 */           hasher.putBytes(bytes);
/*    */         }
/* 66 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBytes(byte[] bytes, int off, int len)
/*    */       {
/* 71 */         for (Hasher hasher : hashers) {
/* 72 */           hasher.putBytes(bytes, off, len);
/*    */         }
/* 74 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putShort(short s)
/*    */       {
/* 79 */         for (Hasher hasher : hashers) {
/* 80 */           hasher.putShort(s);
/*    */         }
/* 82 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putInt(int i)
/*    */       {
/* 87 */         for (Hasher hasher : hashers) {
/* 88 */           hasher.putInt(i);
/*    */         }
/* 90 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putLong(long l)
/*    */       {
/* 95 */         for (Hasher hasher : hashers) {
/* 96 */           hasher.putLong(l);
/*    */         }
/* 98 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putFloat(float f)
/*    */       {
/* :3 */         for (Hasher hasher : hashers) {
/* :4 */           hasher.putFloat(f);
/*    */         }
/* :6 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putDouble(double d)
/*    */       {
/* ;1 */         for (Hasher hasher : hashers) {
/* ;2 */           hasher.putDouble(d);
/*    */         }
/* ;4 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBoolean(boolean b)
/*    */       {
/* ;9 */         for (Hasher hasher : hashers) {
/* <0 */           hasher.putBoolean(b);
/*    */         }
/* <2 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putChar(char c)
/*    */       {
/* <7 */         for (Hasher hasher : hashers) {
/* <8 */           hasher.putChar(c);
/*    */         }
/* =0 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putUnencodedChars(CharSequence chars)
/*    */       {
/* =5 */         for (Hasher hasher : hashers) {
/* =6 */           hasher.putUnencodedChars(chars);
/*    */         }
/* =8 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putString(CharSequence chars, Charset charset)
/*    */       {
/* >3 */         for (Hasher hasher : hashers) {
/* >4 */           hasher.putString(chars, charset);
/*    */         }
/* >6 */         return this;
/*    */       }
/*    */       
/*    */       public <T> Hasher putObject(T instance, Funnel<? super T> funnel)
/*    */       {
/* ?1 */         for (Hasher hasher : hashers) {
/* ?2 */           hasher.putObject(instance, funnel);
/*    */         }
/* ?4 */         return this;
/*    */       }
/*    */       
/*    */       public HashCode hash()
/*    */       {
/* ?9 */         return AbstractCompositeHashFunction.this.makeHash(hashers);
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\AbstractCompositeHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */