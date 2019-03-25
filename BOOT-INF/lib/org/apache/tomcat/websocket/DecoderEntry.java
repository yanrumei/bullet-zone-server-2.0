/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import javax.websocket.Decoder;
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
/*    */ public class DecoderEntry
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final Class<? extends Decoder> decoderClazz;
/*    */   
/*    */   public DecoderEntry(Class<?> clazz, Class<? extends Decoder> decoderClazz)
/*    */   {
/* 28 */     this.clazz = clazz;
/* 29 */     this.decoderClazz = decoderClazz;
/*    */   }
/*    */   
/*    */   public Class<?> getClazz() {
/* 33 */     return this.clazz;
/*    */   }
/*    */   
/*    */   public Class<? extends Decoder> getDecoderClazz() {
/* 37 */     return this.decoderClazz;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\DecoderEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */