/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResource;
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
/*     */ public class JarWarResourceSet
/*     */   extends AbstractArchiveResourceSet
/*     */ {
/*     */   private final String archivePath;
/*     */   
/*     */   public JarWarResourceSet(WebResourceRoot root, String webAppMount, String base, String archivePath, String internalPath)
/*     */     throws IllegalArgumentException
/*     */   {
/*  72 */     setRoot(root);
/*  73 */     setWebAppMount(webAppMount);
/*  74 */     setBase(base);
/*  75 */     this.archivePath = archivePath;
/*  76 */     setInternalPath(internalPath);
/*     */     
/*  78 */     if (getRoot().getState().isAvailable()) {
/*     */       try {
/*  80 */         start();
/*     */       } catch (LifecycleException e) {
/*  82 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected WebResource createArchiveResource(JarEntry jarEntry, String webAppPath, Manifest manifest)
/*     */   {
/*  90 */     return new JarWarResource(this, webAppPath, getBaseUrlString(), jarEntry, this.archivePath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashMap<String, JarEntry> getArchiveEntries(boolean single)
/*     */   {
/* 102 */     synchronized (this.archiveLock) {
/* 103 */       if (this.archiveEntries == null) {
/* 104 */         JarFile warFile = null;
/* 105 */         InputStream jarFileIs = null;
/* 106 */         this.archiveEntries = new HashMap();
/* 107 */         boolean multiRelease = false;
/*     */         try {
/* 109 */           warFile = openJarFile();
/* 110 */           JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
/* 111 */           jarFileIs = warFile.getInputStream(jarFileInWar);
/*     */           
/* 113 */           TomcatJarInputStream jarIs = new TomcatJarInputStream(jarFileIs);Throwable localThrowable3 = null;
/* 114 */           try { JarEntry entry = jarIs.getNextJarEntry();
/* 115 */             while (entry != null) {
/* 116 */               this.archiveEntries.put(entry.getName(), entry);
/* 117 */               entry = jarIs.getNextJarEntry();
/*     */             }
/* 119 */             Manifest m = jarIs.getManifest();
/* 120 */             setManifest(m);
/* 121 */             if ((m != null) && (JreCompat.isJre9Available())) {
/* 122 */               String value = m.getMainAttributes().getValue("Multi-Release");
/* 123 */               if (value != null) {
/* 124 */                 multiRelease = Boolean.parseBoolean(value);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */             entry = jarIs.getMetaInfEntry();
/* 134 */             if (entry != null) {
/* 135 */               this.archiveEntries.put(entry.getName(), entry);
/*     */             }
/* 137 */             entry = jarIs.getManifestEntry();
/* 138 */             if (entry != null) {
/* 139 */               this.archiveEntries.put(entry.getName(), entry);
/*     */             }
/*     */           }
/*     */           catch (Throwable localThrowable1)
/*     */           {
/* 113 */             localThrowable3 = localThrowable1;throw localThrowable1;
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
/*     */           }
/*     */           finally
/*     */           {
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
/* 141 */             if (jarIs != null) if (localThrowable3 != null) try { jarIs.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jarIs.close(); }
/* 142 */           if (multiRelease) {
/* 143 */             processArchivesEntriesForMultiRelease();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */           if (warFile != null) {
/* 151 */             closeJarFile();
/*     */           }
/* 153 */           if (jarFileIs != null) {
/*     */             try {
/* 155 */               jarFileIs.close();
/*     */             }
/*     */             catch (IOException localIOException1) {}
/*     */           }
/*     */         }
/*     */         catch (IOException ioe)
/*     */         {
/* 147 */           this.archiveEntries = null;
/* 148 */           throw new IllegalStateException(ioe);
/*     */         } finally {
/* 150 */           if (warFile != null) {
/* 151 */             closeJarFile();
/*     */           }
/* 153 */           if (jarFileIs != null) {
/*     */             try {
/* 155 */               jarFileIs.close();
/*     */             }
/*     */             catch (IOException localIOException2) {}
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 162 */       return this.archiveEntries;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void processArchivesEntriesForMultiRelease()
/*     */   {
/* 169 */     int targetVersion = JreCompat.getInstance().jarFileRuntimeMajorVersion();
/*     */     
/* 171 */     Map<String, VersionedJarEntry> versionedEntries = new HashMap();
/* 172 */     Iterator<Map.Entry<String, JarEntry>> iter = this.archiveEntries.entrySet().iterator();
/* 173 */     Map.Entry<String, JarEntry> entry; while (iter.hasNext()) {
/* 174 */       entry = (Map.Entry)iter.next();
/* 175 */       String name = (String)entry.getKey();
/* 176 */       if (name.startsWith("META-INF/versions/"))
/*     */       {
/* 178 */         iter.remove();
/*     */         
/*     */ 
/* 181 */         int i = name.indexOf('/', 18);
/* 182 */         if (i > 0) {
/* 183 */           String baseName = name.substring(i + 1);
/* 184 */           int version = Integer.parseInt(name.substring(18, i));
/*     */           
/*     */ 
/*     */ 
/* 188 */           if (version <= targetVersion) {
/* 189 */             VersionedJarEntry versionedJarEntry = (VersionedJarEntry)versionedEntries.get(baseName);
/* 190 */             if (versionedJarEntry == null)
/*     */             {
/*     */ 
/* 193 */               versionedEntries.put(baseName, new VersionedJarEntry(version, 
/* 194 */                 (JarEntry)entry.getValue()));
/*     */ 
/*     */ 
/*     */             }
/* 198 */             else if (version > versionedJarEntry.getVersion())
/*     */             {
/*     */ 
/* 201 */               versionedEntries.put(baseName, new VersionedJarEntry(version, 
/* 202 */                 (JarEntry)entry.getValue()));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 210 */     for (Map.Entry<String, VersionedJarEntry> versionedJarEntry : versionedEntries.entrySet()) {
/* 211 */       this.archiveEntries.put(versionedJarEntry.getKey(), 
/* 212 */         ((VersionedJarEntry)versionedJarEntry.getValue()).getJarEntry());
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
/*     */   protected JarEntry getArchiveEntry(String pathInArchive)
/*     */   {
/* 225 */     throw new IllegalStateException("Coding error");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isMultiRelease()
/*     */   {
/* 233 */     return false;
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     try
/*     */     {
/* 241 */       JarFile warFile = new JarFile(getBase());Throwable localThrowable6 = null;
/* 242 */       try { JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
/* 243 */         InputStream jarFileIs = warFile.getInputStream(jarFileInWar);
/*     */         
/* 245 */         JarInputStream jarIs = new JarInputStream(jarFileIs);Throwable localThrowable7 = null;
/* 246 */         try { setManifest(jarIs.getManifest());
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 245 */           localThrowable7 = localThrowable1;throw localThrowable1;
/*     */         }
/*     */         finally {}
/*     */       }
/*     */       catch (Throwable localThrowable4)
/*     */       {
/* 241 */         localThrowable6 = localThrowable4;throw localThrowable4;
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 248 */         if (warFile != null) if (localThrowable6 != null) try { warFile.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else warFile.close();
/* 249 */       } } catch (IOException ioe) { throw new IllegalArgumentException(ioe);
/*     */     }
/*     */     try
/*     */     {
/* 253 */       setBaseUrl(UriUtil.buildJarSafeUrl(new File(getBase())));
/*     */     } catch (MalformedURLException e) {
/* 255 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class VersionedJarEntry
/*     */   {
/*     */     private final int version;
/*     */     private final JarEntry jarEntry;
/*     */     
/*     */     public VersionedJarEntry(int version, JarEntry jarEntry) {
/* 265 */       this.version = version;
/* 266 */       this.jarEntry = jarEntry;
/*     */     }
/*     */     
/*     */     public int getVersion()
/*     */     {
/* 271 */       return this.version;
/*     */     }
/*     */     
/*     */     public JarEntry getJarEntry()
/*     */     {
/* 276 */       return this.jarEntry;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\JarWarResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */