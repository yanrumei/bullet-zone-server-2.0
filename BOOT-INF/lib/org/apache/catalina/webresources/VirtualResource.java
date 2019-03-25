/*    */ package org.apache.catalina.webresources;
/*    */ 
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
/*    */ public class VirtualResource
/*    */   extends EmptyResource
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public VirtualResource(WebResourceRoot root, String webAppPath, String name)
/*    */   {
/* 27 */     super(root, webAppPath);
/* 28 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isVirtual()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isDirectory()
/*    */   {
/* 38 */     return true;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 43 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\VirtualResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */