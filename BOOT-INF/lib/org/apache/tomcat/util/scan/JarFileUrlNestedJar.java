/*    */ package org.apache.tomcat.util.scan;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.JarURLConnection;
/*    */ import java.net.URL;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarFile;
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
/*    */ public class JarFileUrlNestedJar
/*    */   extends AbstractInputStreamJar
/*    */ {
/*    */   private final JarFile warFile;
/*    */   private final JarEntry jarEntry;
/*    */   
/*    */   public JarFileUrlNestedJar(URL url)
/*    */     throws IOException
/*    */   {
/* 36 */     super(url);
/* 37 */     JarURLConnection jarConn = (JarURLConnection)url.openConnection();
/* 38 */     jarConn.setUseCaches(false);
/* 39 */     this.warFile = jarConn.getJarFile();
/*    */     
/* 41 */     String urlAsString = url.toString();
/* 42 */     int pathStart = urlAsString.indexOf("!/") + 2;
/* 43 */     String jarPath = urlAsString.substring(pathStart);
/* 44 */     this.jarEntry = this.warFile.getJarEntry(jarPath);
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */   {
/* 50 */     closeStream();
/* 51 */     if (this.warFile != null) {
/*    */       try {
/* 53 */         this.warFile.close();
/*    */       }
/*    */       catch (IOException localIOException) {}
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected NonClosingJarInputStream createJarInputStream()
/*    */     throws IOException
/*    */   {
/* 63 */     return new NonClosingJarInputStream(this.warFile.getInputStream(this.jarEntry));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\JarFileUrlNestedJar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */