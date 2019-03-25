/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.Enumeration;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipException;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExpandWar
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(ExpandWar.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.startup");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String expand(Host host, URL war, String pathname)
/*     */     throws IOException
/*     */   {
/*  80 */     JarURLConnection juc = (JarURLConnection)war.openConnection();
/*  81 */     juc.setUseCaches(false);
/*  82 */     URL jarFileUrl = juc.getJarFileURL();
/*  83 */     URLConnection jfuc = jarFileUrl.openConnection();
/*     */     
/*  85 */     boolean success = false;
/*  86 */     File docBase = new File(host.getAppBaseFile(), pathname);
/*  87 */     File warTracker = new File(host.getAppBaseFile(), pathname + "/META-INF/war-tracker");
/*  88 */     long warLastModified = -1L;
/*     */     
/*  90 */     InputStream is = jfuc.getInputStream();Throwable localThrowable9 = null;
/*     */     try {
/*  92 */       warLastModified = jfuc.getLastModified();
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/*  90 */       localThrowable9 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally {
/*  93 */       if (is != null) if (localThrowable9 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable9.addSuppressed(localThrowable2); } else { is.close();
/*     */         }
/*     */     }
/*  96 */     if (docBase.exists())
/*     */     {
/*     */ 
/*     */ 
/* 100 */       if ((!warTracker.exists()) || (warTracker.lastModified() == warLastModified))
/*     */       {
/* 102 */         success = true;
/* 103 */         return docBase.getAbsolutePath();
/*     */       }
/*     */       
/*     */ 
/* 107 */       log.info(sm.getString("expandWar.deleteOld", new Object[] { docBase }));
/* 108 */       if (!delete(docBase)) {
/* 109 */         throw new IOException(sm.getString("expandWar.deleteFailed", new Object[] { docBase }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 114 */     if ((!docBase.mkdir()) && (!docBase.isDirectory())) {
/* 115 */       throw new IOException(sm.getString("expandWar.createFailed", new Object[] { docBase }));
/*     */     }
/*     */     
/*     */ 
/* 119 */     String canonicalDocBasePrefix = docBase.getCanonicalPath();
/* 120 */     if (!canonicalDocBasePrefix.endsWith(File.separator)) {
/* 121 */       canonicalDocBasePrefix = canonicalDocBasePrefix + File.separator;
/*     */     }
/*     */     
/*     */ 
/* 125 */     File warTrackerParent = warTracker.getParentFile();
/* 126 */     if ((!warTrackerParent.isDirectory()) && (!warTrackerParent.mkdirs())) {
/* 127 */       throw new IOException(sm.getString("expandWar.createFailed", new Object[] { warTrackerParent.getAbsolutePath() }));
/*     */     }
/*     */     try {
/* 130 */       JarFile jarFile = juc.getJarFile();Throwable localThrowable10 = null;
/*     */       try {
/* 132 */         Enumeration<JarEntry> jarEntries = jarFile.entries();
/* 133 */         while (jarEntries.hasMoreElements()) {
/* 134 */           JarEntry jarEntry = (JarEntry)jarEntries.nextElement();
/* 135 */           String name = jarEntry.getName();
/* 136 */           File expandedFile = new File(docBase, name);
/* 137 */           if (!expandedFile.getCanonicalPath().startsWith(canonicalDocBasePrefix))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 142 */             throw new IllegalArgumentException(sm.getString("expandWar.illegalPath", new Object[] { war, name, expandedFile
/* 143 */               .getCanonicalPath(), canonicalDocBasePrefix }));
/*     */           }
/*     */           
/* 146 */           int last = name.lastIndexOf('/');
/* 147 */           if (last >= 0)
/*     */           {
/* 149 */             File parent = new File(docBase, name.substring(0, last));
/* 150 */             if ((!parent.mkdirs()) && (!parent.isDirectory()))
/*     */             {
/* 152 */               throw new IOException(sm.getString("expandWar.createFailed", new Object[] { parent }));
/*     */             }
/*     */           }
/* 155 */           if (!name.endsWith("/"))
/*     */           {
/*     */ 
/*     */ 
/* 159 */             InputStream input = jarFile.getInputStream(jarEntry);Throwable localThrowable11 = null;
/* 160 */             try { if (null == input) {
/* 161 */                 throw new ZipException(sm.getString("expandWar.missingJarEntry", new Object[] {jarEntry
/* 162 */                   .getName() }));
/*     */               }
/*     */               
/*     */ 
/* 166 */               expand(input, expandedFile);
/* 167 */               long lastModified = jarEntry.getTime();
/* 168 */               if ((lastModified != -1L) && (lastModified != 0L)) {
/* 169 */                 expandedFile.setLastModified(lastModified);
/*     */               }
/*     */             }
/*     */             catch (Throwable localThrowable4)
/*     */             {
/* 159 */               localThrowable11 = localThrowable4;throw localThrowable4;
/*     */             }
/*     */             finally {}
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */         warTracker.createNewFile();
/* 177 */         warTracker.setLastModified(warLastModified);
/*     */         
/* 179 */         success = true;
/*     */       }
/*     */       catch (Throwable localThrowable7)
/*     */       {
/* 130 */         localThrowable10 = localThrowable7;throw localThrowable7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */         if (jarFile != null) if (localThrowable10 != null) try { jarFile.close(); } catch (Throwable localThrowable8) { localThrowable10.addSuppressed(localThrowable8); } else jarFile.close();
/* 181 */       } } catch (IOException e) { throw e;
/*     */     } finally {
/* 183 */       if (!success)
/*     */       {
/*     */ 
/* 186 */         deleteDir(docBase);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 191 */     return docBase.getAbsolutePath();
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
/*     */   public static void validate(Host host, URL war, String pathname)
/*     */     throws IOException
/*     */   {
/* 210 */     File docBase = new File(host.getAppBaseFile(), pathname);
/*     */     
/*     */ 
/* 213 */     String canonicalDocBasePrefix = docBase.getCanonicalPath();
/* 214 */     if (!canonicalDocBasePrefix.endsWith(File.separator)) {
/* 215 */       canonicalDocBasePrefix = canonicalDocBasePrefix + File.separator;
/*     */     }
/* 217 */     JarURLConnection juc = (JarURLConnection)war.openConnection();
/* 218 */     juc.setUseCaches(false);
/* 219 */     try { JarFile jarFile = juc.getJarFile();Throwable localThrowable3 = null;
/* 220 */       try { Enumeration<JarEntry> jarEntries = jarFile.entries();
/* 221 */         while (jarEntries.hasMoreElements()) {
/* 222 */           JarEntry jarEntry = (JarEntry)jarEntries.nextElement();
/* 223 */           String name = jarEntry.getName();
/* 224 */           File expandedFile = new File(docBase, name);
/* 225 */           if (!expandedFile.getCanonicalPath().startsWith(canonicalDocBasePrefix))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 230 */             throw new IllegalArgumentException(sm.getString("expandWar.illegalPath", new Object[] { war, name, expandedFile
/* 231 */               .getCanonicalPath(), canonicalDocBasePrefix }));
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 219 */         localThrowable3 = localThrowable1;throw localThrowable1;
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
/*     */ 
/* 235 */         if (jarFile != null) if (localThrowable3 != null) try { jarFile.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jarFile.close();
/* 236 */       } } catch (IOException e) { throw e;
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
/*     */   public static boolean copy(File src, File dest)
/*     */   {
/* 250 */     boolean result = true;
/*     */     
/* 252 */     String[] files = null;
/* 253 */     if (src.isDirectory()) {
/* 254 */       files = src.list();
/* 255 */       result = dest.mkdir();
/*     */     } else {
/* 257 */       files = new String[1];
/* 258 */       files[0] = "";
/*     */     }
/* 260 */     if (files == null) {
/* 261 */       files = new String[0];
/*     */     }
/* 263 */     for (int i = 0; (i < files.length) && (result); i++) {
/* 264 */       File fileSrc = new File(src, files[i]);
/* 265 */       File fileDest = new File(dest, files[i]);
/* 266 */       if (fileSrc.isDirectory())
/* 267 */         result = copy(fileSrc, fileDest); else {
/*     */         try {
/* 269 */           FileChannel ic = new FileInputStream(fileSrc).getChannel();Throwable localThrowable6 = null;
/* 270 */           try { FileChannel oc = new FileOutputStream(fileDest).getChannel();Throwable localThrowable7 = null;
/* 271 */             try { ic.transferTo(0L, ic.size(), oc);
/*     */             }
/*     */             catch (Throwable localThrowable1)
/*     */             {
/* 269 */               localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
/*     */           }
/*     */           finally {
/* 272 */             if (ic != null) if (localThrowable6 != null) try { ic.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else ic.close();
/* 273 */           } } catch (IOException e) { log.error(sm.getString("expandWar.copy", new Object[] { fileSrc, fileDest }), e);
/* 274 */           result = false;
/*     */         }
/*     */       }
/*     */     }
/* 278 */     return result;
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
/*     */   public static boolean delete(File dir)
/*     */   {
/* 291 */     return delete(dir, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean delete(File dir, boolean logFailure)
/*     */   {
/*     */     boolean result;
/*     */     
/*     */ 
/*     */ 
/*     */     boolean result;
/*     */     
/*     */ 
/* 306 */     if (dir.isDirectory()) {
/* 307 */       result = deleteDir(dir, logFailure);
/*     */     } else { boolean result;
/* 309 */       if (dir.exists()) {
/* 310 */         result = dir.delete();
/*     */       } else {
/* 312 */         result = true;
/*     */       }
/*     */     }
/* 315 */     if ((logFailure) && (!result)) {
/* 316 */       log.error(sm.getString("expandWar.deleteFailed", new Object[] {dir
/* 317 */         .getAbsolutePath() }));
/*     */     }
/* 319 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean deleteDir(File dir)
/*     */   {
/* 331 */     return deleteDir(dir, true);
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
/*     */   public static boolean deleteDir(File dir, boolean logFailure)
/*     */   {
/* 346 */     String[] files = dir.list();
/* 347 */     if (files == null) {
/* 348 */       files = new String[0];
/*     */     }
/* 350 */     for (int i = 0; i < files.length; i++) {
/* 351 */       File file = new File(dir, files[i]);
/* 352 */       if (file.isDirectory()) {
/* 353 */         deleteDir(file, logFailure);
/*     */       } else {
/* 355 */         file.delete();
/*     */       }
/*     */     }
/*     */     boolean result;
/*     */     boolean result;
/* 360 */     if (dir.exists()) {
/* 361 */       result = dir.delete();
/*     */     } else {
/* 363 */       result = true;
/*     */     }
/*     */     
/* 366 */     if ((logFailure) && (!result)) {
/* 367 */       log.error(sm.getString("expandWar.deleteFailed", new Object[] {dir
/* 368 */         .getAbsolutePath() }));
/*     */     }
/*     */     
/* 371 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void expand(InputStream input, File file)
/*     */     throws IOException
/*     */   {
/* 384 */     BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));Throwable localThrowable3 = null;
/*     */     try {
/* 386 */       byte[] buffer = new byte['ࠀ'];
/*     */       for (;;) {
/* 388 */         int n = input.read(buffer);
/* 389 */         if (n <= 0)
/*     */           break;
/* 391 */         output.write(buffer, 0, n);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 384 */       localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/* 393 */       if (output != null) if (localThrowable3 != null) try { output.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else output.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\ExpandWar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */