/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.util.Set;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.util.ResourceSet;
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
/*     */ public class FileResourceSet
/*     */   extends AbstractFileResourceSet
/*     */ {
/*     */   public FileResourceSet()
/*     */   {
/*  38 */     super("/");
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
/*     */   public FileResourceSet(WebResourceRoot root, String webAppMount, String base, String internalPath)
/*     */   {
/*  61 */     super(internalPath);
/*  62 */     setRoot(root);
/*  63 */     setWebAppMount(webAppMount);
/*  64 */     setBase(base);
/*     */     
/*  66 */     if (getRoot().getState().isAvailable()) {
/*     */       try {
/*  68 */         start();
/*     */       } catch (LifecycleException e) {
/*  70 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public WebResource getResource(String path)
/*     */   {
/*  78 */     checkPath(path);
/*     */     
/*  80 */     String webAppMount = getWebAppMount();
/*  81 */     WebResourceRoot root = getRoot();
/*  82 */     if (path.equals(webAppMount)) {
/*  83 */       File f = file("", true);
/*  84 */       if (f == null) {
/*  85 */         return new EmptyResource(root, path);
/*     */       }
/*  87 */       return new FileResource(root, path, f, isReadOnly(), null);
/*     */     }
/*     */     
/*  90 */     if (path.charAt(path.length() - 1) != '/') {
/*  91 */       path = path + '/';
/*     */     }
/*     */     
/*  94 */     if (webAppMount.startsWith(path)) {
/*  95 */       String name = path.substring(0, path.length() - 1);
/*  96 */       name = name.substring(name.lastIndexOf('/') + 1);
/*  97 */       if (name.length() > 0) {
/*  98 */         return new VirtualResource(root, path, name);
/*     */       }
/*     */     }
/* 101 */     return new EmptyResource(root, path);
/*     */   }
/*     */   
/*     */   public String[] list(String path)
/*     */   {
/* 106 */     checkPath(path);
/*     */     
/* 108 */     if (path.charAt(path.length() - 1) != '/') {
/* 109 */       path = path + '/';
/*     */     }
/* 111 */     String webAppMount = getWebAppMount();
/*     */     
/* 113 */     if (webAppMount.startsWith(path)) {
/* 114 */       webAppMount = webAppMount.substring(path.length());
/* 115 */       if (webAppMount.equals(getFileBase().getName())) {
/* 116 */         return new String[] { getFileBase().getName() };
/*     */       }
/*     */       
/* 119 */       int i = webAppMount.indexOf('/');
/* 120 */       if (i > 0) {
/* 121 */         return new String[] { webAppMount.substring(0, i) };
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 126 */     return EMPTY_STRING_ARRAY;
/*     */   }
/*     */   
/*     */   public Set<String> listWebAppPaths(String path)
/*     */   {
/* 131 */     checkPath(path);
/*     */     
/* 133 */     ResourceSet<String> result = new ResourceSet();
/*     */     
/* 135 */     if (path.charAt(path.length() - 1) != '/') {
/* 136 */       path = path + '/';
/*     */     }
/* 138 */     String webAppMount = getWebAppMount();
/*     */     
/* 140 */     if (webAppMount.startsWith(path)) {
/* 141 */       webAppMount = webAppMount.substring(path.length());
/* 142 */       if (webAppMount.equals(getFileBase().getName())) {
/* 143 */         result.add(path + getFileBase().getName());
/*     */       }
/*     */       else {
/* 146 */         int i = webAppMount.indexOf('/');
/* 147 */         if (i > 0) {
/* 148 */           result.add(path + webAppMount.substring(0, i + 1));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 153 */     result.setLocked(true);
/* 154 */     return result;
/*     */   }
/*     */   
/*     */   public boolean mkdir(String path)
/*     */   {
/* 159 */     checkPath(path);
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   public boolean write(String path, InputStream is, boolean overwrite)
/*     */   {
/* 165 */     checkPath(path);
/* 166 */     return false;
/*     */   }
/*     */   
/*     */   protected void checkType(File file)
/*     */   {
/* 171 */     if (!file.isFile()) {
/* 172 */       throw new IllegalArgumentException(sm.getString("fileResourceSet.notFile", new Object[] {
/* 173 */         getBase(), File.separator, getInternalPath() }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\FileResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */