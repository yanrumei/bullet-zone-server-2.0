/*    */ package org.apache.tomcat.util.net;
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
/*    */ 
/*    */ public abstract class SendfileDataBase
/*    */ {
/* 27 */   public SendfileKeepAliveState keepAliveState = SendfileKeepAliveState.NONE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String fileName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long pos;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public long length;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SendfileDataBase(String filename, long pos, long length)
/*    */   {
/* 50 */     this.fileName = filename;
/* 51 */     this.pos = pos;
/* 52 */     this.length = length;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SendfileDataBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */