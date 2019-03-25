/*     */ package org.apache.tomcat.util.scan;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.tomcat.Jar;
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
/*     */ public abstract class AbstractInputStreamJar
/*     */   implements Jar
/*     */ {
/*     */   private final URL jarFileURL;
/*  39 */   private NonClosingJarInputStream jarInputStream = null;
/*  40 */   private JarEntry entry = null;
/*  41 */   private Boolean multiRelease = null;
/*  42 */   private Map<String, String> mrMap = null;
/*     */   
/*     */   public AbstractInputStreamJar(URL jarFileUrl) {
/*  45 */     this.jarFileURL = jarFileUrl;
/*     */   }
/*     */   
/*     */ 
/*     */   public URL getJarFileURL()
/*     */   {
/*  51 */     return this.jarFileURL;
/*     */   }
/*     */   
/*     */ 
/*     */   public void nextEntry()
/*     */   {
/*  57 */     if (this.jarInputStream == null) {
/*     */       try {
/*  59 */         reset();
/*     */       } catch (IOException e) {
/*  61 */         this.entry = null;
/*  62 */         return;
/*     */       }
/*     */     }
/*     */     try {
/*  66 */       this.entry = this.jarInputStream.getNextJarEntry();
/*  67 */       if (this.multiRelease.booleanValue())
/*     */       {
/*     */ 
/*  70 */         while ((this.entry != null) && (
/*  71 */           (this.mrMap.keySet().contains(this.entry.getName())) || (
/*  72 */           (this.entry.getName().startsWith("META-INF/versions/")) && 
/*  73 */           (!this.mrMap.values().contains(this.entry.getName()))))) {
/*  74 */           this.entry = this.jarInputStream.getNextJarEntry();
/*     */         }
/*     */       }
/*     */       
/*  78 */       while ((this.entry != null) && (this.entry.getName().startsWith("META-INF/versions/"))) {
/*  79 */         this.entry = this.jarInputStream.getNextJarEntry();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe) {
/*  83 */       this.entry = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEntryName()
/*     */   {
/*  92 */     if (this.entry == null) {
/*  93 */       return null;
/*     */     }
/*  95 */     return this.entry.getName();
/*     */   }
/*     */   
/*     */ 
/*     */   public InputStream getEntryInputStream()
/*     */     throws IOException
/*     */   {
/* 102 */     return this.jarInputStream;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean entryExists(String name)
/*     */     throws IOException
/*     */   {
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream(String name)
/*     */     throws IOException
/*     */   {
/* 115 */     gotoEntry(name);
/* 116 */     if (this.entry == null) {
/* 117 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 122 */     this.entry = null;
/* 123 */     return this.jarInputStream;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLastModified(String name)
/*     */     throws IOException
/*     */   {
/* 130 */     gotoEntry(name);
/* 131 */     if (this.entry == null) {
/* 132 */       return -1L;
/*     */     }
/* 134 */     return this.entry.getTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getURL(String entry)
/*     */   {
/* 141 */     StringBuilder result = new StringBuilder("jar:");
/* 142 */     result.append(getJarFileURL().toExternalForm());
/* 143 */     result.append("!/");
/* 144 */     result.append(entry);
/*     */     
/* 146 */     return result.toString();
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */     throws IOException
/*     */   {
/* 152 */     reset();
/* 153 */     return this.jarInputStream.getManifest();
/*     */   }
/*     */   
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 159 */     closeStream();
/* 160 */     this.entry = null;
/* 161 */     this.jarInputStream = createJarInputStream();
/*     */     
/* 163 */     if (this.multiRelease == null) {
/* 164 */       if (JreCompat.isJre9Available()) {
/* 165 */         Manifest manifest = this.jarInputStream.getManifest();
/* 166 */         if (manifest == null) {
/* 167 */           this.multiRelease = Boolean.FALSE;
/*     */         } else {
/* 169 */           String mrValue = manifest.getMainAttributes().getValue("Multi-Release");
/* 170 */           if (mrValue == null) {
/* 171 */             this.multiRelease = Boolean.FALSE;
/*     */           } else {
/* 173 */             this.multiRelease = Boolean.valueOf(mrValue);
/*     */           }
/*     */         }
/*     */       } else {
/* 177 */         this.multiRelease = Boolean.FALSE;
/*     */       }
/* 179 */       if ((this.multiRelease.booleanValue()) && 
/* 180 */         (this.mrMap == null)) {
/* 181 */         populateMrMap();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void closeStream()
/*     */   {
/* 189 */     if (this.jarInputStream != null) {
/*     */       try {
/* 191 */         this.jarInputStream.reallyClose();
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract NonClosingJarInputStream createJarInputStream()
/*     */     throws IOException;
/*     */   
/*     */   private void gotoEntry(String name)
/*     */     throws IOException
/*     */   {
/* 203 */     boolean needsReset = true;
/* 204 */     if (this.multiRelease == null) {
/* 205 */       reset();
/* 206 */       needsReset = false;
/*     */     }
/*     */     
/*     */ 
/* 210 */     if (this.multiRelease.booleanValue()) {
/* 211 */       String mrName = (String)this.mrMap.get(name);
/* 212 */       if (mrName != null) {
/* 213 */         name = mrName;
/*     */       }
/* 215 */     } else if (name.startsWith("META-INF/versions/")) {
/* 216 */       this.entry = null;
/* 217 */       return;
/*     */     }
/*     */     
/* 220 */     if ((this.entry != null) && (name.equals(this.entry.getName()))) {
/* 221 */       return;
/*     */     }
/* 223 */     if (needsReset) {
/* 224 */       reset();
/*     */     }
/*     */     
/* 227 */     JarEntry jarEntry = this.jarInputStream.getNextJarEntry();
/* 228 */     while (jarEntry != null) {
/* 229 */       if (name.equals(jarEntry.getName())) {
/* 230 */         this.entry = jarEntry;
/* 231 */         break;
/*     */       }
/* 233 */       jarEntry = this.jarInputStream.getNextJarEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   private void populateMrMap() throws IOException
/*     */   {
/* 239 */     int targetVersion = JreCompat.getInstance().jarFileRuntimeMajorVersion();
/*     */     
/* 241 */     Map<String, Integer> mrVersions = new HashMap();
/*     */     
/* 243 */     JarEntry jarEntry = this.jarInputStream.getNextJarEntry();
/*     */     
/*     */     String name;
/*     */     
/* 247 */     while (jarEntry != null) {
/* 248 */       name = jarEntry.getName();
/* 249 */       if ((name.startsWith("META-INF/versions/")) && (name.endsWith(".class")))
/*     */       {
/*     */ 
/* 252 */         int i = name.indexOf('/', 18);
/* 253 */         if (i > 0) {
/* 254 */           String baseName = name.substring(i + 1);
/* 255 */           int version = Integer.parseInt(name.substring(18, i));
/*     */           
/*     */ 
/*     */ 
/* 259 */           if (version <= targetVersion) {
/* 260 */             Integer mappedVersion = (Integer)mrVersions.get(baseName);
/* 261 */             if (mappedVersion == null)
/*     */             {
/* 263 */               mrVersions.put(baseName, Integer.valueOf(version));
/*     */ 
/*     */ 
/*     */             }
/* 267 */             else if (version > mappedVersion.intValue())
/*     */             {
/* 269 */               mrVersions.put(baseName, Integer.valueOf(version));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 275 */       jarEntry = this.jarInputStream.getNextJarEntry();
/*     */     }
/*     */     
/* 278 */     this.mrMap = new HashMap();
/*     */     
/* 280 */     for (Map.Entry<String, Integer> mrVersion : mrVersions.entrySet()) {
/* 281 */       this.mrMap.put(mrVersion.getKey(), "META-INF/versions/" + ((Integer)mrVersion.getValue()).toString() + "/" + 
/* 282 */         (String)mrVersion.getKey());
/*     */     }
/*     */     
/*     */ 
/* 286 */     closeStream();
/* 287 */     this.jarInputStream = createJarInputStream();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\AbstractInputStreamJar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */