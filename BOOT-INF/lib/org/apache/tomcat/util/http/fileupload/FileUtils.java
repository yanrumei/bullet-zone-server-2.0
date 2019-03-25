/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
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
/*     */ public class FileUtils
/*     */ {
/*     */   public static void deleteDirectory(File directory)
/*     */     throws IOException
/*     */   {
/*  65 */     if (!directory.exists()) {
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     if (!isSymlink(directory)) {
/*  70 */       cleanDirectory(directory);
/*     */     }
/*     */     
/*  73 */     if (!directory.delete()) {
/*  74 */       String message = "Unable to delete directory " + directory + ".";
/*     */       
/*  76 */       throw new IOException(message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void cleanDirectory(File directory)
/*     */     throws IOException
/*     */   {
/*  88 */     if (!directory.exists()) {
/*  89 */       String message = directory + " does not exist";
/*  90 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */     
/*  93 */     if (!directory.isDirectory()) {
/*  94 */       String message = directory + " is not a directory";
/*  95 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */     
/*  98 */     File[] files = directory.listFiles();
/*  99 */     if (files == null) {
/* 100 */       throw new IOException("Failed to list contents of " + directory);
/*     */     }
/*     */     
/* 103 */     IOException exception = null;
/* 104 */     for (File file : files) {
/*     */       try {
/* 106 */         forceDelete(file);
/*     */       } catch (IOException ioe) {
/* 108 */         exception = ioe;
/*     */       }
/*     */     }
/*     */     
/* 112 */     if (null != exception) {
/* 113 */       throw exception;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void forceDelete(File file)
/*     */     throws IOException
/*     */   {
/* 134 */     if (file.isDirectory()) {
/* 135 */       deleteDirectory(file);
/*     */     } else {
/* 137 */       boolean filePresent = file.exists();
/* 138 */       if (!file.delete()) {
/* 139 */         if (!filePresent) {
/* 140 */           throw new FileNotFoundException("File does not exist: " + file);
/*     */         }
/* 142 */         String message = "Unable to delete file: " + file;
/*     */         
/* 144 */         throw new IOException(message);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void forceDeleteOnExit(File file)
/*     */     throws IOException
/*     */   {
/* 158 */     if (file.isDirectory()) {
/* 159 */       deleteDirectoryOnExit(file);
/*     */     } else {
/* 161 */       file.deleteOnExit();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void deleteDirectoryOnExit(File directory)
/*     */     throws IOException
/*     */   {
/* 173 */     if (!directory.exists()) {
/* 174 */       return;
/*     */     }
/*     */     
/* 177 */     directory.deleteOnExit();
/* 178 */     if (!isSymlink(directory)) {
/* 179 */       cleanDirectoryOnExit(directory);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void cleanDirectoryOnExit(File directory)
/*     */     throws IOException
/*     */   {
/* 191 */     if (!directory.exists()) {
/* 192 */       String message = directory + " does not exist";
/* 193 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */     
/* 196 */     if (!directory.isDirectory()) {
/* 197 */       String message = directory + " is not a directory";
/* 198 */       throw new IllegalArgumentException(message);
/*     */     }
/*     */     
/* 201 */     File[] files = directory.listFiles();
/* 202 */     if (files == null) {
/* 203 */       throw new IOException("Failed to list contents of " + directory);
/*     */     }
/*     */     
/* 206 */     IOException exception = null;
/* 207 */     for (File file : files) {
/*     */       try {
/* 209 */         forceDeleteOnExit(file);
/*     */       } catch (IOException ioe) {
/* 211 */         exception = ioe;
/*     */       }
/*     */     }
/*     */     
/* 215 */     if (null != exception) {
/* 216 */       throw exception;
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
/*     */ 
/*     */ 
/*     */   public static void forceMkdir(File directory)
/*     */     throws IOException
/*     */   {
/* 232 */     if (directory.exists()) {
/* 233 */       if (!directory.isDirectory()) {
/* 234 */         String message = "File " + directory + " exists and is not a directory. Unable to create directory.";
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 239 */         throw new IOException(message);
/*     */       }
/*     */     }
/* 242 */     else if (!directory.mkdirs())
/*     */     {
/*     */ 
/* 245 */       if (!directory.isDirectory()) {
/* 246 */         String message = "Unable to create directory " + directory;
/*     */         
/* 248 */         throw new IOException(message);
/*     */       }
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
/*     */ 
/*     */   public static void forceMkdirParent(File file)
/*     */     throws IOException
/*     */   {
/* 264 */     File parent = file.getParentFile();
/* 265 */     if (parent == null) {
/* 266 */       return;
/*     */     }
/* 268 */     forceMkdir(parent);
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
/*     */   public static boolean isSymlink(File file)
/*     */     throws IOException
/*     */   {
/* 288 */     if (file == null) {
/* 289 */       throw new NullPointerException("File must not be null");
/*     */     }
/*     */     
/* 292 */     if (File.separatorChar == '\\') {
/* 293 */       return false;
/*     */     }
/* 295 */     File fileInCanonicalDir = null;
/* 296 */     if (file.getParent() == null) {
/* 297 */       fileInCanonicalDir = file;
/*     */     } else {
/* 299 */       File canonicalDir = file.getParentFile().getCanonicalFile();
/* 300 */       fileInCanonicalDir = new File(canonicalDir, file.getName());
/*     */     }
/*     */     
/* 303 */     if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
/* 304 */       return false;
/*     */     }
/* 306 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\FileUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */