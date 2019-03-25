/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.websocket.Extension;
/*    */ import javax.websocket.HandshakeResponse;
/*    */ import javax.websocket.server.HandshakeRequest;
/*    */ import javax.websocket.server.ServerEndpointConfig;
/*    */ import javax.websocket.server.ServerEndpointConfig.Configurator;
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
/*    */ public class DefaultServerEndpointConfigurator
/*    */   extends ServerEndpointConfig.Configurator
/*    */ {
/*    */   public <T> T getEndpointInstance(Class<T> clazz)
/*    */     throws InstantiationException
/*    */   {
/*    */     try
/*    */     {
/* 36 */       return (T)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*    */     } catch (InstantiationException e) {
/* 38 */       throw e;
/*    */     } catch (ReflectiveOperationException e) {
/* 40 */       InstantiationException ie = new InstantiationException();
/* 41 */       ie.initCause(e);
/* 42 */       throw ie;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getNegotiatedSubprotocol(List<String> supported, List<String> requested)
/*    */   {
/* 51 */     for (String request : requested) {
/* 52 */       if (supported.contains(request)) {
/* 53 */         return request;
/*    */       }
/*    */     }
/* 56 */     return "";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested)
/*    */   {
/* 63 */     Set<String> installedNames = new HashSet();
/* 64 */     for (Iterator localIterator = installed.iterator(); localIterator.hasNext();) { e = (Extension)localIterator.next();
/* 65 */       installedNames.add(e.getName()); }
/*    */     Extension e;
/* 67 */     Object result = new ArrayList();
/* 68 */     for (Extension request : requested) {
/* 69 */       if (installedNames.contains(request.getName())) {
/* 70 */         ((List)result).add(request);
/*    */       }
/*    */     }
/* 73 */     return (List<Extension>)result;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean checkOrigin(String originHeaderValue)
/*    */   {
/* 79 */     return true;
/*    */   }
/*    */   
/*    */   public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\DefaultServerEndpointConfigurator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */