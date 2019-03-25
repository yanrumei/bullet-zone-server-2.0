/*    */ package org.springframework.boot.context.embedded.undertow;
/*    */ 
/*    */ import io.undertow.UndertowMessages;
/*    */ import io.undertow.server.handlers.resource.Resource;
/*    */ import io.undertow.server.handlers.resource.ResourceChangeListener;
/*    */ import io.undertow.server.handlers.resource.ResourceManager;
/*    */ import io.undertow.server.handlers.resource.URLResource;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
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
/*    */ class JarResourceManager
/*    */   implements ResourceManager
/*    */ {
/*    */   private final String jarPath;
/*    */   
/*    */   JarResourceManager(File jarFile)
/*    */   {
/* 40 */     this(jarFile.getAbsolutePath());
/*    */   }
/*    */   
/*    */   JarResourceManager(String jarPath) {
/* 44 */     this.jarPath = jarPath;
/*    */   }
/*    */   
/*    */   public Resource getResource(String path)
/*    */     throws IOException
/*    */   {
/* 50 */     URL url = new URL("jar:file:" + this.jarPath + "!" + (path.startsWith("/") ? path : new StringBuilder().append("/").append(path).toString()));
/* 51 */     URLResource resource = new URLResource(url, path);
/* 52 */     if (resource.getContentLength().longValue() < 0L) {
/* 53 */       return null;
/*    */     }
/* 55 */     return resource;
/*    */   }
/*    */   
/*    */   public boolean isResourceChangeListenerSupported()
/*    */   {
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   public void registerResourceChangeListener(ResourceChangeListener listener)
/*    */   {
/* 65 */     throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
/*    */   }
/*    */   
/*    */ 
/*    */   public void removeResourceChangeListener(ResourceChangeListener listener)
/*    */   {
/* 71 */     throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\JarResourceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */