/*    */ package org.apache.tomcat.util.buf;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public class ByteBufferHolder
/*    */ {
/*    */   private final ByteBuffer buf;
/*    */   private final AtomicBoolean flipped;
/*    */   
/*    */   public ByteBufferHolder(ByteBuffer buf, boolean flipped)
/*    */   {
/* 32 */     this.buf = buf;
/* 33 */     this.flipped = new AtomicBoolean(flipped);
/*    */   }
/*    */   
/*    */   public ByteBuffer getBuf()
/*    */   {
/* 38 */     return this.buf;
/*    */   }
/*    */   
/*    */   public boolean isFlipped()
/*    */   {
/* 43 */     return this.flipped.get();
/*    */   }
/*    */   
/*    */   public boolean flip()
/*    */   {
/* 48 */     if (this.flipped.compareAndSet(false, true)) {
/* 49 */       this.buf.flip();
/* 50 */       return true;
/*    */     }
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\ByteBufferHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */