/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.jar.Manifest;
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
/*     */ public class JarResourceRoot
/*     */   extends AbstractResource
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(JarResourceRoot.class);
/*     */   
/*     */   private final File base;
/*     */   private final String baseUrl;
/*     */   private final String name;
/*     */   
/*     */   public JarResourceRoot(WebResourceRoot root, File base, String baseUrl, String webAppPath)
/*     */   {
/*  40 */     super(root, webAppPath);
/*     */     
/*  42 */     if (!webAppPath.endsWith("/")) {
/*  43 */       throw new IllegalArgumentException(sm.getString("jarResourceRoot.invalidWebAppPath", new Object[] { webAppPath }));
/*     */     }
/*     */     
/*  46 */     this.base = base;
/*  47 */     this.baseUrl = ("jar:" + baseUrl);
/*     */     
/*     */ 
/*  50 */     String resourceName = webAppPath.substring(0, webAppPath.length() - 1);
/*  51 */     int i = resourceName.lastIndexOf('/');
/*  52 */     if (i > -1) {
/*  53 */       resourceName = resourceName.substring(i + 1);
/*     */     }
/*  55 */     this.name = resourceName;
/*     */   }
/*     */   
/*     */   public long getLastModified()
/*     */   {
/*  60 */     return this.base.lastModified();
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/*  65 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isVirtual()
/*     */   {
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDirectory()
/*     */   {
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFile()
/*     */   {
/*  80 */     return false;
/*     */   }
/*     */   
/*     */   public boolean delete()
/*     */   {
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  90 */     return this.name;
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/*  95 */     return -1L;
/*     */   }
/*     */   
/*     */   public String getCanonicalPath()
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public boolean canRead()
/*     */   {
/* 105 */     return true;
/*     */   }
/*     */   
/*     */   protected InputStream doGetInputStream()
/*     */   {
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   public byte[] getContent()
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public long getCreation()
/*     */   {
/* 120 */     return this.base.lastModified();
/*     */   }
/*     */   
/*     */   public URL getURL()
/*     */   {
/* 125 */     String url = this.baseUrl + "!/";
/*     */     try {
/* 127 */       return new URL(url);
/*     */     } catch (MalformedURLException e) {
/* 129 */       if (log.isDebugEnabled())
/* 130 */         log.debug(sm.getString("fileResource.getUrlFail", new Object[] { url }), e);
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   public URL getCodeBase()
/*     */   {
/*     */     try
/*     */     {
/* 139 */       return new URL(this.baseUrl);
/*     */     } catch (MalformedURLException e) {
/* 141 */       if (getLog().isDebugEnabled())
/* 142 */         getLog().debug(sm.getString("fileResource.getUrlFail", new Object[] { this.baseUrl }), e);
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */   
/*     */   protected Log getLog()
/*     */   {
/* 149 */     return log;
/*     */   }
/*     */   
/*     */   public Certificate[] getCertificates()
/*     */   {
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public Manifest getManifest()
/*     */   {
/* 159 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\JarResourceRoot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */