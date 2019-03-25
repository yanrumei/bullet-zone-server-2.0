/*    */ package org.apache.catalina.webresources.war;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
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
/*    */ public class Handler
/*    */   extends URLStreamHandler
/*    */ {
/*    */   protected URLConnection openConnection(URL u)
/*    */     throws IOException
/*    */   {
/* 28 */     return new WarURLConnection(u);
/*    */   }
/*    */   
/*    */ 
/*    */   protected void setURL(URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query, String ref)
/*    */   {
/* 34 */     if ((path.startsWith("file:")) && (!path.startsWith("file:/")))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */       path = "file:/" + path.substring(5);
/*    */     }
/* 44 */     super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\war\Handler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */