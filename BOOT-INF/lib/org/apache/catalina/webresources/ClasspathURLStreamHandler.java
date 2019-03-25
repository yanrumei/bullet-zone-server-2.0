/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.net.URLStreamHandler;
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
/*    */ public class ClasspathURLStreamHandler
/*    */   extends URLStreamHandler
/*    */ {
/* 30 */   private static final StringManager sm = StringManager.getManager(ClasspathURLStreamHandler.class);
/*    */   
/*    */   protected URLConnection openConnection(URL u)
/*    */     throws IOException
/*    */   {
/* 35 */     String path = u.getPath();
/*    */     
/*    */ 
/* 38 */     URL classpathUrl = Thread.currentThread().getContextClassLoader().getResource(path);
/* 39 */     if (classpathUrl == null)
/*    */     {
/* 41 */       classpathUrl = ClasspathURLStreamHandler.class.getResource(path);
/*    */     }
/*    */     
/* 44 */     if (classpathUrl == null) {
/* 45 */       throw new FileNotFoundException(sm.getString("classpathUrlStreamHandler.notFound", new Object[] { u }));
/*    */     }
/*    */     
/* 48 */     return classpathUrl.openConnection();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\ClasspathURLStreamHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */