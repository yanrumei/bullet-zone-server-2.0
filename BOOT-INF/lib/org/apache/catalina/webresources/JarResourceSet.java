/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.Manifest;
/*    */ import org.apache.catalina.WebResource;
/*    */ import org.apache.catalina.WebResourceRoot;
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
/*    */ 
/*    */ 
/*    */ public class JarResourceSet
/*    */   extends AbstractSingleArchiveResourceSet
/*    */ {
/*    */   public JarResourceSet() {}
/*    */   
/*    */   public JarResourceSet(WebResourceRoot root, String webAppMount, String base, String internalPath)
/*    */     throws IllegalArgumentException
/*    */   {
/* 59 */     super(root, webAppMount, base, internalPath);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected WebResource createArchiveResource(JarEntry jarEntry, String webAppPath, Manifest manifest)
/*    */   {
/* 66 */     return new JarResource(this, webAppPath, getBaseUrlString(), jarEntry);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\JarResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */