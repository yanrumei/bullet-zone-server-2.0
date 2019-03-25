/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtensionValidator
/*     */ {
/*  51 */   private static final Log log = LogFactory.getLog(ExtensionValidator.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.util");
/*     */   
/*  59 */   private static volatile ArrayList<Extension> containerAvailableExtensions = null;
/*     */   
/*  61 */   private static final ArrayList<ManifestResource> containerManifestResources = new ArrayList();
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
/*     */   static
/*     */   {
/*  79 */     String systemClasspath = System.getProperty("java.class.path");
/*     */     
/*  81 */     StringTokenizer strTok = new StringTokenizer(systemClasspath, File.pathSeparator);
/*     */     
/*     */ 
/*     */ 
/*  85 */     while (strTok.hasMoreTokens()) {
/*  86 */       String classpathItem = strTok.nextToken();
/*  87 */       if (classpathItem.toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
/*  88 */         File item = new File(classpathItem);
/*  89 */         if (item.isFile()) {
/*     */           try {
/*  91 */             addSystemResource(item);
/*     */           } catch (IOException e) {
/*  93 */             log.error(sm
/*  94 */               .getString("extensionValidator.failload", new Object[] { item }), e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 101 */     addFolderList("java.ext.dirs");
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
/*     */   public static synchronized boolean validateApplication(WebResourceRoot resources, Context context)
/*     */     throws IOException
/*     */   {
/* 131 */     String appName = context.getName();
/* 132 */     ArrayList<ManifestResource> appManifestResources = new ArrayList();
/*     */     
/*     */ 
/* 135 */     WebResource resource = resources.getResource("/META-INF/MANIFEST.MF");
/* 136 */     if (resource.isFile()) {
/* 137 */       InputStream inputStream = resource.getInputStream();localObject1 = null;
/* 138 */       try { Manifest manifest = new Manifest(inputStream);
/*     */         
/* 140 */         mre = new ManifestResource(sm.getString("extensionValidator.web-application-manifest"), manifest, 2);
/*     */         
/* 142 */         appManifestResources.add(mre);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 137 */         localObject1 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 143 */         if (inputStream != null) { if (localObject1 != null) try { inputStream.close(); } catch (Throwable localThrowable2) { ((Throwable)localObject1).addSuppressed(localThrowable2); } else { inputStream.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 148 */     WebResource[] manifestResources = resources.getClassLoaderResources("/META-INF/MANIFEST.MF");
/* 149 */     Object localObject1 = manifestResources;localThrowable1 = localObject1.length; for (ManifestResource mre = 0; mre < localThrowable1; mre++) { WebResource manifestResource = localObject1[mre];
/* 150 */       if (manifestResource.isFile())
/*     */       {
/* 152 */         String jarName = manifestResource.getURL().toExternalForm();
/* 153 */         Manifest jmanifest = manifestResource.getManifest();
/* 154 */         if (jmanifest != null) {
/* 155 */           ManifestResource mre = new ManifestResource(jarName, jmanifest, 3);
/*     */           
/* 157 */           appManifestResources.add(mre);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 162 */     return validateManifestResources(appName, appManifestResources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void addSystemResource(File jarFile)
/*     */     throws IOException
/*     */   {
/* 174 */     InputStream is = new FileInputStream(jarFile);Throwable localThrowable3 = null;
/* 175 */     try { Manifest manifest = getManifest(is);
/* 176 */       if (manifest != null) {
/* 177 */         ManifestResource mre = new ManifestResource(jarFile.getAbsolutePath(), manifest, 1);
/*     */         
/* 179 */         containerManifestResources.add(mre);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 174 */       localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/* 181 */       if (is != null) { if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { is.close();
/*     */         }
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
/*     */   private static boolean validateManifestResources(String appName, ArrayList<ManifestResource> resources)
/*     */   {
/* 209 */     boolean passes = true;
/* 210 */     int failureCount = 0;
/* 211 */     ArrayList<Extension> availableExtensions = null;
/*     */     
/* 213 */     Iterator<ManifestResource> it = resources.iterator();
/* 214 */     while (it.hasNext()) {
/* 215 */       ManifestResource mre = (ManifestResource)it.next();
/* 216 */       ArrayList<Extension> requiredList = mre.getRequiredExtensions();
/* 217 */       if (requiredList != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 222 */         if (availableExtensions == null) {
/* 223 */           availableExtensions = buildAvailableExtensionsList(resources);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 228 */         if (containerAvailableExtensions == null)
/*     */         {
/* 230 */           containerAvailableExtensions = buildAvailableExtensionsList(containerManifestResources);
/*     */         }
/*     */         
/*     */ 
/* 234 */         Iterator<Extension> rit = requiredList.iterator();
/* 235 */         while (rit.hasNext()) {
/* 236 */           boolean found = false;
/* 237 */           Extension requiredExt = (Extension)rit.next();
/*     */           
/* 239 */           if (availableExtensions != null) {
/* 240 */             Iterator<Extension> ait = availableExtensions.iterator();
/* 241 */             while (ait.hasNext()) {
/* 242 */               Extension targetExt = (Extension)ait.next();
/* 243 */               if (targetExt.isCompatibleWith(requiredExt)) {
/* 244 */                 requiredExt.setFulfilled(true);
/* 245 */                 found = true;
/* 246 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 251 */           if ((!found) && (containerAvailableExtensions != null))
/*     */           {
/* 253 */             Iterator<Extension> cit = containerAvailableExtensions.iterator();
/* 254 */             while (cit.hasNext()) {
/* 255 */               Extension targetExt = (Extension)cit.next();
/* 256 */               if (targetExt.isCompatibleWith(requiredExt)) {
/* 257 */                 requiredExt.setFulfilled(true);
/* 258 */                 found = true;
/* 259 */                 break;
/*     */               }
/*     */             }
/*     */           }
/* 263 */           if (!found)
/*     */           {
/* 265 */             log.info(sm.getString("extensionValidator.extension-not-found-error", new Object[] { appName, mre
/*     */             
/* 267 */               .getResourceName(), requiredExt
/* 268 */               .getExtensionName() }));
/* 269 */             passes = false;
/* 270 */             failureCount++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 275 */     if (!passes) {
/* 276 */       log.info(sm.getString("extensionValidator.extension-validation-error", new Object[] { appName, failureCount + "" }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 281 */     return passes;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList<Extension> buildAvailableExtensionsList(ArrayList<ManifestResource> resources)
/*     */   {
/* 304 */     ArrayList<Extension> availableList = null;
/*     */     
/* 306 */     Iterator<ManifestResource> it = resources.iterator();
/* 307 */     while (it.hasNext()) {
/* 308 */       ManifestResource mre = (ManifestResource)it.next();
/* 309 */       ArrayList<Extension> list = mre.getAvailableExtensions();
/* 310 */       if (list != null) {
/* 311 */         Iterator<Extension> values = list.iterator();
/* 312 */         while (values.hasNext()) {
/* 313 */           Extension ext = (Extension)values.next();
/* 314 */           if (availableList == null) {
/* 315 */             availableList = new ArrayList();
/* 316 */             availableList.add(ext);
/*     */           } else {
/* 318 */             availableList.add(ext);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 324 */     return availableList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Manifest getManifest(InputStream inStream)
/*     */     throws IOException
/*     */   {
/* 334 */     Manifest manifest = null;
/* 335 */     JarInputStream jin = new JarInputStream(inStream);Throwable localThrowable3 = null;
/* 336 */     try { manifest = jin.getManifest();
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 335 */       localThrowable3 = localThrowable1;throw localThrowable1;
/*     */     } finally {
/* 337 */       if (jin != null) if (localThrowable3 != null) try { jin.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else jin.close(); }
/* 338 */     return manifest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addFolderList(String property)
/*     */   {
/* 348 */     String extensionsDir = System.getProperty(property);
/* 349 */     if (extensionsDir != null) {
/* 350 */       StringTokenizer extensionsTok = new StringTokenizer(extensionsDir, File.pathSeparator);
/*     */       
/* 352 */       while (extensionsTok.hasMoreTokens()) {
/* 353 */         File targetDir = new File(extensionsTok.nextToken());
/* 354 */         if (targetDir.isDirectory())
/*     */         {
/*     */ 
/* 357 */           File[] files = targetDir.listFiles();
/* 358 */           if (files != null)
/*     */           {
/*     */ 
/* 361 */             for (int i = 0; i < files.length; i++) {
/* 362 */               if ((files[i].getName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) && 
/* 363 */                 (files[i].isFile())) {
/*     */                 try {
/* 365 */                   addSystemResource(files[i]);
/*     */                 }
/*     */                 catch (IOException e) {
/* 368 */                   log.error(sm
/* 369 */                     .getString("extensionValidator.failload", new Object[] { files[i] }), e);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ExtensionValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */