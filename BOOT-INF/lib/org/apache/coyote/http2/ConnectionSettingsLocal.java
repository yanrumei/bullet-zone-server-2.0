/*    */ package org.apache.coyote.http2;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionSettingsLocal
/*    */   extends ConnectionSettingsBase<IllegalArgumentException>
/*    */ {
/* 35 */   private boolean sendInProgress = false;
/*    */   
/*    */   public ConnectionSettingsLocal(String connectionId)
/*    */   {
/* 39 */     super(connectionId);
/*    */   }
/*    */   
/*    */ 
/*    */   protected synchronized void set(Setting setting, Long value)
/*    */   {
/* 45 */     checkSend();
/* 46 */     if (((Long)this.current.get(setting)).longValue() == value.longValue()) {
/* 47 */       this.pending.remove(setting);
/*    */     } else {
/* 49 */       this.pending.put(setting, value);
/*    */     }
/*    */   }
/*    */   
/*    */   synchronized byte[] getSettingsFrameForPending()
/*    */   {
/* 55 */     checkSend();
/* 56 */     int payloadSize = this.pending.size() * 6;
/* 57 */     byte[] result = new byte[9 + payloadSize];
/*    */     
/* 59 */     ByteUtil.setThreeBytes(result, 0, payloadSize);
/* 60 */     result[3] = FrameType.SETTINGS.getIdByte();
/*    */     
/*    */ 
/*    */ 
/* 64 */     int pos = 9;
/* 65 */     for (Map.Entry<Setting, Long> setting : this.pending.entrySet()) {
/* 66 */       ByteUtil.setTwoBytes(result, pos, ((Setting)setting.getKey()).getId());
/* 67 */       pos += 2;
/* 68 */       ByteUtil.setFourBytes(result, pos, ((Long)setting.getValue()).longValue());
/* 69 */       pos += 4;
/*    */     }
/* 71 */     this.sendInProgress = true;
/* 72 */     return result;
/*    */   }
/*    */   
/*    */   synchronized boolean ack()
/*    */   {
/* 77 */     if (this.sendInProgress) {
/* 78 */       this.sendInProgress = false;
/* 79 */       this.current.putAll(this.pending);
/* 80 */       this.pending.clear();
/* 81 */       return true;
/*    */     }
/* 83 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   private void checkSend()
/*    */   {
/* 89 */     if (this.sendInProgress)
/*    */     {
/* 91 */       throw new IllegalStateException();
/*    */     }
/*    */   }
/*    */   
/*    */   void throwException(String msg, Http2Error error)
/*    */     throws IllegalArgumentException
/*    */   {
/* 98 */     throw new IllegalArgumentException(msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\ConnectionSettingsLocal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */