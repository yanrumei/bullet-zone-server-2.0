/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ByteSource
/*     */ {
/*     */   public CharSource asCharSource(Charset charset)
/*     */   {
/*  79 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream openBufferedStream()
/*     */     throws IOException
/*     */   {
/* 105 */     InputStream in = openStream();
/* 106 */     return (in instanceof BufferedInputStream) ? (BufferedInputStream)in : new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length)
/*     */   {
/* 121 */     return new SlicedByteSource(offset, length);
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
/*     */   public boolean isEmpty()
/*     */     throws IOException
/*     */   {
/* 138 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 139 */     if ((sizeIfKnown.isPresent()) && (((Long)sizeIfKnown.get()).longValue() == 0L)) {
/* 140 */       return true;
/*     */     }
/* 142 */     Closer closer = Closer.create();
/*     */     try {
/* 144 */       InputStream in = (InputStream)closer.register(openStream());
/* 145 */       return in.read() == -1;
/*     */     } catch (Throwable e) {
/* 147 */       throw closer.rethrow(e);
/*     */     } finally {
/* 149 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public Optional<Long> sizeIfKnown()
/*     */   {
/* 169 */     return Optional.absent();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long size()
/*     */     throws IOException
/*     */   {
/* 192 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 193 */     if (sizeIfKnown.isPresent()) {
/* 194 */       return ((Long)sizeIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 197 */     Closer closer = Closer.create();
/*     */     long l;
/* 199 */     try { InputStream in = (InputStream)closer.register(openStream());
/* 200 */       return countBySkipping(in);
/*     */     }
/*     */     catch (IOException localIOException) {}finally
/*     */     {
/* 204 */       closer.close();
/*     */     }
/*     */     
/* 207 */     closer = Closer.create();
/*     */     try {
/* 209 */       InputStream in = (InputStream)closer.register(openStream());
/* 210 */       return ByteStreams.exhaust(in);
/*     */     } catch (Throwable e) {
/* 212 */       throw closer.rethrow(e);
/*     */     } finally {
/* 214 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private long countBySkipping(InputStream in)
/*     */     throws IOException
/*     */   {
/* 223 */     long count = 0L;
/*     */     long skipped;
/* 225 */     while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
/* 226 */       count += skipped;
/*     */     }
/* 228 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(OutputStream output)
/*     */     throws IOException
/*     */   {
/* 241 */     Preconditions.checkNotNull(output);
/*     */     
/* 243 */     Closer closer = Closer.create();
/*     */     try {
/* 245 */       InputStream in = (InputStream)closer.register(openStream());
/* 246 */       return ByteStreams.copy(in, output);
/*     */     } catch (Throwable e) {
/* 248 */       throw closer.rethrow(e);
/*     */     } finally {
/* 250 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(ByteSink sink)
/*     */     throws IOException
/*     */   {
/* 263 */     Preconditions.checkNotNull(sink);
/*     */     
/* 265 */     Closer closer = Closer.create();
/*     */     try {
/* 267 */       InputStream in = (InputStream)closer.register(openStream());
/* 268 */       OutputStream out = (OutputStream)closer.register(sink.openStream());
/* 269 */       return ByteStreams.copy(in, out);
/*     */     } catch (Throwable e) {
/* 271 */       throw closer.rethrow(e);
/*     */     } finally {
/* 273 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] read()
/*     */     throws IOException
/*     */   {
/* 283 */     Closer closer = Closer.create();
/*     */     try {
/* 285 */       InputStream in = (InputStream)closer.register(openStream());
/* 286 */       return ByteStreams.toByteArray(in);
/*     */     } catch (Throwable e) {
/* 288 */       throw closer.rethrow(e);
/*     */     } finally {
/* 290 */       closer.close();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T read(ByteProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 306 */     Preconditions.checkNotNull(processor);
/*     */     
/* 308 */     Closer closer = Closer.create();
/*     */     try {
/* 310 */       InputStream in = (InputStream)closer.register(openStream());
/* 311 */       return (T)ByteStreams.readBytes(in, processor);
/*     */     } catch (Throwable e) {
/* 313 */       throw closer.rethrow(e);
/*     */     } finally {
/* 315 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashCode hash(HashFunction hashFunction)
/*     */     throws IOException
/*     */   {
/* 325 */     Hasher hasher = hashFunction.newHasher();
/* 326 */     copyTo(Funnels.asOutputStream(hasher));
/* 327 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean contentEquals(ByteSource other)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic 29	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: invokestatic 39	com/google/common/io/ByteStreams:createBuffer	()[B
/*     */     //   8: astore_2
/*     */     //   9: invokestatic 39	com/google/common/io/ByteStreams:createBuffer	()[B
/*     */     //   12: astore_3
/*     */     //   13: invokestatic 14	com/google/common/io/Closer:create	()Lcom/google/common/io/Closer;
/*     */     //   16: astore 4
/*     */     //   18: aload 4
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
/*     */     //   24: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
/*     */     //   27: checkcast 16	java/io/InputStream
/*     */     //   30: astore 5
/*     */     //   32: aload 4
/*     */     //   34: aload_1
/*     */     //   35: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
/*     */     //   38: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
/*     */     //   41: checkcast 16	java/io/InputStream
/*     */     //   44: astore 6
/*     */     //   46: aload 5
/*     */     //   48: aload_2
/*     */     //   49: iconst_0
/*     */     //   50: aload_2
/*     */     //   51: arraylength
/*     */     //   52: invokestatic 40	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
/*     */     //   55: istore 7
/*     */     //   57: aload 6
/*     */     //   59: aload_3
/*     */     //   60: iconst_0
/*     */     //   61: aload_3
/*     */     //   62: arraylength
/*     */     //   63: invokestatic 40	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
/*     */     //   66: istore 8
/*     */     //   68: iload 7
/*     */     //   70: iload 8
/*     */     //   72: if_icmpne +11 -> 83
/*     */     //   75: aload_2
/*     */     //   76: aload_3
/*     */     //   77: invokestatic 41	java/util/Arrays:equals	([B[B)Z
/*     */     //   80: ifne +14 -> 94
/*     */     //   83: iconst_0
/*     */     //   84: istore 9
/*     */     //   86: aload 4
/*     */     //   88: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   91: iload 9
/*     */     //   93: ireturn
/*     */     //   94: iload 7
/*     */     //   96: aload_2
/*     */     //   97: arraylength
/*     */     //   98: if_icmpeq +14 -> 112
/*     */     //   101: iconst_1
/*     */     //   102: istore 9
/*     */     //   104: aload 4
/*     */     //   106: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   109: iload 9
/*     */     //   111: ireturn
/*     */     //   112: goto -66 -> 46
/*     */     //   115: astore 5
/*     */     //   117: aload 4
/*     */     //   119: aload 5
/*     */     //   121: invokevirtual 20	com/google/common/io/Closer:rethrow	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
/*     */     //   124: athrow
/*     */     //   125: astore 10
/*     */     //   127: aload 4
/*     */     //   129: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   132: aload 10
/*     */     //   134: athrow
/*     */     // Line number table:
/*     */     //   Java source line #337	-> byte code offset #0
/*     */     //   Java source line #339	-> byte code offset #5
/*     */     //   Java source line #340	-> byte code offset #9
/*     */     //   Java source line #342	-> byte code offset #13
/*     */     //   Java source line #344	-> byte code offset #18
/*     */     //   Java source line #345	-> byte code offset #32
/*     */     //   Java source line #347	-> byte code offset #46
/*     */     //   Java source line #348	-> byte code offset #57
/*     */     //   Java source line #349	-> byte code offset #68
/*     */     //   Java source line #350	-> byte code offset #83
/*     */     //   Java source line #358	-> byte code offset #86
/*     */     //   Java source line #350	-> byte code offset #91
/*     */     //   Java source line #351	-> byte code offset #94
/*     */     //   Java source line #352	-> byte code offset #101
/*     */     //   Java source line #358	-> byte code offset #104
/*     */     //   Java source line #352	-> byte code offset #109
/*     */     //   Java source line #354	-> byte code offset #112
/*     */     //   Java source line #355	-> byte code offset #115
/*     */     //   Java source line #356	-> byte code offset #117
/*     */     //   Java source line #358	-> byte code offset #125
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	135	0	this	ByteSource
/*     */     //   0	135	1	other	ByteSource
/*     */     //   8	89	2	buf1	byte[]
/*     */     //   12	65	3	buf2	byte[]
/*     */     //   16	112	4	closer	Closer
/*     */     //   30	17	5	in1	InputStream
/*     */     //   115	5	5	e	Throwable
/*     */     //   44	14	6	in2	InputStream
/*     */     //   55	40	7	read1	int
/*     */     //   66	5	8	read2	int
/*     */     //   84	26	9	bool	boolean
/*     */     //   125	8	10	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   18	86	115	java/lang/Throwable
/*     */     //   94	104	115	java/lang/Throwable
/*     */     //   112	115	115	java/lang/Throwable
/*     */     //   18	86	125	finally
/*     */     //   94	104	125	finally
/*     */     //   112	127	125	finally
/*     */   }
/*     */   
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources)
/*     */   {
/* 374 */     return new ConcatenatedByteSource(sources);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources)
/*     */   {
/* 396 */     return concat(ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources)
/*     */   {
/* 412 */     return concat(ImmutableList.copyOf(sources));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource wrap(byte[] b)
/*     */   {
/* 422 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource empty()
/*     */   {
/* 431 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */   class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     final Charset charset;
/*     */     
/*     */     AsCharSource(Charset charset)
/*     */     {
/* 442 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public ByteSource asByteSource(Charset charset)
/*     */     {
/* 447 */       if (charset.equals(this.charset)) {
/* 448 */         return ByteSource.this;
/*     */       }
/* 450 */       return super.asByteSource(charset);
/*     */     }
/*     */     
/*     */     public Reader openStream() throws IOException
/*     */     {
/* 455 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 460 */       return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class SlicedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final long offset;
/*     */     final long length;
/*     */     
/*     */     SlicedByteSource(long offset, long length)
/*     */     {
/* 473 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 474 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/* 475 */       this.offset = offset;
/* 476 */       this.length = length;
/*     */     }
/*     */     
/*     */     public InputStream openStream() throws IOException
/*     */     {
/* 481 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException
/*     */     {
/* 486 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 490 */       if (this.offset > 0L)
/*     */       {
/*     */         try {
/* 493 */           skipped = ByteStreams.skipUpTo(in, this.offset);
/*     */         } catch (Throwable e) { long skipped;
/* 495 */           Closer closer = Closer.create();
/* 496 */           closer.register(in);
/*     */           try {
/* 498 */             throw closer.rethrow(e);
/*     */           } finally {
/* 500 */             closer.close();
/*     */           }
/*     */         }
/*     */         long skipped;
/* 504 */         if (skipped < this.offset)
/*     */         {
/* 506 */           in.close();
/* 507 */           return new ByteArrayInputStream(new byte[0]);
/*     */         }
/*     */       }
/* 510 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */     
/*     */     public ByteSource slice(long offset, long length)
/*     */     {
/* 515 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 516 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/* 517 */       long maxLength = this.length - offset;
/* 518 */       return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */     
/*     */     public boolean isEmpty() throws IOException
/*     */     {
/* 523 */       return (this.length == 0L) || (super.isEmpty());
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 528 */       Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
/* 529 */       if (optionalUnslicedSize.isPresent()) {
/* 530 */         long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
/* 531 */         long off = Math.min(this.offset, unslicedSize);
/* 532 */         return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - off)));
/*     */       }
/* 534 */       return Optional.absent();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 539 */       return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteArrayByteSource extends ByteSource
/*     */   {
/*     */     final byte[] bytes;
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes) {
/* 550 */       this(bytes, 0, bytes.length);
/*     */     }
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes, int offset, int length)
/*     */     {
/* 555 */       this.bytes = bytes;
/* 556 */       this.offset = offset;
/* 557 */       this.length = length;
/*     */     }
/*     */     
/*     */     public InputStream openStream()
/*     */     {
/* 562 */       return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*     */     }
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException
/*     */     {
/* 567 */       return openStream();
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 572 */       return this.length == 0;
/*     */     }
/*     */     
/*     */     public long size()
/*     */     {
/* 577 */       return this.length;
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 582 */       return Optional.of(Long.valueOf(this.length));
/*     */     }
/*     */     
/*     */     public byte[] read()
/*     */     {
/* 587 */       return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
/*     */     }
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException
/*     */     {
/* 592 */       output.write(this.bytes, this.offset, this.length);
/* 593 */       return this.length;
/*     */     }
/*     */     
/*     */     public <T> T read(ByteProcessor<T> processor)
/*     */       throws IOException
/*     */     {
/* 599 */       processor.processBytes(this.bytes, this.offset, this.length);
/* 600 */       return (T)processor.getResult();
/*     */     }
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException
/*     */     {
/* 605 */       return hashFunction.hashBytes(this.bytes, this.offset, this.length);
/*     */     }
/*     */     
/*     */     public ByteSource slice(long offset, long length)
/*     */     {
/* 610 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 611 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/*     */       
/* 613 */       offset = Math.min(offset, this.length);
/* 614 */       length = Math.min(length, this.length - offset);
/* 615 */       int newOffset = this.offset + (int)offset;
/* 616 */       return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 621 */       return 
/* 622 */         "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource extends ByteSource.ByteArrayByteSource
/*     */   {
/* 628 */     static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     EmptyByteSource() {
/* 631 */       super();
/*     */     }
/*     */     
/*     */     public CharSource asCharSource(Charset charset)
/*     */     {
/* 636 */       Preconditions.checkNotNull(charset);
/* 637 */       return CharSource.empty();
/*     */     }
/*     */     
/*     */     public byte[] read()
/*     */     {
/* 642 */       return this.bytes;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 647 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource extends ByteSource
/*     */   {
/*     */     final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 656 */       this.sources = ((Iterable)Preconditions.checkNotNull(sources));
/*     */     }
/*     */     
/*     */     public InputStream openStream() throws IOException
/*     */     {
/* 661 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */     
/*     */     public boolean isEmpty() throws IOException
/*     */     {
/* 666 */       for (ByteSource source : this.sources) {
/* 667 */         if (!source.isEmpty()) {
/* 668 */           return false;
/*     */         }
/*     */       }
/* 671 */       return true;
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 676 */       long result = 0L;
/* 677 */       for (ByteSource source : this.sources) {
/* 678 */         Optional<Long> sizeIfKnown = source.sizeIfKnown();
/* 679 */         if (!sizeIfKnown.isPresent()) {
/* 680 */           return Optional.absent();
/*     */         }
/* 682 */         result += ((Long)sizeIfKnown.get()).longValue();
/*     */       }
/* 684 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */     
/*     */     public long size() throws IOException
/*     */     {
/* 689 */       long result = 0L;
/* 690 */       for (ByteSource source : this.sources) {
/* 691 */         result += source.size();
/*     */       }
/* 693 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 698 */       return "ByteSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */