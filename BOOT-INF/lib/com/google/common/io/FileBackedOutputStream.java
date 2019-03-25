/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private OutputStream out;
/*     */   private MemoryOutput memory;
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     byte[] getBuffer()
/*     */     {
/*  53 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  57 */       return this.count;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   synchronized File getFile()
/*     */   {
/*  64 */     return this.file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileBackedOutputStream(int fileThreshold)
/*     */   {
/*  74 */     this(fileThreshold, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize)
/*     */   {
/*  86 */     this.fileThreshold = fileThreshold;
/*  87 */     this.resetOnFinalize = resetOnFinalize;
/*  88 */     this.memory = new MemoryOutput(null);
/*  89 */     this.out = this.memory;
/*     */     
/*  91 */     if (resetOnFinalize) {
/*  92 */       this.source = new ByteSource()
/*     */       {
/*     */         public InputStream openStream() throws IOException
/*     */         {
/*  96 */           return FileBackedOutputStream.this.openInputStream();
/*     */         }
/*     */         
/*     */         protected void finalize()
/*     */         {
/*     */           try {
/* 102 */             FileBackedOutputStream.this.reset();
/*     */           } catch (Throwable t) {
/* 104 */             t.printStackTrace(System.err);
/*     */           }
/*     */         }
/*     */       };
/*     */     } else {
/* 109 */       this.source = new ByteSource()
/*     */       {
/*     */         public InputStream openStream() throws IOException
/*     */         {
/* 113 */           return FileBackedOutputStream.this.openInputStream();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteSource asByteSource()
/*     */   {
/* 125 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 129 */     if (this.file != null) {
/* 130 */       return new FileInputStream(this.file);
/*     */     }
/* 132 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 22	com/google/common/io/FileBackedOutputStream:close	()V
/*     */     //   4: aload_0
/*     */     //   5: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   8: ifnonnull +18 -> 26
/*     */     //   11: aload_0
/*     */     //   12: new 7	com/google/common/io/FileBackedOutputStream$MemoryOutput
/*     */     //   15: dup
/*     */     //   16: aconst_null
/*     */     //   17: invokespecial 8	com/google/common/io/FileBackedOutputStream$MemoryOutput:<init>	(Lcom/google/common/io/FileBackedOutputStream$1;)V
/*     */     //   20: putfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   23: goto +10 -> 33
/*     */     //   26: aload_0
/*     */     //   27: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   30: invokevirtual 23	com/google/common/io/FileBackedOutputStream$MemoryOutput:reset	()V
/*     */     //   33: aload_0
/*     */     //   34: aload_0
/*     */     //   35: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   38: putfield 10	com/google/common/io/FileBackedOutputStream:out	Ljava/io/OutputStream;
/*     */     //   41: aload_0
/*     */     //   42: getfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   45: ifnull +141 -> 186
/*     */     //   48: aload_0
/*     */     //   49: getfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   52: astore_1
/*     */     //   53: aload_0
/*     */     //   54: aconst_null
/*     */     //   55: putfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   58: aload_1
/*     */     //   59: invokevirtual 24	java/io/File:delete	()Z
/*     */     //   62: ifne +30 -> 92
/*     */     //   65: new 25	java/io/IOException
/*     */     //   68: dup
/*     */     //   69: new 26	java/lang/StringBuilder
/*     */     //   72: dup
/*     */     //   73: invokespecial 27	java/lang/StringBuilder:<init>	()V
/*     */     //   76: ldc 28
/*     */     //   78: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   81: aload_1
/*     */     //   82: invokevirtual 30	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   85: invokevirtual 31	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   88: invokespecial 32	java/io/IOException:<init>	(Ljava/lang/String;)V
/*     */     //   91: athrow
/*     */     //   92: goto +94 -> 186
/*     */     //   95: astore_2
/*     */     //   96: aload_0
/*     */     //   97: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   100: ifnonnull +18 -> 118
/*     */     //   103: aload_0
/*     */     //   104: new 7	com/google/common/io/FileBackedOutputStream$MemoryOutput
/*     */     //   107: dup
/*     */     //   108: aconst_null
/*     */     //   109: invokespecial 8	com/google/common/io/FileBackedOutputStream$MemoryOutput:<init>	(Lcom/google/common/io/FileBackedOutputStream$1;)V
/*     */     //   112: putfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   115: goto +10 -> 125
/*     */     //   118: aload_0
/*     */     //   119: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   122: invokevirtual 23	com/google/common/io/FileBackedOutputStream$MemoryOutput:reset	()V
/*     */     //   125: aload_0
/*     */     //   126: aload_0
/*     */     //   127: getfield 9	com/google/common/io/FileBackedOutputStream:memory	Lcom/google/common/io/FileBackedOutputStream$MemoryOutput;
/*     */     //   130: putfield 10	com/google/common/io/FileBackedOutputStream:out	Ljava/io/OutputStream;
/*     */     //   133: aload_0
/*     */     //   134: getfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   137: ifnull +47 -> 184
/*     */     //   140: aload_0
/*     */     //   141: getfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   144: astore_3
/*     */     //   145: aload_0
/*     */     //   146: aconst_null
/*     */     //   147: putfield 2	com/google/common/io/FileBackedOutputStream:file	Ljava/io/File;
/*     */     //   150: aload_3
/*     */     //   151: invokevirtual 24	java/io/File:delete	()Z
/*     */     //   154: ifne +30 -> 184
/*     */     //   157: new 25	java/io/IOException
/*     */     //   160: dup
/*     */     //   161: new 26	java/lang/StringBuilder
/*     */     //   164: dup
/*     */     //   165: invokespecial 27	java/lang/StringBuilder:<init>	()V
/*     */     //   168: ldc 28
/*     */     //   170: invokevirtual 29	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   173: aload_3
/*     */     //   174: invokevirtual 30	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   177: invokevirtual 31	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   180: invokespecial 32	java/io/IOException:<init>	(Ljava/lang/String;)V
/*     */     //   183: athrow
/*     */     //   184: aload_2
/*     */     //   185: athrow
/*     */     //   186: return
/*     */     // Line number table:
/*     */     //   Java source line #144	-> byte code offset #0
/*     */     //   Java source line #146	-> byte code offset #4
/*     */     //   Java source line #147	-> byte code offset #11
/*     */     //   Java source line #149	-> byte code offset #26
/*     */     //   Java source line #151	-> byte code offset #33
/*     */     //   Java source line #152	-> byte code offset #41
/*     */     //   Java source line #153	-> byte code offset #48
/*     */     //   Java source line #154	-> byte code offset #53
/*     */     //   Java source line #155	-> byte code offset #58
/*     */     //   Java source line #156	-> byte code offset #65
/*     */     //   Java source line #158	-> byte code offset #92
/*     */     //   Java source line #146	-> byte code offset #95
/*     */     //   Java source line #147	-> byte code offset #103
/*     */     //   Java source line #149	-> byte code offset #118
/*     */     //   Java source line #151	-> byte code offset #125
/*     */     //   Java source line #152	-> byte code offset #133
/*     */     //   Java source line #153	-> byte code offset #140
/*     */     //   Java source line #154	-> byte code offset #145
/*     */     //   Java source line #155	-> byte code offset #150
/*     */     //   Java source line #156	-> byte code offset #157
/*     */     //   Java source line #158	-> byte code offset #184
/*     */     //   Java source line #160	-> byte code offset #186
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	187	0	this	FileBackedOutputStream
/*     */     //   52	30	1	deleteMe	File
/*     */     //   95	90	2	localObject	Object
/*     */     //   144	30	3	deleteMe	File
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	95	finally
/*     */   }
/*     */   
/*     */   public synchronized void write(int b)
/*     */     throws IOException
/*     */   {
/* 164 */     update(1);
/* 165 */     this.out.write(b);
/*     */   }
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException
/*     */   {
/* 170 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException
/*     */   {
/* 175 */     update(len);
/* 176 */     this.out.write(b, off, len);
/*     */   }
/*     */   
/*     */   public synchronized void close() throws IOException
/*     */   {
/* 181 */     this.out.close();
/*     */   }
/*     */   
/*     */   public synchronized void flush() throws IOException
/*     */   {
/* 186 */     this.out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void update(int len)
/*     */     throws IOException
/*     */   {
/* 194 */     if ((this.file == null) && (this.memory.getCount() + len > this.fileThreshold)) {
/* 195 */       File temp = File.createTempFile("FileBackedOutputStream", null);
/* 196 */       if (this.resetOnFinalize)
/*     */       {
/*     */ 
/* 199 */         temp.deleteOnExit();
/*     */       }
/* 201 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 202 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 203 */       transfer.flush();
/*     */       
/*     */ 
/* 206 */       this.out = transfer;
/* 207 */       this.file = temp;
/* 208 */       this.memory = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\FileBackedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */