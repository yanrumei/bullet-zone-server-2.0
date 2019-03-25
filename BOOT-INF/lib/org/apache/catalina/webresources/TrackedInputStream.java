/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import org.apache.catalina.TrackedWebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TrackedInputStream
/*     */   extends InputStream
/*     */   implements TrackedWebResource
/*     */ {
/*     */   private final WebResourceRoot root;
/*     */   private final String name;
/*     */   private final InputStream is;
/*     */   private final Exception creation;
/*     */   
/*     */   TrackedInputStream(WebResourceRoot root, String name, InputStream is)
/*     */   {
/*  35 */     this.root = root;
/*  36 */     this.name = name;
/*  37 */     this.is = is;
/*  38 */     this.creation = new Exception();
/*     */     
/*  40 */     root.registerTrackedResource(this);
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/*  45 */     return this.is.read();
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException
/*     */   {
/*  50 */     return this.is.read(b);
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/*  55 */     return this.is.read(b, off, len);
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/*  60 */     return this.is.skip(n);
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/*  65 */     return this.is.available();
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/*  70 */     this.root.deregisterTrackedResource(this);
/*  71 */     this.is.close();
/*     */   }
/*     */   
/*     */   public synchronized void mark(int readlimit)
/*     */   {
/*  76 */     this.is.mark(readlimit);
/*     */   }
/*     */   
/*     */   public synchronized void reset() throws IOException
/*     */   {
/*  81 */     this.is.reset();
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/*  86 */     return this.is.markSupported();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  91 */     return this.name;
/*     */   }
/*     */   
/*     */   public Exception getCreatedBy()
/*     */   {
/*  96 */     return this.creation;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 101 */     StringWriter sw = new StringWriter();
/* 102 */     PrintWriter pw = new PrintWriter(sw);
/* 103 */     sw.append('[');
/* 104 */     sw.append(this.name);
/* 105 */     sw.append(']');
/* 106 */     sw.append(System.lineSeparator());
/* 107 */     this.creation.printStackTrace(pw);
/* 108 */     pw.flush();
/*     */     
/* 110 */     return sw.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\TrackedInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */