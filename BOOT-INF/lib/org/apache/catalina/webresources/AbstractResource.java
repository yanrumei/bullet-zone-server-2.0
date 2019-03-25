/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.Date;
/*    */ import org.apache.catalina.WebResource;
/*    */ import org.apache.catalina.WebResourceRoot;
/*    */ import org.apache.catalina.util.ConcurrentDateFormat;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.res.StringManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractResource
/*    */   implements WebResource
/*    */ {
/* 30 */   protected static final StringManager sm = StringManager.getManager(AbstractResource.class);
/*    */   
/*    */   private final WebResourceRoot root;
/*    */   
/*    */   private final String webAppPath;
/* 35 */   private String mimeType = null;
/*    */   private volatile String weakETag;
/*    */   
/*    */   protected AbstractResource(WebResourceRoot root, String webAppPath)
/*    */   {
/* 40 */     this.root = root;
/* 41 */     this.webAppPath = webAppPath;
/*    */   }
/*    */   
/*    */ 
/*    */   public final WebResourceRoot getWebResourceRoot()
/*    */   {
/* 47 */     return this.root;
/*    */   }
/*    */   
/*    */ 
/*    */   public final String getWebappPath()
/*    */   {
/* 53 */     return this.webAppPath;
/*    */   }
/*    */   
/*    */ 
/*    */   public final String getLastModifiedHttp()
/*    */   {
/* 59 */     return ConcurrentDateFormat.formatRfc1123(new Date(getLastModified()));
/*    */   }
/*    */   
/*    */   public final String getETag()
/*    */   {
/* 64 */     if (this.weakETag == null) {
/* 65 */       synchronized (this) {
/* 66 */         if (this.weakETag == null) {
/* 67 */           long contentLength = getContentLength();
/* 68 */           long lastModified = getLastModified();
/* 69 */           if ((contentLength >= 0L) || (lastModified >= 0L)) {
/* 70 */             this.weakETag = ("W/\"" + contentLength + "-" + lastModified + "\"");
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 76 */     return this.weakETag;
/*    */   }
/*    */   
/*    */   public final void setMimeType(String mimeType)
/*    */   {
/* 81 */     this.mimeType = mimeType;
/*    */   }
/*    */   
/*    */ 
/*    */   public final String getMimeType()
/*    */   {
/* 87 */     return this.mimeType;
/*    */   }
/*    */   
/*    */ 
/*    */   public final InputStream getInputStream()
/*    */   {
/* 93 */     InputStream is = doGetInputStream();
/*    */     
/* 95 */     if ((is == null) || (!this.root.getTrackLockedFiles())) {
/* 96 */       return is;
/*    */     }
/*    */     
/* 99 */     return new TrackedInputStream(this.root, getName(), is);
/*    */   }
/*    */   
/*    */   protected abstract InputStream doGetInputStream();
/*    */   
/*    */   protected abstract Log getLog();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */