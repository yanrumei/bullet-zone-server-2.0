/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ 
/*    */ @CanIgnoreReturnValue
/*    */ abstract class AbstractHasher
/*    */   implements Hasher
/*    */ {
/*    */   public final Hasher putBoolean(boolean b)
/*    */   {
/* 31 */     return putByte((byte)(b ? 1 : 0));
/*    */   }
/*    */   
/*    */   public final Hasher putDouble(double d)
/*    */   {
/* 36 */     return putLong(Double.doubleToRawLongBits(d));
/*    */   }
/*    */   
/*    */   public final Hasher putFloat(float f)
/*    */   {
/* 41 */     return putInt(Float.floatToRawIntBits(f));
/*    */   }
/*    */   
/*    */   public Hasher putUnencodedChars(CharSequence charSequence)
/*    */   {
/* 46 */     int i = 0; for (int len = charSequence.length(); i < len; i++) {
/* 47 */       putChar(charSequence.charAt(i));
/*    */     }
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Hasher putString(CharSequence charSequence, Charset charset)
/*    */   {
/* 54 */     return putBytes(charSequence.toString().getBytes(charset));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\AbstractHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */