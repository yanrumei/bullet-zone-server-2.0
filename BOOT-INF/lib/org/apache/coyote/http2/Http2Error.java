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
/*    */ public enum Http2Error
/*    */ {
/* 21 */   NO_ERROR(0L), 
/* 22 */   PROTOCOL_ERROR(1L), 
/* 23 */   INTERNAL_ERROR(2L), 
/* 24 */   FLOW_CONTROL_ERROR(3L), 
/* 25 */   SETTINGS_TIMEOUT(4L), 
/* 26 */   STREAM_CLOSED(5L), 
/* 27 */   FRAME_SIZE_ERROR(6L), 
/* 28 */   REFUSED_STREAM(7L), 
/* 29 */   CANCEL(8L), 
/* 30 */   COMPRESSION_ERROR(9L), 
/* 31 */   CONNECT_ERROR(10L), 
/* 32 */   ENHANCE_YOUR_CALM(11L), 
/* 33 */   INADEQUATE_SECURITY(12L), 
/* 34 */   HTTP_1_1_REQUIRED(13L);
/*    */   
/*    */   private final long code;
/*    */   
/*    */   private Http2Error(long code) {
/* 39 */     this.code = code;
/*    */   }
/*    */   
/*    */   public long getCode()
/*    */   {
/* 44 */     return this.code;
/*    */   }
/*    */   
/*    */   public byte[] getCodeBytes()
/*    */   {
/* 49 */     byte[] codeByte = new byte[4];
/* 50 */     ByteUtil.setFourBytes(codeByte, 0, this.code);
/* 51 */     return codeByte;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2Error.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */