/*    */ package org.apache.tomcat.util.scan;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.JarURLConnection;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
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
/*    */ public class UrlJar
/*    */   extends AbstractInputStreamJar
/*    */ {
/*    */   public UrlJar(URL jarFileURL)
/*    */   {
/* 31 */     super(jarFileURL);
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */   {
/* 37 */     closeStream();
/*    */   }
/*    */   
/*    */   protected NonClosingJarInputStream createJarInputStream()
/*    */     throws IOException
/*    */   {
/* 43 */     JarURLConnection jarConn = (JarURLConnection)getJarFileURL().openConnection();
/* 44 */     URL resourceURL = jarConn.getJarFileURL();
/* 45 */     URLConnection resourceConn = resourceURL.openConnection();
/* 46 */     resourceConn.setUseCaches(false);
/* 47 */     return new NonClosingJarInputStream(resourceConn.getInputStream());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\UrlJar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */