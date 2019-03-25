/*     */ package org.apache.tomcat.util.scan;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipEntry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarFileUrlJar
/*     */   implements Jar
/*     */ {
/*     */   private final JarFile jarFile;
/*     */   private final URL jarFileURL;
/*     */   private final boolean multiRelease;
/*     */   private Enumeration<JarEntry> entries;
/*     */   private Set<String> entryNamesSeen;
/*  48 */   private JarEntry entry = null;
/*     */   
/*     */   public JarFileUrlJar(URL url, boolean startsWithJar) throws IOException {
/*  51 */     if (startsWithJar)
/*     */     {
/*  53 */       JarURLConnection jarConn = (JarURLConnection)url.openConnection();
/*  54 */       jarConn.setUseCaches(false);
/*  55 */       this.jarFile = jarConn.getJarFile();
/*  56 */       this.jarFileURL = jarConn.getJarFileURL();
/*     */     }
/*     */     else
/*     */     {
/*     */       try {
/*  61 */         f = new File(url.toURI());
/*     */       } catch (URISyntaxException e) { File f;
/*  63 */         throw new IOException(e); }
/*     */       File f;
/*  65 */       this.jarFile = JreCompat.getInstance().jarFileNewInstance(f);
/*  66 */       this.jarFileURL = url;
/*     */     }
/*  68 */     this.multiRelease = JreCompat.getInstance().jarFileIsMultiRelease(this.jarFile);
/*     */   }
/*     */   
/*     */ 
/*     */   public URL getJarFileURL()
/*     */   {
/*  74 */     return this.jarFileURL;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public boolean entryExists(String name)
/*     */   {
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream(String name)
/*     */     throws IOException
/*     */   {
/*  87 */     ZipEntry entry = this.jarFile.getEntry(name);
/*  88 */     if (entry == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     return this.jarFile.getInputStream(entry);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLastModified(String name)
/*     */     throws IOException
/*     */   {
/*  98 */     ZipEntry entry = this.jarFile.getEntry(name);
/*  99 */     if (entry == null) {
/* 100 */       return -1L;
/*     */     }
/* 102 */     return entry.getTime();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getURL(String entry)
/*     */   {
/* 108 */     StringBuilder result = new StringBuilder("jar:");
/* 109 */     result.append(getJarFileURL().toExternalForm());
/* 110 */     result.append("!/");
/* 111 */     result.append(entry);
/*     */     
/* 113 */     return result.toString();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 118 */     if (this.jarFile != null) {
/*     */       try {
/* 120 */         this.jarFile.close();
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void nextEntry()
/*     */   {
/* 130 */     if (this.entries == null) {
/* 131 */       this.entries = this.jarFile.entries();
/* 132 */       if (this.multiRelease) {
/* 133 */         this.entryNamesSeen = new HashSet();
/*     */       }
/*     */     }
/*     */     
/* 137 */     if (this.multiRelease)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */       String name = null;
/*     */       
/* 150 */       while (this.entries.hasMoreElements()) {
/* 151 */         this.entry = ((JarEntry)this.entries.nextElement());
/* 152 */         name = this.entry.getName();
/*     */         
/* 154 */         if (name.startsWith("META-INF/versions/")) {
/* 155 */           int i = name.indexOf('/', 18);
/* 156 */           if (i != -1)
/*     */           {
/*     */ 
/* 159 */             name = name.substring(i + 1);
/*     */           }
/* 161 */         } else if ((name.length() != 0) && (!this.entryNamesSeen.contains(name)))
/*     */         {
/*     */ 
/*     */ 
/* 165 */           this.entryNamesSeen.add(name);
/*     */           
/*     */ 
/* 168 */           this.entry = this.jarFile.getJarEntry(this.entry.getName());
/* 169 */           return;
/*     */         } }
/* 171 */       this.entry = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 176 */     else if (this.entries.hasMoreElements()) {
/* 177 */       this.entry = ((JarEntry)this.entries.nextElement());
/*     */     } else {
/* 179 */       this.entry = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getEntryName()
/*     */   {
/* 186 */     if (this.entry == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     return this.entry.getName();
/*     */   }
/*     */   
/*     */   public InputStream getEntryInputStream()
/*     */     throws IOException
/*     */   {
/* 195 */     if (this.entry == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     return this.jarFile.getInputStream(this.entry);
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */     throws IOException
/*     */   {
/* 204 */     return this.jarFile.getManifest();
/*     */   }
/*     */   
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 210 */     this.entries = null;
/* 211 */     this.entryNamesSeen = null;
/* 212 */     this.entry = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\JarFileUrlJar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */