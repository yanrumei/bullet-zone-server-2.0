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
/*    */ public class WarResourceSet
/*    */   extends AbstractSingleArchiveResourceSet
/*    */ {
/*    */   public WarResourceSet() {}
/*    */   
/*    */   public WarResourceSet(WebResourceRoot root, String webAppMount, String base)
/*    */     throws IllegalArgumentException
/*    */   {
/* 55 */     super(root, webAppMount, base, "/");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected WebResource createArchiveResource(JarEntry jarEntry, String webAppPath, Manifest manifest)
/*    */   {
/* 62 */     return new WarResource(this, webAppPath, getBaseUrlString(), jarEntry);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\WarResourceSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */