/*    */ package org.apache.tomcat.websocket;
/*    */ 
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
/*    */ public class WsExtensionParameter
/*    */   implements Extension.Parameter
/*    */ {
/*    */   private final String name;
/*    */   private final String value;
/*    */   
/*    */   WsExtensionParameter(String name, String value)
/*    */   {
/* 27 */     this.name = name;
/* 28 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getValue()
/*    */   {
/* 38 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsExtensionParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */