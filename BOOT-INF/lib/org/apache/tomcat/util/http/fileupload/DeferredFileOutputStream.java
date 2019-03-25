/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class DeferredFileOutputStream
/*     */   extends ThresholdingOutputStream
/*     */ {
/*     */   private ByteArrayOutputStream memoryOutputStream;
/*     */   private OutputStream currentOutputStream;
/*     */   private File outputFile;
/*     */   private final String prefix;
/*     */   private final String suffix;
/*     */   private final File directory;
/*     */   
/*     */   public DeferredFileOutputStream(int threshold, File outputFile)
/*     */   {
/*  90 */     this(threshold, outputFile, null, null, null, 1024);
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
/*     */   private DeferredFileOutputStream(int threshold, File outputFile, String prefix, String suffix, File directory, int initialBufferSize)
/*     */   {
/* 107 */     super(threshold);
/* 108 */     this.outputFile = outputFile;
/* 109 */     this.prefix = prefix;
/* 110 */     this.suffix = suffix;
/* 111 */     this.directory = directory;
/*     */     
/* 113 */     this.memoryOutputStream = new ByteArrayOutputStream(initialBufferSize);
/* 114 */     this.currentOutputStream = this.memoryOutputStream;
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
/*     */   protected OutputStream getStream()
/*     */     throws IOException
/*     */   {
/* 132 */     return this.currentOutputStream;
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
/*     */   protected void thresholdReached()
/*     */     throws IOException
/*     */   {
/* 147 */     if (this.prefix != null) {
/* 148 */       this.outputFile = File.createTempFile(this.prefix, this.suffix, this.directory);
/*     */     }
/* 150 */     FileUtils.forceMkdirParent(this.outputFile);
/* 151 */     FileOutputStream fos = new FileOutputStream(this.outputFile);
/*     */     try {
/* 153 */       this.memoryOutputStream.writeTo(fos);
/*     */     } catch (IOException e) {
/* 155 */       fos.close();
/* 156 */       throw e;
/*     */     }
/* 158 */     this.currentOutputStream = fos;
/* 159 */     this.memoryOutputStream = null;
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
/*     */   public boolean isInMemory()
/*     */   {
/* 175 */     return !isThresholdExceeded();
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
/*     */   public byte[] getData()
/*     */   {
/* 189 */     if (this.memoryOutputStream != null)
/*     */     {
/* 191 */       return this.memoryOutputStream.toByteArray();
/*     */     }
/* 193 */     return null;
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
/*     */   public File getFile()
/*     */   {
/* 213 */     return this.outputFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 225 */     super.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\DeferredFileOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */