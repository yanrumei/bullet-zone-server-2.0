/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.JarInputStream;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.buf.UriUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JarWarResource
/*    */   extends AbstractArchiveResource
/*    */ {
/* 35 */   private static final Log log = LogFactory.getLog(JarWarResource.class);
/*    */   
/*    */   private final String archivePath;
/*    */   
/*    */ 
/*    */   public JarWarResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry, String archivePath)
/*    */   {
/* 42 */     super(archiveResourceSet, webAppPath, "jar:war:" + baseUrl + 
/* 43 */       UriUtil.getWarSeparator() + archivePath + "!/", jarEntry, "war:" + baseUrl + 
/* 44 */       UriUtil.getWarSeparator() + archivePath);
/* 45 */     this.archivePath = archivePath;
/*    */   }
/*    */   
/*    */   protected AbstractArchiveResource.JarInputStreamWrapper getJarInputStreamWrapper()
/*    */   {
/* 50 */     JarFile warFile = null;
/* 51 */     JarInputStream jarIs = null;
/* 52 */     JarEntry entry = null;
/*    */     try {
/* 54 */       warFile = getArchiveResourceSet().openJarFile();
/* 55 */       JarEntry jarFileInWar = warFile.getJarEntry(this.archivePath);
/* 56 */       isInWar = warFile.getInputStream(jarFileInWar);
/*    */       
/* 58 */       jarIs = new JarInputStream(isInWar);
/* 59 */       entry = jarIs.getNextJarEntry();
/* 60 */       while ((entry != null) && 
/* 61 */         (!entry.getName().equals(getResource().getName()))) {
/* 62 */         entry = jarIs.getNextJarEntry();
/*    */       }
/*    */       AbstractArchiveResource.JarInputStreamWrapper localJarInputStreamWrapper;
/* 65 */       if (entry == null) {
/* 66 */         return null;
/*    */       }
/*    */       
/* 69 */       return new AbstractArchiveResource.JarInputStreamWrapper(this, entry, jarIs);
/*    */     } catch (IOException e) { InputStream isInWar;
/* 71 */       if (log.isDebugEnabled()) {
/* 72 */         log.debug(sm.getString("jarResource.getInputStreamFail", new Object[] {
/* 73 */           getResource().getName(), getBaseUrl() }), e);
/*    */       }
/* 75 */       return null;
/*    */     } finally {
/* 77 */       if (entry == null) {
/* 78 */         if (jarIs != null) {
/*    */           try {
/* 80 */             jarIs.close();
/*    */           }
/*    */           catch (IOException localIOException4) {}
/*    */         }
/*    */         
/* 85 */         if (warFile != null) {
/* 86 */           getArchiveResourceSet().closeJarFile();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected Log getLog()
/*    */   {
/* 94 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\JarWarResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */