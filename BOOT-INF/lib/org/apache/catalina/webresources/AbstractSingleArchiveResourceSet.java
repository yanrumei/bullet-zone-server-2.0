/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.tomcat.util.buf.UriUtil;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
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
/*     */ public abstract class AbstractSingleArchiveResourceSet
/*     */   extends AbstractArchiveResourceSet
/*     */ {
/*     */   private volatile Boolean multiRelease;
/*     */   
/*     */   public AbstractSingleArchiveResourceSet() {}
/*     */   
/*     */   public AbstractSingleArchiveResourceSet(WebResourceRoot root, String webAppMount, String base, String internalPath)
/*     */     throws IllegalArgumentException
/*     */   {
/*  49 */     setRoot(root);
/*  50 */     setWebAppMount(webAppMount);
/*  51 */     setBase(base);
/*  52 */     setInternalPath(internalPath);
/*     */     
/*  54 */     if (getRoot().getState().isAvailable()) {
/*     */       try {
/*  56 */         start();
/*     */       } catch (LifecycleException e) {
/*  58 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected HashMap<String, JarEntry> getArchiveEntries(boolean single)
/*     */   {
/*  66 */     synchronized (this.archiveLock) {
/*  67 */       if ((this.archiveEntries == null) && (!single)) {
/*  68 */         JarFile jarFile = null;
/*  69 */         this.archiveEntries = new HashMap();
/*     */         try {
/*  71 */           jarFile = openJarFile();
/*  72 */           Enumeration<JarEntry> entries = jarFile.entries();
/*  73 */           while (entries.hasMoreElements()) {
/*  74 */             JarEntry entry = (JarEntry)entries.nextElement();
/*  75 */             this.archiveEntries.put(entry.getName(), entry);
/*     */           }
/*     */         }
/*     */         catch (IOException ioe) {
/*  79 */           this.archiveEntries = null;
/*  80 */           throw new IllegalStateException(ioe);
/*     */         } finally {
/*  82 */           if (jarFile != null) {
/*  83 */             closeJarFile();
/*     */           }
/*     */         }
/*     */       }
/*  87 */       return this.archiveEntries;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected JarEntry getArchiveEntry(String pathInArchive)
/*     */   {
/*  94 */     JarFile jarFile = null;
/*     */     try {
/*  96 */       jarFile = openJarFile();
/*  97 */       return jarFile.getJarEntry(pathInArchive);
/*     */     }
/*     */     catch (IOException ioe) {
/* 100 */       throw new IllegalStateException(ioe);
/*     */     } finally {
/* 102 */       if (jarFile != null) {
/* 103 */         closeJarFile();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isMultiRelease()
/*     */   {
/* 111 */     if (this.multiRelease == null) {
/* 112 */       synchronized (this.archiveLock) {
/* 113 */         if (this.multiRelease == null) {
/* 114 */           JarFile jarFile = null;
/*     */           try {
/* 116 */             jarFile = openJarFile();
/* 117 */             this.multiRelease = Boolean.valueOf(
/* 118 */               JreCompat.getInstance().jarFileIsMultiRelease(jarFile));
/*     */           }
/*     */           catch (IOException ioe) {
/* 121 */             throw new IllegalStateException(ioe);
/*     */           } finally {
/* 123 */             if (jarFile != null) {
/* 124 */               closeJarFile();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 131 */     return this.multiRelease.booleanValue();
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     try
/*     */     {
/* 139 */       JarFile jarFile = JreCompat.getInstance().jarFileNewInstance(getBase());Throwable localThrowable3 = null;
/* 140 */       try { setManifest(jarFile.getManifest());
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 139 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */       } finally {
/* 141 */         if (jarFile != null) if (localThrowable3 != null) try { jarFile.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jarFile.close();
/* 142 */       } } catch (IOException ioe) { throw new IllegalArgumentException(ioe);
/*     */     }
/*     */     try
/*     */     {
/* 146 */       setBaseUrl(UriUtil.buildJarSafeUrl(new File(getBase())));
/*     */     } catch (MalformedURLException e) {
/* 148 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractSingleArchiveResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */