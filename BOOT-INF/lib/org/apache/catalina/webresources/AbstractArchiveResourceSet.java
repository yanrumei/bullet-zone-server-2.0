/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.util.ResourceSet;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractArchiveResourceSet
/*     */   extends AbstractResourceSet
/*     */ {
/*     */   private URL baseUrl;
/*     */   private String baseUrlString;
/*  42 */   private JarFile archive = null;
/*  43 */   protected HashMap<String, JarEntry> archiveEntries = null;
/*  44 */   protected final Object archiveLock = new Object();
/*  45 */   private long archiveUseCount = 0L;
/*     */   
/*     */   protected final void setBaseUrl(URL baseUrl)
/*     */   {
/*  49 */     this.baseUrl = baseUrl;
/*  50 */     if (baseUrl == null) {
/*  51 */       this.baseUrlString = null;
/*     */     } else {
/*  53 */       this.baseUrlString = baseUrl.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public final URL getBaseUrl()
/*     */   {
/*  59 */     return this.baseUrl;
/*     */   }
/*     */   
/*     */   protected final String getBaseUrlString() {
/*  63 */     return this.baseUrlString;
/*     */   }
/*     */   
/*     */ 
/*     */   public final String[] list(String path)
/*     */   {
/*  69 */     checkPath(path);
/*  70 */     String webAppMount = getWebAppMount();
/*     */     
/*  72 */     ArrayList<String> result = new ArrayList();
/*  73 */     if (path.startsWith(webAppMount))
/*     */     {
/*  75 */       String pathInJar = getInternalPath() + path.substring(webAppMount.length());
/*     */       
/*  77 */       if ((pathInJar.length() > 0) && (pathInJar.charAt(0) == '/')) {
/*  78 */         pathInJar = pathInJar.substring(1);
/*     */       }
/*  80 */       Iterator<String> entries = getArchiveEntries(false).keySet().iterator();
/*  81 */       while (entries.hasNext()) {
/*  82 */         String name = (String)entries.next();
/*  83 */         if ((name.length() > pathInJar.length()) && 
/*  84 */           (name.startsWith(pathInJar))) {
/*  85 */           if (name.charAt(name.length() - 1) == '/') {
/*  86 */             name = name.substring(pathInJar
/*  87 */               .length(), name.length() - 1);
/*     */           } else {
/*  89 */             name = name.substring(pathInJar.length());
/*     */           }
/*  91 */           if (name.length() != 0)
/*     */           {
/*     */ 
/*  94 */             if (name.charAt(0) == '/') {
/*  95 */               name = name.substring(1);
/*     */             }
/*  97 */             if ((name.length() > 0) && (name.lastIndexOf('/') == -1))
/*  98 */               result.add(name);
/*     */           }
/*     */         }
/*     */       }
/*     */     } else {
/* 103 */       if (!path.endsWith("/")) {
/* 104 */         path = path + "/";
/*     */       }
/* 106 */       if (webAppMount.startsWith(path)) {
/* 107 */         int i = webAppMount.indexOf('/', path.length());
/* 108 */         if (i == -1) {
/* 109 */           return new String[] { webAppMount.substring(path.length()) };
/*     */         }
/* 111 */         return new String[] {webAppMount
/* 112 */           .substring(path.length(), i) };
/*     */       }
/*     */     }
/*     */     
/* 116 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */   public final Set<String> listWebAppPaths(String path)
/*     */   {
/* 121 */     checkPath(path);
/* 122 */     String webAppMount = getWebAppMount();
/*     */     
/* 124 */     ResourceSet<String> result = new ResourceSet();
/* 125 */     if (path.startsWith(webAppMount))
/*     */     {
/* 127 */       String pathInJar = getInternalPath() + path.substring(webAppMount.length());
/*     */       
/*     */ 
/* 130 */       if (pathInJar.length() > 0) {
/* 131 */         if (pathInJar.charAt(pathInJar.length() - 1) != '/') {
/* 132 */           pathInJar = pathInJar.substring(1) + '/';
/*     */         }
/* 134 */         if (pathInJar.charAt(0) == '/') {
/* 135 */           pathInJar = pathInJar.substring(1);
/*     */         }
/*     */       }
/*     */       
/* 139 */       Iterator<String> entries = getArchiveEntries(false).keySet().iterator();
/* 140 */       while (entries.hasNext()) {
/* 141 */         String name = (String)entries.next();
/* 142 */         if ((name.length() > pathInJar.length()) && 
/* 143 */           (name.startsWith(pathInJar))) {
/* 144 */           int nextSlash = name.indexOf('/', pathInJar.length());
/* 145 */           if (((nextSlash == -1) || (nextSlash == name.length() - 1)) && 
/* 146 */             (name.startsWith(pathInJar))) {
/* 147 */             result.add(webAppMount + '/' + name
/* 148 */               .substring(getInternalPath().length()));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 154 */       if (!path.endsWith("/")) {
/* 155 */         path = path + "/";
/*     */       }
/* 157 */       if (webAppMount.startsWith(path)) {
/* 158 */         int i = webAppMount.indexOf('/', path.length());
/* 159 */         if (i == -1) {
/* 160 */           result.add(webAppMount + "/");
/*     */         } else {
/* 162 */           result.add(webAppMount.substring(0, i + 1));
/*     */         }
/*     */       }
/*     */     }
/* 166 */     result.setLocked(true);
/* 167 */     return result;
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
/*     */   protected abstract HashMap<String, JarEntry> getArchiveEntries(boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract JarEntry getArchiveEntry(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean mkdir(String path)
/*     */   {
/* 200 */     checkPath(path);
/*     */     
/* 202 */     return false;
/*     */   }
/*     */   
/*     */   public final boolean write(String path, InputStream is, boolean overwrite)
/*     */   {
/* 207 */     checkPath(path);
/*     */     
/* 209 */     if (is == null)
/*     */     {
/* 211 */       throw new NullPointerException(sm.getString("dirResourceSet.writeNpe"));
/*     */     }
/*     */     
/* 214 */     return false;
/*     */   }
/*     */   
/*     */   public final WebResource getResource(String path)
/*     */   {
/* 219 */     checkPath(path);
/* 220 */     String webAppMount = getWebAppMount();
/* 221 */     WebResourceRoot root = getRoot();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */     if (path.startsWith(webAppMount)) {
/* 242 */       String pathInJar = getInternalPath() + path.substring(webAppMount
/* 243 */         .length(), path.length());
/*     */       
/* 245 */       if ((pathInJar.length() > 0) && (pathInJar.charAt(0) == '/')) {
/* 246 */         pathInJar = pathInJar.substring(1);
/*     */       }
/* 248 */       if (pathInJar.equals(""))
/*     */       {
/*     */ 
/* 251 */         if (!path.endsWith("/")) {
/* 252 */           path = path + "/";
/*     */         }
/* 254 */         return new JarResourceRoot(root, new File(getBase()), this.baseUrlString, path);
/*     */       }
/*     */       
/* 257 */       JarEntry jarEntry = null;
/* 258 */       if (isMultiRelease())
/*     */       {
/* 260 */         jarEntry = getArchiveEntry(pathInJar);
/*     */       } else {
/* 262 */         Map<String, JarEntry> jarEntries = getArchiveEntries(true);
/* 263 */         if (pathInJar.charAt(pathInJar.length() - 1) != '/') {
/* 264 */           if (jarEntries == null) {
/* 265 */             jarEntry = getArchiveEntry(pathInJar + '/');
/*     */           } else {
/* 267 */             jarEntry = (JarEntry)jarEntries.get(pathInJar + '/');
/*     */           }
/* 269 */           if (jarEntry != null) {
/* 270 */             path = path + '/';
/*     */           }
/*     */         }
/* 273 */         if (jarEntry == null) {
/* 274 */           if (jarEntries == null) {
/* 275 */             jarEntry = getArchiveEntry(pathInJar);
/*     */           } else {
/* 277 */             jarEntry = (JarEntry)jarEntries.get(pathInJar);
/*     */           }
/*     */         }
/*     */       }
/* 281 */       if (jarEntry == null) {
/* 282 */         return new EmptyResource(root, path);
/*     */       }
/* 284 */       return createArchiveResource(jarEntry, path, getManifest());
/*     */     }
/*     */     
/*     */ 
/* 288 */     return new EmptyResource(root, path);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract boolean isMultiRelease();
/*     */   
/*     */ 
/*     */   protected abstract WebResource createArchiveResource(JarEntry paramJarEntry, String paramString, Manifest paramManifest);
/*     */   
/*     */   public final boolean isReadOnly()
/*     */   {
/* 299 */     return true;
/*     */   }
/*     */   
/*     */   public void setReadOnly(boolean readOnly)
/*     */   {
/* 304 */     if (readOnly)
/*     */     {
/* 306 */       return;
/*     */     }
/*     */     
/*     */ 
/* 310 */     throw new IllegalArgumentException(sm.getString("abstractArchiveResourceSet.setReadOnlyFalse"));
/*     */   }
/*     */   
/*     */   protected JarFile openJarFile() throws IOException {
/* 314 */     synchronized (this.archiveLock) {
/* 315 */       if (this.archive == null) {
/* 316 */         this.archive = JreCompat.getInstance().jarFileNewInstance(getBase());
/*     */       }
/* 318 */       this.archiveUseCount += 1L;
/* 319 */       return this.archive;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void closeJarFile() {
/* 324 */     synchronized (this.archiveLock) {
/* 325 */       this.archiveUseCount -= 1L;
/*     */     }
/*     */   }
/*     */   
/*     */   public void gc()
/*     */   {
/* 331 */     synchronized (this.archiveLock) {
/* 332 */       if ((this.archive != null) && (this.archiveUseCount == 0L)) {
/*     */         try {
/* 334 */           this.archive.close();
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */         
/* 338 */         this.archive = null;
/* 339 */         this.archiveEntries = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractArchiveResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */