/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.util.jar.JarEntry;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
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
/*    */ 
/*    */ public class JarResource
/*    */   extends AbstractSingleArchiveResource
/*    */ {
/* 30 */   private static final Log log = LogFactory.getLog(JarResource.class);
/*    */   
/*    */ 
/*    */   public JarResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry)
/*    */   {
/* 35 */     super(archiveResourceSet, webAppPath, "jar:" + baseUrl + "!/", jarEntry, baseUrl);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Log getLog()
/*    */   {
/* 41 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\JarResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */