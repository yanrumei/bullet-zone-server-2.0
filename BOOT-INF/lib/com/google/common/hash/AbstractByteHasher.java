/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @CanIgnoreReturnValue
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  36 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void update(byte paramByte);
/*     */   
/*     */ 
/*     */ 
/*     */   protected void update(byte[] b)
/*     */   {
/*  47 */     update(b, 0, b.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void update(byte[] b, int off, int len)
/*     */   {
/*  54 */     for (int i = off; i < off + len; i++) {
/*  55 */       update(b[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Hasher putByte(byte b)
/*     */   {
/*  61 */     update(b);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putBytes(byte[] bytes)
/*     */   {
/*  67 */     Preconditions.checkNotNull(bytes);
/*  68 */     update(bytes);
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len)
/*     */   {
/*  74 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  75 */     update(bytes, off, len);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private Hasher update(int bytes)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_0
/*     */     //   2: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   5: invokevirtual 11	java/nio/ByteBuffer:array	()[B
/*     */     //   8: iconst_0
/*     */     //   9: iload_1
/*     */     //   10: invokevirtual 6	com/google/common/hash/AbstractByteHasher:update	([BII)V
/*     */     //   13: aload_0
/*     */     //   14: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   17: invokevirtual 12	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
/*     */     //   20: pop
/*     */     //   21: goto +14 -> 35
/*     */     //   24: astore_2
/*     */     //   25: aload_0
/*     */     //   26: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   29: invokevirtual 12	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
/*     */     //   32: pop
/*     */     //   33: aload_2
/*     */     //   34: athrow
/*     */     //   35: aload_0
/*     */     //   36: areturn
/*     */     // Line number table:
/*     */     //   Java source line #84	-> byte code offset #0
/*     */     //   Java source line #86	-> byte code offset #13
/*     */     //   Java source line #87	-> byte code offset #21
/*     */     //   Java source line #86	-> byte code offset #24
/*     */     //   Java source line #88	-> byte code offset #35
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	37	0	this	AbstractByteHasher
/*     */     //   0	37	1	bytes	int
/*     */     //   24	10	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	13	24	finally
/*     */   }
/*     */   
/*     */   public Hasher putShort(short s)
/*     */   {
/*  93 */     this.scratch.putShort(s);
/*  94 */     return update(2);
/*     */   }
/*     */   
/*     */   public Hasher putInt(int i)
/*     */   {
/*  99 */     this.scratch.putInt(i);
/* 100 */     return update(4);
/*     */   }
/*     */   
/*     */   public Hasher putLong(long l)
/*     */   {
/* 105 */     this.scratch.putLong(l);
/* 106 */     return update(8);
/*     */   }
/*     */   
/*     */   public Hasher putChar(char c)
/*     */   {
/* 111 */     this.scratch.putChar(c);
/* 112 */     return update(2);
/*     */   }
/*     */   
/*     */   public <T> Hasher putObject(T instance, Funnel<? super T> funnel)
/*     */   {
/* 117 */     funnel.funnel(instance, this);
/* 118 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */