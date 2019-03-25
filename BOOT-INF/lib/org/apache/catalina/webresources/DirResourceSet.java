/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*     */ import org.apache.catalina.util.ResourceSet;
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
/*     */ public class DirResourceSet
/*     */   extends AbstractFileResourceSet
/*     */ {
/*  41 */   private static final Log log = LogFactory.getLog(DirResourceSet.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public DirResourceSet()
/*     */   {
/*  47 */     super("/");
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
/*     */   public DirResourceSet(WebResourceRoot root, String webAppMount, String base, String internalPath)
/*     */   {
/*  70 */     super(internalPath);
/*  71 */     setRoot(root);
/*  72 */     setWebAppMount(webAppMount);
/*  73 */     setBase(base);
/*     */     
/*  75 */     if (root.getContext().getAddWebinfClassesResources()) {
/*  76 */       File f = new File(base, internalPath);
/*  77 */       f = new File(f, "/WEB-INF/classes/META-INF/resources");
/*     */       
/*  79 */       if (f.isDirectory()) {
/*  80 */         root.createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", f
/*  81 */           .getAbsolutePath(), null, "/");
/*     */       }
/*     */     }
/*     */     
/*  85 */     if (getRoot().getState().isAvailable()) {
/*     */       try {
/*  87 */         start();
/*     */       } catch (LifecycleException e) {
/*  89 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public WebResource getResource(String path)
/*     */   {
/*  97 */     checkPath(path);
/*  98 */     String webAppMount = getWebAppMount();
/*  99 */     WebResourceRoot root = getRoot();
/* 100 */     if (path.startsWith(webAppMount)) {
/* 101 */       File f = file(path.substring(webAppMount.length()), false);
/* 102 */       if (f == null) {
/* 103 */         return new EmptyResource(root, path);
/*     */       }
/* 105 */       if (!f.exists()) {
/* 106 */         return new EmptyResource(root, path, f);
/*     */       }
/* 108 */       if ((f.isDirectory()) && (path.charAt(path.length() - 1) != '/')) {
/* 109 */         path = path + '/';
/*     */       }
/* 111 */       return new FileResource(root, path, f, isReadOnly(), getManifest());
/*     */     }
/* 113 */     return new EmptyResource(root, path);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] list(String path)
/*     */   {
/* 119 */     checkPath(path);
/* 120 */     String webAppMount = getWebAppMount();
/* 121 */     if (path.startsWith(webAppMount)) {
/* 122 */       File f = file(path.substring(webAppMount.length()), true);
/* 123 */       if (f == null) {
/* 124 */         return EMPTY_STRING_ARRAY;
/*     */       }
/* 126 */       String[] result = f.list();
/* 127 */       if (result == null) {
/* 128 */         return EMPTY_STRING_ARRAY;
/*     */       }
/* 130 */       return result;
/*     */     }
/*     */     
/* 133 */     if (!path.endsWith("/")) {
/* 134 */       path = path + "/";
/*     */     }
/* 136 */     if (webAppMount.startsWith(path)) {
/* 137 */       int i = webAppMount.indexOf('/', path.length());
/* 138 */       if (i == -1) {
/* 139 */         return new String[] { webAppMount.substring(path.length()) };
/*     */       }
/* 141 */       return new String[] {webAppMount
/* 142 */         .substring(path.length(), i) };
/*     */     }
/*     */     
/* 145 */     return EMPTY_STRING_ARRAY;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> listWebAppPaths(String path)
/*     */   {
/* 151 */     checkPath(path);
/* 152 */     String webAppMount = getWebAppMount();
/* 153 */     ResourceSet<String> result = new ResourceSet();
/* 154 */     if (path.startsWith(webAppMount)) {
/* 155 */       File f = file(path.substring(webAppMount.length()), true);
/* 156 */       if (f != null) {
/* 157 */         File[] list = f.listFiles();
/* 158 */         if (list != null) {
/* 159 */           for (File entry : list) {
/* 160 */             StringBuilder sb = new StringBuilder(path);
/* 161 */             if (path.charAt(path.length() - 1) != '/') {
/* 162 */               sb.append('/');
/*     */             }
/* 164 */             sb.append(entry.getName());
/* 165 */             if (entry.isDirectory()) {
/* 166 */               sb.append('/');
/*     */             }
/* 168 */             result.add(sb.toString());
/*     */           }
/*     */         }
/*     */       }
/*     */     } else {
/* 173 */       if (!path.endsWith("/")) {
/* 174 */         path = path + "/";
/*     */       }
/* 176 */       if (webAppMount.startsWith(path)) {
/* 177 */         int i = webAppMount.indexOf('/', path.length());
/* 178 */         if (i == -1) {
/* 179 */           result.add(webAppMount + "/");
/*     */         } else {
/* 181 */           result.add(webAppMount.substring(0, i + 1));
/*     */         }
/*     */       }
/*     */     }
/* 185 */     result.setLocked(true);
/* 186 */     return result;
/*     */   }
/*     */   
/*     */   public boolean mkdir(String path)
/*     */   {
/* 191 */     checkPath(path);
/* 192 */     if (isReadOnly()) {
/* 193 */       return false;
/*     */     }
/* 195 */     String webAppMount = getWebAppMount();
/* 196 */     if (path.startsWith(webAppMount)) {
/* 197 */       File f = file(path.substring(webAppMount.length()), false);
/* 198 */       if (f == null) {
/* 199 */         return false;
/*     */       }
/* 201 */       return f.mkdir();
/*     */     }
/* 203 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean write(String path, InputStream is, boolean overwrite)
/*     */   {
/* 209 */     checkPath(path);
/*     */     
/* 211 */     if (is == null)
/*     */     {
/* 213 */       throw new NullPointerException(sm.getString("dirResourceSet.writeNpe"));
/*     */     }
/*     */     
/* 216 */     if (isReadOnly()) {
/* 217 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 222 */     if (path.endsWith("/")) {
/* 223 */       return false;
/*     */     }
/*     */     
/* 226 */     File dest = null;
/* 227 */     String webAppMount = getWebAppMount();
/* 228 */     if (path.startsWith(webAppMount)) {
/* 229 */       dest = file(path.substring(webAppMount.length()), false);
/* 230 */       if (dest == null) {
/* 231 */         return false;
/*     */       }
/*     */     } else {
/* 234 */       return false;
/*     */     }
/*     */     
/* 237 */     if ((dest.exists()) && (!overwrite)) {
/* 238 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 242 */       if (overwrite) {
/* 243 */         Files.copy(is, dest.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */       } else {
/* 245 */         Files.copy(is, dest.toPath(), new CopyOption[0]);
/*     */       }
/*     */     } catch (IOException ioe) {
/* 248 */       return false;
/*     */     }
/*     */     
/* 251 */     return true;
/*     */   }
/*     */   
/*     */   protected void checkType(File file)
/*     */   {
/* 256 */     if (!file.isDirectory()) {
/* 257 */       throw new IllegalArgumentException(sm.getString("dirResourceSet.notDirectory", new Object[] {
/* 258 */         getBase(), File.separator, getInternalPath() }));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 265 */     super.initInternal();
/*     */     
/* 267 */     if (getWebAppMount().equals(""))
/*     */     {
/* 269 */       File mf = file("META-INF/MANIFEST.MF", true);
/* 270 */       if ((mf != null) && (mf.isFile())) {
/* 271 */         try { FileInputStream fis = new FileInputStream(mf);Throwable localThrowable3 = null;
/* 272 */           try { setManifest(new Manifest(fis));
/*     */           }
/*     */           catch (Throwable localThrowable1)
/*     */           {
/* 271 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*     */           } finally {
/* 273 */             if (fis != null) if (localThrowable3 != null) try { fis.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else fis.close();
/* 274 */           } } catch (IOException e) { log.warn(sm.getString("dirResourceSet.manifestFail", new Object[] { mf.getAbsolutePath() }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\DirResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */