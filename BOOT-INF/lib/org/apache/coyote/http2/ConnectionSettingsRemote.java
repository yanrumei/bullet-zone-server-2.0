/*    */ package org.apache.coyote.http2;
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
/*    */ public class ConnectionSettingsRemote
/*    */   extends ConnectionSettingsBase<ConnectionException>
/*    */ {
/*    */   public ConnectionSettingsRemote(String connectionId)
/*    */   {
/* 26 */     super(connectionId);
/*    */   }
/*    */   
/*    */   void throwException(String msg, Http2Error error)
/*    */     throws ConnectionException
/*    */   {
/* 32 */     throw new ConnectionException(msg, error);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\ConnectionSettingsRemote.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */