/*     */ package org.springframework.boot.loader.data;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Semaphore;
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
/*     */ public class RandomAccessDataFile
/*     */   implements RandomAccessData
/*     */ {
/*     */   private static final int DEFAULT_CONCURRENT_READS = 4;
/*     */   private final File file;
/*     */   private final FilePool filePool;
/*     */   private final long offset;
/*     */   private final long length;
/*     */   
/*     */   public RandomAccessDataFile(File file)
/*     */   {
/*  51 */     this(file, 4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RandomAccessDataFile(File file, int concurrentReads)
/*     */   {
/*  63 */     if (file == null) {
/*  64 */       throw new IllegalArgumentException("File must not be null");
/*     */     }
/*  66 */     if (!file.exists()) {
/*  67 */       throw new IllegalArgumentException(String.format("File %s must exist", new Object[] {file
/*  68 */         .getAbsolutePath() }));
/*     */     }
/*  70 */     this.file = file;
/*  71 */     this.filePool = new FilePool(file, concurrentReads);
/*  72 */     this.offset = 0L;
/*  73 */     this.length = file.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RandomAccessDataFile(File file, FilePool pool, long offset, long length)
/*     */   {
/*  84 */     this.file = file;
/*  85 */     this.filePool = pool;
/*  86 */     this.offset = offset;
/*  87 */     this.length = length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */   {
/*  95 */     return this.file;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream(RandomAccessData.ResourceAccess access) throws IOException
/*     */   {
/* 100 */     return new DataInputStream(access);
/*     */   }
/*     */   
/*     */   public RandomAccessData getSubsection(long offset, long length)
/*     */   {
/* 105 */     if ((offset < 0L) || (length < 0L) || (offset + length > this.length)) {
/* 106 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 108 */     return new RandomAccessDataFile(this.file, this.filePool, this.offset + offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 114 */     return this.length;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 118 */     this.filePool.close();
/*     */   }
/*     */   
/*     */ 
/*     */   private class DataInputStream
/*     */     extends InputStream
/*     */   {
/*     */     private RandomAccessFile file;
/*     */     
/*     */     private int position;
/*     */     
/*     */     DataInputStream(RandomAccessData.ResourceAccess access)
/*     */       throws IOException
/*     */     {
/* 132 */       if (access == RandomAccessData.ResourceAccess.ONCE) {
/* 133 */         this.file = new RandomAccessFile(RandomAccessDataFile.this.file, "r");
/* 134 */         this.file.seek(RandomAccessDataFile.this.offset);
/*     */       }
/*     */     }
/*     */     
/*     */     public int read() throws IOException
/*     */     {
/* 140 */       return doRead(null, 0, 1);
/*     */     }
/*     */     
/*     */     public int read(byte[] b) throws IOException
/*     */     {
/* 145 */       return read(b, 0, b == null ? 0 : b.length);
/*     */     }
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException
/*     */     {
/* 150 */       if (b == null) {
/* 151 */         throw new NullPointerException("Bytes must not be null");
/*     */       }
/* 153 */       return doRead(b, off, len);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int doRead(byte[] b, int off, int len)
/*     */       throws IOException
/*     */     {
/* 166 */       if (len == 0) {
/* 167 */         return 0;
/*     */       }
/* 169 */       int cappedLen = cap(len);
/* 170 */       if (cappedLen <= 0) {
/* 171 */         return -1;
/*     */       }
/* 173 */       RandomAccessFile file = this.file;
/*     */       try {
/* 175 */         if (file == null) {
/* 176 */           file = RandomAccessDataFile.this.filePool.acquire();
/* 177 */           file.seek(RandomAccessDataFile.this.offset + this.position); }
/*     */         int rtn;
/* 179 */         if (b == null) {
/* 180 */           rtn = file.read();
/* 181 */           moveOn(rtn == -1 ? 0 : 1);
/* 182 */           return rtn;
/*     */         }
/*     */         
/* 185 */         return (int)moveOn(file.read(b, off, cappedLen));
/*     */       }
/*     */       finally
/*     */       {
/* 189 */         if ((this.file == null) && (file != null)) {
/* 190 */           RandomAccessDataFile.this.filePool.release(file);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public long skip(long n) throws IOException
/*     */     {
/* 197 */       return n <= 0L ? 0L : moveOn(cap(n));
/*     */     }
/*     */     
/*     */     public void close() throws IOException
/*     */     {
/* 202 */       if (this.file != null) {
/* 203 */         this.file.close();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private int cap(long n)
/*     */     {
/* 214 */       return (int)Math.min(RandomAccessDataFile.this.length - this.position, n);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private long moveOn(int amount)
/*     */     {
/* 223 */       this.position += amount;
/* 224 */       return amount;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class FilePool
/*     */   {
/*     */     private final File file;
/*     */     
/*     */ 
/*     */     private final int size;
/*     */     
/*     */     private final Semaphore available;
/*     */     
/*     */     private final Queue<RandomAccessFile> files;
/*     */     
/*     */ 
/*     */     FilePool(File file, int size)
/*     */     {
/* 244 */       this.file = file;
/* 245 */       this.size = size;
/* 246 */       this.available = new Semaphore(size);
/* 247 */       this.files = new ConcurrentLinkedQueue();
/*     */     }
/*     */     
/*     */     public RandomAccessFile acquire() throws IOException {
/* 251 */       this.available.acquireUninterruptibly();
/* 252 */       RandomAccessFile file = (RandomAccessFile)this.files.poll();
/* 253 */       if (file != null) {
/* 254 */         return file;
/*     */       }
/* 256 */       return new RandomAccessFile(this.file, "r");
/*     */     }
/*     */     
/*     */     public void release(RandomAccessFile file) {
/* 260 */       this.files.add(file);
/* 261 */       this.available.release();
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 6	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:available	Ljava/util/concurrent/Semaphore;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:size	I
/*     */       //   8: invokevirtual 17	java/util/concurrent/Semaphore:acquireUninterruptibly	(I)V
/*     */       //   11: aload_0
/*     */       //   12: getfield 9	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:files	Ljava/util/Queue;
/*     */       //   15: invokeinterface 11 1 0
/*     */       //   20: checkcast 12	java/io/RandomAccessFile
/*     */       //   23: astore_1
/*     */       //   24: aload_1
/*     */       //   25: ifnull +23 -> 48
/*     */       //   28: aload_1
/*     */       //   29: invokevirtual 18	java/io/RandomAccessFile:close	()V
/*     */       //   32: aload_0
/*     */       //   33: getfield 9	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:files	Ljava/util/Queue;
/*     */       //   36: invokeinterface 11 1 0
/*     */       //   41: checkcast 12	java/io/RandomAccessFile
/*     */       //   44: astore_1
/*     */       //   45: goto -21 -> 24
/*     */       //   48: aload_0
/*     */       //   49: getfield 6	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:available	Ljava/util/concurrent/Semaphore;
/*     */       //   52: aload_0
/*     */       //   53: getfield 3	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:size	I
/*     */       //   56: invokevirtual 19	java/util/concurrent/Semaphore:release	(I)V
/*     */       //   59: goto +17 -> 76
/*     */       //   62: astore_2
/*     */       //   63: aload_0
/*     */       //   64: getfield 6	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:available	Ljava/util/concurrent/Semaphore;
/*     */       //   67: aload_0
/*     */       //   68: getfield 3	org/springframework/boot/loader/data/RandomAccessDataFile$FilePool:size	I
/*     */       //   71: invokevirtual 19	java/util/concurrent/Semaphore:release	(I)V
/*     */       //   74: aload_2
/*     */       //   75: athrow
/*     */       //   76: return
/*     */       // Line number table:
/*     */       //   Java source line #265	-> byte code offset #0
/*     */       //   Java source line #267	-> byte code offset #11
/*     */       //   Java source line #268	-> byte code offset #24
/*     */       //   Java source line #269	-> byte code offset #28
/*     */       //   Java source line #270	-> byte code offset #32
/*     */       //   Java source line #274	-> byte code offset #48
/*     */       //   Java source line #275	-> byte code offset #59
/*     */       //   Java source line #274	-> byte code offset #62
/*     */       //   Java source line #276	-> byte code offset #76
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	77	0	this	FilePool
/*     */       //   23	22	1	pooledFile	RandomAccessFile
/*     */       //   62	13	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	48	62	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\data\RandomAccessDataFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */