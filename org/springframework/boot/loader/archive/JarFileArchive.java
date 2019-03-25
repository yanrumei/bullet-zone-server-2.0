/*     */ package org.springframework.boot.loader.archive;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.Manifest;
/*     */ import org.springframework.boot.loader.data.RandomAccessData.ResourceAccess;
/*     */ import org.springframework.boot.loader.jar.JarFile;
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
/*     */ public class JarFileArchive
/*     */   implements Archive
/*     */ {
/*     */   private static final String UNPACK_MARKER = "UNPACK:";
/*     */   private static final int BUFFER_SIZE = 32768;
/*     */   private final JarFile jarFile;
/*     */   private URL url;
/*     */   private File tempUnpackFolder;
/*     */   
/*     */   public JarFileArchive(File file)
/*     */     throws IOException
/*     */   {
/*  57 */     this(file, null);
/*     */   }
/*     */   
/*     */   public JarFileArchive(File file, URL url) throws IOException {
/*  61 */     this(new JarFile(file));
/*  62 */     this.url = url;
/*     */   }
/*     */   
/*     */   public JarFileArchive(JarFile jarFile) {
/*  66 */     this.jarFile = jarFile;
/*     */   }
/*     */   
/*     */   public URL getUrl() throws MalformedURLException
/*     */   {
/*  71 */     if (this.url != null) {
/*  72 */       return this.url;
/*     */     }
/*  74 */     return this.jarFile.getUrl();
/*     */   }
/*     */   
/*     */   public Manifest getManifest() throws IOException
/*     */   {
/*  79 */     return this.jarFile.getManifest();
/*     */   }
/*     */   
/*     */   public List<Archive> getNestedArchives(Archive.EntryFilter filter) throws IOException
/*     */   {
/*  84 */     List<Archive> nestedArchives = new ArrayList();
/*  85 */     for (Archive.Entry entry : this) {
/*  86 */       if (filter.matches(entry)) {
/*  87 */         nestedArchives.add(getNestedArchive(entry));
/*     */       }
/*     */     }
/*  90 */     return Collections.unmodifiableList(nestedArchives);
/*     */   }
/*     */   
/*     */   public Iterator<Archive.Entry> iterator()
/*     */   {
/*  95 */     return new EntryIterator(this.jarFile.entries());
/*     */   }
/*     */   
/*     */   protected Archive getNestedArchive(Archive.Entry entry) throws IOException {
/*  99 */     JarEntry jarEntry = ((JarFileEntry)entry).getJarEntry();
/* 100 */     if (jarEntry.getComment().startsWith("UNPACK:")) {
/* 101 */       return getUnpackedNestedArchive(jarEntry);
/*     */     }
/*     */     try {
/* 104 */       JarFile jarFile = this.jarFile.getNestedJarFile(jarEntry);
/* 105 */       return new JarFileArchive(jarFile);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 109 */       throw new IllegalStateException("Failed to get nested archive for entry " + entry.getName(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Archive getUnpackedNestedArchive(JarEntry jarEntry) throws IOException {
/* 114 */     String name = jarEntry.getName();
/* 115 */     if (name.lastIndexOf("/") != -1) {
/* 116 */       name = name.substring(name.lastIndexOf("/") + 1);
/*     */     }
/* 118 */     File file = new File(getTempUnpackFolder(), name);
/* 119 */     if ((!file.exists()) || (file.length() != jarEntry.getSize())) {
/* 120 */       unpack(jarEntry, file);
/*     */     }
/* 122 */     return new JarFileArchive(file, file.toURI().toURL());
/*     */   }
/*     */   
/*     */   private File getTempUnpackFolder() {
/* 126 */     if (this.tempUnpackFolder == null) {
/* 127 */       File tempFolder = new File(System.getProperty("java.io.tmpdir"));
/* 128 */       this.tempUnpackFolder = createUnpackFolder(tempFolder);
/*     */     }
/* 130 */     return this.tempUnpackFolder;
/*     */   }
/*     */   
/*     */   private File createUnpackFolder(File parent) {
/* 134 */     int attempts = 0;
/* 135 */     while (attempts++ < 1000) {
/* 136 */       String fileName = new File(this.jarFile.getName()).getName();
/*     */       
/* 138 */       File unpackFolder = new File(parent, fileName + "-spring-boot-libs-" + UUID.randomUUID());
/* 139 */       if (unpackFolder.mkdirs()) {
/* 140 */         return unpackFolder;
/*     */       }
/*     */     }
/* 143 */     throw new IllegalStateException("Failed to create unpack folder in directory '" + parent + "'");
/*     */   }
/*     */   
/*     */   private void unpack(JarEntry entry, File file) throws IOException
/*     */   {
/* 148 */     InputStream inputStream = this.jarFile.getInputStream(entry, RandomAccessData.ResourceAccess.ONCE);
/*     */     try {
/* 150 */       OutputStream outputStream = new FileOutputStream(file);
/*     */       try {
/* 152 */         byte[] buffer = new byte[32768];
/*     */         int bytesRead;
/* 154 */         while ((bytesRead = inputStream.read(buffer)) != -1) {
/* 155 */           outputStream.write(buffer, 0, bytesRead);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     finally
/*     */     {
/* 164 */       inputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*     */     try {
/* 171 */       return getUrl().toString();
/*     */     }
/*     */     catch (Exception ex) {}
/* 174 */     return "jar archive";
/*     */   }
/*     */   
/*     */ 
/*     */   private static class EntryIterator
/*     */     implements Iterator<Archive.Entry>
/*     */   {
/*     */     private final Enumeration<JarEntry> enumeration;
/*     */     
/*     */ 
/*     */     EntryIterator(Enumeration<JarEntry> enumeration)
/*     */     {
/* 186 */       this.enumeration = enumeration;
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 191 */       return this.enumeration.hasMoreElements();
/*     */     }
/*     */     
/*     */     public Archive.Entry next()
/*     */     {
/* 196 */       return new JarFileArchive.JarFileEntry((JarEntry)this.enumeration.nextElement());
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 201 */       throw new UnsupportedOperationException("remove");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class JarFileEntry
/*     */     implements Archive.Entry
/*     */   {
/*     */     private final JarEntry jarEntry;
/*     */     
/*     */ 
/*     */     JarFileEntry(JarEntry jarEntry)
/*     */     {
/* 214 */       this.jarEntry = jarEntry;
/*     */     }
/*     */     
/*     */     public JarEntry getJarEntry() {
/* 218 */       return this.jarEntry;
/*     */     }
/*     */     
/*     */     public boolean isDirectory()
/*     */     {
/* 223 */       return this.jarEntry.isDirectory();
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 228 */       return this.jarEntry.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\org\springframework\boot\loader\archive\JarFileArchive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */