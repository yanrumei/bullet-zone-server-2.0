/*    */ package org.apache.catalina.webresources.war;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.security.Permission;
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
/*    */ public class WarURLConnection
/*    */   extends URLConnection
/*    */ {
/*    */   private final URLConnection wrappedJarUrlConnection;
/*    */   private boolean connected;
/*    */   
/*    */   protected WarURLConnection(URL url)
/*    */     throws IOException
/*    */   {
/* 34 */     super(url);
/* 35 */     URL innerJarUrl = UriUtil.warToJar(url);
/* 36 */     this.wrappedJarUrlConnection = innerJarUrl.openConnection();
/*    */   }
/*    */   
/*    */   public void connect()
/*    */     throws IOException
/*    */   {
/* 42 */     if (!this.connected) {
/* 43 */       this.wrappedJarUrlConnection.connect();
/* 44 */       this.connected = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public InputStream getInputStream()
/*    */     throws IOException
/*    */   {
/* 51 */     connect();
/* 52 */     return this.wrappedJarUrlConnection.getInputStream();
/*    */   }
/*    */   
/*    */   public Permission getPermission()
/*    */     throws IOException
/*    */   {
/* 58 */     return this.wrappedJarUrlConnection.getPermission();
/*    */   }
/*    */   
/*    */ 
/*    */   public long getLastModified()
/*    */   {
/* 64 */     return this.wrappedJarUrlConnection.getLastModified();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\war\WarURLConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */