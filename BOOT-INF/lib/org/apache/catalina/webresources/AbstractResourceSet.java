/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.util.jar.Manifest;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.WebResourceSet;
/*     */ import org.apache.catalina.util.LifecycleBase;
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
/*     */ public abstract class AbstractResourceSet
/*     */   extends LifecycleBase
/*     */   implements WebResourceSet
/*     */ {
/*     */   private WebResourceRoot root;
/*     */   private String base;
/*  33 */   private String internalPath = "";
/*     */   
/*     */   private String webAppMount;
/*     */   
/*     */   private boolean classLoaderOnly;
/*     */   private boolean staticOnly;
/*     */   private Manifest manifest;
/*  40 */   protected static final StringManager sm = StringManager.getManager(AbstractResourceSet.class);
/*     */   
/*     */   protected final void checkPath(String path)
/*     */   {
/*  44 */     if ((path == null) || (path.length() == 0) || (path.charAt(0) != '/'))
/*     */     {
/*  46 */       throw new IllegalArgumentException(sm.getString("abstractResourceSet.checkPath", new Object[] { path }));
/*     */     }
/*     */   }
/*     */   
/*     */   public final void setRoot(WebResourceRoot root)
/*     */   {
/*  52 */     this.root = root;
/*     */   }
/*     */   
/*     */   protected final WebResourceRoot getRoot() {
/*  56 */     return this.root;
/*     */   }
/*     */   
/*     */   protected final String getInternalPath()
/*     */   {
/*  61 */     return this.internalPath;
/*     */   }
/*     */   
/*     */   public final void setInternalPath(String internalPath) {
/*  65 */     checkPath(internalPath);
/*     */     
/*  67 */     if (internalPath.equals("/")) {
/*  68 */       this.internalPath = "";
/*     */     } else {
/*  70 */       this.internalPath = internalPath;
/*     */     }
/*     */   }
/*     */   
/*     */   public final void setWebAppMount(String webAppMount) {
/*  75 */     checkPath(webAppMount);
/*     */     
/*  77 */     if (webAppMount.equals("/")) {
/*  78 */       this.webAppMount = "";
/*     */     } else {
/*  80 */       this.webAppMount = webAppMount;
/*     */     }
/*     */   }
/*     */   
/*     */   protected final String getWebAppMount() {
/*  85 */     return this.webAppMount;
/*     */   }
/*     */   
/*     */   public final void setBase(String base) {
/*  89 */     this.base = base;
/*     */   }
/*     */   
/*     */   protected final String getBase() {
/*  93 */     return this.base;
/*     */   }
/*     */   
/*     */   public boolean getClassLoaderOnly()
/*     */   {
/*  98 */     return this.classLoaderOnly;
/*     */   }
/*     */   
/*     */   public void setClassLoaderOnly(boolean classLoaderOnly)
/*     */   {
/* 103 */     this.classLoaderOnly = classLoaderOnly;
/*     */   }
/*     */   
/*     */   public boolean getStaticOnly()
/*     */   {
/* 108 */     return this.staticOnly;
/*     */   }
/*     */   
/*     */   public void setStaticOnly(boolean staticOnly)
/*     */   {
/* 113 */     this.staticOnly = staticOnly;
/*     */   }
/*     */   
/*     */   protected final void setManifest(Manifest manifest) {
/* 117 */     this.manifest = manifest;
/*     */   }
/*     */   
/*     */   protected final Manifest getManifest() {
/* 121 */     return this.manifest;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 128 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */   protected final void stopInternal() throws LifecycleException
/*     */   {
/* 133 */     setState(LifecycleState.STOPPING);
/*     */   }
/*     */   
/*     */   protected final void destroyInternal() throws LifecycleException
/*     */   {
/* 138 */     gc();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */