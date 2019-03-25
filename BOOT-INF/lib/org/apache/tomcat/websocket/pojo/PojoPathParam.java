/*    */ package org.apache.tomcat.websocket.pojo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PojoPathParam
/*    */ {
/*    */   private final Class<?> type;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PojoPathParam(Class<?> type, String name)
/*    */   {
/* 34 */     this.type = type;
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Class<?> getType()
/*    */   {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\pojo\PojoPathParam.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */