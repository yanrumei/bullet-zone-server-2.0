/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.websocket.Extension;
/*    */ import javax.websocket.Extension.Parameter;
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
/*    */ public class WsExtension
/*    */   implements Extension
/*    */ {
/*    */   private final String name;
/* 27 */   private final List<Extension.Parameter> parameters = new ArrayList();
/*    */   
/*    */   WsExtension(String name) {
/* 30 */     this.name = name;
/*    */   }
/*    */   
/*    */   void addParameter(Extension.Parameter parameter) {
/* 34 */     this.parameters.add(parameter);
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<Extension.Parameter> getParameters()
/*    */   {
/* 44 */     return this.parameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsExtension.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */