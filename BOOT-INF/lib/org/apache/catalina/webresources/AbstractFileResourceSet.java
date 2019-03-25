/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.tomcat.util.compat.JrePlatform;
/*     */ import org.apache.tomcat.util.http.RequestUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFileResourceSet
/*     */   extends AbstractResourceSet
/*     */ {
/*  30 */   protected static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*     */   private File fileBase;
/*     */   private String absoluteBase;
/*     */   private String canonicalBase;
/*  35 */   private boolean readOnly = false;
/*     */   
/*     */   protected AbstractFileResourceSet(String internalPath) {
/*  38 */     setInternalPath(internalPath);
/*     */   }
/*     */   
/*     */   protected final File getFileBase() {
/*  42 */     return this.fileBase;
/*     */   }
/*     */   
/*     */   public void setReadOnly(boolean readOnly)
/*     */   {
/*  47 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly()
/*     */   {
/*  52 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   protected final File file(String name, boolean mustExist)
/*     */   {
/*  57 */     if (name.equals("/")) {
/*  58 */       name = "";
/*     */     }
/*  60 */     File file = new File(this.fileBase, name);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  65 */     if ((name.endsWith("/")) && (file.isFile())) {
/*  66 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  71 */     if ((mustExist) && (!file.canRead())) {
/*  72 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  77 */     if (getRoot().getAllowLinking()) {
/*  78 */       return file;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  83 */     if ((JrePlatform.IS_WINDOWS) && (isInvalidWindowsFilename(name))) {
/*  84 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  88 */     String canPath = null;
/*     */     try {
/*  90 */       canPath = file.getCanonicalPath();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/*  94 */     if ((canPath == null) || (!canPath.startsWith(this.canonicalBase))) {
/*  95 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     String absPath = normalize(file.getAbsolutePath());
/* 106 */     if (this.absoluteBase.length() > absPath.length()) {
/* 107 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 113 */     absPath = absPath.substring(this.absoluteBase.length());
/* 114 */     canPath = canPath.substring(this.canonicalBase.length());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */     if (canPath.length() > 0) {
/* 128 */       canPath = normalize(canPath);
/*     */     }
/* 130 */     if (!canPath.equals(absPath)) {
/* 131 */       return null;
/*     */     }
/*     */     
/* 134 */     return file;
/*     */   }
/*     */   
/*     */   private boolean isInvalidWindowsFilename(String name)
/*     */   {
/* 139 */     int len = name.length();
/* 140 */     if (len == 0) {
/* 141 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 145 */     for (int i = 0; i < len; i++) {
/* 146 */       char c = name.charAt(i);
/* 147 */       if ((c == '"') || (c == '<') || (c == '>'))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 154 */         return true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 161 */     if (name.charAt(len - 1) == ' ') {
/* 162 */       return true;
/*     */     }
/* 164 */     return false;
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
/*     */   private String normalize(String path)
/*     */   {
/* 178 */     return RequestUtil.normalize(path, File.separatorChar == '\\');
/*     */   }
/*     */   
/*     */   public URL getBaseUrl()
/*     */   {
/*     */     try {
/* 184 */       return getFileBase().toURI().toURL();
/*     */     } catch (MalformedURLException e) {}
/* 186 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void gc() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 205 */     this.fileBase = new File(getBase(), getInternalPath());
/* 206 */     checkType(this.fileBase);
/*     */     
/* 208 */     this.absoluteBase = normalize(this.fileBase.getAbsolutePath());
/*     */     try
/*     */     {
/* 211 */       this.canonicalBase = this.fileBase.getCanonicalPath();
/*     */     } catch (IOException e) {
/* 213 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void checkType(File paramFile);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractFileResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */