/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public abstract class AbstractArchiveResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final AbstractArchiveResourceSet archiveResourceSet;
/*     */   private final String baseUrl;
/*     */   private final JarEntry resource;
/*     */   private final String codeBaseUrl;
/*     */   private final String name;
/*  35 */   private boolean readCerts = false;
/*     */   private Certificate[] certificates;
/*     */   
/*     */   protected AbstractArchiveResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry, String codeBaseUrl)
/*     */   {
/*  40 */     super(archiveResourceSet.getRoot(), webAppPath);
/*  41 */     this.archiveResourceSet = archiveResourceSet;
/*  42 */     this.baseUrl = baseUrl;
/*  43 */     this.resource = jarEntry;
/*  44 */     this.codeBaseUrl = codeBaseUrl;
/*     */     
/*  46 */     String resourceName = this.resource.getName();
/*  47 */     if (resourceName.charAt(resourceName.length() - 1) == '/') {
/*  48 */       resourceName = resourceName.substring(0, resourceName.length() - 1);
/*     */     }
/*  50 */     String internalPath = archiveResourceSet.getInternalPath();
/*  51 */     if ((internalPath.length() > 0) && (resourceName.equals(internalPath
/*  52 */       .subSequence(1, internalPath.length())))) {
/*  53 */       this.name = "";
/*     */     } else {
/*  55 */       int index = resourceName.lastIndexOf('/');
/*  56 */       if (index == -1) {
/*  57 */         this.name = resourceName;
/*     */       } else {
/*  59 */         this.name = resourceName.substring(index + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected AbstractArchiveResourceSet getArchiveResourceSet() {
/*  65 */     return this.archiveResourceSet;
/*     */   }
/*     */   
/*     */   protected final String getBase() {
/*  69 */     return this.archiveResourceSet.getBase();
/*     */   }
/*     */   
/*     */   protected final String getBaseUrl() {
/*  73 */     return this.baseUrl;
/*     */   }
/*     */   
/*     */   protected final JarEntry getResource() {
/*  77 */     return this.resource;
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/*  82 */     return this.resource.getTime();
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isVirtual()
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDirectory()
/*     */   {
/*  97 */     return this.resource.isDirectory();
/*     */   }
/*     */   
/*     */   public boolean isFile()
/*     */   {
/* 102 */     return !this.resource.isDirectory();
/*     */   }
/*     */   
/*     */   public boolean delete()
/*     */   {
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 112 */     return this.name;
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/* 117 */     if (isDirectory()) {
/* 118 */       return -1L;
/*     */     }
/* 120 */     return this.resource.getSize();
/*     */   }
/*     */   
/*     */   public String getCanonicalPath()
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public boolean canRead()
/*     */   {
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public long getCreation()
/*     */   {
/* 135 */     return this.resource.getTime();
/*     */   }
/*     */   
/*     */   public URL getURL()
/*     */   {
/* 140 */     String url = this.baseUrl + this.resource.getName();
/*     */     try {
/* 142 */       return new URL(url);
/*     */     } catch (MalformedURLException e) {
/* 144 */       if (getLog().isDebugEnabled())
/* 145 */         getLog().debug(sm.getString("fileResource.getUrlFail", new Object[] { url }), e);
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   public URL getCodeBase()
/*     */   {
/*     */     try
/*     */     {
/* 154 */       return new URL(this.codeBaseUrl);
/*     */     } catch (MalformedURLException e) {
/* 156 */       if (getLog().isDebugEnabled())
/* 157 */         getLog().debug(sm.getString("fileResource.getUrlFail", new Object[] { this.codeBaseUrl }), e);
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public final byte[] getContent()
/*     */   {
/* 165 */     long len = getContentLength();
/*     */     
/* 167 */     if (len > 2147483647L)
/*     */     {
/* 169 */       throw new ArrayIndexOutOfBoundsException(sm.getString("abstractResource.getContentTooLarge", new Object[] {
/* 170 */         getWebappPath(), 
/* 171 */         Long.valueOf(len) }));
/*     */     }
/*     */     
/* 174 */     if (len < 0L)
/*     */     {
/* 176 */       return null;
/*     */     }
/*     */     
/* 179 */     int size = (int)len;
/* 180 */     byte[] result = new byte[size];
/*     */     
/* 182 */     int pos = 0;
/* 183 */     try { JarInputStreamWrapper jisw = getJarInputStreamWrapper();Throwable localThrowable4 = null;
/* 184 */       try { if (jisw == null)
/*     */         {
/* 186 */           return null;
/*     */         }
/* 188 */         while (pos < size) {
/* 189 */           int n = jisw.read(result, pos, size - pos);
/* 190 */           if (n < 0) {
/*     */             break;
/*     */           }
/* 193 */           pos += n;
/*     */         }
/*     */         
/* 196 */         this.certificates = jisw.getCertificates();
/* 197 */         this.readCerts = true;
/*     */       }
/*     */       catch (Throwable localThrowable6)
/*     */       {
/* 183 */         localThrowable4 = localThrowable6;throw localThrowable6;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */         if (jisw != null) if (localThrowable4 != null) try { jisw.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else jisw.close();
/* 199 */       } } catch (IOException ioe) { if (getLog().isDebugEnabled()) {
/* 200 */         getLog().debug(sm.getString("abstractResource.getContentFail", new Object[] {
/* 201 */           getWebappPath() }), ioe);
/*     */       }
/*     */       
/* 204 */       return null;
/*     */     }
/*     */     
/* 207 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 213 */     if (!this.readCerts)
/*     */     {
/* 215 */       throw new IllegalStateException();
/*     */     }
/* 217 */     return this.certificates;
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */   {
/* 222 */     return this.archiveResourceSet.getManifest();
/*     */   }
/*     */   
/*     */   protected final InputStream doGetInputStream()
/*     */   {
/* 227 */     if (isDirectory()) {
/* 228 */       return null;
/*     */     }
/* 230 */     return getJarInputStreamWrapper();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract JarInputStreamWrapper getJarInputStreamWrapper();
/*     */   
/*     */ 
/*     */   protected class JarInputStreamWrapper
/*     */     extends InputStream
/*     */   {
/*     */     private final JarEntry jarEntry;
/*     */     
/*     */     private final InputStream is;
/*     */     
/* 245 */     private final AtomicBoolean closed = new AtomicBoolean(false);
/*     */     
/*     */     public JarInputStreamWrapper(JarEntry jarEntry, InputStream is)
/*     */     {
/* 249 */       this.jarEntry = jarEntry;
/* 250 */       this.is = is;
/*     */     }
/*     */     
/*     */     public int read()
/*     */       throws IOException
/*     */     {
/* 256 */       return this.is.read();
/*     */     }
/*     */     
/*     */     public int read(byte[] b)
/*     */       throws IOException
/*     */     {
/* 262 */       return this.is.read(b);
/*     */     }
/*     */     
/*     */     public int read(byte[] b, int off, int len)
/*     */       throws IOException
/*     */     {
/* 268 */       return this.is.read(b, off, len);
/*     */     }
/*     */     
/*     */     public long skip(long n)
/*     */       throws IOException
/*     */     {
/* 274 */       return this.is.skip(n);
/*     */     }
/*     */     
/*     */     public int available()
/*     */       throws IOException
/*     */     {
/* 280 */       return this.is.available();
/*     */     }
/*     */     
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 286 */       if (this.closed.compareAndSet(false, true))
/*     */       {
/* 288 */         AbstractArchiveResource.this.archiveResourceSet.closeJarFile();
/*     */       }
/* 290 */       this.is.close();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void mark(int readlimit)
/*     */     {
/* 296 */       this.is.mark(readlimit);
/*     */     }
/*     */     
/*     */     public synchronized void reset()
/*     */       throws IOException
/*     */     {
/* 302 */       this.is.reset();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean markSupported()
/*     */     {
/* 308 */       return this.is.markSupported();
/*     */     }
/*     */     
/*     */     public Certificate[] getCertificates() {
/* 312 */       return this.jarEntry.getCertificates();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractArchiveResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */