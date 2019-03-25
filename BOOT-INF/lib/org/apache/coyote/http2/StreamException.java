/*    */ package org.apache.coyote.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamException
/*    */   extends Http2Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final int streamId;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public StreamException(String msg, Http2Error error, int streamId)
/*    */   {
/* 29 */     super(msg, error);
/* 30 */     this.streamId = streamId;
/*    */   }
/*    */   
/*    */   public int getStreamId()
/*    */   {
/* 35 */     return this.streamId;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\StreamException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */