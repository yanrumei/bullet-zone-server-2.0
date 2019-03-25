/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
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
/*    */ public abstract class AbstractSingleArchiveResource
/*    */   extends AbstractArchiveResource
/*    */ {
/*    */   protected AbstractSingleArchiveResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry, String codeBaseUrl)
/*    */   {
/* 28 */     super(archiveResourceSet, webAppPath, baseUrl, jarEntry, codeBaseUrl);
/*    */   }
/*    */   
/*    */ 
/*    */   protected AbstractArchiveResource.JarInputStreamWrapper getJarInputStreamWrapper()
/*    */   {
/* 34 */     JarFile jarFile = null;
/*    */     try {
/* 36 */       jarFile = getArchiveResourceSet().openJarFile();
/*    */       
/* 38 */       JarEntry jarEntry = jarFile.getJarEntry(getResource().getName());
/* 39 */       InputStream is = jarFile.getInputStream(jarEntry);
/* 40 */       return new AbstractArchiveResource.JarInputStreamWrapper(this, jarEntry, is);
/*    */     } catch (IOException e) {
/* 42 */       if (getLog().isDebugEnabled()) {
/* 43 */         getLog().debug(sm.getString("jarResource.getInputStreamFail", new Object[] {
/* 44 */           getResource().getName(), getBaseUrl() }), e);
/*    */       }
/* 46 */       if (jarFile != null)
/* 47 */         getArchiveResourceSet().closeJarFile();
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\AbstractSingleArchiveResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */