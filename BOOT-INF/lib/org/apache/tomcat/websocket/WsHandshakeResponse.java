/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.websocket.HandshakeResponse;
/*    */ import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
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
/*    */ public class WsHandshakeResponse
/*    */   implements HandshakeResponse
/*    */ {
/* 33 */   private final Map<String, List<String>> headers = new CaseInsensitiveKeyMap();
/*    */   
/*    */ 
/*    */   public WsHandshakeResponse() {}
/*    */   
/*    */ 
/*    */   public WsHandshakeResponse(Map<String, List<String>> headers)
/*    */   {
/* 41 */     for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/* 42 */       if (this.headers.containsKey(entry.getKey())) {
/* 43 */         ((List)this.headers.get(entry.getKey())).addAll((Collection)entry.getValue());
/*    */       } else {
/* 45 */         List<String> values = new ArrayList((Collection)entry.getValue());
/* 46 */         this.headers.put(entry.getKey(), values);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Map<String, List<String>> getHeaders()
/*    */   {
/* 54 */     return this.headers;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsHandshakeResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */