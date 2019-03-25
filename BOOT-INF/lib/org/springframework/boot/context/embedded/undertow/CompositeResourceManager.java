/*    */ package org.springframework.boot.context.embedded.undertow;
/*    */ 
/*    */ import io.undertow.UndertowMessages;
/*    */ import io.undertow.server.handlers.resource.Resource;
/*    */ import io.undertow.server.handlers.resource.ResourceChangeListener;
/*    */ import io.undertow.server.handlers.resource.ResourceManager;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ class CompositeResourceManager
/*    */   implements ResourceManager
/*    */ {
/*    */   private final List<ResourceManager> resourceManagers;
/*    */   
/*    */   CompositeResourceManager(ResourceManager... resourceManagers)
/*    */   {
/* 38 */     this.resourceManagers = Arrays.asList(resourceManagers);
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 43 */     for (ResourceManager resourceManager : this.resourceManagers) {
/* 44 */       resourceManager.close();
/*    */     }
/*    */   }
/*    */   
/*    */   public Resource getResource(String path) throws IOException
/*    */   {
/* 50 */     for (ResourceManager resourceManager : this.resourceManagers) {
/* 51 */       Resource resource = resourceManager.getResource(path);
/* 52 */       if (resource != null) {
/* 53 */         return resource;
/*    */       }
/*    */     }
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isResourceChangeListenerSupported()
/*    */   {
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   public void registerResourceChangeListener(ResourceChangeListener listener)
/*    */   {
/* 66 */     throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
/*    */   }
/*    */   
/*    */   public void removeResourceChangeListener(ResourceChangeListener listener)
/*    */   {
/* 71 */     throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedde\\undertow\CompositeResourceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */