/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import sun.misc.Unsafe;
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
/*     */ final class LittleEndianByteArray
/*     */ {
/*     */   private static final LittleEndianBytes byteArray;
/*     */   
/*     */   static long load64(byte[] input, int offset)
/*     */   {
/*  42 */     assert (input.length >= offset + 8);
/*     */     
/*  44 */     return byteArray.getLongLittleEndian(input, offset);
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
/*     */   static long load64Safely(byte[] input, int offset, int length)
/*     */   {
/*  58 */     long result = 0L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  63 */     int limit = Math.min(length, 8);
/*  64 */     for (int i = 0; i < limit; i++)
/*     */     {
/*  66 */       result |= (input[(offset + i)] & 0xFF) << i * 8;
/*     */     }
/*  68 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void store64(byte[] sink, int offset, long value)
/*     */   {
/*  80 */     assert ((offset >= 0) && (offset + 8 <= sink.length));
/*     */     
/*  82 */     byteArray.putLongLittleEndian(sink, offset, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int load32(byte[] source, int offset)
/*     */   {
/*  94 */     return source[offset] & 0xFF | (source[(offset + 1)] & 0xFF) << 8 | (source[(offset + 2)] & 0xFF) << 16 | (source[(offset + 3)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean usingUnsafe()
/*     */   {
/* 106 */     return byteArray instanceof UnsafeByteArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface LittleEndianBytes
/*     */   {
/*     */     public abstract long getLongLittleEndian(byte[] paramArrayOfByte, int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract void putLongLittleEndian(byte[] paramArrayOfByte, int paramInt, long paramLong);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract enum UnsafeByteArray
/*     */     implements LittleEndianByteArray.LittleEndianBytes
/*     */   {
/* 128 */     UNSAFE_LITTLE_ENDIAN, 
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
/* 139 */     UNSAFE_BIG_ENDIAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static final Unsafe theUnsafe;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static final int BYTE_ARRAY_BASE_OFFSET;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private UnsafeByteArray() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static Unsafe getUnsafe()
/*     */     {
/*     */       try
/*     */       {
/* 169 */         return Unsafe.getUnsafe();
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */         try {
/* 174 */           (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Unsafe run() throws Exception
/*     */             {
/* 178 */               Class<Unsafe> k = Unsafe.class;
/* 179 */               for (Field f : k.getDeclaredFields()) {
/* 180 */                 f.setAccessible(true);
/* 181 */                 Object x = f.get(null);
/* 182 */                 if (k.isInstance(x)) {
/* 183 */                   return (Unsafe)k.cast(x);
/*     */                 }
/*     */               }
/* 186 */               throw new NoSuchFieldError("the Unsafe");
/*     */             }
/*     */           });
/*     */         } catch (PrivilegedActionException e) {
/* 190 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 195 */     static { theUnsafe = getUnsafe();
/* 196 */       BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
/*     */       
/*     */ 
/* 199 */       if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
/* 200 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract enum JavaLittleEndianBytes
/*     */     implements LittleEndianByteArray.LittleEndianBytes
/*     */   {
/* 209 */     INSTANCE;
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
/*     */     private JavaLittleEndianBytes() {}
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
/*     */   static
/*     */   {
/* 234 */     LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 246 */       String arch = System.getProperty("os.arch");
/* 247 */       if (("amd64".equals(arch)) || ("aarch64".equals(arch)))
/*     */       {
/* 249 */         theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/*     */ 
/* 256 */     byteArray = theGetter;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\LittleEndianByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */