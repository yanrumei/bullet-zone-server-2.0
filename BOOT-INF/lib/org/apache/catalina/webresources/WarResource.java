/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.util.jar.JarEntry;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.buf.UriUtil;
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
/*    */ public class WarResource
/*    */   extends AbstractSingleArchiveResource
/*    */ {
/* 31 */   private static final Log log = LogFactory.getLog(WarResource.class);
/*    */   
/*    */ 
/*    */   public WarResource(AbstractArchiveResourceSet archiveResourceSet, String webAppPath, String baseUrl, JarEntry jarEntry)
/*    */   {
/* 36 */     super(archiveResourceSet, webAppPath, "war:" + baseUrl + UriUtil.getWarSeparator(), jarEntry, baseUrl);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected Log getLog()
/*    */   {
/* 43 */     return log;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\WarResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */