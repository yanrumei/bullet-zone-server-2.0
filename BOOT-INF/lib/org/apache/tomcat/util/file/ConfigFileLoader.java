/*    */ package org.apache.tomcat.util.file;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigFileLoader
/*    */ {
/* 36 */   private static final StringManager sm = StringManager.getManager(ConfigFileLoader.class
/* 37 */     .getPackage().getName());
/*    */   private static final File CATALINA_BASE_FILE;
/*    */   private static final URI CATALINA_BASE_URI;
/*    */   
/*    */   static
/*    */   {
/* 43 */     String catalinaBase = System.getProperty("catalina.base");
/* 44 */     if (catalinaBase != null) {
/* 45 */       CATALINA_BASE_FILE = new File(catalinaBase);
/* 46 */       CATALINA_BASE_URI = CATALINA_BASE_FILE.toURI();
/*    */     } else {
/* 48 */       CATALINA_BASE_FILE = null;
/* 49 */       CATALINA_BASE_URI = null;
/*    */     }
/*    */   }
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
/*    */   public static InputStream getInputStream(String location)
/*    */     throws IOException
/*    */   {
/* 75 */     File f = new File(location);
/* 76 */     if (!f.isAbsolute()) {
/* 77 */       f = new File(CATALINA_BASE_FILE, location);
/*    */     }
/* 79 */     if (f.isFile()) {
/* 80 */       return new FileInputStream(f);
/*    */     }
/*    */     
/*    */     URI uri;
/*    */     
/*    */     URI uri;
/*    */     
/* 87 */     if (CATALINA_BASE_URI != null) {
/* 88 */       uri = CATALINA_BASE_URI.resolve(location);
/*    */     } else {
/* 90 */       uri = URI.create(location);
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 95 */       URL url = uri.toURL();
/* 96 */       return url.openConnection().getInputStream();
/*    */     } catch (IllegalArgumentException e) {
/* 98 */       throw new IOException(sm.getString("configFileLoader.cannotObtainURL", new Object[] { location }), e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\file\ConfigFileLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */