/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.WebResourceSet;
/*     */ import org.apache.catalina.util.LifecycleBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmptyResourceSet
/*     */   extends LifecycleBase
/*     */   implements WebResourceSet
/*     */ {
/*  39 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   private WebResourceRoot root;
/*     */   private boolean classLoaderOnly;
/*     */   private boolean staticOnly;
/*     */   
/*     */   public EmptyResourceSet(WebResourceRoot root)
/*     */   {
/*  46 */     this.root = root;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebResource getResource(String path)
/*     */   {
/*  56 */     return new EmptyResource(this.root, path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] list(String path)
/*     */   {
/*  66 */     return EMPTY_STRING_ARRAY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> listWebAppPaths(String path)
/*     */   {
/*  76 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mkdir(String path)
/*     */   {
/*  86 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean write(String path, InputStream is, boolean overwrite)
/*     */   {
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public void setRoot(WebResourceRoot root)
/*     */   {
/* 101 */     this.root = root;
/*     */   }
/*     */   
/*     */   public boolean getClassLoaderOnly()
/*     */   {
/* 106 */     return this.classLoaderOnly;
/*     */   }
/*     */   
/*     */   public void setClassLoaderOnly(boolean classLoaderOnly)
/*     */   {
/* 111 */     this.classLoaderOnly = classLoaderOnly;
/*     */   }
/*     */   
/*     */   public boolean getStaticOnly()
/*     */   {
/* 116 */     return this.staticOnly;
/*     */   }
/*     */   
/*     */   public void setStaticOnly(boolean staticOnly)
/*     */   {
/* 121 */     this.staticOnly = staticOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URL getBaseUrl()
/*     */   {
/* 131 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadOnly(boolean readOnly) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 152 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void gc() {}
/*     */   
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */   
/*     */ 
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 167 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */   protected void stopInternal() throws LifecycleException
/*     */   {
/* 172 */     setState(LifecycleState.STOPPING);
/*     */   }
/*     */   
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\EmptyResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */